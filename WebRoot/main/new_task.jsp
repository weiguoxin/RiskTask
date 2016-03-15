<%@ page contentType="text/html;charset=GBK"%>

<div id="new_taskForm"></div>
<script>
var new_taskForm = new Ext.FormPanel({
	labelAlign: 'right',
	renderTo :'new_taskForm',
	labelWidth: 90,
	frame: true,
	width: 800,
	height: 200,
	buttonAlign : "center",
//	reader : new Ext.data.JsonReader({
//		root : "r",
//		success : "success"
//	}, fields),
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
				 name:'zxr_dm',
				 hiddenName:'zxr_dm',
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
                })
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
		text:'����',
		icon:'extjs/resources/button/win_yes.gif',
		handler : function(){
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
							if(f == 'yes'){new_taskForm.form.reset();}
							
						});
					}else Ext.Msg.alert('��ʾ','����ʧ��!ԭ��:'+action.res);
				}
			});
		}
	}]
});
</script>