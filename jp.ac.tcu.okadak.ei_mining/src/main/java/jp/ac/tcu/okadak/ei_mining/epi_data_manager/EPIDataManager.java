package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 企業-期間-指標マネジャー.
 *
 * @author K.Okada
 * @version 2019.07.29
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
     * (Integer型の場合のみ有効)
     *
     * @param enterprise	企業
     * @param period	期間
     * @param indicator	指標
     * @param value	値
     */
    public final void addData(final String enterprise, final String period,
            final String indicator, final Object value) {

        // 登録されていればそのデータを得る
        T existingValue = this.getValue(enterprise, period, indicator);

        if (null != existingValue) {
            // 既に登録されていた場合

            Integer addV;
            if (value instanceof Integer) {
                addV = (Integer) value;

                Integer extV;
                if (null != existingValue) {
                    if (existingValue instanceof Integer) {
                        extV = (Integer) existingValue;
                        Integer x = new Integer(addV.intValue() + extV
                                .intValue());
                        this.putData(enterprise, period, indicator, x);
                    }
                }
            }
        } else {
            this.putData(enterprise, period, indicator, value);
        }
        return;
    }

    /**
     * データ値を登録する.
     * (既に値が登録されている場合は更新する)
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
    public final void putData(final String enterprise, final String period,
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

    /**
     * 登録されている企業名を返す.
     *
     * @return	登録されている企業名のリスト
     */
    public final List<String> getEnterprises() {

        List<String> list = new ArrayList<String>(eMap.keySet());
        return list;
    }

    /**
     * 登録されている期間名を返す.
     *
     * @return	登録されている期間名の集合
     */
    public final Set<String> getPeriods() {
        return pMap.keySet();
    }

    /**
     * 登録されている期間名のリストを返す.
     * (期間の欠損も補完の上で、昇順ソーティング)
     *
     * @return	登録されている期間名のリスト
     */
    public final List<String> listPeriods() {

        final int initMAX = 0;
        final int initMIN = 10000;

        List<String> pList = new ArrayList<String>();
        int max = initMAX;
        int min = initMIN;

        Set<String> periods = pMap.keySet();

        // 期間の最小値と最大値を求める
        for (String p : periods) {
            int i = Integer.parseInt(p);
            if (max < i) {
                max = i;
            }
            if (i < min) {
                min = i;
            }
        }

        // 最小値から最大値まで昇順にリスト化する
        for (int i = min; i <= max; i++) {
            pList.add(Integer.toString(i));
        }

        return pList;
    }

    /**
     * 登録されている指標名を返す.
     *
     * @return	登録されている指標名のリスト
     */
    public final List<String> getIndicators() {

        List<String> list = new ArrayList<String>(iMap.keySet());
        return list;
    }

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
    @SuppressWarnings("hiding")
    public final <T> T getValue(final String enterprise, final String period,
            final String indicator) {

        @SuppressWarnings("unchecked")
        EnterpriseDataElement<T> ede = (EnterpriseDataElement<T>) this.eMap.get(
                enterprise);
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

    /**
     * EPIデータをファイルに書き出す.
     *
     * @param fileName ファイル名(パス指定)
     */
    public final void saveData(final String fileName) {

        List<String> enterprises = getEnterprises();
        List<String> periods = listPeriods();
        List<String> indicators = getIndicators();

        String header = ",";
        for (String ind : indicators) {
            header = header + "," + ind;
        }
        System.out.println(header);

        for (String ent : enterprises) {
            for (String prd : periods) {
                String line = ent + "," + prd;
                for (String ind : indicators) {

                    Object value = getValue(ent, prd, ind);

                    String str = value.toString();

                    line = line + "," + str;
                }
            }
        }

        return;
    }
}
