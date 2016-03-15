	Ext.form.ComboBoxTree = Ext.extend(Ext.form.ComboBox, {
        store: new Ext.data.SimpleStore({ fields: [], data: [[]] }),
        editable: false,
        width:300,
        shadow: false,
        mode: 'local',
        triggerAction: 'all',
        selectedClass: '',
        onSelect: null,
        canCollapse: true,
        constructor: function(_cfg) {
            if (_cfg == null) {
                _cfg = {};
            }
            Ext.apply(this, _cfg);
            this.treerenderid = Ext.id();
            this.tpl = String.format('<tpl for="."><div style="height:200px"><div id="ext-combobox-tree{0}"></div></div></tpl>', this.treerenderid);
            Ext.form.ComboBoxTree.superclass.constructor.apply(this, arguments);
            if (this.tree) {
                var cmb = this;
                this.tree.on('click', function(node) {
                   // alert(cmb.value + " " + cmb.text);
                    //cmb.canCollapse = true;
                    if (Ext.isFunction(cmb.onSelect)) {
                        cmb.onSelect(cmb, node);                        
                    } else {
                    	cmb.setValue(node.text);
                        var tmppath = node.getPath().slice(3);
                        var value = tmppath.substring(tmppath.lastIndexOf('/')+1,tmppath.length);
                    	var valueName = cmb.valueName;
                    	var myStore = cmb.myStore;
                    	if(valueName && myStore){
                    		myStore.each(function(r){
                    			if(r.get("nowedit")){
                    				r.set(valueName,value);
                    				if(valueName=='fxzb_dm' && cmb.myGrid!=null){//风险指标判断
                    					r.set("fxydcs",node.attributes.fxydjy);
                    					cmb.myGrid.getColumnModel().getCellEditor(8,0).setValue(node.attributes.fxydjy);
                    					if((value == '2140101' || value == '2140102')){
                    						if(Ext.isEmpty(cmb.myGrid.getColumnModel().getCellEditor(2,0).getValue()))
                    							cmb.myGrid.getColumnModel().getCellEditor(2,0).setValue(' ');
                    					}else if(Ext.isEmpty(cmb.myGrid.getColumnModel().getCellEditor(2,0).getValue())){
                							cmb.myGrid.getColumnModel().getCellEditor(2,0).setValue(cmb.asdzxc);
                						}
                    					/*if(r.get('zx_swjg_dm_dm')){ //风险指标,级联判断专管员
                        					var editor2 = cmb.myGrid.getColumnModel().getCellEditor(10,0).field;
                        					var store = editor2.getStore();
                        					store.proxy = new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=3&&fxzb_dm='+value+"&swjg_dm="+r.get('swjg_dm')});
                        					editor2.clearValue();
                        					store.load();
                    					}*/
                    				}else if(valueName=='swjg_dm' && cmb.myGrid!=null){//主管税务机关,级联专管员
                    					var editor1 = cmb.myGrid.getColumnModel().getCellEditor(5,0).field;
                    					var store = editor1.getStore();
                    					store.proxy = new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type=3&&swjg_dm='+value});
                    					editor1.clearValue();
                    					store.load();
                    				}if(((valueName=='zx_swjg_dm' && r.get('fxzb_dm')) ||
                    						(valueName=='fxzb_dm' && r.get('zx_swjg_dm')))
                    						&& cmb.myGrid!=null){  //执行机关 ,级联执行人
                    					//alert(r.get('zx_swjg_dm')+r.get('fxzb_dm'));
                    					var editor1 = cmb.myGrid.getColumnModel().getCellEditor(10,0).field;
                    					var store = editor1.getStore();
                    					store.proxy = new Ext.data.HttpProxy({url:'WLUserData?action=show&start=0&limit=9999&type=3'});
                    					store.baseParams = {swjg_dm:r.get('zx_swjg_dm'),fxzb_dm:r.get('fxzb_dm')};
                    					editor1.clearValue();
                    					store.load();
                    				}
                    				return false;
                    			}
                    		});
                    	}else{
                    		if(cmb.JiLian){
                    			var type=2;
                    			if(cmb.JiLianType){
                    				type = cmb.JiLianType;
                    			}
                    			var jilian = Ext.getCmp(cmb.JiLian);
                    			var store = jilian.getStore();
                    			store.proxy = new Ext.data.HttpProxy({ url:'WLUserData?action=show&start=0&limit=9999&type='+type+'&&swjg_dm='+value});
                    			jilian.clearValue();
            					store.load();
                    		}
                    		cmb.ownerCt.items.itemAt(0).setValue(value);
                    	}
                        if (cmb.hiddenField) {
                            cmb.hiddenField.value = node.id;
                        }
                    }
                    cmb.collapse();
                });
                
                      
                //以下事件，让combobox能正常关闭
                this.tree.on('expandnode', function() { cmb.canCollapse = true; });
                this.tree.on('beforeload', function() { cmb.canCollapse = false; });
                this.tree.on('beforeexpandnode', function() { cmb.canCollapse = false; });
                this.tree.on('beforecollapsenode', function() { cmb.canCollapse = false; });
            }
            this.on('expand', this.expandHandler, this);
            this.on('collapse', this.collapseHandler, this);
        },
        expandHandler: function expand() {
        	//alert("dongdong1");
            this.canCollapse = true;
            if (this.tree) {
                this.tree.render('ext-combobox-tree' + this.treerenderid);
                this.canCollapse = true;         
                this.tree.getRootNode().expand();
               
            }
        },
        collapseHandler: function collapse() {
        	//alert("dongdong2");
            if (!this.canCollapse) {
                this.expand();
            }
        }
    });
