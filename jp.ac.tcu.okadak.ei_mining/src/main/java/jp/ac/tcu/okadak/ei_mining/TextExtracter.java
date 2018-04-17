package jp.ac.tcu.okadak.ei_mining;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 *
 * テキスト抽出器  (Apache Tika を活用).
 *
 * @author	K.Okada
 * @version	2016.01.05
 *
 */
public class TextExtracter {

	/**
	 *
	 * テキスト抽出を抽出する (Apache Tika を使用).
	 *
	 * @author K.Okada
	 * @version 2015.12.28
	 *
	 * @param fileName		テキスト抽出対象ファイルの名称
	 * @return				抽出されたテキスト文字列
	 */
	final String extract(final String fileName) {
		String str = "";
		try {
			Tika tika = new Tika();
			str = tika.parseToString(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}

		return str;
	}


	/**
	 *
	 * テキストを洗練する.
	 *
	 * @author K.Okada
	 * @version 2016.01.05
	 *
	 * @param orgStr		洗練前の文字列
	 * @return				洗練後の文字列
	 */
	final String cleanUp(final String orgStr) {

		String str1 = orgStr.replaceAll("\r", "");
		String str2 = str1.replaceAll("\n", "");
		String str3 = str2.replaceAll(" ", "");
		String cleanStr = str3.replaceAll("\t", "");

		return cleanStr;
	}


}
