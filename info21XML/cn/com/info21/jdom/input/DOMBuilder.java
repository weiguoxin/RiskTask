// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOMBuilder.java

package cn.com.info21.jdom.input;

import cn.com.info21.dom.Attr;
import cn.com.info21.dom.DocumentType;
import cn.com.info21.dom.NamedNodeMap;
import cn.com.info21.dom.Node;
import cn.com.info21.dom.NodeList;
import cn.com.info21.jdom.DocType;
import cn.com.info21.jdom.Document;
import cn.com.info21.jdom.Element;
import cn.com.info21.jdom.JDOMException;
import cn.com.info21.jdom.Namespace;
import cn.com.info21.jdom.adapters.DOMAdapter;
import org.xml.sax.SAXParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

// Referenced classes of package cn.com.info21.jdom.input:
//            DefaultJDOMFactory, JDOMFactory

public class DOMBuilder
{

    public DOMBuilder()
    {
        this(false);
    }

    public DOMBuilder(boolean validate)
    {
        factory = new DefaultJDOMFactory();
        setValidation(validate);
    }

    public DOMBuilder(String adapterClass)
    {
        this(adapterClass, false);
    }

    public DOMBuilder(String adapterClass, boolean validate)
    {
        factory = new DefaultJDOMFactory();
        this.adapterClass = adapterClass;
        setValidation(validate);
    }

    public void setFactory(JDOMFactory factory)
    {
        this.factory = factory;
    }

    public void setValidation(boolean validate)
    {
        this.validate = validate;
    }

    public Document build(InputStream in)
        throws JDOMException
    {
        Document doc = factory.document((Element)null);
        cn.com.info21.dom.Document domDoc = null;
        try
        {
            if(adapterClass != null)
                try
                {
                    DOMAdapter adapter = (DOMAdapter)Class.forName(adapterClass).newInstance();
                    domDoc = adapter.getDocument(in, validate);
                }
                catch(ClassNotFoundException classnotfoundexception) { }
            else
                try
                {
                    DOMAdapter adapter = (DOMAdapter)Class.forName("cn.com.info21.jdom.adapters.JAXPDOMAdapter").newInstance();
                    domDoc = adapter.getDocument(in, validate);
                }
                catch(ClassNotFoundException classnotfoundexception1) { }
                catch(NoSuchMethodException nosuchmethodexception) { }
                catch(IllegalAccessException illegalaccessexception) { }
                catch(InvocationTargetException ite)
                {
                    throw ite.getTargetException();
                }
            if(domDoc == null && adapterClass == null)
                try
                {
                    DOMAdapter adapter = (DOMAdapter)Class.forName("cn.com.info21.jdom.adapters.XercesDOMAdapter").newInstance();
                    domDoc = adapter.getDocument(in, validate);
                }
                catch(ClassNotFoundException classnotfoundexception2) { }
            buildTree(domDoc, doc, null, true);
        }
        catch(Throwable e)
        {
            if(e instanceof SAXParseException)
            {
                SAXParseException p = (SAXParseException)e;
                String systemId = p.getSystemId();
                if(systemId != null)
                    throw new JDOMException("Error on line " + p.getLineNumber() + " of document " + systemId, e);
                else
                    throw new JDOMException("Error on line " + p.getLineNumber(), e);
            } else
            {
                throw new JDOMException("Error in building from stream", e);
            }
        }
        return doc;
    }

    public Document build(File file)
        throws JDOMException
    {
        try
        {
            FileInputStream in = new FileInputStream(file);
            return build(((InputStream) (in)));
        }
        catch(FileNotFoundException e)
        {
            throw new JDOMException("Error in building from " + file, e);
        }
    }

    public Document build(URL url)
        throws JDOMException
    {
        try
        {
            return build(url.openStream());
        }
        catch(IOException e)
        {
            throw new JDOMException("Error in building from " + url, e);
        }
    }

    public Document build(cn.com.info21.dom.Document domDocument)
    {
        Document doc = factory.document((Element)null);
        buildTree(domDocument, doc, null, true);
        return doc;
    }

    public Element build(cn.com.info21.dom.Element domElement)
    {
        Document doc = factory.document((Element)null);
        buildTree(domElement, doc, null, true);
        return doc.getRootElement();
    }

