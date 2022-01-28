package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuLocationSummaryDTO;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/18 16:18
 * @Description: 库位进销存汇总
 */
public interface BuLocationSummaryService {

    /**
     * 分页
     * @param user
     * @param dto
     * @return
     */
    BuLocationSummaryDTO listByPage(User user, BuLocationSummaryDTO dto);
}
