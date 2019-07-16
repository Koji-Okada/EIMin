package jp.ac.tcu.okadak.ei_mining.text_mining.mnpp_gram_w_dependency;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Cabocha -f3 による分析結果をロードする.
 *
 * @author K.Okada
 * @version 2019.07.16
 */
public class CabochaLoader {

    /**
     * Cabocha -f3 による分析結果をロードする.
     *
     * @param args  デフォルト
     */
    public static void main(final String[] args) {

        System.out.println("Start ... MN++Gram with Dependency");

        CabochaLoader loader = new CabochaLoader();
        loader.load("C:/Users/Okada/Documents/input.xml");

        System.out.println("      ... Fin");

        return;
    }

    /**
     * ロードする.
     *
     * @param filePath 対象ファイルのパス
     */
    final void load(final String filePath) {

        Document xmlDoc = this.loadXMLFile(filePath);
        Node rootNode = xmlDoc.getDocumentElement();
        this.parseDocument(rootNode);

        return;
    }

    /**
     *  XMLドキュメントをロードする.
     *
     * @param filePath  ファイルパス
     * @return ドキュメント
     */
    final Document loadXMLFile(final String filePath) {

        Document xmlDoc = null;

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            xmlDoc = documentBuilder.parse(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlDoc;
    }

    /**
     * 文書全体をパースする.
     *
     * @param rootNode ルートノード
     */
    private void parseDocument(final Node rootNode) {

        NodeList sentenceNodes = rootNode.getChildNodes();

        for (int i = 0; i < sentenceNodes.getLength(); i++) {
            Node sentenceNode = sentenceNodes.item(i);

            if (Node.ELEMENT_NODE == sentenceNode.getNodeType()) {
                System.out.println(sentenceNode.getNodeName());

                parseSenetence(sentenceNode);
            }
            sentenceNode = sentenceNode.getNextSibling();
        }

        return;
    }

    /**
     * 文をパースする.
     *
     * @param sentenceNode 文ノード
     */
    private void parseSenetence(final Node sentenceNode) {

        NodeList chunkNodes = sentenceNode.getChildNodes();

        for (int i = 0; i < chunkNodes.getLength(); i++) {
            Node chunkNode = chunkNodes.item(i);

            if (Node.ELEMENT_NODE == chunkNode.getNodeType()) {
                System.out.println("\t" + chunkNode.getNodeName());

                parseChunk(chunkNode);
            }
            chunkNode = chunkNode.getNextSibling();
        }

        return;
    }

    /**
     * チャンクをパースする.
     *
     * @param chunkNode チャンクノード
     */
    private void parseChunk(final Node chunkNode) {

        NodeList tokNodes = chunkNode.getChildNodes();

        for (int i = 0; i < tokNodes.getLength(); i++) {
            Node tokNode = tokNodes.item(i);

            if (Node.ELEMENT_NODE == tokNode.getNodeType()) {
                System.out.println("\t\t" + tokNode.getNodeName());

                Element el = (Element) tokNode;

                // 属性を取得する
                String id = el.getAttribute("id");
                String feature = el.getAttribute("feature");

                System.out.println("\t\t" + id);
                System.out.println("\t\t" + feature);

                // テキスト部分を取得する
                String val = tokNodes.item(i).getFirstChild().getNodeValue();

                System.out.println("\t\t" + val);
            }
            tokNode = tokNode.getNextSibling();
        }

        return;
    }
}
