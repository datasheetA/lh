package com.l_h.cd.pub.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanggang
 * @version Jul 14, 2009
 */
public class AuthHandle {

	public boolean checkAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//Object userDTO = (Object) request.getSession().getAttribute("userDTO");

		if (true) {
			System.out.println("AuthHandle===================================");
			return true;
		}
		else {
			System.out.println("userDTO is null");
		}
		return false;
	}

}