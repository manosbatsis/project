package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.entity.vo.main.SupplierMerchandiseModel;


public interface SupplierMerchandiseDao extends BaseDao<SupplierMerchandiseModel> {
	/**	
	 * 分页
	 * @param dto
	 * @return
	 */
	SupplierMerchandiseDTO searchDTOByPage(SupplierMerchandiseDTO dto);
	

	 /**
     *  导出商品信息
     * @param id
     * @return
     * @throws Exception
     */
    List<SupplierMerchandiseDTO> exportList(SupplierMerchandiseDTO dto) throws SQLException;






}
