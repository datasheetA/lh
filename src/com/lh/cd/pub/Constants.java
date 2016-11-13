package com.lh.cd.pub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义当前系统中的常量
 * 
 * @author sunny
 * @version 0.1
 */
public class Constants {

	public Constants() {
		super();
	}
	

	public static final String SESSION_RANDOM_VALIDATE = "web_random_validcode";//验证码
	public static final String SESSION_LOGIN_ADMIN_USER_ID = "sysadmin_admin_user_id";//用户id
	public static final String SESSION_LOGIN_ADMIN_USER_ACCOUNT = "sysadmin_admin_user_account";//用户账号
	public static final int SYSTEM_ADMIN_ID = 1;// 系统管理员ID
	public static final int ERRORCODE_NEED_LOGIN = 1;// 错误代码：需要登录
	
	
	

	
	
	public static final String COOKIE_QUERY_PAGE_QUERYURL = "web_website_page_queryurl";

	public static final String COOKIE_QUERY_PAGE_QUERYSTRING = "web_website_page_querystring";

	public static final String COOKIE_LOGIN_USER_ID = "web_website_user_id";

	public static final String COOKIE_LOGIN_USER_ACCOUNT = "web_website_user_account";

	public static final String COOKIE_LOGIN_USER_NAME = "web_website_user_name";

	public static final String COOKIE_ENCRYPT_LOGIN_USER_ID = "web_website_encrypt_user_id";

	public static final String COOKIE_ENCRYPT_LOGIN_USER_ACCOUNT = "web_website_encrypt_user_account";

	public static final String COOKIE_ENCRYPT_LOGIN_USER_NAME = "web_website_encrypt_user_name";


	// 数据常量
	public static final String[] ZD_SEX = { "1:男", "2:女", "0:保密" };
	public static final String[] ZD_BLOOD = { "1:A", "2:B", "3:AB", "4:O", "5:其他" };
	public static final String[] ZD_MARRIAGE = { "1:未婚", "2:已婚", "0:保密" };
	public static final String[] ZD_CERTTYPE = { "1:身份证", "2:学生证", "3:护照" };
	public static final String[] ZD_CONSTELLATION = { "1:白羊座", "2:金牛座", "3:双子座", "4:巨蟹座", "5:狮子座", "6:处女座", "7:天秤座", "8:天蝎座", "9:射手座", "10:摩羯座",
			"11:水瓶座", "12:双鱼座" };
	public static final String[] ZD_EDUCATION = { "1:小学及以下", "2:初中", "3:高中", "4:大专", "5:本科", "6:硕士", "7:博士及以上", "8:保密" };

	// 信息发布
	public static final String[] ZD_PB_TEMPLATE_TYPE = { "1:首页模板", "2:频道模板", "3:文章模板" };
	public static final int TEMPLATE_TYPE_HOMEPAGE = 1;// 首页模板
	public static final int TEMPLATE_TYPE_CHANNEL = 2;// 频道模板
	public static final int TEMPLATE_TYPE_ARTICLE = 3;// 文章模板

	public static final String[] ZD_PB_CHANNEL_LINK_TYPE = { "1:由频道路径决定", "2:链接到频道下的文章页面", "3:自定义" };
	public static final int CHANNEL_LINK_TYPE_BY_CHANNEL = 1;// 由频道路径决定
	public static final int CHANNEL_LINK_TYPE_TO_ARTICLE = 2;// 链接到频道下的文章页面
	public static final int CHANNEL_LINK_TYPE_SELF_DEFINE = 3;// 自定义

	public static final int AD_TYPE_COMMON = 1;// 普通广告类型id
	public static final int AD_TYPE_FLOAT = 3;// 浮动广告类型id
	public static final int AD_TYPE_COUPLET = 3;// 对联广告类型id
	public static final int AD_TYPE_TOP = 4;// 顶部广告类型id
	public static final int AD_TYPE_SLIDE = 5;// 首页滑动广告类型id

	public static final int ALLOW_SEARCH_BY_CHANNEL = 2;// 文章是否允许被检索(根据频道设置来决定)

