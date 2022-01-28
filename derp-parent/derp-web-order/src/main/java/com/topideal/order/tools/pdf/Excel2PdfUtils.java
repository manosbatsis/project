package com.topideal.order.tools.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: excel转pdf
 **/
public class Excel2PdfUtils {

    private static BaseFont BASE_FONT_CHINESE;
    static {
        try {
            BASE_FONT_CHINESE = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            FontFactory.registerDirectories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Excel2Pdf(Workbook workbook, OutputStream os, boolean autoPageNum, Rectangle pageSize) throws Exception {

        Document document = new Document(pageSize);

        PdfWriter writer = PdfWriter.getInstance(document, os);

        //设置页码
        if (autoPageNum) {
            PdfPageHelper pdfPageHelper = new PdfPageHelper();
            writer.setPageEvent(pdfPageHelper);
        }

        document.open();
        // 单个sheet
        if (workbook.getNumberOfSheets() <= 1) {
            PdfPTable table = toCreatePdfTable(workbook, 0);
            document.add(table);
        }
        // 多个sheet
        if (workbook.getNumberOfSheets() > 1) {

            toCreateContentIndexes(writer, document, workbook);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                PdfPTable table = toCreatePdfTable(workbook, i);
                document.add(table);
                document.newPage();
            }
        }

        document.close();
    }


    /**
     * 创建pdf 表格
     */
    private static PdfPTable toCreatePdfTable(Workbook object, int sheetIndex)
            throws Exception {

        PdfPTable table = toParseContent(object, sheetIndex);
        table.setKeepTogether(true);

        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        // 表格置中
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        return table;
    }

    /**
     * 内容索引创建
     */
    private static void toCreateContentIndexes(PdfWriter writer, Document document, Workbook workbook)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setKeepTogether(true);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        Font font = new Font(BASE_FONT_CHINESE, 12, Font.NORMAL);
        font.setColor(new BaseColor(0, 0, 255));

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            PdfPCell cell = new PdfPCell();
            cell.setBorder(0);
            table.addCell(cell);
        }
        document.add(table);
    }

    /**
     * excel的值插入pdf
     */
    private static PdfPTable toParseContent(Workbook workbook, int sheetIndex) throws Exception {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        int rowlength = sheet.getLastRowNum() + 1;
        List<PdfPCell> cells = new ArrayList<PdfPCell>();
        float mw = 0;
        float[] widths = null;
        List<Float> columnWidths = new ArrayList<Float>();

        for (int i = 0; i < 1000; i++) {
            columnWidths.add(0f);
        }

        int columnCount = 0;

        for (int i = 0; i < rowlength; i++) {
            Row row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum < 0) {
                continue;
            }
            float[] cws = new float[lastCellNum];
            int colCount = row.getLastCellNum();

            int rowAllCols = 0;
            for (int j = 0; j < colCount; j++) {

                if (colCount > columnCount) {
                    columnCount = colCount;
                }

                int rowspan = 1;
                int colspan = 1;
                // PDF单元格
                PdfPCell pdfpCell = new PdfPCell();
                Cell cell = row.getCell(j);
                if (cell == null) {
                    PdfPCell nullCell = new PdfPCell();
                    nullCell.setColspan(colspan);
                    nullCell.setRowspan(1);
                    nullCell.disableBorderSide(-1);
                    cells.add(nullCell);
                    j += colspan - 1;
                    rowAllCols += colspan;
                    continue;
                }

                float cw = PoiUtil.getPOIColumnWidth(sheet, cell);
                cws[cell.getColumnIndex()] = cw;
                columnWidths.set(cell.getColumnIndex(), cw);
                if (PoiUtil.isUsed(sheet, cell.getColumnIndex(), row.getRowNum())) {
                    continue;
                }
                CellRangeAddress range = PoiUtil.getColspanRowspanByExcel(sheet, row.getRowNum(), cell.getColumnIndex());

                if (range != null) {
                    rowspan = range.getLastRow() - range.getFirstRow() + 1;
                    colspan = range.getLastColumn() - range.getFirstColumn() + 1;
                }


                int[] rgb = PoiUtil.getBackgroundColor(cell.getCellStyle());
                pdfpCell.setBackgroundColor(new BaseColor(rgb[0],rgb[1],rgb[2]));
                pdfpCell.setColspan(colspan);
                pdfpCell.setRowspan(rowspan);
                pdfpCell.setVerticalAlignment(PoiUtil.getVAlignByExcel(cell.getCellStyle().getVerticalAlignment().getCode()));
                pdfpCell.setHorizontalAlignment(PoiUtil.getHAlignByExcel(cell.getCellStyle().getAlignment().getCode()));
                pdfpCell.setPhrase(PoiUtil.getPhrase(workbook, cell));
                pdfpCell.setMinimumHeight(PoiUtil.getPixelHeight(row.getHeightInPoints()));

                // 不自动换行
                pdfpCell.setNoWrap(false);
                // 保证字体不贴在边缘
                pdfpCell.setPaddingBottom(3);

                //表格边框
                PoiUtil.addBorderByExcel(workbook, pdfpCell, cell.getCellStyle());

                //图片设置
                PoiUtil.addImageByPOICell(pdfpCell, cell, cw);

                cells.add(pdfpCell);
                j += colspan - 1;
                rowAllCols += colspan;
            }

            int emptyColumns = columnCount - colCount;
            if (emptyColumns > 0 && rowAllCols != columnCount) {
                PdfPCell pdfpCell = new PdfPCell();
                pdfpCell.setColspan(emptyColumns);
                pdfpCell.setRowspan(1);
                pdfpCell.disableBorderSide(-1);
                cells.add(pdfpCell);
            }
            float rw = 0;
            for (int j = 0; j < cws.length; j++) {
                rw += cws[j];
            }
            if (rw > mw || mw == 0) {
                widths = cws;
                mw = rw;
            }
        }

        PdfPTable table = new PdfPTable(columnCount);

        float[] colWidths = new float[columnCount];
        for (int i = 0; i < columnCount; i++) {
            colWidths[i] = columnWidths.get(i);
        }

        table.setWidths(colWidths);
        table.setWidthPercentage(100);

        for (PdfPCell pdfpCell : cells) {
            table.addCell(pdfpCell);
        }

        float heights = table.getTotalHeight();
        return table;
    }

}
