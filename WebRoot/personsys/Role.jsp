<%@ page contentType="text/html;charset=GBK"%>
		<div id="RoleGridPanel"></div>
		<script>
		var rolepriv = "1,2";
		/****** ��Grid���Ա���JSON������Ϊ����Դ *******/
		var RoleJsonDataGrid = function() {
			//��������
	    // 1.create the Data Store
		    var store = new Ext.data.JsonStore({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        successProperty:'success',
		        idProperty: 'id',
		        remoteSort: false,
		        
		        fields: ['id', 'name', 'detail'],
		
		        // load using script tags for cross domain, if the data in on the same
				// domain as
		        // this page, an HttpProxy would be better
		        proxy: new Ext.data.HttpProxy({
		            url: 'RoleData?action=show'
		        }),
		         sortInfo: {field: 'id', direction: 'DESC'}
		    });
			/*
			 * ����bbar�еİ�ť
			 * 
			 */    
			var arrbars = new Array(3); // 3 rows
			
			arrbars[0]={text:'����',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:roleaddRecord};

			arrbars[1]={text:'�޸�',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:roleupdateRecord};
					
			arrbars[2]={text:'ɾ��',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:roledeleteRecord};
													    
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
			        width: 200
			    },			{
			        header: "��ɫ����",
			        dataIndex: 'name',
			        width: 100,
			        sortable: true
			    },{
			        header: "��ע",
			        dataIndex: 'detail',
			        width: 100
			    }
			  ]);	
		  		    
		    RoleJsonDataGrid.superclass.constructor.call(this, {
				title     : '',
				autoScroll: true,
				width     : '100%',
				height    : '250',
				id        :'RoleGridPanel',
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
				    				if(rolepriv.indexOf(j.toString())>=0)
				    				{
				    					items.push('-');// ��ť֮����ӷָ���
				    					items.push(arrbars[j]);	
				    				}
				    			} 
		            	return items;
		            }).createDelegate(this)()
		          }),
		            listeners:{
		              	'rowdblclick' : function(grid, rowIndex, e){
		              	//alert(grid.store.getAt(rowIndex).get("id"));
		            	var id = grid.store.getAt(rowIndex).get("id");
						roleForm.form.reset();
						var selNodes = tree.getChecked();
					    Ext.each(selNodes, function(node){
					         node.attributes.checked = false;
					         node.ui.checkbox.checked = false;
					    });		
		            	roleloadForm(id);
		        		//var infoForm = new InfoDetailFrom(id);
		        		//mtp.addTab('view_'+id, '�鿴�ͻ� '+name+' ��ά����Ϣ', infoForm);
		              	/*
		              	 * (grid.getSelectionModel().getSelections())[0].get('id')
		              	 * grid.store.getAt(rowIndex).get("id")
		              	 */
		              	
		            }  
		            }		           		        
			});
			store.load({params:{start:0, limit:15}});		
		};
		
		Ext.extend(RoleJsonDataGrid, Ext.grid.GridPanel);
		var roledatagrid = new RoleJsonDataGrid();
		
		//����Ȩ����
	    var tree = new Ext.ux.tree.CheckTreePanel({
	        rootVisible:false,
	        lines:true,isFormField:true,
	        autoScroll:true,
	        animCollapse:false,
	        animate: true,
	        frame: true,
	        name:'menuId',
	        loader: new Ext.tree.TreeLoader({
				preloadChildren: true,
				clearOnLoad: false,
				dataUrl:'SysRoleMenus'
			}),
	        root: new Ext.tree.AsyncTreeNode({
	            text:'Ext JS',
	            id:'root',
	            expanded:true
	         })
	    });
	    tree.getRootNode().expand(true);
		tree.on('checkchange11111', function(node, checked) { 
			if(checked){
				//��Ϊ�ӽڵ�ѡ��,��ѡȡ�и��ڵ�
				if(node.isLeaf())
				{
					   //��ȡ���и��ڵ�
				      var pNode = node.parentNode;
				      //����ڵ㲻Ϊrootʱִ��
				      if(pNode.id!='01')
				      {
				    	  if (tree.getChecked(id, node.parentNode) == "") {
					            pNode.ui.checkbox.checked = checked;
					            pNode.attributes.checked = checked;
					     }  	    	  
				      }				
				}else
				{
					node.attributes.checked = checked;   
				    //��ȡ�����ӽڵ�,��Ϊѡ��״̬
				    node.cascade( function( node ){
				         node.attributes.checked = checked;
				         node.ui.checkbox.checked = checked;
				     }); 					
				}
			}else
			{
				if(node.isLeaf())
				{
					//��Ϊ�ӽڵ㲻ѡ��ʱ,�ȱ�Ϊ���ڵ�,�鿴�����ӽڵ��Ƿ�ȫ��ѡ,��ȫ��ѡ�򸸽ڵ�䲻ѡ
					var pNode = node.parentNode;
				      if(pNode.id!='01')
				      {					
						var flag = true;
						pNode.eachChild( function( pNode ){
							//alert(pNode.attributes.checked);
							if(pNode.attributes.checked) 
							{
								flag = false;
							}
						});
						if(flag) 
						{
							pNode.attributes.checked = checked; 
							pNode.ui.checkbox.checked = checked;
						} 
				      }	 
				}else
				{
					node.attributes.checked = checked;   
				    //��ȡ�����ӽڵ�,ȫ�ı�Ϊ��ѡ��״̬
				    node.cascade( function( node ){
				         node.attributes.checked = checked;
				         node.ui.checkbox.checked = checked;
				     }); 					
				}
			}			

	      
		}, tree);  
		//�����ɫForm	    	    		
		var roleForm = new Ext.FormPanel({
			labelAlign: 'left',
		    buttonAlign: 'right',
		    bodyStyle: 'padding:5px',
		    frame: true,
		    labelWidth: 88,
			defaults : {
				xtype : "textfield",
				anchor : "95%"
			},
		      reader : new Ext.data.JsonReader({
		    	  root:"data",
		    	  success:"success"
			      },
			      ['id', 'name', 'detail','menuId','add_priv','update_priv','del_priv','data_range']
			  ),
			
			items : [
				{
				xtype:'hidden',
				name : 'id'
				}/*,{
					xtype:'hidden',
					name : 'menuId'
				}*/
				,{
				fieldLabel : "��ɫ����",
				emptyText : "�������ɫ����",
				//disabled : this.mydisabled,  //ָ���ÿؼ��Ƿ����(mydisabledΪ�Զ�������)
				name : "name",
				allowBlank : false
				},{
				fieldLabel : "��ע",
				xtype : "textfield", // �����ı���
				name : "detail" // name
				},{
					xtype: 'fieldset',
			        title: 'Ȩ��',
			        layout: 'column',
			        height: '100%',
			        items: [tree]			
				}
				],
			buttons:[
						{
							xtype:'button',
							text : 'ȷ���ύ',
							cls:'x-btn-text-icon',
							icon:'extjs/resources/button/win_yes.gif',
							handler: rolesubmitForm
						},{
							xtype:'button',
							text : '����',
							cls:'x-btn-text-icon',
							icon:'extjs/resources/button/win_exit.gif',
							handler : function(){
							roleForm.form.reset();
							var selNodes = tree.getChecked();
						    Ext.each(selNodes, function(node){
						         node.attributes.checked = false;
						         node.ui.checkbox.checked = false;
						    });							
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
		var rolewin = new Ext.Window({
			width:350,
			height:500,
			layout:'form',
			modal:true,
			plain:true,
			closeAction:'hide',
			collapsible : true, // ������ť
			resizable:false,
			constrain:false,
			autoScroll:false,
			buttonAlign:'center',		    
			items:[roleForm]				
		});
		// ��ʾ��Ӵ���
		function roleaddRecord(){
			roleForm.form.reset();
			roleForm.isAdd = true;
			rolewin.setTitle("������ɫ");
			rolewin.show();		
		}
		// ��ʾ�޸Ĵ���
		function roleupdateRecord(){
			var stuList = getRoleList();
			
			var num = stuList.length;
			//alert(num>=1);
			if(num>1){
				Ext.MessageBox.alert("��ʾ","ÿ��ֻ���޸�һ����Ϣ,���ѡ��������ݣ�Ĭ�������ֻ�Ե�һ����Ч!",function(btn){
				if(btn=='ok')
				{
					roleForm.form.reset();
					var selNodes = tree.getChecked();
				    Ext.each(selNodes, function(node){
				         node.attributes.checked = false;
				         node.ui.checkbox.checked = false;
				    });					

					var stuId = stuList[0];
					roleloadForm(stuId);// ��ȡ������Ϣ����ϸ����	
				}
				});
			}
			if(num==1){
				roleForm.form.reset();
				var selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			         node.attributes.checked = false;
			         node.ui.checkbox.checked = false;
			    });					
				
				var stuId = stuList[0];
				roleloadForm(stuId);// ��ȡ������Ϣ����ϸ����				
			}	
		}
		// ��ʾɾ���Ի���
		function roledeleteRecord(){
			var stuList = getRoleList();
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
								delOkRole(stuList);
								}
    		 		}
    		});			
		}
		// ȡ����ѡ��¼
		function getRoleList(){
			var recs = roledatagrid.getSelectionModel().getSelections();
			var list = [];
			if(recs.length == 0){
				Ext.MessageBox.alert('��ʾ','��ѡ��Ҫ���в����ļ�¼��');
			}else{
				for(var i = 0 ; i < recs.length ; i++){
					var rec = recs[i];
					list.push(rec.get('id'))
				}
			}
			return list;
		}
		// ɾ����ajax����
		function delOkRole(stuList){
			var stuIds = stuList.join('-');
			var msgTip = Ext.MessageBox.show({
				title:'��ʾ',
				width : 250,
				msg:'����ɾ����Ϣ���Ժ�......'
			});
			Ext.Ajax.request({
				url : 'RoleData?action=delete',
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
		          if((response.responseText.split("|")[1]/(roledatagrid.getBottomToolbar().pageSize))<(roledatagrid.getBottomToolbar().getPageData().activePage))
		          {
		        	  roledatagrid.store.load({params:{start:0, limit:15}});
			      }else
			      {
			    	  roledatagrid.store.reload();
			      }               
				},
				failure : function(response,options){
					msgTip.hide();
         		 Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
				}
			});
		}
		// ���ر�����
		function roleloadForm(stuId){
			roleForm.form.load({
				waitMsg : '���ڼ����������Ժ�',// ��ʾ��Ϣ
				waitTitle : '��ʾ',// ����
				url : 'RoleData?action=getform',// �����url��ַ
				params : {id:stuId},
				method:'GET',// ����ʽ
				success:function(form,action){// ���سɹ��Ĵ�����
					/*if(action.result["data"].menuId!=""){
	         			var menuarray = action.result["data"].menuId.split(", ");
	       	          	for(var i=0;i<menuarray.length-1;i++)
	       	          	{
	       	          		var node = tree.getNodeById(menuarray[i]);
	       	          		if(node.id=='01') continue;
	       	          		node.attributes.checked = true;
	       	        	 	node.ui.checkbox.checked = true;       	          		
	           	         }			
					}	*/
					//form.findField("uerPass1").setValue(action.result["data"].password);
					//form.findField("uerPass2").setValue(action.result["data"].password);
					 Ext.Msg.alert('��ʾ','���ݼ��سɹ�');
					 //roledatagrid.store.reload();
				},
				failure:function(form,action){// ����ʧ�ܵĴ�����
					Ext.Msg.alert('��ʾ','���ݼ���ʧ��');
				}
			});
		}
		// �ύ������
		function rolesubmitForm(){
			// �жϵ�ǰִ�е��ύ������isAddΪtrue��ʾִ���鼮����������false��ʾִ���鼮�޸Ĳ���
			//alert(roleForm.getForm().findField("id").getValue()=="");
			if(roleForm.getForm().findField("id").getValue()==""){
				// ����
			    var msg = '', selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			        if(msg.length > 0){
			            msg += ',';
			        }
			        msg += node.id;
			    });
			    //roleForm.getForm().findField("menuId").setValue(msg);
				if(roleForm.form.isValid()){        		
					roleForm.form.submit({
						clientValidation:true,// ���пͻ�����֤
						waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
						waitTitle : '��ʾ',// ����
						url : 'RoleData?action=new',// �����url��ַ
						method:'POST',// ����ʽ
						success:function(form,action){// ���سɹ��Ĵ�����
			              //userwin.hide();
				          form.reset(); 
						    Ext.each(selNodes, function(node){
						         node.attributes.checked = false;
						         node.ui.checkbox.checked = false;
						    });
			              var result = action.result.success;
			              if(result)
			              {
			                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;�����ɹ�!");
			              }		
			              roledatagrid.store.reload();	
			              //userdatagrid.store.reload();
						},
						failure:function(form,action){// ����ʧ�ܵĴ�����
							Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
						}
					});					
				}	
			}else{
				// �޸���Ϣ
			    var msg = '', selNodes = tree.getChecked();
			    Ext.each(selNodes, function(node){
			        if(msg.length > 0){
			            msg += ',';
			        }
			        msg += node.id;
			    });
			    //roleForm.getForm().findField("menuId").setValue(msg);				
				roleForm.form.submit({
					clientValidation:true,// ���пͻ�����֤
					waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
					waitTitle : '��ʾ',// ����
					url : 'RoleData?action=edit',// �����url��ַ
					method:'POST',// ����ʽ
					success:function(form,action){// ���سɹ��Ĵ�����
					//��form��tree���
						roleForm.form.reset();
					    Ext.each(selNodes, function(node){
					         node.attributes.checked = false;
					         node.ui.checkbox.checked = false;
					    });
					    roledatagrid.store.reload();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;�޸ĳɹ�!");
		                  
		              }	
		             
					},
					failure:function(form,action){// ����ʧ�ܵĴ�����
						Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
					}
				});
			}
		}		
		
		Ext.override(Ext.form.BasicForm,{ 
		    findField : function(id){         
		        var field = this.items.get(id);         
		        if(!field){ 
		            this.items.each(function(f){ 
		                if(f.isXType('radiogroup')||f.isXType('checkboxgroup')){ 
		                    f.items.each(function(c){ 
		                        if(c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id)){ 
		                            field = c; 
		                            return false; 
		                        } 
		                    }); 
		                } 
		                                 
		                if(f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)){ 
		                    field = f; 
		                    return false; 
		                } 
		            }); 
		        } 
		        return field || null; 
		    }  
		});
		new Ext.Panel({
			renderTo:'RoleGridPanel',
			layout:"column",
			border:false,
			items:[{
				columnWidth:.7,
				title:'��ɫ��Ϣ',
				items:roledatagrid
				
			},{
				columnWidth:.3,
				title:'',
				items:roleForm
			}]
		});
        </script>	