    private void buildTree(Node node, Document doc, Element current, boolean atRoot)
    {
        switch(node.getNodeType())
        {
        case 2: // '\002'
        case 6: // '\006'
        default:
            break;

        case 9: // '\t'
        {
            NodeList nodes = node.getChildNodes();
            int i = 0;
            for(int size = nodes.getLength(); i < size; i++)
                buildTree(nodes.item(i), doc, current, true);

            break;
        }

        case 1: // '\001'
        {
            String localName = node.getLocalName();
            String prefix = node.getPrefix();
            String uri = node.getNamespaceURI();
            Element element = null;
            Namespace ns = null;
            if(uri == null)
            {
                if(localName == null)
                    localName = ((cn.com.info21.dom.Element)node).getTagName();
                element = factory.element(localName);
            } else
            {
                ns = Namespace.getNamespace(prefix, uri);
                element = factory.element(localName, ns);
            }
            NamedNodeMap attributeList = node.getAttributes();
            int i = 0;
            for(int size = attributeList.getLength(); i < size; i++)
            {
                Attr att = (Attr)attributeList.item(i);
                String attname = att.getName();
                String attvalue = att.getValue();
                if(attname.equals("xmlns"))
                {
                    Namespace declaredNS = Namespace.getNamespace("", attvalue);
                    if(!declaredNS.equals(ns))
                        element.addNamespaceDeclaration(declaredNS);
                } else
                if(attname.startsWith("xmlns:"))
                {
                    String attsubname = attname.substring(6);
                    Namespace declaredNS = Namespace.getNamespace(attsubname, attvalue);
                    if(!declaredNS.equals(ns))
                        element.addNamespaceDeclaration(declaredNS);
                } else
                {
                    prefix = att.getPrefix();
                    uri = att.getNamespaceURI();
                    String attLocalName = att.getLocalName();
                    if(attLocalName == null)
                        attLocalName = attname;
                    Namespace attns = Namespace.getNamespace(prefix, uri);
                    cn.com.info21.jdom.Attribute attribute = factory.attribute(attLocalName, attvalue, attns);
                    element.setAttribute(attribute);
                }
            }

            if(atRoot)
                doc.setRootElement(element);
            else
                current.addContent(element);
            NodeList children = node.getChildNodes();
            if(children == null)
                break;
            i = 0;
            for(int size = children.getLength(); i < size; i++)
            {
                Node item = children.item(i);
                if(item != null)
                    buildTree(item, doc, element, false);
            }

            break;
        }

        case 3: // '\003'
        {
            String text = node.getNodeValue();
            current.addContent(text);
            break;
        }

        case 4: // '\004'
        {
            String cdata = node.getNodeValue();
            current.addContent(factory.cdata(cdata));
            break;
        }

        case 7: // '\007'
        {
            if(atRoot)
                doc.addContent(factory.processingInstruction(node.getNodeName(), node.getNodeValue()));
            else
                current.addContent(factory.processingInstruction(node.getNodeName(), node.getNodeValue()));
            break;
        }

        case 8: // '\b'
        {
            if(atRoot)
                doc.addContent(factory.comment(node.getNodeValue()));
            else
                current.addContent(factory.comment(node.getNodeValue()));
            break;
        }

        case 5: // '\005'
        {
            cn.com.info21.jdom.EntityRef entity = factory.entityRef(node.getNodeName());
            current.addContent(entity);
            break;
        }

        case 10: // '\n'
        {
            DocumentType domDocType = (DocumentType)node;
            String publicID = domDocType.getPublicId();
            String systemID = domDocType.getSystemId();
            DocType docType = factory.docType(domDocType.getName());
            if(publicID != null && !publicID.equals(""))
                docType.setPublicID(publicID);
            if(systemID != null && !systemID.equals(""))
                docType.setSystemID(systemID);
            doc.setDocType(docType);
            break;
        }
        }
    }

    private static final String CVS_ID = "@(#) $RCSfile: DOMBuilder.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:30 $ $Name:  $";
    private static final String DEFAULT_ADAPTER_CLASS = "cn.com.info21.jdom.adapters.XercesDOMAdapter";
    private boolean validate;
    private String adapterClass;
    private JDOMFactory factory;
}
