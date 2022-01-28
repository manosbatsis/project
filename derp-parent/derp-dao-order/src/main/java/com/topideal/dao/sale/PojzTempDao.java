package com.topideal.dao.sale;

import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.PojzTempModel;


public interface PojzTempDao extends BaseDao<PojzTempModel> {
		
	/**唯品4.0-获取商家+仓库+po+货号临时结转余量
	 * */
	Integer getPojzNum(Map<String, Object> map);









}
