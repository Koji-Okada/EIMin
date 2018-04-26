package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 形態素解析器の抽象クラス.
 *
 * @author K.Okada
 * @version 2018.04.26
 */
public abstract class MorphlogicalAnalyzer {

	/**
	 * ファイルに対して形態素解析を行う.
	 *
	 * @param outputFileName
	 *            出力ファイル名
	 * @param inputFileName
	 *            入力ファイル名
	 * @param mode
	 *            -1 : 分かち書き 正値: 出力品詞指定
	 *
	 */
	final void analyzeFile(final String outputFileName,
			final String inputFileName, final int mode) {

		try {
			// 入力ファイルオープン
			File inputFile = new File(inputFileName);
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			// 出力ファイルオープン
			File outputFile = new File(outputFileName);
			FileWriter fw = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(fw);

			// ループ
			String sentense;
			while (null != (sentense = br.readLine())) {
				String result = analyzeSentense(sentense, mode);

				// テキスト抽出結果を保存する
				bw.write(result);
				bw.newLine();
			}

			// 入出力ファイルを閉じる
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文に対して形態素解析を行う.
	 *
	 * @param targetDoc
	 *            分析対象の文
	 * @param mode
	 *            -1 : 分かち書き 正値: 出力品詞指定
	 * @return 解析結果
	 **/
	abstract String analyzeSentense(final String targetDoc, final int mode);
}
