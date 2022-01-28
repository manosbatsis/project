package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.inventory.service.InventoryFreezeDetailsService;

/**
 * 库存管理-库存冻结和解冻收发明细-service实现类
 */
@Service
public class InventoryFreezeDetailsServiceImpl implements InventoryFreezeDetailsService {

	//库存冻结和解冻收发明细dao
    @Autowired
    private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;

    /**
	 * 库存冻结和解冻收发明细列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InventoryFreezeDetailsModel listInventoryFreezeDetails(InventoryFreezeDetailsModel model)throws SQLException {
        return inventoryFreezeDetailsDao.searchByPage(model);
    }

    /**
     * 导出库存冻结和解冻收发明细
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> exportInventoryFreezeDetailsMap(InventoryFreezeDetailsModel model ) throws Exception {
		// TODO Auto-generated method stub
		return inventoryFreezeDetailsDao.exportInventoryFreezeDetailsMap(model);
	}

   

}
