package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.entity.vo.sale.PlatformMerchandiseInfoModel;

import java.util.List;


public interface PlatformMerchandiseInfoDao extends BaseDao<PlatformMerchandiseInfoModel> {


    /**分页查询*/
    public PlatformMerchandiseInfoDTO searchDTOByPage(PlatformMerchandiseInfoDTO dto) throws Exception;

    /**批量插入
     * */
    Integer insertBatch(List<PlatformMerchandiseInfoModel> list);

    public List<PlatformMerchandiseInfoDTO> listByDto(PlatformMerchandiseInfoDTO dto);




}
