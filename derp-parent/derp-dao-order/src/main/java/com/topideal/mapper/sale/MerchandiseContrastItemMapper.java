package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface MerchandiseContrastItemMapper extends BaseMapper<MerchandiseContrastItemModel> {


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
