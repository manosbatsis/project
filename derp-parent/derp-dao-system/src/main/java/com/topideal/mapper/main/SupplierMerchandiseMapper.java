package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.entity.vo.main.SupplierMerchandiseModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SupplierMerchandiseMapper extends BaseMapper<SupplierMerchandiseModel> {


	PageDataModel<SupplierMerchandiseDTO> searchDTOByPage(SupplierMerchandiseDTO dto);
	 /**
     *  导出商品信息
     * @param id
     * @return
     * @throws Exception
     */
    List<SupplierMerchandiseDTO> exportList(SupplierMerchandiseDTO dto) throws SQLException;
}
