package com.l_h.cd.pub.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lh.cd.pub.Constants;
import com.lh.cd.pub.exception.AppException;

/**
 * 从页面请求中获取参数及请求路径
 * 全部为静态方法
 */
public class HttpUtil {

    /**
     * 获取页面参数到HashMap中
     * @param request 页面请求
     * @return 页面数据，存放在HashMap中，区分大小写
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String key = (String) em.nextElement();
            map.put(key, request.getParameter(key));
        }
        return map;
    }

    /**
     * 获取请求完整URL
     * 如：http://www.forlink.com.cn/index.jsp
     * @param request 页面请求
     * @return url字符串 "http://www.forlink.com.cn/index.jsp"
     */
    public static String getContextPathWithProtocol(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + path + "/";
        return basePath;
    }

    /**
     * 获取请求路径，不包含协议
     * 如 www.forlink.com.cn/index.jsp
     * @param request 页面请求
     * @return ContextPath字符串 "www.forlink.com.cn/index.jsp"
     */
    public static String getContextPath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + path;
        return basePath;
    }

    /**
     * 获取服务器名称和端口
     * 如： www.forlink.com.cn:8080
     * @param request 页面请求
     * @return 服务器名称：www.forlink.com.cn:8080
     */
    public static String getNoContextPath(HttpServletRequest request) {
        String basePath = request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
        return basePath;
    }
    
    
    /**
     * 获取客户端Ip
     * 如： 172.16.1.11
     * @param request 页面请求
     * @return Ip：172.16.1.11
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip == null){
        	return "";
        }
        else{
        	return request.getRemoteAddr();
        }
    }
    
    /**
     * 检查验证码是否正确
     * @param request 页面请求
     * @param validcode 验证码
     * @return void
     */
	public static void checkValidate(HttpServletRequest request, String validcode) throws AppException {
		try {            
			HttpSession currentSession = request.getSession(true);
			if (currentSession == null) {
				throw new AppException("不能获取用户会话！");
			}
	
			String randomValidate = (String) currentSession.getAttribute(Constants.SESSION_RANDOM_VALIDATE);
			if (randomValidate == null){
				throw new AppException("验证码已过期！");
			}
			
			if (!randomValidate.equals(validcode)){
				throw new AppException("验证码不正确！");
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("检查验证码出错！");
		}
	}
}
