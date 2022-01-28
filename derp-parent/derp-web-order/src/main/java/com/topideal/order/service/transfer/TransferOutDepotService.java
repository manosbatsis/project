package com.topideal.order.service.transfer;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;




/**
 * 调拨出库 service
 * @author yucaifu
 * */
public interface TransferOutDepotService {

	
	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	public TransferOutDepotDTO listTransferOutDepotPage(TransferOutDepotDTO dto, User user) throws SQLException;
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	public TransferOutDepotDTO searchDetail(Long id) throws SQLException;
	
	/**
	 * 导出前统计数据量
	 * */
	public Integer listForCount(TransferOutDepotDTO dto, User user);
	
	public List<Map<String, Object>> listForMap(TransferOutDepotDTO dto, User user);
	
	public List<Map<String, Object>> listForMapItem(TransferOutDepotDTO dto, User user);

	public Map<String, String> saveTransferOutDepot(TransferOutDepotDTO dto, User user) throws Exception;

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return map
	 */
	Map saveImportTransferOut(List<Map<String, String>> data,User user) throws Exception;

	/**
	 * 审核
	 * @throws SQLException
	 */
	Map<String, Object> auditTransferOutDepot(List<Long> ids, User user) throws Exception;

	/**
	 * 根据调拨出库单id统计调出商品数量
	 * */
	List<Map<String, Object>> getItemSumByIds(List<Long> ids, String topidealCode) throws Exception;
}
