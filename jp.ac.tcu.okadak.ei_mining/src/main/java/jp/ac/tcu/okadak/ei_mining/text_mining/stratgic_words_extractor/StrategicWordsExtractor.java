package jp.ac.tcu.okadak.ei_mining.text_mining.stratgic_words_extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;
import jp.ac.tcu.okadak.ei_mining.feature_recognizer.InterGroupsAnalyzer;

/**
 * 戦略ワード抽出器.
 *
 * @author K.Okada
 * @version 2018.08.09
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
	 * 戦略ワード候補を抽出する.
	 *
	 * @param args
	 *            ダミー引数
	 */
	public static void main(final String[] args) {

		System.out.println("Start extracting strategic words");

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
		List<String> periods = epiDM.listPeriod();
		int numPeriods = periods.size();
		List<String> indicators = epiDM.getIndicators();
		int numIndicators = indicators.size();

		System.out.println(
				numEnterprises + " : " + numPeriods + " : " + numIndicators);

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

			// ここから、パターン毎に抽出する

			// 特定企業型戦略ワードを抽出する
			specificEnterprise(data, enterprises, periods, ind);

			// 特定期間型戦略ワードを抽出する
			// specificPeriod(data, enterprises, periods, ind);

		}
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
	 */
	private void specificEnterprise(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		final double threshold = 0.999999e0d; // 閾値
		// final double threshold = 0.95e0d; // 閾値

		int numE = enterprises.size();
		int numP = periods.size();

		int[][] clsMask = new int[numE][numP];
		InterGroupsAnalyzer iga = new InterGroupsAnalyzer(true);

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

				// 出力形式に纏める.
				double d;
				String st = "特定企業型" + "\t";
				st = st + ind + "\t" + v + "\t" + enterprises.get(se) + "\t"
						+ periods.get(0) + "\t" + periods.get(numP - 1) + "\t";
				for (int p = 0; p < numP; p++) {
					if (null == data[se][p]) {
						d = 0.0e0d;
					} else {
						d = data[se][p];
					}
					st = st + d + "\t";
				}

				System.out.println(st);
			}
		}
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
	 */
	private void specificPeriod(final Double[][] data,
			final List<String> enterprises, final List<String> periods,
			final String ind) {

		final int minWindowSize = 1; // 窓の最小幅
		final int maxWindowSize = 4; // 窓の最大幅
		final double threshold = 0.999999e0d; // 閾値
		// final double threshold = 0.95e0d; // 閾値

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

		// 出力形式に纏める.
		if (found) {
			for (int e = 0; e < numE; e++) {
				double d;
				String st = "特定期間型" + "\t";
				st = st + ind + "\t" + vMax + "\t" + enterprises.get(e) + "\t"
						+ periods.get(wsMax) + "\t" + periods.get(weMax) + "\t";
				for (int p = 0; p < numP; p++) {
					if (null == data[e][p]) {
						d = 0.0e0d;
					} else {
						d = data[e][p];
					}
					st = st + d + "\t";
				}
				System.out.println(st);
			}
		}
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
			String[] parts1 = fileName.split("_", 3);
			String[] parts2 = parts1[2].split("\\.", 2);
			String docType = parts1[0];
			String entName = parts1[1];
			String year = parts2[0];

			if (docType.equals("アニュアルレポート")) {
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

		System.out.println("[" + entName + "]-[" + year + "]");

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();

			while (null != (str = br.readLine())) {

				CSVTokenizer tknzr = new CSVTokenizer(str);
				tknzr.nextToken();
				String word = tknzr.nextToken();
				String freq = tknzr.nextToken();
				String val = tknzr.nextToken();
				Double d = Double.parseDouble(val);

				epiDM.addData(entName, year, word, d);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
}
