// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CookieUtils.java

package cn.com.info21.authtools;

//import javax.servlet.ServletRequest;
import javax.servlet.http.*;

public class CookieUtils
{

    public CookieUtils()
    {
    }

    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        Cookie cookies[] = request.getCookies();
        if(cookies == null || name == null || name.length() == 0)
            return null;
        Cookie cookie = null;
        for(int i = 0; i < cookies.length; i++)
        {
            if(!cookies[i].getName().equals(name))
                continue;
            cookie = cookies[i];
            if(request.getServerName().equals(cookie.getDomain()))
                break;
        }

        return cookie;
    }

    public static void deleteCookie(HttpServletResponse response, Cookie cookie)
    {
        if(cookie != null)
        {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public static void setCookie(HttpServletResponse response, String name, String value)
    {
        setCookie(response, name, value, 0x278d00);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge)
    {
        if(value == null)
            value = "";
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
