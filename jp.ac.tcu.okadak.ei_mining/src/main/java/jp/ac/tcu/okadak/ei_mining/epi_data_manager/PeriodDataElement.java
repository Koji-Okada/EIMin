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
public class PeriodDataElement<T> {

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement<T>> eMap = new HashMap<String, EnterpriseDataElement<T>>();

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
		if (null != period) {
			System.out.println("有り得ない");
		}

		if ((null == enterprise) && (null == indicator)) {
			// 期間データ・指標データが構成済みの場合
			// 末端処理を実施

			this.value = val;

			return;
		}

		if (null != enterprise) {
			// 企業データ木が未処理の場合

			EnterpriseDataElement<T> eElm;
			eElm = eMap.get(enterprise);
			if (null == eElm) {
				eElm = new EnterpriseDataElement<T>();
			}
			eElm.setValue(null, null, indicator, val);
			eMap.put(enterprise, eElm);
		}

		if (null != indicator) {
			// 指標データ木が未処理の場合

			IndicatorDataElement<T> iElm;
			iElm = iMap.get(indicator);
			if (null == iElm) {
				iElm = new IndicatorDataElement<T>();
			}
			iElm.setValue(enterprise, null, null, val);
			iMap.put(indicator, iElm);
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
	@SuppressWarnings({ "hiding", "unchecked" })
	final <T> T getValue() {
		return (T) this.value;
	}
}
