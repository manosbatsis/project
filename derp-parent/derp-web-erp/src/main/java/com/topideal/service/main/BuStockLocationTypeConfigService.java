package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/13 17:00
 * @Description: 事业部库位类型
 */
public interface BuStockLocationTypeConfigService {

    /**
     * 获取分页数据
     * @param user
     * @param dto
     * @return
     */
    BuStockLocationTypeConfigDTO listByPage(User user, BuStockLocationTypeConfigDTO dto);

    /**
     * 新增库位类型
     * @param user
     * @param dto
     * @return
     */
    void addStockLocationType(User user, BuStockLocationTypeConfigDTO dto) throws Exception;

    /**
     * 修改库位类型
     * @param user
     * @param model
     */
    void modifyStockLocationType(User user, BuStockLocationTypeConfigModel model) throws Exception;

    /**
     * 通过开店公司+开店事业部获取下拉列表
     * @param dto
     * @return
     */
    List<SelectBean> getSelectBeanByBu(BuStockLocationTypeConfigDTO dto) throws Exception;
}
