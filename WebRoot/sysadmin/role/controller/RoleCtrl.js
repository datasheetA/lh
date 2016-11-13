Ext.define('App.sysadmin.role.controller.RoleCtrl', {
			extend : 'Ext.app.Controller',
			models : ['RoleModel'],
			stores : ['RoleStore','UsableRoleUserStore','SelectedRoleUserStore'],
			views : ['RoleListGrid', 'RoleQueryWin', 'RoleEditWin', 'DDRoleGrid', 'RoleAuthEditWin'],
			refs : [{
						ref : 'roleListGrid',
						selector : 'rolelistgrid'
					}, {
						ref : 'roleQueryWin',
						selector : 'rolequerywin',
						xtype : 'rolequerywin',
						autoCreate : true
					}, {
						ref : 'roleEditWin',
						selector : 'roleeditwin',
						xtype : 'roleeditwin',
						autoCreate : true
					}, {
						ref : 'roleAuthEditWin',
						selector : 'roleautheditwin',
						xtype : 'roleautheditwin',
						autoCreate : true
					}],
			init : function() {
				this.control({
							'rolelistgrid toolbar button[action=btnAdd]' : {
								click : this.onAddClick
							},
							'rolelistgrid toolbar button[action=btnUpdate]' : {
								click : this.onUpdateClick
							},
							'rolelistgrid toolbar button[action=btnDelete]' : {
								click : this.onDeleteClick
							},
							'rolelistgrid toolbar button[action=btnQuery]' : {
								click : this.onQueryClick
							},
							'rolelistgrid toolbar button[action=btnSee]' : {
								click : this.onSeeClick
							},
							'rolequerywin button[action=btnDoQuery]' : {
								click : this.onDoQueryClick
							},
							'roleeditwin button[action=btnSave]' : {
								click : this.onSaveClick
							},
							'roleeditwin button[action=btnAddSelected]' : {
								click : this.onAddSelectedClick
							},
							'roleeditwin button[action=btnRemoveSelected]' : {
								click : this.onRemoveSelectedClick
							},
							'roleautheditwin button[action=btnSave]' : {
								click : this.onSaveAuthClick
							},
							'roleautheditwin treepanel' : {
								afterrender : function(obj, eOpts) {
									// obj.expandAll();
								},
								checkchange : function(node, checked) {
									var pNode = node.parentNode;
									for (; pNode != null && pNode.data.id != '0'; pNode = pNode.parentNode) {
										var pChecked = false;
										pNode.eachChild(function(child) {
													if (child.data.checked) {
														pChecked = true;
													}
												});
										pNode.set("checked", pChecked);
										// pNode.data.checked = pChecked;
										// pNode.updateInfo({
										// checked : pChecked
										// });
									}
									node.cascadeBy(function(child) {
												child.set("checked", checked);
											});
								}
							},
							'roleautheditwin tool[type=refresh]' : {
								click : function(btn, e) {
									var tpanel = btn.up('treepanel');
									tpanel.getRootNode().removeAll();
									tpanel.getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													// tpanel.getRootNode().expand();
													tpanel.expandAll();
												}
											});
								}
							},
							'rolelistgrid' : {
								cellclick : this.onCellClick
							}
						});
			},
			onCellClick : function(grid, td, columnIndex, record, tr, rowIndex, e) {
				var cellId = grid.getHeaderCt().getHeaderAtIndex(columnIndex).id;
				var roleId = record.get('role_id');
				switch (cellId) {
					case 'see' :
						this.getRoleInfo(roleId, 'see');
						break;
					case 'update' :
						this.getRoleInfo(roleId, 'update');
						break;
					case 'delete' :
						this.deleteRole(roleId);
						break;
					case 'grant' :
						this.grantAuthWin(roleId);
						break;
				}
			},
			onAddSelectedClick : function(btn, e) {
				var usableGridStore = this.getRoleEditWin().down('grid[id=gridUsableUser]').getStore();
				var selectedGridStore = this.getRoleEditWin().down('grid[id=gridSelectedUser]').getStore();

				Ext.each(this.getRoleEditWin().down('grid[id=gridUsableUser]').getSelectionModel().getSelection(), function(item) {
							selectedGridStore.add(item);
							usableGridStore.remove(item);
						});
			},
			onRemoveSelectedClick : function(btn, e) {
				var usableGridStore = this.getRoleEditWin().down('grid[id=gridUsableUser]').getStore();
				var selectedGridStore = this.getRoleEditWin().down('grid[id=gridSelectedUser]').getStore();
				Ext.each(this.getRoleEditWin().down('grid[id=gridSelectedUser]').getSelectionModel().getSelection(), function(item) {
							usableGridStore.add(item);
							selectedGridStore.remove(item);
						});
			},
			onQueryClick : function(btn, e) {
				this.getRoleQueryWin().show();
			},
			onDoQueryClick : function(btn, e) {			
			    this.getRoleListGrid().down('toolbar').child('searchfield').onTrigger1Click();
				var ds = this.getRoleListGrid().getStore();
				var fm = this.getRoleQueryWin().form.getForm();
				ds.proxy.extraParams = {};
				ds.proxy.extraParams['roleName'] = fm.findField('queryRoleName').getValue();
				ds.proxy.extraParams['status'] = fm.findField('queryStatus').getValue();
				ds.proxy.extraParams['remark'] = fm.findField('queryRemark').getValue();
				ds.loadPage(1);
				this.getRoleQueryWin().hide();
			},
			onAddClick : function(btn, e) {
				this.getRoleEditWin().setTitle('添加角色');
				var btnSave = this.getRoleEditWin().down('toolbar').child('button[action=btnSave]');
				btnSave.setVisible(true);
				btnSave.setText('添加');

				var fm = this.getRoleEditWin().down('form').getForm();
				fm.reset();// 不加文件上传按钮不能点

				fm.findField('operFlag').setValue('add');
				fm.findField('roleId').setValue('');

				fm.findField('roleName').setFieldLabel('角色名称<font color=red>*</font>');
				fm.findField('status').setFieldLabel('状态<font color=red>*</font>');

				fm.findField('roleName').allowBlank = false;
				fm.findField('status').allowBlank = false;

				fm.findField('status').setValue('1');

				var usableGridStore = this.getRoleEditWin().down('grid[id=gridUsableUser]').getStore();
				var selectedGridStore = this.getRoleEditWin().down('grid[id=gridSelectedUser]').getStore();
				usableGridStore.proxy.extraParams['roleId'] = 0;
				usableGridStore.load();
				selectedGridStore.proxy.extraParams['roleId'] = 0;
				selectedGridStore.removeAll();

				this.getRoleEditWin().show();
			},
			onSeeClick : function(btn, e) {
				var oRecords = this.getRoleListGrid().getSelectionModel().getSelection();
				if (oRecords.length == 0) {
					Ext.Msg.alert('提示', '请选择要查看的记录！');
					return;
				}
				else if (oRecords.length > 1) {
					Ext.Msg.alert('提示', '一次只查看一条记录！');
					return;
				}
				else {
					var roleId = oRecords[0].get('role_id');

					this.getRoleInfo(roleId, 'see');
				}
			},
			onUpdateClick : function(btn, e) {
				var oRecords = this.getRoleListGrid().getSelectionModel().getSelection();
				if (oRecords.length == 0) {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
					return;
				}
				else if (oRecords.length > 1) {
					Ext.Msg.alert('提示', '一次只修改一条记录！');
					return;
				}
				else {
					var roleId = oRecords[0].get('role_id');

					this.getRoleInfo(roleId, 'update');
				}
			},
			onDeleteClick : function(btn, e) {
				var sIds = []; // 要删除的id
				Ext.each(this.getRoleListGrid().getSelectionModel().getSelection(), function(item) {
							sIds.push(item.data.role_id);
						});

				if (sIds.length == 0) {
					Ext.Msg.alert('提示', '请选择要删除的记录！');
					return;
				}
				else {
					this.deleteRole(sIds.join(','));
				}
			},
			onSaveClick : function(btn, e) {
				var fm = this.getRoleEditWin().form.getForm();
				var ds = this.getRoleListGrid().getStore();

				var userIds = '';
				var selectedGridStore = this.getRoleEditWin().down('grid[id=gridSelectedUser]').getStore();
				selectedGridStore.each(function(record) {
							userIds += ((userIds == '') ? '' : ',') + record.get('user_id');
						});
				fm.findField('userIds').setValue(userIds);

				if (fm.isValid()) {
					fm.disabled = true;
					fm.submit({
								url : appPath + '/sysadmin/role/saveRoleCtrl.do',
								method : 'post',
								type : 'ajax',
								waitTitle : '请等待',
								waitMsg : '正在保存...',
								success : function(form, action) {
									var data = Ext.JSON.decode(action.response.responseText);
									Ext.Msg.alert('提示信息', data.message);
									ds.load();
								},
								failure : pub_form_excepHandler
							});
					fm.disabled = false;
				}
			},
			getRoleInfo : function(roleId, operFlag) {
				var usableGridStore = this.getRoleEditWin().down('grid[id=gridUsableUser]').getStore();
				var selectedGridStore = this.getRoleEditWin().down('grid[id=gridSelectedUser]').getStore();
				var btnSave = this.getRoleEditWin().down('toolbar').child('button[action=btnSave]');
				var win = this.getRoleEditWin();
				var fm = win.down('form').getForm();
				fm.reset();

				switch (operFlag) {
					case 'see' :
						win.setTitle('查看角色');
						btnSave.setVisible(false);

						fm.findField('roleName').setFieldLabel('角色名称');
						fm.findField('roleName').allowBlank = true;

						fm.findField('status').setFieldLabel('状态');
						fm.findField('status').allowBlank = true;
						break;
					case 'update' :
						win.setTitle('修改角色');
						btnSave.setVisible(true);
						btnSave.setText('修改');

						fm.findField('roleName').setFieldLabel('角色名称<font color=red>*</font>');
						fm.findField('roleName').allowBlank = false;

						fm.findField('status').setFieldLabel('状态<font color=red>*</font>');
						fm.findField('status').allowBlank = false;
						break;
					default :
						win.setTitle('');
						break;
				}

				fm.findField('operFlag').setValue(operFlag);

				var ddGrid = this.getRoleEditWin().down('ddrolegrid').items;

				Ext.getBody().mask("记录加载中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/role/getRoleInfoCtrl.do',
							method : 'POST',
							params : {
								roleId : roleId,
								rand : new Date()
							},
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);

								fm.findField('roleId').setValue(data.roleInfo.roleId);
								fm.findField('roleName').setValue(data.roleInfo.roleName);
								fm.findField('status').setValue('' + data.roleInfo.status);
								fm.findField('remark').setValue(data.roleInfo.remark);

								usableGridStore.proxy.extraParams['roleId'] = roleId;
								usableGridStore.load();

								selectedGridStore.proxy.extraParams['roleId'] = roleId;
								selectedGridStore.load();

								win.show();
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			},
			deleteRole : function(roleId) {
				var ds = this.getRoleListGrid().getStore();
				Ext.Msg.confirm('提示', '确认删除所选记录？', callBackDelete);
				function callBackDelete(selectConfirmId) {
					if (Ext.util.Format.lowercase(selectConfirmId) == 'yes') {
						Ext.getBody().mask("记录删除中，请稍等...", "x-mask-loading");
						Ext.Ajax.request({
									url : appPath + '/sysadmin/role/deleteRoleCtrl.do',
									method : 'POST',
									params : {
										operFlag : 'delete',
										roleIds : roleId,
										rand : new Date()
									},
									success : function(response) {
										Ext.getBody().unmask();
										var data = Ext.JSON.decode(response.responseText);
										ds.load();
									},
									failure : function(response) {
										Ext.getBody().unmask();
									}
								});
					}
				}
			},
			grantAuthWin : function(roleId) {
				var win = this.getRoleAuthEditWin();
				var tpanel = win.down('treepanel');
				tpanel.getRootNode().removeAll();
				tpanel.getStore().proxy.extraParams['roleId'] = roleId;
				tpanel.getStore().load({
							scope : this,
							callback : function(records, operation, success) {
								tpanel.expandAll();
							}
						});

				Ext.getBody().mask("记录加载中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/role/getRoleInfoCtrl.do',
							method : 'POST',
							params : {
								roleId : roleId,
								rand : new Date()
							},
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);
								win.down('hidden[id=authRoleId]').setValue(roleId);
								win.down('label[id=authRoleName]').setText(data.roleInfo.roleName);
								win.show();
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			},
			onSaveAuthClick : function(btn, e) {
				var win = this.getRoleAuthEditWin();
				var roleId = win.down('hidden[id=authRoleId]').getValue();
				var menuIds = [];
				Ext.each(win.down('treepanel').getChecked(), function(node) {
							menuIds.push(node.data.id);
						});

				Ext.getBody().mask("保存数据中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/role/saveRoleAuthCtrl.do',
							method : 'POST',
							params : {
								roleId : roleId,
								menuIds : menuIds.join(','),
								rand : new Date()
							},
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);
								Ext.Msg.alert('提示信息', '保存权限成功！');
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			}
		});
