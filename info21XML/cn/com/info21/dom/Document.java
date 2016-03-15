// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Document.java

package cn.com.info21.dom;


// Referenced classes of package cn.com.info21.dom:
//            Node, DOMException, DocumentType, DOMImplementation, 
//            Element, DocumentFragment, Text, Comment, 
//            CDATASection, ProcessingInstruction, Attr, EntityReference, 
//            NodeList

public interface Document
    extends Node
{

    public abstract DocumentType getDoctype();

    public abstract DOMImplementation getImplementation();

    public abstract Element getDocumentElement();

    public abstract Element createElement(String s)
        throws DOMException;

    public abstract DocumentFragment createDocumentFragment();

    public abstract Text createTextNode(String s);

    public abstract Comment createComment(String s);

    public abstract CDATASection createCDATASection(String s)
        throws DOMException;

    public abstract ProcessingInstruction createProcessingInstruction(String s, String s1)
        throws DOMException;

    public abstract Attr createAttribute(String s)
        throws DOMException;

    public abstract EntityReference createEntityReference(String s)
        throws DOMException;

    public abstract NodeList getElementsByTagName(String s);

    public abstract Node importNode(Node node, boolean flag)
        throws DOMException;

    public abstract Element createElementNS(String s, String s1)
        throws DOMException;

    public abstract Attr createAttributeNS(String s, String s1)
        throws DOMException;

    public abstract NodeList getElementsByTagNameNS(String s, String s1);

    public abstract Element getElementById(String s);
}
