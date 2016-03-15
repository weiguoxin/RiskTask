package cn.com.shasha.sys;

import cn.com.info21.system.*;

public class ArticleHome {
	/**
	* 给出文档标题，创建一个Article对象。
	* 创建日期：(2003-6-8 12:21:00)
	* @param id int 文档编号。
	* @param title String 标题。
	* @return Article 文档对象。
	* @throws SystemException。
	*/
	public static Article create(String title) throws SystemException {
		Article article = null;
		if ((title == null) || ("".equals(title))) {
			throw new SystemException("null or empty parameter 'title'");
		} else {
			article = new Article(title);
		}
		return article;
	}
	/**
		* 根据查询条件 ，获得一个Article对象列表。
		* 创建日期：(2003-6-7 23:30:00)
		* @param condition String 查询条件。
		* @return ArticleIterator。
		* @throws SystemException。
		*/
	public static ArticleIterator findByCondition(String conditionStr)
		throws SystemException {
		ArticleIterator articleIterator = null;
		articleIterator = new ArticleIterator(conditionStr);
		return articleIterator;
	}
	/**
		* 根据查询条件 ，从startIndex开始，获得num个Article对象的列表。
		* 创建日期：(2003-6-7 23:39:12)
		* @param condition String 查询条件。
		* @param startIndex int 开始位置。
		* @param num int 要求返回对象个数。
		* @return ArticleIterator 对象。
		* @throws SystemException。
		*/
	public static ArticleIterator findByCondition(
		String conditionStr,
		int startIndex,
		int num)
		throws SystemException {
		ArticleIterator articleIterator = null;
		articleIterator = new ArticleIterator(conditionStr, startIndex, num);
		return articleIterator;
	}
	/**
	* 根据文档编号 ，获得一个Article对象。
	* 创建日期：(2003-6-7 23:39:20)
	* @param id int 文档编号。
	* @return Article 对象。
	* @throws SystemException。
	*/
	public static Article findById(int id) throws SystemException {
		Article article = new Article(id);
		return article;
	}

	/**
	* 根据查询条件 ，获得符合条件的Article对象的个数。
	* 创建日期：(2003-6-7 23:40:08)
	* @param condition String 查询条件。
	* @return int 用户对象个数。
	* @throws SystemException。
	*/
	public static int getArticleCount(String conditionStr)
		throws SystemException {
		return SysFunction.getCnt("SYSNOTICE", conditionStr);
	}
	/**
	* 给出文档编号
	* 创建日期：(2003-6-7 23:44:12)
	* @param id int 文档编号。
	* @return Article void。
	* @throws SystemException。
	*/
	public static void remove(int id) throws SystemException {
		ArticleHome.findById(id).remove();
	}
}