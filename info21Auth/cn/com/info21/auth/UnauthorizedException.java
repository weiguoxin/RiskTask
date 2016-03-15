// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnauthorizedException.java

package cn.com.info21.auth;

import java.io.PrintStream;
import java.io.PrintWriter;

public class UnauthorizedException extends Exception
{

    public UnauthorizedException()
    {
        nestedThrowable = null;
    }

    public UnauthorizedException(String msg)
    {
        super(msg);
        nestedThrowable = null;
    }

    public UnauthorizedException(Throwable nestedThrowable)
    {
        this.nestedThrowable = null;
        this.nestedThrowable = nestedThrowable;
    }

    public UnauthorizedException(String msg, Throwable nestedThrowable)
    {
        super(msg);
        this.nestedThrowable = null;
        this.nestedThrowable = nestedThrowable;
    }

    public void printStackTrace()
    {
        super.printStackTrace();
        if(nestedThrowable != null)
            nestedThrowable.printStackTrace();
    }

    public void printStackTrace(PrintStream ps)
    {
        super.printStackTrace(ps);
        if(nestedThrowable != null)
            nestedThrowable.printStackTrace(ps);
    }

    public void printStackTrace(PrintWriter pw)
    {
        super.printStackTrace(pw);
        if(nestedThrowable != null)
            nestedThrowable.printStackTrace(pw);
    }

    private Throwable nestedThrowable;
}
