package com.l_h.cd.pub.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * 拼音工具
 * 
 * @author Administrator
 */
public class PinyinUtil {

	static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static {
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	/**
	 * 汉字转拼音缩写
	 * 
	 * @param str //要转换的汉字字符串
	 * @return String //拼音缩写
	 */
	public static String getPYString(String str) {
		List<Set<Character>> ls = new ArrayList<Set<Character>>();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((int) c >= 33 && (int) c <= 126) {// 字母和符号原样保留
				Set<Character> set = new HashSet<Character>();
				set.add(c);
				ls.add(set);
			}
			else if ((int) c == 32) {// 空格 原样保留
				Set<Character> set = new HashSet<Character>();
				set.add(c);
				ls.add(set);
			}
			else {// 累加拼音声母
				Set<Character> set = getPYChar(c);
				ls.add(set);
			}
		}
		String returnStr = getstr("", "", ls, 0);
		return returnStr.substring(0, returnStr.length() - 1);
	}

	/**
	 * 生成拼音字符串
	 * @param _prex 前缀
	 * @param str 字符串
	 * @param ls list
	 * @param index 索引
	 * @return
	 */
	public static String getstr(String _prex, String str, List<Set<Character>> ls, int index) {
		if (ls.size() > index) {
			Set<Character> s = ls.get(index);
			for (Character character : s) {
				if (str.length() > 0 && str.substring(str.length() - 1).equals(",")) {
					str += _prex;
				}
				_prex += character;
				str = getstr(_prex, str + character, ls, index + 1);
				_prex = _prex.substring(0, _prex.length() - 1);
			}
		}
		else {
			str += ",";
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(getPYString("张学友"));
	}

	/**
	 * 取单个字符的拼音声母
	 * 
	 * @param c //要转换的单个汉字
	 * @return String 拼音声母
	 */
	public static Set<Character> getPYChar(char c) {
		Set<Character> set = new HashSet<Character>();
		byte[] array = new byte[2];
		array = String.valueOf(c).getBytes();
		int i = (short) (array[0] - '\0' + 256) * 256 + ((short) (array[1] - '\0' + 256));
		if (i >= 0xA3B0 && i <= 0xA3B9) {
			set.add((char) (i - 0xA3B0 + "0".codePointAt(0)));
		}
		else if (i > 0xA3C0 && i <= 0xA3E6) {
			set.add((char) (i - 0xA3C0 + "a".codePointAt(0) - 1));
		}
		else if (i < 0xB0A1) {
			set.add('?');
		}
		else {
			try {
				String[] _tmp = PinyinHelper.toHanyuPinyinStringArray(c);
				for (int j = 0; j < _tmp.length; j++) {
					String string = _tmp[j];
					set.add(string.charAt(0));
				}
			}
			catch (Exception e) {
				set.add('?');
			}
		}
		return set;
	}

}
