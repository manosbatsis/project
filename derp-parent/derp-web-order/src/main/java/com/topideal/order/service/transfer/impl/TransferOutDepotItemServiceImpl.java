package com.topideal.order.service.transfer.impl;



import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.transfer.TransferOutDepotItemDao;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;
import com.topideal.order.service.transfer.TransferOutDepotItemService;



/**
 * 调拨出库详情service实现类
 * @author yucaifu
 */
@Service
public class TransferOutDepotItemServiceImpl implements TransferOutDepotItemService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TransferOutDepotItemServiceImpl.class);

	///调拨出库详情表
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	
	
	
	
	/**查询调拨出库详情
	 * @param 
	 * @return
	 */
	public TransferOutDepotItemModel searchItem(TransferOutDepotItemModel model) throws SQLException {
		
		return transferOutDepotItemDao.searchByModel(model);
	}
    
	/**根据调拨出库id查询调拨出库详情
	 * @param transferId
	 * @return
	 */
	public List<TransferOutDepotItemDTO> searchItemList(Long transferId) throws SQLException {
		return transferOutDepotItemDao.getDTOItemListByTransferId(transferId);
	}

	
}
