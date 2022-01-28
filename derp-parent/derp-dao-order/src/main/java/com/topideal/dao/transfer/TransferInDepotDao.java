package com.topideal.dao.transfer;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.vo.transfer.TransferInDepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库dao
 * @author lian_
 */
public interface TransferInDepotDao extends BaseDao<TransferInDepotModel>{
		
	public Integer listForCount(TransferInDepotDTO dto);

	public List<Map<String, Object>> listForMap(TransferInDepotDTO dto);
	

	public List<Map<String, Object>> listForMapItem(TransferInDepotDTO dto);
	/**
  	 * 根据条件查询调拨出库单
  	 **/
 	public TransferInDepotModel getByModel(TransferInDepotModel model);

 	/**
 	 * 分页查询DTO
 	 */
 	public TransferInDepotDTO searchDTOByPage(TransferInDepotDTO dto) throws SQLException;

	/**
	 * 根据id查询DTO
	 */
 	public TransferInDepotDTO searchDTOById(Long id) throws SQLException;

	/**
	 * 根据DTO 查询 DTO list
	 * @param transferInDepotDTO
	 * @return
	 */
    List<TransferInDepotDTO> listDTOByDTO(TransferInDepotDTO transferInDepotDTO);

    List<TransferInDepotDTO> listInvailDTOByDTO(TransferInDepotDTO transferInDepotDTO);
}

