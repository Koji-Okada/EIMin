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
	
	
}
