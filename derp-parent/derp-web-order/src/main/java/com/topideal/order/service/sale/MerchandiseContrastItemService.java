package com.topideal.order.service.sale;


import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;

import java.sql.SQLException;
import java.util.List;

public interface MerchandiseContrastItemService {

    /**
     * 根据爬虫表头id查询表体的集合
     * @param id
     * @return
     */
    List<MerchandiseContrastItemModel> searchDetail(MerchandiseContrastItemModel contrastItemModel) throws SQLException;

    /**
     * 根据contrastId获取表体对象
     * @param contrastItemModel
     * @return
     */
    List<MerchandiseContrastItemDTO> getContrastItemByContrastId(MerchandiseContrastItemModel contrastItemModel);
}
