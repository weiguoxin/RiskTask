/**
 * @author 侯非
 * @date 2009年3月10日
 * @class UpLoadFileWindow
 * @extends Ext.Window
 * @description 人员档案信息照片上传窗体
 */
UpLoadFileWindow = Ext.extend(Ext.Window, {
			// 图片上传表单
			upLoadFormPanel : null,
			/**
			 * 构造方法
			 */
			constructor : function() {
			//alert(a);
				// 图片上传表单
				this.upLoadFormPanel = new Ext.form.FormPanel({
							fileUpload : true, // 允许上传文件
							labelWidth : 55,
							layout : "form",
							baseCls : "x-plain", // 统一背景色
							bodyStyle : "padding:10px;",
							items : [
							          {
										xtype : 'fileuploadfield',
										name : "photo",
										allowBlank : false,
										fieldLabel : '文件：',
										anchor : "95%",
										buttonCfg : {
											text : '选择'
										}
							          }
							         ]
						});
				UpLoadFileWindow.superclass.constructor.call(this, {
							title : "文件上传",
							width : 310,
							height : 110,
							plain : true,
							modal : true, // 模态窗体
							closeAction : "hide",
							items : [this.upLoadFormPanel],
							listeners : {
								"beforeshow" : this.onBeforeshow,
								scope : this
							},
							buttons : [{
										text : "上传",
										handler : this.onUpLoad,
										scope : this
									}, {
										text : "取消",
										handler : this.onCloseWin,
										scope : this
									}]
						});
						
						//为当前组件添加自定义事件
						this.addEvents("onUploadSuccess");
			},

			/**
			 * 上传按钮单击事件
			 */
			onUpLoad : function() {
				// 如果表单验证通过,则提交表单
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
							waitTitle : "数据传输",
							waitMsg : "数据传输中,请稍候......",
							success : this.onSuccess,
							failure : this.onFailure,
							scope : this
						});			        	
			        }else
			        {
			        	Ext.Msg.alert("系统消息","只能上传word文件");
			        }

				}
			},

			/**
			 * 上传成功回调函数
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
			 * 上传失败回调函数
			 * 
			 * @param {}
			 *            _form
			 * @param {}
			 *            _action
			 */
			onFailure : function(_form, _action) {
				Ext.Msg.alert("系统消息",_action.result.tipMsg);
			},

			/**
			 * 取消按钮单击事件
			 */
			onCloseWin : function() {
				this.upLoadFormPanel.getForm().reset();
				this.hide();
			},

			/**
			 * 窗体在显示前的事件
			 */
			onBeforeshow : function() {
				//alert(this.ClickForm);
				this.upLoadFormPanel.getForm().reset();
			}
		});