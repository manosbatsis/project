package com.topideal.service.pushback.sale;
/**
 * 移库单商品收发明细生成成功回推
 * 2019/02/27
 * 杨创
 */
public interface MoveOrderPushBackService {
	/**
	 * 移库单商品收发明细生成成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateMoveOrderStatus(String json, String keys, String topics, String tags)throws Exception;

}
