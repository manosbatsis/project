package com.topideal.service.syndata.impl;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.*;
import com.topideal.dao.main.*;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.entity.vo.base.*;
import com.topideal.entity.vo.main.*;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.PackTypeMogo;
import com.topideal.service.syndata.SynsDataService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 同步数据
 *
 * @author 杨创
 * 2019/1/11
 */
@Service
public class SynsDataServiceImpl implements SynsDataService {
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;
	@Autowired
	private CountryOriginDao countryOriginDao;// 原产国
	@Autowired
	private CustomerInfoDao  customerInfoDao;// 客户
	@Autowired
	private DepotInfoDao depotInfoDao;// 仓库
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家
	@Autowired
	private ProductInfoDao productInfoDao;// 产品
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品
	@Autowired
	private MerchandiseScheduleDao ScheduleDao;// 商品附表
	@Autowired
	private com.topideal.dao.base.PackTypeDao packTypeDao;// 包装方式
	@Autowired
	private BrandDao brandDao;// 品牌
	@Autowired
	private CustomerMerchantRelDao customerMerchantRelDao ;
	@Autowired
	private EmailConfigDao emailConfigDao;
	@Autowired
	private MerchantShopRelDao merchantShopRelDao;
	@Autowired
	private PurchaseCommissionDao purchaseCommissionDao;
	@Autowired
	private ReptileConfigDao reptileConfigDao;
	@Autowired
	private UnitInfoDao unitInfoDao;
	@Autowired
	private UserBuRelDao userBuRelDao;
	@Autowired
	private MerchantShopShipperDao merchantShopShipperDao;
	@Autowired
	private DepartmentInfoDao departmentInfoDao;//部门管理
	@Autowired
	private BusinessUnitDao businessUnitDao;//事业部管理
	@Autowired
	private ApiExternalConfigDao apiExternalConfigDao;
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;// 原产国
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;// 客户
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家
	@Autowired
	private PackTypeMongoDao packTypeMongoDao;// 包装方式
	@Autowired
	private ProductInfoMongoDao productInfoMongoDao;// 产品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品
	@Autowired
	private MerchandiseScheduleMongoDao scheduleMongoDao;// 商品附表
	@Autowired
	private BrandMongoDao brandMongoDao;// 品牌
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
	@Autowired
	private ApiExternalConfigMongoDao apiExternalConfigMongoDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private BrandParentMongoDao brandParentMongoDao ;
	@Autowired
	private BrandParentDao brandParentDao ;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao ;
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	/*@Autowired
	private InventoryLocationMappingDao inventoryLocationMappingDao ;
	@Autowired
	private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao ;*/
	@Autowired
	private MerchantBuRelDao merchantBuRelDao ;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private EmailConfigMongoDao emailConfigMongoDao;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;
	@Autowired
	private PurchaseCommissionMongoDao purchaseCommissionMongoDao;
	@Autowired
	private ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	private UnitMongoDao unitMongoDao;
	@Autowired
	private DepotMerchantRelDao depotMerchantRelDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private BrandSuperiorDao brandSuperiorDao;
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao;
	@Autowired
	private CustomerSalePriceDao customerSalePriceDao;//销售价格管理
	@Autowired
	private CustomerSalePriceMongoDao customerSalePriceMongoDao;//销售价格管理
	@Autowired
	private SupplierMerchandisePriceDao supplierMerchandisePriceDao;//采购价格管理
	@Autowired
	private SupplierMerchandisePriceMongoDao supplierMerchandisePriceMongoDao;//采购价格管理
	@Autowired
	private DepotCustomsRelDao depotCustomsRelDao;//仓库关区关联
	@Autowired
	private DepotCustomsRelMongoDao depotCustomsRelMongoDao;//仓库关区关联
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao ;
	@Autowired
	private MerchandiseDepotRelDao merchandiseDepotRelDao;// 商家仓库关系表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	@Autowired
	private MerchandiseCustomsRelDao merchandiseCustomsRelDao;//商品关区关系表
	@Autowired
	private MerchandiseCustomsRelMongoDao merchandiseCustomsRelMongoDao;//商品关区关系表
	@Autowired
	private DepartmentInfoMongoDao departmentInfoMongoDao;//部门管理
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;//事业部管理
	@Autowired
	private MerchandiseExternalWarehouseDao merchandiseExternalWarehouseDao;//外仓备案商品表
	@Autowired
	private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao;//外仓备案商品表
	@Autowired
	private TariffRateConfigMongoDao tariffRateConfigMongoDao;//税率
	@Autowired
	private TariffRateConfigDao tariffRateConfigDao;
	@Autowired
	private FileTaskMongoDao fileTaskMongoDao;
	@Autowired
	private UserMerchantRelDao userMerchantRelDao;
	@Autowired
	private UserMerchantRelMongoDao userMerchantRelMongoDao;
	@Autowired
	private ApiSecretConfigMongoDao apiSecretConfigMongoDao;
	@Autowired
	private MerchandiseDepotRelMongoDao merchandiseDepotRelMongoDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private UserInfoMongoDao userInfoMongoDao;
	@Autowired
	private BuStockLocationTypeConfigDao buStockLocationTypeConfigDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	@Autowired
	private FixedCostPriceDao fixedCostPriceDao;
	@Autowired
	private FixedCostPriceMongoDao fixedCostPriceMongoDao;

