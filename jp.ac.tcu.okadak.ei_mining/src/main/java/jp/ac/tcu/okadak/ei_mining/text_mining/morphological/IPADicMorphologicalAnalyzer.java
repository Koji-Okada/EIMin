package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.util.ArrayList;
import java.util.List;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

/**
 * 2020.10.24 記
 * C:\\user\....\.dl4j-kuromoji の配下をローカルに置かないとエラーとなる．
 */


/**
 * IPA-Dic を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2020.01.06
 */
public class IPADicMorphologicalAnalyzer extends MorphologicalAnalyzer {

	/**
	 * 形態素解析器の本体.
	 */
	private Tokenizer tokenizer;

	/**
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		IPADicMorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();

		// String str = "これは形態素解析の例です。";
		String str = "比較的新しい用語である「クラウド」は、クラとウドに分割されてしまいます。";
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

		Tokenizer tokenizer = new Tokenizer();

		List<Token> tokens = tokenizer.tokenize(targetSentense);
		for (Token token : tokens) {
			// 形態素毎に

			String surface = token.getSurface();
			String base = token.getBaseForm();

			String strPos = token.getPartOfSpeechLevel1();
			int pos;
			if (strPos.equals("名詞")) {
				pos = PartOfSpeech.NORN;
			} else if (strPos.equals("動詞")) {
				pos = PartOfSpeech.VERB;
			} else if (strPos.equals("形容詞")) {
				pos = PartOfSpeech.ADJ;
			} else if (strPos.equals("連体詞")) {
				pos = PartOfSpeech.PREN;
			} else if (strPos.equals("副詞")) {
				pos = PartOfSpeech.ADV;
			} else if (strPos.equals("記号")) {
				pos = PartOfSpeech.SYMB;
			} else {
				pos = PartOfSpeech.OTHERS;
			}

			Morpheme m = new Morpheme(surface, base, pos);
			morphemes.add(m);
		}
		return;
	}
}
