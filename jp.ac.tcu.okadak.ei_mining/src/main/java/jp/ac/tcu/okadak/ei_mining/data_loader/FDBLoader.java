package jp.ac.tcu.okadak.ei_mining.data_loader;

import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

/**
 * 企業財務データのローディング.
 *
 * 元データはライセンス契約により保護されているので注意のこと！
 *
 * @author K.Okada
 * @version 2018.08.10
 */
public class FDBLoader {

	/**
	 *
	 * @param args	デフォルト.
	 */
	public static void main(final String[] args) {

		System.out.println("Start FDB Loading ...");

		FDBLoader fdbl = new FDBLoader();
		fdbl.load();

		System.out.println("... Fin.");
	}


	/**
	 *
	 */
	public void load() {

		EPIDataManager<Double> dm = new EPIDataManager<Double>();

		return;
	}
}
