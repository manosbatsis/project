package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.service.main.CommbarcodeItemService;

@Service
public class CommbarcodeItemServiceImpl implements CommbarcodeItemService{

	@Autowired
	private CommbarcodeItemDao commbarcodeItemDao ;
	/**
	 * 查询符合条件列表
	 * @param commbarcodeItemModel
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<CommbarcodeItemModel> list(CommbarcodeItemModel commbarcodeItemModel) throws SQLException {
		return commbarcodeItemDao.list(commbarcodeItemModel);
	}

}
