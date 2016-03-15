<%@ page contentType="text/html;charset=GBK"%>

<div id="task_zuzhang_Form"></div>
<div id="sh_zuzhangGrid" ></div>
<script>
var task_zuzhang_Form = new Ext.form.FormPanel({
	title: '查询条件',
	labelAlign: 'right',
	labelWidth: 90,
	frame: true,
	renderTo: "task_zuzhang_Form",
	bodyStyle: 'padding: 0 0 0 0;',
	style: 'padding:0 0 0 0;min-width:800px',
	width: '100%',
	buttonAlign : "center",
	items: [{
		layout:'column',
		items: [{
			columnWidth:.5,
            layout: 'form',
			items:[{
				fieldLabel: '纳税人名称',
                xtype: 'textfield',
				anchor: '70%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrmc',
                width:50  		 
			},{
				fieldLabel: '纳税人识别号',
                xtype: 'textfield',
				anchor: '70%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrsbh',
                width:50  		 
			}]},{
				columnWidth:.5,
                layout: 'form',
				items:[
				       	{
							xtype: 'compositefield',
							fieldLabel: '任务日期',
							items: [{
			                	  xtype     : 'datefield',
							        name: 'begin_time_start',
							        value:  getMonthFirstDay(new Date()),
							        width:105,
					                editable: false,
							        format:'Y-m-d'
								},{xtype: 'label',text:'至'},{
								    xtype: 'datefield',
								    fieldLabel: '截至日期',
							        value: getMonthLastDay(new Date()),
							        name: 'begin_time_end',
							        width:105,
					                editable: false,
							        format:'Y-m-d'
							}
								]},{
									xtype: 'compositefield',
									fieldLabel: '状态',
									items: [
							            	new Ext.form.Radio({boxLabel:'全部', name:'status', inputValue : -2,checked: true}),
							            	new Ext.form.Radio({boxLabel:'已完成', name:'status', inputValue : 1}),
							            	new Ext.form.Radio({boxLabel:'待完成', name:'status', inputValue : 0})
							            	]
					            }
								]}]
	}],
	buttons:[{
		text:'查询',
		handler : function(){
		if(task_zuzhang_Form.form.isValid()){
			sh_zuzhangGrid.store.load({params:{start:0,limit:15}});
		}	
	}
	},
	{    
        text:'清空查询条件',    
        handler:function(){ 
        	task_zuzhang_Form.getForm().reset();
	}}]
});  
FileGrid = function(config) {
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
	}, "nsrsbh", "nsrmc", "swjg_mc", "zgy_mc", "fxzb", "fxms", 
			"fxydcs", "swjg_dm", "zgy_dm", "task_man",'zxr_dm','rwhk','zxr_mc','checked','change_zxr','zx_swjg_dm' ];
	var record = Ext.data.Record.create(fields);
	var store = new Ext.data.JsonStore({
		root : 'topics',
		totalProperty : 'totalCount',
		successProperty : 'success',
		idProperty : 'id',
		remoteSort : false,
		fields : fields,
		proxy : new Ext.data.HttpProxy({
			url : 'TasksData?action=show&type=3'
		}),
		listeners : {
			"beforeload" : function(t, o) {
				t.baseParams = config.myForm.form.getValues(false);
			}
		}
	});
	var rn = new Ext.grid.RowNumberer();
	var c_status = {
		header : "状态",
		dataIndex : "status",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 0)
				return '<span class="file-py" style="padding-left:20px;">待执行</span>';
			else if (v == 1)
				return '<span class="file-turn" style="padding-left:20px;">已执行</span>';
			else
				return '';
		}
	};
	var c_taskstatus = {
		header : "紧急程度",
		dataIndex : "taskstatus",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 0)
				return '<span class="flag-green" style="padding-left:20px;">普通</span>';
			else if (v == 1)
				return '<span class="flag-red" style="padding-left:20px;">紧急</span>';
			else
				return '';
		}
	};
	var c_isok = {
		header : "执行结果",
		dataIndex : "isok",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 1) {
				return '<span class="file-ok" style="padding-left:20px;">已执行</span>';
			} else {
				if (v == 2) {
					return '<span class="file-no" style="padding-left:20px;">已撤销</span>';
				} else
					return '';
			}
		}
	};
	var c_aclm = {
		xtype : 'actioncolumn',
		header : "查看任务",
		width : 20,
		items : [ {
			getClass : function(v, meta, rec){ 
				this.items[0].tooltip = '查看任务';
				return 'file_show';
			},
			handler : function(grid, rowIndex, colIndex) {
				var rec = store.getAt(rowIndex);
				var href = config.isDSP ? 'main/dsp_edit_file.jsp'
						: 'main/ypy_edit_file.jsp';
				var title = config.isDSP ? '批阅文件' : '查看文件';
				var docid = config.isDSP ? 'docs-py' : 'docs-show';
				GRDS.editFileTab(docid, rec.get('id'), title, href + '?random='
						+ Math.random() + '&fileid=' + rec.get('id'));
			}
		}
		]
	};
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel(
			[
			 		sm,
					rn,
					{
						header : "纳税人识别号",
						dataIndex : "nsrsbh",
						width : 100
					},
					{
						header : "纳税人名称",
						dataIndex : "nsrmc",
						width : 130
					},
					{
						header : "主管税务机关",
						dataIndex : "swjg_mc",
						width : 130
					},
					{
						header : "专管员",
						dataIndex : "zgy_mc",
						width : 60
					},
					{
						header : "执行人",
						dataIndex : "zxr_mc",
						width : 60
					},
					{
						header : "风险指标",
						dataIndex : "fxzb",
						width : 100
					},
					{
						header : "风险描述",
						dataIndex : "fxms",
						width : 100
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
						header : "任务执行时限",
						dataIndex : "limit_time",
						renderer : renderLast,
						width : 100
					},
					{
						header : "风险应对建议",
						dataIndex : "fxydcs",
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
							else if (v == -1){
								return '<span class="file-py" style="padding-left:20px;">未下发</span>';
							}else if (v == 0){
								return '<span class="flag-red" style="padding-left:20px;">待执行</span>';
							}else if (v == 1)
								return '<span class="file-ok" style="padding-left:20px;">已执行</span>';
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
					} ]);
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
	var tbCfg = [pageBar];
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
		style: 'padding:0 0 0 0;min-width:2000px;',
		loadMask : true,
		cm : cm,
        sm: sm,
		viewConfig : {
			forceFit : true
		},
		bbar : tbCfg,
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				var rec = grid.store.getAt(rowIndex);
				showWin(rec,grid);
			}
		}
	};
	function showWin(rec,grid) {
		var mForm = new Ext.FormPanel({
			labelAlign: 'right',
			labelWidth: 90,
			frame: true,
			width: '100%',
			height: 240,
			buttonAlign : "center",
			items : [{
				layout : 'column',
				items : [ {
					columnWidth : .5,
					layout : 'form',
					items : [{xtype:'hidden',value:rec.get("id"),name:'id'},
					         {xtype:'hidden',value:rec.get("zx_swjg_dm"),name:'zx_swjg_dm'},{
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
						 xtype:'combo',
						 id:'com_id',
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
						          url: 'WLUserData?action=getUserByTaskId&taskId='+rec.get("id")+"&swjg_dm="+rec.get("swjg_dm")
						      }),
						      autoLoad:true,
						      listeners: {
							 		 "load": function(combo) {  
							 			var combo = Ext.getCmp('com_id');
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
						},{
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
		                readOnly: !config.isLead,
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
		                width:50  		 
					},{
						xtype: 'compositefield',
						fieldLabel: '状态',
						items: [
				            	new Ext.form.Radio({boxLabel:'待完成', name:'status', inputValue : 0,checked:rec.get("status")==0}),
				            	new Ext.form.Radio({boxLabel:'已完成', name:'status', inputValue : 1,checked:rec.get("status")==1})
				            	]
		            }]
				} ]
			}],
			buttons:[{
				text:rec.get("change_zxr")==0?'申请修改执行人':'正在申请中',
				hidden: rec.get("status")==0?false:true,
				disabled:rec.get("change_zxr")==0?false:true,
				icon:'extjs/resources/button/user_suit.png',
				handler : function(){
					if(!mForm.form.isValid())return;
					mForm.form.submit({
						url:'TasksData?action=change_zxr',
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.alert('提示','保存成功!');
								sh_zuzhangGrid.store.reload();
								Mwin.close();
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			},{
				text:'确认修改执行人',
				hidden: rec.get("status")==0?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var zxr_dm = mForm.form.getValues().zxr_dm;
					if('' == zxr_dm || -1 == zxr_dm){
						Ext.Msg.alert('','请选择执行人!');return;
					}
					mForm.form.submit({
						url:'TasksData?action=updateTask',
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.alert('提示','保存成功!');
								sh_zuzhangGrid.store.reload();
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
		var Mwin = new Ext.Window({
			title : rec.get("swjg_mc"),
			width : 600,
			height : 272,
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

	config = Ext.applyIf(config || {}, cfg);

	// call父类构建器
	FileGrid.superclass.constructor.call(this, config);
	store.load({
		params : {
			start : 0,
			limit : 15
		}
	});
};
Ext.extend(FileGrid, Ext.grid.GridPanel);

var sh_zuzhangGrid = new FileGrid({
	isLead:false,
	url:'TasksData?action=updateTask_finish',
	myForm:task_zuzhang_Form,
	renderTo:'sh_zuzhangGrid'
});
</script>