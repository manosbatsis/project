package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformCostConfigService {

    /**
     * 分页查询获取数据
     * @param dto
     * @param user
     * @return
     * @throws Exception
     */
    PlatformCostConfigDTO listDTOByPage(PlatformCostConfigDTO dto, User user) throws Exception;


    /**
     * 新增和审核
     * @param dto
     * @param user
     * @param operate
     * @return
     * @throws Exception
     */
    Map saveOrAudit(PlatformCostConfigDTO dto, User user, String operate) throws Exception;


    /**
     * 获取详情
     * @param id
     * @return
     */
    PlatformCostConfigDTO searchDetail(Long id) throws Exception ;

    /**
     * 根据id查看表体
     * @param configId
     * @return
     * @throws Exception
     */
    List<PlatformCostConfigItemDTO> searchItemDetail(Long configId) throws Exception;


    /**
     * 删除
     */
    Map delSdSaleConfig(String ids) throws Exception;


    /**
     * 反审
     * @param id
     * @param user
     * @return
     */
    Map counterTrial(Long id,User user) throws SQLException;



}
