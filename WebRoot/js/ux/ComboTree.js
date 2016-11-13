Ext.define('App.ux.ComboTree', {
			extend : 'Ext.form.field.Picker',
			alias : ['widget.combotree'],
			valueField : 'id',
			displayField : 'text',
			leafField : 'leaf',
			initComponent : function() {
				Ext.apply(this, {
							pickerAlign : 'tl-bl?',
							editable : false
						});
				this.callParent();
			},
			createPicker : function() {
				var self = this;
				var allowSelectBranch = Ext.isEmpty(self.allowSelectBranch) ? true : self.allowSelectBranch;
				var allowSelectedRoot = Ext.isEmpty(self.allowSelectedRoot) ? false : self.allowSelectedRoot;
				var expandChildren = Ext.isEmpty(self.expandChildren) ? false : self.expandChildren;
				var rootId = Ext.isEmpty(self.rootId) ? '0' : self.rootId;
				var store = Ext.create('Ext.data.TreeStore', {
							nodeParam : self.nodeParam || 'parentId',
							// fields : ['id', 'text', 'leaf','checked'],
							root : {
								id : rootId,
								text : Ext.isEmpty(self.rootText) ? '全部' : self.rootText,
								expanded : Ext.isEmpty(self.expandRoot) ? true : self.expandRoot
							},
							proxy : {
								type : 'ajax',
								url : self.storeUrl,
								extraParams : Ext.isEmpty(self.extraParams) ? {} : self.extraParams,
								reader : {
									type : 'json',
									root : Ext.isEmpty(self.nodeList) ? 'root' : self.nodeList
								}
							},
							listeners : {
								load : function(ds, nd, eOpts) {
									if (expandChildren) {
										nd.expandChildren();
									}
								}
							},
							// listeners : {
							// beforeload : function(ds, opration, opt) {
							// opration.params.parentId = opration.node.data.id;
							// opration.params.checked = opration.node.data.checked;
							// }
							// },
							autoLoad : false
						});
				this.picker = Ext.create('Ext.tree.Panel', {
							width : 300,
							height : Ext.isEmpty(self.treeHeight) ? 300 : self.treeHeight,
							autoScroll : true,
							floating : true,
							cls : 'x-combo-tree',
							focusOnToFront : false,
							shadow : true,
							ownerCt : this.ownerCt,
							store : store,
							singleExpand : false,
							rootVisible : Ext.isEmpty(self.showRoot) ? true : self.showRoot,
							displayField : this.displayField
						});
				this.picker.on({
							scope : this,
							'checkchange' : function(node, checked, eOpts) {
								if ((rootId != node.get(this.valueField) || allowSelectedRoot) && (node.get(this.leafField) || allowSelectBranch)) {
									this.setSelectedValue();
								}
								else {
									node.data.checked = false;
									node.updateInfo({
												checked : false
											});
								}
							},
							'itemclick' : function(view, record) {
								if (record.get('checked') != null) {// 多选树
									if ((rootId != record.get(this.valueField) || allowSelectedRoot)
											&& (record.get(this.leafField) || allowSelectBranch)) {
										record.set('checked', !record.data.checked);
										this.setSelectedValue();
									}
								}
								else {
									if ((rootId != record.get(this.valueField) || allowSelectedRoot)
											&& (record.get(this.leafField) || allowSelectBranch)) {
										this.isExpanded = false;
										this.picker.hide();
										this.setValue(record.get(this.valueField));
										this.setRawValue(record.get(this.displayField));
									}
								}
							}
						});
				return this.picker;
			},
			alignPicker : function() {
				var picker, isAbove, aboveSfx = '-above';
				if (this.isExpanded) {
					picker = this.getPicker();
					if (this.matchFieldWidth) {
						picker.setWidth(this.bodyEl.getWidth());
					}
					if (picker.isFloating()) {
						picker.alignTo(this.inputEl, this.pickerAlign, this.pickerOffset);
						isAbove = picker.el.getY() < this.inputEl.getY();
						this.bodyEl[isAbove ? 'addCls' : 'removeCls'](this.openCls + aboveSfx);
						picker.el[isAbove ? 'addCls' : 'removeCls'](picker.baseCls + aboveSfx);
					}
				}
			},
			clearValue : function() {
				this.setValue([]);
			},
			setValue : function(value) {
				this.value = Ext.isEmpty(value) ? null : value;
				this.focus();
				return this;
			},
			getValue : function() {
				return this.value;
			},
			getSubmitValue : function() {
				return this.getValue();
			},
			reset : function() {
				this.setValue(null).setRawValue(null);
			},
			setSelectedValue : function() {
				var self = this;
				var records = this.picker.getView().getChecked(), names = [], values = [];
				Ext.Array.each(records, function(rec) {
							names.push(rec.get(self.displayField));
							values.push(rec.get(self.valueField));
						});
				this.setRawValue(names.join(';'));
				this.setValue(values.join(';'));
			},
			reloadRootNode : function(needExpand) {
				if (this.getPicker().getStore().getRootNode().isLoaded()) {
					this.getPicker().getStore().getRootNode().removeAll();
					this.getPicker().getStore().load({
								scope : this,
								callback : function(records, operation, success) {
									if (!Ext.isEmpty(needExpand) && needExpand) {
										this.getPicker().getStore().getRootNode().expand();
									}
								}
							});
				}
				else {
					if (!this.getPicker().getStore().getRootNode().isLoading()) {
						this.getPicker().getStore().load({
									scope : this,
									callback : function(records, operation, success) {
										if (!Ext.isEmpty(needExpand) && needExpand) {
											this.getPicker().getStore().getRootNode().expand();
										}
									}
								});
					}
				}
			},
			getRootNode : function() {
				return this.getPicker().getStore().getRootNode();
			},
			getStore : function() {
				return this.getPicker().getStore();
			},
			getTreePanel : function() {
				return this.getPicker();
			}
		});
