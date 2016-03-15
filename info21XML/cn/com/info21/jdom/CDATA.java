// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CDATA.java

package cn.com.info21.jdom;

import cn.com.info21.jdom.output.XMLOutputter;
import java.io.Serializable;

// Referenced classes of package cn.com.info21.jdom:
//            IllegalDataException, Verifier

public class CDATA
    implements Serializable, Cloneable
{

    protected CDATA()
    {
    }

    public CDATA(String text)
    {
        String reason;
        if((reason = Verifier.checkCDATASection(text)) != null)
        {
            throw new IllegalDataException(text, "CDATA section", reason);
        } else
        {
            this.text = text;
            return;
        }
    }

    public String getText()
    {
        return text;
    }

    public String toString()
    {
        return "[CDATA: " + (new XMLOutputter()).outputString(this) + "]";
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
        CDATA cdata = null;
        try
        {
            cdata = (CDATA)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return cdata;
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        return "<![CDATA[" + text + "]]>";
    }

    private static final String CVS_ID = "@(#) $RCSfile: CDATA.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected String text;
}
