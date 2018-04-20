package jp.ac.tcu.okadak.ei_mining.text_mining.n_gram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;

/**
 *
 * 共通語.
 *
 * @author K.Okada
 * @version 2016.01.24
 */
public class CommonWords {

	/**
	 * 共通語のマップ.
	 */
	private Map<String, String> commonWords = new HashMap<String, String>();

	/**
	 *
	 * コンストラクタ.
	 *
	 * @author K.Okada
	 * @version 2016.01.24
	 *
	 * @param dicName		共通語辞書のファイル名
	 *
	 */
	public CommonWords(final String dicName) {
		super();

		if (!dicName.equals("")) {
			// 共通語辞書が指定されている場合

			try {
				File dic = new File(dicName);
				BufferedReader br = new BufferedReader(new FileReader(dic));

				String str = br.readLine();

				while (null != (str = br.readLine())) {

					CSVTokenizer tknzr = new CSVTokenizer(str);
					String word = tknzr.nextToken();

					commonWords.put(word, word);
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * 共通語か否かを返す.
	 *
	 * @author K.Okada
	 * @version 2016.01.24
	 *
	 * @param word	用語
	 * @return		用語が共通語か否か
	 */
	final boolean isCommonWords(final String word) {

		return !this.commonWords.get(word).equals("");
	}
}
