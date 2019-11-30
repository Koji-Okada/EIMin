package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * XBRL情報抽出器.
 *
 * @author K.Okada
 * @version 2019.11.30
 */
public class XBRLInfoExtractor {

	/**
	 * XBRLデータの索引.
	 */
	private EPIDataManager<String> xbrlIndex = new EPIDataManager<String>();

	/**
	 * XBRLデータ索引を返す.
	 *
	 * @return	XBRLデータ索引
	 */
	final EPIDataManager<String> getXbrlIndex() {
		return xbrlIndex;
	}

	// =======================================================================
	/**
	 * 指定ファイルに示された企業の XBRL情報を抽出する.
	 *
	 * @param arg
	 *            デフォルト
	 */
	public static void main(final String[] arg) {

		System.out.println("Start XBRL Information Extractor ...");

		XBRLInfoExtractor extractor = new XBRLInfoExtractor();
		extractor.extract();

		System.out.println("... Successfully Complete.");
	}

	/**
	 * 構成設定ファイルで指定された企業リストから XBRL情報を抽出する.
	 */
	private void extract() {

		String settingFilePath; // 抽出条件設定ファイルのパス
		String indexPath; // 索引ファイルのパス
		String outputPath; // 抽出結果の出力先のパス

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/XBRLInfoExtractor.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			settingFilePath = br.readLine();
			indexPath = br.readLine();
			outputPath = br.readLine();

			// XBRLデータの索引を読込む
			loadXBRLIndex(indexPath);

			// 対象企業リストを取得する
			ArrayList<String> targetEnterprises = getTargetEnterprises(
					settingFilePath);

			for (String ent : targetEnterprises) {
				System.out.println(ent);
			}

			// 対象要素リストを取得する
			ArrayList<String> targetElements = getTargetElements(
					settingFilePath);

			for (String elm : targetElements) {
				System.out.println(elm);
			}

			// 対象企業から対象要素を抽出する
			getInfo(targetEnterprises);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 指定された情報を取得する.
	 * 										※作成中：効率悪い
	 *
	 * @param targetEnterprises	対象企業リスト
	 */
	// =======================================================================
	private void getInfo(final ArrayList<String> targetEnterprises) {

		Set<String> prList = (Set<String>) xbrlIndex.getPeriods();

		String[] docTypes = { "q1r", "q2r", "q3r", "asr" };

		for (String ent : targetEnterprises) {
			System.out.println("==== " + ent);

			for (String pr : prList) {

				for (String docType : docTypes) {

					String path = xbrlIndex.getValue(ent, pr, docType);

					if (null != path) {
						System.out.println("*-- " + docType + ":" + pr + ":"
								+ ent + ":" + path);
					}
				}
			}
		}

		return;
	}

	// =======================================================================
	/**
	 * 対象企業リストを取得する.
	 *
	 * @param settingFilePath
	 *            処理対象設定ファイルのパス
	 * @return 企業名のリスト
	 */
	private ArrayList<String> getTargetEnterprises(
			final String settingFilePath) {

		ArrayList<String> list = new ArrayList<String>();
		String enterprise;

		try {
			File file = new File(settingFilePath + "/TargetEnterprises.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (null != (enterprise = br.readLine())) {
				list.add(enterprise);
			}

			// ファイルを閉じる
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 対象要素リストを取得する.
	 *
	 * @param targetPath
	 *            処理対象設定ファイルのパス
	 * @return XBRL要素名のリスト
	 */
	private ArrayList<String> getTargetElements(final String targetPath) {

		ArrayList<String> list = new ArrayList<String>();
		String element;

		try {
			File file = new File(targetPath + "/TargetElements.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (null != (element = br.readLine())) {
				list.add(element);
			}

			// ファイルを閉じる
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	// =======================================================================
	/**
	 * XBRLデータ索引を読込む.
	 *
	 * @param indexPath
	 *            索引ファイルのパス
	 */
	private void loadXBRLIndex(final String indexPath) {

		System.out.println("! ... " + indexPath);

		// フォルダ中のファイルを取得する
		File base = new File(indexPath);
		File[] files = base.listFiles();

		for (File f : files) {
			// ファイル毎に

			String filePath = f.getPath();
			System.out.println("   ... " + filePath);

			// ファイルの中身を読み込む
			if (f.isFile()) {
				load(filePath);
			}
		}

		return;
	}

	/**
	 * XBRLデータ索引を読込む (単一ファイル).
	 *
	 * @param filePath
	 *            XBRLデータ索引ファイル.
	 */
	private void load(final String filePath) {

		try {

			FileReader fr = new FileReader(new File(filePath));
			BufferedReader br = new BufferedReader(fr);

			String str;
			while (null != (str = br.readLine())) {

				CSVTokenizer csvt = new CSVTokenizer(str);

				String docType = csvt.nextToken(); // ドキュメント種別
				csvt.nextToken(); // 版
				csvt.nextToken(); // コード
				csvt.nextToken(); // 証券コード+"0"
				String date = csvt.nextToken(); // 決算日
				String enterprise = csvt.nextToken(); // 企業名
				String path = csvt.nextToken(); // フォルダパス

				if ((0 != enterprise.length()) && (0 != date.length())) {

					// 決算日の表現形式を変換する：yyyy-mm-dd → yyyy-mm
					String period = date.substring(0, 7);

					// 索引を登録する
					xbrlIndex.addData(enterprise, period, docType, path);

					System.out.println(docType + "\t" + period + "\t" + enterprise + "\t"
							+ path);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
