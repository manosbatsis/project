package com.topideal.tools;


/**
 * 子模块导出特殊excel工具类，导出通用型excel请使用ExcelUtilXlsx通用类
 */
public class DownloadExcelUtil {
	
	/*public static List<Map<String, String>> parseExcel(InputStream inStream, String[] fieldNameList) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			XSSFWorkbook book = new XSSFWorkbook(inStream);
			XSSFSheet sheet = book.getSheetAt(0);
			// 1：去除第一行标题
			int rows = sheet.getPhysicalNumberOfRows();
			XSSFRow row;
			Map<String, String> map;
			for (int i = 1; i < rows; i++) {
				row = sheet.getRow(i);
				if (row == null || row.getCell(0) == null || !StringUtils.isNotBlank(row.getCell(0).toString())) {
					continue;
				}
				map = new HashMap<String, String>();
				for (int j = 0; j < fieldNameList.length; j++) {
					XSSFCell cell = row.getCell(j);// 获取单元格对象
					map.put(fieldNameList[j], getStringCellValue(cell).trim());
				}
				resultList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultList;
	}*/
	
	/**
	 * 解析Excel文件
	 * 
	 * @param inStream
	 *            Excel文件的输入流
	 * @param fieldNameList
	 *            属性名集合，顺序与Excel中列的顺序一致
	 * @return Map类型的集合，其中Key为属性名，Value可以理解为属性值（实际为Excel中的列值）
	 */
	/*public static List<Map<String, String>> parseExcel(InputStream inStream, String[] fieldNameList, int bookNum) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			XSSFWorkbook book = new XSSFWorkbook(inStream);
			XSSFSheet sheet = book.getSheetAt(bookNum);
			// 1：去除第一行标题
			int rows = sheet.getPhysicalNumberOfRows();
			XSSFRow row;
			Map<String, String> map;
			for (int i = 1; i < rows; i++) {
				row = sheet.getRow(i);
				if (row == null || row.getCell(0) == null || !StringUtils.isNotBlank(row.getCell(0).toString())) {
					continue;
				}
				map = new HashMap<String, String>();
				if (bookNum == 0) {
					for (int j = 0; j < fieldNameList.length - 3; j++) {
						XSSFCell cell = row.getCell(j);// 获取单元格对象
						map.put(fieldNameList[j], getStringCellValue(cell).trim());
					}
				} else {
					for (int j = 0; j < fieldNameList.length; j++) {
						XSSFCell cell = row.getCell(j);// 获取单元格对象
						map.put(fieldNameList[j], getStringCellValue(cell).trim());
					}
				}
				resultList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultList;
	}*/

	/**
	 * 组合商品--解析Excel文件
	 * 
	 * @param inStream
	 *            Excel文件的输入流
	 * @param fieldNameList
	 *            属性名集合，顺序与Excel中列的顺序一致
	 * @return Map类型的集合，其中Key为属性名，Value可以理解为属性值（实际为Excel中的列值）
	 */
	/*public static List<Map<String, String>> parseToExcel(InputStream inStream, String[] fieldNameList, int bookNum) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			XSSFWorkbook book = new XSSFWorkbook(inStream);
			XSSFSheet sheet = book.getSheetAt(bookNum);
			// 1：去除第一行标题
			int rows = sheet.getPhysicalNumberOfRows();
			XSSFRow row;
			Map<String, String> map;
			for (int i = 1; i < rows; i++) {
				row = sheet.getRow(i);
				if (row == null || row.getCell(0) == null || !StringUtils.isNotBlank(row.getCell(0).toString())) {
					continue;
				}
				map = new HashMap<String, String>();
				
				for (int j = 0; j < fieldNameList.length; j++) {
					XSSFCell cell = row.getCell(j);// 获取单元格对象
					map.put(fieldNameList[j], getStringCellValue(cell).trim());
				}
				
				resultList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultList;
	}*/
	
	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	/*private static String getStringCellValue(XSSFCell cell) {
		String strCell = "";
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 如果是date类型则 ，获取该cell的date值
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				strCell = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			} else { // 纯数字
				DecimalFormat df = new DecimalFormat("#.00");
				String tmp = df.format(cell.getNumericCellValue());
				if (tmp != null) {
					if (tmp.equals(".00")) {
						tmp = "0";
					} else {
						String end = tmp.substring(tmp.indexOf(".") + 1, tmp.length());
						if (end != null && end.equals("00")) {
							tmp = tmp.substring(0, tmp.indexOf("."));
						}
					}
				}
				strCell = tmp;
			}
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}

		return strCell;
	}*/