    @Autowired
    private MongoTemplate mongoTemplate;



	/**
	 *  api密钥配置
	 *
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	@Override
	public void synsApiSecretConfig() throws Exception {
		ApiSecretConfigModel apiSecretConfigModel = new ApiSecretConfigModel();
		List<ApiSecretConfigModel> list = apiSecretConfigDao.list(apiSecretConfigModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("api_secret_config");
		}
		for (ApiSecretConfigModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("apiSecretId",model.getId());
			jsonObject.remove("id");
			apiSecretConfigMongoDao.insertJson(jsonObject.toString());
		}

	}

	/**
	 * 同步原产国
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsCountryOrigin() throws Exception {
		CountryOriginModel countryOriginModel = new CountryOriginModel();
		List<CountryOriginModel> list = countryOriginDao.list(countryOriginModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("country_origin");
		}
		for (CountryOriginModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("countryOriginId",model.getId());
			jsonObject.remove("id");
			countryOriginMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步客户
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsCustomerInfo() throws Exception {
		CustomerInfoModel customerInfoModel = new CustomerInfoModel();
		List<CustomerInfoModel> list = customerInfoDao.list(customerInfoModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("customer_info");
		}

		for (CustomerInfoModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("customerid",model.getId());
			jsonObject.remove("id");
			// 少字段
			customerInfoMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步客户
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synscustomerMerchantRel() throws Exception {
		CustomerMerchantRelModel customerMerchantRelModel = new CustomerMerchantRelModel() ;
		List<CustomerMerchantRelModel> relList = customerMerchantRelDao.list(customerMerchantRelModel) ;
		if (relList.size()>0) {
			mongoTemplate.dropCollection("customer_merchant_rel");
		}
		for (CustomerMerchantRelModel relModel : relList) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(relModel);
			jsonObject.put("customerRelId",relModel.getId());//存关系表id
			jsonObject.remove("id");
			// 少字段
			customerMerchantRelMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步仓库
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsDepotInfo() throws Exception {
		DepotInfoModel depotInfoModel = new DepotInfoModel();
		List<DepotInfoModel> list = depotInfoDao.list(depotInfoModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("depot_info");
		}
		for (DepotInfoModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("depotId",model.getId());
			jsonObject.remove("id");
			depotInfoMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步商品表
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsMerchandiseInfo() throws Exception {
		//清空商品表
		mongoTemplate.dropCollection("merchandise_info");
		MerchandiseInfoModel merchandiseInfoModel = new  MerchandiseInfoModel();
		merchandiseInfoModel.setPageSize(5000);
		merchandiseInfoModel.setBegin(0);
		while(true){
			 MerchandiseInfoModel merchandisePage = merchandiseInfoDao.searchByPage(merchandiseInfoModel);
			 List<MerchandiseInfoModel> list = merchandisePage.getList();

			 if (list.size()==0) break;
			 //查询下一页
			 merchandiseInfoModel.setBegin(merchandiseInfoModel.getBegin()+merchandiseInfoModel.getPageSize());
			 for (MerchandiseInfoModel model : list) {
					//存储到MONGODB
					JSONObject jsonObject=JSONObject.fromObject(model);
					jsonObject.put("merchandiseId",model.getId());
					jsonObject.remove("id");
					merchandiseInfoMogoDao.insertJson(jsonObject.toString());
			 }

		}

	}

	/**
	 * 同步对外接口配置
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synApiExternalConfig() throws Exception {
		ApiExternalConfigModel configModel = new ApiExternalConfigModel();
		List<ApiExternalConfigModel> list = apiExternalConfigDao.list(configModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("api_external_config");
		}
		for (ApiExternalConfigModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("apiExternalId",model.getId());
			jsonObject.remove("id");
			apiExternalConfigMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步商品附表
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsMerchandiseSchedule() throws Exception {
		MerchandiseScheduleModel MerchandiseSchedule = new  MerchandiseScheduleModel();
		List<MerchandiseScheduleModel> list = ScheduleDao.list(MerchandiseSchedule);
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchandise_schedule");
		}
		for (MerchandiseScheduleModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("scheduleId",model.getId());
			jsonObject.remove("id");
			scheduleMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步商家表
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsMerchantInfo() throws Exception {
		MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
		List<MerchantInfoModel> list = merchantInfoDao.list(merchantInfoModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchant_info");
		}
		for (MerchantInfoModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchantId",model.getId());
			jsonObject.remove("id");
			merchantInfoMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步包装方式
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsPackType() throws Exception {
		PackTypeModel packTypeModel= new PackTypeModel();
		List<PackTypeModel> list = packTypeDao.list(packTypeModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("pack_type");
		}
		for (PackTypeModel model : list) {
			PackTypeMogo mongo=new PackTypeMogo();
			mongo.setCode(model.getCode());// 编码
			mongo.setName(model.getName());// 名称
			packTypeMongoDao.insert(mongo);
		}

	}
	/**
	 * 同步产品表
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsproductInfo() throws Exception {

		ProductInfoModel productInfoModel=new ProductInfoModel();
		List<ProductInfoModel> list = productInfoDao.list(productInfoModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("product_info");
		}
		for (ProductInfoModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("productId",model.getId());
			jsonObject.remove("id");
			productInfoMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步品牌
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsBrandInfo() throws Exception {
		BrandModel brandModel= new BrandModel();
		List<BrandModel> list = brandDao.list(brandModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("brand");
		}
		for (BrandModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("brandId",model.getId());
			jsonObject.remove("id");
			brandMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步标准品牌
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsBrandParent() throws Exception {
		BrandParentModel brandModel= new BrandParentModel();
		List<BrandParentModel> list = brandParentDao.list(brandModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("brand_parent");
		}
		for (BrandParentModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("brandParentId",model.getId());
			jsonObject.remove("id");
			brandParentMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步标准条码
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synsCommbarcode() throws Exception {
		CommbarcodeModel commbarcode= new CommbarcodeModel();
		List<CommbarcodeModel> list = commbarcodeDao.list(commbarcode);
		if (list.size()>0) {
			mongoTemplate.dropCollection("commbarcode");
		}
		for (CommbarcodeModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("commbarcodeId",model.getId());
			jsonObject.remove("id");
			commbarcodeMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步汇率
	 * @return
	 * @throws Exception
	 */
	@Override
	public void synExchangeRate() throws Exception {
		ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
		List<ExchangeRateModel> list = exchangeRateDao.list(exchangeRateModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("exchange_rate");
		}
		for (ExchangeRateModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("rateId",model.getId());
			jsonObject.put("effectiveDate", TimeUtils.formatDay(model.getEffectiveDate()));
			jsonObject.remove("id");
			exchangeRateMongoDao.insertJson(jsonObject.toString());
		}
	}

