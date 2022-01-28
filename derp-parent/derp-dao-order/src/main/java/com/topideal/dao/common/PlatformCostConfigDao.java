package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.vo.common.PlatformCostConfigModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.SQLException;
import java.util.List;


public interface PlatformCostConfigDao extends BaseDao<PlatformCostConfigModel> {

    /**
     * 分页查询列表
     * @param dto
     * @return
     */
    PlatformCostConfigDTO listDTOByPage(PlatformCostConfigDTO dto);


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

    /*
    校验是否公司+事业部+客户+平台名称+生效时间+失效时间,已存在相同配置
    * */
    PlatformCostConfigDTO searchByCheck(PlatformCostConfigDTO dto);
}
