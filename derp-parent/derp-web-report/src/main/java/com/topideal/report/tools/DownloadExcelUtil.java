package com.topideal.report.tools;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.entity.vo.reporting.BuFinanceAddPurchaseNotshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddSaleNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddTransferNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;
import com.topideal.entity.vo.reporting.BuFinanceDestroyModel;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceTakesStockResultsModel;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;


/**
 * 子模块导出特殊excel工具类，导出通用型excel请使用ExcelUtilXlsx通用类
 */
public class DownloadExcelUtil {


	
	/**
	 * @desc: 导出excel 2007
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 */
	public static SXSSFWorkbook createBuExcelSXSS5(String sheetName1, String[] columns1, String[] keys1,List<Map<String, Object>> values1,
			String sheetName1A, String[] columns1A, String[] keys1A,List<Map<String, Object>> values1A,
			String sheetName2, String[] columns2, String[] keys2,List<Map<String, Object>> values2,
			String sheetName3, String[] columns3, String[] keys3,List<Map<String, Object>> values3,
			String sheetName3A, String[] columns3A, String[] keys3A,List<Map<String, Object>> values3A,
			String sheetName3B, String[] columns3B, String[] keys3B,List<Map<String, Object>> values3B,
			String sheetName4, String[] columns4, String[] keys4,List<Map<String, Object>> values4,
			String sheetName5, String[] columns5, String[] keys5,List<Map<String, Object>> values5,
			String sheetName5A, String[] columns5A, String[] keys5A,List<Map<String, Object>> values5A,
		    String sheetName6, String[] columns6, String[] keys6,List<Map<String, Object>> values6,
		    /*String sheetName7, String[] columns7, String[] keys7,List<Map<String, Object>> values7,*/
		    String sheetName8, String[] columns8, String[] keys8,List<Map<String, Object>> values8,
		    String sheetName9, String[] columns9, String[] keys9,List<Map<String, Object>> values9,
		   String sheetName10, String[] columns10, String[] keys10,List<Map<String, Object>> values10,
		   String sheetName11, String[] columns11, String[] keys11,List<Map<String, Object>> values11,
		   String sheetName12, String[] columns12, String[] keys12,List<Map<String, Object>> values12
			) {

		SXSSFWorkbook workbook = new SXSSFWorkbook (7000);
		Sheet excel = workbook.createSheet(sheetName1);
		Row headrow = excel.createRow(0);
		// 字体
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置 内容样式
		CellStyle textCellStyle = setBodyCellStyle(workbook, font);
		
		int i = 0;
		for (String title : columns1) { // 设置表头
			Cell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j = 0; j < values1.size(); j++) { // 填值
			Map model = values1.get(j);
			Row row = excel.createRow(j + 1);
			i = 0;
			for (String key : keys1) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		
		// sheet1A
		Sheet excel1A = workbook.createSheet(sheetName1A);
		Row headrow1A = excel1A.createRow(0);
		int k1A = 0;
		for (String title : columns1A) { // 设置表头
			Cell cell = headrow1A.createCell(k1A);
			excel1A.setColumnWidth(k1A, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k1A++;
		}
		// 注入参数
		for (int j1A= 0; j1A < values1A.size(); j1A++) { // 填值
			Map model = values1A.get(j1A);
			Row row = excel1A.createRow(j1A + 1);
			k1A = 0;
			for (String key : keys1A) {
				Cell cell = row.createCell(k1A);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k1A++;
			}
		}
		
		
		
		// sheet2
		Sheet excel2 = workbook.createSheet(sheetName2);
		Row headrow2 = excel2.createRow(0);
		int k = 0;
		for (String title : columns2) { // 设置表头
			Cell cell = headrow2.createCell(k);
			excel2.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j = 0; j < values2.size(); j++) { // 填值
			Map model = values2.get(j);
			Row row = excel2.createRow(j + 1);
			k = 0;
			for (String key : keys2) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		

		
		// sheet3
		Sheet excel3 = workbook.createSheet(sheetName3);
		Row headrow3 = excel3.createRow(0);
		int k3 = 0;
		for (String title : columns3) { // 设置表头
			Cell cell = headrow3.createCell(k3);
			excel3.setColumnWidth(k3, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k3++;
		}
		// 注入参数
		for (int j3 = 0; j3 < values3.size(); j3++) { // 填值
			Map model = values3.get(j3);
			Row row = excel3.createRow(j3 + 1);
			k = 0;
			for (String key : keys3) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}
		
		// sheet3A
		Sheet excel3A = workbook.createSheet(sheetName3A);
		Row headrow3A = excel3A.createRow(0);
		int k3A = 0;
		for (String title : columns3A) { // 设置表头
			Cell cell = headrow3A.createCell(k3A);
			excel3A.setColumnWidth(k3A, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k3A++;
		}
		// 注入参数
		for (int j3A = 0; j3A < values3A.size(); j3A++) { // 填值
			Map model = values3A.get(j3A);
			Row row = excel3A.createRow(j3A + 1);
			k = 0;
			for (String key : keys3A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}
		// sheet3B
		Sheet excel3B = workbook.createSheet(sheetName3B);
		Row headrow3B = excel3B.createRow(0);
		int k3B = 0;
		for (String title : columns3B) { // 设置表头
			Cell cell = headrow3B.createCell(k3B);
			excel3B.setColumnWidth(k3B, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k3B++;
		}
		// 注入参数
		for (int j3B = 0; j3B < values3B.size(); j3B++) { // 填值
			Map model = values3B.get(j3B);
			Row row = excel3B.createRow(j3B + 1);
			k = 0;
			for (String key : keys3B) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}
		
		//sheet4
		Sheet excel4 = workbook.createSheet(sheetName4);
		Row headrow4 = excel4.createRow(0);
		int k4 = 0;
		for (String title : columns4) { // 设置表头
			Cell cell = headrow4.createCell(k4);
			excel4.setColumnWidth(k4, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k4++;
		}
		// 注入参数
		for (int j4 = 0; j4 < values4.size(); j4++) { // 填值
			Map model = values4.get(j4);
			Row row = excel4.createRow(j4 + 1);
			k4 = 0;
			for (String key : keys4) {
				Cell cell = row.createCell(k4);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k4++;
			}
		}
		
		//sheet5
		Sheet excel5 = workbook.createSheet(sheetName5);
		Row headrow5 = excel5.createRow(0);
		int k5 = 0;
		for(String title : columns5) { // 设置表头
			Cell cell = headrow5.createCell(k5);
			excel5.setColumnWidth(k5, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k5++;
		}
		// 注入参数
		for(int j5 = 0; j5 < values5.size(); j5++) { // 填值
			Map model = values5.get(j5);
			Row row = excel5.createRow(j5 + 1);
			k5 = 0;
			for (String key : keys5) {
				Cell cell = row.createCell(k5);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k5++;
			}
		}
		//sheet5A
		Sheet excel5A = workbook.createSheet(sheetName5A);
		Row headrow5A = excel5A.createRow(0);
		int k5A = 0;
		for(String title : columns5A) { // 设置表头
			Cell cell = headrow5A.createCell(k5A);
			excel5A.setColumnWidth(k5A, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k5A++;
		}
		// 注入参数
		for(int j5A = 0; j5A < values5A.size(); j5A++) { // 填值
			Map model = values5A.get(j5A);
			Row row = excel5A.createRow(j5A + 1);
			k5A = 0;
			for (String key : keys5A) {
				Cell cell = row.createCell(k5A);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k5A++;
			}
		}
		

		//sheet6
		Sheet excel6 = workbook.createSheet(sheetName6);
		Row headrow6 = excel6.createRow(0);
		int k6 = 0;
		for(String title : columns6) { // 设置表头
			Cell cell = headrow6.createCell(k6);
			excel6.setColumnWidth(k6, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k6++;
		}
		// 注入参数
		for(int j6 = 0; j6 < values6.size(); j6++) { // 填值
			Map model = values6.get(j6);
			Row row = excel6.createRow(j6 + 1);
			k6 = 0;
			for (String key : keys6) {
				Cell cell = row.createCell(k6);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k6++;
			}
		}

		/*//sheet7
		Sheet excel7 = workbook.createSheet(sheetName7);
		Row headrow7 = excel7.createRow(0);
		int k7 = 0;
		for(String title : columns7) { // 设置表头
			Cell cell = headrow7.createCell(k7);
			excel7.setColumnWidth(k7, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k7++;
		}
		// 注入参数
		for(int j7 = 0; j7 < values7.size(); j7++) { // 填值
			Map model = values7.get(j7);
			Row row = excel7.createRow(j7 + 1);
			k7 = 0;
			for (String key : keys7) {
				Cell cell = row.createCell(k7);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k7++;
			}
		}*/

		//sheet8
		Sheet excel8 = workbook.createSheet(sheetName8);
		Row headrow8 = excel8.createRow(0);
		int k8 = 0;
		for(String title : columns8) { // 设置表头
			Cell cell = headrow8.createCell(k8);
			excel8.setColumnWidth(k8, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k8++;
		}
		// 注入参数
		for(int j8 = 0; j8 < values8.size(); j8++) { // 填值
			Map model = values8.get(j8);
			Row row = excel8.createRow(j8 + 1);
			k8 = 0;
			for (String key : keys8) {
				Cell cell = row.createCell(k8);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k8++;
			}
		}

		//sheet9
		Sheet excel9 = workbook.createSheet(sheetName9);
		Row headrow9 = excel9.createRow(0);
		int k9 = 0;
		for(String title : columns9) { // 设置表头
			Cell cell = headrow9.createCell(k9);
			excel9.setColumnWidth(k9, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k9++;
		}
		// 注入参数
		for(int j9 = 0; j9 < values9.size(); j9++) { // 填值
			Map model = values9.get(j9);
			Row row = excel9.createRow(j9 + 1);
			k9 = 0;
			for (String key : keys9) {
				Cell cell = row.createCell(k9);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k9++;
			}
		}
		
		
		//sheet10
		Sheet excel10 = workbook.createSheet(sheetName10);
		Row headrow10 = excel10.createRow(0);
		int k10 = 0;
		for(String title : columns10) { // 设置表头
			Cell cell = headrow10.createCell(k10);
			excel10.setColumnWidth(k10, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k10++;
		}
		// 注入参数
		for(int j10 = 0; j10 < values10.size(); j10++) { // 填值
			Map model = values10.get(j10);
			Row row = excel10.createRow(j10 + 1);
			k10 = 0;
			for (String key : keys10) {
				Cell cell = row.createCell(k10);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k10++;
			}
		}			

		//sheet11
		Sheet excel11 = workbook.createSheet(sheetName11);
		Row headrow11 = excel11.createRow(0);
		int k11 = 0;
		for(String title : columns11) { // 设置表头
			Cell cell = headrow11.createCell(k11);
			excel11.setColumnWidth(k11, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k11++;
		}
		// 注入参数
		for(int j11 = 0; j11 < values11.size(); j11++) { // 填值
			Map model = values11.get(j11);
			Row row = excel11.createRow(j11 + 1);
			k11 = 0;
			for (String key : keys11) {
				Cell cell = row.createCell(k11);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k11++;
			}
		}

		//sheet12
		Sheet excel12 = workbook.createSheet(sheetName12);
		Row headrow12 = excel12.createRow(0);
		int k12 = 0;
		for(String title : columns12) { // 设置表头
			Cell cell = headrow12.createCell(k12);
			excel12.setColumnWidth(k12, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k12++;
		}
		// 注入参数
		for(int j12 = 0; j12 < values12.size(); j12++) { // 填值
			Map model = values12.get(j12);
			Row row = excel12.createRow(j12 + 1);
			k12 = 0;
			for (String key : keys12) {
				Cell cell = row.createCell(k12);
				cell.setCellStyle(textCellStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k12++;
			}
		}		
		
		
		return workbook;
	}
	
	/**
	 * 此方法是传值传的是 整型 长整形 字符串
	 * 
	 * @desc: 设置单元格的值
	 * @param bodyRow
	 *            行号
	 * @param bodyStyle
	 *            单元样式
	 * @param num
	 *            单元格下标
	 * @param value
	 *            单元格的值
	 */
	private static void setValue2(Row bodyRow, CellStyle bodyStyle, int num, Object value) {
		Cell bodyCell = bodyRow.createCell(num);
		bodyCell.setCellStyle(bodyStyle);

		if (value == null) {
			String value1 = "";
			bodyCell.setCellValue(value1);
		}
		// 一下是判断传过来的value是什么类型
		// 字符串类型
		if (value instanceof String) {
			String value1 = (String) value;
			bodyCell.setCellValue(value1);
		}
		// 整形
		if (value instanceof Integer) {
			String value1 = value.toString();
			bodyCell.setCellValue(value1);
		}
		// 长整型
		if (value instanceof Long) {
			String value1 = value.toString();
			bodyCell.setCellValue(value1);
		}
		// 日期类型 这个导出的类型时日期类型 表格要进行设置成日期格式
		if (value instanceof Timestamp) {
			String value1 = value.toString();
			String intNumber = value1.substring(0, value1.indexOf("."));
			bodyCell.setCellValue(intNumber);
		}

		if (value instanceof BigDecimal) {
			//java.text.DecimalFormat df = new java.text.DecimalFormat();
			//String format = df.format(value);
			if (BigDecimal.ZERO.compareTo(new BigDecimal(value.toString()))==0) {
				value= new BigDecimal(0);
			}
			bodyCell.setCellValue(value.toString());
		}

		if (value instanceof Double) {
			String value1 = value.toString();
			bodyCell.setCellValue(value1);
		}
		if (value instanceof Date) {
			//format的格式可以任意  
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	        String value1 = sdf.format(value); 			
			bodyCell.setCellValue(value1);
		}

	}
	
	//删除目录下的所有子文件
	public static void deleteFile(String dir){
		File file = new File(dir);
		if(file.exists()){//目录存在
			//删除子文件
			File[] files = file.listFiles();
			if(files!=null&&files.length>0){
				for(File f:files){
					f.delete();
				}
			}
		}
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
		    
		    tempClass = tempClass.getSuperclass() ;
		}
	    return map;
	}

	/**
	 * 表头设置单元个样式
	 * @return
	 */
	public static CellStyle setHeadCellStyle(SXSSFWorkbook workbook,Font font,short index){
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用		
		style.setBorderBottom(BorderStyle.THIN); //下边框
		style.setBorderLeft(BorderStyle.THIN);//左边框
		style.setBorderTop(BorderStyle.THIN);//上边框
		style.setBorderRight(BorderStyle.THIN);//右边框
		style.setFont(font);
		style.setFillForegroundColor(index);// 设置背景色
		return style;
	}
	
	/**
	 * 设置表体样式
	 * @param workbook
	 * @param font
	 * @param index
	 * @return
	 */
	public static CellStyle setBodyCellStyle(SXSSFWorkbook workbook,Font font){
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}
	/**
	 * 生成财务经销存汇总和详情 
	 * @desc: 导出excel 2007
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 */
	public static SXSSFWorkbook createBuExcelSXSS6(String sheetName1, String[] columns1, String[] keys1,List<Map<String, Object>> values1,
			String sheetName1A, String[] columns1A, String[] keys1A,List<Map<String, Object>> values1A,
			String sheetName2, String[] columns2,String[] keys2, List<Map<String, Object>> purchaseSummaryList,String title2,
			String sheetName2A, String[] columns2A,String[] keys2A, List<Map<String, Object>> inventorySummaryListExportList,String title2A,
			String sheetName3, String[] columns3, List<BuFinanceWarehouseDetailsModel> warehouseDetailsList,
			String sheetName4, String[] columns4, List<BuFinanceAddPurchaseNotshelfDetailsModel> purchaseNotshelfList,
			String sheetName5, String[] columns5, String[] keys5, List<Map<String, Object>> inItemList,
			String sheetName5A,String[] columns5A, String[] keys5A,List<Map<String, Object>> saleShelfTKList,
			String sheetName6, String[] columns6, List<BuFinanceAddSaleNoshelfDetailsModel>  saleNotshelfList,
			String sheetName7, String[] columns7, List<BuFinancePurchaseDamagedModel> financePurchaseDamagedList,
			String sheetName8, String[] columns8, List<BuFinanceSaleDamagedModel>  saleDamagedList,
			String sheetName9, String[] columns9, List<BuFinanceTakesStockResultsModel> takesStockResultsList,
			String sheetName10,String[] columns10, List<BuFinanceDestroyModel> financeDestroyList,
			//String sheetName11,String[] columns11, List<FinanceAddPurchaseNotshelfSummaryModel> addPurchaseNotshelfSummaryList,
			String sheetName12,String[] columns12, List<BuFinanceAddPurchaseNotshelfDetailsModel> addPurchaseNotshelfDetailsList,
			String sheetName13,String[] columns13, List<BuFinanceAddSaleNoshelfDetailsModel>addSaleNoshelfDetailsList,
			//String sheetName14,String[] columns14,List<FinanceAddSaleNoshelfSummaryModel> addSaleNoshelfSummaryList,
			String sheetName15,String[] columns15,List<BuFinanceDecreaseSaleNoshelfModel> decreaseSaleNoshelfList,
			//String sheetName16,String[] columns16,List<BuFinanceDecreasePurchaseNotshelfModel> decreasePurchaseNotshelfList,
			String sheetName17,String[] columns17,List<BuFinanceAddTransferNoshelfDetailsModel> addTransferNoshelfList,			
			String sheetName17A,String[] columns17A,String[] keys17A,List<Map<String, Object>> saleChannelSummaryList,
			String sheetName18, String[] columns18, String[] keys18, List<Map<String, Object>> itemMoveList,
			String sheetName19, String[] columns19, String[] keys19, List<Map<String, Object>> fgInventList) {

		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		// 设置表头颜色
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式		
		CellStyle centerStyle = setBodyCellStyle(workbook, font);
		
		Sheet excelA = workbook.createSheet(sheetName1A);		
		Row  headrow0A =  excelA.createRow(0);		
		CellRangeAddress region1A = new CellRangeAddress(0, 0, (short) 0, (short) 5);
		CellRangeAddress region2A = new CellRangeAddress(0, 0, (short) 6, (short) 8);
		CellRangeAddress region3A = new CellRangeAddress(0, 0, (short) 9, (short) 13);
		//CellRangeAddress region4A = new CellRangeAddress(0, 0, (short) 15, (short) 16);
		CellRangeAddress region5A = new CellRangeAddress(0, 0, (short) 15, (short) 19);		
		CellRangeAddress region6A = new CellRangeAddress(0, 0, (short) 20, (short) 24);
		CellRangeAddress region7A = new CellRangeAddress(0, 0, (short) 25, (short) 26);
		CellRangeAddress region8A = new CellRangeAddress(0, 0, (short) 27, (short) 30);

		
		for (int iA = 0; iA < 30; iA++) {
			Cell cell = headrow0A.createCell(iA);

			if (iA>=0&&iA<=5) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.CORAL.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("商品信息");
			}else if (iA>5&&iA<=8) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LIGHT_TURQUOISE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期初");
			}else if (iA>8&&iA<=13) {	
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.YELLOW.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本月采购汇总");
			}else if (iA>13&&iA<=14) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.RED.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期调整单价");
			}else if (iA>=14&&iA<=19) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ORANGE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期销售汇总");
			}else if (iA>19&&iA<=24) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ROSE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期损益汇总");
			}else if (iA>24&&iA<=26) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LEMON_CHIFFON.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期末账存汇总");
			}else if (iA>26&&iA<=30) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.GOLD.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("累计在途汇总");
			}
		}
	     
        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
		excelA.addMergedRegion(region1A);
		excelA.addMergedRegion(region2A);
		excelA.addMergedRegion(region3A);
		//excelA.addMergedRegion(region4A);
		excelA.addMergedRegion(region5A);
		excelA.addMergedRegion(region6A);
		excelA.addMergedRegion(region7A);
		excelA.addMergedRegion(region8A);


		Row headrowA = excelA.createRow(1);
		int iA = 0;
		for (String title : columns1A) { // 设置表头
			Cell cell = headrowA.createCell(iA);
			excelA.setColumnWidth(iA, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			iA++;
		}
		// 注入参数
		for (int jA = 0; jA < values1A.size(); jA++) { // 填值
			Map model = values1A.get(jA);
			Row row = excelA.createRow(jA + 2);
			iA = 0;
			for (String key : keys1A) {
				Cell cell = row.createCell(iA);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				iA++;
			}
		}

	
		Sheet excel = workbook.createSheet(sheetName1);		
		Row  headrow0 =  excel.createRow(0);
		CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) 7);
		CellRangeAddress region2 = new CellRangeAddress(0, 0, (short) 8, (short) 10);
		CellRangeAddress region3 = new CellRangeAddress(0, 0, (short) 11, (short) 15);
		//CellRangeAddress region4 = new CellRangeAddress(0, 0, (short) 17, (short) 18);		
		CellRangeAddress region5 = new CellRangeAddress(0, 0, (short) 16, (short) 21);
		CellRangeAddress region6 = new CellRangeAddress(0, 0, (short) 22, (short) 26);
		CellRangeAddress region7 = new CellRangeAddress(0, 0, (short) 27, (short) 28);
		CellRangeAddress region8 = new CellRangeAddress(0, 0, (short) 29, (short) 32);

		
		for (int i = 0; i < 32; i++) {
			Cell cell = headrow0.createCell(i);

			if (i>=0&&i<=7) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.CORAL.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("商品信息");
			}else if (i>7&&i<=10) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LIGHT_TURQUOISE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期初");
			}else if (i>10&&i<=15) {	
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.YELLOW.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本月采购汇总");
			}else if (i>15&&i<=16) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.RED.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期调整单价");
			}else if (i>=16&&i<=21) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ORANGE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期销售汇总");
			}else if (i>21&&i<=26) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ROSE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期损益汇总");
			}else if (i>26&&i<=28) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LEMON_CHIFFON.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期末账存汇总");
			}else if (i>28&&i<=32) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.GOLD.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("累计在途汇总");
			}		
		}

        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
		excel.addMergedRegion(region1);
		excel.addMergedRegion(region2);
		excel.addMergedRegion(region3);
		//excel.addMergedRegion(region4);
		excel.addMergedRegion(region5);
		excel.addMergedRegion(region6);
		excel.addMergedRegion(region7);
		excel.addMergedRegion(region8);
				
		Row headrow = excel.createRow(1);
		int i = 0;
		for (String title : columns1) { // 设置表头
			Cell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j = 0; j < values1.size(); j++) { // 填值
			Map model = values1.get(j);
			Row row = excel.createRow(j + 2);
			i = 0;
			for (String key : keys1) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		values1=null;
					
		

		
		// sheet2				
		Sheet excel2 = workbook.createSheet(sheetName2);
		// 设置sheet第一行表头开始
		Row headrow2 = excel2.createRow(0);
		Cell cell2 = headrow2.createCell(0);
		excel2.setColumnWidth(0, 20 * 256);
		cell2.setCellStyle(cellStyle);
		cell2.setCellValue(title2);
		// 设置sheet第一行表头结束
		//创建第二行
		Row head2row2 = excel2.createRow(1);
		int k = 0;
		for (String title : columns2) { // 设置表头
			Cell cell = head2row2.createCell(k);
			excel2.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j2 = 0; j2 < purchaseSummaryList.size(); j2++) { // 填值
			Map model = purchaseSummaryList.get(j2);
			Row row = excel2.createRow(j2 + 2);
			k = 0;
			for (String key : keys2) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		purchaseSummaryList=null;
		
		
		// sheet2A				
		Sheet excel2A = workbook.createSheet(sheetName2A);
		// 设置sheet第一行表头开始
		Row headrow2A = excel2A.createRow(0);
		Cell cell2A = headrow2A.createCell(0);
		excel2A.setColumnWidth(0, 20 * 256);
		cell2A.setCellStyle(cellStyle);
		cell2A.setCellValue(title2A);
		// 设置sheet第一行表头结束
		//创建第二行
		Row head2row2A = excel2A.createRow(1);
		 k = 0;
		for (String title : columns2A) { // 设置表头
			Cell cell = head2row2A.createCell(k);
			excel2A.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j2A = 0; j2A < inventorySummaryListExportList.size(); j2A++) { // 填值
			Map model = inventorySummaryListExportList.get(j2A);
			Row row = excel2A.createRow(j2A + 2);
			k = 0;
			for (String key : keys2A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		inventorySummaryListExportList=null;
		
		
		// sheet3
		Sheet excel3 = workbook.createSheet(sheetName3);
		Row headrow3 = excel3.createRow(0);
		k = 0;
		for (String title : columns3) { // 设置表头
			Cell cell = headrow3.createCell(k);
			excel3.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		int rowNum = 0;
		Row bodyRow = null;
		// 循环表头入库单信息
		for (int j3 = 0; j3 < warehouseDetailsList.size(); j3++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceWarehouseDetailsModel model = warehouseDetailsList.get(j3);
			bodyRow = excel3.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getSupplierName());//供应商名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 6, model.getWarehouseCode());//入库单号
			String orderType = model.getOrderType();// 类型是采购退货 数量金额 负数显示
			String orderTypeName = null;
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderTypeName = DERP.getLabelByKey(DERP_REPORT.financeWarehouseDetails_orderTypeList, model.getOrderType());
			}
			setValue2(bodyRow, centerStyle, 7, orderTypeName);//单据类型
			setValue2(bodyRow, centerStyle, 8, model.getWarehouseCreateDate());//入库时间
			setValue2(bodyRow, centerStyle, 9, model.getRelDate());//勾稽时间
			setValue2(bodyRow, centerStyle, 10, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 11, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 12, model.getInvoiceNo());//发票号码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 14, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 15, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}			
			setValue2(bodyRow, centerStyle, 16, tallyingUnit);//单位
			Integer warehouseNum = model.getWarehouseNum();
			BigDecimal orderAmount = model.getOrderAmount();
			BigDecimal warehouseAmount = model.getWarehouseAmount();
			if (DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_2.equals(orderType)) {// 采购退货负数显示
				if (warehouseNum!=null)warehouseNum=warehouseNum *-1;
				if (orderAmount!=null)orderAmount=orderAmount.multiply(new BigDecimal("-1"));
				if (warehouseAmount!=null)warehouseAmount=warehouseAmount.multiply(new BigDecimal("-1")); 				
			}
			setValue2(bodyRow, centerStyle, 17, warehouseNum);//数量
			String orderCurrency ="";
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 18, orderCurrency);//采购币种
			setValue2(bodyRow, centerStyle, 19, model.getOrderPrice());//采购单价
			setValue2(bodyRow, centerStyle, 20, orderAmount);//采购金额	
			String  warehouseCurrency="";
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}
			setValue2(bodyRow, centerStyle, 21, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 22, model.getWarehousePrice());//入库单价
			setValue2(bodyRow, centerStyle, 23, warehouseAmount);//入库金额
			setValue2(bodyRow, centerStyle, 24, model.getInDepotName());//入库仓库		
			setValue2(bodyRow, centerStyle, 25, model.getCreateName());//创建人名称	
				
			// 记录行数
			rowNum += 1;
		}						
		
		warehouseDetailsList=null;
		
		// sheet4
		Sheet excel4 = workbook.createSheet(sheetName4);
		Row headrow4 = excel4.createRow(0);
		k = 0;
		for (String title : columns4) { // 设置表头
			Cell cell = headrow4.createCell(k);
			excel4.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j4 = 0; j4 < purchaseNotshelfList.size(); j4++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddPurchaseNotshelfDetailsModel model = purchaseNotshelfList.get(j4);
			bodyRow = excel4.createRow(rowNum);
        	
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//供应商名称
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getSupplierName());//供应商名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 6, model.getEndDate());//订单完结时间
			setValue2(bodyRow, centerStyle, 7, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 9, model.getDrawInvoiceDate());//在途开始日期
			setValue2(bodyRow, centerStyle, 10, model.getInvoiceNo());//发票号码
			setValue2(bodyRow, centerStyle, 11, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 14, tallyingUnit);//单位			
			setValue2(bodyRow, centerStyle, 15, model.getNum());//数量
			String orderCurrency ="";
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 16, orderCurrency);//采购币种		
			setValue2(bodyRow, centerStyle, 17, model.getOrderPrice());//采购币种	
			setValue2(bodyRow, centerStyle, 18, model.getOrderAmount());//采购金额
			//采购币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
			String warehouseCurrency ="";
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}			
			setValue2(bodyRow, centerStyle, 19, warehouseCurrency);//入库币种?????? 
			setValue2(bodyRow, centerStyle, 20, model.getWarehousePrice());//入库单价
			setValue2(bodyRow, centerStyle, 21, model.getWarehouseAmount());//入库金额
			setValue2(bodyRow, centerStyle, 22, model.getDepotName());//入库仓库
			setValue2(bodyRow, centerStyle, 23, "");//创建人名称
			
			// 记录行数
			rowNum += 1;
		}	
		purchaseNotshelfList=null;		

