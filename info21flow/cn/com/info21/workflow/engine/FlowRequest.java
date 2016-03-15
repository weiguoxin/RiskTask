/*
 * �������� 2005-7-25
 */

package cn.com.info21.workflow.engine;

import java.io.*;
import java.util.Hashtable;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author lkh
 */

public class FlowRequest {
	private String par;
	private Hashtable param;
	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	private Document doc = null;
	/**
	 * ���캯��
	 */
	public FlowRequest() {
	}
	/**
	 * ���캯��
	 * @param par ������xml
	 */
	public FlowRequest(String par) {
		try {
			this.par = par;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(this.par)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param par ���ò���
	 */
	public void setPar(String par) {
		this.par = par;
	}
	/**
	 * @return ���ز���
	 */
	public String getPar() {
		return this.par;
	}
	/**
	 * @return Returns the doc.
	 */
	public Document getDoc() {
		return doc;
	}
}
