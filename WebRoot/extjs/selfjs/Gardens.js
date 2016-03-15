GRDS = function(){
	return {
		/**
		 * 用户种类
		 */	
		renderToDM : function(v,p,r){
			if(v!="" && v!=0)
			{
			   var index = comndm.find('txdm',v);
			   if(index!=-1) return String.format(comndm.getAt(index).get('tame'));   		
			}			
		},
		//申请批阅
		onUserCheck: function(item){
            if(fileForm.form.isValid()){
                fileForm.form.submit({
                    clientValidation:true,// 进行客户端验证
                    waitMsg : '正在提交数据请稍后',// 提示信息
                    waitTitle : '提示',// 标题
                    url : 'GardensData?action=new',// 请求的url地址
                    params : {jsr:item.userid},
                    method:'POST',// 请求方式
                    success:function(form,action){// 加载成功的处理函数
                        var result = action.result.success;
                        if(result)
                        {
                            Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;发送成功!");
                        }
                        fileForm.getForm().reset();
                    },
                    failure:function(form,action){// 加载失败的处理函数
                        Ext.Msg.alert("请求错误","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
                    }
                });
            }
            },
		//查看文件
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
		//加载数据
		// 加载表单数据
		fileloadForm :function (tarForm,fileid){
			tarForm.form.load({
				waitMsg : '正在加载数据请稍后',// 提示信息
				waitTitle : '提示',// 标题
				url : 'GardensData?action=getFile',// 请求的url地址
				params : {fileid:fileid},
				method:'GET',// 请求方式
				success:function(form,action){// 加载成功的处理函数
					 Ext.Msg.alert('提示','数据加载成功');
				},
				failure:function(form,action){// 加载失败的处理函数
					Ext.Msg.alert('提示','数据加载失败');
				}
			});
			
		},
		//同意否批阅
		onAgree: function(isok){
			// 批阅
			if(dsp_fileForm.form.isValid()){
				dsp_fileForm.form.submit({
					clientValidation:true,// 进行客户端验证
					waitMsg : '正在提交数据请稍后',// 提示信息
					waitTitle : '提示',// 标题
					url : 'GardensData?action=new',// 请求的url地址
					params : {isok:isok},
					method:'POST',// 请求方式
					success:function(form,action){// 加载成功的处理函数
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;批阅成功!");
		              }			
		              dsp_fileForm.getForm().reset();
		              //刷新grid
		              var mainPanel = Ext.getCmp('doc-body');
		              var tab = mainPanel.getComponent('docs-0102');
		              if(tab){
		              	mainPanel.setActiveTab(tab);
		              	tab.getUpdater().refresh();
		              }	
					},
					failure:function(form,action){// 加载失败的处理函数
						Ext.Msg.alert("请求错误","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
					}
				});					
			}			
		},
		//转送批阅
		onUserTurn: function(item){
			// 转送
			if(dsp_fileForm.form.isValid()){
				dsp_fileForm.form.submit({
					clientValidation:true,// 进行客户端验证
					waitMsg : '正在提交数据请稍后',// 提示信息
					waitTitle : '提示',// 标题
					url : 'GardensData?action=new',// 请求的url地址
					params : {zsr:item.userid},
					method:'POST',// 请求方式
					success:function(form,action){// 加载成功的处理函数
		              var result = action.result.success;
		              if(result)
		              {
		                  Ext.Msg.alert("提示","<img src='extjs/resources/button/icon-info.gif' align='absmiddle'>&nbsp;转送成功!");
		              }			
		              dsp_fileForm.getForm().reset();
					},
					failure:function(form,action){// 加载失败的处理函数
						Ext.Msg.alert("请求错误","<img src='resources/button/icon-error.gif' align='absmiddle'>&nbsp;&nbsp;服务器未响应，请稍后再试!");
					}
				});					
			}		
		}
	};
}();