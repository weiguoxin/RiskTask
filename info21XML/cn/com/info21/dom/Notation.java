// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Notation.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node

public interface Notation
    extends Node
{

    public abstract String getPublicId();

    public abstract String getSystemId();
}
