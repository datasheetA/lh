package com.l_h.cd.pub.http;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取页面参数工具类</br>
 * @version 0.1 2007-9-4 上午11:24:27
 */
public class ParameterUtils {

	/**
	 * 构造函数(无功能)
	 */
	public ParameterUtils() {
	}

	/**
	 * 设置页面编码为GBK
	 * @param request 页面请求
	 * @throws UnsupportedEncodingException
	 */
	public static void setCharacterEncoding(HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
	}

	/**
	 * 按照字节类型获取页面数据
	 * @param request 页面请求
	 * @param paramName 参数名
	 * @return 页面数据 类型 byte
	 */
	public static byte getByte(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0;
		else
			return Byte.parseByte(value);
	}

	/**
	 * 按照长整型类型获取页面数据，非数组
	 * @param request 页面请求
	 * @param paramName 参数名
	 * @return 页面数据 类型 long
	 */
	public static long getLong(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0L;
		else
			return Long.parseLong(value);
	}

	   /**
     * 按照短整型类型获取页面数据，非数组
     * @param request 页面请求
     * @param paramName 参数名
     * @return 页面数据 类型 short
     */
    public static int getInt(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.length() == 0)
            return 0;
        else
            return Integer.parseInt(value);
    }
	
	/**
	 * 按照短整型类型获取页面数据，非数组
	 * @param request 页面请求
	 * @param paramName 参数名
	 * @return 页面数据 类型 short
	 */
	public static short getShort(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0;
		else
			return Short.parseShort(value);
	}

	/**
	 * 按布尔型获取页面参数，非数组<br>
	 * 例: 页面参数为字符串"true"，返回 true； 否则返回false
	 * @param request 页面请求
	 * @param paramName 参数名
	 * @return 页面参数 类型 boolean
	 */
	public static boolean getBoolean(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return false;
		else
			return Boolean.valueOf(value).booleanValue();
	}

}