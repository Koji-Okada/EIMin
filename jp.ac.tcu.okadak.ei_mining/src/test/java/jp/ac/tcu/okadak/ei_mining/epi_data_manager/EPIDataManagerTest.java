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
	public void testAddData() {

		EPIDataManager<Double> dm = new EPIDataManager<Double>();

		String ent0 = "ABC";
		String ent1 = "DEF";
		String per0 = "2000";
		String per1 = "2001";
		String idx0 = "xxx";
		String idx1 = "yyy";

		Double inV000 = new Double(000.0e0d);
		Double inV001 = new Double(001.0e0d);
		Double inV010 = new Double(010.0e0d);
		Double inV011 = new Double(011.0e0d);
		Double inV100 = new Double(100.0e0d);
		Double inV101 = new Double(101.0e0d);
		Double inV110 = new Double(110.0e0d);
		Double inV111 = new Double(111.0e0d);

		Double outVal;

		// 登録
		dm.addData(ent0, per0, idx0, inV000);
		outVal = dm.getValue(ent0, per0, idx0);
		assertThat(outVal, sameInstance(inV000));

		outVal = dm.getValue(ent1, per0, idx0);
		assertThat(outVal, nullValue());

		outVal = dm.getValue(ent0, per1, idx0);
		assertThat(outVal, nullValue());

		outVal = dm.getValue(ent0, per0, idx1);
		assertThat(outVal, nullValue());

		// 登録 × 7
		dm.addData(ent0, per0, idx1, inV001);
		outVal = dm.getValue(ent0, per0, idx1);
		assertThat(outVal, sameInstance(inV001));

		dm.addData(ent0, per1, idx0, inV010);
		outVal = dm.getValue(ent0, per1, idx0);
		assertThat(outVal, sameInstance(inV010));

		dm.addData(ent0, per1, idx1, inV011);
		outVal = dm.getValue(ent0, per1, idx1);
		assertThat(outVal, sameInstance(inV011));

		dm.addData(ent1, per0, idx0, inV100);
		outVal = dm.getValue(ent1, per0, idx0);
		assertThat(outVal, sameInstance(inV100));

		dm.addData(ent1, per0, idx1, inV101);
		outVal = dm.getValue(ent1, per0, idx1);
		assertThat(outVal, sameInstance(inV101));

		dm.addData(ent1, per1, idx0, inV110);
		outVal = dm.getValue(ent1, per1, idx0);
		assertThat(outVal, sameInstance(inV110));

		dm.addData(ent1, per1, idx1, inV111);
		outVal = dm.getValue(ent1, per1, idx1);
		assertThat(outVal, sameInstance(inV111));

		// 再確認
		outVal = dm.getValue(ent0, per0, idx0);
		assertThat(outVal, sameInstance(inV000));

		outVal = dm.getValue(ent1, per0, idx0);
		assertThat(outVal, sameInstance(inV100));

		outVal = dm.getValue(ent0, per1, idx0);
		assertThat(outVal, sameInstance(inV010));

		outVal = dm.getValue(ent0, per0, idx1);
		assertThat(outVal, sameInstance(inV001));
	}
}
