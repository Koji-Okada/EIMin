package jp.ac.tcu.okadak.ei_mining.text_mining.stratgic_words_extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;
import jp.ac.tcu.okadak.ei_mining.feature_recognizer.InterGroupsAnalyzer;

/**
 * 戦略ワード抽出器.
 *
 * @author K.Okada
 * @version 2019.01.18
 */
public final class StrategicWordsExtractor {

	/**
	 * EPIデータマネジャー.
	 */
	private EPIDataManager<Double> epiDM = new EPIDataManager<Double>();

	/**
	 * パブリックコンストラクタをオーバーライド.
	 */
	private StrategicWordsExtractor() {
		return;
	}

	/**
	 * 戦略ワードを抽出する.
	 *
	 * @param args
	 *            ダミー引数
	 */
	public static void main(final String[] args) {

		System.out.println("Start extracting strategic words ...");

		StrategicWordsExtractor swe = new StrategicWordsExtractor();
		swe.extract();

		System.out.println("... Fin");
		return;
	}

	/**
	 * 戦略ワードを抽出する.
	 */
	private void extract() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/StrategicWordsExtractor.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			outputPath = br.readLine();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// 候補ワードを読込む
		loadWords(targetPath);

		// それぞれの要素数を求める
		List<String> enterprises = epiDM.getEnterprises();
		int numEnterprises = enterprises.size();
		List<String> periods = epiDM.listPeriods();
		int numPeriods = periods.size();
		List<String> indicators = epiDM.getIndicators();
		int numIndicators = indicators.size();

		System.out.println(numEnterprises + " : " + numPeriods + " : "
				+ numIndicators);

