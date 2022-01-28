package com.topideal.common.tools.excelReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 大数据Excel解析抽象类，分别继承2007处理类和实现2003处理监听器
 * @author Gy
 *
 */
public abstract class ExcelReader extends DefaultHandler implements HSSFListener {

	/**
     * 根据文件路径解析单页sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processSingleSheet(String filename) throws Exception ;
    
    /**
     * 根据文件流解析单页sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processSingleSheet(InputStream is) throws Exception ;
    
    /**
     * 根据文件路径解析所有sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<List<Map<String, String>>> processMultiSheet(String filename) throws Exception ;
    
    /**
     * 根据文件流解析所有sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<List<Map<String, String>>> processMultiSheet(InputStream is) throws Exception ;

    /**
     * 根据文件路径解析，指定sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processAppointSheet(String filename, Integer appointSheetIndex) throws Exception ;
    
    /**
     * 根据文件流解析，指定sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processAppointSheet(InputStream is, Integer appointSheetIndex) throws Exception ;
    
    /**
     * 根据文件路径解析，指定sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processAppointSheet(String filename, String appointSheetName) throws Exception ;
    
    /**
     * 根据文件流解析，指定sheet
     * @param filename
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, String>> processAppointSheet(InputStream is, String appointSheetName) throws Exception ;
	
}
