package jp.ac.tcu.okadak.ei_mining.text_mining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * N-Gram 分析器.
 *
 *
 * @author K.Okada
 * @version 2016.08.18
 *
 */
public class NGramAnalyzer {

	/**
	 * 結果出力のための優先順位評点の閾値.
	 */
	private final double scoreThreshold = 0.5e0;


	/**
	 * N-Gram ハッシュマップ.
	 */
	private Map<String, NGramData> nGramMap = new HashMap<String, NGramData>();

	/**
	 * N-Gram毎の単語種類数.
	 * 	(nTermType[i-1]に i-Gramの単語種類数)
	 */
	private int[] nTermType;

	/**
	 * N-Gram毎のポアソン分布等の統計値.
	 */
	private StatisticsValues sValue = new StatisticsValues();

	/**
	 *
	 * N-Gram 分析を行う.
	 *
	 * @author K.Okada
	 * @version 2016.08.18
	 *
	 * @param targetDoc		分析対象の文書
	 * @param maxN			N-Gram の上限文字数
	 * @return				解析結果
	 */
	final String analyze(final String targetDoc, final int maxN) {

		// 対象文書中の文字種数を求める.
		int numChrTyp = countNumOfChr(targetDoc);

		// N-Gram ハッシュマップを生成する.
		generateNGramMap(targetDoc, maxN);

		// 統計値を算出する.
		sValue.initialize(targetDoc.length(), maxN, this.nTermType);

		// N-Gram ハッシュマップを整理統合する.
		consolidateNGram(targetDoc.length());

		// N-Gram ハッシュマップをソーティングする.
		List<Entry<String, NGramData>> entries = sortNGram();

		// ソート結果を表示する.
		String results = "";
		int count = 0;

		for (Entry<String, NGramData> en : entries) {

			if (this.scoreThreshold >= en.getValue().getScore()) {
				// 閾値を以下となったら出力を止める
				break;
			}

			results = results + (++count) + ",\"" + en.getKey() + "\"," + en
					.getValue().getNum() + "," + en.getValue().getScore()
					+ "\r\n";
		}

		return results;
	}

	/**
	 *
	 *  文字種数を算出する.
	 *
	 * @author K.Okada
	 * @version 2016.01.09
	 *
	 * @param doc			文章
	 * @return				文字種数
	 */
	private int countNumOfChr(final String doc) {

		Map<String, String> chrsMap = new HashMap<String, String>();

		int dLen = doc.length();

		for (int i = 0; i <= dLen - 1; i++) {
			String str = doc.substring(i, i + 1);

			String data = chrsMap.get(str);
			if (null == data) {
				// エントリーを新規に作成する
				chrsMap.put(str, str);
			}
		}

		return chrsMap.size();
	}

	/**
	 *
	 * N-Gram ハッシュマップ を作成する.
	 *
	 * @author K.Okada
	 * @version 2016.08.18
	 *
	 * @param orgDoc		分析対象の文書
	 * @param maxN			N-Gram の上限文字数
	 */
	private void generateNGramMap(final String orgDoc, final int maxN) {

		int orgDocLen = orgDoc.length();
		int targetDocLen = orgDocLen + 2;
		String targetDoc = "*" + orgDoc + "*"; // 前後に 1文字追加

		int max = maxN;
		if (orgDocLen < maxN) {
			// 文字列長が短い場合の対応
			max = orgDocLen;
		}

		this.nTermType = new int[max + 1];

		// maxN に +1 するのは、独立性を算出するため 1文字長い文字列で
		for (int n = 2; n <= max + 1; n++) {

			this.nTermType[n - 1] = 0;

			for (int i = 0; i <= targetDocLen - n; i++) {
				boolean fwdEd = false;
				if (0 == i) {
					fwdEd = true;
				}
				boolean bwdEd = false;
				if ((targetDocLen - n) == i) {
					fwdEd = true;
				}

				String str = targetDoc.substring(i, i + n);

				NGramData data = this.nGramMap.get(str);
				if (null == data) {
					// エントリーを新規に作成する
					this.nGramMap.put(str, new NGramData(n, fwdEd, bwdEd));

					this.nTermType[n - 1]++;
				} else {
					// 既存のエントリーを更新する
					data.increase();
				}
			}
			//	System.out.println(nTermType[n - 1]);
		}
		return;
	}

	/**
	 *
	 * N-Gram ハッシュマップを整理統合する.
	 *
	 * @author K.Okada
	 * @version 2016.08.18
	 *
	 * @param dLen			文字列長
	 */
	private void consolidateNGram(final int dLen) {

		// 調整値を算出する.
		for (Map.Entry<String, NGramData> e : this.nGramMap.entrySet()) {

			String str = e.getKey();
			int num = e.getValue().getNum();

			String fwdStr = str.substring(0, str.length() - 1);
			NGramData fwd = this.nGramMap.get(fwdStr);
			if (null != fwd) {
				fwd.fwdAdjust(num);
			}

			String bwdStr = str.substring(1, str.length());
			NGramData bwd = this.nGramMap.get(bwdStr);
			if (null != bwd) {
				bwd.bwdAdjust(num);
			}
		}

		// スコア計算を行う.
		for (Map.Entry<String, NGramData> e : this.nGramMap.entrySet()) {
			e.getValue().calcScore(this.sValue);
		}

		// 不要な要素を削除する.
		for (Iterator<Map.Entry<String, NGramData>> it = this.nGramMap
				.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, NGramData> en = it.next();
			if (en.getValue().isDrop()) {
				it.remove();
			} else if (en.getKey().matches("^[0-9.,]*$")) {
				// 数値だけの要素を削除する.
				// (ヒューリスティクス処理)
				it.remove();
			} else if (en.getKey().matches("^[、。，．,.].*$")) {
				// 句読点から始まる要素を削除する.
				// (ヒューリスティクス処理)
				it.remove();
			}
		}

		return;
	}

	/**
	 *
	 * N-Gram ハッシュマップ をソーティングする.
	 *
	 * @author K.Okada
	 * @version 2016.08.18
	 *
	 * @return				ソーティング結果 (リスト構造)
	 */
	private List<Entry<String, NGramData>> sortNGram() {

		List<Entry<String, NGramData>> entries = new ArrayList<Map.Entry<String, NGramData>>(
				this.nGramMap.entrySet());

		Collections.sort(entries,
				new Comparator<Map.Entry<String, NGramData>>() {
					public int compare(final Entry<String, NGramData> e1,
							final Entry<String, NGramData> e2) {
						return ((NGramData) e2.getValue()).compareTo(
								(NGramData) e1.getValue());
					}
				});

		return entries;
	}
}
