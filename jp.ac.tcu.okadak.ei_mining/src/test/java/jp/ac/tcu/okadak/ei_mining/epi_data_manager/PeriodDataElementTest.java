package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.08.07
 */
public class PeriodDataElementTest {

	@Test
	public void testSetValue() {

		Double inVal;
		Double outVal;
		PeriodDataElement<Double> pd;

		// 末端処理のテスト
		pd = new PeriodDataElement<Double>();
		inVal = new Double(1.2e0d);
		pd.setValue(null, null, null, inVal);
		outVal = pd.getValue();
		assertThat(outVal, sameInstance(inVal));

		// ジェネリクスのテスト
		EnterpriseDataElement<String> pdStr = new EnterpriseDataElement<String>();
		String inStr = "ABCDEF";
		pdStr.setValue(null, null, null, inStr);
		String outStr = pdStr.getValue();
		assertThat(outStr, sameInstance(inStr));

		// 企業生成前の処理のテスト
		pd = new PeriodDataElement<Double>();
		inVal = new Double(2.3e0d);
		pd.setValue("AAAA", null, null, inVal);
		outVal = pd.getValue();
		assertThat(outVal, nullValue());

		// 指標生成前の処理のテスト
		pd = new PeriodDataElement<Double>();
		inVal = new Double(3.4e0d);
		pd.setValue(null, null, "index", inVal);
		outVal = pd.getValue();
		assertThat(outVal, nullValue());

		// 期間・指標生成前の処理のテスト
		pd = new PeriodDataElement<Double>();
		inVal = new Double(4.5e0d);
		pd.setValue("AAAA", null, "index", inVal);
		outVal = pd.getValue();
		assertThat(outVal, nullValue());
	}
}
