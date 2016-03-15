// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Text.java

package cn.com.info21.jdom;

import java.io.Serializable;

// Referenced classes of package cn.com.info21.jdom:
//            Element, Document

public class Text
    implements Serializable, Cloneable
{

    protected Text()
    {
    }

    public Text(String stringValue)
    {
        value = new StringBuffer(stringValue);
    }

    public String getValue()
    {
        return value.toString();
    }

    public void setValue(String stringValue)
    {
        value.setLength(0);
        if(stringValue == null)
            value.append("");
        else
            value.append(stringValue);
    }

    public void append(String stringValue)
    {
        if(stringValue == null)
        {
            return;
        } else
        {
            value.append(stringValue);
            return;
        }
    }

    public Element getParent()
    {
        return parent;
    }

    public Document getDocument()
    {
        if(parent != null)
            return parent.getDocument();
        else
            return null;
    }

    public String toString()
    {
        return (new StringBuffer(64)).append("Text: ").append(getValue()).toString();
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    public Object clone()
    {
        Text text = null;
        try
        {
            text = (Text)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        text.parent = null;
        text.value = new StringBuffer(value.toString());
        return text;
    }

    public final boolean equals(Object ob)
    {
        return this == ob;
    }

    protected StringBuffer value;
    protected Element parent;
}
