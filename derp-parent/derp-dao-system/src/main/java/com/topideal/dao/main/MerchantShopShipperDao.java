package com.topideal.dao.main;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantShopShipperModel;


public interface MerchantShopShipperDao extends BaseDao<MerchantShopShipperModel> {
		

	public List<MerchantShopShipperDTO> listDTO(MerchantShopShipperModel model);

	List<MerchantShopShipperDTO> listForExportShipper(MerchantShopRelDTO dto);

}
