package com.topideal.service.main.impl;

import com.topideal.dao.base.BrandDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantRelDao;
import com.topideal.dao.main.ProductInfoDao;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.main.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 货品
 * @author zhanghx
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

	@Autowired
	private ProductInfoDao productInfoDao;
	@Autowired
	private MerchantRelDao merchantRelDao;
	// 商品分类dao
	@Autowired
	private MerchandiseCatDao merchandiseCatDao;
	@Autowired
	private BrandDao brandDao;
	
    /**
	 * 根据id获取商品详情
	 * @param id
	 * @return
	 */
    @Override
    public ProductInfoDTO searchDetail(Long id)throws SQLException {
    	ProductInfoDTO dto = new ProductInfoDTO() ;
    	dto.setId(id);
        return  productInfoDao.getDetails(dto);
    }
	/**
	 * 分页
	 */
	@Override
	public MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO model) throws SQLException {
		List ids = new ArrayList();
    	//查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(model.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if(list!=null && list.size()>0){
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		ids.add(model.getMerchantId());
		model.setMerchantIds(ids);
		return productInfoDao.getListByPage(model);
	}
	
	/**
	 * 修改品类
	 */
	@Override

	public boolean modifyProduct(ProductInfoModel model)  throws SQLException {
		//获取商品品类 二级
		MerchandiseCatModel merchandiseCatModel = new MerchandiseCatModel();
		merchandiseCatModel.setId(model.getProductTypeId());
		merchandiseCatModel = merchandiseCatDao.searchByModel(merchandiseCatModel);
		model.setProductTypeName(merchandiseCatModel.getName());
		// 获取一级分类
		MerchandiseCatModel merchandiseCatModel0 = new MerchandiseCatModel();
		merchandiseCatModel0.setId(model.getProductTypeId0());
		merchandiseCatModel0 = merchandiseCatDao.searchByModel(merchandiseCatModel0);
		model.setProductTypeName0(merchandiseCatModel0.getName());
		//获取品牌
		BrandModel brandModel = new BrandModel();
		brandModel.setId(model.getBrandId());
		brandModel = brandDao.searchByModel(brandModel);
		model.setBrandName(brandModel.getName());
		int num = productInfoDao.modify(model);
		if (num  >= 1) {
			return true;
		}
			return false;
	}
	
	/**
	 * 批量修改产品类别
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@Override

	public boolean updateBatch(List<Long> list,String productTypeId) throws Exception {
		// 获取二级分类
		MerchandiseCatModel twoCatModel = new MerchandiseCatModel();
		twoCatModel.setId(Long.valueOf(productTypeId));
		twoCatModel = merchandiseCatDao.searchByModel(twoCatModel);
		if (null==twoCatModel) {
			throw new RuntimeException("根据产品类别id没有获取到二级类目信息");
		}
		// 根据二级分类获取一级分类
		
		MerchandiseCatModel oneCatModel = new MerchandiseCatModel();
		oneCatModel.setId(twoCatModel.getParentId());
		oneCatModel = merchandiseCatDao.searchByModel(oneCatModel);
		if (null==oneCatModel) {
			throw new RuntimeException("根据产品类别id没有获取到一级类目信息");
		}
		
		for (Long id : list) {
			//获取商品品类		
			ProductInfoModel productInfoModel= new ProductInfoModel();
			productInfoModel.setId(id);
			productInfoModel.setProductTypeId(twoCatModel.getId());
			productInfoModel.setProductTypeName(twoCatModel.getName());
			productInfoModel.setProductTypeId0(oneCatModel.getId());
			productInfoModel.setProductTypeName0(oneCatModel.getName());
			productInfoDao.modify(productInfoModel);
		}
		return true;
	}

}
