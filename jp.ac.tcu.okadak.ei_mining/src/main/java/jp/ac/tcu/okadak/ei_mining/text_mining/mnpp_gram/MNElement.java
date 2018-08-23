package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;

/**
 * 形態素N-Gram要素.
 *
 * @author K.Okada
 * @version 2018.08.23
 */
public class MNElement {

	/**
	 * 形態素 N-Gram要素 (形態素 n個分の配列).
	 */
	private Morpheme[] mors;

	/**
	 * 形態素N-Gramの登録数.
	 */
	private int num;

	/**
	 * コンストラクタ.
	 * @param n	グラム数.
	 */
	MNElement(final int n) {
		super();
		this.mors = new Morpheme[n];
		this.num = 1;
		return;
	}


	/**
	 * 形態素を追加して、形態素N-Gramを構成する.
	 *
	 * @param mor 追加される形態素
	 * @param loc 追加する位置
	 */
	final void appendMorpheme(final Morpheme mor, final int loc) {
		this.mors[loc] = mor;

		return;
	}

	/**
	 * 形態素N-Gram の登録数をカウントアップする.
	 */
	final void countUp() {
		this.num++;

		System.out.println("Count up! " + num);
		return;
	}
}
