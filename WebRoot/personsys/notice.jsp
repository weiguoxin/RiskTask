<%@ page contentType="text/html;charset=GBK"%>
		<div id="noticeGrid" ></div>
		<script>
		var noticeGrid = function( ) {
			//定义数据
	    // 1.create the Data Store
		    var store = new Ext.data.JsonStore({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        successProperty:'success',
		        idProperty: 'id',
		        remoteSort: false,		        
		        fields: ['id', 'title', {name: 'createdate',type:'date',dateFormat:'Y/m/d H:i:s'}, 'notice'],
		
		        // load using script tags for cross domain, if the data in on the same
				// domain as
		        // this page, an HttpProxy would be better
		        proxy: new Ext.data.HttpProxy({
		            url: 'WLUserData?action=shownotice'
		        })
		    });	
		    function renderToNotice(v,p,r)
		    {
				if(v==1)
				{
					return "<img src='extjs/resources/button/already_incept.gif'/>";
				}else
				{
					return "<img src='extjs/resources/button/win_exit.gif'";
				}	
			}
			/*-- ontime时间转换--*/
		    function renderToNowz(value){
		        return String.format(value.dateFormat('Y年m月d日 H:i:s'));
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
			        width: 50
			    },{
			        header: "标题",
			        dataIndex: 'title',
			        width: 100
			    },{
			        header: "是否公告",
			        dataIndex: 'notice',
			        width: 20,
			        renderer: renderToNotice
			    },{
			        header: "发布时间",
			        dataIndex: 'createdate',
			        renderer: renderToNowz,
			        width: 60
			    }
			  ]);
			  	
			var arrbars = new Array(3); // 3 rows
			
			arrbars[0]={text:'添加',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:noticeaddRecord};

			arrbars[1]={text:'修改',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:noticeupdateRecord};
					
			arrbars[2]={text:'删除',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:noticedeleteRecord};

		  		    
			noticeGrid.superclass.constructor.call(this, {
				title     : '系统新闻',
				renderTo  : 'noticeGrid',
				autoScroll: true,
				width     : '100%',
				height    : '400',
				//autoHeight: true,
		        store: store,
		        // 当鼠标移上去时是否高亮显示
		        trackMouseOver:true,
		        loadMask: true,
		        // grid columns
		        sm:sm,
		        cm:cm,
		        // customize view config
		        viewConfig: {
		            forceFit:true
		        },			        
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
		        		noticeupdateRecord();	
	           		 }  
	            }			           		        
			});
			store.load({params:{start:0, limit:15}});	
	}
	Ext.extend(noticeGrid, Ext.grid.GridPanel);	
	var noticegrid = new noticeGrid();


	var noticeForm = new Ext.FormPanel({
		layout : "form", // 当前布局为form布局
		id:"noticeForm",
		frame : true, // 填充颜色
		labelWidth : 45,
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
	      ['id', 'title', 'notice','doccontent']
		  ),
		
		items : [
			{
			xtype:'hidden',
			name : 'id'
			},{
			fieldLabel : "标题",
			emptyText : "请输入标题",
			name : "title",
			allowBlank : false
			}, {
				xtype:'checkbox',
				boxLabel : "是否为公告",
				name : "notice",
				inputValue : 1,
				checked:true
			}, {
	            xtype:'htmleditor',
	            name:'doccontent',
	            fieldLabel:'内容',
	            fontFamilies : ['宋体','黑体','隶书','Arial','Courier New','Tahoma','Times New Roman','Verdana'],
	            defaultFont: '宋体',	            
	            height:300,
	            anchor:'98%'
	        }
		],
		buttons:[
					{
						xtype:'button',
						text : '确定提交',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_yes.gif',
						handler : noticesubmitForm
					},{
						xtype:'button',
						text : '关闭退出',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_exit.gif',
						handler : function(){
							noticewin.hide();
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
	var noticewin = new Ext.Window({
		width:600,
		layout:'form',
		modal:true,
		plain:true,
		closeAction:'hide',
		resizable:false,
		constrain:false,
		autoScroll:false,
		buttonAlign:'center',		    
		items:[noticeForm]			
	});
	// 显示添加窗口
	function noticeaddRecord(){
		noticeForm.form.reset();
		noticeForm.isAdd = true;
		noticewin.setTitle("添加新闻");
		noticewin.show();	

	}


	
	// 显示修改窗口
	function noticeupdateRecord(){
		var stuList = getnoticeList();
		
		var num = stuList.length;
		//alert(num>=1);
		if(num>1){
			Ext.MessageBox.alert("提示","每次只能修改一条信息,如果选择多行数据，默认情况下只对第一行有效!",function(btn){
			if(btn=='ok')
			{
				noticeForm.form.reset();
				noticeForm.getForm().findField("noticename").disabled = true;
				noticeForm.isAdd = false;
				noticewin.setTitle("修改新闻信息");
				noticewin.show();
				var stuId = stuList[0];
				noticeloadForm(stuId);// 获取单条信息的详细数据	
			}
			});
		}
		if(num==1){
			noticeForm.form.reset();
			noticeForm.isAdd = false;
			noticewin.setTitle("修改新闻信息");
			noticewin.show();
			var stuId = stuList[0];
			noticeloadForm(stuId);// 获取单条信息的详细数据				
		}	
	}
	// 显示删除对话框
	function noticedeleteRecord(){
		var stuList = getnoticeList();
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
							delOknotice(stuList);
							}
		 		}
		});			
	}
	// 取得所选记录
	function getnoticeList(){
		var recs = noticegrid.getSelectionModel().getSelections();
		var list = [];
		if(recs.length == 0){
			Ext.MessageBox.alert('提示','请选择要进行操作的记录！');
		}else{
			for(var i = 0 ; i < recs.length ; i++){
				var rec = recs[i];
				list.push(rec.get('id'))
			}
		}
		return list;
	}
	// 删除的ajax请求
	function delOknotice(stuList){
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'提示',
			width : 250,
			msg:'正在删除信息请稍后......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=delnotice',
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
	          if((response.responseText.split("|")[1]/(noticegrid.getBottomToolbar().pageSize))<(noticegrid.getBottomToolbar().getPageData().activePage))
	          {
	        	  noticegrid.store.load({params:{start:0, limit:15}});
		      }else
		      {
		    	  noticegrid.store.reload();
		      }               
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
			}
		});
	}
	// 加载表单数据
	function noticeloadForm(stuId){
		noticeForm.form.load({
			waitMsg : '正在加载数据请稍后',// 提示信息
			waitTitle : '提示',// 标题
			url : 'WLUserData?action=getnotice',// 请求的url地址
			params : {id:stuId},
			method:'GET',// 请求方式
			success:function(form,action){// 加载成功的处理函数
				 Ext.Msg.alert('提示','数据加载成功');
			},
			failure:function(form,action){// 加载失败的处理函数
				Ext.Msg.alert('提示','数据加载失败');
			}
		});
	}
	// 提交表单数据
	function noticesubmitForm(){
		// 判断当前执行的提交操作，isAdd为true表示执行书籍新增操作，false表示执行书籍修改操作
		if(noticeForm.isAdd){
			// 新增
			if(noticeForm.form.isValid()){
				noticeForm.form.submit({
					clientValidation:true,// 进行客户端验证
					waitMsg : '正在提交数据请稍后',// 提示信息
					waitTitle : '提示',// 标题
					url : 'WLUserData?action=newnotice',// 请求的url地址
					method:'POST',// 请求方式
					success:function(form,action){// 加载成功的处理函数
		              noticewin.hide();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;新增成功!");
		              }			
		              noticegrid.store.reload();
					},
					failure:function(form,action){// 加载失败的处理函数

						Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
					}
				});					
			}
		}else{
			// 修改信息
		if(noticeForm.form.isValid()){
			noticeForm.form.submit({
				clientValidation:true,// 进行客户端验证
				waitMsg : '正在提交数据请稍后',// 提示信息
				waitTitle : '提示',// 标题
				url : 'WLUserData?action=editnotice',// 请求的url地址
				method:'POST',// 请求方式
				success:function(form,action){// 加载成功的处理函数
	              noticewin.hide();
	              var result = action.result.success;
	              if(result)
	              {
	                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;修改成功!");
	                  
	              }	
	              //sm.clearSelections();
	              noticegrid.store.reload();
				},
				failure:function(form,action){// 加载失败的处理函数
					Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
				}
			});
		}	
		}
	}	
		</script>