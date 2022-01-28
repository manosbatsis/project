package com.topideal.order.tools.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.itextpdf.text.Element.*;
import static com.topideal.order.tools.pdf.PdfUtils.textSongfont;
import static org.apache.poi.hssf.record.cf.BorderFormatting.*;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * excel单元格与pdf单元格转化
 */
public class PoiUtil {

    public static String PATH = "" ;

    public static final String SIMKAI_FILE = "SIMKAI.TTF" ;
    public static final String SIMSUN_FILE = "SIMSUN.TTC,0" ;

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

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sheet的列宽像素
     */
    public static int getPOIColumnWidth(Sheet sheet, Cell cell) {
        int poiCWidth = sheet.getColumnWidth(cell.getColumnIndex());
        int colWidthpoi = poiCWidth;
        int widthPixel = 0;
        if (colWidthpoi >= 416) {
            widthPixel = (int) (((colWidthpoi - 416.0) / 256.0) * 8.0 + 13.0 + 0.5);
        } else {
            widthPixel = (int) (colWidthpoi / 416.0 * 13.0 + 0.5);
        }
        return widthPixel;
    }

    /**
     * 获取当前行的行/列合并数
     */
    public static CellRangeAddress getColspanRowspanByExcel(Sheet sheet, int rowIndex, int colIndex) {
        CellRangeAddress result = null;
        int num = sheet.getNumMergedRegions();
        for (int i = 0; i < num; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if (range.getFirstColumn() == colIndex && range.getFirstRow() == rowIndex) {
                result = range;
            }
        }
        return result;
    }

    /**
     * 获取单元格背景颜色
     */
    public static int[] getBackgroundColor(CellStyle style) {
        Color color = style.getFillForegroundColorColor();
        int red = 0;
        int green = 0;
        int blue = 0;

        if (color instanceof HSSFColor) {
            HSSFColor hssfColor = (HSSFColor) color;
            short[] rgb = hssfColor.getTriplet();
            red = rgb[0];
            green = rgb[1];
            blue = rgb[2];
        }else  if (color instanceof XSSFColor) {
            XSSFColor xssfColor = (XSSFColor) color;
            byte[] rgb = xssfColor.getRGB();
            if(rgb != null) {
                red = (rgb[0] < 0) ? (rgb[0] + 256) : rgb[0];
                green = (rgb[1] < 0) ? (rgb[1] + 256) : rgb[1];
                blue = (rgb[2] < 0) ? (rgb[2] + 256) : rgb[2];
            }
        }

        if(red != 0 || green != 0 || blue != 0){
            return new int[] {red,green,blue};
        }

        return new int[] {255,255,255};
    }


    /**
     * 获取单元格文字垂直分布
     */
    public static int getVAlignByExcel(short align) {
        int result = 0;
        if (align == VerticalAlignment.TOP.getCode()) {
            result = ALIGN_TOP;
        }

        if (align == VerticalAlignment.CENTER.getCode()) {
            result = ALIGN_MIDDLE;
        }

        if (align == VerticalAlignment.JUSTIFY.getCode()) {
            result = ALIGN_JUSTIFIED;
        }

        if (align == VerticalAlignment.BOTTOM.getCode()) {
            result = ALIGN_BOTTOM;
        }
        return result;
    }

    /**
     * 获取单元格文字水平分布
     */
    public static int getHAlignByExcel(short align) {
        int result = 0;
        if (align == HorizontalAlignment.LEFT.getCode()) {
            result = ALIGN_LEFT;
        }
        if (align == HorizontalAlignment.RIGHT.getCode()) {
            result = ALIGN_RIGHT;
        }
        if (align == HorizontalAlignment.CENTER.getCode()) {
            result = ALIGN_CENTER;
        }
        if (align == HorizontalAlignment.JUSTIFY.getCode()) {
            result = ALIGN_JUSTIFIED;
        }


        return result;
    }

    public static Phrase getPhrase(Workbook wb, Cell cell) {

        if (cell.getCellType() == NUMERIC) {
            String value = null;
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                value = format.format(date);
            } else {
                value = (cell + "").trim();
            }

            Phrase phrase = new Phrase(value, getFontByExcel(wb, cell.getCellStyle()));
            return phrase;
        }

