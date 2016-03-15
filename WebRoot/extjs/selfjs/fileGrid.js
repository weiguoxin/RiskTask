// 综合应用
FileGrid = function(config) {
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
	}, "nsrsbh", "nsrmc", "swjg_mc", "zgy_mc", "fxzb", "fxms", 
			"fxydcs", "swjg_dm", "zgy_dm", "task_man",'zxr_dm','rwhk','zxr_mc' ];
	var record = Ext.data.Record.create(fields);
	var store = new Ext.data.JsonStore({
		root : 'topics',
		totalProperty : 'totalCount',
		successProperty : 'success',
		idProperty : 'id',
		remoteSort : false,
		fields : fields,
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
	// 定义列
	// id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time,
	// limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man

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
			getClass : function(v, meta, rec) { // Or return a class from a
												// function
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
	// id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time,
	// limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man
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
					} ]);
	var pageBar = new Ext.PagingToolbar({
		/*
		 * 设置每次显示的记录条数 '-' 代表分符
		 * pressed:表示按钮干在开始的时候是否被按下，只有enableToggle为真的时候才有用
		 * enableToggle：表示按钮在开始的时候是否能处于被按下的状态，允许在按下没按下之间切换，添加移除x-btn-pressed样式类
		 * text:按钮干上显示的文字 cls：按钮的CSS类
		 * toggleHandler:设置enableToggle为TRUE时点击按钮时的事件处理函数
		 */
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
		icon:'extjs/resources/button/computer_go.png',
		handler : function(){
			var recs = shgrid.getSelectionModel().getSelections();
			if(recs.length == 0){
				Ext.MessageBox.alert('提示','请选择要进行操作的记录！');
			}else{
				Ext.Msg.confirm('确认','是否推送任务' + recs.length +"条?", function(fn){//alert(fn);
					if(fn != 'yes'){
						return;
					}
					var list = [];
					for(var i = 0 ; i < recs.length ; i++){
						var rec = recs[i];
						list.push(rec.get('id'));
					}
					Ext.Ajax.request({
						url:'TasksData?action=upDateStatus&idList='+list,
						params:{statusNew:0,statusOld:-1},
						success:function(resp){
							var res =Ext.util.JSON.decode(resp.responseText);
							if(res.r > 0){
								Ext.MessageBox.alert('提示',"成功推送任务"+res.r+"条.",function(){
										shgrid.store.reload();
									});
							}else{
								Ext.MessageBox.alert('提示','任务推送失败,请确定任务是否已指派执行人.');
							}
						}
					});
				});
			}
		},
		tooltip:"推送任务给执行人",
		scope : this
	};
	var impExl = {text:'导入Excel',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_xls.gif',handler:function(){excelWin.show();}};
	var tbCfg = config.isLead ? [pageBar,xiafaTask,impExl] : [pageBar];
	// var cm = config.isDSP ?
	// new Ext.grid.ColumnModel([rn, c_taskstatus, c_title, c_fqr, c_qsRiQi,
	// c_jsr, c_status, c_aclm])
	// :(config.isYPY ?
	// new Ext.grid.ColumnModel([rn, c_taskstatus, c_title, c_fqr, c_qsRiQi,
	// c_pyr, c_pyRiQi, c_isok, c_aclm])
	// :new Ext.grid.ColumnModel([rn, c_taskstatus, c_title, c_fqr, c_qsRiQi,
	// c_jsr, c_status, c_pyr, c_pyRiQi, c_isok, c_aclm])
	// );
	// 准备配置参数
	var cfg = {
		title : '',
		plain : true,
		frame : true,
		autoScroll : true,
		iconCls : 'silk-grid',
		width : '100%',
		height : 300,
		store : store,
		trackMouseOver : true,
		style: 'padding:0 0 0 0;min-width:1600px',
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
						 xtype:'combo',
						 id:'com_id',
						 fieldLabel:'执行人',
						 name:'zxr_dm',
						 hiddenName:'zxr_dm',
						 displayField:'username',
						 anchor: '90%',
						 valueField:'id',
						 mode:'local',
						 value: rec.get("zxr_dm"),
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
		                readOnly: config.isLead,
		                name: 'rwhk',
		                value: rec.get("rwhk"),
		                width:50  		 
					},{
						xtype: 'compositefield',
						fieldLabel: '状态',
						items: [
				            	new Ext.form.Radio({boxLabel:'未下发', name:'status', inputValue : -1,checked:rec.get("status")==-1}),
				            	new Ext.form.Radio({boxLabel:'待完成', name:'status', inputValue : 0,checked:rec.get("status")==0}),
				            	new Ext.form.Radio({boxLabel:'已完成', name:'status', inputValue : 1,checked:rec.get("status")==1})
				            	]
		            }/*,{
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
					} */]
				} ]
			}],
			buttons:[{
				text:'保存',
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					mForm.load({
						url:config.url,
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
							Ext.Msg.alert('提示','保存成功!');
							Mwin.close();
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){
								Ext.Msg.alert('提示','保存成功!');
								Mwin.close();
								grid.store.reload();
							}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			}]
		});
		var Mwin = new Ext.Window({
			id : 'show_info',
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
