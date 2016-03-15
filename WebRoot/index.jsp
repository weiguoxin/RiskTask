<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="cn.com.shasha.sys.*"%>
<%@ page import="com.wgx.listeners.SessionListener"%>
<%
int currentNum = SessionListener.getCurrentNum();
Object o = session.getAttribute("Session_User");
//System.out.println(o);
if(null == o){response.sendRedirect("LoginForm.jsp");}
WLUser nowuser  = (WLUser)o;
String loginname = null!=nowuser.getUsername()? nowuser.getUsername():"";
String user_swjg_mc = nowuser.getSwjg_mc();
String userDM = "";
try {
	userDM = new WLUser().getUserDM();
}catch(Exception e){
	e.printStackTrace();
}

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
    <title>综合事务管理系统  - <%=loginname %></title>
    <link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="extjs/resources/docs.css" />
	<link rel="stylesheet" type="text/css" href="extjs/resources/style.css" /> 
	<link rel="stylesheet" type="text/css" href="res/css/fileuploadfield.css"/>
    <link rel="stylesheet" type="text/css" href="extjs/ux/css/Ext.ux.tree.CheckTreePanel.css">
	<link rel="stylesheet" href="extjs/ux/css/RowEditor.css" type="text/css"/>
	<link rel="stylesheet" href="extjs/ux/Ext.ux.form.LovCombo.css" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="extjs/resources/edittreegrid.css" />
	<link rel="icon" href="extjs/resources/favicon.ico" />
	<link rel="shortcut icon" href="extjs/resources/favicon.ico" />
	<style type="text/css">
	.cssLabel
	{
		color:red;
		font-size:20px;
		font-weight: bold;
	}
	.gTop {
	 	height:100px;
		background: url('res/img/top_b_bg.gif') left bottom repeat-x;
		position:relative;
		background-color:#FFF;
		font-family:Verdana,Arial,Helvetica,sans-serif,"宋体";
		font-size:12px;	
	}
	#header{
	 	height:100px!important;
		height:100px;
	}
	body, div{
		margin:0;
		padding:0;
		}

	.gSrh {
		position:absolute;
		top:-26px;
		right:0px;
		width:334px;
		height:121px;
		background:url('res/img/top_r_bg.gif') 30px 0 no-repeat;
		}
	.fLe, .fLeft {
		float:left
	}
	.gUmsg {
		position:absolute;
		left:520px;
		top:30px;
		z-index:99;
	}
	.gUmsg a,.gUmsg a:visited{
	color:#2A7AA3;
	text-decoration:underline;
	}
	.gLogo {
		position:absolute;
		left:5px;
		top:5px;
		z-index:3;
	}
	#ruku_grid input:focus
	{
		font-weight: bold;
		font-size: 16px;
	}
	.error_rw td{
			font-weight:bold;
			font-size:14px;
			color:red;
			background-color:#beed98;
		}
	.silk-grid { background-image: url(extjs/resources/grid.png) !important; background-repeat: no-repeat; }
	.author-m {
	    background-image: url(res/extimg/user.gif) !important;
	}
	.x-action-col-cell img.revoke-col {
	    height: 16px;
	    width: 16px;
	    background-image: url(res/extimg/revoke.gif);
	    cursor:hand;
	}
	.x-action-col-cell img.patch-col {
	    height: 16px;
	    width: 16px;
	    background-image: url(res/extimg/database_edit.png);
	    cursor:hand;
	}
	#notice ul,#notice li{display:inline;padding-right:20px;}
	/*--main.jsp--*/  
	#start-div{
		width:70%;
		margin: 20 10 10 10;
	}
	#start-div div, #start-div img, #start-div ul, #start-div li, #start-div p {
	    border: 0 none;
	    margin: 0;
	    padding: 0;
	}
	#start-div h2 {
	    font-size: 14px;
	    color: #555;
	    padding-bottom:5px;
	    border-bottom:1px solid #C3D0DF;
	}
	#start-div p {
	    margin: 10px 0;
		font-size:12px;
		line-height:18px;
	}
	.modules-list {
	    clear: both;
	    margin: 2px 0 0;
	}
	#start-div ul, #start-div li {
	    list-style: none outside none;
	}
	.modules-list li {
	    clear: both;
	    height: 22px;
	    line-height: 22px;
	    overflow: hidden;
		font-size:12px;
	}
	#start-div a:link {
	    color: #000000;
	    text-decoration: none;
	}
	#start-div a:hover {
	    color: #BA2636;
	    text-decoration: underline;
	}
	.silk-grid { background-image: url('extjs/resources/grid.png') !important; background-repeat: no-repeat; }
	
	.btn-panel td {
    	padding-left:5px;
	}	
	.file-up {
	    background-image: url('extjs/resources/button/tool_up.gif') !important;
	}
	.file-py {
	    background-image: url('extjs/resources/button/tool_edit.gif') !important;
	    background-repeat: no-repeat; 
	}
	.file-ok {
	    background-image: url('extjs/resources/button/already_incept.gif') !important ;
	    background-repeat: no-repeat; 
	}
	.file-no {
	    background-image: url('extjs/resources/button/win_exit.gif') !important;
	    background-repeat: no-repeat; 
	}
	.file-turn {
	    background-image: url('extjs/resources/button/tool_clean.gif') !important;
	    background-repeat: no-repeat; 
	}	
	.file-fj {
	    background-image: url('extjs/resources/button/user_suit.png') !important;
	    background-repeat: no-repeat; 
	}
	.file-jz {
	    background-image: url('extjs/resources/button/user_gray.png') !important;
	    background-repeat: no-repeat; 
	}
    .flag-red {
        background-image: url('extjs/resources/button/flag_red.gif') !important;
        background-repeat: no-repeat;
    }
    .hourglass{
        background-image: url('extjs/resources/button/hourglass.png') !important;
        background-repeat: no-repeat;
    }
    .flag-green {
        background-image: url('extjs/resources/button/flag_green.gif') !important;
        background-repeat: no-repeat;
    }
    .x-action-col-cell img.file_show{
         height: 16px;
         width: 16px;
         background-image: url('extjs/resources/button/tool_copy.gif');
         margin-right:10px;
         cursor:pointer !important;
         cursor:hand;
     }
