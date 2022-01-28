package com.topideal.storage.service.takesstock.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.TakesStockItemDao;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.storage.service.takesstock.TakesStockItemService;

@Service
public class TakesStockItemServiceImpl implements TakesStockItemService{
	
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TakesStockItemServiceImpl.class);
      
	@Autowired
	private TakesStockItemDao takesStockItemDao;
	
	/**
	 * 查询列表
	 * 
	 * @param model
	 * @return
	 */
	public List<TakesStockItemModel> list(TakesStockItemModel model) throws SQLException {
		
		return takesStockItemDao.list(model);
	}
	
}
