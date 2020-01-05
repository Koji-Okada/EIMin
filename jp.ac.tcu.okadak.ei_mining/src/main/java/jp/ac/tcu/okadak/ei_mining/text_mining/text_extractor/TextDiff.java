package jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import difflib.Chunk;
import difflib.Delta;
import difflib.Delta.TYPE;
import difflib.DiffUtils;
import difflib.Patch;

/**
 *
 *
 * @author K.Okada
 * @version 2020.01.05
 */
public class TextDiff {

	/**
	 *
	 * @param args
	 */
	public static void main(final String[] args) {

		System.out.println("Start ...");

		TextDiff td = new TextDiff();

		String path = "C:/Users/Okada/TextMining/MorphologicalFiles/";
		String originalFilePath = "YH(東京急行電鉄)2017.pdf.txt";
		String revisedFilePath = "YH(東京急行電鉄)2018.pdf.txt";

		td.analyze(path + revisedFilePath, path + originalFilePath);

		System.out.println("... Fin.");

		return;
	}

	/**
	 *
	 * @param revisedFile	更新後ファイルのファイルパス
	 * @param orginalFile	更新前ファイルのファイルパス
	 */
	final void analyze(final String revisedFile, final String orginalFile) {

		// ファイルから文字列を取得する
		ArrayList<String> org = getLines(orginalFile, true);
		ArrayList<String> rev = getLines(revisedFile, false);

		System.out.println(" .. Check A.");

		// 差分を求める
		Patch<String> diff = DiffUtils.diff(org, rev);

		System.out.println(" .. Check B.");

		List<Delta<String>> deltas = diff.getDeltas();
		for (Delta<String> delta : deltas) {
			TYPE type = delta.getType();
			System.out.println(type);
			Chunk<String> oc = delta.getOriginal();
			Chunk<String> rc = delta.getRevised();

			//			if (rc.size() >= 8) {
			System.out.printf("num=%d: position=%d, lines=%s%n", oc.size(), oc
					.getPosition(), oc.getLines());
			System.out.printf("num=%d: position=%d, lines=%s%n", rc.size(), rc
					.getPosition(), rc.getLines());
					//			}

			//			System.out.printf("del: position=%d, lines=%s%n", oc.getPosition(), oc.getLines());
			//			System.out.println(oc.size());
			//			System.out.printf("add: position=%d, lines=%s%n", rc.getPosition(), rc.getLines());
			//			System.out.println(rc.size());
		}

		return;
	}

	/**
	 * ファイルから 1行毎の文字列のリストを作成する.
	 *
	 * @param filePath	ファイルパス
	 * @param org		更新前ファイルの場合 true
	 * @return	文字列のリスト
	 */
	final ArrayList<String> getLines(final String filePath, boolean org) {

		ArrayList<String> lines = new ArrayList<String>();

		try {
			File file = new File(filePath);

			BufferedReader br = new BufferedReader(new FileReader(file));

			TextFormatter form = new TextFormatter();
			String ln;
			while ((ln = br.readLine()) != null) {

				if (org) {
					// 行頭、行末のタグを除去する
					ln = form.eliminateHTMLTags(ln);
				}

				lines.add(ln);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

}
