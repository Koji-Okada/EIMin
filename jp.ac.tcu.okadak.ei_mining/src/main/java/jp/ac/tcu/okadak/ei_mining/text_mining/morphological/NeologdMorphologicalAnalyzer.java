package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.JapaneseTokenizer;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;

/**
 * NeoLogD を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2018.04.21
 *
 */
public class NeologdMorphologicalAnalyzer {

	/**
	 * 形態素解析器の本体.
	 */
	private JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
			JapaneseTokenizer.Mode.NORMAL);
	// private JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
	// JapaneseTokenizer.DEFAULT_MODE);

	/**
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		NeologdMorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

		// String str = "これは形態素解析の例です。";
		String str = "クラウドはクラとウドに分割されません。";
		String res = mla.analyze(str);

		System.out.println(str);
		System.out.println(res);
	}

	/**
	 * 形態素解析を行う.
	 *
	 * @param targetDoc
	 *            分析対象の文
	 * @return 解析結果
	 */
	final String analyze(final String targetDoc) {

		StringBuilder builder = new StringBuilder();

		try {
			// JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
			// JapaneseTokenizer.DEFAULT_MODE);

			tokenizer.setReader(new StringReader(targetDoc));
			CharTermAttribute ct = tokenizer
					.addAttribute(CharTermAttribute.class);
			PartOfSpeechAttribute pa = tokenizer
					.addAttribute(PartOfSpeechAttribute.class);

			tokenizer.reset();
			while (tokenizer.incrementToken()) {
				builder.append(ct.toString());
				builder.append(" ");

				System.out.println(ct.toString() + "\t" + pa.getPartOfSpeech());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
