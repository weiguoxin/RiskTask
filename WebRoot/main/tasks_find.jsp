
<%@ page contentType="text/html;charset=GBK"%>

<div id="TaskForm_Find"></div>
<div id="TaskGrid_Find"></div>
<script>
var now_add_Find = false;
	var TaskForm_Find = new Ext.form.FormPanel({
		title : '查询条件',
		labelAlign : 'right',
		labelWidth : 90,
		frame : true,
		renderTo : "TaskForm_Find",
		bodyStyle : 'padding: 0 0 0 0;',
		style: 'padding:0 0 0 0;min-width:970px',
		width : '100%',
		buttonAlign : "center",
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .33,
				layout : 'form',
	            defaults: {width: 180},
				items : [ /*{
					fieldLabel : '纳税人名称',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'nsrmc',
					width : 50
				}*/  {
					xtype : 'hidden',
					name : 'imp_swjg_dm'
				}, new Ext.form.ComboBoxTree({
					fieldLabel : '任务下发机关',
					emptyText : '全部',
					anchor : '98%',
					mode : 'local',
					displayField : 'text',
					valueField : 'id',
					hiddenName : 'swjg_mc',
					name : 'swjg_mc',
					JiLian:'Count_RWXiaFaYuan',
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
					 id: 'Count_RWXiaFaYuan',
					 fieldLabel : '任务下发员',
					 emptyText : '全部',
					 //tpl:'请先选择税务机关',
					 displayField:'username',
					 valueField:'userid',
					 anchor: '98%',
					 mode:'local',
					 triggerAction:'all',
					 hiddenName : 'imp_userid',
					 forceSelection:true,
		             editable: false,
					 store:new Ext.data.JsonStore({
					      root: 'topics',
					      fields: ['userid','username'],
					      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=2&&swjg_dm=21409000000'}),
					      autoLoad: false
				 	 })
					},{
						xtype: 'compositefield',
						fieldLabel: '下发级别',
						 anchor: '98%',
						items: [
				            	new Ext.form.Radio({boxLabel:'全部', name:'shijuxiafa', inputValue : -2,checked: true}),
				            	new Ext.form.Radio({boxLabel:'市局下发', name:'shijuxiafa', inputValue : 1}),
				            	new Ext.form.Radio({boxLabel:'本局下发', name:'shijuxiafa', inputValue : 0})
				            	]
		            }]
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
				}*/ {
					fieldLabel : '纳税人识别号',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'nsrsbh',
					width : 50
				}, {
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
					fieldLabel : '下发日期',
					items : [ {
						xtype : 'datefield',
						name : 'begin_time_start',
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
						name : 'begin_time_end',
						width : 105,
						editable : false,
						format : 'Y-m-d'
					} ]
				}, {
					xtype : 'compositefield',
					fieldLabel : '状态',
					items : [ new Ext.form.Radio({
						boxLabel : '全部',
						name : 'status',
						inputValue : -2,
						checked : true
					}), new Ext.form.Radio({
						boxLabel : '未下发',
						name : 'status',
						inputValue : -1
					}), new Ext.form.Radio({
						boxLabel : '执行中',
						name : 'status',
						inputValue : 0
					}), new Ext.form.Radio({
						boxLabel : '已完成',
						name : 'status',
						inputValue : 1
					}) ]
				} ]
			} ]
		},{
            xtype:'fieldset',
            title: '更多选项',
            collapsible: true,
            collapsed : true,
            autoHeight:true,
            items :[{
    			layout : 'column',
    			items : [ {
    				columnWidth : .27,
    				layout : 'form',
    	            defaults: {width: 180},
    	            defaultType: 'textfield',
    				items : [  {xtype:'hidden',name:'fxzb_dm'},
								new Ext.form.ComboBoxTree({
								    name:'fxzb',
									anchor: '90%',
								    //xtype:'checktreepanel',
								    fieldLabel:'风险指标',
								    autoScroll:true,
								    selectNodeModel: 'leaf', 
								 	displayField:'text',
								 	valueField:'id',
								    tree:  new Ext.tree.TreePanel({
								        rootVisible: false,
								        border: false,
								        method: 'GET',
								        dataUrl: 'TaskManData?action=treeQuanXian	',
								    	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
								    }),
								    listeners :{
								    	"expand": function(c){
								    		c.list.setWidth('320px'); 
								            c.innerList.setWidth('auto');
								    	}
								    }
								}),{
				                    fieldLabel: '风险描述',
				                    name: 'fxzb'
				                },{
		                    fieldLabel: '任务回馈',
		                    name: 'rwhk'
		                }
		               ]},{
		    				columnWidth : .36,
		    				layout : 'form',
		    	            defaultType: 'textfield',
		    				items : [{xtype:'hidden',name:'swjg_dm'},
		        				     new Ext.form.ComboBoxTree({
		 								fieldLabel: '主管机关',
		 								selectNodeModel:'all',
		 								emptyText: '选择..',
		 								anchor: '90%',
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
		 				                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
		 				                }),
		 				                listeners :{
		 				                	"expand": function(c){
		 				                		c.list.setWidth('320px'); 
		 				                        c.innerList.setWidth('auto');
		 				                	}
		 				                }
		 				          }),{
		 								xtype : 'compositefield',
		 								fieldLabel : '限定日期',
		 								items : [ {
		 									xtype : 'datefield',
		 									name : 'limit_time_s',
		 									width : 100,
		 									editable : false,
		 									format : 'Y-m-d'
		 								}, {
		 									xtype : 'label',
		 									text : '至'
		 								}, {
		 									xtype : 'datefield',
		 									fieldLabel : '截至日期',
		 									name : 'limit_time_e',
		 									width : 100,
		 									editable : false,
		 									format : 'Y-m-d'
		 								} ]
		 							},{
		 								xtype : 'compositefield',
		 								fieldLabel : '完成日期',
		 								items : [ {
		 									xtype : 'datefield',
		 									name : 'end_time_s',
		 									width : 100,
		 									editable : false,	
		 									format : 'Y-m-d'
		 								}, {
		 									xtype : 'label',
		 									text : '至'
		 								}, {
		 									xtype : 'datefield',
		 									fieldLabel : '截至日期',
		 									width : 100,
		 									name : 'end_time_e',
		 									editable : false,
		 									format : 'Y-m-d'
		 								} ]
		 							}
				               ]},{
				    				columnWidth : .36,
				    				layout : 'form',
				    	            defaultType: 'textfield',
				    				items : [ 
				    				     {xtype:'hidden',name:'zx_swjg_dm'},
    				     new Ext.form.ComboBoxTree({
								fieldLabel: '全部',
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
				          }),{
								xtype : 'compositefield',
								fieldLabel : '审核',
								items : [ new Ext.form.Radio({
									boxLabel : '全部',
									name : 'checked',
									inputValue : -1,
									checked : true
								}), new Ext.form.Radio({
									boxLabel : '已审核',
									name : 'checked',
									inputValue : 1
								}), new Ext.form.Radio({
									boxLabel : '未审核',
									name : 'checked',
									inputValue : 0
								}) ]
							},{
								xtype : 'compositefield',
								fieldLabel : '修改执行人',
								items : [ new Ext.form.Radio({
									boxLabel : '全部',
									name : 'change_zxr',
									inputValue : -1,
									checked : true
								}), new Ext.form.Radio({
									boxLabel : '申请',
									name : 'change_zxr',
									inputValue : 1
								}), new Ext.form.Radio({
									boxLabel : '未申请',
									name : 'change_zxr',
									inputValue : 0
								}) ]
							}
						               ]}
    			]}
            ]
        } ],
		buttons : [ {
			text : '查询',
			handler : function() {
				if (TaskForm_Find.form.isValid()) {
					TaskGrid_Find.store.load({
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
				TaskForm_Find.getForm().reset();
			}
		},{
			text : '导出EXCEL',
			icon:'extjs/resources/button/tool_xls.gif',
			handler : function() {
				if (TaskForm_Find.getForm().isValid()) {
					var param = TaskForm_Find.form.getValues(false);
					param.start=0;
					param.limit=9999999;
					window.open("TasksData?action=show&t=excel&"+encodeURI(obj2str(param))+"&a="+Math.random(),"_blank");
				}
			}
		} ]
	});

	TaskGrid_Find = function(config) {
		var thisGrid = this;
		// 数据源信息
		var fields = [ {
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
		}, "nsrsbh", "nsrmc", "swjg_mc", "zgy_mc", "fxzb", "fxms", "fxydcs",
				"swjg_dm", "zgy_dm", "task_man", 'zxr_dm', 'rwhk', 'zxr_mc',
				'checked','fxzb_dm','zx_swjg_dm','zx_swjg_mc','change_zxr','shijuxiafa','imp_username','imp_swjg_mc'  ];
		var record = Ext.data.Record.create(fields);
		var store = new Ext.data.GroupingStore ({
			reader: new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				successProperty : 'success',
				idProperty : 'id',
				root : 'topics',
				fields : fields
			}),
			remoteSort : true,
			proxy : new Ext.data.HttpProxy({
				url : 'TasksData?action=show&type='
						+ (config.isLead ? '1' : '2')
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
		var cm = new Ext.grid.ColumnModel ({
			 defaults:{   //公共属性配置
		           sortable:true,
		           menuDisabled:true
		        },
			columns:
				[
						sm,
						rn,
						{
							header : "纳税人识别号",
							dataIndex : "nsrsbh",
							width : 100,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "纳税人名称",
							dataIndex : "nsrmc",
							width : 200,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "主管税务机关",
							dataIndex : "swjg_mc",
							width : 180,
							editor:new Ext.form.ComboBoxTree({
								fieldLabel: '所属机关',
								selectNodeModel:'all',
								emptyText: '选择..',
								anchor: '90%',
				                mode: 'local',
							 	displayField:'text',
							 	valueField:'id',
				                hiddenName:'swjg_mc',
				                name:'swjg_mc',
					            editable: false,
					            autoLoad: true,
								allowBlank : false,
								valueName:'swjg_dm',
								myStore: store,
								myGrid: thisGrid,
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
				          })
						},
						{
							header : "专管员",
							dataIndex : "zgy_mc",
							width : 60,
							editor:{
								 xtype:'combo',
								 displayField:'username',
								 valueField:'username',
								 anchor: '90%',
								 mode:'local',
								 triggerAction:'all',
								 emptyText:'请选择...',
								 forceSelection:true,
								 allowBlank : false,
								 blankText:'请先选择税务机关',
								 selectOnFocus:true,
					             editable: false,
								 store:new Ext.data.JsonStore({
								      root: 'topics',
								      fields: ['userid','username'],
								      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=3&&swjg_dm=99999999999'}),
								      autoLoad: false
							 	 }),
							 	 listeners:{
							 		 "select":function(c,r,i){
							 			 var v = r.get("userid");
						 				store.each(function(rr){
			                    			if(rr.get("nowedit")){
			                    				rr.set('zgy_dm',v);
			                    				return false;
			                    			}
			                    		});
							 			 
							 		 }   
							 	 }
								}
						},
						{
							header : "风险指标",
							dataIndex : "fxzb",
							width : 150,
							editor:new Ext.form.ComboBoxTree({
					            name:'fxzb',
								anchor: '90%',
					            //xtype:'checktreepanel',
					            fieldLabel:'风险指标',
								allowBlank : false,
					            autoScroll:true,
					            selectNodeModel: 'leaf', 
							 	displayField:'text',
							 	valueField:'id',
							 	valueName:'fxzb_dm',
								myStore: store,
								myGrid: thisGrid,
					            bodyStyle:'background-color:white;border:1px solid #B5B8C8',
					            tree:  new Ext.tree.TreePanel({
				                    rootVisible: false,
				                    border: false,
				                    method: 'GET',
				                    dataUrl: 'TaskManData?action=treeQuanXian	',
				                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
				                }),
				                listeners :{
				                	"expand": function(c){
				                		c.list.setWidth('320px'); 
				                        c.innerList.setWidth('auto');
				                	}
				                }
					        })
						},
						{
							header : "风险描述",
							dataIndex : "fxms",
							width : 150,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "风险应对建议",
							dataIndex : "fxydcs",
							width : 180,
							editor:new Ext.form.TextField({  
								readOnly: true
				            })
						},{
							header : "执行税务机关",
							dataIndex : "zx_swjg_mc",
							width : 180,
							editor:new Ext.form.ComboBoxTree({
								fieldLabel: '所属机关',
								selectNodeModel:'all',
								emptyText: '选择..',
								anchor: '90%',
				                mode: 'local',
							 	displayField:'text',
							 	valueField:'id',
				                hiddenName:'zx_swjg_mc',
				                name:'zx_swjg_mc',
					            editable: false,
					            autoLoad: true,
								//allowBlank : false,
								valueName:'zx_swjg_dm',
								myStore: store,
								myGrid: thisGrid,
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
				          })
						},{
							header : "执行人",
							dataIndex : "zxr_mc",
							width : 60,
							editor:{
								 xtype:'combo',
								 displayField:'username',
								 valueField:'username',
								 anchor: '90%',
								 mode:'local',
								 triggerAction:'all',
								 emptyText:'请选择...',
								 tooltip:'请先选择税务机关和风险指标',
								 forceSelection:true,
								 selectOnFocus:true,
					             editable: false,
								 store:new Ext.data.JsonStore({
								      root: 'topics',
								      fields: ['userid','username'],
								      proxy: new Ext.data.HttpProxy({
								          url: 'WLUserData?action=show&start=0&limit=9999&type=3&&swjg_dm=99999999999'
								      }),autoLoad:false
							 	 }),
							 	 listeners:{
							 		 "select":function(c,r,i){
							 			 var v = r.get("userid");
						 				store.each(function(rr){
			                    			if(rr.get("nowedit")){
			                    				rr.set('zxr_dm',v);
			                    				return false;
			                    			}
			                    		});
							 		 }   
							 	 }
								}
						},
						{
							header : "任务下发时间",
							dataIndex : "begin_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "任务执行时限",
							dataIndex : "limit_time",
							renderer : renderLast,
							width : 100,
							editor:{
								xtype : 'datefield',
								allowBlank : false,
								value : new Date(),
								editable : false,
								format : 'Y-m-d'
							}
						},
						{
							header : "实际完成时间",
							dataIndex : "end_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "任务执行时长",
							dataIndex : "end_time",
							renderer : function(v, p, r) {
								//计算任务执行时间差
								var b_time = r.get('begin_time');
								var e_time = r.get('end_time');
								if (b_time && e_time) {
									var date3 = e_time.getTime()
											- b_time.getTime(); //时间差的毫秒数
									var days = Math.floor(date3
											/ (24 * 3600 * 1000)); //计算出相差天数
									var leave1 = date3 % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
									var hours = Math.floor(leave1
											/ (3600 * 1000)); //计算出小时数
									var leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
									var minutes = Math.floor(leave2
											/ (60 * 1000)); //计算相差分钟数
									var s = '';
									if (days > 0) {
										s += days + "天";
									}
									if (hours > 0) {
										s += hours + "小时";
									}
									if (minutes > 0) {
										s += minutes + "分钟";
									}
									//alert(s);
									return s;
								}
							},
							width : 100
						},
						{
							header : "任务回馈",
							dataIndex : "rwhk",
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
						},
						{
							header : "审核",
							dataIndex : "checked",
							width : 80,
							renderer : function(v, p, r) {
								if (v == 1)
									return '<span class="file-ok" style="padding-left:20px;">已审核</span>';
								else
									return '<span class="flag-red" style="padding-left:20px;">未审核</span>';
							}
						},
						{
							header : "市局下发",
							dataIndex : "shijuxiafa",
							width : 80,
							renderer : function(v, p, r) {
								if (v == 0)
									return '否';
								else
									return '是';
							}
						},
						{
							header : "任务下发人",
							dataIndex : "imp_username",
							width : 80
						} ,
						{
							header : "下发机关",
							dataIndex : "imp_swjg_mc",
							width : 150
						}]});
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
		var xiafaTask = {
			text : "推送任务",
			icon : 'extjs/resources/button/computer_go.png',
			handler : function() {send_Task();},
			tooltip : "推送任务给执行人",
			scope : this
		};
		var newTask = {
			text : '新建任务',
			cls : 'x-btn-text-icon details',
			icon : 'extjs/resources/button/add.png',
			handler : function() {
            		now_add_Find = true;
					if(!_canEdit())return false;
	                var Plant = store.recordType;
	                var p = new Plant({
	                	id:-1,
	                	nsrsbh:"",nsrmc:"",swjg_mc:"",zgy_mc:"",fxzb:"",fxms:"",
	                	fxydcs:"",swjg_dm:"",zgy_dm:"",task_man:'',zxr_dm:'',
	                	rwhk:'',zxr_mc:'',checked:false,fxzb_dm:'',status:-1
	                });
	                TaskGrid_Find.stopEditing();
	                store.insert(0, p);
	                editor.startEditing(0);
			}
		};
		var updateTask = {
				text : '修改任务',
				cls : 'x-btn-text-icon details',
				icon : 'extjs/resources/button/tool_edit_old.gif',
				handler : function() {
						var rec = TaskGrid_Find.getSelectionModel().getSelected();
						if (rec == null) {
							Ext.MessageBox.alert('提示', '请选择要进行操作的记录！');
							return;
						}
	            		now_add_Find = true;
						if(!_canEdit())return false;
		                TaskGrid_Find.stopEditing();
		                editor.startEditing(store.indexOfId(rec.get("id")));
		                
				}
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
			width : '100%',
			height : 386,
			store : store,
			trackMouseOver : true,
			style : 'padding:0 0 0 0;min-width:2310px',
			loadMask : true,
			cm : cm,
			sm : sm,
			viewConfig : {
				forceFit : true
			},
			bbar : [ pageBar/*, xiafaTask, newTask,updateTask, delTask */],
			listeners : {
				'rowdblclick' : function(grid, rowIndex, e) {
					var rec = grid.store.getAt(rowIndex);
					showWin_Find(rec,grid);
				}
			}
		};
		function del_Task() {
			var recs = TaskGrid_Find.getSelectionModel().getSelections();
			var n = recs.length;
			if (n == 0) {
				Ext.MessageBox.alert('提示', '请选择要进行操作的记录！');
				return;
			}
			Ext.Msg.confirm('确认', '是否确认删除' + n + "条任务?(删除不可恢复)", function(f) {
				if (f != 'yes')
					return;
				var list = [];
				for ( var i = 0; i < recs.length; i++) {
					var rec = recs[i];
					list.push(rec.get('id'));
				}
				Ext.Ajax.request({
					url : 'TasksData?action=del&idList=' + list,
					params : {},
					success : function(resp) {
						var res = Ext.util.JSON.decode(resp.responseText);
						if (res.r > 0) {
							Ext.MessageBox.alert('提示', "成功删除任务" + res.r + "条.",
									function() {
										TaskGrid_Find.store.reload();
									});
						} else {
							Ext.MessageBox.alert('提示', '任务删除失败,原因:' + res.res);
						}
					}
				});
			});
		}
		function send_Task(){
			var recs = TaskGrid_Find.getSelectionModel().getSelections();
			if (recs.length == 0) {
				Ext.MessageBox.alert('提示', '请选择要进行操作的记录！');
			} else {
				Ext.Msg
						.confirm(
								'确认',
								'是否推送任务' + recs.length + "条?",
								function(fn) {//alert(fn);
									if (fn != 'yes') {
										return;
									}
									var list = [];
									for ( var i = 0; i < recs.length; i++) {
										var rec = recs[i];
										list.push(rec.get('id'));
									}
									Ext.Ajax
											.request({
												url : 'TasksData?action=upDateStatus&idList='
														+ list,
												params : {
													statusNew : 0,
													statusOld : -1
												},
												success : function(resp) {
													var res = Ext.util.JSON
															.decode(resp.responseText);
													if (res.r > 0) {
														Ext.MessageBox
																.alert(
																		'提示',
																		"成功推送任务"
																				+ res.r
																				+ "条.",
																		function() {
																			TaskGrid_Find.store
																					.reload();
																		});
													} else {
														Ext.MessageBox
																.alert(
																		'提示',
																		'任务推送失败,原因:'
																				+ res.res);
													}
												}
											});
								});
			}
		}
		config = Ext.applyIf(config || {}, cfg);
		TaskGrid_Find.superclass.constructor.call(this, config);
		store.load({
			params : {
				start : 0,
				limit : 15
			}
		});
	};
	Ext.extend(TaskGrid_Find, Ext.grid.GridPanel);
	function _canEdit(){
		var b = true;
		TaskGrid_Find.store.each(function(r){
			if(r.get("nowedit")){
				b=false;
				return b;
			}
		});
		if(now_add_Find&&b){return true;}
		return false;
	}
	function after_or_cancelEdit(roweditor,changes,record,rowIndex){now_add_Find=false;
		TaskGrid_Find.store.each(function(r){
			if(r.get("nowedit")&&r.get('id')==-1){
				TaskGrid_Find.store.remove(r);
				return false;
			}if(r.get("nowedit")){
				r.set('nowedit',false);
				return false;
			}
		});
	}
	function afteredit(roweditor, changes, record, rowIndex){
		 record.set('nowedit',false);
		 var id = record.get('id');
		 var url = id==-1?"TasksData?action=newTask":"TasksData?action=updateTask";
		 Ext.Ajax.request({ 
		      url   : url, 
		      method: 'POST', 
		      params: record.data, 
		      success: function() { 
		    	  TaskGrid_Find.store.reload();
		    	  Ext.Msg.alert('','操作成功!');
		      },
		      failure:function(){
		    	  TaskGrid_Find.store.remove(record);
		    	  Ext.Msg.alert('','操作失败!');
		      }
		  }); 
	}
	function _beforeedit(roweditor,rowIndex){
		if(!_canEdit())return false;
		var r = TaskGrid_Find.store.getAt(rowIndex);
		r.set('nowedit',true);
		//alert(r.get('id'));
	}
	var editor = new Ext.ux.grid.RowEditor({
        saveText: '保存',
        cancelText: '取消',
        listeners:{
        	afteredit: afteredit,
        	beforeedit: _beforeedit,
        	canceledit: after_or_cancelEdit
        }
    });
	
	/*Grid初始化*/
	var TaskGrid_Find = new TaskGrid_Find({
        plugins: [editor],
		isLead : true,
        clicksToEdit: 2,
        view: new Ext.grid.GroupingView({
            markDirty: false,
            enableGroupingMenu: false,
			getRowClass  : function(record,rowIndex,rowParams,store){
				var status = record.get('status');
				var limit_time = record.get('limit_time');
				var end_time = record.get('end_time');
				if(status < 0 || limit_time == null || end_time==null) return;
				var now_time = new Date();
				if((end_time&&(end_time.getTime() > limit_time.getTime())) || (!end_time&&(now_time.getTime()>limit_time.getTime()))){
					//alert(limit_time + '\n' + end_time + '\n'+ now_time);
					return 'x-grid-record-red';
				}else if(!end_time && (limit_time.getTime() - now_time.getTime() < 1000*60*60*2)){
					return 'x-grid-record-yellow';
				}
			}
        }),
		url : 'TasksData?action=updateTask',
		myForm : TaskForm_Find,
		renderTo : 'TaskGrid_Find'
	});
	
	function showWin_Find(rec,grid) {
		var mForm_Find = new Ext.FormPanel({
			labelAlign: 'right',
			labelWidth: 90,
			frame: true,
			width: '90%',
			//height: 290,
			buttonAlign : "center",
//			reader : new Ext.data.JsonReader({
//				root : "r",
//				success : "success"
//			}, fields),
			items : [{
				layout : 'column',
				items : [ {
					columnWidth : .5,
					layout : 'form',
					items : [{xtype:'hidden',value:rec.get("id"),name:'id'},{
						fieldLabel: '纳税人识别号',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrsbh',
		                value:rec.get("nsrsbh"),
		                width:50  		 
					},{
						fieldLabel: '纳税人名称',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrmc',
		                value:rec.get("nsrmc"),
		                width:50  		 
					},{
						fieldLabel: '主管税务机关',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'swjg_mc',
		                value: rec.get("swjg_mc"),
		                width:50  		 
					},{
						fieldLabel: '专管员',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'zgy_mc',
		                value: rec.get("zgy_mc"),
		                width:50  		 
					},{
						fieldLabel: '执行税务机关',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'swjg_mc',
		                value: rec.get("zx_swjg_mc"),
		                width:50  		 
					},{
						fieldLabel: '执行人',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'swjg_mc',
		                value: rec.get("zxr_mc"),
		                width:50  		 
					},/*{
						 xtype:'combo',
						 id:'com_id_Find',
						 fieldLabel:'执行人',
						 name:'zxr_dm',
						 hiddenName:'zxr_dm',
						 displayField:'username',
						 anchor: '90%',
						 valueField:'id',
						 mode:'local',
						 value: rec.get("zxr_dm")==-1?'':rec.get("zxr_dm"),
						 triggerAction:'all',
						 emptyText:'请选择...',
						 forceSelection:true,
						 selectOnFocus:true,
			             editable: false,
						 store:new Ext.data.JsonStore({
						      root: 'data',
						      fields: ['id','username'],
						      proxy: new Ext.data.HttpProxy({
						          url: 'WLUserData?action=getUserByTaskId&taskId='+rec.get("id")+"&swjg_dm="+rec.get("zx_swjg_dm")
						      }),
						      autoLoad:true,
						      listeners: {
							 		 "load": function(combo) {  
							 			var combo = Ext.getCmp('com_id_Find');
							 	        combo.setValue(combo.getValue());
							 	     } 
						      }
					 	 }),
					 	 listeners:{
					 		 "select":function(c,r,i){
					 			 var v = r.get("id");
					 			 if(v == -1){
					 				 c.setValue(rec.get("zxr_dm"));
					 				 //alert(c.value);
					 			 }
					 		 }   
					 	 }
						},/*{
						fieldLabel: '专管员姓名',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrmc',
		                value: rec.get("zgy_mc"),
		                width:50  		 
					},*/{
						fieldLabel: '风险指标',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'fxzb',
		                value: rec.get("fxzb"),
		                width:50  		 
					},{
						fieldLabel: '风险描述',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'fxms',
		                value: rec.get("fxms"),
		                width:50  		 
					} ]
				}, {
					columnWidth : .5,
					layout : 'form',
					items : [{
						fieldLabel: '计划执行时限',
		                xtype: 'datefield',
						anchor: '90%',
						name:'limit_time',
					    selectOnFocus:true,
						allowBlank : false,
		                editable: false,
		                format: 'Y-m-d',
		                value: rec.get("limit_time"),
		                width:50  		 
					},{
						fieldLabel: '计划执行时间',
		                xtype: 'datefield',
						anchor: '90%',
						name:'begin_time',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                format: 'Y-m-d h:m',
		                value: rec.get("begin_time"),
		                width:50  		 
					},{
						fieldLabel: '完成时间',
		                xtype: 'datefield',
						anchor: '90%',
						name:"end_time",
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                format: 'Y-m-d',
		                value: rec.get("end_time"),
		                width:50  		 
					},{
						fieldLabel: '风险应对建议',
		                xtype: 'textfield',
						anchor: '90%',
						name:"fxydcs",
					    selectOnFocus:true,
		                readOnly: true,
		                value: rec.get("fxydcs"),
		                width:50  		 
					},{
						fieldLabel: '任务回馈',
		                xtype: 'textarea',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'rwhk',
		                value: rec.get("rwhk"),
		                width:50,
		                height: 80
					}/*,{
						xtype: 'compositefield',
						fieldLabel: '状态',
						items: [
				            	new Ext.form.Radio({boxLabel:'未下发', name:'status', inputValue : -1,checked:rec.get("status")==-1}),
				            	new Ext.form.Radio({boxLabel:'待完成', name:'status', inputValue : 0,checked:rec.get("status")==0}),
				            	new Ext.form.Radio({boxLabel:'已完成', name:'status', inputValue : 1,checked:rec.get("status")==1})
				            	]
		            }/*/,{
						fieldLabel: '状态',
		                xtype: 'label',
						anchor: '90%',
						name:'status',
						text : rec.get("status"),
		                width:50,
		                listeners:{
		                	beforerender:function(label){
		                    	var v = this.text;
		                    	var s = '';
		                    	if (null == v)
									s = v;
		                    	if (v == -1){
									this.html = '<span class="file-py" style="padding-left:20px;">未下发</span>';
								}else if (v == 0){
									this.html = '<span class="flag-red" style="padding-left:20px;">待执行</span>';
								}else if (v == 1){
									this.html = '<span class="file-ok" style="padding-left:20px;">已执行</span>';
								}
		                    }
		                 }
					} ]
				} ]
			},{
	            xtype:'fieldset',
	            title: '附件',
	            collapsible: true,
	            collapsed : true,
	            autoHeight:true,
	            items :[],
	             listeners:{
	            	 render:function(p){
	            		 Ext.Ajax.request({
	            			 url:'FileAction',
	                         params:{act:'getFileList',taskid:rec.get("id")},
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
	                  }}]
		});
		var Mwin_Find = new Ext.Window({
			title : rec.get("swjg_mc"),
			width : 690,
			height : 400,
			layout : 'form',
			modal : true,
			plain : false,
			resizable : false,
			constrain : false,
			autoScroll : true,
			buttonAlign : 'center',
			items : [ mForm_Find ]
		}).show();
	}
</script>