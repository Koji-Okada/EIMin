package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XBRLData {

	String xbrlUri;
	
	public XBRLData(String xbrlUri) {
		super();
		
		this.xbrlUri = xbrlUri;
	}

	// ==================================================================================
	// 基礎データ
	//
	String docType = "不明"; // 報告書種別
	String entName = null; // 企業名
	String sCode = null; // 株式コード
	String eCode = null; // EDINETコード
	String acStandard = null; // 会計基準
	String numSubmission = null; // 提出回数

	/**
	 * 
	 */
	void showBasicInfo() {

		System.out.println("== 基礎情報");
		System.out.println("  報告書種別:\t" + docType);
		System.out.println("  企業名：\t" + entName);
		System.out.println("  株式コード:\t" + sCode);
		System.out.println("  EDINETコード:\t" + eCode);
		System.out.println("  会計基準:\t" + acStandard);
		System.out.println("  提出回数:\t" + numSubmission);

		return;
	}

	// ==================================================================================
	// 日時データ
	//
	String currentYearInstant = null;
	String prior1YearInstant = null;
	String currentYearStartDate = null;
	String currentYearEndDate = null;

	String currentQuarterInstant = null;
	String currentQuarterStartDate = null;
	String currentQuarterEndDate = null;

	String currentQuarterInstantNonConsolidated = null; // 単体決算の場合：追加調査が必要

	/**
	 * 
	 */
	void showDateInfo() {

		System.out.println("== 日時情報");

		System.out.println("  本年度決算日:\t" + currentYearInstant);
		System.out.println("  前年度決算日:\t" + prior1YearInstant);

		System.out.println("  本四半期決算日:\t" + currentQuarterInstant);
		System.out.println("  本四半期決算日(単体):\t" + currentQuarterInstantNonConsolidated);

	}

	// ==================================================================================
	// P/Lデータ
	//

	// 売上高
	Long[] netSales = new Long[5];
	Long[] netSalesNC = new Long[5];
	// 営業収益(売上高相当)
	Long[] operatingRevenue1SummaryOfBusinessResults = new Long[5];
	Long[] operatingRevenue1SummaryOfBusinessResultsNC = new Long[5];
	// 売上原価
	Long costOfSalesCrtYTDuration = null;
	Long costOfSalesCrtYTDurationNC = null;
	Long costOfSalesPr1YTDuration = null;
	Long costOfSalesPr1YTDurationNC = null;
	// 売上総利益
	Long grossProfitCrtYTDuration = null;
	Long grossProfitCrtYTDurationNC = null;
	Long grossProfitPr1YTDuration = null;
	Long grossProfitPr1YTDurationNC = null;
	// 販管費
	Long sellingGeneralAndAdministrativeExpensesCrtYTDuration = null;
	Long sellingGeneralAndAdministrativeExpensesCrtYTDurationNC = null;
	Long sellingGeneralAndAdministrativeExpensesPr1YTDuration = null;
	Long sellingGeneralAndAdministrativeExpensesPr1YTDurationNC = null;
	// 営業利益
	Long operatingIncomeCrtYTDuration = null;
	Long operatingIncomeCrtYTDurationNC = null;
	Long operatingIncomePr1YTDuration = null;
	Long operatingIncomePr1YTDurationNC = null;
	// 営業外収益
	Long nonOperatingIncomeCrtYTDuration = null;
	Long nonOperatingIncomeCrtYTDurationNC = null;
	Long nonOperatingIncomePr1YTDuration = null;
	Long nonOperatingIncomePr1YTDurationNC = null;
	// 営業外費用
	Long nonOperatingExpensesCrtYTDuration = null;
	Long nonOperatingExpensesCrtYTDurationNC = null;
	Long nonOperatingExpensesPr1YTDuration = null;
	Long nonOperatingExpensesPr1YTDurationNC = null;
	// 経常利益
	Long ordinaryIncomeCrtYTDuration = null;
	Long ordinaryIncomeCrtYTDurationNC = null;
	Long ordinaryIncomePr1YTDuration = null;
	Long ordinaryIncomePr1YTDurationNC = null;
	// 特別利益
	Long extraordinaryIncomeCrtYTDuration = null;
	Long extraordinaryIncomeCrtYTDurationNC = null;
	Long extraordinaryIncomePr1YTDuration = null;
	Long extraordinaryIncomePr1YTDurationNC = null;
	// 特別損失
	Long extraordinaryLossCrtYTDuration = null;
	Long extraordinaryLossCrtYTDurationNC = null;
	Long extraordinaryLossPr1YTDuration = null;
	Long extraordinaryLossPr1YTDurationNC = null;
	// 税引前利益
	Long incomeBeforeIncomeTaxesCrtYTDuration = null;
	Long incomeBeforeIncomeTaxesCrtYTDurationNC = null;
	Long incomeBeforeIncomeTaxesPr1YTDuration = null;
	Long incomeBeforeIncomeTaxesPr1YTDurationNC = null;
	// 純利益
	Long profitLossCrtYTDuration = null;
	Long profitLossCrtYTDurationNC = null;
	Long profitLossPr1YTDuration = null;
	Long profitLossPr1YTDurationNC = null;
	// 当社株主帰属純利益
	Long profitLossAttributableToOwnersOfParentCrtYTDuration = null;
	Long profitLossAttributableToOwnersOfParentCrtYTDurationNC = null;
	Long profitLossAttributableToOwnersOfParentPr1YTDuration = null;
	Long profitLossAttributableToOwnersOfParentPr1YTDurationNC = null;

	void showPLInfo() {
		System.out.println("== P/L情報");
		System.out.println(" 連結");
		System.out.println("  売上高:\t" + netSales[0] + " <- " + netSales[1]);
		System.out.println("  売上原価:\t" + costOfSalesCrtYTDuration + " <- " + costOfSalesPr1YTDuration);
		System.out.println("  売上総利益:\t" + grossProfitCrtYTDuration + " <- " + grossProfitPr1YTDuration);
		System.out.println("  販管費:\t" + sellingGeneralAndAdministrativeExpensesCrtYTDuration + " <- "
				+ sellingGeneralAndAdministrativeExpensesPr1YTDuration);
		System.out.println("  営業利益:\t" + operatingIncomeCrtYTDuration + " <- " + operatingIncomePr1YTDuration);
		System.out.println("  営業外収益:\t" + nonOperatingIncomeCrtYTDuration + " <- " + nonOperatingIncomePr1YTDuration);
		System.out
				.println("  営業外費用:\t" + nonOperatingExpensesCrtYTDuration + " <- " + nonOperatingExpensesPr1YTDuration);
		System.out.println("  経常利益:\t" + ordinaryIncomeCrtYTDuration + " <- " + ordinaryIncomePr1YTDuration);
		System.out.println("  特別利益:\t" + extraordinaryIncomeCrtYTDuration + " <- " + extraordinaryIncomePr1YTDuration);
		System.out.println("  特別損失:\t" + extraordinaryLossCrtYTDuration + " <- " + extraordinaryLossPr1YTDuration);
		System.out.println(
				"  税引前利益:\t" + incomeBeforeIncomeTaxesCrtYTDuration + " <- " + incomeBeforeIncomeTaxesPr1YTDuration);
		System.out.println("  純利益:\t" + profitLossCrtYTDuration + " <- " + profitLossPr1YTDuration);
		System.out.println("  当社株主帰属純利益:\t" + profitLossAttributableToOwnersOfParentCrtYTDuration + " <- "
				+ profitLossAttributableToOwnersOfParentPr1YTDuration);
		System.out.println(" 単体");
		System.out.println("  売上高:\t" + netSalesNC[0] + " <- " + netSalesNC[1]);
		System.out.println("  売上原価:\t" + costOfSalesCrtYTDurationNC + " <- " + costOfSalesPr1YTDurationNC);
		System.out.println("  売上総利益:\t" + grossProfitCrtYTDurationNC + " <- " + grossProfitPr1YTDurationNC);
		System.out.println("  販管費:\t" + sellingGeneralAndAdministrativeExpensesCrtYTDurationNC + " <- "
				+ sellingGeneralAndAdministrativeExpensesPr1YTDurationNC);
		System.out.println("  営業利益:\t" + operatingIncomeCrtYTDurationNC + " <- " + operatingIncomePr1YTDurationNC);
		System.out
				.println("  営業外収益:\t" + nonOperatingIncomeCrtYTDurationNC + " <- " + nonOperatingIncomePr1YTDurationNC);
		System.out.println(
				"  営業外費用:\t" + nonOperatingExpensesCrtYTDurationNC + " <- " + nonOperatingExpensesPr1YTDurationNC);
		System.out.println("  経常利益:\t" + ordinaryIncomeCrtYTDurationNC + " <- " + ordinaryIncomePr1YTDurationNC);
		System.out.println(
				"  特別利益:\t" + extraordinaryIncomeCrtYTDurationNC + " <- " + extraordinaryIncomePr1YTDurationNC);
		System.out.println("  特別損失:\t" + extraordinaryLossCrtYTDurationNC + " <- " + extraordinaryLossPr1YTDurationNC);
		System.out.println("  税引前利益:\t" + incomeBeforeIncomeTaxesCrtYTDurationNC + " <- "
				+ incomeBeforeIncomeTaxesPr1YTDurationNC);
		System.out.println("  純利益:\t" + profitLossCrtYTDurationNC + " <- " + profitLossPr1YTDurationNC);
		System.out.println("  当社株主帰属純利益:\t" + profitLossAttributableToOwnersOfParentCrtYTDurationNC + " <- "
				+ profitLossAttributableToOwnersOfParentPr1YTDurationNC);

		return;
	}

	// ==================================================================================
	String output() {

		String date = "";

		if (null != currentYearInstant) {
			date = currentYearInstant;
		} else if (null != currentQuarterInstant) {
			date = currentQuarterInstant;
		} else if (null != currentQuarterInstantNonConsolidated) {
			date = currentQuarterInstantNonConsolidated;
		}

		String str = "";
		str = str + eCode + "\t" + entName;
		str = str + "\t" + date;
		str = str + "\t" + docType;

		boolean consolidated = false;
		if ((null != netSales[0]) || (null != operatingRevenue1SummaryOfBusinessResults[0])) {
			consolidated = true;
		}
		
		if (consolidated) {
			// 連結決算がある場合
			if (null != netSales[0]) {
				str = str + "\t" + toStr(netSales[0]);
			} else {
				str = str + "\t" + toStr(operatingRevenue1SummaryOfBusinessResults[0]);
			}
			str = str + "\t" + toStr(operatingIncomeCrtYTDuration);
			str = str + "\t" + toStr(nonOperatingIncomeCrtYTDuration);
			str = str + "\t" + toStr(nonOperatingExpensesCrtYTDuration);
			str = str + "\t" + toStr(ordinaryIncomeCrtYTDuration);
			str = str + "\t" + toStr(extraordinaryIncomeCrtYTDuration);
			str = str + "\t" + toStr(extraordinaryLossCrtYTDuration);
			str = str + "\t" + toStr(incomeBeforeIncomeTaxesCrtYTDuration);
			str = str + "\t" + toStr(profitLossCrtYTDuration);
			str = str + "\t" + toStr(profitLossAttributableToOwnersOfParentCrtYTDuration);
		} else {
			// 連結決算がない場合
			if (null != netSalesNC[0]) {
				str = str + "\t" + toStr(netSalesNC[0]);
			} else {
				str = str + "\t" + toStr(operatingRevenue1SummaryOfBusinessResultsNC[0]);
			}
			str = str + "\t" + toStr(operatingIncomeCrtYTDurationNC);
			str = str + "\t" + toStr(nonOperatingIncomeCrtYTDurationNC);
			str = str + "\t" + toStr(nonOperatingExpensesCrtYTDurationNC);
			str = str + "\t" + toStr(ordinaryIncomeCrtYTDurationNC);
			str = str + "\t" + toStr(extraordinaryIncomeCrtYTDurationNC);
			str = str + "\t" + toStr(extraordinaryLossCrtYTDurationNC);
			str = str + "\t" + toStr(incomeBeforeIncomeTaxesCrtYTDurationNC);
			str = str + "\t" + toStr(profitLossCrtYTDurationNC);
			str = str + "\t" + toStr(profitLossAttributableToOwnersOfParentCrtYTDurationNC);
		}
		return str;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	private String toStr(Long v) {
		String str = "";
		if (null != v) {
			str = str + (v / 1000000);
		} else {
			str = str + "null";
		}
		
		return str;
	}
	
	
	// ==================================================================================
	// テキストデータ
	//
	String orgText = "";
	String trnsText = "";
	
	/**
	 *
	 */
	void output2(String path) {

		String ty = "--";
		String date = null;
		
		if (null == docType) {
			System.out.println("***Error "+ entName + ":" + docType);
		}
		
		
		if (docType.contains("有価証券報告書")) {
			ty = "YH";
			date = currentYearInstant;
		} else if (docType.contains("四半期報告書")) {
			ty = "QR";

			if (null != currentQuarterInstant)
				date = currentQuarterInstant;
			else { // 場当たり的なので要見直し
				date = currentQuarterInstantNonConsolidated;
			}
		}


		String fName = ty + "(" + eCode + "_" + entName + ")" + date + "_" + numSubmission + ".txt";
		if (null == date)
			System.out.println("Err!..." + fName + ":" + xbrlUri);

		
		FileWriter fw = null;
		try {
			File file = new File(path + fName);
			fw = new FileWriter(file);
			fw.write(trnsText);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}
}
