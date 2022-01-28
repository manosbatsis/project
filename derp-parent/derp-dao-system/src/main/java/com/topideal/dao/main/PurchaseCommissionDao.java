package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;

public interface PurchaseCommissionDao extends BaseDao<PurchaseCommissionModel> {

	PurchaseCommissionDTO getListByPage(PurchaseCommissionDTO dto);
		










}
