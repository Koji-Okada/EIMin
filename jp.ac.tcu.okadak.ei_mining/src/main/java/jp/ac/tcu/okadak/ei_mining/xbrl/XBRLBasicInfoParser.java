package jp.ac.tcu.okadak.ei_mining.xbrl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XBRLBasicInfoParser extends XBRLDataParser {

	
	XBRLData dt;
	
	/**
	 *
	 * @param node
	 */
	void get(XBRLData data, Node node) {

		this.dt = data;
		
		getBasicInfo(node);

		return;
	}
	
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
			dt.docType = childNode.getNodeValue();
//			System.out.println(docType);
		}

		// 企業名
		if (nodeName.equals("jpdei_cor:FilerNameInJapaneseDEI")) {
			dt.entName = childNode.getNodeValue();
			// System.out.println(entName);
		}

		// 株式コード
		if (nodeName.equals("jpdei_cor:SecurityCodeDEI")) {
			dt.sCode = childNode.getNodeValue();
			// System.out.println(sCode);
		}

		// EDINETコード
		if (nodeName.equals("jpdei_cor:EDINETCodeDEI")) {
			dt.eCode = childNode.getNodeValue();
			// System.out.println(eCode);
		}

		// 会計基準
		if (nodeName.equals("jpdei_cor:AccountingStandardsDEI")) {
			dt.acStandard = childNode.getNodeValue();
			// System.out.println(acStandard);
		}

		// 提出回数
		if (nodeName.equals("jpdei_cor:NumberOfSubmissionDEI")) {
			dt.numSubmission = childNode.getNodeValue();
		}

		return;
	}

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
								dt.currentYearInstant = getDate(e, "xbrli:instant");
								// System.out.println(currentYearInstant);
							}
						}
					} else if (term.equals("CurrentYearDuration")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								dt.currentYearStartDate = getDate(e, "xbrli:startDate");
								// System.out.println("St:" + currentYearStartDate);
								dt.currentYearEndDate = getDate(e, "xbrli:endDate");
								// System.out.println("Ed:" + currentYearEndDate);
							}
						}
					} else if (term.equals("CurrentQuarterInstant")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								dt.currentQuarterInstant = getDate(e, "xbrli:instant");
								// System.out.println(currentQuarterInstant);
							}
						}
					} else if (term.equals("CurrentQuarterDuration")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								dt.currentQuarterStartDate = getDate(e, "xbrli:startDate");
								// System.out.println("St:" + currentQuarterStartDate);
								dt.currentQuarterEndDate = getDate(e, "xbrli:endDate");
								// System.out.println("Ed:" + currentQuarterEndDate);
							}
						}

					} else if (term.equals("CurrentQuarterInstant_NonConsolidatedMember")) {
						NodeList nodeList = node.getChildNodes();
						for (int j = 0; j < nodeList.getLength(); j++) {
							Node e = nodeList.item(j);
							String name = e.getNodeName();
							if (name.equals("xbrli:period")) {
								dt.currentQuarterInstantNonConsolidated = getDate(e, "xbrli:instant");
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
}
