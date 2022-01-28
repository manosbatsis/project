package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;


public interface SdPurchaseConfigDao extends BaseDao<SdPurchaseConfigModel>{

	SdPurchaseConfigDTO getListByPage(SdPurchaseConfigDTO dto);

	SdPurchaseConfigDTO searchDTO(SdPurchaseConfigDTO dto);

	/**
	 * 获取生效失效时间内最新数据
	 * @param queryModel
	 * @return
	 */
	SdPurchaseConfigModel getLastestModel(SdPurchaseConfigModel queryModel);
		










}
