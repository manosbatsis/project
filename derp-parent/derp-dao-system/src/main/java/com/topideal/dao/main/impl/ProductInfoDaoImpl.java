package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.ProductInfoDao;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.mapper.main.ProductInfoMapper;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.ProductInfoMongoDao;

import net.sf.json.JSONObject;

/**
 * 产品表 impl
 * 
 * @author lianchenxing
 */
@Repository
public class ProductInfoDaoImpl implements ProductInfoDao {

	@Autowired
	private ProductInfoMapper mapper;
	@Autowired
	private ProductInfoMongoDao productInfoMongoDao;// 产品MongoDB

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
	 * @param model
	 */
	@Override
	public Long save(ProductInfoModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			ProductInfoModel product = new ProductInfoModel();
	    	product.setId(model.getId());
	    	product = mapper.get(product);

			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(product);
			jsonObject.put("productId",model.getId());

			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("countyName");
			jsonObject.remove("brandName");
			jsonObject.remove("merchandiesId");	
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");
			productInfoMongoDao.insertJson(jsonObject.toString());						       			
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(ProductInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());

		int num = mapper.update(model);

		if(num > 0){
			//修改货品信息存储到MONGODB
			ProductInfoModel product = new ProductInfoModel();
			product.setId(model.getId());
			product = mapper.get(product);

			//修改mongodb 货品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("productId",product.getId());

			JSONObject jsonObject=JSONObject.fromObject(product);
			jsonObject.put("productId",model.getId());
			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("countyName");
			jsonObject.remove("brandName");
			jsonObject.remove("merchandiesId");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");
			productInfoMongoDao.update(params,jsonObject);
    	   	
		}
		return num;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public ProductInfoModel searchByPage(ProductInfoModel model) throws SQLException {
		PageDataModel<ProductInfoModel> pageModel = mapper.listByPage(model);
		return (ProductInfoModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
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
	 * @param model
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

			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(productInfoModel);
			jsonObject.put("productId",productInfoModel.getId());

			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("countyName");
			jsonObject.remove("brandName");
			jsonObject.remove("merchandiesId");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");
			productInfoMongoDao.insertJson(jsonObject.toString());

		} else {
			productInfoModel.setId(pModel.getId());
			mapper.update(productInfoModel);

			//修改货品信息存储到MONGODB
			ProductInfoModel product = new ProductInfoModel();
			product.setId(pModel.getId());
			product = mapper.get(product);

			//修改mongodb 货品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("productId",product.getId());

			JSONObject jsonObject=JSONObject.fromObject(product);
			jsonObject.put("productId", pModel.getId());
			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("countyName");
			jsonObject.remove("brandName");
			jsonObject.remove("merchandiesId");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");

			productInfoMongoDao.update(params,jsonObject);
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
	public MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO model) throws SQLException {
		PageDataModel<MerchandiseInfoDTO> pageModel = mapper.getListByPage(model);
		return (MerchandiseInfoDTO) pageModel.getModel();
	}
	
	/**
	 * 货品详情
	 */
	@Override
	public ProductInfoDTO getDetails(ProductInfoDTO dto) throws SQLException {
		return mapper.getDetails(dto);
	}

	/**
	 * 批量修改产品类别
	 */
	@Override
	public boolean updateBatch(List<ProductInfoModel> list) throws SQLException {
		return mapper.updateBatch(list);
	}

//	@Override
//	public boolean updateBatch(ProductInfoModel model) throws SQLException {
//		// TODO Auto-generated method stub
//		return false;
//	 }
}
