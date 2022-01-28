package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.vo.sale.CutomerQuotaWarningModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface CutomerQuotaWarningMapper extends BaseMapper<CutomerQuotaWarningModel> {

    /**
     * 获取分页信息
     * @param dto
     * @return
     */
    PageDataModel<CutomerQuotaWarningDTO> listDTOByPage(CutomerQuotaWarningDTO dto);

    /**
     *  获取列表
     * @param dto
     * @return
     */
    List<CutomerQuotaWarningDTO> listDTO(CutomerQuotaWarningDTO dto) ;

}
