package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.vo.sale.CutomerQuotaWarningModel;

import java.util.List;


public interface CutomerQuotaWarningDao extends BaseDao<CutomerQuotaWarningModel> {

    /**
     * 获取分页信息
     * @param dto
     * @return
     */
    CutomerQuotaWarningDTO listDTOByPage(CutomerQuotaWarningDTO dto) throws Exception;

    /**
     *  获取列表
     * @param dto
     * @return
     */
    List<CutomerQuotaWarningDTO> listDTO(CutomerQuotaWarningDTO dto)  throws Exception;


}