.checked{background-image:url(extjs/resources/images/default/menu/checked.gif)}  
.unchecked{background-image:url(extjs/resources/images/default/menu/unchecked.gif)} 

.x-grid-record-red {
	background: #ff5858;
}

.x-grid-record-yellow {
	background: #fffd37;
}
				
	</style>
	<!-- GC -->
    <script type="text/javascript" src="extjs/extjs/ext-base.js"></script>
    <script type="text/javascript" src="extjs/extjs/ext-all.js"></script>
    <script type="text/javascript" src="extjs/ux/TabCloseMenu.js"></script>
	<script type="text/javascript" src="extjs/extjs/ext-lang-zh_CN.js"  charset="UTF-8"></script>    
    <script type="text/javascript" src="extjs/selfjs/indexpage.js"></script>
    
    <script type="text/javascript" src="extjs/selfjs/FileUploadField.js"></script>
    <script type="text/javascript" src="extjs/selfjs/UploadExcelWindow.js"></script>

    <script type="text/javascript" src="extjs/ux/Ext.ux.tree.CheckTreePanel.js"></script>

    <script type="text/javascript" charset="GBK" src="extjs/selfjs/public.js"></script>
    <script type="text/javascript" src="extjs/selfjs/ComBoxTree.js"></script>
    <script type="text/javascript" src="extjs/ux/RowEditor.js"></script>
	<script type="text/javascript" src="extjs/ux/Ext.ux.util.js"></script>
	<script type="text/javascript" src="extjs/ux/Ext.ux.form.LovCombo.js"></script>
    <!--
    <script type="text/javascript" src="extjs/selfjs/fileForm.js"></script>
    <script type="text/javascript" src="extjs/selfjs/UpLoadFileWindow.js"></script>
    <script type="text/javascript" src="extjs/selfjs/Gardens.js"></script>
    <script type="text/javascript" src="extjs/ux/Ext.ux.form.XCheckbox.js"></script>
    <script type="text/javascript" src="extjs/selfjs/fileGrid.js"></script>
    -->
	<script  type="text/javascript" >
	Ext.QuickTips.init();  
    /*backspace防止返回*/
    Ext.getDoc().on('keydown',function(e){
        if(e.getKey() == 8 && e.getTarget().type =='text' && !e.getTarget().readOnly){
            
        }else if(e.getKey() == 8 && e.getTarget().type =='textarea' && !e.getTarget().readOnly){ 
        
        }else if(e.getKey() == 8){
            e.preventDefault();
        }
           
    });
	/*var comndm = new Ext.data.SimpleStore({
		fields:['txdm','tame'],
		data:<%=userDM%>
	});*/
	
		if(window.parent.document.getElementById("tbpa")!=null)
		{
			if(window.parent.document.getElementById("tbpa").name=="tbpa")
			{
				window.parent.location.reload();
			}
		}	
		function renderFormula(v,p,r)
		{
			//alert(r.data.endcode);
			var benshu = (r.data.endcode - r.data.begincode+1)/ (r.data.fenshu);
			return benshu.toFixed(2);
		}
	    function renderLast(value){
	    	if(typeof(value)!="undefined" && value!="" && value!=null)
	         return String.format(value.dateFormat('Y-m-d'));
	    }
	    function renderdate(value){
	    	if(typeof(value)!="undefined" && value!="" && value!=null)
	         return String.format(value.dateFormat('Y-m-d H:i:s'));
	    }
		Ext.apply(Ext.form.VTypes, {
		    daterange : function(val, field) {
		        var date = field.parseDate(val);

		        if(!date){
		            return false;
		        }
		        if (field.startDateField) {
		            var start = Ext.getCmp(field.startDateField);
		            if (!start.maxValue || (date.getTime() != start.maxValue.getTime())) {
		                start.setMaxValue(date);
		                start.validate();
		            }
		        }
		        else if (field.endDateField) {
		            var end = Ext.getCmp(field.endDateField);
		            if (!end.minValue || (date.getTime() != end.minValue.getTime())) {
		                end.setMinValue(date);
		                end.validate();
		            }
		        }
		        /*
		         * Always return true since we're only using this vtype to set the
		         * min/max allowed values (these are tested for after the vtype test)
		         */
		        return true;
		    },

		    password : function(val, field) {
		        if (field.initialPassField) {
		            var pwd = Ext.getCmp(field.initialPassField);
		            return (val == pwd.getValue());
		        }
		        return true;
		    },

		    passwordText : 'Passwords do not match'
		});	
		Ext.data.Store.prototype.applySort = function() {
		    if (this.sortInfo && !this.remoteSort) {
		        var s = this.sortInfo, f = s.field;
		        var st = this.fields.get(f).sortType;
		        var fn = function(r1, r2) {
		            var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
		            if (typeof(v1) == "string") {
		                return v1.localeCompare(v2);
		            }
		            return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
		        };
		        this.data.sort(s.direction, fn);
		        if(this.snapshot && this.snapshot != this.data) {
		            this.snapshot.sort(s.direction, fn);
		        }
		    }
		};
		function shownoticewin(tid){
			new Ext.Window({
				id:'changpasswin',
				width:450,
				height:330,
				layout:'form',
				title:'系统消息',
				modal:true,
				plain:true,
				resizable:false,
				constrain:false,
				autoScroll:false,
				buttonAlign:'center',		    
				html:"<iframe id=thisIframe width=430 height=330 frameborder=0 scrolling=auto src='shownotice.jsp?id="+tid+"'></iframe>"
			}).show();			
		}	
		//修改密码
		function changePass () {
			changepasswin.show();
		}
		var changpassform=new Ext.FormPanel({
			defaultType : 'textfield',//表单默认类型
			labelAlign : 'center',//位置
			labelWidth : 75,
			frame : true,
			items : //元素
			[
				{
				fieldLabel : "旧密码",
				name : "oldpwd",
				inputType : "password",
				allowBlank : false
				},{
				fieldLabel : "新密码",
				name : "uerPass1",
				inputType : "password",
				allowBlank : false
				}, {
				fieldLabel : "确认密码",
				inputType : "password",
				name : "uerPass2",
				invalidText : "两次输入的密码不一致", // 验证失败出现的提示
				allowBlank : false, // 不允许为空
				validator : function() { // 验证
					var _pwd1 = this.ownerCt.items.itemAt(1)
							.getValue(1);
					var _pwd2 = this.getValue();
					if (_pwd1 == _pwd2) {
						return true;
					} else {
						return false;
					}
				}
			}
			],
			buttons:[
			{text:'修改',
			 handler:function(){
				if(changpassform.form.isValid()){
					changpassform.form.submit({
						clientValidation:true,// 进行客户端验证
						waitMsg : '正在修改密码',// 提示信息
						waitTitle : '提示',// 标题
						url : 'personsys/changpass.jsp',// 请求的url地址
						method:'POST',// 请求方式
						success:function(form,action){// 加载成功的处理函数
			              var result = action.result.success;
			              if(result)
			              {
			                  Ext.Msg.alert("提示","密码修改成功,重新登录!");		                 
				              changepasswin.hide();	
				              setTimeout(window.location.href='Logout.jsp',500);
			              }
						},
						failure:function(form,action){// 加载失败的处理函数
						  var result = 	action.result.because;
					      if (result!=""){
							 Ext.Msg.alert("提示",result);
							 form.findField('oldpwd').setValue('');
			            	 form.findField('oldpwd').focus();
					      }else{
							Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");				    	  
					      }
						}
					});					
				}
				
			}},
			{
				text : '重置',
				handler : function () {
					changpassform.form.reset();
				}
			}
			]
			});	 
		var changepasswin = new Ext.Window({
			width:250,
			height:160,
			layout:'form',
			title:'修改密码',
			modal:true,
			plain:false,
			resizable:false,
			closeAction:'hide',
			constrain:false,
			autoScroll:true,
			buttonAlign:'center',		    
			items:[changpassform]
		});
	</script></head>
<body scroll="no" id="docs">
  <div id="loading-mask" style=""></div>
  <div id="loading">
    <div class="loading-indicator"><img src="extjs/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...</div>
  </div>

  <div id="header">
<div class="gTop gTopMus" id="dvTop">
<img src="res/img/weboa_logo.gif" class="gLogo" border="0">
<!--帐户信息-->
<div class="gUmsg">
 		 <div class="fLe"><b id="bUid"><%=loginname +  " - " + user_swjg_mc%></b>&nbsp;[&nbsp;<a href="#" title="帮助">帮助</a>，&nbsp;<a href="javascript:void(0);" onclick="changePass();" title="修改密码">修改密码</a>，<a href="Logout.jsp" title="退出">退出</a>&nbsp;，当前在线人数: <%=currentNum %>]&nbsp;</div>
 </div>
		<div id="fun_btn" class="gSrh">

		</div>
  </div>
</div>
  <div id="classes"></div>

  <div id="main"></div>
  </body>
</html>
