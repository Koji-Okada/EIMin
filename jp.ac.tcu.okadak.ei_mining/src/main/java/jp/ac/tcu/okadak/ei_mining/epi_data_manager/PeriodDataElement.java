package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

/**
 * 期間データ要素.
 *
 * @author K.Okada
 * @version 2018.05.01
 *
 * @param <T>
 *            ジェネリックス
 */
public class PeriodDataElement<T> {

	/**
	 * 企業データマネジャー.
	 */
	private EnterpriseDataManager eDManager = null;

	/**
	 * 指標データマネジャー.
	 */
	private IndicatorDataManager iDManager = null;

	/**
	 * 値.
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
		if (null != period) {
			System.out.println("有り得ない");
		}

		if ((null == enterprise) && (null == indicator)) {
			// 企業データ・指標データが構成済みの場合
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
			eDManager.addData(null, null, indicator, val);
		}

		if (null != indicator) {
			// 指標データ木が未処理の場合

			if (null == iDManager) {
				// 指標データマネジャーが未生成の場合
				// 指標データマネジャーを生成する
				iDManager = new IndicatorDataManager();
			}
			// 指標データマネジャーにデータ値を追加する
			iDManager.addData(enterprise, null, null, val);
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
