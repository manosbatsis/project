package com.topideal.mapper.sale;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.CutomerQuotaWarningItemDTO;
import com.topideal.entity.vo.sale.CutomerQuotaWarningItemModel;
import com.topideal.mapper.BaseMapper;



@MyBatisRepository
public interface CutomerQuotaWarningItemMapper extends BaseMapper<CutomerQuotaWarningItemModel> {
    /**
     * 获取分页信息
     * @param dto
     * @return
     */
    PageDataModel<CutomerQuotaWarningItemDTO> listDTOByPage(CutomerQuotaWarningItemDTO dto);
    /**
     * 根据表头id删除表体数据
     * @param warningId
     * @return
     * @throws SQLException
     */
    int deleteByWarningId(@Param("waringId")Long waringId)throws SQLException;
}