		try {
			// 出力先を用意する
			File outFile = new File(outputPath + "/" + "strategicWords.txt");
			FileWriter fw = new FileWriter(outFile);
			BufferedWriter bw = new BufferedWriter(fw);
			String res;

			for (String ind : indicators) {
				// 全てのワードに対して

				// 時系列財務データ分析との互換性の観点から、
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

				// == ここから、戦略ワード出現パターン毎に抽出する

				// 特定企業型戦略ワードを抽出する

//				res = specificEnterprise(data, enterprises, periods, ind);
//				if ("" != res) {
//					bw.write(res);
//				}

				// 特定期間型戦略ワードを抽出する
//				res = specificPeriod(data, enterprises, periods, ind);
//				if ("" != res) {
//					bw.write(res);
//				}

				// 業界動向型戦略ワードを抽出する
				res = industrialTrend(data, enterprises, periods, ind);
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

	// =================================================================
	/**
	 * 特定企業型戦略ワードを抽出する.
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
	private String specificEnterprise(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

//		final double threshold = 0.999999e0d; // 閾値 (2019.01.17)
		final double threshold = 0.95e0d; // 閾値

		int numE = enterprises.size();
		int numP = periods.size();

		int[][] clsMask = new int[numE][numP];
		InterGroupsAnalyzer iga = new InterGroupsAnalyzer(true);

		String res = "";

		for (int se = 0; se < numE; se++) {
			// 特定企業を選択する

			// 特定企業の場合：群1、特定企業でない場合：群2 に設定する.
			for (int e = 0; e < numE; e++) {
				for (int p = 0; p < numP; p++) {
					if (e == se) {
						// 群1の条件を満たす場合
						clsMask[e][p] = 1;
					} else {
						clsMask[e][p] = 2;
					}
				}
			}

			// 群1と群2の違いを分散分析する
			double v = iga.compareGropus(data, clsMask);
			if (threshold <= v) {
				// 有意差がある場合

				// 出現範囲を算出する
				int first = -1;
				int last = -1;
				for (int p = 0; p < numP; p++) {
					if (null != data[se][p]) {
						if (-1 == first) {
							first = p;
						}
						last = p;
					}
				}

				// 出力形式に纏める
				String st = "特定企業型" + ",";
				st = st + "\"" + ind + "\",";
				st = st + enterprises.get(se) + ",";
				st = st + periods.get(first) + ",";
				st = st + periods.get(last) + ",";
				st = st + v + ",";
				st = st + periods.get(0) + ",";
				st = st + periods.get(numP - 1);
				for (int p = 0; p < numP; p++) {
					if (null != data[se][p]) {
						st = st + "," + data[se][p];
					} else {
						st = st + ",";
					}
				}
				System.out.println(st);
				res = res + st + "\r\n";
			}
		}
		return res;
	}

	// =================================================================
	/**
	 * 特定期間型戦略ワードを抽出する.
	 *
	 * 窓の幅に制限を加えることで抽出精度を高めている.
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
	private String specificPeriod(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		final int minWindowSize = 1; // 窓の最小幅
		final int maxWindowSize = 4; // 窓の最大幅
//		final double threshold = 0.999999e0d; // 閾値 (2019.01.17)
		final double threshold = 0.95e0d; // 閾値

		String res = "";

		// 1社しかない場合は処理しない
		if (1 >= countEnterpriseWithWord(data, enterprises, periods)) {
			return res;
		}

		int numE = enterprises.size();
		int numP = periods.size();

		int[][] clsMask = new int[numE][numP];
		InterGroupsAnalyzer iga = new InterGroupsAnalyzer(true);

		double vMax = 0.0e0d;
		int wsMax = 0;
		int weMax = numP - 1;
		boolean found = false;

		for (int wSize = minWindowSize; wSize <= maxWindowSize; wSize++) {
			// 期間窓の幅を選択する
			for (int ws = 1; ws < numP - wSize - 1; ws++) {
				// 期間窓の始点を選択する
				int we = ws + wSize;

				// System.out.println(ws + ":" + we);

				// 特定企業の場合：群1、特定企業でない場合：群2 に設定する.
				for (int e = 0; e < numE; e++) {
					for (int p = 0; p < numP; p++) {
						if ((ws <= p) && (p <= we)) {
							// 群1の条件を満たす場合
							clsMask[e][p] = 1;
						} else {
							clsMask[e][p] = 2;
						}
					}
				}

				// 群1と群2の違いを分散分析する
				double v = iga.compareGropus(data, clsMask);
				if (threshold <= v) {
					// 有意差がある場合
					found = true;

					if (v >= vMax) {
						// if ((we - ws) < (weMax - wsMax)) {
						vMax = v;
						weMax = we;
						wsMax = ws;
					}
				}
			}
		}

		String st = null;
		// 出力形式に纏める.
		if (found) {
			for (int e = 0; e < numE; e++) {

				st = "特定期間型" + ",";
				st = st + "\"" + ind + "\",";
				st = st + enterprises.get(e) + ",";
				st = st + periods.get(wsMax) + ",";
				st = st + periods.get(weMax) + ",";
				st = st + vMax + ",";
				st = st + periods.get(0) + ",";
				st = st + periods.get(numP - 1) + ",";
				for (int p = 0; p < numP; p++) {
					if (null != data[e][p]) {
						st = st + "," + data[e][p];
					} else {
						st = st + ",";
					}
				}
				System.out.println(st);
				res = res + st + "\r\n";
			}
		}
		return res;
	}

	// =================================================================
	/**
	 * 業界動向型戦略ワードを抽出する.
	 *
	 * 窓の幅に制限を加えることで抽出精度を高めている.
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
	private String industrialTrend(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		String res = "";

		// 1社しかない場合は処理しない
		if (1 >= countEnterpriseWithWord(data, enterprises, periods)) {
			return res;
		}

		final int minWindowSize = 1; // 窓の最小幅
		final int maxWindowSize = 4; // 窓の最大幅
//		final double threshold = 0.999999e0d; // 閾値 (2019.01.17)
		final double threshold = 0.95e0d; // 閾値

		int numE = enterprises.size();
		int numP = periods.size();

		int[][] clsMask = new int[numE][numP];
		InterGroupsAnalyzer iga = new InterGroupsAnalyzer(true);

		double vMax = 0.0e0d;
		int wsMax = 0;
		int weMax = numP - 1;
		boolean found = false;

		for (int wSize = minWindowSize; wSize <= maxWindowSize; wSize++) {
			// 期間窓の幅を選択する
			for (int ws = 1; ws < numP - wSize - 1; ws++) {
				// 期間窓の終点を選択する
				int we = ws + wSize + 1;

				// System.out.println(ws + ":" + we);

				// 特定企業の場合：群1、特定企業でない場合：群2 に設定する.
				for (int e = 0; e < numE; e++) {
					for (int p = 0; p < numP; p++) {
						if (we <= p) {
							// 群1の条件を満たす場合
							clsMask[e][p] = 1;
						} else if (p <= ws) {
							clsMask[e][p] = 2;
						}
					}
				}

				// 群1と群2の違いを分散分析する
				double v = iga.compareGropus(data, clsMask);
				if (threshold <= v) {
					// 有意差がある場合
					found = true;

					if (v >= vMax) {
						// if ((we - ws) < (weMax - wsMax)) {
						vMax = v;
						weMax = we;
						wsMax = ws;
					}
				}
			}
		}

		// 出力形式に纏める.
		if (found) {
			for (int e = 0; e < numE; e++) {
				String st = "業界動向型" + ",";
				st = st + "\"" + ind + "\",";
				st = st + enterprises.get(e) + ",";
				st = st + periods.get(wsMax) + ",";
				st = st + periods.get(weMax) + ",";
				st = st + vMax + ",";
				st = st + periods.get(0) + ",";
				st = st + periods.get(numP - 1) + ",";
				for (int p = 0; p < numP; p++) {
					if (null != data[e][p]) {
						st = st + "," + data[e][p];
					} else {
						st = st + ",";
					}
				}
				System.out.println(st);
				res = res + st + "\r\n";
			}
		}
		return res;
	}

	// =================================================================
	/**
	 * 指標を含む企業の数を算出する.
	 *
	 * @param data
	 *            値[企業][期間].矩形を想定.
	 * @param enterprises
	 *            企業のリスト
	 * @param periods
	 *            期間のリスト
	 * @return
	 * 				指標を含む企業の数
	 */
	private int countEnterpriseWithWord(final Double[][] data,
			final List<String> enterprises, final List<String> periods) {

		int numE = enterprises.size();
		int numP = periods.size();
		int c = 0;
		for (int ent = 0; ent < numE; ent++) {
			boolean flg = false;
			for (int prd = 0; prd < numP; prd++) {
				if (null != data[ent][prd]) {
					flg = true;
					break;
				}
			}
			if (flg) {
				c++;
			}
		}

		return c;
	}

	// =================================================================
	/**
	 * 候補ワードを読込む.
	 *
	 * @param targetPath
	 *            入力パス
	 */
	private void loadWords(final String targetPath) {

		// ファイル名の一覧を取得する
		File file = new File(targetPath);
		File[] files = file.listFiles();

		for (File f : files) {
			// 各ファイルに対して

			// ファイル名から、企業と年度の情報を取り出す.
			// ※ファイル名の名称ルールに依存
			String fileName = f.getName();

			String[] parts1 = fileName.split("\\.", 3);
			String[] parts2 = parts1[0].split("\\)", 2);
			String[] parts3 = parts2[0].split("\\(", 2);

			String docType = parts3[0];
			String entName = parts3[1];
			String year = parts2[1];

			if (docType.equals("AR")) {
				// ※文書種別を限定

				load(f, entName, year); // 個別のファイルからワードを読込む
			}
		}

		return;
	}

	/**
	 * 個別のファイルからワードを読込む.
	 *
	 * @param file
	 *            ファイル
	 * @param entName
	 *            企業名
	 * @param year
	 *            年度
	 */
	private void load(final File file, final String entName,
			final String year) {

		// System.out.println("[" + entName + "]-[" + year + "]");

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();

			while (null != (str = br.readLine())) {

				CSVTokenizer tknzr = new CSVTokenizer(str);
				String word = tknzr.nextToken();
				String gram = tknzr.nextToken();
				String freq = tknzr.nextToken();
				String val = tknzr.nextToken();
				Double d = Double.parseDouble(val);

				epiDM.putData(entName, year, word, d);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
}
