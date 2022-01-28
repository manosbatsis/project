package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.YunjiAccountDataDao;
import com.topideal.entity.vo.sale.YunjiAccountDataModel;
import com.topideal.mapper.sale.YunjiAccountDataMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class YunjiAccountDataDaoImpl implements YunjiAccountDataDao {

    @Autowired
    private YunjiAccountDataMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiAccountDataModel> list(YunjiAccountDataModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiAccountDataModel model) throws SQLException {
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
    public int modify(YunjiAccountDataModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiAccountDataModel  searchByPage(YunjiAccountDataModel  model) throws SQLException{
        PageDataModel<YunjiAccountDataModel> pageModel=mapper.listByPage(model);
        return (YunjiAccountDataModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiAccountDataModel  searchById(Long id)throws SQLException {
        YunjiAccountDataModel  model=new YunjiAccountDataModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiAccountDataModel searchByModel(YunjiAccountDataModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取云集结算账单表头有哪些商家
	 */
	@Override
	public List<Map<String, Object>> getYunjiAccountMerchant(Map<String, Object> map) {
		return mapper.getYunjiAccountMerchant(map);
	}
	@Override
	public List<YunjiAccountDataModel> getPlatformStatementData(Map<String, Object> queryMap) {
		return mapper.getPlatformStatementData(queryMap) ;
	}
}