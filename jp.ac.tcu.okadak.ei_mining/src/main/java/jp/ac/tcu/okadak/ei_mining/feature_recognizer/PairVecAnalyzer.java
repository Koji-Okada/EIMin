package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import org.apache.commons.math3.distribution.FDistribution;

/**
 * ベクトルデータの一対比較を行う.
 *
 * @author K.Okada
 * @version 2018.08.15
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
	public Double getPValue() {
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
	 * @return 相関係数
	 */
	public Double CosSimularity(Double[] data1, Double[] data2, int s, int e) {

//		System.out.println(s + "->" + e);
		final int minDegreeOfFreedom = 3;		// 最小自由度

		int c = 0; // カウンター
		double dst1 = 0.0e0d;
		double dst2 = 0.0e0d;
		double prod = 0.0e0d;
		for (int i = s; i <= e; i++) {
			if ((null != data1[i]) && (null != data2[i])) {
				// 対データの両方が欠損値でない場合

				dst1 += data1[i] * data1[i];
				dst2 += data2[i] * data2[i];
				prod += data1[i] * data2[i];
				c++;
			}
		}
		if (c < minDegreeOfFreedom) {
			// 最小自由度未満の場合
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

		return (Double)cor;
	}
}
