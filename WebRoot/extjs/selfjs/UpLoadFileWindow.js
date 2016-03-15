/**
 * @author ���
 * @date 2009��3��10��
 * @class UpLoadFileWindow
 * @extends Ext.Window
 * @description ��Ա������Ϣ��Ƭ�ϴ�����
 */
UpLoadFileWindow = Ext.extend(Ext.Window, {
			// ͼƬ�ϴ���
			upLoadFormPanel : null,
			/**
			 * ���췽��
			 */
			constructor : function() {
			//alert(a);
				// ͼƬ�ϴ���
				this.upLoadFormPanel = new Ext.form.FormPanel({
							fileUpload : true, // �����ϴ��ļ�
							labelWidth : 55,
							layout : "form",
							baseCls : "x-plain", // ͳһ����ɫ
							bodyStyle : "padding:10px;",
							items : [
							          {
										xtype : 'fileuploadfield',
										name : "photo",
										allowBlank : false,
										fieldLabel : '�ļ���',
										anchor : "95%",
										buttonCfg : {
											text : 'ѡ��'
										}
							          }
							         ]
						});
				UpLoadFileWindow.superclass.constructor.call(this, {
							title : "�ļ��ϴ�",
							width : 310,
							height : 110,
							plain : true,
							modal : true, // ģ̬����
							closeAction : "hide",
							items : [this.upLoadFormPanel],
							listeners : {
								"beforeshow" : this.onBeforeshow,
								scope : this
							},
							buttons : [{
										text : "�ϴ�",
										handler : this.onUpLoad,
										scope : this
									}, {
										text : "ȡ��",
										handler : this.onCloseWin,
										scope : this
									}]
						});
						
						//Ϊ��ǰ�������Զ����¼�
						this.addEvents("onUploadSuccess");
			},

			/**
			 * �ϴ���ť�����¼�
			 */
			onUpLoad : function() {
				// �������֤ͨ��,���ύ��
				if (this.upLoadFormPanel.getForm().isValid() == true) {
					var fileName =this.upLoadFormPanel.getForm().findField("photo").getValue();
			        var extArray = new Array(".docx", ".doc", ".pdf");       
			        var ext = fileName.slice(fileName.indexOf(".")).toLowerCase();
			        var flag = 0;
			        for (var i = 0; i < extArray.length; i++) 
			        {
			             if (extArray[i] == ext) 
			             { 
			                flag = 1;
			             }
			        } 
			        if(flag == 1)
			        {
						this.upLoadFormPanel.getForm().submit({
							url : "main/upload.jsp",
							waitTitle : "���ݴ���",
							waitMsg : "���ݴ�����,���Ժ�......",
							success : this.onSuccess,
							failure : this.onFailure,
							scope : this
						});			        	
			        }else
			        {
			        	Ext.Msg.alert("ϵͳ��Ϣ","ֻ���ϴ�word�ļ�");
			        }

				}
			},

			/**
			 * �ϴ��ɹ��ص�����
			 * 
			 * @param {}
			 *            _form
			 * @param {}
			 *            _action
			 */
			onSuccess : function(_form, _action) {
				onUploadSuccess(_form,_action);
				this.hide();
			},

			/**
			 * �ϴ�ʧ�ܻص�����
			 * 
			 * @param {}
			 *            _form
			 * @param {}
			 *            _action
			 */
			onFailure : function(_form, _action) {
				Ext.Msg.alert("ϵͳ��Ϣ",_action.result.tipMsg);
			},

			/**
			 * ȡ����ť�����¼�
			 */
			onCloseWin : function() {
				this.upLoadFormPanel.getForm().reset();
				this.hide();
			},

			/**
			 * ��������ʾǰ���¼�
			 */
			onBeforeshow : function() {
				//alert(this.ClickForm);
				this.upLoadFormPanel.getForm().reset();
			}
		});