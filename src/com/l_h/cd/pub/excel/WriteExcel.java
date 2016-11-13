/*
 * WriteExcel.java 
 * 创建日期 2005-10-8 13:29:02 
 * 时力永联科技有限公司 北京市海淀区中关村南大街乙56号方圆大厦9层
 * 电话：88026633 传真：88026633-291 邮编：100044 版权所有
 */

package com.l_h.cd.pub.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.lh.cd.pub.data.ResultMap;

/**
 * 导出excel文件工具类 详细使用请参看 main函数
 * @author Administrator
 * @version 2008-10-8 11:20:02 创建
 */
public class WriteExcel {
	public static Logger logger = Logger.getLogger(WriteExcel.class);

	/** 表名 */
	public final WritableCellFormat formatTableName;
	/** 表头 */
	public final WritableCellFormat formatHead;
	/** 表内容 */
	public final WritableCellFormat formatContent;

	/** 导出excle的文件名 */
	private String fileName;

	/** 存放要显示的Sheet */
	private List listSheet = new ArrayList();

	/**
	 * 构造函数
	 * @param fileName 文件名
	 * @throws WriteException excel表写异常
	 */
	public WriteExcel(String fileName) throws WriteException {

		/* 定义标题格式 */
		WritableFont font = new WritableFont(WritableFont.TIMES, 20, WritableFont.BOLD);
		formatTableName = new WritableCellFormat(font);
		formatTableName.setAlignment(Alignment.CENTRE);
		formatTableName.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatTableName.setBorder(Border.ALL, BorderLineStyle.THIN);

		// 定义表头格式
		formatHead = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD));
		formatHead.setAlignment(Alignment.CENTRE);
		formatHead.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatHead.setBorder(Border.ALL, BorderLineStyle.THIN);

		// 定义普通单元格格式
		formatContent = new WritableCellFormat();
		formatContent.setAlignment(Alignment.CENTRE);
		formatContent.setVerticalAlignment(VerticalAlignment.CENTRE);
		formatContent.setBorder(Border.ALL, BorderLineStyle.THIN);

		this.fileName = fileName;
	}

	/**
	 * 往EXCEL里加入一个元素
	 * @param sheet 要添加的sheet
	 * @return true（根据 Collection.add 方法的常规协定）。
	 */
	public boolean add(ExcelTable sheet) {
		return listSheet.add(sheet);
	}

	/**
	 * 导出excel文件到web
	 * @param response
	 * @throws Exception
	 */
	public void export(HttpServletResponse response) throws Exception {
		response.reset();
		String fileName = new String(this.fileName.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "inline;filename=" + fileName + ".xls");
		response.setContentType("application/vnd.ms-excel");
		export(response.getOutputStream());
	}

	/**
	 * 导出excel文件到outPutStream
	 * @param outPutStream
	 * @throws Exception
	 */
	public void export(OutputStream outPutStream) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(outPutStream);

		for (int i = 0; i < listSheet.size(); i++) {
			System.out.println(i);
			ExcelTable data = (ExcelTable) listSheet.get(i);
			WritableSheet sheet = workbook.createSheet(data.getName(), i);
			write(sheet, data);
		}
		workbook.write();
		workbook.close();

	}

	/**
	 * @param sheet 表
	 * @param data 数据
	 * @throws Exception
	 */
	private void write(WritableSheet sheet, ExcelTable data) throws Exception {
		/* 设置每列宽度 */
		for (int i = 0; i < data.getTotalColumn(); i++) {
			sheet.setColumnView(i, data.getColumnWidth(i));
		}

		/* 当前行 */
		int curRow = 0;

		// 写表名
		mergeCells(sheet, data, 0, curRow, data.getTotalColumn() - 1, curRow);
		sheet.addCell(new Label(0, curRow, data.getTitle(), formatTableName));
		curRow++;

		/* 写表头 */
		Iterator iter = null;
		iter = data.getHead().iterator();
		curRow = writeDatas(sheet, iter, data, curRow, formatHead);

		/* 写内容 */
		iter = data.getContent().iterator();
		curRow = writeDatas(sheet, iter, data, curRow, formatContent);
	}

	/**
	 * 合并单元格
	 * @param sheet 表
	 * @param data 数据
	 * @param startCol 起始列
	 * @param startRow 起始行
	 * @param endCol 结束列
	 * @param endRow 结束行
	 * @throws RowsExceededException 行越界异常
	 * @throws WriteException 写表异常
	 */
	private void mergeCells(WritableSheet sheet, ExcelTable data, int startCol, int startRow, int endCol, int endRow)
			throws RowsExceededException, WriteException {
		if (data.getMergeCell(startCol, startRow) != null) {
			/* 该单元格已经被合并过 */
			return;
		}
		data.setMergeCell(startCol, startRow, endCol, endRow);
		sheet.mergeCells(startCol, startRow, endCol, endRow);
	}

	/**
	 * 写一组数据
	 * @param sheet 表单
	 * @param iter 表头
	 * @param data 数据
	 * @param curRow 当前行
	 * @return 当前行
	 * @throws RowsExceededException 行越界异常
	 * @throws WriteException 写表异常
	 */
	private int writeDatas(WritableSheet sheet, Iterator iter, ExcelTable data, int curRow, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		while (iter.hasNext()) {
			//Data map = Data.creatData(iter.next());
			ResultMap map = new ResultMap((Map) iter.next());
			for (int curColumn = 0; curColumn < data.getTotalColumn(); curColumn++) {
				merge(sheet, data, curRow, map, curColumn);

				String str = map.getString(data.getKey(curColumn), "");

				if (data.getColumnType(curColumn) == ExcelTable.NUMBER) {
					try {
						double d = Double.parseDouble(str);
						sheet.addCell(new Number(curColumn, curRow, d, format));
					}
					catch (NumberFormatException e) {
						sheet.addCell(new Label(curColumn, curRow, str, format));
					}
				}
				else {
					sheet.addCell(new Label(curColumn, curRow, str, format));
				}
			}
			curRow++;
		}
		return curRow;
	}

	/**
	 * 合并单元格
	 * @param sheet 表
	 * @param data 数据
	 * @param curRow 当前行
	 * @param map 表头数据
	 * @param curColumn 当前列
	 * @throws WriteException 写表异常
	 * @throws RowsExceededException 行越界异常
	 */
	private void merge(WritableSheet sheet, ExcelTable data, int curRow, ResultMap map, int curColumn)
			throws WriteException, RowsExceededException {
		Object spanCol = map.get(data.getKey(curColumn) + ExcelTable.COL_SPAN);
		Object spanRow = map.get(data.getKey(curColumn) + ExcelTable.ROW_SPAN);
		int col = 0;
		int row = 0;
		if (spanCol != null) {
			try {
				col = Integer.parseInt(spanCol.toString()) - 1;
			}
			catch (Exception e) {
			}
		}
		if (spanRow != null) {
			try {
				row = Integer.parseInt(spanRow.toString()) - 1;
			}
			catch (Exception e) {
			}
		}
		mergeCells(sheet, data, curColumn, curRow, curColumn + col, curRow + row);
	}

	/**
	 * 无用方法，可以直接删除
	 * @param i
	 * @deprecated
	 */
	public void setTotalColumn(int i) {
	}

	/**
	 * 使用示例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		WriteExcel excel = new WriteExcel("testE.xls");
		ExcelTable sheet = new ExcelTable("test1");
		sheet.setTitle("test");

		Map map = null;

		sheet.addHead("1", "A", 0, 1, 3);
		/* 仍然需要设置他对应的key值 */
		sheet.addHead("2", "", 0);
		sheet.addHead("3", "", 0);

		sheet.addHead("4", "D", 0);
		sheet.addHead("5", "E", 0);
		sheet.addHead("1", "F", 1);
		sheet.addHead("2", "G", 1);
		sheet.addHead("3", "H", 1);

		List content = new ArrayList();
		map = new ResultMap();
		content.add(map);
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");
		/* 在map里面这样设置，COL_SPAN表示横向合并两个单元格（本格和右面一格） */
		map.put("4" + ExcelTable.COL_SPAN, "2");
		/* 在map里面这样设置，COL_SPAN表示纵向合并三个单元格（本格和下面两格） */
		map.put("4" + ExcelTable.ROW_SPAN, "3");
		map.put("5", "5");

		map = new ResultMap();
		content.add(map);
		map.put("1", "1");
		map.put("1" + ExcelTable.COL_SPAN, "2");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");
		map.put("5", "5");
		sheet.setContent(content);

		excel.add(sheet);

		OutputStream out = new FileOutputStream("d://test.xls");
		excel.export(out);
		/*
		 * 如果是在页面上输出传入参数response即可 excel.export(response);
		 */
		out.close();
	}
}
