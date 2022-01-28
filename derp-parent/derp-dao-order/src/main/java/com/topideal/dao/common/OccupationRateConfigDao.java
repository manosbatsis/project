package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.vo.common.OccupationRateConfigModel;


public interface OccupationRateConfigDao extends BaseDao<OccupationRateConfigModel> {
    /**
     * 查询分页数据
     * @param dto
     * @return
     */
    OccupationRateConfigDTO listDTOByPage(OccupationRateConfigDTO dto);
    /**
     * 获取详情
     * @param dto
     * @return
     */
    OccupationRateConfigDTO searchDTO(OccupationRateConfigDTO dto);
}
