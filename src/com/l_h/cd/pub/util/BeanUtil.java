package com.l_h.cd.pub.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Bean相关操作
 * @author sunny
 * @version 1.0 Apr 10, 2008
 */
public class BeanUtil {
    private static Logger  logger = Logger.getLogger(BeanUtil.class);

	private final static Map wrapClassMap = new HashMap();
    static {
        wrapClassMap.put(byte.class, Byte.class);
        wrapClassMap.put(short.class, Short.class);
        wrapClassMap.put(int.class, Integer.class);
        wrapClassMap.put(long.class, Long.class);
        wrapClassMap.put(float.class, Float.class);
        wrapClassMap.put(double.class, Double.class);
        wrapClassMap.put(char.class, Character.class);
        wrapClassMap.put(boolean.class, Boolean.class);
    }

    public final Object                  bean;

    /**
     * 构造一个对bean的操作工具
     * @param bean 要操作bean
     */
    public BeanUtil(Object bean) {
        this.bean = bean;
    }

    /**
     * 构造一个对bean的操作工具
     * @param className 要操作bean的名称
     * @throws ClassNotFoundException
     */
    public BeanUtil(String className) throws Exception {
        this(Class.forName(className).newInstance());
    }

    /**
     * 调用一个bean的get方法取得一个值 相当于 运行：bean.get[name]()
     * @param name
     * @return bean.get[name]() 的返回值
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object get(String name)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return bean.getClass().getMethod("get" + StringUtil.headUpper(name), new Class[]{}).invoke(bean, new Object[]{});
    }

    /**
     * 调用一个bean的get方法取得一个String类型的值 相当于 运行：bean.get[name]().toString()
     * @param name
     * @return name对应的String型值，如果对应值为null，则返回null
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public String getToString(String name)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return StringUtil.toString(get(name));
    }

    /**
     * 调用一个bean的get方法取得一个String类型的值 相当于 运行：bean.get[name]().toString()
     * @param name
     * @param defaultValue 为空时返回的默认值
     * @return name对应的String型值，如果对应值为null，则返回null
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public String getToString(String name, String defaultValue)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String result = getToString(name);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 调用一个bean的set方法设置一个值 相当于 运行：bean.set[name](value)
     * @param name 设置的key
     * @param value 设置的值
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public void set(String name, String value)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Method method = bean.getClass().getMethod("set" + StringUtil.headUpper(name), new Class[]{String.class});
        method.invoke(bean, new Object[]{value});
    }

    /**
     * 如果是基本类型则转换成包裹类，其他类型不变
     * @return 包裹类
     */
    private static Class convertToWraps(Class className) {
        Class wraps = (Class) wrapClassMap.get(className);
        if (wraps == null) {
            wraps = className;
        }
        return wraps;
    }

    /**
     * 对bean做拷贝，将src的数据全部拷贝到dest中
     * @param src 源数据bean
     * @param dest 宿数据bean
     * @return copy的数据个数
     * @throws Exception
     */
    public static int copy(Object src, Object dest)
            throws Exception {
        return copy(src, dest, true);
    }

