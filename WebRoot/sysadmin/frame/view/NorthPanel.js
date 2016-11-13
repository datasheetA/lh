// 页面顶部panel
Ext.define('App.sysadmin.frame.view.NorthPanel', {
			extend : 'Ext.Component',
			alias : 'widget.northpanel',

			region : 'north',
			height : 60,
			cls : 'header',
			html : '<table width="100%" height="100%"><tr><td><h1>后台管理系统</h1></td></tr><tr><td align="right" style="color:#FFFFFF">当前用户：<label id="frameTopLoginUserAccount"></label>，<a href="../logout.do" class="a1">退出登录</a>！&nbsp;&nbsp;</td></tr></table>',

			initComponent : function() {
				this.callParent();
			}
		});