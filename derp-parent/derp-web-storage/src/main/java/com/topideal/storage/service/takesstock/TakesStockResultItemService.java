package com.topideal.storage.service.takesstock;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.vo.TakesStockResultItemModel;

public interface TakesStockResultItemService {
    
	
	/**
	 * 查询列表
	 * 
	 * @param model
	 * @return
	 */
	public List<TakesStockResultItemDTO> list(TakesStockResultItemDTO model) throws SQLException;


}
