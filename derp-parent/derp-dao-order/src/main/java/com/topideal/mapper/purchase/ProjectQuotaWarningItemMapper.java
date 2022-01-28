package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface ProjectQuotaWarningItemMapper extends BaseMapper<ProjectQuotaWarningItemModel> {


    PageDataModel<ProjectQuotaWarningItemDTO> getItemListByPage(ProjectQuotaWarningItemDTO dto);

    List<ProjectQuotaWarningItemDTO> exportProjectQuotaWarningDetail(ProjectQuotaWarningItemDTO dto);
}
