// 查询窗口
Ext.define('App.sysadmin.user.view.UserQueryWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.userquerywin',

			title : '用户查询',
			layout : 'fit',
			height : 300,
			width : 500,
			closeAction : 'hide',

			initComponent : function() {
				this.form = Ext.create('Ext.form.Panel', {
							bodyPadding : '5 5 5 5',
							defaults : {
								xtype : 'textfield',
								labelWidth : 80,
								labelAlign : 'right',
								margin: 2,
								maxLength : 30,
								allowBlank : true
							},
							frame : true,
							border : false,
							baseCls: 'my-panel-no-border',
							layout : 'column',
							items : [{
										columnWidth : 1,
										height : 26,
										xtype : 'label',
										text : '请在下面输入查询条件：'
									}, {
										columnWidth : .5,
										fieldLabel : '用户账号',
										name : 'queryUserAccount'
									}, {
										columnWidth : .5,
										fieldLabel : '用户名称',
										name : 'queryUserName'
									}, {
										columnWidth : .5,
										xtype : 'combo',
										id : 'queryStatus',
										name : 'queryStatus',
										store : Ext.create('App.common.store.QueryStatus'),
										fieldLabel : '状态',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										forceSelection : true,
										triggerAction : 'all'
									}, {
										columnWidth : .5,
										fieldLabel : '联系电话',
										name : 'queryTel'
									}]
						});
				this.items = [this.form];
				this.buttons = [{
							text : '查询',
							scope : this,
							action : 'btnDoQuery'
						}, {
							text : '关闭',
							scope : this,
							handler : function(btn) {
								this.hide();
							}
						}];

				this.callParent();
			}

		});