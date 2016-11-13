// 页面底部panel
Ext.define('App.sysadmin.frame.view.SouthPanel', {
			extend : 'Ext.Toolbar',
			alias : 'widget.southpanel',

			region : "south",
			height : 23,

			initComponent : function() {
				this.items = [ '->',
						"技术支持:<a href='http://qq.375461826.com.cn' target='_blank' style='text-decoration:none;'>http://www.鏉庢亽.com,qq:375461826</a>&nbsp;&nbsp;"];
				this.callParent();

				this.on('afterrender', function(obj, eOpts) {}, this);
			}
		});
