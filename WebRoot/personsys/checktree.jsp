<%@ page contentType="text/html;charset=GBK"%>
	<div id="OtherUserGrid" ></div>
	<script>
 	Ext.form.VTypes.telnumVal  = /(^(\d{2,4}[-_－—]?)?\d{3,8}([-_－—]?\d{3,8})?([-_－—]?\d{1,7})?$)|(^0?1[35]\d{9}$)/	;
 	Ext.form.VTypes.telnumText = '联系电话有误(0311-1234567,13999999999)'; 	
 	var data_type1 = '';
 	
	Ext.form.VTypes.telnum 	= function(v){
		return Ext.form.VTypes.telnumVal.test(v);
	};
	/****** 简单Grid，以本地JSON数据作为数据源 *******/
	var OtherUserJsonDataGrid = function() {
		//定义数据
	    var store = new Ext.data.JsonStore({
	        root: 'topics',
	        totalProperty: 'totalCount',
	        successProperty:'success',
	        idProperty: 'id',
	        remoteSort: false,
	        fields: ['userid', 'usercode', 'username','usertype','userquan', 'userkebe','userstatus','telnum','swjg_mc'],
	        proxy: new Ext.data.HttpProxy({
	            url: 'WLUserData?action=show'
	        })
	    });
		/*
		 * 定义bbar中的按钮
		 * 
		 */    
		var arrbars = new Array(3); // 3 rows
		
		arrbars[0]={text:'注册',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:useraddRecord};

		arrbars[1]={text:'修改',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:userupdateRecord};
				
		arrbars[2]={text:'删除',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:userdeleteRecord};

		arrbars[3]={text:'激活',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_key.gif',handler:userliveRecord};

		arrbars[4]={text:'冻结',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_lock.gif',handler:userdiedRecord};
	    
	    function renderStatus(value, p, r){
			if(value==0)   return String.format('<b style="color:green">未激活</b>');
			if(value==1)   return String.format('正常');
			if(value==2)   return String.format('<b style="color:red">冻结</b>');
	    }
	    String.prototype.trim = function()  
        {  
            return this.replace(/(^\s*)|(\s*$)/g, "");  
        };
	    function renderType(v){
	    	v = v.replace("，",",");
	    	var nn = data_type1.length;
	    	var vv = v.split(",");//alert(v+ " : " +vv.length);
	    	var str = " ";
	    	for(var i=0;i<vv.length;i++){
	    		for(var n=0;n<nn;n++){
	    			//alert(vv[n]);
	    			if(vv[i] == data_type1[n].id){ 
	    				str+=data_type1[n].name+"，";
	    				
	    			}
	    		}
	    	}str=str.substring(0,str.length-1);
	    	return str;
	    }
	    function renderUQuan(value, p, r){
		    if(value=="") 
			    return String.format('无');
		    else
		    	 return String.format('有');
	    }		    	    
		// 2.create the column Model
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel
		([
			 sm,
			 new Ext.grid.RowNumberer({header:"编号",width:50}),
			{
		        header: "编号",
		        dataIndex: 'id',
		        hidden:true,
		        width: 150
		    },{
		        header: "登录名",
		        dataIndex: 'usercode',
		        width: 100,
		        sortable: true
		    },{
		        header: "姓名",
		        dataIndex: 'username',
		        width: 100
		    },{
		        header: "用户角色",
		        dataIndex: 'usertype',
		        width: 150,
		        renderer:renderType
		    },{
		        header: "所属机关",
		        dataIndex: 'swjg_mc',
		        width: 200
		    },{
		        header: "状态",
		        dataIndex: 'userstatus',
		        renderer:renderStatus,
		        width: 100,
		        sortable: true
		    }
		  ]);	
		    
		OtherUserJsonDataGrid.superclass.constructor.call(this, {
			title     : '',
			renderTo  : 'OtherUserGrid',
			autoScroll: true,
			width     : '100%',
			height    : '374',
			id        :'OtherUserGridPanel',
			//autoHeight: true,
	        store: store,
	        // 当鼠标移上去时是否高亮显示
	        trackMouseOver:true,
	        loadMask: true,
	        sm: sm,
	        // grid columns
	        cm:cm,
	        // customize view config
	        viewConfig: {
	            forceFit:true
	        }, 
			tbar  : [{
						xtype : 'hidden',
						name : 'swjg_dm',
						id:'findRY_swjg_dm'
					}, '-','税务机关：',new Ext.form.ComboBoxTree({
						fieldLabel : '所属机关',
						emptyText : '全部',
						anchor : '98%',
						mode : 'local',
						displayField : 'text',
						valueField : 'id',
						hiddenName : 'swjg_mc',
						name : 'swjg_mc',
						editable : false,
						autoLoad : true,
						tree : new Ext.tree.TreePanel({
							rootVisible : false,
							border : false,
							method : 'GET',
							dataUrl : 'TaskManData?action=treeSwjg',
							root : new Ext.tree.AsyncTreeNode({
								text : 'children',
								id : '0',
								expanded : true
							}),
							listeners : {
								"load" : function(node) {
									node.expand(false);
								}
							}
						}),
						listeners : {
							"expand" : function(c) {
								c.list.setWidth('320px');
								c.innerList.setWidth('auto');
							}
						}
					}),'-','姓名: ', {
				xtype : "textfield",
				id : "swry_name"
			}, "-", {
				text : "查看",
				icon:'extjs/resources/button/tool_seh.gif',
				handler : function(){
		  			// 获取top工具条
		  			var _tbar = this.getTopToolbar();
		  			// 获取text框的值
		  			var swjg_dm = _tbar.findById("findRY_swjg_dm").getValue();
		  			var swry_name = _tbar.findById("swry_name").getValue();
		  			store.baseParams = {swjg_dm:swjg_dm,
			  				swry_name:swry_name};
		  			store.load({params:{start:0, limit:15}});
				},
				tooltip:"根据姓名模糊查询",
				scope : this
			}
		    ],		            		
	        bbar: new Ext.PagingToolbar({
	        	/*
				 * 设置每次显示的记录条数 '-' 代表分符
				 * pressed:表示按钮干在开始的时候是否被按下，只有enableToggle为真的时候才有用
				 * enableToggle：表示按钮在开始的时候是否能处于被按下的状态，允许在按下没按下之间切换，添加移除x-btn-pressed样式类
				 * text:按钮干上显示的文字 cls：按钮的CSS类
				 * toggleHandler:设置enableToggle为TRUE时点击按钮时的事件处理函数
				 */
	            pageSize: 15,
	            store: store,
	            displayInfo: true,
	            firstText:"首页",
	            lastText:"末页",
	            nextText:"下一页",
	            preText:"上一页",
	            refreshText:"刷新",
	            displayMsg: '显示 第 {0} - {1}条记录，共 {2} 条',
	            emptyMsg: "没有记录",
	            items:(function(){
	            	// 根据传递过来的权限参数，进行按钮的添加
	            	var items = [];
			    			for(var j=0;j<arrbars.length;j++)
			    			{
			    					items.push('-');// 按钮之间添加分隔线
			    					items.push(arrbars[j]);	
			    			} 
	            	return items;
	            }).createDelegate(this)()
	          }),
	            listeners:{
              	'rowdblclick' : function(grid, rowIndex, e){
              		userupdateRecord();
	            	var userid = [grid.store.getAt(rowIndex).get("userid")];
	            	iniUpForm(userid);
	            	//otherUserForm.form.reset();	            		
            	}  
            }			          		           		        
		});
		Ext.Ajax.request({
			   url: 'WLUserData?action=getRole',
			   success: function(resp){
				   var r = Ext.util.JSON.decode(resp.responseText);  
				   data_type1 = r.data;
				   store.load({params:{start:0, limit:15}});
			   }
			});
				
	};
	Ext.extend(OtherUserJsonDataGrid, Ext.grid.GridPanel);
	var otheruserdatagrid = new OtherUserJsonDataGrid();
	
	var otherUserForm = new Ext.FormPanel({
		layout : "form", // 当前布局为form布局
		id:"otherUserForm",
		frame : true, // 填充颜色
		labelWidth : 55,
		baseCls : "x-plain", // 统一背景色
		bodyStyle : "padding:10px;", // css填充10个像素
		//mydisabled : false,
		defaults : {
			xtype : "textfield",
			anchor : "95%"
		},
	      reader : new Ext.data.JsonReader({
	    	  root:"data",
	    	  success:"success"
	      },
	      ['userid', 'usercode', 'username','usertype','userquan', 'userkebe','userstatus','telnum','swjg_mc','userrole']
		  ),
		
		items : [
			{
				xtype:'hidden',
				name : 'userkebe'
			},
			{
				xtype:'hidden',
				name : 'userid'
			},{
			fieldLabel : "用户名",
			emptyText : "请输入用户名",
			disabled : false,  //指定该控件是否可用(mydisabled为自定义属性)
			name : "usercode",
			allowBlank : false
				// 不允许为空
			}, {
			fieldLabel : "密码",
			name : "uerPass1",
			inputType : "password",
			allowBlank : false
				// 不允许为空
			}, {
			fieldLabel : "确认密码",
			inputType : "password",
			name : "uerPass2",
			invalidText : "两次输入的密码不一致", // 验证失败出现的提示
			allowBlank : false, // 不允许为空
			validator : function() { // 验证
				var _pwd1 = this.ownerCt.items.itemAt(3)
						.getValue(1);
				var _pwd2 = this.getValue();
				if (_pwd1 == _pwd2) {
					return true;
				} else {
					return false;
				}
			}
		},{
			fieldLabel : "真实姓名",
			emptyText : "请输入真实姓名",
			name : "username",
			allowBlank : false
		},new Ext.ux.form.LovCombo({
						fieldLabel:'角色',
						width:300,
						hideOnSelect:false,
			            editable: false,
			            name:'usertype',
						hiddenName:'usertype',
						maxHeight:200,
						store:new Ext.data.JsonStore({
						      root: 'data',
						      fields: ['id','name'],
						      proxy: new Ext.data.HttpProxy({
						          url: 'WLUserData?action=getRole'
						      }),
						      autoLoad:true
					 	 })
						,triggerAction:'all'
						,valueField:'id',
						 displayField: "name"
						,mode:'local'
					}),
			new Ext.form.ComboBoxTree({
				fieldLabel: '所属机关',
				isFormField:true,
				emptyText: '选择..',
				anchor: '100%',
                mode: 'local',
			 	displayField:'text',
			 	valueField:'id',
                hiddenName:'swjg_mc',
                name:'swjg_mc',
	            editable: false,
	            autoLoad: true,
                tree:  new Ext.tree.TreePanel({
                    rootVisible: false,
                    border: false,
                    method: 'GET',
                    dataUrl: 'TaskManData?action=treeSwjg',
                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true}),
                	listeners:{
                    	"load": function(node) {  
    			 	     }
                	}
                }),
                listeners :{
                	"expand": function(c){
                		c.list.setWidth('320px'); 
                        c.innerList.setWidth('auto');
                	} 
                }
          }),
          new Ext.ux.tree.CheckTreePanel({
              name:'userquan',
              isFormField:true,
              bubbleCheck:'none',
              cascadeCheck:'all',
              collapsible: true,
              fieldLabel:'用户权限',
              height:180,
              autoScroll:true,
              border:true,
              bodyStyle:'background-color:white;border:1px solid #B5B8C8',
              rootVisible:false,
              loader: new Ext.tree.TreeLoader({
                  preloadChildren: true,
                  clearOnLoad: false,
                  dataUrl:'TaskManData?action=treeQuanXian'
              }),
              root: new Ext.tree.AsyncTreeNode({
              	 text:'',
                  id:'root',
                 expanded:true
              })
          })
        ],
		buttons:[
					{
						xtype:'button',
						text : '确定提交',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_yes.gif',
						handler : usersubmitForm
					},{
						xtype:'button',
						text : '关闭退出',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_exit.gif',
						handler : function(){
							userwin.hide();
						}					
					}
			]			
	});
	
	// 创建弹出窗口
	/*
	 * 1.closeAction:枚举值为：close(默认值)，当点击关闭后，关闭window窗口 hide,关闭后，只是 hidden窗口
	 * 2.closable:true在右上角显示小叉叉的关闭按钮，默认为true 3.constrain：true
	 * 则强制此window控制在viewport，默认为false 4.modal:true为模式窗口，后面的内容都不能操作，默认为false
	 * 5.plain：//true则主体背景透明，false则主体有小差别的背景色，默认为false
	 */
	var userwin = new Ext.Window({
		width:350,
		layout:'form',
		modal:true,
		plain:true,
		closeAction:'hide',
		collapsible : true, // 伸缩按钮
		resizable:false,
		constrain:false,
		autoScroll:false,
		buttonAlign:'center',		    
		items:[otherUserForm]			
	});
	// 显示添加窗口
	userwin.show();	
	userwin.hide();
	function useraddRecord(){
		otherUserForm.getForm().reset();
		otherUserForm.isAdd = true;
		otherUserForm.getForm().findField("usercode").enable();
		userwin.setTitle("注册用户");
		userwin.show();	
	}


	
	// 显示修改窗口
	function userupdateRecord(){
		var stuList = getUserList();
		var num = stuList.length;
		if(num>1){
			Ext.MessageBox.alert("提示","每次只能修改一条信息,如果选择多行数据，默认情况下只对第一行有效!",function(btn){
			if(btn=='ok'){
				iniUpForm(stuList);
			}});
		}
		if(num==1){
			iniUpForm(stuList);
		}	
	}
	function iniUpForm(stuList){
		var stuId = stuList[0];
		var n = otheruserdatagrid.store.getCount();
		var v = '';
		for(var i=0;i<n;i++){
			if(otheruserdatagrid.store.getAt(i).get("userid")==stuId){
				v = otheruserdatagrid.store.getAt(i).get("swjg_mc");
			}
		}
		otherUserForm.form.reset();
		otherUserForm.getForm().findField("usercode").disable();
		otherUserForm.getForm().findField("uerPass1").allowBlank = true;
		otherUserForm.getForm().findField("uerPass2").allowBlank = true;			
		otherUserForm.isAdd = false;
		userwin.setTitle("修改用户信息");
		userloadForm(stuId,v);// 获取单条信息的详细数据		
		
			
	}
	// 加载表单数据
	function userloadForm(stuId,v){
		otherUserForm.form.load({
			waitMsg : '正在加载数据请稍后...',
			waitTitle : '提示',
			url : 'WLUserData?action=getform',
			params : {userid:stuId},
			method:'post',
			success:function(form,action){
				otherUserForm.getForm().findField("swjg_mc").setValue(v);
				userwin.show();
			},
			failure:function(form,action){// 加载失败的处理函数
				Ext.Msg.alert('提示','数据加载失败');
			}
		});
	}
	// 显示删除对话框
	function userdeleteRecord(){
		var stuList = getUserList();
		var num = stuList.length;
		if(num == 0){
			Ext.Msg.alert("提示","请至少选择一行数据然后才能够删除!");
			return;
		}
		Ext.Msg.confirm("确认消息框","是否确定对选中的行进行删除操作，删除后不能够恢复!",
		function(btn,txt)
		 {
		 		if(btn=='no')
		 				return false;
		 		else
		 		{
		 					if(btn == 'yes'){
							delOkUser(stuList);
							}
		 		}
		});			
	}
	// 取得所选记录
	function getUserList(){
		var recs = otheruserdatagrid.getSelectionModel().getSelections();
		var list = [];
		if(recs.length == 0){
			Ext.MessageBox.alert('提示','请选择要进行操作的记录！');
		}else{
			for(var i = 0 ; i < recs.length ; i++){
				var rec = recs[i];
				list.push(rec.get('userid'));
			}
		}
		return list;
	}
	// 删除的ajax请求
	function delOkUser(stuList){
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'提示',
			width : 250,
			msg:'正在删除信息请稍后......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=delete',
			params : {delid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
     			var result = response.responseText;
     	          switch (result.split("|")[0])
     	          {
     	             case '1': result='删除成功！';break;
     	             case '0': result='删除失败！';break;
     	          }
      		 Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;"+result);
	          if((response.responseText.split("|")[1]/(otheruserdatagrid.getBottomToolbar().pageSize))<(otheruserdatagrid.getBottomToolbar().getPageData().activePage))
	          {
	        	  otheruserdatagrid.store.load({params:{start:0, limit:15}});
		      }else
		      {
		    	  otheruserdatagrid.store.reload();
		      }               
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
			}
		});
	}
	
	// 提交表单数据
	function usersubmitForm(){
		// 判断当前执行的提交操作，isAdd为true表示执行书籍新增操作，false表示执行书籍修改操作
		if(otherUserForm.isAdd){
			// 新增
			if(otherUserForm.form.isValid()){
				otherUserForm.form.submit({
					clientValidation:true,// 进行客户端验证
					waitMsg : '正在提交数据请稍后',// 提示信息
					waitTitle : '提示',// 标题
					url : 'WLUserData?action=new',// 请求的url地址
					method:'POST',// 请求方式
					success:function(form,action){// 加载成功的处理函数
		              userwin.hide();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;新增成功!");
		              }			
		              otheruserdatagrid.store.reload();
					},
					failure:function(form,action){// 加载失败的处理函数
						//alert(action.result.because);
						//alert(action.result.because!="");
						if(action.result.because!="")
						{
							Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;该用户名已存在!");								
						}else
						{
						Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
						}
					}
				});					
			}
		}else{
			// 修改信息
		if(otherUserForm.form.isValid()){
			otherUserForm.form.submit({
				clientValidation:true,// 进行客户端验证
				waitMsg : '正在提交数据请稍后',// 提示信息
				waitTitle : '提示',// 标题
				url : 'WLUserData?action=edit',// 请求的url地址
				method:'POST',// 请求方式
				success:function(form,action){// 加载成功的处理函数
	              userwin.hide();
	              var result = action.result.success;
	              if(result)
	              {
	                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;修改成功!");
	                  
	              }
	              otheruserdatagrid.store.reload();
				},
				failure:function(form,action){// 加载失败的处理函数
					Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
				}
			});
		}	
		}
	}
	//激活用户
	function userliveRecord(){
		var stuList = getUserList();
		//alert(stuList.length);
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'提示',
			width : 250,
			msg:'正在激活用户请稍后......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=liveuser&type=1',
			params : {userid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
		    	otheruserdatagrid.store.reload();             
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
			}
		});
	
	}
	//激活用户
	function userdiedRecord(){
		var stuList = getUserList();
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'提示',
			width : 250,
			msg:'正在激活用户请稍后......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=liveuser&type=2',
			params : {userid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
		    	otheruserdatagrid.store.reload();             
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
				
		}
		});
		
	}
											
	</script>