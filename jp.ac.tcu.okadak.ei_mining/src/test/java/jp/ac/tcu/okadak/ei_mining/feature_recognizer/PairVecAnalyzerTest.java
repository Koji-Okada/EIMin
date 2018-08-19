package jp.ac.tcu.okadak.ei_mining.feature_recognizer;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author K.Okada
 * @version 2018.08.18
 */
public class PairVecAnalyzerTest {

	@Test
	public void testCosSimularity() {
		final double DELTA = 1e-3;	// Rの表示精度に合わせる

		PairVecAnalyzer pva = new PairVecAnalyzer();
		Double cor;
		Double pValue;

		// ====
		Double[] data1 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data2 = {9.0e0d, 8.0e0d, 6.0e0d, 7.0e0d, 5.0e0d};

		cor = pva.calcCOSSimularity(data1, data2, 0, 4);
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
		cor = pva.calcCOSSimularity(data3, data4, 0, 4);
		pValue = pva.getPValue();
//		System.out.println(cor + "\t" + pValue);

		// R上で
		// data3 <- c(1.0, 2.0, 4.0, 5.0)
		// data4 <- c(9.0, 6.0, 7.0, 5.0)
		// cor.test(data3, data4)
		assertEquals(cor, -0.7483315e0d, DELTA);
		assertEquals(pValue, 0.2517e0d, DELTA);

		Double[] data5 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data6 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		cor = pva.calcCOSSimularity(data5, data6, 0, 4);
		pValue = pva.getPValue();
//		System.out.println(cor + "\t" + pValue);

		// R上で
		// data5 <- c(1.0, 3.0, 4.0, 5.0)
		// data6 <- c(9.0, 8.0, 7.0, 5.0)
		// cor.test(data5, data6)
		assertEquals(cor, -0.94285710d, DELTA);
		assertEquals(pValue, 0.05714e0d, DELTA);

		// ==== 最小自由度以下の場合
		Double[] data7 = {1.0e0d, null, 2.0e0d, 4.0e0d, null};
		Double[] data8 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		cor = pva.calcCOSSimularity(data7, data8, 0, 4);
		pValue = pva.getPValue();
//		System.out.println(cor + "\t" + pValue);

		assertThat(cor, is(nullValue()));
		assertThat(pValue, is(nullValue()));
	}


	@Test
	public void testAmpRatio() {
		final double DELTA = 1e-3;	// Rの表示精度に合わせる

		PairVecAnalyzer pva = new PairVecAnalyzer();
		Double rVar;
		Double pValue;

		// ====
		Double[] data1 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data2 = {9.0e0d, 8.0e0d, 6.0e0d, 7.0e0d, 0.0e0d};

		rVar = pva.calcVarRatio(data1, data2, 0, 4);
		pValue = pva.getPValue();

//		System.out.println(rVar + "\t" + pValue);
		// R上で
		// data1 <- c(1.0, 3.0, 2.0, 4.0, 5.0)
		// data2 <- c(9.0, 8.0, 6.0, 7.0, 0.0)
		// var.test(data1, data2)	// 両側検定値
		assertEquals(rVar, 0.2e0d, DELTA);
		assertEquals(pValue, 0.1481e0d / 2.0e0d, DELTA);

		rVar = pva.calcVarRatio(data2, data1, 0, 4);
		pValue = pva.getPValue();

		// 基準の入替え
//		System.out.println(rVar + "\t" + pValue);
		assertEquals(rVar, 5.0e0d, DELTA);
		assertEquals(pValue, 0.1481e0d / 2.0e0d, DELTA);

		// ==== 欠損値のある場合
		Double[] data3 = {1.0e0d, null, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data4 = {9.0e0d, 8.0e0d, null, 7.0e0d, 0.0e0d};
		rVar = pva.calcVarRatio(data3, data4, 0, 4);
		pValue = pva.getPValue();
//		System.out.println(rVar + "\t" + pValue);

		// R上で
		// data3 <- c(1.0, 2.0, 4.0, 5.0)
		// data4 <- c(9.0, 8.0, 7.0, 0.0)
		// var.test(data3, data4)
		assertEquals(rVar,0.2e0d, DELTA);
		assertEquals(pValue, 0.2191e0d / 2.0e0d, DELTA);

		// ==== 最小自由度以下の場合
		Double[] data5 = {null, null, 2.0e0d, 4.0e0d, null};
		Double[] data6 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		rVar = pva.calcVarRatio(data5, data6, 0, 4);
		pValue = pva.getPValue();
//		System.out.println(rVar + "\t" + pValue);

		assertThat(rVar, is(nullValue()));
		assertThat(pValue, is(nullValue()));
	}

	@Test
	public void testAvgRatio() {
		final double DELTA = 1e-3;	// Rの表示精度に合わせる

		PairVecAnalyzer pva = new PairVecAnalyzer();
		Double rAvg;
		Double pValue;

		// ====
		Double[] data1 = {1.0e0d, 3.0e0d, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data2 = {9.0e0d, 8.0e0d, 6.0e0d, 7.0e0d, 5.0e0d};

		rAvg = pva.calcAvgRatio(data1, data2, 0, 4);
		pValue = pva.getPValue();
		System.out.println(rAvg + "\t" + pValue);

		// R上で
		// data1 <- c(1.0, 3.0, 2.0, 4.0, 5.0)
		// data2 <- c(9.0, 8.0, 6.0, 7.0, 5.0)
		// mean(data1)/mean(data2)
		// t.test(data1, data2)
		assertEquals(rAvg, 0.42857142857142855e0d, DELTA);
		assertEquals(pValue, 0.003949772803445328e0d, DELTA);

		// ==== 欠損値のある場合
		Double[] data3 = {1.0e0d, null, 2.0e0d, 4.0e0d, 5.0e0d};
		Double[] data4 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};

		rAvg = pva.calcAvgRatio(data3, data4, 0, 4);
		pValue = pva.getPValue();
		System.out.println(rAvg + "\t" + pValue);

		// R上で
		// data3 <- c(1.0, 2.0, 4.0, 5.0)
		// data4 <- c(9.0, 8.0, 7.0, 5.0)
		// mean(data3)/mean(data4)
		// t.test(data3, data4)		// 分散分析は t検定と等価
		assertEquals(rAvg, 0.4137931e0d, DELTA);
		assertEquals(pValue, 0.0146e0d, DELTA);

		// ==== 最小自由度以下の場合
		Double[] data5 = {null, null, 2.0e0d, 4.0e0d, null};
		Double[] data6 = {9.0e0d, 8.0e0d, null, 7.0e0d, 5.0e0d};
		rAvg = pva.calcAvgRatio(data5, data6, 0, 4);
		pValue = pva.getPValue();
		System.out.println(rAvg + "\t" + pValue);

		assertThat(rAvg, is(nullValue()));
		assertThat(pValue, is(nullValue()));

	}
}
