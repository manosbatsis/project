package com.topideal.service.main;


import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 供应商询价池service实现类
 */
public interface SupplierInquiryPoolService {
	/**
	 * 供应商询价池列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SupplierInquiryPoolDTO listSIPool(SupplierInquiryPoolDTO dto)throws SQLException;
	/**
	 * 删除供应商询价池
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	boolean delSIPool(List ids)throws SQLException;
	/**
	 * 导入供应商询价池
	 * @param data
	 * @param user
	 * @return
	 */
	Map importSIPool(Map<Integer, List<List<Object>>> data, Long merchantId, Long userId);
	/**
	 * 获取供应商询价池详情
	 * @param id
	 * @return
	 */
	SupplierInquiryPoolDTO searchDetail(Long id)throws SQLException;

	/**
	 * 修改供应商询价池
	 * @param model
	 * @return
	 */
	boolean modifySIPool(SupplierInquiryPoolModel model, Long userId)throws SQLException;
	
	
    /**
     *  导出供应商询价池
     * @param id
     * @return
     * @throws Exception
     */
    List<SupplierInquiryPoolModel> exportList(SupplierInquiryPoolModel model) throws SQLException;

}
