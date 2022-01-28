package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.main.CommbarcodeItemModel;

public interface CommbarcodeItemService {

	/**
	 * 查询符合条件列表
	 * @param commbarcodeItemModel
	 * @return
	 * @throws SQLException 
	 */
	List<CommbarcodeItemModel> list(CommbarcodeItemModel commbarcodeItemModel) throws SQLException;

}
