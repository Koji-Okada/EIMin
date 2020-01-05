package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 形態素解析器の抽象クラス.
 *
 * @author K.Okada
 * @version 2020.01.05
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

		//		String separator = " ";
		String separator = "\r\n"; // 暫定的に変更

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
				List<Morpheme> mor = new ArrayList<Morpheme>();
				analyze(mor, sentense);

				String result;
				if (PartOfSpeech.ALL == mode) {
					result = getSurface(mor, separator, mode);
				} else {
					result = getBase(mor, separator, mode);
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
		return;
	}

	/**
	 * ファイルを分析し形態素リストを得る.
	 *
	 * @param inputFileName 分析対象ファイル名
	 * @return 形態素リスト
	 */
	public final List<Morpheme> getMorphemes(final String inputFileName) {

		List<Morpheme> mor = new ArrayList<Morpheme>();

		try {
			// 入力ファイルオープン
			File inputFile = new File(inputFileName);
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			// ループ
			String sentense;
			while (null != (sentense = br.readLine())) {
				analyze(mor, sentense);
			}

			// 入力ファイルを閉じる
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mor;
	}

	/**
	 * 表示文字列を得る.
	 *
	 * @param mor	形態素のリスト
	 * @param sep	セパレータ
	 * @param mode	表示する品詞
	 * @return	文字列
	 */
	public final String getSurface(final List<Morpheme> mor, final String sep,
			final int mode) {

		StringBuilder builder = new StringBuilder();

		builder.append("<newLine>" + sep);	// 暫定的に追加

		for (Morpheme m : mor) {
			// リスト中の形態素に対して

			int pos = m.getPartOfSpeech();
			if ((pos & mode) == pos) {
				// modeで指定された品詞であれば
				builder.append(m.getSurface());
				builder.append(sep);
			}
		}

		builder.append("</newLine>");	// 暫定的に追加

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
	public final String getBase(final List<Morpheme> mor, final String sep,
			final int mode) {

		StringBuilder builder = new StringBuilder();

		for (Morpheme m : mor) {
			// リスト中の形態素に対して

			int pos = m.getPartOfSpeech();
			if ((pos & mode) == pos) {
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
	 * @param morphemes 形態素を追加するリスト
	 * @param targetSentense 分析対象の文
	 **/
	public abstract void analyze(final List<Morpheme> morphemes,
			final String targetSentense);
}
