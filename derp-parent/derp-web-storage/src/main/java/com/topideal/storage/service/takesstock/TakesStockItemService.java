package com.topideal.storage.service.takesstock;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.TakesStockItemModel;

public interface TakesStockItemService {
    
	
	/**
	 * 查询列表
	 * 
	 * @param model
	 * @return
	 */
	public List<TakesStockItemModel> list(TakesStockItemModel model) throws SQLException;

}
