package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MerchandiseDepotRelMapper extends BaseMapper<MerchandiseDepotRelModel> {

	/**
	 * 根据商品id批量删除商品仓库关系表
	 * @param goodIdList
	 * @return
	 * @throws SQLException
	 */
	public int deleteByGoodsId(List<Long> list)throws SQLException;

}
