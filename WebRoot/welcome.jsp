<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.shasha.sys.*"%>
<div id="start-div">
    <div style="float:left;" ><img src="res/img/welcome.gif" /></div>
	<div style="margin-left:100px;">
		<h2>欢迎使用本系统!</h2>
        <p>任务调度</p>
    </div>
    <div style="margin-left:100px;width:300px;">
        <h2>系统通知</h2>
        <p>
			<ul class="modules-list list-decoration-dot">
				<%	try{
				ArticleIterator aiter = ArticleHome.findByCondition("notice=1 order by createdate desc",0,6);
				while(aiter.hasNext())
				{
					Article article = (Article)aiter.next();
				%>
				         <li>·&nbsp;<a onclick="document.all?event.cancelBubble=true:event.stopPropagation()" href="javascript:shownoticewin(<%=article.getId()%>);"><%=article.getTitle()%></a></li>
				<%
				}
				}catch(Exception e){
				        	
				  }
       			 %>			
         	</ul>              
        </p>
    </div>            
</div> 