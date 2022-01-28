package com.topideal.order.service.bill.impl;

import java.sql.SQLException;

import com.topideal.dao.bill.PlatformCostOrderItemDao;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.bill.PlatformCostOrderDao;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.order.service.bill.PlatformCostOrderService;

/**
 * @Description: 平台费用service实现类
 * @Author: 杨创
 * @Date: 2020/08/31 15:01
 **/
@Service
public class PlatformCostOrderServiceImpl implements PlatformCostOrderService {

    @Autowired
    private PlatformCostOrderDao platformCostOrderDao;

	@Autowired
    private PlatformCostOrderItemDao platformCostOrderItemDao;

   
    /**
     * 分页查询
     */
    @Override
    public PlatformCostOrderDTO listLatformCostOrderByPage(PlatformCostOrderDTO dto) throws SQLException {
    	PlatformCostOrderDTO platformCostOrderDTO = platformCostOrderDao.listLatformCostOrderByPage(dto);
        return platformCostOrderDTO;
    }

    /**
     * 获取详情分页
     */
	@Override
	public PlatformCostOrderItemModel listLatformCostOrderItemByPage(PlatformCostOrderItemModel model) throws SQLException {
		model = platformCostOrderDao.listLatformCostOrderItemByPage(model);
		return model;
	}

	@Override
	public PlatformCostOrderItemDTO listPlatformCostOrderItemByPage(PlatformCostOrderItemDTO dto) throws SQLException {
		return platformCostOrderItemDao.listPlatformCostOrderDTOByPage(dto);
	}

	/**
	 * 获取详情
	 */
	@Override
	public PlatformCostOrderDTO getDetails(PlatformCostOrderDTO dto) throws SQLException {
		return platformCostOrderDao.getDetails(dto);
	}

	

	


 
}
