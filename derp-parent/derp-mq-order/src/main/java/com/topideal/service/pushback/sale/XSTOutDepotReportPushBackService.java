package com.topideal.service.pushback.sale;
/**
 * 销售退出库打托减库存成功回推
 * @author wenyan
 *
 */
public interface XSTOutDepotReportPushBackService {
	/**
	 * 销售退出库打托减库存成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSTOutDepotReportPushBack(String json,String keys,String topics,String tags)throws Exception;
}
