package com.topideal.service.main;


import com.topideal.entity.vo.main.MerchantRelModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 子商家
 * @author zhanghx
 */
public interface MerchantRelService {
	
	/**
	 * 获取集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<MerchantRelModel> list(MerchantRelModel model) throws SQLException;
	
	/**
	 *  获取当前商家的所有代理商家
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    List<MerchantRelModel> getRelMerchantIds(MerchantRelModel model) throws SQLException;
}
