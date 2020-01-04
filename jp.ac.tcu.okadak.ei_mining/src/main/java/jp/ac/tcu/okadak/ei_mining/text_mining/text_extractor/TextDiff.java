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
 * @version 2019.12.28
 */
public class TextDiff {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

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
	 */
	void analyze(String revFile, String orgFile) {

		ArrayList org = getLines(orgFile);
		ArrayList rev = getLines(revFile);
		Patch<String> diff = DiffUtils.diff(org, rev);

		List<Delta<String>> deltas = diff.getDeltas();
		for (Delta<String> delta : deltas) {
			TYPE type = delta.getType();
//			System.out.println(type);
			Chunk<String> oc = delta.getOriginal();
			Chunk<String> rc = delta.getRevised();

			if (rc.size() >= 8) {
				System.out.printf("num=%d: position=%d, lines=%s%n", rc.size(), rc.getPosition(), rc.getLines());
			}

//			System.out.printf("del: position=%d, lines=%s%n", oc.getPosition(), oc.getLines());
//			System.out.println(oc.size());
//			System.out.printf("add: position=%d, lines=%s%n", rc.getPosition(), rc.getLines());
//			System.out.println(rc.size());
		}

		return;
	}

	/**
	 *
	 * @param filePath
	 * @return
	 */
	ArrayList<String> getLines(String filePath) {

		ArrayList<String> lines = new ArrayList<String>();

		try {
			File file = new File(filePath);

			BufferedReader br = new BufferedReader(new FileReader(file));

			String ln;
			while ((ln = br.readLine()) != null) {

				lines.add(ln);
//				System.out.println(ln);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

}
