package com.topideal.report.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * pdf工具类
 * @author 杨创
 *https://blog.csdn.net/weixin_37848710/article/details/89522862
 */
public class PdfUtils {
	
    // 定义全局的字体静态变量
	private static Font titlefont;
	private static Font headfont;
	private static Font keyfont;
	private static Font textfont;
    // 最大宽度
	private static int maxWidth = 520;
	// 静态代码块
    static {
        try {
        	String classPath  = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
        	//String path=classPath+"META-INF/resource/resources/fonts/SIMYOU.TTF";
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);   
        	//BaseFont bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
           
            /*String path="src/main/webapp/resources/fonts/SIMSUN.TTC,0";
        	BaseFont bfChinese = BaseFont.createFont(path, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);*/
        	//String fontPath="/usr/share/fonts/dejavu/DejaVuSansCondensed.ttf";
            //BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            /*String path="/com/topideal/report/tools/SIMYOU.TTF";*/
        	//BaseFont bfChinese = BaseFont.createFont(path, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);

            titlefont = new Font(bfChinese, 14, Font.NORMAL);// 不加粗
            headfont = new Font(bfChinese, 11, Font.NORMAL);// 不加粗
            keyfont = new Font(bfChinese, 11, Font.NORMAL);// BOLD加粗
            textfont = new Font(bfChinese, 11, Font.NORMAL);
 
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
	public static void main(String[] args) {
		//getPdfFile("");
	}

	/**
	 * 生成暂估应收Pdf
	 * @param pdfPath
	 * @param PdfToBList
	 * @param PdfToCList
	 * @param merchantNameParm
	 * @param creatDate
	 * @param oneCellvale
	 */
	public static void getPdfFile(String pdfPath,
			ArrayList<String> keyList,
			List<Map<String, Object>> pdfToBList,
			List<Map<String, Object>>pdfToCList,
			String merchantNameParm,
			String creatDate,
			String oneCellvale) {
		try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
 
            // 2.建立一个书写器(Writer)与document对象关联
            //File file = new File("D:\\PDFDemo112.pdf");
            File file = new File(pdfPath);// 文件输出
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            //writer.setPageEvent(new Watermark("HELLO ITEXTPDF"));// 水印
			//writer.setPageEvent(new MyHeaderFooter());// 页眉/页脚 
            // 3.打开文档
            document.open();
			document.addTitle("Title@PDF-Java");// 标题
			document.addAuthor("Author@umiz");// 作者
			document.addSubject("Subject@iText pdf sample");// 主题
			document.addKeywords("Keywords@iTextpdf");// 关键字
			document.addCreator("Creator@umiz`s");// 创建者
 
            // 4.向文档中添加内容
			generatePDF(document,pdfPath,keyList,pdfToBList,pdfToCList,merchantNameParm,creatDate,oneCellvale);
 
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	
	// 生成PDF文件
	public static void  generatePDF(Document document,
			String pdfPath,
			ArrayList<String> keyList,
			List<Map<String, Object>> pdfToBList,
			List<Map<String, Object>>PdfToCList,
			String merchantNameParm,
			String creatDate,
			String oneCellvale) throws Exception {
	  
	     	// 段落
	 		//Paragraph paragraph = new Paragraph("2020年11月元森泰事业部E暂估应收", titlefont);
	 		Paragraph paragraph = new Paragraph(oneCellvale, titlefont);
	 		paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
	 		paragraph.setIndentationLeft(12); //设置左缩进
	 		paragraph.setIndentationRight(12); //设置右缩进
	 		paragraph.setFirstLineIndent(24); //设置首行缩进
	 		paragraph.setLeading(20f); //行间距
	 		paragraph.setSpacingBefore(5f); //设置段落上空白
	 		paragraph.setSpacingAfter(10f); //设置段落下空白
	  
	 		// 直线
	 		//Paragraph p1 = new Paragraph();
	 		//p1.add(new Chunk(new LineSeparator()));
	  
	 		// 点线
	 		//Paragraph p2 = new Paragraph();
	 		//p2.add(new Chunk(new DottedLineSeparator()));
	  
	 		// 超链接
	 		//Anchor anchor = new Anchor("baidu");
	 		//anchor.setReference("www.baidu.com");
	  
	 		// 定位
	 		//Anchor gotoP = new Anchor("goto");
	 		//gotoP.setReference("#top");
	  
	 		// 添加图片
	 		//Image image = Image.getInstance("https://img-blog.csdn.net/20180801174617455?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzg0ODcxMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70");
	 		//image.setAlignment(Image.ALIGN_CENTER);
	 		//image.scalePercent(40); //依照比例缩放
	 		// 表格
	 		PdfPTable table = createTable(new float[] { 160,140,120,80,80,80 });
	 		table.addCell(createCell(merchantNameParm, headfont, Element.ALIGN_LEFT, 3, false));
	 		table.addCell(createCell("创建时间："+creatDate, headfont, Element.ALIGN_RIGHT, 4, false));
	 		
	 		///BaseColor borderColor = new BaseColor(90, 140, 200);
	 		BaseColor bgColor = new BaseColor(220,220,220);
	 		
	 		table.addCell(createCellBeiJIng("渠道类型：To B", keyfont,6,Element.ALIGN_CENTER,null,bgColor));
	 		table.addCell(createCell("NC科目", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("客商名称", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("母品牌", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("数量", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("金额", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("币种", keyfont, Element.ALIGN_CENTER));
	 		
	 		
	 		for (int j1 = 0; j1 < pdfToBList.size(); j1++) { // 填值
				Map model = pdfToBList.get(j1);							
				for (String key : keyList) {
					Object obj = model.get(key);								
					if (obj instanceof Timestamp) {
						String value1 = obj.toString();
						String intNumber = value1.substring(0, value1.indexOf("."));
						table.addCell(createCell(intNumber, textfont));
					} else if (obj instanceof BigDecimal) {
						BigDecimal value1 = new BigDecimal(obj.toString());		
						table.addCell(createCell(value1.toString(), textfont));
					} else {
						String value1="";
						if (obj==null)value1="";
						if (obj!=null)value1=obj.toString();
						table.addCell(createCell(value1.toString(), textfont));
					}
				}
			}
	 		/*//Integer totalQuantity = 0;
	 		for (int i = 0; i < 10; i++) {
	 			table.addCell(createCell("60010104\\主营业务收入\\商 品销售\\经销业务TO B", textfont));
	 			table.addCell(createCell("第e仓", textfont));
	 			table.addCell(createCell("拜耳", textfont));
	 			table.addCell(createCell("-1", textfont));
	 			table.addCell(createCell("-126.000", textfont));
	 			table.addCell(createCell("CNY", textfont));
	 			//totalQuantity ++;
	 		}*/
	 		table.addCell(createCell("  ", headfont, Element.ALIGN_LEFT, 6, false));
	 		table.addCell(createCellBeiJIng("渠道类型：To C", keyfont,6,Element.ALIGN_CENTER,null,bgColor));
	 		table.addCell(createCell("NC科目", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("客商名称", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("母品牌", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("数量", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("金额", keyfont, Element.ALIGN_CENTER));
	 		table.addCell(createCell("币种", keyfont, Element.ALIGN_CENTER));
	 		for (int j1 = 0; j1 < PdfToCList.size(); j1++) { // 填值
				Map model = PdfToCList.get(j1);							
				for (String key : keyList) {
					Object obj = model.get(key);								
					if (obj instanceof Timestamp) {
						String value1 = obj.toString();
						String intNumber = value1.substring(0, value1.indexOf("."));
						table.addCell(createCell(intNumber, textfont));
					} else if (obj instanceof BigDecimal) {
						BigDecimal value1 = new BigDecimal(obj.toString());		
						table.addCell(createCell(value1.toString(), textfont));
					} else {
						String value1="";
						if (obj==null)value1="";
						if (obj!=null)value1=obj.toString();
						table.addCell(createCell(value1.toString(), textfont));
					}
				}
			}
	 		/*for (int i = 0; i < 10; i++) {
	 			table.addCell(createCell("60010104\\主营业务收入\\商 品销售\\经销业务TO B", textfont));
	 			table.addCell(createCell("第e仓", textfont));
	 			table.addCell(createCell("拜耳", textfont));
	 			table.addCell(createCell("-1", textfont));
	 			table.addCell(createCell("-126.000", textfont));
	 			table.addCell(createCell("CNY", textfont));
	 			//totalQuantity ++;
	 		}*/
	 		//table.addCell(createCell("总计", keyfont));
	 		//table.addCell(createCell("", textfont));
	 		//table.addCell(createCell("", textfont));
	 		//table.addCell(createCell("", textfont));
	 		//table.addCell(createCell(String.valueOf(totalQuantity) + "件事", textfont));
	 		//table.addCell(createCell("", textfont));
	  
	 		document.add(paragraph);
	 		//document.add(anchor);
	 		//document.add(p2);
	 		//document.add(gotoP);
	 		//document.add(p1);
	 		document.add(table);
	 		//document.add(image);
	 	}
	 	/**------------------------创建表格单元格的方法start----------------------------*/
	    /**
	     * 创建单元格(指定字体)
	     * @param value
	     * @param font
	     * @return
	     */
	    public static PdfPCell createCell(String value, Font font) {
	        PdfPCell cell = new PdfPCell();
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setPhrase(new Phrase(value, font));
	        return cell;
	    }

	    /**
	     * 创建单元格（指定字体、水平..）
	     * @param value
	     * @param font
	     * @param align
	     * @return
	     */
		public static PdfPCell createCell(String value, Font font, int align) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(align);
			cell.setPhrase(new Phrase(value, font));
			return cell;
		}
	    /**
	     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
	     * @param value
	     * @param font
	     * @param align
	     * @param colspan
	     * @return
	     */
	    public static PdfPCell createCell(String value, Font font, int align, int colspan) {
	        PdfPCell cell = new PdfPCell();
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(align);
	        cell.setColspan(colspan);
	        cell.setPhrase(new Phrase(value, font));
	        return cell;
	    }
	    /**
	     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
	     * @param value
	     * @param font
	     * @param align
	     * @param colspan
	     * @param boderFlag
	     * @return
	     */
	    public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
	        PdfPCell cell = new PdfPCell();
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(align);
	        cell.setColspan(colspan);
	        cell.setPhrase(new Phrase(value, font));
	        cell.setPadding(3.0f);
	        if (!boderFlag) {
	            cell.setBorder(0);
	            cell.setPaddingTop(15.0f);
	            cell.setPaddingBottom(8.0f);
	        } else if (boderFlag) {
	            cell.setBorder(0);
	            cell.setPaddingTop(0.0f);
	            cell.setPaddingBottom(15.0f);
	        }
	        return cell;
	    }
	    
		public static PdfPCell createCellBeiJIng(String value, Font font, int colspan, int align,BaseColor borderColor,BaseColor bgColor) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(align);
			cell.setColspan(colspan);
			cell.setPhrase(new Phrase(value, font));
			if (borderColor != null)
	            cell.setBorderColor(borderColor);
	        if (bgColor != null)
	            cell.setBackgroundColor(bgColor);
	        return cell;
		}
	    
	    /**
	     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
	     * @param value
	     * @param font
	     * @param align
	     * @param borderWidth
	     * @param paddingSize
	     * @param flag
	     * @return
	     */
		public static PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(align);
			cell.setPhrase(new Phrase(value, font));
			cell.setBorderWidthLeft(borderWidth[0]);
			cell.setBorderWidthRight(borderWidth[1]);
			cell.setBorderWidthTop(borderWidth[2]);
			cell.setBorderWidthBottom(borderWidth[3]);
			cell.setPaddingTop(paddingSize[0]);
			cell.setPaddingBottom(paddingSize[1]);
			if (flag) {
				cell.setColspan(2);
			}
			return cell;
		}
	/**------------------------创建表格单元格的方法end----------------------------*/
	 
	 
	/**--------------------------创建表格的方法start------------------- ---------*/
	    /**
	     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
	     * @param colNumber
	     * @param align
	     * @return
	     */
		public static PdfPTable createTable(int colNumber, int align) {
			PdfPTable table = new PdfPTable(colNumber);
			try {
				table.setTotalWidth(maxWidth);
				table.setLockedWidth(true);
				table.setHorizontalAlignment(align);
				table.getDefaultCell().setBorder(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return table;
		}
	    /**
	     * 创建指定列宽、列数的表格
	     * @param widths
	     * @return
	     */
		public static PdfPTable createTable(float[] widths) {
			PdfPTable table = new PdfPTable(widths);
			try {
				table.setTotalWidth(maxWidth);
				table.setLockedWidth(true);
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.getDefaultCell().setBorder(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return table;
		}
	    /**
	     * 创建空白的表格
	     * @return
	     */
		public static PdfPTable createBlankTable() {
			PdfPTable table = new PdfPTable(1);
			table.getDefaultCell().setBorder(0);
			table.addCell(createCell("", keyfont));
			table.setSpacingAfter(20.0f);
			table.setSpacingBefore(20.0f);
			return table;
		}
	/**--------------------------创建表格的方法end------------------- ---------*/
	 
	 

	
}
