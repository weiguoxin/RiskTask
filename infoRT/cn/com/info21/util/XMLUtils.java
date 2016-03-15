/*
 * Created on 2004-7-12
 */

package cn.com.info21.util;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 * @author Hu Guohua
 */

public class XMLUtils {
	/**
	 * @param file XML Document file uri
	 * @return 对应的Document
	 */
	public static Document getDocument(String file) {
		Document rtn = null;
		try {
			XMLUtils util = new XMLUtils();
			InputStream in = util.getClass().getResourceAsStream(file);
			DocumentBuilderFactory dbf = null;
			DocumentBuilder db = null;
			Document doc = null;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(in);
			rtn = doc;
		} catch (Exception e) {
			rtn = null;
			e.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param xml XML Document Content
	 * @return string对应的Element
	 */
	public static Element getElement(String xml) {
		Element rtn = null;
		try {
			DocumentBuilderFactory dbf = null;
			DocumentBuilder db = null;
			Document doc = null;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(xml)));
			rtn = doc.getDocumentElement();
		} catch (Exception e) {
			rtn = null;
		}
		return rtn;
	}
	/**
	 * @param n XML Node
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static int getAttInt(Node n, String key) {
		int rtn = 0;
		try {
			rtn = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
		} catch (Exception e) {
			e.printStackTrace();
			rtn = 0;
		}
		return rtn;
	}
	/**
	 * @param n XML Node
	 * @param key 关键字
	 * @return 关键字对应的String值
	 */
	public static String getAttString(Node n, String key) {
		String rtn = "";
		try {
			rtn = n.getAttributes().getNamedItem(key).getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
			rtn = "";
		}
		return rtn;
	}
	/**
	 * @param ele XML Element
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static int getInt(Element ele, String key) {
		int rtn = 0;
		try {
			NodeList nl = ele.getElementsByTagName(key);
			rtn = Integer.parseInt(nl.item(0).getFirstChild().getNodeValue());
		} catch (Exception e) {
			e.printStackTrace();
			rtn = 0;
		}
		return rtn;
	}

	/**
	 * @param xml XML Document Content
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static int getInt(String xml, String key) {
		int rtn = 0;
		try {
			DocumentBuilderFactory dbf = null;
			DocumentBuilder db = null;
			Document doc = null;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(xml)));
			rtn = getInt(doc, key);
		} catch (Exception e) {
			rtn = 0;
		}
		return rtn;
	}
	/**
	 * @param doc XML Document
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static int getInt(Document doc, String key) {
		int rtn = 0;
		try {
			NodeList nl = doc.getElementsByTagName(key);
			rtn = Integer.parseInt(nl.item(0).getFirstChild().getNodeValue());
		} catch (Exception e) {
			e.printStackTrace();
			rtn = 0;
		}
		return rtn;
	}
	/**
	 * @param doc XML Document
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static String getString(Document doc, String key) {
		String rtn = "";
		try {
			NodeList nl = doc.getElementsByTagName(key);
			rtn = nl.item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
			rtn = "";
		}
		return rtn;
	}
	/**
	 * @param ele XML Element
	 * @param key 关键字
	 * @return 关键字对应的int值
	 */
	public static String getString(Element ele, String key) {
		String rtn = "";
		try {
			NodeList nl = ele.getElementsByTagName(key);
			rtn = nl.item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			rtn = "";
		}
		return rtn;
	}
	/**
	 * @param doc XML Document
	 * @param key 关键字
	 * @return 关键字对应的Element
	 */
	public static Element getElement(Document doc, String key) {
		Element rtn = null;
		try {
			NodeList nl = doc.getElementsByTagName(key);
			rtn = (Element) nl.item(0);
		} catch (Exception e) {
			e.printStackTrace();
			rtn = null;
		}
		return rtn;
	}
	/**
	 * @param doc XML Document
	 * @param key 关键字
	 * @return 关键字对应的Element String
	 */
	public static String getElementString(Document doc, String key) {
		String rtn = "";
		try {
			NodeList nl = doc.getElementsByTagName(key);
			rtn = nl.item(0).getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
			rtn = "";
		}
		return rtn;
	}
}
