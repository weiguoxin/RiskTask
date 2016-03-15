// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SAXOutputter.java

package cn.com.info21.jdom.output;

import cn.com.info21.jdom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package cn.com.info21.jdom.output:
//            NamespaceStack

public class SAXOutputter
{

    public SAXOutputter(ContentHandler contentHandler)
    {
        declareNamespaces = false;
        this.contentHandler = contentHandler;
    }

    public SAXOutputter(ContentHandler contentHandler, ErrorHandler errorHandler, DTDHandler dtdHandler, EntityResolver entityResolver)
    {
        declareNamespaces = false;
        this.contentHandler = contentHandler;
        this.errorHandler = errorHandler;
        this.dtdHandler = dtdHandler;
        this.entityResolver = entityResolver;
    }

    public void setContentHandler(ContentHandler contentHandler)
    {
        this.contentHandler = contentHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
    }

    public void setDTDHandler(DTDHandler dtdHandler)
    {
        this.dtdHandler = dtdHandler;
    }

    public void setEntityResolver(EntityResolver entityResolver)
    {
        this.entityResolver = entityResolver;
    }

    public void setReportNamespaceDeclarations(boolean declareNamespaces)
    {
        this.declareNamespaces = declareNamespaces;
    }

    public void output(Document document)
        throws JDOMException
    {
        if(document == null)
            return;
        documentLocator(document);
        startDocument();
        entityResolver(document);
        for(Iterator i = document.getContent().iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
                element(document.getRootElement(), new NamespaceStack());
            else
            if(obj instanceof ProcessingInstruction)
                processingInstruction((ProcessingInstruction)obj);
            else
            if(obj instanceof CDATA)
                characters(((CDATA)obj).getText());
        }

        endDocument();
    }

    private void entityResolver(Document document)
        throws JDOMException
    {
        if(entityResolver != null)
        {
            DocType docType = document.getDocType();
            String publicID = null;
            String systemID = null;
            if(docType != null)
            {
                publicID = docType.getPublicID();
                systemID = docType.getSystemID();
            }
            if(publicID != null || systemID != null)
                try
                {
                    entityResolver.resolveEntity(publicID, systemID);
                }
                catch(SAXException se)
                {
                    throw new JDOMException("Exception in entityResolver", se);
                }
                catch(IOException ioe)
                {
                    throw new JDOMException("Exception in entityResolver", ioe);
                }
        }
    }

    private void documentLocator(Document document)
    {
        LocatorImpl locator = new LocatorImpl();
        String publicID = null;
        String systemID = null;
        DocType docType = document.getDocType();
        if(docType != null)
        {
            publicID = docType.getPublicID();
            systemID = docType.getSystemID();
        }
        locator.setPublicId(publicID);
        locator.setSystemId(systemID);
        locator.setLineNumber(-1);
        locator.setColumnNumber(-1);
        contentHandler.setDocumentLocator(locator);
    }

    private void startDocument()
        throws JDOMException
    {
        try
        {
            contentHandler.startDocument();
        }
        catch(SAXException se)
        {
            throw new JDOMException("Exception in startDocument", se);
        }
    }

    private void endDocument()
        throws JDOMException
    {
        try
        {
            contentHandler.endDocument();
        }
        catch(SAXException se)
        {
            throw new JDOMException("Exception in endDocument", se);
        }
    }

    private void processingInstruction(ProcessingInstruction pi)
        throws JDOMException
    {
        if(pi != null)
        {
            String target = pi.getTarget();
            String data = pi.getData();
            try
            {
                contentHandler.processingInstruction(target, data);
            }
            catch(SAXException se)
            {
                throw new JDOMException("Exception in processingInstruction", se);
            }
        }
    }

    private void element(Element element, NamespaceStack namespaces)
        throws JDOMException
    {
        int previouslyDeclaredNamespaces = namespaces.size();
        Attributes nsAtts = startPrefixMapping(element, namespaces);
        startElement(element, nsAtts);
        elementContent(element, namespaces);
        endElement(element);
        endPrefixMapping(namespaces, previouslyDeclaredNamespaces);
    }

