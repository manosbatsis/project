package com.topideal.mapper.transfer;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferInDepotMapper extends BaseMapper<TransferInDepotModel> {

    public Integer listForCount(TransferInDepotDTO dto);
    
    public List<Map<String, Object>> listForMap(TransferInDepotDTO dto);
    
    public List<Map<String, Object>> listForMapItem(TransferInDepotDTO dto);
    
    /**
     * 查询所有数据
     * @return
     */
     PageDataModel<TransferInDepotModel> getListByPage(TransferInDepotModel model)throws SQLException;
     /**
  	* 根据条件查询调拨出库单
  	* */
 	public TransferInDepotModel getByModel(TransferInDepotModel model);

    PageDataModel<TransferInDepotDTO> getDTOListByPage(TransferInDepotDTO dto)throws SQLException;

    /**
     * 根据id查询DTO
     */
    TransferInDepotDTO searchDTOById(Long id) throws SQLException;

    List<TransferInDepotDTO> listDTOByDTO(TransferInDepotDTO transferInDepotDTO);

    List<TransferInDepotDTO> listInvailDTOByDTO(TransferInDepotDTO transferInDepotDTO);
}

