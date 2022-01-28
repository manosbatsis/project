package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseLinkInfoDTO;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;


public interface PurchaseLinkInfoDao extends BaseDao<PurchaseLinkInfoModel> {

	PurchaseLinkInfoDTO getPurchaseLinkDtoByPurchaseId(Long id);

	PurchaseLinkInfoDTO getPurchaseLinkDtoById(Long id);
		










}
