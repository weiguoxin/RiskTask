// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOMOutputter.java

package cn.com.info21.jdom.output;

import cn.com.info21.dom.Attr;
//import cn.com.info21.dom.Node;
import cn.com.info21.jdom.Attribute;
import cn.com.info21.jdom.CDATA;
import cn.com.info21.jdom.Comment;
import cn.com.info21.jdom.DocType;
import cn.com.info21.jdom.Document;
import cn.com.info21.jdom.Element;
import cn.com.info21.jdom.EntityRef;
import cn.com.info21.jdom.JDOMException;
import cn.com.info21.jdom.Namespace;
import cn.com.info21.jdom.ProcessingInstruction;
import cn.com.info21.jdom.adapters.DOMAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
//import java.util.List;

// Referenced classes of package cn.com.info21.jdom.output:
//            NamespaceStack

public class DOMOutputter
{

    public DOMOutputter()
    {
    }

    public DOMOutputter(String adapterClass)
    {
        this.adapterClass = adapterClass;
    }

    public cn.com.info21.dom.Document output(Document document)
        throws JDOMException
    {
        NamespaceStack namespaces = new NamespaceStack();
        cn.com.info21.dom.Document domDoc = null;
        try
        {
            DocType dt = document.getDocType();
            domDoc = createDOMDocument(dt);
            for(Iterator itr = document.getContent().iterator(); itr.hasNext();)
            {
                Object node = itr.next();
                if(node instanceof Element)
                {
                    Element element = (Element)node;
                    cn.com.info21.dom.Element domElement = output(element, domDoc, namespaces);
                    cn.com.info21.dom.Element root = domDoc.getDocumentElement();
                    if(root == null)
                        domDoc.appendChild(domElement);
                    else
                        domDoc.replaceChild(domElement, root);
                } else
                if(node instanceof Comment)
                {
                    Comment comment = (Comment)node;
                    cn.com.info21.dom.Comment domComment = domDoc.createComment(comment.getText());
                    domDoc.appendChild(domComment);
                } else
                if(node instanceof ProcessingInstruction)
                {
                    ProcessingInstruction pi = (ProcessingInstruction)node;
                    cn.com.info21.dom.ProcessingInstruction domPI = domDoc.createProcessingInstruction(pi.getTarget(), pi.getData());
                    domDoc.appendChild(domPI);
                } else
                {
                    throw new JDOMException("Document contained top-level content with type:" + node.getClass().getName());
                }
            }

        }
        catch(Throwable e)
        {
            throw new JDOMException("Exception outputting Document", e);
        }
        return domDoc;
    }

    public cn.com.info21.dom.Element output(Element element)
        throws JDOMException
    {
        try
        {
            cn.com.info21.dom.Document domDoc = createDOMDocument();
            return output(element, domDoc, new NamespaceStack());
        }
        catch(Throwable e)
        {
            throw new JDOMException("Exception outputting Element " + element.getQualifiedName(), e);
        }
    }

    private cn.com.info21.dom.Document createDOMDocument()
        throws Throwable
    {
        return createDOMDocument(null);
    }

    private cn.com.info21.dom.Document createDOMDocument(DocType dt)
        throws Throwable
    {
        if(adapterClass != null)
            try
            {
                DOMAdapter adapter = (DOMAdapter)Class.forName(adapterClass).newInstance();
                return adapter.createDocument(dt);
            }
            catch(ClassNotFoundException classnotfoundexception) { }
        else
            try
            {
                DOMAdapter adapter = (DOMAdapter)Class.forName("cn.com.info21.jdom.adapters.JAXPDOMAdapter").newInstance();
                return adapter.createDocument(dt);
            }
            catch(ClassNotFoundException classnotfoundexception1) { }
            catch(NoSuchMethodException nosuchmethodexception) { }
            catch(IllegalAccessException illegalaccessexception) { }
            catch(InvocationTargetException ite)
            {
                throw ite.getTargetException();
            }
        try
        {
            DOMAdapter adapter = (DOMAdapter)Class.forName("cn.com.info21.jdom.adapters.XercesDOMAdapter").newInstance();
            return adapter.createDocument(dt);
        }
        catch(ClassNotFoundException classnotfoundexception2)
        {
            throw new Exception("No JAXP or default parser available");
        }
    }

