package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.util.List;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

/**
 * IPA-Dic を用いた形態素解析器.
 *
 * @author K.Okada
 * @version 2018.04.26
 */
public class IPADicMorphologicalAnalyzer extends MorphlogicalAnalyzer {

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
		String str = "大きなクラウドはクラとウドにすぐに分割されてしまいます。";
		String res = mla.analyzeSentense(str, PartOfSpeech.ALL);
		String res2 = mla.analyzeSentense(str, 15);

		System.out.println(str);
		System.out.println(res);
		System.out.println(res2);
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

		List<Token> tokens = tokenizer.tokenize(targetSentense);

		// 結果を出力してみる
		for (Token token : tokens) {

			if (PartOfSpeech.ALL == mode) {
				builder.append(token.getSurface());
				builder.append(" ");
				continue;
			}
			if (PartOfSpeech.NORN == (mode & PartOfSpeech.NORN)) {
				// 名詞を出力
				if (token.getPartOfSpeechLevel1().equals("名詞")) {
					builder.append(token.getBaseForm());
					builder.append(" ");
				}
			}
			if (PartOfSpeech.VERB == (mode & PartOfSpeech.VERB)) {
				// 動詞を出力
				if (token.getPartOfSpeechLevel1().equals("動詞")) {
					builder.append(token.getBaseForm());
					builder.append(" ");
				}
			}
			if (PartOfSpeech.ADJ == (mode & PartOfSpeech.ADJ)) {
				// 形容詞を出力
				if (token.getPartOfSpeechLevel1().equals("形容詞")
						|| token.getPartOfSpeechLevel1().equals("連体詞")) {
					builder.append(token.getBaseForm());
					builder.append(" ");
				}
			}
			if (PartOfSpeech.ADV == (mode & PartOfSpeech.ADV)) {
				// 副詞を出力
				if (token.getPartOfSpeechLevel1().equals("副詞")) {
					builder.append(token.getBaseForm());
					builder.append(" ");
				}
			}
		}

		return builder.toString();
	}
}
