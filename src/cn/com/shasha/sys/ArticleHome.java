package cn.com.shasha.sys;

import cn.com.info21.system.*;

public class ArticleHome {
	/**
	* �����ĵ����⣬����һ��Article����
	* �������ڣ�(2003-6-8 12:21:00)
	* @param id int �ĵ���š�
	* @param title String ���⡣
	* @return Article �ĵ�����
	* @throws SystemException��
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
		* ���ݲ�ѯ���� �����һ��Article�����б�
		* �������ڣ�(2003-6-7 23:30:00)
		* @param condition String ��ѯ������
		* @return ArticleIterator��
		* @throws SystemException��
		*/
	public static ArticleIterator findByCondition(String conditionStr)
		throws SystemException {
		ArticleIterator articleIterator = null;
		articleIterator = new ArticleIterator(conditionStr);
		return articleIterator;
	}
	/**
		* ���ݲ�ѯ���� ����startIndex��ʼ�����num��Article������б�
		* �������ڣ�(2003-6-7 23:39:12)
		* @param condition String ��ѯ������
		* @param startIndex int ��ʼλ�á�
		* @param num int Ҫ�󷵻ض��������
		* @return ArticleIterator ����
		* @throws SystemException��
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
	* �����ĵ���� �����һ��Article����
	* �������ڣ�(2003-6-7 23:39:20)
	* @param id int �ĵ���š�
	* @return Article ����
	* @throws SystemException��
	*/
	public static Article findById(int id) throws SystemException {
		Article article = new Article(id);
		return article;
	}

	/**
	* ���ݲ�ѯ���� ����÷���������Article����ĸ�����
	* �������ڣ�(2003-6-7 23:40:08)
	* @param condition String ��ѯ������
	* @return int �û����������
	* @throws SystemException��
	*/
	public static int getArticleCount(String conditionStr)
		throws SystemException {
		return SysFunction.getCnt("SYSNOTICE", conditionStr);
	}
	/**
	* �����ĵ����
	* �������ڣ�(2003-6-7 23:44:12)
	* @param id int �ĵ���š�
	* @return Article void��
	* @throws SystemException��
	*/
	public static void remove(int id) throws SystemException {
		ArticleHome.findById(id).remove();
	}
}