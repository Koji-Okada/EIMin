package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.05.06
 */
public class EPIDataManagerTest {

	@Test
	public void test() {

		EPIDataManager<Double> dm = new EPIDataManager<Double>();

		String ent1 = "ABC";
		String ent2 = "DEF";
		String per1 = "2000";
		String per2 = "2001";
		String idx1 = "xxx";
		String idx2 = "yyy";



		Double inVal1 = new Double(1.23e0d);
		Double inVal2 = new Double(2.34e0d);
		Double outVal;

		dm.addData(ent1, per1, idx1, inVal1);
		outVal = dm.getValue(ent1, per1, idx1);
		assertThat(outVal, sameInstance(inVal1));

		outVal = dm.getValue(ent2, per1, idx1);
		assertThat(outVal, nullValue());

		outVal = dm.getValue(ent1, per2, idx1);
		assertThat(outVal, nullValue());

		outVal = dm.getValue(ent1, per1, idx2);
		assertThat(outVal, nullValue());

		dm.addData(ent1, per1, idx2, inVal2);

		outVal = dm.getValue(ent1, per1, idx2);
		assertThat(outVal, sameInstance(inVal2));
	}
}
