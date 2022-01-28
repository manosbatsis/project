package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.entity.vo.sale.PlatformMerchandiseInfoModel;
import com.topideal.mapper.BaseMapper;
import io.lettuce.core.protocol.CommandHandler;

import java.util.List;


@MyBatisRepository
public interface PlatformMerchandiseInfoMapper extends BaseMapper<PlatformMerchandiseInfoModel> {

    /**分页查询*/
    PageDataModel<PlatformMerchandiseInfoDTO>  searchDTOByPage(PlatformMerchandiseInfoDTO dto);

    List<PlatformMerchandiseInfoDTO>  listByDto(PlatformMerchandiseInfoDTO dto);

    Integer insertBatch(List<PlatformMerchandiseInfoModel> list);

}
