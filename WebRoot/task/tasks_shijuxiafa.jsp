<%@ page contentType="text/html;charset=utf-8"%>

<div id="Task_ShijuxiafaForm"></div>
<div id="Task_ShijuxiafaGrid"></div>
<script>
	var Task_ShijuxiafaForm = new Ext.form.FormPanel({
		title : '查询条件',
		labelAlign : 'right',
		labelWidth : 90,
		frame : true,
		renderTo : "Task_ShijuxiafaForm",
		bodyStyle : 'padding: 0 0 0 0;min-width:600px;',
		style: 'padding:0 0 0 0',
		width : '100%',
		buttonAlign : "center",
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .50,
				layout : 'form',
				items : [ {
					fieldLabel : '任务包名称',
					xtype : 'textfield',
					//anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'name',
					width : 230
				} ]
			}, {
				columnWidth : .50,
				layout : 'form',
				items : [ {
					xtype : 'compositefield',
					fieldLabel : '下发日期',
					items : [ {
						xtype : 'datefield',
						name : 'date_start',
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
						name : 'date_end',
						width : 105,
						editable : false,
						format : 'Y-m-d'
					} ]
				}/*,{
					xtype : 'hidden',
					name : 'swjgdm'
				},new Ext.form.ComboBoxTree({
					fieldLabel : '所属机关',
					emptyText : '全部',
					//anchor : '98%',
					width: 200,
					mode : 'local',
					displayField : 'text',
					valueField : 'id',
					hiddenName : 'swjgmc',
					name : 'swjgmc',
					JiLian:'Count_XFR',
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
				}) ,{
					 xtype:'combo',
					 id: 'Count_XFR',
					 fieldLabel : '任务下发员',
					 emptyText : '全部',
					 //tpl:'请先选择税务机关',
					 displayField:'username',
					 valueField:'userid',
					 //anchor: '98%',
					 width: 200,
					 mode:'local',
					 triggerAction:'all',
					 hiddenName : 'xfrdm',
					 forceSelection:true,
		             editable: false,
					 store:new Ext.data.JsonStore({
					      root: 'topics',
					      fields: ['userid','username'],
					      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=2&&swjg_dm=21409000000'}),
					      autoLoad: false
				 	 })
					} */ ]
			}]
		} ],
		buttons : [ {
			text : '查询',
			handler : function() {
				if (Task_ShijuxiafaForm.form.isValid()) {
					Task_ShijuxiafaGrid.store.load({
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
				Task_ShijuxiafaForm.getForm().reset();
			}
		} ]
	});

	Task_ShijuxiafaGrid = function(config) {
		var thisGrid = this;
		
		var task_package_fields = ['id', 'name', 'swjgdm', 'swjgmc', 'xfrdm', 'xfrmc',  
		   {
			name : 'xfdate',
			type : 'date',
			dateFormat : 'Y/m/d H:i:s'
			},  'all_count', 'get_count', 'beizhu'];
		var store = new Ext.data.JsonStore ({
				totalProperty : 'totalCount',
				successProperty : 'success',
				idProperty : 'id',
				root : 'topics',
				fields : task_package_fields,
				remoteSort : false,
				proxy : new Ext.data.HttpProxy({
					url : 'TaskPackageServices?action=getTaskPackageList&'+Math.random()
				}),
				listeners : {
					"beforeload" : function(t, o) {
						t.baseParams = config.myForm.form.getValues(false);
					}
				}
		});
		var rn = new Ext.grid.RowNumberer();
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel(
				[ sm,rn,
				  {
					header : "任务包名称",
					dataIndex : "name",
					width : 230,
					editor:new Ext.form.TextField({  
		                allowBlank:false  
		            })
				},{
					header : "下发税务机关",
					dataIndex : "swjgmc",
					width : 280
				},{
					header : "下发人",
					dataIndex : "xfrmc",
					width : 180
				},{
					header : "下发日期",
					dataIndex : "xfdate",
					renderer : renderdate,
					width : 180
				},{
					header : "任务总数",
					dataIndex : "all_count",
					width : 150
				},{
					header : "已接收",
					dataIndex : "get_count",
					width : 150
				} ]);
		var pageBar = new Ext.PagingToolbar({
			pageSize : 15,
			store : store,
			cm:cm,
			displayInfo : true,
			firstText : "首页",
			lastText : "末页",
			nextText : "下一页",
			preText : "上一页",
			refreshText : "刷新",
			displayMsg : '显示 第 {0} - {1}条记录，共 {2} 条',
			emptyMsg : "没有记录"
		});
		var impExl = {text:'新建任务包',
				cls: 'x-btn-text-icon details',
				icon:'extjs/resources/button/tool_xls.gif',
				handler:function(){
					var excelWin = new UpLoadExcelWindow({
						textHidden:false,height: 150, url:'FileAction',flushGrid: thisGrid,actionType:'upLoadTaskPackage',
						TemplatesLink:'<a href="docs/task_shiju.xls" target="_blank">下载EXCEL模版</a>'});
					excelWin.show();
				}
			};
		var delTask = {
			text : '删除任务包',
			cls : 'x-btn-text-icon details',
			icon : 'extjs/resources/button/win_exit.gif',
			handler : function() {del_Task();}
		};
		function del_Task() {
			var recs = Task_ShijuxiafaGrid.getSelectionModel().getSelections();
			var n = recs.length;
			if (n == 0) {
				Ext.MessageBox.alert('提示', '请选择要进行操作的记录！');
				return;
			}
			Ext.Msg.confirm('确认', '是否确认删除' + n + "条任务包?(如果包内有任务已接收,则本包不可删除.)", function(f) {
				if (f != 'yes')
					return;
				var list = [];
				for ( var i = 0; i < recs.length; i++) {
					var rec = recs[i];
					list.push(rec.get('id'));
				}
				Ext.Ajax.request({
					url : 'TaskPackageServices?action=delTaskPackage&idList=' + list,
					params : {},
					success : function(resp) {
						var res = Ext.util.JSON.decode(resp.responseText);
						if (res.r > 0) {
							Ext.MessageBox.alert('提示', "成功删除任务" + res.r + "条.",
									function() {
										Task_ShijuxiafaGrid.store.reload();
									});
						} else {
							Ext.MessageBox.alert('提示', '任务删除失败,原因:' + res.res);
						}
					}
				});
			});
		}
		var cfg = {
			title : '',
			plain : true,
			frame : true,
			autoScroll : true,
			iconCls : 'silk-grid',
			width : '100%',
			height : 386,
			store : store,
			trackMouseOver : true,
			style : 'padding:0 0 0 0;min-width:600px',
			loadMask : true,
			cm : cm,
			sm : sm,
			viewConfig : {
				forceFit : true
			},
			bbar : [ pageBar,impExl, delTask ],
			listeners : {
				'rowdblclick' : function(grid, rowIndex, e) {
					var rec = grid.store.getAt(rowIndex);
					show_taskpackage_detail(rec);
				}
			}
		};
		
		//显示数据包所有任务
		function show_taskpackage_detail(rec){
			var limit = 22;
			var taskpackageForm = new Ext.form.FormPanel({
				//title : '查询条件',
				labelAlign : 'right',
				labelWidth : 90,
				frame : true,
				bodyStyle : 'padding: 0 0 0 0;',
				style: 'padding:0 0 0 0',
				width : '100%',
				buttonAlign : "center",
				items : [ {
					layout : 'column',
					items : [ {
						columnWidth : .33,
						layout : 'form',
			            defaults: {width: 180},
						items : [ {
							fieldLabel : '纳税人名称',
							xtype : 'textfield',
							anchor : '90%',
							forceSelection : true,
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							name : 'nsrmc',
							width : 50
						}, {
							fieldLabel : '纳税人识别号',
							xtype : 'textfield',
							anchor : '90%',
							forceSelection : true,
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							name : 'nsrsbh',
							width : 50
						} ]
					}, {
						columnWidth : .25,
						layout : 'form',
						items : [ /*{
							fieldLabel : '专管员',
							xtype : 'textfield',
							anchor : '90%',
							forceSelection : true,
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							name : 'zgy_mc'
						}*/  {xtype:'hidden',name:'zx_swjg_dm'},
   				     new Ext.form.ComboBoxTree({
							fieldLabel: '执行机关',
							selectNodeModel:'all',
							emptyText: '选择..',
							anchor: '90%',
			                mode: 'local',
						 	displayField:'text',
						 	valueField:'id',
			                hiddenName:'zx_swjg_mc',
				            editable: false,
				            autoLoad: true,
			                tree:  new Ext.tree.TreePanel({
			                    rootVisible: false,
			                    border: false,
			                    method: 'GET',
			                    dataUrl: 'TaskManData?action=treeSwjg',
			                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
			                }),
			                listeners :{
			                	"expand": function(c){
			                		c.list.setWidth('320px'); 
			                        c.innerList.setWidth('auto');
			                	}
			                }
			          }) ,{
							fieldLabel : '执行人',
							xtype : 'textfield',
							anchor : '90%',
							forceSelection : true,
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							name : 'zxr_mc'
						} ]
					}, {
						columnWidth : .42,
						layout : 'form',
						items : [ {
							xtype : 'compositefield',
							fieldLabel : '接收日期',
							items : [ {
								xtype : 'datefield',
								name : 'jieshou_time_start',
								//value : getMonthFirstDay(new Date()),
								width : 105,
								editable : false,
								format : 'Y-m-d'
							}, {
								xtype : 'label',
								text : '至'
							}, {
								xtype : 'datefield',
								fieldLabel : '截至日期',
								//value : getMonthLastDay(new Date()),
								name : 'jieshou_time_end',
								width : 105,
								editable : false,
								format : 'Y-m-d'
							} ]
						}, {
							xtype : 'compositefield',
							fieldLabel : '状态',
							items : [ new Ext.form.Radio({
								boxLabel : '全部',
								name : 'jieshou_status',
								inputValue : -2,
								checked : true
							}), new Ext.form.Radio({
								boxLabel : '已接收',
								name : 'jieshou_status',
								inputValue : -1
							}), new Ext.form.Radio({
								boxLabel : '未接收',
								name : 'jieshou_status',
								inputValue : 0
							})]
						} ]
					} ]
				} ],
		    	buttons : [ {
					text : '查询',
					handler : function() {
						if (taskpackageForm.form.isValid()) {
							TaskpackageDetailGrid.store.load({
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
						taskpackageForm.getForm().reset();
					}
				}]
			});
			var fields_taskpackage = [ {
				name : "id",
				type : "int"
			}, {
				name : "status",
				type : "int"
			}, {
				name : 'begin_time',
				type : 'date',
				dateFormat : 'Y/m/d H:i:s'
			}, {
				name : 'end_time',
				type : 'date',
				dateFormat : 'Y/m/d H:i:s'
			}, {
				name : 'limit_time',
				type : 'date',
				dateFormat : 'Y/m/d H:i:s'
			},{
				name : 'jieshoudate',
				type : 'date',
				dateFormat : 'Y/m/d H:i:s'
			}, 
			"nsrsbh", "nsrmc", "swjg_mc", "zgy_mc", "fxzb", "fxms", "fxydcs",
			"swjg_dm", "zgy_dm", "task_man", 'zxr_dm', 'rwhk', 'zxr_mc',
			'checked','fxzb_dm','zx_swjg_dm','zx_swjg_mc','shijuxiafa','jieshouren', 'jieshourenmc','jieshourenswjg' ];
			var store_taskpackage = new Ext.data.JsonStore ({
				totalProperty : 'totalCount',
				successProperty : 'success',
				idProperty : 'id',
				root : 'topics',
				fields : fields_taskpackage,
				remoteSort : false,
				proxy : new Ext.data.HttpProxy({
					url :  'TasksTempData?action=show&type=4&status=-2&shijuxiafa='+rec.get("id")
				}),listeners : {
					"beforeload" : function(t, o) {
						t.baseParams = taskpackageForm.form.getValues(false);
					}
				}
		});
			var rn_taskpackage = new Ext.grid.RowNumberer();
			var cm_taskpackage = new Ext.grid.ColumnModel(
					[
						rn_taskpackage,
						{
							header : "纳税人识别号",
							dataIndex : "nsrsbh",
							width : 100
						},
						{
							header : "纳税人名称",
							dataIndex : "nsrmc",
							width : 200
						},
						{
							header : "主管税务机关",
							dataIndex : "swjg_mc",
							width : 200
						},
						{
							header : "专管员",
							dataIndex : "zgy_mc",
							width : 60
						},
						{
							header : "风险指标",
							dataIndex : "fxzb",
							width : 150
						},
						{
							header : "风险描述",
							dataIndex : "fxms",
							width : 150
						},
						{
							header : "风险应对建议",
							dataIndex : "fxydcs",
							width : 180
						},{
							header : "执行税务机关",
							dataIndex : "zx_swjg_mc",
							width : 200
						},{
							header : "执行人",
							dataIndex : "zxr_mc",
							width : 60
						},
						{
							header : "任务执行时限",
							dataIndex : "limit_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "任务接收人",
							dataIndex : "jieshourenmc",
							width : 100
						},
						{
							header : "接收时间",
							dataIndex : "jieshoudate",
							renderer : renderdate,
							width : 100
						} ]);
			var pageBar2 = new Ext.PagingToolbar({
				pageSize : limit,
				store : store_taskpackage,
				displayInfo : true,
				firstText : "首页",
				lastText : "末页",
				nextText : "下一页",
				preText : "上一页",
				refreshText : "刷新",
				displayMsg : '显示 第 {0} - {1}条记录，共 {2} 条',
				emptyMsg : "没有记录"
			});
			var TaskpackageDetailGrid = new Ext.grid.GridPanel({
				plain : true,
				frame : true,
				autoScroll : true,
				iconCls : 'silk-grid',
				width : "100%",
				height : 535,
				store : store_taskpackage,
				trackMouseOver : true,
				loadMask : true,
				cm : cm_taskpackage,
				bbar: [ pageBar2 ],
				viewConfig : {
					forceFit : true
				}
			});
			new Ext.Window({
				title : rec.get("name"),
				width : "100%",
				height : 650,
				layout : 'form',
				modal : true,
				plain : false,
				resizable : false,
				constrain : false,
				autoScroll : true,
				buttonAlign : 'center',
				items : [ taskpackageForm,TaskpackageDetailGrid ]
			}).show();
			store_taskpackage.load({params : {start : 0,limit : limit}});
		}
		
		config = Ext.applyIf(config || {}, cfg);
		Task_ShijuxiafaGrid.superclass.constructor.call(this, config);
		store.load({params : {start : 0,limit : 15}});
	};
	Ext.extend(Task_ShijuxiafaGrid, Ext.grid.EditorGridPanel );
	var Task_ShijuxiafaGrid = new Task_ShijuxiafaGrid({ 
		isLead : true,
		url : 'TasksTempData?action=updateTask',
		myForm : Task_ShijuxiafaForm,
		renderTo : 'Task_ShijuxiafaGrid'
	});
	
</script>