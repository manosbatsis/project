package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.FixedCostPriceDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/16 21:06
 * @Description: 固定成本价盘
 */
public interface FixedCostPriceService {
    /**
     * 分页获取列表
     * @param user
     * @param dto
     * @return
     */
    FixedCostPriceDTO listByPage(User user, FixedCostPriceDTO dto)throws Exception;

    /**
     * 批量删除
     * @param
     */
    void delCostPrice(String ids) throws Exception;

    /**
     * 批量审核
     * @param ids
     * @param user
     */
    void auditCostPrice(String ids, User user) throws Exception;

    int countByDTO(FixedCostPriceDTO dto);

    /**
     * 导出
     * @param user
     * @param dto
     * @return
     */
    List<ExportExcelSheet> exportCostPrice(User user, FixedCostPriceDTO dto);

    /**
     * 导入
     * @param data
     * @param user
     * @return
     */
    Map importCostPrice(Map<Integer, List<List<Object>>> data, User user)throws Exception;
}
