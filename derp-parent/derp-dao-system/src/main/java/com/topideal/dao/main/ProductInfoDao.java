package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;

/**
 * 产品表 dao
 * @author lianchenxing
 *
 */
public interface ProductInfoDao extends BaseDao<ProductInfoModel>{


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
    MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO model) throws SQLException;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	ProductInfoDTO getDetails(ProductInfoDTO dto)throws SQLException;
	
	/**
	 * 批量修改产品类别
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean updateBatch(List<ProductInfoModel> list) throws SQLException;

	//boolean updateBatch(List<ProductInfoModel> list) throws SQLException;
	
}

