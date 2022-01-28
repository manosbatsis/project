package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.AlipayDailySettlebatchDao;
import com.topideal.entity.vo.platformdata.AlipayDailySettlebatchModel;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;
import com.topideal.mapper.platformdata.AlipayDailySettlebatchMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AlipayDailySettlebatchDaoImpl implements AlipayDailySettlebatchDao {

    @Autowired
    private AlipayDailySettlebatchMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AlipayDailySettlebatchModel> list(AlipayDailySettlebatchModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AlipayDailySettlebatchModel model) throws SQLException {
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
    public int modify(AlipayDailySettlebatchModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AlipayDailySettlebatchModel  searchByPage(AlipayDailySettlebatchModel  model) throws SQLException{
        PageDataModel<AlipayDailySettlebatchModel> pageModel=mapper.listByPage(model);
        return (AlipayDailySettlebatchModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AlipayDailySettlebatchModel  searchById(Long id)throws SQLException {
        AlipayDailySettlebatchModel  model=new AlipayDailySettlebatchModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AlipayDailySettlebatchModel searchByModel(AlipayDailySettlebatchModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 批量新增
	 */
	@Override
	public Integer alipayBatchSave(List<AlipayDailySettlebatchModel> saveList) {
		return mapper.alipayBatchSave(saveList);
	}
	
	/**
	 * 平台结算单-统计
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<AlipayDailySettlebatchModel> getPlatformStatementData(Map<String, Object> queryMap) {
		return mapper.getPlatformStatementData(queryMap);
	}

}