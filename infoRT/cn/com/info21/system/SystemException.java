package cn.com.info21.system;

/**
 * @author db2admin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

public class SystemException extends Exception {
	public SystemException() {
		super();
	}
	public SystemException(Exception e) {
		super(e.toString());
	}
	public SystemException(String s) {
		super(s);
	}
	/**
		 * ���캯��3
		 * @param obj ���з����������
		 * @param s ���д�������
		 */
		public SystemException(Object obj, String s) {
			//����Ϣд�뵽��־�ļ���
			//Logger logger = Logger.getLogger(obj.getClass());
			//logger.info(s);
		}
		/**
		 * ���캯��4
		 * @param className ����
		 * @param s ����
		 */
		public SystemException(String className, String s) {
			super(s);
			//Log.info(s);
		}
}
