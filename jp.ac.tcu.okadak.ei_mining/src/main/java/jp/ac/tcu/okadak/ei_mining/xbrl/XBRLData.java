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
	Long netSalesCrtYTDuration = null;	// 売上高
	Long netSalesPr1YTDuration = null;
	Long costOfSalesCrtYTDuration = null;	//売上原価
	Long costOfSalesPr1YTDuration = null;
	Long grossProfitCrtYTDuration = null;	// 売上総利益
	Long grossProfitPr1YTDuration = null;
	Long sellingGeneralAndAdministrativeExpensesCrtYTDuration = null;	// 販管費
	Long sellingGeneralAndAdministrativeExpensesPr1YTDuration = null;
	Long operatingIncomeCrtYTDuration = null;	// 営業利益
	Long operatingIncomePr1YTDuration = null;
	Long nonOperatingIncomeCrtYTDuration = null;	// 営業外収益
	Long nonOperatingIncomePr1YTDuration = null;
	Long nonOperatingExpensesCrtYTDuration = null;	// 営業外費用
	Long nonOperatingExpensesPr1YTDuration = null;
	Long ordinaryIncomeCrtYTDuration = null;	// 経常利益
	Long ordinaryIncomePr1YTDuration = null;
	Long extraordinaryIncomeCrtYTDuration = null;	// 特別利益
	Long extraordinaryIncomePr1YTDuration = null;
	Long extraordinaryLossCrtYTDuration = null;	// 特別損失
	Long extraordinaryLossPr1YTDuration = null;
	Long incomeBeforeIncomeTaxesCrtYTDuration = null;	// 税引前利益
	Long incomeBeforeIncomeTaxesPr1YTDuration = null;
	Long profitLossCrtYTDuration = null;	// 純利益
	Long profitLossPr1YTDuration = null;
	Long profitLossAttributableToOwnersOfParentCrtYTDuration = null;	// 当社株主帰属純利益
	Long profitLossAttributableToOwnersOfParentPr1YTDuration = null;
	
	
	void showPLInfo() {
		System.out.println("== P/L情報");
		
		System.out.println("  売上高:\t" + netSalesCrtYTDuration + " <- " + netSalesPr1YTDuration);
		System.out.println("  売上原価:\t" + costOfSalesCrtYTDuration + " <- " + costOfSalesPr1YTDuration);
		System.out.println("  売上総利益:\t" + grossProfitCrtYTDuration + " <- " + grossProfitPr1YTDuration);
		System.out.println("  販管費:\t" + sellingGeneralAndAdministrativeExpensesCrtYTDuration + " <- " + sellingGeneralAndAdministrativeExpensesPr1YTDuration);
		System.out.println("  営業利益:\t" + operatingIncomeCrtYTDuration + " <- " + operatingIncomePr1YTDuration);
		System.out.println("  営業外収益:\t" + nonOperatingIncomeCrtYTDuration + " <- " + nonOperatingIncomePr1YTDuration);
		System.out.println("  営業外費用:\t" + nonOperatingExpensesCrtYTDuration + " <- " + nonOperatingExpensesPr1YTDuration);
		System.out.println("  経常利益:\t" + ordinaryIncomeCrtYTDuration + " <- " + ordinaryIncomePr1YTDuration);
		System.out.println("  特別利益:\t" + extraordinaryIncomeCrtYTDuration + " <- " + extraordinaryIncomePr1YTDuration);
		System.out.println("  特別損失:\t" + extraordinaryLossCrtYTDuration + " <- " + extraordinaryLossPr1YTDuration);
		System.out.println("  税引前利益:\t" + incomeBeforeIncomeTaxesCrtYTDuration + " <- " + incomeBeforeIncomeTaxesPr1YTDuration);
		System.out.println("  純利益:\t" + profitLossCrtYTDuration + " <- " + profitLossPr1YTDuration);
		System.out.println("  当社株主帰属純利益:\t" + profitLossAttributableToOwnersOfParentCrtYTDuration + " <- " + profitLossAttributableToOwnersOfParentPr1YTDuration);

	return;
	}
}
