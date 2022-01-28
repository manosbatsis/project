package com.topideal.order.service.purchase;

import java.sql.SQLException;
import java.util.List;


/**
 * 校验商品是否被使用
 * @author zhanghx
 */
public interface CheckGoodsIsUseService {

	/**
	 * 校验商品是否被使用
	 * @return
	 */
	boolean checkGoodsIsUse(List<Long> ids) throws SQLException,RuntimeException;
	
}
