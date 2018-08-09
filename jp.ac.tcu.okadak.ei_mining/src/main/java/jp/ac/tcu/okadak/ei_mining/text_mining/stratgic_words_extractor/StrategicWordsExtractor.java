package jp.ac.tcu.okadak.ei_mining.text_mining.stratgic_words_extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

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
	 *
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

		loadWords(targetPath); // 候補ワードを読込む

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

					Double d = epiDM.getValue(nameE, nameP, ind);

					data[e][p] = d;

					// ここから、パターン毎に抽出する


					System.out.println(nameE + " : " + nameP + " : " + ind + " = " + data[e][p]);
				}
			}
		}
	}

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
