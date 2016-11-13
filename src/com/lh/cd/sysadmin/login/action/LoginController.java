package com.lh.cd.sysadmin.login.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;

import com.l_h.cd.pub.http.AppController;
import com.l_h.cd.pub.http.Parameter;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.login.service.LoginService;

@Controller
public class LoginController extends AppController {
	private static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	private LoginService service;

	@RequestMapping(value = "/login.do")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("login");
		try{			
			Parameter para = new Parameter(request);
			String operFlag = para.getString("operFlag", "");
			logger.debug("operFlag="+operFlag);
			
			if(operFlag.equals("login")){
				System.out.println("operFlag========="+operFlag);
				service.doLogin(request, response);
				response.sendRedirect(request.getContextPath()+"/sysadmin");
				return null;
			}
		}
		catch (AppException e) {
			e.printStackTrace();
			model.addObject("message", e.getMessage());
		}
		catch (Exception e) {
			model.addObject("message", "用户登录出错！");
		}
		return model;
	}
	
	
	@RequestMapping(value = "/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("logout");
		try{
			service.doLogout(request, response);
			response.sendRedirect(request.getContextPath());
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

}
