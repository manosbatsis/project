package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;

import java.util.List;


public interface MerchandiseContrastItemDao extends BaseDao<MerchandiseContrastItemModel> {


    /**
     * 根据表头的id=表体的contrast_id删除表体中的数据
     * @param id
     * @return
     */
    Long deleteByContrastId(Long id);

    /**
     * 根据contrastId获取表体对象
     * @param contrastItemModel
     * @return
     */
    List<MerchandiseContrastItemDTO> getContrastItemByContrastId(MerchandiseContrastItemModel contrastItemModel);
}
