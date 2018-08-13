package jp.ac.tcu.okadak.ei_mining.financial_data_analyzer;

import java.util.List;

import jp.ac.tcu.okadak.ei_mining.data_loader.FDBDataLoader;
import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * 財務データ分析器.
 *
 * @author K.Okada
 * @version 2018.08.13
 */
public class FinancialDataAnalyzer {


	/**
	 * パブリックコンストラクタをオーバーライド.
	 */
	private FinancialDataAnalyzer() {
		return;
	}

	/**
	 * 財務データを分析する.
	 *
	 * @param args
	 *            ダミー引数
	 */
	public static void main(final String[] args) {

		System.out.println("Start analyzing financial data ...");

		FinancialDataAnalyzer fda = new FinancialDataAnalyzer();
		fda.analyze();

		System.out.println("... Fin");
		return;
	}

	/**
	 * 財務データを分析する.
	 */
	private void analyze() {

		// 財務データを読込む
		FDBDataLoader loader = new FDBDataLoader();
		EPIDataManager<Double> epiDM = loader.load();

		// それぞれの要素数を求める
		List<String> enterprises = epiDM.getEnterprises();
		int numEnterprises = enterprises.size();
		List<String> periods = epiDM.listPeriod();
		int numPeriods = periods.size();
		List<String> indicators = epiDM.getIndicators();
		int numIndicators = indicators.size();

		System.out.println(
				numEnterprises + " : " + numPeriods + " : " + numIndicators);


		return;
	}

}
