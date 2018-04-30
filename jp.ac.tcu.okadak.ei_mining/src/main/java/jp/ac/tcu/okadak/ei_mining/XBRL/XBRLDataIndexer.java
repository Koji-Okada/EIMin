package jp.ac.tcu.okadak.ei_mining.XBRL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * XBRLデータの索引生成器.
 *
 * @author K.Okada
 * @version 2018.04.27
 */
public final class XBRLDataIndexer {

	/**
	 * コントラクタを隠蔽.
	 */
	private XBRLDataIndexer() {
		super();
	}

	/**
	 * 出力ファイル.
	 */
	private BufferedWriter bw;

	/**
	 * 指定されたフォルダから XBRLインデックスを作成する.
	 *
	 * @param arg
	 *            デフォルト
	 */
	public static void main(final String[] arg) {

		System.out.println("Start XBRL Loader ...");

		XBRLDataIndexer indexer = new XBRLDataIndexer();
		indexer.index();

		System.out.println("... Successfully Complete.");
	}

	/**
	 * 構成設定ファイルで指定されたフォルダに対して XBRLデータの索引を作成する.
	 */
	private void index() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/XBRLDataIndexer.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			outputPath = br.readLine();

			System.out.println("target: " + targetPath);
			System.out.println("output: " + outputPath);

			// 出力ファイルを開く
			File indexFile = new File(outputPath + "/" + "index.txt");
			FileWriter fw = new FileWriter(indexFile);
			bw = new BufferedWriter(fw);

			// XBRLを探す
			searchXBRLData(targetPath);

			// 入出力ファイルを閉じる
			br.close();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 対象とする XBRLファイルを階層的に探す. (再帰呼出し)
	 *
	 * @param targetDir
	 *            処理対象ディレクトリ
	 */
	private void searchXBRLData(final String targetDir) {

		File base = new File(targetDir);
		File[] files = base.listFiles();

		try {
			// SAXパーサーファクトリを生成する
			SAXParserFactory spf = SAXParserFactory.newInstance();

			// SAXパーサーを生成する
			SAXParser parser = spf.newSAXParser();

			// パーサが処理を委譲するハンドラを生成する
			XBRLBasicInfoHandler handler = new XBRLBasicInfoHandler();

			for (File f : files) {
				// ファイル毎に

				if (f.isDirectory()) {
					// サブディレクトリの場合
					// 再帰呼出し

					// System.out.println(f.getPath());

					searchXBRLData(f.getPath());
				} else {
					// ファイルの場合

					String fileName = f.getName();

					if (fileName.contains("asr")) {
						// 有価証券報告書 asr に絞込む
						if (fileName.contains("_E")) {
							// EDNETコードの先頭は E から始まる
							// ファンドコードの場合は G

							if (fileName.contains("header")) {
								// 表紙ファイルに絞込む

								// パース処理を行う
								parser.parse(f, handler);

								// EDINETコードを取得する
								String ediNetCode = handler.getEdiNetCode();

								// 証券コードを取得する
								String securityCode = handler.getSecurityCode();

								// 企業名を取得する
								String enterpriseName = handler
										.getEnterpriseName();

								// 会計期間終了日を取得する
								String date = handler
										.getCurrentFiscalYearEndDate();

								this.bw.write(ediNetCode + ",");
								this.bw.write(securityCode + ",");
								this.bw.write(date + ",");
								this.bw.write(enterpriseName + ",");
								this.bw.write(f.getParent());
								this.bw.newLine();

								System.out.println(date + "\t" + enterpriseName
										+ "\t" + f.getParent());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
