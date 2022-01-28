package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.AlipayDailyFeeDao;
import com.topideal.entity.vo.platformdata.AlipayDailyFeeModel;
import com.topideal.mapper.platformdata.AlipayDailyFeeMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AlipayDailyFeeDaoImpl implements AlipayDailyFeeDao {

    @Autowired
    private AlipayDailyFeeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AlipayDailyFeeModel> list(AlipayDailyFeeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AlipayDailyFeeModel model) throws SQLException {
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
    public int modify(AlipayDailyFeeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AlipayDailyFeeModel  searchByPage(AlipayDailyFeeModel  model) throws SQLException{
        PageDataModel<AlipayDailyFeeModel> pageModel=mapper.listByPage(model);
        return (AlipayDailyFeeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AlipayDailyFeeModel  searchById(Long id)throws SQLException {
        AlipayDailyFeeModel  model=new AlipayDailyFeeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AlipayDailyFeeModel searchByModel(AlipayDailyFeeModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 批量新增
	 */
	@Override
	public Integer alipayBatchSave(List<AlipayDailyFeeModel> saveList) {
		return mapper.alipayBatchSave(saveList);
	}

}