FileForm = function(config) {
	var userpriv = config.userPriv;
	/*
	 * ����bbar�еİ�ť
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
        text    : '�ļ��ϴ�',
        iconCls : 'file-up',
        handler : function() { alert('Wrong!'); },
        scope   : this
    };
	arrbars[1] = {
        text    : '����ָ��',
        iconCls : 'file-py',
        scope   : this,
        menu    : menu
    };
	arrbars[2] = {
        text    : '����ύ',
        iconCls : 'file-ok',
        handler : function() { GRDS.onAgree(1); },
        scope   : this
    };
	arrbars[3] = {
        text    : '��������',
        iconCls : 'file-no',
        handler : function() { GRDS.onAgree(2); },
        scope   : this
    };
	arrbars[4] = {
        text    : 'ת�;ֳ�',
        iconCls : 'file-turn',
        scope   : this,
        menu    : menu
    };

	// ׼�����ò���
	var cfg = {
		frame      : true,
		labelWidth : 70,
		baseCls    : "x-plain",
		width      : 450,
		tbar : new Ext.Toolbar({
            style : { marginBottom : '4px'},
            items : (function(){
            	// ���ݴ��ݹ�����Ȩ�޲��������а�ť�����
            	var items = [];
    			for(var j=0; j<arrbars.length; j++)
    			{
    				if(userpriv.indexOf(j.toString())>=0)
    				{
    					items.push('-');// ��ť֮����ӷָ���
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
                fieldLabel : "����",
                emptyText  : "���������",
                name       : "title",
                allowBlank : false,
                readOnly   : config.type==1 ? false : true,
				disabled   : config.type==1 ? false : true
			},{
				fieldLabel : "������",
				name       : "fqr",
				value      : config.type==1 ? config.fqr : '',
				readOnly   : true, //config.type==1 ? false : true,
				disabled   : config.type==1 ? false : true
			},{
			    fieldLabel : "��ʼ����",
                xtype      : 'datefield',
                format     : 'Y-m-d',
                name       : "qc_riqi",
			    value      : config.type==1 ? config.qc_riqi : '',
			    readOnly   : config.type==1 ? false:true,
			    disabled   : config.type==1 ? false:true
			},{
			    fieldLabel : "��������",
			    xtype      : 'textarea',
			    name       : 'beizhu',
			    readOnly   : config.type==1 ? false : true,
			    disabled   : config.type==1 ? false : true
			},{
                xtype          : 'combo',
                fieldLabel     : '��Ҫ�̶�',
                allowBlank     : false,
                name           : 'taskstatus',
                hiddenName     : 'taskstatus',
                displayField   : 'name',
                valueField     : 'id',
                mode           : 'local',
                triggerAction  : 'all',
                emptyText      : '��ѡ��...',
                forceSelection : true,
                selectOnFocus  : true,
                value          : '0',
                store          : new Ext.data.SimpleStore({
                    fields   : ['id','name'],
                    data     : [[0, '��ͨ'],[1, '����']],
                    autoLoad : true
                })
            },{
			    fieldLabel : "�����ܽ�",
			    xtype      : 'textarea',
			    name       : 'fj_yj',
                allowBlank : false,
			    readOnly   : config.type==1 || config.type==3 || config.type==4 ? true : false,
			    disabled   : config.type==1 || config.type==3 || config.type==4 ? true : false
			},{
			    fieldLabel : "�ֳ����",
			    xtype      : 'hidden',
			    name       : 'zj_yj',
			    readOnly   : config.type==1 || config.type==2 || config.type==4 ? true : false,
				disabled   : config.type==1 || config.type==2 || config.type==4 ? true : false
			}			
		]
	};

	config = Ext.applyIf(config || {}, cfg);
	// call���๹����
	FileForm.superclass.constructor.call(this, config);
	
};
Ext.extend(FileForm, Ext.form.FormPanel);