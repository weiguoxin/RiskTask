/*
 * Created on 2004-12-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.com.info21.util;
import cn.com.info21.util.regexp.RE;
/**
 * @author realfox
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArrayUtils {

	/**
	 * 
	 */
	public ArrayUtils() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param str 待分割的string
	 * @param x 分隔符
	 * @return int数组
	 */
	public static int[] getIntArray(String str, String x) {
		int[] rtn = new int[0];
		RE re = null;
		if (str != null && x != null) {
			if (!str.equals("") && !x.equals("")) {
				//String[] tmp = str.split(x);
				re = new RE(x);
				String[] tmp = re.split(str);
				if (tmp != null && tmp.length > 0) {
					rtn = new int[tmp.length];
					for (int i = 0; i < tmp.length; i++) {
						rtn[i] = Integer.parseInt(tmp[i]);
					}
				}
			}
		}
		return rtn;
	}

	/**
	 * @param str 待分割的string
	 * @param x1 分隔符1
	 * @param x2 分隔符2
	 * @return int[][]数组
	 */
	public static int[][] getIntArray(String str, String x1, String x2) {
		int[][] rtn = new int[0][0];
		int size1 = 0;
		int size2 = 0;
		RE re = null;
		if (str != null && x1 != null && x2 != null) {
			if (!str.equals("") && !x1.equals("") && !x2.equals("")) {
				//String[] tmp = str.split(x1);
				re = new RE(x1);
				String[] tmp = re.split(str);
				if (tmp != null && tmp.length > 0) {
					size1 = tmp.length;
					//String[] tmp2 = tmp[0].split(x2);
					re = new RE(x2);
					String[] tmp2 = re.split(tmp[0]);
					if (tmp2 != null && tmp2.length > 0) {
						size2 = tmp2.length;
						rtn = new int [size1][size2];
						for (int i = 0; i < size1; i++) {
							//tmp2 = tmp[i].split(x2);
							re = new RE(x2);
							tmp2 = re.split(tmp[i]);
							for (int j = 0; j < size2; j++) {
								rtn[i][j] = Integer.parseInt(tmp2[j]);
							}
						}
					}
				}
			}
		}
		return rtn;
	}

	public static String array2String(Object[] objs, String sep) {
		String rtn = null;
		if (objs != null) {
			rtn = "";
			for (int i = 0; i < objs.length; i++) {
				if (!rtn.equals("")) {
					rtn += sep;
				}
				rtn += objs[i].toString();
			}
		}
		return rtn;
	}
}