	/**
	 * 解析Excel文件
	 * 
	 * @param file
	 *            Excel文件
	 * @param fieldNameList
	 *            属性名集合，顺序与Excel中列的顺序一致
	 * @return Map类型的集合，其中Key为属性名，Value可以理解为属性值（实际为Excel中的列值）
	 */
	/*public static List<Map<String, String>> parseExcel(File file, String[] fieldNameList, int bookNum) {
		try {
			return parseExcel(new FileInputStream(file), fieldNameList, bookNum);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/
	
	/**
	 * @desc: 导出excel
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 */
	/*public static HSSFWorkbook createExcel(String sheetName, String[] columns, String[] keys,
			List<Map<String, Object>> values) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet excel = workbook.createSheet(sheetName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow headrow = excel.createRow(0);
		HSSFCellStyle cellStyle = workbook.createCellStyle();// 设置表头
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

		HSSFCellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
		textCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
		textCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

		HSSFCellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
		numCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平居右
		numCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置字体
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		cellStyle.setFont(font);
		textCellStyle.setFont(font);
		numCellStyle.setFont(font);
		int i = 0;
		for (String title : columns) { // 设置表头
			HSSFCell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j = 0; j < values.size(); j++) { // 填值
			Map model = values.get(j);
			HSSFRow row = excel.createRow(j + 1);
			i = 0;
			for (String key : keys) {
				HSSFCell cell = row.createCell(i);
				Object obj = model.get(key);
				
				if (model.get(key) instanceof Timestamp ) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
					String intNumber = sdf.format((Timestamp)model.get(key)) ;
					cell.setCellValue(intNumber);
					cell.setCellStyle(textCellStyle);
				}else if (model.get(key) instanceof BigDecimal ) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
					cell.setCellStyle(numCellStyle);
				}else if (model.get(key) instanceof Long ||model.get(key) instanceof Double || model.get(key) instanceof Integer) {
					cell.setCellValue(obj != null ? obj.toString() : "");
					cell.setCellStyle(numCellStyle);
				}else{
					cell.setCellValue(obj != null ? obj.toString() : "");
					cell.setCellStyle(textCellStyle);
				}
				i++;
			}
		}
		return workbook;
	}*/
	
	/**
	 * 创建 HSSFWorkbook
	 *
	 *            值
	 * @return HSSFWorkbook
	 */
	/*public static HSSFWorkbook createExcel(String sheetName, List<String> columns, List<Map<String, Object>> values) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet excel = workbook.createSheet(sheetName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow headrow = excel.createRow(0);
		HSSFCellStyle cellStyle = workbook.createCellStyle();// 设置表头
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

		HSSFCellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
		textCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
		textCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

		HSSFCellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
		numCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平居右
		numCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

		// 设置字体
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小
		cellStyle.setFont(font);
		textCellStyle.setFont(font);
		numCellStyle.setFont(font);
		int i = 0;
		for (String title : columns) { // 设置表头
			HSSFCell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		for (int j = 0; j < values.size(); j++) { // 填值
			Map model = values.get(j);
			HSSFRow row = excel.createRow(j + 1);
			i = 0;
			for (String key : columns) {
				HSSFCell cell = row.createCell(i);
				Object obj = model.get(key);

				if (model.get(key) instanceof Timestamp ) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
					String intNumber = sdf.format((Timestamp)model.get(key)) ;
					cell.setCellValue(intNumber);
					cell.setCellStyle(textCellStyle);
				}else if (model.get(key) instanceof BigDecimal ) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
					cell.setCellStyle(numCellStyle);
				}else if (model.get(key) instanceof Long ||model.get(key) instanceof Double || model.get(key) instanceof Integer) {
					cell.setCellValue(obj != null ? obj.toString() : "");
					cell.setCellStyle(numCellStyle);
				}else{
					cell.setCellValue(obj != null ? obj.toString() : "");
					cell.setCellStyle(textCellStyle);
				}
				i++;
			}
		}
		return workbook;
	}*/
	

