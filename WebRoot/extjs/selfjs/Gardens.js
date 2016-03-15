GRDS = function(){
	return {
		/**
		 * �û�����
		 */	
		renderToDM : function(v,p,r){
			if(v!="" && v!=0)
			{
			   var index = comndm.find('txdm',v);
			   if(index!=-1) return String.format(comndm.getAt(index).get('tame'));   		
			}			
		},
		//��������
		onUserCheck: function(item){
            if(fileForm.form.isValid()){
                fileForm.form.submit({
                    clientValidation:true,// ���пͻ�����֤
                    waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
                    waitTitle : '��ʾ',// ����
                    url : 'GardensData?action=new',// �����url��ַ
                    params : {jsr:item.userid},
                    method:'POST',// ����ʽ
                    success:function(form,action){// ���سɹ��Ĵ�����
                        var result = action.result.success;
                        if(result)
                        {
                            Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;���ͳɹ�!");
                        }
                        fileForm.getForm().reset();
                    },
                    failure:function(form,action){// ����ʧ�ܵĴ�����
                        Ext.Msg.alert("�������","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
                    }
                });
            }
            },
		//�鿴�ļ�
		editFileTab:function(docid,fileid,title,href){
			
			//var autoLoad = {url: 'main/edit_file.jsp',scripts:true};
            var mainPanel = Ext.getCmp('doc-body');
            var id = docid;
            var tab = mainPanel.getComponent(id);
            if(tab){
            	mainPanel.setActiveTab(tab);

                tab.getUpdater().refresh();
            }else{
                var autoLoad = {url: href,scripts:true};

                var p = mainPanel.add(new DocPanel({
                    id: id,
                    title:title,
                    autoLoad: autoLoad
                }));
                mainPanel.setActiveTab(p);
            }           
		},
		//��������
		// ���ر�����
		fileloadForm :function (tarForm,fileid){
			tarForm.form.load({
				waitMsg : '���ڼ����������Ժ�',// ��ʾ��Ϣ
				waitTitle : '��ʾ',// ����
				url : 'GardensData?action=getFile',// �����url��ַ
				params : {fileid:fileid},
				method:'GET',// ����ʽ
				success:function(form,action){// ���سɹ��Ĵ�����
					 Ext.Msg.alert('��ʾ','���ݼ��سɹ�');
				},
				failure:function(form,action){// ����ʧ�ܵĴ�����
					Ext.Msg.alert('��ʾ','���ݼ���ʧ��');
				}
			});
			
		},
		//ͬ�������
		onAgree: function(isok){
			// ����
			if(dsp_fileForm.form.isValid()){
				dsp_fileForm.form.submit({
					clientValidation:true,// ���пͻ�����֤
					waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
					waitTitle : '��ʾ',// ����
					url : 'GardensData?action=new',// �����url��ַ
					params : {isok:isok},
					method:'POST',// ����ʽ
					success:function(form,action){// ���سɹ��Ĵ�����
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;���ĳɹ�!");
		              }			
		              dsp_fileForm.getForm().reset();
		              //ˢ��grid
		              var mainPanel = Ext.getCmp('doc-body');
		              var tab = mainPanel.getComponent('docs-0102');
		              if(tab){
		              	mainPanel.setActiveTab(tab);
		              	tab.getUpdater().refresh();
		              }	
					},
					failure:function(form,action){// ����ʧ�ܵĴ�����
						Ext.Msg.alert("�������","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
					}
				});					
			}			
		},
		//ת������
		onUserTurn: function(item){
			// ת��
			if(dsp_fileForm.form.isValid()){
				dsp_fileForm.form.submit({
					clientValidation:true,// ���пͻ�����֤
					waitMsg : '�����ύ�������Ժ�',// ��ʾ��Ϣ
					waitTitle : '��ʾ',// ����
					url : 'GardensData?action=new',// �����url��ַ
					params : {zsr:item.userid},
					method:'POST',// ����ʽ
					success:function(form,action){// ���سɹ��Ĵ�����
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("��ʾ","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;ת�ͳɹ�!");
		              }			
		              dsp_fileForm.getForm().reset();
					},
					failure:function(form,action){// ����ʧ�ܵĴ�����
						Ext.Msg.alert("�������","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;������δ��Ӧ�����Ժ�����!");
					}
				});					
			}		
		}
	};
}();