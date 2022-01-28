package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CrawlerOutTempDao;
import com.topideal.entity.vo.sale.CrawlerOutTempModel;
import com.topideal.mapper.sale.CrawlerOutTempMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CrawlerOutTempDaoImpl implements CrawlerOutTempDao {

    @Autowired
    private CrawlerOutTempMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerOutTempModel> list(CrawlerOutTempModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerOutTempModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(CrawlerOutTempModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerOutTempModel  searchByPage(CrawlerOutTempModel  model) throws SQLException{
        PageDataModel<CrawlerOutTempModel> pageModel=mapper.listByPage(model);
        return (CrawlerOutTempModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerOutTempModel  searchById(Long id)throws SQLException {
        CrawlerOutTempModel  model=new CrawlerOutTempModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerOutTempModel searchByModel(CrawlerOutTempModel model) throws SQLException {
		return mapper.get(model);
	}
  
	public int del(Long id) throws SQLException {
		return mapper.del(id);
	}
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	 /**清空临时表*/
    public void clearTable(Map<String, Object> map){
    	 mapper.clearTable(map);
    }
    /**唯品4.0-按商家id、仓库id、PO号、货号统计可核销量
     * */
    public Integer getVerifiSumNum(Map<String, Object> map) {
    	return mapper.getVerifiSumNum(map);
    }
	/**4.0-统计货号库存余量
	 * */
	public Integer getSurplusNum(Map<String, Object> map){
		return mapper.getSurplusNum(map);
	}
	/**4.0-扣减临时表商家、仓库、货号的库存量
	 * */
    public int updateLowerNum(Map<String, Object> map){
    	return mapper.updateLowerNum(map);
    }
    /**4.0-扣减临时表商家、仓库、po号、货号的可核销量
	 * */
    public int updateLowerVerifiNum(Map<String, Object> map) {
    	return mapper.updateLowerVerifiNum(map);
    }
    /**
     * 唯品4.0-查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种分组
     * */
    public List<Map<String, Object>> getListByBillTypeCurrency(Map<String, Object> map){
    	return mapper.getListByBillTypeCurrency(map);
    }
    /**
     * 新版云集该账单该sku 账单和sku
     */
	/*@Override
	public List<Map<String, Object>> getYunjiBillIdSaleCodeList(Map<String, Object> map) {
		return mapper.getYunjiBillIdSaleCodeList(map);
	}*/
    /**
     * 新版云集该账单该 事业部 sku 账单和sku
     */
	/*@Override
	public List<Map<String, Object>> getBuYunjiBillIdSaleCodeList(Map<String, Object> map) {
		return mapper.getBuYunjiBillIdSaleCodeList(map);
	}*/
	/**
	 * 新版云集销售订单下的sku下的商品量
	 */
	@Override
	public List<Map<String, Object>> getYunjiOrderBillList(Map<String, Object> map) {
		return mapper.getYunjiOrderBillList(map);
	}
	/**
	 * 唯品4.0相同sku的分配明细汇总后出库
	 * */
	public List<Map<String,Object>> getListSumGroupBySku(CrawlerOutTempModel tempMode){
		return mapper.getListSumGroupBySku(tempMode);
	}
	

}