package jp.ac.tcu.okadak.ei_mining.xbrl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XBRLParser {

	/**
	 *
	 */
	XbrlInfoRecorder rec;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		//		String path = "D:\\XBRL_Data\\2020\\10\\30\\S100K029\\S100K029\\XBRL\\PublicDoc";

		// Japan GAAPの例
		//		String path1 = "D:/XBRL_Data/2020/10/12/S100JW5U/S100JW5U/XBRL/PublicDoc";
		String path1 = "X:/2020/10/12/S100JW5U/S100JW5U/XBRL/PublicDoc";

		// US GAAPの例
		//		String path2 = "D:/XBRL_Data/2020/3/27/S100IB70/S100IB70/XBRL/PublicDoc";
		String path2 = "X:/2020/3/27/S100IB70/S100IB70/XBRL/PublicDoc";

		// IFRSの例
		//		String path3 = "D:/XBRL_Data/2020/11/27/S100K9IP/S100K9IP/XBRL/PublicDoc";
		String path3 = "X:/2020/11/27/S100K9IP/S100K9IP/XBRL/PublicDoc";

		path3 = "X:\\2020\\11\\27\\S100K9IZ\\S100K9IZ\\XBRL\\PublicDoc";

		System.out.println("Start ...");

		String xbrlUri;
		XBRLParser p = new XBRLParser();

		xbrlUri = p.findXbrlFile(path1);
		p.parse(xbrlUri);

		xbrlUri = p.findXbrlFile(path2);
		p.parse(xbrlUri);

		xbrlUri = p.findXbrlFile(path3);
		p.parse(xbrlUri);

		System.out.println("... Fin.");
		return;
	}

	/** ===============================================================================================================
	 *
	 *	XBRLファイルを探し URIを返す.
	 *
	 *
	 */
	public String findXbrlFile(String path) {

		//		System.out.println(path);

		File folder = new File(path);
		File fileList[] = folder.listFiles();

		for (File f : fileList) {
			String name = f.getName();
			int pos = name.lastIndexOf(".");

			if (pos > 0) {
				String fileType = name.substring(name.lastIndexOf("."));

				if (0 == fileType.compareTo(".xbrl")) {
					return f.getAbsolutePath();
				}
			}
		}

		return null;
	}

	/** ===============================================================================================================
	 *
	 */
	public void parse(String uri) {

		//		System.out.println(uri);

		// 新規の記録器を作成する
		rec = new XbrlInfoRecorder();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			Node root = builder.parse(uri);
			parseRecursive(root);

		} catch (Exception e) {
			System.out.println(e);
		}

		//		rec.output();
		rec.output2();

		return;
	}

	/** ===============================================================================================================
	 *
	 */
	private void parseRecursive(Node node) {

		if (Node.TEXT_NODE == node.getNodeType())
			return;

		rec.get(node);

		// 子ノードに対して再帰処理
		NodeList nodeList = node.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node e = nodeList.item(i);
			parseRecursive(e);
		}

		return;
	}
}