        cell.setCellType(STRING);
        Phrase phrase = new Phrase(cell.getStringCellValue(), getFontByExcel(wb, cell.getCellStyle()));
        return phrase;
    }

    public static com.itextpdf.text.Font getFontByExcel(Workbook wb, CellStyle style) {
        // 字体样式索引
        short index = style.getFontIndex();
        Font font = wb.getFontAt(index);
        // 转换 POI Font 到 iText Font
//        com.itextpdf.text.Font result = BaseFont.createFont(PATH + SIMSUN_FILE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font.getFontHeightInPoints());
        com.itextpdf.text.Font result = FontFactory.getFont(PATH + SIMSUN_FILE,  BaseFont.IDENTITY_H, BaseFont.EMBEDDED,font.getFontHeightInPoints());

        result.setStyle(com.itextpdf.text.Font.NORMAL);
        // 字体加粗、斜体
        if (font.getBold() && font.getItalic()) {
            result.setStyle(com.itextpdf.text.Font.BOLDITALIC);
        }else if (font.getBold()) { // 粗体
            result.setStyle(com.itextpdf.text.Font.BOLD);
        }else if (font.getItalic()) { // 斜体
            result.setStyle(com.itextpdf.text.Font.ITALIC);
        }

        // 字体颜色
        int colorIndex = font.getColor();
        HSSFColor color = HSSFColor.getIndexHash().get(colorIndex);
        if (color != null) {
            int rbg = new java.awt.Color(0, 0, 0).getRGB();
            result.setColor(new BaseColor(rbg));
        }

        // 下划线
        FontUnderline underline = FontUnderline.valueOf(font.getUnderline());
        if (underline == FontUnderline.SINGLE) {
            String ulString = com.itextpdf.text.Font.FontStyle.UNDERLINE.getValue();
            result.setStyle(ulString);
        }


        return result;
    }

    public static float getPixelHeight(float poiHeight) {
        float pixel = poiHeight / 28.6f * 26f;
        return pixel;
    }

    /*
     * 设置单元格边框
     */
    protected static void addBorderByExcel(Workbook wb, PdfPCell cell, CellStyle style) {
        short left = getBorderWidth(style.getBorderLeft().getCode());
        short right = getBorderWidth(style.getBorderRight().getCode());
        short top = getBorderWidth(style.getBorderTop().getCode());
        short bottom = getBorderWidth(style.getBorderBottom().getCode());
        if (left > 0)
            cell.enableBorderSide(PdfPCell.LEFT);
        else
            cell.disableBorderSide(PdfPCell.LEFT);
        if (right > 0)
            cell.enableBorderSide(PdfPCell.RIGHT);
        else
            cell.disableBorderSide(PdfPCell.RIGHT);
        if (top > 0)
            cell.enableBorderSide(PdfPCell.TOP);
        else
            cell.disableBorderSide(PdfPCell.TOP);
        if (bottom > 0)
            cell.enableBorderSide(PdfPCell.BOTTOM);
        else
            cell.disableBorderSide(PdfPCell.BOTTOM);
    }


    public static short getBorderWidth(short borderType) {
        switch (borderType) {
            case BORDER_DASH_DOT:
            case BORDER_DASH_DOT_DOT:
            case BORDER_DASHED:
            case BORDER_DOTTED:
            case BORDER_HAIR:
            case BORDER_DOUBLE:
            case BORDER_MEDIUM:
            case BORDER_MEDIUM_DASH_DOT:
            case BORDER_MEDIUM_DASH_DOT_DOT:
            case BORDER_MEDIUM_DASHED:
            case BORDER_SLANTED_DASH_DOT:
                return 1;
            case BORDER_NONE:
                return 0;
            case BORDER_THIN:
                return 1;
            case BORDER_THICK:
                return 2;
            default:
                return 1;
        }
    }

    public static boolean isUsed(Sheet sheet ,int colIndex, int rowIndex) {
        boolean result = false;
        int num = sheet.getNumMergedRegions();
        for (int i = 0; i < num; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            if (firstRow < rowIndex && lastRow >= rowIndex) {
                if (firstColumn <= colIndex && lastColumn >= colIndex) {
                    result = true;
                }
            }
        }
        return result;
    }



    public static void addImageByPOICell(PdfPCell pdfpCell, Cell cell, float cellWidth)
            throws BadElementException, MalformedURLException, IOException {
        POIImage poiImage = new POIImage().getCellImage(cell);
        byte[] bytes = poiImage.getBytes();
        if (bytes != null) {
            Image image = Image.getInstance(bytes);
            image.setAlignment(Image.ALIGN_CENTER);
            image.scalePercent(150); //依照比例缩放
            image.setAbsolutePosition(80,90);
            pdfpCell.setImage(image);
        }
    }
}
