package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.mongo.entity.FileTaskMongo;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:43
 * @Description: Toc暂估月结快照
 */
public interface TocTempReceiveBillMonthlyService {

    /**
     * 获取暂估收入月结分页数据
     * @param user
     * @param dto
     * @return
     */
    TocTempReceiveBillItemMonthlyDTO listReceiveByPage(User user, TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 获取暂估费用月结分页数据
     * @param user
     * @param dto
     * @return
     */
    TocTempCostBillItemMonthlyDTO listCostByPage(User user, TocTempCostBillItemMonthlyDTO dto);

    /**
     * count
     * @param dto
     * @return
     */
    int countByDTO(TocTempReceiveBillItemMonthlyDTO dto, String type);

    /**
     * 应收月结导出
     * @param task
     * @param basePath
     * @return
     */
    String createReceiveBillExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 费用月结导出
     * @param task
     * @param basePath
     * @return
     */
    String createCostBillExcel(FileTaskMongo task, String basePath) throws Exception;
}
