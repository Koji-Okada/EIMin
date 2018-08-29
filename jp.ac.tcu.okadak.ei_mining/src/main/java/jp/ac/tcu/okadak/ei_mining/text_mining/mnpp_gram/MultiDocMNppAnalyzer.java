package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 複数文書に対する形態素階層化N-Gram解析器.
 *
 * @author K.Okada
 * @version 2018.08.29
 */
public class MultiDocMNppAnalyzer {

	/**
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static final void main(final String[] args) {

		System.out.println("Start MultiDocMorphological N++Gram Analyzer ...");

		MultiDocMNppAnalyzer analyzer = new MultiDocMNppAnalyzer();
		analyzer.analyze();

		System.out.println("... Successfully Complete.");

		return;
	}

	/**
	 * 構成設定ファイルで指定された複数ファイルに対して形態素階層化N-Gram解析を行う.
	 */
	private void analyze() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/MNppGramAnalyzer.txt");
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

		// 形態素階層化N-Gram解析を生成する
		MNppAnalyzer mnppa = new MNppAnalyzer();

		for (File f : files) {
			// 各ファイルに対して

			if (!f.isFile()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			String fName = f.getName();
			System.out.println("====" + fName);

			// 形態素階層化N-Gram解析を行う

			mnppa.analyzeFile(outputPath + fName,
					targetPath + fName);
		}
	}
}
