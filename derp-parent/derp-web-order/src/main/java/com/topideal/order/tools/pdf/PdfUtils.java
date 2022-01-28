package com.topideal.order.tools.pdf;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Files;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.tools.DownloadUtil;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextFontResolver;

public class PdfUtils {
	
	/**pdf 垂直和水平变量*/
	public static final String HORIZONTAL = "horizontal" ;
	public static final String VERTICAL = "vertical" ;
	
	public static String PATH = "" ;
	
	public static final String SIMKAI_FILE = "SIMKAI.TTF" ;
	public static final String SIMSUN_FILE = "SIMSUN.TTC,0" ;
	public static final String MSYH_FILE = "msyh.ttf" ;
	
	// 定义全局的字体静态变量
	public static Font titlefont;
	public static Font headfont;
	public static Font keyfont;
	public static Font textfont;
	
	// 楷体
	public static Font textKaifont;
	public static Font titleKaifont;
	// 宋体
	public static Font textSongfont;
	public static Font titleSongfont;
	// TIMES_ROMAN
	public static Font textEnfont;
	
	//换行
	public static final String LINEFEED = "\r" ;
	
	//英文空格
	public static final String SPACE = "\u00a0" ;
	public static final String TAB = "\u00a0\u00a0\u00a0\u00a0" ;
	public static final String SPACE6 = "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0" ;
	public static final String SPACE8 = "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0" ;
	public static final String SPACE12 = "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0" ;
	
	//中文圆角空格
	public static final String SPACE_CN = "\u3000" ;
	public static final String TAB_CN = "\u3000\u3000\u3000\u3000" ;
	public static final String SPACE6_CN = "\u3000\u3000\u3000\u3000\u3000\u3000" ;
	public static final String SPACE8_CN = "\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" ;
	public static final String SPACE12_CN = "\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" ;
	
    // 最大宽度
	private static int maxWidth = 520;
	
	private static final Logger LOG = Logger.getLogger(PdfUtils.class) ;
	
