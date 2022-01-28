package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.vo.bill.PlatformStatementItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface PlatformStatementItemDao extends BaseDao<PlatformStatementItemModel>{

	 Integer deleteByOrderId(Long platformStatementIds) ;

	PlatformStatementItemDTO getListByPage(PlatformStatementItemDTO dto);


	 List<Map<String, Object>> listInvoiceItem(List<Long> ids, String source) throws SQLException;

	 List<Map<String, Object>> listInvoiceItemByType(List<Long> ids) throws SQLException;

	 /**
	  * 根据平台结算单id 统计表体数量
	  **/
	 Map<String, Object> statisticsByPlatformIdList(Long billId) throws SQLException;



}
