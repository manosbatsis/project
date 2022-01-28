package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.vo.common.PlatformCostConfigModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PlatformCostConfigMapper extends BaseMapper<PlatformCostConfigModel> {


    /*分页查询所有*/
    PageDataModel<PlatformCostConfigDTO> listDTOByPage(PlatformCostConfigDTO dto);

    /**
     * 根据公司+平台的维度查询在失效日期范围内的最新审核记录
     * @param dto
     * @return
     */
    List<PlatformCostConfigDTO> getLatestList(PlatformCostConfigDTO dto) throws Exception;

    /**
     * 根据公司+平台+事业部的维度查询在失效日期范围内的最新审核记录
     * @param dto
     * @return
     */
    PlatformCostConfigDTO getLatestDTO(PlatformCostConfigDTO dto) throws Exception;

    /**
     * 校验是否公司+事业部+客户+平台名称+生效时间+失效时间,已存在相同配置
     * @param model
     * @return
     */
    PlatformCostConfigDTO searchByCheck(PlatformCostConfigDTO model);
}
