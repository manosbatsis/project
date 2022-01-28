package com.topideal.common.tools.excel;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出2007
 * */
public class ExcelExportXlsx {

    /**导出单个sheet Excel2007
     * @desc: 通过对象列表导出
     */
    public static SXSSFWorkbook createSXSSExcelByObjList(String sheetName, String[] columns,
                                                         String[] keys, List listObj) throws Exception {
        List<Map<String , Object>> mapList = new ArrayList<>() ;
        for (Object object : listObj) {
            Map<String , Object> map = entityToMap(object) ;
            mapList.add(map) ;
        }
        return createSXSSExcel(sheetName, columns, keys, mapList) ;
    }
    
    /**导出单个sheet Excel2007
     * 保留8位小数
     * @desc: 通过对象列表导出
     */
    public static SXSSFWorkbook createSXSSExcelByObjList8(String sheetName, String[] columns,
                                                         String[] keys, List listObj) throws Exception {
        List<Map<String , Object>> mapList = new ArrayList<>() ;
        for (Object object : listObj) {
            Map<String , Object> map = entityToMap(object) ;
            mapList.add(map) ;
        }
        return createSXSSExcel8(sheetName, columns, keys, mapList) ;
    }
    /**
     * 导出单个sheet Excel2007
     * 通过map列表导出
     * */
    public static SXSSFWorkbook createSXSSExcel(String sheetName, String[] columns, String[] keys,
                                                List<Map<String, Object>> mapList) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet excel = workbook.createSheet(sheetName);
        Row headrow = excel.createRow(0);

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
        for (String title : columns) { // 设置表头
            Cell cell = headrow.createCell(i);
            excel.setColumnWidth(i, 20 * 256);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title);
            i++;
        }
        // 注入参数
        for (int j = 0; j < mapList.size(); j++) { // 填值
            Map model = mapList.get(j);
            Row row = excel.createRow(j + 1);
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
    
    /**
     * 导出单个sheet Excel2007
     * 保留8位小数
     * 通过map列表导出
     * */
    public static SXSSFWorkbook createSXSSExcel8(String sheetName, String[] columns, String[] keys,
                                                List<Map<String, Object>> mapList) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet excel = workbook.createSheet(sheetName);
        Row headrow = excel.createRow(0);

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
        textCellStyle.setAlignment(HorizontalAlignment.LEFT);// 水平居左
        textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        textCellStyle.setFont(font);

        CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
        numCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
        numCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        numCellStyle.setFont(font);

        int i = 0;
        for (String title : columns) { // 设置表头
            Cell cell = headrow.createCell(i);
            excel.setColumnWidth(i, 20 * 256);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title);
            i++;
        }
        // 注入参数
        for (int j = 0; j < mapList.size(); j++) { // 填值
            Map model = mapList.get(j);
            Row row = excel.createRow(j + 1);
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
                    BigDecimal value2 = value1.setScale(8, BigDecimal.ROUND_HALF_UP);
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
    /**
     * 导出结果为多个sheet实体对象列表
     * @param sheetList
     * @return
     */
    public static SXSSFWorkbook createMutiSheetExcel(List<ExportExcelSheet> sheetList) {
        // 创建一个webbook，对应一个Excel文件
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        for (ExportExcelSheet exportExcelSheet : sheetList) {

            String sheetName = exportExcelSheet.getSheetNames();
            String[] columns = exportExcelSheet.getColums() ;
            String[] keys = exportExcelSheet.getKeys();
            List resultList = exportExcelSheet.getResultList();
            //创建sheet
            Sheet sheet = workbook.createSheet(sheetName);

            Row headrow = sheet.createRow(0);
            CellStyle cellStyle = workbook.createCellStyle();// 设置表头
            cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框

            CellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
            textCellStyle.setAlignment(HorizontalAlignment.LEFT);// 水平居左
            textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中

            CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
            numCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
            numCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
            // 设置字体
            Font font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)10);//设置字体大小
            cellStyle.setFont(font);
            textCellStyle.setFont(font);
            numCellStyle.setFont(font);
            int i = 0;
            for (String title : columns) { // 设置表头
                Cell cell = headrow.createCell(i);
                sheet.setColumnWidth(i, 20 * 256);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(title);
                i++;
            }

            for (int j = 0; j < resultList.size(); j++) { // 填值
                Map model = null ;
                Object returnObj = resultList.get(j);
                if(returnObj instanceof Map){
                    model = (Map)returnObj ;
                }else {
                    model = entityToMap(returnObj);
                }

                Row row = sheet.createRow(j + 1);
                i = 0;
                for (String key : keys) {
                    Cell cell = row.createCell(i);
                    Object obj = model.get(key);
                    if (obj instanceof Timestamp) {
                        String value1 = obj.toString();
                        String intNumber = value1.substring(0, value1.indexOf("."));
                        cell.setCellValue(intNumber);
                        cell.setCellStyle(textCellStyle);
                    } else if (obj instanceof BigDecimal) {
                        BigDecimal value1 = new BigDecimal(obj.toString());
                        BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
                        cell.setCellValue(value2.toString());
                        cell.setCellStyle(numCellStyle);
                    } else if(obj instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
                        String date = sdf.format((Date)obj) ;
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
        }
        return workbook;
    }
    /**
     * 导出结果为多个sheet实体对象列表 保留8位小数
     * @param sheetList
     * @return
     */
    public static SXSSFWorkbook createMutiSheetExcel8(List<ExportExcelSheet> sheetList) {
        // 创建一个webbook，对应一个Excel文件
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        for (ExportExcelSheet exportExcelSheet : sheetList) {

            String sheetName = exportExcelSheet.getSheetNames();
            String[] columns = exportExcelSheet.getColums() ;
            String[] keys = exportExcelSheet.getKeys();
            List resultList = exportExcelSheet.getResultList();
            //创建sheet
            Sheet sheet = workbook.createSheet(sheetName);

            Row headrow = sheet.createRow(0);
            CellStyle cellStyle = workbook.createCellStyle();// 设置表头
            cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框

            CellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
            textCellStyle.setAlignment(HorizontalAlignment.LEFT);// 水平居左
            textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中

            CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
            numCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
            numCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
            // 设置字体
            Font font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)10);//设置字体大小
            cellStyle.setFont(font);
            textCellStyle.setFont(font);
            numCellStyle.setFont(font);
            int i = 0;
            for (String title : columns) { // 设置表头
                Cell cell = headrow.createCell(i);
                sheet.setColumnWidth(i, 20 * 256);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(title);
                i++;
            }

            for (int j = 0; j < resultList.size(); j++) { // 填值
                Map model = null ;
                Object returnObj = resultList.get(j);
                if(returnObj instanceof Map){
                    model = (Map)returnObj ;
                }else {
                    model = entityToMap(returnObj);
                }

                Row row = sheet.createRow(j + 1);
                i = 0;
                for (String key : keys) {
                    Cell cell = row.createCell(i);
                    Object obj = model.get(key);
                    if (obj instanceof Timestamp) {
                        String value1 = obj.toString();
                        String intNumber = value1.substring(0, value1.indexOf("."));
                        cell.setCellValue(intNumber);
                        cell.setCellStyle(textCellStyle);
                    } else if (obj instanceof BigDecimal) {
                        BigDecimal value1 = new BigDecimal(obj.toString());
                        BigDecimal value2 = value1.setScale(8, BigDecimal.ROUND_HALF_UP);
                        cell.setCellValue(value2.toPlainString());
                        cell.setCellStyle(numCellStyle);
                    } else if(obj instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
                        String date = sdf.format((Date)obj) ;
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
        }
        return workbook;
    }
    /***
     * 创建sheet实例
     * @param sheetName
     * @param colums
     * @param keys
     * @param resultList
     * @return
     */
    public static ExportExcelSheet createSheet(String sheetName , String[] colums , String[] keys , List resultList) {
        ExportExcelSheet exportExcelSheet = new ExportExcelSheet();
        exportExcelSheet.setSheetNames(sheetName);
        exportExcelSheet.setColums(colums);
        exportExcelSheet.setKeys(keys);
        exportExcelSheet.setResultList(resultList);
        return exportExcelSheet;
    }
    /**
     * 实体类转Map
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        Class<? extends Object> tempClass = object.getClass();
        while( tempClass != null) {
            for (Field field : tempClass.getDeclaredFields()){
                try {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object o = field.get(object);
                    map.put(field.getName(), o);
                    field.setAccessible(flag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            tempClass = tempClass.getSuperclass();
        }
        return map;
    }
}
