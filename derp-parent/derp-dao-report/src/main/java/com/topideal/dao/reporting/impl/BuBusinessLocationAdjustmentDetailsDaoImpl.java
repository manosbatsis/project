package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessLocationAdjustmentDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessLocationAdjustmentDetailsModel;
import com.topideal.mapper.reporting.BuBusinessLocationAdjustmentDetailsMapper;
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
public class BuBusinessLocationAdjustmentDetailsDaoImpl implements BuBusinessLocationAdjustmentDetailsDao {

    @Autowired
    private BuBusinessLocationAdjustmentDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessLocationAdjustmentDetailsModel> list(BuBusinessLocationAdjustmentDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessLocationAdjustmentDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessLocationAdjustmentDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessLocationAdjustmentDetailsModel  searchByPage(BuBusinessLocationAdjustmentDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessLocationAdjustmentDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessLocationAdjustmentDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessLocationAdjustmentDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessLocationAdjustmentDetailsModel  model=new BuBusinessLocationAdjustmentDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessLocationAdjustmentDetailsModel searchByModel(BuBusinessLocationAdjustmentDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 删除(事业部业务经销存)库位类型调整明细表
	 */
	@Override
	public int delBuBusinessLocationAdjustmentDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessLocationAdjustmentDetails(map);
	}
	/**
	 * 
	 */
	@Override
	public List<Map<String, Object>> getLocationAdjustmentDetailsForMap(Map<String, Object> map) {
		return mapper.getLocationAdjustmentDetailsForMap(map);
	}

}