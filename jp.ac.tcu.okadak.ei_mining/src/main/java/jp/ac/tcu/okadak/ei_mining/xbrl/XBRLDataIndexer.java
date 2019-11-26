package jp.ac.tcu.okadak.ei_mining.xbrl;

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
 * @version 2019.11.26
 */
public final class XBRLDataIndexer {

	/**
	 * 報告書の種類.
	 */
	private String docs[] = { "asr", "q1r", "q2r", "q3r" };

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
	 *
	 */
	private int counterForException = 0;

	/**
	 * 指定されたフォルダから XBRLインデックスを作成する.
	 *
	 * @param arg
	 *            デフォルト
	 */
	public static void main(final String[] arg) {

		System.out.println("Start XBRL Indexer ...");

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

		System.out.println("    Exceptions = " + this.counterForException);

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

					searchXBRLData(f.getPath());
				} else {
					// ファイルの場合

					String fileName = f.getName();

					for (String doc : docs) {

						if (fileName.contains(doc)) {
							// 有価証券報告書 asr に絞込む
							if (fileName.contains("_E")) {
								// EDNETコードの先頭は E から始まる
								// ファンドコードの場合は G

								if (fileName.contains(".xbrl")) {

									// パース処理を行う
									parser.parse(f, handler);

									// EDINETコードを取得する
									String ediNetCode = handler.getEdiNetCode();
									if (null == ediNetCode) {
										ediNetCode = "";
									}

									// 証券コードを取得する
									String securityCode = handler
											.getSecurityCode();
									if (null == securityCode) {
										securityCode = "";
									}

									// 企業名を取得する
									String enterpriseName = handler
											.getEnterpriseName();
									if (null == enterpriseName) {
										enterpriseName = "";
									}

									// 会計期間終了日を取得する
									String date = handler
											.getCurrentFiscalYearEndDate();
									if (null == date) {
										date = "";
									}

									// 提出回数を取得する
									String numOfSubmission = handler
											.getNumOfSubmission();
									if (null == numOfSubmission) {
										numOfSubmission = "";
									}

									String str = doc + "," + numOfSubmission + ","
											+ ediNetCode + "," + securityCode
											+ "," + date + "," + enterpriseName
											+ "," + f.getParent();

									this.bw.write(str);
									this.bw.newLine();

									System.out.println(doc + "\t"
											+ numOfSubmission + "\t"
											+ date + "\t" + enterpriseName
											+ "\t" + f.getParent());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			counterForException++; // 例外発生事象をカウントする
			e.printStackTrace();
		}
		return;
	}
}
