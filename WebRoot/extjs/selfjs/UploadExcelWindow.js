/**
 * @author _wgx
 * @date 2014��8��27��
 * @class UpLoadExcelWindow
 * @extends Ext.Window
 * @description excel�ϴ�����
 */
UpLoadExcelWindow = Ext.extend(Ext.Window, {
			// excel�ϴ���
			excelFormPanel : null,
			ClickForm       : null,
			textHidden: true,
			width : 330,
			height : 120,
			url: "personsys/importexcel.jsp",
			title : "����excel�ļ�",
			plain : true,
			modal : true, // ģ̬����
			closeAction : "hide",
			flushGrid: false,
			actionType:'',
			TemplatesLink:'<a href="docs/task.xls" target="_blank">����EXCELģ��</a>',
			/**
			 * ���췽��
			 */
			constructor : function(cfg) {
				UpLoadExcelWindow.superclass.constructor.call(this, cfg);
				//alert(this.textHidden);
				// excel�ϴ���
				this.excelFormPanel = new Ext.form.FormPanel({
							fileUpload : true, // �����ϴ��ļ�
							labelWidth : 90,
							layout : "form",
							baseCls : "x-plain", // ͳһ����ɫ
							bodyStyle : "padding:10px;",
							items : [							         
							          {xtype:'hidden',name:'act',value:this.actionType},{
											fieldLabel : '���������',
											xtype : 'textfield',
											anchor : '95%',
											forceSelection : true,
											selectOnFocus : true,
											editable : false,
											allowBlank : true,
											hidden: this.textHidden,
											name : 'name'
										},{
										xtype : 'fileuploadfield',
										name : "excelfile",
										allowBlank : false,
										fieldLabel : 'ѡ��excel�ļ�',
										anchor : "95%",
										buttonCfg : {
											text : 'ѡ��'
										}
							          },{
							        	  xtype:'displayfield',
										  fieldLabel : '����EXCELģ��',
							        	  html: this.TemplatesLink
							          }
							         ]
						});
				UpLoadExcelWindow.superclass.constructor.call(this, {
							items : [this.excelFormPanel],
							listeners : {
								"beforeshow" : this.onBeforeshow,
								scope : this
							},
							buttons : [{
										text : "����",
										handler : this.onUpLoad,
										scope : this
									}, {
										text : "ȡ��",
										handler : this.onCloseWin,
										scope : this
									}]
						});
						
						//Ϊ��ǰ�������Զ����¼�
						this.addEvents("excelUploadSuccess");
			},

			/**
			 * �ϴ���ť�����¼�
			 */
			onUpLoad : function() {
				// �������֤ͨ��,���ύ��
				if (this.excelFormPanel.getForm().isValid() == true) {
					var fileName =this.excelFormPanel.getForm().findField("excelfile").getValue();
			        var extArray = [".xls",".xlsx"];        
			        var ext = fileName.slice(fileName.indexOf(".")).toLowerCase();
			        var flag = 0;
			        //alert(ext);
			        for (var i = 0; i < extArray.length; i++) {
			             if (extArray[i] == ext) { 
			                flag = 1;
			             }
			        } 
			        if(flag == 1){
						this.excelFormPanel.getForm().submit({
							url : this.url,
							waitTitle : "���ݴ���",
							waitMsg : "���ݴ�����,���Ժ�......",
							success : this.onSuccess,
							failure : this.onFailure,
							scope : this
						});			        	
			        }else
			        {
			        	Ext.Msg.alert("ϵͳ��Ϣ","ֻ���ϴ�.xls��excel�ļ�");
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
				Ext.Msg.alert("ϵͳ��Ϣ",_action.result.tipMsg);
				if(this.flushGrid){
					this.flushGrid.store.reload();
				}
				this.hide();
				//excelUploadSuccess(_form,_action);
				//this.fireEvent("onUploadSuccess",_form,_action);
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
				this.excelFormPanel.getForm().reset();
				this.hide();
			},

			/**
			 * ��������ʾǰ���¼�
			 */
			onBeforeshow : function() {
				//alert(this.ClickForm);
				this.excelFormPanel.getForm().reset();
			}
		});