package com.topideal.order.service.platformdata;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;

public interface PlatformCategoryConfigService {

    /**
     * 分页获取
     * @param dto
     * @return
     */
    PlatformCategoryConfigDTO getListByPage(PlatformCategoryConfigDTO dto);

    /**
     * 新增
     * @param dto
     */
    void savePlatformCategoryConfig(PlatformCategoryConfigDTO dto) throws SQLException;

    /**
     * 导入
     * @param data
     * @param user
     * @return
     */
    Map<String, Object> importPlatformCategoryConfig(List<Map<String, String>> data, User user) throws SQLException;

    /**
     * 根据当前商家查询列表
     * @param dto
     * @return
     */
    List<PlatformCategoryConfigModel> getListByMerchantId(PlatformCategoryConfigDTO dto) throws SQLException;
    
    /**
     * 获取下拉数据
     * @param dto
     * @return
     */
    List<SelectBean> getSelectBean(PlatformCategoryConfigDTO dto) throws SQLException;
}
