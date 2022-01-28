package com.topideal.service.timer;

import java.util.List;
import java.util.Map;

import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;

/**
 * 唯品4.0-爬虫账单生成销售出库单
 */
public interface VipCreateSaleOutDepotService {
	/**
	 * 查询未使用过的账单按商家+账单号分组去重
	 */
	public List<Map<String, Object>> searchMerchantIdBillCodeList(Map<String, Object> map);
	/**清空临时表
	 * */
    public void clearTable(Map<String, Object> map);
    /**唯品生成出库单
     * */
    public List<InvetAddOrSubtractRootJson> saveVIPSaleOutDepot(Long merchantId,String billCode,List<Long> idList) throws Exception;
    /**唯品生成出库单
     * */
    public List<InvetAddOrSubtractRootJson> saveVIPSaleInDepot(Long merchantId,String billCode,List<Long> idList) throws Exception;
    /**
	 * 推送库存扣减报文、仓库唯品红冲报文。
	 * @throws Exception
	 */
    public void pushMQ(List<InvetAddOrSubtractRootJson> rootJsonList);
}
