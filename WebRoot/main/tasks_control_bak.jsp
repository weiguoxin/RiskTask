<%@ page contentType="text/html;charset=GBK"%>

<div id="manageTaskForm"></div>
<div id="myTaskGrid" ></div>
<script>
var manageTaskForm = new Ext.form.FormPanel({
	title: '��ѯ����',
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
				fieldLabel: '��˰������',
                xtype: 'textfield',
				anchor: '90%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrmc',
                width:50  		 
			},{
				fieldLabel: '��˰��ʶ���',
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
					fieldLabel: 'ר��Ա',
	                xtype: 'textfield',
					anchor: '90%',
	   			    forceSelection:true,
				    selectOnFocus:true,
	                editable: false,
	                allowBlank: true,
	                name: 'zgy_mc'	 
				},{
					fieldLabel: 'ִ����',
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
							fieldLabel: '��������',
							items: [{
			                	  xtype     : 'datefield',
							        name: 'begin_time_start',
							        value:  getMonthFirstDay(new Date()),
							        width:105,
					                editable: false,
							        format:'Y-m-d'
								},{xtype: 'label',text:'��'},{
								    xtype: 'datefield',
								    fieldLabel: '��������',
							        value: getMonthLastDay(new Date()),
							        name: 'begin_time_end',
							        width:105,
					                editable: false,
							        format:'Y-m-d'
							}
								]}
								,{
									xtype: 'compositefield',
									fieldLabel: '״̬',
									items: [
							            	new Ext.form.Radio({boxLabel:'ȫ��', name:'status', inputValue : -2,checked: true}),
							            	new Ext.form.Radio({boxLabel:'δ�·�', name:'status', inputValue : -1}),
							            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 1}),
							            	new Ext.form.Radio({boxLabel:'ִ����', name:'status', inputValue : 0})
							            	]
					            }]}]
	}],
	buttons:[{
		text:'��ѯ',
		handler : function(){
		if(manageTaskForm.form.isValid()){
			myTaskGrid.store.load({params:{start:0,limit:15}});
		}	
	}
	},
	{    
        text:'��ղ�ѯ����',    
        handler:function(){ 
        	manageTaskForm.getForm().reset();
	}}]
});


