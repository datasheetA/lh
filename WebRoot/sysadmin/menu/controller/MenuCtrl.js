Ext.define('App.sysadmin.menu.controller.MenuCtrl', {
			extend : 'Ext.app.Controller',
			models : ['MenuModel'],
			stores : ['MenuStore'],
			views : ['MenuPanel', 'EditPanel'],
			refs : [{
						ref : 'menuPanel',
						selector : 'viewport > menupanel'
					}, {
						ref : 'editPanel',
						selector : 'viewport > editpanel'
					}],
			onLaunch : function() {
				this.onInitAddForm();
			},
			init : function() {
				this.control({
							'menupanel' : {
								// itemmousedown : this.loadMenuDataOnMouseDown,
								itemclick : function(view, record, node) {
									// alert("点击的节点ID是：" + record.data.id + ",文字是：" + record.data.text + ",url是：" + record.data.indexUrl);
									if (record.isExpanded()) {
										view.collapse(record);
									}
									else {
										view.expand(record);
									}
									this.loadMenuData(record.get('id'));
								},
								'itemcontextmenu' : function(menutree, record, items, index, e) {
									e.preventDefault();
									e.stopEvent();

									var self = this;
									var fm = self.getEditPanel().down('form').getForm();
									fm.clearInvalid();

									var nodemenu = new Ext.menu.Menu({
												floating : true,
												items : [{
															text : "新增子菜单",
															iconCls : 'add',
															handler : function() {
																self.onInitAddForm(record.get('id'), record.get('text'));
															}
														}, {
															text : "编辑",
															iconCls : 'edit',
															handler : function() {
																self.loadMenuData(record.get('id'));
															}
														}, {
															text : "删除",
															iconCls : 'delete',
															handler : function() {
																self.deleteMenuInfo(record.get('id'));
															}
														}]
											});
									nodemenu.showAt(e.getXY());
								}
							},
							'editpanel toolbar button[action=btnClear]' : {
								click : this.onClearClick
							},
							'editpanel toolbar button[action=btnSave]' : {
								click : this.onSaveClick
							},
							'editpanel toolbar button[action=btnDelete]' : {
								// onDeleteClick有参数，不能用 click : this.onDeleteClick
								click : function(btn, e) {
									this.deleteMenuInfo();
								}
							},
							'menupanel tool[type=refresh]' : {
								click : this.refreshAllNode
							},
							'menupanel toolbar button[action=refresh]' : {
								click : this.refreshAllNode
							},
							'menupanel toolbar button[action=expand]' : {
								click : function(btn, e) {
									btn.up('treepanel').collapseAll();
									btn.up('treepanel').expandAll();
								}
							},
							'menupanel toolbar button[action=collapse]' : {
								click : function(btn, e) {
									btn.up('treepanel').collapseAll();
								}
							},
							'menupanel toolbar button[action=delete]' : {
								click : function(btn, e) {
									this.deleteMenuInfo();
								}
							},
							'menupanel toolbar button[action=add]' : {
								click : function(btn, e) {
									this.onInitAddForm();
								}
							}
						});
			},
			refreshAllNode : function(btn, e) {
				var tpanel = this.getMenuPanel();
				tpanel.getRootNode().removeAll();
				tpanel.getStore().load({
							scope : this,
							callback : function(records, operation, success) {
								tpanel.expandAll();
							}
						});
			},
			loadMenuDataOnMouseDown : function(selModel, record) {
				this.loadMenuData(record.get('id'));
			},
			loadMenuData : function(menuId) {
				if (!Ext.isEmpty(menuId) && menuId != '0') {
					var fm = this.getEditPanel().down('form').getForm();
					fm.reset();

					fm.findField('operFlag').setValue('update');
					this.getEditPanel().down('toolbar').child('button[action=btnSave]').setText('修改');

					Ext.getBody().mask("记录加载中，请稍等...", "x-mask-loading");
					Ext.Ajax.request({
								url : appPath + '/sysadmin/menu/getMenuInfoCtrl.do',
								method : 'POST',
								params : {
									menuId : menuId,
									rand : new Date()
								},
								success : function(response) {
									Ext.getBody().unmask();
									var data = Ext.JSON.decode(response.responseText);

									fm.findField('menuId').setValue(data.menuInfo.menuId);
									fm.findField('menuName').setValue(data.menuInfo.menuName);
									fm.findField('siteNo').setValue(data.menuInfo.siteNo);
									fm.findField('status').setValue('' + data.menuInfo.status);
									fm.findField('remark').setValue(data.menuInfo.remark);
									fm.findField('indexUrl').setValue(data.menuInfo.indexUrl);

									fm.findField('parentId').setValue(data.menuInfo.parentId);
									if (data.menuInfo.parentId == 0) {
										fm.findField('parentId').setRawValue('菜单列表');
									}
									else {
										fm.findField('parentId').setRawValue(data.parentName);
									}

									var otherUrl = '';
									for (var i = 0; i < data.menuFileUrl.length; i++) {
										otherUrl += data.menuFileUrl[i].fileUrl + ((i == data.menuFileUrl.length - 1) ? '' : '\r\n');
									}
									fm.findField('otherUrl').setValue(otherUrl);
									fm.clearInvalid();
								},
								failure : function(response) {
									Ext.getBody().unmask();
								}
							});
				}
				else {
					this.onInitAddForm();
				}
			},
			onInitAddForm : function(parentId, parentName) {
				var fm = this.getEditPanel().down('form').getForm();
				fm.reset();
				fm.findField('operFlag').setValue('add');
				this.getEditPanel().down('toolbar').child('button[action=btnSave]').setText('添加');
				// this.getEditPanel().down('toolbar').child('label[action=lblTips]').setVisible(false);
				fm.findField('status').setValue('1');
				// fm.findField('menuName').focus();
				if (!Ext.isEmpty(parentId) && !Ext.isEmpty(parentName)) {
					if (parentId == '0') {
						fm.findField('parentId').setValue(parentId);
						fm.findField('parentId').setRawValue('菜单列表');
					}
					else {
						fm.findField('parentId').setValue(parentId);
						fm.findField('parentId').setRawValue(parentName);
					}
				}
				fm.clearInvalid();
			},
			onClearClick : function(btn, e) {
				var fm = this.getEditPanel().down('form').getForm();
				if (fm.findField('operFlag').getValue() == 'update') {
					fm.findField('menuName').setValue('');
					fm.findField('siteNo').setValue('');
					fm.findField('remark').setValue('');
					fm.findField('indexUrl').setValue('');
					fm.findField('otherUrl').setValue('');
					fm.clearInvalid();
				}
				else {
					this.onInitAddForm();
				}
			},
			onSaveClick : function(btn, e) {
				var self = this;
				var fm = this.getEditPanel().form.getForm();
				var menuId = fm.findField('menuId').getValue();
				var parentId = fm.findField('parentId').getValue();
				var operFlag = fm.findField('operFlag').getValue();
				if (!Ext.isEmpty(menuId) && menuId == parentId) {
					Ext.Msg.alert('提示信息', '上级菜单不能是自己！');
					return;
				}
				else {
					if (fm.isValid()) {
						fm.disabled = true;
						fm.submit({
									url : appPath + '/sysadmin/menu/saveMenuCtrl.do',
									method : 'post',
									type : 'ajax',
									waitTitle : '请等待',
									waitMsg : '正在保存...',
									success : function(form, action) {
										var data = Ext.JSON.decode(action.response.responseText);
										self.refreshTreePanel(data.menuId);
										Ext.Msg.alert('提示信息', data.message);
									},
									failure : pub_form_excepHandler
								});
						fm.disabled = false;
					}
				}
			},
			deleteMenuInfo : function(deleteMenuId) {
				var self = this;
				var fm = this.getEditPanel().down('form').getForm();
				fm.clearInvalid();

				var menuId = '';
				if (!Ext.isEmpty(deleteMenuId)) {
					menuId = deleteMenuId;
				}
				else {
					menuId = fm.findField('menuId').getValue();
				}

				if (Ext.isEmpty(menuId) || menuId == '0') {
					Ext.Msg.alert('提示', '请选择要删除的菜单！');
					return;
				}
				else {
					Ext.Msg.confirm('提示', '确认删除所选菜单？', callBackDelete);
					function callBackDelete(selectConfirmId) {
						if (Ext.util.Format.lowercase(selectConfirmId) == 'yes') {
							Ext.getBody().mask("记录删除中，请稍等...", "x-mask-loading");
							Ext.Ajax.request({
										url : appPath + '/sysadmin/menu/deleteMenuCtrl.do',
										method : 'POST',
										params : {
											operFlag : 'delete',
											menuId : menuId,
											rand : new Date()
										},
										success : function(response) {
											var data = Ext.JSON.decode(response.responseText);
											self.refreshTreePanel(null);
											Ext.getBody().unmask();
											self.onInitAddForm();
										},
										failure : function(response) {
											Ext.getBody().unmask();
										}
									});
						}
					}
				}
			},
			refreshOneTreePanel : function(obj, selectedId) {
				obj.getRootNode().removeAll();
				obj.getStore().load({
							scope : this,
							node : obj.getRootNode(),
							callback : function(records, operation, success) {
								obj.expandAll(function() {
											if (!Ext.isEmpty(selectedId)) {
												var node2 = obj.getStore().getNodeById(selectedId);
												if (!Ext.isEmpty(node2)) {
													obj.selectPath(node2.getPath(), 'id');
												}
											}
										}, this);

							}
						});
			},
			refreshTreePanel : function(parentId, selectedId) {
				var mPanel = this.getMenuPanel();
				var ePanel = this.getEditPanel().down('combotree').getTreePanel();
				this.refreshOneTreePanel(mPanel, selectedId);
				this.refreshOneTreePanel(ePanel, selectedId);
			}
		});
