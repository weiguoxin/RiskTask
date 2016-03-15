// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JAXPDOMAdapter.java

package cn.com.info21.jdom.adapters;

import cn.com.info21.dom.Document;
import cn.com.info21.jdom.input.BuilderErrorHandler;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package cn.com.info21.jdom.adapters:
//            AbstractDOMAdapter

public class JAXPDOMAdapter extends AbstractDOMAdapter
{

    public JAXPDOMAdapter()
    {
    }

    public Document getDocument(InputStream in, boolean validate)
        throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Class.forName("javax.xml.transform.Transformer");
        Class factoryClass = Class.forName("javax.xml.parsers.DocumentBuilderFactory");
        Method newParserInstance = factoryClass.getMethod("newInstance", null);
        Object factory = newParserInstance.invoke(null, null);
        Method setValidating = factoryClass.getMethod("setValidating", new Class[] {
            Boolean.TYPE
        });
        setValidating.invoke(factory, new Object[] {
            new Boolean(validate)
        });
        Method setNamespaceAware = factoryClass.getMethod("setNamespaceAware", new Class[] {
            Boolean.TYPE
        });
        setNamespaceAware.invoke(factory, new Object[] {
            Boolean.TRUE
        });
        Method newDocBuilder = factoryClass.getMethod("newDocumentBuilder", null);
        Object jaxpParser = newDocBuilder.invoke(factory, null);
        Class parserClass = jaxpParser.getClass();
        Method setErrorHandler = parserClass.getMethod("setErrorHandler", new Class[] {
			org.xml.sax.ErrorHandler.class
        });
        setErrorHandler.invoke(jaxpParser, new Object[] {
            new BuilderErrorHandler()
        });
        Method parse = parserClass.getMethod("parse", new Class[] {
            java.io.InputStream.class
        });
        Document domDoc = (Document)parse.invoke(jaxpParser, new Object[] {
            in
        });
        return domDoc;
    }

    public Document createDocument()
        throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Class.forName("javax.xml.transform.Transformer");
        Class factoryClass = Class.forName("javax.xml.parsers.DocumentBuilderFactory");
        Method newParserInstance = factoryClass.getMethod("newInstance", null);
        Object factory = newParserInstance.invoke(null, null);
        Method newDocBuilder = factoryClass.getMethod("newDocumentBuilder", null);
        Object jaxpParser = newDocBuilder.invoke(factory, null);
        Class parserClass = jaxpParser.getClass();
        Method newDoc = parserClass.getMethod("newDocument", null);
        Document domDoc = (Document)newDoc.invoke(jaxpParser, null);
        return domDoc;
    }

    private static final String CVS_ID = "@(#) $RCSfile: JAXPDOMAdapter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:32 $ $Name:  $";
}
