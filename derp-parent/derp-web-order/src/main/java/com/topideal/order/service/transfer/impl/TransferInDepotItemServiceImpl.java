package com.topideal.order.service.transfer.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.transfer.TransferInDepotItemDao;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;
import com.topideal.order.service.transfer.TransferInDepotItemService;

/**
 * 调拨入库详情service实现类
 * @author yucaifu
 */
@Service
public class TransferInDepotItemServiceImpl implements TransferInDepotItemService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TransferInDepotItemServiceImpl.class);

	///调拨入库详情表
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;

	/**
	 * 列表查询
	 * @param transferId
	 */
	public List<TransferInDepotItemDTO> searchItemList(Long transferId) throws SQLException {
		return transferInDepotItemDao.getItemByTransferInId(transferId);
	}
	
	
	
	
	
}
