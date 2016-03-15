// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOMImplementation.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            DOMException, DocumentType, Document

public interface DOMImplementation
{

    public abstract boolean hasFeature(String s, String s1);

    public abstract DocumentType createDocumentType(String s, String s1, String s2)
        throws DOMException;

    public abstract Document createDocument(String s, String s1, DocumentType documenttype)
        throws DOMException;
}
