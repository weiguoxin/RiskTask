// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IllegalTargetException.java

package cn.com.info21.jdom;


public class IllegalTargetException extends IllegalArgumentException
{

    public IllegalTargetException(String target, String reason)
    {
        super("The target \"" + target + "\" is not legal for JDOM/XML Processing Instructions: " + reason + ".");
    }

    public IllegalTargetException(String target)
    {
        super("The name \"" + target + "\" is not legal for JDOM/XML Processing Instructions.");
    }

    private static final String CVS_ID = "@(#) $RCSfile: IllegalTargetException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
}
