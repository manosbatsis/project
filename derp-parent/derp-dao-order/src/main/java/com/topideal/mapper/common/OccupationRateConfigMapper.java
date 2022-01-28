package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.vo.common.OccupationRateConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface OccupationRateConfigMapper extends BaseMapper<OccupationRateConfigModel> {
    /**
     * 查询分页数据
     * @param dto
     * @return
     */
    PageDataModel<OccupationRateConfigDTO> listDTOByPage (OccupationRateConfigDTO dto);

    /**
     * 获取详情
     * @param dto
     * @return
     */
    OccupationRateConfigDTO searchDTO(OccupationRateConfigDTO dto);
}
