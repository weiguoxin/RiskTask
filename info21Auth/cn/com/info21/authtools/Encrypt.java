/*
 * 创建日期 2005-11-8
 */

package cn.com.info21.authtools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liukehua
 */

public class Encrypt {
	private static MessageDigest digest = null;
	private static final char zeroArray[] =
		"0000000000000000000000000000000000000000000000000000000000000000"
			.toCharArray();
	public Encrypt () {
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
	
	public static final String dateToMillis(java.util.Date date) {
		return zeroPadString(Long.toString(date.getTime()), 15);
	}
	public static final String zeroPadString(String string, int length) {
		if (string == null || string.length() > length) {
			return string;
		} else {
			StringBuffer buf = new StringBuffer(length);
			buf.append(zeroArray, 0, length - string.length()).append(string);
			return buf.toString();
		}
	}
}
