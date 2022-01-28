package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseCommissionMapper extends BaseMapper<PurchaseCommissionModel> {

	PageDataModel<PurchaseCommissionDTO> getListByPage(PurchaseCommissionDTO dto);



}
