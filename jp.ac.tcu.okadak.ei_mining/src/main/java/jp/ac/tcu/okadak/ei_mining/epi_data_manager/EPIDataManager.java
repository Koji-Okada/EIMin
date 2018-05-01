package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 企業-期間-指標マネジャー.
 *
 * @author K.Okada
 * @version 2018.05.01
 *
 * @param <T>
 *            ジェネリックス
 */
public class EPIDataManager<T> {

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement<T>> eMap;

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement<T>> pMap;

	/**
	 * 指標データ要素のマップ.
	 */
	private Map<String, IndicatorDataElement<T>> iMap;

	/**
	 * コンストラクタ.
	 */
	public EPIDataManager() {
		super();
		this.eMap = null;
		this.pMap = null;
		this.iMap = null;

		return;
	}

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
		if (null != enterprise) {
			// 企業データ木が未処理の場合

			if (null == eMap) {
				// 企業データ木が存在しない場合
				// 企業データ木を生成する
				eMap = new HashMap<String, EnterpriseDataElement<T>>();
			}

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
		}

		// 期間データ木に追加処理を行う --------
		if (null != period) {
			// 期間データ木が未処理の場合

			if (null == pMap) {
				// 期間データ木が存在しない場合
				// 期間データ木を生成する
				pMap = new HashMap<String, PeriodDataElement<T>>();
			}

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
		}

		// 指標データ木に追加処理を行う --------
		if (null != indicator) {
			// 指標データ木が未処理の場合

			if (null == iMap) {
				// 指標データ木が存在しない場合
				// 指標データ木を生成する
				iMap = new HashMap<String, IndicatorDataElement<T>>();
			}

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
		}

		return;
	}

}
