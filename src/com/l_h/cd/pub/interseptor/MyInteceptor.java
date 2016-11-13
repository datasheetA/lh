package com.l_h.cd.pub.interseptor;

import javax.servlet.http.HttpServletRequest;   
import javax.servlet.http.HttpServletResponse;   
  
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;   
import org.springframework.web.servlet.ModelAndView;   
  
/**  
 * 拦截器测试  
 *   
 */  
public class MyInteceptor implements HandlerInterceptor {   
	private static Logger logger = Logger.getLogger(MyInteceptor.class);
	
    /**  
     * 处理之前执行  
     */  
    public boolean preHandle(HttpServletRequest request,   
            HttpServletResponse response, Object handler) throws Exception {   
        //这里进行权限控制工作   
    	logger.debug("拦截器:*********Action之前执行**********");   
    	logger.debug("*************getServletPath="+request.getServletPath());  
    	logger.debug("*************getContextPath="+request.getContextPath());  
    	logger.debug("*************getQueryString="+request.getQueryString());  
    	logger.debug("*************getRealPath="+request.getRealPath("/"));
    	logger.debug("*************getRequestURI="+request.getRequestURI());
    	logger.debug("*************getRequestURL="+request.getRequestURL());
    	logger.debug("*************getPathInfo="+request.getPathInfo());
        // return false;//不执行后续   
        return true;// 执行后续   
    }   
  
    /**  
     * 控制器执行完，生成视图之前可以执行，  
     */  
    public void postHandle(HttpServletRequest request,   
            HttpServletResponse response, Object handler,   
            ModelAndView modelAndView) throws Exception {
    	logger.debug("拦截器:Action执行完，生成视图之前执行");   
    }   
  
    /**  
     * 释放资源  
     */  
    public void afterCompletion(HttpServletRequest request,   
            HttpServletResponse response, Object handler, Exception ex)   
            throws Exception {   
        //配置了<bean id="exceptionResolver" class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        //后(全局的异常处理)，ex一直都为空，也就是说这里记错误日志不行
        //只有不使用全局的异常处理时，这里ex才不为空
        //ex不为空时，记错误日志   
    	logger.debug("拦截器:---------释放资源--------");   
    }   
}  
