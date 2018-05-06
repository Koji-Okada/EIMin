package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 企業-期間-指標マネジャー.
 *
 * @author K.Okada
 * @version 2018.05.06
 *
 * @param <T>
 *            ジェネリックス
 */
public class EPIDataManager<T> {

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement<T>> eMap = new HashMap<String, EnterpriseDataElement<T>>();

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement<T>> pMap = new HashMap<String, PeriodDataElement<T>>();

	/**
	 * 指標データ要素のマップ.
	 */
	private Map<String, IndicatorDataElement<T>> iMap = new HashMap<String, IndicatorDataElement<T>>();

	/**
	 * データ値を追加する.
	 *
	 * @param period
	 *            期間
	 * @param enterprise
	 *            企業
	 * @param indicator
	 *            指標
	 * @param value
	 *            データ値
	 */
	final void addData(final String enterprise, final String period,
			final String indicator, final Object value) {

		// 企業データ木に追加処理を行う --------
		// 企業データ木から企業データ要素を検索する
		EnterpriseDataElement<T> eDataElement;
		eDataElement = eMap.get(enterprise);
		if (null == eDataElement) {
			// 企業データ要素が存在しなければ、
			// 企業データ要素を生成する
			eDataElement = new EnterpriseDataElement<T>();
			eMap.put(enterprise, eDataElement);
		}

		// 企業データ要素にデータ値を登録する
		eDataElement.setValue(null, period, indicator, value);

		// 期間データ木に追加処理を行う --------
		// 期間データ木から期間データ要素を検索する
		PeriodDataElement<T> pDataElement;
		pDataElement = pMap.get(period);

		if (null == pDataElement) {
			// 期間データ要素が存在しなければ、
			// 期間データ要素を生成する
			pDataElement = new PeriodDataElement<T>();
			pMap.put(period, pDataElement);
		}

		// 期間データ要素にデータ値を登録する
		pDataElement.setValue(enterprise, null, indicator, value);

		// 指標データ木に追加処理を行う --------
		// 指標データ木から指標データ要素を検索する
		IndicatorDataElement<T> iDataElement;
		iDataElement = iMap.get(indicator);

		if (null == iDataElement) {
			// 指標データ要素が存在しなければ、
			// 指標データ要素を生成する
			iDataElement = new IndicatorDataElement<T>();
			iMap.put(indicator, iDataElement);
		}

		// 指標データ要素にデータ値を登録する
		iDataElement.setValue(enterprise, period, null, value);

		return;
	}

//	/**
//	 * 企業データのマップを返す.
//	 *
//	 * @return 企業データのマップ
//	 */
//	final Map<String, EnterpriseDataElement<T>> getEMap() {
//
//		return (Map<String, EnterpriseDataElement<T>>) this.eMap;
//	}

	/**
	 * データ値を返す.
	 *
	 * @param enterprise
	 *            企業
	 * @param period
	 *            期間
	 * @param indicator
	 *            指標
	 * @param <T>
	 *            ジェネリックス
	 * @return 設定されたデータ値
	 */
	final <T> T getValue(final String enterprise, final String period,
			final String indicator) {

		@SuppressWarnings("unchecked")
		EnterpriseDataElement<T> ede = (EnterpriseDataElement<T>) this.eMap
				.get(enterprise);
		if (null == ede) {
			return null;
		}
		Map<String, PeriodDataElement<T>> pm = ede.getPMap();
		PeriodDataElement<T> pde = pm.get(period);
		if (null == pde) {
			return null;
		}
		Map<String, IndicatorDataElement<T>> im = pde.getIMap();
		IndicatorDataElement<T> ide = im.get(indicator);
		if (null == ide) {
			return null;
		}
		T val = ide.getValue();

		return val;
	}
}
