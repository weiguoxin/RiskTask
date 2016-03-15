<%@ page contentType="text/html;charset=GBK"%>

<div id="manageTaskForm"></div>
<div id="myTaskGrid" ></div>
<script>
var manageTaskForm = new Ext.form.FormPanel({
	title: '查询条件',
	labelAlign: 'right',
	labelWidth: 90,
	frame: true,
	renderTo: "manageTaskForm",
	bodyStyle: 'padding: 0 0 0 0;',
	//style: 'padding:0 0 0 0;min-width:1600px',
	width: '100%',
	buttonAlign : "center",
	items: [{
		layout:'column',
		items: [{
			columnWidth:.33,
            layout: 'form',
			items:[{
				fieldLabel: '纳税人名称',
                xtype: 'textfield',
				anchor: '90%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrmc',
                width:50  		 
			},{
				fieldLabel: '纳税人识别号',
                xtype: 'textfield',
				anchor: '90%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrsbh',
                width:50  		 
			}]},{
				columnWidth:.33,
	            layout: 'form',
				items:[{
					fieldLabel: '专管员',
	                xtype: 'textfield',
					anchor: '90%',
	   			    forceSelection:true,
				    selectOnFocus:true,
	                editable: false,
	                allowBlank: true,
	                name: 'zgy_mc'	 
				},{
					fieldLabel: '执行人',
	                xtype: 'textfield',
					anchor: '90%',
	   			    forceSelection:true,
				    selectOnFocus:true,
	                editable: false,
	                allowBlank: true,
	                name: 'zxr_mc'	 
				}]},{
				columnWidth:.34,
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
								]}
								,{
									xtype: 'compositefield',
									fieldLabel: '状态',
									items: [
							            	new Ext.form.Radio({boxLabel:'全部', name:'status', inputValue : -2,checked: true}),
							            	new Ext.form.Radio({boxLabel:'未下发', name:'status', inputValue : -1}),
							            	new Ext.form.Radio({boxLabel:'已完成', name:'status', inputValue : 1}),
							            	new Ext.form.Radio({boxLabel:'执行中', name:'status', inputValue : 0})
							            	]
					            }]}]
	}],
	buttons:[{
		text:'查询',
		handler : function(){
		if(manageTaskForm.form.isValid()){
			myTaskGrid.store.load({params:{start:0,limit:15}});
		}	
	}
	},
	{    
        text:'清空查询条件',    
        handler:function(){ 
        	manageTaskForm.getForm().reset();
	}}]
});


