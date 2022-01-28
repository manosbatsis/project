package com.topideal.mapper.transfer;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 调拨出库表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferOutDepotMapper extends BaseMapper<TransferOutDepotModel> {

    public Integer listForCount(TransferOutDepotDTO dto);
	 
    public List<Map<String, Object>> listForMap(TransferOutDepotDTO dto);
    
    public List<Map<String, Object>> listForMapItem(TransferOutDepotDTO dto);
   
    /**
     * 查询所有数据
     * @return
     */
    PageDataModel<TransferOutDepotModel> getListByPage(TransferOutDepotModel model)throws SQLException;
    /**
 	* 根据条件查询调拨出库单
 	* */
	public TransferOutDepotModel getByModel(TransferOutDepotModel model) throws SQLException;

    /**
     * 分页查询dto
     */
    public PageDataModel<TransferOutDepotDTO> listDTOByPage(TransferOutDepotDTO dto) throws SQLException;

    /**
     * 查询dto
     */
    public TransferOutDepotDTO getByDto(TransferOutDepotDTO dto) throws SQLException;

    /**
     * 查询DTO list
     * @param transferOutDepotDTO
     * @return
     */
    List<TransferOutDepotDTO> listDTObyDTO(TransferOutDepotDTO transferOutDepotDTO);

    List<TransferOutDepotDTO> listInvailDTOByDTO(TransferOutDepotDTO transferOutDepotDTO);
}

