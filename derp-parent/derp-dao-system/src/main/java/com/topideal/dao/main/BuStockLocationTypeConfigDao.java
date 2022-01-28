package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;


public interface BuStockLocationTypeConfigDao extends BaseDao<BuStockLocationTypeConfigModel> {


    BuStockLocationTypeConfigDTO listDTOByPage(BuStockLocationTypeConfigDTO dto);
}
