package com.wgx.util.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static void main(String []a){
		try {
			Date limit = DateUtil.getDate("2014/4/10", "yyyy/M/d");
			System.out.println(limit.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * String ת Date
	 * 
	 * @param dateString
	 *            1987-05-18
	 * @return Date
	 */
	public static Date getDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println("�ַ�����ʽ����ȷ,��:1987-05-18. " + e.getMessage());
		}
		return date;
	}
	
	/**
	 * String ת Date
	 * 
	 * @param dateString
	 * @param dateFormat �Զ���ת����ʽ           
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date getDate(String dateString, String dateFormat) throws ParseException {
		if(null==dateString||"".equals(dateString)||null==dateFormat||"".equals(dateFormat)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		date = sdf.parse(dateString);
		
		return date;
	}

	/**
	 * 
	 * Date ת �ַ��� e:1987-5-1
	 * @return
	 */
	public static String getNowDateString() {
		return getNowDateString(new Date());
	}

	public static String getNowDateString(Date date) {
		String dateS = new SimpleDateFormat("yyyy-M-dd").format(date);
		return dateS;
	}

	public static String getDateString(Date date) {
		if(date == null)
			return new SimpleDateFormat("yyyy-MM").format(getDate("1949-01-1"));
		String dateS = new SimpleDateFormat("yyyy-MM").format(date);
		return dateS;
	}
	
	public static String getDateString(Date date, String dateFormat) {
		String dateS = new SimpleDateFormat(dateFormat).format(date);
		return dateS;
	}
	
	/**
	 * �Ƿ�ͬһ��
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// ���12�µ����һ�ܺ�������һ�ܵĻ������һ�ܼ���������ĵ�һ��
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * �Ƿ���ͬ�·�
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMonth(Date date1,Date date2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.MONTH) == cal2
					.get(Calendar.MONTH))
				return true;
		}
		return false;
	}
	
	/**
	 * �����һ������
	 * @param date
	 * @return
	 */
	public static String getMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return new SimpleDateFormat("yyyy-M-dd").format(c.getTime());
	}

	/**
	 * ������������
	 * @param date
	 * @return
	 */
	public static String getFriday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		return new SimpleDateFormat("yyyy-M-dd").format(c.getTime());
	}
	
	/**
	 * ���ĳ��1��
	 * @return
	 */
	public static String getFirstDayOfMonth(Date date){      
	       String str = "";    
	       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");        
	       Calendar lastDate = Calendar.getInstance();  
	       lastDate.setTime(date);
	       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��    
	       str=sdf.format(lastDate.getTime());    
	       return str;      
	    }     

	/**
	 * ���ĳ��1��
	 * @return Date
	 */
	public static Date getFirstDayOfMonthDate(Date date){      
	       String str = "";    
	       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");        
	       Calendar lastDate = Calendar.getInstance();  
	       lastDate.setTime(date);
	       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��     
	       return lastDate.getTime();      
	    }    
	
	
	/**
	 *  ���ĳ�����һ��
	 * @return
	 */
    public static String getLastDayOfMonth(Date date){      
       String str = "";    
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");        
       Calendar lastDate = Calendar.getInstance();   
       lastDate.setTime(date);
       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��    
       lastDate.add(Calendar.MONTH,1);//��һ���£���Ϊ���µ�1��    
       lastDate.add(Calendar.DATE,-1);//��ȥһ�죬��Ϊ�������һ��    
       str=sdf.format(lastDate.getTime());    
       return str;      
    }  
}
