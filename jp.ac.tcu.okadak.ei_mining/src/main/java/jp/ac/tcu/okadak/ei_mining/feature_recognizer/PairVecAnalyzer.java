package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import org.apache.commons.math3.distribution.FDistribution;

/**
 * ベクトルデータの一対比較を行う.
 *
 * @author K.Okada
 * @version 2018.08.16
 */
public class PairVecAnalyzer {

	/**
	 * 直前の計算結果の有意性(p値).
	 */
	private Double pValue = null;

	/**
	 * 直前の計算結果の有意性(p値)を返す.
	 *
	 * @return 直前の計算結果の有意性(p値)
	 */
	public final Double getPValue() {
		return this.pValue;
	}

	/**
	 * 欠損値も考慮して 2つの COS類似度(相関係数)を求める.
	 *
	 * @param data1
	 *            データ系列1
	 * @param data2
	 *            データ系列2
	 * @param s
	 *            対象範囲(始点)
	 * @param e
	 *            対象範囲(終点)
	 * @return COS類似度(相関係数) (1.0：完全一致、0.0：無関係、-1.0：完全逆向き)
	 */
	public final Double cosSimularity(final Double[] data1,
			final Double[] data2, final int s, final int e) {

		// System.out.println(s + "->" + e);
		final int minDegreeOfFreedom = 3; // 最小自由度

		// 有効要素数のカウントと平均値算出処理を行う
		double sum1 = 0.0e0d;
		double sum2 = 0.0e0d;
		int c = 0; // カウンター
		for (int i = s; i <= e; i++) {
			if ((null != data1[i]) && (null != data2[i])) {
				// 対データの両方が欠損値でない場合

				sum1 += data1[i];
				sum2 += data2[i];
				c++;
			}
		}
		double avg1 = sum1 / (double)c;
		double avg2 = sum2 / (double)c;

		// COS類似度(相関係数)を算出する
		double dst1 = 0.0e0d;
		double dst2 = 0.0e0d;
		double prod = 0.0e0d;
		for (int i = s; i <= e; i++) {
			if ((null != data1[i]) && (null != data2[i])) {
				// 対データの両方が欠損値でない場合

				double d1 = data1[i] - avg1;
				double d2 = data2[i] - avg2;

				dst1 += d1 * d1;
				dst2 += d2 * d2;
				prod += d1 * d2;
			}
		}
		if (c < minDegreeOfFreedom) {
			// 最小自由度未満の場合
			this.pValue = null;		// p値もリセットする
			return null;
		}

		double cor = prod / (Math.sqrt(dst1 * dst2));

		// 相関係数 (COS類似度と等価) の p値を算出する
		double vSSTR = cor * cor;
		double vSSE = 1.0e0d - vSSTR;
		double dFTR = 1.0e0d; // 2群(r==2): r-1
		double dFE = (double) (c - 2); // 2群(r==2): n-r
		double vMSTR = vSSTR / dFTR;
		double vMSE = vSSE / dFE;
		double vF = vMSTR / vMSE;
		FDistribution fD = new FDistribution(dFTR, dFE);
		this.pValue = 1.0e0d - fD.cumulativeProbability(vF);

		return (Double) cor;
	}
}
