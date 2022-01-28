package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CrawlerVipExtraDataDao;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;
import com.topideal.mapper.sale.CrawlerVipExtraDataMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CrawlerVipExtraDataDaoImpl implements CrawlerVipExtraDataDao {

    @Autowired
    private CrawlerVipExtraDataMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerVipExtraDataModel> list(CrawlerVipExtraDataModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerVipExtraDataModel model) throws SQLException {
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
    public int modify(CrawlerVipExtraDataModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerVipExtraDataModel  searchByPage(CrawlerVipExtraDataModel  model) throws SQLException{
        PageDataModel<CrawlerVipExtraDataModel> pageModel=mapper.listByPage(model);
        return (CrawlerVipExtraDataModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerVipExtraDataModel  searchById(Long id)throws SQLException {
        CrawlerVipExtraDataModel  model=new CrawlerVipExtraDataModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerVipExtraDataModel searchByModel(CrawlerVipExtraDataModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer batchSave(List<CrawlerVipExtraDataModel> saveList) {
		return mapper.batchSave(saveList);
	}
	/**
	 * // 分组获取平台费用单根据 商家,客户,费用类型,币种
	 */
	@Override
	public List<Map<String, Object>> getGroupCrawlerVipExtra(Map<String, Object> map) throws SQLException {
		
		return mapper.getGroupCrawlerVipExtra(map);
	}
	/**
	 * 分页获取平台费用单数据
	 */
	/*@Override
	public List<CrawlerVipExtraDataModel> getCrawlerVipExtraDatailList(Map<String, Object> map) throws SQLException {
		return mapper.getCrawlerVipExtraDatailList(map);
	}*/
	@Override
	public List<Map<String,Object>> getGroupByGoodNoCrawlerVipExtra(Map<String, Object> map) throws SQLException {
		return mapper.getGroupByGoodNoCrawlerVipExtra(map);
	}
	@Override
	public List<Map<String,Object>> getGoodNoCrawlerVipExtra(Map<String, Object> map) throws SQLException {
		return mapper.getGoodNoCrawlerVipExtra(map);
	}
	
	// 修改
	@Override
	public int updateCrawlerVipExtra(CrawlerVipExtraDataModel model) throws SQLException {
		return mapper.updateCrawlerVipExtra(model);
	}
	
	/***
	 * 获取平台结算单指定类型汇总明细
	 * @param itemQueryMap
	 * @return 
	 */
	@Override
	public List<CrawlerVipExtraDataModel> getPlatformStatementData(Map<String, Object> itemQueryMap) {
		return mapper.getPlatformStatementData(itemQueryMap);
	}
	
	/**
	 * 获取平台结算单金额和币种
	 * @param amountAndCurrencyMap
	 * @return
	 */
	@Override
	public CrawlerVipExtraDataModel getPlatformStatementAmountAndCurrency(Map<String, Object> amountAndCurrencyMap) {
		return mapper.getPlatformStatementAmountAndCurrency(amountAndCurrencyMap);
	}

}