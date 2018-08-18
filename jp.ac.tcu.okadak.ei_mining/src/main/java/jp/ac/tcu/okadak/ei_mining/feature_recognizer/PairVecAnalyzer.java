package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import org.apache.commons.math3.distribution.FDistribution;

/**
 * ベクトルデータの一対比較を行う.
 *
 * @author K.Okada
 * @version 2018.08.18
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
	 * 欠損値も考慮して 2つの系列データのCOS類似度(相関係数)を求める.
	 *
	 * @param data0
	 *            時系列データ(基準)
	 * @param data1
	 *            時系列データ(対象)
	 * @param s
	 *            期間開点
	 * @param e
	 *            期間終点
	 * @return COS類似度(相関係数)
	 * 			(1.0：完全一致、0.0：無関係、-1.0：完全逆向き)
	 */
	public final Double calcCOSSimularity(final Double[] data0,
			final Double[] data1, final int s, final int e) {

		// System.out.println(s + "->" + e);
		final int minDegreeOfFreedom = 3; // 最小自由度

		// 有効要素数のカウントと平均値算出処理を行う
		double sum0 = 0.0e0d;
		double sum1 = 0.0e0d;
		int c = 0; // カウンター
		for (int i = s; i <= e; i++) {
			if ((null != data0[i]) && (null != data1[i])) {
				// 対データの両方が欠損値でない場合

				sum0 += data0[i];
				sum1 += data1[i];
				c++;
			}
		}
		double avg0 = sum0 / (double) c;
		double avg1 = sum1 / (double) c;

		// COS類似度(相関係数)を算出する
		double dst0 = 0.0e0d;
		double dst1 = 0.0e0d;
		double prod = 0.0e0d;
		for (int i = s; i <= e; i++) {
			if ((null != data0[i]) && (null != data1[i])) {
				// 対データの両方が欠損値でない場合

				double d0 = data0[i] - avg0;
				double d1 = data1[i] - avg1;

				dst0 += d0 * d0;
				dst1 += d1 * d1;
				prod += d0 * d1;
			}
		}
		if (c < minDegreeOfFreedom) {
			// 最小自由度未満の場合
			this.pValue = null; // p値もリセットする
			return null;
		}

		double cor = prod / (Math.sqrt(dst0 * dst1));

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

	/**
	 * 欠損値も考慮して 2つの系列データの分散比を算出する.
	 *
	 * @param data0
	 *            時系列データ(基準)
	 * @param data1
	 *            時系列データ(対象)
	 * @param s
	 *            期間開点
	 * @param e
	 *            期間終点
	 * @return 振幅比
	 * 			∞ : 当該時系列が他に比べ小さい
	 * 			 1 : 同一
	 * 			 0 : 当該時系列が他に比べ大きい
	 */
	public final Double calcAmpRatio(final Double[] data0, final Double[] data1,
			final int s, final int e) {

		final int minDegreeOfFreedom = 3; // 最小自由度

		// 有効要素数をカウントする
		double sum0 = 0.0e0d;
		double sum1 = 0.0e0d;
		int c0 = 0;
		int c1 = 0;
		for (int i = s; i <= e; i++) {
			if (null != data0[i]) {
				sum0 += data0[i];
				c0++;

			}
			if (null != data1[i]) {
				sum1 += data1[i];
				c1++;
			}
		}

		if ((c0 < minDegreeOfFreedom) || (c1 < minDegreeOfFreedom)) {
			// どちらかが最小自由度未満の場合
			this.pValue = null; // p値もリセットする
			return null;
		}

		// 平均値を算出する
		double avg0 = sum0 / (double) c0;
		double avg1 = sum1 / (double) c1;

		// 振幅比を算出する
		double sumS0 = 0.0e0d;
		double sumS1 = 0.0e0d;
		for (int i = s; i <= e; i++) {
			if (null != data0[i]) {
				double d0 = data0[i] - avg0;
				sumS0 += d0 * d0;
			}
			if (null != data1[i]) {
				double d1 = data1[i] - avg1;
				sumS1 += d1 * d1;
			}
		}

		double rVar = (sumS0 / (double) c0) / (sumS1 / (double) c1);

		// 振幅比の p値を算出する
		double dF0 = (double) (c0 - 1);
		double dF1 = (double) (c0 - 1);
		double variance0 = sumS0 / dF0;
		double variance1 = sumS1 / dF1;
		double pV;

		if (variance0 >= variance1) {
			FDistribution fD = new FDistribution(dF0, dF1);
			pV = 1.0e0d - fD.cumulativeProbability(variance0 / variance1);
		} else {
			FDistribution fD = new FDistribution(dF1, dF0);
			pV = 1.0e0d - fD.cumulativeProbability(variance1 / variance0);

		}
		this.pValue = pV; // 片側検定値

		return (Double) rVar;
	}

	/**
	 * 欠損値も考慮して 2つの系列データの平均値の比を算出する.
	 *
	 * 竹松の経営情報学会発表時には、比ではなく差で判定しているが
	 * 指標の特質に依存する差よりも比を用いた方が良い
	 *
	 *
	 * @param data0
	 *            時系列データ(基準)
	 * @param data1
	 *            時系列データ(対象)
	 * @param s
	 *            期間開点
	 * @param e
	 *            期間終点
	 * @return 平均値の比
	 * 			∞ : 当該時系列が他に比べ小さい
	 * 			 1 : 同一
	 * 			 0 : 当該時系列が他に比べ大きい
	 */
	public final Double calcAvgRatio(final Double[] data0, final Double[] data1,
			final int s, final int e) {

		final int minDegreeOfFreedom = 3; // 最小自由度

		// 有効要素数をカウントする
		double sum0 = 0.0e0d;
		double sum1 = 0.0e0d;
		int c0 = 0;
		int c1 = 0;
		for (int i = s; i <= e; i++) {
			if (null != data0[i]) {
				sum0 += data0[i];
				c0++;

			}
			if (null != data1[i]) {
				sum1 += data1[i];
				c1++;
			}
		}

		if ((c0 < minDegreeOfFreedom) || (c1 < minDegreeOfFreedom)) {
			// どちらかが最小自由度未満の場合
			this.pValue = null; // p値もリセットする
			return null;
		}

		// 系列毎の平均値を算出する
		double avg0 = sum0 / (double) c0;
		double avg1 = sum1 / (double) c1;

		// 総平均値を算出する
		double avg = (avg0 * (double) c0 + avg1 * (double) c1) / (double) (c0
				+ c1);

		// 偏差平方和の算出
		double vSSTR = (avg0 - avg) * (avg0 - avg) * c0 + (avg1 - avg) * (avg1
				- avg) * c1;

		// 系列ごとの偏差平方和を算出する
		double sumS0 = 0.0e0d;
		double sumS1 = 0.0e0d;
		for (int i = s; i <= e; i++) {
			if (null != data0[i]) {
				double d0 = data0[i] - avg0;
				sumS0 += d0 * d0;
			}
			if (null != data1[i]) {
				double d1 = data1[i] - avg1;
				sumS1 += d1 * d1;
			}
		}
		double vSSE = sumS0 + sumS1;

		// 自由度の算出
		double dFTR = 1.0e0; // 2群比較なので常に 2-1=1
		double dFE = (double) (c0 + c1 - 2);

		// 標準偏差の算出
		double vMSTR = vSSTR / dFTR;
		double vMSE = vSSE / dFE;

		// 対象時系列データから見た基準時系列データの水準の差
		double diff = avg0 / avg1;

		// 平均値の差の p値を算出する
		double pV;
		FDistribution fD = new FDistribution(dFTR, dFE);
		pV = 1.0e0d - fD.cumulativeProbability(vMSTR / vMSE);
		this.pValue = pV; // 片側検定値

		return (Double) diff;
	}
}
