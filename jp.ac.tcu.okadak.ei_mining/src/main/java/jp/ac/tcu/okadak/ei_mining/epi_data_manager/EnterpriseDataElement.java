package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

/**
 * 企業データ要素.
 *
 * @author K.Okada
 * @version 2018.05.01
 *
 * @param <T>
 *            ジェネリックス
 */
public class EnterpriseDataElement<T> {

	/**
	 * 期間データマネジャー.
	 */
	private PeriodDataManager pDManager = null;

	/**
	 * 指標データマネジャー.
	 */
	private IndicatorDataManager iDManager = null;

	/**
	 * データ.
	 */
	private Object value = null;

	/**
	 * 要素に値を設定する.
	 *
	 * @param enterprise
	 *            企業
	 * @param period
	 *            期間
	 * @param indicator
	 *            指標
	 * @param val
	 *            値
	 */
	final void setValue(final String enterprise, final String period,
			final String indicator, final Object val) {

		// 引数の事前検証
		if (null != enterprise) {
			System.out.println("有り得ない");
		}

		if ((null == period) && (null == indicator)) {
			// 期間データ・指標データが構成済みの場合
			// 末端処理を実施
			this.value = val;

			return;
		}

		if (null != period) {
			// 期間データ木が未処理の場合

			if (null == pDManager) {
				// 期間データマネジャーが未生成の場合
				// 期間データマネジャーを生成する
				pDManager = new PeriodDataManager();
			}
			// 期間データマネジャーにデータ値を追加する
			pDManager.addData(null, null, indicator, val);
		}

		if (null != indicator) {
			// 指標データ木が未処理の場合

			if (null == iDManager) {
				// 指標データマネジャーが未生成の場合
				// 指標データマネジャーを生成する
				iDManager = new IndicatorDataManager();
			}
			// 指標データマネジャーにデータ値を追加する
			iDManager.addData(null, period, null, val);
		}

		return;
	}

	/**
	 * 期間データマネジャーを返す.
	 *
	 * @return 期間データマネジャー
	 */
	final PeriodDataManager getPeriodDataManager() {
		return this.pDManager;
	}

	/**
	 * 指標データマネジャーを返す.
	 *
	 * @return 指標データマネジャー
	 */
	final IndicatorDataManager getIndicatorDataManager() {
		return this.iDManager;
	}

	/**
	 * データ値を返す.
	 *
	 * @param <T>
	 *            ジェネリックス
	 * @return 値
	 */
	@SuppressWarnings({"hiding", "unchecked"})
	final <T> T getValue() {
		return (T) this.value;
	}
}
