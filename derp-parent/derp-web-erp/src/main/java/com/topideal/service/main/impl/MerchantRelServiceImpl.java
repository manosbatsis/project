package com.topideal.service.main.impl;

import com.topideal.dao.main.MerchantRelDao;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.service.main.MerchantRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 子商家
 * @author zhanghx
 */
@Service
public class MerchantRelServiceImpl implements MerchantRelService {

	@Autowired
	private MerchantRelDao merchantRelDao;
	
	/**
	 * 获取集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MerchantRelModel> list(MerchantRelModel model) throws SQLException {
		return merchantRelDao.getMerchantById(model.getMerchantId());
	}

	/**
	 *  获取当前商家的所有代理商家
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MerchantRelModel> getRelMerchantIds(MerchantRelModel model) throws SQLException {
		return merchantRelDao.list(model);
	}

}
