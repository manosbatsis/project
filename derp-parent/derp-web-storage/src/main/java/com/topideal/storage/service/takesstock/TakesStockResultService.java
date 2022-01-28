package com.topideal.storage.service.takesstock;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.TakesStockResultsDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TakesStockResultService {
    
	/**
	 * 列表（分页）
	 * 
	 * @param dto
	 * @return
	 */
	public TakesStockResultsDTO listTakesStockResultPage(TakesStockResultsDTO dto) throws SQLException;
	/**
	 * 根据id查询盘点结果
	 * @return
	 */
	public TakesStockResultsDTO queryById(Long Id) throws SQLException;
	/**
	 * 确认盘点结果
	 * */
    public String updateConfirmTakesStock(Long userId, String userName, String topidealCode, String ids, String confirmType) throws Exception;

	/**
	 * 导入
	 * @param data
	 * @return map
	 */
	Map importTakesStockResult(List<List<Map<String, String>>> data, User user) throws Exception;

	/**
	 * 通过ids获取盘点结果表体盘亏的商品信息
	 */
	List<Map<String, Object>> getItemByResultIds(List<Long> resultIds);

	/**
	 * 判断单据的归属时间是否大于关账时间/结转时间
	 * @param ids
	 * @return
	 */
	String checkSourceTime(List<Long> ids) throws SQLException;

	public Map<String, String> confirmInDepot(User user, TakesStockResultsDTO dto) throws Exception;

	/**
	 * 根据条件查询导出盘点结果
	 */
	public List<Map<String, Object>> listForExport(TakesStockResultsDTO dto);

	/**
	 * 根据条件查询导出盘点结果表体
	 */
	public List<Map<String, Object>> listForExportItem(TakesStockResultsDTO dto);
}
