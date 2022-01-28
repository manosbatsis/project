package com.topideal.service.pushback;
/**
 *  仓储确认手工导入盘点结果单库存加减成功回推
 *  2020/02/11
 */
public interface InventoryResultsConfirmPushBackService {
	/**
	 * 仓储盘点结果库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateinventoryResultsPushBackInfo(String json, String keys, String topics, String tags)throws Exception;
}
