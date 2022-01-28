package com.topideal.service.main;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantShopShipperModel;

public interface MerchantShopShipperService {

	public List<MerchantShopShipperModel> getListByModel(MerchantShopShipperModel model)throws Exception;

	public List<MerchantShopShipperDTO> getListDTOByModel(MerchantShopShipperModel model);

	public List<Map<String, Object>> listForExportShipper(MerchantShopRelDTO dto);
}
