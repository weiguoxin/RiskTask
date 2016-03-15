// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JDOMException.java

package cn.com.info21.jdom;

import java.io.PrintStream;
import java.io.PrintWriter;
public class JDOMException extends Exception
{

    public JDOMException()
    {
        super("Error occurred in JDOM application.");
    }

    public JDOMException(String message)
    {
        super(message);
    }

    public JDOMException(String message, Throwable cause)
    {
        super(message);
        this.cause = cause;
    }

    public String getMessage()
    {
        if(cause != null)
            return super.getMessage() + ": " + cause.getMessage();
        else
            return super.getMessage();
    }

    public void printStackTrace()
    {
        super.printStackTrace();
        if(cause != null)
        {
            cause.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s)
    {
        super.printStackTrace(s);
        if(cause != null)
        {
            s.print("Root cause: ");
            cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter w)
    {
        super.printStackTrace(w);
        if(cause != null)
        {
            w.print("Root cause: ");
            cause.printStackTrace(w);
        }
    }

    public Throwable getCause()
    {
        return cause;
    }

    /**
     * @deprecated Method getRootCause is deprecated
     */

    public Throwable getRootCause()
    {
        return cause;
    }

    private static final String CVS_ID = "@(#) $RCSfile: JDOMException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected Throwable cause;
}
