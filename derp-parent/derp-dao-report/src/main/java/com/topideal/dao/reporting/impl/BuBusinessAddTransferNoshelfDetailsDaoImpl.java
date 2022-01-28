package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessAddTransferNoshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessAddTransferNoshelfDetailsModel;
import com.topideal.mapper.reporting.BuBusinessAddTransferNoshelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuBusinessAddTransferNoshelfDetailsDaoImpl implements BuBusinessAddTransferNoshelfDetailsDao {

    @Autowired
    private BuBusinessAddTransferNoshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessAddTransferNoshelfDetailsModel> list(BuBusinessAddTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessAddTransferNoshelfDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessAddTransferNoshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessAddTransferNoshelfDetailsModel  searchByPage(BuBusinessAddTransferNoshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessAddTransferNoshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessAddTransferNoshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessAddTransferNoshelfDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessAddTransferNoshelfDetailsModel  model=new BuBusinessAddTransferNoshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessAddTransferNoshelfDetailsModel searchByModel(BuBusinessAddTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 *删除(事业部业务经销存)累计调拨在途明细表
	 */
	@Override
	public int delBuBusinessAddTransferNoshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessAddTransferNoshelf(map);
	}
	
	/**
	 * 查询事业部商家、仓库、月份(事业部业务经销存)累计本调拨在途(导出)
	 */
	@Override
	public List<Map<String, Object>> listBuAddTransferNoshelfDetailsMap(Map<String, Object> map) {
		return mapper.listBuAddTransferNoshelfDetailsMap(map);
	}
	/***
	 * 查询商家、仓库、月份(事业部业务经销存)本调拨在途(导出)
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listBuTransferNoshelfDetailsMap(Map<String, Object> map) {
		return mapper.listBuTransferNoshelfDetailsMap(map);
	}
	
	/**
	 * 在库天数查询累计调拨在途
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getInWarehouseSumTransferAccount(Map<String, Object> queryMap) {
		return mapper.getInWarehouseSumTransferAccount(queryMap);
	}

}