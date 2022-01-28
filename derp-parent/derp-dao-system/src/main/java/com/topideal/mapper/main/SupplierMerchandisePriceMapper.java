package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface SupplierMerchandisePriceMapper extends BaseMapper<SupplierMerchandisePriceModel> {

    /**
     * 列表查询(分页)
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<SupplierMerchandisePriceDTO> getDTOListByPage(SupplierMerchandisePriceDTO dto) throws SQLException;
    /**
     * 根据条件查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<SupplierMerchandisePriceDTO> queryDTOList(SupplierMerchandisePriceDTO dto)throws SQLException;

    /**
     * 获取详情
     */
    SupplierMerchandisePriceDTO getDetails(Long id) throws SQLException;
    
    /**
     * 采购订单获取采购价格
     * @param model
     * @return
     */
	SupplierMerchandisePriceModel getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model);

    /**
     * 统计各个状态下的采购价格数量
     * @param dto
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> statisticsStateNum(SupplierMerchandisePriceDTO dto);

    /**
     * 根据id集合查询
     * @param ids
     * @return
     * @throws SQLException
     */
    List<SupplierMerchandisePriceModel> listByIds(@Param("ids") List<Long> ids);

    SupplierMerchandisePriceDTO searchDTOById(@Param("id")Long id);

    /**
     * 根据“公司+事业部+条码+库存类型”分组查询审核日期最新的几率
     * @param model
     * @return
     */
    List<SupplierMerchandisePriceModel> listGroupByStockType(SupplierMerchandisePriceModel model);
}
