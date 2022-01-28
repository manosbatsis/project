package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.ProductInfoDao;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.ProductInfoModel;
import com.topideal.mapper.system.ProductInfoMapper;

/**
 * 产品表 impl
 * 
 * @author lianchenxing
 */
@Repository
public class ProductInfoDaoImpl implements ProductInfoDao {

	@Autowired
	private ProductInfoMapper mapper;
	
	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<ProductInfoModel> list(ProductInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param ProductInfoModel
	 */
	@Override
	public Long save(ProductInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		model.setCreateDate(TimeUtils.getNow());
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(ProductInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param ProductInfoModel
	 */
	@Override
	public ProductInfoModel searchByPage(ProductInfoModel model) throws SQLException {
		PageDataModel<ProductInfoModel> pageModel = mapper.listByPage(model);
		return (ProductInfoModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public ProductInfoModel searchById(Long id) throws SQLException {
		ProductInfoModel model = new ProductInfoModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public ProductInfoModel searchByModel(ProductInfoModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public boolean insertProductInfo(ProductInfoModel productInfoModel) throws SQLException {
		// 产品条形码
		ProductInfoModel pModel = new ProductInfoModel();
		// 产品条形码
		pModel.setBarcode(productInfoModel.getBarcode());
		pModel = mapper.get(pModel);
		// 找不到，就新增
		if (pModel == null) {
			mapper.insert(productInfoModel);
		} else {
			productInfoModel.setId(pModel.getId());
			mapper.update(productInfoModel);
		}
		return true;
	}

	/**
	 * 分页
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Override
	public MerchandiseInfoModel getListByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel = mapper.getListByPage(model);
		return (MerchandiseInfoModel) pageModel.getModel();
	}
	
	/**
	 * 货品详情
	 */
	@Override
	public ProductInfoModel getDetails(ProductInfoModel model) throws SQLException {
		return mapper.getDetails(model);
	}
}
