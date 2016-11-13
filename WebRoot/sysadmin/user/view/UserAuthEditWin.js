// 编辑窗口
Ext.define('App.sysadmin.user.view.UserAuthEditWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.userautheditwin',

			title : '用户权限编辑',
			layout : 'fit',
			width : document.body.clientWidth * 0.8,
			height : 450,
			closeAction : 'hide',

			border : false,
			initComponent : function() {
				this.items = Ext.create('Ext.Panel', {
							bodyPadding : '2 2 2 2',
							layout : 'border',
							items : [{
										region : 'north',
										frame : true,
										bodyPadding : '5 5 5 5',
										baseCls : 'my-panel-no-border',
										items : [{
													xtype : 'hidden',
													id : 'authUserId',
													name : 'authUserId'
												}, {
													xtype : 'label',
													text : '请为用户('
												}, {
													xtype : 'label',
													id : 'authUserAccount',
													text : ''
												}, {
													xtype : 'label',
													text : ')授权:'
												}]
									}, {
										xtype : 'treepanel',
										id : 'grantTreePanel',
										name : 'grantTreePanel',
										region : 'west',
										title : '菜单授权',
										split : false,
										width : '50%',
										store : Ext.create('App.sysadmin.user.store.MenuStore', {
													autoLoad : false,
													storeUrl : appPath + '/sysadmin/user/getGrantUserAuthCtrl.do'
												}),
										tools : [{
													type : 'refresh'
												}],
										listeners : {}
									}, {
										xtype : 'treepanel',
										id : 'realTreePanel',
										name : 'realTreePanel',
										region : 'center',
										title : '实际权限(含所属角色权限)',
										split : false,
										width : '50%',
										store : Ext.create('App.sysadmin.user.store.MenuStore', {
													autoLoad : false,
													storeUrl : appPath + '/sysadmin/user/getRealUserAuthCtrl.do'
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