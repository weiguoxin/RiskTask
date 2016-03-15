/**
 * @author _wgx
 * @date 2014年8月27日
 * @class UpLoadExcelWindow
 * @extends Ext.Window
 * @description excel上传窗体
 */
UpLoadExcelWindow = Ext.extend(Ext.Window, {
			// excel上传表单
			excelFormPanel : null,
			ClickForm       : null,
			textHidden: true,
			width : 330,
			height : 120,
			url: "personsys/importexcel.jsp",
			title : "导入excel文件",
			plain : true,
			modal : true, // 模态窗体
			closeAction : "hide",
			flushGrid: false,
			actionType:'',
			TemplatesLink:'<a href="docs/task.xls" target="_blank">下载EXCEL模版</a>',
			/**
			 * 构造方法
			 */
			constructor : function(cfg) {
				UpLoadExcelWindow.superclass.constructor.call(this, cfg);
				//alert(this.textHidden);
				// excel上传表单
				this.excelFormPanel = new Ext.form.FormPanel({
							fileUpload : true, // 允许上传文件
							labelWidth : 90,
							layout : "form",
							baseCls : "x-plain", // 统一背景色
							bodyStyle : "padding:10px;",
							items : [							         
							          {xtype:'hidden',name:'act',value:this.actionType},{
											fieldLabel : '任务包名称',
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
										fieldLabel : '选择excel文件',
										anchor : "95%",
										buttonCfg : {
											text : '选择'
										}
							          },{
							        	  xtype:'displayfield',
										  fieldLabel : '下载EXCEL模版',
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
										text : "导入",
										handler : this.onUpLoad,
										scope : this
									}, {
										text : "取消",
										handler : this.onCloseWin,
										scope : this
									}]
						});
						
						//为当前组件添加自定义事件
						this.addEvents("excelUploadSuccess");
			},

			/**
			 * 上传按钮单击事件
			 */
			onUpLoad : function() {
				// 如果表单验证通过,则提交表单
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
							waitTitle : "数据传输",
							waitMsg : "数据传输中,请稍候......",
							success : this.onSuccess,
							failure : this.onFailure,
							scope : this
						});			        	
			        }else
			        {
			        	Ext.Msg.alert("系统消息","只能上传.xls的excel文件");
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
				Ext.Msg.alert("系统消息",_action.result.tipMsg);
				if(this.flushGrid){
					this.flushGrid.store.reload();
				}
				this.hide();
				//excelUploadSuccess(_form,_action);
				//this.fireEvent("onUploadSuccess",_form,_action);
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
				this.excelFormPanel.getForm().reset();
				this.hide();
			},

			/**
			 * 窗体在显示前的事件
			 */
			onBeforeshow : function() {
				//alert(this.ClickForm);
				this.excelFormPanel.getForm().reset();
			}
		});