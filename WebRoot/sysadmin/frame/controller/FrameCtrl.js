varCenterTabPanel=null;
Ext.define('App.sysadmin.frame.controller.FrameCtrl', {
			extend : 'Ext.app.Controller',
			models : ['MenuModel'],
			stores : ['MenuStore'],
			views : ['NorthPanel', 'WestPanel', 'CenterTabPanel','SouthPanel'],
			refs : [{
						ref : 'westPanel',
						selector : 'viewport > westpanel'// 和App.sysadmin.frame.view.WestPanel中的alias对应
					}, {
						ref : 'northPanel',
						selector : 'viewport > northpanel'
					}, {
						ref : 'centerTabPanel',
						selector : 'viewport > centertabpanel'
					},{
						ref : 'southpanel',
						selector : 'viewport > southpanel'
						
					}],
			init : function() {
				
				this.control({
							'westpanel' : {
								itemmousedown : this.loadMenu,
								itemclick : function(view, record, node, i, e) {
									//alert("单击的节点ID是：" + record.data.id + ",文字是：" + record.data.text + ",url是：" + record.data.indexUrl);
								
									if (record.isExpanded()) {
										view.collapse(record);
									}
									else {
										view.expand(record);
									}
								},
								itemdblclick: function(view, record, node, i, e) {
									//alert("双击的节点ID是：" + record.data.id + ",文字是：" + record.data.text + ",url是：" + record.data.indexUrl);
									if (record.isExpanded()) {
										view.collapse(record);
									}
									else {
										view.expand(record);
									}
								}
							},
							'northpanel' : {
								afterrender : function(obj, eOpts) {
									this.getLoginUserAccount();
								}
							},
							'centertabpanel':{
								afterrender : function(value) {
									varCenterTabPanel=value;
								}	
							}
						});
			},
			getLoginUserAccount : function() {
				
				var topPanel = this.getNorthPanel();
				Ext.Ajax.request({
							url : appPath + '/sysadmin/frame/getLoginUserAccountCtrl.do',
							method : 'POST',
							params : {
								rand : new Date()
							},
							success : function(response) {
								var data = Ext.JSON.decode(response.responseText);
								document.getElementById('frameTopLoginUserAccount').innerHTML = data.userAccount;
								document.getElementById('frameTopLoginUserAccount').hight = '100%';
							},
							failure : function(response) {
								//
							}
						});
			},
			openTabPanel : function(panel, id) {
				var o = (typeof panel == "string" ? panel : id || panel.id);
				var main = this.getCenterTabPanel();// Ext.getCmp("centerTabPanel");
				var tab = main.getComponent(o);
				if (tab) {
					main.setActiveTab(tab);
				}
				else if (typeof panel != "string") {
					panel.id = o;
					var p = main.add(panel);
					main.setActiveTab(p);
				}
			},
			loadMenu : function(selModel, record) {
				
				if (record.get('leaf')) {// 叶节点
						//走这里说明已经不能再展开了
						
					var panelPrefix = "openPanel";
					var panel = Ext.getCmp(panelPrefix + record.get('id'));
					
					if (!panel) {
						//说明右边没的这个面板,需要新建在
						panel = Ext.create("Ext.container.Container", {
									id : panelPrefix + record.get('id'),
									title : record.get('text'),
									defaults : {
										autoScroll : true,
										bodyPadding : 0
									},
									loadMask : true,
									html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + appPath
											+ record.get('indexUrl') + '?' + escape(new Date()) + '"></iframe>',
									closable : true
								});
						this.openTabPanel(panel, panelPrefix + record.get('id'));
					}else {
						//走这里说明,已经有了那个面板,只是激活一下,
						var main = this.getCenterTabPanel();// Ext.getCmp("centerTabPanel");
						main.setActiveTab(panel);
					}
				}
				
			}
		});
