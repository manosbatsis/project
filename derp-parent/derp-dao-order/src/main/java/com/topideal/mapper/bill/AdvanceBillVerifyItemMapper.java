package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillVerifyItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


@MyBatisRepository
public interface AdvanceBillVerifyItemMapper extends BaseMapper<AdvanceBillVerifyItemModel> {

    /**
     * 根据预收账单id查看核销记录
     * @param advanceId
     * @return
     */
    List<AdvanceBillVerifyItemDTO> getAdvanceById(@Param("advanceId") long advanceId);


    /**
     * 根据预收账单id删除核销记录
     * @param advanceId
     * @return
     */
    int delItems(@Param("advanceId") Long advanceId);

    /**
     * 根据预收账单id获取核销金额
     * @param advanceId
     * @return
     */
    BigDecimal getTotalVerifyPrice(@Param("advanceId") Long advanceId);


    /**
     * 根据预收账单id集合查看预收账单最近日期的核销记录
     * @param advanceIds
     * @return
     */
    List<AdvanceBillVerifyItemModel>  getAdvancesByIds(@Param("advanceIds")List<Long> advanceIds);

    /**
     * 获取预收单的最新核销收款记录
     */
    List<AdvanceBillVerifyItemModel> getLatestVerifyModelByAdvanceIds(@Param("advanceIds") List<Long> advanceIds) ;
}
