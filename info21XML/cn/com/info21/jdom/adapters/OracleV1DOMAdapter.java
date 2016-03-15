// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OracleV1DOMAdapter.java

package cn.com.info21.jdom.adapters;

import cn.com.info21.dom.Document;
import org.xml.sax.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package cn.com.info21.jdom.adapters:
//            AbstractDOMAdapter

public class OracleV1DOMAdapter extends AbstractDOMAdapter
{

    public OracleV1DOMAdapter()
    {
    }

    public Document getDocument(InputStream in, boolean validate)
        throws IOException
    {
        Throwable targetException;
        try
        {
            Class parserClass = Class.forName("oracle.xml.parser.XMLParser");
            Object parser = parserClass.newInstance();
            Method parse = parserClass.getMethod("parse", new Class[] {
				org.xml.sax.InputSource.class
            });
            parse.invoke(parser, new Object[] {
                new InputSource(in)
            });
            Method getDocument = parserClass.getMethod("getDocument", null);
            Document doc = (Document)getDocument.invoke(parser, null);
            return doc;
        }
        catch(InvocationTargetException e)
        {
            targetException = e.getTargetException();
        }
        catch(Exception e)
        {
            throw new IOException(e.getClass().getName() + ": " + e.getMessage());
        }
        if(targetException instanceof SAXParseException)
        {
            SAXParseException parseException = (SAXParseException)targetException;
            throw new IOException("Error on line " + parseException.getLineNumber() + " of XML document: " + parseException.getMessage());
        } else
        {
            throw new IOException(targetException.getMessage());
        }
    }

    public Document createDocument()
        throws IOException
    {
        try
        {
            return (Document)Class.forName("oracle.xml.parser.XMLDocument").newInstance();
        }
        catch(Exception e)
        {
            throw new IOException(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private static final String CVS_ID = "@(#) $RCSfile: OracleV1DOMAdapter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:32 $ $Name:  $";
}
