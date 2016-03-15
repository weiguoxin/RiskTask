// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractDOMAdapter.java

package cn.com.info21.jdom.adapters;

import cn.com.info21.dom.DOMImplementation;
import cn.com.info21.dom.Document;
import cn.com.info21.jdom.DocType;
import java.io.*;

// Referenced classes of package cn.com.info21.jdom.adapters:
//            DOMAdapter

public abstract class AbstractDOMAdapter
    implements DOMAdapter
{

    public AbstractDOMAdapter()
    {
    }

    public Document getDocument(File filename, boolean validate)
        throws Exception
    {
        return getDocument(((InputStream) (new FileInputStream(filename))), validate);
    }

    public abstract Document getDocument(InputStream inputstream, boolean flag)
        throws Exception;

    public abstract Document createDocument()
        throws Exception;

    public Document createDocument(DocType doctype)
        throws Exception
    {
        if(doctype == null)
        {
            return createDocument();
        } else
        {
            DOMImplementation domImpl = createDocument().getImplementation();
            cn.com.info21.dom.DocumentType domDocType = domImpl.createDocumentType(doctype.getElementName(), doctype.getPublicID(), doctype.getSystemID());
            return domImpl.createDocument("http://temporary", doctype.getElementName(), domDocType);
        }
    }

    private static final String CVS_ID = "@(#) $RCSfile: AbstractDOMAdapter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:31 $ $Name:  $";
}
