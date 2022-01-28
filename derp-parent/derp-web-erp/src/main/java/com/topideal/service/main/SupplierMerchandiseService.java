package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.main.SupplierMerchandiseDTO;

public interface SupplierMerchandiseService {

	/**
	 * 查询列表信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	SupplierMerchandiseDTO listSupplierMerchandise(SupplierMerchandiseDTO dto) throws SQLException;

	 /**
     *  导出商品信息
     * @param id
     * @return
     * @throws Exception
     */
    List<SupplierMerchandiseDTO> exportList(SupplierMerchandiseDTO dto) throws SQLException;
}
