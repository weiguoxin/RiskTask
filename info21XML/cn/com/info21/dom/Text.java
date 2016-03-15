// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Text.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            CharacterData, DOMException

public interface Text
    extends CharacterData
{

    public abstract Text splitText(int i)
        throws DOMException;
}
