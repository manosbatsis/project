package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.VipShelfDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipShelfDetailsDao;
import com.topideal.entity.vo.reporting.VipShelfDetailsModel;
import com.topideal.mapper.reporting.VipShelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipShelfDetailsDaoImpl implements VipShelfDetailsDao {

    @Autowired
    private VipShelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipShelfDetailsModel> list(VipShelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	@Override
	public List<VipShelfDetailsDTO> listDTO(VipShelfDetailsDTO dto){
		return mapper.listDTO(dto);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipShelfDetailsModel model) throws SQLException {
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
    public int modify(VipShelfDetailsModel model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipShelfDetailsModel searchByPage(VipShelfDetailsModel model) throws SQLException{
        PageDataModel<VipShelfDetailsModel> pageModel=mapper.listByPage(model);
        return (VipShelfDetailsModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipShelfDetailsModel searchById(Long id)throws SQLException {
        VipShelfDetailsModel model=new VipShelfDetailsModel();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipShelfDetailsModel searchByModel(VipShelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	/**
	 * 清空本商家、仓库、po、标准条码的上架明细
	 */
	@Override
	public int deleteByModel(VipShelfDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipShelfDetailsModel> list) {
		return mapper.batchInsert(list);
	}

}