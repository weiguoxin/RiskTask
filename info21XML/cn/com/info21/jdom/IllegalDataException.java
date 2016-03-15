// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IllegalDataException.java

package cn.com.info21.jdom;


public class IllegalDataException extends IllegalArgumentException
{

    public IllegalDataException(String data, String construct, String reason)
    {
        super("The data \"" + data + "\" is not legal for a JDOM " + construct + ": " + reason + ".");
    }

    public IllegalDataException(String data, String construct)
    {
        super("The data \"" + data + "\" is not legal for a JDOM " + construct + ".");
    }

    private static final String CVS_ID = "@(#) $RCSfile: IllegalDataException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
}
