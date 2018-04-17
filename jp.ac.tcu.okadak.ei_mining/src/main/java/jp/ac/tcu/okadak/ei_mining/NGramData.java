package jp.ac.tcu.okadak.ei_mining;

/**
 *
 * N-Gram のデータ.
 *
 * @author	K.Okada
 * @version	2016.08.18
 *
 */
public class NGramData implements Comparable<NGramData> {

	/**
	 * 出現回数.
	 */
	private int num;

	/**
	 * 文字列長.
	 */
	private int length;

	/**
	 * 前方調整数.
	 */
	private int fwdAdjust;

	/**
	 * 後方調整数.
	 */
	private int bwdAdjust;

	/**
	 * 独立性指標.
	 */
	private int independency;

	/**
	 * 優先順位評点.
	 */
	private double score;

	/**
	 * 前方端点フラグ.
	 */
	private boolean fwdEdge;

	/**
	 * 後方端点フラグ.
	 */
	private boolean bwdEdge;

	/**
	 *
	 * N-Gram のデータを生成する.
	 *
	 * @author	K.Okada
	 * @version	2015.12.28
	 *
	 * @param len			文字長
	 * @param fwdEd			前方端点
	 * @param bwdEd			後方端点
	 */
	public NGramData(final int len, final boolean fwdEd, final boolean bwdEd) {
		super();
		this.num = 1;
		this.fwdAdjust = 0;
		this.bwdAdjust = 0;
		this.length = len;
		this.fwdEdge = fwdEd;
		this.bwdEdge = bwdEd;
	}

	/**
	 *
	 * 出現回数を返す.
	 *
	 * @author	K.Okada
	 * @version	2015.12.27
	 *
	 * @return				出現回数
	 */
	final int getNum() {
		return this.num;
	}

	/**
	 *
	 * 優先順位評点を返す.
	 *
	 * @author	K.Okada
	 * @version	2016.01.04
	 *
	 * @return				優先順位評点
	 */
	final double getScore() {
		return this.score;
	}

	/**
	 *
	 * 優先順位評点を算出する.
	 *
	 * @author	K.Okada
	 * @version	2016.08.25
	 *
	 * @param dLen			文章長
	 * @param numChrTyp		文字種数
	 */
	final void calcScore(final StatisticsValues sValue) {

		this.independency = this.num * this.num - Math.max(this.fwdAdjust,
				this.bwdAdjust);

		if (!isDrop()) {
			// 削除される文字列は評価しない

			double num = (double) this.num;
			double num2 = num * num;
			double indp = this.independency * num2 / (num2 - num);

			// 優先順位評点を算出する
			int adjNum = (int) StrictMath.ceil(StrictMath.sqrt(indp)) - 1;

			this.score = sValue.getProbability(this.length, adjNum);
		}

		return;
	}

	/**
	 *
	 * 出現頻度を増加させる.
	 *
	 * @author	K.Okada
	 * @version	2016.01.11
	 *
	 * @return				増加後の出現回数
	 */
	final int increase() {
		return ++this.num;
	}

	/**
	 *
	 * 前方調整数を更新する.
	 *
	 * @author	K.Okada
	 * @version	2016.08.18
	 *
	 * @param n				上位エントリーの出現頻度
	 */
	final void fwdAdjust(final int n) {
		this.fwdAdjust += n * n;

		return;
	}

	/**
	 *
	 * 後方調整数を更新する.
	 *
	 * @author	K.Okada
	 * @version	2016.08.18
	 *
	 * @param n				上位エントリーの出現頻度
	 */
	final void bwdAdjust(final int n) {
		this.bwdAdjust += n * n;

		return;
	}

	/**
	 *
	 * 整理統合の結果として削除するか否かを返す.
	 *
	 * @author	K.Okada
	 * @version	2015.12.28
	 *
	 * @return				要削除フラグ
	 */
	final boolean isDrop() {
		return (0 == this.independency) || (0 == (this.fwdAdjust
				+ this.bwdAdjust)) || this.fwdEdge || this.bwdEdge;
	}

	/**
	 *
	 * ソート時に、他の要素との比較を行う.
	 *
	 * @author	K.Okada
	 * @version	2015.12.27
	 *
	 * @param cmpData		比較するデータ
	 * @return				comparableインタフェースに準拠
	 */
	public final int compareTo(final NGramData cmpData) {
		int res;
		if (this.score > cmpData.getScore()) {
			res = 1;
		} else if (this.score < cmpData.getScore()) {
			res = -1;
		} else {
			res = 0;
		}
		return res;
	}
}
