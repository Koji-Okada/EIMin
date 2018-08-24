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
	 * 重要度判定指標値.
	 */
	static final double THRESHOLD = 0.8e0d;

	/**
	 * 形態素解析器.
	 */
//		private MorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();
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

//		for (MNElement elm:results) {
//			double v = elm.getScore();
//			String idStr = elm.getIdStr();
//			String surface = elm.getSurface();
//
//			System.out.println(v + ":" + idStr);
//			System.out.println(v + ":" + surface);
//		}
		return;
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
		List<MNElement> results = new ArrayList<MNElement>();
		this.analyze(results, morphemes);

		for (MNElement elm:results) {
			double v = elm.getScore();
//			String idStr = elm.getIdStr();
			String surface = elm.getSurface();

//			System.out.println(v + ":" + idStr);
			System.out.println(v + ":" + surface);
		}
		return;
	}

	/**
	 * 形態素階層化 N-Gram解析を行う.
	 *
	 * @param results 重要度の高い形態素N-Gramを追加するリスト
	 * @param morphemes 形態素のリスト
	 */
	final void analyze(final List<MNElement> results,
			final List<Morpheme> morphemes) {

		int length = morphemes.size(); // 文書長
		int max = maxN; // 最大グラム数
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

			targetMap.calcScore(results, chance);
		}
		return;
	}
}
