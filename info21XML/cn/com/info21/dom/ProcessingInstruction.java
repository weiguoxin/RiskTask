// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessingInstruction.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, DOMException

public interface ProcessingInstruction
    extends Node
{

    public abstract String getTarget();

    public abstract String getData();

    public abstract void setData(String s)
        throws DOMException;
}
