// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Attribute.java

package cn.com.info21.jdom;

import java.io.*;

// Referenced classes of package cn.com.info21.jdom:
//            IllegalNameException, IllegalDataException, DataConversionException, Namespace, 
//            Element, Verifier, Document

public class Attribute
    implements Serializable, Cloneable
{

    protected Attribute()
    {
    }

    public Attribute(String name, String value, Namespace namespace)
    {
        setName(name);
        setValue(value);
        setNamespace(namespace);
    }

    public Attribute(String name, String value)
    {
        this(name, value, Namespace.NO_NAMESPACE);
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

    protected Attribute setParent(Element parent)
    {
        this.parent = parent;
        return this;
    }

    public Attribute detach()
    {
        Element p = getParent();
        if(p != null)
            p.removeAttribute(getName(), getNamespace());
        return this;
    }

    public String getName()
    {
        return name;
    }

    public Attribute setName(String name)
    {
        String reason;
        if((reason = Verifier.checkAttributeName(name)) != null)
        {
            throw new IllegalNameException(name, "attribute", reason);
        } else
        {
            this.name = name;
            return this;
        }
    }

    public String getQualifiedName()
    {
        String prefix = namespace.getPrefix();
        if(prefix != null && !prefix.equals(""))
            return prefix + ':' + getName();
        else
            return getName();
    }

    public String getNamespacePrefix()
    {
        return namespace.getPrefix();
    }

    public String getNamespaceURI()
    {
        return namespace.getURI();
    }

    public Namespace getNamespace()
    {
        return namespace;
    }

    public Attribute setNamespace(Namespace namespace)
    {
        if(namespace == null)
            namespace = Namespace.NO_NAMESPACE;
        if(namespace != Namespace.NO_NAMESPACE && namespace.getPrefix().equals(""))
        {
            throw new IllegalNameException("", "attribute namespace", "An attribute namespace without a prefix can only be the NO_NAMESPACE namespace");
        } else
        {
            this.namespace = namespace;
            return this;
        }
    }

    public String getValue()
    {
        return value;
    }

    public Attribute setValue(String value)
    {
        String reason = null;
        if((reason = Verifier.checkCharacterData(value)) != null)
        {
            throw new IllegalDataException(value, "attribute", reason);
        } else
        {
            this.value = value;
            return this;
        }
    }

    public String toString()
    {
        return "[Attribute: " + getQualifiedName() + "=\"" + value + "\"" + "]";
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
        Attribute attribute = null;
        try
        {
            attribute = (Attribute)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        attribute.parent = null;
        return attribute;
    }

    public int getIntValue()
        throws DataConversionException
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new DataConversionException(name, "int");
        }
    }

    public long getLongValue()
        throws DataConversionException
    {
        try
        {
            return Long.parseLong(value);
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new DataConversionException(name, "long");
        }
    }

    public float getFloatValue()
        throws DataConversionException
    {
        try
        {
            return Float.valueOf(value).floatValue();
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new DataConversionException(name, "float");
        }
    }

    public double getDoubleValue()
        throws DataConversionException
    {
        try
        {
            return Double.valueOf(value).doubleValue();
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new DataConversionException(name, "double");
        }
    }

    public boolean getBooleanValue()
        throws DataConversionException
    {
        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes"))
            return true;
        if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("no"))
            return false;
        else
            throw new DataConversionException(name, "boolean");
    }

    private void writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        namespace = Namespace.getNamespace((String)in.readObject(), (String)in.readObject());
    }

    /**
     * @deprecated Method Attribute is deprecated
     */

    public Attribute(String name, String prefix, String uri, String value)
    {
        this(name, value, Namespace.getNamespace(prefix, uri));
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        return getQualifiedName() + "=\"" + value + "\"";
    }

    private static final String CVS_ID = "@(#) $RCSfile: Attribute.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
    protected String name;
    protected transient Namespace namespace;
    protected String value;
    protected Element parent;
}
