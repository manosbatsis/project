package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PlatformPurchaseOrderMapper extends BaseMapper<PlatformPurchaseOrderModel> {

	PageDataModel<PlatformPurchaseOrderDTO> getListByPage(PlatformPurchaseOrderDTO dto);

	int updateWhenSubmit(PlatformPurchaseOrderModel updateOrder);

	PlatformPurchaseOrderDTO searchDTOById(Long id);

	List<PlatformPurchaseOrderExportDTO> getExportList(PlatformPurchaseOrderExportDTO dto);

    List<PlatformPurchaseOrderDTO> listPlatformPurchaseOrderByCodeAndPoNo(PlatformPurchaseOrderDTO dto);

    /**
     * 分页查询近两个月的平台采购单(平台采购单未上架信息发送消息)
     * @param map
     * @return
     */
    List<PlatformPurchaseOrderModel> getPurchaseOrderMonth(Map<String, Object> map)throws SQLException;

	List<PlatformPurchaseOrderDTO> listDTO(PlatformPurchaseOrderDTO dto);
}
