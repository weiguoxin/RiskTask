// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NamespaceStack.java

package cn.com.info21.jdom.output;

import cn.com.info21.jdom.Namespace;
import java.util.Stack;
//import java.util.Vector;

class NamespaceStack
{

    public NamespaceStack()
    {
        prefixes = new Stack();
        uris = new Stack();
    }

    public void push(Namespace ns)
    {
        prefixes.push(ns.getPrefix());
        uris.push(ns.getURI());
    }

    public String pop()
    {
        String prefix = (String)prefixes.pop();
        uris.pop();
        return prefix;
    }

    public int size()
    {
        return prefixes.size();
    }

    public String getURI(String prefix)
    {
        int index = prefixes.lastIndexOf(prefix);
        if(index == -1)
        {
            return null;
        } else
        {
            String uri = (String)uris.elementAt(index);
            return uri;
        }
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        String sep = System.getProperty("line.separator");
        buf.append("Stack: " + prefixes.size() + sep);
        for(int i = 0; i < prefixes.size(); i++)
            buf.append(prefixes.elementAt(i) + "&" + uris.elementAt(i) + sep);

        return buf.toString();
    }

    private static final String CVS_ID = "@(#) $RCSfile: NamespaceStack.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:29 $ $Name:  $";
    private Stack prefixes;
    private Stack uris;
}
