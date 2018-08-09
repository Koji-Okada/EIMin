package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.08.07
 */
public class IndicatorDataElementTest {

	@Test
	public void testSetValue() {

		Double inVal;
		Double outVal;
		IndicatorDataElement<Double> id;

		// 末端処理のテスト
		id = new IndicatorDataElement<Double>();
		inVal = new Double(1.2e0d);
		id.setValue(null, null, null, inVal);
		outVal = id.getValue();
		assertThat(outVal, sameInstance(inVal));

		// ジェネリクスのテスト
		IndicatorDataElement<String> edStr = new IndicatorDataElement<String>();
		String inStr = "ABCDEF";
		edStr.setValue(null, null, null, inStr);
		String outStr = edStr.getValue();
		assertThat(outStr, sameInstance(inStr));

		// 企業生成前の処理のテスト
		id = new IndicatorDataElement<Double>();
		inVal = new Double(3.4e0d);
		id.setValue("AAAA", null, null, inVal);
		outVal = id.getValue();
		assertThat(outVal, nullValue());

		// 期間生成前の処理のテスト
		id = new IndicatorDataElement<Double>();
		inVal = new Double(2.3e0d);
		id.setValue(null, "2000", null, inVal);
		outVal = id.getValue();
		assertThat(outVal, nullValue());


		// 企業・指標生成前の処理のテスト
		id = new IndicatorDataElement<Double>();
		inVal = new Double(4.5e0d);
		id.setValue("AAAA", "2000", null, inVal);
		outVal = id.getValue();
		assertThat(outVal, nullValue());
	}
}
