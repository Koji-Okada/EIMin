package jp.ac.tcu.okadak.ei_mining.text_mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;

/**
 *
 * @author K.Okada
 * @version 2016.08.02
 */

public class NoiseFilter {

	public static void main(final String[] args) {

		final String targetDir = "C:/Users/okada_kouji/Documents/NGram/NoiseReduction/Org/";
		final String recordDir = "C:/Users/okada_kouji/Documents/NGram/NoiseReduction/Res/";
		final String dicDir = "C:/Users/okada_kouji/Documents/NGram/NoiseReduction/";
		final String commonDic = "dic.txt";

		// 共通語を登録
		Map<String, String> commonWords = new HashMap<String, String>();

		try {
			File tgtFile = new File(dicDir + commonDic);
			BufferedReader br = new BufferedReader(new FileReader(tgtFile));

			String str;
			while (null != (str = br.readLine())) {

				CSVTokenizer tknzr = new CSVTokenizer(str);
				String word = tknzr.nextToken();
				commonWords.put(word, word);

				//				System.out.println(word);
			}
			br.close();

			// ファイル名の一覧を取得する
			File file = new File(targetDir);
			File[] files = file.listFiles();

			for (int i = 0; i < files.length; i++) {

				if (files[i].isDirectory()) {
					// ファイルの場合のみ処理対象とする
					continue;
				}

				System.out.println(files[i].getName());

				String outs = "";

				br = new BufferedReader(new FileReader(files[i]));
				while (null != (str = br.readLine())) {

					CSVTokenizer tknzr = new CSVTokenizer(str);
					String term = tknzr.nextToken();
					term = tknzr.nextToken();

					//					System.out.println("** " + term);

					if (null == commonWords.get(term)) {
						outs = outs + str + "\r\n";
					}
				}

				File recFile = new File(recordDir + files[i].getName());
				FileWriter filewriter = new FileWriter(recFile);

				filewriter.write(outs);

				filewriter.close();
			}
			System.out.println("... Successfully Complete !!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
