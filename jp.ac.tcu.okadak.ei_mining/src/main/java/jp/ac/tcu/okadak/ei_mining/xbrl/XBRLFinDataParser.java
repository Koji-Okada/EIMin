package jp.ac.tcu.okadak.ei_mining.xbrl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XBRLFinDataParser extends XBRLDataParser{

	
	XBRLData dt;
	
	/**
	 *
	 * @param node
	 */
	void get(XBRLData data, Node node) {

		this.dt = data;
		
		getFinJPGAAP(node);		// 国内会計基準
//		getFinancialInfo(node);

		return;
	}

	// ==================================================================================
	/**
	 * 財務データ抽出
	 *
	 *
	 * @param node
	 */

	// 期間-年-連結
	final String[] YDC = { "CurrentYearDuration", "Prior1YearDuration", "Prior2YearDuration", "Prior3YearDuration",
			"Prior4YearDuration" };

	// 期間-年-単体
	final String[] YDNC = { "CurrentYearDuration_NonConsolidatedMember", "Prior1YearDuration_NonConsolidatedMember",
			"Prior2YearDuration_NonConsolidatedMember", "Prior3YearDuration_NonConsolidatedMember",
			"Prior4YearDuration_NonConsolidatedMember" };

	
	// CurrentYTDDuration, Prior1YTDDuration
	
	// 期間-四半期-連結
	final String[] QDC = { "CurrentYTDDuration", "Prior1YTDDuration", "Prior2YTDDuration",
			"Prior3YTDDuration", "Prior4YTDDuration" };
//	final String[] QDC = { "CurrentQuarterDuration", "Prior1QuarterDuration", "Prior2QuarterDuration",
//			"Prior3QuarterDuration", "Prior4QuarterDuration" };

	// 期間-四半期-単体
	final String[] QDNC = { "CurrentQuarterDuration_NonConsolidatedMember", "Prior1QuarterDuration_NonConsolidatedMember",
			"Prior2QuarterDuration_NonConsolidatedMember",
			"Prior3QuarterDuration_NonConsolidatedMember", "Prior4QuarterDuration_NonConsolidatedMember" };

	// 日時-年-連結
	final String[] YIC = { "CurrentYearInstant", "Prior1YearInstant",
			"Prior2YearInstant", "Prior3YearInstant",
			"Prior4YearInstant"} ;

	// 日時-年-連結
	final String[] YINC = { "CurrentYearInstant_NonConsolidatedMember", "Prior1YearInstant_NonConsolidatedMember",
			"Prior2YearInstant_NonConsolidatedMember", "Prior3YearInstant_NonConsolidatedMember",
			"Prior4YearInstant_NonConsolidatedMember"} ;
		
	// 日時-四半期-単体
	final String[] QIC = { "CurrentQuarterInstant", "Prior1QuarterInstant",
			"Prior2QuarterInstant", "Prior3QuarterInstant",
			"Prior4QuarterInstant"} ;
	
	// 日時-四半期-単体
	final String[] QINC = { "CurrentQuarterInstant_NonConsolidatedMember", "Prior1QuarterInstant_NonConsolidatedMember",
			"Prior2QuarterInstant_NonConsolidatedMember", "Prior3QuarterInstant_NonConsolidatedMember",
			"Prior4QuarterInstant_NonConsolidatedMember"} ;
	
	// ==================================================================================
	/**
	 * 
	 */
	void getFinJPGAAP(Node node) {

		String nodeName = node.getNodeName();
		Node childNode = node.getFirstChild();

		if (null == childNode)
			return; // 具体的な値が無ければ調べる意味がない

		// 売上高を抽出する
		if (nodeName.equals("jppfs_cor:NetSales")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.netSalesCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.netSalesPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 売上原価を抽出する
		if (nodeName.equals("jppfs_cor:CostOfSales")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.costOfSalesCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.costOfSalesPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 売上総利益を抽出する
		if (nodeName.equals("jppfs_cor:GrossProfit")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.grossProfitCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.grossProfitPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 販管費を抽出する
		if (nodeName.equals("jppfs_cor:SellingGeneralAndAdministrativeExpenses")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.sellingGeneralAndAdministrativeExpensesCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.sellingGeneralAndAdministrativeExpensesPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 営業利益を抽出する
		if (nodeName.equals("jppfs_cor:OperatingIncome")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.operatingIncomeCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.operatingIncomePr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 営業外収益を抽出する
		if (nodeName.equals("jppfs_cor:NonOperatingIncome")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.nonOperatingIncomeCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.nonOperatingIncomePr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 営業外費用を抽出する
		if (nodeName.equals("jppfs_cor:NonOperatingExpenses")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.nonOperatingExpensesCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.nonOperatingExpensesPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 経常利益を抽出する
		if (nodeName.equals("jppfs_cor:OrdinaryIncome")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.ordinaryIncomeCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.ordinaryIncomePr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 特別損失を抽出する
		if (nodeName.equals("jppfs_cor:ExtraordinaryLoss")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.extraordinaryLossCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.extraordinaryLossPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 税引前利益を抽出する
		if (nodeName.equals("jppfs_cor:IncomeBeforeIncomeTaxes")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.incomeBeforeIncomeTaxesCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.incomeBeforeIncomeTaxesPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 純利益を抽出する
		if (nodeName.equals("jppfs_cor:ProfitLoss")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.profitLossCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.profitLossPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		// 純利益を抽出する
		if (nodeName.equals("jppfs_cor:ProfitLossAttributableToOwnersOfParent")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					if (term.equals(YDC[0]) || term.equals(QDC[0])) {
						String value = childNode.getNodeValue();
						this.dt.profitLossAttributableToOwnersOfParentCrtYTDuration = Long.valueOf(value);
					} else if (term.equals(YDC[1]) || term.equals(QDC[1])) {
						String value = childNode.getNodeValue();
						this.dt.profitLossAttributableToOwnersOfParentPr1YTDuration = Long.valueOf(value);
					}
					
					// 単体決算に関して
				}
			}
		}
		
		return;
	}

	// ==================================================================================
	String[] netSales = new String[5];
	String[] netSalesNC = new String[5];

	// 基本
	// JPGAAP:NetSalesSummaryOfBusinessResults
	// USGAAP:RevenuesUSGAAPSummaryOfBusinessResults
	// IFRS :RevenueIFRSSummaryOfBusinessResults
	String[] netSales0 = new String[5];
	String[] netSalesNC0 = new String[5];

	// 代替 (OperatingRevenue1SummaryOfBusinessResults)
	String[] netSales1 = new String[5];
	String[] netSalesNC1 = new String[5];
	// 代替 (OperatingRevenue2SummaryOfBusinessResults)
	String[] netSales2 = new String[5];
	String[] netSalesNC2 = new String[5];
	// 代替 (OrdinaryIncomeSummaryOfBusinessResults)
	String[] netSales3 = new String[5];
	String[] netSalesNC3 = new String[5];

	/**
	 *
	 * @param node
	 */
	void getFinancialInfo(Node node) {

		String nodeName = node.getNodeName();
		Node childNode = node.getFirstChild();

		if (null == childNode)
			return; // 具体的な値が無ければ調べる意味がない

		// 売上高を抽出する
		if (nodeName.equals("jpcrp_cor:NetSalesSummaryOfBusinessResults")
				|| nodeName.equals("jpcrp_cor:RevenuesUSGAAPSummaryOfBusinessResults")
				|| nodeName.equals("jpcrp_cor:RevenueIFRSSummaryOfBusinessResults")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDC[j])) {
							netSales0[j] = childNode.getNodeValue();
						}
					}

					// 単体決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDNC[j])) {
							netSalesNC0[j] = childNode.getNodeValue();
						}
					}
				}
			}
		} else if (nodeName.equals("jpcrp_cor:OperatingRevenue1SummaryOfBusinessResults")) {
			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDC[j])) {
							netSales1[j] = childNode.getNodeValue();
						}
					}

					// 単体決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDNC[j])) {
							netSalesNC1[j] = childNode.getNodeValue();
						}
					}
				}
			}
		} else if (nodeName.equals("jpcrp_cor:OperatingRevenue2SummaryOfBusinessResults")) {
			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDC[j])) {
							netSales2[j] = childNode.getNodeValue();
						}
					}

					// 単体決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDNC[j])) {
							netSalesNC2[j] = childNode.getNodeValue();
						}
					}
				}
			}
		} else if (nodeName.equals("jpcrp_cor:OrdinaryIncomeSummaryOfBusinessResults")) {
			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("contextRef")) {
					String term = attr.item(i).getNodeValue();

					// 連結決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDC[j])) {
							netSales3[j] = childNode.getNodeValue();
						}
					}

					// 単体決算に関して
					for (int j = 0; j < 5; j++) {
						if (term.equals(YDNC[j])) {
							netSalesNC3[j] = childNode.getNodeValue();
						}
					}
				}
			}
		}
	}

	// ==========================================================================================================
	/**
	 *
	 * @return
	 */
	void output() {

		String res;

		int count1 = 0;
		if (null != netSales0[0])
			count1++;
		if (null != netSales1[0])
			count1++;
		if (null != netSales2[0])
			count1++;
		if (null != netSales3[0])
			count1++;

		int count2 = 0;
		if (null != netSalesNC0[0])
			count2++;
		if (null != netSalesNC1[0])
			count2++;
		if (null != netSalesNC2[0])
			count2++;
		if (null != netSalesNC3[0])
			count2++;

		String date = dt.currentYearInstant;
		if (null == date) {
			date = dt.currentQuarterInstant;
		}
		if (null == date) {
			date = dt.currentQuarterInstantNonConsolidated;
		}

		// res = count1 + "," + count2 + ":" + entName + "," + sCode + "," + eCode + ","
		// + acStandard + "," + numSubmission + "," + currentYearEndDate;
		res = dt.entName + "," + dt.sCode + "," + dt.eCode + "," + dt.acStandard + "," + dt.numSubmission + "," + date;

		for (int i = 0; i < 5; i++) {

			if (null == netSales[i]) {
				netSales[i] = netSales0[i];
			}
			if (null == netSales[i]) {
				netSales[i] = netSales1[i];
			}
			if (null == netSales[i]) {
				netSales[i] = netSales2[i];
			}
			if (null == netSales[i]) {
				netSales[i] = netSales3[i];
			}

			if (null == netSalesNC[i]) {
				netSalesNC[i] = netSalesNC0[i];
			}
			if (null == netSalesNC[i]) {
				netSalesNC[i] = netSalesNC1[i];
			}
			if (null == netSalesNC[i]) {
				netSalesNC[i] = netSalesNC2[i];
			}
			if (null == netSalesNC[i]) {
				netSalesNC[i] = netSalesNC3[i];
			}
		}

		if (null != netSales[0]) {
			// 連結決算がある場合
//			for (int i = 0; i < 5; i++) {
			for (int i = 0; i < 2; i++) {
				res = res + "," + netSales[i];
			}
		} else {
			// 単体決算しかない場合
//			for (int i = 0; i < 5; i++) {
			for (int i = 0; i < 2; i++) {

				res = res + "," + netSalesNC[i];
			}
		}

		System.out.println(res);

		return;
	}
}
