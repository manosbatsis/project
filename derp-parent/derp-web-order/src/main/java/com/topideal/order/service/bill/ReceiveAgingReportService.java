package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportModel;
import com.topideal.mongo.entity.FileTaskMongo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReceiveAgingReportService {

    /**
     * 分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
   public ReceiveAgingReportDTO listReceiveBillByPage(ReceiveAgingReportDTO dto, User user) throws SQLException;

    /**
     * 获取刷新日期
     * @return
     * @throws SQLException
     */
    public Map<String, Object>  getMaxRefrshDate(Long merchantId) throws SQLException;


    /**
     * 导出查询
     * @param dto
     * @return
     */
   public List<ReceiveAgingReportDTO> listForExport(ReceiveAgingReportDTO dto, User user);


    /**
     * 刷新
     * @param dto
     * @return
     */
   public Map<String,Object> refreshReceiveAgingReport(ReceiveAgingReportDTO dto);


    /**
     * 根据id查看详情
     * @param id
     * @return
     */
   public ReceiveAgingReportDTO getDetailsById(Long id) throws SQLException;

    /**
     * 月结导出
     * @param task
     * @param basePath
     * @return
     */
    String createMonthlyReportExcel(FileTaskMongo task, String basePath) throws Exception;
}
