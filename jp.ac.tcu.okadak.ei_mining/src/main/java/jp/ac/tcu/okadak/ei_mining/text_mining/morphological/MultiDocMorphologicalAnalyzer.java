package jp.ac.tcu.okadak.ei_mining.text_mining.morphological;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 複数文書に対する形態素解析器.
 *
 * @author K.Okada
 * @version 2019.12.28
 */
public class MultiDocMorphologicalAnalyzer {

	/**
	 * 解析モード. デフォルトは「分かち書き」
	 */
	private int mode = PartOfSpeech.ALL;

	/**
	 * 解析モードを設定する.
	 *
	 * @param m
	 *            -1 : 分かち書き 正値: 出力品詞指定
	 */
	final void setMode(final int m) {
		this.mode = m;
		return;
	}

	/**
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static final void main(final String[] args) {

		System.out.println("Start MultiDocMorphologicalAnalyzer ...");

		MultiDocMorphologicalAnalyzer mdpa = new MultiDocMorphologicalAnalyzer();

//		mdpa.setMode(PartOfSpeech.ALL);
		mdpa.setMode(PartOfSpeech.NORN | PartOfSpeech.VERB | PartOfSpeech.ADJ
				| PartOfSpeech.ADV);
		mdpa.analyze();

		System.out.println("... Successfully Complete.");

		return;
	}

	/**
	 * 構成設定ファイルで指定された複数ファイルに対して形態素解析を行う.
	 */
	private void analyze() {

		String targetPath;
		String outputPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/MorphologicalAnalyzer.txt");
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

		// 形態素解析を生成する
//		MorphologicalAnalyzer mla = new IPADicMorphologicalAnalyzer();
		MorphologicalAnalyzer mla = new NeologdMorphologicalAnalyzer();

		for (File f : files) {
			// 各ファイルに対して

			if (!f.isFile()) {
				// ファイルの場合のみ処理対象とする
				continue;
			}

			String fName = f.getName();
			System.out.println("====" + fName);

			// 形態素解析を行う

			mla.analyzeFile(outputPath + fName, targetPath + fName, this.mode);
		}
	}
}
