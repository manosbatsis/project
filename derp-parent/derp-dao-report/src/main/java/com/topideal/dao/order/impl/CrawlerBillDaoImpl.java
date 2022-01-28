package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.entity.vo.order.CrawlerBillModel;
import com.topideal.mapper.order.CrawlerBillMapper;

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
    @SuppressWarnings("rawtypes")
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
	public List<CrawlerBillModel> listBillModel() throws SQLException  {
		return mapper.listBillModel();
	}
	

	
	/**
	 * 定时器:查询昨天之前的所有的类型为1的爬虫账单
	 * @param model
	 * @return
	 */
	@Override
	public List<CrawlerBillModel> timeBeforeYesterdayCrawler(CrawlerBillModel model) throws SQLException {
		return mapper.timeBeforeYesterdayCrawler(model);
	}
	
	/**
	 * 根据po、sku（单个/多个）去爬虫账单表账单总量
	 */
	@Override
	public List<Map<String, Object>> getBillByPoAndSku(Map<String, Object> crawlerQueryMap) {
		return mapper.getBillByPoAndSku(crawlerQueryMap);
	}
	
	/**
	 * 查询本po号、sku的所有爬虫账单明细
	 */
	@Override
	public List<CrawlerBillModel> listByPoAndSkus(Map<String, Object> queryMap) {
		return mapper.listByPoAndSkus(queryMap);
	}
	@Override
	public String getBillTypeByBillCode(String vipBillCode) {
		return mapper.getBillTypeByBillCode(vipBillCode);
	}
	@Override
	public String getCustomerNameByPO(String poNo) {
		return mapper.getCustomerNameByPO(poNo);
	}
	@Override
	public List<Map<String, Object>> getVipAutoVeriList(Map<String, Object> params) {
		return mapper.getVipAutoVeriList(params);
	}
	@Override
	public int updateVipVeriState(Map<String, Object> updateMap) {
		return mapper.updateVipVeriState(updateMap) ;
	}
	@Override
	public Integer getDecreaseNum(Map<String, Object> queryCrawlerMap) {
		return mapper.getDecreaseNum(queryCrawlerMap);
	}
	@Override
	public int countByPoAndSkus(Map<String, Object> queryMap) {
		return mapper.countByPoAndSkus(queryMap);
	}
	
	/**
	 * 获取当年云集的 账单数据
	 */
	@Override
	public List<Map<String, Object>> getYearYunjiCrawlerBill(CrawlerBillModel model) throws SQLException {
		return mapper.getYearYunjiCrawlerBill(model);
	}
	/**
	 *  获取当月云集的账单数据
	 */
	@Override
	public List<Map<String, Object>> getMonthYunjiCrawlerBill(CrawlerBillModel model) throws SQLException {
		return mapper.getMonthYunjiCrawlerBill(model);
	}
	/**
	 * 获取当日云集的账单数据
	 */
	@Override
	public List<Map<String, Object>> getDayYunjiCrawlerBill(CrawlerBillModel model) throws SQLException {
		return mapper.getDayYunjiCrawlerBill(model);
	}
	@Override
	public List<Map<String, Object>> getUnVeriList(Map<String, Object> params) {
		return mapper.getUnVeriList(params);
	}
	

    
}
