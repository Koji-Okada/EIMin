package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;

/**
 * 形態素N-Gram要素の検索用マップ.
 *
 * @author K.Okada
 * @version 2018.08.23
 */
public class MapMNGram {

	/**
	 * 形態素N-Gram要素の検索用マップ.
	 */
	private Map<String, MNElement> map = new HashMap<String, MNElement>();

	/**
	 * 形態素のリストから、指定されたグラム数で検索用マップを作成する.
	 *
	 * @param morList	形態素のリスト
	 * @param n			グラム数
	 */
	public final void add(final List<Morpheme> morList, final int n) {

		Morpheme[] mors = (Morpheme[]) morList.toArray(new Morpheme[morList
				.size()]); // 処理軽量化のため配列化する

		for (int i = 0; i <= mors.length - n; i++) {

			String idStr = ""; // 識別子文字列
			MNElement elm = new MNElement(n); // 形態素N-Gram要素


			for (int j = 0; j < n; j++) {
				// N-Gram 要素を構成する
				elm.appendMorpheme(mors[i + j], j);
				idStr = idStr + mors[i + j].getIdStr() + ",";
			}
			System.out.println("- " + idStr);

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

	/**
	 * 登録されている形態素の数を返す.
	 *
	 * @return 登録されている形態素の数.
	 */
	public final int numOfMorpheme() {
		return this.map.size();
	}
}
