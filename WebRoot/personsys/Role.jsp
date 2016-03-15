<%@ page contentType="text/html;charset=GBK"%>
		<div id="RoleGridPanel"></div>
		<script>
		var rolepriv = "1,2";
		/****** 简单Grid，以本地JSON数据作为数据源 *******/
		var RoleJsonDataGrid = function() {
			//定义数据
	    // 1.create the Data Store
		    var store = new Ext.data.JsonStore({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        successProperty:'success',
		        idProperty: 'id',
		        remoteSort: false,
		        
		        fields: ['id', 'name', 'detail'],
		
		        // load using script tags for cross domain, if the data in on the same
				// domain as
		        // this page, an HttpProxy would be better
		        proxy: new Ext.data.HttpProxy({
		            url: 'RoleData?action=show'
		        }),
		         sortInfo: {field: 'id', direction: 'DESC'}
		    });
			/*
			 * 定义bbar中的按钮
			 * 
			 */    
			var arrbars = new Array(3); // 3 rows
			
			arrbars[0]={text:'新增',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:roleaddRecord};

			arrbars[1]={text:'修改',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:roleupdateRecord};
					
			arrbars[2]={text:'删除',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:roledeleteRecord};
													    
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
			        width: 200
			    },			{
			        header: "角色名称",
			        dataIndex: 'name',
			        width: 100,
			        sortable: true
			    },{
			        header: "备注",
			        dataIndex: 'detail',
			        width: 100
			    }
			  ]);	
		  		    
		    RoleJsonDataGrid.superclass.constructor.call(this, {
				title     : '',
				autoScroll: true,
				width     : '100%',
				height    : '250',
				id        :'RoleGridPanel',
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
				    				if(rolepriv.indexOf(j.toString())>=0)
				    				{
				    					items.push('-');// 按钮之间添加分隔线
				    					items.push(arrbars[j]);	
				    				}
				    			} 
		            	return items;
		            }).createDelegate(this)()
		          }),
		            listeners:{
		              	'rowdblclick' : function(grid, rowIndex, e){
		              	//alert(grid.store.getAt(rowIndex).get("id"));
		            	var id = grid.store.getAt(rowIndex).get("id");
						roleForm.form.reset();
						var selNodes = tree.getChecked();
					    Ext.each(selNodes, function(node){
					         node.attributes.checked = false;
					         node.ui.checkbox.checked = false;
					    });		
		            	roleloadForm(id);
		        		//var infoForm = new InfoDetailFrom(id);
		        		//mtp.addTab('view_'+id, '查看客户 '+name+' 的维修信息', infoForm);
		              	/*
		              	 * (grid.getSelectionModel().getSelections())[0].get('id')
		              	 * grid.store.getAt(rowIndex).get("id")
		              	 */
		              	
		            }  
		            }		           		        
			});
			store.load({params:{start:0, limit:15}});		
		};
		
		Ext.extend(RoleJsonDataGrid, Ext.grid.GridPanel);
		var roledatagrid = new RoleJsonDataGrid();
		
		//定义权限树
	    var tree = new Ext.ux.tree.CheckTreePanel({
	        rootVisible:false,
	        lines:true,isFormField:true,
	        autoScroll:true,
	        animCollapse:false,
	        animate: true,
	        frame: true,
	        name:'menuId',
	        loader: new Ext.tree.TreeLoader({
				preloadChildren: true,
				clearOnLoad: false,
				dataUrl:'SysRoleMenus'
			}),
	        root: new Ext.tree.AsyncTreeNode({
	            text:'Ext JS',
	            id:'root',
	            expanded:true
	         })
	    });
	    tree.getRootNode().expand(true);
		tree.on('checkchange11111', function(node, checked) { 
			if(checked){
				//若为子节点选中,则选取中父节点
				if(node.isLeaf())
				{
					   //获取所有父节点
				      var pNode = node.parentNode;
				      //当你节点不为root时执行
				      if(pNode.id!='01')
				      {
				    	  if (tree.getChecked(id, node.parentNode) == "") {
					            pNode.ui.checkbox.checked = checked;
					            pNode.attributes.checked = checked;
					     }  	    	  
				      }				
				}else
				{
					node.attributes.checked = checked;   
				    //获取所有子节点,变为选中状态
				    node.cascade( function( node ){
				         node.attributes.checked = checked;
				         node.ui.checkbox.checked = checked;
				     }); 					
				}
			}else
			{
				if(node.isLeaf())
				{
					//若为子节点不选中时,先变为父节点,查看所有子节点是否全不选,若全不选则父节点变不选
					var pNode = node.parentNode;
				      if(pNode.id!='01')
				      {					
						var flag = true;
						pNode.eachChild( function( pNode ){
							//alert(pNode.attributes.checked);
							if(pNode.attributes.checked) 
							{
								flag = false;
							}
						});
						if(flag) 
						{
							pNode.attributes.checked = checked; 
							pNode.ui.checkbox.checked = checked;
						} 
				      }	 
				}else
				{
					node.attributes.checked = checked;   
				    //获取所有子节点,全改变为不选中状态
				    node.cascade( function( node ){
				         node.attributes.checked = checked;
				         node.ui.checkbox.checked = checked;
				     }); 					
				}
			}			

	      
		}, tree);  
		//定义角色Form	    	    		
		var roleForm = new Ext.FormPanel({
			labelAlign: 'left',
		    buttonAlign: 'right',
		    bodyStyle: 'padding:5px',
		    frame: true,
		    labelWidth: 88,
			defaults : {
				xtype : "textfield",
				anchor : "95%"
			},
		      reader : new Ext.data.JsonReader({
		    	  root:"data",
		    	  success:"success"
			      },
			      ['id', 'name', 'detail','menuId','add_priv','update_priv','del_priv','data_range']
			  ),
			
			items : [
				{
				xtype:'hidden',
				name : 'id'
				}/*,{
					xtype:'hidden',
					name : 'menuId'
				}*/
				,{
				fieldLabel : "角色名称",
				emptyText : "请输入角色名称",
				//disabled : this.mydisabled,  //指定该控件是否可用(mydisabled为自定义属性)
				name : "name",
				allowBlank : false
				},{
				fieldLabel : "备注",
				xtype : "textfield", // 多行文本框
				name : "detail" // name
				},{
					xtype: 'fieldset',
			        title: '权限',
			        layout: 'column',
			        height: '100%',
			        items: [tree]			
				}
				],
			buttons:[
						{
							xtype:'button',
							text : '确定提交',
							cls:'x-btn-text-icon',
							icon:'extjs/resources/button/win_yes.gif',
							handler: rolesubmitForm
						},{
							xtype:'button',
							text : '重置',
							cls:'x-btn-text-icon',
							icon:'extjs/resources/button/win_exit.gif',
							handler : function(){
							roleForm.form.reset();
							var selNodes = tree.getChecked();
						    Ext.each(selNodes, function(node){
						         node.attributes.checked = false;
						         node.ui.checkbox.checked = false;
						    });							
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
		var rolewin = new Ext.Window({
			width:350,
			height:500,
			layout:'form',
			modal:true,
			plain:true,
			closeAction:'hide',
			collapsible : true, // 伸缩按钮
			resizable:false,
			constrain:false,
			autoScroll:false,
			buttonAlign:'center',		    
			items:[roleForm]				
		});
		// 显示添加窗口
		function roleaddRecord(){
			roleForm.form.reset();
			roleForm.isAdd = true;
			rolewin.setTitle("新增角色");
			rolewin.show();		
		}
		// 显示修改窗口
		function roleupdateRecord(){
			var stuList = getRoleList();
			
			var num = stuList.length;
			//alert(num>=1);
			if(num>1){
				Ext.MessageBox.alert("提示","每次只能修改一条信息,如果选择多行数据，默认情况下只对第一行有效!",function(btn){
				if(btn=='ok')
				{
					roleForm.form.reset();
					var selNodes = tree.getChecked();
				    Ext.each(selNodes, function(node){
				         node.attributes.checked = false;
				         node.ui.checkbox.checked = false;
				    });					

					var stuId = stuList[0];
					roleloadForm(stuId);// 获取单条信息的详细数据	
				}
				});
			}
			if(num==1){
				roleForm.form.reset();
				var selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			         node.attributes.checked = false;
			         node.ui.checkbox.checked = false;
			    });					
				
				var stuId = stuList[0];
				roleloadForm(stuId);// 获取单条信息的详细数据				
			}	
		}
		// 显示删除对话框
		function roledeleteRecord(){
			var stuList = getRoleList();
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
								delOkRole(stuList);
								}
    		 		}
    		});			
		}
		// 取得所选记录
		function getRoleList(){
			var recs = roledatagrid.getSelectionModel().getSelections();
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
		function delOkRole(stuList){
			var stuIds = stuList.join('-');
			var msgTip = Ext.MessageBox.show({
				title:'提示',
				width : 250,
				msg:'正在删除信息请稍后......'
			});
			Ext.Ajax.request({
				url : 'RoleData?action=delete',
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
		          if((response.responseText.split("|")[1]/(roledatagrid.getBottomToolbar().pageSize))<(roledatagrid.getBottomToolbar().getPageData().activePage))
		          {
		        	  roledatagrid.store.load({params:{start:0, limit:15}});
			      }else
			      {
			    	  roledatagrid.store.reload();
			      }               
				},
				failure : function(response,options){
					msgTip.hide();
         		 Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
				}
			});
		}
		// 加载表单数据
		function roleloadForm(stuId){
			roleForm.form.load({
				waitMsg : '正在加载数据请稍后',// 提示信息
				waitTitle : '提示',// 标题
				url : 'RoleData?action=getform',// 请求的url地址
				params : {id:stuId},
				method:'GET',// 请求方式
				success:function(form,action){// 加载成功的处理函数
					/*if(action.result["data"].menuId!=""){
	         			var menuarray = action.result["data"].menuId.split(", ");
	       	          	for(var i=0;i<menuarray.length-1;i++)
	       	          	{
	       	          		var node = tree.getNodeById(menuarray[i]);
	       	          		if(node.id=='01') continue;
	       	          		node.attributes.checked = true;
	       	        	 	node.ui.checkbox.checked = true;       	          		
	           	         }			
					}	*/
					//form.findField("uerPass1").setValue(action.result["data"].password);
					//form.findField("uerPass2").setValue(action.result["data"].password);
					 Ext.Msg.alert('提示','数据加载成功');
					 //roledatagrid.store.reload();
				},
				failure:function(form,action){// 加载失败的处理函数
					Ext.Msg.alert('提示','数据加载失败');
				}
			});
		}
		// 提交表单数据
		function rolesubmitForm(){
			// 判断当前执行的提交操作，isAdd为true表示执行书籍新增操作，false表示执行书籍修改操作
			//alert(roleForm.getForm().findField("id").getValue()=="");
			if(roleForm.getForm().findField("id").getValue()==""){
				// 新增
			    var msg = '', selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			        if(msg.length > 0){
			            msg += ',';
			        }
			        msg += node.id;
			    });
			    //roleForm.getForm().findField("menuId").setValue(msg);
				if(roleForm.form.isValid()){        		
					roleForm.form.submit({
						clientValidation:true,// 进行客户端验证
						waitMsg : '正在提交数据请稍后',// 提示信息
						waitTitle : '提示',// 标题
						url : 'RoleData?action=new',// 请求的url地址
						method:'POST',// 请求方式
						success:function(form,action){// 加载成功的处理函数
			              //userwin.hide();
				          form.reset(); 
						    Ext.each(selNodes, function(node){
						         node.attributes.checked = false;
						         node.ui.checkbox.checked = false;
						    });
			              var result = action.result.success;
			              if(result)
			              {
			                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;新增成功!");
			              }		
			              roledatagrid.store.reload();	
			              //userdatagrid.store.reload();
						},
						failure:function(form,action){// 加载失败的处理函数
							Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
						}
					});					
				}	
			}else{
				// 修改信息
			    var msg = '', selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			        if(msg.length > 0){
			            msg += ',';
			        }
			        msg += node.id;
			    });
			    //roleForm.getForm().findField("menuId").setValue(msg);				
				roleForm.form.submit({
					clientValidation:true,// 进行客户端验证
					waitMsg : '正在提交数据请稍后',// 提示信息
					waitTitle : '提示',// 标题
					url : 'RoleData?action=edit',// 请求的url地址
					method:'POST',// 请求方式
					success:function(form,action){// 加载成功的处理函数
					//将form及tree清空
						roleForm.form.reset();
					    Ext.each(selNodes, function(node){
					         node.attributes.checked = false;
					         node.ui.checkbox.checked = false;
					    });
					    roledatagrid.store.reload();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;修改成功!");
		                  
		              }	
		             
					},
					failure:function(form,action){// 加载失败的处理函数
						Ext.Msg.alert("请求错误","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
					}
				});
			}
		}		
		
		Ext.override(Ext.form.BasicForm,{ 
		    findField : function(id){         
		        var field = this.items.get(id);         
		        if(!field){ 
		            this.items.each(function(f){ 
		                if(f.isXType('radiogroup')||f.isXType('checkboxgroup')){ 
		                    f.items.each(function(c){ 
		                        if(c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id)){ 
		                            field = c; 
		                            return false; 
		                        } 
		                    }); 
		                } 
		                                 
		                if(f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)){ 
		                    field = f; 
		                    return false; 
		                } 
		            }); 
		        } 
		        return field || null; 
		    }  
		});
		new Ext.Panel({
			renderTo:'RoleGridPanel',
			layout:"column",
			border:false,
			items:[{
				columnWidth:.7,
				title:'角色信息',
				items:roledatagrid
				
			},{
				columnWidth:.3,
				title:'',
				items:roleForm
			}]
		});
        </script>	