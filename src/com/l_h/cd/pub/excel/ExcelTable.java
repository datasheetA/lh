/*
 * ExcelTable.java 
 * 创建日期 2006-4-14 16:16:24 
 * 时力永联科技有限公司 北京市海淀区中关村南大街乙56号方圆大厦9层
 * 电话：88026633 传真：88026633-291 邮编：100044 版权所有
 */

package com.l_h.cd.pub.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lh.cd.pub.data.ResultMap;

/**
 * 导出excel文件 工具类，用于组织要导出的数据 详细使用请参看 Excel.main()函数
 * @author Administrator
 * @version 0.1 2006-4-18 13:12:13
 */
public class ExcelTable {
	/** 行合并标记 */
	public static final String ROW_SPAN = "_rowspan";
	/** 列合并标记 */
	public static final String COL_SPAN = "_colspan";
	/** 隐藏字段 */
	public static final String HIDDEN = "_hidden";
	/** 文本字段 */
	public static final String STRING = "_string";
	/** 数值字段 */
	public static final String NUMBER = "_number";

	/** sheet的名称 */
	private String name = "";
	/** sheet的标题 在excel表第一行的题目 */
	private String title = "";
	/** 每列所对应的关键字 */
	private List key = new ArrayList();
	/** 每列的表头 */
	private List head = new ArrayList();
	/** 内容 */
	private List content = null;

	/** 每个列的宽度 */
	private List columnWidth = new ArrayList();

	/** 每个列的数据类型 */
	private List columnType = new ArrayList();

	/** 每列的默认宽度 */
	private int defaultWidth = 20;

	/** 存储单元格合并信息 */
	private Map mergeCell = new HashMap();

	/**
	 * 记录单元格合并情况
	 * @param startCol 起始列
	 * @param startRow 起始行
	 * @param endCol 结束列
	 * @param endRow 结束行
	 */
	void setMergeCell(int startCol, int startRow, int endCol, int endRow) {
		for (int col = startCol; col <= endCol; col++) {
			for (int row = startRow; row <= endRow; row++) {
				mergeCell.put(col + "-" + row, "sub");
			}
		}
		mergeCell.put(startCol + "-" + startRow, "pri");
	}

	/**
	 * 得到单元格合并情况
	 * @param col
	 * @param row
	 * @return
	 */
	String getMergeCell(int col, int row) {
		return (String) mergeCell.get(col + "-" + row);
	}

	/**
	 * 构造函数
	 * @param name 表名
	 */
	public ExcelTable(String name) {
		this.name = name;
		this.title = name;
	}

	/**
	 * 构造函数
	 */
	public ExcelTable() {

	}

	/**
	 * 得到每列的宽度
	 * @return 每列的宽度
	 */
	public List getColumnWidth() {
		return columnWidth;
	}

	/**
	 * 设置每列的宽度
	 * @param columnWidth 每列的宽度
	 */
	public void setColumnWidth(List columnWidth) {
		this.columnWidth = columnWidth;
	}

	/**
	 * 设置第n列的宽度
	 * @param n
	 * @param width 第n列的宽度
	 */
	public void setColumnWidth(int n, int width) {
		for (int i = columnWidth.size(); i <= n; i++) {
			columnWidth.add(new Integer(defaultWidth));
		}
		columnWidth.add(n, new Integer(width));
	}

	/**
	 * 得到第n列的宽度(从0开始计数)
	 * @param n
	 * @return 第n列的宽度(从0开始计数)
	 */
	public int getColumnWidth(int n) {
		int reslut = defaultWidth;
		if (n < columnWidth.size()) {
			reslut = ((Integer) columnWidth.get(n)).intValue();
		}
		return reslut;
	}

	/**
	 * 设置关键字为key的列的类型
	 * @param key
	 * @param type 类型
	 */
	public void setColumnType(String key, String type) {
		for (int i = 0; i < this.key.size(); i++) {
			String element = (String) this.key.get(i);
			if (key.equals(element)) {
				setColumnType(i, type);
			}
		}
	}

	/**
	 * 设置第n列的类型
	 * @param n 列数
	 * @param type 类型
	 */
	public void setColumnType(int n, String type) {
		for (int i = columnType.size(); i <= n; i++) {
			columnType.add(STRING);
		}
		columnType.add(n, type);
	}

