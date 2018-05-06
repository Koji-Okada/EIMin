package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.05.06
 */
public class IndicatorDataElementTest {

	@Test
	public void testSetValue() {

		Double inVal;
		Double outVal;
		IndicatorDataElement<Double> ix;

		// 末端処理のテスト
		ix = new IndicatorDataElement<Double>();
		inVal = new Double(1.2e0d);
		ix.setValue(null, null, null, inVal);
		outVal = ix.getValue();
		assertThat(outVal, sameInstance(inVal));
		assertThat(ix.getEnterpriseDataManager(), nullValue());
		assertThat(ix.getPeriodDataManager(), nullValue());

		// ジェネリクスのテスト
		IndicatorDataElement<String> ixStr = new IndicatorDataElement<String>();
		String inStr = "ABCDEF";
		ixStr.setValue(null, null, null, inStr);
		String outStr = ixStr.getValue();
		assertThat(outStr, sameInstance(inStr));
		assertThat(ixStr.getEnterpriseDataManager(), nullValue());
		assertThat(ixStr.getPeriodDataManager(), nullValue());

		// 企業生成前の処理のテスト
		ix = new IndicatorDataElement<Double>();
		inVal = new Double(2.3e0d);
		ix.setValue("Ent1", null, null, inVal);
		outVal = ix.getValue();
		assertThat(outVal, nullValue());
		assertThat(ix.getEnterpriseDataManager(), notNullValue());
		assertThat(ix.getPeriodDataManager(), nullValue());

		// 期間生成前の処理のテスト
		ix = new IndicatorDataElement<Double>();
		inVal = new Double(3.4e0d);
		ix.setValue(null, "2000", null, inVal);
		outVal = ix.getValue();
		assertThat(outVal, nullValue());
		assertThat(ix.getEnterpriseDataManager(), nullValue());
		assertThat(ix.getPeriodDataManager(), notNullValue());

		// 期間・指標生成前の処理のテスト
		ix = new IndicatorDataElement<Double>();
		inVal = new Double(4.5e0d);
		ix.setValue("ent2", "2000", null, inVal);
		outVal = ix.getValue();
		assertThat(outVal, nullValue());
		assertThat(ix.getEnterpriseDataManager(), notNullValue());
		assertThat(ix.getPeriodDataManager(), notNullValue());
	}
}
