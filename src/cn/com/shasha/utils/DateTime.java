package cn.com.shasha.utils;
import java.text.DateFormat;   
import java.text.ParsePosition;   
import java.text.SimpleDateFormat;   
import java.util.Calendar;   
import java.util.Date;   
import java.util.GregorianCalendar; 
public class DateTime {
    //����ȫ�ֿ��� ��һ�ܣ����ܣ���һ�ܵ������仯   
    private  int weeks = 0;   
    private int MaxDate;//һ���������   
    private int MaxYear;//һ���������   
    
    
    /**  
        * �õ��������ڼ�ļ������  
        */  
    public static String getTwoDay(String sj1, String sj2) {   
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");   
        long day = 0;   
        try {   
         java.util.Date date = myFormatter.parse(sj1);   
         java.util.Date mydate = myFormatter.parse(sj2);   
         day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
        } catch (Exception e) {   
         return "";   
        }   
        return day + "";   
    }   
  
  
    /**  
        * ����һ�����ڣ����������ڼ����ַ���  
        *   
        * @param sdate  
        * @return  
        */  
    public static String getWeek(String sdate) {   
        // ��ת��Ϊʱ��   
        Date date = DateTime.strToDate(sdate);   
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
        // int hour=c.get(Calendar.DAY_OF_WEEK);   
        // hour�д�ľ������ڼ��ˣ��䷶Χ 1~7   
        // 1=������ 7=����������������   
        return new SimpleDateFormat("EEEE").format(c.getTime());   
    }   
  
    /**  
        * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd   
        *   
        * @param strDate  
        * @return  
        */  
    public static Date strToDate(String strDate) {   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
        ParsePosition pos = new ParsePosition(0);   
        Date strtodate = formatter.parse(strDate, pos);   
        return strtodate;   
    }   
  
    /**  
        * ����ʱ��֮�������  
        *   
        * @param date1  
        * @param date2  
        * @return  
        */  
    public static long getDays(String date1, String date2) {   
        if (date1 == null || date1.equals(""))   
         return 0;   
        if (date2 == null || date2.equals(""))   
         return 0;   
        // ת��Ϊ��׼ʱ��   
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");   
        java.util.Date date = null;   
        java.util.Date mydate = null;   
        try {   
         date = myFormatter.parse(date1);   
         mydate = myFormatter.parse(date2);   
        } catch (Exception e) {   
        }   
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
        return day;   
    }   
  
  
  
       
    // ���㵱�����һ��,�����ַ���   
    public String getDefaultDay(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��   
       lastDate.add(Calendar.MONTH,1);//��һ���£���Ϊ���µ�1��   
       lastDate.add(Calendar.DATE,-1);//��ȥһ�죬��Ϊ�������һ��   
          
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    // ���µ�һ��   
    public String getPreviousMonthFirst(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��   
       lastDate.add(Calendar.MONTH,-1);//��һ���£���Ϊ���µ�1��   
       //lastDate.add(Calendar.DATE,-1);//��ȥһ�죬��Ϊ�������һ��   
          
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    //��ȡ���µ�һ��   
    public String getFirstDayOfMonth(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//��Ϊ��ǰ�µ�1��   
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    // ��ñ��������յ�����     
    public String getCurrentWeekday() {   
        weeks = 0;   
        int mondayPlus = this.getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus+6);   
        Date monday = currentDate.getTime();   
           
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
       
       
    //��ȡ����ʱ��    
    public String getNowTime(String dateformat){   
        Date   now   =   new   Date();      
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat(dateformat);//���Է�����޸����ڸ�ʽ      
        String  hehe  = dateFormat.format(now);      
        return hehe;   
    }   
       
    // ��õ�ǰ�����뱾������������   
    private int getMondayPlus() {   
        Calendar cd = Calendar.getInstance();   
        // ��ý�����һ�ܵĵڼ��죬�������ǵ�һ�죬���ڶ��ǵڶ���......   
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //��Ϊ���й����һ��Ϊ��һ�����������1   
        if (dayOfWeek == 1) {   
            return 0;   
        } else {   
            return 1 - dayOfWeek;   
        }   
    }   
       
    //��ñ���һ������   
    public String getMondayOFWeek(){   
         weeks = 0;   
         int mondayPlus = this.getMondayPlus();   
         GregorianCalendar currentDate = new GregorianCalendar();   
         currentDate.add(GregorianCalendar.DATE, mondayPlus);   
         Date monday = currentDate.getTime();   
            
         DateFormat df = DateFormat.getDateInstance();   
         String preMonday = df.format(monday);   
         return preMonday;   
    }   
       
  //�����Ӧ�ܵ�����������   
    public String getSaturday() {   
        int mondayPlus = this.getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
       
 // ������������յ�����   
    public String getPreviousWeekSunday() {   
        weeks=0;   
        weeks--;   
        int mondayPlus = this.getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus+weeks);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
  
 // �����������һ������   
    public String getPreviousWeekday() {   
        weeks--;   
        int mondayPlus = this.getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
       
    // �����������һ������   
    public String getNextMonday() {   
        weeks++;   
        int mondayPlus = this.getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday; 
    } 
    /*
     public static void main(String[] args) {   
         DateTime tt = new DateTime();   
         System.out.println("��ȡ��������:"+tt.getNowTime("yyyy-MM-dd"));   
         System.out.println("��ȡ����һ����:"+tt.getMondayOFWeek());   
         System.out.println("��ȡ�����յ�����~:"+tt.getCurrentWeekday());   
         System.out.println("��ȡ����һ����:"+tt.getPreviousWeekday());   
         System.out.println("��ȡ����������:"+tt.getPreviousWeekSunday());   
         System.out.println("��ȡ����һ����:"+tt.getNextMonday());   
         System.out.println("��ȡ����������:"+tt.getNextSunday());   
         System.out.println("�����Ӧ�ܵ�����������:"+tt.getNowTime("yyyy-MM-dd"));   
         System.out.println("��ȡ���µ�һ������:"+tt.getFirstDayOfMonth());   
         System.out.println("��ȡ�������һ������:"+tt.getDefaultDay());   
         System.out.println("��ȡ���µ�һ������:"+tt.getPreviousMonthFirst());   
         System.out.println("��ȡ�������һ�������:"+tt.getPreviousMonthEnd());   
         System.out.println("��ȡ���µ�һ������:"+tt.getNextMonthFirst());   
         System.out.println("��ȡ�������һ������:"+tt.getNextMonthEnd());   
         System.out.println("��ȡ����ĵ�һ������:"+tt.getCurrentYearFirst());   
         System.out.println("��ȡ�������һ������:"+tt.getCurrentYearEnd());   
         System.out.println("��ȡȥ��ĵ�һ������:"+tt.getPreviousYearFirst());   
         System.out.println("��ȡȥ������һ������:"+tt.getPreviousYearEnd());   
         System.out.println("��ȡ�����һ������:"+tt.getNextYearFirst());   
         System.out.println("��ȡ�������һ������:"+tt.getNextYearEnd());   
         System.out.println("��ȡ�����ȵ�һ�쵽���һ��:"+tt.getThisSeasonTime(11));   
         System.out.println("��ȡ��������֮��������2008-12-1~2008-9.29:"+TimeTest.getTwoDay("2008-12-1","2008-9-29"));   
     } 
     */
}
