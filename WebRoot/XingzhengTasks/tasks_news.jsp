<%@ page contentType="text/html;charset=utf-8"%>

<div id="newTaskForm"></div>
<div id="newTaskGrid"></div>
<script>
	var newTaskForm = new Ext.form.FormPanel({
		title : '查询条件',
		labelAlign : 'right',
		labelWidth : 90,
		frame : true,
		renderTo : "newTaskForm",
		bodyStyle : 'padding: 0 0 0 0;',
		style: 'padding:0 0 0 0;min-width:970px',
		width : '100%',
		buttonAlign : "center",
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .33,
				layout : 'form',
				items : [ {
					fieldLabel : '标题',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'title',
					width : 50
				},{
					xtype : 'compositefield',
					fieldLabel : '下发日期',
					items : [ {
						xtype : 'datefield',
						name : 'begindate',
						value : getMonthFirstDay(new Date()),
						width : 105,
						editable : false,
						format : 'Y-m-d'
					}, {
						xtype : 'label',
						text : '至'
					}, {
						xtype : 'datefield',
						fieldLabel : '截至日期',
						value : getMonthLastDay(new Date()),
						name : 'enddate',
						width : 105,
						editable : false,
						format : 'Y-m-d'
					} ]
				} ]
			}, {
				columnWidth : .33,
				layout : 'form',
				items : [ {
					xtype : 'hidden',
					name : 'jieshouswjg'
				}, new Ext.form.ComboBoxTree({
					fieldLabel : '任务接收机关',
					emptyText : '全部',
					anchor : '98%',
					mode : 'local',
					displayField : 'text',
					valueField : 'id',
					hiddenName : 'jieshouswjg',
					name : 'jieshouswjg',
					JiLian:'AAADDD',
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
				}),{
					 xtype:'combo',
					 id: 'AAADDD',
					 fieldLabel : '任务接收人',
					 emptyText : '全部',
					 //tpl:'请先选择税务机关',
					 displayField:'username',
					 valueField:'userid',
					 anchor: '98%',
					 mode:'local',
					 triggerAction:'all',
					 hiddenName : 'jieshourenid',
					 forceSelection:true,
		             editable: false,
					 store:new Ext.data.JsonStore({
					      root: 'topics',
					      fields: ['userid','username'],
					      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=2&&swjg_dm=21409000000'}),
					      autoLoad: false
				 	 })
					} ]
			} , {
				columnWidth : .33,
				layout : 'form',
				items : [ {
					xtype : 'compositefield',
					fieldLabel : '状态',
					items : [ new Ext.form.Radio({
						boxLabel : '全部',
						name : 'status',
						inputValue : -2,
						checked : true
					}), new Ext.form.Radio({
						boxLabel : '执行中',
						name : 'status',
						inputValue : 0
					}), new Ext.form.Radio({
						boxLabel : '已完成',
						name : 'status',
						inputValue : 1
					}) ]
				}]
			}]
		} ],
		buttons : [ {
			text : '查询',
			handler : function() {
				if (newTaskForm.form.isValid()) {
					newTaskGrid.store.load({
						params : {
							start : 0,
							limit : 15
						}
					});
				}
			}
		}, {
			text : '清空查询条件',
			handler : function() {
				newTaskForm.getForm().reset();
			}
		} ]
	});

	newTaskGrid = function(config) {
		var thisGrid = this;
		// 数据源信息
		var fields = [ {
			name : "id",
			type : 'int'
		}, {
			name : "begindate",
			type : 'date',
			dateFormat : 'Y/m/d H:i:s'
		}, {
			name : "enddate",
			type : 'date',
			dateFormat : 'Y/m/d H:i:s'
		},  "title", "content", "xiafarenid", "xiafaswjg", "jieshourenid", "jieshouswjg", "articles", "status",
		"xiafarenswjgmc","xiafarenmc","jieshourenswjgmc","jieshourenmc"];
		var record = Ext.data.Record.create(fields);
		var store = new Ext.data.JsonStore ({
				totalProperty : 'totalCount',
				successProperty : 'success',
				idProperty : 'id',
				root : 'topics',
				fields : fields,
				remoteSort : false,
				proxy : new Ext.data.HttpProxy({
					url : 'XingZhengTasks?act=show&type=4'
				}),
				listeners : {
					"beforeload" : function(t, o) {
						t.baseParams = config.myForm.form.getValues(false);
					}
				}
		});
		var rn = new Ext.grid.RowNumberer();
		var sm = new Ext.grid.CheckboxSelectionModel();
		Ext.applyIf(sm, { getEditor: function() { return false; } }); 
		var cm = new Ext.grid.ColumnModel(
				[
						sm,
						rn,
						{
							header : "任务名称",
							dataIndex : "title",
							width : 100,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "任务下发机关",
							dataIndex : "xiafarenswjgmc",
							width : 180
						},
						{
							header : "任务下发人",
							dataIndex : "xiafarenmc",
							width : 60
						},{
							header : "执行税务机关",
							dataIndex : "jieshourenswjgmc",
							width : 180
						},{
							header : "执行人",
							dataIndex : "jieshourenmc",
							width : 60
						},
						{
							header : "下发时间",
							dataIndex : "begindate",
							renderer : renderLast,
							width : 100
						},
						{
							header : "完成时间",
							dataIndex : "enddate",
							renderer : renderLast,
							width : 100
						},
						{
							header : "状态",
							dataIndex : "status",
							width : 80,
							renderer : function(v, p, r) {
								if (null == v)
									return '';
								else if (v == -1) {
									return '<span class="file-py" style="padding-left:20px;">未下发</span>';
								} else if (v == 0) {
									return '<span class="hourglass" style="padding-left:20px;">执行中</span>';
								} else if (v == 1)
									return '<span class="flag-green" style="padding-left:20px;">已执行</span>';
								else
									return '';
							}
						}]);
		var pageBar = new Ext.PagingToolbar({
			pageSize : 15,
			store : store,
			displayInfo : true,
			firstText : "首页",
			lastText : "末页",
			nextText : "下一页",
			preText : "上一页",
			refreshText : "刷新",
			displayMsg : '显示 第 {0} - {1}条记录，共 {2} 条',
			emptyMsg : "没有记录"
		});
		
		var newTask = {
			text : '新建任务',
			cls : 'x-btn-text-icon details',
			icon : 'extjs/resources/button/add.png',
			handler : function() {	newXingZHengTaskWin(thisGrid,null);	}
		};
		var delTask = {
			text : '删除任务',
			cls : 'x-btn-text-icon details',
			icon : 'extjs/resources/button/win_exit.gif',
			handler : function() {del_Task();}
		};
		
		var cfg = {
			title : '',
			plain : true,
			frame : true,
			autoScroll : true,
			iconCls : 'silk-grid',
			style : 'padding:0 0 0 0;min-width:900px',
			width : '100%',
			height : 386,
			store : store,
			trackMouseOver : true,
			loadMask : true,
			cm : cm,
			sm : sm,
			viewConfig : {
				forceFit : true
			},
			bbar : [ pageBar, newTask, delTask ],
			listeners : {
				'rowdblclick' : function(grid, rowIndex, e) {
					var rec = grid.store.getAt(rowIndex);
					newXingZHengTaskWin(grid,rec);
				}
			}
		};
		function del_Task() {
			var recs = newTaskGrid.getSelectionModel().getSelections();
			var n = recs.length;
			if (n == 0) {
				Ext.MessageBox.alert('提示', '请选择要进行操作的记录！');
				return;
			}
			Ext.Msg.confirm('确认', '是否确认删除' + n + "条任务?(删除不可恢复,已完成任务不可删除.)", function(f) {
				if (f != 'yes')
					return;
				var list = [];
				for ( var i = 0; i < recs.length; i++) {
					var rec = recs[i];
					list.push(rec.get('id'));
				}
				Ext.Ajax.request({
					url : 'XingZhengTasks?act=del&idList=' + list,
					params : {},
					success : function(resp) {
						var res = Ext.util.JSON.decode(resp.responseText);
						if (res.r > 0) {
							Ext.MessageBox.alert('提示', "成功删除任务" + res.r + "条.",function() {newTaskGrid.store.reload();});
						} else {
							Ext.MessageBox.alert('提示', '任务删除失败,原因:' + res.res);
						}
					}
				});
			});
		}
		config = Ext.applyIf(config || {}, cfg);
		newTaskGrid.superclass.constructor.call(this, config);
		store.load({
			params : {
				start : 0,
				limit : 15
			}
		});
	};
	Ext.extend(newTaskGrid, Ext.grid.EditorGridPanel );
	var newTaskGrid = new newTaskGrid({
		isLead : true,
		url : 'TasksTempData?action=updateTask',
		myForm : newTaskForm,
		renderTo : 'newTaskGrid'
	});
	function newXingZHengTaskWin(grid,rec) {
		var add = null==rec?true:false;
		var editer = false;
		if(!add){editer=rec.get("status")==1?true:false;}
		var mForm = new Ext.FormPanel({
			labelAlign: 'right',
			fileUpload:true,
			labelWidth: 90,
			frame: true,
			width: '90%',
			//height: 290,
			buttonAlign : "center",
			items : [{
				xtype : 'hidden',
				name : 'jieshouswjg',
				value: add?"0":rec.get('jieshouswjg')
			},{
				xtype : 'hidden',
				name : 'id',
				value: add?"0":rec.get('id')
			},{
				fieldLabel: '任务标题',
                xtype: 'textfield',
				anchor: '70%',
			    selectOnFocus:true,
				allowBlank : false,
				readOnly: editer,
				value: add?"":rec.get('title'),
                name: 'title',
                width:50  		 
			}, new Ext.form.ComboBoxTree({
				fieldLabel : '任务接收机关',
				emptyText : '请选择',
				anchor : '50%',
				mode : 'local',
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'jieshouswjgmc',
				name : 'jieshouswjgmc',
				JiLian:'qweqwe',
				JiLianType: 3,
				allowBlank : false,
				editable : false,
				readOnly: editer,
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
			}),{
				 xtype:'combo',
				 id: 'qweqwe',
				 fieldLabel : '任务接收人',
				 emptyText : '自动',
				 //tpl:'请先选择税务机关',
				 displayField:'username',
				 valueField:'userid',
				 anchor: '50%',
				 mode:'local',
				 triggerAction:'all',
				 name:'jieshourenid',
				 hiddenName : 'jieshourenid',
				 forceSelection:true,
	             editable: false,
				 readOnly: editer,
				 store:new Ext.data.JsonStore({
				      root: 'topics',
				      fields: ['userid','username'],
				      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=2&&swjg_dm=21409000000'}),
				      autoLoad: false
			 	 })
				},
				{
					fieldLabel: '任务内容',
	                xtype: 'textarea',
					anchor: '90%',
				    selectOnFocus:true,
	                editable: false,
					readOnly: editer,
	                name: 'content',
					value: add?"":rec.get('content'),
	                width:50 ,
	                height: 200
				} ,
				{
					fieldLabel: '任务回馈',
	                xtype: 'textarea',
					anchor: '90%',
				    selectOnFocus:true,
	                editable: false,
	                readOnly:true,
	                hidden:add,
	                name: 'articles',
					value: add?"":rec.get('articles'),
	                width:50 ,
	                height: 200
				} ,{
		            xtype:'fieldset',
		            id:'fujianField',
		            hidden:  !add,
		            title: '查看/添加原始附件',
		            collapsible: true,
		            collapsed : true,
		            autoHeight:true,
		            items :[{
						xtype: 'compositefield',
						fieldLabel: '添加附件',
						items: [
							{
							    xtype:'textfield',
							    name:'upfileName',
							    autoCreate:{
							        tag:'input',
							        type:'file',
							        size:'50'
							    }
							},{
							    text:'上传',
							    xtype:'button',
							    handler : function() {
							    	mForm._doUpload();
							    }}]}
		                    ]},{
		            xtype:'fieldset',
		            title: '查看附件',
		            collapsible: true,
		            collapsed : true,
		            autoHeight:true,
	                hidden:add,
		            items :[{
			            xtype:'fieldset',
			            title: '原始附件',
			            collapsible: true,
			            collapsed : false,
			            autoHeight:true,
		                hidden:add,
			            items :[],
			             listeners:{
			            	 render:function(p){
			            		 Ext.Ajax.request({
			            			 url:'FileAction',
			                         params:{act:'getFileList_new',taskid:add?"-1":rec.get('id'),fujianField:'file1',tbName:'xingzhengtasks'},
			                         method:'post',
			                         success:function(action,o){
			                            var json = Ext.util.JSON.decode(action.responseText);
			                            if(json.success&&json.r>0) {
			                            	var ja = json.fileList;var len=ja.length;
			                            	for(var i=0;i<len;i++){
			                            		addCompositefield(p,ja[i],false);
			                            	}
			                             	p.doLayout();
			                             }
			                         }
			            		 });
			            	 }
			                  }},
			                  {
						            xtype:'fieldset',
						            title: '执行人上传附件',
						            collapsible: true,
						            collapsed : false,
						            autoHeight:true,
					                hidden:add,
						            items :[],
						             listeners:{
						            	 render:function(p){
						            		 Ext.Ajax.request({
						            			 url:'FileAction',
						                         params:{act:'getFileList_new',taskid:add?"-1":rec.get('id'),fujianField:'file2',tbName:'xingzhengtasks'},
						                         method:'post',
						                         success:function(action,o){
						                            var json = Ext.util.JSON.decode(action.responseText);
						                            if(json.success&&json.r>0) {
						                            	var ja = json.fileList;var len=ja.length;
						                            	for(var i=0;i<len;i++){
						                            		addCompositefield(p,ja[i],false);
						                            	}
						                             	p.doLayout();
						                             }
						                         }
						            		 });
						            	 }
						                  }}]}
			],

	        _doUpload:function(){
	        		var file =  mForm.getForm().findField('upfileName').getValue();
	        		if(file==''){
	        			Ext.Msg.alert('提示','请选择文件再上传!');
	        			return;
	        		}
	                mForm.getForm().submit({
	                    url:'FileAction',
	                    params:{act:'uploadfile'},
	                    method:'post',
	                    success:function(form,action){
	                        var json = Ext.util.JSON.decode(action.response.responseText);
	                        if(json.success && json.r>0 && json.r_save>0 && json.r==json.r) {
	                           //Ext.Msg.alert('上传成功!',json.path);
	                           	//fujian.setValue(fujian.getValue()+","+json.id);
	                          	var field = mForm.findById('fujianField');
	                        	addCompositefield(field,json,true);
	                           	var fujian = mForm.getForm().findField('fujianid');
	                        	//alert(fujian.getValue());
	                        } else {
	                        	Ext.Msg.alert('上传失败!',json.res || '文件类型错误或着超出大小限制，最大可上传100M.');
	                        }
	                    },
	                    failure:function(form,o) {
	                        var json = Ext.decode(o.result);
	                        Ext.Msg.alert('错误',json.res || '服务器错误或表单不完整.');
	                    },
	                    scope:this
	                }); 
	        },
			buttons:[{
				text:'保存并下发',
				hidden: !add,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					mForm.load({
						url:'XingZhengTasks?act=newXingZhengTask',
						waitMsg: "正在保存任务，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(form,action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.alert('提示','保存成功!');
								newTaskGrid.store.reload();
								Mwin.close();
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){
								Ext.Msg.alert('提示','保存成功!');
								newTaskGrid.store.reload();
								Mwin.close();
							}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			},{
				text:'修改',
				hidden: add || editer,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var jieshourenid = rec.get("jieshourenid");
					var jieshourenmc =  mForm.form.getValues().jieshourenid;
					if(jieshourenmc == rec.get("jieshourenmc")){
						mForm.form.findField("jieshourenid").setValue(jieshourenid);
					}
					mForm.form.submit({
						url:'XingZhengTasks?act=updateXingZhengTask',
						waitMsg: "正在修改任务，请稍候...",
						waitTitle : '提示',
						//params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.alert('提示','修改成功!');
								newTaskGrid.store.reload();
								Mwin.close();
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			}]
		});
		if(!add){
			mForm.form.findField("jieshouswjgmc").setValue(rec.get("jieshourenswjgmc"));
			mForm.form.findField("jieshourenid").setValue(rec.get("jieshourenmc"));
		}
		var Mwin = new Ext.Window({
			title : (editer?"查看任务 - 任务已完成不可修改":"新建任务"),
			width : 790,
			height : 400,
			layout : 'form',
			modal : true,
			plain : false,
			resizable : false,
			constrain : false,
			autoScroll : true,
			buttonAlign : 'center',
			items : [ mForm ]
		}).show();
	}
</script>