    protected cn.com.info21.dom.Element output(Element element, cn.com.info21.dom.Document domDoc, NamespaceStack namespaces)
        throws JDOMException
    {
        try
        {
            int previouslyDeclaredNamespaces = namespaces.size();
            cn.com.info21.dom.Element domElement = null;
            if("".equals(element.getNamespacePrefix()))
                domElement = domDoc.createElement(element.getQualifiedName());
            else
                domElement = domDoc.createElementNS(element.getNamespaceURI(), element.getQualifiedName());
            Namespace ns = element.getNamespace();
            if(ns != Namespace.XML_NAMESPACE && (ns != Namespace.NO_NAMESPACE || namespaces.getURI("") != null))
            {
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if(!ns.getURI().equals(uri))
                {
                    namespaces.push(ns);
                    String attrName = getXmlnsTagFor(ns);
                    domElement.setAttribute(attrName, ns.getURI());
                }
            }
            for(Iterator itr = element.getAdditionalNamespaces().iterator(); itr.hasNext();)
            {
                Namespace additional = (Namespace)itr.next();
                String prefix = additional.getPrefix();
                String uri = namespaces.getURI(prefix);
                if(!additional.getURI().equals(uri))
                {
                    String attrName = getXmlnsTagFor(additional);
                    domElement.setAttribute(attrName, additional.getURI());
                    namespaces.push(additional);
                }
            }

            for(Iterator itr = element.getAttributes().iterator(); itr.hasNext();)
            {
                Attribute attribute = (Attribute)itr.next();
                domElement.setAttributeNode(output(attribute, domDoc));
                Namespace ns1 = attribute.getNamespace();
                if(ns1 != Namespace.NO_NAMESPACE && ns1 != Namespace.XML_NAMESPACE)
                {
                    String prefix = ns1.getPrefix();
                    String uri = namespaces.getURI(prefix);
                    if(!ns.getURI().equals(uri))
                    {
                        String attrName = getXmlnsTagFor(ns1);
                        domElement.setAttribute(attrName, ns1.getURI());
                        namespaces.push(ns);
                    }
                }
                if("".equals(attribute.getNamespacePrefix()))
                    domElement.setAttribute(attribute.getQualifiedName(), attribute.getValue());
                else
                    domElement.setAttributeNS(attribute.getNamespaceURI(), attribute.getQualifiedName(), attribute.getValue());
            }

            for(Iterator itr = element.getContent().iterator(); itr.hasNext();)
            {
                Object node = itr.next();
                if(node instanceof Element)
                {
                    Element e = (Element)node;
                    cn.com.info21.dom.Element domElt = output(e, domDoc, namespaces);
                    domElement.appendChild(domElt);
                } else
                if(node instanceof String)
                {
                    String str = (String)node;
                    cn.com.info21.dom.Text domText = domDoc.createTextNode(str);
                    domElement.appendChild(domText);
                } else
                if(node instanceof CDATA)
                {
                    CDATA cdata = (CDATA)node;
                    cn.com.info21.dom.CDATASection domCdata = domDoc.createCDATASection(cdata.getText());
                    domElement.appendChild(domCdata);
                } else
                if(node instanceof Comment)
                {
                    Comment comment = (Comment)node;
                    cn.com.info21.dom.Comment domComment = domDoc.createComment(comment.getText());
                    domElement.appendChild(domComment);
                } else
                if(node instanceof ProcessingInstruction)
                {
                    ProcessingInstruction pi = (ProcessingInstruction)node;
                    cn.com.info21.dom.ProcessingInstruction domPI = domDoc.createProcessingInstruction(pi.getTarget(), pi.getData());
                    domElement.appendChild(domPI);
                } else
                if(node instanceof EntityRef)
                {
                    EntityRef entity = (EntityRef)node;
                    cn.com.info21.dom.EntityReference domEntity = domDoc.createEntityReference(entity.getName());
                    domElement.appendChild(domEntity);
                } else
                {
                    throw new JDOMException("Element contained content with type:" + node.getClass().getName());
                }
            }

            for(; namespaces.size() > previouslyDeclaredNamespaces; namespaces.pop());
            return domElement;
        }
        catch(Exception e)
        {
            throw new JDOMException("Exception outputting Element " + element.getQualifiedName(), e);
        }
    }

    public Attr output(Attribute attribute)
        throws JDOMException
    {
        try
        {
            cn.com.info21.dom.Document domDoc = createDOMDocument();
            return output(attribute, domDoc);
        }
        catch(Throwable e)
        {
            throw new JDOMException("Exception outputting Attribute " + attribute.getQualifiedName(), e);
        }
    }

    protected Attr output(Attribute attribute, cn.com.info21.dom.Document domDoc)
        throws JDOMException
    {
        Attr domAttr = null;
        try
        {
            if("".equals(attribute.getNamespacePrefix()))
                domAttr = domDoc.createAttribute(attribute.getQualifiedName());
            else
                domAttr = domDoc.createAttributeNS(attribute.getNamespaceURI(), attribute.getQualifiedName());
            domAttr.setValue(attribute.getValue());
        }
        catch(Exception e)
        {
            throw new JDOMException("Exception outputting Attribute " + attribute.getQualifiedName(), e);
        }
        return domAttr;
    }

    private String getXmlnsTagFor(Namespace ns)
    {
        String attrName = "xmlns";
        if(!ns.getPrefix().equals(""))
        {
            attrName = attrName + ":";
            attrName = attrName + ns.getPrefix();
        }
        return attrName;
    }

    private static final String CVS_ID = "@(#) $RCSfile: DOMOutputter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:29 $ $Name:  $";
    private static final String DEFAULT_ADAPTER_CLASS = "cn.com.info21.jdom.adapters.XercesDOMAdapter";
    private String adapterClass;
}
