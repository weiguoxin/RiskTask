// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOMAdapter.java

package cn.com.info21.jdom.adapters;

import cn.com.info21.dom.Document;
import cn.com.info21.jdom.DocType;
import java.io.File;
import java.io.InputStream;

public interface DOMAdapter
{

    public abstract Document getDocument(File file, boolean flag)
        throws Exception;

    public abstract Document getDocument(InputStream inputstream, boolean flag)
        throws Exception;

    public abstract Document createDocument()
        throws Exception;

    public abstract Document createDocument(DocType doctype)
        throws Exception;
}
