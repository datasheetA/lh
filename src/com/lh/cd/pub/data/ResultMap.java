package com.lh.cd.pub.data;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.l_h.cd.pub.util.DateUtil;


/**
 * key值HashMap
 * 
 * @version 0.1
 */
public class ResultMap implements Map<Object, Object> {
	private Map<Object, Object> map;

	public ResultMap() {
		map = new HashMap<Object, Object>();
	}

	public ResultMap(Map<Object, Object> map) {
		this.map = map;
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * 如果key是String 则返回小写的key
	 * 
	 * @param key
	 * @return 如果key是String 则返回小写的key
	 */
	private Object toLowerCase(Object key) {
		if (key != null && key instanceof String) {
			key = ((String) key).toLowerCase();
		}
		return key;
	}

	
	public boolean containsKey(Object key) {
		return map.containsKey(toLowerCase(key));
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(toLowerCase(key));
	}
	
	public Object put(Object key, Object value) {
		return map.put(toLowerCase(key), value);
	}

	public Object remove(Object key) {
		return map.remove(toLowerCase(key));
	}

	/**
	 * 放置一个map中的所有参数，（注：直接将map中的参数逐个放入，效率略差于HashMap） 覆盖方法
	 * 
	 * @param m
	 */
	@SuppressWarnings("unchecked")
	public void putAll(Map<?, ?> m) {
		for (Iterator<?> i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			put(e.getKey(), e.getValue());
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<Object> keySet() {
		return map.keySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	@SuppressWarnings("unchecked")
	public Set entrySet() {
		return map.entrySet();
	}

	public String toString() {
		return map.toString();
	}
	
	
	/**
	 * 如果得到值为空，则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue 值为空时的代替返回值
	 * @return
	 */
	public Object get(Object key, Object defaultValue) {
		Object result = get(key);
		if (result == null || result.equals("")) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 得到String类型的返回值
	 * 
	 * @param key
	 * @return
	 */
	public String getString(Object key) {
		Object result = get(key);
		return (result == null)?null:result.toString();
	}

	/**
	 * 得到String类型的返回值， 如果为空则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(Object key, String defaultValue) {
		Object result = get(key, defaultValue);
		return (result == null)?null:result.toString();
	}

	/**
	 * 将日期类型返回成一个表示日期的字符串 YYYY-MM-DD格式
	 * 
	 * @param key
	 * @return
	 */
	public String getDateString(Object key) {
		Date date = (Date) get(key);
		return DateUtil.format(date, "yyyy-MM-dd");
	}

	/**
	 * 将日期类型返回成一个表示日期的字符串 YYYY-MM-DD格式 如果为空 则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getDateString(Object key, String defaultValue) {
		String result = defaultValue;
		Date date = (Date) get(key);
		if (date != null) {
			result = DateUtil.format(date, "yyyy-MM-dd");
		}
		return result;
	}

	/**
	 * 得到int型的返回值
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(Object key) {
		return (int) Double.parseDouble(getString(key));
	}

	/**
	 * 得到int型的返回值， 如果为空则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(Object key, int defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		if (result.length() < 1) {
			result = "0";
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * 得到long型的返回值 将返回值强转为long型
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(Object key) {
		return (long) Double.parseDouble(getString(key));
	}

	/**
	 * 得到long型的返回值， 如果为空则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getLong(Object key, long defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		if (result.length() < 1) {
			result = "0";
		}

		return (long) Double.parseDouble(result);
	}

	/**
	 * 得到double型的返回值
	 * 
	 * @param key
	 * @return
	 */
	public double getDouble(Object key) {
		return Double.parseDouble(getString(key));
	}

	/**
	 * 得到double型的返回值, 如果为空则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(Object key, double defaultValue) {
		return Double.parseDouble(getString(key, String.valueOf(defaultValue)));
	}
	
	/**
	 * 得到boolean型的返回值 将返回值强转为boolean型
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(Object key) {
		return Boolean.parseBoolean(getString(key).toLowerCase());
	}

	/**
	 * 得到boolean型的返回值， 如果为空则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(Object key, boolean defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		if (result.length() < 1) {
			result = "false";
		}

		return Boolean.parseBoolean(getString(key).toLowerCase());
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
	}

}