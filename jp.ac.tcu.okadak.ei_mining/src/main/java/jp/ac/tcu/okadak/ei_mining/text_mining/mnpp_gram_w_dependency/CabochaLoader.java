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
 * @version 2019.07.15
 */
public class CabochaLoader {

    /**
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
     *
     * ロードする.
     *
     * @param filePath 対象ファイルのパス
     */
    final void load(final String filePath) {

        Document xmlDoc = this.loadXMLFile(filePath);
        Node rootNode = xmlDoc.getDocumentElement();
        this.loadDocument(rootNode);

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
     *
     * @param rootNode ルートノード
     */
    private void loadDocument(final Node rootNode) {

        NodeList sentenceNodes = rootNode.getChildNodes();

        for (int i = 0; i < sentenceNodes.getLength(); i++) {
            Node sentenceNode = sentenceNodes.item(i);

            if (Node.ELEMENT_NODE == sentenceNode.getNodeType()) {
                System.out.println(sentenceNode.getNodeName());

                loadSenetence(sentenceNode);
            }
            sentenceNode = sentenceNode.getNextSibling();
        }

        return;
    }

    /**
     *
     * @param sentenceNode 文ノード
     */
    private void loadSenetence(final Node sentenceNode) {

        NodeList chunkNodes = sentenceNode.getChildNodes();

        for (int i = 0; i < chunkNodes.getLength(); i++) {
            Node chunkNode = chunkNodes.item(i);

            if (Node.ELEMENT_NODE == chunkNode.getNodeType()) {
                System.out.println("\t" + chunkNode.getNodeName());

                loadChunk(chunkNode);
            }
            chunkNode = chunkNode.getNextSibling();
        }

        return;
    }

    /**
     *
     * @param chunkNode チャンクノード
     */
    private void loadChunk(final Node chunkNode) {

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

                //               String val = tokNodes.item(++i).getTextContent();
                //               System.out.println(val);
            }
            tokNode = tokNode.getNextSibling();
        }

        return;
    }
}
