package com.topideal.mapper.platformdata;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface OrealPurchaseOrderMapper extends BaseMapper<OrealPurchaseOrderModel> {
	
	/**
	 * 分页
	 * @param dto
	 * @return
	 */
    PageDataModel<OrealPurchaseOrderDTO>  getListByPage(OrealPurchaseOrderDTO dto);
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
