package jp.ac.tcu.okadak.ei_mining.text_mining.n_gram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor.TextFormatter;

/**
 *
 * 複数文書のN-Gram分析器.
 *
 * @author K.Okada
 * @version 2018.04.20
 */
public class MultiDocNGramAnalyzer {

	/**
	 * N-Gram の最大値.
	 */
	private int maxN;

	/**
	 * N-Gram の最大値を設定する.
	 *
	 * @param maxNGram
	 *            N-Gramの最大値
	 */
	public final void setMaxN(final int maxNGram) {

		this.maxN = maxNGram;
		return;
	}

	/**
	 * 複数文書の N-Gram分析を行う(メイン).
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static final void main(final String[] args) {

		System.out.println("Start MultiDocAnalyzer ...");

		MultiDocNGramAnalyzer mda = new MultiDocNGramAnalyzer();
//		mda.setMaxN(32);
		mda.setMaxN(24);

		mda.analyzeNGram();

		System.out.println("... Successfully Complete.");

		return;
	}

	/**
	 * 文書毎に階層化N-Gram分析する.
	 */
	private void analyzeNGram() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/MultiDocNGramAnalyzer.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			outputPath = br.readLine();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// ファイル名の一覧を取得する
		File file = new File(targetPath);
		File[] files = file.listFiles();

		for (File f : files) {
			// 各ファイルに対して

			if (!f.isFile()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			String fName = f.getName();
			System.out.println("====" + fName);

			// テキストファイル全文を読込む
			TextFormatter tr = new TextFormatter();
			String str0 = tr.readAll(targetPath + fName);
			System.out.print("  Loading done");

			// 改行の削除
			String str = str0.replaceAll("\r\n", "");

			// System.out.println("DocLen=" + targetStr.length());

			NGramAnalyzer nga = new NGramAnalyzer();

			int nGram = this.maxN;
			if (str.length() < this.maxN) {
				// 対象文書長が短い場合
				nGram = str.length();
			}

			// N-Gram分析を行う
			String results = "COUNT,TERM,FREQ,SCORE\r\n";
			results = results + nga.analyze(str, nGram);

			System.out.println(results);

			// 結果を記録する
			try {
				File recFile = new File(outputPath + fName);
				FileWriter filewriter = new FileWriter(recFile);

				filewriter.write(results);

				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return;
	}
}
