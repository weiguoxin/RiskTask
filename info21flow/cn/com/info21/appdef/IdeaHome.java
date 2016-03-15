/*
 * �������� 2005-8-1
 *
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class IdeaHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public IdeaHome() {
	}
	/**
	 * ��������
	 * @param ideaname ��������
	 * @return ����
	 */
	public static Idea create(String ideaname) {
		Idea idea = null;
		try {
			idea = new Idea(ideaname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idea;
	}
	/**
	 * ����ID���ҷ���
	 * @param id ����ID
	 * @return ����
	 */
	public static Idea findById(int id) {
		Idea idea = null;
		try {
			idea = new Idea(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idea;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return IdeaIterator
	 * @throws SystemException �˻����
	 */
	public static IdeaIterator findBySQL(String sql)
	throws SystemException {
		return IdeaIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return IdeaIterator
	 * @throws SystemException �������
	 */
	public static IdeaIterator findByCondition(String condition)
	throws SystemException {
		return new IdeaIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return IdeaIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static IdeaIterator findByCondition(String condition ,
			int start , int num) {
		return new IdeaIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Idea.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getIdeaCount(String conditionStr) {
		return SysFunction.getCnt(Idea.TABLENAME, conditionStr);
	}
	public static void removeByXId(int xid) {
	    try {
	        IAMappingIterator ii = IAMappingHome.findByCondition(" ideaid = " + xid);
	        IAMapping iam = null;
	        while (ii.hasNext()) {
	            iam = (IAMapping) ii.next();
	            IAMappingHome.remove(iam.getId());
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
