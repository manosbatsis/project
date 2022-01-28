package com.topideal.service;

import java.util.List;

import com.topideal.mongo.tools.PageMongo;

import net.sf.json.JSONObject;

/**
 * 爬虫日志
 */

public interface CrawlerBillLogService {

	PageMongo searchLog(PageMongo pageMongo, String platformType, String saleOutDepotCode, String status,
                        String billCode, String goodsNo, String errorMsg, String createDateStr, Long depotId, Long merchantId, String poNo);
	/**
	 * 发送日志到logMQ
	 * @param ids
	 * @throws Exception 
	 */
	boolean sendMQ(String ids) throws Exception;
	/**
	 * 统计数量
	 * @param platformType
	 * @param saleOutDepotCode
	 * @param status
	 * @param billCode
	 * @param goodsNo
	 * @param errorMsg
	 * @param createDateStr
	 * @param poNo 
	 * @return
	 */
	Integer count(String platformType, String saleOutDepotCode, String status, String billCode, String goodsNo,
                  String errorMsg, String createDateStr, Long depotId, Long merchantId, String poNo);

	/**
	 * 获取导出的数据
	 * @param platformType
	 * @param saleOutDepotCode
	 * @param status
	 * @param billCode
	 * @param goodsNo
	 * @param errorMsg
	 * @param createDateStr
	 * @param count
	 * @param poNo 
	 * @return
	 */
	List getExportList(String platformType, String saleOutDepotCode, String status,
                       String billCode, String goodsNo, String errorMsg, String createDateStr, Integer count, Long depotId, Long merchantId, String poNo);
	
}
