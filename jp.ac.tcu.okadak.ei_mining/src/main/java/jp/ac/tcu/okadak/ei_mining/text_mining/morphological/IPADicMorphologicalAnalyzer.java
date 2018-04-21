package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.util.Arrays;
import java.util.List;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

/**
 * IPA-Dic を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2018.04.21
 */
public class IPADicMorphologicalAnalyzer {

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

//		String str = "これは形態素解析の例です。";
		String str = "クラウドはクラとウドに分割されてしまいます。";
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

		List<Token> tokens = tokenizer.tokenize(targetDoc);

		// 結果を出力してみる
		for (Token token : tokens) {

			builder.append(token.getSurface());
			builder.append(" ");

			 System.out.println(
			 "==================================================");
			 System.out.println("allFeatures : " + token.getAllFeatures());
			 System.out
			 .println("partOfSpeech : " + token.getPartOfSpeechLevel1());
			 System.out.println("position : " + token.getPosition());
			 System.out.println("reading : " + token.getReading());
			 System.out.println("surfaceFrom : " + token.getSurface());
			 System.out.println("allFeaturesArray : "
			 + Arrays.asList(token.getAllFeaturesArray()));
			 System.out.println("辞書にある言葉? : " + token.isKnown());
			 System.out.println("未知語? : " + token.isKnown());
			 System.out.println("ユーザ定義? : " + token.isUser());
		}

		return builder.toString();
	}
}
