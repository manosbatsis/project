package com.topideal.mapper.inventory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.inventory.MonthlyAccountModel;

/**
 * 月结账单 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface MonthlyAccountMapper extends BaseMapper<MonthlyAccountModel> {


   
   /**
    * 统计货品货号在仓库的月结库存 
    * @param paramMap
    * @return
    * @throws SQLException
    */
   Map<String, Object> getBeginSumByMonthlyAccountToGoodsNo(Map<String, Object> paramMap) throws SQLException;


   /**
    * 统计商家、仓库、指定时间的月结明细
    */
   public List<Map<String, Object>> getMonthlyDetails(Map<String, Object> map);
   


	/**
	 * 月结自动校验按条码统计
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMonthListGroupByBarcode(Map<String, Object> params);
}

