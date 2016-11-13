package com.l_h.cd.pub.interseptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.lh.cd.sysadmin.login.service.LoginService;

public class AuthorizeUrlInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(AuthorizeUrlInterceptor.class);
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private String[] onlyCheckLoginAccessUrls;
	private String[] noCheckAccessUrls;// 不需要保护的url资源
	private PathMatcher pathMatcher = new AntPathMatcher();

	private String loginUrl;
	
	@Autowired
	private LoginService service;

	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setOnlyCheckLoginAccessUrls(String[] onlyCheckLoginAccessUrls) {
		this.onlyCheckLoginAccessUrls = onlyCheckLoginAccessUrls;
	}

	public void setNoCheckAccessUrls(String[] noCheckAccessUrls) {
		this.noCheckAccessUrls = noCheckAccessUrls;
	}

	public boolean preHandle1(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	logger.debug("拦截器:**********Action之前执行**********");
    	
		String url = urlPathHelper.getLookupPathForRequest(request);
		logger.debug("url="+url);
		if (url.startsWith("/sysadmin/user")) {

			logger.debug("response.sendRedirect="+request.getContextPath());
			response.sendRedirect(request.getContextPath());
			return false;
		}
		return true;
	}

	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	logger.debug("拦截器:**********Action之前执行**********");
    	final String TIME_OUT = "{\"failure\":true,\"exception.message\":\"获取用户登录信息出错！\"}";
		String url = urlPathHelper.getLookupPathForRequest(request);
		//logger.debug("######url="+url);

		String requestedWith = request.getHeader("x-requested-with");
		System.out.println("####################requestedWith="+requestedWith);

		logger.debug("######url="+url+",isNeedCheck="+isNeedCheck(url)+",isOnlyCheckLogin="+isOnlyCheckLogin(url));
		
		//boolean isNeedAuthCheck = isNeedCheck(url);
		if (!isNeedCheck(url)) {
			return true;// 所请求的资源不需要保护.
		}
		else if(isOnlyCheckLogin(url)){
			//boolean isLogin = service.checkLogin(request);
			boolean isLogin = true;
			if (!isLogin) {
				if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {
					response.setHeader("session-status", "timeout");
					response.setCharacterEncoding("utf-8");
					response.setStatus(500);
					response.getWriter().print(TIME_OUT);
				} 
				else{
					logger.debug("response.sendRedirect1="+request.getContextPath() + loginUrl);
					response.sendRedirect(request.getContextPath() + "/js/test.html");
				}
				return false;
			}
			return true;
		}
		else if (loginUrl != null && this.pathMatcher.match(url, loginUrl)) {
			return true;
		}
		
		//boolean checkValue = service.checkAuth(request, response, url, this.pathMatcher);
		boolean checkValue = true;
		if(!checkValue){
		logger.debug("######url="+url+",checkValue="+checkValue);
		}
		if (!checkValue && loginUrl != null) {
			if ((requestedWith != null && "XMLHttpRequest".equals(requestedWith))){
				response.setHeader("session-status", "timeout");
				response.setCharacterEncoding("utf-8");
				response.setStatus(500);
				response.getWriter().print(TIME_OUT);
			} 
			else{
			logger.debug("response.sendRedirect2="+request.getContextPath() + loginUrl);
			response.sendRedirect(request.getContextPath() + loginUrl);
			}
			return false;
		}
		return checkValue;
	}

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
		
		try {
			logger.debug("response.sendRedirect1="+request.getContextPath() + loginUrl);
			response.sendRedirect(request.getContextPath() + "/js/test.html");
			return;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public boolean isNeedCheck(String urlPath) {
		if (noCheckAccessUrls != null) {
			for (int i = 0; i < this.noCheckAccessUrls.length; i++) {
				String registeredPath = noCheckAccessUrls[i];
				if (registeredPath == null) {
					throw new IllegalArgumentException("Entry number " + i + " in noCheckAccessUrls array is null.");
				}
				else {
					if (this.pathMatcher.match(registeredPath, urlPath)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	public boolean isOnlyCheckLogin(String urlPath) {
		if (this.onlyCheckLoginAccessUrls != null) {
			for (int i = 0; i < this.onlyCheckLoginAccessUrls.length; i++) {
				String registeredPath = onlyCheckLoginAccessUrls[i];
				if (registeredPath == null) {
					throw new IllegalArgumentException("Entry number " + i + " in checkAccessUrls array is null.");
				}
				else {
					if (this.pathMatcher.match(registeredPath, urlPath)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
        //配置了<bean id="exceptionResolver" class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        //后(全局的异常处理)，ex一直都为空，也就是说这里记错误日志不行
        //只有不使用全局的异常处理时，这里ex才不为空
        //ex不为空时，记错误日志   
    	logger.debug("拦截器:---------释放资源---------");   
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
    	logger.debug("拦截器:Action执行完，生成视图之前执行");   
	}

}