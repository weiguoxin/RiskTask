// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DocumentType.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, NamedNodeMap

public interface DocumentType
    extends Node
{

    public abstract String getName();

    public abstract NamedNodeMap getEntities();

    public abstract NamedNodeMap getNotations();

    public abstract String getPublicId();

    public abstract String getSystemId();

    public abstract String getInternalSubset();
}
