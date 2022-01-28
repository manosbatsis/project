package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.entity.vo.reporting.SaleTargetModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleTargetMapper extends BaseMapper<SaleTargetModel> {

	/**
	 * 批量插入
	 * @param saleTargetList
	 * @return
	 */
	Integer batchSave(List<SaleTargetModel> list);
	
	/**
	 * 列表获取分页list
	 * @param dto
	 * @return
	 */
	List<SaleTargetDTO> getSaleTargetList(SaleTargetDTO dto);

	Integer getCountSaleTargetList(SaleTargetDTO dto);

	/**
	 * 获取导出列表
	 * @param queryDto
	 * @return
	 */
	List<SaleTargetDTO> getExpotList(SaleTargetDTO queryDto);

	/**
	 * 删除
	 * @param queryDto
	 * @return
	 */
	Integer delSaleTarget(SaleTargetDTO queryDto);

	/**
	 * 查询DTO列表
	 * @param dto
	 * @return
	 */
	List<SaleTargetDTO> listDto(SaleTargetDTO dto);

	/**
	 * 根据集合查询
	 * @param countMap
	 * @return
	 */
	Map<String, Object> countByMap(Map<String, Object> countMap);

	/**
	 * 获取销售计划表有记录的电商平台
	 * @param queryMap
	 * @return
	 */
	List<String> getHasPlatformData(Map<String, Object> queryMap);

	/**
	 * 获取销售计划表有记录的店铺
	 * @param queryMap
	 * @return
	 */
	List<String> getHasShopData(Map<String, Object> queryMap);



}
