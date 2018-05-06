package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.05.01
 */
public class EnterpriseDataElementTest {

	@Test
	public void testSetValue() {

		Double inVal;
		Double outVal;
		EnterpriseDataElement<Double> ed;

		// 末端処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(1.2e0d);
		ed.setValue(null, null, null, inVal);
		outVal = ed.getValue();
		assertThat(outVal, sameInstance(inVal));
		assertThat(ed.getPeriodDataManager(), nullValue());
		assertThat(ed.getIndicatorDataManager(), nullValue());

		// ジェネリクスのテスト
		EnterpriseDataElement<String> edStr = new EnterpriseDataElement<String>();
		String inStr = "ABCDEF";
		edStr.setValue(null, null, null, inStr);
		String outStr = edStr.getValue();
		assertThat(outStr, sameInstance(inStr));
		assertThat(edStr.getPeriodDataManager(), nullValue());
		assertThat(edStr.getIndicatorDataManager(), nullValue());

		// 期間生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(2.3e0d);
		ed.setValue(null, "2000", null, inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());
		assertThat(ed.getPeriodDataManager(), notNullValue());
		assertThat(ed.getIndicatorDataManager(), nullValue());

		// 指標生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(3.4e0d);
		ed.setValue(null, null, "index", inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());
		assertThat(ed.getPeriodDataManager(), nullValue());
		assertThat(ed.getIndicatorDataManager(), notNullValue());

		// 期間・指標生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(4.5e0d);
		ed.setValue(null, "2000", "index", inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());
		assertThat(ed.getPeriodDataManager(), notNullValue());
		assertThat(ed.getIndicatorDataManager(), notNullValue());
	}
}
