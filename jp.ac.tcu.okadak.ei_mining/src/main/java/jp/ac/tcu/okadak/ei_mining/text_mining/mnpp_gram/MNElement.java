package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import org.apache.commons.math3.distribution.PoissonDistribution;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;

/**
 * 形態素N-Gram要素.
 *
 * @author K.Okada
 * @version 2019.05.07
 */
public class MNElement {

	/**
	 * 識別文字列の形態素間セパレータ文字列.
	 */
	private final String separator = ",";

	/**
	 * 形態素N-Gram要素 (形態素 n個分の配列).
	 */
	private Morpheme[] mors;

	/**
	 * グラム数を返す.
	 *
	 * @return グラム数
	 */
	final int getNumOfGram() {
		return this.mors.length;
	}

	/**
	 * 形態素N-Gram要素配列を返す.
	 *
	 * @return 形態素N-Gram要素配列
	 */
	final Morpheme[] getMoroheme() {
		return this.mors;
	}

	// ========================================================================
	/**
	 * 形態素N-Gramの出現数.
	 */
	private int num;

	/**
	 * 形態素N-Gramの出現数を返す.
	 *
	 * @return 形態素N-Gramの出現数
	 */
	final int getNum() {
		return this.num;
	}

	/**
	 * 前方調整数.
	 */
	private int fwdAdjust;

	/**
	 * 後方調整数.
	 */
	private int bwdAdjust;

	/**
	 * 重要度.
	 */
	private double score;

	/**
	 * 重要度の値を返す.
	 *
	 * @return 重要度の値
	 */
	final double getScore() {
		return this.score;
	}

	/**
	 * 強制的にスコアを置き換える.
	 *
	 * @param sc	新スコア
	 */
	final void replaceScore(final double sc) {
		this.score = sc;

		return;
	}

	/**
	 * 削除フラグ.
	 */
	private boolean dropFlag;

	/**
	 * 削除フラグを立てる.
	 */
	final void drop() {
		this.dropFlag = true;
	}

	/**
	 * 削除フラグを降ろす.
	 */
	final void cancelDrop() {
		this.dropFlag = false;
	}


	/**
	 * 削除されたか否かを返す.
	 *
	 * @return 削除されたか否か
	 */
	final boolean isDrop() {
		return this.dropFlag;
	}

	// ========================================================================
	/**
	 * コンストラクタ.
	 * @param n	グラム数.
	 */
	MNElement(final int n) {
		super();
		this.mors = new Morpheme[n];
		this.num = 1;
		this.fwdAdjust = 0;
		this.bwdAdjust = 0;
		this.dropFlag = false;
		this.score = 0.0e0d;
		return;
	}

	/**
	 * 形態素を追加して、形態素N-Gram要素を構成する.
	 *
	 * @param mor 追加される形態素
	 * @param loc 追加する位置
	 */
	final void appendMorpheme(final Morpheme mor, final int loc) {
		this.mors[loc] = mor;
		return;
	}

	// ========================================================================
	/**
	 * 形態素N-Gram の登録数をカウントアップする.
	 */
	final void countUp() {
		this.num++;
		return;
	}

	// ========================================================================
	/**
	 * 前方調整数を更新する.
	 *
	 * @param n	上位階層の形態素N-Gram要素の出現数
	 */
	final void adjustForword(final int n) {
		this.fwdAdjust += n * n;
		return;
	}

	/**
	 * 後方調整数を更新する.
	 *
	 * @param n	上位階層の形態素N-Gram要素の出現数
	 */
	final void adjustBackword(final int n) {
		this.bwdAdjust += n * n;
		return;
	}

	// ========================================================================
	/**
	 * 重要度指標値を算出する.
	 *
	 * @param poi	ポアソン分布
	 * @return 重要度指標値
	 */
	final double calcScore(final PoissonDistribution poi) {

		int ind2; // 独立性指標の二乗値
		ind2 = this.num * this.num - Math.max(this.fwdAdjust, this.bwdAdjust);

		int max = this.num * this.num - this.num;
		double stdInd; // 正規化独立性指標
		stdInd = Math.sqrt((double) ind2 / (double) max);

		int adjFreq; // 補正出現頻度
		adjFreq = (int) (Math.ceil((double) this.num * stdInd)) - 1;

		double sc = poi.cumulativeProbability(adjFreq); // 重要度指標値

		this.score = sc;

		return this.score;
	}

	// ========================================================================
	/**
	 * 識別子文字列を求める.
	 *
	 * @return 識別子文字列
	 */
	final String getIdStr() {

		String str = "";
		for (int i = 0; i < mors.length; i++) {
			str = str + mors[i].getIdStr() + this.separator;
		}
		return str;
	}

	/**
	 * 前方識別子文字列を求める.
	 *
	 * @return 前方識別子文字列
	 */
	final String getForwardIdStr() {

		String str = "";
		for (int i = 0; i < mors.length - 1; i++) {
			str = str + mors[i].getIdStr() + this.separator;
		}
		return str;
	}

	/**
	 * 後方識別子文字列を求める.
	 *
	 * @return 後方識別子文字列
	 */
	final String getBackwardIdStr() {

		String str = "";
		for (int i = 1; i < mors.length; i++) {
			str = str + mors[i].getIdStr() + this.separator;
		}
		return str;
	}

	/**
	 * 出現文字列を求める.
	 *
	 * @return 出現文字列
	 */
	final String getSurface() {
		String str = "";
		for (int i = 0; i < mors.length; i++) {
			str = str + mors[i].getSurface();
		}
		return str;
	}
}
