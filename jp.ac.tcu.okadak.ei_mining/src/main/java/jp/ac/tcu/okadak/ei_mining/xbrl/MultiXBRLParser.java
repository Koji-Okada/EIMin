package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;

public class MultiXBRLParser {

	public static void main(String[] args) {

//		String indexFile = "X:\\XBRL_Data\\index.txt";
		String indexFile = "E:/Target.txt";
//		String indexFile = "D:/XBRL/TargetList.txt";

		System.out.println("Start ...");

		MultiXBRLParser m = new MultiXBRLParser();
		m.parse(indexFile);

		System.out.println("... Fin.");
		return;
	}


	/**
	 *
	 * @param indexLst
	 */
	void parse(String indexLst) {

		try {
			File settingFile = new File(indexLst);
			FileReader fr = new FileReader(settingFile);
			BufferedReader br = new BufferedReader(fr);

			XBRLParserRC parserRC = new XBRLParserRC();

			String line;
//			System.out.println("会社名,株式コード,EDINETコード,会計基準,Version,決算日,売上高(決算日),売上高(1年前),売上高(2年前),売上高(3年前),売上高(4年前)");
			System.out.println("会社名,株式コード,EDINETコード,会計基準,Version,決算日,売上高(決算日),売上高(1年前)");

			while (null != (line = br.readLine())) {
				CSVTokenizer tokenizer = new CSVTokenizer(line);

				String type = tokenizer.nextToken();
				String ver = tokenizer.nextToken();
				String eCode = tokenizer.nextToken();
				String sCode = tokenizer.nextToken();
				String repDate = tokenizer.nextToken();
				String entName = tokenizer.nextToken();
				String path = tokenizer.nextToken();

//				if (0 == type.compareTo("asr")) {
//					// ※有価証券報告書だけに限定している
					
				if ((0 == type.compareTo("asr")) || 0 == type.compareTo("q1r") ||
						(0 == type.compareTo("q2r")) || 0 == type.compareTo("q3r")) {

//					path = path.replace("X:", "D:/XBRL/Target");		// 開発環境の違いを吸収するため

					String xbrlUri = parserRC.findXbrlFile(path);
					
					parserRC.data = new XBRLData();	// XBRLデータオブジェクトを作成
					
					parserRC.parser = new XBRLBasicInfoParser();
					parserRC.parse(xbrlUri);
//					parserRC.data.showBasicInfo();
//					parserRC.data.showDateInfo();
					
					parserRC.parser = new XBRLFinDataParser();
					parserRC.parse(xbrlUri);
//					parserRC.data.showPLInfo();
					
					
					System.out.println(parserRC.data.output());
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

}
