<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=GBK"%>
<div id="Count_Tasks_Swjg_FormPanel"></div>
<div id="Count_Tasks_Swjg_Panel"></div>
<script type="text/javascript" language="javascript">
Ext.ns("Ext.ux.tree");Ext.ux.tree.TreeGridSorter=Ext.extend(Ext.tree.TreeSorter,{sortClasses:["sort-asc","sort-desc"],sortAscText:"Sort Ascending",sortDescText:"Sort Descending",constructor:function(a,b){if(!Ext.isObject(b)){b={property:a.columns[0].dataIndex||"text",folderSort:true}}Ext.ux.tree.TreeGridSorter.superclass.constructor.apply(this,arguments);this.tree=a;a.on("headerclick",this.onHeaderClick,this);a.ddAppendOnly=true;me=this;this.defaultSortFn=function(k,j){var f=me.dir&&me.dir.toLowerCase()=="desc";var c=me.property||"text";var e=me.sortType;var g=me.folderSort;var h=me.caseSensitive===true;var d=me.leafAttr||"leaf";if(g){if(k.attributes[d]&&!j.attributes[d]){return 1}if(!k.attributes[d]&&j.attributes[d]){return -1}}var m=e?e(k):(h?k.attributes[c]:k.attributes[c].toUpperCase());var l=e?e(j):(h?j.attributes[c]:j.attributes[c].toUpperCase());if(m<l){return f?+1:-1}else{if(m>l){return f?-1:+1}else{return 0}}};a.on("afterrender",this.onAfterTreeRender,this,{single:true});a.on("headermenuclick",this.onHeaderMenuClick,this)},onAfterTreeRender:function(){if(this.tree.hmenu){this.tree.hmenu.insert(0,{itemId:"asc",text:this.sortAscText,cls:"xg-hmenu-sort-asc"},{itemId:"desc",text:this.sortDescText,cls:"xg-hmenu-sort-desc"})}this.updateSortIcon(0,"asc")},onHeaderMenuClick:function(d,b,a){if(b==="asc"||b==="desc"){this.onHeaderClick(d,null,a);return false}},onHeaderClick:function(e,b,a){if(e&&!this.tree.headersDisabled){var d=this;d.property=e.dataIndex;d.dir=e.dir=(e.dir==="desc"?"asc":"desc");d.sortType=e.sortType;d.caseSensitive===Ext.isBoolean(e.caseSensitive)?e.caseSensitive:this.caseSensitive;d.sortFn=e.sortFn||this.defaultSortFn;this.tree.root.cascade(function(c){if(!c.isLeaf()){d.updateSort(d.tree,c)}});this.updateSortIcon(a,e.dir)}},updateSortIcon:function(b,a){var d=this.sortClasses;var c=this.tree.innerHd.select("td").removeClass(d);c.item(b).addClass(d[a=="desc"?1:0])}});Ext.tree.ColumnResizer=Ext.extend(Ext.util.Observable,{minWidth:14,constructor:function(a){Ext.apply(this,a);Ext.tree.ColumnResizer.superclass.constructor.call(this)},init:function(a){this.tree=a;a.on("render",this.initEvents,this)},initEvents:function(a){a.mon(a.innerHd,"mousemove",this.handleHdMove,this);this.tracker=new Ext.dd.DragTracker({onBeforeStart:this.onBeforeStart.createDelegate(this),onStart:this.onStart.createDelegate(this),onDrag:this.onDrag.createDelegate(this),onEnd:this.onEnd.createDelegate(this),tolerance:3,autoStart:300});this.tracker.initEl(a.innerHd);a.on("beforedestroy",this.tracker.destroy,this.tracker)},handleHdMove:function(f,k){var g=5,j=f.getPageX(),d=f.getTarget(".x-treegrid-hd",3,true);if(d){var b=d.getRegion(),l=d.dom.style,c=d.dom.parentNode;if(j-b.left<=g&&d.dom!==c.firstChild){var a=d.dom.previousSibling;while(a&&Ext.fly(a).hasClass("x-treegrid-hd-hidden")){a=a.previousSibling}if(a){this.activeHd=Ext.get(a);l.cursor=Ext.isWebKit?"e-resize":"col-resize"}}else{if(b.right-j<=g){var h=d.dom;while(h&&Ext.fly(h).hasClass("x-treegrid-hd-hidden")){h=h.previousSibling}if(h){this.activeHd=Ext.get(h);l.cursor=Ext.isWebKit?"w-resize":"col-resize"}}else{delete this.activeHd;l.cursor=""}}}},onBeforeStart:function(a){this.dragHd=this.activeHd;return !!this.dragHd},onStart:function(b){this.tree.headersDisabled=true;this.proxy=this.tree.body.createChild({cls:"x-treegrid-resizer"});this.proxy.setHeight(this.tree.body.getHeight());var a=this.tracker.getXY()[0];this.hdX=this.dragHd.getX();this.hdIndex=this.tree.findHeaderIndex(this.dragHd);this.proxy.setX(this.hdX);this.proxy.setWidth(a-this.hdX);this.maxWidth=this.tree.outerCt.getWidth()-this.tree.innerBody.translatePoints(this.hdX).left},onDrag:function(b){var a=this.tracker.getXY()[0];this.proxy.setWidth((a-this.hdX).constrain(this.minWidth,this.maxWidth))},onEnd:function(c){var b=this.proxy.getWidth(),a=this.tree;this.proxy.remove();delete this.dragHd;a.columns[this.hdIndex].width=b;a.updateColumnWidths();setTimeout(function(){a.headersDisabled=false},100)}});Ext.ux.tree.TreeGridNodeUI=Ext.extend(Ext.tree.TreeNodeUI,{isTreeGridNodeUI:true,renderElements:function(d,l,h,m){var o=d.getOwnerTree(),k=o.columns,j=k[0],e,b,g;this.indentMarkup=d.parentNode?d.parentNode.ui.getChildIndent():"";b=['<tbody class="x-tree-node">','<tr ext:tree-node-id="',d.id,'" class="x-tree-node-el x-tree-node-leaf ',l.cls,'">','<td class="x-treegrid-col">','<span class="x-tree-node-indent">',this.indentMarkup,"</span>",'<img src="',this.emptyIcon,'" class="x-tree-ec-icon x-tree-elbow">','<img src="',l.icon||this.emptyIcon,'" class="x-tree-node-icon',(l.icon?" x-tree-node-inline-icon":""),(l.iconCls?" "+l.iconCls:""),'" unselectable="on">','<a hidefocus="on" class="x-tree-node-anchor" href="',l.href?l.href:"#",'" tabIndex="1" ',l.hrefTarget?' target="'+l.hrefTarget+'"':"",">",'<span unselectable="on">',(j.tpl?j.tpl.apply(l):l[j.dataIndex]||j.text),"</span></a>","</td>"];for(e=1,g=k.length;e<g;e++){j=k[e];b.push('<td class="x-treegrid-col ',(j.cls?j.cls:""),'">','<div unselectable="on" class="x-treegrid-text"',(j.align?' style="text-align: '+j.align+';"':""),">",(j.tpl?j.tpl.apply(l):l[j.dataIndex]),"</div>","</td>")}b.push('</tr><tr class="x-tree-node-ct"><td colspan="',k.length,'">','<table class="x-treegrid-node-ct-table" cellpadding="0" cellspacing="0" style="table-layout: fixed; display: none; width: ',o.innerCt.getWidth(),'px;"><colgroup>');for(e=0,g=k.length;e<g;e++){b.push('<col style="width: ',(k[e].hidden?0:k[e].width),'px;" />')}b.push("</colgroup></table></td></tr></tbody>");if(m!==true&&d.nextSibling&&d.nextSibling.ui.getEl()){this.wrap=Ext.DomHelper.insertHtml("beforeBegin",d.nextSibling.ui.getEl(),b.join(""))}else{this.wrap=Ext.DomHelper.insertHtml("beforeEnd",h,b.join(""))}this.elNode=this.wrap.childNodes[0];this.ctNode=this.wrap.childNodes[1].firstChild.firstChild;var f=this.elNode.firstChild.childNodes;this.indentNode=f[0];this.ecNode=f[1];this.iconNode=f[2];this.anchor=f[3];this.textNode=f[3].firstChild},animExpand:function(a){this.ctNode.style.display="";Ext.ux.tree.TreeGridNodeUI.superclass.animExpand.call(this,a)}});Ext.ux.tree.TreeGridRootNodeUI=Ext.extend(Ext.tree.TreeNodeUI,{isTreeGridNodeUI:true,render:function(){if(!this.rendered){this.wrap=this.ctNode=this.node.ownerTree.innerCt.dom;this.node.expanded=true}if(Ext.isWebKit){var a=this.ctNode;a.style.tableLayout=null;(function(){a.style.tableLayout="fixed"}).defer(1)}},destroy:function(){if(this.elNode){Ext.dd.Registry.unregister(this.elNode.id)}delete this.node},collapse:Ext.emptyFn,expand:Ext.emptyFn});Ext.ux.tree.TreeGridLoader=Ext.extend(Ext.tree.TreeLoader,{createNode:function(a){if(!a.uiProvider){a.uiProvider=Ext.ux.tree.TreeGridNodeUI}return Ext.tree.TreeLoader.prototype.createNode.call(this,a)}});(function(){Ext.override(Ext.list.Column,{init:function(){var b=Ext.data.Types,a=this.sortType;if(this.type){if(Ext.isString(this.type)){this.type=Ext.data.Types[this.type.toUpperCase()]||b.AUTO}}else{this.type=b.AUTO}if(Ext.isString(a)){this.sortType=Ext.data.SortTypes[a]}else{if(Ext.isEmpty(a)){this.sortType=this.type.sortType}}}});Ext.tree.Column=Ext.extend(Ext.list.Column,{});Ext.tree.NumberColumn=Ext.extend(Ext.list.NumberColumn,{});Ext.tree.DateColumn=Ext.extend(Ext.list.DateColumn,{});Ext.tree.BooleanColumn=Ext.extend(Ext.list.BooleanColumn,{});Ext.reg("tgcolumn",Ext.tree.Column);Ext.reg("tgnumbercolumn",Ext.tree.NumberColumn);Ext.reg("tgdatecolumn",Ext.tree.DateColumn);Ext.reg("tgbooleancolumn",Ext.tree.BooleanColumn)})();Ext.ux.tree.TreeGrid=Ext.extend(Ext.tree.TreePanel,{rootVisible:false,useArrows:true,lines:false,borderWidth:Ext.isBorderBox?0:2,cls:"x-treegrid",columnResize:true,enableSort:true,reserveScrollOffset:true,enableHdMenu:true,columnsText:"Columns",initComponent:function(){if(!this.root){this.root=new Ext.tree.AsyncTreeNode({text:"Root"})}var a=this.loader;if(!a){a=new Ext.ux.tree.TreeGridLoader({dataUrl:this.dataUrl,requestMethod:this.requestMethod,store:this.store})}else{if(Ext.isObject(a)&&!a.load){a=new Ext.ux.tree.TreeGridLoader(a)}else{if(a){a.createNode=function(c){if(!c.uiProvider){c.uiProvider=Ext.ux.tree.TreeGridNodeUI}return Ext.tree.TreeLoader.prototype.createNode.call(this,c)}}}}this.loader=a;Ext.ux.tree.TreeGrid.superclass.initComponent.call(this);this.initColumns();if(this.enableSort){this.treeGridSorter=new Ext.ux.tree.TreeGridSorter(this,this.enableSort)}if(this.columnResize){this.colResizer=new Ext.tree.ColumnResizer(this.columnResize);this.colResizer.init(this)}var b=this.columns;if(!this.internalTpl){this.internalTpl=new Ext.XTemplate('<div class="x-grid3-header">','<div class="x-treegrid-header-inner">','<div class="x-grid3-header-offset">','<table cellspacing="0" cellpadding="0" border="0"><colgroup><tpl for="columns"><col /></tpl></colgroup>','<thead><tr class="x-grid3-hd-row">','<tpl for="columns">','<td class="x-grid3-hd x-grid3-cell x-treegrid-hd" style="text-align: {align};" id="',this.id,'-xlhd-{#}">','<div class="x-grid3-hd-inner x-treegrid-hd-inner" unselectable="on">',this.enableHdMenu?'<a class="x-grid3-hd-btn" href="#"></a>':"",'{header}<img class="x-grid3-sort-icon" src="',Ext.BLANK_IMAGE_URL,'" />',"</div>","</td></tpl>","</tr></thead>","</div></table>","</div></div>","</div>",'<div class="x-treegrid-root-node">','<table class="x-treegrid-root-table" cellpadding="0" cellspacing="0" style="table-layout: fixed;"></table>',"</div>")}if(!this.colgroupTpl){this.colgroupTpl=new Ext.XTemplate('<colgroup><tpl for="columns"><col style="width: {width}px"/></tpl></colgroup>')}},initColumns:function(){var e=this.columns,a=e.length,d=[],b,f;for(b=0;b<a;b++){f=e[b];if(!f.isColumn){f.xtype=f.xtype?(/^tg/.test(f.xtype)?f.xtype:"tg"+f.xtype):"tgcolumn";f=Ext.create(f)}f.init(this);d.push(f);if(this.enableSort!==false&&f.sortable!==false){f.sortable=true;this.enableSort=true}}this.columns=d},onRender:function(){Ext.tree.TreePanel.superclass.onRender.apply(this,arguments);this.el.addClass("x-treegrid");this.outerCt=this.body.createChild({cls:"x-tree-root-ct x-treegrid-ct "+(this.useArrows?"x-tree-arrows":this.lines?"x-tree-lines":"x-tree-no-lines")});this.internalTpl.overwrite(this.outerCt,{columns:this.columns});this.mainHd=Ext.get(this.outerCt.dom.firstChild);this.innerHd=Ext.get(this.mainHd.dom.firstChild);this.innerBody=Ext.get(this.outerCt.dom.lastChild);this.innerCt=Ext.get(this.innerBody.dom.firstChild);this.colgroupTpl.insertFirst(this.innerCt,{columns:this.columns});if(this.hideHeaders){this.header.dom.style.display="none"}else{if(this.enableHdMenu!==false){this.hmenu=new Ext.menu.Menu({id:this.id+"-hctx"});if(this.enableColumnHide!==false){this.colMenu=new Ext.menu.Menu({id:this.id+"-hcols-menu"});this.colMenu.on({scope:this,beforeshow:this.beforeColMenuShow,itemclick:this.handleHdMenuClick});this.hmenu.add({itemId:"columns",hideOnClick:false,text:this.columnsText,menu:this.colMenu,iconCls:"x-cols-icon"})}this.hmenu.on("itemclick",this.handleHdMenuClick,this)}}},setRootNode:function(a){a.attributes.uiProvider=Ext.ux.tree.TreeGridRootNodeUI;a=Ext.ux.tree.TreeGrid.superclass.setRootNode.call(this,a);if(this.innerCt){this.colgroupTpl.insertFirst(this.innerCt,{columns:this.columns})}return a},clearInnerCt:function(){if(Ext.isIE){var a=this.innerCt.dom;while(a.firstChild){a.removeChild(a.firstChild)}}else{Ext.ux.tree.TreeGrid.superclass.clearInnerCt.call(this)}},initEvents:function(){Ext.ux.tree.TreeGrid.superclass.initEvents.apply(this,arguments);this.mon(this.innerBody,"scroll",this.syncScroll,this);this.mon(this.innerHd,"click",this.handleHdDown,this);this.mon(this.mainHd,{scope:this,mouseover:this.handleHdOver,mouseout:this.handleHdOut})},onResize:function(b,c){Ext.ux.tree.TreeGrid.superclass.onResize.apply(this,arguments);var e=this.innerBody.dom;var f=this.innerHd.dom;if(!e){return}if(Ext.isNumber(c)){e.style.height=this.body.getHeight(true)-f.offsetHeight+"px"}if(Ext.isNumber(b)){var a=Ext.num(this.scrollOffset,Ext.getScrollBarWidth());if(this.reserveScrollOffset||((e.offsetWidth-e.clientWidth)>10)){this.setScrollOffset(a)}else{var d=this;setTimeout(function(){d.setScrollOffset(e.offsetWidth-e.clientWidth>10?a:0)},10)}}},updateColumnWidths:function(){var k=this.columns,m=k.length,a=this.outerCt.query("colgroup"),l=a.length,h,e,d,b;for(d=0;d<m;d++){h=k[d];for(b=0;b<l;b++){e=a[b];e.childNodes[d].style.width=(h.hidden?0:h.width)+"px"}}for(d=0,a=this.innerHd.query("td"),len=a.length;d<len;d++){h=Ext.fly(a[d]);if(k[d]&&k[d].hidden){h.addClass("x-treegrid-hd-hidden")}else{h.removeClass("x-treegrid-hd-hidden")}}var f=this.getTotalColumnWidth();Ext.fly(this.innerHd.dom.firstChild).setWidth(f+(this.scrollOffset||0));this.outerCt.select("table").setWidth(f);this.syncHeaderScroll()},getVisibleColumns:function(){var c=[],d=this.columns,a=d.length,b;for(b=0;b<a;b++){if(!d[b].hidden){c.push(d[b])}}return c},getTotalColumnWidth:function(){var d=0;for(var b=0,c=this.getVisibleColumns(),a=c.length;b<a;b++){d+=c[b].width}return d},setScrollOffset:function(a){this.scrollOffset=a;this.updateColumnWidths()},handleHdDown:function(j,f){var h=j.getTarget(".x-treegrid-hd");if(h&&Ext.fly(f).hasClass("x-grid3-hd-btn")){var b=this.hmenu.items,g=this.columns,a=this.findHeaderIndex(h),k=g[a],d=k.sortable;j.stopEvent();Ext.fly(h).addClass("x-grid3-hd-menu-open");this.hdCtxIndex=a;this.fireEvent("headerbuttonclick",b,k,h,a);this.hmenu.on("hide",function(){Ext.fly(h).removeClass("x-grid3-hd-menu-open")},this,{single:true});this.hmenu.show(f,"tl-bl?")}else{if(h){var a=this.findHeaderIndex(h);this.fireEvent("headerclick",this.columns[a],h,a)}}},handleHdOver:function(d,a){var c=d.getTarget(".x-treegrid-hd");if(c&&!this.headersDisabled){index=this.findHeaderIndex(c);this.activeHdRef=a;this.activeHdIndex=index;var b=Ext.get(c);this.activeHdRegion=b.getRegion();b.addClass("x-grid3-hd-over");this.activeHdBtn=b.child(".x-grid3-hd-btn");if(this.activeHdBtn){this.activeHdBtn.dom.style.height=(c.firstChild.offsetHeight-1)+"px"}}},handleHdOut:function(c,a){var b=c.getTarget(".x-treegrid-hd");if(b&&(!Ext.isIE||!c.within(b,true))){this.activeHdRef=null;Ext.fly(b).removeClass("x-grid3-hd-over");b.style.cursor=""}},findHeaderIndex:function(d){d=d.dom||d;var b=d.parentNode.childNodes;for(var a=0,e;e=b[a];a++){if(e==d){return a}}return -1},beforeColMenuShow:function(){var d=this.columns,b=d.length,a,e;this.colMenu.removeAll();for(a=1;a<b;a++){e=d[a];if(e.hideable!==false){this.colMenu.add(new Ext.menu.CheckItem({itemId:"col-"+a,text:e.header,checked:!e.hidden,hideOnClick:false,disabled:e.hideable===false}))}}},handleHdMenuClick:function(b){var a=this.hdCtxIndex,c=b.getItemId();if(this.fireEvent("headermenuclick",this.columns[a],c,a)!==false){a=c.substr(4);if(a>0&&this.columns[a]){this.setColumnVisible(a,!b.checked)}}return true},setColumnVisible:function(a,b){this.columns[a].hidden=!b;this.updateColumnWidths()},scrollToTop:function(){this.innerBody.dom.scrollTop=0;this.innerBody.dom.scrollLeft=0},syncScroll:function(){this.syncHeaderScroll();var a=this.innerBody.dom;this.fireEvent("bodyscroll",a.scrollLeft,a.scrollTop)},syncHeaderScroll:function(){var a=this.innerBody.dom;this.innerHd.dom.scrollLeft=a.scrollLeft;this.innerHd.dom.scrollLeft=a.scrollLeft},registerNode:function(a){Ext.ux.tree.TreeGrid.superclass.registerNode.call(this,a);if(!a.uiProvider&&!a.isRoot&&!a.ui.isTreeGridNodeUI){a.ui=new Ext.ux.tree.TreeGridNodeUI(a)}}});Ext.reg("treegrid",Ext.ux.tree.TreeGrid);Ext.ux.tree.TreeRowEditor=Ext.extend(Ext.Panel,{hidden:true,floating:true,shadow:false,layout:"hbox",cls:"x-small-editor",buttonAlign:"center",baseCls:"x-row-editor",elements:"header,footer,body",frameWidth:5,buttonPad:3,monitorValid:true,focusDelay:250,saveText:"保存",cancelText:"取消",adjustHeight:35,adjustButtonHeight:7,columnButton:true,defaults:{normalWidth:true},initComponent:function(){if(this.columnButton){this.adjustHeight=this.adjustButtonHeight;this.buttonWidth=this.buttonWidth||50}else{this.buttonWidth=this.buttonWidth||this.minButtonWidth}Ext.ux.tree.TreeRowEditor.superclass.initComponent.call(this);this.addEvents("beforeedit","canceledit","validateedit","afteredit")},init:function(a){this.tree=a;this.ownerCt=a;Ext.applyIf(a,{editNode:this.editNode.createDelegate(this)});a.on({scope:this,beforedestroy:this.beforeDestroy,destroy:this.destroy,bodyscroll:{buffer:250,fn:this.positionButtons}})},beforeDestroy:function(){this.stopEditing(false);this.node=null;Ext.destroy(this.saveBtn,this.cancelBtn);if(this.btns){Ext.destroy(this.btns)}},refreshFields:function(){this.initFields();this.verifyLayout()},isDirty:function(){var a;this.items.each(function(b){if(String(this.values[b.id])!==String(b.getValue())){a=true;return false}},this);return a},startEditing:function(d,s){if(this.editing){return}if(this.fireEvent("beforeedit",d)!==false){this.editing=true;var q=this.tree,h=q.innerBody;this.node=d;this.values={};if(!this.rendered){this.render(h)}var o=q.innerCt.getWidth();this.setSize(o);if(!this.initialized){this.initFields()}var p=q.columns,m,j=this.items.items,l,b;for(var g=0,k=p.length;g<k;g++){m=p[g],l=j[g];if(!m.buttons&&this.isField(l)){b=this.preEditValue(d,m.dataIndex);l.setValue(b);this.values[l.id]=Ext.isEmpty(b)?"":b}}this.verifyLayout(d,true);var r=Ext.fly(d.ui.elNode).getXY(),a=Ext.fly(d.ui.elNode).getHeight(),e=h.getXY()[1]+h.getHeight();var n=r[1]+a+this.adjustHeight;if(n>e){h.scroll("b",n);r=Ext.fly(d.ui.elNode).getXY();n=r[1]+a+this.adjustHeight;if(n>e){r[1]=e-a-this.adjustHeight}}if(!this.isVisible()){this.setPagePosition(r)}else{this.el.setXY(r,{duration:0.15})}if(!this.isVisible()){this.show().doLayout()}if(s!==false){this.doFocus.defer(this.focusDelay,this)}}},stopEditing:function(o){this.editing=false;if(!this.isVisible()){return}if(o===false||!this.isValid()){this.hide();this.fireEvent("canceledit",this.node);return}var j={},b=this.node,k=false,m=this.tree.columns,h,f=this.items.items;for(var e=0,g=m.length;e<g;e++){h=m[e];if(!h.hidden&&!h.buttons){var d=h.dataIndex;var a=b.attributes[d],l=this.postEditValue(f[e].getValue(),a,b,d);if(String(a)!==String(l)){j[d]=l;k=true}}}if(k&&this.fireEvent("validateedit",b,j)!==false){Ext.apply(b.attributes,j);Ext.iterate(j,function(q,s){var p=0,t;for(var r=0,n=m.length;r<n;r++){t=m[r];if(t.dataIndex==q){p=r;break}}if(p==0){b.ui.textNode.innerHTML=t.tpl?t.tpl.apply(b.attributes):s}else{b.ui.elNode.childNodes[p].firstChild.innerHTML=t.tpl?t.tpl.apply(b.attributes):s}});this.fireEvent("afteredit",b,j)}this.hide()},verifyLayout:function(c,a){if(this.el&&(this.isVisible()||a===true)){var b=c.ui.elNode;this.setSize(Ext.fly(b).getWidth(),Ext.isIE?Ext.fly(b).getHeight()+9:undefined);var k=this.tree.columns,e=this.items.items,g;for(var d=0,h=k.length;d<h;d++){g=e[d];if(this.isField(g)){var j=0;if(d===(h-1)){j+=3}else{j+=1}g.setWidth(k[d].width-j)}}this.doLayout();this.positionButtons()}},slideHide:function(){this.hide()},initFields:function(){var b=this.tree.columns,f=Ext.layout.ContainerLayout.prototype.parseMargins;this.removeAll(false);for(var e=0,a=b.length;e<a;e++){var g=b[e],d=g.editor;if(this.columnButton){if(e==a-1){this.cancelBtn.margins=f("0 0 2 2");this.add([this.saveBtn,this.cancelBtn]);continue}else{if(g.buttons){d=new Ext.BoxComponent()}}}else{if(g.buttons){continue}}if(!d){d=g.displayEditor||new Ext.form.DisplayField()}if(e==0){d.margins=f("0 1 2 1")}else{if(e==a-1){d.margins=f("0 0 2 1")}else{d.margins=f("0 1 2")}}d.setWidth(g.width);d.column=g;if(d.ownerCt!==this){d.on("specialkey",this.onKey,this)}this.insert(e,d)}this.initialized=true},onKey:function(a,b){if(b.getKey()===b.ENTER){this.stopEditing(true);b.stopPropagation()}},editNode:function(a){this.startEditing(a,true)},onRender:function(){Ext.ux.tree.TreeRowEditor.superclass.onRender.apply(this,arguments);this.el.swallowEvent(["keydown","keyup","keypress"]);this.saveBtn=new Ext.Button({ref:"saveBtn",itemId:"saveBtn",text:this.saveText,width:this.buttonWidth,handler:this.stopEditing.createDelegate(this,[true])});this.cancelBtn=new Ext.Button({text:this.cancelText,width:this.buttonWidth,handler:this.stopEditing.createDelegate(this,[false])});if(!this.columnButton){this.btns=new Ext.Panel({baseCls:"x-plain",cls:"x-btns",elements:"body",layout:"table",width:(this.buttonWidth*2)+(this.frameWidth*2)+(this.buttonPad*4),items:[this.saveBtn,this.cancelBtn]});this.btns.render(this.bwrap)}},afterRender:function(){Ext.ux.tree.TreeRowEditor.superclass.afterRender.apply(this,arguments);this.positionButtons();if(this.monitorValid){this.startMonitoring()}},onShow:function(){if(this.monitorValid){this.startMonitoring()}Ext.ux.tree.TreeRowEditor.superclass.onShow.apply(this,arguments)},onHide:function(){Ext.ux.tree.TreeRowEditor.superclass.onHide.apply(this,arguments);this.stopMonitoring()},positionButtons:function(){if(this.btns){var b=this.tree,d=this.el.dom.clientHeight,a=b.innerBody.dom.scrollLeft,e=this.btns.getWidth(),c=b.getWidth();this.btns.el.shift({left:(c/2)-(e/2)+a,top:d-2,stopFx:true,duration:0.2})}},preEditValue:function(a,c){var b=a.attributes[c];return this.autoEncode&&typeof b==="string"?Ext.util.Format.htmlDecode(b):b},postEditValue:function(c,a,b,d){return this.autoEncode&&typeof c=="string"?Ext.util.Format.htmlEncode(c):c},doFocus:function(){if(this.isVisible()){var b=this.tree.columns,e;for(var d=0,a=b.length;d<a;d++){e=b[d];if(!e.hidden&&e.editor){e.editor.focus();break}}}},startMonitoring:function(){if(!this.bound&&this.monitorValid){this.bound=true;Ext.TaskMgr.start({run:this.bindHandler,interval:this.monitorPoll||200,scope:this})}},stopMonitoring:function(){this.bound=false},isValid:function(){var a=true;this.items.each(function(b){if(this.isField(b)&&!b.isValid(true)){a=false;return false}},this);return a},isField:function(a){return !!a.setValue&&!!a.getValue&&!!a.markInvalid&&!!a.clearInvalid},bindHandler:function(){if(!this.bound){return false}var a=this.isValid();this.saveBtn.setDisabled(!a);this.fireEvent("validation",this,a)}});Ext.preg("ptreeroweditor",Ext.ux.tree.TreeRowEditor);Ext.ux.tree.EditTreeGrid=Ext.extend(Ext.ux.tree.TreeGrid,{idProperty:"id",enableSort:false,enableHdMenu:false,highlightColor:"#d9e8fb",depth:Number.MAX_VALUE,rowEdit:true,isTreeEditor:true,initComponent:function(){this.enableHdMenu=false;if(this.rowEdit){this.animate=false;this.editor=new Ext.ux.tree.TreeRowEditor({listeners:{scope:this,canceledit:this.cancelEdit,afteredit:this.saveNode}});this.plugins=this.plugins||[];this.plugins.push(this.editor)}Ext.ux.tree.EditTreeGrid.superclass.initComponent.call(this)},beforeDestroy:function(){Ext.destroy(this.editor);Ext.ux.tree.EditTreeGrid.superclass.beforeDestroy.call(this)},initColumns:function(){var f=this.columns,a=f.length,e=[],d,g,b;for(d=0;d<a;d++){g=f[d];if(!g.isColumn){g.xtype=g.xtype?(/^tg/.test(g.xtype)?g.xtype:"tg"+g.xtype):"tgcolumn";if(g.buttons){g.buttons=Ext.isArray(g.buttons)?g.buttons:[g.buttons];g.buttonIconCls=Ext.isDefined(g.buttonIconCls)?(Ext.isArray(g.buttonIconCls)?g.buttonIconCls:[g.buttonIconCls]):[];g.buttonText=Ext.isDefined(g.buttonText)?(Ext.isArray(g.buttonText)?g.buttonText:[g.buttonText]):[];g.buttonTips=Ext.isDefined(g.buttonTips)?(Ext.isArray(g.buttonTips)?g.buttonTips:[g.buttonTips]):[];if(this.rowEdit){g.buttonHandler=[]}else{g.buttonHandler=g.buttonHandler||[];g.buttonHandler=Ext.isArray(g.buttonHandler)?g.buttonHandler:[g.buttonHandler]}b=[];Ext.each(g.buttons,function(c,h){c=Ext.util.Format.lowercase(c);b.push('<div gridbtn="',c,'" class="x-treegrid-button-item x-toolbar"></div>');if(this.rowEdit){g.buttonHandler.push(this[c+"Node"])}},this);g.tpl=new Ext.XTemplate(b);g.dataIndex=this.idProperty;g.editable=false}g=Ext.create(g)}g.init(this);e.push(g);if(this.enableSort!==false&&g.sortable!==false){g.sortable=true;this.enableSort=true}}this.columns=e},updateColumnWidths:function(){var k=this.columns,m=k.length,a=this.outerCt.query("colgroup"),l=a.length,h,e,d,b;for(d=0;d<m;d++){h=k[d];for(b=0;b<l;b++){e=a[b];e.childNodes[d].style.width=(h.hidden?0:h.width)+"px"}}for(d=0,a=this.innerHd.query("td"),len=a.length;d<len;d++){h=Ext.fly(a[d]);if(k[d]&&k[d].hidden){h.addClass("x-treegrid-hd-hidden")}else{h.removeClass("x-treegrid-hd-hidden")}}var f=this.getTotalColumnWidth();Ext.fly(this.innerHd.dom.firstChild).setWidth(f+(this.scrollOffset||0));this.outerCt.select("table").each(function(j,n,g){if(!j.hasClass("x-btn")){j.setWidth(f)}},this);this.syncHeaderScroll()},addNode:function(b){if(this.editor.editing||b.getDepth()+1>this.depth){return}var g={_isNewTreeGridNode:true};g[this.idProperty]="";var e=this.columns,a=e.length,h;for(i=0;i<a;i++){h=e[i];if(h.dataIndex){g[h.dataIndex]=""}}var f=new Ext.tree.TreeNode(g);if(b.isLeaf()){b.leaf=false}else{if(b.lastChild){var d=this.getButton(b.lastChild,"degrade");if(d){d.enable()}}}b.expand();b.appendChild(f);Ext.fly(f.ui.elNode).highlight(this.highlightColor);this.editNode(f)},updateNode:function(a){if(this.editor.editing){return}this.editNode(a)},cancelEdit:function(c){if(c.attributes._isNewTreeGridNode){var a=c.parentNode;if(a.childNodes.length==1){a.leaf=true}c.remove();if(a.childNodes.length<1){this.updateLeafIcon(a)}else{var b=this.getButton(a.lastChild,"degrade");if(b){b.disable()}}}},saveNode:function(e,c){Ext.fly(e.ui.elNode).highlight(this.highlightColor);var d={},b={node:e,changes:c};Ext.applyIf(d,e.attributes);d.parentNodeId=e.parentNode.id;var a=this.columns;Ext.iterate(c,function(h,k){var g=0,l;for(var j=0,f=a.length;j<f;j++){l=a[j];if(l.dataIndex==h){g=j;break}}Ext.fly(e.ui.elNode.childNodes[g]).addClass("x-grid3-dirty-cell")});this.doRequest(e.attributes._isNewTreeGridNode?"add":"update",this.filterParams(d),this.processSave,b)},processSave:function(b,c){try{var h=c.node,d=c.changes;if(h.attributes._isNewTreeGridNode){var g=Ext.decode(b.responseText);h.attributes._isNewTreeGridNode=false;if(g.id){h.setId(g.id)}if(g[this.idProperty]){h.attributes[this.idProperty]=g[this.idProperty]}}var a=this.columns;Ext.iterate(d,function(k,m){var j=0,n;for(var l=0,e=a.length;l<e;l++){n=a[l];if(n.dataIndex==k){j=l;break}}Ext.fly(h.ui.elNode.childNodes[j]).removeClass("x-grid3-dirty-cell")})}catch(f){}},removeNode:function(g){if(this.editor.editing){return}var a=g.parentNode,c=g.previousSibling,d=g.nextSibling;if(a.childNodes.length==1){a.leaf=true}g.remove();if(a.childNodes.length<1){this.updateLeafIcon(a)}else{if(c&&c.isLast()){var b=this.getButton(c,"degrade");if(b){b.disable()}}if(d&&d.isFirst()){var f=this.getButton(d,"upgrade");if(f){f.disable()}}}var e={id:g.id,parentNodeId:a.id};e[this.idProperty]=g.attributes[this.idProperty];this.doRequest("remove",this.filterParams(e))},upgradeNode:function(b){if((this.editor&&this.editor.editing)||b.isFirst()){return}b.parentNode.insertBefore(b,b.previousSibling);if(b.isFirst()){this.getButton(b,"upgrade").disable();this.getButton(b,"degrade").enable();this.getButton(b.nextSibling,"upgrade").enable();if(b.nextSibling.isLast()){this.getButton(b.nextSibling,"degrade").disable()}}else{this.getButton(b,"degrade").enable();this.getButton(b.nextSibling,"upgrade").enable();if(b.nextSibling.isLast()){this.getButton(b.nextSibling,"degrade").disable()}}Ext.fly(b.ui.elNode).highlight(this.highlightColor);var a={id:b.id,parentNodeId:b.parentNode.id};a[this.idProperty]=b.attributes[this.idProperty];this.doRequest("upgrade",this.filterParams(a))},degradeNode:function(b){if((this.editor&&this.editor.editing)||b.isLast()){return}b.parentNode.insertBefore(b,b.nextSibling.nextSibling);if(b.isLast()){this.getButton(b,"upgrade").enable();this.getButton(b,"degrade").disable();if(b.previousSibling.isFirst()){this.getButton(b.previousSibling,"upgrade").disable()}this.getButton(b.previousSibling,"degrade").enable()}else{this.getButton(b,"upgrade").enable();this.getButton(b,"degrade").enable();if(b.previousSibling.isFirst()){this.getButton(b.previousSibling,"upgrade").disable()}this.getButton(b.previousSibling,"degrade").enable()}Ext.fly(b.ui.elNode).highlight(this.highlightColor);var a={id:b.id,parentNodeId:b.parentNode.id};a[this.idProperty]=b.attributes[this.idProperty];this.doRequest("degrade",this.filterParams(a))},doRequest:function(a,c,d,b){if(!this.requestApi||!this.requestApi[a]){return}c=Ext.apply({requestAction:a},c);b=Ext.applyIf(b||{},{params:c});if(Ext.isString(this.requestApi[a])){b.url=this.requestApi[a]}else{Ext.applyIf(b,this.requestApi[a])}if(d){if(b.success){b.success=d.createDelegate(this).createSequence(b.success)}else{if(b.callback){b.callback=d.createDelegate(this).createSequence(b.callback)}else{b.success=d.createDelegate(this)}}}Ext.Ajax.request(b)},getButton:function(b,a){return b.buttons.get(a)},updateLeafIcon:function(a){Ext.fly(a.ui.elNode).replaceClass("x-tree-node-collapsed","x-tree-node-leaf")},filterParams:function(a){delete a.uiProvider;delete a.iconCls;delete a.loader;delete a.leaf;delete a.children;delete a._isNewTreeGridNode;return a},disableButton:function(c,a){c=Ext.isString(c)?this.getNodeById(c):c;c.disableButton(a)},enableButton:function(c,a){c=Ext.isString(c)?this.getNodeById(c):c;c.enableButton(a)},hideButton:function(c,a){c=Ext.isString(c)?this.getNodeById(c):c;c.hideButton(a)},showButton:function(c,a){c=Ext.isString(c)?this.getNodeById(c):c;c.showButton(a)}});Ext.reg("edittreegrid",Ext.ux.tree.EditTreeGrid);Ext.apply(Ext.ux.tree.TreeGridNodeUI.prototype,{renderElements:function(d,l,h,m){var p=d.getOwnerTree(),k=p.columns,j=k[0],e,b,g;this.indentMarkup=d.parentNode?d.parentNode.ui.getChildIndent():"";b=['<tbody class="x-tree-node">','<tr ext:tree-node-id="',d.id,'" class="x-tree-node-el x-tree-node-leaf ',l.cls,'">','<td class="x-treegrid-col">','<span class="x-tree-node-indent">',this.indentMarkup,"</span>",'<img src="',this.emptyIcon,'" class="x-tree-ec-icon x-tree-elbow">','<img src="',l.icon||this.emptyIcon,'" class="x-tree-node-icon',(l.icon?" x-tree-node-inline-icon":""),(l.iconCls?" "+l.iconCls:""),'" unselectable="on">','<a hidefocus="on" class="x-tree-node-anchor" href="',l.href?l.href:"#",'" tabIndex="1" ',l.hrefTarget?' target="'+l.hrefTarget+'"':"",">",'<span unselectable="on">',(j.tpl?j.tpl.apply(l):l[j.dataIndex]||j.text),"</span></a>","</td>"];for(e=1,g=k.length;e<g;e++){j=k[e];b.push('<td class="x-treegrid-col ',(j.cls?j.cls:""),'">','<div unselectable="on" class="',j.buttons?"x-treegrid-button":"x-treegrid-text",'"',(j.align?' style="text-align: '+j.align+';"':""),">",(j.tpl?j.tpl.apply(l):l[j.dataIndex]),"</div>","</td>")}b.push('</tr><tr class="x-tree-node-ct"><td colspan="',k.length,'">','<table class="x-treegrid-node-ct-table" cellpadding="0" cellspacing="0" style="table-layout: fixed; display: none; width: ',p.innerCt.getWidth(),'px;"><colgroup>');for(e=0,g=k.length;e<g;e++){b.push('<col style="width: ',(k[e].hidden?0:k[e].width),'px;" />')}b.push("</colgroup></table></td></tr></tbody>");if(m!==true&&d.nextSibling&&d.nextSibling.ui.getEl()){this.wrap=Ext.DomHelper.insertHtml("beforeBegin",d.nextSibling.ui.getEl(),b.join(""))}else{this.wrap=Ext.DomHelper.insertHtml("beforeEnd",h,b.join(""))}if(!d.buttons){d.buttons=new Ext.util.MixedCollection(false,function(a){return a.itemId})}var o=Ext.get(this.wrap);for(e=0,g=k.length;e<g;e++){j=k[e];if(j.buttons){Ext.each(j.buttons,function(a,c){var n=new Ext.Button({itemId:a,disabled:(d.attributes[a+"BtnDisabled"]===true)||(a=="add"&&d.getDepth()==p.depth),hidden:(d.attributes[a+"BtnHidden"]===true),iconCls:j.buttonIconCls[c],text:j.buttonText[c],tooltip:j.buttonTips[c],handler:j.buttonHandler[c].createDelegate(p,[d]),scope:p});if((a=="upgrade"&&d.isFirst())||(a=="degrade"&&d.isLast())){n.disable()}d.buttons.add(n);n.render(o.child("[gridbtn="+a+"]"))},this)}}this.elNode=this.wrap.childNodes[0];this.ctNode=this.wrap.childNodes[1].firstChild.firstChild;var f=this.elNode.firstChild.childNodes;this.indentNode=f[0];this.ecNode=f[1];this.iconNode=f[2];this.anchor=f[3];this.textNode=f[3].firstChild}});Ext.apply(Ext.tree.TreeNode.prototype,{disableButton:function(a){if(a=="upgrade"||a=="degrade"){return}if(a){this.buttons.get(a).disable()}},enableButton:function(a){if(a=="upgrade"||a=="degrade"){return}if(a){this.buttons.get(a).enable()}},hideButton:function(a){if(a){this.buttons.get(a).hide()}},showButton:function(a){if(a){this.buttons.get(a).show()}},originalDestroy:Ext.tree.TreeNode.prototype.destroy,destroy:function(a){if(this.buttons){this.buttons.each(function(b){Ext.destroy(b)},this);this.buttons.clear()}this.originalDestroy.call(this,a)}});

	Ext.chart.Chart.CHART_URL = 'extjs/resources/charts.swf';
	function getPercent(v,c,r){
		if(r.get("a") == 0){return '0%';}
		 return (Math.round(r.get('c') / r.get("a") * 10000) / 100.00 + "%");
	}
	/**
	 *  任务统计
	 */
	Ext.onReady(function() {
		var Count_Tasks_Swjg_PanelStore = new Ext.data.JsonStore({
			root : 'topic',
			totalProperty : 'topcount',
			storeId : "id",
			idProperty : 'id',
			successProperty : 'success',
			fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'swjg_dm' ],
			proxy : new Ext.data.HttpProxy({
				url : 'TasksData?action=Count_By_SWJG&type=' + Math.random()
			}),
			listeners : {
				"beforeload" : function(t, o) {
					var para = Count_Tasks_Form.form.getValues(false);
					t.baseParams = para;
				}
			}
		});

		

		
		var Count_Tasks_Form = new Ext.form.FormPanel({
			title : '查询条件',
			labelAlign : 'right',
			labelWidth : 90,
			frame : true,
			renderTo : "Count_Tasks_Swjg_FormPanel",
			bodyStyle : 'padding: 0 0 0 0;',
			style : 'padding:0 0 0 0;min-width:1010px;',
			//width : 1010,
			buttonAlign : "center",
			items : [ {
				layout : 'column',
				items : [ {
					columnWidth : .33,
					layout : 'form',
					items : [ {
						xtype : 'hidden',
						name : 'swjg_dm'
					}, new Ext.form.ComboBoxTree({
						fieldLabel : '所属机关',
						emptyText : '全部',
						anchor : '98%',
						mode : 'local',
						displayField : 'text',
						valueField : 'id',
						hiddenName : 'swjg_mc',
						name : 'swjg_mc',
						editable : false,
						autoLoad : true,
						tree : new Ext.tree.TreePanel({
							rootVisible : false,
							border : false,
							method : 'GET',
							dataUrl : 'TaskManData?action=treeSwjg',
							root : new Ext.tree.AsyncTreeNode({
								text : 'children',
								id : '0',
								expanded : true
							}),
							listeners : {
								"load" : function(node) {
									node.expand(false);
								}
							}
						}),
						listeners : {
							"expand" : function(c) {
								c.list.setWidth('320px');
								c.innerList.setWidth('auto');
							}
						}
					}),{
						fieldLabel: '执行人代码',
		                xtype: 'textfield',
						anchor: '98%',
		   			    forceSelection:true,
					    selectOnFocus:true,
		                editable: false,
		                allowBlank: true,
		                name: 'zxr_dm',
		                width:50  		 
					} ]
				}, {
					columnWidth : .29,
					layout : 'form',
					items : [{
						xtype:'hidden',
						name : 'fxzb_dm'
					}, new Ext.form.ComboBoxTree({
			            name:'fxzb',
						anchor: '98%',
			            //xtype:'checktreepanel',
			            isFormField:true,
			            fieldLabel:'风险指标',
						emptyText : '全部',
			            autoScroll:true,
			            selectNodeModel: 'leaf', 
					 	displayField:'text',
					 	valueField:'id',
			            bodyStyle:'background-color:white;border:1px solid #B5B8C8',
			            tree:  new Ext.tree.TreePanel({
		                    rootVisible: false,
		                    border: false,
		                    method: 'GET',
		                    dataUrl: 'TaskManData?action=treeQuanXian',
		                	root:new Ext.tree.AsyncTreeNode({text: 'children',id:'0',expanded:true})
		                }),
		                listeners :{
		                	"expand": function(c){
		                		c.list.setWidth('320px'); 
		                        c.innerList.setWidth('auto');
		                	}
		                }
			        }),{
						fieldLabel: '执行人名称',
		                xtype: 'textfield',
						anchor: '98%',
		   			    forceSelection:true,
					    selectOnFocus:true,
		                editable: false,
		                allowBlank: true,
		                name: 'zxr_mc',
		                width:50  		 
					} ]
				}, {
					columnWidth : .38,
					layout : 'form',
					items : [ {
						xtype : 'compositefield',
						fieldLabel : '任务日期',
						items : [ {
							xtype : 'datefield',
							name : 'task_date_start',
							editable : false,
							value : getMonthFirstDay(new Date()),
							width : 105,
							format : 'Y-m-d'
						}, {
							xtype : 'label',
							text : '至'
						}, {
							xtype : 'datefield',
							fieldLabel : '截至日期',
							editable : false,
							value : new Date(),
							name : 'task_date_end',
							width : 105,
							format : 'Y-m-d'
						} ]
					} ]
				} ]
			} ],
			buttons : [
					{
						text : '查询',
						handler : function() {
							if (Count_Tasks_Form.form.isValid()) {
								Count_Tasks_Swjg_PanelStore.load({
									params : {
										start : 0,
										limit : 15
									}
								});
							}
						}
					},
					{
						text : '清空查询条件',
						handler : function() {
							Count_Tasks_Form.getForm().reset();
						}
					} ]
		});
		
		//var Count_Tasks_csm = new Ext.grid.CheckboxSelectionModel();
		var Count_Tasks_Swjg_PanelCm = [
                              						  {
                            								header : "税务机关",
                            								dataIndex : 'swjg_mc',
                            								width : 300
                            							}, {
                            								header : "所有任务",
                            								dataIndex : 'a',
                            								width : 100
                            							}, {
                            								header : "未完成任务",
                            								dataIndex : 'b',
                            								width : 100
                            							}, {
                            								header : "已完成任务",
                            								dataIndex : 'c',
                            								width : 100
                            							}, {
                            								header : "完成百分比",
                            								dataIndex : '',
                            								width : 100,
                            								renderer: getPercent
                            							},{
                            								header : "类别详细",
                            				                width: 100,
                            				                items: [
                            									{
                            									    icon   : 'extjs/resources/button/page_add.png',
                            									    tooltip: "任务分类明细",
                            									    handler: function(grid, rowIndex, colIndex) {
                            									    	//查询任务分类详情
                            									    	var rec = grid.store.getAt(rowIndex);
                            									    	showTaskTypeDetail(rec);
                            									    }
                            									} ]},
                            									{
                            										header : "图形统计",
                            						                width: 100,
                            						                items: [
                            											{
                            											    icon   : 'extjs/resources/button/chart_bar.png',
                            										        iconCls:'chart',
                            											    tooltip: "显示柱状图",
                            											    handler: function(grid, rowIndex, colIndex) {
                            											    	var rec = grid.store.getAt(rowIndex);
                            											    	showChart(rec);
                            											    }
                            											} ]},{
                            										header : "人员详细",
                            						                width: 100,
                            						                items: [
                            											{
                            											    icon   : 'extjs/resources/button/user_suit.png',
                            											    tooltip: "人员完成明细",
                            											    handler: function(grid, rowIndex, colIndex) {
                            											    	//查询任务分类详情
                            											    	var rec = grid.store.getAt(rowIndex);
                            											    	showPersen(rec);
                            											    }
                            											} ]} ];
		var Count_Tasks_Swjg_PanelGrid = new Ext.ux.tree.EditTreeGrid({
			width : Count_Tasks_Form.getWidth()-15,
			height : 365,
			maxDepth: 5,
			columns : Count_Tasks_Swjg_PanelCm,
	        enableDD: true,
			loader: new Ext.tree.TreeLoader({
            	dataUrl:'TasksData?action=Count_By_SWJG&type=' + Math.random()
	            	//dataUrl:'js/leftTree.json'
        	})
		});
		function showChart(rec){
			var swjg_dm = rec.get('swjg_dm');
			var char_Store = new Ext.data.JsonStore({
				root : 'topic',
				storeId : "id",
				autoLoad: true,
				fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'd', 'swjg_dm'],
				proxy : new Ext.data.HttpProxy({
					url : 'TasksData?action=Count_By_Fxzb_Dm&sw_dm=' + swjg_dm
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var chart = {
		            xtype: 'stackedbarchart',
		            store: char_Store,
		            yField: 'swjg_mc',
		            xAxis: new Ext.chart.NumericAxis({
		                stackingEnabled: true,
		                majorUnit:1 ,
		                labelRenderer: Ext.util.Format.numberRenderer('0')
		            }),
		            series: [{
		                xField: 'a',
		                displayName: '登记类'
		            },{
		                xField: 'b',
		                displayName: '申报类'
		            },{
		                xField: 'c',
		                displayName: '征收类'
		            },{
		                xField: 'd',
		                displayName: '管理类'
		            }],
		            chartStyle: {
		                padding: 10,
		                animationEnabled: true,
		                font: {
		                    name: 'Tahoma',
		                    color: 0x444444,
		                    size: 11
		                },
		                dataTip: {
		                    padding: 5,
		                    border: {
		                        color: 0x99bbe8,
		                        size:1
		                    },
		                    background: {
		                        color: 0xDAE7F6,
		                        alpha: .9
		                    },
		                    font: {
		                        name: 'Tahoma',
		                        color: 0x15428B,
		                        size: 12,
		                        bold: true
		                    }
		                }
		            }
		        };
			new Ext.Window({
		        iconCls:'chart',
				title:rec.get('swjg_mc'),
				width:800,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[chart]
			}).show();
		}
		function showTaskTypeDetail(rec){
			var swjg_dm = rec.get('swjg_dm');
			var TypeDetail_Store = new Ext.data.JsonStore({
				root : 'topic',
				totalProperty : 'topcount',
				storeId : "id",
				idProperty : 'id',
				successProperty : 'success',
				autoLoad: true,
				fields : [ 'id', 'swjg_mc', 'a', 'b', 'c', 'd', 'swjg_dm'],
				proxy : new Ext.data.HttpProxy({
					url : 'TasksData?action=Count_By_Fxzb_Dm&sw_dm=' + swjg_dm
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var TypeDetailCm = new Ext.grid.ColumnModel([
             				 {
             					header : "序号",
             					sortable : true,
             					dataIndex : 'id',
             					width : 35
             				}, {
             					header : "机关名称",
             					sortable: true,
             					dataIndex : 'swjg_mc',
             					width : 220
             				}, {
             					header : "登记类",
             					sortable: true,
             					dataIndex : 'a',
             					width : 80
             				}, {
             					header : "申报类",
             					sortable: true,
             					dataIndex : 'b',
             					width : 80
             				}, {
             					header : "征收类",
             					sortable: true,
             					dataIndex : 'c',
             					width : 80
             				}, {
             					header : "管理类",
             					sortable: true,
             					dataIndex : 'd',
             					width : 80
             				} ]);
			new Ext.Window({
				title:rec.get('swjg_mc'),
				width:600,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[new Ext.grid.GridPanel({
					style : 'padding:0 0 0 0',
					width : '100%',
					height : 365,
					store : TypeDetail_Store,
					trackMouseOver : true,
					loadMask : true,
					cm : TypeDetailCm,
					viewConfig : {
						forceFit : false
					}
				})]
			}).show();
		}
		function showPersen(rec){
			var swjg_dm = rec.get('swjg_dm');
			if(-1 == swjg_dm){
				return;
			}
			var person_Store = new Ext.data.JsonStore({
				root : 'topic',
				totalProperty : 'topcount',
				storeId : "id",
				idProperty : 'id',
				successProperty : 'success',
				autoLoad: true,
				fields : [ 'id', 'zxr_dm', 'a', 'b', 'c', 'username' ],
				proxy : new Ext.data.HttpProxy({
					url : 'TasksData?action=Count_By_SWJG_DM&sw_dm=' + rec.get('swjg_dm')
				}),
				listeners : {
					"beforeload" : function(t, o) {
						var para = Count_Tasks_Form.form.getValues(false);
						t.baseParams = para;
					}
				}
			});
			var personCm = new Ext.grid.ColumnModel([
             				 {
             					header : "序号",
             					sortable : true,
             					dataIndex : 'id',
             					width : 35
             				}, {
             					header : "执行人姓名",
             					sortable: true,
             					dataIndex : 'username',
             					width : 130
             				}, {
             					header : "所有任务",
             					sortable: true,
             					dataIndex : 'a',
             					width : 100
             				}, {
             					header : "未完成任务",
             					sortable: true,
             					dataIndex : 'b',
             					width : 100
             				}, {
             					header : "已完成任务",
             					sortable: true,
             					dataIndex : 'c',
             					width : 100
             				}, {
             					header : "完成百分比",
             					sortable: true,
             					dataIndex : '',
             					width : 97,
             					renderer: getPercent
             				} ]);
			new Ext.Window({
				title:rec.get('swjg_mc'),
				width:600,
				height:400,
				layout:'form',
				modal:true,
				plain:false,
				resizable:false,
				constrain:false,
				autoScroll:true,
				buttonAlign:'center',		    
				items:[new Ext.grid.GridPanel({
					style : 'padding:0 0 0 0',
					width : '97%',
					height : 368,
					store : person_Store,
					trackMouseOver : true,
					loadMask : true,
					cm : personCm,
					viewConfig : {
						forceFit : false
					},
					bbar:['->',{    
				        text:'打印',    
						icon:'extjs/resources/button/action_print.gif',
				        handler:function(){ 
				        	var count = person_Store.getCount();
				        	if(count <= 0){
				        		Ext.Msg.alert("提示","无数据不可打印.");
				        		return;
				        	}
				        	window.open("main/printter.jsp?action=Count_By_SWJG_DM&type=print&sw_dm=" + rec.get('swjg_dm')+"&"+Ext.urlEncode(Count_Tasks_Form.form.getValues(false)),'_blank',"width=0,height=0,channelmode=0,directories=0,location=0,menubar=0,resizable=0,scrollbars=0,status=0,titlebar=0,toolbar=0");
					}},'->']
				})]
			}).show();
		}
		Count_Tasks_Swjg_PanelGrid.render('Count_Tasks_Swjg_Panel');
	});
</script>
