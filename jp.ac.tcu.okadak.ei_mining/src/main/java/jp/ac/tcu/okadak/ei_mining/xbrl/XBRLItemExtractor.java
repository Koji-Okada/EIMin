package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

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

		String path = "X:/2018/6/29/S100DJ2G/S100DJ2G/XBRL/PublicDoc/jpcrp030000-asr-001_E01737-000_2018-03-31_01_2018-06-29.xbrl";

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
				i++;

				System.out.println(subNode.getNodeName());

				subNode = subNode.getNextSibling();
			}
			System.out.println(i);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

}
