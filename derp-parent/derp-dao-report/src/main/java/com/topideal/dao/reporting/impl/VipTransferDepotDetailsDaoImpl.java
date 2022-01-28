package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipTransferDepotDetailsDao;
import com.topideal.entity.dto.VipTransferDepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipTransferDepotDetailsModel;
import com.topideal.mapper.reporting.VipTransferDepotDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipTransferDepotDetailsDaoImpl implements VipTransferDepotDetailsDao {

    @Autowired
    private VipTransferDepotDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipTransferDepotDetailsModel> list(VipTransferDepotDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
    @Override
    public List<VipTransferDepotDetailsDTO> listDTO(VipTransferDepotDetailsDTO dto){
        return mapper.listDTO(dto);
    }
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipTransferDepotDetailsModel model) throws SQLException {
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
    public int modify(VipTransferDepotDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipTransferDepotDetailsModel  searchByPage(VipTransferDepotDetailsModel  model) throws SQLException{
        PageDataModel<VipTransferDepotDetailsModel> pageModel=mapper.listByPage(model);
        return (VipTransferDepotDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipTransferDepotDetailsModel  searchById(Long id)throws SQLException {
        VipTransferDepotDetailsModel  model=new VipTransferDepotDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipTransferDepotDetailsModel searchByModel(VipTransferDepotDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer deleteByModel(VipTransferDepotDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipTransferDepotDetailsModel> list) {
		return mapper.batchInsert(list);
	}

}