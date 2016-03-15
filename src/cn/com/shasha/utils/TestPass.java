package cn.com.shasha.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.text.*;
import cn.com.info21.util.*;
import com.url.ajax.json.*;
public class TestPass
{
 public static void main(String[] args)
 {
  String strDate="2005年04月22日";
  //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
  //必须捕获异常
  String abc = "太$2_";
  System.out.println("abc--"+abc.split("\\$")[0]);
  try
  { 
   Date date=simpleDateFormat.parse(strDate);
   System.out.println(StringUtils.toDate("2011-7-19 00:00:00"));
   System.out.println(StringUtils.toTimestamp("0000-7-19 11:03:25",0));
   System.out.println(StringUtils.toTimestamp("2011-7-19 00:00:00",0).compareTo(StringUtils.toTimestamp("2010-7-19 11:03:25",0)));
   JSONArray jsonHDArray = new JSONArray();
   JSONObject jsontemp = new JSONObject();
	jsontemp.put("operdate", "2011-7-19 00:00:00");
	jsontemp.put("invcode", "111");	
	jsontemp.put("operuser", "ss");
	jsontemp.put("operdept", "ee");
	jsontemp.put("operstatus", "3");
	jsontemp.put("company", "ttt");
	jsontemp.put("details", 1);	 
	jsontemp.put("operdate", "2012-7-19 00:00:00");
	jsontemp.put("invcode", "222");	
	jsontemp.put("operuser", "dd");
	jsontemp.put("operdept", "yy");
	jsontemp.put("operstatus", "5");
	jsontemp.put("company", "");
	jsontemp.put("details", 1);		
	jsonHDArray.put(jsontemp);
	System.out.println(jsonHDArray.toString());
	char colchar=(char)(3+65);
	System.out.println((char)(colchar-3));
    String strBoolean = "";
    
    //Do the String to boolean conversion
    boolean theValue = Boolean.parseBoolean(strBoolean);
    
    System.out.println(theValue);	
    java.util.Calendar c = java.util.Calendar.getInstance(); 
    System.out.println(c.get(java.util.Calendar.YEAR));
  }
  catch(Exception e)
  {
   e.printStackTrace();
  }
 }
}
		