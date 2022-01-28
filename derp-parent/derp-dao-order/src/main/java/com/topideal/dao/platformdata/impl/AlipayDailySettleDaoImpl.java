package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.AlipayDailySettleDao;
import com.topideal.entity.vo.platformdata.AlipayDailySettleModel;
import com.topideal.mapper.platformdata.AlipayDailySettleMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AlipayDailySettleDaoImpl implements AlipayDailySettleDao {

    @Autowired
    private AlipayDailySettleMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AlipayDailySettleModel> list(AlipayDailySettleModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AlipayDailySettleModel model) throws SQLException {
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
    public int modify(AlipayDailySettleModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AlipayDailySettleModel  searchByPage(AlipayDailySettleModel  model) throws SQLException{
        PageDataModel<AlipayDailySettleModel> pageModel=mapper.listByPage(model);
        return (AlipayDailySettleModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AlipayDailySettleModel  searchById(Long id)throws SQLException {
        AlipayDailySettleModel  model=new AlipayDailySettleModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AlipayDailySettleModel searchByModel(AlipayDailySettleModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 批量新增
	 */
	@Override
	public Integer alipayBatchSave(List<AlipayDailySettleModel> saveList) {
		return mapper.alipayBatchSave(saveList);
	}

}