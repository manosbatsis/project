package com.topideal.dao.purchase;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.DeclarePurchaseRelModel;

/**
 * 预申报单关联采购订单表
 * @author lianchenxing dao
 *
 */
public interface DeclarePurchaseRelDao extends BaseDao<DeclarePurchaseRelModel>{
		
	/**
	 * 根据采购订单id获取所有预申报的预计到港时间
	 * @param id
	 * @return
	 */
	List<DeclareOrderModel> getTimeById(Long id);

}

