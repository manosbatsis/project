package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SdPurchaseConfigMapper extends BaseMapper<SdPurchaseConfigModel> {

	PageDataModel<SdPurchaseConfigDTO> getListByPage(SdPurchaseConfigDTO dto);

	SdPurchaseConfigDTO searchDTO(SdPurchaseConfigDTO dto);

	SdPurchaseConfigModel getLastestModel(SdPurchaseConfigModel queryModel);



}
