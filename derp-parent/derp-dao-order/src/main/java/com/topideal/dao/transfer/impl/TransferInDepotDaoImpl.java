package com.topideal.dao.transfer.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferInDepotDao;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.mapper.transfer.TransferInDepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库 impl
 * @author lchenxing
 */
@Repository
public class TransferInDepotDaoImpl implements TransferInDepotDao {

    @Autowired
    private TransferInDepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TransferInDepotModel> list(TransferInDepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TransferInDepotModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(TransferInDepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TransferInDepotModel  searchByPage(TransferInDepotModel  model) throws SQLException{
        PageDataModel<TransferInDepotModel> pageModel=mapper.getListByPage(model);
        return (TransferInDepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TransferInDepotModel  searchById(Long id)throws SQLException {
        TransferInDepotModel  model=new TransferInDepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TransferInDepotModel searchByModel(TransferInDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	  /**
  	* 根据条件查询调拨出库单
  	* */
 	public TransferInDepotModel getByModel(TransferInDepotModel model){
 		return mapper.getByModel(model);
 	}

	@Override
	public TransferInDepotDTO searchDTOByPage(TransferInDepotDTO dto) throws SQLException {
		PageDataModel<TransferInDepotDTO> pageModel = mapper.getDTOListByPage(dto);
		return (TransferInDepotDTO) pageModel.getModel();
	}

	@Override
	public TransferInDepotDTO searchDTOById(Long id) throws SQLException {
		return mapper.searchDTOById(id);
	}

	@Override
	public List<TransferInDepotDTO> listDTOByDTO(TransferInDepotDTO transferInDepotDTO) {
		return mapper.listDTOByDTO(transferInDepotDTO);
	}

	@Override
	public List<TransferInDepotDTO> listInvailDTOByDTO(TransferInDepotDTO transferInDepotDTO) {
		return mapper.listInvailDTOByDTO(transferInDepotDTO);
	}

	public Integer listForCount(TransferInDepotDTO dto){
		return mapper.listForCount(dto);
	}
	
	@Override
	public List<Map<String, Object>> listForMap(TransferInDepotDTO dto) {
		return mapper.listForMap(dto);
	}
	@Override
	public List<Map<String, Object>> listForMapItem(TransferInDepotDTO dto) {
		return mapper.listForMapItem(dto);
	}
	
}
