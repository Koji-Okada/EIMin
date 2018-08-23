package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.util.List;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.MorphologicalAnalyzer;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.NeologdMorphologicalAnalyzer;

/**
 * 形態素階層化N-Gram解析器.
 *
 * @author K.Okada
 * @version 2018.08.23
 */
public class MNppAnalyzer {

	/**
	 * 形態素解析器.
	 */
	//	private MorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();
	private MorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

	/**
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		System.out.println("Start Morphlogical N++Gram.");

		//		String testStr = "歌を歌う。";
		String testStr = "かえるがかえる。　かえるがおよぐ。";

		MNppAnalyzer mnpp = new MNppAnalyzer();
		mnpp.analyze(testStr);

		System.out.println("... Fin.");
	}

	/**
	 * 形態素 N-Gram解析を行う.
	 *
	 * @param txt	対象テキスト
	 */
	public final void analyze(final String txt) {

		List<Morpheme> list = this.mla.analyze(txt);

		System.out.println("1-Gram ...");
		MapMNGram map1 = new MapMNGram();
		map1.add(list, 1);
		System.out.println(map1.numOfMorpheme());

		System.out.println("2-Gram ...");
		MapMNGram map2 = new MapMNGram();
		map2.add(list, 2);
		System.out.println(map2.numOfMorpheme());

		System.out.println("3-Gram ...");
		MapMNGram map3 = new MapMNGram();
		map3.add(list, 3);
		System.out.println(map3.numOfMorpheme());

	}

}
