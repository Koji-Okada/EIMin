package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 指標データマネジャー.
 *
 * @author K.Okada
 * @version 2018.04.30
 */
public class IndicatorDataManager {

	/**
	 * 期間データ要素のマップ.
	 */
	private Map<String, PeriodDataElement> pMap = null;

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement> eMap = null;

	/**
	 * データを追加する.
	 *
	 * @param period
	 *            期間
	 * @param enterprise
	 *            企業
	 * @param indicator
	 *            指標
	 * @param value
	 *            値
	 */
	final void addData(final String enterprise, final String period,
			final String indicator, final Double value) {

		// 企業データ木に追加処理を行う
		if (null != enterprise) {
			// 企業データ木が未処理の場合

			if (null == eMap) {
				// 企業データ木が存在しない場合
				// 企業データ木を生成する
				eMap = new HashMap<String, EnterpriseDataElement>();
			}

			// 企業データ木から企業データ要素を検索する
			EnterpriseDataElement eDataElement;
			eDataElement = eMap.get(enterprise);

			if (null == eDataElement) {
				// 企業データ要素が存在しなければ、
				// 企業データ要素を生成する
				eDataElement = new EnterpriseDataElement();
				eMap.put(enterprise, eDataElement);
			}

			// 企業データ要素にデータ値を登録する
			eDataElement.setValue(period, null, indicator, value);
		}

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

		return;
	}
}