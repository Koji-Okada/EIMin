package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PairVecAnalyzerTest {

	@Test
	public void testCosSimularity() {
		final double DELTA = 1e-4;	// Rの表示精度に合わせる

		PairVecAnalyzer pva = new PairVecAnalyzer();
		Double cor;
		Double pValue;

		// ====
		Double[] data1 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data2 = {9.0e0d, 8.0e0d, 6.0e0d, 7.0e0d, 5.0e0d};

		cor = pva.cosSimularity(data1, data2, 0, 4);
		pValue = pva.getPValue();

		// R上で
		// data1 <- c(1.0, 3.0, 2.0, 4.0, 5.0)
		// data2 <- c(9.0, 8.0, 6.0, 7.0, 5.0)
		// cor.test(data1, data2)
		assertEquals(cor, -0.7e0d, DELTA);
		assertEquals(pValue, 0.1881e0d, DELTA);


		// ==== 欠損値のある場合

		Double[] data3 = {1.0e0d, null, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data4 = {9.0e0d, 8.0e0d, 6.0e0d, 7.0e0d, 5.0e0d};
		cor = pva.cosSimularity(data3, data4, 0, 4);
		pValue = pva.getPValue();
		System.out.println(cor + "\t" + pValue);

		// R上で
		// data3 <- c(1.0, 2.0, 4.0, 5.0)
		// data4 <- c(9.0, 6.0, 7.0, 5.0)
		// cor.test(data1, data2)
		assertEquals(cor, -0.7483315e0d, DELTA);
		assertEquals(pValue, 0.2517e0d, DELTA);

		Double[] data5 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data6 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		cor = pva.cosSimularity(data5, data6, 0, 4);
		pValue = pva.getPValue();
		System.out.println(cor + "\t" + pValue);

		// R上で
		// data5 <- c(1.0, 3.0, 4.0, 5.0)
		// data6 <- c(9.0, 8.0, 7.0, 5.0)
		// cor.test(data1, data2)
		assertEquals(cor, -0.94285710d, DELTA);
		assertEquals(pValue, 0.05714e0d, DELTA);

		// ==== 最小自由度以下の場合
		Double[] data7 = {1.0e0d, null, 2.0e0d, 4.0e0d, null};
		Double[] data8 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		cor = pva.cosSimularity(data7, data8, 0, 4);
		pValue = pva.getPValue();
		System.out.println(cor + "\t" + pValue);

		assertThat(cor, is(nullValue()));
		assertThat(pValue, is(nullValue()));
	}
}
