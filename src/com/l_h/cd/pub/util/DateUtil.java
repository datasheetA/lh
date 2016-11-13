package com.l_h.cd.pub.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期相关操作
 * @author sunny
 * @version 1.0 Apr 10, 2008
 */
public class DateUtil {
	/** 存储所有日期格式类 */
	private static final Map<Object, Object> mapFormat = new HashMap<Object, Object>();

	/**
	 * 得到中文的日期格式字符串 具体格式：yyyy年MM月dd日 HH点mm分ss秒.
	 * @param date
	 * @return 中文的日期字符串
	 */
	public static String formatCNTime(Date date) {
		return format(date, "yyyy年MM月dd日 HH点mm分ss秒");
	}

	/**
	 * 得到紧凑的日期格式字符串 具体格式：yyyy-MM-dd.
	 * @param date
	 * @return 中文的日期字符串
	 */
	public static String formatDate(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 得到默认日期格式字符串 具体格式：yyyy-MM-dd HH:mm:ss.
	 * @param date
	 * @return 默认格式字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回特定格式的日期表达形式.
	 * @param date
	 * @param pattern 日期格式字符串
	 * @return 表示日期的字符串
	 */
	public static String format(Date date, String pattern) {
		return getDateFormat(pattern).format(date);
	}

	/**
	 * 解析日期字符串
	 * @param strDate 要解析的字符串
	 * @param pattern 解析格式
	 * @return 解析出的日期
	 * @throws ParseException
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		if (strDate == null || strDate.equals("") || pattern ==  null || pattern.equals("")){
			return null;
		}		
		return getDateFormat(pattern).parse(strDate);
	}

	/**
	 * 解析日期字符串
	 * @param strDate 要解析的字符串
	 * @param pattern 解析格式
	 * @return 解析出的日期
	 * @throws ParseException
	 */
	public static Date parse(String strDate) throws ParseException {
		String pattern = "yyyy-MM-dd";	
		if (strDate.indexOf("-") > 0 && strDate.indexOf(":") > 0){
			pattern = "yyyy-MM-dd HH:mm:ss";	
		}
		else if (strDate.indexOf("-") < 0 && strDate.indexOf(" ") > 0 && strDate.indexOf(":") > 0){
			pattern = "yyyyMMdd HH:mm:ss";	
		}
		else if (strDate.indexOf("-") > 0 && strDate.indexOf(":") < 0){
			pattern = "yyyy-MM-dd";	
		}
		else if (strDate.indexOf("-") < 0 && strDate.indexOf(":") < 0){
			pattern = "yyyyMMdd";	
		}
		else if (strDate.indexOf("-") < 0 && strDate.indexOf(":") > 0){
			pattern = "HH:mm:ss";
		}

		return parse(strDate, pattern);
	}
	
	/**
	 * 得到指定的日期格式化类
	 * @param key 指定格式
	 * @return 所指定的日期格式化类
	 */
	private static DateFormat getDateFormat(String key) {
		DateFormat format = (DateFormat) mapFormat.get(key);
		if (format == null) {
			format = new SimpleDateFormat(key);
		}
		return format;
	}

	/**
	 * 获取当前日期.
	 * @param pattern 日期格式字符串
	 * @return 当前日期的字符串
	 */
	public static String getCurDate(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
	}
	
	/**
	 * 获取当前日期.
	 * @return 当前日期的字符串
	 */
	public static String getCurDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
	}
	
	/**
	 * 判断是否是合法的日期
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str) {
		return isDate(str, "-");
	}
	
	/**
	 * 判断是否是合法的日期
	 * @param str
	 * @param splt
	 * @return
	 */
	public static boolean isDate(String str, String splt) {
		int first = str.indexOf(splt);
		int second = str.lastIndexOf(splt);
		if (first == second) {
			System.out.println("您输入的日期缺少\""+splt+"\"符号");
			return false;
		}
		else {
			String year = str.substring(0, first);
			String month = str.substring(first + 1, second);
			String day = str.substring(second + 1, str.length());
			int maxDays = 31;
			System.out.println(year + splt + month + splt + day);
			if (StringUtil.isNumber(year) == false || StringUtil.isNumber(month) == false
					|| StringUtil.isNumber(day) == false) {
				System.out.println("您输入的日期包含不可用字符");
				return false;
			}
			else if (year.length() < 4) {
				System.out.println("您输入的年份少于四位");
				return false;
			}

			int y = Integer.parseInt(year);
			int m = Integer.parseInt(month);
			int d = Integer.parseInt(day);
			if (m > 12 || m < 1) {
				System.out.println("您输入的月份不在规定范围内");
				return false;

			}
			else if (m == 4 || m == 6 || m == 9 || m == 11) {
				maxDays = 30;
			}
			else if (m == 2) {
				if (y % 4 > 0)
					maxDays = 28;
				else if (y % 100 == 0 && y % 400 > 0)
					maxDays = 28;
				else
					maxDays = 29;
			}
			if (d < 1 || d > maxDays) {
				System.out.println("您输入的日期不在规定范围内");
				return false;
			}
		}
		return true;
	}

	
	public static void main(String[] args) throws ParseException {
		if (false) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -30);
			System.out.println(format(calendar.getTime()));
			calendar.setTime(parse("20051031", "yyyyMMdd"));
			calendar.add(Calendar.MONTH, -1);
			System.out.println(format(calendar.getTime()));
			System.out.println(calendar.get(Calendar.YEAR));
			System.out.println(calendar.get(Calendar.MONDAY));
			System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
		}

		if (false) {
			Date date = getDateFormat("yyyy-MM-dd HH:mm:ss").parse("2006-01-01 00:00:00");
			System.out.println(date.getTime());
		}
	}
}