package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

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
 * @version 2018.08.24
 */
public class MNppAnalyzer {

	/**
	 * 形態素解析器.
	 */
	//	private MorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();
	private MorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

	/**
	 * 最大グラム数.
	 */
	private int maxN = 16;

	/**
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		System.out.println("Start Morphlogical N++Gram.");

		MNppAnalyzer mna = new MNppAnalyzer();
		mna.process();

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

		this.analyze(morphemes);

	}

	/**
	 * ファイルに対して形態素階層化 N-Gram解析を行う.
	 *
	 * @param outputFileName	入力ファイル
	 * @param inputFileName	解析結果出力ファイル
	 */
	final void analyzeFile(final String outputFileName,
			final String inputFileName) {

		// ファイルの中身を形態素リストに変換する
		List<Morpheme> morphemes = this.mla.getMorphemes(inputFileName);

		// 形態素階層化 N-Gram解析を行う.
		this.analyze(morphemes);

		return;
	}

	/**
	 * 形態素階層化 N-Gram解析を行う.
	 *
	 * @param morphemes 形態素のリスト
	 */
	final void analyze(final List<Morpheme> morphemes) {

		int length = morphemes.size();	// 文書長
		int max = maxN;					// 最大グラム数
		if (length <= maxN) {
			// 文書長の方が短い場合
			max = length;
		}

		MapMNGram parentMap = new MapMNGram();
		parentMap.generateMap(morphemes, 1);

		MapMNGram targetMap;
		for (int i = 1; i < max; i++) {
			targetMap = parentMap;
			parentMap = new MapMNGram();
			parentMap.generateMap(morphemes, i + 1);

			// 包含関係の解析を行う
			targetMap.adjustInvolvement(parentMap);

			int chance = length - i + 1;

			targetMap.calcScore(chance);

		}
		return;
	}
}
