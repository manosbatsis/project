package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipSaleReturnOdepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipSaleReturnOdepotDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface VipSaleReturnOdepotDetailsMapper extends BaseMapper<VipSaleReturnOdepotDetailsModel> {

	/**
	 * 清空本商家、仓库、po、标准条码的销售退明细
	 * @param model
	 * @return
	 */
	int deleteByModel(VipSaleReturnOdepotDetailsModel model);

	/**
	 * 询销售退出库表本本商家、仓库、po、标准条码的销售退出库明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipSaleReturnOdepot(Map<String, Object> queryMap);

	int batchInsert(List<VipSaleReturnOdepotDetailsModel> list);

	List<VipSaleReturnOdepotDetailsDTO> listDTO(VipSaleReturnOdepotDetailsDTO model);

}
