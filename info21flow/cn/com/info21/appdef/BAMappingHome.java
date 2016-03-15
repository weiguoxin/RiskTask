/*
 * �������� 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */
public class BAMappingHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public BAMappingHome () {
	}
	/**
	 * ����������֮���ӳ��
	 * @param appname Ӧ������
	 * @return Ӧ��
	 */
	public static BAMapping create(int buttonid, int actid) {
	    BAMapping bam = null;
		try {
			bam = new BAMapping(buttonid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bam;
	}
	/**
	 * ����ID����Ӧ��
	 * @param id Ӧ��ID
	 * @return Ӧ��
	 */
	public static BAMapping findById(int id) {
	    BAMapping bam = null;
		try {
			bam = new BAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bam;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �˻����
	 */
	public static BAMappingIterator findBySQL(String sql)
	throws SystemException {
		return BAMappingIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �������
	 */
	public static BAMappingIterator findByCondition(String condition) throws SystemException {
		return new BAMappingIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return AppIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static BAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new BAMappingIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(BAMapping.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getBAMappingCount(String conditionStr) {
		return SysFunction.getCnt(BAMapping.TABLENAME, conditionStr);
	}
	public static void removeByXId(int xid) {
	    try {
	        BAMappingIterator bi = BAMappingHome.findByCondition(" buttonid = " + xid);
	        BAMapping bam = null;
	        while (bi.hasNext()) {
	            bam = (BAMapping) bi.next();
	            BAMappingHome.remove(bam.getId());
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