	/**
	 * 导出excel通过列表对象
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param listObj
	 * @return
	 * @throws Exception
	 */
	/*public static HSSFWorkbook createExcelByObjList(String sheetName, String[] columns, String[] keys,
			List listObj) throws Exception {
		
		List<Map<String , Object>> mapList = new ArrayList<>() ;
		
		for (Object object : listObj) {
			Map<String , Object> map = entityToMap(object) ;
			
			mapList.add(map) ;
		}
		
		return createExcel(sheetName, columns, keys, mapList) ;
		
	}*/
	
	/**
	 * 实体类转Map
	 * @param object
	 * @return
	 */
	/*public static Map<String, Object> entityToMap(Object object) {
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
	}*/
	
	
	/***
	 * 创建sheet实例
	 * @param sheetName
	 * @param colums
	 * @param keys
	 * @param resultList
	 * @return
	 */
	/*public static ExportExcelSheet createSheet(String sheetName , String[] colums , String[] keys , List resultList) {
		ExportExcelSheet exportExcelSheet = new ExportExcelSheet() ;
		
		exportExcelSheet.setSheetNames(sheetName);
		exportExcelSheet.setColums(colums);
		exportExcelSheet.setKeys(keys);
		exportExcelSheet.setResultList(resultList);
		
		return exportExcelSheet ;
	}*/
	
	/**
	 * @desc: 通过列表对象导出单个sheet excel
	 * @param sheetName
	 * @param columns
	 * @param keys
	 * @param values
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	/*public static SXSSFWorkbook createSXSSExcelByObjList(String sheetName, String[] columns, String[] keys,
			List listObj) throws Exception {
		
		List<Map<String , Object>> mapList = new ArrayList<>() ;
		
		for (Object object : listObj) {
			Map<String , Object> map = entityToMap(object) ;
			
			mapList.add(map) ;
		}
		
		return createSXSSExcel(sheetName, columns, keys, mapList) ;
		
	}*/
	
	/*public static SXSSFWorkbook createSXSSExcel(String sheetName, String[] columns, String[] keys,
			List<Map<String, Object>> mapList) {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		Sheet excel = workbook.createSheet(sheetName);
		Row headrow = excel.createRow(0);
		CellStyle cellStyle = workbook.createCellStyle();
		// 设置字体
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
		cellStyle.setFont(font);
		// 设置背景颜色
		cellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
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
				} else if (model.get(key) instanceof BigDecimal) {
					BigDecimal value1 = new BigDecimal(model.get(key).toString());
					BigDecimal value2 = value1.setScale(2, BigDecimal.ROUND_HALF_UP);
					cell.setCellValue(value2.toString());
				} else if(model.get(key) instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
					String date = sdf.format((Date)model.get(key)) ;
					cell.setCellValue(date);
				} else {
					cell.setCellValue(obj != null ? obj.toString() : "");
				}
				i++;
			}
		}
		return workbook;
	}*/
	
	/**
	 * 导出结果为实体对象列表,多sheet
	 * @param sheetList
	 * @return
	 */
	/*public static SXSSFWorkbook createMutiSheetExcel(List<ExportExcelSheet> sheetList) {
		// 创建一个webbook，对应一个Excel文件
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		for (ExportExcelSheet exportExcelSheet : sheetList) {
			
			String sheetName = exportExcelSheet.getSheetNames() ;
			String[] columns = exportExcelSheet.getColums() ;
			String[] keys = exportExcelSheet.getKeys() ;
			List resultList = exportExcelSheet.getResultList() ;
			//创建sheet
			Sheet excel = workbook.createSheet(sheetName);
			
			Row headrow = excel.createRow(0);
			CellStyle cellStyle = workbook.createCellStyle();// 设置表头
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

			CellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
			textCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
			textCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

			CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
			numCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平居右
			numCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
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
				excel.setColumnWidth(i, 20 * 256);
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
					model = entityToMap(returnObj) ;
				}
				
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
					} else if (model.get(key) instanceof Long ||model.get(key) instanceof Double || model.get(key) instanceof Integer) {
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
	}*/
	
}