package jp.ac.tcu.okadak.ei_mining.text_mining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * 複数文書のN-Gram分析器.
 *
 * @author K.Okada
 * @version 2016.01.17
 */
public class MultiDocAnalyzer {

	/**
	 *
	 */
	private String finalResults = "all.txt";

	/**
	 *
	 * 複数文書のN-Gram分析を行う(メイン).
	 *
	 * @author K.Okada
	 * @version 2016.08.18
	 *
	 * @param args			ダミー引数
	 */
	public static void main(final String[] args) {

		final String targetDir = "C:/Users/okada_kouji/Documents/NGram/Target/";
		final String recordDir = targetDir + "Results/";
		final int maxN = 64;

		MultiDocAnalyzer mda = new MultiDocAnalyzer();

		mda.analyzeNGram(maxN, targetDir, recordDir);

		return;
	}

	/**
	 *
	 * 文書毎に分析する.
	 *
	 * @author K.Okada
	 * @version 2016.01.05
	 *
	 * @param maxN			N-Gram分析の Nの最大値
	 * @param targetDir		分析対象のディレクトリ
	 * @param recordDir		結果を記録するディレクトリ
	 */
	private void analyzeNGram(final int maxN, final String targetDir,
			final String recordDir) {

		// ファイル名の一覧を取得する
		File file = new File(targetDir);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			System.out.println("====" + files[i].getName());

			TextExtracter txtExr = new TextExtracter();
			String orgStr = txtExr.extract(targetDir + files[i].getName());

			// 改行・空白文字を削除する
			String targetStr = txtExr.cleanUp(orgStr);

//			System.out.println("DocLen=" + targetStr.length());

			NGramAnalyzer nga = new NGramAnalyzer();

			int nGram = maxN;
			if (targetStr.length() < maxN) {
				// 対象文書長が短い場合
				nGram = targetStr.length();
			}

			// N-Gram分析を行う
			String results = nga.analyze(targetStr, nGram);

			System.out.println(results);

			// 結果を記録する
			try {
				File recFile = new File(recordDir + files[i].getName()
						+ ".txt");
				FileWriter filewriter = new FileWriter(recFile);

				filewriter.write(results);

				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// 分析結果を統合する
		integrateResults(recordDir);

		System.out.println("... Successfully Complete !!");
		return;
	}

	/**
	 *
	 * 分析結果を統合する.
	 *
	 * @author K.Okada
	 * @version 2016.01.17
	 *
	 * @param recordDir		結果が記録されたディレクトリ
	 */
	private void integrateResults(final String recordDir) {

		try {
			File allRecords = new File(recordDir + finalResults);
			BufferedWriter bw = new BufferedWriter(new FileWriter(allRecords));

			bw.write("DOC,COUNT,TERM,FREQ,SCORE\r\n");
			add(bw, recordDir);

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 *
	 *
	 * @author K.Okada
	 * @version 2016.01.08
	 *
	 * @param bw			出力先ファイル
	 * @param recordDir		結果が記録されたディレクトリ
	 */
	private void add(final BufferedWriter bw, final String recordDir) {

		// ファイル名の一覧を取得する
		File file = new File(recordDir);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				// ファイルの場合のみ処理対象とする
				break;
			}

			if (0 == files[i].getName().compareTo(finalResults)) {
				// all.txt 自身は対象外
				continue;
			}

			try {
				BufferedReader br = new BufferedReader(new FileReader(
						files[i]));

				String str;

				while (null != (str = br.readLine())) {
					bw.write(files[i].getName() + "," + str + "\r\n");
				}

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return;
	}
}
