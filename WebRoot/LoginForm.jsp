<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*, cn.com.info21.util.*, cn.com.info21.system.*,cn.com.info21.org.*,cn.com.shasha.sys.*"%>
<%@include file="public.jsp"%>
<%
boolean errors = false;
String username = ParameterUtils.getParameter(request,"username");
String password = ParameterUtils.getParameter(request,"password");
boolean autoLogin = ParameterUtils.getBooleanParameter(request,"autoLogin");
boolean doLogin = request.getParameter("login") != null;
boolean doContinue = request.getParameter("continue") != null;
String referringPage = (String)session.getAttribute("refererURL");
// The user auth token:
AuthToken authToken = null;
// Check the request/response for a login token:
boolean canTryAutoLogin = false;
try {
	authToken = AuthFactory.getAuthToken(request, response);
        canTryAutoLogin = true;
} catch (UnauthorizedException ue) {}
if (ParameterUtils.getBooleanParameter(request,"login")) {
	try {
            	authToken = AuthFactory.setAuthToken(request, response, username, password, autoLogin);
	} catch (UnauthorizedException ue) {
            	errors = true;
	}
}
String errorMessage = "登录失败: 确认用户名和密码是否正确.";
if (!errors && (doLogin || doContinue)) {
	WLUser wluser = WLUserHome.findById((int)authToken.getUserID(),session,authToken);
	referringPage = request.getContextPath() + "/index.jsp";
	response.sendRedirect(referringPage);	
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>忻州税务局 - 综合事务管理系统</title>
<link rel="shortcut icon" href="">
<link rel="stylesheet" href="./res/css/style_login.css" type="text/css">
<script type="text/javascript">
<!--
	function $(sId) {
		return document.getElementById(sId);
	}
	function fCheck() {
		var oTxtUser = $("txtUser");
		oTxtUser.value = fTrim(oTxtUser.value);
		if( oTxtUser.value =="") {
			$("divError").innerHTML = "请输入你的用户名";
			oTxtUser.focus();
			return false;
		}

		if( $("txtPassword").value.length =="") {
			$("divError").innerHTML = "请输入你的密码";
			$("txtPassword").focus();
			return false;
		}
		fSaveLoginInfo();
		loginForm.submit();
	}
	function fTrim(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	function fSaveLoginInfo() {
		if($("rmbUser").checked){
			fSetCookie("login_user", $("txtUser").value,true);			
		}else
		{
			fSetCookie("login_user", "",true);	
		}
	}
	function fGetLoginInfo(){
		var sCookie = fGetCookie("login_user");
		if(sCookie)
		{	
			$("txtUser").value	= sCookie;
		}
		$("txtPassword").value = "";
	}
	function fGetCookie(sName) {
	   var search = sName + "="
	   if(document.cookie.length > 0) {
		  offset = document.cookie.indexOf(search)
		  if(offset != -1) {
			 offset += search.length
			 end = document.cookie.indexOf(";", offset)
			 if(end == -1) end = document.cookie.length
			 return unescape(document.cookie.substring(offset, end))
		  }
		  else return ""
	   }
	}
	function fSetCookie(sName, sValue, isForever) {
		document.cookie = sName + "=" + escape(sValue) + (isForever?";expires="+  (new Date(2099,12,31)).toGMTString():"");
	}
	window.onload = function(){
		fGetLoginInfo();
	};
	// 遮罩与弹框
	function fShowPhoneReg(bType,tid){
		var sUrl = "shownotice.jsp",
		oDiv = $("phoneRegDiv"),
		oIframe = $("phoneRegFrame"),
		oMask = $("mask");
		if(bType){
			oIframe.src = sUrl+"?id="+tid;
		}
		oMask.style.display = oDiv.style.display = (bType ? "" : "none");
	}	
	function onFocus(){
		var user = $('txtUser');
		if(user.value == null || user.value ==''){user.focus();}else{
			$('txtPassword').focus();
		}
	}
//-->
</script>
<!-- 163 free mail index html -->
</head>
<body class="fmi_bdWp" onload='onFocus()'>
<div class="fmi_mWpIn">
  <!-- 头部 Begin -->
  <div class="fmi_HeadWp">
    <h1 class="Logo"><img src="res/img/weboa_logo.gif" alt="忻州市地方税务局综合事务管理系统" style="width: 428px; height: 75px;"></h1>
    <div class="Extra"><a href="#" onclick="window.external.AddFavorite('http://localhost','忻州市地方税务局综合事务管理系统');return false;">加入收藏</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="res/caozuoshouce.doc" title="操作手册">帮助</a></div>
  </div>
  <!-- 头部 End -->
  <!-- 主体 Begin -->
  <div class="fmi_MidWp">
    <div class="I_Style I_Style_1 LoginOn">
      <form method="post" name="loginForm" action="" id="loginForm" onSubmit="return fCheck()">
        <!-- 登录框 Begin -->
       <input type="hidden" name="login" value="true">
        <div class="fmi_LgBxCon">
          <h3 class="ImgJ2 LgBx_Name">登录系统</h3>
          <table class="LgBxLst_tb" border="0" cellpadding="0" cellspacing="0">
            <tbody>
              <tr id="uName">
                <th nowrap="nowrap">用户名</th>
                <td width="200"><input name="username" id="txtUser" class="Ipt" style="width: 190px; ime-mode: disabled;" onFocus="this.className='Ipt IptOnF'" onBlur="this.className='Ipt'" maxlength="50" tabindex="1" type="text"></td>
              </tr>
              <tr>
                <th></th>
                <td style="height:6px;"></td>
              </tr>
              <tr>
                <th nowrap="nowrap">密　码</th>
                <td><input name="password" id="txtPassword" class="Ipt" style="width: 190px;" onFocus="this.className='Ipt IptOnF'" onBlur="this.className='Ipt'" tabindex="2" type="password"></td>
              </tr>
              <tr>
                <th>&nbsp;</th>
                <td class="Chk_Wp" nowrap="nowrap">
<input name="rmbUser" id="rmbUser" class="Chk_RName" checked="checked" tabindex="6" type="checkbox">
                  <label for="rmbUser">记住用户名</label></td>
              </tr>
              <tr>
                <th>&nbsp;</th>
                <td  class="Smb_Wp"><input value="登 录" class="Btn SmbBtn" onMouseOver="this.className='Btn SmbBtn BtnHv'" onMouseOut="this.className='Btn SmbBtn'" onMouseDown="this.className='Btn SmbBtn BtnDw'" tabindex="9" type="submit"></td>
              </tr>
            </tbody>
          </table>
          <div class="Error" id="divError">
					<%	if( errors ) { %>
					登录失败: 确认用户名和密码是否正确.
					<%	} %>	          
          </div>

          <div class="notice">
            <marquee direction="up" height="45" scrollamount="1" >
            <ul>
			<%	try{
				ArticleIterator aiter = ArticleHome.findByCondition("notice=1 order by createdate desc",0,4);
				while(aiter.hasNext())
				{
					Article article = (Article)aiter.next();
			%>
              <li><a href="javascript:fShowPhoneReg(true,<%=article.getId()%>);"><%=article.getTitle()%></a></li>
			<%
				}
            }catch(Exception e){
		    }
            %>
            </ul>
            </marquee>
          </div>
        </div>
      </form>
    </div>
  </div>  
  <!-- 主体 End -->
  <!-- 底部版权信息 Begin -->
  <div class="fmi_FootWp"> 建议将本网站设为信任站点，为获得更好的视觉效果，请将分辨率设置为1024*768 </div>
  <!-- 底部版权信息 End -->
</div>
<!--遮罩-->
<div id="mask" class="mask" style="display:none;"></div>
<!--弹框-->
<div id="phoneRegDiv" class="dialogbox" style="width:487px;display:none;left: 397px; top: 224px;">
	<div class="hd">
		<a href="javascript:;" class="btn btn-close" onClick="fShowPhoneReg(false);" title="关闭">关闭</a>

		<span class="rc rc-l"></span>
		<span class="rc rc-r"></span>
	</div>
	<div class="bd">
		<iframe id="phoneRegFrame" src="" frameborder="0" style="width:485px;height:330px;overflow:auto;"></iframe>
	</div>
</div> 
<!--遮罩end-->
</body>
</html>