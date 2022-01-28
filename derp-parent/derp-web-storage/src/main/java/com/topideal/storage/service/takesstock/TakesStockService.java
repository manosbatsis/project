package com.topideal.storage.service.takesstock;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.dto.TakesStockFrom;
import com.topideal.entity.vo.TakesStockModel;

public interface TakesStockService {
    
	/**
	 * 列表（分页）
	 * 
	 * @param dto
	 * @return
	 */
	public TakesStockDTO listTakesStockPage(TakesStockDTO dto) throws SQLException;
	/**
	 * 保存盘点申请
	 * 
	 * @param model
	 * @return
	 */
	public Map<String, Object> saveTakesstock(TakesStockFrom model, Long userId, String userName, Long merchantId, String merchantName) throws Exception;
	/**
	 * 根据id查询盘点申请
	 * 
	 * @param model
	 * @return
	 */
	public TakesStockModel queryById(String id) throws Exception;
	
	/**
	 * 更新盘点申请
	 * */
	public Map<String, Object> updateTakesstock(TakesStockFrom model) throws Exception;
	/**
	 * 根据id删除(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delTakesStockBatch(List<Long> ids) throws SQLException;
	/**
	 * 提交发送盘点指令
	 * */
    public String updateSendtakesStock(Long userId, String userName, String topidealCode, String ids) throws Exception;

    /**
     * 获取详情
     */
    public TakesStockDTO queryDTOById(Long id) throws SQLException;
}
