
<%@ page contentType="text/html;charset=GBK"%>

<div id="TaskForm_Find"></div>
<div id="TaskGrid_Find"></div>
<script>
var now_add_Find = false;
	var TaskForm_Find = new Ext.form.FormPanel({
		title : '��ѯ����',
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
					fieldLabel : '��˰������',
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
					fieldLabel : '�����·�����',
					emptyText : 'ȫ��',
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
					 fieldLabel : '�����·�Ա',
					 emptyText : 'ȫ��',
					 //tpl:'����ѡ��˰�����',
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
						fieldLabel: '�·�����',
						 anchor: '98%',
						items: [
				            	new Ext.form.Radio({boxLabel:'ȫ��', name:'shijuxiafa', inputValue : -2,checked: true}),
				            	new Ext.form.Radio({boxLabel:'�о��·�', name:'shijuxiafa', inputValue : 1}),
				            	new Ext.form.Radio({boxLabel:'�����·�', name:'shijuxiafa', inputValue : 0})
				            	]
		            }]
			}, {
				columnWidth : .25,
				layout : 'form',
				items : [ /*{
					fieldLabel : 'ר��Ա',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'zgy_mc'
				}*/ {
					fieldLabel : '��˰��ʶ���',
					xtype : 'textfield',
					anchor : '90%',
					forceSelection : true,
					selectOnFocus : true,
					editable : false,
					allowBlank : true,
					name : 'nsrsbh',
					width : 50
				}, {
					fieldLabel : 'ִ����',
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
					fieldLabel : '�·�����',
					items : [ {
						xtype : 'datefield',
						name : 'begin_time_start',
						value : getMonthFirstDay(new Date()),
						width : 105,
						editable : false,
						format : 'Y-m-d'
					}, {
						xtype : 'label',
						text : '��'
					}, {
						xtype : 'datefield',
						fieldLabel : '��������',
						value : getMonthLastDay(new Date()),
						name : 'begin_time_end',
						width : 105,
						editable : false,
						format : 'Y-m-d'
					} ]
				}, {
					xtype : 'compositefield',
					fieldLabel : '״̬',
					items : [ new Ext.form.Radio({
						boxLabel : 'ȫ��',
						name : 'status',
						inputValue : -2,
						checked : true
					}), new Ext.form.Radio({
						boxLabel : 'δ�·�',
						name : 'status',
						inputValue : -1
					}), new Ext.form.Radio({
						boxLabel : 'ִ����',
						name : 'status',
						inputValue : 0
					}), new Ext.form.Radio({
						boxLabel : '�����',
						name : 'status',
						inputValue : 1
					}) ]
				} ]
			} ]
		},{
            xtype:'fieldset',
            title: '����ѡ��',
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
								    fieldLabel:'����ָ��',
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
				                    fieldLabel: '��������',
				                    name: 'fxzb'
				                },{
		                    fieldLabel: '�������',
		                    name: 'rwhk'
		                }
		               ]},{
		    				columnWidth : .36,
		    				layout : 'form',
		    	            defaultType: 'textfield',
		    				items : [{xtype:'hidden',name:'swjg_dm'},
		        				     new Ext.form.ComboBoxTree({
		 								fieldLabel: '���ܻ���',
		 								selectNodeModel:'all',
		 								emptyText: 'ѡ��..',
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
		 								fieldLabel : '�޶�����',
		 								items : [ {
		 									xtype : 'datefield',
		 									name : 'limit_time_s',
		 									width : 100,
		 									editable : false,
		 									format : 'Y-m-d'
		 								}, {
		 									xtype : 'label',
		 									text : '��'
		 								}, {
		 									xtype : 'datefield',
		 									fieldLabel : '��������',
		 									name : 'limit_time_e',
		 									width : 100,
		 									editable : false,
		 									format : 'Y-m-d'
		 								} ]
		 							},{
		 								xtype : 'compositefield',
		 								fieldLabel : '�������',
		 								items : [ {
		 									xtype : 'datefield',
		 									name : 'end_time_s',
		 									width : 100,
		 									editable : false,	
		 									format : 'Y-m-d'
		 								}, {
		 									xtype : 'label',
		 									text : '��'
		 								}, {
		 									xtype : 'datefield',
		 									fieldLabel : '��������',
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
								fieldLabel: 'ȫ��',
								selectNodeModel:'all',
								emptyText: 'ѡ��..',
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
								fieldLabel : '���',
								items : [ new Ext.form.Radio({
									boxLabel : 'ȫ��',
									name : 'checked',
									inputValue : -1,
									checked : true
								}), new Ext.form.Radio({
									boxLabel : '�����',
									name : 'checked',
									inputValue : 1
								}), new Ext.form.Radio({
									boxLabel : 'δ���',
									name : 'checked',
									inputValue : 0
								}) ]
							},{
								xtype : 'compositefield',
								fieldLabel : '�޸�ִ����',
								items : [ new Ext.form.Radio({
									boxLabel : 'ȫ��',
									name : 'change_zxr',
									inputValue : -1,
									checked : true
								}), new Ext.form.Radio({
									boxLabel : '����',
									name : 'change_zxr',
									inputValue : 1
								}), new Ext.form.Radio({
									boxLabel : 'δ����',
									name : 'change_zxr',
									inputValue : 0
								}) ]
							}
						               ]}
    			]}
            ]
        } ],
		buttons : [ {
			text : '��ѯ',
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
			text : '��ղ�ѯ����',
			handler : function() {
				TaskForm_Find.getForm().reset();
			}
		},{
			text : '����EXCEL',
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
			 defaults:{   //������������
		           sortable:true,
		           menuDisabled:true
		        },
			columns:
				[
						sm,
						rn,
						{
							header : "��˰��ʶ���",
							dataIndex : "nsrsbh",
							width : 100,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "��˰������",
							dataIndex : "nsrmc",
							width : 200,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "����˰�����",
							dataIndex : "swjg_mc",
							width : 180,
							editor:new Ext.form.ComboBoxTree({
								fieldLabel: '��������',
								selectNodeModel:'all',
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
							header : "ר��Ա",
							dataIndex : "zgy_mc",
							width : 60,
							editor:{
								 xtype:'combo',
								 displayField:'username',
								 valueField:'username',
								 anchor: '90%',
								 mode:'local',
								 triggerAction:'all',
								 emptyText:'��ѡ��...',
								 forceSelection:true,
								 allowBlank : false,
								 blankText:'����ѡ��˰�����',
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
							header : "����ָ��",
							dataIndex : "fxzb",
							width : 150,
							editor:new Ext.form.ComboBoxTree({
					            name:'fxzb',
								anchor: '90%',
					            //xtype:'checktreepanel',
					            fieldLabel:'����ָ��',
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
							header : "��������",
							dataIndex : "fxms",
							width : 150,
							editor:new Ext.form.TextField({  
				                allowBlank:false  
				            })
						},
						{
							header : "����Ӧ�Խ���",
							dataIndex : "fxydcs",
							width : 180,
							editor:new Ext.form.TextField({  
								readOnly: true
				            })
						},{
							header : "ִ��˰�����",
							dataIndex : "zx_swjg_mc",
							width : 180,
							editor:new Ext.form.ComboBoxTree({
								fieldLabel: '��������',
								selectNodeModel:'all',
								emptyText: 'ѡ��..',
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
							header : "ִ����",
							dataIndex : "zxr_mc",
							width : 60,
							editor:{
								 xtype:'combo',
								 displayField:'username',
								 valueField:'username',
								 anchor: '90%',
								 mode:'local',
								 triggerAction:'all',
								 emptyText:'��ѡ��...',
								 tooltip:'����ѡ��˰����غͷ���ָ��',
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
							header : "�����·�ʱ��",
							dataIndex : "begin_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "����ִ��ʱ��",
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
							header : "ʵ�����ʱ��",
							dataIndex : "end_time",
							renderer : renderLast,
							width : 100
						},
						{
							header : "����ִ��ʱ��",
							dataIndex : "end_time",
							renderer : function(v, p, r) {
								//��������ִ��ʱ���
								var b_time = r.get('begin_time');
								var e_time = r.get('end_time');
								if (b_time && e_time) {
									var date3 = e_time.getTime()
											- b_time.getTime(); //ʱ���ĺ�����
									var days = Math.floor(date3
											/ (24 * 3600 * 1000)); //������������
									var leave1 = date3 % (24 * 3600 * 1000); //����������ʣ��ĺ�����
									var hours = Math.floor(leave1
											/ (3600 * 1000)); //�����Сʱ��
									var leave2 = leave1 % (3600 * 1000); //����Сʱ����ʣ��ĺ�����
									var minutes = Math.floor(leave2
											/ (60 * 1000)); //������������
									var s = '';
									if (days > 0) {
										s += days + "��";
									}
									if (hours > 0) {
										s += hours + "Сʱ";
									}
									if (minutes > 0) {
										s += minutes + "����";
									}
									//alert(s);
									return s;
								}
							},
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
								else if (v == -1) {
									return '<span class="file-py" style="padding-left:20px;">δ�·�</span>';
								} else if (v == 0) {
									return '<span class="hourglass" style="padding-left:20px;">ִ����</span>';
								} else if (v == 1)
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
							width : 150
						}]});
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
		var xiafaTask = {
			text : "��������",
			icon : 'extjs/resources/button/computer_go.png',
			handler : function() {send_Task();},
			tooltip : "���������ִ����",
			scope : this
		};
		var newTask = {
			text : '�½�����',
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
				text : '�޸�����',
				cls : 'x-btn-text-icon details',
				icon : 'extjs/resources/button/tool_edit_old.gif',
				handler : function() {
						var rec = TaskGrid_Find.getSelectionModel().getSelected();
						if (rec == null) {
							Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ���в����ļ�¼��');
							return;
						}
	            		now_add_Find = true;
						if(!_canEdit())return false;
		                TaskGrid_Find.stopEditing();
		                editor.startEditing(store.indexOfId(rec.get("id")));
		                
				}
			};
		var delTask = {
			text : 'ɾ������',
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
				Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ���в����ļ�¼��');
				return;
			}
			Ext.Msg.confirm('ȷ��', '�Ƿ�ȷ��ɾ��' + n + "������?(ɾ�����ɻָ�)", function(f) {
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
							Ext.MessageBox.alert('��ʾ', "�ɹ�ɾ������" + res.r + "��.",
									function() {
										TaskGrid_Find.store.reload();
									});
						} else {
							Ext.MessageBox.alert('��ʾ', '����ɾ��ʧ��,ԭ��:' + res.res);
						}
					}
				});
			});
		}
		function send_Task(){
			var recs = TaskGrid_Find.getSelectionModel().getSelections();
			if (recs.length == 0) {
				Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ���в����ļ�¼��');
			} else {
				Ext.Msg
						.confirm(
								'ȷ��',
								'�Ƿ���������' + recs.length + "��?",
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
																		'��ʾ',
																		"�ɹ���������"
																				+ res.r
																				+ "��.",
																		function() {
																			TaskGrid_Find.store
																					.reload();
																		});
													} else {
														Ext.MessageBox
																.alert(
																		'��ʾ',
																		'��������ʧ��,ԭ��:'
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
		    	  Ext.Msg.alert('','�����ɹ�!');
		      },
		      failure:function(){
		    	  TaskGrid_Find.store.remove(record);
		    	  Ext.Msg.alert('','����ʧ��!');
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
        saveText: '����',
        cancelText: 'ȡ��',
        listeners:{
        	afteredit: afteredit,
        	beforeedit: _beforeedit,
        	canceledit: after_or_cancelEdit
        }
    });
	
	/*Grid��ʼ��*/
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
						fieldLabel: 'ִ��˰�����',
		                xtype: 'textfield',
						anchor: '90%',
					    selectOnFocus:true,
		                editable: false,
		                readOnly: true,
		                name: 'swjg_mc',
		                value: rec.get("zx_swjg_mc"),
		                width:50  		 
					},{
						fieldLabel: 'ִ����',
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
		                readOnly: true,
		                value: rec.get("fxydcs"),
		                width:50  		 
					},{
						fieldLabel: '�������',
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
			},{
	            xtype:'fieldset',
	            title: '����',
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