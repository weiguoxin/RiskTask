// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataUnformatFilter.java

package cn.com.info21.dom;

import org.xml.sax.*;


import java.util.Stack;

// Referenced classes of package cn.com.info21.util:
//            XMLFilterBase

public class DataUnformatFilter extends XMLFilterBase
{
    public DataUnformatFilter()
    {
        state = SEEN_NOTHING;
        stateStack = new Stack();
        whitespace = new StringBuffer();
    }

    public DataUnformatFilter(XMLReader xmlreader)
    {
        super(xmlreader);
        state = SEEN_NOTHING;
        stateStack = new Stack();
        whitespace = new StringBuffer();
    }

    public void reset()
    {
        state = SEEN_NOTHING;
        stateStack = new Stack();
        whitespace = new StringBuffer();
    }

    public void startDocument()
        throws SAXException
    {
        reset();
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException
    {
        clearWhitespace();
        stateStack.push(SEEN_ELEMENT);
        state = SEEN_NOTHING;
        super.startElement(uri, localName, qName, atts);
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        if(state == SEEN_ELEMENT)
            clearWhitespace();
        else
            emitWhitespace();
        state = stateStack.pop();
        super.endElement(uri, localName, qName);
    }

    public void characters(char ch[], int start, int length)
        throws SAXException
    {
        if(state != SEEN_DATA)
        {
            int end;
            for(end = start + length; end-- > start;)
                if(!isXMLWhitespace(ch[end]))
                    break;

            if(end < start)
            {
                saveWhitespace(ch, start, length);
            } else
            {
                state = SEEN_DATA;
                emitWhitespace();
            }
        }
        if(state == SEEN_DATA)
            super.characters(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length)
        throws SAXException
    {
        emitWhitespace();
    }

    public void processingInstruction(String target, String data)
        throws SAXException
    {
        emitWhitespace();
        super.processingInstruction(target, data);
    }

    protected void saveWhitespace(char ch[], int start, int length)
    {
        whitespace.append(ch, start, length);
    }

    protected void emitWhitespace()
        throws SAXException
    {
        char data[] = new char[whitespace.length()];
        if(whitespace.length() > 0)
        {
            whitespace.getChars(0, data.length, data, 0);
            whitespace.setLength(0);
            super.characters(data, 0, data.length);
        }
    }

    protected void clearWhitespace()
    {
        whitespace.setLength(0);
    }

    private boolean isXMLWhitespace(char c)
    {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();
    private Object state;
    private Stack stateStack;
    private StringBuffer whitespace;

}
