package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceLocationAdjustmentDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceLocationAdjustmentDetailsModel;
import com.topideal.mapper.reporting.BuFinanceLocationAdjustmentDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceLocationAdjustmentDetailsDaoImpl implements BuFinanceLocationAdjustmentDetailsDao {

    @Autowired
    private BuFinanceLocationAdjustmentDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceLocationAdjustmentDetailsModel> list(BuFinanceLocationAdjustmentDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceLocationAdjustmentDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceLocationAdjustmentDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceLocationAdjustmentDetailsModel  searchByPage(BuFinanceLocationAdjustmentDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceLocationAdjustmentDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceLocationAdjustmentDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceLocationAdjustmentDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceLocationAdjustmentDetailsModel  model=new BuFinanceLocationAdjustmentDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceLocationAdjustmentDetailsModel searchByModel(BuFinanceLocationAdjustmentDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int delFinanceLocationAdjustmentDetails(Map<String, Object> map) throws SQLException {
		return mapper.delFinanceLocationAdjustmentDetails(map);
	}

}