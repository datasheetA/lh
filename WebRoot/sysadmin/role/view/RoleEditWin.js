// 编辑窗口
Ext.define('App.sysadmin.role.view.RoleEditWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.roleeditwin',

			title : '角色编辑',
			layout : 'fit',
			height : 460,
			width : 600,
			closeAction : 'hide',

			initComponent : function() {
				this.form = Ext.create('Ext.form.Panel', {
							bodyPadding : '5 5 5 5',
							defaults : {
								xtype : 'textfield',
								labelWidth : 65,
								labelAlign : 'right',
								margin: 2,
								maxLength : 30,
								allowBlank : true
							},
							frame : true,
							border : false,
							layout : 'column',
							items : [{
										xtype : 'hidden',
										id : 'operFlag',
										name : 'operFlag'
									}, {
										xtype : 'hidden',
										id : 'roleId',
										name : 'roleId'
									}, {
										xtype : 'hidden',
										id : 'userIds',
										name : 'userIds'
									}, {
										columnWidth : 1,
										height : 26,
										xtype : 'label',
										text : '请在下面输入角色信息：'
									}, {
										columnWidth : .5,
										fieldLabel : '角色名称<font color=red>*</font>',
										id : 'roleName',
										name : 'roleName',
										allowBlank : false,
										blankText : '角色名称必须填写',
										minLength : 1,
										maxLength : 30
									}, {
										columnWidth : .5,
										xtype : 'combo',
										id : 'status',
										name : 'status',
										store : Ext.create('App.common.store.EditStatus'),
										fieldLabel : '状态<font color=red>*</font>',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										allowBlank : false,
										forceSelection : true,
										triggerAction : 'all'
									}, {
										columnWidth : 1,
										xtype : 'textarea',
										fieldLabel : '备注',
										name : 'remark',
										maxLength : 120,
										height : 60
									}, {
										columnWidth : 1,
										height : 26,
										xtype : 'label',
										text : '请选择拥有此角色的用户：'
									},{
										columnWidth : 1,
										xtype : 'ddrolegrid',
										height : 230
									}]
						});
				this.items = [this.form];
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