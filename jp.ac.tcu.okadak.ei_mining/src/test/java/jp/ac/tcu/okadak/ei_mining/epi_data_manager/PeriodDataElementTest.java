package jp.ac.tcu.okadak.ei_mining.epi_data_manager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.05.06
 */
public class PeriodDataElementTest {

	@Test
	public void testSetValue() {

		Double inVal;
		Double outVal;
		PeriodDataElement<Double> pr;

		// 末端処理のテスト
		pr = new PeriodDataElement<Double>();
		inVal = new Double(1.2e0d);
		pr.setValue(null, null, null, inVal);
		outVal = pr.getValue();
		assertThat(outVal, sameInstance(inVal));
		assertThat(pr.getEnterpriseDataManager(), nullValue());
		assertThat(pr.getIndicatorDataManager(), nullValue());

		// ジェネリクスのテスト
		PeriodDataElement<String> prStr = new PeriodDataElement<String>();
		String inStr = "ABCDEF";
		prStr.setValue(null, null, null, inStr);
		String outStr = prStr.getValue();
		assertThat(outStr, sameInstance(inStr));
		assertThat(prStr.getEnterpriseDataManager(), nullValue());
		assertThat(prStr.getIndicatorDataManager(), nullValue());

		// 企業生成前の処理のテスト
		pr = new PeriodDataElement<Double>();
		inVal = new Double(2.3e0d);
		pr.setValue("Ent1", null, null, inVal);
		outVal = pr.getValue();
		assertThat(outVal, nullValue());
		assertThat(pr.getEnterpriseDataManager(), notNullValue());
		assertThat(pr.getIndicatorDataManager(), nullValue());

		// 指標生成前の処理のテスト
		pr = new PeriodDataElement<Double>();
		inVal = new Double(3.4e0d);
		pr.setValue(null, null, "index", inVal);
		outVal = pr.getValue();
		assertThat(outVal, nullValue());
		assertThat(pr.getEnterpriseDataManager(), nullValue());
		assertThat(pr.getIndicatorDataManager(), notNullValue());

		// 期間・指標生成前の処理のテスト
		pr = new PeriodDataElement<Double>();
		inVal = new Double(4.5e0d);
		pr.setValue("ent2", null, "index", inVal);
		outVal = pr.getValue();
		assertThat(outVal, nullValue());
		assertThat(pr.getEnterpriseDataManager(), notNullValue());
		assertThat(pr.getIndicatorDataManager(), notNullValue());
	}
}
