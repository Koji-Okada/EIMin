package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.MorphologicalAnalyzer;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.NeologdMorphologicalAnalyzer;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.PartOfSpeech;


/**
 * 形態素階層化N-Gram解析器.
 *
 * @author K.Okada
 * @version 2019.05.08
 */
public class MNppAnalyzer {

	/**
	 * 重要度判定指標値.
	 */
	static final double THRESHOLD = 0.5e0d;

	/**
	 * 形態素解析器.
	 */
//	 private MorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();
	private MorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

	/**
	 * 最大グラム数.
	 */
	private int maxN = 32;

	/**
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		System.out.println("Start Morphlogical N++Gram.");

		MNppAnalyzer mna = new MNppAnalyzer();
		mna.analyzeFile("出力ファイル", "入力ファイル");

		System.out.println("... Fin.");
	}

	/**
	 * 処理する.
	 */
	final void process() {

		// 空の形態素リストを作る
		List<Morpheme> morphemes = new ArrayList<Morpheme>();

		String testStr1 = "かえるがかえる。";
		mla.analyze(morphemes, testStr1); // 形態素リストに変換、追加する
		String testStr2 = "かえるがおよぐ。";
		mla.analyze(morphemes, testStr2); // 形態素リストに変換、追加する

		System.out.println("(" + morphemes.size() + "): " + mla.getSurface(
				morphemes, " ", PartOfSpeech.ALL));

		// 形態素階層化 N-Gram解析を行う.
		List<MNElement> results = new ArrayList<MNElement>();
		this.analyze(results, morphemes);

		return;
	}

	/**
	 * ファイルに対して形態素階層化 N-Gram解析を行う.
	 *
	 * @param outputFileName
	 *            入力ファイル
	 * @param inputFileName
	 *            解析結果出力ファイル
	 */
	final void analyzeFile(final String outputFileName,
			final String inputFileName) {

		// ファイルの中身を形態素リストに変換する
		List<Morpheme> morphemes = this.mla.getMorphemes(inputFileName);

		// 形態素階層化N-Gram解析を行う.
		List<MNElement> results = new ArrayList<MNElement>();
		this.analyze(results, morphemes);

		try {
			// 出力ファイルオープン
			File outputFile = new File(outputFileName);
			FileWriter fw = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(fw);

			for (MNElement elm : results) {
				// 抽出された形態素N-Gram要素毎に

				double v = elm.getScore();
				int n = elm.getNum();
				int g = elm.getNumOfGram();
				String surface = elm.getSurface();

				// 無理矢理なヒューリスティク処理なので要見直し (2019.01.18)
				if (surface.matches(".*[0-9].*")) {
					continue;
				}
				if (surface.matches(".*。.*")) {
					continue;
				}
				if (surface.matches("^、.*")) {
					continue;
				}
				// ここまで (2019.01.18 追加)

				String outputStr = "\"" + surface + "\",";
				outputStr = outputStr + g + ",";
				outputStr = outputStr + n + ",";
				outputStr = outputStr + v;
				System.out.println(outputStr);

				bw.write(outputStr);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 形態素階層化 N-Gram解析を行う.
	 *
	 * @param results
	 *            重要度の高い形態素N-Gramを追加するリスト
	 * @param morphemes
	 *            形態素のリスト
	 */
	final void analyze(final List<MNElement> results,
			final List<Morpheme> morphemes) {

		int length = morphemes.size(); // 文書長
		int max = maxN; // 最大グラム数
		if (length <= maxN) {
			// 文書長の方が短い場合
			max = length;
		}

		// グラム数毎に降順で処理する
		// 降順にするのは重要度調整を下方に伝播させるため
		MapMNGram targetMap = new MapMNGram();
		targetMap.generateMap(morphemes, max);
		MapMNGram parentMap;
		for (int i = max - 1; 1 <= i; i--) {
			// グラム数毎に降順で処理する

			parentMap = targetMap;
			targetMap = new MapMNGram();
			targetMap.generateMap(morphemes, i);

			// 包含関係の解析を行う
			targetMap.adjustInvolvement(parentMap);

			// 重要度指標値を算出する
			int chance = length - i + 1;
			targetMap.calcScore(chance);

			// 終端の品詞により、上位を調整する
			// ※ここが未だ上手くできていない
			targetMap.checkEndsPartOfSpeech(parentMap);

			// 品詞を用いて非重要形態素N-Gramを削除する
			targetMap.checkPartOfSpeech();

			if (max - 1 != i) {
				// 出力を１階層ずらして行っているので…
				parentMap.setResults(results);			// 結果をリストに返す
			}
		}
		// 出力を１階層ずらして行っているので…
		targetMap.setResults(results);

		return;
	}
}
