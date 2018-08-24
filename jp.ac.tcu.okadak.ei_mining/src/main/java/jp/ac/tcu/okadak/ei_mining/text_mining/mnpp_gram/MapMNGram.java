package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.PoissonDistribution;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;

/**
 * 形態素N-Gram要素の検索用マップ.
 *
 * @author K.Okada
 * @version 2018.08.24
 */
public class MapMNGram {

	/**
	 * 形態素N-Gram要素の検索用マップ.
	 */
	private Map<String, MNElement> map = new HashMap<String, MNElement>();

	// =========================================================================
	/**
	 * 形態素のリストから、指定されたグラム数で検索用マップを作成する.
	 *
	 * @param morList	形態素のリスト
	 * @param n			グラム数
	 */
	final void generateMap(final List<Morpheme> morList, final int n) {

		Morpheme[] mors = (Morpheme[]) morList.toArray(new Morpheme[morList
				.size()]); // 処理軽量化のため配列化する

		for (int i = 0; i <= mors.length - n; i++) {

			MNElement elm = new MNElement(n); // 形態素N-Gram要素

			for (int j = 0; j < n; j++) {
				// N-Gram 要素を構成する
				elm.appendMorpheme(mors[i + j], j);
			}

			String idStr = elm.getIdStr(); // 識別子文字列

			MNElement registered = this.map.get(idStr);
			if (null == registered) {
				// 同一の N-Gram要素が無ければ、マップに追加する
				this.map.put(idStr, elm);
			} else {
				// 同一の N-Gram要素があれば、N-Gram要素をカウントアップする
				registered.countUp();
			}
		}
	}

	// =========================================================================
	/**
	 * 登録されている形態素の数を返す.
	 *
	 * @return 登録されている形態素N-Gram要素の数.
	 */
	public final int numOfMorpheme() {
		return this.map.size();
	}

	/**
	 * 登録されている形態素N-Gram要素を返す.
	 *
	 * @return 形態素N-Gram要素の集合
	 */
	final Collection<MNElement> getMNElements() {
		return this.map.values();
	}

	/**
	 * 独立性による補正計算を行う.
	 *
	 * @param parentMap		上位階層の形態素N-Gram要素のマップ
	 */
	final void adjustInvolvement(final MapMNGram parentMap) {

		Collection<MNElement> elms = parentMap.getMNElements();
		for (MNElement elm : elms) {
			// 上位階層の形態素N-Gram要素毎に

			String str = elm.getIdStr();
			int num = elm.getNum(); // 出現回数を求める

			String fwdString = elm.getForwardIdStr(); // 前方識別文字列を求める
			MNElement fwdElm = this.map.get(fwdString); // 前方形態素N-Gram要素を得る
			fwdElm.adjustBackword(num); // 前方形態素N-Gram要素の後方調整数を更新する

			String bwdString = elm.getBackwardIdStr(); // 後方識別文字列を求める
			MNElement bwdElm = this.map.get(bwdString); // 後方形態素N-Gram要素を得る
			bwdElm.adjustForword(num); // 後方形態素N-Gram要素の前方調整数を更新する
		}
		return;
	}

	/**
	 * 重要度指標値を算出する.
	 *
	 * @param chance	ポアソン分布算出用：
	 * 					確率×回数 の回数
	 */
	final void calcScore(final int chance) {

		int k = numOfMorpheme(); // N-Gram要素の語彙数

		// ポアソン分布を生成する
		double lamda = (double) chance / (double) k;
		PoissonDistribution poi = new PoissonDistribution(lamda);

		Collection<MNElement> elms = getMNElements();
		for (MNElement elm : elms) {
			double v = elm.calcScore(poi);	// ポアソン分布を基に重要度指標値を算出する
		}
		return;
	}
}
