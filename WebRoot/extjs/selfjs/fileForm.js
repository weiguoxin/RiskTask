FileForm = function(config) {
	var userpriv = config.userPriv;
	/*
	 * 定义bbar中的按钮
	 * 
	 */

	if(typeof(userRolsItems)=="string")
	{
		if(config.type!=1 ) 
			userRolsItems = Ext.decode(userRolsItems.replace("Check","Turn"));
		else
			userRolsItems = Ext.decode(userRolsItems);		
	}	

    var menu = new Ext.menu.Menu({
        id    : 'mainMenu',
        items : userRolsItems
    });	
	var arrbars = new Array(4);
	
	arrbars[0] = {
        text    : '文件上传',
        iconCls : 'file-up',
        handler : function() { alert('Wrong!'); },
        scope   : this
    };
	arrbars[1] = {
        text    : '任务指派',
        iconCls : 'file-py',
        scope   : this,
        menu    : menu
    };
	arrbars[2] = {
        text    : '完成提交',
        iconCls : 'file-ok',
        handler : function() { GRDS.onAgree(1); },
        scope   : this
    };
	arrbars[3] = {
        text    : '撤销任务',
        iconCls : 'file-no',
        handler : function() { GRDS.onAgree(2); },
        scope   : this
    };
	arrbars[4] = {
        text    : '转送局长',
        iconCls : 'file-turn',
        scope   : this,
        menu    : menu
    };

	// 准备配置参数
	var cfg = {
		frame      : true,
		labelWidth : 70,
		baseCls    : "x-plain",
		width      : 450,
		tbar : new Ext.Toolbar({
            style : { marginBottom : '4px'},
            items : (function(){
            	// 根据传递过来的权限参数，进行按钮的添加
            	var items = [];
    			for(var j=0; j<arrbars.length; j++)
    			{
    				if(userpriv.indexOf(j.toString())>=0)
    				{
    					items.push('-');// 按钮之间添加分隔线
    					items.push(arrbars[j]);	
    				}
    			} 
    			return items;
            }).createDelegate(this)()			
		}),
		defaults : {
			xtype  : "textfield",
			anchor : "95%"
		},
		reader : new Ext.data.JsonReader({
			root    : "data",
		    success : "success"
			},
		    ['id', 'title',{name : 'fqr',  convert:function(v){if(v!="" && v!=0){var index = comndm.find('txdm',v);if(index!=-1) return String.format(comndm.getAt(index).get('tame'));}}},{name : 'qc_riqi', type : 'date', dateFormat : 'Y-m-d H:i:s'}, 'taskstatus', 'beizhu', 'fj_yj', 'zj_yj']
		),
		items : [
		    {
                xtype : 'hidden',
                name  : 'id'
            },{
                fieldLabel : "标题",
                emptyText  : "请输入标题",
                name       : "title",
                allowBlank : false,
                readOnly   : config.type==1 ? false : true,
				disabled   : config.type==1 ? false : true
			},{
				fieldLabel : "发起人",
				name       : "fqr",
				value      : config.type==1 ? config.fqr : '',
				readOnly   : true, //config.type==1 ? false : true,
				disabled   : config.type==1 ? false : true
			},{
			    fieldLabel : "起始日期",
                xtype      : 'datefield',
                format     : 'Y-m-d',
                name       : "qc_riqi",
			    value      : config.type==1 ? config.qc_riqi : '',
			    readOnly   : config.type==1 ? false:true,
			    disabled   : config.type==1 ? false:true
			},{
			    fieldLabel : "任务描述",
			    xtype      : 'textarea',
			    name       : 'beizhu',
			    readOnly   : config.type==1 ? false : true,
			    disabled   : config.type==1 ? false : true
			},{
                xtype          : 'combo',
                fieldLabel     : '紧要程度',
                allowBlank     : false,
                name           : 'taskstatus',
                hiddenName     : 'taskstatus',
                displayField   : 'name',
                valueField     : 'id',
                mode           : 'local',
                triggerAction  : 'all',
                emptyText      : '请选择...',
                forceSelection : true,
                selectOnFocus  : true,
                value          : '0',
                store          : new Ext.data.SimpleStore({
                    fields   : ['id','name'],
                    data     : [[0, '普通'],[1, '紧急']],
                    autoLoad : true
                })
            },{
			    fieldLabel : "任务总结",
			    xtype      : 'textarea',
			    name       : 'fj_yj',
                allowBlank : false,
			    readOnly   : config.type==1 || config.type==3 || config.type==4 ? true : false,
			    disabled   : config.type==1 || config.type==3 || config.type==4 ? true : false
			},{
			    fieldLabel : "局长意见",
			    xtype      : 'hidden',
			    name       : 'zj_yj',
			    readOnly   : config.type==1 || config.type==2 || config.type==4 ? true : false,
				disabled   : config.type==1 || config.type==2 || config.type==4 ? true : false
			}			
		]
	};

	config = Ext.applyIf(config || {}, cfg);
	// call父类构建器
	FileForm.superclass.constructor.call(this, config);
	
};
Ext.extend(FileForm, Ext.form.FormPanel);