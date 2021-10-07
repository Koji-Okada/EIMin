package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	 */
	void output2(String path) {

		String ty = "--";
		String date = null;
		if (dt.docType.contains("有価証券報告書")) {
			ty = "YH";
			date = dt.currentYearInstant;
		} else if (dt.docType.contains("四半期報告書")) {
			ty = "QR";

			if (null != dt.currentQuarterInstant)
				date = dt.currentQuarterInstant;
			else { // 場当たり的なので要見直し
				date = dt.currentQuarterInstantNonConsolidated;
			}
		}

		if (null == date)
			System.out.println("Err!...");

		String fName = ty + "(" + dt.eCode + "_" + dt.entName + ")" + date + "_" + dt.numSubmission + ".txt";

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
