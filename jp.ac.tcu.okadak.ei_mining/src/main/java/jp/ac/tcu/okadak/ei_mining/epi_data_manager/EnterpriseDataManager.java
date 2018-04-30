package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 企業データマネジャー.
 *
 * @author K.Okada
 * @version 2018.04.30
 */
public class EnterpriseDataManager {

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement> pMap = null;

	/**
	 * 指標データ要素のマップ.
	 */
	private Map<String, IndicatorDataElement> iMap = null;

	/**
	 * データを追加する.
	 *
	 * @param enterprise
	 *            企業
	 * @param period
	 *            期間
	 * @param indicator
	 *            指標
	 * @param value
	 *            値
	 */
	final void addData(final String enterprise, final String period,
			final String indicator, final Double value) {

		// 期間データ木に追加処理を行う
		if (null != period) {
			// 期間データ木が未処理の場合

			if (null == pMap) {
				// 期間データ木が存在しない場合
				// 期間データ木を生成する
				pMap = new HashMap<String, PeriodDataElement>();
			}

			// 期間データ木から期間データ要素を検索する
			PeriodDataElement pDataElement;
			pDataElement = pMap.get(period);

			if (null == pDataElement) {
				// 期間データ要素が存在しなければ、
				// 期間データ要素を生成する
				pDataElement = new PeriodDataElement();
				pMap.put(period, pDataElement);
			}

			// 期間データ要素にデータ値を登録する
			pDataElement.setValue(null, enterprise, indicator, value);
		}

		// 指標データ木に追加処理を行う
		if (null != indicator) {
			// 指標データ木が未処理の場合

			if (null == iMap) {
				// 指標データ木が存在しない場合
				// 指標データ木を生成する
				iMap = new HashMap<String, IndicatorDataElement>();
			}

			// 指標データ木から指標データ要素を検索する
			IndicatorDataElement iDataElement;
			iDataElement = iMap.get(indicator);

			if (null == iDataElement) {
				// 指標データ要素が存在しなければ、
				// 指標データ要素を生成する
				iDataElement = new IndicatorDataElement();
				iMap.put(indicator, iDataElement);
			}

			// 指標データ要素にデータ値を登録する
			iDataElement.setValue(period, enterprise, null, value);
		}

		return;
	}
}
