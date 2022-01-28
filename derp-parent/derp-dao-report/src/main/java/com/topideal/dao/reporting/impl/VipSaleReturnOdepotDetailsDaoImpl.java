package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.VipSaleReturnOdepotDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipSaleReturnOdepotDetailsDao;
import com.topideal.entity.vo.reporting.VipSaleReturnOdepotDetailsModel;
import com.topideal.mapper.reporting.VipSaleReturnOdepotDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipSaleReturnOdepotDetailsDaoImpl implements VipSaleReturnOdepotDetailsDao {

    @Autowired
    private VipSaleReturnOdepotDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipSaleReturnOdepotDetailsModel> list(VipSaleReturnOdepotDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	@Override
	public List<VipSaleReturnOdepotDetailsDTO> listDTO(VipSaleReturnOdepotDetailsDTO model){
		return mapper.listDTO(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipSaleReturnOdepotDetailsModel model) throws SQLException {
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
    public int modify(VipSaleReturnOdepotDetailsModel model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipSaleReturnOdepotDetailsModel searchByPage(VipSaleReturnOdepotDetailsModel model) throws SQLException{
        PageDataModel<VipSaleReturnOdepotDetailsModel> pageModel=mapper.listByPage(model);
        return (VipSaleReturnOdepotDetailsModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipSaleReturnOdepotDetailsModel searchById(Long id)throws SQLException {
        VipSaleReturnOdepotDetailsModel model=new VipSaleReturnOdepotDetailsModel();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipSaleReturnOdepotDetailsModel searchByModel(VipSaleReturnOdepotDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清空本商家、仓库、po、标准条码的销售退明细
	 */
	@Override
	public int deleteByModel(VipSaleReturnOdepotDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipSaleReturnOdepotDetailsModel> list) {
		return mapper.batchInsert(list);
	}

}