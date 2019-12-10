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
 * @version 2019.12.10
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

		//  日立製作所 ASR 201803
//				String path = "X:/2018/6/29/S100DJ2G/S100DJ2G/XBRL/PublicDoc/jpcrp030000-asr-001_E01737-000_2018-03-31_01_2018-06-29.xbrl";

		// パナソニック ASR 201803
//				String path = "X:/2018/6/29/S100DI5Q/S100DI5Q/XBRL/PublicDoc/jpcrp030000-asr-001_E01772-000_2018-03-31_01_2018-06-29.xbrl";

		// シャープ ASR 201803
//				String path = "X:/2018/6/21/S100D8OO/S100D8OO/XBRL/PublicDoc/jpcrp030000-asr-001_E01773-000_2018-03-31_01_2018-06-21.xbrl";

		// ソニー ASR 201803
//		String path = "X:/2018/6/19/S100D83E/S100D83E/XBRL/PublicDoc/jpcrp030000-asr-001_E01777-000_2018-03-31_01_2018-06-19.xbrl";

		// 東芝 ASR 201803
//		String path = "X:/2018/6/27/S100DD6J/S100DD6J/XBRL/PublicDoc/jpcrp030000-asr-001_E01738-000_2018-03-31_01_2018-06-27.xbrl";

		// 三菱電機 ASR 201803
//		String path = "X:/2018/6/28/S100D9SC/S100D9SC/XBRL/PublicDoc/jpcrp030000-asr-001_E01739-000_2018-03-31_01_2018-06-28.xbrl";

		// 日本電気 ASR 201803
//		String path = "X:/2018/6/25/S100DAXT/S100DAXT/XBRL/PublicDoc/jpcrp030000-asr-001_E01765-000_2018-03-31_01_2018-06-25.xbrl";

		// 富士通 ASR 201803
//		String path = "X:/2018/6/25/S100DCSS/S100DCSS/XBRL/PublicDoc/jpcrp030000-asr-001_E01766-000_2018-03-31_01_2018-06-25.xbrl";

		// 東京急行電鉄 ASR 201803
//		String path = "X:/2018/6/28/S100DG8K/S100DG8K/XBRL/PublicDoc/jpcrp030000-asr-001_E04090-000_2018-03-31_01_2018-06-28.xbrl";

		// 小田急電鉄 ASR 201803
		String path = "X:/2018/6/28/S100DHC7/S100DHC7/XBRL/PublicDoc/jpcrp030000-asr-001_E04088-000_2018-03-31_01_2018-06-28.xbrl";


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
		if (v.length() < 64) {
			System.out.print(v + "\t");
		} else {
			String head = v.substring(0, 32);
			String tail = v.substring(v.length() - 16, v.length());

			System.out.print(head + " ... " + tail + "\t");
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
