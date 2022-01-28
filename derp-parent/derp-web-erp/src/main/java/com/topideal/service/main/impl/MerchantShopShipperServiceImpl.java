package com.topideal.service.main.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.main.MerchantShopRelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.main.MerchantShopShipperDao;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantShopShipperModel;
import com.topideal.service.main.MerchantShopShipperService;

@Service
public class MerchantShopShipperServiceImpl implements MerchantShopShipperService{
  
	@Autowired
	private MerchantShopShipperDao shipperDao;
	
	public List<MerchantShopShipperModel> getListByModel(MerchantShopShipperModel model) throws Exception{
		return shipperDao.list(model);
	}
	public List<MerchantShopShipperDTO> getListDTOByModel(MerchantShopShipperModel model){
		return shipperDao.listDTO(model);
	}

	@Override
	public List<Map<String, Object>> listForExportShipper(MerchantShopRelDTO dto) {
		List<MerchantShopShipperDTO> shopShipperDTOS = shipperDao.listForExportShipper(dto);
		List<Map<String, Object>> shopShipperExportList = new ArrayList<>();
		for (MerchantShopShipperDTO shopShipperDTO : shopShipperDTOS) {
			Map<String, Object> exportMap = new HashMap<>();
			exportMap.put("shopCode", shopShipperDTO.getShopCode());
			exportMap.put("shopName", shopShipperDTO.getShopName());
			exportMap.put("merchantName", shopShipperDTO.getMerchantName());
			exportMap.put("buName", shopShipperDTO.getBuName());
			shopShipperExportList.add(exportMap);
		}
		return shopShipperExportList;
	}
}
