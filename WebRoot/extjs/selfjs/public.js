
function getCookie(objName){//获取指定名称的cookie的??
	var arrStr = document.cookie.split("; ");
	for(var i = 0;i < arrStr.length;i ++){
	var temp = arrStr[i].split("=");
	if(temp[0] == objName) return unescape(temp[1]);
	} 
}
/*精确运算加法*/
function addNum(num1,num2){
	var sq1,sq2,m;
	try{sq1=num1.toString().split(".")[1].length;} catch(e){sq1=0;}
	try{sq2=num2.toString().split(".")[1].length;} catch(e){sq2=0;}

	m=Math.pow(10,Math.max(sq1,sq2));
	return ( num1 * m + num2 * m ) / m;
	}
function accAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}
function obj2str(o){
    var s = '';
    for(var st in o){
    	s += st + "=" + eval('o.' + st) + "&";
    }
    s = s.substring(0,s.length -1);
    return s;
}
function getMonthLastDay(date){
	date.setMonth(date.getMonth()+1);
	date.setDate(0);
	return date;
	}
function getMonthFirstDay(date){
	date.setDate(1);
	return date;
	}

function getTaxName(v){
	for(var i=0;i<Docs.static_tax.length;i++){
		if(Docs.static_tax[i][1] == v){
			return Docs.static_tax[i][0];
		}
	}
	return v;
}
function getJiaokuantype(v){
	for(var i=0;i<Docs.static_jiaokuantype.length;i++){
		if(Docs.static_jiaokuantype[i][0] == v){
			return Docs.static_jiaokuantype[i][1];
		}
	}
	return v;
}

function getTaxType(v){
	for(var i=0;i<Docs.static_shuizhong.length;i++){
		if(Docs.static_shuizhong[i][1] == v){
			return Docs.static_shuizhong[i][0];
		}
	}
	return v;
}

function getPowerName(v){
	for(var i=0;i<Docs.static_menu.length;i++){
		if(Docs.static_menu[i][1] == v){
			return Docs.static_menu[i][0];
		}
	}
	return v;
}
function date2String(v){
	if(v instanceof Date){
		return trimDate(Ext.util.Format.dateRenderer('Y-m-d')(v));
	}else return trimDate(v);
}
function trimDate(v){
	if(v == '1900-01-01'){
		return '';
	}else return v;
}
Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
        var date = field.parseDate(val);
        if(!date){
            return false;
        }
        if (field.startDateField) {
            var start = Ext.getCmp(field.startDateField);
            if (!start.maxValue || (date.getTime() != start.maxValue.getTime())) {
                start.setMaxValue(date);
                start.validate();
            }
        }
        else if (field.endDateField) {
            var end = Ext.getCmp(field.endDateField);
            if (!end.minValue || (date.getTime() != end.minValue.getTime())) {
                end.setMinValue(date);
                end.validate();
            }
        }
        return true;
    }});
// Checkbox 多选
Ext.override(Ext.grid.CheckboxSelectionModel, {   
    handleMouseDown : function(g, rowIndex, e){     
      if(e.button !== 0 || this.isLocked()){     
        return;     
      }     
      var view = this.grid.getView();     
      if(e.shiftKey && !this.singleSelect && this.last !== false){     
        var last = this.last;     
        this.selectRange(last, rowIndex, e.ctrlKey);     
        this.last = last; // reset the last     
        view.focusRow(rowIndex);     
      }else{     
        var isSelected = this.isSelected(rowIndex);     
        if(isSelected){     
          this.deselectRow(rowIndex);     
        }else if(!isSelected || this.getCount() > 1){     
          this.selectRow(rowIndex, true);     
          view.focusRow(rowIndex);     
        }     
      }     
    }   
}); 
function addCompositefield(field, json, flag){
	var fileName = json.fileName;
    var filePath = json.path;
    var fileSuffix = fileName.substring(fileName.lastIndexOf('.')+1,fileName.length);
    var src = 'extjs/resources/button/win_yes.gif';
    if('xls' == fileSuffix || 'xlsx' == fileSuffix ){
 	  src = 'extjs/resources/button/fu_exl.gif';
    }
    var comid = Ext.id();
    var hideId = Ext.id();
    var item1 = {xtype:'hidden', id: hideId, name:flag?"fujianid":"", value: json.id};
    var item2 = {xtype:'label', html:" <img src='"+src+"' height=15 width=15/> <a href='"+filePath+"' target='_blank'>"+ fileName +"</a> "};
    var item3 = {text:'删除',xtype:'button',handler : function() {
    	Ext.Ajax.request({
    		url:'FileAction',
    		params:{act:'delete',id:json.id},
            method:'post',
            success:function(action,o){
            	field.remove(comid);
            }
    	});
    	}};
    //Ext.applyIf(item1 , { getName: function() {return 'fujianid'; } });
    Ext.applyIf(item1 , { isValid: function() {return true; } });
    var showitem = flag?[item1,item2,item3]:[item1,item2];
    field.add({
		xtype: 'compositefield',
		id: comid,
		items: showitem});
    field.doLayout();
}
