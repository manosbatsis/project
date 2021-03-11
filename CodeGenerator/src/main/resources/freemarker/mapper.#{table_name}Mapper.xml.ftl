<?xml version="1.0" encoding="UTF-8" ?> 
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.mapper.${table_name}Mapper">
	
	<resultMap type="${table_name}Model" id="${table_name}Map">
		<#list table_columns as column>
			<result property="${column.dataName}" column="${column.columnName}" />
		</#list>
	</resultMap>

	<sql id="sql_columns">
		<#list table_columns as column>
			${column.columnName}<#if column_has_next>,</#if>
		</#list>
	</sql>
	
	<sql id="sql_where">
		<where>
            <trim suffixOverrides='and'>
			<#list table_columns as column>
				<#if column.dataType == "String">
					<if test='${column.dataName} != null and !"".equals(customerName)'> ${column.columnName} = #${r"{"}${column.dataName}} and </if>
				<#else>	
					<if test='${column.dataName} != null'> ${column.columnName} = #${r"{"}${column.dataName}} and </if>
				</#if>	
			</#list>
			</trim>
        </where>
	</sql>
	
	<!-- 添加 -->
	<insert id="insert" parameterType="${table_name}Model" keyProperty="id" useGeneratedKeys="true">
		INSERT INTO ${raw_table}
		<trim prefix="(" suffix=")" suffixOverrides="," >
			<#list table_columns as column>
				<#if column.dataType == "String">
					<if test='${column.dataName} != null and !"".equals(customerName)'> ${column.columnName},</if>
				<#else>	
					<if test='${column.dataName} != null'> ${column.columnName},</if>
				</#if>
			</#list>
		</trim>
		VALUES 
		<trim prefix="(" suffix=")" suffixOverrides="," >
			<#list table_columns as column>
				<#if column.dataType == "String">
					<if test='${column.dataName} != null and !"".equals(customerName)'> #${r"{"}${column.dataName}}, </if>
				<#else>	
					<if test='${column.dataName} != null'> #${r"{"}${column.dataName}}, </if>
				</#if>							
			</#list>
		</trim>
	</insert>
	
	<!-- 修改 -->
	<update id="update" parameterType="${table_name}Model" keyProperty="id" useGeneratedKeys="true">
		UPDATE 
			${raw_table} 
		SET
		<trim prefix="(" suffix=")" suffixOverrides="," >
			<#list table_columns as column>
				<#if column.dataType == "String">
					<if test='${column.dataName} != null and !"".equals(customerName)'> ${column.columnName} = #${r"{"}${column.dataName}}, </if>
				<#else>	
					<if test='${column.dataName} != null'> ${column.columnName} = #${r"{"}${column.dataName}}, </if>
				</#if>							
			</#list>
		</trim>
		WHERE 
			${primary_key} = #${r"{"}${j_primary_key}}
	</update>
	
	<!-- 查询所有信息 -->
    <select id="list" resultMap="${table_name}Map" parameterType="${table_name}Model">
        select <include refid='sql_columns' /> from ${raw_table}  <include refid="sql_where" />
    </select>

	<!-- 根据主键查询 -->
	<select id="getById" parameterType="java.lang.Long" resultMap="${table_name}Map">
		SELECT <include refid="sql_columns" /> FROM ${raw_table} WHERE ${primary_key} = #${r"{"}${j_primary_key}}
	</select>
	
	<!-- 根据条件查询 -->
	<select id="getByModel" parameterType="${table_name}Model" resultMap="${table_name}Map">
		SELECT <include refid="sql_columns" /> FROM ${raw_table} WHERE <include refid="sql_where" />
	</select>
	
	<!-- 根据主键删除 -->
	<delete id="deleteById" parameterType="java.lang.Long">
        DELETE FROM ${raw_table}  WHERE ${primary_key} = #${r"{"}${j_primary_key}}
    </delete>
	
	<!-- 根据主键批量删除 -->
	<delete id="BatchDelete" parameterType="java.util.ArrayList">
        DELETE FROM ${raw_table}  WHERE ${primary_key} in 
        <foreach collection='list' item='id' separator=',' open='(' close=')'>
            #{id}
        </foreach>        
    </delete>
</mapper>