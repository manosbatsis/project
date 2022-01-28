package com.topideal.report.service.reporting.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.entity.dto.InventoryWarningCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.dao.reporting.InventoryFallingPriceReservesDao;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;
import com.topideal.report.service.reporting.InventoryFallingPriceReservesService;

@Service
public class InventoryFallingPriceReservesServiceImpl implements InventoryFallingPriceReservesService{

	@Autowired
	InventoryFallingPriceReservesDao inventoryFallingPriceReservesdao ;

	@SuppressWarnings("unchecked")
	@Override
	public InventoryFallingPriceReservesDTO listInventoryFallingPriceReservesPage(InventoryFallingPriceReservesDTO model) {
		List<InventoryFallingPriceReservesDTO> list = inventoryFallingPriceReservesdao.listInventoryFallingPriceReservesPage(model) ;
		int total = inventoryFallingPriceReservesdao.countInventoryFallingPriceReserves(model) ;
		model.setList(list);
		model.setTotal(total);
		return model;
	}
	public int getCount(InventoryFallingPriceReservesDTO dto) {
		int total = inventoryFallingPriceReservesdao.countInventoryFallingPriceReserves(dto) ;
		return total;
	}
	@Override
	public List<InventoryFallingPriceReservesDTO> getExportList(InventoryFallingPriceReservesDTO inventoryFallingPriceReservesModel) throws SQLException {
		
		List<InventoryFallingPriceReservesDTO> returnList = inventoryFallingPriceReservesdao.getExportList(inventoryFallingPriceReservesModel) ;
		
		for (InventoryFallingPriceReservesDTO model : returnList) {

			//剩余效期占比转换
			BigDecimal surplusProportion = model.getSurplusProportion();
			if(surplusProportion!=null) {
				surplusProportion = surplusProportion.multiply(new BigDecimal(100)).setScale(2);
				model.setSurplusProportion(surplusProportion);
			}
			
			//跌价准备比例转换
			BigDecimal depreciationReserveProportion = model.getDepreciationReserveProportion();
			if(depreciationReserveProportion!=null) {
				depreciationReserveProportion = depreciationReserveProportion.multiply(new BigDecimal(100)).setScale(2);
				model.setDepreciationReserveProportion(depreciationReserveProportion);
			}
			//2个月剩余效期占比转换
			BigDecimal twoMonthsSurplusProportion = model.getTwoMonthsSurplusProportion();
			if(twoMonthsSurplusProportion!=null){
				twoMonthsSurplusProportion = twoMonthsSurplusProportion.multiply(new BigDecimal(100)).setScale(2);
			    model.setTwoMonthsSurplusProportion(twoMonthsSurplusProportion);
			}
		}
		
		return returnList;
	}

	@Override
	public List<InventoryWarningCountDto> countInventoryWarning(Map<String, Object> params) throws SQLException {
		return inventoryFallingPriceReservesdao.countInventoryWarning(params);
	}

}
