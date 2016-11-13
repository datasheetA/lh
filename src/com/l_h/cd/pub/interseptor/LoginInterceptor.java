package com.l_h.cd.pub.interseptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 验证用户登陆拦截器
 * 
 * @author chenxin
 * @date 2011-3-13 下午09:02:00
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	/** 超时提醒 */
	public static final String TIME_OUT = "{\"error\":true,\"msg\":\"获取用户登录信息出错！\"}";
	/** 保存session中的admin用户key */
	public static final String CURRENT_USER = "CURRENT_USER";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
System.out.println("################################"+request.getRequestURI());
		// 如果session中没有user对象
		if (null == request.getSession().getAttribute(CURRENT_USER)) {
			String requestedWith = request.getHeader("x-requested-with");
			System.out.println("requestedWith="+requestedWith);
			// ajax请求
			if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {
				response.setHeader("session-status", "timeout");
				response.getWriter().print(TIME_OUT);
			} else {
				// 普通页面请求
				response.sendRedirect(request.getContextPath() + "/");
			}
			return false;
		}
		return true;

	}

}
