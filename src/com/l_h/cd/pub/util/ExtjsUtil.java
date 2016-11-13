package com.l_h.cd.pub.util;

import java.util.Map;

import com.lh.cd.pub.db.Paginate;

public class ExtjsUtil {


	/**
	 * 得到order by
	 * 
	 * @param paraMap
	 * @param defaultOrderBy
	 * @return
	 */
	public static String getExtOrderBy(Map<String, Object> paraMap, String defaultOrderBy) {
		StringBuffer sbf = new StringBuffer();
		if (paraMap.get("sort") != null && !paraMap.get("sort").equals("")) {
			sbf.append(("" + paraMap.get("sort")).replaceAll("roleName", "role_name"));
			if (paraMap.get("dir") != null && !paraMap.get("dir").equals("")) {
				sbf.append(" " + paraMap.get("dir"));
			}
		}

		if (!sbf.toString().trim().equals("")) {
			return sbf.toString();
		}
		else if (defaultOrderBy != null && !defaultOrderBy.equals("")) {
			return defaultOrderBy;
		}
		else {
			return null;
		}
	}

	
	public static Paginate getExtPaginate(int start, int limit) {
		start = (start <= 0) ? 0 : start;
		limit = (limit <= 0) ? Paginate.DEFAULT_PAGE_SIZE : limit;
		//return new Paginate((int) (((start==1&&limit==1)?0:start) / limit) + 1, limit);
		return new Paginate((int) (start / limit) + 1, limit);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
