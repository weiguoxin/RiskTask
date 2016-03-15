// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IllegalNameException.java

package cn.com.info21.jdom;


public class IllegalNameException extends IllegalArgumentException
{

    public IllegalNameException(String name, String construct, String reason)
    {
        super("The name \"" + name + "\" is not legal for JDOM/XML " + construct + "s: " + reason + ".");
    }

    public IllegalNameException(String name, String construct)
    {
        super("The name \"" + name + "\" is not legal for JDOM/XML " + construct + "s.");
    }

    private static final String CVS_ID = "@(#) $RCSfile: IllegalNameException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
}
