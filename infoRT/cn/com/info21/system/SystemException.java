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
		 * 构造函数3
		 * @param obj 运行发生错误对象
		 * @param s 运行错误描述
		 */
		public SystemException(Object obj, String s) {
			//将信息写入到日志文件中
			//Logger logger = Logger.getLogger(obj.getClass());
			//logger.info(s);
		}
		/**
		 * 构造函数4
		 * @param className 类名
		 * @param s 错误
		 */
		public SystemException(String className, String s) {
			super(s);
			//Log.info(s);
		}
}
