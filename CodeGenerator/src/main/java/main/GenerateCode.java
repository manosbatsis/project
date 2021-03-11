package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.CodeColumn;
import model.CodeTable;
import utils.FileUtils;
import utils.JDBCUtils;
import utils.MySqlType;
import utils.SelectSQLs;
import utils.StringUtill;

public class GenerateCode {	
	
	public void Generate(String[] tables,String dbName,String dirpath,String packagePath) throws Exception {		
		List<CodeTable> tableList = new ArrayList<CodeTable>();;
		for (String table : tables) {
			List<CodeColumn> codeColumnList = null;
			CodeTable codeTable = new CodeTable();
			codeTable.setTableName(table);
			codeColumnList = queryCodeColumnList(table,dbName);
			codeTable.setColumns(codeColumnList);
			tableList.add(codeTable);
		}
		
		// 将表、列原数据加工下
		polishTableList(tableList, dbName);
		
		// 模板路径
		String freemarkpath;
		freemarkpath = URLDecoder.decode(this.getClass().getResource("/freemarker").getPath(), "UTF-8");
		doMainProcess(tableList, dirpath, packagePath, freemarkpath);
		
	}
	/**
	 * 获取表字段集合
	 * @param tableName
	 * @param dbName
	 * @return
	 */
	private List<CodeColumn> queryCodeColumnList(String tableName,String dbName){
		List<CodeColumn> codeColumnList = new ArrayList<CodeColumn>();
		Connection conn = JDBCUtils.getConnection();
		String sqlColum = "";
		if(JDBCUtils.getDbType().equals("MYSQL")) {
			sqlColum =SelectSQLs.MYSQL_QUERY_COLUMNS;
		}else if(JDBCUtils.getDbType().equals("ORACLE")){
			sqlColum =SelectSQLs.ORACLE_QUERY_COLUMNS;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sqlColum);
			// 执行动态sql
			pstmt.setString(1, tableName);
			pstmt.setString(2, dbName);
			ResultSet rs = pstmt.executeQuery();

			// 封装结果
			CodeColumn codeColumn = null;
			while (rs.next()) {
				codeColumn = new CodeColumn();
				codeColumn.setColumnName(rs.getString(1));
				codeColumn.setColumnType(rs.getString(2));
				codeColumn.setColumnComments(rs.getString(3));
				codeColumn.setIsPrimary(rs.getString(4));
				codeColumnList.add(codeColumn);
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}finally {
			JDBCUtils.release(conn, pstmt, null);
		}
		return codeColumnList;
	}
	
	/**
	 * @Title: polishTableList
	 * @Description:将数据库获取的表、列数据，转化成模板需要的形式
	 * @param codeTableList
	 * @return boolean 返回类型
	 */
	private boolean polishTableList(List<CodeTable> codeTableList, String databaseType) {
		
		for (CodeTable codeTable : codeTableList) {

			// 将待下划线的表名改成驼峰命名的存入TableNamePolished中
			codeTable.setTableNamePolished(StringUtill.camelTableName(codeTable.getTableName().substring(2)));

			String jdbcType = "";// 对应的列在数据库中的类型
			String javaType = "";// 对应的列在java中的类型
			for (CodeColumn codeColumn : codeTable.getColumns()) {
				jdbcType = codeColumn.getColumnType().toUpperCase();
				javaType = MySqlType.valueOf(jdbcType).getJavaType();
				codeColumn.setDataType(javaType);
				codeColumn.setDataName(StringUtill.camelColumnName(codeColumn.getColumnName()));
				if ("desc".equals(codeColumn.getColumnName())) {
					codeColumn.setColumnNameKeep("`desc`");
				}
				if ("status".equals(codeColumn.getColumnName())) {
					codeColumn.setColumnNameKeep("`status`");
				}
			
				// 如果是主键，将其提取出放入表对象的主键列属性中
				// 只能有一个主键
				if ("1".equals(codeColumn.getIsPrimary())) {
					codeTable.setMainColumn(codeColumn);
				}				
			}

			// 如果找不到主键，将第一列作为主键
			if (null == codeTable.getMainColumn()) {
				codeTable.setMainColumn(codeTable.getColumns().get(0));
			}
		}

		return true;
	}

	public boolean doMainProcess(List<CodeTable> codeTableList,String path,String packagePath,String freemarkpath) throws Exception {
		Configuration cfg = new Configuration();
		File freemarkerTemps = new File(freemarkpath);
		// 加载freemarker模板文件
		cfg.setDirectoryForTemplateLoading(freemarkerTemps);
		
		// 设置对象包装器
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		// 设计异常处理器
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		// 获取文件列表，遍历
		File[] templetes = freemarkerTemps.listFiles();
		for (File file : templetes) {
			for (CodeTable codeTable : codeTableList) {
				String TempleteDestFilePath = getTempleteDestFilePath(file.getName(), codeTable, path,packagePath);
				if (StringUtill.isEmptyString(TempleteDestFilePath)) {
					continue;
				}
				if (!createTempleteDestFile(TempleteDestFilePath, file.getName(), codeTable, cfg,packagePath)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private String getTempleteDestFilePath(String freemarkTempleteName, CodeTable codeTable,String path,String packagePath) {
		String replacedTemplete = freemarkTempleteName;		
		replacedTemplete = replacedTemplete.replace("#{table_name}", codeTable.getTableNamePolished());
		replacedTemplete = packagePath + "." +replacedTemplete;
		String templeteDestPath = "";
		String[] tempNamePaths = replacedTemplete.split("\\.");
		
		// 文件后缀不为ftl，非正确模板
		if (!"ftl".equals(tempNamePaths[tempNamePaths.length - 1])) {
			return null;
		}		
		// 当前文件ftl命名方式为  包名.类名.类型.ftl，这一步获取包名
		if (tempNamePaths.length >= 3) {
			String[] tempPath = new String[tempNamePaths.length - 3];
			for (int i = 0; i < tempNamePaths.length - 3; i++)
				tempPath[i] = tempNamePaths[i];
			// 在项目路径下创建模板对应的文件夹路径
			// 判断生成文件格式
			templeteDestPath = FileUtils.createDirByPackge(tempPath, path);
		}
		// ftl前的两个字符串组成目标文件名称
		String templeteDestName = tempNamePaths[tempNamePaths.length - 3] + "." + tempNamePaths[tempNamePaths.length - 2];
		return templeteDestPath + templeteDestName;
	}
	
	private boolean createTempleteDestFile(String templeteDestFilePath, String freemarkTempleteName, CodeTable codeTable, Configuration cfg,String packagePath) {
		if (StringUtill.isEmptyString(templeteDestFilePath) || StringUtill.isEmptyString(freemarkTempleteName)) return false;
		// 最终模板需要转换的目标文件
		File templeteDestFile = new File(templeteDestFilePath);

		// 定义并设置数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("package", packagePath);
		data.put("code_table", codeTable);
		data.put("raw_table", codeTable.getTableName());
		data.put("table_name", codeTable.getTableNamePolished());
		data.put("table_columns", codeTable.getColumns());
		data.put("primary_key", codeTable.getMainColumn().getColumnName());
		data.put("primary_type", codeTable.getMainColumn().getDataType());
		data.put("j_primary_key", codeTable.getMainColumn().getDataName());

		Template template = null;
		// 获取指定模板文件
		try {
			template = cfg.getTemplate(freemarkTempleteName);
			// 定义输入文件，默认生成在工程根目录
			Writer out = new OutputStreamWriter(new FileOutputStream(templeteDestFile), "UTF-8");
			// 最后开始生成
			template.process(data, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