TaskGrid = function(config) {
	// ����Դ��Ϣ
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
						header : "��˰��ʶ���",
						dataIndex : "nsrsbh",
						width : 100
					},
					{
						header : "��˰������",
						dataIndex : "nsrmc",
						width : 130
					},
					{
						header : "����˰�����",
						dataIndex : "swjg_mc",
						width : 130
					},
					{
						header : "ר��Ա",
						dataIndex : "zgy_mc",
						width : 60
					},
					{
						header : "ִ����",
						dataIndex : "zxr_mc",
						width : 60
					},
					{
						header : "����ָ��",
						dataIndex : "fxzb",
						width : 100
					},
					{
						header : "��������",
						dataIndex : "fxms",
						width : 100
					},
					{
						header : "����ִ��ʱ��",
						dataIndex : "begin_time",
						renderer : renderLast,
						width : 100
					},
					{
						header : "���ʱ��",
						dataIndex : "end_time",
						renderer : renderLast,
						width : 100
					},
					{
						header : "����ִ��ʱ��",
						dataIndex : "end_time",
						renderer : function(v,p,r){
							var b_time = r.get('begin_time');
							var e_time = r.get('end_time');
							if(b_time && e_time){
								var date3=e_time.getTime()-b_time.getTime();  //ʱ���ĺ�����
								var days=Math.floor(date3/(24*3600*1000));	//������������
								var leave1=date3%(24*3600*1000);    		//����������ʣ��ĺ�����
								var hours=Math.floor(leave1/(3600*1000));	//�����Сʱ��
								var leave2=leave1%(3600*1000);        		//����Сʱ����ʣ��ĺ�����
								var minutes=Math.floor(leave2/(60*1000));	//������������
								var s = '';
								if(days>0){
									s += days+"��";
								}if(hours>0){
									s += hours+"Сʱ";
								}if(minutes>0){
									s += minutes+"����";
								}
								//alert(s);
								return s;
							}
						},
						width : 100
					},
					{
						header : "����ִ��ʱ��",
						dataIndex : "limit_time",
						renderer : renderLast,
						width : 100
					},
					{
						header : "����Ӧ�Խ���",
						dataIndex : "fxydcs",
						width : 100
					},
					{
						header : "�������",
						dataIndex : "rwhk",
						width : 100
					},
					{
						header : "״̬",
						dataIndex : "status",
						width : 80,
						renderer : function(v, p, r) {
							if (null == v)
								return '';
							else if (v == -1){
								return '<span class="file-py" style="padding-left:20px;">δ�·�</span>';
							}else if (v == 0){
								return '<span class="hourglass" style="padding-left:20px;">ִ����</span>';
							}else if (v == 1)
								return '<span class="flag-green" style="padding-left:20px;">��ִ��</span>';
							else
								return '';
						}
					},
					{
						header : "���",
						dataIndex : "checked",
						width : 80,
						renderer : function(v, p, r) {
							if (v == 1)
								return '<span class="file-ok" style="padding-left:20px;">�����</span>';
							else
								return '<span class="flag-red" style="padding-left:20px;">δ���</span>';
						}
					} ]);
	var pageBar = new Ext.PagingToolbar({
		/*
		 * ����ÿ����ʾ�ļ�¼���� '-' ����ַ�
		 * pressed:��ʾ��ť���ڿ�ʼ��ʱ���Ƿ񱻰��£�ֻ��enableToggleΪ���ʱ�������
		 * enableToggle����ʾ��ť�ڿ�ʼ��ʱ���Ƿ��ܴ��ڱ����µ�״̬�������ڰ���û����֮���л�������Ƴ�x-btn-pressed��ʽ��
		 * text:��ť������ʾ������ cls����ť��CSS��
		 * toggleHandler:����enableToggleΪTRUEʱ�����ťʱ���¼�������
		 */
		pageSize : 15,
		store : store,
		displayInfo : true,
		firstText : "��ҳ",
		lastText : "ĩҳ",
		nextText : "��һҳ",
		preText : "��һҳ",
		refreshText : "ˢ��",
		displayMsg : '��ʾ �� {0} - {1}����¼���� {2} ��',
		emptyMsg : "û�м�¼"
	});
	var xiafaTask = {
		text : "��������",
		icon:'extjs/resources/button/computer_go.png',
		handler : function(){
			var recs = myTaskGrid.getSelectionModel().getSelections();
			if(recs.length == 0){
				Ext.MessageBox.alert('��ʾ','��ѡ��Ҫ���в����ļ�¼��');
			}else{
				Ext.Msg.confirm('ȷ��','�Ƿ���������' + recs.length +"��?", function(fn){//alert(fn);
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
								Ext.MessageBox.alert('��ʾ',"�ɹ���������"+res.r+"��.",function(){
										myTaskGrid.store.reload();
									});
							}else{
								Ext.MessageBox.alert('��ʾ','��������ʧ��,ԭ��:' + res.res);
							}
						}
					});
				});
			}
		},
		tooltip:"���������ִ����",
		scope : this
	};
	var impExl = {text:'����Excel',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_xls.gif',handler:function(){excelWin.show();}};
	var newTask = {text:'�½�����',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/add.png',handler:function(){showNewTaskWin();}};
	var delTask = {text:'ɾ������',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/win_exit.gif',handler:function(){

		var recs = myTaskGrid.getSelectionModel().getSelections();
		var n = recs.length;
		if(n == 0){
			Ext.MessageBox.alert('��ʾ','��ѡ��Ҫ���в����ļ�¼��');
			return;
		}
							Ext.Msg.confirm('ȷ��','�Ƿ�ȷ��ɾ��'+n+"������?(ɾ�����ɻָ�)",function(f){
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
											Ext.MessageBox.alert('��ʾ',"�ɹ�ɾ������"+res.r+"��.",function(){
													myTaskGrid.store.reload();
												});
										}else{
											Ext.MessageBox.alert('��ʾ','����ɾ��ʧ��,ԭ��:'+res.res);
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
	// ׼�����ò���
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
						fieldLabel: '��˰��ʶ���',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrsbh',
		                value:rec.get("nsrsbh"),
		                width:50  		 
					},{
						fieldLabel: '��˰������',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrmc',
		                value:rec.get("nsrmc"),
		                width:50  		 
					},{
						fieldLabel: '����˰�����',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'swjg_mc',
		                value: rec.get("swjg_mc"),
		                width:50  		 
					},{
						fieldLabel: 'ר��Ա',
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
						 fieldLabel:'ִ����',
						 name:'zxr_dm',
						 hiddenName:'zxr_dm',
						 displayField:'username',
						 anchor: '90%',
						 valueField:'id',
						 mode:'local',
						 value: rec.get("zxr_dm")==-1?'':rec.get("zxr_dm"),
						 triggerAction:'all',
						 emptyText:'��ѡ��...',
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
						fieldLabel: 'ר��Ա����',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'nsrmc',
		                value: rec.get("zgy_mc"),
		                width:50  		 
					},*/{
						fieldLabel: '����ָ��',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'fxzb',
		                value: rec.get("fxzb"),
		                width:50  		 
					},{
						fieldLabel: '��������',
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
						fieldLabel: '�ƻ�ִ��ʱ��',
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
						fieldLabel: '�ƻ�ִ��ʱ��',
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
						fieldLabel: '���ʱ��',
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
						fieldLabel: '����Ӧ�Խ���',
		                xtype: 'textfield',
						anchor: '90%',
						name:"fxydcs",
					    selectOnFocus:true,
		                readOnly: !config.isLead,
		                value: rec.get("fxydcs"),
		                width:50  		 
					},{
						fieldLabel: '�������',
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
						fieldLabel: '״̬',
						items: [
				            	new Ext.form.Radio({boxLabel:'δ�·�', name:'status', inputValue : -1,checked:rec.get("status")==-1}),
				            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 0,checked:rec.get("status")==0}),
				            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 1,checked:rec.get("status")==1})
				            	]
		            }/*/,{
						fieldLabel: '״̬',
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
									this.html = '<span class="file-py" style="padding-left:20px;">δ�·�</span>';
								}else if (v == 0){
									this.html = '<span class="flag-red" style="padding-left:20px;">��ִ��</span>';
								}else if (v == 1){
									this.html = '<span class="file-ok" style="padding-left:20px;">��ִ��</span>';
								}
		                    }
		                 }
					} ]
				} ]
			}],
			buttons:[{
				text:'����',
				hidden: rec.get("status")==0?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var zxr_dm = mForm.form.getValues().zxr_dm;
					if('' == zxr_dm || -1 == zxr_dm){
						Ext.Msg.alert('','��ѡ��ִ����!');return;
					}
					mForm.form.submit({
						url:config.url,
						waitMsg: "���ڼ������ݣ����Ժ�...",
						waitTitle : '��ʾ',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
							Ext.Msg.alert('��ʾ','����ɹ�!');
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('��ʾ','����ʧ��!ԭ��:'+action.res);
						}
					});
				}
			},{
				text:'���沢����',
				hidden: rec.get("status")==-1?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!mForm.form.isValid())return;
					var zxr_dm = mForm.form.getValues().zxr_dm;
					if('' == zxr_dm || -1 == zxr_dm){
						Ext.Msg.alert('','��ѡ��ִ����!');return;
					}
					mForm.form.submit({
						url:config.url,
						waitMsg: "���ڼ������ݣ����Ժ�...",
						waitTitle : '��ʾ',
						params:mForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
							Ext.Msg.alert('��ʾ','����ɹ�!');
							Ext.Ajax.request({
								url:'TasksData?action=upDateStatus&idList='+rec.get('id'),
								params:{statusNew:0,statusOld:-1},
								success:function(resp){
									var res =Ext.util.JSON.decode(resp.responseText);
									if(res.r > 0){
										Ext.MessageBox.alert('��ʾ',"�ɹ���������"+res.r+"��.",function(){
												myTaskGrid.store.reload();
												Mwin.close();
											});
									}else{
										Ext.MessageBox.alert('��ʾ','��������ʧ��,ԭ��:' + res.res);
									}
								}
							});
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){}else Ext.Msg.alert('��ʾ','����ʧ��!ԭ��:'+action.res);
						}
					});
				}
			},{	
				text:'���ͨ��',
				hidden: rec.get("status")==1?false:true,
				icon:'extjs/resources/button/win_yes.gif',
				handler :function(){
					Ext.Ajax.request({
						url:'TasksData?action=check&id='+rec.get('id'),
						params:{type:0},
						success:function(resp){
							var res =Ext.util.JSON.decode(resp.responseText);
							if(res.r > 0){
								Ext.MessageBox.alert('��ʾ',"���ͨ��.",function(){
										myTaskGrid.store.reload();
										Mwin.close();
									});
							}else{
								Ext.MessageBox.alert('��ʾ','���ʧ��,ԭ��:' + res.res);
							}
						}
					});
				}},{	
					text:'��˾ܾ�',
					hidden: rec.get("status")==1?false:true,
					icon:'extjs/resources/button/win_exit.gif',
					handler :function(){
						Ext.Ajax.request({
							url:'TasksData?action=check&id='+rec.get('id'),
							params:{type:1},
							success:function(resp){
								var res =Ext.util.JSON.decode(resp.responseText);
								if(res.r > 0){
									Ext.MessageBox.alert('��ʾ',"�����ɹ�!",function(){
											myTaskGrid.store.reload();	
											Mwin.close();
										});
								}else{
									Ext.MessageBox.alert('��ʾ','ʧ��,ԭ��:' + res.res);
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
						fieldLabel: '��˰��ʶ���',
		                xtype: 'textfield',
						anchor: '90%',
						allowBlank : false,
					    selectOnFocus:true,
		                editable: false,
		                name: 'nsrsbh',
		                width:50  		 
					},{
						fieldLabel: '��˰������',
		                xtype: 'textfield',
						anchor: '90%',
						allowBlank : false,
					    selectOnFocus:true,
		                editable: false,
		                name: 'nsrmc',
		                width:50  		 
					},new Ext.form.ComboBoxTree({
						fieldLabel: '��������',
						emptyText: 'ѡ��..',
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
						 fieldLabel:'ר��Ա',
						 name:'zgy_dm',
						 hiddenName:'zgy_dm',
						 displayField:'username',
						 valueField:'userid',
						 anchor: '90%',
						 mode:'local',
						 triggerAction:'all',
						 emptyText:'��ѡ��...',
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
						fieldLabel: 'ר��Ա����',
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
						fieldLabel: '�ƻ�ִ��ʱ��',
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
			            fieldLabel:'����ָ��',
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
						fieldLabel: '����Ӧ�Խ���',
		                xtype: 'textfield',
						anchor: '90%',
						name:"fxydcs",
					    selectOnFocus:true,
		                width:50  		 
					},{
						fieldLabel: '��������',
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
				text:'��������',
				icon:'extjs/resources/button/win_yes.gif',
				handler : function(){
					if(!new_taskForm.form.isValid()){return;}
					new_taskForm.load({
						url: 'TasksData?action=newTask',
						waitMsg: "���ڼ������ݣ����Ժ�...",
						waitTitle : '��ʾ',
						params:new_taskForm.form.getValues(),
						success:function(f, action){
							if(Ext.util.JSON.decode(action.response.responseText).r>0){
								Ext.Msg.confirm('��ʾ','����ɹ�!,�Ƿ�����½�?',function(f){
									if(f == 'yes'){new_taskForm.form.reset();}
								});
							}
						},
						failure:function(f, action){
							action = Ext.util.JSON.decode(action.response.responseText);
							if(action.r>0){
								Ext.Msg.confirm('��ʾ','����ɹ�!,�Ƿ�����½�?',function(f){
									if(f == 'yes'){new_taskForm.form.reset();}else{
										Mwin.close();
										myTaskGrid.store.reload();
									}
									
								});
							}else Ext.Msg.alert('��ʾ','����ʧ��!ԭ��:'+action.res);
						}
					});
				}
			}]
		});
		var Mwin = new Ext.Window({
			id : 'show_info',
			title : '�½�����',
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