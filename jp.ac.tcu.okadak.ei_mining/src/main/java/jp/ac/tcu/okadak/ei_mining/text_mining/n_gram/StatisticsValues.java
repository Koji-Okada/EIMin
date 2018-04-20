package jp.ac.tcu.okadak.ei_mining.text_mining.n_gram;

/**
 * N-Gramのスコア計算のための統計値
 *
 * @author	K.Okada
 * @version	2016.08.18
 *
 */
public class StatisticsValues {

	private int docLen;
	private int maxGram;
	private int[] numTermType;

	private int maxNumOfFactorial = 128; // 階乗を求めるのは128程度が限界
	private double factorial[];

	private double np[]; // Gram毎の NP値

	double poisson[][];
	double cPoisson[][];

	/**
	 * 出現回数num 以上出現する確率を求める.
	 *
	 * @author	K.Okada
	 * @version	2016.08.18
	 *
	 * @param gram		Gram数
	 * @param num		出現回数
	 * @return			出現回数num 未満出現する確率
	 */
	final double getProbability(final int gram, int num) {

		double probability;
		if (num >= this.maxNumOfFactorial) {
			num = this.maxNumOfFactorial - 1;
		}

		if (0 == num) {
			probability = 1.0e0;
		} else {
			probability = cPoisson[gram][num - 1];
		}

		if (0.0e0 > probability) {
			// 演算誤差で負値になる場合の補正
			probability = 0.0e0;
		} else if (1.0e0 < probability) {
			// 演算誤差で1.0以上の値になる場合の補正
			probability = 1.0e0;
		}

		return probability;

	}

	/**
	 * 統計量を算出し内部変数に設定する.
	 *
	 * @author	K.Okada
	 * @version	2016.08.18	 *
	 *
	 * @param dLen			文書長
	 * @param mGram			N-Gramの最大値
	 * @param nTermType		Gram毎の文字列種類数
	 */
	public final void initialize(int dLen, int mGram, int[] nTermType) {

		// 内部変数に設定する
		this.docLen = dLen;
		this.maxGram = mGram;
		this.numTermType = nTermType;

		// 階乗値を設定する
		this.factorial = new double[this.maxNumOfFactorial];
		this.factorial[0] = 1.0e0;
		for (int i = 1; i < this.maxNumOfFactorial; i++) {
			this.factorial[i] = this.factorial[i - 1] * (double) i;
		}

		// np値を設定する
		this.np = new double[this.maxGram + 1];
		for (int i = 2; i <= this.maxGram; i++) {
			// Gram数のループ
			this.np[i] = (double) (this.docLen + 1 - i)
					/ (double) this.numTermType[i];
		}

		poisson = new double[this.maxGram + 1][this.maxNumOfFactorial];
		cPoisson = new double[this.maxGram + 1][this.maxNumOfFactorial];

		for (int i = 2; i <= this.maxGram; i++) {
			// Gram数のループ
			for (int k = 0; k < this.maxNumOfFactorial; k++) {
				// 出現数のループ
				poisson[i][k] = StrictMath.pow(StrictMath.E,
						-(double) this.np[i]) * StrictMath.pow(
								(double) this.np[i], (double) k)
						/ this.factorial[k];

				if (0 == k) {
					cPoisson[i][k] = poisson[i][k];
				} else {
					cPoisson[i][k] = cPoisson[i][k - 1] + poisson[i][k];
				}
			}
		}

		return;
	}

}
