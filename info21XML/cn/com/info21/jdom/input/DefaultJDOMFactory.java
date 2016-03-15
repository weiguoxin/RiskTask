// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultJDOMFactory.java

package cn.com.info21.jdom.input;

import cn.com.info21.jdom.*;
import java.util.Map;

// Referenced classes of package cn.com.info21.jdom.input:
//            JDOMFactory

public class DefaultJDOMFactory
    implements JDOMFactory
{

    public DefaultJDOMFactory()
    {
    }

    public Attribute attribute(String name, String value, Namespace namespace)
    {
        return new Attribute(name, value, namespace);
    }

    public Attribute attribute(String name, String value)
    {
        return new Attribute(name, value);
    }

    public CDATA cdata(String text)
    {
        return new CDATA(text);
    }

    public Comment comment(String text)
    {
        return new Comment(text);
    }

    public DocType docType(String elementName, String publicID, String systemID)
    {
        return new DocType(elementName, publicID, systemID);
    }

    public DocType docType(String elementName, String systemID)
    {
        return new DocType(elementName, systemID);
    }

    public DocType docType(String elementName)
    {
        return new DocType(elementName);
    }

    public Document document(Element rootElement, DocType docType)
    {
        return new Document(rootElement, docType);
    }

    public Document document(Element rootElement)
    {
        return new Document(rootElement);
    }

    public Element element(String name, Namespace namespace)
    {
        return new Element(name, namespace);
    }

    public Element element(String name)
    {
        return new Element(name);
    }

    public Element element(String name, String uri)
    {
        return new Element(name, uri);
    }

    public Element element(String name, String prefix, String uri)
    {
        return new Element(name, prefix, uri);
    }

    public ProcessingInstruction processingInstruction(String target, Map data)
    {
        return new ProcessingInstruction(target, data);
    }

    public ProcessingInstruction processingInstruction(String target, String data)
    {
        return new ProcessingInstruction(target, data);
    }

    public EntityRef entityRef(String name)
    {
        return new EntityRef(name);
    }

    public EntityRef entityRef(String name, String publicID, String systemID)
    {
        return new EntityRef(name, publicID, systemID);
    }

    private static final String CVS_ID = "@(#) $RCSfile: DefaultJDOMFactory.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:30 $ $Name:  $";
}
