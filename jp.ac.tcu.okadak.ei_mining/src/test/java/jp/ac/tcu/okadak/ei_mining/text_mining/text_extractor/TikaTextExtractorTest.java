package jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor.TextFormatter;

public class TikaTextExtractorTest {

	@Test
	public void testDividSentencet() {

		TextFormatter tr = new TextFormatter();

		String str = "This is a pen. It is 100.0 kg.";
		String res = "This is a pen.\r\n It is 100.0 kg.";

		assertThat(tr.dividSentence(str), is(res));
	}

}
