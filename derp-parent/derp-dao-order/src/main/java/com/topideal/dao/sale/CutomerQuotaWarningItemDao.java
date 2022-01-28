package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.CutomerQuotaWarningItemDTO;
import com.topideal.entity.vo.sale.CutomerQuotaWarningItemModel;


public interface CutomerQuotaWarningItemDao extends BaseDao<CutomerQuotaWarningItemModel> {

    /**
     * 获取分页信息
     * @param dto
     * @return
     */
    CutomerQuotaWarningItemDTO listDTOByPage(CutomerQuotaWarningItemDTO dto);
    /**
     * 根据表头id删除表体数据
     * @param ids
     * @return
     * @throws SQLException
     */
    int deleteByWarningId(Long warningId)throws SQLException;
}
