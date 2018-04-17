package jp.ac.tcu.okadak.ei_mining.text_mining;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import jp.ac.tcu.okadak.ei_mining.text_mining.NGramData;

/**
 *
 * テストケース.
 *
 * @author K.Okada
 * @version	2015.01.14
 */
public class NGramDataTest {

	/**
	 *
	 * 出現数の増加をテストする.
	 *
	 * @author K.Okada
	 * @versinn	2015.01.14
	 */
	@Test
	public final void testIncrease() {

		NGramData data = new NGramData(2, false, false);

		assertThat(data.getNum(), is(1));		// 生成直後は 1

		data.increase();
		assertThat(data.getNum(), is(2));		// 1つ増加

		return;
	}
}
