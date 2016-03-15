// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JDOMFactory.java

package cn.com.info21.jdom.input;

import cn.com.info21.jdom.*;
import java.util.Map;

public interface JDOMFactory
{

    public abstract Attribute attribute(String s, String s1, Namespace namespace);

    public abstract Attribute attribute(String s, String s1);

    public abstract CDATA cdata(String s);

    public abstract Comment comment(String s);

    public abstract DocType docType(String s, String s1, String s2);

    public abstract DocType docType(String s, String s1);

    public abstract DocType docType(String s);

    public abstract Document document(Element element1, DocType doctype);

    public abstract Document document(Element element1);

    public abstract Element element(String s, Namespace namespace);

    public abstract Element element(String s);

    public abstract Element element(String s, String s1);

    public abstract Element element(String s, String s1, String s2);

    public abstract ProcessingInstruction processingInstruction(String s, Map map);

    public abstract ProcessingInstruction processingInstruction(String s, String s1);

    public abstract EntityRef entityRef(String s);

    public abstract EntityRef entityRef(String s, String s1, String s2);
}
