package jp.ac.tcu.okadak.ei_mining.financial_data_analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.FDBDataLoader;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;
import jp.ac.tcu.okadak.ei_mining.feature_recognizer.PairVecAnalyzer;

/**
 * 財務データ分析器.
 *
 * @author K.Okada
 * @version 2018.08.21
 */
public final class FinancialDataAnalyzer {

	/**
	 * パブリックコンストラクタをオーバーライド.
	 */
	private FinancialDataAnalyzer() {
		return;
	}

	/**
	 * 財務データを分析する.
	 *
	 * @param args
	 *            ダミー引数
	 */
	public static void main(final String[] args) {

		System.out.println("Start analyzing financial data ...");

		FinancialDataAnalyzer fda = new FinancialDataAnalyzer();
		String outputPath = fda.analyze();

		// 抽出結果を集約する
		// 未実装
		FeatureAggregator fa = new FeatureAggregator();
		fa.aggregate(outputPath);

		System.out.println("... Fin");
		return;
	}

	/**
	 * 財務データを分析する.
	 *
	 * @return 出力先のファイルパス
	 */
	String analyze() {

		// 財務データを読込む
		FDBDataLoader loader = new FDBDataLoader();
		EPIDataManager<Double> epiDM = loader.load();

		// 出力先ファイルパスを読込む.
		String outputPath = loader.getOutputPath();

		// それぞれの要素数を求める
		List<String> enterprises = epiDM.getEnterprises();
		int numEnterprises = enterprises.size();
		List<String> periods = epiDM.listPeriods();
		int numPeriods = periods.size();
		List<String> indicators = epiDM.getIndicators();
		int numIndicators = indicators.size();

		try {
			// 出力先を用意する
			File outFile = new File(outputPath + "/" + "trendFeatures.txt");
			FileWriter fw = new FileWriter(outFile);
			BufferedWriter bw = new BufferedWriter(fw);
			String res;

			for (String ind : indicators) {
				// 全ての財務指標に対して

				// 欠損値も扱えるようにラッパーを用いている.
				// 後処理の容易性から時系列軸を基本軸としている.
				Double[][] data = new Double[numEnterprises][numPeriods];

				// データを設定する.
				for (int e = 0; e < numEnterprises; e++) {
					for (int p = 0; p < numPeriods; p++) {
						String nameE = enterprises.get(e);
						String nameP = periods.get(p);
						data[e][p] = epiDM.getValue(nameE, nameP, ind);
					}
				}

				// == ここから、財務データ時系列特徴パターン毎に抽出する
				res = specificFeatures(data, enterprises, periods, ind);

				if ("" != res) {
					bw.write(res);
				}

			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputPath;
	}

	/**
	 * 財務時系列データの特異部を抽出する.
	 *
	 * @param data
	 *            値[企業][期間].矩形を想定.
	 * @param enterprises
	 *            企業のリスト
	 * @param periods
	 *            期間のリスト
	 * @param ind
	 *            指標
	 * @return 抽出結果
	 */
	private String specificFeatures(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		String res = "";

		final int minWindowSize = 3; // 窓の最小幅

		String resShape = "";
		String resAmp = "";
		String resLvl = "";

		int numP = periods.size();

		for (int ws = 1; ws < numP - minWindowSize; ws++) {
			// 期間窓の始点を選択する
			for (int we = ws + minWindowSize; we < numP; we++) {
				// 期間窓の終点を選択する

				// 財務時系列データの波形特異部位を抽出する
				resShape = specificShape(data, enterprises, periods, ind, ws,
						we);
				res = res + resShape;

				// 財務時系列データの振幅特異部位を抽出する
				resAmp = specificAmp(data, enterprises, periods, ind, ws, we);
				res = res + resAmp;

				// 財務時系列データの水準特異部位を抽出する
				resLvl = specificLvl(data, enterprises, periods, ind, ws, we);
				res = res + resLvl;
			}
		}
		return res;
	}

	/**
	 * 財務時系列データの波形特異部位を抽出する.
	 *
	 * @param data
	 *            値[企業][期間].矩形を想定.
	 * @param enterprises
	 *            企業のリスト
	 * @param periods
	 *            期間のリスト
	 * @param ind
	 *            指標
	 * @param ws
	 *            期間窓の始点
	 * @param we
	 *            期間窓の終点
	 * @return 抽出結果
	 */
	private String specificShape(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind, final int ws, final int we) {

		final double thSpecificity = 0.50e0d; // 特異度の判定閾値
		final double thPValue = 0.10e0d; // 特異度の判定閾値 (p値)

		String res = "";

		int numE = enterprises.size();
		int numP = periods.size();

		for (int e = 0; e < numE; e++) {
			// 企業を選択する

			double sum = 0.0e0d; // コサイン類似度の合計
			double maxP = 0.0e0d; // p値の最大値
			int c = 0;

			for (int i = 0; i < numE; i++) {
				// 比較企業を選択する
				if (e != i) {
					// 2社比較

					// COS類似度と p値 を算出する.
					PairVecAnalyzer pva = new PairVecAnalyzer();
					Double sim = pva.calcCOSSimularity(data[e], data[i], ws,
							we);
					Double pValue = pva.getPValue();

					// 特異度算出用の値を求める
					if (null != sim) {
						sum += sim;
						if (pValue > maxP) {
							maxP = pValue;
						}
						c++;
					}
				}
			}

			String st = "";
			if (0 != c) {
				// 有効な値がある場合

				// 類似度から特異度を算出する
				double x = sum / (double) c;
				double specificity = (1.0e0d - x) / 2.0e0d;

				// 竹松の経営情報学会発表時には正規化処理を行っているが
				// ２社比較も可能とするため、正規化処理は行わず、
				// 特異度そのもので判定を行う形に変更

				if ((thSpecificity <= specificity) && (thPValue >= maxP)) {
					// 特徴パターンと認められる場合

					st = "波形特異" + ",";
					st = st + ind + ",";
					st = st + enterprises.get(e) + ",";
					st = st + periods.get(ws) + ",";
					st = st + periods.get(we) + ",";
					st = st + specificity + ",";
					st = st + periods.get(0) + ",";
					st = st + periods.get(numP - 1);
					for (int p = 0; p < numP; p++) {
						if (null != data[e][p]) {
							st = st + "," + data[e][p];
						} else {
							st = st + ",";
						}
					}
//					System.out.println(st);
					st = st + "\r\n";
				}
			}
			res = res + st;
		}
		return res;
	}

	/**
	 * 財務時系列データの振幅特異部位を抽出する.
	 *
	 * @param data
	 *            値[企業][期間].矩形を想定.
	 * @param enterprises
	 *            企業のリスト
	 * @param periods
	 *            期間のリスト
	 * @param ind
	 *            指標
	 * @param ws
	 *            期間窓の始点
	 * @param we
	 *            期間窓の終点
	 * @return 抽出結果
	 */
	private String specificAmp(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind, final int ws, final int we) {

		final double thUSpecificity = Math.tanh(Math.log((double) 10)); // 特異度の判定閾値(上方)
		final double thLSpecificity = Math.tanh(Math.log((double) 0.1)); // 特異度の判定閾値(下方)
		final double thPValue = 0.05e0d; // 特異度の判定閾値 (p値)

		String res = "";

		int numE = enterprises.size();
		int numP = periods.size();

		for (int e = 0; e < numE; e++) {
			// 企業を選択する

			double sum = 0.0e0d; // 分散類似度の合計
			double maxP = 0.0e0d; // p値の最大値
			int c = 0;

			for (int i = 0; i < numE; i++) {
				// 比較企業を選択する
				if (e != i) {
					// 2社比較

					// 分散比と p値 を算出する.
					PairVecAnalyzer pva = new PairVecAnalyzer();
					Double sim = pva.calcVarRatio(data[e], data[i], ws, we);
					Double pValue = pva.getPValue();

					// 特異度算出用の値を求める
					if (null != sim) {
						sum += Math.log(sim); // 自然対数変換
						if (pValue > maxP) {
							maxP = pValue;
						}
						c++;
					}
				}
			}

			String st = "";
			if (0 != c) {
				// 有効な値がある場合

				// 類似度から特異度を算出する
				double x = sum / (double) c; // 対数値の平均 (すなわち幾何平均)
				double specificity = Math.tanh(x); // -1.0 ～ 0.0 ～ 1.0 に変換

				// 竹松の経営情報学会発表時には正規化処理を行っているが
				// ２社比較も可能とするため、正規化処理は行わず、
				// 特異度そのもので判定を行う形に変更

				if (((thUSpecificity <= specificity) && (thPValue >= maxP))
						|| ((thLSpecificity >= specificity)
								&& (thPValue >= maxP))) {
					// 特徴パターンと認められる場合

					if (thUSpecificity <= specificity) {
						st = "振幅特異(大)" + ",";
					} else {
						st = "振幅特異(小)" + ",";
					}
					st = st + ind + ",";
					st = st + enterprises.get(e) + ",";
					st = st + periods.get(ws) + ",";
					st = st + periods.get(we) + ",";
					st = st + specificity + ",";
					st = st + periods.get(0) + ",";
					st = st + periods.get(numP - 1);
					for (int p = 0; p < numP; p++) {
						if (null != data[e][p]) {
							st = st + "," + data[e][p];
						} else {
							st = st + ",";
						}
					}
//					System.out.println(st);
					st = st + "\r\n";
				}
			}
			res = res + st;
		}
		return res;
	}

	/**
	 * 財務時系列データの水準特異部位を抽出する.
	 *
	 * @param data
	 *            値[企業][期間].矩形を想定.
	 * @param enterprises
	 *            企業のリスト
	 * @param periods
	 *            期間のリスト
	 * @param ind
	 *            指標
	 * @param ws
	 *            期間窓の始点
	 * @param we
	 *            期間窓の終点
	 * @return 抽出結果
	 */
	private String specificLvl(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind, final int ws, final int we) {

		final double thUSpecificity = Math.tanh(Math.log((double) 10)); // 特異度の判定閾値(上方)
		final double thLSpecificity = Math.tanh(Math.log((double) 0.1)); // 特異度の判定閾値(下方)
		final double thPValue = 0.05e0d; // 特異度の判定閾値 (p値)

		String res = "";

		int numE = enterprises.size();
		int numP = periods.size();

		for (int e = 0; e < numE; e++) {
			// 企業を選択する

			double sum = 0.0e0d; // 平均値の類似度の合計
			double maxP = 0.0e0d; // p値の最大値
			int c = 0;

			for (int i = 0; i < numE; i++) {
				// 比較企業を選択する
				if (e != i) {
					// 2社比較

					// 平均値比と p値 を算出する.
					PairVecAnalyzer pva = new PairVecAnalyzer();
					Double sim = pva.calcAvgRatio(data[e], data[i], ws, we);
					Double pValue = pva.getPValue();

					// 特異度算出用の値を求める
					if (null != sim) {
						sum += Math.log(sim); // 自然対数変換
						if (pValue > maxP) {
							maxP = pValue;
						}
						c++;
					}
				}
			}

			String st = "";
			if (0 != c) {
				// 有効な値がある場合

				// 類似度から特異度を算出する
				double x = sum / (double) c; // 対数値の平均 (すなわち幾何平均)
				double specificity = Math.tanh(x); // -1.0 ～ 0.0 ～ 1.0 に変換

				// 竹松の経営情報学会発表時には正規化処理を行っているが
				// ２社比較も可能とするため、正規化処理は行わず、
				// 特異度そのもので判定を行う形に変更

				if (((thUSpecificity <= specificity) && (thPValue >= maxP))
						|| ((thLSpecificity >= specificity)
								&& (thPValue >= maxP))) {
					// 特徴パターンと認められる場合

					if (thUSpecificity <= specificity) {
						st = "水準特異(大)" + ",";
					} else {
						st = "水準特異(小)" + ",";
					}
					st = st + ind + ",";
					st = st + enterprises.get(e) + ",";
					st = st + periods.get(ws) + ",";
					st = st + periods.get(we) + ",";
					st = st + specificity + ",";
					st = st + periods.get(0) + ",";
					st = st + periods.get(numP - 1);
					for (int p = 0; p < numP; p++) {
						if (null != data[e][p]) {
							st = st + "," + data[e][p];
						} else {
							st = st + ",";
						}
					}
//					System.out.println(st);
					st = st + "\r\n";
				}
			}
			res = res + st;
		}
		return res;
	}
}
