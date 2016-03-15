/*
 * 创建日期 2005-7-26
 */

package cn.com.info21.workflow.engine;
import java.io.StringReader;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
/**
 * @author lkh
 */

public class TargetHome {
    public Vector getTargets(String xmlstr) {
        Vector targets = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = null;
    		doc = db.parse(new InputSource(new StringReader(xmlstr)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targets;
    }
}
