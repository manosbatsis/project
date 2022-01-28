package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillItemModel;


public interface AdvanceBillItemDao extends BaseDao<AdvanceBillItemModel> {

    /**
     * 根据ids查询预收账单表体信息
     */
	List<Map<String, Object>> getAdvanceBillItemList(List<Long> ids);
    /**
     * 根据预收账单id查看表体明细
     * @param advanceId
     * @return
     */
    List<AdvanceBillItemDTO> getAdvanceById(Long advanceId);


    /**
     * 根据预收账单id删除预收账单详情
     * @param advanceId
     * @return
     * @throws SQLException
     */
    int delItems(Long advanceId) throws SQLException;


    /**
     * 根据费项维度查询预收总金额
     * @return
     */
    List<AdvanceBillItemDTO> synNcItemByIds(List<Long> advanceId);


    /**
     * 获取预收账单总金额
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> listItemPrice(List<Long> ids) throws SQLException;

    /**
     * 根据关联的销售订单号统计各预收单的预收款
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> listItemPriceByOrderCodes(List<Long> ids, List<String> orderCodes) throws SQLException;

    /**
     * 根据销售订单号查询未被勾稽的已审核预收单明细
     * @param orderCodes 销售订单号集合
     * @return
     */
    List<AdvanceBillItemDTO> listWithoutVerify(List<String> orderCodes);

    /**
     * 账单明细id集合查询预收账单明细
     * @param ids 账单明细id
     * @return
     */
    List<AdvanceBillItemModel> listByIds(List<Long> ids);
}
