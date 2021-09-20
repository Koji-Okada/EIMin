package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XbrlDataParser {

	/**
	 *
	 * @param node
	 */
	void get(Node node) {

		getBasicInfo(node);
		getDateInfo(node);
		getFinancialInfo(node);
		getTextInfo(node);

		return;
	}

	// ===========================================================================================
	String docType = null; // 報告書種別
	String entName = null; // 企業名
	String sCode = null; // 株式コード
	String eCode = null; // EDINETコード
	String acStandard = null; // 会計基準
	String numSubmission = null; // 提出回数

	/**
	 *
	 * @param node
	 */
	void getBasicInfo(Node node) {
		String nodeName = node.getNodeName();
		Node childNode = node.getFirstChild();

		if (null == childNode)
			return; // 具体的な値が無ければ調べる意味がない

		// 報告書種別
		if (nodeName.equals("jpcrp_cor:DocumentTitleCoverPage")) {
			docType = childNode.getNodeValue();
//			System.out.println(docType);
		}

		// 企業名
		if (nodeName.equals("jpdei_cor:FilerNameInJapaneseDEI")) {
			entName = childNode.getNodeValue();
			// System.out.println(entName);
		}

		// 株式コード
		if (nodeName.equals("jpdei_cor:SecurityCodeDEI")) {
			sCode = childNode.getNodeValue();
			// System.out.println(sCode);
		}

		// EDINETコード
		if (nodeName.equals("jpdei_cor:EDINETCodeDEI")) {
			eCode = childNode.getNodeValue();
			// System.out.println(eCode);
		}

		// 会計基準
		if (nodeName.equals("jpdei_cor:AccountingStandardsDEI")) {
			acStandard = childNode.getNodeValue();
			// System.out.println(acStandard);
		}

		// 提出回数
		if (nodeName.equals("jpdei_cor:NumberOfSubmissionDEI")) {
			numSubmission = childNode.getNodeValue();
		}

		return;
	}

	// ==================================================================================
	String currentYearInstant = null;
	String prior1YearInstant = null;
	String currentYearStartDate = null;
	String currentYearEndDate = null;

	String currentQuarterInstant = null;
	String currentQuarterStartDate = null;
	String currentQuarterEndDate = null;
	
	String currentQuarterInstantNonConsolidated = null;		// 単体決算の場合：追加調査が必要
	
	/**
	 * 日時データ抽出
	 *
	 * @param node
	 */
	void getDateInfo(Node node) {

		String nodeName = node.getNodeName();

		if (nodeName.equals("xbrli:context")) {

			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {

				if (attr.item(i).getNodeName().equals("id")) {
					String term = attr.item(i).getNodeValue();

					if (term.equals("CurrentYearInstant")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								currentYearInstant = getDate(e, "xbrli:instant");
								// System.out.println(currentYearInstant);
							}
						}
					} else if (term.equals("CurrentYearDuration")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								currentYearStartDate = getDate(e, "xbrli:startDate");
								// System.out.println("St:" + currentYearStartDate);
								currentYearEndDate = getDate(e, "xbrli:endDate");
								// System.out.println("Ed:" + currentYearEndDate);
							}
						}
					} 	else if (term.equals("CurrentQuarterInstant")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								currentQuarterInstant = getDate(e, "xbrli:instant");
								// System.out.println(currentQuarterInstant);
							}
						}
					} else if (term.equals("CurrentQuarterDuration")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								currentQuarterStartDate = getDate(e, "xbrli:startDate");
								// System.out.println("St:" + currentQuarterStartDate);
								currentQuarterEndDate = getDate(e, "xbrli:endDate");
								// System.out.println("Ed:" + currentQuarterEndDate);
							}
						}
						
					} 	else if (term.equals("CurrentQuarterInstant_NonConsolidatedMember")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								currentQuarterInstantNonConsolidated = getDate(e, "xbrli:instant");
								// System.out.println(currentQuarterInstant);
							}
						}	
					}
				}
			}
		}
	}

	/**
	 *
	 * @param node
	 * @param tag
	 * @return
	 */
	private String getDate(Node node, String tag) {

		String date = null;
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node e = nodeList.item(i);
			String name = e.getNodeName();
			if (name.equals(tag)) {
				Node childNode = e.getFirstChild();
				date = childNode.getNodeValue();
			}
		}

		return date;
	}

	// ==================================================================================
	/**
	 * 財務データ抽出
	 *
	 *
	 * @param node
	 */

	final String[] YDC = { "CurrentYearDuration", "Prior1YearDuration", "Prior2YearDuration", "Prior3YearDuration",
			"Prior4YearDuration" };

	final String[] YDNC = { "CurrentYearDuration_NonConsolidatedMember", "Prior1YearDuration_NonConsolidatedMember",
			"Prior2YearDuration_NonConsolidatedMember", "Prior3YearDuration_NonConsolidatedMember",
			"Prior4YearDuration_NonConsolidatedMember" };

	String[] netSales = new String[5];
	String[] netSalesNC = new String[5];

	// 基本
	// JPGAAp:NetSalesSummaryOfBusinessResults
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

	// ========================================================================================

	String orgText = "";
	String trnsText = "";

	/**
	 *
	 * @param node
	 */
	void getTextInfo(Node node) {

		String nodeName = node.getNodeName();
		Node childNode = node.getFirstChild();

		if (null == childNode)
			return; // 具体的な値が無ければ調べる意味がない

		// テキストブロック
		if (nodeName.contains("jpcrp_cor:") && nodeName.contains("TextBlock")) {

			String text = childNode.getNodeValue();

			orgText = orgText + "\n" + text;
			trnsText = trnsText + "\n" + removeHTMLTag(text);
		}

		return;
	}

	/**
	 *
	 * @param org
	 * @return
	 */
	private String removeHTMLTag(String org) {

		boolean flag = false;
		String trns = "";

		int j = 0;
		for (int i = 0; i < org.length(); i++) {
			char ch = org.charAt(i);
			if ('<' == ch) {
				flag = true;
			} else if ('>' == ch) {
				flag = false;
			} else if (!flag) {
				if ('\n' != ch) {
					trns = trns + ch;
				}
			}
		}

		return trns;
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

		
		String date = currentYearInstant;
		if (null == date) {
			date = currentQuarterInstant;
		}
		if (null == date) {
			date = currentQuarterInstantNonConsolidated;
		}
		
		// res = count1 + "," + count2 + ":" + entName + "," + sCode + "," + eCode + ","
		// + acStandard + "," + numSubmission + "," + currentYearEndDate;
		res = entName + "," + sCode + "," + eCode + "," + acStandard + "," + numSubmission + "," + date;

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

	/**
	 *
	 */
	void output2(String path) {

		String ty = "--";
		String date = null;
		if (docType.contains("有価証券報告書")) {
			ty = "YH";
			date = currentYearInstant;
		} else if (docType.contains("四半期報告書")) {
			ty = "QR";
			
			if (null != currentQuarterInstant)
				date = currentQuarterInstant;
			else {	// 場当たり的なので要見直し
				date = currentQuarterInstantNonConsolidated;
			}
		}
		
		if (null == date) System.out.println("Err!...");
		
		String fName = ty + "(" + eCode + "_" + entName + ")" + date + "_" + numSubmission + ".txt";

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
