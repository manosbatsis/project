package com.topideal.order.service.transfer.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.transfer.TransferOrderItemDao;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.order.service.transfer.TransferOrderItemService;

/**
 * 调拨订单service实现类
 * @author yucaifu
 */
@Service
public class TransferOrderItemServiceImpl implements TransferOrderItemService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TransferOrderItemServiceImpl.class);

	//调拨订单商品表
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	
	
	
	
	/**根据调拨订单id查询调拨商品
	 * @param dto
	 * @return
	 */
	public List<TransferOrderItemDTO> searchTransferOrderItem(TransferOrderItemDTO dto) throws SQLException {
		return transferOrderItemDao.searchTransferOrderItem(dto);
	}
	/**
	 * 按商品货号统计调拨单各商品数量
	 * */
	public List<TransferOrderItemModel> countByGoodsNo(TransferOrderItemModel model){
		return transferOrderItemDao.countByGoodsNo(model);
	}
	
}
