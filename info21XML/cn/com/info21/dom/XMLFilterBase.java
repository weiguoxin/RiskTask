// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLFilterBase.java

package cn.com.info21.dom;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

class XMLFilterBase extends XMLFilterImpl
{

    public XMLFilterBase()
    {
    }

    public XMLFilterBase(XMLReader parent)
    {
        super(parent);
    }

    public void startElement(String uri, String localName)
        throws SAXException
    {
        startElement(uri, localName, "", EMPTY_ATTS);
    }

    public void startElement(String localName)
        throws SAXException
    {
        startElement("", localName, "", EMPTY_ATTS);
    }

    public void endElement(String uri, String localName)
        throws SAXException
    {
        endElement(uri, localName, "");
    }

    public void endElement(String localName)
        throws SAXException
    {
        endElement("", localName, "");
    }

    public void emptyElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException
    {
        startElement(uri, localName, qName, atts);
        endElement(uri, localName, qName);
    }

    public void emptyElement(String uri, String localName)
        throws SAXException
    {
        emptyElement(uri, localName, "", EMPTY_ATTS);
    }

    public void emptyElement(String localName)
        throws SAXException
    {
        emptyElement("", localName, "", EMPTY_ATTS);
    }

    public void dataElement(String uri, String localName, String qName, Attributes atts, String content)
        throws SAXException
    {
        startElement(uri, localName, qName, atts);
        characters(content);
        endElement(uri, localName, qName);
    }

    public void dataElement(String uri, String localName, String content)
        throws SAXException
    {
        dataElement(uri, localName, "", EMPTY_ATTS, content);
    }

    public void dataElement(String localName, String content)
        throws SAXException
    {
        dataElement("", localName, "", EMPTY_ATTS, content);
    }

    public void characters(String data)
        throws SAXException
    {
        char ch[] = data.toCharArray();
        characters(ch, 0, ch.length);
    }

    protected static final Attributes EMPTY_ATTS = new AttributesImpl();

}