		// sheet5				
		Sheet excel5 = workbook.createSheet(sheetName5);
		Row headrow5 = excel5.createRow(0);
		k = 0;
		for (String title : columns5) { // 设置表头
			Cell cell = headrow5.createCell(k);
			excel5.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j5 = 0; j5 < inItemList.size(); j5++) { // 填值
			Map model = inItemList.get(j5);
			Row row = excel5.createRow(j5 + 1);
			k = 0;
			for (String key : keys5) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		inItemList=null;
		
		
		Sheet excel5A = workbook.createSheet(sheetName5A);
		Row headrow5A = excel5A.createRow(0);
		k = 0;
		for (String title : columns5A) { // 设置表头
			Cell cell = headrow5A.createCell(k);
			excel5A.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j5A = 0; j5A < saleShelfTKList.size(); j5A++) { // 填值
			Map model = saleShelfTKList.get(j5A);
			Row row = excel5A.createRow(j5A + 1);
			k = 0;
			for (String key : keys5A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		saleShelfTKList=null;
		
		
		// sheet6
		Sheet excel6 = workbook.createSheet(sheetName6);
		Row headrow6 = excel6.createRow(0);
		k = 0;
		for (String title : columns6) { // 设置表头
			Cell cell = headrow6.createCell(k);
			excel6.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j6 = 0; j6 < saleNotshelfList.size(); j6++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddSaleNoshelfDetailsModel model = saleNotshelfList.get(j6);
			bodyRow = excel6.createRow(rowNum);  
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部名称
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌名称
			setValue2(bodyRow, centerStyle, 3, model.getCustomerName());//客户名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 6, model.getOutOrderCode());//销售出库单号
			String orderType = model.getOrderType();
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeSaleNotshelf_orderTypeList, model.getOrderType());
			}			
			setValue2(bodyRow, centerStyle, 7, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 9, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 13,tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getNum());//数量
			String saleCurrency ="";
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}
			setValue2(bodyRow, centerStyle, 15, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 16, model.getSalePrice());//销售单价		
			setValue2(bodyRow, centerStyle, 17, model.getSaleAmount());//销售金额	
			String outDepotCurrency ="";
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 18, outDepotCurrency);//出库币种						
			setValue2(bodyRow, centerStyle, 19, model.getOutDepotPrice());//出库单价	
			setValue2(bodyRow, centerStyle, 20, model.getOutDepotAmount());//出库金额
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//出仓仓库	
			setValue2(bodyRow, centerStyle, 22, "");//创建人名称
			// 记录行数
			rowNum += 1;
		}	
		
		saleNotshelfList=null;
		
		// sheet7
		Sheet excel7 = workbook.createSheet(sheetName7);
		Row headrow7 = excel7.createRow(0);
		k = 0;
		for (String title : columns7) { // 设置表头
			Cell cell = headrow7.createCell(k);
			excel7.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j7 = 0; j7 < financePurchaseDamagedList.size(); j7++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinancePurchaseDamagedModel model = financePurchaseDamagedList.get(j7);						   
			bodyRow = excel7.createRow(rowNum);  
			String orderType ="";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financePurchaseDamaged_orderTypeList, model.getOrderType());
			}
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, orderType);//残损类型 1来货残次、2
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCode());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 7, model.getWarehouseCreateDate());//入库时间
			setValue2(bodyRow, centerStyle, 8, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 9, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 10, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 11, model.getNum());//短缺/残次数量						
			setValue2(bodyRow, centerStyle, 12, model.getDepotName());//入库仓库	
			setValue2(bodyRow, centerStyle, 13, model.getCreateName());//创建人名称
			// 记录行数
			rowNum += 1;
		}				
		financePurchaseDamagedList=null;
				

		// sheet8
		Sheet excel8 = workbook.createSheet(sheetName8);
		Row headrow8 = excel8.createRow(0);
		k = 0;
		for (String title : columns8) { // 设置表头
			Cell cell = headrow8.createCell(k);
			excel8.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j8 = 0; j8 < saleDamagedList.size(); j8++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceSaleDamagedModel model = saleDamagedList.get(j8);						   
			bodyRow = excel8.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//出库单号
			setValue2(bodyRow, centerStyle, 6, model.getCustomerName());//客户
			setValue2(bodyRow, centerStyle, 7, model.getPoNo());//po号
			String orderType = model.getOrderType();
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeSaleDamaged_orderTypeList, model.getOrderType());
			}
			setValue2(bodyRow, centerStyle, 8, orderType);//订单类型
			setValue2(bodyRow, centerStyle, 9, model.getDeliverDate());//发货时间
			setValue2(bodyRow, centerStyle, 10, model.getShelfDate());//上架时间
			setValue2(bodyRow, centerStyle, 11, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 14, model.getPrice());//出库单价					
			setValue2(bodyRow, centerStyle, 15,model.getNum());//残损数量
			setValue2(bodyRow, centerStyle, 16,model.getLackNum());//缺货数量
			setValue2(bodyRow, centerStyle, 17,model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 18,model.getSaleAmount());//销售金额
			setValue2(bodyRow, centerStyle, 19,model.getSaleCurrency());//销售币种
			setValue2(bodyRow, centerStyle, 20,model.getAmount());//残损金额	
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//出仓仓库
			setValue2(bodyRow, centerStyle, 22, model.getShelfName());//上架人名称
			// 记录行数
			rowNum += 1;
		}		
		saleDamagedList=null;
		
		// sheet9
		Sheet excel9 = workbook.createSheet(sheetName9);
		Row headrow9 = excel9.createRow(0);
		k = 0;
		for (String title : columns9) { // 设置表头
			Cell cell = headrow9.createCell(k);
			excel9.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j9 = 0; j9 < takesStockResultsList.size(); j9++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceTakesStockResultsModel model = takesStockResultsList.get(j9);						   
			bodyRow = excel9.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//盘点单据
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCode());//盘点单据
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//盘点时间
			setValue2(bodyRow, centerStyle, 5, model.getDepotName());//盘点仓库
			setValue2(bodyRow, centerStyle, 6, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 7, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 8, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 9, model.getSurplusNum());//盘盈数量
			setValue2(bodyRow, centerStyle, 10, model.getDeficientNum());//盘亏数量			
			setValue2(bodyRow, centerStyle, 11, model.getPrice());//本期单价
			setValue2(bodyRow, centerStyle, 12, model.getAmount());//盘点结转金额
			setValue2(bodyRow, centerStyle, 13,model.getBatchNo());//批次号			
			String isDamage = null;
			if (StringUtils.isNotBlank(model.getIsDamage())) {
				isDamage = DERP.getLabelByKey(DERP.isDamageList, model.getIsDamage());
			}
			setValue2(bodyRow, centerStyle, 14, isDamage);//是否坏品
			setValue2(bodyRow, centerStyle, 15, model.getProductionDate());//生产日期
			setValue2(bodyRow, centerStyle, 16, model.getOverdueDate());//失效日期
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}

			setValue2(bodyRow, centerStyle, 17, tallyingUnit);//理货单位
			
			// 记录行数
			rowNum += 1;
		}		
		takesStockResultsList=null;
		
		// sheet10
		Sheet excel10 = workbook.createSheet(sheetName10);
		Row headrow10 = excel10.createRow(0);
		k = 0;
		for (String title : columns10) { // 设置表头
			Cell cell = headrow10.createCell(k);
			excel10.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j10 = 0; j10 < financeDestroyList.size(); j10++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDestroyModel model = financeDestroyList.get(j10);
        	
			bodyRow = excel10.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 4, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 5, model.getBarcode());//商品条码
			setValue2(bodyRow, centerStyle, 6, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 7, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 8, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 9, model.getOrderCode());//调整单据号
			String type =null ;			
			if (StringUtils.isNotBlank(model.getType())) {
				type = DERP.getLabelByKey(DERP_INVENTORY.inventory_operateTypeList, model.getType());
			}
			setValue2(bodyRow, centerStyle, 10, type);//调整类型
			setValue2(bodyRow, centerStyle, 11, model.getBatchNo());//原始批次号					
			String isDamage = null;
			if (StringUtils.isNotBlank(model.getIsDamage())) {
				isDamage = DERP.getLabelByKey(DERP.isDamageList, model.getIsDamage());
			}
			setValue2(bodyRow, centerStyle, 12, isDamage);//是否坏品
			setValue2(bodyRow, centerStyle, 13, model.getNum());//调整数量
			setValue2(bodyRow, centerStyle, 14, model.getPrice());//本期单价
			setValue2(bodyRow, centerStyle, 15, model.getAmount());//销毁结转金额
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 16, tallyingUnit);//理货单位
			setValue2(bodyRow, centerStyle, 17, model.getProductionDate());//生产日期
			setValue2(bodyRow, centerStyle, 18, model.getOverdueDate());//失效日期
			setValue2(bodyRow, centerStyle, 19, model.getSourceTime());//调整时间
			setValue2(bodyRow, centerStyle, 20, model.getMonth());//归属月份
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//调整仓库名称		
			setValue2(bodyRow, centerStyle, 22, model.getCreateName());//创建人名称
			// 记录行数
			rowNum += 1;
		}	
		financeDestroyList=null;
		
		// sheet11
		/*Sheet excel11 = workbook.createSheet(sheetName11);
		Row headrow11 = excel11.createRow(0);
		k = 0;
		for (String title : columns11) { // 设置表头
			Cell cell = headrow11.createCell(k);
			excel11.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j11 = 0; j11 < addPurchaseNotshelfSummaryList.size(); j11++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			FinanceAddPurchaseNotshelfSummaryModel model = addPurchaseNotshelfSummaryList.get(j11);        	
			bodyRow = excel11.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 1, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 2, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 3, model.getInitPurchaseNotshelfNum());//期初采购在途数量
			setValue2(bodyRow, centerStyle, 4, model.getInitPurchaseNotshelfAmount());//期初采购在途金额
			setValue2(bodyRow, centerStyle, 5, model.getIncreasedPurchaseNotshelfNum());//本期新增在途量
			setValue2(bodyRow, centerStyle, 6, model.getDecreasePurchaseNotshelfNum());//本期减少在途量
			setValue2(bodyRow, centerStyle, 7, model.getEndPurchaseNotshelfNum());//期末采购在途数量
			setValue2(bodyRow, centerStyle, 8, model.getEndPurchaseNotshelfAmount());//期末采购在途金额
			setValue2(bodyRow, centerStyle, 9, model.getDepotName());//入库仓库					
			// 记录行数
			rowNum += 1;
		}
		addPurchaseNotshelfSummaryList=null;*/
		// sheet12
		Sheet excel12 = workbook.createSheet(sheetName12);
		Row headrow12 = excel12.createRow(0);
		k = 0;
		for (String title : columns12) { // 设置表头
			Cell cell = headrow12.createCell(k);
			excel12.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j12 = 0; j12 < addPurchaseNotshelfDetailsList.size(); j12++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddPurchaseNotshelfDetailsModel model = addPurchaseNotshelfDetailsList.get(j12);
        	
			bodyRow = excel12.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 7, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 8, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 9, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 10, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}        	
			setValue2(bodyRow, centerStyle, 11, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 12, model.getNum());//数量
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 13, orderCurrency);//采购币种
			setValue2(bodyRow, centerStyle, 14, model.getOrderPrice());//采购币种
			setValue2(bodyRow, centerStyle, 15, model.getOrderAmount());//采购金额
			String warehouseCurrency=null;
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}		
			setValue2(bodyRow, centerStyle, 16, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 17, model.getWarehousePrice());//入库单价
			setValue2(bodyRow, centerStyle, 18, model.getWarehouseAmount());//入库金额
			setValue2(bodyRow, centerStyle, 19, model.getDepotName());//入库仓库					
			// 记录行数
			rowNum += 1;
		}	
		addPurchaseNotshelfDetailsList=null;
		
		// sheet13
		Sheet excel13 = workbook.createSheet(sheetName13);
		Row headrow13 = excel13.createRow(0);
		k = 0;
		for (String title : columns13) { // 设置表头
			Cell cell = headrow13.createCell(k);
			excel13.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j13 = 0; j13 < addSaleNoshelfDetailsList.size(); j13++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddSaleNoshelfDetailsModel model = addSaleNoshelfDetailsList.get(j13);       	
			bodyRow = excel13.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌			
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//销售出库单号
			String orderType = "";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeAddSaleNoshelfDetails_orderTypeList, model.getOrderType());
			}	
			setValue2(bodyRow, centerStyle, 6, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 9, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 10, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 11, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 12, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 13, model.getNum());//数量
			String saleCurrency=null;
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}
			setValue2(bodyRow, centerStyle, 14, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 15, model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 16, model.getSaleAmount());//销售金额
			String outDepotCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 17, outDepotCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 18,model.getOutDepotPrice() );//出库单价
			setValue2(bodyRow, centerStyle, 19, model.getOutDepotAmount());//出库金额
			setValue2(bodyRow, centerStyle, 20, model.getDepotName());//出仓仓库
        					
			// 记录行数
			rowNum += 1;
		}
		addSaleNoshelfDetailsList=null;
		
		// sheet14
		/*Sheet excel14 = workbook.createSheet(sheetName14);
		Row headrow14 = excel14.createRow(0);
		k = 0;
		for (String title : columns14) { // 设置表头
			Cell cell = headrow14.createCell(k);
			excel14.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j14 = 0; j14 < addSaleNoshelfSummaryList.size(); j14++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			FinanceAddSaleNoshelfSummaryModel model = addSaleNoshelfSummaryList.get(j14);       	
			bodyRow = excel14.createRow(rowNum);			
			setValue2(bodyRow, centerStyle, 0, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 1, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 2, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 3, model.getInitSaleNotshelfNum());//期初销售在途数量
			setValue2(bodyRow, centerStyle, 4, model.getInitSaleNotshelfAmount());//期初销售在途金额
			setValue2(bodyRow, centerStyle, 5, model.getIncreasedSaleNotshelfNum());//本期新增在途量
			setValue2(bodyRow, centerStyle, 6, model.getDecreaseSaleNotshelfNum());//本期减少在途量		
			setValue2(bodyRow, centerStyle, 7, model.getEndSaleNotshelfNum());//期末销售在途数量
			setValue2(bodyRow, centerStyle, 8, model.getEndSaleNotshelfAmount());//期末销售在途金额		
			setValue2(bodyRow, centerStyle, 9, model.getDepotName());//出仓仓库			        					
			// 记录行数
			rowNum += 1;
		}
		addSaleNoshelfSummaryList=null;*/
		
		// sheet15
		Sheet excel15 = workbook.createSheet(sheetName15);
		Row headrow15 = excel15.createRow(0);
		k = 0;
		for (String title : columns15) { // 设置表头
			Cell cell = headrow15.createCell(k);
			excel15.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j15 = 0; j15 < decreaseSaleNoshelfList.size(); j15++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDecreaseSaleNoshelfModel model = decreaseSaleNoshelfList.get(j15);       	
			bodyRow = excel15.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌	
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//销售出库单号
			String orderType = "";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeDecreaseSaleNoshelf_orderTypeList, model.getOrderType());
			}	

			setValue2(bodyRow, centerStyle, 6, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getShelfDate());//上架时间
			setValue2(bodyRow, centerStyle, 9, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称							
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 13, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getShelfNum());//上架数量
			setValue2(bodyRow, centerStyle, 15, model.getDamagedNum());//残次数量
			setValue2(bodyRow, centerStyle, 16, model.getLackNum());//缺货数量
			setValue2(bodyRow, centerStyle, 17, model.getNum());//数量
			String saleCurrency=null;
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}						
			setValue2(bodyRow, centerStyle, 18, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 19, model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 20, model.getSaleAmount());//销售金额
			String outDepotCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 21, outDepotCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 22,model.getOutDepotPrice() );//出库单价
			setValue2(bodyRow, centerStyle, 23, model.getOutDepotAmount());//出库金额
			setValue2(bodyRow, centerStyle, 24, model.getDepotName());//出仓仓库
			setValue2(bodyRow, centerStyle, 25, model.getShelfName());//上架人名称
        					
			// 记录行数
			rowNum += 1;
		}	
		decreaseSaleNoshelfList=null;
		// sheet16
		/**
		Sheet excel16 = workbook.createSheet(sheetName16);
		Row headrow16 = excel16.createRow(0);
		k = 0;
		for (String title : columns16) { // 设置表头
			Cell cell = headrow16.createCell(k);
			excel16.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j16 = 0; j16 < decreasePurchaseNotshelfList.size(); j16++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDecreasePurchaseNotshelfModel model = decreasePurchaseNotshelfList.get(j16);       	
			bodyRow = excel16.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 7, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 8, model.getWarehouseCreateDates());//入库时间
			setValue2(bodyRow, centerStyle, 9, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称

			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}

			setValue2(bodyRow, centerStyle, 13, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getNum());//数量
			
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 15, orderCurrency);//采购币种
			setValue2(bodyRow, centerStyle, 16, model.getOrderPrice());//采购单价
			setValue2(bodyRow, centerStyle, 17, model.getOrderAmount());//采购金额
			String warehouseCurrency=null;
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}
			setValue2(bodyRow, centerStyle, 18, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 19, model.getWarehousePrice());//入库单价
			setValue2(bodyRow, centerStyle, 20, model.getWarehouseAmount());//入库金额
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//入库仓库			
        					
			// 记录行数
			rowNum += 1;
		}	
		decreasePurchaseNotshelfList=null;
		**/
		// sheet 17
		Sheet excel17 = workbook.createSheet(sheetName17);
		Row headrow17 = excel17.createRow(0);
		k = 0;
		for (String title : columns17) { // 设置表头
			Cell cell = headrow17.createCell(k);
			excel17.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j17 = 0; j17 < addTransferNoshelfList.size(); j17++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddTransferNoshelfDetailsModel model = addTransferNoshelfList.get(j17);       	
			bodyRow = excel17.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getOrderCode());//调拨订单号
			setValue2(bodyRow, centerStyle, 4, model.getOutOrderCode());//调拨出单号
			setValue2(bodyRow, centerStyle, 5, model.getOutDepotName());//调出仓库
			setValue2(bodyRow, centerStyle, 6, model.getInDepotName());//调入仓库
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getMonth());//归属月份
			setValue2(bodyRow, centerStyle, 9, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 11, model.getBarcode());//条码
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getPoNo());//PO号
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 14,tallyingUnit);//理货单位
			setValue2(bodyRow, centerStyle, 15, model.getNum());//在途数量
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 16,orderCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 17, model.getOutDepotPrice());//出库单价
			setValue2(bodyRow, centerStyle, 18, model.getOutDepotAmount());//出库金额
			       					
			// 记录行数
			rowNum += 1;
		}	
		addTransferNoshelfList=null;	
		
		
		Sheet excel17A = workbook.createSheet(sheetName17A);
		Row headrow17A = excel17A.createRow(0);
		k = 0;
		for (String title : columns17A) { // 设置表头
			Cell cell = headrow17A.createCell(k);
			excel17A.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j17A = 0; j17A < saleChannelSummaryList.size(); j17A++) { // 填值
			Map model = saleChannelSummaryList.get(j17A);
			Row row = excel17A.createRow(j17A + 1);
			k = 0;
			for (String key : keys17A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		saleChannelSummaryList=null;
		

		// sheet18				
		Sheet excel18 = workbook.createSheet(sheetName18);
		Row headrow18 = excel18.createRow(0);
		k = 0;
		for (String title : columns18) { // 设置表头
			Cell cell = headrow18.createCell(k);
			excel18.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j18 = 0; j18 < itemMoveList.size(); j18++) { // 填值
			Map model = itemMoveList.get(j18);
			Row row = excel18.createRow(j18 + 1);
			k = 0;
			for (String key : keys18) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		itemMoveList=null;
		// sheet18				
		Sheet excel19 = workbook.createSheet(sheetName19);
		Row headrow19 = excel19.createRow(0);
		k = 0;
		for (String title : columns19) { // 设置表头
			Cell cell = headrow19.createCell(k);
			excel19.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		// 注入参数
		for (int j19 = 0; j19 < fgInventList.size(); j19++) { // 填值
			Map model = fgInventList.get(j19);
			Row row = excel19.createRow(j19 + 1);
			k = 0;
			for (String key : keys19) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);	
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		fgInventList=null;	
		
		return workbook;
	}
	
	
	
	/**
	 * 生成SD财务经销存汇总和详情 
	 * @desc: 导出excel 2007
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 */
	public static SXSSFWorkbook createInterestTask(List<Map<String, Object>>excelMapList) {						
		// 防止导出科学计数法
		List<String> valueAmountkeyList= new ArrayList<String>();
		valueAmountkeyList.add("sd_price");
		valueAmountkeyList.add("sd_tz_price");
		valueAmountkeyList.add("sd_order_price");

		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		// 设置表头颜色
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式		
		CellStyle centerStyle = setBodyCellStyle(workbook, font);
		
		
		// 循环生成excel 各个表格数据
		for (Map<String, Object> map : excelMapList) {
			String sheetName = (String) map.get("sheetName");
			String[] columns =  (String[]) map.get("columns");
			String[] keys =  (String[]) map.get("keys");
			List<Map<String, Object>> listMap=(List<Map<String, Object>>) map.get("list");
			// 创建sheet
			Sheet excel = workbook.createSheet(sheetName);
			Row headrow = excel.createRow(0);
			int k = 0;
			for (String title : columns) { // 设置表头
				Cell cell = headrow.createCell(k);
				excel.setColumnWidth(k, 20 * 256);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title);
				k++;
			}
			
			for (int i = 0; i < listMap.size(); i++) { // 填值
				Map model = listMap.get(i);
				Row row = excel.createRow(i + 1);
				k = 0;
				for (String key : keys) {
					Cell cell = row.createCell(k);
					cell.setCellStyle(centerStyle);
					Object obj = model.get(key);
					if (model.get(key) instanceof Timestamp) {
						String value1 = model.get(key).toString();
						String intNumber = value1.substring(0, value1.indexOf("."));
						cell.setCellValue(intNumber);
					} else if (model.get(key) instanceof BigDecimal) {
						BigDecimal value1 = new BigDecimal(model.get(key).toString());
						BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);	
						cell.setCellValue(value2.toString());
					} else {
						cell.setCellValue(obj != null ? obj.toString() : "");
					}
					k++;
				}
			}	
			
			listMap=null;
			sheetName=null;
			keys=null;
			columns=null;
		}
		return workbook;
	}
	
	/**
	 * 生成财务经销存  美赞成本差异导出 
	 * @desc: 导出excel 2007
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 */
	public static SXSSFWorkbook createCostDifferenceTask(List<Map<String, Object>>excelMapList) {						
		// 防止导出科学计数法
		List<String> valueAmountkeyList= new ArrayList<String>();
		valueAmountkeyList.add("difference_price");
		valueAmountkeyList.add("price");

		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		// 设置表头颜色
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式		
		CellStyle centerStyle = setBodyCellStyle(workbook, font);
		
		
		// 循环生成excel 各个表格数据
		for (Map<String, Object> map : excelMapList) {
			String sheetName = (String) map.get("sheetName");
			String[] columns =  (String[]) map.get("columns");
			String[] keys =  (String[]) map.get("keys");
			List<Map<String, Object>> listMap=(List<Map<String, Object>>) map.get("list");
			// 创建sheet
			Sheet excel = workbook.createSheet(sheetName);
			Row headrow = excel.createRow(0);
			int k = 0;
			for (String title : columns) { // 设置表头
				Cell cell = headrow.createCell(k);
				excel.setColumnWidth(k, 20 * 256);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title);
				k++;
			}
			
			for (int i = 0; i < listMap.size(); i++) { // 填值
				Map model = listMap.get(i);
				Row row = excel.createRow(i + 1);
				k = 0;
				for (String key : keys) {
					Cell cell = row.createCell(k);
					cell.setCellStyle(centerStyle);
					Object obj = model.get(key);
					if (model.get(key) instanceof Timestamp) {
						String value1 = model.get(key).toString();
						String intNumber = value1.substring(0, value1.indexOf("."));
						cell.setCellValue(intNumber);
					} else if (model.get(key) instanceof BigDecimal) {
						BigDecimal value1 = new BigDecimal(model.get(key).toString());
						BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);
						cell.setCellValue(value2.toString());
					} else {
						cell.setCellValue(obj != null ? obj.toString() : "");
					}
					k++;
				}
			}	
			
			listMap=null;
			sheetName=null;
			keys=null;
			columns=null;
		}
		return workbook;
	}

	public static SXSSFWorkbook createBuSdExcelSXSS6(String sheetName1, String[] columns1, String[] keys1,List<Map<String, Object>> values1,
			String sheetName1A, String[] columns1A, String[] keys1A,List<Map<String, Object>> values1A,
			String sheetName3, String[] columns3, List<BuFinanceWarehouseDetailsModel> warehouseDetailsList,
			String sheetName3A,String[] columns3A, List<String>  List3A,List<Map<String, Object>> buFinanceSdWarehouseDetailsMapList,
			String sheetNameA4,String[] columnsA4, List<String>  ListA4,List<Map<String, Object>> buFinanceSdAddPurchaseNotshelfList,
			String sheetName4, String[] columns4, List<BuFinanceAddPurchaseNotshelfDetailsModel> purchaseNotshelfList,
			String sheetName5, String[] columns5, String[] keys5, List<Map<String, Object>> inItemList,
			String sheetName6, String[] columns6, List<BuFinanceAddSaleNoshelfDetailsModel>  saleNotshelfList,
			String sheetName7, String[] columns7, List<BuFinancePurchaseDamagedModel> financePurchaseDamagedList,
			String sheetName8, String[] columns8, List<BuFinanceSaleDamagedModel>  saleDamagedList,
			String sheetName9, String[] columns9, List<BuFinanceTakesStockResultsModel> takesStockResultsList,
			String sheetName10,String[] columns10, List<BuFinanceDestroyModel> financeDestroyList,
			//String sheetName11,String[] columns11, List<FinanceAddPurchaseNotshelfSummaryModel> addPurchaseNotshelfSummaryList,
			String sheetName12,String[] columns12, List<BuFinanceAddPurchaseNotshelfDetailsModel> addPurchaseNotshelfDetailsList,
			String sheetName13,String[] columns13, List<BuFinanceAddSaleNoshelfDetailsModel>addSaleNoshelfDetailsList,
			//String sheetName14,String[] columns14,List<FinanceAddSaleNoshelfSummaryModel> addSaleNoshelfSummaryList,
			String sheetName15,String[] columns15,List<BuFinanceDecreaseSaleNoshelfModel> decreaseSaleNoshelfList,
			//String sheetName16,String[] columns16,List<BuFinanceDecreasePurchaseNotshelfModel> decreasePurchaseNotshelfList,
			String sheetName17,String[] columns17,List<BuFinanceAddTransferNoshelfDetailsModel> addTransferNoshelfList,			
			String sheetName17A,String[] columns17A,String[] keys17A,List<Map<String, Object>> saleChannelSummaryList,
			String sheetName18, String[] columns18, String[] keys18, List<Map<String, Object>> itemMoveList,
			String sheetName19, String[] columns19, String[] keys19, List<Map<String, Object>> fgInventList) {
		
		
		List<String> valueAmountkeyList= new ArrayList<String>();
		valueAmountkeyList.add("sd_price");
		valueAmountkeyList.add("sd_tz_price");
		valueAmountkeyList.add("sd_order_price");
		valueAmountkeyList.add("sd_order_price");

		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		// 设置表头颜色
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式		
		CellStyle centerStyle = setBodyCellStyle(workbook, font);
		
		Sheet excelA = workbook.createSheet(sheetName1A);		
		Row  headrow0A =  excelA.createRow(0);		
		CellRangeAddress region1A = new CellRangeAddress(0, 0, (short) 0, (short) 5);
		CellRangeAddress region2A = new CellRangeAddress(0, 0, (short) 6, (short) 8);
		CellRangeAddress region3A = new CellRangeAddress(0, 0, (short) 9, (short) 13);
		//CellRangeAddress region5A = new CellRangeAddress(0, 0, (short) 14, (short) 14);		
		CellRangeAddress region6A = new CellRangeAddress(0, 0, (short) 15, (short) 19);
		CellRangeAddress region7A = new CellRangeAddress(0, 0, (short) 20, (short) 24);
		CellRangeAddress region8A = new CellRangeAddress(0, 0, (short) 25, (short) 26);
		CellRangeAddress region9A = new CellRangeAddress(0, 0, (short) 27, (short) 30);

		
		for (int iA = 0; iA < 30; iA++) {
			Cell cell = headrow0A.createCell(iA);

			if (iA>=0&&iA<=5) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.CORAL.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("商品信息");
			}else if (iA>5&&iA<=8) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LIGHT_TURQUOISE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期初");
			}else if (iA>8&&iA<=13) {	
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.YELLOW.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本月采购汇总");
			}else if (iA>13&&iA<=14) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.RED.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期调整单价");
			}else if (iA>14&&iA<=19) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ORANGE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期销售汇总");
			}else if (iA>19&&iA<=24) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ROSE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期损益汇总");
			}else if (iA>24&&iA<=26) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LEMON_CHIFFON.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期末账存汇总");
			}else if (iA>26&&iA<=30) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.GOLD.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("累计在途汇总");
			}
		}
	     
        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
		excelA.addMergedRegion(region1A);
		excelA.addMergedRegion(region2A);
		excelA.addMergedRegion(region3A);
		//excelA.addMergedRegion(region5A);
		excelA.addMergedRegion(region6A);
		excelA.addMergedRegion(region7A);
		excelA.addMergedRegion(region8A);
		excelA.addMergedRegion(region9A);


		Row headrowA = excelA.createRow(1);
		int iA = 0;
		for (String title : columns1A) { // 设置表头
			Cell cell = headrowA.createCell(iA);
			excelA.setColumnWidth(iA, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			iA++;
		}
		// 注入参数
		for (int jA = 0; jA < values1A.size(); jA++) { // 填值
			Map model = values1A.get(jA);
			Row row = excelA.createRow(jA + 2);
			iA = 0;
			for (String key : keys1A) {
				Cell cell = row.createCell(iA);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					//获取金额位数
					BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);			
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				iA++;
			}
		}

	
		Sheet excel = workbook.createSheet(sheetName1);		
		Row  headrow0 =  excel.createRow(0);
		CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) 7);
		CellRangeAddress region2 = new CellRangeAddress(0, 0, (short) 8, (short) 10);
		CellRangeAddress region3 = new CellRangeAddress(0, 0, (short) 11, (short) 15);
		//CellRangeAddress region5 = new CellRangeAddress(0, 0, (short) 16, (short) 16);
		CellRangeAddress region6 = new CellRangeAddress(0, 0, (short) 17, (short) 21);
		CellRangeAddress region7 = new CellRangeAddress(0, 0, (short) 22, (short) 26);
		CellRangeAddress region8 = new CellRangeAddress(0, 0, (short) 27, (short) 28);
		CellRangeAddress region9 = new CellRangeAddress(0, 0, (short) 29, (short) 32);

		
		for (int i = 0; i < 32; i++) {
			Cell cell = headrow0.createCell(i);
			if (i>=0&&i<=7) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.CORAL.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("商品信息");
			}else if (i>7&&i<=10) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LIGHT_TURQUOISE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期初");
			}else if (i>10&&i<=15) {	
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.YELLOW.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本月采购汇总");
			}else if (i>15&&i<=16) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.RED.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期调整单价");
			}else if (i>16&&i<=21) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ORANGE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期销售汇总");
			}else if (i>21&&i<=26) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.ROSE.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期损益汇总");
			}else if (i>26&&i<=28) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.LEMON_CHIFFON.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("本期期末账存汇总");
			}else if (i>28&&i<=32) {
				CellStyle style = setHeadCellStyle(workbook, font, IndexedColors.GOLD.getIndex());
				cell.setCellStyle(style);
				cell.setCellValue("累计在途汇总");
			}		
		}

        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
		excel.addMergedRegion(region1);
		excel.addMergedRegion(region2);
		excel.addMergedRegion(region3);
		//excel.addMergedRegion(region5);
		excel.addMergedRegion(region6);
		excel.addMergedRegion(region7);
		excel.addMergedRegion(region8);
		excel.addMergedRegion(region9);
				
		Row headrow = excel.createRow(1);
		int i = 0;
		for (String title : columns1) { // 设置表头
			Cell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j = 0; j < values1.size(); j++) { // 填值
			Map model = values1.get(j);
			Row row = excel.createRow(j + 2);
			i = 0;
			for (String key : keys1) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);	
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		values1=null;
					
		

		
		
		// sheet3
		Sheet excel3 = workbook.createSheet(sheetName3);
		Row headrow3 = excel3.createRow(0);
		int k = 0;
		for (String title : columns3) { // 设置表头
			Cell cell = headrow3.createCell(k);
			excel3.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		int rowNum = 0;
		Row bodyRow = null;
		// 循环表头入库单信息
		for (int j3 = 0; j3 < warehouseDetailsList.size(); j3++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			
			BuFinanceWarehouseDetailsModel model = warehouseDetailsList.get(j3);
			bodyRow = excel3.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getSupplierName());//供应商名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 6, model.getWarehouseCode());//入库单号
			String orderType = model.getOrderType();// 类型是采购退货 数量金额 负数显示
			String orderTypeName = null;
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderTypeName = DERP.getLabelByKey(DERP_REPORT.financeWarehouseDetails_orderTypeList, model.getOrderType());
			}
        	
			setValue2(bodyRow, centerStyle, 7, orderTypeName);//单据类型
			setValue2(bodyRow, centerStyle, 8, model.getWarehouseCreateDate());//入库时间
			setValue2(bodyRow, centerStyle, 9, model.getRelDate());//勾稽时间
			setValue2(bodyRow, centerStyle, 10, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 11, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 12, model.getInvoiceNo());//发票号码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 14, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 15, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}	

			setValue2(bodyRow, centerStyle, 16, tallyingUnit);//单位
			Integer warehouseNum = model.getWarehouseNum();
			BigDecimal orderAmount = model.getOrderAmount();
			BigDecimal warehouseAmount = model.getWarehouseAmount();
			BigDecimal sdTgtAmount = model.getSdTgtAmount();
			if (DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_2.equals(orderType)) {// 采购退货负数显示
				if (warehouseNum!=null)warehouseNum=warehouseNum *-1;
				if (orderAmount!=null)orderAmount=orderAmount.multiply(new BigDecimal("-1"));
				if (warehouseAmount!=null)warehouseAmount=warehouseAmount.multiply(new BigDecimal("-1")); 					
				if (sdTgtAmount!=null)sdTgtAmount=sdTgtAmount.multiply(new BigDecimal("-1")); 

			}
			setValue2(bodyRow, centerStyle, 17, warehouseNum);//数量
			String orderCurrency ="";
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}

			setValue2(bodyRow, centerStyle, 18, orderCurrency);//采购币种
			String  warehouseCurrency="";
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}
			setValue2(bodyRow, centerStyle, 19, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 20, sdTgtAmount);//采购SD金额（本币）
			setValue2(bodyRow, centerStyle, 21, model.getInDepotName());//入库仓库		
			setValue2(bodyRow, centerStyle, 22, model.getCreateName());//创建人名称	
				
			// 记录行数
			rowNum += 1;
		}						
		
		warehouseDetailsList=null;
		
		
		
		// sheet3A				
		Sheet excel3A = workbook.createSheet(sheetName3A);
		Row headrow3A = excel3A.createRow(0);
		k = 0;
		for (String title : columns3A) { // 设置表头
			Cell cell = headrow3A.createCell(k);
			excel3A.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j3A = 0; j3A < buFinanceSdWarehouseDetailsMapList.size(); j3A++) { // 填值
			Map model = buFinanceSdWarehouseDetailsMapList.get(j3A);
			Row row = excel3A.createRow(j3A + 1);
			k = 0;
			for (String key : List3A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);	
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		buFinanceSdWarehouseDetailsMapList=null;
		
		
		// sheetA4				
		Sheet excelA4 = workbook.createSheet(sheetNameA4);
		Row headrowA4 = excelA4.createRow(0);
		k = 0;
		for (String title : columnsA4) { // 设置表头
			Cell cell = headrowA4.createCell(k);
			excelA4.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int jA4 = 0; jA4 < buFinanceSdAddPurchaseNotshelfList.size(); jA4++) { // 填值
			Map model = buFinanceSdAddPurchaseNotshelfList.get(jA4);
			Row row = excelA4.createRow(jA4+ 1);
			k = 0;
			for (String key : ListA4) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);	
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		buFinanceSdAddPurchaseNotshelfList=null;	
		
		
		// sheet4
		Sheet excel4 = workbook.createSheet(sheetName4);
		Row headrow4 = excel4.createRow(0);
		k = 0;
		for (String title : columns4) { // 设置表头
			Cell cell = headrow4.createCell(k);
			excel4.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j4 = 0; j4 < purchaseNotshelfList.size(); j4++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddPurchaseNotshelfDetailsModel model = purchaseNotshelfList.get(j4);
			bodyRow = excel4.createRow(rowNum);
        	
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getSupplierName());//供应商名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 6, model.getEndDate());//订单完结时间
			setValue2(bodyRow, centerStyle, 7, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 9, model.getDrawInvoiceDate());//在途开始日期
			setValue2(bodyRow, centerStyle, 10, model.getInvoiceNo());//发票号码
			setValue2(bodyRow, centerStyle, 11, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 14, tallyingUnit);//单位			
			setValue2(bodyRow, centerStyle, 15, model.getNum());//数量
			String orderCurrency ="";
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 16, orderCurrency);//采购币种		
			//采购币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
			String warehouseCurrency ="";
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}			
			setValue2(bodyRow, centerStyle, 17, warehouseCurrency);//入库币种?????? 
			setValue2(bodyRow, centerStyle, 18, model.getSdTgtAmount());//采购SD金额（本币）
			setValue2(bodyRow, centerStyle, 19, model.getDepotName());//入库仓库
			setValue2(bodyRow, centerStyle, 20, "");//创建人名称
			
			// 记录行数
			rowNum += 1;
		}	
		purchaseNotshelfList=null;		

		// sheet5				
		Sheet excel5 = workbook.createSheet(sheetName5);
		Row headrow5 = excel5.createRow(0);
		k = 0;
		for (String title : columns5) { // 设置表头
			Cell cell = headrow5.createCell(k);
			excel5.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j5 = 0; j5 < inItemList.size(); j5++) { // 填值
			Map model = inItemList.get(j5);
			Row row = excel5.createRow(j5 + 1);
			k = 0;
			for (String key : keys5) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2= getValueAmount(key,value1,valueAmountkeyList);	
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		inItemList=null;
							
		// sheet6
		Sheet excel6 = workbook.createSheet(sheetName6);
		Row headrow6 = excel6.createRow(0);
		k = 0;
		for (String title : columns6) { // 设置表头
			Cell cell = headrow6.createCell(k);
			excel6.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j6 = 0; j6 < saleNotshelfList.size(); j6++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddSaleNoshelfDetailsModel model = saleNotshelfList.get(j6);
			bodyRow = excel6.createRow(rowNum);  
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部名称
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌名称
			setValue2(bodyRow, centerStyle, 3, model.getCustomerName());//客户名称
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 5, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 6, model.getOutOrderCode());//销售出库单号
			String orderType = model.getOrderType();
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeSaleNotshelf_orderTypeList, model.getOrderType());
			}			
			setValue2(bodyRow, centerStyle, 7, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 9, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 13,tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getNum());//数量
			String saleCurrency ="";
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}
			setValue2(bodyRow, centerStyle, 15, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 16, model.getSalePrice());//销售单价		
			setValue2(bodyRow, centerStyle, 17, model.getSaleAmount());//销售金额	
			String outDepotCurrency ="";
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 18, outDepotCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 19, model.getSdOrderPrice());//本期SD单价				
			setValue2(bodyRow, centerStyle, 20, model.getSdOrderAmount());//本期SD金额				
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//出仓仓库	
			setValue2(bodyRow, centerStyle, 22, "");//创建人名称
			// 记录行数
			rowNum += 1;
		}	
		
		saleNotshelfList=null;
		
		// sheet7
		Sheet excel7 = workbook.createSheet(sheetName7);
		Row headrow7 = excel7.createRow(0);
		k = 0;
		for (String title : columns7) { // 设置表头
			Cell cell = headrow7.createCell(k);
			excel7.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j7 = 0; j7 < financePurchaseDamagedList.size(); j7++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinancePurchaseDamagedModel model = financePurchaseDamagedList.get(j7);						   
			bodyRow = excel7.createRow(rowNum);  
			String orderType ="";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financePurchaseDamaged_orderTypeList, model.getOrderType());
			}
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, orderType);//残损类型 1来货残次、2
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCode());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 7, model.getWarehouseCreateDate());//入库时间
			setValue2(bodyRow, centerStyle, 8, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 9, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 10, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 11, model.getSdOrderPrice());//本期SD单价
			setValue2(bodyRow, centerStyle, 12, model.getNum());//短缺/残次数量						
			setValue2(bodyRow, centerStyle, 13,model.getSdOrderAmount());//短缺/残次SD金额
			setValue2(bodyRow, centerStyle, 14, model.getDepotName());//入库仓库	
			setValue2(bodyRow, centerStyle, 15, model.getCreateName());//创建人名称
			// 记录行数
			rowNum += 1;
		}				
		financePurchaseDamagedList=null;
				

		// sheet8
		Sheet excel8 = workbook.createSheet(sheetName8);
		Row headrow8 = excel8.createRow(0);
		k = 0;
		for (String title : columns8) { // 设置表头
			Cell cell = headrow8.createCell(k);
			excel8.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j8 = 0; j8 < saleDamagedList.size(); j8++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceSaleDamagedModel model = saleDamagedList.get(j8);						   
			bodyRow = excel8.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//出库单号
			setValue2(bodyRow, centerStyle, 6, model.getCustomerName());//客户
			setValue2(bodyRow, centerStyle, 7, model.getPoNo());//po号
			String orderType = model.getOrderType();
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeSaleDamaged_orderTypeList, model.getOrderType());
			}
			setValue2(bodyRow, centerStyle, 8, orderType);//订单类型
			setValue2(bodyRow, centerStyle, 9, model.getDeliverDate());//发货时间
			setValue2(bodyRow, centerStyle, 10, model.getShelfDate());//上架时间
			setValue2(bodyRow, centerStyle, 11, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 14, model.getSdOrderPrice());//本期SD单价
			setValue2(bodyRow, centerStyle, 15,model.getNum());//残损数量
			setValue2(bodyRow, centerStyle, 16,model.getLackNum());//缺货数量
			setValue2(bodyRow, centerStyle, 17,model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 18,model.getSaleAmount());//销售金额
			setValue2(bodyRow, centerStyle, 19,model.getSaleCurrency());//销售币种
			setValue2(bodyRow, centerStyle, 20,model.getSdOrderAmount());//本期SD金额
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//出仓仓库
			setValue2(bodyRow, centerStyle, 22, model.getShelfName());//上架人名称
			// 记录行数
			rowNum += 1;
		}		
		saleDamagedList=null;
		
		// sheet9
		Sheet excel9 = workbook.createSheet(sheetName9);
		Row headrow9 = excel9.createRow(0);
		k = 0;
		for (String title : columns9) { // 设置表头
			Cell cell = headrow9.createCell(k);
			excel9.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j9 = 0; j9 < takesStockResultsList.size(); j9++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceTakesStockResultsModel model = takesStockResultsList.get(j9);						   
			bodyRow = excel9.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//盘点单据
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCode());//盘点单据
			setValue2(bodyRow, centerStyle, 4, model.getOrderCreateDate());//盘点时间
			setValue2(bodyRow, centerStyle, 5, model.getDepotName());//盘点仓库
			setValue2(bodyRow, centerStyle, 6, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 7, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 8, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 9, model.getSurplusNum());//盘盈数量
			setValue2(bodyRow, centerStyle, 10, model.getDeficientNum());//盘亏数量	
			setValue2(bodyRow, centerStyle, 11, model.getPrice());//本期单价
			setValue2(bodyRow, centerStyle, 12, model.getAmount());//盘点结转金额
			setValue2(bodyRow, centerStyle, 13,model.getBatchNo());//批次号			
			String isDamage = null;
			if (StringUtils.isNotBlank(model.getIsDamage())) {
				isDamage = DERP.getLabelByKey(DERP.isDamageList, model.getIsDamage());
			}
			setValue2(bodyRow, centerStyle, 14, isDamage);//是否坏品
			setValue2(bodyRow, centerStyle, 15, model.getProductionDate());//生产日期
			setValue2(bodyRow, centerStyle, 16, model.getOverdueDate());//失效日期
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}

			setValue2(bodyRow, centerStyle, 17, tallyingUnit);//理货单位
			
			// 记录行数
			rowNum += 1;
		}		
		takesStockResultsList=null;
		
		// sheet10
		Sheet excel10 = workbook.createSheet(sheetName10);
		Row headrow10 = excel10.createRow(0);
		k = 0;
		for (String title : columns10) { // 设置表头
			Cell cell = headrow10.createCell(k);
			excel10.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j10 = 0; j10 < financeDestroyList.size(); j10++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDestroyModel model = financeDestroyList.get(j10);
        	
			bodyRow = excel10.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 4, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 5, model.getBarcode());//商品条码
			setValue2(bodyRow, centerStyle, 6, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 7, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 8, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 9, model.getOrderCode());//调整单据号
			String type =null ;			
			if (StringUtils.isNotBlank(model.getType())) {
				type = DERP.getLabelByKey(DERP_INVENTORY.inventory_operateTypeList, model.getType());
			}
			setValue2(bodyRow, centerStyle, 10, type);//调整类型
			setValue2(bodyRow, centerStyle, 11, model.getBatchNo());//原始批次号					
			String isDamage = null;
			if (StringUtils.isNotBlank(model.getIsDamage())) {
				isDamage = DERP.getLabelByKey(DERP.isDamageList, model.getIsDamage());
			}
			setValue2(bodyRow, centerStyle, 12, isDamage);//是否坏品
			setValue2(bodyRow, centerStyle, 13, model.getNum());//调整数量
			setValue2(bodyRow, centerStyle, 14, model.getPrice());//本期单价
			setValue2(bodyRow, centerStyle, 15, model.getAmount());//销毁结转金额
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 16, tallyingUnit);//理货单位
			setValue2(bodyRow, centerStyle, 17, model.getProductionDate());//生产日期
			setValue2(bodyRow, centerStyle, 18, model.getOverdueDate());//失效日期
			setValue2(bodyRow, centerStyle, 19, model.getSourceTime());//调整时间
			setValue2(bodyRow, centerStyle, 20, model.getMonth());//归属月份
			setValue2(bodyRow, centerStyle, 21, model.getDepotName());//调整仓库名称		
			setValue2(bodyRow, centerStyle, 22, model.getCreateName());//创建人名称
			// 记录行数
			rowNum += 1;
		}	
		financeDestroyList=null;
		
		
		// sheet12
		Sheet excel12 = workbook.createSheet(sheetName12);
		Row headrow12 = excel12.createRow(0);
		k = 0;
		for (String title : columns12) { // 设置表头
			Cell cell = headrow12.createCell(k);
			excel12.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j12 = 0; j12 < addPurchaseNotshelfDetailsList.size(); j12++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddPurchaseNotshelfDetailsModel model = addPurchaseNotshelfDetailsList.get(j12);
        	
			bodyRow = excel12.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 7, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 8, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 9, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 10, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}        	
			setValue2(bodyRow, centerStyle, 11, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 12, model.getNum());//数量
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 13, orderCurrency);//采购币种
			String warehouseCurrency=null;
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}		
			setValue2(bodyRow, centerStyle, 14, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 15, model.getSdTgtAmount());//采购SD金额（本币）
			setValue2(bodyRow, centerStyle, 16, model.getDepotName());//入库仓库					
			// 记录行数
			rowNum += 1;
		}	
		addPurchaseNotshelfDetailsList=null;
		
		// sheet13
		Sheet excel13 = workbook.createSheet(sheetName13);
		Row headrow13 = excel13.createRow(0);
		k = 0;
		for (String title : columns13) { // 设置表头
			Cell cell = headrow13.createCell(k);
			excel13.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j13 = 0; j13 < addSaleNoshelfDetailsList.size(); j13++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddSaleNoshelfDetailsModel model = addSaleNoshelfDetailsList.get(j13);       	
			bodyRow = excel13.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌			
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//销售出库单号
			String orderType = "";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeAddSaleNoshelfDetails_orderTypeList, model.getOrderType());
			}	
			setValue2(bodyRow, centerStyle, 6, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 9, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 10, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 11, model.getGoodsName());//商品名称
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 12, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 13, model.getNum());//数量
			String saleCurrency=null;
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}
			setValue2(bodyRow, centerStyle, 14, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 15, model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 16, model.getSaleAmount());//销售金额
			String outDepotCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 17, outDepotCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 18,model.getSdOrderPrice() );//本期SD单价
			setValue2(bodyRow, centerStyle, 19, model.getSdOrderAmount());//本期SD金额
			setValue2(bodyRow, centerStyle, 20, model.getDepotName());//出仓仓库
        					
			// 记录行数
			rowNum += 1;
		}
		addSaleNoshelfDetailsList=null;
		
		
		// sheet15
		Sheet excel15 = workbook.createSheet(sheetName15);
		Row headrow15 = excel15.createRow(0);
		k = 0;
		for (String title : columns15) { // 设置表头
			Cell cell = headrow15.createCell(k);
			excel15.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j15 = 0; j15 < decreaseSaleNoshelfList.size(); j15++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDecreaseSaleNoshelfModel model = decreaseSaleNoshelfList.get(j15);       	
			bodyRow = excel15.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌	
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//销售订单号
			setValue2(bodyRow, centerStyle, 5, model.getOutOrderCode());//销售出库单号
			String orderType = "";
			if (StringUtils.isNotBlank(model.getOrderType())) {
				orderType = DERP.getLabelByKey(DERP_REPORT.financeDecreaseSaleNoshelf_orderTypeList, model.getOrderType());
			}	

			setValue2(bodyRow, centerStyle, 6, orderType);//销售类型
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getShelfDate());//上架时间
			setValue2(bodyRow, centerStyle, 9, model.getPoNo());//po号
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称							
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 13, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getShelfNum());//上架数量
			setValue2(bodyRow, centerStyle, 15, model.getDamagedNum());//残次数量
			setValue2(bodyRow, centerStyle, 16, model.getLackNum());//缺货数量
			setValue2(bodyRow, centerStyle, 17, model.getNum());//数量
			String saleCurrency=null;
			if (StringUtils.isNotBlank(model.getSaleCurrency())) {
				saleCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getSaleCurrency());
			}						
			setValue2(bodyRow, centerStyle, 18, saleCurrency);//销售币种
			setValue2(bodyRow, centerStyle, 19, model.getSalePrice());//销售单价
			setValue2(bodyRow, centerStyle, 20, model.getSaleAmount());//销售金额
			String outDepotCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				outDepotCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 21, outDepotCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 22,model.getSdOrderPrice());//本期SD单价
			setValue2(bodyRow, centerStyle, 23, model.getSdOrderAmount());//本期SD金额
			setValue2(bodyRow, centerStyle, 24, model.getDepotName());//出仓仓库
			setValue2(bodyRow, centerStyle, 25, model.getShelfName());//上架人名称
        					
			// 记录行数
			rowNum += 1;
		}	
		decreaseSaleNoshelfList=null;
		
		/**
		// sheet16
		Sheet excel16 = workbook.createSheet(sheetName16);
		Row headrow16 = excel16.createRow(0);
		k = 0;
		for (String title : columns16) { // 设置表头
			Cell cell = headrow16.createCell(k);
			excel16.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j16 = 0; j16 < decreasePurchaseNotshelfList.size(); j16++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceDecreasePurchaseNotshelfModel model = decreasePurchaseNotshelfList.get(j16);       	
			bodyRow = excel16.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//品牌	
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌		
			setValue2(bodyRow, centerStyle, 3, model.getOrderCreateDate());//订单日期
			setValue2(bodyRow, centerStyle, 4, model.getOrderCode());//采购订单号
			setValue2(bodyRow, centerStyle, 5, model.getWarehouseCodes());//入库单号
			setValue2(bodyRow, centerStyle, 6, model.getPoNo());//PO号
			setValue2(bodyRow, centerStyle, 7, model.getDrawInvoiceDate());//发票日期
			setValue2(bodyRow, centerStyle, 8, model.getWarehouseCreateDates());//入库时间
			setValue2(bodyRow, centerStyle, 9, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//货号
			setValue2(bodyRow, centerStyle, 11, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 12, model.getGoodsName());//商品名称

			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}

			setValue2(bodyRow, centerStyle, 13, tallyingUnit);//单位
			setValue2(bodyRow, centerStyle, 14, model.getNum());//数量
			
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOrderCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOrderCurrency());
			}
			setValue2(bodyRow, centerStyle, 15, orderCurrency);//采购币种
			String warehouseCurrency=null;
			if (StringUtils.isNotBlank(model.getWarehouseCurrency())) {
				warehouseCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getWarehouseCurrency());
			}
			setValue2(bodyRow, centerStyle, 16, warehouseCurrency);//入库币种
			setValue2(bodyRow, centerStyle, 17, model.getSdTgtAmount());//采购SD金额（本币）
			setValue2(bodyRow, centerStyle, 18, model.getDepotName());//入库仓库				
			// 记录行数
			rowNum += 1;
		}	
		decreasePurchaseNotshelfList=null;
		**/
		// sheet 17
		Sheet excel17 = workbook.createSheet(sheetName17);
		Row headrow17 = excel17.createRow(0);
		k = 0;
		for (String title : columns17) { // 设置表头
			Cell cell = headrow17.createCell(k);
			excel17.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		rowNum = 0;
		bodyRow = null;
		// 循环表头入库单信息
		for (int j17 = 0; j17 < addTransferNoshelfList.size(); j17++) { // 填值
			if (rowNum == 0) {
				rowNum = 1;
			}
			BuFinanceAddTransferNoshelfDetailsModel model = addTransferNoshelfList.get(j17);       	
			bodyRow = excel17.createRow(rowNum);
			setValue2(bodyRow, centerStyle, 0, model.getBuName());//事业部
			setValue2(bodyRow, centerStyle, 1, model.getSuperiorParentBrand());//母品牌
			setValue2(bodyRow, centerStyle, 2, model.getBrandName());//品牌
			setValue2(bodyRow, centerStyle, 3, model.getOrderCode());//调拨订单号
			setValue2(bodyRow, centerStyle, 4, model.getOutOrderCode());//调拨出单号
			setValue2(bodyRow, centerStyle, 5, model.getOutDepotName());//调出仓库
			setValue2(bodyRow, centerStyle, 6, model.getInDepotName());//调入仓库
			setValue2(bodyRow, centerStyle, 7, model.getDeliverDate());//出库时间
			setValue2(bodyRow, centerStyle, 8, model.getMonth());//归属月份
			setValue2(bodyRow, centerStyle, 9, model.getGoodsName());//商品名称
			setValue2(bodyRow, centerStyle, 10, model.getGoodsNo());//商品货号
			setValue2(bodyRow, centerStyle, 11, model.getBarcode());//条码
			setValue2(bodyRow, centerStyle, 12, model.getCommbarcode());//标准条码
			setValue2(bodyRow, centerStyle, 13, model.getPoNo());//PO号
			String tallyingUnit = "";//理货单位(00-托盘，01-箱，02-件)
			if (StringUtils.isNotBlank(model.getTallyingUnit())) {
				tallyingUnit = DERP.getLabelByKey(DERP.order_tallyingUnitList, model.getTallyingUnit());
			}
			setValue2(bodyRow, centerStyle, 14,tallyingUnit);//理货单位
			setValue2(bodyRow, centerStyle, 15, model.getNum());//在途数量
			String orderCurrency=null;
			if (StringUtils.isNotBlank(model.getOutDepotCurrency())) {
				orderCurrency = DERP.getLabelByKey(DERP.currencyCodeList, model.getOutDepotCurrency());
			}
			setValue2(bodyRow, centerStyle, 16,orderCurrency);//出库币种
			setValue2(bodyRow, centerStyle, 17, model.getSdOrderPrice());//本期SD单价
			setValue2(bodyRow, centerStyle, 18, model.getSdOrderAmount());//本期SD金额
			       					
			// 记录行数
			rowNum += 1;
		}	
		addTransferNoshelfList=null;	
		
		
		Sheet excel17A = workbook.createSheet(sheetName17A);
		Row headrow17A = excel17A.createRow(0);
		k = 0;
		for (String title : columns17A) { // 设置表头
			Cell cell = headrow17A.createCell(k);
			excel17A.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j17A = 0; j17A < saleChannelSummaryList.size(); j17A++) { // 填值
			Map model = saleChannelSummaryList.get(j17A);
			Row row = excel17A.createRow(j17A + 1);
			k = 0;
			for (String key : keys17A) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		saleChannelSummaryList=null;
		

		// sheet18				
		Sheet excel18 = workbook.createSheet(sheetName18);
		Row headrow18 = excel18.createRow(0);
		k = 0;
		for (String title : columns18) { // 设置表头
			Cell cell = headrow18.createCell(k);
			excel18.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}
		// 注入参数
		for (int j18 = 0; j18 < itemMoveList.size(); j18++) { // 填值
			Map model = itemMoveList.get(j18);
			Row row = excel18.createRow(j18 + 1);
			k = 0;
			for (String key : keys18) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		itemMoveList=null;
		// sheet18				
		Sheet excel19 = workbook.createSheet(sheetName19);
		Row headrow19 = excel19.createRow(0);
		k = 0;
		for (String title : columns19) { // 设置表头
			Cell cell = headrow19.createCell(k);
			excel19.setColumnWidth(k, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			k++;
		}		
		// 注入参数
		for (int j19 = 0; j19 < fgInventList.size(); j19++) { // 填值
			Map model = fgInventList.get(j19);
			Row row = excel19.createRow(j19 + 1);
			k = 0;
			for (String key : keys19) {
				Cell cell = row.createCell(k);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);
				if (model.get(key) instanceof Timestamp) {
					String value1 = model.get(key).toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				k++;
			}
		}		
		fgInventList=null;		
		
		return workbook;
	}
	
	
	
	
	private static BigDecimal getValueAmount(String key, BigDecimal value1,List<String> valueAmountkeyList) {				
		// 去除科学计数法
		if (value1.compareTo(BigDecimal.ZERO)==0) {
			return new BigDecimal(0);
		}
		if (valueAmountkeyList.contains(key)) {
			BigDecimal value2 = value1.setScale(8, BigDecimal.ROUND_HALF_UP);
			return value2;
		}else {
			BigDecimal value2 = value1.setScale(8, BigDecimal.ROUND_HALF_UP);
			return value2;
		}
		
		
	}

	/**
	 * 
	 * @param sheetName1
	 * @param accountShelfToB
	 * @param accountShelfToC
	 * @param stockGroupByType
	 * @return
	 */
	public static SXSSFWorkbook createBuAllAccount(
			String sheetName1,ArrayList<String> columnsList1,ArrayList<String> keyList1,List<Map<String,Object>>saleList,
			String sheetName2, ArrayList<String>columnsListOrder2,ArrayList<String>keyListOrder2,List<Map<String,Object>>orderList,
			                  ArrayList<String> columnsListItem2,ArrayList<String>keyListItem2, List<Map<String, Object>> orderItemList               
			) {		
		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		// 设置表头颜色
		CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式		
		CellStyle centerStyle = setBodyCellStyle(workbook, font);
		
		CellStyle sheetName1Style = workbook.createCellStyle();
		
		sheetName1Style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		sheetName1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用		
		sheetName1Style.setBorderBottom(BorderStyle.THIN); //下边框
		sheetName1Style.setBorderLeft(BorderStyle.THIN);//左边框
		sheetName1Style.setBorderTop(BorderStyle.THIN);//上边框
		sheetName1Style.setBorderRight(BorderStyle.THIN);//右边框
		sheetName1Style.setFont(font);
		sheetName1Style.setWrapText(true);

		Sheet excel1 = workbook.createSheet(sheetName1);
		excel1.setColumnWidth(0, 100 * 256);
		Row  headrow0 =  excel1.createRow(0);			
		Cell sheetName1cell = headrow0.createCell(0);	
		sheetName1cell.setCellStyle(sheetName1Style);
		sheetName1cell.setCellValue(
				"*导入须知:                                                                                                           "
				+ "1.表格中不能增、删、改列及固有内容                                                                                                                                                                                                                                                                                                             "
				+ "2.所有内容必须为文本格式;表格中有多个档案名称字段是为了实现多语,如果没有多语只录第一个名称字段即可                                                                                                                                                                         "
				+ "3.枚举项输入错误时，则按默认值处理;勾选框的导入需输入N、Y                                                                            "
				+ "4.导入带有子表的档案时,表格中主表与	子表之间必须有一空行,且主、子表对应数据需加上相同的行号                                                                                                                                                                                   "
				+ "5.辅助核算字段保存凭证分录的辅助核算信息，格式为“辅助核算值编码：辅助核算项目类型编码”                                                            "
				+ "例如：002:0004。前面的002表示辅助核算值编码，后面的0004表示辅助核算项目类型编码                                                                                                                                                                                                      "
				+ "如果想导入总账，需按照上述例子中的格式填好辅助核算值编码和辅助核算类型编码                                                                                                                                                                                                                                   ");
		


		Row headrow1 = excel1.createRow(1);
		int i = 0;
		for (String title : columnsList1) { // 设置表头
			Cell cell = headrow1.createCell(i);
			if (i==0) {
				excel1.setColumnWidth(0, 97 * 256);	
			}else {
				excel1.setColumnWidth(i, 20 * 256);
			}
			
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j1 = 0; j1 < saleList.size(); j1++) { // 填值
			Map model = saleList.get(j1);	
			Row row = excel1.createRow(j1 + 2);
			i = 0;						
			for (String key : keyList1) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);								
				if (obj instanceof Timestamp) {
					String value1 = obj.toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (obj instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(obj.toString());		
					cell.setCellValue(value1.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		int saleSize= saleList.size();
		
		String saleSheetName1Parm2="\"cashflow,m_flag,cashflowcurr,m_money,m_moneymain,m_moneygroup,m_moneyglobal,cashflowinnercorp,cashflowName,cashflowCode\"";
    	ArrayList<String> columnsList1parm2 = new ArrayList<String>(){{add(saleSheetName1Parm2);add("方向");add("分析币种");add("原币");add("组织本币");
    	add("集团本币");add("全局本币");add("内部单位");add("现金流量名称");add("现金流量编码");
    	}};
		
    	Row headrowSaleSize = excel1.createRow(saleSize+3);
		i = 0;
		for (String title : columnsList1parm2) { // 设置表头
			Cell cell = headrowSaleSize.createCell(i);
			if (i==0) {
				excel1.setColumnWidth(0, 97 * 256);	
			}else {
				excel1.setColumnWidth(i, 20 * 256);
			}
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		
		
		Sheet excel2 = workbook.createSheet(sheetName2);		
		Row  head2row0 =  excel2.createRow(0);	
		excel2.setColumnWidth(0, 97 * 256);	
		Cell sheetName2cell = head2row0.createCell(0);
		sheetName2cell.setCellStyle(sheetName1Style);
		sheetName2cell.setCellValue(
				"\"*导入须知:                                                                                                               "
				+ "1.表格中不能增、删、改列及固有内容                                                                                                                                                                                                                                                                                                                                  "
				+ "2.所有内容必须为文本格式;表格中有多个档案名称字段是为了实现多语,如果没有多语只录第一个名称字段即可                                                                                                                                                                                                "
				+ "3.枚举项输入错误时，则按默认值处理;勾选框的导入需输入N、Y                                                                                  "
				+ "4.导入带有子表的档案时,表格中主表与子表之间必须有一空行,且主、子表对应数据需加上相同的行号\"                                                             ");
		


		Row head2row1 = excel2.createRow(1);
		i = 0;
		for (String title : columnsListOrder2) { // 设置表头
			Cell cell = head2row1.createCell(i);
			if (i==0) {
				excel2.setColumnWidth(0, 97 * 256);	
			}else {
				excel2.setColumnWidth(i, 20 * 256);
			}	
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j1 = 0; j1 < orderList.size(); j1++) { // 填值
			Map model = orderList.get(j1);	
			Row row = excel2.createRow(j1 + 2);
			i = 0;						
			for (String key : keyListOrder2) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);								
				if (obj instanceof Timestamp) {
					String value1 = obj.toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (obj instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(obj.toString());		
					cell.setCellValue(value1.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		
		int size = orderList.size();
		
		
		// 表体
		
		Row head2rowsize = excel2.createRow(size+3);
		i = 0;
		for (String title : columnsListItem2) { // 设置表头
			Cell cell = head2rowsize.createCell(i);
			if (i==0) {
				excel2.setColumnWidth(0, 97 * 256);	
			}else {
				excel2.setColumnWidth(i, 20 * 256);
			}
			
			
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j1 = 0; j1 < orderItemList.size(); j1++) { // 填值
			Map model = orderItemList.get(j1);	
			Row row = excel2.createRow(size + 4);
			i = 0;						
			for (String key : keyListItem2) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle);
				Object obj = model.get(key);								
				if (obj instanceof Timestamp) {
					String value1 = obj.toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (obj instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(obj.toString());		
					cell.setCellValue(value1.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
			size++;
		}
		
		return workbook;
	}
	/**
	 * 事业部财务经销存暂估应收导出数据
	 * @param columnsList
	 * @param keyList
	 * @param pdfToBList
	 * @param pdfToCList
	 * @return
	 */
	public static SXSSFWorkbook createBuTempEstimate(ArrayList<String> columnsList, ArrayList<String> keyList,
			List<Map<String, Object>> pdfToBList, List<Map<String, Object>> pdfToCList,
			String merchantNameParm,String creatDate,String oneCellvale) {
		SXSSFWorkbook workbook = new SXSSFWorkbook(7000);
		Font font16 = workbook.createFont();
		font16.setFontName("宋体");
		font16.setFontHeightInPoints((short)14);//设置字体大小
		Font font11 = workbook.createFont();
		font11.setFontName("宋体");
		font11.setFontHeightInPoints((short)10);//设置字体大小 HSSFColor.WHITE
		// 设置表头颜色
		//CellStyle cellStyle = setHeadCellStyle(workbook, font, IndexedColors.GREY_25_PERCENT.getIndex());
		//设置表体内容格式	
		CellStyle centerStyle16 = workbook.createCellStyle();
		centerStyle16.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		centerStyle16.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		centerStyle16.setFont(font16);
		CellStyle centerStyle11 = workbook.createCellStyle();
		centerStyle11.setFont(font11);

		// 带边框 带背景 字体11
		CellStyle cellStyleWHITE11 = setHeadCellStyle(workbook, font11, IndexedColors.GREY_25_PERCENT.getIndex());
		// 带边框字体11
		CellStyle centerStyle11Bk = workbook.createCellStyle();
		centerStyle11Bk.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		centerStyle11Bk.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		centerStyle11Bk.setFont(font11);
		centerStyle11Bk.setBorderBottom(BorderStyle.THIN); //下边框
		centerStyle11Bk.setBorderLeft(BorderStyle.THIN);//左边框
		centerStyle11Bk.setBorderTop(BorderStyle.THIN);//上边框
		centerStyle11Bk.setBorderRight(BorderStyle.THIN);//右边框
		centerStyle11Bk.setWrapText(true); 


		Sheet excel1 = workbook.createSheet("Sheet1");
		Row  headrow0 =  excel1.createRow(0);// 第1行
		for (int i = 0; i < 6; i++) {
			Cell sheetName1cell = headrow0.createCell(i);	
			sheetName1cell.setCellStyle(centerStyle16);
			sheetName1cell.setCellValue(oneCellvale);
		}
			
		CellRangeAddress region1A = new CellRangeAddress(0, 0, (short) 0, (short) 5);
		excel1.addMergedRegion(region1A);// 合并第3行单元格				
		Row  headrow2 =  excel1.createRow(2);// 第3行
		Cell sheetName2cell0 = headrow2.createCell(0);
		sheetName2cell0.setCellValue(merchantNameParm);
		Cell sheetName2cel4 = headrow2.createCell(4);
		sheetName2cel4.setCellValue("创建时间:"+creatDate);
		/*Cell sheetName2cel5 = headrow2.createCell(5);
		sheetName2cel5.setCellValue(creatDate);*/
		
		Row  headrow5 =  excel1.createRow(4);// 第5行
		for (int i = 0; i < 6; i++) {
			Cell sheetName5cell = headrow5.createCell(i);
			sheetName5cell.setCellStyle(cellStyleWHITE11);
			sheetName5cell.setCellValue("渠道类型：To B");
		}
		
		CellRangeAddress region2A = new CellRangeAddress(4, 4, (short) 0, (short) 5);
		excel1.addMergedRegion(region2A);// 合并第5行单元格


		Row headrow6 = excel1.createRow(5);
		int i = 0;
		//
		for (String title : columnsList) { // 设置表头
			Cell cell = headrow6.createCell(i);
			
			if (i==0) {
				excel1.setColumnWidth(i, 23 * 256);	
			}else if (i==1) {
				excel1.setColumnWidth(i, 20 * 256);	
			}else if (i==2) {
				excel1.setColumnWidth(i, 9 * 256);	
			}else if (i==3) {
				excel1.setColumnWidth(i, 9 * 256);	
			}else if (i==4) {
				excel1.setColumnWidth(i, 11 * 256);	
			}else if (i==5) {
				excel1.setColumnWidth(i, 9 * 256);	
			}
					
			cell.setCellStyle(centerStyle11Bk);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j1 = 0; j1 < pdfToBList.size(); j1++) { // 填值
			Map model = pdfToBList.get(j1);	
			Row row = excel1.createRow(j1 + 6);
			i = 0;						
			for (String key : keyList) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle11Bk);
				Object obj = model.get(key);								
				if (obj instanceof Timestamp) {
					String value1 = obj.toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (obj instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(obj.toString());		
					cell.setCellValue(value1.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		int saleSize= pdfToBList.size();
		

		Row  headrowsaleSize8 =  excel1.createRow(saleSize+8);// 第saleSize+8行
		for (int j = 0; j < 6; j++) {
			Cell sheetNameSize8cell = headrowsaleSize8.createCell(j);
			sheetNameSize8cell.setCellStyle(cellStyleWHITE11);
			sheetNameSize8cell.setCellValue("渠道类型：To C");
		}
		
		CellRangeAddress regionsaleSize8A = new CellRangeAddress(saleSize+8, saleSize+8, (short) 0, (short) 5);
		excel1.addMergedRegion(regionsaleSize8A);// 合并第regionsaleSize+8行单元格
		
		
		
    	Row headrowSaleSize = excel1.createRow(saleSize+9);
		i = 0;
		for (String title : columnsList) { // 设置表头
			Cell cell = headrowSaleSize.createCell(i);
			cell.setCellStyle(centerStyle11Bk);
			cell.setCellValue(title);
			i++;
		}
		
		for (int j1 = 0; j1 < pdfToCList.size(); j1++) { // 填值
			Map model = pdfToCList.get(j1);	
			Row row = excel1.createRow(j1 + saleSize+10);
			i = 0;						
			for (String key : keyList) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(centerStyle11Bk);
				Object obj = model.get(key);								
				if (obj instanceof Timestamp) {
					String value1 = obj.toString();
					String intNumber = value1.substring(0, value1.indexOf("."));
					cell.setCellValue(intNumber);
				} else if (obj instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(obj.toString());		
					cell.setCellValue(value1.toString());
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		
		
		
		
		
		
		
		return workbook;
	}
	

	
}