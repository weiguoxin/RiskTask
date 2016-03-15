// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOMException.java

package cn.com.info21.dom;


public class DOMException extends RuntimeException
{

    public DOMException(short code, String message)
    {
        super(message);
        this.code = code;
    }

    public short code;
    public static final short INDEX_SIZE_ERR = 1;
    public static final short DOMSTRING_SIZE_ERR = 2;
    public static final short HIERARCHY_REQUEST_ERR = 3;
    public static final short WRONG_DOCUMENT_ERR = 4;
    public static final short INVALID_CHARACTER_ERR = 5;
    public static final short NO_DATA_ALLOWED_ERR = 6;
    public static final short NO_MODIFICATION_ALLOWED_ERR = 7;
    public static final short NOT_FOUND_ERR = 8;
    public static final short NOT_SUPPORTED_ERR = 9;
    public static final short INUSE_ATTRIBUTE_ERR = 10;
    public static final short INVALID_STATE_ERR = 11;
    public static final short SYNTAX_ERR = 12;
    public static final short INVALID_MODIFICATION_ERR = 13;
    public static final short NAMESPACE_ERR = 14;
    public static final short INVALID_ACCESS_ERR = 15;
}
