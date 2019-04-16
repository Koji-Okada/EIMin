package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.PoissonDistribution;

import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.PartOfSpeech;

/**
 * 形態素N-Gram要素の検索用マップ.
 *
 * @author K.Okada
 * @version 2019.01.18
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
			double score = elm.calcScore(poi); // ポアソン分布を基に重要度指標値を算出する

			if (MNppAnalyzer.THRESHOLD > score) {
				// 重要度が判断閾値よりも低い場合
				elm.drop(); // 削除フラグを立てる
			}
		}
	}

	/**
	 * 形態素N-Gramの品詞をチェックする.
	 * (ヒューリスティックな処理)
	 * ストップワードのみの場合、削除フラグを立てる.
	 *
	 */
	final void checkPartOfSpeech() {

		Collection<MNElement> elms = getMNElements();
		for (MNElement elm : elms) {
			// 形態素N-Gram毎に
			boolean meaningful = false;
			Morpheme[] mors = elm.getMoroheme();
			for (Morpheme m : mors) {
				// 形態素毎に

				//				if (PartOfSpeech.OTHERS != m.getPartOfSpeech()) {
				//					// ストップワード以外を含む場合
				//					meaningful = true;	//意味ありフラグを立てる
				//				}

				// 名詞を含まない場合は捨てる (2019.01.17)
				if (PartOfSpeech.NORN == m.getPartOfSpeech()) {
					// 名詞を含む場合
					meaningful = true; //意味ありフラグを立てる
				}
			}
			if (!meaningful) {
				// 意味ありフラグが立っていない場合
				elm.drop(); // 削除フラグを立てる
			}
		}
		return;
	}

	/**
	 * 形態素N-Gramの両端の品詞をチェックする.
	 * (ヒューリスティックな処理)
	 *
	 *
	 */
	final void checkEndsPartOfSpeech(final MapMNGram parentMap) {

		Collection<MNElement> elms = parentMap.getMNElements();
		for (MNElement elm : elms) {
			// 上位階層の形態素N-Gram要素毎に

			int n = elm.getNumOfGram();
			Morpheme[] mors = elm.getMoroheme();

			int last = mors[n - 1].getPartOfSpeech();
			int last2 = mors[n - 2].getPartOfSpeech();

			if (((PartOfSpeech.OTHERS == last)
					&& (PartOfSpeech.OTHERS == last2))
					|| ((PartOfSpeech.OTHERS == last)
							&& (PartOfSpeech.NORN == last2))) {

				// ※重要度指標の伝播修正処理
				String fwdString = elm.getForwardIdStr(); // 前方識別文字列を求める
				MNElement fwdElm = this.map.get(fwdString); // 前方形態素N-Gram要素を得る
				double fwdScore = fwdElm.getScore();
				double score = elm.getScore();
				if (score > fwdScore) {
					fwdElm.replaceScore(score);
					//					System.out.println("* " + score + "->" + fwdScore);
				}
				//				System.out.println("* " +  elm.getSurface() + "is dropped!");
				elm.drop(); // 削除フラグを立てる
			}
			int first = mors[0].getPartOfSpeech();
			int second = mors[1].getPartOfSpeech();

			if (((PartOfSpeech.OTHERS == first)
					&& (PartOfSpeech.OTHERS == second))
					|| ((PartOfSpeech.OTHERS == first)
							&& (PartOfSpeech.NORN == second))) {

				// ※重要度指標の伝播修正処理
				String bwdString = elm.getBackwardIdStr(); // 後方識別文字列を求める
				MNElement bwdElm = this.map.get(bwdString); // 後方形態素N-Gram要素を得る
				double bwdScore = bwdElm.getScore();
				double score = elm.getScore();
				if (score > bwdScore) {
					bwdElm.replaceScore(score);
//					System.out.println("* " + score + "->" + bwdScore);
				}

				//				System.out.println("- " +  elm.getSurface() + "is dropped!");
				elm.drop(); // 削除フラグを立てる
			}
		}
		return;
	}

	/**
	 * 削除フラグに基づき結果を返す.
	 *
	 * @param results	結果
	 */
	final void setResults(final List<MNElement> results) {

		Collection<MNElement> elms = getMNElements();
		for (MNElement elm : elms) {

			if (!elm.isDrop()) {
				// 削除されていない場合

				results.add(elm); // リストに追加する
			}
		}

	}
}
