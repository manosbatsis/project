package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillOperateItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface AdvanceBillOperateItemMapper extends BaseMapper<AdvanceBillOperateItemModel> {


    /**
     * 根据预收账单id查看操作日志
     * @param advanceId
     * @return
     */
    List<AdvanceBillOperateItemDTO> getAdvanceById(@Param("advanceId") long advanceId);

    /**
     * 根据预收账单id删除操作日志
     * @param advanceId
     * @return
     */
    int delItems(@Param("advanceId") long advanceId);

    /**
     * 获取预收单的最大审核时间
     */
    List<Map<String, Object>> getMaxAuditDate(@Param("advanceIds") List<Long> advanceIds);

    /**
     * 获取预收单的最新审核通过记录
     */
    List<AdvanceBillOperateItemModel> getLatestAuditModelByAdvanceIds(@Param("advanceIds") List<Long> advanceIds) ;
}
