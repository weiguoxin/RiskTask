// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SAXHandler.java

package cn.com.info21.jdom.input;

import cn.com.info21.jdom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.*;

// Referenced classes of package cn.com.info21.jdom.input:
//            DefaultJDOMFactory, JDOMFactory

public class SAXHandler extends DefaultHandler
    implements LexicalHandler, DeclHandler
{

    /**
     * @deprecated Method SAXHandler is deprecated
     */

    public SAXHandler(Document document)
        throws IOException
    {
        this(((JDOMFactory) (new DefaultJDOMFactory())));
        this.document = document;
    }

    public SAXHandler()
        throws IOException
    {
        this((JDOMFactory)null);
    }

    public SAXHandler(JDOMFactory factory)
        throws IOException
    {
        inDTD = false;
        inCDATA = false;
        expand = true;
        suppress = false;
        entityDepth = 0;
        ignoringWhite = false;
        if(factory != null)
            this.factory = factory;
        else
            this.factory = new DefaultJDOMFactory();
        atRoot = true;
        stack = new Stack();
        declaredNamespaces = new LinkedList();
        availableNamespaces = new LinkedList();
        availableNamespaces.add(Namespace.XML_NAMESPACE);
        externalEntities = new HashMap();
        document = this.factory.document((Element)null);
    }

    public Document getDocument()
    {
        return document;
    }

    public void setExpandEntities(boolean expand)
    {
        this.expand = expand;
    }

    public void setIgnoringElementContentWhitespace(boolean ignoringWhite)
    {
        this.ignoringWhite = ignoringWhite;
    }

    public void externalEntityDecl(String name, String publicId, String systemId)
        throws SAXException
    {
        externalEntities.put(name, new String[] {
            publicId, systemId
        });
    }

    public void attributeDecl(String s, String s1, String s2, String s3, String s4)
    {
    }

    public void elementDecl(String s, String s1)
    {
    }

    public void internalEntityDecl(String s, String s1)
    {
    }

    public void processingInstruction(String target, String data)
        throws SAXException
    {
        if(suppress)
            return;
        if(atRoot)
            document.addContent(factory.processingInstruction(target, data));
        else
            ((Element)stack.peek()).addContent(factory.processingInstruction(target, data));
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
        if(suppress)
        {
            return;
        } else
        {
            Namespace ns = Namespace.getNamespace(prefix, uri);
            declaredNamespaces.add(ns);
            return;
        }
    }

    public void endPrefixMapping(String prefix)
        throws SAXException
    {
        if(suppress)
            return;
        for(Iterator itr = availableNamespaces.iterator(); itr.hasNext();)
        {
            Namespace ns = (Namespace)itr.next();
            if(prefix.equals(ns.getPrefix()))
            {
                itr.remove();
                return;
            }
        }

    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
        throws SAXException
    {
        if(suppress)
            return;
        Element element = null;
        if(namespaceURI != null && !namespaceURI.equals(""))
        {
            String prefix = "";
            if(localName != qName)
            {
                int split = qName.indexOf(":");
                prefix = qName.substring(0, split);
            }
            Namespace elementNamespace = Namespace.getNamespace(prefix, namespaceURI);
            element = factory.element(localName, elementNamespace);
            if(declaredNamespaces.size() > 0)
                declaredNamespaces.remove(elementNamespace);
        } else
        {
            element = factory.element(localName);
        }
        if(declaredNamespaces.size() > 0)
            transferNamespaces(element);
        int i = 0;
        for(int len = atts.getLength(); i < len; i++)
        {
            cn.com.info21.jdom.Attribute attribute = null;
            String attLocalName = atts.getLocalName(i);
            String attQName = atts.getQName(i);
            if(!attQName.startsWith("xmlns:") && !attQName.equals("xmlns"))
            {
                if(attLocalName != attQName)
                {
                    String attPrefix = attQName.substring(0, attQName.indexOf(":"));
                    attribute = factory.attribute(attLocalName, atts.getValue(i), getNamespace(attPrefix));
                } else
                {
                    attribute = factory.attribute(attLocalName, atts.getValue(i));
                }
                element.setAttribute(attribute);
            }
        }

        if(atRoot)
        {
            document.setRootElement(element);
            stack.push(element);
            atRoot = false;
        } else
        {
            ((Element)stack.peek()).addContent(element);
            stack.push(element);
        }
    }

    private void transferNamespaces(Element element)
    {
        Namespace ns;
        for(Iterator i = declaredNamespaces.iterator(); i.hasNext(); element.addNamespaceDeclaration(ns))
        {
            ns = (Namespace)i.next();
            availableNamespaces.addFirst(ns);
        }

        declaredNamespaces.clear();
    }

    private Namespace getNamespace(String prefix)
    {
        for(Iterator i = availableNamespaces.iterator(); i.hasNext();)
        {
            Namespace ns = (Namespace)i.next();
            if(prefix.equals(ns.getPrefix()))
                return ns;
        }

        return Namespace.NO_NAMESPACE;
    }

    public void characters(char ch[], int start, int length)
        throws SAXException
    {
        if(suppress)
            return;
        String data = new String(ch, start, length);
        if(inCDATA)
        {
            ((Element)stack.peek()).addContent(factory.cdata(data));
        } else
        {
            Element e = (Element)stack.peek();
            e.addContent(data);
        }
    }

    public void ignorableWhitespace(char ch[], int start, int length)
        throws SAXException
    {
        if(suppress)
            return;
        if(ignoringWhite)
        {
            return;
        } else
        {
            ((Element)stack.peek()).addContent(new String(ch, start, length));
            return;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException
    {
        if(suppress)
            return;
        Element element = (Element)stack.pop();
        if(stack.empty())
            atRoot = true;
        List addl = element.getAdditionalNamespaces();
        if(addl.size() > 0)
            availableNamespaces.removeAll(addl);
    }

    public void startDTD(String name, String publicId, String systemId)
        throws SAXException
    {
        document.setDocType(factory.docType(name, publicId, systemId));
        inDTD = true;
    }

    public void endDTD()
        throws SAXException
    {
        inDTD = false;
    }

    public void startEntity(String name)
        throws SAXException
    {
        entityDepth++;
        if(expand || entityDepth > 1)
            return;
        if(!inDTD && !name.equals("amp") && !name.equals("lt") && !name.equals("gt") && !name.equals("apos") && !name.equals("quot") && !expand)
        {
            String pub = null;
            String sys = null;
            String ids[] = (String[])externalEntities.get(name);
            if(ids != null)
            {
                pub = ids[0];
                sys = ids[1];
            }
            cn.com.info21.jdom.EntityRef entity = factory.entityRef(name, pub, sys);
            ((Element)stack.peek()).addContent(entity);
            suppress = true;
        }
    }

    public void endEntity(String name)
        throws SAXException
    {
        entityDepth--;
        if(entityDepth == 0)
            suppress = false;
    }

    public void startCDATA()
        throws SAXException
    {
        if(suppress)
        {
            return;
        } else
        {
            inCDATA = true;
            return;
        }
    }

    public void endCDATA()
        throws SAXException
    {
        if(suppress)
        {
            return;
        } else
        {
            inCDATA = false;
            return;
        }
    }

    public void comment(char ch[], int start, int length)
        throws SAXException
    {
        if(suppress)
            return;
        String commentText = new String(ch, start, length);
        if(!inDTD && !commentText.equals(""))
            if(stack.empty())
                document.addContent(factory.comment(commentText));
            else
                ((Element)stack.peek()).addContent(factory.comment(commentText));
    }

    private static final String CVS_ID = "@(#) $RCSfile: SAXHandler.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:30 $ $Name:  $";
    private Document document;
    private Stack stack;
    private boolean atRoot;
    private boolean inDTD;
    private boolean inCDATA;
    private boolean expand;
    private boolean suppress;
    private int entityDepth;
    private LinkedList declaredNamespaces;
    private LinkedList availableNamespaces;
    private Map externalEntities;
    private JDOMFactory factory;
    private boolean ignoringWhite;
}
