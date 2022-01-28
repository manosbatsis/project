package com.topideal.dao.platformdata;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;


public interface OrealPurchaseOrderDao extends BaseDao<OrealPurchaseOrderModel>{
		
    /**分页查询*/
    public OrealPurchaseOrderDTO getListByPage(OrealPurchaseOrderDTO dto) throws Exception;
    /**
     * 导出
     * @param dto
     * @return
     */
    public List<Map<String, Object>> getExportList(OrealPurchaseOrderDTO dto);

    /**
     * 详情
     * @param model
     * @return
     * @throws Exception
     */
    public OrealPurchaseOrderDTO searchDTODetail(OrealPurchaseOrderDTO model) throws Exception;




}
