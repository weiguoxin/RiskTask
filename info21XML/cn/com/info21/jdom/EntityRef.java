// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EntityRef.java

package cn.com.info21.jdom;

import java.io.Serializable;

// Referenced classes of package cn.com.info21.jdom:
//            Element, Document

public class EntityRef
    implements Serializable, Cloneable
{

    protected EntityRef()
    {
    }

    public EntityRef(String name)
    {
        this.name = name;
    }

    public EntityRef(String name, String publicID, String systemID)
    {
        this.name = name;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public Object clone()
    {
        EntityRef entity = null;
        try
        {
            entity = (EntityRef)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        entity.parent = null;
        entity.document = null;
        return entity;
    }

    public EntityRef detach()
    {
        Element p = getParent();
        if(p != null)
            p.removeContent(this);
        return this;
    }

    public final boolean equals(Object ob)
    {
        return ob == this;
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

    public String getName()
    {
        return name;
    }

    public Element getParent()
    {
        return parent;
    }

    public String getPublicID()
    {
        return publicID;
    }

    public String getSystemID()
    {
        return systemID;
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    protected EntityRef setParent(Element parent)
    {
        this.parent = parent;
        return this;
    }

    public EntityRef setName(String name)
    {
        this.name = name;
        return this;
    }

    public EntityRef setPublicID(String newPublicID)
    {
        publicID = newPublicID;
        return this;
    }

    public EntityRef setSystemID(String newSystemID)
    {
        systemID = newSystemID;
        return this;
    }

    public String toString()
    {
        return "[EntityRef: " + "&" + name + ";" + "]";
    }

    private static final String CVS_ID = "@(#) $RCSfile: EntityRef.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected String name;
    protected String publicID;
    protected String systemID;
    protected Element parent;
    protected Document document;
}
