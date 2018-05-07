package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

import jp.ac.tcu.okadak.ei_mining.epi_data_manager.EnterpriseDataElement;

/**
 *
 * @author K.Okada
 * @version 2018.05.06
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

		// ジェネリクスのテスト
		EnterpriseDataElement<String> edStr = new EnterpriseDataElement<String>();
		String inStr = "ABCDEF";
		edStr.setValue(null, null, null, inStr);
		String outStr = edStr.getValue();
		assertThat(outStr, sameInstance(inStr));

		// 期間生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(2.3e0d);
		ed.setValue(null, "2000", null, inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());

		// 指標生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(3.4e0d);
		ed.setValue(null, null, "index", inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());

		// 期間・指標生成前の処理のテスト
		ed = new EnterpriseDataElement<Double>();
		inVal = new Double(4.5e0d);
		ed.setValue(null, "2000", "index", inVal);
		outVal = ed.getValue();
		assertThat(outVal, nullValue());
	}
}
