package jp.ac.tcu.okadak.ei_mining.data_loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * 企業財務データのローディング.
 *
 * 元データはライセンス契約により保護されているので ライセンスを得たローカルPC上のみに置かれている,
 *
 * @author K.Okada
 * @version 2018.08.21
 */
public class FDBDataLoader {

	/**
	 *
	 */
	private static int UNIT = 1000000;

	/**
	 * 読込んだ企業財務データ.
	 */
	private EPIDataManager<Double> dm = new EPIDataManager<Double>();

	/**
	 * 対象データ要素の一覧.
	 */
	private Map<String, String> targetDataElements = new HashMap<String, String>();

	/**
	 * 対象企業の一覧.
	 */
	private Map<String, String> targetEnterprises = new HashMap<String, String>();

	/**
	 * 出力先ファイルパス.
	 *
	 * (設定ファイルで指定される出力先ファイルパスを保持しておく)
	 */
	private String outputPath;

	/**
	 * 出力先ファイルパスを返す.
	 *
	 * @return 出力先ファイルパス
	 */
	public String getOutputPath() {
		return this.outputPath;
	}

	/**
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static void main(final String[] args) {

		System.out.println("Start FDB Loading ...");

		FDBDataLoader fdbl = new FDBDataLoader();
		fdbl.load();

		System.out.println("... Fin.");
	}

	/**
	 * 企業財務データを読込む.
	 *
	 * @return EPIデータマネジャー
	 */
	public EPIDataManager<Double> load() {

		String targetFile;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/FDBDataLoader.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			// ==== 設定を読込む
			String settingPath = br.readLine();
			String targetPath = br.readLine();
			this.outputPath = br.readLine();

			// 対象データ要素を読込む
			String dataElementsSettingFile = settingPath
					+ "/targetElements.txt";
			loadSetting(targetDataElements, dataElementsSettingFile);

			// 対象企業を読込む
			String enterprisesSettingFile = settingPath
					+ "/targetEnterprises.txt";
			loadSetting(targetEnterprises, enterprisesSettingFile);

			// ==== データを読込む

			// 損益計算書データを読込む
			targetFile = targetPath + "/連結損益計算書.txt";
			loadData(targetFile);

			// 貸借対照表(借方)データを読込む
			targetFile = targetPath + "/連結貸借対照表1.txt";
			loadData(targetFile);

			// 貸借対照表(貸方)データを読込む
			targetFile = targetPath + "/連結貸借対照表2.txt";
			loadData(targetFile);

			// キャッシュフロー計算書データを読込む
			targetFile = targetPath + "/連結キャッシュフロー計算書.txt";
			loadData(targetFile);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.dm;
	}

	/**
	 * 設定ファイルを読込む.
	 *
	 * @param res
	 *            読込んだ値
	 * @param target
	 *            設定ファイル
	 */
	private void loadSetting(final Map<String, String> res,
			final String target) {

		try {
			File settingFile = new File(target);
			FileReader fr = new FileReader(settingFile);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while (null != (line = br.readLine())) {
				res.put(line, line);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 企業財務データバンクファイルからデータを読込む.
	 *
	 * @param fName
	 *            企業財務データバンクのファイル名
	 */
	private void loadData(final String fName) {

		System.out.println(fName);

		String line;
		CSVTokenizer tokenizer;

		try {
			File file = new File(fName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// ファイル毎の索引を作成する
			line = br.readLine();
			tokenizer = new CSVTokenizer(line);
			Map<Integer, String> index = new HashMap<Integer, String>();
			int c = 0;
			while (tokenizer.hasMoreTokens()) {
				String sValue = tokenizer.nextToken();

				if (null != targetDataElements.get(sValue)) {
					// 対象データ要素の場合
					index.put(c, sValue);
//					System.out.println(c + " : " + sValue);
				}
				c++;
			}
			System.out.println("...");

//			for (String e : targetEnterprises.keySet()) {
//				System.out.println(" - " + e);
//			}

			// データを読込む
			while (null != (line = br.readLine())) {
				// 全ての行に対して

				tokenizer = new CSVTokenizer(line);

				// 基本情報の読込み
				tokenizer.nextToken(); // 空読み
				String entName = tokenizer.nextToken();
				String entID = tokenizer.nextToken();
				String date = tokenizer.nextToken();

				// date の年度への変換
				date = period(date);

				if (targetEnterprises.containsKey(entID)) {
					// 対象企業の場合

					String indicator;
					Double value;
					int i = 4;
					while (tokenizer.hasMoreTokens()) {
						// 全てのデータ要素に対して

						String sValue = tokenizer.nextToken();

						if (null != (indicator = index.get(i))) {

							if (0 != sValue.length()) {
								// 単位換算する
								long x = Long.parseLong(sValue) / UNIT;
								value = (Double) ((double)x);
							} else {
								value = null;
							}
							dm.putData(entName, date, indicator, value);
						}
						i++;
					}
				}
			}

			// System.out.println("...");

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 年月を標準的な会計年度に変換する.
	 *
	 * @param str
	 *            年月
	 * @return 年度
	 */
	private String period(final String str) {

		// 標準会計年度の終わりは 3月
		final int endOfFinancialYear = 3;
		String[] s = str.split("-", 2); // 年月文字列を年と月に分割
		String sYear = s[0];
		String sMonth = s[1];

		int year = Integer.parseUnsignedInt(sYear);
		int month = Integer.parseUnsignedInt(sMonth);

		if (month <= endOfFinancialYear) {
			year--;
		}

		return String.valueOf(year);
	}
}
