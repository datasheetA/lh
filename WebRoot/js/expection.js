var pub_excepHandler_alert = function(res) {
	if (res) {
		var hint = res['exception.message'];
		var msgTitle = '错误';
		if (res['success']) {
			msgTitle = '提示';
		}
		if (hint) {
			window.top.Ext.Msg.alert(msgTitle, hint, function() {
						var errorCode = res['errorCode'];
						if (hint == '获取用户登录信息出错！' || (!Ext.isEmpty(errorCode) && errorCode == '1') ) {
							
							window.top.location.href = appPath;
						}
					});
		}
	}
};

var pub_form_excepHandler = function(form, action) {
	if (Ext.isEmpty(action.response.responseText)) {
		switch (action.failureType) {
			case Ext.form.Action.CONNECT_FAILURE :
				window.top.Ext.Msg.alert('错误', '网络异常！');
				break;
			default :
				window.top.Ext.Msg.alert('错误', '系统错误！');
				break;
		}
	}
	else {
		pub_excepHandler_alert(Ext.JSON.decode(action.response.responseText));
	}
}

var pub_excepHandler = function(xhr, response) {
	pub_excepHandler_alert(Ext.JSON.decode(response.responseText));
};

Ext.Ajax.on('requestexception', pub_excepHandler);