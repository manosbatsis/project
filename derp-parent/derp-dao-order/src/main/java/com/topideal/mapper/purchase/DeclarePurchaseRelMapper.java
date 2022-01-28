package com.topideal.mapper.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.DeclarePurchaseRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 预申报单关联采购订单表
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface DeclarePurchaseRelMapper extends BaseMapper<DeclarePurchaseRelModel> {

	/**
	 * 根据采购订单id获取所有预申报的预计到港时间
	 * @param id
	 * @return
	 */
	List<DeclareOrderModel> getTimeById(@Param("id") Long id);

}

