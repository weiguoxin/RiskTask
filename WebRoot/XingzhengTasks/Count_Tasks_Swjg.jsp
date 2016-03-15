<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=GBK"%>
<div id="Count_XingZheng_Tasks_FormPanel"></div>
<div id="Count_XingZheng_Tasks_Panel"></div>
<script type="text/javascript" language="javascript">
	Ext.ns('Ext.chart.Chart');
	Ext.chart.Chart.CHART_URL = 'extjs/resources/charts.swf';
	function getPercent(v,c,r){
		if(r.get("a") == 0){return '0%';}
		 return (Math.round(r.get('c') / r.get("a") * 10000) / 100.00 + "%");
	}
	/**
	 *  任务统计
	 */
	Ext.onReady(function() {
		
		var Count_XingZheng_Tasks_Form = new Ext.form.FormPanel({
			title : '查询条件',
			labelAlign : 'right',
			labelWidth : 90,
			frame : true,
			renderTo : "Count_XingZheng_Tasks_FormPanel",
			bodyStyle : 'padding: 0 0 0 0;',
			style : 'padding:0 0 0 0;',
			//width : 1010,
			buttonAlign : "center",
			items : [ {
				layout : 'column',
				items : [ {
					columnWidth : .33,
					layout : 'form',
					items : [ {
						xtype : 'hidden',
						name : 'xiafaswjg'
					}, new Ext.form.ComboBoxTree({
						fieldLabel : '下发机关',
						emptyText : '全部',
						anchor : '98%',
						mode : 'local',
						displayField : 'text',
						valueField : 'id',
						hiddenName : 'swjg_mc',
						name : 'swjg_mc',
						JiLian:'Count_RuXiaFaYuan2',
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
						 id: 'Count_RuXiaFaYuan2',
						 fieldLabel : '下发人',
						 emptyText : '全部',
						 //tpl:'请先选择税务机关',
						 displayField:'username',
						 valueField:'userid',
						 anchor: '98%',
						 mode:'local',
						 triggerAction:'all',
						 hiddenName : 'xiafarenid',
						 forceSelection:true,
			             editable: false,
						 store:new Ext.data.JsonStore({
						      root: 'topics',
						      fields: ['userid','username'],
						      proxy: new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=2&&swjg_dm=21409000000'}),
						      autoLoad: false
					 	 })
						} ]
				}, {
					columnWidth : .29,
					layout : 'form',
					items : [ {
						xtype : 'hidden',
						name : 'jieshouswjg'
					}, new Ext.form.ComboBoxTree({
						fieldLabel : '执行机关',
						emptyText : '全部',
						anchor : '98%',
						mode : 'local',
						displayField : 'text',
						valueField : 'id',
						hiddenName : 'swjg_mc',
						name : 'swjg_mc',
						JiLian:'Count_RuXiaFaYuan1111',
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
						 id: 'Count_RuXiaFaYuan1111',
						 fieldLabel : '执行人',
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
				}, {
					columnWidth : .38,
					layout : 'form',
					items : [ {
						xtype : 'compositefield',
						fieldLabel : '任务日期',
						items : [ {
							xtype : 'datefield',
							name : 'task_date_start',
							editable : false,
							value : getMonthFirstDay(new Date()),
							width : 105,
							format : 'Y-m-d'
						}, {
							xtype : 'label',
							text : '至'
						}, {
							xtype : 'datefield',
							fieldLabel : '截至日期',
							editable : false,
							value : new Date(),
							name : 'task_date_end',
							width : 105,
							format : 'Y-m-d'
						} ]
					} ]
				} ]
			} ],
			buttons : [
					{
						text : '查询',
						icon:'extjs/resources/button/find.gif',
						handler : function() {
							if (Count_XingZheng_Tasks_Form.form.isValid()) {
								Count_XingZheng_Tasks_Store.load({
									params : {
										start : 0,
										limit : 15
									}
								});
							}
						}
					},
					{
						text : '清空查询条件',
						icon:'extjs/resources/button/cancel_find.gif',
						handler : function() {
							Count_XingZheng_Tasks_Form.getForm().reset();
						}
					},{
						text : '导出EXCEL',
						icon:'extjs/resources/button/tool_xls.gif',
						handler : function() {
							if (Count_XingZheng_Tasks_Form.form.isValid()) {
								var param = Count_XingZheng_Tasks_Form.form.getValues(false);
								window.open("XingZhengTasks?act=Count_By_SWJG&t=excel&"+encodeURI(obj2str(param))+"&a="+Math.random(),"_blank");
							}
						}
					} ]
		});
		var Count_XingZheng_Tasks_Store = new Ext.data.JsonStore({
			root : 'topic',
			totalProperty : 'topcount',
			storeId : "id",
			idProperty : 'id',
			successProperty : 'success',
			fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'swjg_dm' ],
			proxy : new Ext.data.HttpProxy({
				url : 'XingZhengTasks?act=Count_By_SWJG&type=' + Math.random()
			}),
			listeners : {
				"beforeload" : function(t, o) {
					var para = Count_XingZheng_Tasks_Form.form.getValues(false);
					t.baseParams = para;
				}
			}
		});

		//var Count_Tasks_csm = new Ext.grid.CheckboxSelectionModel();
		var Count_XingZheng_Tasks_Swjg_PanelCm = new Ext.grid.ColumnModel([
				 {
					header : "序号",
					sortable : true,
					dataIndex : 'id',
					width : 35
				}, {
					header : "税务机关",
					sortable: true,
					dataIndex : 'swjg_mc',
					width : 260
				}, {
					header : "所有任务",
					sortable: true,
					dataIndex : 'a',
					width : 150
				}, {
					header : "未完成任务",
					sortable: true,
					dataIndex : 'b',
					width : 150
				}, {
					header : "已完成任务",
					sortable: true,
					dataIndex : 'c',
					width : 100
				}, {
					header : "完成百分比",
					sortable: true,
					dataIndex : '',
					width : 100,
					renderer: getPercent
				},
					{
							header : "图形统计",
			                xtype: 'actioncolumn',
			                width: 60,
			                items: [
								{
								    icon   : 'extjs/resources/button/chart_bar.png',
							        iconCls:'chart',
								    tooltip: "显示柱状图",
								    handler: function(grid, rowIndex, colIndex) {
								    	var rec = grid.store.getAt(rowIndex);
								    	showChartXingZheng(rec);
								    }
								} ]},{
							header : "人员详细",
			                xtype: 'actioncolumn',
			                width: 60,
			                items: [
								{
								    icon   : 'extjs/resources/button/user_suit.png',
								    tooltip: "人员完成明细",
								    handler: function(grid, rowIndex, colIndex) {
								    	//查询任务分类详情
								    	var rec = grid.store.getAt(rowIndex);
								    	showPersenXingZheng(rec);
								    }
								} ]} ]);
		//主要grid
		var Count_XingZheng_Tasks_PanelGrid = new Ext.grid.GridPanel({
			style : 'padding:0 0 0 0',
			width : '100%',
			height : 365,
			store : Count_XingZheng_Tasks_Store,
			trackMouseOver : true,
			loadMask : true,
			cm : Count_XingZheng_Tasks_Swjg_PanelCm,
			viewConfig : {
				forceFit : false
			},
		    listeners:{
		      	'rowdblclick' : function(grid, rowIndex, e){
		        	var rec = grid.store.getAt(rowIndex);
		        	showNextSwjg_XingZheng(rec);
		          }
		    }
		});

		//显示下级机构统计
		function showNextSwjg_XingZheng(rec){
			var swjg_dm = rec.get('swjg_dm');
			if(-1 == swjg_dm){
				return;
			}
			var nextStore = new Ext.data.JsonStore({
				root : 'topic',
				storeId : "id",
				idProperty : 'id',
				successProperty : 'success',
				fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'swjg_dm' ],
				proxy : new Ext.data.HttpProxy({
					url : 'XingZhengTasks?act=Count_By_SWJG&type=nextSwjg&' + Math.random()
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_XingZheng_Tasks_Form.form.getValues(false);
						para.jieshouswjg = rec.get('swjg_dm');
						t.baseParams = para;
					}
				}
			});
			var nextGrid = new Ext.grid.GridPanel({
				style : 'padding:0 0 0 0',
				width : '100%',
				height : 365,
				store : nextStore,
				trackMouseOver : true,
				loadMask : true,
				cm : Count_XingZheng_Tasks_Swjg_PanelCm,
				viewConfig : {
					forceFit : false
				}
			});
			new Ext.Window({
				title:rec.get('swjg_mc'),
				width:1000,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				closeAction:'hide',
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[nextGrid]
			}).show();
			nextStore.load({params : {start : 0,limit : 9999}});
		}
		//显示图表
		function showChartXingZheng(rec){
			var swjg_dm = rec.get('swjg_dm');
			var char_Store = new Ext.data.JsonStore({
				root : 'topic',
				storeId : "id",
				autoLoad: true,
				fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'd', 'e','f', 'swjg_dm'],
				proxy : new Ext.data.HttpProxy({
					url : 'XingZhengTasks?act=Count_By_SWJG&type=nextSwjg&jieshouswjg=' + swjg_dm
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_XingZheng_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var chart = {
		            xtype: 'stackedbarchart',
		            store: char_Store,
		            yField: 'swjg_mc',
		            xAxis: new Ext.chart.NumericAxis({
		                stackingEnabled: true,
		                majorUnit:100 ,
		                labelRenderer: Ext.util.Format.numberRenderer('0')
		            }),
		            series: [{
		                xField: 'b',
		                displayName: '已完成'
		            },{
		                xField: 'c',
		                displayName: '未完成'
		            }],
		            chartStyle: {
		                padding: 10,
		                animationEnabled: true,
		                font: {
		                    name: 'Tahoma',
		                    color: 0x444444,
		                    size: 11
		                },
		                dataTip: {
		                    padding: 5,
		                    border: {
		                        color: 0x99bbe8,
		                        size:1
		                    },
		                    background: {
		                        color: 0xDAE7F6,
		                        alpha: .9
		                    },
		                    font: {
		                        name: 'Tahoma',
		                        color: 0x15428B,
		                        size: 12,
		                        bold: true
		                    }
		                }
		            }
		        };
			new Ext.Window({
		        iconCls:'chart',
				title:rec.get('swjg_mc'),
				width:800,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[chart]
			}).show();
		}
		function showTaskTypeDetailXingZheng(rec){
			var swjg_dm = rec.get('swjg_dm');
			var TypeDetail_Store = new Ext.data.JsonStore({
				root : 'topic',
				totalProperty : 'topcount',
				storeId : "id",
				idProperty : 'id',
				successProperty : 'success',
				autoLoad: true,
				fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'd', 'e', 'f', 'swjg_dm'],
				proxy : new Ext.data.HttpProxy({
					url : 'TasksData?action=Count_By_Fxzb_Dm&sw_dm=' + swjg_dm
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_XingZheng_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var TypeDetailCm = new Ext.grid.ColumnModel([
             				 {
             					header : "序号",
             					sortable : true,
             					dataIndex : 'id',
             					width : 35
             				}, {
             					header : "机关名称",
             					sortable: true,
             					dataIndex : 'swjg_mc',
             					width : 220
             				}, {
             					header : "登记类",
             					sortable: true,
             					dataIndex : 'a',
             					width : 80
             				}, {
             					header : "申报类",
             					sortable: true,
             					dataIndex : 'b',
             					width : 80
             				}, {
             					header : "管理类",
             					sortable: true,
             					dataIndex : 'c',
             					width : 80
             				}, {
             					header : "发票类",
             					sortable: true,
             					dataIndex : 'd',
             					width : 80
             				}, {
             					header : "财务类",
             					sortable: true,
             					dataIndex : 'e',
             					width : 80
             				}, {
             					header : "征收类",
             					sortable: true,
             					dataIndex : 'f',
             					width : 80
             				} ]);
			new Ext.Window({
				title:rec.get('swjg_mc'),
				width:750,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[new Ext.grid.GridPanel({
					style : 'padding:0 0 0 0',
					width : '100%',
					height : 365,
					store : TypeDetail_Store,
					trackMouseOver : true,
					loadMask : true,
					cm : TypeDetailCm,
					viewConfig : {
						forceFit : false
					}
				})]
			}).show();
		}
		function showPersenXingZheng(rec){
			var swjg_dm = rec.get('swjg_dm');
			if(-1 == swjg_dm){
				return;
			}
			var person_Store = new Ext.data.JsonStore({
				root : 'topic',
				totalProperty : 'topcount',
				storeId : "id",
				idProperty : 'id',
				successProperty : 'success',
				autoLoad: true,
				fields : [ 'id', 'zxr_dm', 'a', 'b', 'c', 'username' ],
				proxy : new Ext.data.HttpProxy({
					url : 'XingZhengTasks?act=Count_By_SWJG_DM&jieshouswjg=' + rec.get('swjg_dm')
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_XingZheng_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var personCm = new Ext.grid.ColumnModel([
             				 {
             					header : "序号",
             					sortable : true,
             					dataIndex : 'id',
             					width : 35
             				}, {
             					header : "执行人姓名",
             					sortable: true,
             					dataIndex : 'username',
             					width : 130
             				}, {
             					header : "所有任务",
             					sortable: true,
             					dataIndex : 'a',
             					width : 100
             				}, {
             					header : "未完成任务",
             					sortable: true,
             					dataIndex : 'b',
             					width : 100
             				}, {
             					header : "已完成任务",
             					sortable: true,
             					dataIndex : 'c',
             					width : 100
             				}, {
             					header : "完成百分比",
             					sortable: true,
             					dataIndex : '',
             					width : 97,
             					renderer: getPercent
             				} ]);
			new Ext.Window({
				title:rec.get('swjg_mc'),
				width:600,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[new Ext.grid.GridPanel({
					style : 'padding:0 0 0 0',
					width : '97%',
					height : 368,
					store : person_Store,
					trackMouseOver : true,
					loadMask : true,
					cm : personCm,
					viewConfig : {
						forceFit : false
					},
					bbar:['->',{    
				        text:'EXCEl',    
						icon:'extjs/resources/button/tool_xls.gif',
				        handler:function(){ 
				        	var count = person_Store.getCount();
				        	if(count <= 0){
				        		Ext.Msg.alert("提示","无数据.");
				        		return;
				        	}
				        	window.open("XingZhengTasks?act=Count_By_SWJG_DM&type=excel&sw_dm=" + rec.get('swjg_dm')+"&"+Ext.urlEncode(Count_XingZheng_Tasks_Form.form.getValues(false)),'_blank');
					}},'->']
				})]
			}).show();
		}
		Count_XingZheng_Tasks_PanelGrid.render('Count_XingZheng_Tasks_Panel');
	});
</script>
