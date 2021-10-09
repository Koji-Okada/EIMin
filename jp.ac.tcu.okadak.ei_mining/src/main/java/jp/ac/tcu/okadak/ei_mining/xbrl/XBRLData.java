package jp.ac.tcu.okadak.ei_mining.xbrl;

public class XBRLData {

	// ==================================================================================
	// 基礎データ
	//
	String docType = null; // 報告書種別
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
	Long netSalesCrtYTDuration = null;
	Long netSalesCrtYTDurationNC = null;
	Long netSalesPr1YTDuration = null;
	Long netSalesPr1YTDurationNC = null;
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
		System.out.println("  売上高:\t" + netSalesCrtYTDuration + " <- " + netSalesPr1YTDuration);
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
		System.out.println("  売上高:\t" + netSalesCrtYTDurationNC + " <- " + netSalesPr1YTDurationNC);
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

		if (null != netSalesCrtYTDuration) {
			// 連結決算がある場合
			str = str + "\t" + (netSalesCrtYTDuration / 1000000);
			str = str + "\t" + (operatingIncomeCrtYTDuration / 1000000);
			str = str + "\t" + (nonOperatingIncomeCrtYTDuration / 1000000);
			str = str + "\t" + (nonOperatingExpensesCrtYTDuration / 1000000);
			str = str + "\t" + (ordinaryIncomeCrtYTDuration / 1000000);
			if (null != extraordinaryIncomeCrtYTDuration) {
				str = str + "\t" + (extraordinaryIncomeCrtYTDuration / 1000000);
			} else {
				str = str + "\t";
			}
			if (null != extraordinaryLossCrtYTDuration) {
				str = str + "\t" + (extraordinaryLossCrtYTDuration / 1000000);
			} else {
				str = str + "\t";
			}
			str = str + "\t" + (incomeBeforeIncomeTaxesCrtYTDuration / 1000000);
			str = str + "\t" + (profitLossCrtYTDuration / 1000000);
			if (null != profitLossAttributableToOwnersOfParentCrtYTDuration) {
				str = str + "\t" + (profitLossAttributableToOwnersOfParentCrtYTDuration / 1000000);
			} else {
				str = str + "\t";
			}
		} else {
			// 連結決算がない場合
			str = str + "\t" + (netSalesCrtYTDurationNC / 1000000);
			str = str + "\t" + (operatingIncomeCrtYTDurationNC / 1000000);
			str = str + "\t" + (nonOperatingIncomeCrtYTDurationNC / 1000000);
			str = str + "\t" + (nonOperatingExpensesCrtYTDurationNC / 1000000);
			str = str + "\t" + (ordinaryIncomeCrtYTDurationNC / 1000000);
			if (null != extraordinaryIncomeCrtYTDurationNC) {
				str = str + "\t" + (extraordinaryIncomeCrtYTDurationNC / 1000000);
			} else {
				str = str + "\t";
			}
			if (null != extraordinaryLossCrtYTDurationNC) {
				str = str + "\t" + (extraordinaryLossCrtYTDurationNC / 1000000);
			} else {
				str = str + "\t";
			}
			str = str + "\t" + (incomeBeforeIncomeTaxesCrtYTDurationNC / 1000000);
			str = str + "\t" + (profitLossCrtYTDurationNC / 1000000);
			if (null != profitLossAttributableToOwnersOfParentCrtYTDurationNC) {
				str = str + "\t" + (profitLossAttributableToOwnersOfParentCrtYTDurationNC / 1000000);
			} else {
				str = str + "\t";
			}

		}

		return str;
	}

}
