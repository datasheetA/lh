Ext.define('App.sysadmin.user.controller.UserCtrl', {
			extend : 'Ext.app.Controller',
			models : ['UserModel'],
			stores : ['UserStore'],
			views : ['UserListGrid', 'UserQueryWin', 'UserEditWin', 'UserAuthEditWin'],
			refs : [{
						ref : 'userListGrid',
						selector : 'userlistgrid'
					}, {
						ref : 'userQueryWin',
						selector : 'userquerywin',
						xtype : 'userquerywin',
						autoCreate : true
					}, {
						ref : 'userEditWin',
						selector : 'usereditwin',
						xtype : 'usereditwin',
						autoCreate : true
					}, {
						ref : 'userAuthEditWin',
						selector : 'userautheditwin',
						xtype : 'userautheditwin',
						autoCreate : true
					}],
			init : function() {
				this.control({
							'userlistgrid toolbar button[action=btnAdd]' : {
								click : this.onAddClick
							},
							'userlistgrid toolbar button[action=btnUpdate]' : {
								click : this.onUpdateClick
							},
							'userlistgrid toolbar button[action=btnDelete]' : {
								click : this.onDeleteClick
							},
							'userlistgrid toolbar button[action=btnQuery]' : {
								click : this.onQueryClick
							},
							'userlistgrid toolbar button[action=btnSee]' : {
								click : this.onSeeClick
							},
							'userlistgrid' : {
								cellclick : this.onCellClick
							},
							'userquerywin button[action=btnDoQuery]' : {
								click : this.onDoQueryClick
							},
							'usereditwin button[action=btnSave]' : {
								click : this.onSaveClick
							},
							'userautheditwin button[action=btnSave]' : {
								click : this.onSaveAuthClick
							},
							'userautheditwin treepanel' : {
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
									}
									node.cascadeBy(function(child) {
												child.set("checked", checked);
											});
								}
							},
							'userautheditwin tool[type=refresh]' : {
								click : function(btn) {
									var tpanel = btn.up('treepanel');
									tpanel.getRootNode().removeAll();
									tpanel.getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													tpanel.expandAll();
												}
											});
								}
							}
						});
			},
			onCellClick : function(grid, td, columnIndex, record, tr, rowIndex, e) {
				var cellId = grid.getHeaderCt().getHeaderAtIndex(columnIndex).id;
				var userId = record.get('user_id');
				switch (cellId) {
					case 'see' :
						this.getUserInfo(userId, 'see');
						break;
					case 'update' :
						this.getUserInfo(userId, 'update');
						break;
					case 'delete' :
						this.deleteUser(userId);
						break;
					case 'grant' :
						this.grantAuthWin(userId);
						break;
				}
			},
			onQueryClick : function(btn, e) {
				this.getUserQueryWin().show();
			},
			onDoQueryClick : function(btn, e) {
				this.getUserListGrid().down('toolbar').child('searchfield').onTrigger1Click();
				var ds = this.getUserListGrid().getStore();
				var fm = this.getUserQueryWin().form.getForm();
				ds.proxy.extraParams = {};
				ds.proxy.extraParams['userAccount'] = fm.findField('queryUserAccount').getValue();
				ds.proxy.extraParams['userName'] = fm.findField('queryUserName').getValue();
				ds.proxy.extraParams['status'] = fm.findField('queryStatus').getValue();
				ds.proxy.extraParams['tel'] = fm.findField('queryTel').getValue();
				ds.loadPage(1);
				this.getUserQueryWin().hide();
			},
			onAddClick : function(btn, e) {
				this.getUserEditWin().setTitle('添加用户');
				var btnSave = this.getUserEditWin().down('toolbar').child('button[action=btnSave]');
				btnSave.setVisible(true);
				btnSave.setText('添加');

				var fm = this.getUserEditWin().down('form').getForm();
				fm.reset();// 不加文件上传按钮不能点

				fm.findField('operFlag').setValue('add');
				fm.findField('userId').setValue('');

				fm.findField('userAccount').setFieldLabel('用户账号<font color=red>*</font>');
				fm.findField('passwd').setFieldLabel('密码<font color=red>*</font>');
				fm.findField('passwd1').setFieldLabel('密码确认<font color=red>*</font>');
				fm.findField('status').setFieldLabel('状态<font color=red>*</font>');

				fm.findField('userAccount').setReadOnly();
				fm.findField('userAccount').allowBlank = false;

				fm.findField('passwd').allowBlank = false;
				fm.findField('passwd1').allowBlank = false;

				fm.findField('status').setValue('1');
				fm.findField('passwd').setValue('1111');
				fm.findField('passwd1').setValue('1111');

				fm.findField('province').getStore().removeAll();
				fm.findField('city').getStore().removeAll();
				fm.findField('county').getStore().removeAll();
				fm.findField('district').getStore().removeAll();
				fm.findField('province').getStore().proxy.extraParams['parentId'] = 0;
				fm.findField('province').getStore().load();

				fm.findField('birthplace').reloadRootNode();
				fm.findField('workArea').getTreePanel().getStore().proxy.extraParams['userId'] = '';
				fm.findField('workArea').reloadRootNode(true);
				fm.findField('roleIds').getTreePanel().getStore().proxy.extraParams['userId'] = '';
				fm.findField('roleIds').reloadRootNode(true);

				this.getUserEditWin().show();
			},
			onSeeClick : function(btn, e) {
				var oRecords = this.getUserListGrid().getSelectionModel().getSelection();
				if (oRecords.length == 0) {
					Ext.Msg.alert('提示', '请选择要查看的记录！');
					return;
				}
				else if (oRecords.length > 1) {
					Ext.Msg.alert('提示', '一次只查看一条记录！');
					return;
				}
				else {
					var userId = oRecords[0].get('user_id');

					this.getUserInfo(userId, 'see');
				}
			},
			onUpdateClick : function(btn, e) {
				var oRecords = this.getUserListGrid().getSelectionModel().getSelection();
				if (oRecords.length == 0) {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
					return;
				}
				else if (oRecords.length > 1) {
					Ext.Msg.alert('提示', '一次只修改一条记录！');
					return;
				}
				else {
					var userId = oRecords[0].get('user_id');

					this.getUserInfo(userId, 'update');
				}
			},
			onDeleteClick : function(btn, e) {
				var sIds = []; // 要删除的id
				Ext.each(this.getUserListGrid().getSelectionModel().getSelection(), function(item) {
							sIds.push(item.data.user_id);
						});

				if (sIds.length == 0) {
					Ext.Msg.alert('提示', '请选择要删除的记录！');
					return;
				}
				else {
					this.deleteUser(sIds.join(','));
				}
			},
			onSaveClick : function(btn, e) {
				var fm = this.getUserEditWin().form.getForm();
				var ds = this.getUserListGrid().getStore();

				if (fm.isValid()) {
					fm.disabled = true;
					fm.submit({
								url : appPath + '/sysadmin/user/saveUserCtrl.do',
								method : 'post',
								type : 'ajax',
								waitTitle : '请等待',
								waitMsg : '正在保存...',
								success : function(form, action) {
									var data = Ext.JSON.decode(action.response.responseText);
									// Ext.Msg.alert('提示信息', data.message);
									Ext.defer(function() {
												Ext.Msg.alert('提示信息', data.message);
											}, 100);
									ds.load();
								},
								failure : pub_form_excepHandler
							});
					fm.disabled = false;
				}
			},
			getUserInfo : function(userId, operFlag) {
				var btnSave = this.getUserEditWin().down('toolbar').child('button[action=btnSave]');
				var win = this.getUserEditWin();
				var fm = win.down('form').getForm();
				fm.reset();

				switch (operFlag) {
					case 'see' :
						win.setTitle('查看用户');
						btnSave.setVisible(false);
						fm.findField('status').setFieldLabel('状态');
						break;
					case 'update' :
						win.setTitle('修改用户');
						btnSave.setVisible(true);
						btnSave.setText('修改');
						fm.findField('status').setFieldLabel('状态<font color=red>*</font>');
						break;
					default :
						win.setTitle('');
						break;
				}

				fm.findField('operFlag').setValue(operFlag);

				fm.findField('userAccount').setFieldLabel('用户账号');
				fm.findField('passwd').setFieldLabel('密码');
				fm.findField('passwd1').setFieldLabel('密码确认');

				fm.findField('userAccount').setReadOnly('true');
				fm.findField('userAccount').allowBlank = true;
				fm.findField('passwd').allowBlank = true;
				fm.findField('passwd1').allowBlank = true;

				Ext.getBody().mask("记录加载中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/user/getUserInfoCtrl.do',
							params : {
								userId : userId,
								rand : new Date()
							},
							method : 'POST',
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);

								fm.findField('userId').setValue(userId);
								fm.findField('userAccount').setValue(data.userAccount.userAccount);
								fm.findField('userName').setValue(data.userAccount.userName);
								fm.findField('status').setValue('' + data.userAccount.status);

								fm.findField('birthday').setValue(rendererDate(data.userInfo.birthday));
								fm.findField('certNumber').setValue(data.userInfo.certNumber);

								fm.findField('address').setValue(data.userInfo.address);
								fm.findField('postalcode').setValue(data.userInfo.postalcode);
								fm.findField('tel').setValue(data.userInfo.tel);
								fm.findField('mobile').setValue(data.userInfo.mobile);
								fm.findField('qq').setValue(data.userInfo.qq);
								fm.findField('email').setValue(data.userInfo.email);
								fm.findField('msn').setValue(data.userInfo.msn);
								fm.findField('intro').setValue(data.userInfo.intro);

								fm.findField('birthplace').reloadRootNode();
								if (data.userInfo.birthplace != 0) {
									fm.findField('birthplace').setValue(data.userInfo.birthplace);
									fm.findField('birthplace').setRawValue(data.birthplaceString);
								}

								fm.findField('workArea').getTreePanel().getStore().proxy.extraParams['userId'] = userId;
								fm.findField('workArea').reloadRootNode(true);
								if (!Ext.isEmpty(data.userInfo.workArea)) {
									fm.findField('workArea').setValue(data.userInfo.workArea);
									fm.findField('workArea').setRawValue(data.workAreaString);
								}

								fm.findField('roleIds').getTreePanel().getStore().proxy.extraParams['userId'] = userId;
								fm.findField('roleIds').reloadRootNode(true);
								if (!Ext.isEmpty(data.roleIds)) {
									fm.findField('roleIds').setValue(data.roleIds);
									fm.findField('roleIds').setRawValue(data.roleNames);
								}

								if (true) {
									fm.findField('province').getStore().proxy.extraParams['parentId'] = 0;
									fm.findField('province').getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													if (!Ext.isEmpty(data.userInfo.province) && data.userInfo.province != 0) {
														fm.findField('province').setValue(data.userInfo.province);
													}
												}
											});
								}

								if (!Ext.isEmpty(data.userInfo.province) && data.userInfo.province != 0) {
									fm.findField('city').getStore().proxy.extraParams['parentId'] = data.userInfo.province;
									fm.findField('city').getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													if (!Ext.isEmpty(data.userInfo.city) && data.userInfo.city != 0) {
														fm.findField('city').setValue(data.userInfo.city);
													}
												}
											});
								}

								if (!Ext.isEmpty(data.userInfo.city) && data.userInfo.city != 0) {
									fm.findField('county').getStore().proxy.extraParams['parentId'] = data.userInfo.city;
									fm.findField('county').getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													if (!Ext.isEmpty(data.userInfo.county) && data.userInfo.county != 0) {
														fm.findField('county').setValue(data.userInfo.county);
													}
												}
											});
								}

								if (!Ext.isEmpty(data.userInfo.county) && data.userInfo.county != 0) {
									fm.findField('district').getStore().proxy.extraParams['parentId'] = data.userInfo.county;
									fm.findField('district').getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													if (!Ext.isEmpty(data.userInfo.district) && data.userInfo.district != 0) {
														fm.findField('district').setValue(data.userInfo.district);
													}
												}
											});
								}

								if (!Ext.isEmpty(data.userInfo.certType) && data.userInfo.certType != 0) {
									fm.findField('certType').getStore().load({
												scope : this,
												callback : function(records, operation, success) {
													fm.findField('certType').setValue('' + data.userInfo.certType);
												}
											});
								}

								fm.findField('sex').getStore().load({
											scope : this,
											callback : function(records, operation, success) {
												fm.findField('sex').setValue('' + data.userInfo.sex);
											}
										});
								win.show();
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			},
			deleteUser : function(userId) {
				var ds = this.getUserListGrid().getStore();
				Ext.Msg.confirm('提示', '确认删除所选记录？', callBackDelete);
				function callBackDelete(selectConfirmId) {
					if (Ext.util.Format.lowercase(selectConfirmId) == 'yes') {
						Ext.getBody().mask("记录删除中，请稍等...", "x-mask-loading");
						Ext.Ajax.request({
									url : appPath + '/sysadmin/user/deleteUserCtrl.do',
									method : 'POST',
									params : {
										operFlag : 'delete',
										userIds : userId,
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
			loadGrantTreePanel : function(userId) {
				var grantTreePanel = this.getUserAuthEditWin().down('treepanel[id=grantTreePanel]');
				grantTreePanel.getRootNode().removeAll();
				grantTreePanel.getStore().proxy.extraParams['userId'] = userId;
				grantTreePanel.getStore().load({
							scope : this,
							callback : function(records, operation, success) {
								grantTreePanel.expandAll();
							}
						});
			},
			loadRealTreePanel : function(userId) {
				var realTreePanel = this.getUserAuthEditWin().down('treepanel[id=realTreePanel]');
				realTreePanel.getRootNode().removeAll();
				realTreePanel.getStore().proxy.extraParams['userId'] = userId;
				realTreePanel.getStore().load({
							scope : this,
							callback : function(records, operation, success) {
								realTreePanel.expandAll();
							}
						});
			},
			grantAuthWin : function(userId) {
				var win = this.getUserAuthEditWin();
				this.loadGrantTreePanel(userId);
				this.loadRealTreePanel(userId);

				Ext.getBody().mask("记录加载中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/user/getUserInfoCtrl.do',
							method : 'POST',
							params : {
								userId : userId,
								rand : new Date()
							},
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);
								win.down('hidden[id=authUserId]').setValue(userId);
								win.down('label[id=authUserAccount]').setText(data.userAccount.userAccount);
								win.show();
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			},
			onSaveAuthClick : function(btn, e) {
				var self = this;
				var win = this.getUserAuthEditWin();
				var userId = win.down('hidden[id=authUserId]').getValue();
				var menuIds = [];
				Ext.each(win.down('treepanel[id=grantTreePanel]').getChecked(), function(node) {
							menuIds.push(node.data.id);
						});

				Ext.getBody().mask("保存数据中，请稍等...", "x-mask-loading");
				Ext.Ajax.request({
							url : appPath + '/sysadmin/user/saveUserAuthCtrl.do',
							method : 'POST',
							params : {
								userId : userId,
								menuIds : menuIds.join(','),
								rand : new Date()
							},
							success : function(response) {
								Ext.getBody().unmask();
								var data = Ext.JSON.decode(response.responseText);
								self.loadRealTreePanel(userId);
								Ext.Msg.alert('提示信息', '保存权限成功！');
							},
							failure : function(response) {
								Ext.getBody().unmask();
							}
						});
			}
		});
