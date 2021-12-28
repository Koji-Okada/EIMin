package jp.ac.tcu.okadak.ei_mining.xbrl;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XBRL 基本情報抽出器.
 *
 * @author K.Okada
 * @version 2019.04.23
 */
public class XBRLBasicInfoHandler extends DefaultHandler {

	// =======================================================================
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
	 * 提出回数.
	 */
	private String numOfSubmission;

	/**
	 * 提出回数を返す.
	 *
	 * @return 提出回数
	 */
	final String getNumOfSubmission() {
		return this.numOfSubmission;
	}

	// =======================================================================
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
	 * EDINETコードの処理中を示すフラグ.
	 */
	private boolean flagNumOfSubmission = false;

	// =======================================================================
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

		if (0 == qName.compareTo("jpdei_cor:FilerNameInJapaneseDEI")) {
			// 企業名(日本語)の記述部分に遭遇した場合
			flagEnterpriseName = true;
		} else if (0 == qName.compareTo(
				"jpdei_cor:CurrentPeriodEndDateDEI")) {
//			"jpdei_cor:CurrentFiscalYearEndDateDEI")) {
		// 現状会計年度終了日の記述部分に遭遇した場合
			flagCurrentFiscalYearEndDate = true;				
		} else if (0 == qName.compareTo("jpdei_cor:SecurityCodeDEI")) {
			// 証券コードの記述部分に遭遇した場合
			flagSecurityCode = true;
		} else if (0 == qName.compareTo("jpdei_cor:EDINETCodeDEI")) {
			// EDINETコードの記述部分に遭遇した場合
			flagEdiNetCode = true;
		} else if (0 == qName.compareTo("jpdei_cor:NumberOfSubmissionDEI")) {
			// EDINETコードの記述部分に遭遇した場合
			flagNumOfSubmission = true;
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
			// 企業名(日本語)の記述部分を処理中の場合
			this.enterpriseName = getString(ch, offset, length);
			this.flagEnterpriseName = false;
			// System.out.println(enterpriseName);
		} else if (this.flagCurrentFiscalYearEndDate) {
			// 現状会計年度終了日の記述部分を処理中の場合
			this.currentFiscalYearEndDate = getString(ch, offset, length);
			this.flagCurrentFiscalYearEndDate = false;
			// System.out.println("\t" + currentFiscalYearEndDate);
		} else if (this.flagSecurityCode) {
			// 証券コード記述部分を処理中の場合
			this.securityCode = getString(ch, offset, length);
			this.flagSecurityCode = false;
			// System.out.println("\t" + securityCode);
		} else if (this.flagEdiNetCode) {
			// EDINETコードの記述部分を処理中の場合
			this.ediNetCode = getString(ch, offset, length);
			this.flagEdiNetCode = false;
			// System.out.println("\t" + ediNetCode);
		} else if (this.flagNumOfSubmission) {
			// 提出回数の記述部分を処理中の場合
			this.numOfSubmission = getString(ch, offset, length);
			this.flagNumOfSubmission = false;
		}

		return;
	}

	/**
	 * 文字列を取り出す.
	 *
	 * @param ch	文字配列
	 * @param offset	先頭
	 * @param length	文字列長
	 * @return	文字列
	 */
	private String getString(final char[] ch, final int offset,
			final int length) {

		String str = new String(ch, offset, length);
		str = str.trim();
		if (0 == str.length()) {
			str = null;
		}

		return str;
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
