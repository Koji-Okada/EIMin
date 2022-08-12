package jp.ac.tcu.okadak.ei_mining.xbrl;

import org.w3c.dom.Node;

public class XBRLTextInfoParser extends XBRLDataParser {

	XBRLData dt;
	
	/**
	 *
	 * @param node
	 */
	void get(XBRLData data, Node node) {

		this.dt = data;
		
		getTextInfo(node);

		return;
	}

	// ========================================================================================

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
//		if (nodeName.contains("jpcrp_cor:") && nodeName.contains("TextBlock")) {
//			
//			String text = childNode.getNodeValue();
//
//			dt.orgText = dt.orgText + "\n" + text;
//			dt.trnsText = dt.trnsText + "\n" + removeHTMLTag(text);
//		}

		// テキストブロック
		if (nodeName.contains("jpcrp_cor:") && nodeName.contains("AnalysisOfFinancialPositionOperatingResultsAndCashFlowsTextBlock")) {
			
			String text = childNode.getNodeValue();

			dt.orgText = dt.orgText + "\n" + text;
			dt.trnsText = dt.trnsText + "\n" + removeHTMLTag(text);
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


}
