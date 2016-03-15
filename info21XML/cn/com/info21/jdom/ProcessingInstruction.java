// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessingInstruction.java

package cn.com.info21.jdom;

import cn.com.info21.jdom.output.XMLOutputter;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package cn.com.info21.jdom:
//            IllegalTargetException, Verifier, Element, Document

public class ProcessingInstruction
    implements Serializable, Cloneable
{

    protected ProcessingInstruction()
    {
    }

    public ProcessingInstruction(String target, Map data)
    {
        String reason;
        if((reason = Verifier.checkProcessingInstructionTarget(target)) != null)
        {
            throw new IllegalTargetException(target, reason);
        } else
        {
            this.target = target;
            setData(data);
            return;
        }
    }

    public ProcessingInstruction(String target, String data)
    {
        String reason;
        if((reason = Verifier.checkProcessingInstructionTarget(target)) != null)
        {
            throw new IllegalTargetException(target, reason);
        } else
        {
            this.target = target;
            setData(data);
            return;
        }
    }

    public Element getParent()
    {
        return parent;
    }

    protected ProcessingInstruction setParent(Element parent)
    {
        this.parent = parent;
        return this;
    }

    public ProcessingInstruction detach()
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

    protected ProcessingInstruction setDocument(Document document)
    {
        this.document = document;
        return this;
    }

    public String getTarget()
    {
        return target;
    }

    public String getData()
    {
        return rawData;
    }

    public ProcessingInstruction setData(String data)
    {
        rawData = data;
        mapData = parseData(data);
        return this;
    }

    public ProcessingInstruction setData(Map data)
    {
        rawData = toString(data);
        mapData = data;
        return this;
    }

    public String getValue(String name)
    {
        return (String)mapData.get(name);
    }

    public ProcessingInstruction setValue(String name, String value)
    {
        mapData.put(name, value);
        rawData = toString(mapData);
        return this;
    }

    public boolean removeValue(String name)
    {
        if(mapData.remove(name) != null)
        {
            rawData = toString(mapData);
            return true;
        } else
        {
            return false;
        }
    }

    private String toString(Map mapData)
    {
        StringBuffer rawData = new StringBuffer();
        String name;
        String value;
        for(Iterator i = mapData.keySet().iterator(); i.hasNext(); rawData.append(name).append("=\"").append(value).append("\" "))
        {
            name = (String)i.next();
            value = (String)mapData.get(name);
        }

        rawData.setLength(rawData.length() - 1);
        return rawData.toString();
    }

    private Map parseData(String rawData)
    {
        Map data = new HashMap();
        for(String inputData = rawData.trim(); !inputData.trim().equals("");)
        {
            String name = "";
            String value = "";
            int startName = 0;
            char previousChar = inputData.charAt(startName);
            int pos;
            for(pos = 1; pos < inputData.length(); pos++)
            {
                char currentChar = inputData.charAt(pos);
                if(currentChar == '=')
                {
                    name = inputData.substring(startName, pos).trim();
                    value = extractQuotedString(inputData.substring(pos + 1).trim());
                    if(value == null)
                        return new HashMap();
                    pos += value.length() + 1;
                    break;
                }
                if(Character.isWhitespace(previousChar) && !Character.isWhitespace(currentChar))
                    startName = pos;
                previousChar = currentChar;
            }

            inputData = inputData.substring(pos);
            if(name.length() > 0 && value != null)
                data.put(name, value);
        }

        return data;
    }

    private String extractQuotedString(String rawData)
    {
        boolean inQuotes = false;
        char quoteChar = '"';
        int start = 0;
        for(int pos = 0; pos < rawData.length(); pos++)
        {
            char currentChar = rawData.charAt(pos);
            if(currentChar == '"' || currentChar == '\'')
                if(!inQuotes)
                {
                    quoteChar = currentChar;
                    inQuotes = true;
                    start = pos + 1;
                } else
                if(quoteChar == currentChar)
                {
                    inQuotes = false;
                    return rawData.substring(start, pos);
                }
        }

        return null;
    }

    public String toString()
    {
        return "[ProcessingInstruction: " + (new XMLOutputter()).outputString(this) + "]";
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
        ProcessingInstruction pi = null;
        try
        {
            pi = (ProcessingInstruction)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        pi.parent = null;
        pi.document = null;
        if(mapData != null)
            pi.mapData = parseData(rawData);
        return pi;
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        if(!"".equals(rawData))
            return "<?" + target + " " + rawData + "?>";
        else
            return "<?" + target + "?>";
    }

    private static final String CVS_ID = "@(#) $RCSfile: ProcessingInstruction.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
    protected String target;
    protected String rawData;
    protected Map mapData;
    protected Element parent;
    protected Document document;
}
