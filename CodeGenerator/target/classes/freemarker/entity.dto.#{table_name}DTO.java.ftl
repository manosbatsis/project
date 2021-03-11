package ${package}.entity.dto;
import java.util.Date;

public class ${table_name}DTO {

	<#list table_columns as column>	 
	 //${column.columnComments} <#if column.isPrimary = "1">( 主键 )</#if> 	
	private ${column.dataType} ${column.dataName};
	</#list>
	
	<#list table_columns as column>	 	
	public ${column.dataType} get${column.dataName?cap_first}() {
		return ${column.dataName};
	}
	
	public void set${column.dataName?cap_first}(${column.dataType} ${column.dataName}) {
		this.${column.dataName} = ${column.dataName};
	}
	</#list>
}