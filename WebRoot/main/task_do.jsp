<%@ page contentType="text/html;charset=GBK"%>

<div id="taskForm"></div>
<div id="shGrid" ></div>
<script>
var taskForm = new Ext.form.FormPanel({
	title: '��ѯ����',
	labelAlign: 'right',
	labelWidth: 90,
	frame: true,
	renderTo: "taskForm",
	bodyStyle: 'padding: 0 0 0 0;',
	style: 'padding:0 0 0 0;min-width:800px',
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
				anchor: '70%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrmc',
                width:50  		 
			},{
				fieldLabel: '��˰��ʶ���',
                xtype: 'textfield',
				anchor: '70%',
   			    forceSelection:true,
			    selectOnFocus:true,
                editable: false,
                allowBlank: true,
                name: 'nsrsbh',
                width:50  		 
			}]},{
				columnWidth:.33,
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
								]},{
									xtype: 'compositefield',
									fieldLabel: '״̬',
									items: [
							            	new Ext.form.Radio({boxLabel:'ȫ��', name:'status', inputValue : -2,checked: true}),
							            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 1}),
							            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 0})
							            	]
					            }
								]},{
									columnWidth:.33,
						            layout: 'form',
									items:[{
										xtype: 'compositefield',
										fieldLabel: '�·�����',
										items: [
								            	new Ext.form.Radio({boxLabel:'ȫ��', name:'shijuxiafa', inputValue : -2,checked: true}),
								            	new Ext.form.Radio({boxLabel:'�о�', name:'shijuxiafa', inputValue : 1}),
								            	new Ext.form.Radio({boxLabel:'����', name:'shijuxiafa', inputValue : 0})
								            	]
						            }]}]
	}],
	buttons:[{
		text:'��ѯ',
		handler : function(){
		if(taskForm.form.isValid()){
			shgrid.store.load({params:{start:0,limit:15}});
		}	
	}
	},
	{    
        text:'��ղ�ѯ����',    
        handler:function(){ 
        	taskForm.getForm().reset();
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
			"fxydcs", "swjg_dm", "zgy_dm", "task_man",'zxr_dm','rwhk','zxr_mc','checked','jjyy','shijuxiafa','imp_username','imp_swjg_mc'  ];
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
	var c_status = {
		header : "״̬",
		dataIndex : "status",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 0)
				return '<span class="file-py" style="padding-left:20px;">��ִ��</span>';
			else if (v == 1)
				return '<span class="file-turn" style="padding-left:20px;">��ִ��</span>';
			else
				return '';
		}
	};
	var c_taskstatus = {
		header : "�����̶�",
		dataIndex : "taskstatus",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 0)
				return '<span class="flag-green" style="padding-left:20px;">��ͨ</span>';
			else if (v == 1)
				return '<span class="flag-red" style="padding-left:20px;">����</span>';
			else
				return '';
		}
	};
	var c_isok = {
		header : "ִ�н��",
		dataIndex : "isok",
		width : 50,
		renderer : function(v, p, r) {
			if (v == 1) {
				return '<span class="file-ok" style="padding-left:20px;">��ִ��</span>';
			} else {
				if (v == 2) {
					return '<span class="file-no" style="padding-left:20px;">�ѳ���</span>';
				} else
					return '';
			}
		}
	};
	var c_aclm = {
		xtype : 'actioncolumn',
		header : "�鿴����",
		width : 20,
		items : [ {
			getClass : function(v, meta, rec){ 
				this.items[0].tooltip = '�鿴����';
				return 'file_show';
			},
			handler : function(grid, rowIndex, colIndex) {
				var rec = store.getAt(rowIndex);
				var href = config.isDSP ? 'main/dsp_edit_file.jsp'
						: 'main/ypy_edit_file.jsp';
				var title = config.isDSP ? '�����ļ�' : '�鿴�ļ�';
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
						header : "��˰��ʶ���",
						dataIndex : "nsrsbh",
						width : 150
					},
					{
						header : "��˰������",
						dataIndex : "nsrmc",
						width : 150
					},
					{
						header : "����˰�����",
						dataIndex : "swjg_mc",
						width : 180
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
						width : 120
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
								return '<span class="flag-red" style="padding-left:20px;">��ִ��</span>';
							}else if (v == 1)
								return '<span class="file-ok" style="padding-left:20px;">��ִ��</span>';
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
					},
					{
						header : "�о��·�",
						dataIndex : "shijuxiafa",
						width : 80,
						renderer : function(v, p, r) {
							if (v == 0)
								return '��';
							else
								return '��';
						}
					},
					{
						header : "�����·���",
						dataIndex : "imp_username",
						width : 80
					} ,
					{
						header : "�·�����",
						dataIndex : "imp_swjg_mc",
						width : 180
					} ]);
	var pageBar = new Ext.PagingToolbar({
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
	var tbCfg = [pageBar];
	var cfg = {
		title : '',
		plain : true,
		frame : true,
		autoScroll : true,
		iconCls : 'silk-grid',
		width : 2310,
		height : 386,
		store : store,
		trackMouseOver : true,
		style: 'padding:0 0 0 0;min-width:2310px;',
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


	config = Ext.applyIf(config || {}, cfg);

	// call���๹����
	FileGrid.superclass.constructor.call(this, config);
	store.load({
		params : {
			start : 0,
			limit : 15
		}
	});
};
Ext.extend(FileGrid, Ext.grid.GridPanel);

var shgrid = new FileGrid({
	isLead:false,
	url:'TasksData?action=updateTask_finish',
	myForm:taskForm,
	renderTo:'shGrid'
});

function showWin(rec,grid) {
	var mForm = new Ext.FormPanel({
		id:'UpForm',
		fileUpload:true,
		labelAlign: 'right',
		labelWidth: 90,
		frame: true,
		width: '100%',
		//height: 240,
		buttonAlign : "center",
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
					fieldLabel: 'ִ����',
	                xtype: 'textfield',
					anchor: '90%',
				    selectOnFocus:true,
	                editable: false,
	                readOnly: true,
	                name: 'zxr_dm',
					 value: rec.get("zxr_mc"),
	                width:50  		 
				},{
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
	                readOnly: true,
	                value: rec.get("fxydcs"),
	                width:50  		 
				},{
					fieldLabel: '�������',
	                xtype: 'textarea',
					anchor: '90%',
				    selectOnFocus:true,
	                editable: false,
					allowBlank : false,
	                name: 'rwhk',
	                value: rec.get("rwhk"),
	                width:50  		 
				},{
					xtype: 'compositefield',
					fieldLabel: '״̬',
					items: [
			            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 0,checked:rec.get("status")==0}),
			            	new Ext.form.Radio({boxLabel:'�����', name:'status', inputValue : 1,checked:rec.get("status")==1})
			            	]
	            }/*,{
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
				} */]
			} ]
		},{
			fieldLabel: '�ܾ����ԭ��',
            xtype: 'textfield',
			anchor: '90%',
		    selectOnFocus:true,
            editable: false,
            readOnly: true,
            name: 'jjyy',
            value: rec.get("jjyy")
            //width:50  		 
		},{
            xtype:'fieldset',
            id:'fujianField',
            title: '����',
            collapsible: true,
            collapsed : true,
            autoHeight:true,
            items :[{
				xtype: 'compositefield',
				fieldLabel: '��Ӹ���',
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
					    text:'�ϴ�',
					    xtype:'button',
					    handler : function() {
					    	mForm._doUpload();
					    }}]}
                    ],
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
                            		addCompositefield(p,ja[i],true&&rec.get('checked')==0);
                            	}
                             	p.doLayout();
                             }
                         }
            		 });
            	 }
                  }}],
        _doUpload:function(){
        		var file =  mForm.getForm().findField('upfileName').getValue();
        		if(file==''){
        			Ext.Msg.alert('��ʾ','��ѡ���ļ����ϴ�!');
        			return;
        		}
        		if(rec.get('checked')==1){
        			Ext.Msg.alert('��ʾ','����������ˣ������ϴ�����!');
        			return;
        		}
                mForm.getForm().submit({
                    url:'FileAction',
                    params:{act:'uploadfile'},
                    method:'post',
                    success:function(form,action){
                        var json = Ext.util.JSON.decode(action.response.responseText);
                        if(json.success&&json.r>0&&json.r_save>0&&json.r==json.r) {
                           //Ext.Msg.alert('�ϴ��ɹ�!',json.path);
                           var field = mForm.findById('fujianField');
                        	addCompositefield(field,json,true);
                        	field.doLayout();
                        } else {
                        	Ext.Msg.alert('�ϴ�ʧ��!',json.res || '�ļ����ʹ�����ų�����С���ƣ������ϴ�100M.');
                        }
                    },
                    failure:function(form,o) {
                        var json = Ext.decode(o.result);
                        Ext.Msg.alert('����',json.res || '������������������.');
                    },
                    scope:this
                }); 
        },
		buttons:[{
			text:'�ύ',
			hidden: rec.get('checked')==1,
			icon:'extjs/resources/button/win_yes.gif',
			handler : function(){
				if(!mForm.form.isValid())return;
				mForm.load({
					url:'TasksData?action=updateTask_finish',
					waitMsg: "���ڼ������ݣ����Ժ�...",
					waitTitle : '��ʾ',
					params:mForm.form.getValues(),
					success:function(f, action){
						if(Ext.util.JSON.decode(action.response.responseText).r>0){
						Ext.Msg.alert('��ʾ','����ɹ�!');
						Mwin.close();
						}
					},
					failure:function(f, action){
						action = Ext.util.JSON.decode(action.response.responseText);
						if(action.r>0){
							Ext.Msg.alert('��ʾ','����ɹ�!');
							Mwin.close();
							grid.store.reload();
						}else Ext.Msg.alert('��ʾ','����ʧ��!ԭ��:'+action.res);
					}
				});
			}
		}]
	});
	var Mwin = new Ext.Window({
		title : rec.get("swjg_mc"),
		width : 600,
		height : 300,
		boxMaxHeight: 600,
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