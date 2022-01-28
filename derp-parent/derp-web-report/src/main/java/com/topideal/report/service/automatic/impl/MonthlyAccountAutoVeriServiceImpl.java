package com.topideal.report.service.automatic.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationDao;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationItemDao;
import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;
import com.topideal.report.service.automatic.MonthlyAccountAutoVeriService;

@Service
public class MonthlyAccountAutoVeriServiceImpl implements MonthlyAccountAutoVeriService{

	@Autowired
	private MonthlyAccountAutomaticVerificationDao monthlyAccountAutomaticVerificationDao;
	@Autowired
	private MonthlyAccountAutomaticVerificationItemDao monthlyAccountAutomaticVerificationItemDao ;

	@Override
	public MonthlyAccountAutomaticVerificationDTO listAutomaticVeriByPage(MonthlyAccountAutomaticVerificationDTO dto) {
		
		dto = monthlyAccountAutomaticVerificationDao.listAutomaticVeriByPage(dto) ;
		
		Integer total = monthlyAccountAutomaticVerificationDao.countList(dto) ;
		
		dto.setTotal(total);
		
		return dto;
	}

	@Override
	public List<MonthlyAccountAutomaticVerificationItemModel> listForExport(
			MonthlyAccountAutomaticVerificationDTO dto) throws SQLException {
		
		MonthlyAccountAutomaticVerificationItemModel item = new MonthlyAccountAutomaticVerificationItemModel() ;
		item.setDepotId(dto.getDepotId());
		item.setMerchantId(dto.getMerchantId());
		item.setMonth(dto.getMonth());
		
		List<MonthlyAccountAutomaticVerificationItemModel> list = monthlyAccountAutomaticVerificationItemDao.list(item);
		
		for (MonthlyAccountAutomaticVerificationItemModel monthlyAccountAutomaticVerificationItemModel : list) {
			
			String type = monthlyAccountAutomaticVerificationItemModel.getType();
			
			type = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type) ;
			
			monthlyAccountAutomaticVerificationItemModel.setType(type);
			
		}
		
		return list;
	}

	@Override
	public MonthlyAccountAutomaticVerificationModel searchById(Long id) throws SQLException {
		return monthlyAccountAutomaticVerificationDao.searchById(id);
	}

	@Override
	public int modifyNullValue(MonthlyAccountAutomaticVerificationModel model) {
		return monthlyAccountAutomaticVerificationDao.modifyNullValue(model);
	}
}
