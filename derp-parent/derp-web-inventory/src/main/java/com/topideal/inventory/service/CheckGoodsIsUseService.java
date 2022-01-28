package com.topideal.inventory.service;

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
	boolean checkGoodsIsUse(List<Long> ids) throws Exception;
	
	/**
	 * 校验仓库是否被使用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean checkDepotIsUse (List<Long> ids)throws Exception;
	
}
