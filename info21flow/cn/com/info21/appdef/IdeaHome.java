/*
 * 创建日期 2005-8-1
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
	 * 默认构造函数
	 */
	public IdeaHome() {
	}
	/**
	 * 创建方法
	 * @param ideaname 方法名称
	 * @return 方法
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
	 * 根据ID查找方法
	 * @param id 方法ID
	 * @return 方法
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
	 * 构造函数
	 * @param sql 查询条件
	 * @return IdeaIterator
	 * @throws SystemException 扑获错误
	 */
	public static IdeaIterator findBySQL(String sql)
	throws SystemException {
		return IdeaIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return IdeaIterator
	 * @throws SystemException 捕获错误
	 */
	public static IdeaIterator findByCondition(String condition)
	throws SystemException {
		return new IdeaIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return IdeaIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static IdeaIterator findByCondition(String condition ,
			int start , int num) {
		return new IdeaIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Idea.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
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
