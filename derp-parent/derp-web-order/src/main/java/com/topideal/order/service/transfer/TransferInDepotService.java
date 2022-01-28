package com.topideal.order.service.transfer;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 调拨入库 service
 * @author yucaifu
 * */
public interface TransferInDepotService {

	  
	/**
	 * 分页查询
	 * */
    public TransferInDepotDTO  searchByPage(TransferInDepotDTO dto, User user) throws SQLException;
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    public TransferInDepotDTO searchById(Long id)throws SQLException;
    
    public Integer listForCount(TransferInDepotDTO dto, User user);
    
	public List<Map<String, Object>> listForMap(TransferInDepotDTO dto, User user);
	
	public List<Map<String, Object>> listForMapItem(TransferInDepotDTO dto, User user);

	public Map<String, String> saveTransferInDepot(TransferInDepotDTO dto, User user) throws Exception;

	/**
	 * 导入调拨入库单
	 */
	public Map<String, Object> saveImportTransferIn(User user, List<Map<String, String>> data) throws Exception;

	/**
	 * 审核
	 * @throws SQLException
	 */
	Map<String, Object> auditTransferInDepot(List<Long> ids, User user) throws Exception;
}
