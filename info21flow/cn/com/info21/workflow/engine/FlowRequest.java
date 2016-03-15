/*
 * 创建日期 2005-7-25
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
	 * 构造函数
	 */
	public FlowRequest() {
	}
	/**
	 * 构造函数
	 * @param par 请求体xml
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
	 * @param par 设置参数
	 */
	public void setPar(String par) {
		this.par = par;
	}
	/**
	 * @return 返回参数
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
