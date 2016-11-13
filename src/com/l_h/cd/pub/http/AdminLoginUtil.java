package com.l_h.cd.pub.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.lh.cd.pub.Constants;
import com.lh.cd.pub.exception.AppException;

/**
 * 获取后台登录用户信息
 * 
 * @author Administrator
 */
public class AdminLoginUtil {
	private static Logger logger = Logger.getLogger(AdminLoginUtil.class);
	private static String LoginFailureErrorMessage = "获取用户登录信息出错！";
	/**
	 * 获取登录用户id
	 * 
	 * @param request
	 * @return
	 * @throws AppException
	 */
	public static int getLoginUserId(HttpServletRequest request) throws AppException {
		int ret = 0;
		try {
			HttpSession currentSession = request.getSession(false);
			if (currentSession == null) {
				throw new Exception();
			}

			String userId = (String) currentSession.getAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ID);
			if (userId == null || userId.equals("")) {
				throw new AppException(LoginFailureErrorMessage);
			}

			logger.debug("currnet login userId=" + userId);
			ret = Integer.parseInt(userId);
		}
		catch (AppException e) {
			throw new AppException(""+Constants.ERRORCODE_NEED_LOGIN, e.getMessage());
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new AppException(""+Constants.ERRORCODE_NEED_LOGIN, LoginFailureErrorMessage);
		}

		return ret;
	}

	/**
	 * 获取登录用户名
	 * 
	 * @param request
	 * @return
	 * @throws AppException
	 */
	public static String getLoginUserAccount(HttpServletRequest request) throws AppException {
		String userAccount = null;
		try {
			HttpSession currentSession = request.getSession(false);
			if (currentSession == null) {
				throw new Exception();
			}

			userAccount = (String) currentSession.getAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ACCOUNT);
			if (userAccount == null || userAccount.equals("")) {
				throw new AppException(""+Constants.ERRORCODE_NEED_LOGIN, LoginFailureErrorMessage);
			}

			logger.debug("currnet login userAccount=" + userAccount);
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new AppException(""+Constants.ERRORCODE_NEED_LOGIN, LoginFailureErrorMessage);
		}
		return userAccount;
	}
}
