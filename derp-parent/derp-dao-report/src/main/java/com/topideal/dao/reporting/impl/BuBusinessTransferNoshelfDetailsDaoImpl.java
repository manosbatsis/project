package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessTransferNoshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessTransferNoshelfDetailsModel;
import com.topideal.mapper.reporting.BuBusinessTransferNoshelfDetailsMapper;
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
public class BuBusinessTransferNoshelfDetailsDaoImpl implements BuBusinessTransferNoshelfDetailsDao {

    @Autowired
    private BuBusinessTransferNoshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessTransferNoshelfDetailsModel> list(BuBusinessTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessTransferNoshelfDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessTransferNoshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessTransferNoshelfDetailsModel  searchByPage(BuBusinessTransferNoshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessTransferNoshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessTransferNoshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessTransferNoshelfDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessTransferNoshelfDetailsModel  model=new BuBusinessTransferNoshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessTransferNoshelfDetailsModel searchByModel(BuBusinessTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 *  删除(事业部业务经销存)调拨在途明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delBuBusinessTransferNoshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessTransferNoshelf(map);
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

}