	public static final String SYS_CONFIG_KEY_HOMEPAGE_TEMPLATE_ID = "homepage_template_id";

	public static final String NEW_FLAG_URL_PATH = "/images/new.gif";
	public static final String TOP_FLAG_URL_PATH = "/images/top.gif";
	public static final String DEFAULT_PREVIEW_IMAGE = "/admin/images/pview.gif";
	public static final String DEFAULT_BANNER_FILE = "/images/default-banner.gif";
	public static final String DEFAULT_IMAGE_FILE = "/images/default-image.gif";
	public static final String DEFAULT_ICON_FILE = "/images/default-icon.gif";

	/**
	 * 通过velocity获取常量(例：CONST_1;CONST_2)
	 * 
	 * @param column
	 * @param id
	 * @return 常量值
	 */
	public String get(String column, Object id) {
		StringBuffer fieldName = new StringBuffer(1024);
		try {
			fieldName.append(column.toUpperCase());
			fieldName.append("_");
			fieldName.append(id);
			Field field = this.getClass().getField(fieldName.toString());
			return (field == null) ? "" : field.get(this).toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过velocity获取常量
	 * 
	 * @param column
	 * @return 常量值
	 */
	public String get(String column) {
		StringBuffer fieldName = new StringBuffer(1024);
		try {
			fieldName.append(column.toUpperCase());
			Field field = this.getClass().getField(fieldName.toString());
			return (field == null) ? "" : field.get(this).toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取常量数组
	 * 
	 * @param column
	 * @return 常量数组
	 */
	public List<Map<String, String>> getList(String column) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			StringBuffer fieldName = new StringBuffer(128);
			fieldName.append(column.toUpperCase());
			Field field = this.getClass().getField(fieldName.toString());
			if (field != null) {
				String[] item = (String[]) field.get(this);
				for (int i = 0; i < item.length; i++) {
					String val[] = item[i].split(":");
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", val[0]);
					map.put("name", val[1].toString());
					list.add(map);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据从常量数组中获取名称
	 * 
	 * @param column
	 * @param name
	 * @return id
	 */
	public int getId(String column, String name) {

		try {
			StringBuffer fieldName = new StringBuffer(128);
			fieldName.append(column.toUpperCase());
			Field field = this.getClass().getField(fieldName.toString());
			if (field != null) {
				String[] item = (String[]) field.get(this);
				for (int i = 0; i < item.length; i++) {
					String val[] = item[i].split(":");
					if (val[1].equals(name)) {
						return Integer.parseInt(val[0]);
					}
				}
			}
			return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 更加id从常量数组中获取名称
	 * 
	 * @param column
	 * @param id
	 * @return 名称
	 */
	public String getName(String column, Object id) {
		try {
			StringBuffer fieldName = new StringBuffer(128);
			fieldName.append(column.toUpperCase());
			Field field = this.getClass().getField(fieldName.toString());
			if (field != null) {
				String[] item = (String[]) field.get(this);
				for (int i = 0; i < item.length; i++) {
					String val[] = item[i].split(":");
					if (Integer.parseInt(val[0]) == Integer.parseInt(id.toString())) {
						return val[1].toString();
					}
				}
			}
			return "";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 更加id从数组(从数据库中查询出来的对象数组)中获取名称
	 * 
	 * @param list
	 * @param id
	 * @return 名称
	 */
	@SuppressWarnings("unchecked")
	public String getNameById(List<?> list, Object id) {
		try {
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Object obj = (Object) list.get(i);
					Object idObj = obj.getClass().getMethod("getId", new Class[] {}).invoke(obj, new Object[] {});
					String tid = (idObj == null) ? "" : idObj.toString();
					String tname = (String) obj.getClass().getMethod("getName", new Class[] {}).invoke(obj, new Object[] {});
					if (tid.equals(id.toString())) {
						return tname;
					}
				}
			}
			return "";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Constants c = new Constants();
		System.out.println(c.getName("ZD_SEX", "1"));
		System.out.println(c.get("ZD_SEX", "1"));
		System.out.println("time:" + (System.currentTimeMillis() - time));

		System.out.println(c.getName("RESOURCE_CATEGORY", 1));
	}

}
