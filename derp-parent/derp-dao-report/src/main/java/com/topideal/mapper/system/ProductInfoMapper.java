package com.topideal.mapper.system;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.ProductInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 产品表 Mapper
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface ProductInfoMapper extends BaseMapper<ProductInfoModel> {

	/**
	 * 分页
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<MerchandiseInfoModel> getListByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 获取详情
	 */
	ProductInfoModel getDetails(ProductInfoModel model) throws SQLException;
}

