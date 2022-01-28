package com.topideal.inventory.service.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventoryWarningDao;
import com.topideal.entity.vo.InventoryWarningModel;
import com.topideal.inventory.service.InventoryWarningService;

/**
 * 库存管理-库存预警-service实现类
 */
@Service
public class InventoryWarningServiceImpl implements InventoryWarningService {

	//库存预警dao
    @Autowired
    private InventoryWarningDao inventoryWarningDao;

    /**
	 * 库存预警列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InventoryWarningModel listInventoryWarning(InventoryWarningModel model)throws SQLException {
     
    	
    	return inventoryWarningDao.searchByPage(model);
    }

   

}
