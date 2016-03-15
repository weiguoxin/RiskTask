package cn.com.info21.util;

/**
 * StringUtils.java
 * 2005-11-7 刘克华整理,去掉MD5加密、Html转换相关方法。
 */

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;

/**
 * 对字符串进行多种处理的公共函数
 * 创建日期：(2001-4-13 9:18:11)
 * @author：suker
 */

public class StringUtils {

	private static Random randGen = null;
	private static char numbersAndLetters[] = null;
	private static MessageDigest digest = null;

	public StringUtils() {
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2002-1-17 12:47:18)
	 * @return java.lang.String[]
	 * @param str java.lang.String
	 * @param divStr java.lang.String
	 */
	public static final String[] explode(String str, String divStr) {
		String tempStr = str;
		if (tempStr == null) {
			tempStr = "";
		}
		ArrayList tempList = new ArrayList();
		int index;
		while (tempStr.indexOf(divStr) > 0) {
			index = tempStr.indexOf(divStr);
			tempList.add(tempStr.substring(0, index));
			tempStr = tempStr.substring(index + divStr.length());
		}
		tempList.add(tempStr);
		String result[] = new String[tempList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) tempList.get(i);
		}
		return result;
	}
	/**
	 * 获取指定格式的当前时间
	 * 默认下时间格式为(yyyy-MM-dd)
	 * @param format 指定你需要的日期格式，如果使用默认的可以指定为null
	 * @return 返回当前的日期
	*/
	public static final String getCurrentDate(String format) {
		SimpleDateFormat formatter = null;
		if (format == null || "".equals(format)) {
			formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		} else {
			formatter = new SimpleDateFormat(format, Locale.getDefault());
		}
		java.util.Date currentDate = new java.util.Date();
		return formatter.format(currentDate).trim();
	}
	/**
	 * 检查指定字符串中是否存在中文字符。
	 * @param checkStr 指定需要检查的字符串。
	 * @return 逻辑值（True Or False）。
	 */
	public static final boolean hasChinese(String checkStr) {
		boolean checkedStatus = false, isError = false;
		String spStr = " _-";
		int checkStrLength = checkStr.length() - 1;
		for (int i = 0; i <= checkStrLength; i++) {
			char ch = checkStr.charAt(i);
			if (ch < '\176') {
				ch = Character.toUpperCase(ch);
				if ((ch < 'A' || ch > 'Z')
					&& (ch < '0' || ch > '9')
					&& (spStr.indexOf(ch) < 0)) {
					isError = true;
				}
			}
		}
		checkedStatus = !isError;
		return checkedStatus;
	}
	/** 
	 * MD5加密函数（JDK自带）。
	 * @since JDK1.2以上
	 * @param s 是需要加密的字符串。
	 * @return 加密后的字符串。
	 */
	public static final synchronized String hash(String s) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nosuchalgorithmexception) {

				nosuchalgorithmexception.printStackTrace();
			}
		}
		digest.update(s.getBytes());
		return toHex(digest.digest());
	}
	/**
	 * 将加密后的内容输出.
	 * @param abyte0 是经过MessageDigest处理后得到的Byte类型的值.
	 * @return 转换后的字符串。
	 */
	public static final String toHex(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);
		for (int i = 0; i < abyte0.length; i++) {
			if ((abyte0[i] & 0xff) < 16)
				stringbuffer.append("0");
			stringbuffer.append(Long.toString(abyte0[i] & 0xff, 16));
		}
		return stringbuffer.toString();
	}	
	/**
	 * 判断一个字符串是否在数组内
	 * @param array 字符串数组
	 * @param str 需要查找的字符串
	 * @return boolean
	 */
	public static final boolean inArray(String[] array, String str) {
		boolean inArray = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(str)) {
				inArray = true;
				break;
			}
		}
		return inArray;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2002-3-7 16:04:45)
	 * @return java.lang.String
	 * @param i int
	 * @param length int
	 */
	public static final String intToStr(int i, int length) {
		String str = "";
		String tempStr = (new Integer(i).toString());
		for (int j = 0; j <= length; j++) {
			tempStr = "0" + tempStr;
		}
		str = tempStr.substring(tempStr.length() - length, tempStr.length());
		return str;
	}
	/**
	 * 检查给定字符串是否为有效的邮件格式
	 * @param Email 邮件地址字符串
	 * @return boolean
	 */
	public static final boolean isValidMail(String Email) {
		boolean checked_status = true, at_let = false, point_let = false;
		int at_ps = -1, str_length = Email.length() - 1;
		Email = Email.toUpperCase();
		for (int i = 0; i <= str_length; i++) {
			char ch = Email.charAt(i);
			if ((ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9')) {
				switch (ch) {
					case '@' :
						{
							if (i == 0 || i == str_length || at_let) {
								checked_status = false;
							}
							at_let = true;
							at_ps = i;
							break;
						}
					case '.' :
						{
							if (i == 0
								|| i == str_length
								|| i == at_ps + 1
								|| i == at_ps - 1) {
								checked_status = false;
							}
							point_let = true;
							break;
						}
					case '_' :
					case '-' :
						{
							if (i == 0 || i == str_length) {
								checked_status = false;
							}
							break;
						}
					default :
						checked_status = false;
						break;
				}
			}

			if (!checked_status) {
				break;
			}
		}
		checked_status = checked_status & point_let & at_let;
		return checked_status;
	}
	/**
	 * 返回一个随机字符串。
	 * @param i 需要得到的随机字符串的长度。
	 * @return 指定长度的随机字符串。
	 */
	public static final String randomString(int i) {
		if (i < 1)
			return null;
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters =
				"0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
					.toCharArray();
		}
		try {
			char ac[] = new char[i];
			for (int j = 0; j < ac.length; j++)
				ac[j] = numbersAndLetters[randGen.nextInt(71)];
			return new String(ac);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 替换字符串中的字符。
	 * @param s 指定需要处理的字符串。
	 * @param s1 指定需要替换的字符串。
	 * @param s2 指定需要替换的字符串替换后的结果。
	 * @return 经过替换处理后的字符串。
	 */
	public static final String replace(String s, String s1, String s2) {
		String returnStr = s;
		int i = 0;
		if ((i = returnStr.indexOf(s1)) >= 0) {
			char ac[] = returnStr.toCharArray();
			char ac1[] = s2.toCharArray();
			int j = s1.length();
			StringBuffer stringbuffer = new StringBuffer(ac.length);
			stringbuffer.append(ac, 0, i).append(ac1);
			i += j;
			int k;
			for (k = i;(i = s.indexOf(s1, i)) > 0; k = i) {
				stringbuffer.append(ac, k, i - k).append(ac1);
				i += j;
			}
			stringbuffer.append(ac, k, ac.length - k);
			returnStr = stringbuffer.toString();
		}
		return returnStr;
	}
	/**
	 * 把星期转换为中文。
	 * 创建日期：(2002-3-18 9:52:20)
	 * @return java.lang.String
	 * @param week int
	 */
	public static final String weekToCN(int week) {
		String weekStr = "";
		switch (week) {
			case 0 :
				weekStr = "日";
				break;
			case 1 :
				weekStr = "一";
				break;
			case 2 :
				weekStr = "二";
				break;
			case 3 :
				weekStr = "三";
				break;
			case 4 :
				weekStr = "四";
				break;
			case 5 :
				weekStr = "五";
				break;
			case 6 :
				weekStr = "六";
				break;
			default :
				weekStr = "数字超出范围！";
		}
		return weekStr;
	}
	/**
	 * 将任意类型的对象转换为字符串
	 * @param object Object对象
	 * @return 字符串
	 */
	public static String toString(Object object) {
		String returnStr = "";
		if (object != null) {
			returnStr = object.toString();
		}
		return returnStr;
	}
	/**
	 * 将整数转换为字符串
	 * @param data 整数
	 * @return 字符串
	 */
	public static String toString(int data) {
		String returnStr = "";
		if (data != 0) {
			returnStr = String.valueOf(data);
		}
		return returnStr;
	}
	/**
	 * 将float类型转换为字符串
	 * @param data float类型数据
	 * @return 字符串
	 */
	public static String toString(float data) {
		String returnStr = "";
		if (data != 0) {
			returnStr = String.valueOf(data);
		}
		return returnStr;
	}
	/**
	 * 将double类型转换为字符串
	 * @param data double
	 * @return 字符串
	 */
	public static String toString(double data) {
		String returnStr = "";
		if (data != 0) {
			returnStr = String.valueOf(data);
		}
		return returnStr;
	}
	/**
	 * 
	 * @param data
	 * @param fractionDigits
	 * @return
	 */
	public static String toString(float data, int fractionDigits) {
		String returnStr = "";
		if (data != 0) {
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setMinimumFractionDigits(fractionDigits);
			nf.setMaximumFractionDigits(fractionDigits);
			nf.setGroupingUsed(false);
			returnStr = nf.format(data);
		}
		return returnStr;
	}
	public static String toString(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		return toString(date, fmt);
	}
	public static String toString(Date date, java.text.DateFormat fmt) {
		String returnStr = "";
		if (date != null) {
			returnStr = fmt.format(date);
		}
		return returnStr;
	}
	/**
	 * 中文转换成英文
	 * @param enStr 英文字符串
	 * @return 
	 */
	public static String cnToEn(String enStr) {
		String cnStr = enStr;
		try {
			cnStr = new String(enStr.getBytes("GB2312"), "ISO8859-1");
		} catch (Exception e) {

		}
		return cnStr;
	}
	/**
	 * 将英文转换成中文
	 * @param enStr 英文字符串
	 * @return String
	 */
	public static String enToCn(String enStr) {
		String cnStr = enStr;
		try {
			cnStr = new String(enStr.getBytes("ISO8859-1"), "GB2312");
		} catch (Exception e) {
			
		}
		return cnStr;
	}
	/**
	 * 将字符串转换为时间格式
	 * @param s 字符串
	 * @param format ==1,表示时间格式为：yyyy-mm-dd；==0，表示时间格式为：yyyy-mm-dd hh:mm:ss
	 * @return 数据库中需要的数据类型Timestamp
	 */
	public static java.sql.Timestamp toTimestamp(String s, int format) {
		if (format == 1 && null != s &&!"".equals(s)) {
			s = s + " 00:00:00";
		}
		return toTimestamp(s);
	}
	/**
	 * 将字串转换为时间格式
	 * @param s 时间格式
	 * @return 数据库中需要的数据类型Timestamp
	 */
	public static java.sql.Timestamp toTimestamp(String s) {
		java.sql.Timestamp rtn = null;
		java.util.Date d = toDate(s);
		if (d != null) {
			rtn = new java.sql.Timestamp(d.getTime());
		}
		return rtn;
	}
	/**
	 * 将字符串转换为Java时间类型
	 * @param s 字符串
	 * @return 格式为yyyy-mm-dd类型的时间格式
	 */
	public static java.util.Date toDate(String s) {
		java.util.Date rtn = null;
		if (s != null && !"".equals(s.trim())) {
			SimpleDateFormat dtformat =
				new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			try {
				rtn = dtformat.parse(s);
			} catch (Exception e) {
				rtn = null;
				e.printStackTrace();
			}
		}
		return rtn;
	}
	/**
	 * 将字符串转换为整数
	 * @param s 字符串
	 * @return 整形数据
	 */
	public static int toInt(String s) {
		int rtn = 0;
		if (s != null && !s.equals("")) {
			try {
				rtn = Integer.parseInt(s);
			} catch (Exception e) {
				rtn = 0;
			}
		}
		return rtn;
	}
	/**
	 * 将字符串转换为float类型数据
	 * @param s 字符串
	 * @return float
	 */
	public static float toFloat(String s) {
		float rtn = 0;
		if (s != null && !s.equals("")) {
			try {
				rtn = Float.parseFloat(s);
			} catch (Exception e) {
				rtn = 0;
			}
		}
		return rtn;
	}
	/**
	 * 将字符串转换为double类型数据
	 * @param s 字符串
	 * @return double
	 */
	public static double toDouble(String s) {
		double rtn = 0;
		if (s != null && !s.equals("")) {
			try {
				rtn = Double.parseDouble(s);
			} catch (Exception e) {
				rtn = 0;
			}
		}
		return rtn;
	}
}
