package jp.ac.tcu.okadak.ei_mining.financial_data_analyzer;

import jp.ac.tcu.okadak.ei_mining.data_loader.FDBDataLoader;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * 財務データの選択器.
 *
 * @author K.Okada
 * @version 2019.07.31
 */
public class FinancialDataSelector {

    /**
     * 財務データを選択する.
     *
     * @param args ダミー引数
     */
    public static void main(final String[] args) {

        System.out.println("Start data selector ...");

        FinancialDataSelector fds = new FinancialDataSelector();
        fds.select();

        System.out.println("... Fin");
        return;
    }

    /**
     * 財務データを選択する.
     */
    final void select() {

        // 財務データを読込む
        FDBDataLoader loader = new FDBDataLoader();
        EPIDataManager<Double> epiDM = loader.load();

        String path = "selected.txt";
        epiDM.saveData2(path);

        return;
    }
}