    private Attributes startPrefixMapping(Element element, NamespaceStack namespaces)
        throws JDOMException
    {
        AttributesImpl nsAtts = null;
        Namespace ns = element.getNamespace();
        if(ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE)
        {
            String prefix = ns.getPrefix();
            String uri = namespaces.getURI(prefix);
            if(!ns.getURI().equals(uri))
            {
                namespaces.push(ns);
                nsAtts = addNsAttribute(nsAtts, ns);
                try
                {
                    contentHandler.startPrefixMapping(prefix, ns.getURI());
                }
                catch(SAXException se)
                {
                    throw new JDOMException("Exception in startPrefixMapping", se);
                }
            }
        }
        List additionalNamespaces = element.getAdditionalNamespaces();
        if(additionalNamespaces != null)
        {
            for(Iterator itr = additionalNamespaces.iterator(); itr.hasNext();)
            {
                ns = (Namespace)itr.next();
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if(!ns.getURI().equals(uri))
                {
                    namespaces.push(ns);
                    nsAtts = addNsAttribute(nsAtts, ns);
                    try
                    {
                        contentHandler.startPrefixMapping(prefix, ns.getURI());
                    }
                    catch(SAXException se)
                    {
                        throw new JDOMException("Exception in startPrefixMapping", se);
                    }
                }
            }

        }
        return nsAtts;
    }

    private void endPrefixMapping(NamespaceStack namespaces, int previouslyDeclaredNamespaces)
        throws JDOMException
    {
        while(namespaces.size() > previouslyDeclaredNamespaces) 
        {
            String prefix = namespaces.pop();
            try
            {
                contentHandler.endPrefixMapping(prefix);
            }
            catch(SAXException se)
            {
                throw new JDOMException("Exception in endPrefixMapping", se);
            }
        }
    }

    private void startElement(Element element, Attributes nsAtts)
        throws JDOMException
    {
        String namespaceURI = element.getNamespaceURI();
        String localName = element.getName();
        String rawName = element.getQualifiedName();
        AttributesImpl atts = nsAtts == null ? new AttributesImpl() : new AttributesImpl(nsAtts);
        List attributes = element.getAttributes();
        Attribute a;
        for(Iterator i = attributes.iterator(); i.hasNext(); atts.addAttribute(a.getNamespaceURI(), a.getName(), a.getQualifiedName(), "CDATA", a.getValue()))
            a = (Attribute)i.next();

        try
        {
            contentHandler.startElement(namespaceURI, localName, rawName, atts);
        }
        catch(SAXException se)
        {
            throw new JDOMException("Exception in startElement", se);
        }
    }

    private void endElement(Element element)
        throws JDOMException
    {
        String namespaceURI = element.getNamespaceURI();
        String localName = element.getName();
        String rawName = element.getQualifiedName();
        try
        {
            contentHandler.endElement(namespaceURI, localName, rawName);
        }
        catch(SAXException se)
        {
            throw new JDOMException("Exception in endElement", se);
        }
    }

    private void elementContent(Element element, NamespaceStack namespaces)
        throws JDOMException
    {
        List eltContent = element.getContent();
        boolean empty = eltContent.size() == 0;
        boolean stringOnly = !empty && eltContent.size() == 1 && (eltContent.get(0) instanceof String);
        if(stringOnly)
        {
            characters(element.getText());
        } else
        {
            Object content = null;
            int i = 0;
            for(int size = eltContent.size(); i < size; i++)
            {
                content = eltContent.get(i);
                if(content instanceof Element)
                    element((Element)content, namespaces);
                else
                if(content instanceof String)
                    characters((String)content);
                else
                if(content instanceof CDATA)
                    characters(((CDATA)content).getText());
                else
                if(content instanceof ProcessingInstruction)
                    processingInstruction((ProcessingInstruction)content);
            }

        }
    }

    private void characters(String elementText)
        throws JDOMException
    {
        char c[] = elementText.toCharArray();
        try
        {
            contentHandler.characters(c, 0, c.length);
        }
        catch(SAXException se)
        {
            throw new JDOMException("Exception in characters", se);
        }
    }

    private AttributesImpl addNsAttribute(AttributesImpl atts, Namespace ns)
    {
        if(declareNamespaces)
        {
            if(atts == null)
                atts = new AttributesImpl();
            atts.addAttribute("", "", "xmlns:" + ns.getPrefix(), "CDATA", ns.getURI());
        }
        return atts;
    }

    private static final String CVS_ID = "@(#) $RCSfile: SAXOutputter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:29 $ $Name:  $";
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private boolean declareNamespaces;
}
