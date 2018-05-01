package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 期間データマネジャー.
 *
 * @author K.Okada
 * @version 2018.05.01
 */
public class PeriodDataManager {

	/**
	 * 企業データ要素のマップ.
	 */
	private Map<String, EnterpriseDataElement> eMap = null;

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
			final String indicator, final Object value) {

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
