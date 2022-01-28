package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.entity.vo.bill.PlatformCostOrderModel;


public interface PlatformCostOrderDao extends BaseDao<PlatformCostOrderModel> {
		
    void batchUpdateOrderStatus(PlatformCostOrderModel model, List<Long> ids) throws Exception;


	 /**
    * 分页查询
    * @param model
    * @return
    */
    PlatformCostOrderDTO  listLatformCostOrderByPage(PlatformCostOrderDTO model)throws SQLException;
    
    /**
     * 获取详情分页
     * @param model
     * @return
     * @throws SQLException
     */
    PlatformCostOrderItemModel  listLatformCostOrderItemByPage(PlatformCostOrderItemModel model)throws SQLException;
	/**
	 * 获取详情
	 * @return
	 */
	PlatformCostOrderDTO getDetails(PlatformCostOrderDTO dto)throws SQLException;



    

}
