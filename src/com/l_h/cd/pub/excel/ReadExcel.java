package com.l_h.cd.pub.excel;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 读取Excel文件
 * @author Administrator
 *
 */
public class ReadExcel {

	/**
	 * 读取excel文件
	 * @param filename 文件名
	 * @throws BiffException
	 * @throws IOException
	 */
	public void readExcel(String filename) throws BiffException, IOException {
		File excelFile = new File(filename);
		Workbook wb = Workbook.getWorkbook(excelFile);
		Sheet s = wb.getSheet(0);// 第1个sheet
		Cell c = null;
		int row = s.getRows();// 总行数
		int col = s.getColumns();// 总列数
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				c = s.getCell(j, i);
				System.out.print(c.getContents() + "  ");
			}
			System.out.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadExcel re = new ReadExcel();
		try {
			re.readExcel("f:\\test.xls");
		}
		catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
