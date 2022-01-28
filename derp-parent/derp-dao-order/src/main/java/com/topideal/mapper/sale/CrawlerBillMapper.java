package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.CrawlerBillModel;
import com.topideal.mapper.BaseMapper;

/**
 * 爬虫账单
 * @author lian_
 *
 */
@MyBatisRepository
public interface CrawlerBillMapper extends BaseMapper<CrawlerBillModel> {

	public int batchIn(@Param(value = "orderList") List<CrawlerBillModel> orderList);
	/**
	 * 获取爬虫账单的分组信息（po单号、订单的创建时间）
	 * @return
	 */
	public List<CrawlerBillModel> searchBillGroup()throws SQLException;
	/**
	 * 新版唯品-根据临时表账单号、po号、货号查询一条账单明细
	 * @return
	 */
	public CrawlerBillModel getOneByTempGoodsNo(Map<String, Object> map);
	/**
	 * 根据sku和po单号统计唯品账单总量
	 */
	public Map<String, Object> getBySkuAndPo(CrawlerBillModel crawlerBillModel);
	
	/**
	 * 唯品4.0-查询未使用过的账单按商家+账单号分组去重
	 */
	public List<Map<String, Object>> searchMerchantIdBillCodeList(Map<String, Object> map);
	/**
	 * 唯品4.0-查询本商家+账单号未使用过的账单明细按sku去重
	 */
	public List<String> searchSkuList(Map<String, Object> map);
	/**
	 * 唯品4.0-查询本商家+账单号+处理类型+指定账单id未使用过的账单明细
	 */
	public List<Map<String, Object>> getBillList(Map<String, Object> map);
	
	/**
	 * 获取需要生成平台结算单的唯品账单号
	 * @param queryMap
	 * @return
	 */
	public List<String> getPlatformStatementCode(Map<String, Object> queryMap);
	
	/***
	 * 获取平台结算单指定类型汇总明细
	 * @param itemQueryMap
	 * @return 
	 */
	public List<CrawlerBillModel> getPlatformStatementData(Map<String, Object> itemQueryMap);
	
	/**
	 * 获取平台结算单金额和币种
	 * @param amountAndCurrencyMap
	 * @return
	 */
	public CrawlerBillModel getPlatformStatementAmountAndCurrency(Map<String, Object> amountAndCurrencyMap);
	
	/**
	 * 根据SKU 获取最新商品名
	 * @param queryGoodsName
	 * @return
	 */
	public String getLastGoodsNameBySku(Map<String, Object> queryGoodsName);
	/**
	 * 修改唯品账单为未使用
	 * @param model
	 * @throws SQLException
	 */
	public int updateNotUsed(CrawlerBillModel model)throws SQLException;
}

