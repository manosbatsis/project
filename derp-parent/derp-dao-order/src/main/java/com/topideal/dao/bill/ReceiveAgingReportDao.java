package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportModel;

import java.util.List;
import java.util.Map;


public interface ReceiveAgingReportDao extends BaseDao<ReceiveAgingReportModel>{


    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    public ReceiveAgingReportDTO listReceiveBillByPage(ReceiveAgingReportDTO dto);


    /**
     * 导出查询
     * @param dto
     * @return
     */
    public  List<ReceiveAgingReportDTO> listForExport(ReceiveAgingReportDTO dto);


    /**
     * 按条件删除
     * @param
     */
    public void deleteByReceiveAging(Map<String,Object> searchQueryMap);


    /**
     * 获取刷新日期
     * @return
     */
   public Map<String, Object> getMaxRefrshDate( Long merchantId);


    List<ReceiveAgingReportDTO> listBySearchQuery(Map<String,Object> queryMap);
}
