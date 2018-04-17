package jp.ac.tcu.okadak.ei_mining.text_mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;

/**
 *
 * 共通語の抽出器.
 *
 * @author K.Okada
 * @version 2016.01.24
 *
 */
public class CommonWordsExtractor {

	/**
	 * 共通語と識別する閾値.
	 */
	private final double threshold = 0.3e0;
	//	private final double threshold = 0.5e0;

	/**
	 *
	 * 共通語を抽出する(メイン).
	 *
	 * @author K.Okada
	 * @version 2016.01.22
	 *
	 * @param args			ダミー引数
	 */
	public static void main(final String[] args) {

		final String targetDir = "C:/Users/okada_kouji/Documents/NGram/Target/";
		final String recordDir = targetDir + "Results/";
		final String target = "all.txt";
		final String commonDic = "dic.txt";

		System.out.println("Start ... Creating common words dictionary.");

		CommonWordsExtractor cmExr = new CommonWordsExtractor();
		cmExr.countCommons(recordDir + target);
		cmExr.recordCommons(recordDir + commonDic);

		System.out.println(" ... Fin.");
	}

	/**
	 *
	 * 用語共通数および母数となる文書数をカウントする.
	 *
	 * @author K.Okada
	 * @version 2016.01.22
	 *
	 * @param target		共通語抽出処理の対象となるファイル.
	 */
	private void countCommons(final String target) {

		try {
			File tgtFile = new File(target);
			BufferedReader br = new BufferedReader(new FileReader(tgtFile));

			String str = br.readLine();
			System.out.println(str);
			if (str.equals("DOC,COUNT,TERM,FREQ,SCORE")) {
				// 先頭行でファイル互換性を確認.

				while (null != (str = br.readLine())) {

					CSVTokenizer tknzr = new CSVTokenizer(str);
					String doc = tknzr.nextToken();
					tknzr.nextToken();
					String term = tknzr.nextToken();

					this.countDocs(doc); // 文書数をカウントする.
					this.countTerms(term); // 用語共通数をカウントする.

					System.out.println(doc + ":" + term);
				}

				System.out.println("Num of Docs :" + this.numOfDocs);
			} else {
				System.out.println("Data Format was mismatch.");
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 *
	 * 共通語を記録する.
	 *
	 * @author K.Okada
	 * @version 2016.08.02
	 * @version 2016.01.22
	 *
	 * @param record		共通語を記録するファイル名
	 */
	private void recordCommons(final String record) {

		try {
			List<Entry<String, Integer>> entries = sortCommons();

			// 結果を記録する

			File recFile = new File(record);
			FileWriter filewriter = new FileWriter(recFile);

			for (Entry<String, Integer> en : entries) {
				String str = "";

				String term = en.getKey();
				int frq = en.getValue().intValue();

				double rate = (double) frq / (double) this.numOfDocs;

				if (threshold <= rate) {
					str = str + "\"" + term + "\"," + rate + "\r\n";
//				} else if (1 == frq) { // 後できちんと整理
//					str = str + "\"" + term + "\"," + 0.0e0 + "\r\n";
				}
				filewriter.write(str);
				System.out.println(str);

			}

			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 用語のマップ.
	 */
	private Map<String, Integer> termMap = new HashMap<String, Integer>();

	/**
	 *
	 * 用語共通数(用語の出現する文書数)をカウントする.
	 *
	 * @author K.Okada
	 * @version 2016.01.17
	 *
	 * @param str			用語
	 * @return 				用語の出現する文書数
	 */
	private int countTerms(final String str) {

		int c;
		if (null == this.termMap.get(str)) {
			c = 1;
		} else {
			c = this.termMap.get(str).intValue() + 1;
		}
		this.termMap.put(str, new Integer(c));
		return c;
	}

	/**
	 * 文書のマップ.
	 */
	private Map<String, Integer> docMap = new HashMap<String, Integer>();

	/**
	 * 文書数.
	 */
	private int numOfDocs = 0;

	/**
	 *
	 * 文書数をカウントする.
	 *
	 * @author K.Okada
	 * @version 2016.01.17
	 *
	 * @param str			文書名
	 * @return 				文書数
	 */
	private int countDocs(final String str) {

		if (null == this.docMap.get(str)) {
			this.docMap.put(str, new Integer(++this.numOfDocs));
		}
		return this.numOfDocs;
	}

	/**
	 *
	 * 共通語をソートする.
	 *
	 * @author K.Okada
	 * @version 2016.01.19
	 *
	 * @return ソートされた用語のリスト
	 */
	private List<Entry<String, Integer>> sortCommons() {

		List<Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(
				this.termMap.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(final Entry<String, Integer> e1,
					final Entry<String, Integer> e2) {
				return ((Integer) e2.getValue()).compareTo((Integer) e1
						.getValue());
			}
		});

		return entries;
	}
}
