package com.topideal.order.service.platformdata;

import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;

import java.util.List;
import java.util.Map;


/**
 * 京东商品管理
 */
public interface PlatformMerchandiseService {

    /**获取分页数据
     * */
    public PlatformMerchandiseInfoDTO getListByPage(PlatformMerchandiseInfoDTO dto) throws Exception;

    public List<PlatformMerchandiseInfoDTO> getList(PlatformMerchandiseInfoDTO dto) throws Exception;
    /**平台商品导入
     * */
    public Map savePlatformMerchandiseImport(List<Map<String, String>> data,Long merchantId) throws Exception;
    /**箱规导入
     * */
    public Map updateCartonSizeImport(List<Map<String, String>> data,Long merchantId) throws Exception;
}
