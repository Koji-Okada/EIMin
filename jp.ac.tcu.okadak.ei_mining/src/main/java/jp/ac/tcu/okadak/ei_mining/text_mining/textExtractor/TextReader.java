package jp.ac.tcu.okadak.ei_mining.text_mining.textExtractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * テキストファイルリーダー.
 *
 * @author Okada
 * @version 2018.04.20
 */
public class TextReader {

	/**
	 * テキストファイルから全文を読込む.
	 *
	 * @param filePath
	 *            テキストファイルのファイルパス
	 * @return 全文文字列
	 */
	public final String readAll(final String filePath) {

		StringBuilder builder = new StringBuilder();

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();
			while (null != str) {
				builder.append(str + System.getProperty("line.separator"));
				str = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

	/**
	 * 空白文字を取り除く.
	 *
	 * @param orgStr
	 *            元の文字列
	 * @return 空白文字除去後の文字列
	 */
	public final String removeWhiteSpace(final String orgStr) {

		String str1 = orgStr.replaceAll("\r", "");
		String str2 = str1.replaceAll("\n", "");
		String str3 = str2.replaceAll(" ", "");
		String newStr = str3.replaceAll("\t", "");

		return newStr;
	}

	/**
	 * 文単位に分割する.
	 *
	 * @param orgStr
	 *            元の文字列
	 * @return 分割後の文字列
	 */
	public final String dividSentence(final String orgStr) {

		String str1 = orgStr.replaceAll("。", orgStr);
		String newStr = orgStr.replaceAll("．", str1);

		return newStr;
	}
}
