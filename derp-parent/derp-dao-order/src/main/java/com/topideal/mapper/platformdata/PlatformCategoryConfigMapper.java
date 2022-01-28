package com.topideal.mapper.platformdata;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PlatformCategoryConfigMapper extends BaseMapper<PlatformCategoryConfigModel> {


    PageDataModel<PlatformCategoryConfigDTO> getListByPage(PlatformCategoryConfigDTO dto);
    
    List<SelectBean> getSelectBean(PlatformCategoryConfigDTO dto);
}
