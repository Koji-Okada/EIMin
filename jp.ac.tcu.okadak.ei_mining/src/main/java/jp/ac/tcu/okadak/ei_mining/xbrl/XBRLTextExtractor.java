package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * XBRLテキスト情報抽出器.
 *
 * @author K.Okada
 * @version 2019.04.25
 */
public class XBRLTextExtractor {

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
	 * 指定ファイルに示された企業の XBRLテキストを抽出する.
	 *
	 * @param arg
	 *            デフォルト
	 */
	public static void main(final String[] arg) {

		System.out.println("Start XBRL Text Extractor ...");

		XBRLTextExtractor extractor = new XBRLTextExtractor();
		extractor.extract();

		System.out.println("... Successfully Complete.");
	}

	/**
	 * 構成設定ファイルで指定された企業リストから XBRLテキストを抽出する.
	 */
	private void extract() {

		String settingFilePath; // 抽出条件設定ファイルのパス
		String indexPath; // 索引ファイルのパス
		String outputPath; // 抽出結果の出力先のパス

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/XBRLTextExtractor.txt");
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
			// ◆◆◆◆　　未だ未実装！！

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

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

				csvt.nextToken();
				csvt.nextToken();
				csvt.nextToken();
				String date = csvt.nextToken();
				String enterprise = csvt.nextToken();
				String path = csvt.nextToken();

				if ((0 != enterprise.length()) && (0 != date.length())) {
					xbrlIndex.addData(enterprise, date, "index", path);

					System.out.println(date + "\t" + enterprise + "\t" + path);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
