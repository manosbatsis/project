package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveAgingReportMapper extends BaseMapper<ReceiveAgingReportModel> {


    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    PageDataModel<ReceiveAgingReportDTO> getDTOListByPage(ReceiveAgingReportDTO dto);


    /**
     * 导出查询
     * @param dto
     * @return
     */
    List<ReceiveAgingReportDTO> listForExport(ReceiveAgingReportDTO dto);


    /**
     * 删除
     */
    void deleteByReceiveAging(Map<String,Object> searchQueryMap);


    Map<String, Object> getMaxRefrshDate(@Param("merchantId") Long merchantId);


    List<ReceiveAgingReportDTO> listBySearchQuery(Map<String, Object> queryMap);
}
