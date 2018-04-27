package jp.ac.tcu.okadak.ei_mining.data_loader.XBRL;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XBRL 基本情報抽出器.
 *
 * @author K.Okada
 * @version 2018.04.27
 */
public class XBRLBasicInfoExtractor extends DefaultHandler {

	/**
	 * 企業名.
	 */
	private String enterpriseName = null;

	/**
	 * 企業名を返す.
	 *
	 * @return 企業名
	 */
	final String getEnterpriseName() {
		return this.enterpriseName;
	}

	/**
	 * 会計期間終了日.
	 */
	private String currentFiscalYearEndDate = null;

	/**
	 * 会計期間終了日を返す.
	 *
	 * @return 会計期間終了日
	 */
	final String getCurrentFiscalYearEndDate() {
		return this.currentFiscalYearEndDate;
	}

	/**
	 * 企業名の処理中を示すフラグ.
	 */
	private boolean flagEnterpriseName = false;

	/**
	 * 会計期間終了日の処理中を示すフラグ.
	 */
	private boolean flagCurrentFiscalYearEndDate = false;

	/**
	 *
	 * @param args
	 *            デフォルト.
	 */
	public static void main(final String[] args) {
		try {
			// SAXパーサファクトリを生成する
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// SAXパーサを生成する
			SAXParser parser = spf.newSAXParser();

			// パーサが処理を委譲するハンドラを生成する
			XBRLBasicInfoExtractor handler = new XBRLBasicInfoExtractor();

			parser.parse(new File(args[0]), handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 開始タグ読込み時の処理.
	 *
	 * @param uri
	 *            DefaultHandlerの仕様
	 * @param localName
	 *            DefaultHandlerの仕様
	 * @param qName
	 *            DefaultHandlerの仕様
	 * @param attributes
	 *            DefaultHandlerの仕様
	 */
	public final void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes) {

		if (0 == qName.compareTo("ix:nonNumeric")) {

			String name = attributes.getValue("name");

			if (0 == name.compareTo("jpdei_cor:FilerNameInJapaneseDEI")) {
				flagEnterpriseName = true;
			} else
				if (0 == name
						.compareTo("jpdei_cor:CurrentFiscalYearEndDateDEI")) {
				flagCurrentFiscalYearEndDate = true;
			}
		}
		return;
	}

	/**
	 * テキストデータ読込み時の処理.
	 *
	 * @param ch
	 *            DefaultHandlerの仕様
	 * @param offset
	 *            DefaultHandlerの仕様
	 * @param length
	 *            DefaultHandlerの仕様
	 */
	public final void characters(final char[] ch, final int offset,
			final int length) {

		if (flagEnterpriseName) {
			enterpriseName = new String(ch, offset, length);
			flagEnterpriseName = false;
//			System.out.println(enterpriseName);
		} else if (flagCurrentFiscalYearEndDate) {
			currentFiscalYearEndDate = new String(ch, offset, length);
			flagCurrentFiscalYearEndDate = false;
//			System.out.println("\t" + currentFiscalYearEndDate);
		}
		return;
	}

	/**
	 * 終了タグ読込み時の処理.
	 *
	 * @param uri
	 *            DefaultHandlerの仕様
	 * @param localName
	 *            DefaultHandlerの仕様
	 * @param qName
	 *            DefaultHandlerの仕様
	 */
	public final void endElement(final String uri, final String localName,
			final String qName) {
		// 何もしない.
		return;
	}

	/**
	 * ドキュメント終了時の処理.
	 */
	public final void endDocument() {
		// 何もしない.
		return;
	}
}
