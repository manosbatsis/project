package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;


public interface MerchandiseDepotRelDao extends BaseDao<MerchandiseDepotRelModel> {
		

	/**
	 * 根据商品id批量删除商品仓库关系表
	 * @param goodIdList
	 * @return
	 * @throws SQLException
	 */
	public int deleteByGoodsId(List<Long> goodIdList)throws SQLException;





}
