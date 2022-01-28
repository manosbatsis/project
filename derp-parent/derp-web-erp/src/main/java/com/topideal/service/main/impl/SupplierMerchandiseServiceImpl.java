package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.main.SupplierMerchandiseDao;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.service.main.SupplierMerchandiseService;

@Service
public class SupplierMerchandiseServiceImpl implements SupplierMerchandiseService{

	@Autowired
	private SupplierMerchandiseDao supplierMerchandiseDao ;
	/**	
	 * 分页
	 */
	@Override
	public SupplierMerchandiseDTO listSupplierMerchandise(SupplierMerchandiseDTO dto) throws SQLException {
		return supplierMerchandiseDao.searchDTOByPage(dto);
	}
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SupplierMerchandiseDTO> exportList(SupplierMerchandiseDTO dto) throws SQLException {
		return supplierMerchandiseDao.exportList(dto);
	}
}