	/**
	 * 得到第n列的类型(从0开始计数)
	 * @param n
	 * @return 第n列的类型
	 */
	public String getColumnType(int n) {
		String reslut = STRING;
		if (n < columnType.size()) {
			reslut = ((String) columnType.get(n));
		}
		return reslut;
	}

	/**
	 * 得到总列数
	 * @return 总列数
	 */
	public int getTotalColumn() {
		return key.size();
	}

	/**
	 * 得到sheet的内容部分
	 * @return sheet的内容部分
	 */
	public List getContent() {
		return content;
	}

	/**
	 * 设置sheet的内容部分
	 * @param content 内容
	 */
	public void setContent(List content) {
		this.content = content;
	}

	/**
	 * 建议使用setContent(List content)方法代替
	 * @param content
	 * @deprecated
	 */
	public void setValue(List content) {
		setContent(content);
	}

	/**
	 * 得到表头
	 * @return 表头列表
	 */
	public List getHead() {
		return head;
	}

	/**
	 * 用list设置表头
	 * @param head 表头
	 */
	public void setHead(List head) {
		this.head = head;
	}

	/**
	 * 得到表头第n个的名称
	 * @param n
	 * @return 表头第n个的名称
	 */
	public String getHeadName(int n) {
		return (String) head.get(n);
	}

	/**
	 * 得到所有关键字
	 * @return 所有关键字
	 */
	public List getKey() {
		return key;
	}

	/**
	 * 设置所有关键字
	 * @param key
	 */
	public void setKey(List key) {
		this.key = key;
	}

	/**
	 * 添加一个关键字
	 * @param object 关键字对象
	 */
	public void addKey(Object object) {
		key.add(object);
	}

	/**
	 * 添加关键字和表头（添第n行的表头）
	 * @param key 关键字
	 * @param head 表头的一个单元格
	 * @param row 将表头添加到第n行
	 */
	public void addHead(Object key, String head, int row) {
		addHead(key, head, row, 1, 1);
	}

	/**
	 * 添加关键字和表头，并包括合并单元格（添到第n行表头）
	 * @param key 关键字
	 * @param head 表头的一个单元格
	 * @param row 将表头添加到第n行
	 * @param rowSpan 合并的行数
	 * @param colSpan 合并的列数
	 */
	public void addHead(Object key, String head, int row, int rowSpan, int colSpan) {
		if (rowSpan < 1 || colSpan < 1) {
			throw new IllegalArgumentException("合并的单元格数不能小于1");
		}

		if (row == 0) {
			this.key.add(key);
		}
		for (int i = this.head.size(); i <= row; i++) {
			Map map = new ResultMap();
			this.head.add(map);
		}
		Map map = (Map) this.head.get(row);
		map.put(key, head);

		if (rowSpan > 1) {
			map.put(key + ROW_SPAN, String.valueOf(rowSpan));
		}

		if (colSpan > 1) {
			map.put(key + COL_SPAN, String.valueOf(colSpan));
		}
	}

	/**
	 * 添加关键字和表头（添第0行的表头）
	 * @param key 关键字
	 * @param head 表头
	 */
	public void addHead(Object key, String head) {
		addHead(key, head, 0);
	}

	/**
	 * 建议使用addHead(Object key, String head)方法代替
	 * @param headName
	 * @param key
	 * @deprecated
	 */
	public void setColumn(String headName, String key) {
		addHead(key, headName);
	}

	/**
	 * 得到第n个关键字
	 * @return 第n个关键字
	 */
	public Object getKey(int n) {
		return key.get(n);
	}

	/**
	 * 得到sheet的名称
	 * @return sheet的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置sheet的名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 得到每个sheet的标题 在第一行
	 * @return 每个sheet的标题 在第一行
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置每个sheet的标题
	 * @param title 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public static void main(String[] args) {
		ExcelTable sheet = new ExcelTable();

		sheet.setColumnWidth(10, 5);
		System.out.println(sheet.getColumnWidth(5));
		System.out.println(sheet.getColumnWidth(15));
		System.out.println(sheet.getColumnWidth(10));
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}
}
