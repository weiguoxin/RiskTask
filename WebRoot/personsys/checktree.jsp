<%@ page contentType="text/html;charset=GBK"%>
	<div id="OtherUserGrid" ></div>
	<script>
 	Ext.form.VTypes.telnumVal  = /(^(\d{2,4}[-_����]?)?\d{3,8}([-_����]?\d{3,8})?([-_����]?\d{1,7})?$)|(^0?1[35]\d{9}$)/	;
 	Ext.form.VTypes.telnumText = '��ϵ�绰����(0311-1234567,13999999999)'; 	
 	var data_type1 = '';
 	
	Ext.form.VTypes.telnum 	= function(v){
		return Ext.form.VTypes.telnumVal.test(v);
	};
	/****** ��Grid���Ա���JSON������Ϊ����Դ *******/
	var OtherUserJsonDataGrid = function() {
		//��������
	    var store = new Ext.data.JsonStore({
	        root: 'topics',
	        totalProperty: 'totalCount',
	        successProperty:'success',
	        idProperty: 'id',
	        remoteSort: false,
	        fields: ['userid', 'usercode', 'username','usertype','userquan', 'userkebe','userstatus','telnum','swjg_mc'],
	        proxy: new Ext.data.HttpProxy({
	            url: 'WLUserData?action=show'
	        })
	    });
		/*
		 * ����bbar�еİ�ť
		 * 
		 */    
		var arrbars = new Array(3); // 3 rows
		
		arrbars[0]={text:'ע��',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:useraddRecord};

		arrbars[1]={text:'�޸�',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:userupdateRecord};
				
		arrbars[2]={text:'ɾ��',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:userdeleteRecord};

		arrbars[3]={text:'����',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_key.gif',handler:userliveRecord};

		arrbars[4]={text:'����',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_lock.gif',handler:userdiedRecord};
	    
	    function renderStatus(value, p, r){
			if(value==0)   return String.format('<b style="color:green">δ����</b>');
			if(value==1)   return String.format('����');
			if(value==2)   return String.format('<b style="color:red">����</b>');
	    }
	    String.prototype.trim = function()  
        {  
            return this.replace(/(^\s*)|(\s*$)/g, "");  
        };
	    function renderType(v){
	    	v = v.replace("��",",");
	    	var nn = data_type1.length;
	    	var vv = v.split(",");//alert(v+ " : " +vv.length);
	    	var str = " ";
	    	for(var i=0;i<vv.length;i++){
	    		for(var n=0;n<nn;n++){
	    			//alert(vv[n]);
	    			if(vv[i] == data_type1[n].id){ 
	    				str+=data_type1[n].name+"��";
	    				
	    			}
	    		}
	    	}str=str.substring(0,str.length-1);
	    	return str;
	    }
	    function renderUQuan(value, p, r){
		    if(value=="") 
			    return String.format('��');
		    else
		    	 return String.format('��');
	    }		    	    
		// 2.create the column Model
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel
		([
			 sm,
			 new Ext.grid.RowNumberer({header:"���",width:50}),
			{
		        header: "���",
		        dataIndex: 'id',
		        hidden:true,
		        width: 150
		    },{
		        header: "��¼��",
		        dataIndex: 'usercode',
		        width: 100,
		        sortable: true
		    },{
		        header: "����",
		        dataIndex: 'username',
		        width: 100
		    },{
		        header: "�û���ɫ",
		        dataIndex: 'usertype',
		        width: 150,
		        renderer:renderType
		    },{
		        header: "��������",
		        dataIndex: 'swjg_mc',
		        width: 200
		    },{
		        header: "״̬",
		        dataIndex: 'userstatus',
		        renderer:renderStatus,
		        width: 100,
		        sortable: true
		    }
		  ]);	
		    
		OtherUserJsonDataGrid.superclass.constructor.call(this, {
			title     : '',
			renderTo  : 'OtherUserGrid',
			autoScroll: true,
			width     : '100%',
			height    : '374',
			id        :'OtherUserGridPanel',
			//autoHeight: true,
	        store: store,
	        // ���������ȥʱ�Ƿ������ʾ
	        trackMouseOver:true,
	        loadMask: true,
	        sm: sm,
	        // grid columns
	        cm:cm,
	        // customize view config
	        viewConfig: {
	            forceFit:true
	        }, 
			tbar  : [{
						xtype : 'hidden',
						name : 'swjg_dm',
						id:'findRY_swjg_dm'
					}, '-','˰����أ�',new Ext.form.ComboBoxTree({
						fieldLabel : '��������',
						emptyText : 'ȫ��',
						anchor : '98%',
						mode : 'local',
						displayField : 'text',
						valueField : 'id',
						hiddenName : 'swjg_mc',
						name : 'swjg_mc',
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
					}),'-','����: ', {
				xtype : "textfield",
				id : "swry_name"
			}, "-", {
				text : "�鿴",
				icon:'extjs/resources/button/tool_seh.gif',
				handler : function(){
		  			// ��ȡtop������
		  			var _tbar = this.getTopToolbar();
		  			// ��ȡtext���ֵ
		  			var swjg_dm = _tbar.findById("findRY_swjg_dm").getValue();
		  			var swry_name = _tbar.findById("swry_name").getValue();
		  			store.baseParams = {swjg_dm:swjg_dm,
			  				swry_name:swry_name};
		  			store.load({params:{start:0, limit:15}});
				},
				tooltip:"��������ģ����ѯ",
				scope : this
			}
		    ],		            		
	        bbar: new Ext.PagingToolbar({
	        	/*
				 * ����ÿ����ʾ�ļ�¼���� '-' ����ַ�
				 * pressed:��ʾ��ť���ڿ�ʼ��ʱ���Ƿ񱻰��£�ֻ��enableToggleΪ���ʱ�������
				 * enableToggle����ʾ��ť�ڿ�ʼ��ʱ���Ƿ��ܴ��ڱ����µ�״̬�������ڰ���û����֮���л�������Ƴ�x-btn-pressed��ʽ��
				 * text:��ť������ʾ������ cls����ť��CSS��
				 * toggleHandler:����enableToggleΪTRUEʱ�����ťʱ���¼�������
				 */
	            pageSize: 15,
	            store: store,
	            displayInfo: true,
	            firstText:"��ҳ",
	            lastText:"ĩҳ",
	            nextText:"��һҳ",
	            preText:"��һҳ",
	            refreshText:"ˢ��",
	            displayMsg: '��ʾ �� {0} - {1}����¼���� {2} ��',
	            emptyMsg: "û�м�¼",
	            items:(function(){
	            	// ���ݴ��ݹ�����Ȩ�޲��������а�ť�����
	            	var items = [];
			    			for(var j=0;j<arrbars.length;j++)
			    			{
			    					items.push('-');// ��ť֮����ӷָ���
			    					items.push(arrbars[j]);	
			    			} 
	            	return items;
	            }).createDelegate(this)()
	          }),
	            listeners:{
              	'rowdblclick' : function(grid, rowIndex, e){
              		userupdateRecord();
	            	var userid = [grid.store.getAt(rowIndex).get("userid")];
	            	iniUpForm(userid);
	            	//otherUserForm.form.reset();	            		
            	}  
            }			          		           		        
		});
		Ext.Ajax.request({
			   url: 'WLUserData?action=getRole',
			   success: function(resp){
				   var r = Ext.util.JSON.decode(resp.responseText);  
				   data_type1 = r.data;
				   store.load({params:{start:0, limit:15}});
			   }
			});
				
	};
	Ext.extend(OtherUserJsonDataGrid, Ext.grid.GridPanel);
	var otheruserdatagrid = new OtherUserJsonDataGrid();
	
	var otherUserForm = new Ext.FormPanel({
		layout : "form", // ��ǰ����Ϊform����
		id:"otherUserForm",
		frame : true, // �����ɫ
		labelWidth : 55,
		baseCls : "x-plain", // ͳһ����ɫ
		bodyStyle : "padding:10px;", // css���10������
		//mydisabled : false,
		defaults : {
			xtype : "textfield",
			anchor : "95%"
		},
	      reader : new Ext.data.JsonReader({
	    	  root:"data",
	    	  success:"success"
	      },
	      ['userid', 'usercode', 'username','usertype','userquan', 'userkebe','userstatus','telnum','swjg_mc','userrole']
		  ),
		
		items : [
			{
				xtype:'hidden',
				name : 'userkebe'
			},
			{
				xtype:'hidden',
				name : 'userid'
			},{
			fieldLabel : "�û���",
			emptyText : "�������û���",
			disabled : false,  //ָ���ÿؼ��Ƿ����(mydisabledΪ�Զ�������)
			name : "usercode",
			allowBlank : false
				// ������Ϊ��
			}, {
			fieldLabel : "����",
			name : "uerPass1",
			inputType : "password",
			allowBlank : false
				// ������Ϊ��
			}, {
			fieldLabel : "ȷ������",
			inputType : "password",
			name : "uerPass2",
			invalidText : "������������벻һ��", // ��֤ʧ�ܳ��ֵ���ʾ
			allowBlank : false, // ������Ϊ��
			validator : function() { // ��֤
				var _pwd1 = this.ownerCt.items.itemAt(3)
						.getValue(1);
				var _pwd2 = this.getValue();
				if (_pwd1 == _pwd2) {
					return true;
				} else {
					return false;
				}
			}
		},{
			fieldLabel : "��ʵ����",
			emptyText : "��������ʵ����",
			name : "username",
			allowBlank : false
		},new Ext.ux.form.LovCombo({
						fieldLabel:'��ɫ',
						width:300,
						hideOnSelect:false,
			            editable: false,
			            name:'usertype',
						hiddenName:'usertype',
						maxHeight:200,
						store:new Ext.data.JsonStore({
						      root: 'data',
						      fields: ['id','name'],
						      proxy: new Ext.data.HttpProxy({
						          url: 'WLUserData?action=getRole'
						      }),
						      autoLoad:true
					 	 })
						,triggerAction:'all'
						,valueField:'id',
						 displayField: "name"
						,mode:'local'
					}),
			new Ext.form.ComboBoxTree({
				fieldLabel: '��������',
				isFormField:true,
				emptyText: 'ѡ��..',
				anchor: '100%',
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
                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true}),
                	listeners:{
                    	"load": function(node) {  
    			 	     }
                	}
                }),
                listeners :{
                	"expand": function(c){
                		c.list.setWidth('320px'); 
                        c.innerList.setWidth('auto');
                	} 
                }
          }),
          new Ext.ux.tree.CheckTreePanel({
              name:'userquan',
              isFormField:true,
              bubbleCheck:'none',
              cascadeCheck:'all',
              collapsible: true,
              fieldLabel:'�û�Ȩ��',
              height:180,
              autoScroll:true,
              border:true,
              bodyStyle:'background-color:white;border:1px solid #B5B8C8',
              rootVisible:false,
              loader: new Ext.tree.TreeLoader({
                  preloadChildren: true,
                  clearOnLoad: false,
                  dataUrl:'TaskManData?action=treeQuanXian'
              }),
              root: new Ext.tree.AsyncTreeNode({
              	 text:'',
                  id:'root',
                 expanded:true
              })
          })
        ],
		buttons:[
					{
						xtype:'button',
						text : 'ȷ���ύ',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_yes.gif',
						handler : usersubmitForm
					},{
						xtype:'button',
						text : '�ر��˳�',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_exit.gif',
						handler : function(){
							userwin.hide();
						}					
					}
			]			
	});
	
	// ������������
	/*
	 * 1.closeAction:ö��ֵΪ��close(Ĭ��ֵ)��������رպ󣬹ر�window���� hide,�رպ�ֻ�� hidden����
	 * 2.closable:true�����Ͻ���ʾС���Ĺرհ�ť��Ĭ��Ϊtrue 3.constrain��true
	 * ��ǿ�ƴ�window������viewport��Ĭ��Ϊfalse 4.modal:trueΪģʽ���ڣ���������ݶ����ܲ�����Ĭ��Ϊfalse
	 * 5.plain��//true�����屳��͸����false��������С���ı���ɫ��Ĭ��Ϊfalse
	 */
	var userwin = new Ext.Window({
		width:350,
		layout:'form',
		modal:true,
		plain:true,
		closeAction:'hide',
		collapsible : true, // ������ť
		resizable:false,
		constrain:false,
		autoScroll:false,
		buttonAlign:'center',		    
		items:[otherUserForm]			
	});
	// ��ʾ��Ӵ���
	userwin.show();	
	userwin.hide();
	function useraddRecord(){
		otherUserForm.getForm().reset();
		otherUserForm.isAdd = true;
		otherUserForm.getForm().findField("usercode").enable();
		userwin.setTitle("ע���û�");
		userwin.show();	
	}


	
	// ��ʾ�޸Ĵ���
	function userupdateRecord(){
		var stuList = getUserList();
		var num = stuList.length;
		if(num>1){
			Ext.MessageBox.alert("��ʾ","ÿ��ֻ���޸�һ����Ϣ,���ѡ��������ݣ�Ĭ�������ֻ�Ե�һ����Ч!",function(btn){
			if(btn=='ok'){
				iniUpForm(stuList);
			}});
		}
		if(num==1){
			iniUpForm(stuList);
		}	
	}
	function iniUpForm(stuList){
		var stuId = stuList[0];
		var n = otheruserdatagrid.store.getCount();
		var v = '';
		for(var i=0;i<n;i++){
			if(otheruserdatagrid.store.getAt(i).get("userid")==stuId){
				v = otheruserdatagrid.store.getAt(i).get("swjg_mc");
			}
		}
		otherUserForm.form.reset();
		otherUserForm.getForm().findField("usercode").disable();
		otherUserForm.getForm().findField("uerPass1").allowBlank = true;
		otherUserForm.getForm().findField("uerPass2").allowBlank = true;			
		otherUserForm.isAdd = false;
		userwin.setTitle("�޸��û���Ϣ");
		userloadForm(stuId,v);// ��ȡ������Ϣ����ϸ����		
		
			
	}
	// ���ر�����
	function userloadForm(stuId,v){
		otherUserForm.form.load({
			waitMsg : '���ڼ����������Ժ�...',
			waitTitle : '��ʾ',
			url : 'WLUserData?action=getform',
			params : {userid:stuId},
			method:'post',
			success:function(form,action){
				otherUserForm.getForm().findField("swjg_mc").setValue(v);
				userwin.show();
			},
			failure:function(form,action){// ����ʧ�ܵĴ�����
				Ext.Msg.alert('��ʾ','���ݼ���ʧ��');
			}
		});
	}
	// ��ʾɾ���Ի���
	function userdeleteRecord(){
		var stuList = getUserList();
		var num = stuList.length;
		if(num == 0){
			Ext.Msg.alert("��ʾ","������ѡ��һ������Ȼ����ܹ�ɾ��!");
			return;
		}
		Ext.Msg.confirm("ȷ����Ϣ��","�Ƿ�ȷ����ѡ�е��н���ɾ��������ɾ�����ܹ��ָ�!",
		function(btn,txt)
		 {
		 		if(btn=='no')
		 				return false;
		 		else
		 		{
		 					if(btn == 'yes'){
							delOkUser(stuList);
							}
		 		}
		});			
	}
	// ȡ����ѡ��¼
	function getUserList(){
		var recs = otheruserdatagrid.getSelectionModel().getSelections();
		var list = [];
		if(recs.length == 0){
			Ext.MessageBox.alert('��ʾ','��ѡ��Ҫ���в����ļ�¼��');
		}else{
			for(var i = 0 ; i < recs.length ; i++){
				var rec = recs[i];
				list.push(rec.get('userid'));
			}
		}
		return list;
	}
	// ɾ����ajax����
	function delOkUser(stuList){
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'��ʾ',
			width : 250,
			msg:'����ɾ����Ϣ���Ժ�......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=delete',
			params : {delid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
     			var result = response.responseText;
     	          switch (result.split("|")[0])
     	          {
     	             case '1': result='ɾ���ɹ���';break;
     	             case '0': result='ɾ��ʧ�ܣ�';break;
     	          }
      		 Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;"+result);
	          if((response.responseText.split("|")[1]/(otheruserdatagrid.getBottomToolbar().pageSize))<(otheruserdatagrid.getBottomToolbar().getPageData().activePage))
	          {
	        	  otheruserdatagrid.store.load({params:{start:0, limit:15}});
		      }else
		      {
		    	  otheruserdatagrid.store.reload();
		      }               
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
			}
		});
	}
	
	// �ύ������
	function usersubmitForm(){
		// �жϵ�ǰִ�е��ύ������isAddΪtrue��ʾִ���鼮����������false��ʾִ���鼮�޸Ĳ���
		if(otherUserForm.isAdd){
			// ����
			if(otherUserForm.form.isValid()){
				otherUserForm.form.submit({
					clientValidation:true,// ���пͻ�����֤
					waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
					waitTitle : '��ʾ',// ����
					url : 'WLUserData?action=new',// �����url��ַ
					method:'POST',// ����ʽ
					success:function(form,action){// ���سɹ��Ĵ�����
		              userwin.hide();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;�����ɹ�!");
		              }			
		              otheruserdatagrid.store.reload();
					},
					failure:function(form,action){// ����ʧ�ܵĴ�����
						//alert(action.result.because);
						//alert(action.result.because!="");
						if(action.result.because!="")
						{
							Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;���û����Ѵ���!");								
						}else
						{
						Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
						}
					}
				});					
			}
		}else{
			// �޸���Ϣ
		if(otherUserForm.form.isValid()){
			otherUserForm.form.submit({
				clientValidation:true,// ���пͻ�����֤
				waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
				waitTitle : '��ʾ',// ����
				url : 'WLUserData?action=edit',// �����url��ַ
				method:'POST',// ����ʽ
				success:function(form,action){// ���سɹ��Ĵ�����
	              userwin.hide();
	              var result = action.result.success;
	              if(result)
	              {
	                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;�޸ĳɹ�!");
	                  
	              }
	              otheruserdatagrid.store.reload();
				},
				failure:function(form,action){// ����ʧ�ܵĴ�����
					Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
				}
			});
		}	
		}
	}
	//�����û�
	function userliveRecord(){
		var stuList = getUserList();
		//alert(stuList.length);
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'��ʾ',
			width : 250,
			msg:'���ڼ����û����Ժ�......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=liveuser&type=1',
			params : {userid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
		    	otheruserdatagrid.store.reload();             
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
			}
		});
	
	}
	//�����û�
	function userdiedRecord(){
		var stuList = getUserList();
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'��ʾ',
			width : 250,
			msg:'���ڼ����û����Ժ�......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=liveuser&type=2',
			params : {userid:stuIds},
			method : 'POST',
			success : function(response,options){
				msgTip.hide();
		    	otheruserdatagrid.store.reload();             
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
				
		}
		});
		
	}
											
	</script>