    /**
     * 对bean做拷贝，将src的数据全部拷贝到dest中
     * @param src 源数据bean
     * @param dest 宿数据bean
     * @param isOverwrite 是否覆盖，如果为false则保留dest中存在的数据，只set为null的数据(注：基本类型无法判断会全部覆写)
     * @return copy的数据个数
     * @throws Exception
     */
    public static int copy(Object src, Object dest, boolean isOverwrite)
            throws Exception {
        Method[] methodsSrc = src.getClass().getMethods();
        Method[] methodsDest = dest.getClass().getMethods();
        Map destSetMethod = new HashMap();
        Map destGetMethod = new HashMap();
        
        if (methodsDest != null && methodsDest.length > 0){
            for (int i=0; i<methodsDest.length; i++){
                Method method = methodsDest[i];
                String name = method.getName();
                if (name.startsWith("set") && method.getParameterTypes().length == 1) {
                    String key = getMethodKey(name, method.getParameterTypes()[0]);
                    if (!destSetMethod.containsKey(key)) {
                        destSetMethod.put(key, method);
                    }
                    else {
                        destSetMethod.remove(key);
                    }
                }
                else if (!isOverwrite && name.startsWith("get") && method.getParameterTypes().length == 0) {
                    destGetMethod.put(getMethodKey(name, method.getReturnType()), method);
                }
            }
        }

        int copyCount = 0;
        if (methodsSrc != null && methodsSrc.length > 0){
            for (int i=0; i<methodsSrc.length; i++){
                Method method = methodsSrc[i];
                String name = method.getName();
                if (name.startsWith("get") && !name.equals("getClass") && method.getParameterTypes().length == 0) {
                    String key = getMethodKey(name, method.getReturnType());
                    if (!destGetMethod.containsKey(key) || ((Method)destGetMethod.get(key)).invoke(dest, new Object[]{}) == null) {
                        Method methodDest = (Method) destSetMethod.get(key);
                        if (methodDest == null) {
                            throw new IllegalArgumentException("输入bean错误！\n" + "无法找到get函数：" + method.toString() + "对应的set函数。\n" + "可能由于该set函数不存在，或者存在两个对应的set函数，参数分别为基本类型和封包类型导致程序无法确认。");
                        }
                        methodDest.invoke(dest, new Object[]{method.invoke(src, new Object[]{})});
                        copyCount++;
                    }
                }
            }
        }

        return copyCount;
    }

    
    /**
     * 返回方法的key
     * @param methodName 方法名
     * @param className 类名
     */
    private static String getMethodKey(String methodName, Class className) {
        return methodName.substring(3) + ":" + convertToWraps(className);
    }

    /**
     * 覆盖方法 得到bean中所有get方法名以及其对应的值
     * @return bean中所有get方法名以及其对应的值
     */
    public static Map<String,Object> toMap(Object obj) {
    	Map<String,Object> map = new HashMap<String,Object>();
        Method[] methods = obj.getClass().getMethods();
        if (methods != null && methods.length > 0){
            for (int i=0; i<methods.length; i++){
	            try {
	            	Method method = methods[i];
	                String name = method.getName();
	            	 if (name.startsWith("get") && !name.equals("getClass") && method.getParameterTypes().length == 0) {
	            		 map.put(StringUtil.headLower(name.substring(3)),  method.invoke(obj, new Object[]{}));
	            	 }
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
            }
        }
        return map;
    }
    
    
    /**
     * 覆盖方法 得到bean中所有get方法名以及其对应的值
     * @return bean中所有get方法名以及其对应的值
     */
    public String toString() {
        StringBuffer result = new StringBuffer(bean.toString() + "\n");
        Method[] methods = bean.getClass().getMethods();
        if (methods != null && methods.length > 0){
            for (int i=0; i<methods.length; i++){
                Method method = methods[i];
                if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                    try {
                        result.append(method.getName().substring(3) + ":\t" + method.invoke(bean, new Object[]{}) + "\n");
                    }
                    catch (Exception e) {
                        result.append(method.getName() + "方法调用失败！\n");
                    }
                }
            }
        }

        return result.toString();
    }

    /**
     * 判断一个get方法是否存在
     * @param name
     * @return
     */
    public boolean inNull(String name) {
        Method[] methods = bean.getClass().getMethods();
        if (methods != null && methods.length > 0){
            for (int i=0; i<methods.length; i++){
                Method method = methods[i];
                if (method.getName().equals("get" + StringUtil.headUpper(name)) && method.getParameterTypes().length == 0) {
                    return false;
                }
            } 
        }
        return true;
    }

    
    
    /**
     * 打印bean中的内容
     * @param src 源数据bean
     * @throws Exception
     */
    public static void print(Object src){
        try {
			Method[] methodsSrc = src.getClass().getMethods();
			if (methodsSrc != null && methodsSrc.length > 0){
			    for (int i=0; i<methodsSrc.length; i++){
			        Method method = methodsSrc[i];
			        String name = method.getName();
			        if (name.startsWith("get") && !name.equals("getClass") && method.getParameterTypes().length == 0) {
			        	logger.info(name.substring(3)+":"+method.invoke(src, new Object[]{}));
			        }
			    }
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

    
    public static void main(String[] args)
            throws Exception {
     
    }
}
