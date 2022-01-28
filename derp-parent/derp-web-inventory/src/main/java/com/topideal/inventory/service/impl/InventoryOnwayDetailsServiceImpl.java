package com.topideal.inventory.service.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventoryOnwayDetailsDao;
import com.topideal.entity.vo.InventoryOnwayDetailsModel;
import com.topideal.inventory.service.InventoryOnwayDetailsService;

/**
 * 库存管理-库存在途明细-service实现类
 */
@Service
public class InventoryOnwayDetailsServiceImpl implements InventoryOnwayDetailsService {

	//商品收发汇总dao
    @Autowired
    private InventoryOnwayDetailsDao inventoryOnwayDetailsDao;

    /**
	 * 商品收发汇总列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InventoryOnwayDetailsModel listInventoryOnwayDetails(InventoryOnwayDetailsModel model)throws SQLException {
        return inventoryOnwayDetailsDao.searchByPage(model);
    }

   

}
