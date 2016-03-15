/*
 * �������� 2005-11-8
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
	 * MD5���ܺ�����JDK�Դ�����
	 * @since JDK1.2����
	 * @param s ����Ҫ���ܵ��ַ�����
	 * @return ���ܺ���ַ�����
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
	 * �����ܺ���������.
	 * @param abyte0 �Ǿ���MessageDigest�����õ���Byte���͵�ֵ.
	 * @return ת������ַ�����
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
	 * ����ת����Ӣ��
	 * @param enStr Ӣ���ַ���
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
	 * ��Ӣ��ת��������
	 * @param enStr Ӣ���ַ���
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
