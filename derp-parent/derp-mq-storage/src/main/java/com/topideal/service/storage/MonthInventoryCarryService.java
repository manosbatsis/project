package com.topideal.service.storage;
/**
 * 月库存结转Service
 * @author 杨创
 *2018/7/16
 */
public interface MonthInventoryCarryService {
	/**
	 * 保存月库存结转信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveMonthInventoryCarryInfo(String json, String keys, String topics, String tags)throws Exception;
}
