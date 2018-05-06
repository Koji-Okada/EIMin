package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.05.01
 */
public class EPIDataManagerTest {

	@Test
	public void test() {

		EPIDataManager<Double> dm = new EPIDataManager<Double>();

		Double v1 = new Double(1.23e0d);
		Double v2 = new Double(2.34e0d);

		dm.addData("ABC", "2000", "xxx", v1);
//		dm.addData("ABC", "2000", "yyy", v2);

	}

}
