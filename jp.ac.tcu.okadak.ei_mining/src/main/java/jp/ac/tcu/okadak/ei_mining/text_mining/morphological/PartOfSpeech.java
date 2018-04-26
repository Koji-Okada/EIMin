package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

/**
 * 形態素解析の品詞を示す定数.
 *
 * @author K.Okada
 * @version 2018.04.23
 */
public final class PartOfSpeech {

	/**
	 * コンストラクタを無効化.
	 */
	private PartOfSpeech() {
		super();
	}

	/**
	 * 全指定.
	 */
	static final int ALL = -1;


	/**
	 * 名詞.
	 */
	static final int NORN = 1;

	/**
	 * 動詞.
	 */
	static final int VERB = 2;

	/**
	 * 形容詞.
	 */
	static final int ADJ = 4;

	/**
	 * 形容詞.
	 */
	static final int ADV = 8;
}
