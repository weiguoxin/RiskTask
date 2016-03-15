<%@ page contentType="text/html;charset=GBK"%>
		<div id="noticeGrid" ></div>
		<script>
		var noticeGrid = function( ) {
			//��������
	    // 1.create the Data Store
		    var store = new Ext.data.JsonStore({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        successProperty:'success',
		        idProperty: 'id',
		        remoteSort: false,		        
		        fields: ['id', 'title', {name: 'createdate',type:'date',dateFormat:'Y/m/d H:i:s'}, 'notice'],
		
		        // load using script tags for cross domain, if the data in on the same
				// domain as
		        // this page, an HttpProxy would be better
		        proxy: new Ext.data.HttpProxy({
		            url: 'WLUserData?action=shownotice'
		        })
		    });	
		    function renderToNotice(v,p,r)
		    {
				if(v==1)
				{
					return "<img src='extjs/resources/button/already_incept.gif'/>";
				}else
				{
					return "<img src='extjs/resources/button/win_exit.gif'";
				}	
			}
			/*-- ontimeʱ��ת��--*/
		    function renderToNowz(value){
		        return String.format(value.dateFormat('Y��m��d�� H:i:s'));
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
			        width: 50
			    },{
			        header: "����",
			        dataIndex: 'title',
			        width: 100
			    },{
			        header: "�Ƿ񹫸�",
			        dataIndex: 'notice',
			        width: 20,
			        renderer: renderToNotice
			    },{
			        header: "����ʱ��",
			        dataIndex: 'createdate',
			        renderer: renderToNowz,
			        width: 60
			    }
			  ]);
			  	
			var arrbars = new Array(3); // 3 rows
			
			arrbars[0]={text:'���',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_add.gif',handler:noticeaddRecord};

			arrbars[1]={text:'�޸�',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_edit.gif',handler:noticeupdateRecord};
					
			arrbars[2]={text:'ɾ��',cls: 'x-btn-text-icon details',icon:'extjs/resources/button/tool_delete.gif',handler:noticedeleteRecord};

		  		    
			noticeGrid.superclass.constructor.call(this, {
				title     : 'ϵͳ����',
				renderTo  : 'noticeGrid',
				autoScroll: true,
				width     : '100%',
				height    : '400',
				//autoHeight: true,
		        store: store,
		        // ���������ȥʱ�Ƿ������ʾ
		        trackMouseOver:true,
		        loadMask: true,
		        // grid columns
		        sm:sm,
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
				    				items.push('-');// ��ť֮����ӷָ���
				    				items.push(arrbars[j]);	
				    			} 
		            	return items;
		            }).createDelegate(this)()
		          }),
		            listeners:{
	              	'rowdblclick' : function(grid, rowIndex, e){
		        		noticeupdateRecord();	
	           		 }  
	            }			           		        
			});
			store.load({params:{start:0, limit:15}});	
	}
	Ext.extend(noticeGrid, Ext.grid.GridPanel);	
	var noticegrid = new noticeGrid();


	var noticeForm = new Ext.FormPanel({
		layout : "form", // ��ǰ����Ϊform����
		id:"noticeForm",
		frame : true, // �����ɫ
		labelWidth : 45,
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
	      ['id', 'title', 'notice','doccontent']
		  ),
		
		items : [
			{
			xtype:'hidden',
			name : 'id'
			},{
			fieldLabel : "����",
			emptyText : "���������",
			name : "title",
			allowBlank : false
			}, {
				xtype:'checkbox',
				boxLabel : "�Ƿ�Ϊ����",
				name : "notice",
				inputValue : 1,
				checked:true
			}, {
	            xtype:'htmleditor',
	            name:'doccontent',
	            fieldLabel:'����',
	            fontFamilies : ['����','����','����','Arial','Courier New','Tahoma','Times New Roman','Verdana'],
	            defaultFont: '����',	            
	            height:300,
	            anchor:'98%'
	        }
		],
		buttons:[
					{
						xtype:'button',
						text : 'ȷ���ύ',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_yes.gif',
						handler : noticesubmitForm
					},{
						xtype:'button',
						text : '�ر��˳�',
						cls:'x-btn-text-icon',
						icon:'extjs/resources/button/win_exit.gif',
						handler : function(){
							noticewin.hide();
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
	var noticewin = new Ext.Window({
		width:600,
		layout:'form',
		modal:true,
		plain:true,
		closeAction:'hide',
		resizable:false,
		constrain:false,
		autoScroll:false,
		buttonAlign:'center',		    
		items:[noticeForm]			
	});
	// ��ʾ��Ӵ���
	function noticeaddRecord(){
		noticeForm.form.reset();
		noticeForm.isAdd = true;
		noticewin.setTitle("�������");
		noticewin.show();	

	}


	
	// ��ʾ�޸Ĵ���
	function noticeupdateRecord(){
		var stuList = getnoticeList();
		
		var num = stuList.length;
		//alert(num>=1);
		if(num>1){
			Ext.MessageBox.alert("��ʾ","ÿ��ֻ���޸�һ����Ϣ,���ѡ��������ݣ�Ĭ�������ֻ�Ե�һ����Ч!",function(btn){
			if(btn=='ok')
			{
				noticeForm.form.reset();
				noticeForm.getForm().findField("noticename").disabled = true;
				noticeForm.isAdd = false;
				noticewin.setTitle("�޸�������Ϣ");
				noticewin.show();
				var stuId = stuList[0];
				noticeloadForm(stuId);// ��ȡ������Ϣ����ϸ����	
			}
			});
		}
		if(num==1){
			noticeForm.form.reset();
			noticeForm.isAdd = false;
			noticewin.setTitle("�޸�������Ϣ");
			noticewin.show();
			var stuId = stuList[0];
			noticeloadForm(stuId);// ��ȡ������Ϣ����ϸ����				
		}	
	}
	// ��ʾɾ���Ի���
	function noticedeleteRecord(){
		var stuList = getnoticeList();
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
							delOknotice(stuList);
							}
		 		}
		});			
	}
	// ȡ����ѡ��¼
	function getnoticeList(){
		var recs = noticegrid.getSelectionModel().getSelections();
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
	function delOknotice(stuList){
		var stuIds = stuList.join('-');
		var msgTip = Ext.MessageBox.show({
			title:'��ʾ',
			width : 250,
			msg:'����ɾ����Ϣ���Ժ�......'
		});
		Ext.Ajax.request({
			url : 'WLUserData?action=delnotice',
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
	          if((response.responseText.split("|")[1]/(noticegrid.getBottomToolbar().pageSize))<(noticegrid.getBottomToolbar().getPageData().activePage))
	          {
	        	  noticegrid.store.load({params:{start:0, limit:15}});
		      }else
		      {
		    	  noticegrid.store.reload();
		      }               
			},
			failure : function(response,options){
				msgTip.hide();
     		 Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
			}
		});
	}
	// ���ر�����
	function noticeloadForm(stuId){
		noticeForm.form.load({
			waitMsg : '���ڼ����������Ժ�',// ��ʾ��Ϣ
			waitTitle : '��ʾ',// ����
			url : 'WLUserData?action=getnotice',// �����url��ַ
			params : {id:stuId},
			method:'GET',// ����ʽ
			success:function(form,action){// ���سɹ��Ĵ�����
				 Ext.Msg.alert('��ʾ','���ݼ��سɹ�');
			},
			failure:function(form,action){// ����ʧ�ܵĴ�����
				Ext.Msg.alert('��ʾ','���ݼ���ʧ��');
			}
		});
	}
	// �ύ������
	function noticesubmitForm(){
		// �жϵ�ǰִ�е��ύ������isAddΪtrue��ʾִ���鼮����������false��ʾִ���鼮�޸Ĳ���
		if(noticeForm.isAdd){
			// ����
			if(noticeForm.form.isValid()){
				noticeForm.form.submit({
					clientValidation:true,// ���пͻ�����֤
					waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
					waitTitle : '��ʾ',// ����
					url : 'WLUserData?action=newnotice',// �����url��ַ
					method:'POST',// ����ʽ
					success:function(form,action){// ���سɹ��Ĵ�����
		              noticewin.hide();
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;�����ɹ�!");
		              }			
		              noticegrid.store.reload();
					},
					failure:function(form,action){// ����ʧ�ܵĴ�����

						Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
					}
				});					
			}
		}else{
			// �޸���Ϣ
		if(noticeForm.form.isValid()){
			noticeForm.form.submit({
				clientValidation:true,// ���пͻ�����֤
				waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
				waitTitle : '��ʾ',// ����
				url : 'WLUserData?action=editnotice',// �����url��ַ
				method:'POST',// ����ʽ
				success:function(form,action){// ���سɹ��Ĵ�����
	              noticewin.hide();
	              var result = action.result.success;
	              if(result)
	              {
	                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;&nbsp;�޸ĳɹ�!");
	                  
	              }	
	              //sm.clearSelections();
	              noticegrid.store.reload();
				},
				failure:function(form,action){// ����ʧ�ܵĴ�����
					Ext.Msg.alert("�������","<img src='extjs/resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
				}
			});
		}	
		}
	}	
		</script>