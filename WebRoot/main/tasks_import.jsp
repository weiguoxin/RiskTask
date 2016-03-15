
<%@ page contentType="text/html;charset=utf-8"%>

<div id="importTaskForm"></div>
<div id="importTaskGrid"></div>
<script>
	var importTaskForm = new Ext.form.FormPanel({
		title : '查询条件',
		labelAlign : 'right',
		labelWidth : 90,
		frame : true,
		renderTo : "importTaskForm",
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
				items : [ {
					fieldLabel : '专管员',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'zgy_mc'
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
					fieldLabel : '任务日期',
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
						boxLabel : '待转任务',
						name : 'status',
						inputValue : -1,
						checked : true
					}), new Ext.form.Radio({
						boxLabel : '已转任务',
						name : 'status',
						inputValue : 0
					}), new Ext.form.Radio({
						boxLabel : '全部',
						name : 'status',
						inputValue : -2
					}) ]
				} ]
			} ]
		} ],
		buttons : [ {
			text : '查询',
			handler : function() {
				if (importTaskForm.form.isValid()) {
					importTaskGrid.store.load({
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
				importTaskForm.getForm().reset();
			}
		} ]
	});

	ImportTaskGrid = function(config) {
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
				'checked','fxzb_dm','zx_swjg_dm','zx_swjg_mc','shijuxiafa','imp_username','imp_swjg_mc' ];
		var record = Ext.data.Record.create(fields);
		var store = new Ext.data.JsonStore ({
				totalProperty : 'totalCount',
				successProperty : 'success',
				idProperty : 'id',
				root : 'topics',
				fields : fields,
				remoteSort : false,
				proxy : new Ext.data.HttpProxy({
					url : 'TasksTempData?action=show&type=4'
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
								allowBlank : false,
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
							header : "任务执行时间",
							dataIndex : "begin_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "完成时间",
							dataIndex : "end_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "任务执行时长",
							dataIndex : "end_time",
							renderer : function(v, p, r) {//计算任务执行时间差
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
							width : 80
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
		var impExl = {text:"任务导入",cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_xls.gif',handler:function(){
			//var upLoadFileWin = new UpLoadFileWindow();//文件上传并转为swf
		    var excelWin = new UpLoadExcelWindow({flushGrid:importTaskGrid});excelWin.show();}};
		var impShijuTask = {text:"接收上级任务",icon:'extjs/resources/button/folder_go.png',cls: 'x-btn-text-icon details',handler:function(){
			var task_package_fields = ['id', 'name', 'swjgdm', 'swjgmc', 'xfrdm', 'xfrmc', 'xfdate',  'hasTasks', 'get_count', 'beizhu'];
        		var store_taskpackage = new Ext.data.JsonStore ({
        				totalProperty : 'totalCount',
        				successProperty : 'success',
        				idProperty : 'id',
        				root : 'topics',
        				fields : task_package_fields,
        				remoteSort : false,
        				proxy : new Ext.data.HttpProxy({
        					url : 'TaskPackageServices?action=getTaskPackageListBySwjg&'+Math.random()
        				}),
        				listeners : {
        					"beforeload" : function(t, o) {
        						t.baseParams = config.myForm.form.getValues(false);
        					}
        				}
        		});
        		var rn = new Ext.grid.RowNumberer();
        		var cm_taskpackage = new Ext.grid.ColumnModel(
        				[ rn,
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
        					width : 180
        				},{
        					header : "可接任务数量",
        					dataIndex : "hasTasks",
        					align:'center',
        					width : 120
        				}]);
        		var TaskpackageDetailGrid = new Ext.grid.GridPanel({
    				plain : true,
    				frame : true,
    				autoScroll : true,
    				iconCls : 'silk-grid',
    				width : '100%',
    				height : 368,
    				store : store_taskpackage,
    				trackMouseOver : true,
    				loadMask : true,
    				cm : cm_taskpackage,
    				viewConfig : {
    					forceFit : true
    				},
    				bbar : [ {text:"全部接收",cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_xls.gif',
    					handler:function(){
    						Ext.Msg.confirm("","全部接收后,任务将直接进入[任务下发]中,是否全部接收?",function(flag){
    							if('yes'==flag){
    								Ext.Ajax.request({
    									url:'TaskPackageServices?action=reveiveTaskPackageAll&'+Math.random(),
    									method:'post',
    									params:{},
    									success:function(response,option){Ext.MessageBox.alert('成功', '接收到 ' + (Ext.util.JSON.decode(response.responseText)).r_tasks+"条任务并转入[任务下发],请进入[任务下发]推送任务.");store_taskpackage.load({params : {start : 0,limit : 99999}});},
    									falure:function(response,options){Ext.MessageBox.alert('失败', '请求超时或网络故障,错误代码：' + response.status);}
    								});
    							}
    						});
    					}} ]
    			});
    			new Ext.Window({
    				title : '导入市局下发任务',
    				width : 750,
    				height : 400,
    				layout : 'form',
    				modal : true,
    				plain : false,
    				resizable : false,
    				constrain : false,
    				autoScroll : true,
    				buttonAlign : 'center',
    				items : [ TaskpackageDetailGrid ]
    			}).show();
    			store_taskpackage.load({params : {start : 0,limit : 99999}});
		    }};
		var newTask = {
			text : '新建任务',
			cls : 'x-btn-text-icon details',
			icon : 'extjs/resources/button/add.png',
			handler : function() {
	                var Plant = store.recordType;
	                var p = new Plant({
	                	id:-1,
	                	nsrsbh:"",nsrmc:"",swjg_mc:"",zgy_mc:"",fxzb:"",fxms:"",
	                	fxydcs:"",swjg_dm:"",zgy_dm:"",task_man:'',zxr_dm:'',
	                	rwhk:'',zxr_mc:'',checked:false,fxzb_dm:'',status:-1
	                });
	                importTaskGrid.stopEditing();
	                store.insert(0, p);
	                importTaskGrid.startEditing(0,0);
	                //editor.startEditing(0);
			}
		};
		var updateTask = {
				text : '转任务下发',
				cls : 'x-btn-text-icon details',
				icon : 'extjs/resources/button/tool_edit_old.gif',
				handler : function() {
					Ext.Msg.confirm('提示','将可执行任务转到[任务下发]?',function(fn){
						if('yes'==fn){
							Ext.Ajax.request({
				                url: 'TasksTempData?action=turnTasks',
				                method: 'post',
				                success: function (response, options) {
				                	var j = Ext.util.JSON.decode(response.responseText);
				                	var insert_r = j.insert_r;
				                	var update_r = j.update_r;
				                	if(update_r == insert_r && insert_r > 0){
				                		Ext.MessageBox.alert('提示','转入成功' + insert_r + "条任务.");
				                		importTaskGrid.store.reload();
				                	}else{
				                		Ext.MessageBox.alert('提示','未找到可以转入执行的任务,请检查任务数据是否完整.');
				                	}
				                },
				                failure: function (response, options) {
				                    Ext.MessageBox.alert('失败', '请求超时或网络故障');
				                }
				            });
						}
					});
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
			width : 2000,
			height : 386,
			store : store,
			trackMouseOver : true,
			style : 'padding:0 0 0 0;min-width:2000px',
			loadMask : true,
			cm : cm,
			sm : sm,
			viewConfig : {
				forceFit : true
			},
			bbar : [ pageBar,impExl, impShijuTask,delTask,updateTask ],
			listeners : {
				'rowdblclick' : function(grid, rowIndex, e) {
					var rec = grid.store.getAt(rowIndex);
					//showWin(rec,grid);
				},
				beforeedit:function(e){
					var r = e.record;
					r.set('nowedit',true);
					var column = e.column;
					if(10 == column && r.get('zx_swjg_dm')&&r.get('fxzb_dm')){
						var editor = e.grid.getColumnModel().getCellEditor(10,0).field;
						var store = editor.getStore();
						store.proxy = new Ext.data.HttpProxy({url:'WLUserData?action=show&start=0&limit=9999&type=3'});
						store.baseParams = {swjg_dm:r.get('zx_swjg_dm'),fxzb_dm:r.get('fxzb_dm')};
						editor.clearValue();
						store.load();
					}else if(5 == column && r.get('swjg_dm')){
						var editor = e.grid.getColumnModel().getCellEditor(5,0).field;
						var store = editor.getStore();
						store.proxy = new Ext.data.HttpProxy({url:'WLUserData?action=show&start=0&limit=9999&type=3'});
						store.baseParams = {swjg_dm:r.get('swjg_dm')};
						editor.clearValue();
						store.load();
					}
				},
				"afteredit":function(e){
		    		var g = e.grid;
		    		var r = e.record;
		    		var f = e.field;
		    		var v = e.value;
					r.set('nowedit',false);
		    		if(0 == r.get("id") || -1 == r.get("id")){
		    			
		    		}else{
			    		Ext.Ajax.request({
			                url: 'TasksTempData?action=updateTask',
			                params: r.data,
			                method: 'post',
			                success: function (response, options) {
			                	var j = Ext.util.JSON.decode(response.responseText);
			                	if(j.r > 0){
			                		//Ext.MessageBox.alert('提示','修改成功!');
			                		g.store.reload();
			                	}else{
			                		Ext.MessageBox.alert('提示','错误:' + j.res + '</br>请重新登录尝试.');
			                	}
			                },
			                failure: function (response, options) {
			                    Ext.MessageBox.alert('失败', '请求超时或网络故障');
			                }
			            });
		    		}
		    	}
			}
		};
		function del_Task() {
			var recs = importTaskGrid.getSelectionModel().getSelections();
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
					url : 'TasksTempData?action=del&idList=' + list,
					params : {},
					success : function(resp) {
						var res = Ext.util.JSON.decode(resp.responseText);
						if (res.r > 0) {
							Ext.MessageBox.alert('提示', "成功删除任务" + res.r + "条.",
									function() {
										importTaskGrid.store.reload();
									});
						} else {
							Ext.MessageBox.alert('提示', '任务删除失败,原因:' + res.res);
						}
					}
				});
			});
		}
		function send_Task(){
			var recs = importTaskGrid.getSelectionModel().getSelections();
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
												url : 'TasksTempData?action=upDateStatus&idList='
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
																			importTaskGrid.store
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
		ImportTaskGrid.superclass.constructor.call(this, config);
		store.load({
			params : {
				start : 0,
				limit : 15
			}
		});
	};
	Ext.extend(ImportTaskGrid, Ext.grid.EditorGridPanel );
	var importTaskGrid = new ImportTaskGrid({
		isLead : true,
		url : 'TasksTempData?action=updateTask',
		myForm : importTaskForm,
		renderTo : 'importTaskGrid'
	});
	
</script>