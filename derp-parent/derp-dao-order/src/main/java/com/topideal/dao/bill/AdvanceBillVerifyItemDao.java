package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillOperateItemModel;
import com.topideal.entity.vo.bill.AdvanceBillVerifyItemModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


public interface AdvanceBillVerifyItemDao extends BaseDao<AdvanceBillVerifyItemModel> {


    /**
     * 根据预收账单查看预收账单核销记录
     * @param id
     * @return
     */
    List<AdvanceBillVerifyItemDTO>  getAdvanceById(long id);


    /**
     * 根据预收账单id删除预收账单核销记录
     * @param advanceId
     * @return
     * @throws SQLException
     */
    int delItems(Long advanceId) throws SQLException;

    /**
     * 根据预收账单id集合查看预收账单的核销记录
     * @param advanceIds
     * @return
     */
    List<AdvanceBillVerifyItemModel>  getAdvancesByIds(List<Long> advanceIds);

    /**
     * 根据预收账单id获取核销总金额
     * @param advanceId
     * @return
     * @throws SQLException
     */
    BigDecimal getTotalVerifyPrice(Long advanceId) throws SQLException;

    /**
     * 获取预收单的最新核销收款记录
     */
    List<AdvanceBillVerifyItemModel> getLatestVerifyModelByAdvanceIds(List<Long> advanceIds) ;
}
