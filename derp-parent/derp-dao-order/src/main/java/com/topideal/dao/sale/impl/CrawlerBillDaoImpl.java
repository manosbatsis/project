package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.CrawlerBillDao;
import com.topideal.entity.vo.sale.CrawlerBillModel;
import com.topideal.mapper.sale.CrawlerBillMapper;

/**爬虫账单
 * @author lchenxing
 */
@Repository
public class CrawlerBillDaoImpl implements CrawlerBillDao {

    @Autowired
    private CrawlerBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerBillModel> list(CrawlerBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerBillModel model) throws SQLException {
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
    public int modify(CrawlerBillModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerBillModel  searchByPage(CrawlerBillModel  model) throws SQLException{
        PageDataModel<CrawlerBillModel> pageModel=mapper.listByPage(model);
        return (CrawlerBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerBillModel  searchById(Long id)throws SQLException {
        CrawlerBillModel  model=new CrawlerBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerBillModel searchByModel(CrawlerBillModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int batchIn(List<CrawlerBillModel> orderList) {
		return mapper.batchIn(orderList);
	}
	
	@Override
	public List<CrawlerBillModel> searchBillGroup() throws SQLException{
		return mapper.searchBillGroup();
	}
	/**
	 * 新版唯品-根据临时表账单号、po号、货号查询一条账单明细
	 * @return
	 */
	public CrawlerBillModel getOneByTempGoodsNo(Map<String, Object> map){
		return mapper.getOneByTempGoodsNo(map);
	}
	@Override
	public Map<String, Object> getBySkuAndPo(CrawlerBillModel model) {
		return mapper.getBySkuAndPo(model);
	}
	/**
	 *唯品4.0-查询未使用过的账单按商家+账单号分组去重
	 */
	public List<Map<String, Object>> searchMerchantIdBillCodeList(Map<String, Object> map){
		return mapper.searchMerchantIdBillCodeList(map);	
	}
	/**
	 *唯品4.0-查询本商家+账单号未使用过的账单明细按sku去重
	 */
	public List<String> searchSkuList(Map<String, Object> map){
		return mapper.searchSkuList(map);	
	}
	/**
	 * 唯品4.0-查询本商家+账单号+处理类型+指定账单id未使用过的账单明细
	 */
	public List<Map<String, Object>> getBillList(Map<String, Object> map){
		return mapper.getBillList(map);
	}
	
	/**
	 * 获取需要生成平台结算单的唯品账单号
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<String> getPlatformStatementCode(Map<String, Object> queryMap) {
		return mapper.getPlatformStatementCode(queryMap);
	}
	/***
	 * 获取平台结算单指定类型汇总明细
	 * @param itemQueryMap
	 * @return 
	 */
	@Override
	public List<CrawlerBillModel> getPlatformStatementData(Map<String, Object> itemQueryMap) {
		return mapper.getPlatformStatementData(itemQueryMap);
	}
	/**
	 * 获取平台结算单金额和币种
	 * @param amountAndCurrencyMap
	 * @return
	 */
	@Override
	public CrawlerBillModel getPlatformStatementAmountAndCurrency(Map<String, Object> amountAndCurrencyMap) {
		return mapper.getPlatformStatementAmountAndCurrency(amountAndCurrencyMap);
	}
	
	/**
	 * 根据SKU 获取最新商品名
	 * @param queryGoodsName
	 * @return
	 */
	@Override
	public String getLastGoodsNameBySku(Map<String, Object> queryGoodsName) {
		return mapper.getLastGoodsNameBySku(queryGoodsName) ;
	}
	@Override
	public int updateNotUsed(CrawlerBillModel model) throws SQLException {
		return mapper.updateNotUsed(model);
	}
}
