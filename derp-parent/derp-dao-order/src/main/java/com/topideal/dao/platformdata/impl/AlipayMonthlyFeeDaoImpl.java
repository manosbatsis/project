package com.topideal.dao.platformdata.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.AlipayMonthlyFeeDao;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;
import com.topideal.mapper.platformdata.AlipayMonthlyFeeMapper;
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
public class AlipayMonthlyFeeDaoImpl implements AlipayMonthlyFeeDao {

    @Autowired
    private AlipayMonthlyFeeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AlipayMonthlyFeeModel> list(AlipayMonthlyFeeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AlipayMonthlyFeeModel model) throws SQLException {
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
    public int modify(AlipayMonthlyFeeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AlipayMonthlyFeeModel  searchByPage(AlipayMonthlyFeeModel  model) throws SQLException{
        PageDataModel<AlipayMonthlyFeeModel> pageModel=mapper.listByPage(model);
        return (AlipayMonthlyFeeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AlipayMonthlyFeeModel  searchById(Long id)throws SQLException {
        AlipayMonthlyFeeModel  model=new AlipayMonthlyFeeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AlipayMonthlyFeeModel searchByModel(AlipayMonthlyFeeModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 批量新增
	 */
	@Override
	public Integer alipayBatchSave(List<AlipayMonthlyFeeModel> saveList) {
		return mapper.alipayBatchSave(saveList);
	}

    @Override
    public List<AlipayMonthlyFeeModel> statisticsByExCodesAndMerId(List<String> partnerTransactionIds, Long merchantId) throws SQLException {
        return mapper.statisticsByExCodesAndMerId(partnerTransactionIds, merchantId);
    }

    @Override
    public Integer statisticsByModel(AlipayMonthlyFeeModel model) throws SQLException {
        return mapper.statisticsByModel(model);
    }

    @Override
    public Integer statisticsDistinctByModel(AlipayMonthlyFeeModel alipayMonthlyFeeModel) {
        return mapper.statisticsDistinctByModel(alipayMonthlyFeeModel);
    }

    @Override
    public List<AlipayMonthlyFeeModel> listByMap(Map<String, Object> queryMap) {
        return mapper.listByMap(queryMap);
    }

    @Override
    public AlipayMonthlyFeeModel searchDistinctByPage(AlipayMonthlyFeeModel alipayMonthlyFeeModel) {
        PageDataModel<AlipayMonthlyFeeModel> pageModel=mapper.listDistinctByPage(alipayMonthlyFeeModel);
        return (AlipayMonthlyFeeModel ) pageModel.getModel();
    }

}