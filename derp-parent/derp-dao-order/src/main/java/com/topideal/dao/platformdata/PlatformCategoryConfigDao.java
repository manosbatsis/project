package com.topideal.dao.platformdata;

import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;


public interface PlatformCategoryConfigDao extends BaseDao<PlatformCategoryConfigModel> {


    /**
     * 分页获取
     * @param dto
     * @return
     */
    PlatformCategoryConfigDTO getListByPage(PlatformCategoryConfigDTO dto);
    /**
     * 获取下拉数据
     * @param dto
     * @return
     */
    List<SelectBean> getSelectBean(PlatformCategoryConfigDTO dto);
}
