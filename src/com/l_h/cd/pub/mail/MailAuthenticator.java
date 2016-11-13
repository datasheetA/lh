package com.l_h.cd.pub.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
	// ******************************
	// 由于发送邮件的地方比较多,
	// 下面统一定义用户名,口令.
	// ******************************
	private String strUser;
	private String strPwd;

	public MailAuthenticator(String user, String password) {
		this.strUser = user;
		this.strPwd = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(strUser, strPwd);
	}
}
