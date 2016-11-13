package com.l_h.cd.pub.http;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.lh.cd.pub.data.ResultMap;

/**
 * 获取页面参数公共类
 * @author sunny
 * @version 1.0 2006-8-12
 */
public class Parameter {
    /** 回写类型：不回写 */
    public static final int          NONE      = 0;

    /** 回写类型：回写到 ELEMENT */
    public static final int          ELEMENT   = 1;

    private final HttpServletRequest request;

    private int                      writeType = NONE;

    private Element                  element   = null;

    /**
     * 构造函数
     * @param request
     */
    public Parameter(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 得到 HttpServletRequest
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 构造函数 将取得的参数回写到 element 中。
     * @param request 页面传入信息
     * @param root 取得的参数将要返写会的element（不设置该参数则不返写）
     */
    public Parameter(HttpServletRequest request, Element root) {
        this(request);
        setResult(root);
    }

    /**
     * 设置将读取的参数回写到 element
     * @param root 要写入的element
     */
    public void setResult(Element root) {
        writeType = ELEMENT;
        this.element = root;
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @return
     */
    public String getString(String key) {
        String result = null;
        String[] array = request.getParameterValues(key);
        if (array != null) {
            if (array.length > 1) {
                throw new IllegalArgumentException(key + "的属性为数组，建议不单独取值");
            }
            result = array[0];
        }

        write(key, result);

        return result;
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @param defaultValue 如果该参数为空则返回 defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String result = getString(key);
        if (result == null || result.length() == 0) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @return
     */
    public short getShort(String key) {
        return Short.parseShort(getString(key));
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @param defaultValue 如果该参数为空则返回 defaultValue
     * @return
     */
    public short getShort(String key, short defaultValue) {
    	short result;
        try {
            result = getShort(key);
        }
        catch (NumberFormatException e) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @return
     */
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @param defaultValue 如果该参数为空则返回 defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        int result;
        try {
            result = getInt(key);
        }
        catch (NumberFormatException e) {
            result = defaultValue;
        }

        return result;
    }
    
    /**
     * 取出指定参数
     * @param key 参数名
     * @return
     */
    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @param defaultValue 如果该参数为空则返回 defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        long result;
        try {
            result = getLong(key);
        }
        catch (NumberFormatException e) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @return
     */
    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    /**
     * 取出指定参数
     * @param key 参数名
     * @param defaultValue 如果该参数为空则返回 defaultValue
     * @return
     */
    public double getDouble(String key, double defaultValue) {
        double result;
        try {
            result = getDouble(key);
        }
        catch (Exception e) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * 得到数组参数，如果不存在，则返回0长度的string[]
     * @param key
     * @return
     */
    public String[] getStringValues(String key) {
        String[] str = request.getParameterValues(key);
        if (str == null) {
            str = new String[0];
        }
        return str;
    }

    /**
     * 得到整型的数组参数，如果不存在，则返回0长度的int[]
     * @param key
     * @return
     */
    public int[] getIntValue(String key) {
        String[] str = getStringValues(key);
        int[] result = new int[str.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(str[i]);
        }
        return result;
    }

    /**
     * 将参数写入指定位置
     * @param key 参数名
     * @param value 参数值
     * @throws DOMException
     */
    private void write(String key, String value)
            throws DOMException {
        if (writeType == ELEMENT) {
            element.setAttribute(key, value);
        }
    }
    
    
    public ResultMap getParams(HttpServletRequest request) {
    	ResultMap map = new ResultMap();
		Enumeration<?> paramNames = request.getParameterNames();
		
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			
			System.out.println("参数："+paramName+"="+paramValues[0]);
			map.put(paramName, paramValues[0]);
		}
		return map;
	}
}