	// 静态代码块
    static {
        try {
        	
        	PATH = PdfUtils.class.getResource("/").getPath() ;
        	
        	/*
        	 * 简单区分war包部署和jar包部署 classpath路径不一样情况
        	 */
        	if(PATH.indexOf("WEB-INF") > -1) {
        		PATH = PATH.substring(0, PATH.indexOf("WEB-INF")) + "resources/fonts/" ;
        	}else {
        		PATH = PATH + "resources/fonts/" ;
        		String[] fontsFils = new String[] {"SIMKAI.TTF", "SIMSUN.TTC", "msyh.ttf"} ;
        		
        		File file = new File(PATH) ;
        		if(!file.exists()) {
        			
        			file.mkdirs() ;
        			
        			for(int i = 0 ; i < fontsFils.length; i ++) {
        				ClassPathResource resource = new ClassPathResource("static/fonts/" + fontsFils[i]);
            			InputStream inputStream = resource.getInputStream();
            			
            			OutputStream output = new FileOutputStream(PATH + fontsFils[i]);        
            			byte[] buf = new byte[1024];        
            			int bytesRead;        
            			while ((bytesRead = inputStream.read(buf)) != -1) {
            				output.write(buf, 0, bytesRead);
            			}
            			
            			output.close();
            			
            			LOG.debug(PATH + fontsFils[i]);
        			}
        			
        		}
        	}
        	
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            BaseFont bfEnglish = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            BaseFont bfSIMKAI = BaseFont.createFont(PATH + SIMKAI_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont bfSIMSUN = BaseFont.createFont(PATH + SIMSUN_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, BaseFont.NOT_EMBEDDED);
            
            titlefont = new Font(bfChinese, 12, Font.BOLD);
            headfont = new Font(bfChinese, 12, Font.BOLD);
            keyfont = new Font(bfChinese, 11, Font.BOLD);
            textfont = new Font(bfChinese, 11, Font.NORMAL);
            
            titleSongfont = new Font(bfSIMSUN, 12, Font.BOLD);
            titleKaifont = new Font(bfSIMKAI, 12, Font.BOLD);
            
            textEnfont = new Font(bfEnglish, 11, Font.NORMAL);
            textKaifont = new Font(bfSIMKAI, 11, Font.NORMAL);
            textSongfont = new Font(bfSIMSUN, 11, Font.NORMAL);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建一个段落
     * @param content
     * @param font
     * @return
     */
    public static Paragraph createParagraph(String content, Font font) {
    	
    	Paragraph paragraph = new Paragraph(content, font);
    	paragraph.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
		paragraph.setLeading(20f); //行间距
		paragraph.setSpacingBefore(5f); //设置段落上空白
		paragraph.setSpacingAfter(10f); //设置段落下空白
		
		return paragraph ;
    }
    
    /**
     * 创建一个空段落
     * @param content
     * @param font
     * @return
     */
    public static Paragraph createParagraph() {
    	
    	Paragraph paragraph = new Paragraph();
    	paragraph.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
		paragraph.setLeading(20f); //行间距
		paragraph.setSpacingBefore(5f); //设置段落上空白
		paragraph.setSpacingAfter(10f); //设置段落下空白
		
		return paragraph ;
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
     * 创建无边框单元格（指定字体、垂直、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
	public static PdfPCell createNoBorderCell(String value, Font font, int verticalAlignment, int horizontalAlignment) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
	
	/**
     * 创建无边框单元格（指定字体）
     * @param value
     * @param font
     * @param align
     * @return
     */
	public static PdfPCell createNoBorderCell(String value, Font font) {
		return createNoBorderCell(value, font, Element.ALIGN_CENTER, Element.ALIGN_LEFT) ;
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
	
	/**
	 * 段落添加Chunk(简单)
	 */
	public static void paragraphAddChunk(Paragraph paragraph, String content, Font font) {
		Chunk chunk = new Chunk(content, font) ;
		paragraph.add(chunk);
	}
	
	/**
	 * 判断是否中文
	 * @param strName
	 * @return
	 */
	public static boolean isChinese(String strName) {
	    char[] ch = strName.toCharArray();
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (isChinese(c)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private static boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 根据html，水印和页头导出pdf
	 * @param html
	 * @param waterPrint
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream html2PdfMain(String html, String waterPrint, String direction, String headerImagePath) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            // 解决中文不显示问题
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(PATH + "SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            fontResolver.addFont(PATH + "SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            fontResolver.addFont(PATH + "msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            
            com.itextpdf.text.Rectangle rectangel = new RectangleReadOnly(PageSize.A4.getWidth(), PageSize.A4.getHeight()) ;
            renderer.setDirection(PdfUtils.VERTICAL);
            
            if(PdfUtils.HORIZONTAL.equals(direction)) {
            	rectangel = new RectangleReadOnly(PageSize.A4.getHeight(), PageSize.A4.getWidth());
            	renderer.setDirection(PdfUtils.HORIZONTAL);
            }
            
            renderer.layout();
            
            boolean isSetWaterPrint = false ; 
            if(StringUtils.isNotBlank(waterPrint)) {
            	isSetWaterPrint = true ;
            	renderer.setWaterPrint(waterPrint);
            }
            
            if(StringUtils.isNotBlank(headerImagePath)) {
            	renderer.setHeaderImageUrl(headerImagePath);
            }
            
            renderer.createPDF(os, rectangel, isSetWaterPrint);
            
            return os ;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return null ;
	}
	
	/**
	 * 根据html和页眉导出pdf
	 * @param html
	 * @param waterPrint
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream html2PdfWithHeader(String html, String headerImageUrl, String direction) throws Exception {
        return html2PdfMain(html, null, direction, headerImageUrl) ;
	}
	
	/**
	 * 根据html和水印导出pdf
	 * @param html
	 * @param waterPrint
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream html2PdfWithWaterPrint(String html, String waterPrint, String direction) throws Exception {
        return html2PdfMain(html, waterPrint, direction, null) ;
	}
	
	/**
	 * 根据html导出PDF
	 * @param html
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream html2Pdf(String html, String direction) throws Exception {
		return html2PdfWithWaterPrint(html, null, direction) ;
	}

	/**
	 * 根据pdf文件地址和水印图片地址，给pdf文件添加水印图片
	 * @param input PDF文件路径
	 * @param waterMarkPath 水印图片路径
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void setWatermark(String input, byte[] waterMarkPath) throws DocumentException, IOException {
		PdfStamper stamper = null;
		BufferedOutputStream bos = null;
		PdfReader reader = null;
		File copyFile = null;
		try {
			//截取文件路径
			input = input.trim();
			String filePath = input.substring(0, input.lastIndexOf("/") + 1);
			String copyName = "copy.pdf";
			copyFile = new File(filePath+copyName);
			Files.copy(new File(input), copyFile);
			bos = new BufferedOutputStream(new FileOutputStream(new File(input)));
			reader = new PdfReader(filePath+copyName);
			stamper = new PdfStamper(reader, bos);
			PdfContentByte content;
			PdfGState gs = new PdfGState();
			content = stamper.getOverContent(1);// 在内容上方加水印
			gs.setStrokeOpacity(0.2f);
			content.setGState(gs);
			content.beginText();
			Image image = Image.getInstance(waterMarkPath);
			image.setAbsolutePosition(200, 350); // set the first background
			image.scaleToFit(150, 75);
			image.setRotationDegrees(30);//旋转 */
			content.addImage(image);
			content.endText();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		} finally {
			stamper.close();
			bos.close();
			reader.close();
			copyFile.delete();
		}
	}

    /**
     * 根据pdf文件地址和水印图片地址，给pdf文件添加水印图片
     * @param input PDF文件路径
     * @param waterMarkPath 水印图片路径
	 * @param fileName 上传文件名称
	 * @param tempPath 暂存地址
     * @throws DocumentException
     * @throws IOException
     */
    public static Map<String, Object> setWatermark(String input, byte[] waterMarkPath, String fileName, String tempPath) throws DocumentException, IOException {
        Map<String, Object> resultMap = new HashMap<>();
        PdfReader reader = null;
    	PdfStamper stamper = null;
		BufferedOutputStream bufferedOutputStream = null;
		File tempFile = null;
		FileOutputStream fileOutputStream = null;
		ByteArrayOutputStream bos = null;
		FileInputStream fileInputStream= null;
        try {
            byte[] bytes = DownloadUtil.loadFileByteFromURL(input);
            new File(tempPath).mkdirs();
            tempFile = new File(tempPath + "/copy.pdf");
			fileOutputStream = new FileOutputStream(tempFile);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(bytes);

            reader = new PdfReader(bytes);
            stamper = new PdfStamper(reader, bufferedOutputStream);

			// 水印透明度
            PdfGState gs = new PdfGState();
			gs.setStrokeOpacity(0.2f);
			gs.setFillOpacity(0.5f);

			BaseFont font = BaseFont.createFont();
			// 第一页插入水印
			PdfContentByte content = stamper.getOverContent(1);
            content.setGState(gs);
			content.setFontAndSize(font, 38);
			content.setTextMatrix(10, 50);

            //图片水印
            Image image = Image.getInstance(waterMarkPath);
            image.setAbsolutePosition(200, 350);
            image.scaleToFit(150, 75);
            image.setRotationDegrees(30);//旋转 */
            content.addImage(image);
			stamper.close();
			reader.close();

			bos = new ByteArrayOutputStream();
			fileInputStream = new FileInputStream(tempFile);
			IOUtils.copy(fileInputStream, bos);

            //文件上传到蓝精灵
			String ext = "pdf";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
			jsonObject.put("fileName", fileName);
			jsonObject.put("fileBytes", Base64.encodeBase64String(bos.toByteArray()));
			jsonObject.put("fileExt",ext);
			jsonObject.put("fileSize",String.valueOf(bos.toByteArray().length));
			String resultJson= SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

			JSONObject jsonObj = JSONObject.fromObject(resultJson);

			if(jsonObj.getInt("code")!= 200){
				throw new RuntimeException("保存文件失败！");
			}
			//返回文件服务器存储URL
			String saveUrl = jsonObj.getString("url") ;
			resultMap.put("saveUrl", saveUrl);
			resultMap.put("fileSize", bos.toByteArray().length);
			return resultMap;
		} catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
        } finally {
			fileOutputStream.close();
			bufferedOutputStream.close();
			fileInputStream.close();
            bos.close();
            tempFile.delete();
        }

    }


}
