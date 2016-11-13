// 编辑窗口

Ext.define('App.sysadmin.role.view.RoleAuthEditWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.roleautheditwin',

			title : '角色权限编辑',
			layout : 'fit',
			height : 450,
			width : 600,
			closeAction : 'hide',

			initComponent : function() {
				this.items = Ext.create('Ext.Panel', {
							bodyPadding : '2 2 2 2',
							frame : true,
							border : false,
							items : [{
										xtype : 'hidden',
										id : 'authRoleId',
										name : 'authRoleId'
									}, {
										xtype : 'label',
										text : '请为角色('
									}, {
										xtype : 'label',
										id : 'authRoleName',
										text : ''
									}, {
										xtype : 'label',
										text : ')授权:'
									}, {
										xtype : 'treepanel',
										id : 'authMenuId',
										name : 'authMenuId',
										title : '系统菜单',
										split : false,
										padding : '0 0 0 0',
										height : 360,
										store : Ext.create('App.sysadmin.role.store.MenuStore', {
													autoLoad : false
												}),
										tools : [{
													type : 'refresh'
												}],
										listeners : {}
									}]
						});
				this.buttons = [{
							text : '保存',
							action : 'btnSave',
							scope : this
						}, {
							text : '关闭',
							scope : this,
							handler : function(btn) {
								this.hide();
							}
						}];
				this.listeners = {

				}
				this.callParent();
			}

		});