// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassUtils.java

package cn.com.info21.util;

import java.io.InputStream;

public class ClassUtils
{

    public static Class forName(String className)
        throws ClassNotFoundException
    {
        return instance.loadClass(className);
    }

    public static InputStream getResourceAsStream(String name)
    {
        return instance.loadResource(name);
    }

    private ClassUtils()
    {
    }

    public Class loadClass(String className)
        throws ClassNotFoundException
    {
        Class theClass = null;
        try
        {
            theClass = Class.forName(className);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            try
            {
                theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            }
            catch(ClassNotFoundException classnotfoundexception1)
            {
                theClass = getClass().getClassLoader().loadClass(className);
            }
        }
        return theClass;
    }

    public InputStream loadResource(String name)
    {
        InputStream in = getClass().getResourceAsStream(name);
        if(in == null)
        {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if(in == null)
                in = getClass().getClassLoader().getResourceAsStream(name);
        }
        return in;
    }

    private static ClassUtils instance = new ClassUtils();

}
