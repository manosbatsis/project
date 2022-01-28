package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.sale.PreSaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface PreSaleOrderItemDao extends BaseDao<PreSaleOrderItemModel> {
    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<PreSaleOrderItemDTO> listPreSaleOrderItemDTO(PreSaleOrderItemDTO dto)throws SQLException;

    /**
     * 根据表头Id查询的数据（除itemIds之外的数据） 要删除预售单商品id
     * @param itemIds
     * @param id
     * @return
     */
    List<PreSaleOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id);

    /**
     * 根据表头Id删除不要的数据（除itemIds之外的数据）
     * @param itemIds
     * @param id 表头id
     */
    void delBesidesIds(List<Long> itemIds, Long id);

    /**
     *  根据预售单号、po号、商品货号查询预售单明细
     * @param map
     * @return
     * @throws SQLException
     */
    List<PreSaleOrderItemModel> getDetailsByReceive (Map<String, Object> map);
}
