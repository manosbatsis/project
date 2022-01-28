package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.dto.sale.CutomerQuotaWarningItemDTO;

import java.util.List;

public interface CutomerQuotaWarningService {
    /**
     * 获取列表分页
     * @param dto
     * @return
     * @throws Exception
     */
    public CutomerQuotaWarningDTO listDTOByPage(CutomerQuotaWarningDTO dto,User user) throws Exception;

    /**
     * 获取体列表分页
     * @param dto
     * @return
     * @throws Exception
     */
    public CutomerQuotaWarningItemDTO getItemListByPage(CutomerQuotaWarningItemDTO dto,User user) throws Exception;

    /**
     * 根据条件获取列表 不分页
     * @param dto
     * @return
     * @throws Exception
     */
    public List<CutomerQuotaWarningDTO> listCutomerQuotaWarning(CutomerQuotaWarningDTO dto, User user) throws Exception;
}
