// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharacterData.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, DOMException

public interface CharacterData
    extends Node
{

    public abstract String getData()
        throws DOMException;

    public abstract void setData(String s)
        throws DOMException;

    public abstract int getLength();

    public abstract String substringData(int i, int j)
        throws DOMException;

    public abstract void appendData(String s)
        throws DOMException;

    public abstract void insertData(int i, String s)
        throws DOMException;

    public abstract void deleteData(int i, int j)
        throws DOMException;

    public abstract void replaceData(int i, int j, String s)
        throws DOMException;
}
