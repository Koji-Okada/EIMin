package jp.ac.tcu.okadak.ei_mining.word2vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

/**
 * Word2Vecの応用 (テスト的クラス).
 *
 * @author K.Okada
 * @version 2018.05.30
 */
public class Word2VecApplication {

	/**
	 * Word2Vec分析結果.
	 */
	private Word2Vec vec;

	/**
	 *
	 * @param args
	 *            デフォルト
	 */
	public static void main(final String[] args) {

		Word2VecApplication w2vApp = new Word2VecApplication();
		w2vApp.apply();

		return;
	}

	/**
	 * 適用する.
	 */
	private void apply() {

		String targetPath;

		// 設定ファイルから入出力パスを取得する
		try {
			File confFile = new File("conf/Word2VecAnalyzer.txt");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);

			br.readLine();
			targetPath = br.readLine();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// 単語ベクトルの読込
		File file = new File(targetPath + "/words.txt");

		try {
			vec = (Word2Vec) WordVectorSerializer.loadTxtVectors(file);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}



		// ==== 評価値を出力する ====

		// 類似度
		String word1 = "プロジェクト";
		String word2 = "シミュレーション";
		double similarity = vec.similarity(word1, word2);
		System.out.println(
				String.format("[%s] - [%s] = %f", word1, word2, similarity));

		System.out.println();

		// 類似ワード
		String word;
		int ranking;
		Collection<String> similarTop;

		word = "プロジェクト";
		ranking = 20;
		similarTop = vec.wordsNearest(word, ranking);
		System.out.println(String.format("[%s] ≒ [%s]", word, similarTop));

		word = "マネジャー";
		ranking = 20;
		similarTop = vec.wordsNearest(word, ranking);
		System.out.println(String.format("[%s] ≒ [%s]", word, similarTop));

		word = "マネージャ";
		ranking = 20;
		similarTop = vec.wordsNearest(word, ranking);
		System.out.println(String.format("[%s] ≒ [%s]", word, similarTop));

		word = "マネージャー";
		ranking = 20;
		similarTop = vec.wordsNearest(word, ranking);
		System.out.println(String.format("[%s] ≒ [%s]", word, similarTop));

		word = "シミュレーション";
		ranking = 20;
		similarTop = vec.wordsNearest(word, ranking);
		System.out.println(String.format("[%s] ≒ [%s]", word, similarTop));

		System.out.println();

		// ワードベクトル演算
		List<String> positiveList;
		List<String> negativeList;
		Collection<String> nearestList;

		positiveList = Arrays.asList("グローバル", "海外", "オフショア開発");
		negativeList = Arrays.asList("国内", "日本");
		nearestList = vec.wordsNearest(positiveList, negativeList, 10);
		System.out.println(String.format("[%s] － [%s] ≒ %s",
				String.join(" + ", positiveList),
				String.join(" + ", negativeList), nearestList));

		positiveList = Arrays.asList("品質", "レビュー");
		negativeList = Arrays.asList("コスト");
		nearestList = vec.wordsNearest(positiveList, negativeList, 10);
		System.out.println(String.format("[%s] － [%s] ≒ %s",
				String.join(" + ", positiveList),
				String.join(" + ", negativeList), nearestList));

		positiveList = Arrays.asList("プログラム", "ベネフィット");
		negativeList = Arrays.asList("プロジェクト");
		nearestList = vec.wordsNearest(positiveList, negativeList, 10);
		System.out.println(String.format("[%s] － [%s] ≒ %s",
				String.join(" + ", positiveList),
				String.join(" + ", negativeList), nearestList));

		positiveList = Arrays.asList("プロジェクト", "QCD");
		negativeList = Arrays.asList("プログラム");
		nearestList = vec.wordsNearest(positiveList, negativeList, 10);
		System.out.println(String.format("[%s] － [%s] ≒ %s",
				String.join(" + ", positiveList),
				String.join(" + ", negativeList), nearestList));

		return;
	}
}
