// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataConversionException.java

package cn.com.info21.jdom;


// Referenced classes of package cn.com.info21.jdom:
//            JDOMException

public class DataConversionException extends JDOMException
{

    public DataConversionException(String name, String dataType)
    {
        super("The XML construct " + name + " could not be converted to a " + dataType);
    }

    private static final String CVS_ID = "@(#) $RCSfile: DataConversionException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
}
