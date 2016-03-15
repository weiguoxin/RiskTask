// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Element.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, DOMException, Attr, NodeList

public interface Element
    extends Node
{

    public abstract String getTagName();

    public abstract String getAttribute(String s);

    public abstract void setAttribute(String s, String s1)
        throws DOMException;

    public abstract void removeAttribute(String s)
        throws DOMException;

    public abstract Attr getAttributeNode(String s);

    public abstract Attr setAttributeNode(Attr attr)
        throws DOMException;

    public abstract Attr removeAttributeNode(Attr attr)
        throws DOMException;

    public abstract NodeList getElementsByTagName(String s);

    public abstract String getAttributeNS(String s, String s1);

    public abstract void setAttributeNS(String s, String s1, String s2)
        throws DOMException;

    public abstract void removeAttributeNS(String s, String s1)
        throws DOMException;

    public abstract Attr getAttributeNodeNS(String s, String s1);

    public abstract Attr setAttributeNodeNS(Attr attr)
        throws DOMException;

    public abstract NodeList getElementsByTagNameNS(String s, String s1);

    public abstract boolean hasAttribute(String s);

    public abstract boolean hasAttributeNS(String s, String s1);
}
