package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SkuPriceWarnDTO;
import com.topideal.entity.vo.reporting.SkuPriceWarnModel;

import java.sql.SQLException;
import java.util.List;


public interface SkuPriceWarnDao extends BaseDao<SkuPriceWarnModel> {

    /**
     * 查询未维护的SKU预警
     */
    List<SkuPriceWarnDTO> listToDeal(SkuPriceWarnDTO dto) throws SQLException;

    /**
     * 统计未维护的SKU预警
     */
    Long countForDeal(SkuPriceWarnDTO dto) throws SQLException;
}
