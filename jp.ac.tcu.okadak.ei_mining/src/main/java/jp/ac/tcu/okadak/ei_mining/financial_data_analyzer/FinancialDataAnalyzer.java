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
 * @version 2018.08.16
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
		fda.analyze();

		System.out.println("... Fin");
		return;
	}

	/**
	 * 財務データを分析する.
	 */
	private void analyze() {

		// 財務データを読込む
		FDBDataLoader loader = new FDBDataLoader();
		EPIDataManager<Double> epiDM = loader.load();

		// 出力先ファイルパスを読込む.
		String outputPath = loader.getOutputPath();

		// それぞれの要素数を求める
		List<String> enterprises = epiDM.getEnterprises();
		int numEnterprises = enterprises.size();
		List<String> periods = epiDM.listPeriod();
		int numPeriods = periods.size();
		List<String> indicators = epiDM.getIndicators();
		int numIndicators = indicators.size();

		System.out.println(
				numEnterprises + " : " + numPeriods + " : " + numIndicators);

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

				// ※未実装

				// 財務時系列データの波形特異部位を抽出する

				res = specificShape(data, enterprises, periods, ind);
				if ("" != res) {
					bw.write(res);
				}

			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 財務時系列データの波形特異部を抽出する.
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
	private String specificShape(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		String res = "";

		final int minWindowSize = 3; // 窓の最小幅

		int numE = enterprises.size();
		int numP = periods.size();

		for (int ws = 1; ws < numP - minWindowSize; ws++) {
			// 期間窓の始点を選択する
			for (int we = ws + minWindowSize; we < numP; we++) {
				// 期間窓の終点を選択する

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

					// 類似度から特異度を算出する
					double diff = sum / (double) c;

//					System.out.println("diff = " + diff + "\t p =" + maxP);

					String st = "";
					if ((diff < 0.0e0d) && (maxP < 0.10)) {
						// 特徴パターンと認められる場合

						st = "波形独自性" + ",";
						st  = st + ind + ",";
						st = st + diff + ",";
						st = st + enterprises.get(e) + ",";
						st = st + periods.get(ws) + ",";
						st = st + periods.get(we) + ",";

						double d;
						for (int p = 0; p < numP; p++) {
							if (null == data[e][p]) {
								d = 0.0e0d;
							} else {
								d = data[e][p];
							}
							st = st + d + ",";
						}
						System.out.println(st);
					}
					res = res + st;
				}
			}
		}
		return res;
	}
}
