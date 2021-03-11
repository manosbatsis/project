package ${package}.dao.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ${package}.dao.${table_name}Dao;
import ${package}.mapper.${table_name}Mapper;
import ${package}.entity.model.${table_name}Model;

@Repository
public class ${table_name}DaoImpl implements ${table_name}Dao{

	@Autowired
    private ${table_name}Mapper mapper;
	
	/**
	 * 添加
	 * @param ${table_name?uncap_first}Model
	 * @return
	 */
	public int insert (${table_name}Model ${table_name?uncap_first}Model){
		return mapper.insert(${table_name?uncap_first}Model);
	}
	/**
	 * 查询列表
	 * @param ${table_name?uncap_first}Model
	 * @return
	 */
	public List<${table_name}Model> list(${table_name}Model ${table_name?uncap_first}Model){
		return mapper.list(${table_name?uncap_first}Model);
	}
	
	/**
	 * 根据主键查询
	 * @param ${j_primary_key}
	 * @return
	 */
	public ${table_name}Model getById(Long ${j_primary_key}){
		return mapper.getById(${j_primary_key});
	}

	/**
	 * 根据条件查询
	 * @param ${j_primary_key}
	 * @return
	 */
	public ${table_name}Model getByModel(${table_name}Model ${table_name?uncap_first}Model){
		return mapper.getByModel(${table_name?uncap_first}Model);
	}
	
	/**
	 * 根据主键删除
	 * @param ${j_primary_key}
	 * @return
	 */
	public int deleteById(Long ${j_primary_key}){
		return mapper.deleteById(${j_primary_key});
	}
	
	/**
	 * 批量删除
	 * @param ${j_primary_key}
	 * @return
	 */
	public int BatchDelete(List ids){
		return mapper.BatchDelete(ids);
	}
}