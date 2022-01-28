package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.PurchaseOrderDTO;
import com.topideal.entity.vo.order.PurchaseOrderModel;

/**
 * 采购订单 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrderModel> {
	
	/**
	 * 根据id获取供应商
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderModel> getSupplierIdById(@Param("id") Long id) throws SQLException;

	List<Map<String, Object>> getDepotVeriClByParam(Map<String, Object> param);

	/**
	 * 报表API获取采购订单
	 * @param queryMap
	 * @return
	 */
	List<PurchaseOrderDTO> getRepotApiList(Map<String, Object> queryMap);

	Integer getRepotApiListCount(Map<String, Object> queryMap);

	/** 
	 *	获取本月和前月的无入库本位币金额的采购单 
	 */
	List<PurchaseOrderDTO> getNoTgtAmountOrder();

	/**查询时间范围内有修改的采购单号
	 * */
	List<String> getPurchaseCodeList(Map<String,Object> map);
    /**根据单号查询订单
	 * */
	List<Map<String,Object>> getPurchaseByCodeList(Map<String,Object> map);
	/**根据单号查询订单表体
	 * */
	List<Map<String,Object>> getPurchaseItemByCodeList(Map<String,Object> map);
}

