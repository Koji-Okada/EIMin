package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.inference.OneWayAnova;

/**
 * 群間比較を行う.
 *
 *
 * @author K.Okada
 * @version 2018.08.09
 */
public class InterGroupsAnalyzer {

	/**
	 * 欠損値を含めるか否か.
	 */
	private boolean withNullValue = false;

	/**
	 * 欠損値を用いる場合の代替値.
	 */
	private double alternativeValue = 0.0e0d;

	/**
	 *
	 * @param wnv
	 *            欠損値を含めるか否か
	 */
	public InterGroupsAnalyzer(final boolean wnv) {
		super();
		this.withNullValue = wnv;
		return;
	}

	/**
	 * 分散分析により平均値の比較する. ただし二群比較に限定.
	 *
	 * @param data
	 *            分析する元データ
	 * @param clsIDs
	 *            データの分類
	 * @return 平均値の大きい側：1-p
	 *         平均値の小さい側：-(1-p)
	 */
	public final double compareGropus(final Double[][] data,
			final int[][] clsIDs) {

		// プレ処理として群毎の要素数をカウントする
		int cntrCls1 = 0;
		int cntrCls2 = 0;
		for (int i = 0; i < clsIDs.length; i++) {
			for (int j = 0; j < clsIDs[i].length; j++) {
				if (1 == clsIDs[i][j]) {
					if ((null != data[i][j]) || (this.withNullValue)) {
						cntrCls1++;
					}
				} else if (2 == clsIDs[i][j]) {
					if ((null != data[i][j]) || (this.withNullValue)) {
						cntrCls2++;
					}
				}
			}
		}

		// 分散分析用の配列を作成する
		double[] cls1 = new double[cntrCls1];
		double[] cls2 = new double[cntrCls2];
		List<double[]> allCls = new ArrayList<double[]>();
		allCls.add(cls1);
		allCls.add(cls2);

		// 分散分析用の配列に値を設定する
		cntrCls1 = 0;
		cntrCls2 = 0;
		for (int i = 0; i < clsIDs.length; i++) {
			for (int j = 0; j < clsIDs[i].length; j++) {
				if (1 == clsIDs[i][j]) {
					if (null != data[i][j]) {
						cls1[cntrCls1++] = data[i][j];
					} else if (this.withNullValue) {
						cls1[cntrCls1++] = this.alternativeValue;
					}
				} else if (2 == clsIDs[i][j]) {
					if (null != data[i][j]) {
						cls2[cntrCls2++] = data[i][j];
					} else if (this.withNullValue) {
						cls2[cntrCls2++] = this.alternativeValue;
					}
				}
			}
		}

		// 分散分析を行い p値を求める
		OneWayAnova anova = new OneWayAnova();
		double p = anova.anovaPValue(allCls);

		// それぞれの群の平均値を求める
		double v;
		double sum1 = 0.0e0d;
		for (int i = 0; i < cls1.length; i++) {
			sum1 += cls1[i];
		}
		double sum2 = 0.0e0d;
		for (int i = 0; i < cls2.length; i++) {
			sum2 += cls2[i];
		}

		// 返り値を算出する
		if (sum1 > sum2) {
			v = (1.0e0d - p);
		} else {
			v = (p - 1.0e0d);
		}

		return v;
	}
}
