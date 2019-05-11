package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.JapaneseTokenizer;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;

/**
 * NeoLogD を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2019.05.09
 *
 */
public class NeologdMorphologicalAnalyzer extends MorphologicalAnalyzer {

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
		String str = "比較的新しい用語である「クラウド」は、クラとウドに分割されません。";
		List<Morpheme> mor = new ArrayList<Morpheme>();
		mla.analyze(mor, str);
		String res = mla.getSurface(mor, " ", PartOfSpeech.ALL);
		String res2 = mla.getSurface(mor, " ", 31);
		String res3 = mla.getBase(mor, " ", 31);

		System.out.println(str);
		System.out.println(res);
		System.out.println(res2);
		System.out.println(res3);
	}

	/**
	 * 文に対して形態素解析を行う.
	 * オーバーライドしている
	 *
	 * @param morphemes 形態素を追加するリスト
	 * @param targetSentense 分析対象の文
	 */
	public final void analyze(final List<Morpheme> morphemes,
			final String targetSentense) {

		//		List<Morpheme> morphemes = new ArrayList<Morpheme>();

		try {
			// トークナイザを設定する
			tokenizer.setReader(new StringReader(targetSentense));
			CharTermAttribute ct = tokenizer.addAttribute(
					CharTermAttribute.class);
			PartOfSpeechAttribute pa = tokenizer.addAttribute(
					PartOfSpeechAttribute.class);
			BaseFormAttribute ba = tokenizer.addAttribute(
					BaseFormAttribute.class);

			tokenizer.reset();
			while (tokenizer.incrementToken()) {
				// 形態素毎に

				String surface = ct.toString();
				String base = ba.getBaseForm();
				if (null == base) { // IPADICの場合に揃える
					base = surface;
				}

				String strPos = pa.getPartOfSpeech();
				int pos;
				if (strPos.contains("名詞-")) {
					pos = PartOfSpeech.NORN;
				} else if (strPos.contains("動詞-")) {
					pos = PartOfSpeech.VERB;
				} else if (strPos.contains("形容詞-")) {
					pos = PartOfSpeech.ADJ;
				} else if (strPos.contains("連体詞")) {
					pos = PartOfSpeech.PREN;
				} else if (strPos.contains("副詞-")) {
					pos = PartOfSpeech.ADV;
				} else if (strPos.contains("記号-")) {
					pos = PartOfSpeech.SYMB;
				} else {
					pos = PartOfSpeech.OTHERS;
				}

//				System.out.println(strPos + ":" + surface + ":" + base);

				Morpheme m = new Morpheme(surface, base, pos);

				//				System.out.println("!:" + surface + ":" + base + ":" + pos);
				morphemes.add(m);
			}
			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
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
			CharTermAttribute ct = tokenizer.addAttribute(
					CharTermAttribute.class);
			PartOfSpeechAttribute pa = tokenizer.addAttribute(
					PartOfSpeechAttribute.class);
			BaseFormAttribute ba = tokenizer.addAttribute(
					BaseFormAttribute.class);

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
						builder.append(ct.toString());
						builder.append(" ");
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
					if (pa.getPartOfSpeech().contains("形容詞-") || pa
							.getPartOfSpeech().contains("連体詞")) {
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
