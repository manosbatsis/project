package com.topideal.mapper.purchase;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.purchase.PurchaseLinkInfoDTO;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseLinkInfoMapper extends BaseMapper<PurchaseLinkInfoModel> {

	PurchaseLinkInfoDTO getPurchaseLinkDtoByPurchaseId(@Param("id")Long id);

	PurchaseLinkInfoDTO getPurchaseLinkDtoById(@Param("id")Long id);



}
