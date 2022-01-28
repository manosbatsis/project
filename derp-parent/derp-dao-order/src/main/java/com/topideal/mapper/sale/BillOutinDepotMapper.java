package com.topideal.mapper.sale;

import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface BillOutinDepotMapper extends BaseMapper<BillOutinDepotModel> {

	PageDataModel<BillOutinDepotDTO> getListByPage(BillOutinDepotDTO dto);

	BillOutinDepotDTO searchDTOById(Long id);

	BillOutinDepotModel searchUnDelModel(BillOutinDepotModel tempModel);

	Integer getOrderCount(BillOutinDepotDTO dto);

	List<BillOutinDepotDTO> listDto(BillOutinDepotDTO dto);

	List<BillOutinDepotItemDTO> getExportItemList(BillOutinDepotDTO dto);

	/**
	 * 查询出同个业务单号和同个事业部的账单出库单的最大出库时间
	 */
	Timestamp getMaxOutDepotDate(@Param("billCodes") List<String> billCodes, @Param("buId") Long buId);

}
