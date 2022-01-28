package com.topideal.order.service.platformdata.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.platformdata.OrealPurchaseOrderDao;
import com.topideal.dao.platformdata.OrealPurchaseOrderItemDao;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.order.service.platformdata.OrealPurchaseOrderService;


/**
 * 欧莱雅订单管理
 */
@Service
public class OrealPurchaseOrderServiceImpl implements OrealPurchaseOrderService {

	@Autowired
	public OrealPurchaseOrderDao orealPurchaseOrderDao;
	@Autowired
	public OrealPurchaseOrderItemDao orealPurchaseOrderItemDao;
	/**
	 * 分页查询
	 */
	@Override
	public OrealPurchaseOrderDTO getListByPage(OrealPurchaseOrderDTO dto) throws Exception {
		return orealPurchaseOrderDao.getListByPage(dto);
	}
	
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws Exception
	 */
    @Override
    public List<Map<String, Object>> getExportList(OrealPurchaseOrderDTO dto) throws Exception {
        return orealPurchaseOrderDao.getExportList(dto);
    }

	@Override
	public OrealPurchaseOrderDTO searchDTODetail(OrealPurchaseOrderDTO model) throws Exception {
		return  orealPurchaseOrderDao.searchDTODetail(model);
	}

	@Override
	public List<OrealPurchaseOrderItemModel> getOrderItrmList(OrealPurchaseOrderItemModel model) throws Exception {
		return orealPurchaseOrderItemDao.list(model);
	}


   

}
