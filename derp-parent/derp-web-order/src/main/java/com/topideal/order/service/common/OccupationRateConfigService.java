package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;

public interface OccupationRateConfigService {
    /**
     * 查询列表信息 分页
     * @param dto
     * @return
     */
    OccupationRateConfigDTO listDTOByPage(OccupationRateConfigDTO dto, User user) throws Exception;
    /**
     * 保存
     * @param dto
     * @throws Exception
     */
    void saveOccuptionRateCongfig(OccupationRateConfigDTO model, User user) throws Exception;

    /**
     * 删除
     * @param dto
     * @throws Exception
     */
    void delOccuptionRateCongfig(String ids) throws Exception;
}
