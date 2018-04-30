package jp.ac.tcu.okadak.ei_mining.XBRL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * XBRLテキスト情報抽出器.
 *
 * @author K.Okada
 * @version 2018.04.30
 */
public class XBRLTextExtractor {

	// <ix:nonNumeric contextRef="FilingDateInstant"
	// name="jpcrp_cor:BusinessRisksTextBlock" escape="true">
	// </ix:nonNumeric>

	/**
	 * 指定ファイルに示された企業の XBRLテキストを作成する.
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
	 * 構成設定ファイルで指定された企業リストから XBRLテキストを作成する.
	 */
	private void extract() {

		String targetPath;
		String indexPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/XBRLTextExtractor.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			indexPath = br.readLine();
			outputPath = br.readLine();

			System.out.println("target: " + targetPath);
			System.out.println("index:  " + indexPath);
			System.out.println("output: " + outputPath);

			// 索引を読込む (未実装)



			// 対象企業リストを取得する
			ArrayList<String> targetEnterprises = getTargetEnterprises(
					targetPath);

			// 対象要素リストを取得する
			ArrayList<String> targetElements = getTargetElements(
					targetPath);

			// 対象企業から対象要素を抽出する
			for (String ent:targetEnterprises) {
				System.out.println(ent);
			}

			for (String elm:targetElements) {
				System.out.println(elm);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 対象企業リストを取得する.
	 *
	 * @param targetPath
	 *            処理対象設定ファイルのパス
	 * @return 企業名のリスト
	 */
	private ArrayList<String> getTargetEnterprises(final String targetPath) {

		ArrayList<String> list = new ArrayList<String>();
		String enterprise;

		try {
			File file = new File(targetPath + "/TargetEnterprises.txt");
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


}
