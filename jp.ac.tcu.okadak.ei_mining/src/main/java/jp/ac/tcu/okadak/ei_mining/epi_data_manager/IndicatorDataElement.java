package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 指標データ要素.
 *
 * @author K.Okada
 * @version 2018.05.06
 *
 * @param <T>
 *            ジェネリックス
 */
public class IndicatorDataElement<T> {

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement<T>> eMap = new HashMap<String, EnterpriseDataElement<T>>();

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement<T>> pMap = new HashMap<String, PeriodDataElement<T>>();

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

			EnterpriseDataElement<T> eElm = new EnterpriseDataElement<T>();
			eElm.setValue(null, null, indicator, val);
			eMap.put(period, eElm);
		}

		if (null != period) {
			// 期間データ木が未処理の場合

			PeriodDataElement<T> pElm = new PeriodDataElement<T>();
			pElm.setValue(enterprise, null, null, val);
			pMap.put(period, pElm);
		}

		return;
	}


	/**
	 * 企業データのマップを返す.
	 *
	 * @return 企業データのマップ
	 */
	final Map<String, EnterpriseDataElement<T>> getEMap() {

		return (Map<String, EnterpriseDataElement<T>>) this.eMap;
	}


	/**
	 * 期間データのマップを返す.
	 *
	 * @return 期間データのマップ
	 */
	final Map<String, PeriodDataElement<T>> getPMap() {

		return (Map<String, PeriodDataElement<T>>) this.pMap;
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
