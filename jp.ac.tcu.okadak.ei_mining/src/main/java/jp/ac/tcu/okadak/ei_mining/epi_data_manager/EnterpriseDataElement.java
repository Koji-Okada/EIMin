package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

/**
 * 企業データ要素.
 *
 * @author K.Okada
 * @version 2018.04.30
 */
public class EnterpriseDataElement {

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
	private Double value = null;

	/**
	 * 要素に値を設定する.
	 *
	 * @param period
	 *            期間
	 * @param enterprise
	 *            企業
	 * @param indicator
	 *            指標
	 * @param val
	 *            値
	 */
	final void setValue(final String period, final String enterprise,
			final String indicator, final Double val) {

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
			pDManager.addData(period, null, indicator, val);
		}

		if (null != indicator) {
			// 指標データ木が未処理の場合

			if (null == iDManager) {
				// 指標データマネジャーが未生成の場合
				// 指標データマネジャーを生成する
				iDManager = new IndicatorDataManager();
			}
			// 指標データマネジャーにデータ値を追加する
			iDManager.addData(null, enterprise, indicator, val);
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
	 * @return	指標データマネジャー
	 */
	final IndicatorDataManager getIndicatorDataManager() {
		return this.iDManager;
	}

	/**
	 * データ値を返す.
	 *
	 * @return 値
	 */
	final Double getValue() {
		return this.value;
	}
}
