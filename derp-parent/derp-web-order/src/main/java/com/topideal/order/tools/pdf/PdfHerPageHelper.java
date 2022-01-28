package com.topideal.order.tools.pdf;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
	
/**
 * PDF横向页脚
 * @author Guobs
 *
 */
public class PdfHerPageHelper extends PdfPageEventHelper{
	
	private PdfTemplate total;

    private BaseFont bfChinese;
    
    private String waterMarContent ;
    
    private String headerImageUrl ;

	public void setWaterMarContent(String waterMarContent) {
		this.waterMarContent = waterMarContent;
	}
	
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}

	public PdfHerPageHelper() {
		super() ;
	}
	
	@Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // 得到文档的内容并为该内容新建一个模板
        total = writer.getDirectContent().createTemplate(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        try {

            String font = PdfUtils.PATH + PdfUtils.SIMSUN_FILE;

            // 设置字体对象为Windows系统默认的字体
            bfChinese = BaseFont.createFont(font , BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
	
	
	@Override
    public void onEndPage(PdfWriter writer, Document document) {
		
		if(StringUtils.isNotBlank(waterMarContent)) {
			//加入水印
			addWaterMar(writer) ;
		}
		
		if(StringUtils.isNotBlank(headerImageUrl)) {
			try {
				if(writer.getCurrentPageNumber() == 1) {
					addTableHeader(writer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        // 新建获得用户页面文本和图片内容位置的对象
        PdfContentByte pdfContentByte = writer.getDirectContent();
        // 保存图形状态
        pdfContentByte.saveState();
        String text = String.valueOf(writer.getPageNumber()) ;
        // 获取点字符串的宽度
        float textSize = bfChinese.getWidthPoint(text, 10);
        pdfContentByte.beginText();
        // 设置随后的文本内容写作的字体和字号
        pdfContentByte.setFontAndSize(bfChinese, 10);

        // 定位'X/'
        float x = (document.right() + document.left()) / 2;
        float y = 20f;
        pdfContentByte.setTextMatrix(x, y);
        pdfContentByte.showText(text);
        pdfContentByte.endText();

        // 将模板加入到内容（content）中- // 定位'Y'
        pdfContentByte.addTemplate(total, x + textSize, y);

        pdfContentByte.restoreState();
    }
	
	/**
     * 重写PdfPageEventHelper中的onCloseDocument方法
     */
    /*@Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(bfChinese, 9);
        total.setTextMatrix(0, 0);
        // 设置总页数的值到模板上，并应用到每个界面
        total.showText("共"+String.valueOf(writer.getPageNumber() - 1)+"页");
        total.endText();
    }*/

	private void addWaterMar(PdfWriter writer) {
		
		float x = 0 ;
		float fontSize = 0 ;
		float yMargin = 0 ;
		
		if(waterMarContent.length() > 20) {
			x = 600 ;
			fontSize = 40 ;
			yMargin = 170 ;
		}else {
			x = 570 ;
			fontSize = 48 ;
			yMargin = 250 ;
		}
		
		for(int i = 1 ; i <= 3; i ++) {
			// 加入水印
			PdfContentByte waterMar = writer.getDirectContentUnder();
			// 开始设置水印
			waterMar.beginText();
			// 设置水印透明度
			PdfGState gs = new PdfGState();
			// 设置填充字体不透明度
			gs.setFillOpacity(0.15f);
			
			if(i == 1) {
				gs.setFillOpacity(0.09f);
			}
			
			try {
				// 设置水印字体参数及大小                  (字体参数，字体编码格式，是否将字体信息嵌入到pdf中（一般不需要嵌入），字体大小)
				waterMar.setFontAndSize(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), fontSize);
				// 设置透明度
				waterMar.setGState(gs);
				// 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
				waterMar.showTextAligned(Element.ALIGN_RIGHT, waterMarContent, x, i * yMargin, 35);
				// 设置水印颜色
				waterMar.setColorFill(BaseColor.GRAY);
				//结束设置
				waterMar.endText();
				waterMar.stroke();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				waterMar = null;
				gs = null;
			}
		}
	}
	
	/**
     * 设置页眉
     * @param writer
     * @param req
     * @throws MalformedURLException
     * @throws IOException
     * @throws DocumentException
     */
    private void addTableHeader(PdfWriter writer) throws MalformedURLException, IOException, DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(65);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:"+headerImageUrl);
        InputStream inputStream = resource.getInputStream();
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] byteArray = output.toByteArray();
        
        Image image= Image.getInstance(byteArray); //图片自己传
        image.setWidthPercentage(80);
        cell.setPaddingTop(-30f);
        cell.addElement(image);
        table.addCell(cell);
        
        table.writeSelectedRows(0, -1, 36, 806, writer.getDirectContent());
    }
}
