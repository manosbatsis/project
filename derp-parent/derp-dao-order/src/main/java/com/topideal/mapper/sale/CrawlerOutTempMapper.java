package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.CrawlerOutTempModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CrawlerOutTempMapper extends BaseMapper<CrawlerOutTempModel> {
    
	/**清空临时表*/
    public void clearTable(Map<String, Object> map);
	
    /**唯品4.0-按商家id、仓库id、PO号、货号统计可核销量
     * */
    public Integer getVerifiSumNum(Map<String, Object> map);
    /**4.0-统计货号库存余量
	 * */
	public Integer getSurplusNum(Map<String, Object> map);
	/**扣减临时表商家、仓库、货号的库存量
	 * */
    public int updateLowerNum(Map<String, Object> map);
    /**扣减临时表商家、仓库、po号、货号的可核销量
	 * */
    public int updateLowerVerifiNum(Map<String, Object> map);
    /**
     * 唯品4.0-查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种分组
     * */
    public List<Map<String, Object>> getListByBillTypeCurrency(Map<String, Object> map);

    /**
     * 新版云集该账单该 sku 账单和sku
     * @return
     */
    //public List<Map<String,Object>> getYunjiBillIdSaleCodeList(Map<String, Object> map);
  

    /**
     *新版云集该账单该 事业部  sku 账单和sku
     * @return
     */
   // public List<Map<String,Object>> getBuYunjiBillIdSaleCodeList(Map<String, Object> map);
    /**
     * 新版云集销售订单下的sku下的商品量
     * @param map
     * @return
     */
    public List<Map<String,Object>> getYunjiOrderBillList(Map<String, Object> map);
    /**
     * 唯品4.0相同sku的分配明细汇总后出库
     * */
    public List<Map<String,Object>> getListSumGroupBySku(CrawlerOutTempModel tempMode);

}
