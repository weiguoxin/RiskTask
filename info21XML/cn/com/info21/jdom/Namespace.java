// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Namespace.java

package cn.com.info21.jdom;

import java.util.HashMap;

// Referenced classes of package cn.com.info21.jdom:
//            IllegalNameException, Verifier

public final class Namespace
{

    public static Namespace getNamespace(String prefix, String uri)
    {
        if(prefix == null || prefix.trim().equals(""))
            prefix = "";
        if(uri == null || uri.trim().equals(""))
            uri = "";
        if(prefix.equals("xml"))
            return XML_NAMESPACE;
        String lookup = (new StringBuffer(64)).append(prefix).append('&').append(uri).toString();
        Namespace preexisting = (Namespace)namespaces.get(lookup);
        if(preexisting != null)
            return preexisting;
        String reason;
        if((reason = Verifier.checkNamespacePrefix(prefix)) != null)
            throw new IllegalNameException(prefix, "Namespace prefix", reason);
        if((reason = Verifier.checkNamespaceURI(uri)) != null)
            throw new IllegalNameException(uri, "Namespace URI", reason);
        if(!prefix.equals("") && uri.equals(""))
        {
            throw new IllegalNameException("", "namespace", "Namespace URIs must be non-null and non-empty Strings");
        } else
        {
            Namespace ns = new Namespace(prefix, uri);
            namespaces.put(lookup, ns);
            return ns;
        }
    }

    public static Namespace getNamespace(String uri)
    {
        return getNamespace("", uri);
    }

    private Namespace(String prefix, String uri)
    {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getURI()
    {
        return uri;
    }

    public boolean equals(Object ob)
    {
        if(ob instanceof Namespace)
            return uri.equals(((Namespace)ob).uri);
        else
            return false;
    }

    public String toString()
    {
        return "[Namespace: prefix \"" + prefix + "\" is mapped to URI \"" + uri + "\"]";
    }

    public int hashCode()
    {
        return uri.hashCode();
    }

    private static final String CVS_ID = "@(#) $RCSfile: Namespace.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    private static HashMap namespaces;
    public static final Namespace NO_NAMESPACE;
    public static final Namespace XML_NAMESPACE;
    private String prefix;
    private String uri;

    static 
    {
        NO_NAMESPACE = new Namespace("", "");
        XML_NAMESPACE = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
        namespaces = new HashMap();
        namespaces.put("&", NO_NAMESPACE);
        namespaces.put("xml&http://www.w3.org/XML/1998/namespace", XML_NAMESPACE);
    }
}
