package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 形態素解析器の抽象クラス.
 *
 * @author K.Okada
 * @version 2018.08.22
 */
public abstract class MorphologicalAnalyzer {

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
				List<Morpheme> mor = analyze(sentense);

				String result;
				if (PartOfSpeech.ALL == mode) {
					result = getSurface(mor, " ", mode);
				} else {
					result = getBase(mor, " ", mode);
				}


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
	 * 表示文字列を得る.
	 *
	 * @param mor	形態素のリスト
	 * @param sep	セパレータ
	 * @param mode	表示する品詞
	 * @return	文字列
	 */
	final String getSurface(final List<Morpheme> mor, final String sep,
			final int mode) {

		StringBuilder builder = new StringBuilder();

		for (Morpheme m : mor) {
			// リスト中の形態素に対して

			int poq = m.getPartOfSpeech();
			if ((poq & mode) == poq) {
				// modeで指定された品詞であれば
				builder.append(m.getSurface());
				builder.append(sep);
			}
		}
		return builder.toString();
	}

	/**
	 * 基底文字列を得る.
	 *
	 * @param mor	形態素のリスト
	 * @param sep	セパレータ
	 * @param mode	表示する品詞
	 * @return	文字列
	 */
	final String getBase(final List<Morpheme> mor, final String sep,
			final int mode) {

		StringBuilder builder = new StringBuilder();

		for (Morpheme m : mor) {
			// リスト中の形態素に対して

			int poq = m.getPartOfSpeech();
			if ((poq & mode) == poq) {
				// modeで指定された品詞であれば
				builder.append(m.getBase());
				builder.append(sep);
			}
		}
		return builder.toString();
	}


	/**
	 * 文に対して形態素解析を行う.
	 *
	 * @param targetDoc
	 *            分析対象の文
	 * @return 解析結果
	 **/
	abstract List<Morpheme> analyze(final String targetDoc);
}
