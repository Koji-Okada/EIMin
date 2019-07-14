package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram_w_dependency;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * Cabochaの -i3 分析結果をロードする.
 *
 * @author K.Okada
 * @version 2019.07.14
 */
public class CabochaLoader {

	public static void main(String[] args) {

		System.out.println("Start ... MN++Gram with Dependency");

		CabochaLoader loader = new CabochaLoader();
		loader.analyze("C:/Users/Okada/Documents/input.xml");

		System.out.println("      ... Fin");

		return;
	}

	/**
	 *
	 * 分析する.
	 *
	 * @param filePath		対象ファイルのパス
	 */
	void analyze(String filePath) {

		Document document = this.loadXMLFile(filePath);
		Node rootNode = document.getDocumentElement();
		this.analyzeDocument(rootNode);

		return;
	}

	/**
	 *
	 */
	Document loadXMLFile(String filePath) {

		Document document = null;

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			document = documentBuilder.parse(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 *
	 * @param document
	 */
	void analyzeDocument(Node rootNode) {

		NodeList sentenceNodes = rootNode.getChildNodes();

		for (int i = 0; i < sentenceNodes.getLength(); i++) {
			Node sentenceNode = sentenceNodes.item(i);

			if (Node.ELEMENT_NODE == sentenceNode.getNodeType()) {
				System.out.println(sentenceNode.getNodeName());

				analyzeSenetence(sentenceNode);
			}
			sentenceNode = sentenceNode.getNextSibling();
		}

		return;
	}

	/**
	 *
	 * @param sentenceNode
	 */
	void analyzeSenetence(Node sentenceNode) {

		NodeList chunkNodes = sentenceNode.getChildNodes();

		for (int i = 0; i < chunkNodes.getLength(); i++) {
			Node chunkNode = chunkNodes.item(i);

			if (Node.ELEMENT_NODE == chunkNode.getNodeType()) {
				System.out.println("\t" + chunkNode.getNodeName());

				analyzeChunk(chunkNode);
			}
			chunkNode = chunkNode.getNextSibling();
		}

		return;
	}

	/**
	 *
	 * @param chunkNode
	 */
	void analyzeChunk(Node chunkNode) {

		NodeList tokNodes = chunkNode.getChildNodes();

		for (int i = 0; i < tokNodes.getLength(); i++) {
			Node tokNode = tokNodes.item(i);

			if (Node.ELEMENT_NODE == tokNode.getNodeType()) {
				System.out.println("\t\t" + tokNode.getNodeName());

				Element el = (Element) tokNode;

				String id = el.getAttribute("id");
				String feature = el.getAttribute("feature");

				System.out.println("\t\t" + id);
				System.out.println("\t\t" + feature);
			} else {

			}
			tokNode = tokNode.getNextSibling();
		}

		return;
	}
}
