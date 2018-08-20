package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 企業データ要素.
 *
 * @author K.Okada
 * @version 2018.08.07
 *
 * @param <T>
 *            ジェネリックス
 */
public class EnterpriseDataElement<T> {

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement<T>> pMap = new HashMap<String, PeriodDataElement<T>>();

	/**
	 * 指標データ要素のマップ.
	 */
	private Map<String, IndicatorDataElement<T>> iMap = new HashMap<String, IndicatorDataElement<T>>();

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

			PeriodDataElement<T> pElm;
			pElm = pMap.get(period);
			if (null == pElm) {
				pElm = new PeriodDataElement<T>();
			}
			pElm.setValue(null, null, indicator, val);
			pMap.put(period, pElm);
		}

		if (null != indicator) {
			// 指標データ木が未処理の場合

			IndicatorDataElement<T> iElm;
			iElm = iMap.get(indicator);
			if (null == iElm) {
				iElm = new IndicatorDataElement<T>();
			}
			iElm.setValue(null, period, null, val);
			iMap.put(indicator, iElm);
		}

		return;
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
	 * 指標データのマップを返す.
	 *
	 * @return 指標データのマップ
	 */
	final Map<String, IndicatorDataElement<T>> getIMap() {

		return (Map<String, IndicatorDataElement<T>>) this.iMap;
	}

	/**
	 * データ値を返す.
	 *
	 * @param <T>
	 *            ジェネリックス
	 * @return 値
	 */
	@SuppressWarnings("unchecked")
	final <T> T getValue() {
		return (T) this.value;
	}
}
