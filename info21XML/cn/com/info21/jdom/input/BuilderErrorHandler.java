// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BuilderErrorHandler.java

package cn.com.info21.jdom.input;

import org.xml.sax.*;

public class BuilderErrorHandler
    implements ErrorHandler
{

    public BuilderErrorHandler()
    {
    }

    public void warning(SAXParseException saxparseexception)
        throws SAXException
    {
    }

    public void error(SAXParseException exception)
        throws SAXException
    {
        throw exception;
    }

    public void fatalError(SAXParseException exception)
        throws SAXException
    {
        throw exception;
    }

    private static final String CVS_ID = "@(#) $RCSfile: BuilderErrorHandler.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:31 $ $Name:  $";
}
