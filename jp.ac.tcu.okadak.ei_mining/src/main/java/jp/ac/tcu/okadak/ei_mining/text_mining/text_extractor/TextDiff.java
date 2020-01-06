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
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.IPADicMorphologicalAnalyzer;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.Morpheme;
import jp.ac.tcu.okadak.ei_mining.text_mining.morphological.PartOfSpeech;

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

		String path = "C:/Users/Okada/TextMining/TextFiles/";
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
		ArrayList<String> org = getLines(orginalFile);
		ArrayList<String> rev = getLines(revisedFile);

		System.out.println(" .. Check A.");

		// 差分を求める
		Patch<String> diff = DiffUtils.diff(org, rev);

		System.out.println(" .. Check B.");

		IPADicMorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();

		List<Delta<String>> deltas = diff.getDeltas();
		for (Delta<String> delta : deltas) {
			TYPE type = delta.getType();

			if (TYPE.INSERT == type) {
				// 挿入部分の場合

				Chunk<String> rc = delta.getRevised();
				List<String> rLines = rc.getLines();
				for (String ln:rLines) {
					System.out.println("*:" + ln);
				}
			} else if (TYPE.CHANGE == type) {
				// 変更部分の場合

				Chunk<String> oc = delta.getOriginal();
				List<String> oLines = oc.getLines();

				String oStrs = "";
				for (String ln:oLines) {
					oStrs = oStrs + ln;
				}

				Chunk<String> rc = delta.getRevised();
				List<String> rLines = rc.getLines();

				String rStrs = "";
				for (String ln:rLines) {
					rStrs = rStrs + ln;
				}

				List<Morpheme> oMor = new ArrayList<Morpheme>();
				mla.analyze(oMor, oStrs);
				String oSep = mla.getSurface(oMor, " ", PartOfSpeech.ALL);

				List<Morpheme> rMor = new ArrayList<Morpheme>();
				mla.analyze(rMor, rStrs);
				String rSep = mla.getSurface(rMor, " ", PartOfSpeech.ALL);

				System.out.println("-:" + oSep);
				System.out.println("+:" + rSep);


			}


//			System.out.println(type);
//			Chunk<String> oc = delta.getOriginal();
//			Chunk<String> rc = delta.getRevised();
//
//			//			if (rc.size() >= 8) {
//			System.out.printf("num=%d: position=%d, lines=%s%n", oc.size(), oc
//					.getPosition(), oc.getLines());
//			System.out.printf("num=%d: position=%d, lines=%s%n", rc.size(), rc
//					.getPosition(), rc.getLines());
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
	 * @return	文字列のリスト
	 */
	final ArrayList<String> getLines(final String filePath) {

		ArrayList<String> lines = new ArrayList<String>();

		try {
			File file = new File(filePath);

			BufferedReader br = new BufferedReader(new FileReader(file));

			TextFormatter form = new TextFormatter();
			String ln;
			while ((ln = br.readLine()) != null) {
				lines.add(ln);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

}
