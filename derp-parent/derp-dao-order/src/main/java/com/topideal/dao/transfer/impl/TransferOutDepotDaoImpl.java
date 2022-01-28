package com.topideal.dao.transfer.impl;


import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.mapper.transfer.TransferOutDepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨出库表 impl
 * @author lchenxing
 */
@Repository
public class TransferOutDepotDaoImpl implements TransferOutDepotDao {

    @Autowired
    private TransferOutDepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TransferOutDepotModel> list(TransferOutDepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TransferOutDepotModel model) throws SQLException {
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
    public int modify(TransferOutDepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TransferOutDepotModel  searchByPage(TransferOutDepotModel  model) throws SQLException{
        PageDataModel<TransferOutDepotModel> pageModel=mapper.getListByPage(model);
        return (TransferOutDepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TransferOutDepotModel  searchById(Long id)throws SQLException {
        TransferOutDepotModel  model=new TransferOutDepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
     /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TransferOutDepotModel searchByModel(TransferOutDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
 	* 根据条件查询调拨出库单
 	* */
	@Override
	public TransferOutDepotModel getByModel(TransferOutDepotModel model) throws SQLException {
		return mapper.getByModel(model);
	}

	@Override
	public TransferOutDepotDTO listDTOByPage(TransferOutDepotDTO dto) throws SQLException {
		PageDataModel<TransferOutDepotDTO> pageModel=mapper.listDTOByPage(dto);
		return (TransferOutDepotDTO) pageModel.getModel();
	}

	@Override
	public TransferOutDepotDTO getByDto(TransferOutDepotDTO dto) throws SQLException {
		return mapper.getByDto(dto);
	}

	@Override
	public List<TransferOutDepotDTO> listDTObyDTO(TransferOutDepotDTO transferOutDepotDTO) {
		return mapper.listDTObyDTO(transferOutDepotDTO);
	}

	@Override
	public List<TransferOutDepotDTO> listInvailDTOByDTO(TransferOutDepotDTO transferOutDepotDTO) {
		return mapper.listInvailDTOByDTO(transferOutDepotDTO);
	}

	public Integer listForCount(TransferOutDepotDTO dto){
		return mapper.listForCount(dto);
	}
	@Override
	public List<Map<String, Object>> listForMap(TransferOutDepotDTO dto) {
		return mapper.listForMap(dto);
	}
	@Override
	public List<Map<String, Object>> listForMapItem(TransferOutDepotDTO dto) {
		return mapper.listForMapItem(dto);
	}
	
}
