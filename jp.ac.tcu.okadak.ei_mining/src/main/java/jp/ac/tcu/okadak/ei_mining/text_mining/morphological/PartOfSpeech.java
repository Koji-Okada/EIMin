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
	public static final int ALL = -1;


	/**
	 * 名詞.
	 */
	public static final int NORN = 1;

	/**
	 * 動詞.
	 */
	public static final int VERB = 2;

	/**
	 * 形容詞.
	 */
	public static final int ADJ = 4;

	/**
	 * 連体詞.
	 */
	public static final int PREN = 8;


	/**
	 * 副詞.
	 */
	public static final int ADV = 16;

	/**
	 * その他.
	 */
	public static final int OTHERS = 128;
}
