package com.topideal.order.service.sale;


import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.mongo.entity.FileTaskMongo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformTemporaryCostOrderService {

    /**
     * 分页查询数据
     * @param user
     * @param dto
     * @return
     * @throws SQLException
     */
    PlatformTemporaryCostOrderDTO listPlatformTemporaryCost(User user, PlatformTemporaryCostOrderDTO dto) throws SQLException;

    /**
     * 根据id查看详情
     * @param user
     * @param id
     * @return
     */
    PlatformTemporaryCostOrderDTO searchDTOById(User user,Long id) throws SQLException;


    /**
     * 校验发货日期是否已经关账
     * @param user
     * @param id
     * @return
     */
    Boolean checkDate(User user,Long id);


    /**
     * 删除
     * @param user
     * @param ids
     * @return
     * @throws SQLException
     */
    Map deleteById(User user, String ids) throws SQLException;


    /**
     * 修改暂估费用金额
     * @param user
     * @param dto
     * @return
     * @throws SQLException
     */
    Map updatePlatformTemporary(User user,PlatformTemporaryCostOrderDTO dto) throws SQLException;


    /**
     * 获取导出数据
     * @param dto
     * @return
     */
    public List<Map<String, Object>> listForMapItem(PlatformTemporaryCostOrderDTO dto);



    /**
     * 生成暂估费用单
     *
     * @param user
     * @param month
     * @return
     */
    Map savePlatformCostOrder(User user, String month, String orderType, String storePlatformCodes) throws SQLException;

    /**
     * 生成toc平台暂估费用导出excel
     */
    String createTempCostExcel(FileTaskMongo task, String basePath) throws Exception;
}
