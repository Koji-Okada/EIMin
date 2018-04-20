package jp.ac.tcu.okadak.ei_mining.text_mining.textExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * テキスト抽出器 (Apache Tika を活用).
 *
 * @author K.Okada
 * @version 2018.04.20
 */
public class TikaTextExtractor {

	/**
	 * Aoache Tika のバッファサイズ.
	 */
	private static final int TIKA_BUFFER_SIZE = 1024 * 1024 * 1024;

	/**
	 * ファイルからテキストを抽出する(メイン).
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static final void main(final String[] args) {

		System.out.println("Start TextExtractor ...");

		TikaTextExtractor txtExtr = new TikaTextExtractor();
		txtExtr.multiDocExtract();

		System.out.println("... Successfully Complete.");

		return;
	}

	/**
	 * 構成設定ファイルで指定された複数ファイルからテキスト抽出を行う.
	 */
	final void multiDocExtract() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/TextExtractor.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			outputPath = br.readLine();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// ファイル名の一覧を取得する
		File file = new File(targetPath);
		File[] files = file.listFiles();

		for (File f : files) {
			// 各ファイルに対して

			if (!f.isFile()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			String fName = f.getName();
			System.out.println("====" + fName);

			String text = extract(targetPath + fName);

			// テキスト抽出結果を保存する
			try {
				File recFile = new File(outputPath + fName + ".txt");
				FileWriter filewriter = new FileWriter(recFile);
				filewriter.write(text);
				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * テキスト抽出を抽出する (Apache Tika を使用).
	 *
	 * @param fileName
	 *            テキスト抽出対象のファイル名
	 * @return 抽出されたテキスト
	 */
	public final String extract(final String fileName) {
		String str = "";
		try {
			Tika tika = new Tika();
			tika.setMaxStringLength(TIKA_BUFFER_SIZE); // バッファサイズ変更

			str = tika.parseToString(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}

		return str;
	}
}
