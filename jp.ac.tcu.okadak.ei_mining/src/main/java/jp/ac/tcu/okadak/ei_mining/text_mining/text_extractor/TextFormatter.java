package jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * テキストファイルフォーマッタ.
 *
 * @author K.Okada
 * @version 2020.01.05
 */
public class TextFormatter {

	/**
	 * テキストファイルから全文を読込む.
	 *
	 * @param filePath	テキストファイルのファイルパス
	 * @return			全文文字列
	 */
	public final String readAll(final String filePath) {

		StringBuilder builder = new StringBuilder();

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();
			while (null != str) {
				builder.append(str + "\r\n");
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
	 * @param orgStr	元の文字列
	 * @return			空白文字除去後の文字列
	 */
	public final String removeWhiteSpace(final String orgStr) {

		// 日本語文中の空白文字の処理(前方から)
		String str1 = orgStr.replaceAll("([^0-9a-zA-Z])\r", "$1");
		String str2 = str1.replaceAll("([^0-9a-zA-Z])\n", "$1");
		String str3 = str2.replaceAll("([^0-9a-zA-Z])\t", "$1");
		String str4 = str3.replaceAll("([^0-9a-zA-Z]) ", "$1");
		// 日本語文中の空白文字の処理(後方から)
		String str5 = str4.replaceAll("\n([^0-9a-zA-Z])", "$1");
		String str6 = str5.replaceAll("\r([^0-9a-zA-Z])", "$1");
		String str7 = str6.replaceAll("([^0-9a-zA-Z])\t", "$1");
		String str8 = str7.replaceAll("([^0-9a-zA-Z]) ", "$1");

		// 残った改行、タブの処理
		String str9 = str8.replaceAll("\r", " ");
		String str10 = str9.replaceAll("\n", " ");
		String newStr = str10.replaceAll("\t", " ");

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

		// 日本語文中の文分割
		String str1 = orgStr.replaceAll("。", "。\r\n");
		String str2 = str1.replaceAll("．", "．\r\n");

		// 英語の文分割
		String newStr = str2.replaceAll("\\.([^0-9])", "\\.\r\n$1");

		return newStr;
	}

	/**
	 * 英文部分を削除する.
	 *
	 * @param orgStr	元の文字列
	 * @return			削除後の文字列
	 */
	public final String eliminateEnglishSentece(final String orgStr) {

		BufferedReader bf = new BufferedReader(new StringReader(orgStr));

		StringBuilder sb = new StringBuilder();
		String ln;

		Pattern p = Pattern.compile("^\\p{ASCII}*$");
		try {
			while (null != (ln = bf.readLine())) {

				// 半角文字のみで構成される行を検索
				Matcher m = p.matcher(ln);
				if (!m.find()) {
					// 半角のみではない行の場合
					sb.append(ln + "\r\n");
				}
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String newStr = sb.toString();

		return newStr;
	}

	/**
	 * HTMLタグを削除する.
	 *
	 * @param orgStr	元の文字列
	 * @return			削除後の文字列
	 */
	public final String eliminateHTMLTags(final String orgStr) {

		String str1 = orgStr.replaceAll("<.*?>", "");

		return str1;
	}

	
	public static void main(final String[] args) {

		TextFormatter form = new TextFormatter();

		String org = "<h3>見出し</h3>テキスト<br>。\r\n";

		String s = form.eliminateHTMLTags(org);

		System.out.println(s);
	}

}
