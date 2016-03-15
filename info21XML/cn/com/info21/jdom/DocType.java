// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DocType.java

package cn.com.info21.jdom;

import cn.com.info21.jdom.output.XMLOutputter;
import java.io.Serializable;

// Referenced classes of package cn.com.info21.jdom:
//            Document

public class DocType
    implements Serializable, Cloneable
{

    protected DocType()
    {
    }

    public DocType(String elementName, String publicID, String systemID)
    {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public DocType(String elementName, String systemID)
    {
        this(elementName, "", systemID);
    }

    public DocType(String elementName)
    {
        this(elementName, "", "");
    }

    public String getElementName()
    {
        return elementName;
    }

    public String getPublicID()
    {
        return publicID;
    }

    public DocType setPublicID(String publicID)
    {
        this.publicID = publicID;
        return this;
    }

    public String getSystemID()
    {
        return systemID;
    }

    public DocType setSystemID(String systemID)
    {
        this.systemID = systemID;
        return this;
    }

    public Document getDocument()
    {
        return document;
    }

    protected DocType setDocument(Document document)
    {
        this.document = document;
        return this;
    }

    public String toString()
    {
        return "[DocType: " + (new XMLOutputter()).outputString(this) + "]";
    }

    public final boolean equals(Object ob)
    {
        if(ob instanceof DocType)
        {
            DocType dt = (DocType)ob;
            return stringEquals(dt.elementName, elementName) && stringEquals(dt.publicID, publicID) && stringEquals(dt.systemID, systemID);
        } else
        {
            return false;
        }
    }

    private boolean stringEquals(String s1, String s2)
    {
        if(s1 == null && s2 == null)
            return true;
        if(s1 == null && s2 != null)
            return false;
        else
            return s1.equals(s2);
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    public Object clone()
    {
        DocType docType = null;
        try
        {
            docType = (DocType)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        docType.document = null;
        return docType;
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        boolean hasPublic = false;
        StringBuffer serForm = (new StringBuffer()).append("<!DOCTYPE ").append(elementName);
        if(publicID != null && !publicID.equals(""))
        {
            serForm.append(" PUBLIC \"").append(publicID).append("\"");
            hasPublic = true;
        }
        if(systemID != null && !systemID.equals(""))
        {
            if(!hasPublic)
                serForm.append(" SYSTEM");
            serForm.append(" \"").append(systemID).append("\"");
        }
        serForm.append(">");
        return serForm.toString();
    }

    private static final String CVS_ID = "@(#) $RCSfile: DocType.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected String elementName;
    protected String publicID;
    protected String systemID;
    protected Document document;
}
