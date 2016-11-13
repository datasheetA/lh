package com.l_h.cd.pub.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.lh.cd.pub.exception.AppException;


/**
 * 字符串工具
 * 
 * @author sunny
 * @version 1.0 Apr 10, 2008
 */
public class StringUtil {
	/**
	 * 连接字符串数组，将其连成一个字符串。
	 * 
	 * @param str 要连接的字符串数组
	 * @return 连接后的字符串
	 */
	public static String connect(String[] str) {
		return connect(str, "\n");
	}

	/**
	 * 连接字符串数组，将其连成一个字符串。
	 * 
	 * @param str 要连接的字符串数组
	 * @param separator 两个字符串之间的分隔符号
	 * @return 连接后的字符串
	 */
	public static String connect(String[] str, String separator) {
		if (str == null) {
			return null;
		}
		if (str.length == 0) {
			return "";
		}

		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < str.length - 1; i++) {
			result.append(str[i]);
			result.append(separator);
		}
		result.append(str[str.length - 1]);

		return result.toString();
	}

	/**
	 * 字符串替换，将 source 中的 oldString 全部换成 newString
	 * 
	 * @param source 源字符串
	 * @param oldString 老的字符串
	 * @param newString 新的字符串
	 * @return 替换后的字符串
	 */
	public static String Replace(String source, String oldString, String newString) {
		if (source == null || source.equals("")) {
			return "";
		}

		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length(); // 源字符串长度
		int lengthOfOld = oldString.length(); // 老字符串长度
		int posStart = 0; // 开始搜索位置
		int pos; // 搜索到老字符串的位置

		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}

		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}

		return output.toString();
	}

	/**
	 * 相当于JScript中的函数split,将字符串str,按字符delim进行分割 如str = "a,b,c",delim =
	 * ",";,把str按delim拆开后把它们加入到List类的对象中
	 * 
	 * @param str 要进行判断的字符
	 * @param delim 分隔符号
	 * @return 分隔后的字符数组
	 */
	public static List<String> split(String str, String delim) {
		List<String> splitList = null;
		StringTokenizer st = null;

		if (str == null) {
			return splitList;
		}

		if (delim != null) {
			st = new StringTokenizer(str, delim);
		}
		else {
			st = new StringTokenizer(str);
		}

		if (st != null && st.hasMoreTokens()) {
			splitList = new ArrayList<String>();
			while (st.hasMoreTokens())
				splitList.add(st.nextToken());
		}

		return splitList;
	}

	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/**
	 * 判断某个字符串是否是整数
	 * 
	 * @param str 要转换的字符串
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		if (str == null || str == "") {
			return false;
		}

		String num = "0123456789";
		for (int i = 0; i < str.length(); i++) {
			char a = str.charAt(i);
			if (num.indexOf(a) == -1) {
				/* 说明str中含有数字以外的字符 */
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断某个字符串是否是double型的数字
	 * 
	 * @param str 要转换的字符串
	 * @return boolean
	 */
	public static boolean isDouble(String str) {
		if (str == null || str == "") {
			return false;
		}

		String num = "0123456789.-";
		for (int i = 0; i < str.length(); i++) {
			char a = str.charAt(i);
			if (num.indexOf(a) == -1) {
				/* 说明str中含有数字以外的字符 */
				return false;
			}
		}
		
		if (str.indexOf("-") > 0){
			return false;
		}

		return true;
	}

	/**
	 * 判断某个字符是否是数字
	 * 
	 * @param c 要进行判断的字符
	 * @return boolean
	 */
	public static boolean isNumber(char c) {
		String num = "0123456789";
		if (num.indexOf(c) == -1) {
			/* 说明c此时不是数字 */
			return false;
		}
		return true;
	}

	/**
	 * 校验email地址的是否合法
	 * 
	 * @param email 要进行判断的email地址
	 * @return boolean
	 */
	public static boolean isEmail(String email) {
		if (email == null || email == "") {
			return false;
		}

		List<?> names = split(email, "@");
		int count = names.size();
		if (count != 2) {
			return false;
		}

		for (int i = 0; i < count; i++) {
			String name = (String) names.get(i);
			if (name.length() <= 0) {
				return false;
			}
			for (int j = 0; j < name.length(); j++) {
				name = name.toLowerCase();
				char c = name.charAt(j);
				String s = "abcdefghijklmnopqrstuvwxyz_-.";
				if (s.indexOf(c) == -1 && !isNumber(c)) {
					return false;
				}
			}
			if (name.substring(0, 1) == "." || name.substring(name.length() - 1) == ".") {
				return false;
			}
		}

		String name1 = (String) names.get(1);
		if (name1.indexOf(".") == -1) {
			return false;
		}

		int k = name1.length() - name1.lastIndexOf(".") - 1;
		if (k != 2 && k != 3) {
			return false;
		}

		if (email.indexOf("..") != -1) {
			return false;
		}

		return true;
	}

	/**
	 * 将一个字符串解析成整型
	 * 
	 * @param str 要解析的字符串
	 * @param defaultValue 为空时的默认值
	 * @return
	 */
	public static int parseInt(String str, int defaultValue) {
		if (str != null && str.trim().length() > 0) {
			try {
				return Integer.parseInt(str);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/**
	 * 将一个字符串解析成长整型
	 * 
	 * @param str 要解析的字符串
	 * @param defaultValue 为空时的默认值
	 * @return
	 */
	public static long parseLong(String str, long defaultValue) {
		if (str != null && str.trim().length() > 0) {
			try {
				return Long.parseLong(str);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/**
	 * 四舍五入
	 * 
	 * @param val 需要处理的值
	 * @param scale 保留到小数点后多少位
	 * @return 四舍五入结果
	 */
	public static double round(double val, int scale) {
		double ret = 0;
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal base = new BigDecimal(Double.toString(val));
		BigDecimal one = new BigDecimal("1");
		ret = base.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		base = null;
		one = null;
		return ret;
	}
	
	public static long round(double val) {
		return (long)round(val, 0);
	}

	/**
	 * 将strInt当作整数和n相加
	 * 
	 * @param strInt
	 * @param n
	 * @return strInt +（数字加） n
	 */
	public static String addInt(String strInt, int n) {
		int result = 0;
		if (strInt != null) {
			result = Integer.parseInt(strInt);
		}
		result += n;
		return String.valueOf(result);
	}

	/**
	 * 将两个string当成整型相加
	 * 
	 * @param strInt
	 * @param n
	 * @return strInt +（数字加） n
	 */
	public static String addInt(String str1, String str2) {
		return String.valueOf(parseInt(str1, 0) + parseInt(str2, 0));
	}

	/**
	 * 在 pre 与 end 之间插入若干字符 pad ，直至到 dig 位 举例： 中间补0： pad("205", "ABC", '0', 12) ==
	 * “205000000ABC” 左边补0 pad("", "ABC", '0', 12) == “000000000ABC” 右补0
	 * pad("205", "", '0', 12) == “205000000000” pad("", "", '0', 12) ==
	 * “000000000000”
	 * 
	 * @param pre
	 * @param end
	 * @param pad
	 * @param dig 返回字符串的总长度
	 * @return 返回组合後的字符串
	 */
	public static String pad(String pre, String end, char pad, int dig) {
		int pLen = pre.length();
		int eLen = end.length();
		if ((pLen + eLen) >= dig) {
			return pre + end;
		}
		else {
			int padLen = dig - pLen - eLen;
			StringBuffer sb = new StringBuffer();
			while (padLen-- > 0) {
				sb.append(pad);
			}
			return pre + sb.toString() + end;
		}
	}

	/**
	 * 在数字前面加上0，使字符串成为bt位
	 * 
	 * @param nb 要处理的数字
	 * @param bt 输出的位数
	 * @return 输出bt位字符串
	 */
	public static String appendZeroBeforeNumber(int nb, int bt) {
		String val = String.valueOf(nb);
		if (val.length() >= bt) {
			return val;
		}
		else {
			int len = val.length();
			for (int i = len; i < bt; i++) {
				val = "0" + val;
			}
			return val;
		}
	}

	/**
	 * 去掉数字后面无效的 0
	 * 
	 * @param str 要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String removeInvalidZero(String str) {
		if (str == null || str.equals("") || str.equals("0")) {
			return str;
		}

		// 不符合格式规范
		if (str.indexOf('.') == -1) {
			return str;
		}

		String ret = str;
		int site = ret.length() - 1;

		for (; site > 0; site--) {
			if (ret.charAt(site) != '0')
				break;
		}

		ret = ret.substring(0, site + 1);
		// 删除位于最后的小数点
		if ((ret.indexOf('.') > 0) && (ret.indexOf('.') == ret.length() - 1)) {
			ret = ret.substring(0, ret.indexOf('.'));
		}

		return ret;
	}

	/**
	 * 转换为百分比
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static String toPercent(double val) {
		DecimalFormat formatter = new DecimalFormat("#.#%");
		return formatter.format(val);
	}
	
	/**
	 * 将元转换成分
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long dollarToCent(double val) {
		return (long) round(val * 100.00, 0);
	}

	/**
	 * 将分转换成元
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static double centToDollar(long val) {
		return (double) round(val / 100.00, 2);
	}
	
	/**
	 * 将厘转换成分
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static double liToCent(long val) {
		return (double) round(val / 10.00, 1);
	}

	/**
	 * 将分转换成厘
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long centToLi(double val) {
		return (long) round(val * 10.00, 0);
	}
	
	/**
	 * 将厘转换成分
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static double liToDollar(long val) {
		return (double) round(val / 1000.00, 1);
	}

	/**
	 * 将分转换成厘
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long dollarToLi(double val) {
		return (long) round(val * 1000.00, 0);
	}
	
	/**
	 * 将分转换成元
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static String formatCentToDollar(long val) {
		DecimalFormat formatter = new DecimalFormat("#.00");
		return formatter.format((double) round(val / 100.00, 2));
	}
	
	
	/**
	 * 转换比例
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static double getPart(long val) {
		return (double) round(val / 10000.00, 5);
	}
	
	/**
	 * 保存比例
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long toPart(double val) {
		return round(val * 10000.00);
	}
	
	
	/**
	 * 乘以100
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long rideHundred(double val) {
		return (long) round(val * 100.00, 0);
	}
	
	/**
	 * 除以100
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static double divideHundred(long val) {
		return (double) round(val / 100.00, 2);
	}
	
	/**
	 * 将秒转换成分
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static int minuteToSecond(int val) {
		return val * 60;
	}

	/**
	 * 将分转换成秒
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static int secondToMinute(int val) {
		return (int) Math.ceil(val / 60.0);
	}
	
	
	/**
	 * 将秒转换成毫秒
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static long secondToMillisecond(long val) {
		return val * 1000;
	}
	
	/**
	 * 将毫秒转换成秒
	 * 
	 * @param val 需要处理的值
	 * @return 处理后的结果
	 */
	public static int millisecondToSecond(long val) {
		return (int) Math.ceil(val / 1000.0);
	}
	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/**
	 * &nbsp;将数字金额转换成中文大写<br>
	 * <code>作者：source 版本：0.1 2008-10-18 </code> 附：正确填写票据和结算凭证的基本规定<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;银行、单位和个人填写的各种票据和结算凭证是办理支付结算和现金收付的重要依据，直接关系到支付结算的准确、及时和安全。<br>
	 * 票据和结算凭证是银行、单位和个人凭以记载账务的会计凭证，是记载经济业务和明确经济责任的一种书面证明。因此，填写票据和结算凭证，必须做到标准化、规范化，要要素齐全、数字正确、字迹清晰、不错漏、不潦草，防止涂改。</tr>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;一、中文大写金额数字应用正楷或行书填写，如<br>
	 * 壹（壹）、贰（贰）、叁、肆（肆）、伍（伍）、陆（陆）、柒、捌、玖、拾、佰、仟、万（万）、亿、元、角、分、零、整（正）等字样。<br>
	 * 不得用一、二（两）、三、四、五、六、七、八、九、十、念、毛、另（或0）填写，不得自造简化字。<br>
	 * 如果金额数字书写中使用繁体字，如貳、陸、億、萬、圓的，也应受理。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;二、中文大写金额数字到“元”为止的，在“元”之后，应写“整”（或“正”）字，在“角”之后可以不写“整”（或“正”）字。<br>
	 * 大写金额数字有“分”的，“分”后面不写“整”（或“正”）字。</tr>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;三、中文大写金额数字前应标明“人民币”字样，大写金额数字应紧接“人民币”字样填写，不得留有空白。<br>
	 * 大写金额数字前未印“人民币”字样的，应加填“人民币”三字。<br>
	 * 在票据和结算凭证大写金额栏内不得预印固定的“仟、佰、拾、万、仟、伯、拾、元、角、分”字样。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;四、阿拉伯小写金额数字中有“0”时，<br>
	 * 中文大写应按照汉语语言规律、金额数字构成和防止涂改的要求进行书写。<br>
	 * 举例如下：<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;（一）阿拉伯数字中间有“O”时，中文大写金额要写“零”字。如￥1，409．50，应写成人民币壹仟肆佰零玖元伍角。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;（二）阿拉伯数字中间连续有几个“0”时，中文大写金额中间可以只写一个“零”字。如￥6，007．14，应写成人民币陆仟零柒元壹角肆分。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;（三）阿拉伯金额数字万位或元位是“0”，或者数字中间连续有几个“0”，万位、元位也是“0’，但千位、角位不是“0”时，<br>
	 * 中文大写金额中可以只写一个零字，也可以不写“零”字。如￥1，680．32，应写成人民币壹仟陆佰捌拾元零叁角贰分，<br>
	 * 或者写成人民币壹仟陆佰捌拾元叁角贰分；又如￥107，000．53，应写成人民币壹拾万柒仟元零伍角叁分，或者写成人民币壹拾万零柒仟元伍角叁分。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;（四）阿拉伯金额数字角位是“0”，而分位不是“0”时，中文大写金额“元”后面应写“零”字。如￥16，409.02，<br>
	 * 应写成人民币壹万陆仟肆佰零玖元零贰分；又如￥325．04，应写成人民币叁佰贰拾伍元零肆分。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;五、阿拉伯小写金额数字前面，均应填写入民币符号“￥”（或草写：）。<br>
	 * 阿拉伯小写金额数字要认真填写，不得连写分辨不清。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;六、票据的出票日期必须使用中文大写。<br>
	 * 为防止变造票据的出禀日期，在填写月、日时，月为壹、贰和壹拾的，日为壹至玖和壹拾、贰拾和叁抬的，应在其前加“零”；<br>
	 * 日为抬壹至拾玖的，应在其前加“壹”。如1月15日，应写成零壹月壹拾伍日。再如10月20日，应写成零壹拾月零贰拾日。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;七、票据出票日期使用小写填写的，银行不予受理。大写日期未按要求规范填写的，银行可予受理，但由此造成损失的，由出票入自行承担。<br>
	 * 
	 * @param money 要转换的数字金额
	 * @return 大写的金额
	 */
	public static String convertChinese(double money) {
		char[] s1 = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
		char[] s4 = { '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万' };
		String str = String.valueOf(Math.round(money * 100 + 0.001));
		String result = "";

		for (int i = 0; i < str.length(); i++) {
			int n = str.charAt(str.length() - 1 - i) - 48;
			result = s1[n] + "" + s4[i] + result;
		}

		result = result.replaceAll("零仟", "零");
		result = result.replaceAll("零佰", "零");
		result = result.replaceAll("零拾", "零");
		result = result.replaceAll("零亿", "亿");
		result = result.replaceAll("零万", "万");
		result = result.replaceAll("零元", "元");
		result = result.replaceAll("零角", "零");
		result = result.replaceAll("零分", "零");

		result = result.replaceAll("零零", "零");
		result = result.replaceAll("零亿", "亿");
		result = result.replaceAll("零零", "零");
		result = result.replaceAll("零万", "万");
		result = result.replaceAll("零零", "零");
		result = result.replaceAll("零元", "元");
		result = result.replaceAll("亿万", "亿");

		result = result.replaceAll("零$", "");
		result = result.replaceAll("元$", "元整");
		return result;
	}

	/**
	 * 字符串的行数
	 * 
	 * @param val 处理的字符串
	 * @return 行数
	 */
	public static int getLineSum(String str) {
		if (str == null || str.equals("")) {
			return 0;
		}

		int ret = 1;
		String temp = str;

		while (temp.indexOf('\n') >= 0) {
			temp = temp.substring(temp.indexOf('\n') + 1, temp.length());
			ret++;
		}

		return ret;
	}

	/**
	 * 取指定长度的标题
	 * 
	 * @param title 处理的标题
	 * @param num_char 允许的长度
	 * @param dot 超过指定长度，截取允许长度后添加的提示符号
	 * @return 处理后的标题
	 */
	public static String getSubTitle(String title, int num_char, String dot) {
		String sFinTitle = "";
		try {
			if (title == null || title.equals("")) {
				return "";
			}

			if (title.getBytes("GB2312").length <= num_char) {
				return title;
			}

			int ln = 0;
			for (int i = 0; i < title.length(); i++) {
				String temp = title.substring(i, i + 1);
				if (temp.getBytes("GB2312").length == 2) {
					ln += 2;
				}
				else {
					ln++;
				}
				if (ln >= num_char - dot.length()) {
					sFinTitle = title.substring(0, i) + dot;
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return sFinTitle;
	}

	/**
	 * 功能：获取文件所在的目录
	 * 
	 * @param filename 文件名称 例: /home/tomcat/index.html 返回: /home/tomcat
	 * @return 文件所在的目录
	 */
	public static String getParDir(String filename) {
		String ret = "";
		try {
			if (filename != null) {
				ret = filename;
				String separator = System.getProperty("file.separator");
				if (!ret.equals(separator) && ret.indexOf(separator) >= 0) {
					ret = ret.substring(0, ret.lastIndexOf(separator));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 将String转换为ResultMap对象，String 的格式 name=name1,value=value2 ...
	 * 
	 * @param str
	 * @return ResultMap
	 * @throws Exception
	 */
	public static Map<String, String> str2Map(String str) throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		// 将String按 , 分组
		String strArr[] = str.split(",");

		// 将分组的串放入map中
		for (int j = 0; j < strArr.length; j++) {
			String tmpStr[] = strArr[j].split("=");
			map.put(tmpStr[0].trim(), tmpStr[1].trim());
		}

		return map;
	}
/*
	public static void getZdData(List<?> dataList, List<?> zdList, String dataIdStr, String dataNameStr, String zdIdStr, String zdNameStr) throws Exception {
		if (dataList != null && zdList != null){
			for (int i=0; i<dataList.size(); i++){
				ResultMap dataMap = (ResultMap)dataList.get(i);
				for (int j=0; j<zdList.size(); j++){
					ResultMap zdMap = (ResultMap)zdList.get(j);
					if (zdMap.getString(zdIdStr, "").equals(dataMap.getString(dataIdStr, ""))){
						dataMap.put(dataNameStr, zdMap.getString(zdNameStr, ""));
						break;
					}
				}
			}
		}
	}
*/
	/**
	 * 将输入的字符串转换为首字母大写
	 * 
	 * @param str 要转换的字符串
	 * @return 变为首字母大写的字符串
	 */
	public static String headUpper(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	/**
	 * 将输入的字符串转换为首字母小写
	 * 
	 * @param str 要转换的字符串
	 * @return 变为首字母小写的字符串
	 */
	public static String headLower(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * 将一个对象转换成String, 如果为null仍然返回null
	 * 
	 * @param object
	 * @return 转换后的String, 如果为null仍然返回null
	 */
	public static String toString(Object object) {
		if (object == null) {
			return null;
		}
		return object.toString();
	}

	/**
	 * 将一个对象转换成String, 如果为null仍然返回null
	 * 
	 * @param object
	 * @return 转换后的String, 如果为null仍然返回null
	 */
	public static int toInt(Object object) {
		if (object == null) {
			return 0;
		}
		else if (isNumber(object.toString())){
			return Integer.parseInt(object.toString());
		}
		else{
			return 0;
		}
	}
	
	/**
	 * 将普通字符串格式化成数据库认可的字符串格式
	 * 
	 * @param str 要格式化的字符串
	 * @return 合法的数据库字符串
	 */
	public static String toSql(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		else {
			String sql = new String(str);
			return Replace(sql, "'", "''");
		}
	}

	/**
	 * 将字符串格式化成 HTML 代码输出,只转换特殊字符，适合于 HTML 中的表单区域
	 * 
	 * @param str 要格式化的字符串
	 * @return 格式化后的字符串
	 */
	public static String encodeHtml(String str) {
		if (str == null || str.equals("")) {
			return "";
		}

		String html = new String(str);
		html = Replace(html, "&", "&amp;");
		html = Replace(html, "<", "&lt;");
		html = Replace(html, ">", "&gt;");

		return html;
	}

	/**
	 * 将字符串格式化成 HTML 代码输出 除普通特殊字符外，还对空格、制表符和换行进行转换 适合于 HTML 中的显示输出
	 * 
	 * @param str 要格式化的字符串
	 * @return 格式化后的字符串
	 */
	public static String toHtml(String str) {
		if (str == null || str.equals("")) {
			return "";
		}

		String html = new String(str);
		html = Replace(html, "\r\n", "\n");
		html = Replace(html, "\n", "<br/>");
		html = Replace(html, "\t", "    ");
		html = Replace(html, " ", "&nbsp;");

		return html;
	}

	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/** *********************************************************************************** */

	/**
	 * 将编码 ISO-8859-1 转换成 gb2312
	 * 
	 * @param src 待转换的字符串
	 * @param ignoreWindows 是否在windows忽略
	 * @author liujj
	 * @return
	 */
	public static String iso2GB2312(String src, boolean ignoreWindows) {
		if (ignoreWindows && System.getProperty("os.name").indexOf("Windows") != -1) {// 主要是方便本地调试
			return src;
		}
		else {
			return iso2GB2312(src);
		}
	}

	/**
	 * 将编码 ISO-8859-1 转换成 gb2312
	 * 
	 * @param src 待转换的字符串
	 * @author liujj
	 * @return
	 */
	public static String iso2GB2312(String src) {
		try {
			return (new String(src.getBytes("ISO-8859-1"), "gb2312"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return src;
	}

	/**
	 * 使用系统默认编码转换成 gb2312
	 * 
	 * @param src
	 * @return
	 */
	public static String toGB2312(String src) {
		try {
			return (new String(src.getBytes(), "gb2312"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return src;
	}

	/** *********************************************************************************** */
	/** *********************************************************************************** */
	/** *********************************************************************************** */

	/**
	 * 将字符串转为UTF-8编码，用于处理特殊字符，类似预javascript中的escape函数
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		if (src == null || src.equals("")) {
			return "";
		}
		else {
			int i;
			char j;
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length() * 6);

			for (i = 0; i < src.length(); i++) {

				j = src.charAt(i);

				if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
					tmp.append(j);
				else if (j < 256) {
					tmp.append("%");
					if (j < 16)
						tmp.append("0");
					tmp.append(Integer.toString(j, 16));
				}
				else {
					tmp.append("%u");
					tmp.append(Integer.toString(j, 16));
				}
			}
			return tmp.toString();
		}
	}

	/**
	 * 将UTF-8编码转为字符串，用于处理特殊字符，类似预javascript中的unescape函数
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		if (src == null || src.equals("")) {
			return "";
		}
		else {
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length());
			int lastPos = 0, pos = 0;
			char ch;
			while (lastPos < src.length()) {
				pos = src.indexOf("%", lastPos);
				if (pos == lastPos) {
					if (src.charAt(pos + 1) == 'u') {
						ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
						tmp.append(ch);
						lastPos = pos + 6;
					}
					else {
						ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
						tmp.append(ch);
						lastPos = pos + 3;
					}
				}
				else {
					if (pos == -1) {
						tmp.append(src.substring(lastPos));
						lastPos = src.length();
					}
					else {
						tmp.append(src.substring(lastPos, pos));
						lastPos = pos;
					}
				}
			}
			return tmp.toString();
		}
	}
	
	/** 
	 * 将strDecimal和n相乘
	 * @param strDecimal
	 * @param n
	 * @return strDecimal * n
	 */
	public static String multiply(String strDecimal, String n)
	{
		if (strDecimal == null || strDecimal.length() == 0)
		{
			return strDecimal;
		}
		
		BigDecimal decimal1 = new BigDecimal(strDecimal);
		BigDecimal decimal2 = new BigDecimal(n);
		return format(decimal1.multiply(decimal2).toString());
	}
	
	/** 
	 * 将strDecimal和n相除
	 * @param strDecimal
	 * @param n
	 * @param scale 精度，保留scale位小数
	 * @return strDecimal / n
	 */
	public static String divide(String strDecimal, String n, int scale)
	{
		return divide(strDecimal, n, scale, BigDecimal.ROUND_HALF_UP);
	}

	/** 将strDecimal和n相除
	 * @param strDecimal
	 * @param n
	 * @param scale
	 * @param roundMethod 四舍五入模式
	 * @return
	 */
	public static String divide(String strDecimal, String n, int scale, int roundMethod)
	{
		if (strDecimal == null || strDecimal.length() == 0)
		{
			return strDecimal;
		}
		
		
		BigDecimal decimal1 = new BigDecimal(strDecimal);
		BigDecimal decimal2 = new BigDecimal(n);
		String result = format(decimal1.divide(decimal2, scale, roundMethod).toString());
		return result;
	}
	
	/** 格式化把小数点后面的无用0干掉
	 * @param result
	 * @return 
	 */
	public static String format(String result)
	{
		if(result.indexOf(".") != -1)
		{
			for (int i = result.length(); i > 0; i--)
			{
				if(result.charAt(i - 1) != '0')
				{
					result = result.substring(0, i);
					break;
				}
			}
			if(result.charAt(result.length() - 1) == '.')
			{
				result = result.substring(0, result.length() - 1);
			}
			
		}
		return result;
	}
	
	
	/** 格式化把小数点后面的无用0干掉
	 * @param result
	 * @return 
	 */
	public static String format(double val)
	{
		String result = String.valueOf(val);
		if(result.indexOf(".") != -1)
		{
			for (int i = result.length(); i > 0; i--)
			{
				if(result.charAt(i - 1) != '0')
				{
					result = result.substring(0, i);
					break;
				}
			}
			if(result.charAt(result.length() - 1) == '.')
			{
				result = result.substring(0, result.length() - 1);
			}
			
		}
		return result;
	}
	
	

	/**
	 * 按照格式串格式化Java数据对象
	 * @param pattern 格式串
	 * @param obj Java数据对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Object obj) {
		if (obj == null)
			return "";

		if (obj instanceof String) {
			return (String) obj;
		}

		if (pattern == null) {
			return obj.toString();
		}

		if (obj instanceof java.util.Date) {
			return format(pattern, (java.util.Date) obj);
		}

		if (obj instanceof java.math.BigDecimal) {
			return format(pattern, (java.math.BigDecimal) obj);
		}

		if (obj instanceof Double) {
			return format(pattern, (Double) obj);
		}

		if (obj instanceof Float) {
			return format(pattern, (Float) obj);
		}

		if (obj instanceof Long) {
			return format(pattern, (Long) obj);
		}

		if (obj instanceof Integer) {
			return format(pattern, (Integer) obj);
		}

		return obj.toString();
	}

	/**
	 * 按照格式串格式化Date对象
	 * @param pattern 格式串
	 * @param locale 区域属性
	 * @param d Date对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Locale locale, java.util.Date d) {
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		return sdf.format(d);
	}

	/**
	 * 按照格式串格式化Date对象
	 * @param pattern 格式串
	 * @param d Date对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, java.util.Date d) {
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}

	/**
	 * 按照格式串格式化BigDecimal对象
	 * @param pattern 格式串
	 * @param n BigDecimal对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, java.math.BigDecimal n) {
		if (n == null)
			return "";
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}

	/**
	 * 按照格式串格式化Double对象
	 * @param pattern 格式串
	 * @param n Double对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Double n) {
		if (n == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}

	/**
	 * 按照格式串格式化Float对象
	 * @param pattern 格式串
	 * @param n Float对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Float n) {
		if (n == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}

	/**
	 * 按照格式串格式化Integer对象
	 * @param pattern 格式串
	 * @param n Integer对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Integer n) {
		if (n == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}

	/**
	 * 按照格式串格式化Long对象
	 * @param pattern 格式串
	 * @param n Long对象
	 * @return 格式化后的字符串
	 */
	public static String format(String pattern, Long n) {
		if (n == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}
	
	/** 
	 * 获取上传文件路径
	 * @param basePath
	 * @return 路径
	 */
	public static String getUploadFilePath(String basePath){
		StringBuffer sbf = new StringBuffer();
		try {
			sbf.append(basePath);
			if (!new File(sbf.toString()).exists()) {
				new File(sbf.toString()).mkdirs();
			}
			
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMM");
			String dateDirName = dateFormater.format(new Date());

			sbf.append(System.getProperty("file.separator"));
			sbf.append(dateDirName);
			if (!new File(sbf.toString()).exists()) {
				new File(sbf.toString()).mkdirs();
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sbf.toString();
	}
	
	/** 
	 * 获取时间文件名
	 * @return 文件名
	 */
	public static String getTimeFileName(){
		StringBuffer sbf = new StringBuffer();
		try {
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			sbf.append(dateFormater.format(new Date()));
			sbf.append(round(Math.random()*1000));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sbf.toString();
	}
	
	/**
	 *  byte转换为Kb
	 * @param k
	 * @return
	 */
	public static Double byteToKb(long b)
	{
		Double ret = 0.0;
		ret = Double.valueOf(b);
		ret = ret/1024;
		ret = round(ret,2);
		return ret;
	}
	
	/**
	 *  byte转换为Mb
	 * @param k
	 * @return
	 */
	public static Double byteToMb(long b)
	{
		Double ret = 0.0;
		ret = Double.valueOf(b);
		ret = ret/(1024*1024);
		ret = round(ret,2);
		return ret;
	}
	

	/**
	 *  KB转换为mb
	 * @param k
	 * @return
	 */
	public static Double kbToMb(long k)
	{
		Double ret = 0.0;
		ret = Double.valueOf(k);
		ret = ret/1024;
		ret = round(ret,2);
		return ret;
	}
	
	/**
	 *  KB转换为Byte
	 * @param k
	 * @return
	 */
	public static long kbToByte(Double k)
	{
		long ret = 0;
		ret = Long.valueOf(round(k*1024)+"");
		return ret;
	}
	
	/**
	 * Mb转换为Kb
	 * @param m
	 * @return
	 */
	public static long mbToKb(Double m)
	{
		long ret = 0;
		ret = Long.valueOf(round(m*1024)+"");
		return ret;
	}
	
	/**
	 * Mb转换为Byte
	 * @param m
	 * @return
	 */
	public static long mbToByte(Double m)
	{
		long ret = 0;
		ret = Long.valueOf(round(m*1024*1024)+"");
		return ret;
	}
	
	/**
	 * 格式化字节
	 * @param b
	 * @return
	 */
	public static String formatByte(long b)
	{
		String str = "";
		Double size = 0.0;
		size = StringUtil.byteToMb(b);
		str = size+"M";
		if(size<1.0)
		{
			size = StringUtil.byteToKb(b);
			str = size+"K";
			if(size<1.0)
			{
				str = b+"B";
			}
		}
		return str;
	}
	/**
	* 删除文件，可以是单个文件或文件夹
	* @param   fileName    待删除的文件名
	* @return 文件删除成功返回true,否则返回false
	*/
	public static boolean delete(String fileName)
	{
		File file = new File(fileName);
		if(!file.exists())
		{
			System.out.println("删除文件失败："+fileName+"文件不存在");
			return false;
		}
		else
		{
			if(file.isFile())
			{
				return deleteFile(fileName);
			}
			else
			{
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	* 删除单个文件
	* @param   fileName    被删除文件的文件名
	* @return 单个文件删除成功返回true,否则返回false
	*/
	public static boolean deleteFile(String fileName)
	{
		File file = new File(fileName);
		if(file.isFile() && file.exists())
		{
			file.delete();
			System.out.println("删除单个文件"+fileName+"成功！");
			return true;
		}
		else
		{
			System.out.println("删除单个文件"+fileName+"失败！");
			return false;
		}
	}

	/**
	* 删除目录（文件夹）以及目录下的文件
	* @param   dir 被删除目录的文件路径
	* @return  目录删除成功返回true,否则返回false
	*/
	public static boolean deleteDirectory(String dir)
	{
		//如果dir不以文件分隔符结尾，自动添加文件分隔符
		if(!dir.endsWith(File.separator))
		{
			dir = dir+File.separator;
		}
		File dirFile = new File(dir);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if(!dirFile.exists() || !dirFile.isDirectory())
		{
			System.out.println("删除目录失败"+dir+"目录不存在！");
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for(int i=0;i<files.length;i++)
		{
			//删除子文件
			if(files[i].isFile())
			{
			flag = deleteFile(files[i].getAbsolutePath());
			if(!flag)
			{
				break;
			}
			}
			//删除子目录
			else
			{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if(!flag)
				{
					break;
				}
			}
		}
		if(!flag)
		{
			System.out.println("删除目录失败");
			return false;
		}
		//删除当前目录
		if(dirFile.delete())
		{
			System.out.println("删除目录"+dir+"成功！");
			return true;
		}
		else
		{
			System.out.println("删除目录"+dir+"失败！");
			return false;
		}
	}
	
	public static String removeLocalUrl(HttpServletRequest request, String content) throws AppException {
		try {
			String saveUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
//			System.out.println("request.getRequestURL().toString() : "+request.getRequestURL().toString());
//			System.out.println(" saveUrl :"+saveUrl);
			return content.replaceAll(saveUrl, "");
		}
		catch (Exception e) {
			e.printStackTrace();
			return content;
		}
	}
	
	public static String replaceHtmlCode(String content) throws AppException {
		try {
			String str = "";
			if (content != null){
				str = content.replaceAll("'", "&#39;");
				str = str.replaceAll("\"", "&#34;");
				str = str.replaceAll("<", "&lt;");
				str = str.replaceAll(">", "&gt;");
			}
			return str;
		}
		catch (Exception e) {
			e.printStackTrace();
			return content;
		}
	}
	
	/**
	 * 删除input字符串中的html格式
	 * @param input
	 * @param length
	 * @return
	 */
	public static String splitAndFilterHtml(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		}
		else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}
	

    /**
     * 字符串连接函数（类似js的array.join(",")）
     * @param join
     * @param strAry
     * @return
     */
    public static String join(String join, Object[] strAry) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < strAry.length; i++) {
			if (i == (strAry.length - 1)) {
				sbf.append("" + strAry[i]);
			}
			else {
				sbf.append("" + strAry[i]).append(join);
			}
		}
		return new String(sbf);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//System.out.println(getUploadFilePath("e:\\test"));
		System.out.println(getTimeFileName());
		System.out.println((1024*1024));
		System.out.println(byteToMb(1024*1024));
	}
}