	/**
	 * 同步库位映射
	 */
/*	@Override
	public void synsLocationMapping() throws Exception {
		InventoryLocationMappingModel inventoryLocationMappingModel = new InventoryLocationMappingModel();
		List<InventoryLocationMappingModel> list = inventoryLocationMappingDao.list(inventoryLocationMappingModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("inventory_location_mapping");
		}
		for (InventoryLocationMappingModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("inventoryLocationMappingId",model.getId());
			jsonObject.remove("id");
			inventoryLocationMappingMongoDao.insertJson(jsonObject.toString());
		}
	}*/
	/**
	 * 同步公司事业部
	 */
	@Override
	public void synsMerchantBuRel() throws Exception {
		MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();
		List<MerchantBuRelModel> list = merchantBuRelDao.list(merchantBuRelModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchant_bu_rel");
		}
		for (MerchantBuRelModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchantBuRelId",model.getId());
			jsonObject.remove("id");
			merchantBuRelMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 邮件配置表
	 */
	@Override
	public void synsEmailConfig() throws Exception {
		EmailConfigModel emailConfigModel = new EmailConfigModel();
		List<EmailConfigModel> list = emailConfigDao.list(emailConfigModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("email_config");
		}
		for (EmailConfigModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("emailId",model.getId());
			jsonObject.remove("id");
			emailConfigMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 商家店铺表
	 */
	@Override
	public void synsMerchantShopRel() throws Exception {
		MerchantShopRelModel merchantShopRelModel = new MerchantShopRelModel();
		List<MerchantShopRelModel> list = merchantShopRelDao.list(merchantShopRelModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchant_shop_rel");
		}
		for (MerchantShopRelModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("shopId",model.getId());
			jsonObject.remove("id");
			merchantShopRelMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 加价比例配置表
	 */
	@Override
	public void synsPurchaseCommission() throws Exception {
		PurchaseCommissionModel purchaseCommissionModel = new PurchaseCommissionModel();
		List<PurchaseCommissionModel> list = purchaseCommissionDao.list(purchaseCommissionModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("purchase_commission");
		}
		for (PurchaseCommissionModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("purchaseCommissionId",model.getId());
			jsonObject.remove("id");
			purchaseCommissionMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 爬虫配置表
	 */
	@Override
	public void synsReptileConfig() throws Exception {
		ReptileConfigModel reptileConfigModel = new ReptileConfigModel();
		List<ReptileConfigModel> list = reptileConfigDao.list(reptileConfigModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("reptile_config");
		}
		for (ReptileConfigModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("reptileId",model.getId());
			jsonObject.remove("id");
			reptileConfigMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 单位表
	 */
	@Override
	public void synsUnit() throws Exception {
		UnitInfoModel unitInfoModel = new UnitInfoModel();
		List<UnitInfoModel> list = unitInfoDao.list(unitInfoModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("unit_info");
		}
		for (UnitInfoModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("unitId",model.getId());
			jsonObject.remove("id");
			unitMongoDao.insertJson(jsonObject.toString());
		}
	}
	@Override
	public void synsdepotMerchantRel() throws Exception {
		DepotMerchantRelModel depotMerchantRelModel = new DepotMerchantRelModel();
		List<DepotMerchantRelModel> list = depotMerchantRelDao.list(depotMerchantRelModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("depot_merchant_rel");
		}
		for (DepotMerchantRelModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			//jsonObject.put("depotMerchantRelId",model.getId());// 是根据 商家+仓库查询的 不用 自增长id
			jsonObject.remove("id");
			depotMerchantRelMongoDao.insertJson(jsonObject.toString());
		}

	}
	@Override
	public void synsBrandSuperior() throws Exception {
		BrandSuperiorModel brandSuperiorModel =  new BrandSuperiorModel();
		List<BrandSuperiorModel> list = brandSuperiorDao.list(brandSuperiorModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("brand_superior");
		}
		for(BrandSuperiorModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("brandSuperiorId",model.getId());
			jsonObject.remove("id");
			brandSuperiorMongoDao.insertJson(jsonObject.toString());
		}

	}
	@Override
	public void synsCustomerSalePrice() throws Exception {
		CustomerSalePriceModel customerSalePriceModel =  new CustomerSalePriceModel();
		List<CustomerSalePriceModel> list = customerSalePriceDao.list(customerSalePriceModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("customer_sale_price");
		}
		for(CustomerSalePriceModel model : list) {
			//存储到MONGODB
			 JSONObject jsonObject = JSONObject.fromObject(model);
	         jsonObject.put("customerSalePriceId", model.getId());
	         jsonObject.remove("id");
	         jsonObject.remove("expiryDate");
	         jsonObject.remove("effectiveDate");
	         jsonObject.remove("auditDate");
	         if (model.getExpiryDate()!=null) jsonObject.put("expiryDate", TimeUtils.format(model.getExpiryDate(), "yyyy-MM-dd"));
	         if (model.getEffectiveDate()!=null)jsonObject.put("effectiveDate", TimeUtils.format(model.getEffectiveDate(), "yyyy-MM-dd"));
	         if (model.getAuditDate()!=null)jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
	         customerSalePriceMongoDao.insertJson(jsonObject.toString());
		}

	}
	//采购价格管理
	@Override
	public void synsSupplierMerchandisePrice() throws Exception {

		SupplierMerchandisePriceModel SupplierMerchandisePriceModel =  new SupplierMerchandisePriceModel();
		List<SupplierMerchandisePriceModel> list = supplierMerchandisePriceDao.list(SupplierMerchandisePriceModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("supplier_merchandise_price");
		}
		for(SupplierMerchandisePriceModel model : list) {
			//存储到MONGODB
			 JSONObject jsonObject = JSONObject.fromObject(model);
	         jsonObject.put("supplierMerchandisePriceId", model.getId());
	         jsonObject.remove("id");
	         jsonObject.remove("expiryDate");
	         jsonObject.remove("effectiveDate");
	         jsonObject.remove("auditDate");
	         jsonObject.put("expiryDate", TimeUtils.format(model.getExpiryDate(), "yyyy-MM-dd"));
	         jsonObject.put("effectiveDate", TimeUtils.format(model.getEffectiveDate(), "yyyy-MM-dd"));
	         if(model.getAuditDate() != null){
	         	jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
			 }
	         supplierMerchandisePriceMongoDao.insertJson(jsonObject.toString());
		}

	}
	@Override
	public void synsDepotCustomsRel() throws Exception {
		DepotCustomsRelModel DepotCustomsRelModel =  new DepotCustomsRelModel();
		List<DepotCustomsRelModel> list = depotCustomsRelDao.list(DepotCustomsRelModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("depot_customs_rel");
		}
		for(DepotCustomsRelModel model : list) {
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("depotCustomsRelId",model.getId());
			jsonObject.remove("id");
			depotCustomsRelMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void synsMerchantDepotBuRel() throws Exception {

		MerchantDepotBuRelModel merchantDepotBuRelModel = new MerchantDepotBuRelModel() ;

		List<MerchantDepotBuRelModel> list = merchantDepotBuRelDao.list(merchantDepotBuRelModel);
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchant_depot_bu_rel");
		}

		for (MerchantDepotBuRelModel temp : list) {

			JSONObject jsonObject=JSONObject.fromObject(temp);
			jsonObject.put("merchantDepotBuRelId", temp.getId());
			jsonObject.remove("id");
			merchantDepotBuRelMongoDao.insertJson(jsonObject.toString());

		}
	}


	/**
	 * 同步用户事业部
	 */
	@Override
	public void synsUserBuRel() throws Exception {
		List<UserBuRelModel> list = userBuRelDao.list(new UserBuRelModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("user_bu_rel");
		}
		for (UserBuRelModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("userBuRelId", model.getId());
			jsonObject.remove("id");
			userBuRelMongoDao.insertJson(jsonObject.toString());
		}

	}

	/**
	 * 用户公司关联
	 */
	@Override
	public void synsUserMerchantRel() throws Exception{
		List<UserMerchantRelModel> list = userMerchantRelDao.list(new UserMerchantRelModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("user_merchant_rel");
		}
		for (UserMerchantRelModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("userMerchantRelId", model.getId());
			jsonObject.remove("id");
			userMerchantRelMongoDao.insertJson(jsonObject.toString());
		}
	}


	/**
	 * 同步商家店铺事业部表体
	 */
	@Override
	public void synsMerchantShopShipper() throws Exception {
		List<MerchantShopShipperModel> list = merchantShopShipperDao.list(new MerchantShopShipperModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchant_shop_shipper");
		}
		for (MerchantShopShipperModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("shipperId", model.getId());
			jsonObject.remove("id");
			merchantShopShipperMongoDao.insertJson(jsonObject.toString());
		}
	}
	/**
	 * 同步商品关区关系表
	 */
	@Override
	public void synsMerchandiseCustomsRel() throws Exception {
		List<MerchandiseCustomsRelModel> list = merchandiseCustomsRelDao.list(new MerchandiseCustomsRelModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchandise_customs_rel");
		}
		for (MerchandiseCustomsRelModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchandiseCustomsRelId", model.getId());
			jsonObject.remove("id");
			merchandiseCustomsRelMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void synsDepartmentInfo() throws Exception {
		List<DepartmentInfoModel> list = departmentInfoDao.list(new DepartmentInfoModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("department_info");
		}
		for (DepartmentInfoModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("departmentInfoId", model.getId());
			jsonObject.remove("id");
			departmentInfoMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void synsBusinessUnit() throws Exception {
		List<BusinessUnitModel> list = businessUnitDao.list(new BusinessUnitModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("business_unit");
		}
		for (BusinessUnitModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("businessUnitId", model.getId());
			jsonObject.remove("id");
			businessUnitMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void synsMerchandiseExternalWarehouse() throws Exception {
		List<MerchandiseExternalWarehouseModel> list = merchandiseExternalWarehouseDao.list(new MerchandiseExternalWarehouseModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchandise_external_warehouse");
		}
		for (MerchandiseExternalWarehouseModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchandiseExternalWarehouseId", model.getId());
			jsonObject.remove("id");
			merchandiseExternalWarehouseMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void sysTariffRateConfig() throws Exception {
		List<TariffRateConfigModel> list = tariffRateConfigDao.list(new TariffRateConfigModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("tariff_rate_config");
		}
		for (TariffRateConfigModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("tariffRateConfigId", model.getId());
			jsonObject.remove("id");
			tariffRateConfigMongoDao.insertJson(jsonObject.toString());
		}
	}

	// 商品仓库关系表
	@Override
	public void sysMerchandiseDepotRel() throws Exception {
		List<MerchandiseDepotRelModel> list = merchandiseDepotRelDao.list(new MerchandiseDepotRelModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("merchandise_depot_rel");
		}
		for (MerchandiseDepotRelModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchandiseDepotRelId", model.getId());
			jsonObject.remove("id");
			merchandiseDepotRelMongoDao.insertJson(jsonObject.toString());
		}

	}

	@Override
	public void sysUserInfo()throws Exception {
		List<UserInfoModel> list = userInfoDao.list(new UserInfoModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("user_info");
		}
		for (UserInfoModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("userId", model.getId());
			jsonObject.remove("id");
			userInfoMongoDao.insertJson(jsonObject.toString());
		}
	}

	@Override
	public void sysBuStockLocationTypeConfig()throws Exception {
		List<BuStockLocationTypeConfigModel> list = buStockLocationTypeConfigDao.list(new BuStockLocationTypeConfigModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("bu_stock_location_type_config");
		}
		for (BuStockLocationTypeConfigModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("buStockLocationTypeId", model.getId());
			jsonObject.remove("id");
			buStockLocationTypeConfigMongoDao.insertJson(jsonObject.toString());
		}
	}
	@Override
	public void sysFixedCostPrice()throws Exception {
		List<FixedCostPriceModel> list = fixedCostPriceDao.list(new FixedCostPriceModel());
		if (list.size()>0) {
			mongoTemplate.dropCollection("fixed_cost_price");
		}
		for (FixedCostPriceModel model : list) {
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("fixedCostPriceId", model.getId());
			jsonObject.remove("id");
			if(model.getAuditDate() != null){
				jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
			}
			fixedCostPriceMongoDao.insertJson(jsonObject.toString());
		}
	}

	/*
	 * @Override public void synsgoodsMongdb() throws Exception {
	 *
	 * Map<String, Object>map=new HashMap<String, Object>();
	 *
	 * List<MerchandiseInfoMogo> findAll = merchandiseInfoMogoDao.findAll(map);
	 * List<Long>idlist=new ArrayList<Long>(); for (MerchandiseInfoMogo
	 * merchandiseInfoMogo : findAll) { if
	 * (idlist.contains(merchandiseInfoMogo.getMerchandiseId())) { continue; }
	 * idlist.add(merchandiseInfoMogo.getMerchandiseId()); MerchandiseInfoModel
	 * model=new MerchandiseInfoModel();
	 * model.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
	 * model.setCode(merchandiseInfoMogo.getCode());
	 * model.setProductId(merchandiseInfoMogo.getProductId());
	 * model.setModifier(merchandiseInfoMogo.getModifier());
	 * model.setRemark(merchandiseInfoMogo.getRemark());
	 * model.setSource(merchandiseInfoMogo.getSource());
	 * model.setMinStock(merchandiseInfoMogo.getMinStock());
	 * model.setUniques(merchandiseInfoMogo.getUnique());
	 * model.setName(merchandiseInfoMogo.getName());
	 * model.setMaxStock(merchandiseInfoMogo.getMaxStock());
	 * model.setId(merchandiseInfoMogo.getMerchandiseId());
	 * model.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
	 * model.setDescribe(merchandiseInfoMogo.getDescribe());
	 * model.setFilingPrice(merchandiseInfoMogo.getFilingPrice());
	 * model.setFactoryNo(merchandiseInfoMogo.getFactoryNo());
	 * model.setPackType(merchandiseInfoMogo.getPackType());
	 * model.setWarningType(merchandiseInfoMogo.getWarningType());
	 * model.setCreater(merchandiseInfoMogo.getCreater());
	 * model.setMerchantName(merchandiseInfoMogo.getMerchantName());
	 * model.setMerchantId(merchandiseInfoMogo.getMerchantId());
	 * model.setIsRecord(merchandiseInfoMogo.getIsRecord());
	 * model.setIsGroup(merchandiseInfoMogo.getIsGroup());
	 * model.setUpdateName(merchandiseInfoMogo.getUpdateName());
	 * model.setCreateName(merchandiseInfoMogo.getCreateName());
	 * model.setIsSelf(merchandiseInfoMogo.getIsSelf() );
	 * model.setBarcode(merchandiseInfoMogo.getBarcode());
	 * model.setStatus(merchandiseInfoMogo.getStatus());
	 * model.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
	 * model.setBoxToUnit(merchandiseInfoMogo.getBoxToUnit());
	 * model.setTorrToUnit(merchandiseInfoMogo.getTorrToUnit());
	 * model.setEnglishGoodsName(merchandiseInfoMogo.getEnglishGoodsName());
	 * model.setBrandId(merchandiseInfoMogo.getBrandId());
	 * model.setCountyId(merchandiseInfoMogo.getCountyId());
	 * model.setUnit(merchandiseInfoMogo.getUnit());
	 * model.setProductTypeId(merchandiseInfoMogo.getProductId());
	 *
	 * model.setProductTypeId0(merchandiseInfoMogo.getProductTypeId0());
	 * model.setProductTypeName(merchandiseInfoMogo.getProductTypeName());
	 * model.setProductTypeName0(merchandiseInfoMogo.getProductTypeName0());
	 * model.setShelfLifeDays(merchandiseInfoMogo.getShelfLifeDays());
	 * model.setSpec(merchandiseInfoMogo.getSpec());
	 * model.setGrossWeight(merchandiseInfoMogo.getGrossWeight());
	 * model.setNetWeight(merchandiseInfoMogo.getNetWeight());
	 * model.setHsCode(merchandiseInfoMogo.getHsCode());
	 * model.setLength(merchandiseInfoMogo.getLength() );
	 * model.setWidth(merchandiseInfoMogo.getWidth());
	 * model.setHeight(merchandiseInfoMogo.getHeight());
	 * model.setVolume(merchandiseInfoMogo.getVolume());
	 * model.setProductImg01(merchandiseInfoMogo.getProductImg01());
	 * model.setManufacturer(merchandiseInfoMogo.getManufacturer());
	 * model.setManufacturerAddr(merchandiseInfoMogo.getManufacturerAddr());
	 * model.setColor(merchandiseInfoMogo.getColor());
	 * model.setSize(merchandiseInfoMogo.getSize());
	 * model.setComponent(merchandiseInfoMogo.getComponent());
	 *
	 *
	 * model.setOutDepotFlag(merchandiseInfoMogo.getOutDepotFlag());
	 * model.setTenantCode(merchandiseInfoMogo.getTenantCode());
	 * model.setVersion(merchandiseInfoMogo.getVersion());
	 * model.setSourceGoodsId(merchandiseInfoMogo.getSourceGoodsId());
	 * model.setCustomsAreaId(merchandiseInfoMogo.getCustomsAreaId());
	 * model.setDeclareFactor(merchandiseInfoMogo.getDeclareFactor());
	 * model.setCustomsGoodsRecordNo(merchandiseInfoMogo.getCustomsGoodsRecordNo());
	 *
	 * model.setUnitNameOne(merchandiseInfoMogo.getUnitNameOne());
	 * model.setUnitNameTwo(merchandiseInfoMogo.getUnitNameTwo());
	 * model.setImageUrl1(merchandiseInfoMogo.getImageUrl1());
	 * model.setImageUrl2(merchandiseInfoMogo.getImageUrl2());
	 * model.setImageUrl3(merchandiseInfoMogo.getImageUrl3());
	 * model.setImageUrl4(merchandiseInfoMogo.getImageUrl4());
	 * model.setImageUrl5(merchandiseInfoMogo.getImageUrl5());
	 *
	 *
	 * merchandiseInfoDao.save(model);
	 *
	 *
	 * }
	 *
	 * }
	 */

	/*
	 * @Override public void synsFileTask() throws Exception { List<FileTaskModel>
	 * list = fileTaskDao.list(new FileTaskModel()); for (FileTaskModel model :
	 * list) { JSONObject jsonObject = JSONObject.fromObject(model);
	 * jsonObject.put("code", model.getId()); jsonObject.put("module", "1"); if
	 * (model.getDepotId() == null) { jsonObject.put("depotId", ""); }
	 * jsonObject.put("createDate",
	 * TimeUtils.formatFullTime(model.getCreateDate()));
	 * jsonObject.put("modifyDate",
	 * TimeUtils.formatFullTime(model.getModifyDate())); jsonObject.remove("id");
	 * fileTaskMongoDao.insertJson(jsonObject.toString()); } }
	 */




}
