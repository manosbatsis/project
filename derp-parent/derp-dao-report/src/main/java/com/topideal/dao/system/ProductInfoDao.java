package com.topideal.dao.system;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.ProductInfoModel;

/**
 * 产品表 dao
 * @author lianchenxing
 *
 */
public interface ProductInfoDao extends BaseDao<ProductInfoModel> {


    /**
     * 插入产品
     * 1、条形码不存在，新增
     * 2、条形码存在，修改
     * @param productInfoModel
     * @return
     * @throws SQLException
     */
    public boolean insertProductInfo(ProductInfoModel productInfoModel)throws SQLException;
		
    /**
	 * 分页
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoModel getListByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ProductInfoModel getDetails(ProductInfoModel model)throws SQLException;
}

