package jp.ac.tcu.okadak.ei_mining.xbrl;

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
public class XBRLBasicInfoHandler extends DefaultHandler {

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
	 * 証券コード.
	 */
	private String securityCode = null;

	/**
	 * 証券コードを返す.
	 *
	 * @return 証券コード
	 */
	final String getSecurityCode() {
		return this.securityCode;
	}

	/**
	 * EDINETコード.
	 */
	private String ediNetCode = null;

	/**
	 * EDINETコードを返す.
	 *
	 * @return EDINETコード
	 */
	final String getEdiNetCode() {
		return this.ediNetCode;
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
	 * 証券コードの処理中を示すフラグ.
	 */
	private boolean flagSecurityCode = false;

	/**
	 * EDINETコードの処理中を示すフラグ.
	 */
	private boolean flagEdiNetCode = false;

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
			XBRLBasicInfoHandler handler = new XBRLBasicInfoHandler();

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
			} else if (0 == name.compareTo("jpdei_cor:SecurityCodeDEI")) {
				flagSecurityCode = true;
			} else if (0 == name.compareTo("jpdei_cor:EDINETCodeDEI")) {
				flagEdiNetCode = true;
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

		if (this.flagEnterpriseName) {
			this.enterpriseName = new String(ch, offset, length);
			this.flagEnterpriseName = false;
			// System.out.println(enterpriseName);
		} else if (this.flagCurrentFiscalYearEndDate) {
			this.currentFiscalYearEndDate = new String(ch, offset, length);
			this.flagCurrentFiscalYearEndDate = false;
			// System.out.println("\t" + currentFiscalYearEndDate);
		} else if (this.flagSecurityCode) {
			this.securityCode = new String(ch, offset, length);
			this.flagSecurityCode = false;
			// System.out.println("\t" + securityCode);
		} else if (this.flagEdiNetCode) {
			this.ediNetCode = new String(ch, offset, length);
			this.flagEdiNetCode = false;
			// System.out.println("\t" + ediNetCode);
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
