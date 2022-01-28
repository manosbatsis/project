package com.topideal.mapper.sale;

import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.PojzTempModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PojzTempMapper extends BaseMapper<PojzTempModel> {
   
	/**唯品4.0-获取商家+仓库+po+货号临时结转余量
	 * */
	Integer getPojzNum(Map<String, Object> map);

}
