package com.topideal.common.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel工具类 功能：提供各种Excel操作的方法
 */

public class ExcelUtil {


    /**
     * 解析Excel表中的数据   多sheet(忽略空行空列)
     * @param inputStream  文件流
     * @param fileName     文件名称
     * @param sheetSize     sheet个数
     * @return      数据集
     * @throws IOException
     */
    public static Map<Integer,List<List<Object>>> parseExcel(InputStream inputStream,String fileName,int sheetSize)throws IOException{
        Map<Integer,List<List<Object>>> datas=new HashMap<Integer,List<List<Object>>>();
        boolean isExcel2017=getIsE2007(fileName);
        Workbook workBook=getExcelWorkbook(inputStream,isExcel2017);
        for (int i = 0; i < sheetSize; i++) {
            Sheet sheet=getExcelSheet(workBook,i);
            datas.put(i,getAllExcelCellValues(sheet));
        }
        return datas;
    }

    
    
    /**
     * 解析Excel表中的数据   多sheet(不忽略空行空列)
     * @param inputStream  文件流
     * @param fileName     文件名称
     * @param sheetSize     sheet个数
     * @return      数据集
     * @throws IOException
     */
    public static Map<Integer,List<List<Object>>> parseExcelIncludeNull(InputStream inputStream,String fileName,int sheetSize)throws IOException{
        Map<Integer,List<List<Object>>> datas=new HashMap<Integer,List<List<Object>>>();
        boolean isExcel2017=getIsE2007(fileName);
        Workbook workBook=getExcelWorkbook(inputStream,isExcel2017);
        for (int i = 0; i < sheetSize; i++) {
            Sheet sheet=getExcelSheet(workBook,i);
            datas.put(i,getAllExcelCellValuesByFor(sheet));
        }
        return datas;
    }
    
    /**
     * 解析Excel表中的数据
     * @param inputStream 文件流
     * @param fileName  文件名称
     * @return
     * @throws IOException
     */
    public static List<List<Object>> parseExcel(InputStream inputStream,String fileName)throws IOException{
        return parseExcel(inputStream,fileName,1).get(0);
    }

    /**
     *  获取 工作簿的所有内容
     * @param sheet 工作簿(Iterator 会忽略空行空列)
     * @return
     */
    public static List<List<Object>> getAllExcelCellValues(Sheet sheet){
        if(sheet == null) return null;

        Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
        if(rows == null) return null;

        List<List<Object>> rowList = new ArrayList<List<Object>>();
        while (rows.hasNext()) {
            Row row = rows.next();  //获得行数据
            if(row == null){
                rowList.add(null);
                continue;
            }
            //获得行号从0开始,获得第一行的迭代器
            Iterator<Cell> cells = row.cellIterator();
            if(cells == null){
                rowList.add(null);
                continue;
            }

            List<Object> colList = new ArrayList<Object>();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if(cell == null){
                    colList.add(null);
                    continue;
                }
//                int columnIndex = cell.getColumnIndex();

                switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                    case NUMERIC:
                        colList.add(cell.getNumericCellValue());
                        break;
                    case STRING:
                        colList.add(cell.getStringCellValue());
                        break;
                    case BOOLEAN:
                        colList.add(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        colList.add(cell.getCellFormula());
                        break;
                    default:
                        colList.add(null);
                        break;
                }
            }
            rowList.add(colList);
        }
        return rowList;
    }

    /**
     * 获取 工作簿的所有内容
     * 优化POI读取 CELL_TYPE_NUMERIC 出现附加.0的问题
     * @param sheet 工作簿(for基本读取 不忽略空行空列)
     * @return
     */
    public static List<List<Object>> getAllExcelCellValuesByFor(Sheet sheet){
        if(sheet == null) return null;

        List<List<Object>> rowList = new ArrayList<List<Object>>();

        // Decide which rows to process
        int rowStart = sheet.getFirstRowNum();

        int rowCount = sheet.getPhysicalNumberOfRows();
        int rowEnd = sheet.getLastRowNum()+1; //getLastRowNum() 存在文件删除数据后 没有正确清理 下标在删除后数据没有发生改变  带出许多空白行

        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);

            if(r == null) {
            	continue ;
            }
            
            int lastColumn = Math.max(r.getLastCellNum(), 62);

            List<Object> colList = new ArrayList<Object>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (c == null) {
                    colList.add("");
                } else {

                    switch (c.getCellTypeEnum()) {   //根据cell中的类型来输出数据
                        case NUMERIC:
                        	
                        	 if(HSSFDateUtil.isCellDateFormatted(c)){
                             	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
                             	colList.add(sdf.format(c.getDateCellValue()));
                             	break ;
                         	}
                        	 
                            long longVal = Math.round(c.getNumericCellValue());
                            if (Double.parseDouble(longVal + ".0") == c.getNumericCellValue()) {
                                colList.add(longVal);
                            }else {
                                colList.add(c.getNumericCellValue());
                        	}
                            break;
                        case STRING:
                            colList.add(c.getStringCellValue().trim());
                            break;
                        case BOOLEAN:
                            colList.add(c.getBooleanCellValue());
                            break;
                        case FORMULA:
                            colList.add(c.getCellFormula().trim());
                            break;
                        default:
                            colList.add("");
                            break;
                    }
                }
            }
            rowList.add(colList);
        }
        return rowList;
    }

    /**
     *  获取Excel 的工作簿
     * @param workbook
     * @param index 第几个工作簿，从0开始
     * @return
     */
    public static Sheet getExcelSheet(Workbook workbook, int index){
        if(workbook==null) return null;
        int numberOfSheets = workbook.getNumberOfSheets();
        if(index >= numberOfSheets) return null;

        return workbook.getSheetAt(index);
    }

    /**
     *  获取Excel 的工作簿
     * @param workbook
     * @param sheetName 工作簿名称
     * @return
     */
    public static Sheet getExcelSheet(Workbook workbook, String sheetName){
        if(workbook==null ) return null;
        return workbook.getSheet(sheetName);
    }

    /**
     *  获取 Excel的Workbook工作簿
     *  注：最后记得 excelWorkbook.close();
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static Workbook getExcelWorkbook(String fileName) throws IOException {
        if(StringUtils.isBlank(fileName)) return null;

        File file = new File(fileName);
        if(!file.exists()) return null;

        InputStream input = new FileInputStream(file);  //建立输入流
        boolean isE2007 = getIsE2007(fileName);    //判断是否是excel2007格式

        return getExcelWorkbook(input, isE2007);
    }

    /**
     *  获取 Excel的Workbook工作簿
     *  注：最后记得 excelWorkbook.close();
     * @param input
     * @param isE2007 是否是Excel2007以上的版本，文件名以xlsx结尾
     * @return
     * @throws IOException
     */
    public static Workbook getExcelWorkbook(InputStream input, boolean isE2007) throws IOException {
        if(input==null) return null;

        Workbook wb  = null;
        //根据文件格式(2003或者2007)来初始化
        if(isE2007){
            wb = new XSSFWorkbook(input);
        }
        else{
            wb = new HSSFWorkbook(input);
        }
        return wb;
    }

    /**
     * 判断是否是excel2007格式
     * @param fileName 文件名
     * @return
     */
    public static boolean getIsE2007(String fileName){
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx")){
            isE2007 = true;
        }
        return isE2007;
    }
    
}