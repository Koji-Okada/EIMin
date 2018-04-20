package jp.ac.tcu.okadak.ei_mining.text_mining.n_gram;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * テストケース.
 *
 * @author K.Okada
 * @version 2018.04.20
 */
public class NGramDataTest {

	/**
	 * 出現数の増加をテストする.
	 */
	@Test
	public final void testIncrease() {

		NGramData data = new NGramData(2, false, false);

		assertThat(data.getNum(), is(1)); // 生成直後は 1

		data.increase();
		assertThat(data.getNum(), is(2)); // 1つ増加

		return;
	}
}
