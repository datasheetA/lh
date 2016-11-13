package com.l_h.cd.pub.pdf;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


/**
 * 创建pdf文件
 * @author Administrator
 *
 */
public class CreatePdf {
	public static Logger logger = Logger.getLogger(CreatePdf.class);
	private BaseFont baseFontChinese = null;
	private OutputStream outStream = null;
	private String fileName = null;
	private List<Element> list = null;
	private Font fontChinese = null;
	private String title = null;
	private String author = null;
	private String subject = null;
	private String keywords = null;
	private String creator = null;
	
	public OutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Element> getList() {
		return list;
	}

	public void setList(List<Element> list) {
		this.list = list;
	}

	public BaseFont getBaseFontChinese() {
		return baseFontChinese;
	}

	public void setBaseFontChinese(BaseFont baseFontChinese) {
		this.baseFontChinese = baseFontChinese;
	}

	public Font getFontChinese() {
		return fontChinese;
	}

	public void setFontChinese(Font fontChinese) {
		this.fontChinese = fontChinese;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public CreatePdf() throws DocumentException, IOException{
		baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		fontChinese = new Font(baseFontChinese, 12, Font.NORMAL);
		list = new ArrayList<Element>();
	}
	
	public void add(Element e){
		list.add(e);
	}
	
	
	/**
	 * 通过HttpServletResponse生成pdf文件
	 * @param response HttpServletResponse
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void writeHttpResponsePdf(HttpServletResponse response) throws DocumentException, IOException{
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		setOutStream(ba);
		
		save();
		
		String encodeFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".pdf");
		response.setContentType("application/pdf");   
        response.setContentLength(ba.size());   
        ServletOutputStream out = response.getOutputStream();   
        ba.writeTo(out);   
        out.flush();
        out.close();
        ba.close();
        out = null;
        ba = null; 
	}
	
	public void writePdf() throws DocumentException, FileNotFoundException{
		setOutStream(new FileOutputStream(fileName));
        save();
	}
	
	/**
	 * 保存pdf文件
	 * @throws DocumentException
	 */
	public void save() throws DocumentException{
		//创建一个Document对象
		Document document = new Document();

		try {
			//添加PDF文档的一些信息
			if (title != null){
				document.addTitle(title);
			}
			if (author != null){
				document.addAuthor(author);
			}
			if (subject != null){
				document.addSubject(subject);
			}
			if (keywords != null){
				document.addKeywords(keywords);
			}
			if (creator != null){
				document.addCreator(creator);
			}

			//生成名为 HelloWorld.pdf 的文档
			PdfWriter.getInstance(document, outStream);
			
			// 打开文档，将要写入内容
			document.open();
			
			for(int i=0; i<list.size(); i++){
				document.add((Element)list.get(i));
			}
		}
		catch (DocumentException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DocumentException(e.getMessage());
		}
		finally{
			document.close();
		}
	}
	public void demo()throws Exception {
		// 创建一个Document对象
		Document document = new Document();

		try {
			BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(baseFontChinese, 12, Font.NORMAL);

			// 生成名为 HelloWorld.pdf 的文档
			PdfWriter.getInstance(document, new FileOutputStream("f:\\HelloWorld.pdf"));
			// 添加PDF文档的一些信息
			document.addTitle("Hello World example");
			document.addAuthor("Bruno Lowagie");
			document.addSubject("This example explains how to add metadata.");
			document.addKeywords("iText, Hello World, step 3, metadata");
			document.addCreator("My program using iText");
			// 打开文档，将要写入内容
			document.open();
			// 插入一个段落
			document.add(new Paragraph("Hello World1! 你好！", fontChinese));

			Image img = Image.getInstance("f:\\62696.jpg");
			//Image image = Image.getInstance(new URL(http://xxx.com/logo.jpg));

			img.setAbsolutePosition(0, 0);
			document.add(img);

			//增加表格
			Table aTable = new Table(3);
			int width[] = {25,25,50};
			aTable.setWidths(width);
			aTable.setWidth(80); // 占页面宽度 80%
			aTable.setAutoFillEmptyCells(true); //自动填满
			aTable.setPadding(1);
			aTable.setSpacing(1);
			aTable.setBorder(0);
			Cell cell = new Cell(new Phrase("这是一个测试的 3*3 Table 数据", fontChinese));
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			cell.setRowspan(3);
			cell.setColspan(1);
			aTable.addCell(cell);
			aTable.addCell(new Cell("#1"));
			aTable.addCell(new Cell("#2"));
			aTable.addCell(new Cell("#3"));
			aTable.addCell(new Cell("#4"));
			aTable.addCell(new Cell("#5"));
			aTable.addCell(new Cell("#6"));
            document.add(aTable);
		}
		catch (DocumentException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally{
			// 关闭打开的文档
			document.close();
		}
	}
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	    int BOXLISTTABLE_COLUMN = 4;
	    float[] boxlistwidths = {0.1f, 0.05f, 0.05f, 0.05f};
	    
		CreatePdf pdf = new CreatePdf();
		
		// 插入一个段落
		pdf.add(new Paragraph("Hello World1! 你好！", pdf.getFontChinese()));

		//增加图片
		Image img = Image.getInstance("f:\\62696.jpg");
		img.setAbsolutePosition(0, 0);
		pdf.add(img);

		//增加表格
		PdfPTable table = new PdfPTable(boxlistwidths);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.setHeaderRows(3);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        
        PdfPCell title = new PdfPCell(new Paragraph("详细资料" + "\n" + "甄凯", pdf.getFontChinese()));
        title.setColspan(BOXLISTTABLE_COLUMN);
        title.setFixedHeight(35);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(title);
        
        
        PdfPCell footer = new PdfPCell(new Paragraph("时间:" + "2007-02-05", pdf.getFontChinese()));
        footer.setColspan(BOXLISTTABLE_COLUMN);
        footer.setFixedHeight(25);
        footer.setPaddingRight(10); //到右侧边框的距离
        footer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(footer);
        
        table.addCell(new Paragraph("姓名", pdf.getFontChinese()));
        table.addCell(new Paragraph("年龄", pdf.getFontChinese()));
        table.addCell(new Paragraph("性别", pdf.getFontChinese()));
        table.addCell(new Paragraph("国籍", pdf.getFontChinese()));
        
        PdfPCell nameCell = new PdfPCell(new Paragraph("甄凯", pdf.getFontChinese()));
        PdfPCell ageCell = new PdfPCell(new Paragraph("23", pdf.getFontChinese()));
        PdfPCell sexCell =new PdfPCell(new Paragraph("男", pdf.getFontChinese()));
        PdfPCell nationalCell =new PdfPCell( new Paragraph("中国", pdf.getFontChinese()));
        
   
        //加粗表格边框 -- 若需要可使用
        /*
        Rectangle border = new Rectangle(0f, 0f);
        border.setBorderWidthLeft(0.5f);
        border.setBorderWidthRight(0.5f);
        border.setBorderColor(Color.BLACK);
        border.setBorderWidthBottom(1f);
        border.setBorderWidthTop(0.5f);
        
        nameCell.cloneNonPositionParameters(border);
        ageCell.cloneNonPositionParameters(border);
        sexCell.cloneNonPositionParameters(border);
       */
        
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        sexCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nationalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sexCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nationalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                
        table.addCell(nameCell);
        table.addCell(ageCell);
        table.addCell(sexCell);
        table.addCell(nationalCell);
		pdf.add(table);
        
        pdf.setFileName("f:\\HelloWorld.pdf");

        pdf.writePdf();
	}
}
