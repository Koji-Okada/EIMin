package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.util.ArrayList;
import java.util.List;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

/**
 * IPA-Dic を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2018.08.22
 */
public class IPADicMorphologicalAnalyzer extends MorphologicalAnalyzer {

	/**
	 * 形態素解析器の本体.
	 */
	private Tokenizer tokenizer = new Tokenizer();

	/**
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		IPADicMorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();

		// String str = "これは形態素解析の例です。";
		String str = "比較的新しい用語であるクラウドはクラとウドに分割されてしまいます。";
		List<Morpheme> mor = mla.analyze(str);
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
	 * @param targetSentense	分析対象の文
	 * @return 形態素のリスト
	 */
	public final List<Morpheme> analyze(final String targetSentense) {

		List<Morpheme> morphemes = new ArrayList<Morpheme>();

		List<Token> tokens = tokenizer.tokenize(targetSentense);
		for (Token token : tokens) {
			// 形態素毎に

			String surface = token.getSurface();
			String base = token.getBaseForm();

			String strPoq = token.getPartOfSpeechLevel1();
			int poq;
			if (strPoq.equals("名詞")) {
				poq = PartOfSpeech.NORN;
			} else if (strPoq.equals("動詞")) {
				poq = PartOfSpeech.VERB;
			} else if (strPoq.equals("形容詞")) {
				poq = PartOfSpeech.ADJ;
			} else if (strPoq.equals("連体詞")) {
				poq = PartOfSpeech.PREN;
			} else if (strPoq.equals("副詞")) {
				poq = PartOfSpeech.ADV;
			} else {
				poq = PartOfSpeech.OTHERS;
			}

//			System.out.println(strPoq + ":" + surface + ":" + base);

			Morpheme m = new Morpheme(surface, base, poq);
			morphemes.add(m);
		}
		return morphemes;
	}
}
