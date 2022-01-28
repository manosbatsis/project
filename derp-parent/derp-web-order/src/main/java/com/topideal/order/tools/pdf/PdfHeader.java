package com.topideal.order.tools.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
	
/**
 * PDF页头
 * @author Guobs
 *
 */
public class PdfHeader extends PdfPageEventHelper{
	
	private static String  IMAGE_PATH =  PdfHeader.class.getResource("/").getPath() ;
	
	public static PdfPTable header;
	
	static {
		IMAGE_PATH = IMAGE_PATH.substring(0, IMAGE_PATH.indexOf("WEB-INF")) ;
		IMAGE_PATH += "resources/image/topideal.jpg" ;
	}
	
	public PdfHeader() {
		super() ;
	}
	
	public PdfHeader(PdfPTable header) {
		PdfHeader.header = header;
	}
	
	@Override
    public void onEndPage(PdfWriter writer, Document document) {
        //把页眉表格定位
        header.writeSelectedRows(0, -1, 36, 806, writer.getDirectContent());
    }

    /**
     * 设置页眉
     * @param writer
     * @param req
     * @throws MalformedURLException
     * @throws IOException
     * @throws DocumentException
     */
    public void setTableHeader(PdfWriter writer) throws MalformedURLException, IOException, DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(65);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        Image image= Image.getInstance(IMAGE_PATH); //图片自己传
        image.setWidthPercentage(80);
        cell.setPaddingTop(-30f);
        cell.addElement(image);
        table.addCell(cell);
        PdfHeader event = new PdfHeader(table);
        writer.setPageEvent(event);
    }   
}
