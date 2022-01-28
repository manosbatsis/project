package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.VipOutDepotDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipOutDepotDetailsDao;
import com.topideal.entity.vo.reporting.VipOutDepotDetailsModel;
import com.topideal.mapper.reporting.VipOutDepotDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipOutDepotDetailsDaoImpl implements VipOutDepotDetailsDao {

    @Autowired
    private VipOutDepotDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipOutDepotDetailsModel> list(VipOutDepotDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	@Override
	public List<VipOutDepotDetailsDTO> listDTO(VipOutDepotDetailsDTO dto){
		return mapper.listDTO(dto);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipOutDepotDetailsModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(VipOutDepotDetailsModel model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipOutDepotDetailsModel searchByPage(VipOutDepotDetailsModel model) throws SQLException{
        PageDataModel<VipOutDepotDetailsModel> pageModel=mapper.listByPage(model);
        return (VipOutDepotDetailsModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipOutDepotDetailsModel searchById(Long id)throws SQLException {
        VipOutDepotDetailsModel model=new VipOutDepotDetailsModel();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipOutDepotDetailsModel searchByModel(VipOutDepotDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据实体删除
	 */
	@Override
	public int deleteByModel(VipOutDepotDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipOutDepotDetailsModel> list) {
		return mapper.batchInsert(list) ;
	}

}