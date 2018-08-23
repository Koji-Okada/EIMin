package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

/**
 * 形態素
 *
 * @author K.Okada
 * @version 2018.08.22
 */
public class Morpheme {

	/**
	 * コンストラクタ.
	 *
	 * @param strSurface	出現文字列
	 * @param strBase		基底文字列
	 * @param poq			品詞
	 */
	public Morpheme(final String strSurface, final String strBase,
			final int poq) {
		super();

		this.surface = strSurface;
		this.base = strBase;
		this.partOfSpeech = poq;

		return;
	}

	/**
	 * 出現文字列.
	 */
	private String surface;

	/**
	 * 出現文字列を返す.
	 *
	 * @return	出現文字列
	 */
	public final String getSurface() {
		return this.surface;
	}

	/**
	 * 基底文字列.
	 */
	private String base;

	/**
	 * 基底文字列を返す.
	 *
	 * @return	基底文字列
	 */
	public final String getBase() {
		return this.base;
	}

	/**
	 * 品詞.
	 */
	private int partOfSpeech;

	/**
	 * 品詞を返す.
	 *
	 * @return	品詞
	 */
	public final int getPartOfSpeech() {
		return this.partOfSpeech;
	}


	/**
	 * 識別子として使用可能な文字列を返す.
	 *
	 * @return	識別子文字列
	 */
	public final String getIdStr() {
		String idStr;

		idStr = String.valueOf(this.partOfSpeech);
		idStr = idStr + ":" + this.surface;
		idStr = idStr + ":" + this.base;

		return idStr;
	}
}
