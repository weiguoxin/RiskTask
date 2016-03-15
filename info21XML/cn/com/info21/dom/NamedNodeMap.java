// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NamedNodeMap.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            DOMException, Node

public interface NamedNodeMap
{

    public abstract Node getNamedItem(String s);

    public abstract Node setNamedItem(Node node)
        throws DOMException;

    public abstract Node removeNamedItem(String s)
        throws DOMException;

    public abstract Node item(int i);

    public abstract int getLength();

    public abstract Node getNamedItemNS(String s, String s1);

    public abstract Node setNamedItemNS(Node node)
        throws DOMException;

    public abstract Node removeNamedItemNS(String s, String s1)
        throws DOMException;
}
