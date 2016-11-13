/*
 * ! @author caizhiping 下拉树
 */
Ext.define('App.ux.ComboBoxTree', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.comboboxtree',
	store : new Ext.data.ArrayStore({
				fields : [],
				data : [[]]
			}),
	editable : false,
	listConfig : {
		resizable : true,
		minWidth : 100,
		maxWidth : 600
	},
	_idValue : null,
	_txtValue : null,
	storeUrl : null,
	nodeParam : null,
	rootId : null,
	rootText : null,
	expandRoot : null,
	expandAll : null,
	extraParams : null,
	nodeList : null,
	allowSelectedRoot : true,
	allowSelectBranch : true,
	valueField : 'id',
	displayField : 'text',
	leafField : 'leaf',
	initComponent : function() {
		this.treeObj = new Ext.tree.Panel({
					border : false,
					height : Ext.isEmpty(this.treeHeight) ? 300 : this.treeHeight,
					autoScroll : true,
					rootVisible : Ext.isEmpty(this.showRoot) ? true : this.showRoot,
					store : Ext.create('Ext.data.TreeStore', {
								// fields : ['id', 'text', 'leaf'],
								autoLoad : false,
								nodeParam : this.nodeParam || 'parentId',
								root : {
									id : Ext.isEmpty(this.rootId) ? '0' : this.rootId,
									text : Ext.isEmpty(this.rootText) ? '全部' : this.rootText,
									expanded : Ext.isEmpty(this.expandRoot) ? false : this.expandRoot
								},
								proxy : {
									type : 'ajax',
									url : this.storeUrl,
									extraParams : Ext.isEmpty(this.extraParams) ? {} : this.extraParams,
									reader : {
										type : 'json',
										root : Ext.isEmpty(this.nodeList) ? 'root' : this.nodeList
									}
								}
							})
				});
		this.treeRenderId = Ext.id();
		this.tpl = "<tpl for='.'><div id='" + this.treeRenderId + "'></div></tpl>";
		this.callParent(arguments);
		this.on({
					'expand' : function() {
						if (!this.treeObj.rendered && this.treeObj && !this.readOnly) {
							Ext.defer(function() {
										this.treeObj.render(this.treeRenderId);
									}, 300, this);
						}
					}
				});
		this.treeObj.on('itemclick', function(view, rec) {
					if (rec) {
						// this.setValue(this._txtValue = rec.get('text'));
						// this._idValue = rec.get('id');
						// this.collapse();
						// this.focus();
						var rootId = this.treeObj.getRootNode().data.id;
						if (rec.get('checked') != null) {// 多选树
							if ((rootId != rec.get(this.valueField) || this.allowSelectedRoot) && (rec.get(this.leafField) || this.allowSelectBranch)) {
								rec.set('checked', !rec.data.checked);
								this.setSelectedValue();
							}
						}
						else {
							if ((rootId != rec.get(this.valueField) || this.allowSelectedRoot) && (rec.get(this.leafField) || this.allowSelectBranch)) {								
								this.setRawValue(rec.get(this.displayField));
								// alert(rec.get(this.displayField));
								this.setValue(rec.get(this.valueField));
						alert(this.getValue());
							}
						}
						this.collapse();
						this.focus();
					}
				}, this);

		this.treeObj.on('checkchange', function(node, checked, eOpts) {
					var rootId = this.treeObj.getRootNode().data.id;
					if ((rootId != node.get(this.valueField) || this.allowSelectedRoot) && (node.get(this.leafField) || this.allowSelectBranch)) {
						this.setSelectValue();
					}
					else {
						node.data.checked = false;
						node.updateInfo({
									checked : false
								});
					}
				}, this);

		this.treeObj.on('afterrender', function(view, rec) {
					if (!Ext.isEmpty(this.expandAll) && this.expandAll) {
						this.treeObj.expandAll();
					}
				}, this);
	},
	getIdValue : function() {// 获取id值
		return this._idValue;
	},
	getTextValue : function() {// 获取text值
		return this._txtValue;
	},
	setLocalValue : function(txt, id) {// 设值
		this._idValue = id;
		this.setValue(this._txtValue = txt);
	},
	getTreePanel : function() {
		return this.treeObj;
	},
	setSelectedValue : function() {
		var self = this;
		var records = this.treeObj.getView().getChecked(), names = [], values = [];
		Ext.Array.each(records, function(rec) {
					names.push(rec.get(self.displayField));
					values.push(rec.get(self.valueField));
				});
		this.setRawValue(names.join(';'));
		this.setValue(values.join(';'));
	}
});