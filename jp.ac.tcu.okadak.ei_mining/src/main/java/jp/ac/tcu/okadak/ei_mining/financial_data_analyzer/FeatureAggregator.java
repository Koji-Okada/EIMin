package jp.ac.tcu.okadak.ei_mining.financial_data_analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * 財務データ特徴の集約器.
 *
 * @author K.Okada
 * @version 2018.08.21
 */
public class FeatureAggregator {

	/**
	 * 多重検出される特徴パターンを集約する.
	 *
	 * @param filePath 処理対象のファイルパス
	 */
	final void aggregate(final String filePath) {

		System.out.println("==== Aggregate");

		EPIDataManager<Integer> epiDM = load(filePath);
		record(filePath, epiDM);

		return;
	}

	/**
	 * 特徴パターンデータを読み込む.
	 *
	 * @param filePath 処理対象のファイルパス
	 * @return 読込んだデータ
	 */
	final EPIDataManager<Integer> load(final String filePath) {

		EPIDataManager<Integer> epiDM = new EPIDataManager<Integer>();

		try {
			File inputFile = new File(filePath + "/" + "trendFeatures.txt");
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while (null != (line = br.readLine())) {
				// 1レコード(1行)ずつ処理する

				CSVTokenizer ct = new CSVTokenizer(line);

				String feature = ct.nextToken(); // 特徴
				String indicator = ct.nextToken(); // 指標
				String enterprise = ct.nextToken(); // 企業
				String sp = ct.nextToken(); // 開始点
				String ep = ct.nextToken(); // 終了点

				String feInd = feature + "," + indicator;	// 特徴＋指標

				int s = Integer.parseInt(sp);
				int e = Integer.parseInt(ep);

				for (int i = s; i <= e; i++) {
					// 開始点から終了点まで
					epiDM.addData(enterprise, String.valueOf(i), feInd,
							new Integer(1));
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return epiDM;
	}


	/**
	 * 集約した結果を記録する.
	 *
	 * @param filePath	処理対象のファイルパス
	 * @param epiDM	処理対象データ
	 */
	final void record(final String filePath, final EPIDataManager<Integer> epiDM) {

		List<String> enterprises = epiDM.getEnterprises();
		List<String> periods = epiDM.listPeriods();
		List<String> indicators = epiDM.getIndicators();

		try {
			File outputFile = new File(filePath + "/" + "aggregatedTrendFeatures.txt");
			FileWriter fw = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(fw);

			for (String i : indicators) {
				for (String e : enterprises) {

					// 合計、特徴開始点、特徴終了点を求める
					int sum = 0;
					String sp = null;
					String ep = null;
					for (String p : periods) {
						Integer v = epiDM.getValue(e, p, i);
						if (null != v) {
							sum += v;
							ep = p;
							if (null == sp) {
								sp = p;
							}
						}
					}
					String str = i + "," + e + ",";
					str = str + sp + "," + ep + ",";
					str = str + periods.get(0) + "," + periods.get(periods.size()-1);

					for (String p : periods) {
						Integer v = epiDM.getValue(e, p, i);
						if (null != v) {
							double val = (double) v / (double) sum;
							str = str + "," + val;
						} else {
							str = str + "," + 0.0e0d;
						}
					}
					if (0 < sum) {
						bw.write(str);
						bw.newLine();
						System.out.println(str);
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
}