TaskGrid = function(config) {
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
			"fxydcs", "swjg_dm", "zgy_dm", "task_man",'zxr_dm','rwhk','zxr_mc','checked' ];
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
						header : "任务执行时长",
						dataIndex : "end_time",
						renderer : function(v,p,r){
							var b_time = r.get('begin_time');
							var e_time = r.get('end_time');
							if(b_time && e_time){
								var date3=e_time.getTime()-b_time.getTime();  //时间差的毫秒数
								var days=Math.floor(date3/(24*3600*1000));	//计算出相差天数
								var leave1=date3%(24*3600*1000);    		//计算天数后剩余的毫秒数
								var hours=Math.floor(leave1/(3600*1000));	//计算出小时数
								var leave2=leave1%(3600*1000);        		//计算小时数后剩余的毫秒数
								var minutes=Math.floor(leave2/(60*1000));	//计算相差分钟数
								var s = '';
								if(days>0){
									s += days+"天";
								}if(hours>0){
									s += hours+"小时";
								}if(minutes>0){
									s += minutes+"分钟";
								}
								//alert(s);
								return s;
							}
						},
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
								return '<span class="hourglass" style="padding-left:20px;">执行中</span>';
							}else if (v == 1)
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
			var recs = myTaskGrid.getSelectionModel().getSelections();
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
										myTaskGrid.store.reload();
									});
							}else{
								Ext.MessageBox.alert('提示','任务推送失败,原因:' + res.res);
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
	var newTask = {text:'新建任务',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/add.png',handler:function(){showNewTaskWin();}};
	var delTask = {text:'删除任务',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/win_exit.gif',handler:function(){

		var recs = myTaskGrid.getSelectionModel().getSelections();
		var n = recs.length;
		if(n == 0){
			Ext.MessageBox.alert('提示','请选择要进行操作的记录！');
			return;
		}
							Ext.Msg.confirm('确认','是否确认删除'+n+"条任务?(删除不可恢复)",function(f){
								if(f != 'yes')return;
								var list = [];
								for(var i = 0 ; i < recs.length ; i++){
									var rec = recs[i];
									list.push(rec.get('id'));
								}
								Ext.Ajax.request({
									url:'TasksData?action=del&idList='+list,
									params:{},
									success:function(resp){
										var res =Ext.util.JSON.decode(resp.responseText);
										if(res.r > 0){
											Ext.MessageBox.alert('提示',"成功删除任务"+res.r+"条.",function(){
													myTaskGrid.store.reload();
												});
										}else{
											Ext.MessageBox.alert('提示','任务删除失败,原因:'+res.res);
										}
									}
								});
							});
							
					}};
	
	var tbCfg = config.isLead ? [pageBar,xiafaTask,impExl,newTask,delTask] : [pageBar];
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
			}],
			buttons:[{
				text:'保存',
				hidden: rec.get("status")==0?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var zxr_dm = mForm.form.getValues().zxr_dm;
					if('' == zxr_dm || -1 == zxr_dm){
						Ext.Msg.alert('','请选择执行人!');return;
					}
					mForm.form.submit({
						url:config.url,
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
							Ext.Msg.alert('提示','保存成功!');
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			},{
				text:'保存并推送',
				hidden: rec.get("status")==-1?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var zxr_dm = mForm.form.getValues().zxr_dm;
					if('' == zxr_dm || -1 == zxr_dm){
						Ext.Msg.alert('','请选择执行人!');return;
					}
					mForm.form.submit({
						url:config.url,
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
							Ext.Msg.alert('提示','保存成功!');
							Ext.Ajax.request({
								url:'TasksData?action=upDateStatus&idList='+rec.get('id'),
								params:{statusNew:0,statusOld:-1},
								success:function(resp){
									var res =Ext.util.JSON.decode(resp.responseText);
									if(res.r > 0){
										Ext.MessageBox.alert('提示',"成功推送任务"+res.r+"条.",function(){
												myTaskGrid.store.reload();
												Mwin.close();
											});
									}else{
										Ext.MessageBox.alert('提示','任务推送失败,原因:' + res.res);
									}
								}
							});
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			},{	
				text:'审核通过',
				hidden: rec.get("status")==1?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler :function(){
					Ext.Ajax.request({
						url:'TasksData?action=check&id='+rec.get('id'),
						params:{type:0},
						success:function(resp){
							var res =Ext.util.JSON.decode(resp.responseText);
							if(res.r > 0){
								Ext.MessageBox.alert('提示',"审核通过.",function(){
										myTaskGrid.store.reload();
										Mwin.close();
									});
							}else{
								Ext.MessageBox.alert('提示','审核失败,原因:' + res.res);
							}
						}
					});
				}},{	
					text:'审核拒绝',
					hidden: rec.get("status")==1?false:true,
					icon:'extjs/resources/button/win_exit.gif',
					handler :function(){
						Ext.Ajax.request({
							url:'TasksData?action=check&id='+rec.get('id'),
							params:{type:1},
							success:function(resp){
								var res =Ext.util.JSON.decode(resp.responseText);
								if(res.r > 0){
									Ext.MessageBox.alert('提示',"操作成功!",function(){
											myTaskGrid.store.reload();	
											Mwin.close();
										});
								}else{
									Ext.MessageBox.alert('提示','失败,原因:' + res.res);
								}
							}
						});
					}}]
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
	function showNewTaskWin(){
		var new_taskForm = new Ext.FormPanel({
			labelAlign: 'right',
			labelWidth: 90,
			frame: true,
			width: '100%',
			height: 170,
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
					items : [{
						xtype:'hidden',
						name : 'userkebe'
					},{
						fieldLabel: '纳税人识别号',
		                xtype: 'textfield',
						anchor: '90%',
						allowBlank : false,
					    selectOnFocus:true,
		                editable: false,
		                name: 'nsrsbh',
		                width:50  		 
					},{
						fieldLabel: '纳税人名称',
		                xtype: 'textfield',
						anchor: '90%',
						allowBlank : false,
					    selectOnFocus:true,
		                editable: false,
		                name: 'nsrmc',
		                width:50  		 
					},new Ext.form.ComboBoxTree({
						fieldLabel: '所属机关',
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
						 xtype:'combo',
						 fieldLabel:'专管员',
						 name:'zgy_dm',
						 hiddenName:'zgy_dm',
						 displayField:'username',
						 valueField:'userid',
						 anchor: '90%',
						 mode:'local',
						 triggerAction:'all',
						 emptyText:'请选择...',
						 forceSelection:true,
						allowBlank : false,
						 selectOnFocus:true,
			             editable: false,
						 store:new Ext.data.JsonStore({
						      root: 'topics',
						      fields: ['userid','username'],
						      proxy: new Ext.data.HttpProxy({
						          url: 'WLUserData?action=show&start=0&limit=9999&type=3'
						      }),autoLoad:true
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
						}/*{
						fieldLabel: '专管员姓名',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrmc',
		                value: rec.get("zgy_mc"),
		                width:50  		 
					},*/ ]
				}, {
					columnWidth : .5,
					layout : 'form',
					items : [{
						xtype:'hidden',
						name : 'fxzb_dm'
					},{
						fieldLabel: '计划执行时限',
		                xtype: 'datefield',
						anchor: '90%',
						name:'limit_time',
					    selectOnFocus:true,
		                editable: false,
		                format: 'Y-m-d',
		                width:50  		 
					},new Ext.form.ComboBoxTree({
			            name:'fxzb',
						anchor: '90%',
			            //xtype:'checktreepanel',
			            isFormField:true,
			            fieldLabel:'风险指标',
						allowBlank : false,
			            autoScroll:true,
			            selectNodeModel: 'leaf', 
					 	displayField:'text',
					 	valueField:'id',
			            bodyStyle:'background-color:white;border:1px solid #B5B8C8',
			            tree:  new Ext.tree.TreePanel({
		                    rootVisible: false,
		                    border: false,
		                    method: 'GET',
		                    dataUrl: 'TaskManData?action=treeQuanXian',
		                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
		                }),
		                listeners :{
		                	"expand": function(c){
		                		c.list.setWidth('320px'); 
		                        c.innerList.setWidth('auto');
		                	}
		                }
			        }),{
						fieldLabel: '风险应对建议',
		                xtype: 'textfield',
						anchor: '90%',
						name:"fxydcs",
					    selectOnFocus:true,
		                width:50  		 
					},{
						fieldLabel: '风险描述',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                name: 'fxms',
		                width:50  		 
					}]
				} ]
			}],
			buttons:[{
				text:'保存任务',
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!new_taskForm.form.isValid()){return;}
					new_taskForm.load({
						url: 'TasksData?action=newTask',
						waitMsg: "正在加载数据，请稍候...",
						waitTitle : '提示',
						params:new_taskForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.confirm('提示','保存成功!,是否继续新建?',function(f){
									if(f == 'yes'){new_taskForm.form.reset();}
								});
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){
								Ext.Msg.confirm('提示','保存成功!,是否继续新建?',function(f){
									if(f == 'yes'){new_taskForm.form.reset();}else{
										Mwin.close();
										myTaskGrid.store.reload();
									}
									
								});
							}else Ext.Msg.alert('提示','保存失败!原因:'+action.res);
						}
					});
				}
			}]
		});
		var Mwin = new Ext.Window({
			id : 'show_info',
			title : '新建任务',
			width : 600,
			height : 203,
			layout : 'form',
			modal : true,
			plain : false,
			resizable : false,
			constrain : false,
			autoScroll : true,
			buttonAlign : 'center',
			items : [ new_taskForm ]
		}).show();
	}
	config = Ext.applyIf(config || {}, cfg);
	TaskGrid.superclass.constructor.call(this, config);
	store.load({
		params : {
			start : 0,
			limit : 15
		}
	});
};
Ext.extend(TaskGrid, Ext.grid.GridPanel);
var myTaskGrid = new TaskGrid({
	isLead:true,
	url:'TasksData?action=updateTask',
	myForm:manageTaskForm,
	renderTo:'myTaskGrid'
});
</script>