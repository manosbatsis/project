package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.BuMoveInventoryDao;
import com.topideal.entity.vo.order.BuMoveInventoryModel;
import com.topideal.mapper.order.BuMoveInventoryMapper;
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
public class BuMoveInventoryDaoImpl implements BuMoveInventoryDao {

    @Autowired
    private BuMoveInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuMoveInventoryModel> list(BuMoveInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuMoveInventoryModel model) throws SQLException {
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
    public int modify(BuMoveInventoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuMoveInventoryModel  searchByPage(BuMoveInventoryModel  model) throws SQLException{
        PageDataModel<BuMoveInventoryModel> pageModel=mapper.listByPage(model);
        return (BuMoveInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuMoveInventoryModel  searchById(Long id)throws SQLException {
        BuMoveInventoryModel  model=new BuMoveInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuMoveInventoryModel searchByModel(BuMoveInventoryModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取(事业部财务经销存) 本期事业部移库明细
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceMoveDetails(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveDetails(map);
	}

	/**
	 * 获取(事业部财务经销存) 本期事业部移库明细加权
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceMoveDetailsWeighted(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveDetailsWeighted(map);
	}
	
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (入)
	 */
	@Override
	public int getBuFinanceMoveInDetailsCount(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveInDetailsCount(map);
	}
	
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (入)
	 */
	@Override
	public int getBuFinanceMoveInDetailsCountWeighted(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveInDetailsCountWeighted(map);
	}
	
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (出)
	 */
	@Override
	public int getBuFinanceMoveOutDetailsCount(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveOutDetailsCount(map);
	}

	@Override
	public List<Map<String, Object>> getBuFinanceMoveDetailsInNum(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveDetailsInNum(map);
	}
	@Override
	public List<Map<String, Object>> getBuFinanceMoveDetailsOutNum(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceMoveDetailsOutNum(map);
	}
	
}