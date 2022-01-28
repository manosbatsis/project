package com.topideal.order.service.platformdata;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformGoodsCategoryService {

    /**
     * 分页
     * @param dto
     * @return
     */
    PlatfromGoodsCategoryDTO getListByPage(PlatfromGoodsCategoryDTO dto, User user);

    /**
     * 导入
     * @param data
     * @param user
     * @return
     */
    Map<String, Object> importPlatformGoodsCategory(List<Map<String, String>> data, User user) throws SQLException;
}
