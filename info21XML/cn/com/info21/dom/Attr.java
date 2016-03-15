// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Attr.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, DOMException, Element

public interface Attr
    extends Node
{

    public abstract String getName();

    public abstract boolean getSpecified();

    public abstract String getValue();

    public abstract void setValue(String s)
        throws DOMException;

    public abstract Element getOwnerElement();
}
