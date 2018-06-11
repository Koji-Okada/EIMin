package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.JapaneseTokenizer;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;

/**
 * NeoLogD を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2018.06.04
 *
 */
public class NeologdMorphologicalAnalyzer extends MorphlogicalAnalyzer {

	/**
	 * 形態素解析器の本体.
	 */
	private JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
			JapaneseTokenizer.Mode.NORMAL);

	/**
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		NeologdMorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

		// String str = "これは形態素解析の例です。";
		String str = "大きなクラウドはクラとウドにすぐに分割されません。";
		String res = mla.analyzeSentense(str, 15);

		System.out.println(str);
		System.out.println(res);
	}

	/**
	 * 文に対して形態素解析を行う.
	 *
	 * @param targetSentense
	 *            分析対象の文
	 * @param mode
	 *            -1 : 分かち書き 正値: 出力品詞指定
	 * @return 解析結果
	 */
	final String analyzeSentense(final String targetSentense, final int mode) {

		StringBuilder builder = new StringBuilder();

		try {
			// JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
			// JapaneseTokenizer.DEFAULT_MODE);

			tokenizer.setReader(new StringReader(targetSentense));
			CharTermAttribute ct = tokenizer
					.addAttribute(CharTermAttribute.class);
			PartOfSpeechAttribute pa = tokenizer
					.addAttribute(PartOfSpeechAttribute.class);
			BaseFormAttribute ba = tokenizer
					.addAttribute(BaseFormAttribute.class);

			tokenizer.reset();
			while (tokenizer.incrementToken()) {

				if (PartOfSpeech.ALL == mode) {
					builder.append(ct.toString());
					builder.append(" ");
					continue;
				}

				// System.out.println(ct.toString() + "\t\t" +
				// pa.getPartOfSpeech());

				if (PartOfSpeech.NORN == (mode & PartOfSpeech.NORN)) {
					// 名詞を出力
					if (pa.getPartOfSpeech().contains("名詞-")) {

						String str = ct.toString();
						if (!StringUtils.isNumeric(str)) {
							builder.append(str);
							builder.append(" ");
						}
					}
				}
				if (PartOfSpeech.VERB == (mode & PartOfSpeech.VERB)) {
					// 動詞を出力
					if (pa.getPartOfSpeech().contains("動詞-")) {
						if (null != ba.getBaseForm()) {
							builder.append(ba.getBaseForm());
						} else {
							builder.append(ct.toString());
						}

						builder.append(" ");
					}
				}
				if (PartOfSpeech.ADJ == (mode & PartOfSpeech.ADJ)) {
					// 形容詞を出力
					if (pa.getPartOfSpeech().contains("形容詞-")
							|| pa.getPartOfSpeech().contains("連体詞")) {
						builder.append(ct.toString());
						builder.append(" ");
					}
				}
				if (PartOfSpeech.ADV == (mode & PartOfSpeech.ADV)) {
					// 副詞を出力
					if (pa.getPartOfSpeech().contains("副詞-")) {
						builder.append(ct.toString());
						builder.append(" ");
					}
				}

			}
			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
