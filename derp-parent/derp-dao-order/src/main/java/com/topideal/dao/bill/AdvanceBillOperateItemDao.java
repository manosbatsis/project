package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillOperateItemModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface AdvanceBillOperateItemDao extends BaseDao<AdvanceBillOperateItemModel> {


    /**
     * 根据预收账单id查看操作日志
     * @param advanceId
     * @return
     */
    List<AdvanceBillOperateItemDTO> getAdvanceById(Long advanceId);



    /**
     * 根据预收账单id删除预收账单日志
     * @param advanceId
     * @return
     * @throws SQLException
     */
    int delItems(Long advanceId) throws SQLException;

    /**
     * 获取预收单的最大审核时间
     */
    List<Map<String, Object>> getMaxAuditDate(List<Long> advanceIds) ;

    /**
     * 获取预收单的最新审核通过记录
     */
    List<AdvanceBillOperateItemModel> getLatestAuditModelByAdvanceIds(List<Long> advanceIds) ;


}
