package jp.ac.tcu.okadak.ei_mining.word2vec;

import java.io.File;
import java.io.IOException;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Word2Vec解析器.
 *
 * @author
 * @version 2018.06.01
 */
public class Word2VecAnalyzer {

	/**
	 * Word2Vec分析結果.
	 */
	private Word2Vec vec;


	/**
	 * 初期化する.
	 *
	 * @return Word2Vec本体
	 */
	final Word2Vec initialize() {
		System.out.println("  ... Initializing");

		// モデル作成
		int batchSize = 1000; // ミニバッチで学習する単語数
		int iterations = 100; // 反復回数
		int layerSize = 200; // 次元数
		int minFrequency = 10; // 単語の最低出現頻度(下回るの学習対象外)
		double learningRate = 0.025; // 学習率
		double minLearningRate = 1.0e-3d; // 学習率の最小値
		int negaSamples = 10; // ネガティブサンプリング数
		TokenizerFactory tokenizer = new DefaultTokenizerFactory();

		Word2Vec.Builder builder = new Word2Vec.Builder();
		builder.batchSize(batchSize);
		builder.minWordFrequency(minFrequency);
		builder.useAdaGrad(false);
		builder.layerSize(layerSize);
		builder.iterations(iterations);
		builder.learningRate(learningRate);
		builder.minLearningRate(minLearningRate);
		builder.negativeSample(negaSamples);
		builder.tokenizerFactory(tokenizer);

		// 語彙リストを別途作成するため、モデルをリセットしない設定にする
		builder.resetModel(false);

		this.vec = builder.build();

		System.out.println("    ... Fin Initializing");

		return this.vec;
	}

	/**
	 * 語彙を作成する.
	 */
	final void setVocabulary(final File file) {

		// 分析する文書を登録する
		SentenceIterator iterator = new LineSentenceIterator(file);
		this.vec.setSentenceIterator(iterator);

		// これで良いのか？
		System.out.println("  ... Building Vocabulary");
		this.vec.buildVocab();

		return;
	}


	/**
	 * 指定された文書を分析する.
	 *
	 * @param file
	 *            分析対象とする文書
	 * @return Word2Vec本体
	 */
	final Word2Vec analyze(final File file) {

		System.out.println("  ... Learning");

		// 学習させる
		this.vec.fit();

		System.out.println("  ... Fin Learning");
		return this.vec;
	}

	/**
	 * 指定されたファイルに分析結果を保存する.
	 *
	 * @param file
	 *            出力ファイル
	 */
	final void save(final File file) {
		System.out.println("Save Model...");
		try {
			WordVectorSerializer.writeWordVectors(this.vec, file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
