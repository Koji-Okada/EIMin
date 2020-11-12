package jp.ac.tcu.okadak.ei_mining.word2vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * 複数文書の Word2Vec分析器.
 *
 * @author K.Okada
 * @version 2018.05.30
 */
public class MultiDocW2VAnalyzer {

	/**
	 * 複数文書の Word2Vec分析を行う(メイン).
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		System.out.println("Start Word2VecAnalyzer ...");

		MultiDocW2VAnalyzer mda = new MultiDocW2VAnalyzer();
		mda.analyze();

		System.out.println("... Successfully Complete.");

		return;
	}

	/**
	 *
	 */
	private void analyze() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/Word2VecAnalyzer.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			targetPath = br.readLine();
			outputPath = br.readLine();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}


		// Word2Vec分析器を生成する
		Word2VecAnalyzer w2v = new Word2VecAnalyzer();
		w2v.initialize();

		// ファイル名の一覧を取得する
		File file = new File(targetPath);
		File[] files = file.listFiles();

		for (File f : files) {
			// 各ファイルに対して

			System.out.println("..." + f);

			if (!f.isFile()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			// 対象ファイルを分析する
			w2v.analyze(f);

			System.out.println("...!");

		}

		// 分析結果を保存する
		w2v.save(new File(outputPath + "/words.txt"));

		return;
	}
}
