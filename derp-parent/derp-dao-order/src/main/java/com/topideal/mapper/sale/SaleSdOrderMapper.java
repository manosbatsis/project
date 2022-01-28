package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.vo.sale.SaleSdOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleSdOrderMapper extends BaseMapper<SaleSdOrderModel> {
	
	PageDataModel<SaleSdOrderDTO> listDTOByPage(SaleSdOrderDTO dto)throws SQLException;
	
	List<Map<String,Object>> exportSaleSdOrder(SaleSdOrderDTO dto)throws SQLException;


}
