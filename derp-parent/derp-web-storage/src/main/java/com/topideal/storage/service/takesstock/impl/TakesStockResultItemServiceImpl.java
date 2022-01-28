package com.topideal.storage.service.takesstock.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.storage.service.takesstock.TakesStockResultItemService;

@Service
public class TakesStockResultItemServiceImpl implements TakesStockResultItemService{
	
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TakesStockResultItemServiceImpl.class);
      
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;
	
	/**
	 * 查询列表
	 * 
	 * @param dto
	 * @return
	 */
	public List<TakesStockResultItemDTO> list(TakesStockResultItemDTO dto) throws SQLException {
		return takesStockResultItemDao.listDto(dto);
	}
	
}
