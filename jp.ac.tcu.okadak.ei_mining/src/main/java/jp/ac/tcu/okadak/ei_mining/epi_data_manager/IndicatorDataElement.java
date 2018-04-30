package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

/**
 * 指標データ要素.
 *
 * @author K.Okada
 * @version 2018.04.30
 */
public class IndicatorDataElement {

	/**
	 * 企業データマネジャー.
	 */
	private EnterpriseDataManager eDManager = null;

	/**
	 * 期間データマネジャー.
	 */
	private PeriodDataManager pDManager = null;

	/**
	 * 値.
	 */
	private Double value = null;

	/**
	 *
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
			final String indicator, final Double val) {

		// 引数の事前検証
		if (null != indicator) {
			System.out.println("有り得ない");
		}

		if ((null == enterprise) && (null == period)) {
			// 企業データ・期間データが構成済みの場合
			// 末端処理を実施
			this.value = val;

			return;
		}

		if (null != enterprise) {
			// 企業データ木が未処理の場合

			if (null == eDManager) {
				// 企業データマネジャーが未生成の場合
				// 企業データマネジャーを生成する
				eDManager = new EnterpriseDataManager();
			}
			// 企業データマネジャーにデータ値を追加する
			eDManager.addData(null, enterprise, indicator, val);
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

		return;
	}

	/**
	 * 企業データマネジャーを返す.
	 *
	 * @return 企業データマネジャー
	 */
	final EnterpriseDataManager getEnterpriseDataManager() {
		return this.eDManager;
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
	 * データ値を返す.
	 *
	 * @return 値
	 */
	final Double getValue() {
		return this.value;
	}
}
