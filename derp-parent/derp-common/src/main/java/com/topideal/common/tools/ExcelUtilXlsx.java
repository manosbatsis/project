package com.topideal.common.tools;


import com.topideal.common.tools.excel.ExcelExportXlsx;
import com.topideal.common.tools.excel.ExcelReaderXlsx;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Excel工具类2007 大数据模式导出、导入
 */
public class ExcelUtilXlsx extends ExcelExportXlsx {

    /*****************************大数据模式导入start****************************/
    /**
     * 默认解析Excel表中的第一个sheet
     * @param inputStream  文件流
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetOne(InputStream inputStream)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processSingleSheet(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * 默认解析Excel表中的第一个sheet
     * @param filePath  文件路径
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetOne(String filePath)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processSingleSheet(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 解析Excel表中指定下标的sheet
     * @param inputStream  文件流
     * @param indexSheet  sheet下标
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetByIndex(InputStream inputStream,Integer indexSheet)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processAppointSheet(inputStream,indexSheet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * 解析Excel表中指定下标的sheet
     * @param  filePath 文件路径
     * @param indexSheet  sheet下标
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetByIndex(String filePath,Integer indexSheet)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processAppointSheet(filePath, indexSheet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }
    /**
     * 解析Excel表中指定名称的sheet
     * @param inputStream  文件流
     * @param sheetName  sheet名称
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetBySheetName(InputStream inputStream,String sheetName)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processAppointSheet(inputStream,sheetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * 解析Excel表中指定名称的sheet
     * @param filePath  文件路径
     * @param sheetName  sheet名称
     * @return      数据集
     * @throws IOException
     */
    public static List<Map<String, String>> parseSheetBySheetName(String filePath,String sheetName)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<Map<String, String>> dataList = null;
        try {
            dataList = excelReader.processAppointSheet(filePath,sheetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 解析Excel所有sheet
     * @param inputStream  文件流
     * @return      数据集
     * @throws IOException
     */
    public static List<List<Map<String, String>>> parseAllSheet(InputStream inputStream)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<List<Map<String, String>>> dataList = null;
        try {
            dataList = excelReader.processMultiSheet(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * 解析Excel所有sheet
     * @param filePath  文件路径
     * @return      数据集
     * @throws IOException
     */
    public static List<List<Map<String, String>>> parseAllSheet(String filePath)  {
        ExcelReaderXlsx excelReader = new ExcelReaderXlsx();
        List<List<Map<String, String>>> dataList = null;
        try {
            dataList = excelReader.processMultiSheet(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 合并单元格
     * @param sheet     所在的sheet
     * @param startRow  开始行
     * @param endRow    结束行
     * @param startCell 开始列
     * @param endCell   结束列
     */
    public static void mergeCells(Sheet sheet, int startRow, int endRow, int startCell, int endCell){
        sheet.addMergedRegion(new CellRangeAddress(startRow,endRow,startCell,endCell));
    }

    /**
     * 合并单元格对象
     *
     * @param firstRow 合并起始行
     * @param lastRow  合并结束行
     * @param firstCol 合并开始列
     * @param lastCol  合并结束列
     * @param sheet    sheet页对象
     */
    protected static void mergeCell(int firstRow, int lastRow, int firstCol, int lastCol, Sheet sheet) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);

        BorderStyle borderStyle = BorderStyle.valueOf(BorderStyle.THIN.getCode());
        RegionUtil.setBorderTop(borderStyle, cellRangeAddress, sheet);
        RegionUtil.setBorderBottom(borderStyle, cellRangeAddress, sheet);
        RegionUtil.setBorderLeft(borderStyle, cellRangeAddress, sheet);
        RegionUtil.setBorderRight(borderStyle, cellRangeAddress, sheet);

        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.index, cellRangeAddress, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.index, cellRangeAddress, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.index, cellRangeAddress, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.index, cellRangeAddress, sheet);
        sheet.addMergedRegion(cellRangeAddress);
    }


    /**
     * 合并单元格并填入值
     *
     * @param row         行对象
     * @param columnIndex 列索引
     * @param cellStyle   单元格样式
     * @param cellValue   单元格值
     * @param mergeSize   合并大小
     * @param sheet       sheet对象实例
     */
    private static void cellValueFiller(Row row, int columnIndex, CellStyle cellStyle, String cellValue, int mergeSize, Sheet sheet) {
        Cell currentCell = row.createCell(columnIndex);
        currentCell.setCellStyle(cellStyle);
        currentCell.setCellValue(cellValue);
        int rowNum = row.getRowNum();
        mergeCell(rowNum, rowNum + mergeSize - 1, columnIndex, columnIndex, sheet);
    }

    /*****************************大数据模式导入end****************************/

    /*****************************大数据模式导出在继承类ExcelExportXlsx****************************/


    public static SXSSFWorkbook writeExcelWithCollap(SXSSFWorkbook workbook, String sheetName, String[] columns, String[] keys,
                                            List<Map<String, Object>> mapList,
                                            List<CellRangeAddress> regionsData)throws IOException {
//        SXSSFWorkbook workbook = new SXSSFWorkbook();
//        Sheet excel = workbook.createSheet(sheetName);
        Sheet excel = workbook.getSheet(sheetName);
//        Row headrow = excel.createRow(0);
//        Row secondRow = excel.createRow(1);

        // 设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)10);//设置字体大小

        CellStyle cellStyle = workbook.createCellStyle();// 设置表头
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setFont(font);


        CellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
        textCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
        textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        textCellStyle.setFont(font);

        CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
        numCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
        numCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        numCellStyle.setFont(font);

        int i = 0;
//        for (String title : columns) { // 设置表头
//            Cell cell = headrow.createCell(i);
//            excel.setColumnWidth(i, 20 * 256);
//            cell.setCellStyle(cellStyle);
//            cell.setCellValue(title);
//            i++;
//        }

        // 合并单元格
        for (CellRangeAddress cellRangeAddress : regionsData) {
            excel.addMergedRegion(cellRangeAddress);
        }

        // 注入参数
        for (int j = 0; j < mapList.size(); j++) { // 填值
            Map model = mapList.get(j);
            Row row = excel.createRow(j + 2);
            i = 0;
            for (String key : keys) {
                Cell cell = row.createCell(i);
                Object obj = model.get(key);
                if (model.get(key) instanceof Timestamp) {
                    String value1 = model.get(key).toString();
                    String intNumber = value1.substring(0, value1.indexOf("."));
                    cell.setCellValue(intNumber);
                    cell.setCellStyle(textCellStyle);
                } else if (model.get(key) instanceof BigDecimal) {
                    BigDecimal value1 = new BigDecimal(model.get(key).toString());
                    BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
                    cell.setCellValue(value2.toString());
                    cell.setCellStyle(numCellStyle);
                } else if(model.get(key) instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
                    String date = sdf.format((Date)model.get(key));
                    cell.setCellValue(date);
                }else if (obj instanceof Long ||obj instanceof Double || obj instanceof Integer) {
                    cell.setCellValue(obj != null ? obj.toString() : "");
                    cell.setCellStyle(numCellStyle);
                } else {
                    cell.setCellValue(obj != null ? obj.toString() : "");
                    cell.setCellStyle(textCellStyle);
                }
                i++;
            }
        }
        return workbook;
    }
}