package com.topideal.mapper.main;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantShopShipperModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MerchantShopShipperMapper extends BaseMapper<MerchantShopShipperModel> {

	public List<MerchantShopShipperDTO> listDTO(MerchantShopShipperModel model);

	List<MerchantShopShipperDTO> listForExportShipper(MerchantShopRelDTO dto);
}
