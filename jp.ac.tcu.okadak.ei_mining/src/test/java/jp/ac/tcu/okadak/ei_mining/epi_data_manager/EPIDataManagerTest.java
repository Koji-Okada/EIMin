package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EPIDataManager;

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
		Double out = dm.getValue("ABC", "2000", "xxx");

		assertThat(out, sameInstance(v1));
	}
}
