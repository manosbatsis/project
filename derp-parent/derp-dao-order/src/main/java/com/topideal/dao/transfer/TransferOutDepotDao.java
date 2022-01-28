package com.topideal.dao.transfer;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨出库表 dao
 * @author lian_
 *
 */
public interface TransferOutDepotDao extends BaseDao<TransferOutDepotModel>{
		
	public Integer listForCount(TransferOutDepotDTO dto);
	
	public List<Map<String, Object>> listForMap(TransferOutDepotDTO dto);
	
	public List<Map<String, Object>> listForMapItem(TransferOutDepotDTO dto);
    
	/**
 	* 根据条件查询调拨出库单
 	* */
	public TransferOutDepotModel getByModel(TransferOutDepotModel model) throws SQLException;

	/**
	 * 分页查询dto
	 */
	public TransferOutDepotDTO listDTOByPage(TransferOutDepotDTO dto) throws SQLException;

	/**
	 * 查询dto
	 */
	public TransferOutDepotDTO getByDto(TransferOutDepotDTO dto) throws SQLException;


	/**
	 * 根据DTO 查询DTO list
	 * @param transferOutDepotDTO
	 * @return
	 */
    List<TransferOutDepotDTO> listDTObyDTO(TransferOutDepotDTO transferOutDepotDTO);

    List<TransferOutDepotDTO> listInvailDTOByDTO(TransferOutDepotDTO transferOutDepotDTO);
}

