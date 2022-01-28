package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.SkuPriceWarnDTO;
import com.topideal.entity.vo.reporting.SkuPriceWarnModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface SkuPriceWarnMapper extends BaseMapper<SkuPriceWarnModel> {

    /**
     * 查询未维护的SKU预警
     */
    List<SkuPriceWarnDTO> listToDeal(SkuPriceWarnDTO dto) throws SQLException;

    /**
     * 统计未维护的SKU预警
     */
    Long countForDeal(SkuPriceWarnDTO dto) throws SQLException;

}
