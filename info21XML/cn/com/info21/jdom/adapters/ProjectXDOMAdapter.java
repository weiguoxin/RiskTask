// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectXDOMAdapter.java

package cn.com.info21.jdom.adapters;

import cn.com.info21.dom.Document;
//import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package cn.com.info21.jdom.adapters:
//            AbstractDOMAdapter

public class ProjectXDOMAdapter extends AbstractDOMAdapter
{

    public ProjectXDOMAdapter()
    {
    }

    public Document getDocument(InputStream in, boolean validate)
        throws IOException
    {
        Throwable targetException;
        try
        {
            Class parameterTypes[] = new Class[2];
            parameterTypes[0] = Class.forName("java.io.InputStream");
            parameterTypes[1] = Boolean.TYPE;
            Object args[] = new Object[2];
            args[0] = in;
            args[1] = new Boolean(false);
            Class parserClass = Class.forName("com.sun.xml.tree.XmlDocument");
            Method createXmlDocument = parserClass.getMethod("createXmlDocument", parameterTypes);
            Document doc = (Document)createXmlDocument.invoke(null, args);
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
            return (Document)Class.forName("com.sun.xml.tree.XmlDocument").newInstance();
        }
        catch(Exception e)
        {
            throw new IOException(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private static final String CVS_ID = "@(#) $RCSfile: ProjectXDOMAdapter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:32 $ $Name:  $";
}
