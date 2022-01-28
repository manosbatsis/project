package com.topideal.service.pushback.sale;
/**
 * 库位调整单商品收发明细生成成功回推
 */
public interface InventoryLocationAdjustPushBackService {
	/**
	 * 库位调整单商品收发明细生成成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateAdjsutOrderStatus(String json, String keys, String topics, String tags)throws Exception;

}
