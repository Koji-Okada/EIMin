package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import jp.ac.tcu.okadak.ei_mining.text_mining.text_extractor.TextFormatter;

/**
 * XBRL項目抽出器.
 *
 * @author K.Okada
 * @version 2019.11.30
 */
public class XBRLItemExtractor {

	/**
	 *
	 *
	 * @param args デフォルト
	 */
	public static void main(final String[] args) {

		System.out.println("Start ... XBRL Item Extracyor");

		XBRLItemExtractor itemExtractor = new XBRLItemExtractor();

		//  日立製作所 201803
//		String path = "X:/2018/6/29/S100DJ2G/S100DJ2G/XBRL/PublicDoc/jpcrp030000-asr-001_E01737-000_2018-03-31_01_2018-06-29.xbrl";

		// パナソニック 201803
//		String path = "X:/2018/6/29/S100DI5Q/S100DI5Q/XBRL/PublicDoc/jpcrp030000-asr-001_E01772-000_2018-03-31_01_2018-06-29.xbrl";

		// シャープ 201803
		String path = "X:/2018/6/21/S100D8OO/S100D8OO/XBRL/PublicDoc/jpcrp030000-asr-001_E01773-000_2018-03-31_01_2018-06-21.xbrl";

		itemExtractor.extract(path);

		System.out.println("      ... Fin");

		return;
	}

	/**
	 *
	 */
	void extract(String filePath) {

		try {
			// XML解析器を用意する
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			// XML解析器で、XBRLファイルを解析する
			Document document = documentBuilder.parse(new File(filePath));

			int i = 0;
			Node root = document.getDocumentElement();
			System.out.println(root.getNodeName());

			Node subNode = root.getFirstChild();
			while (null != subNode) {

				if (Node.ELEMENT_NODE == subNode.getNodeType()) {
					i++;

					//	if (subNode.getNodeName().equals(
					//	"jpcrp_cor:RevenueIFRSSummaryOfBusinessResults")) {

					extractIndex((Element) subNode);

					//					System.out.println("========");
				}
				//					System.out.println(subNode.getNodeName());
				//				}

				subNode = subNode.getNextSibling();
			}
			System.out.println(i);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 *
	 */
	private void extractIndex(final Element el) {

		TextFormatter form = new TextFormatter();

		String elName = el.getNodeName(); // 要素名
		System.out.print(elName + "\t");


		// 値
		String value = el.getTextContent();
		String v = form.removeWhiteSpace(value);
		if (v.length() < 32) {
		System.out.print(v + "\t");
		} else {
			System.out.print("** LongText **" + "\t");
		}

		// 属性
		NamedNodeMap nm = el.getAttributes();

		int max = nm.getLength();
		for (int i = 0; i < max; i++) {

			Node aNode = nm.item(i);
			System.out.print(aNode.getNodeName() + ":\t");
			System.out.print(aNode.getNodeValue() + "\t");
		}

		System.out.println();

		return;
	}

}
