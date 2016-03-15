// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Comment.java

package cn.com.info21.jdom;

import cn.com.info21.jdom.output.XMLOutputter;
import java.io.Serializable;

// Referenced classes of package cn.com.info21.jdom:
//            IllegalDataException, Element, Document, Verifier

public class Comment
    implements Serializable, Cloneable
{

    protected Comment()
    {
    }

    public Comment(String text)
    {
        setText(text);
    }

    public Element getParent()
    {
        return parent;
    }

    protected Comment setParent(Element parent)
    {
        this.parent = parent;
        return this;
    }

    public Comment detach()
    {
        Element p = getParent();
        if(p != null)
        {
            p.removeContent(this);
        } else
        {
            Document d = getDocument();
            if(d != null)
                d.removeContent(this);
        }
        return this;
    }

    public Document getDocument()
    {
        if(document != null)
            return document;
        Element p = getParent();
        if(p != null)
            return p.getDocument();
        else
            return null;
    }

    protected Comment setDocument(Document document)
    {
        this.document = document;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public Comment setText(String text)
    {
        String reason;
        if((reason = Verifier.checkCommentData(text)) != null)
        {
            throw new IllegalDataException(text, "comment", reason);
        } else
        {
            this.text = text;
            return this;
        }
    }

    public String toString()
    {
        return "[Comment: " + (new XMLOutputter()).outputString(this) + "]";
    }

    public final boolean equals(Object ob)
    {
        return ob == this;
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    public Object clone()
    {
        Comment comment = null;
        try
        {
            comment = (Comment)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        comment.parent = null;
        comment.document = null;
        return comment;
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        return "<!--" + text + "-->";
    }

    private static final String CVS_ID = "@(#) $RCSfile: Comment.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected String text;
    protected Element parent;
    protected Document document;
}
