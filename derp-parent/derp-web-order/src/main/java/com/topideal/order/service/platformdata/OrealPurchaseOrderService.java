package com.topideal.order.service.platformdata;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;


/**
 * 欧莱雅订单管理
 */
public interface OrealPurchaseOrderService {

    /**获取分页数据
     * */
    public OrealPurchaseOrderDTO getListByPage(OrealPurchaseOrderDTO dto) throws Exception;
    
    /**
     * 导出
     * @param dto
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getExportList(OrealPurchaseOrderDTO dto) throws Exception;
    
    /**
     * 查询详情
     * @param dto
     * @return
     * @throws Exception
     */
    public OrealPurchaseOrderDTO searchDTODetail(OrealPurchaseOrderDTO model) throws Exception;
    /**
     * 查询表体
     * @param dto
     * @return
     * @throws Exception
     */
    public List<OrealPurchaseOrderItemModel> getOrderItrmList(OrealPurchaseOrderItemModel model) throws Exception;
    

}
