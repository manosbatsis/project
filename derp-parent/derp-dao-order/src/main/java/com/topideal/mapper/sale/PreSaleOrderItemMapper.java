package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.sale.PreSaleOrderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PreSaleOrderItemMapper extends BaseMapper<PreSaleOrderItemModel> {

    /**
     * 查询所有数据
     * @return
     */
    List<PreSaleOrderItemDTO> listPreSaleOrderItemDTO(PreSaleOrderItemDTO dto)throws SQLException;
    /**
     * 根据表头Id查询的数据（除itemIds之外的数据） 要删除预售单商品id
     * @param itemIds
     * @param orderId
     * @return
     */
    List<PreSaleOrderItemModel> getListByBesidesIds (@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);

    /**
     * 根据表头Id删除不要的数据（除itemIds之外的数据）
     * @param itemIds
     * @param orderId
     */
    void delBesidesIds(@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);

    /**
     *  根据预售单号、po号、商品货号查询预售单明细
     * @param map
     * @return
     * @throws SQLException
     */
    List<PreSaleOrderItemModel> getDetailsByReceive (Map<String, Object> map);
}
