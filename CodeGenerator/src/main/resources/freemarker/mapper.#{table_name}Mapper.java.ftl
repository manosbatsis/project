package ${package}.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ${package}.entity.model.${table_name}Model;


public interface ${table_name}Mapper {

	/**
	 * 添加
	 * @param ${table_name?uncap_first}Model
	 * @return
	 */
	public int insert(${table_name}Model ${table_name?uncap_first}Model);

	/**
	 * 查询列表
	 * @param ${table_name?uncap_first}Model
	 * @return
	 */
	public List<${table_name}Model> list(${table_name}Model ${table_name?uncap_first}Model);
	
	/**
	 * 根据主键查询
	 * @param ${j_primary_key}
	 * @return
	 */
	public ${table_name}Model getById(Long ${j_primary_key});
	
	/**
	 * 根据条件查询
	 * @param ${j_primary_key}
	 * @return
	 */
	public ${table_name}Model getByModel(${table_name}Model ${table_name?uncap_first}Model);
	
	/**
	 * 根据主键删除
	 * @param ${j_primary_key}
	 * @return
	 */
	public int deleteById(Long ${j_primary_key});
	
	/**
	 * 批量删除
	 * @param ${j_primary_key}
	 * @return
	 */
	public int BatchDelete(List ids);

}