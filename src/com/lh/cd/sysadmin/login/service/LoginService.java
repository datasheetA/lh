package com.lh.cd.sysadmin.login.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.l_h.cd.pub.http.AdminLoginUtil;
import com.l_h.cd.pub.http.HttpUtil;
import com.l_h.cd.pub.http.Parameter;
import com.l_h.cd.pub.util.MD5;
import com.lh.cd.entity.mapper.TabUserAccountMapper;
import com.lh.cd.entity.model.TabUserAccount;
import com.lh.cd.entity.model.TabUserAccountExample;
import com.lh.cd.pub.Constants;
import com.lh.cd.pub.exception.AppException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class LoginService {

	private static Logger logger = Logger.getLogger(LoginService.class);

	@Autowired
	private TabUserAccountMapper tabUserAccountMapper;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void doLogin(HttpServletRequest request, HttpServletResponse response) throws AppException {
		try {
			Parameter para = new Parameter(request);
			String userAccount = para.getString("userAccount", "");
			String passwd = para.getString("passwd", "");
			String validcode = para.getString("validcode", "");//我们输入的验证码

			logger.debug("userAccount=" + userAccount + ",passwd=" + passwd + ",validcode=" + validcode);
			HttpSession currentSession = request.getSession(true);

			// 检查验证码
			String randomValidate = (String) currentSession.getAttribute(Constants.SESSION_RANDOM_VALIDATE);//session中的验证码
			if (randomValidate == null) {
				logger.info("validcode=" + validcode);
				logger.info("randomValidate=" + randomValidate);
				throw new AppException("登录失败，验证码已经过期！");
			}

			 if (!randomValidate.equals(validcode)) {
				 logger.info("validcode=" + validcode);
				 logger.info("randomValidate=" + randomValidate);
				 throw new AppException("登录失败，验证码不正确！");
			 }

			TabUserAccount record = this.getTabUserAccount(userAccount);
			if (record == null) {
				logger.info("userAccount=" + userAccount);
				throw new AppException("登录失败，用户账号不存在！");
			}
			else if (!record.getPasswd().equals(MD5.md5crypt(MD5.md5crypt(passwd) + record.getSalt()))) {
				logger.info("passwd=" + passwd);
				logger.info("record.getPassword=" + record.getPasswd());
				throw new AppException("登录失败，用户密码不正确！");
			}
			else if (record.getStatus() != 1) {
				logger.info("record.getStatus=" + record.getStatus());
				throw new AppException("用户帐号被禁止，不允许登录！");
			}

			currentSession.setAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ID, "" + record.getUserId());
			logger.debug("record.getUserId()=" + record.getUserId());
			logger.debug("currentSession admin_id=" + currentSession.getAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ID));
			currentSession.setAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ACCOUNT, record.getUserAccount());
			
			
			if (record.getSessionInterval() == -1) {
				currentSession.setMaxInactiveInterval(-1);
			}  else {
				currentSession.setMaxInactiveInterval(record.getSessionInterval() * 60);
			}

			record.setCurSessionId(currentSession.getId());
			record.setLastLoginIp(HttpUtil.getClientIp(request));
			record.setLastLoginTime(new Date());
			record.setLoginCount(record.getLoginCount() + 1);
			record.setUpdTime(new Date());

			this.updateTabUserAccount(record);
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("用户登录出错！");
		}
	}
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @throws AppException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void doLogout(HttpServletRequest request, HttpServletResponse response) throws AppException {
		try {
			String userAccount = AdminLoginUtil.getLoginUserAccount(request);
			TabUserAccount record = this.getTabUserAccount(userAccount);
			if (record != null) {
				record.setCurSessionId("");
				record.setUpdTime(new Date());
				this.updateTabUserAccount(record);
			}

			HttpSession currentSession = request.getSession(true);
			if (currentSession != null) {
				currentSession.setAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ID, null);
				currentSession.setAttribute(Constants.SESSION_LOGIN_ADMIN_USER_ACCOUNT, null);
				currentSession.invalidate();
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("退出登录出错！");
		}
	}
	
	/**
	 * 查询这个账号是否存在
	 * @param userAccount
	 * @return
	 */
	private TabUserAccount getTabUserAccount(String userAccount) {
		TabUserAccountExample example = new TabUserAccountExample();
		example.createCriteria().andUserAccountEqualTo(userAccount);
		List<TabUserAccount> list = tabUserAccountMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return null;
		}
		else {
			return (TabUserAccount) list.get(0);
		}
	}
	
	/**
	 * 更新用户账号
	 * @param record	
	 */
	private void updateTabUserAccount(TabUserAccount record) {
		tabUserAccountMapper.updateByPrimaryKeySelective(record);
	}

	
}
