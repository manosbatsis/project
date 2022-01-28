package com.topideal.controller.dev;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.tools.TimeUtils;
import com.topideal.service.syndata.SynsDataService;

import net.sf.json.JSONObject;

/**
 * 同步原产国数据
 * @author 杨创
 *2018/1/11
 */
@Controller
@RequestMapping("/derpapi/1003")
public class SynDataContoller {


	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SynDataContoller.class);
	@Autowired
	private SynsDataService synsDataService;

	@RequestMapping(params="method=erp1003",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject getSysData (@RequestBody String json){

		LOGGER.info("mongdb数据同步开始"+json);
		JSONObject jsonData=new JSONObject();
		JSONObject returnJsonData=new JSONObject();

		String currentTime = TimeUtils.formatDay();


		int num=0;
		try {
			jsonData = JSONObject.fromObject(json);
			if (jsonData.get("userName")==null||StringUtils.isBlank(jsonData.getString("userName"))) {
				returnJsonData.put("userName", "userName IS NULL");
				return returnJsonData;
			}
			if (jsonData.get("passWord")==null||StringUtils.isBlank(jsonData.getString("passWord"))) {
				returnJsonData.put("passWord", "passWord IS NULL");
				return returnJsonData;
			}
			if (jsonData.get("tableName")==null||StringUtils.isBlank(jsonData.getString("tableName"))) {
				returnJsonData.put("tableName", "tableName IS NULL");
				return returnJsonData;
			}

			if (!"admin".equals(jsonData.getString("userName"))) {
				returnJsonData.put("userName", "用户名错误");
				return returnJsonData;
			}
			// 当前时间
			if (!currentTime.equals(jsonData.getString("passWord"))) {
				returnJsonData.put("passWord", "密码错误");
				return returnJsonData;
			}


			//原产国同步
			if ("st_api_secret_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsApiSecretConfig();
				num=num+1;
			}
			//原产国同步
			if ("st_country_origin".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsCountryOrigin();
				num=num+1;
			}
			// 客户同步
			if("st_customer_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsCustomerInfo();
				num=num+1;
			}
			// 商家客户关系同步
			if("st_customer_merchant_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synscustomerMerchantRel();
				num=num+1;
			}
			// 仓库同步
			if("st_depot_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsDepotInfo();
				num=num+1;
			}
			// 商品同步
			if("st_merchandise_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchandiseInfo();
				num=num+1;
			}
			// 商家同步
			if("st_merchant_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchantInfo();
				num=num+1;
			}
			// 包装方式同步
			if("st_pack_type".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsPackType();
				num=num+1;
			}
			// 产品表同步
			if("st_product_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsproductInfo();
				num=num+1;
			}
			// 品牌同步
			if("st_brand".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsBrandInfo();
				num=num+1;
			}
			// 商品附表同步
			if("st_merchandise_schedule".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchandiseSchedule();
				num=num+1;
			}
			// 对接接口同步
			if("st_api_external_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synApiExternalConfig();
				num=num+1;
			}
			// 汇率同步
			if("st_exchange_rate".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synExchangeRate();
				num=num+1;
			}
			// 标准品牌同步
			if("st_brand_parent".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsBrandParent();
				num=num+1;
			}
			// 标准条码同步
			if("st_commbarcode".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsCommbarcode();
				num=num+1;
			}
			// 库位映射表同步
			/*if("st_inventory_location_mapping".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsLocationMapping();
				num=num+1;
			}*/
			// 商家事业部关系表同步
			if("st_merchant_bu_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchantBuRel();
				num=num+1;
			}
			// 邮件配置同步
			if("st_email_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsEmailConfig();
				num=num+1;
			}
			// 商品店铺关系同步
			if("st_merchant_shop_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchantShopRel();
				num=num+1;
			}
			//加价比例配置表同步
			if("st_purchase_commission".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsPurchaseCommission();
				num=num+1;
			}

			//爬虫配置表同步
			if("st_reptile_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsReptileConfig();
				num=num+1;
			}
			// 单位同步
			if("st_unit_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsUnit();
				num=num+1;
			}
			// 仓库商家关系表同步
			if("st_depot_merchant_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsdepotMerchantRel();
				num=num+1;
			}
			// 母品牌同步
			if("st_brand_superior".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsBrandSuperior();
				num=num+1;
			}
			// 销售价格管理同步
			if("st_customer_sale_price".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsCustomerSalePrice();
				num=num+1;
			}
			//采购价格管理同步
			if("st_supplier_merchandise_price".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsSupplierMerchandisePrice();
				num=num+1;
			}
			//仓库关区关系表 同步
			if("st_depot_customs_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsDepotCustomsRel();
				num=num+1;
			}
			// 商家仓库 事业部关系表同步
			if("st_merchant_depot_bu_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchantDepotBuRel();
				num=num+1;
			}
			// 用户事业部关系同步
			if("st_user_bu_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsUserBuRel();
				num=num+1;
			}
			// 用户商家关系同步
			if("st_user_merchant_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsUserMerchantRel();
				num=num+1;
			}
			// 店铺货主表
			if("st_merchant_shop_shipper".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchantShopShipper();
				num=num+1;
			}
			// 商品关区关系表同步
			if("st_merchandise_customs_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchandiseCustomsRel();
				num=num+1;
			}
			// 部门同步
			if("st_department_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsDepartmentInfo();
				num=num+1;
			}
			// 事业部同步
			if("st_business_unit".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsBusinessUnit();
				num=num+1;
			}
			//外仓备案商品列表 同步
			if("st_merchandise_external_warehouse".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.synsMerchandiseExternalWarehouse();
				num=num+1;
			}
			//税率配置表 同步
			if ("st_tariff_rate_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.sysTariffRateConfig();
				num=num+1;
			}

			//商品仓库关系表 同步
			if ("st_merchandise_depot_rel".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.sysMerchandiseDepotRel();
				num=num+1;
			}

			//用户信息表 同步
			if ("st_user_info".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.sysUserInfo();
				num=num+1;
			}

			//事业部库位类型配置表 同步
			if ("st_bu_stock_location_type_config".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.sysBuStockLocationTypeConfig();
				num=num+1;
			}

			//固定成本价盘维护 同步
			if ("st_fixed_cost_price".equals(jsonData.getString("tableName"))||"ALL".equals(jsonData.getString("tableName"))) {
				synsDataService.sysFixedCostPrice();
				num=num+1;
			}

			// 没有找到同步的表
			if (num==0) {
				returnJsonData.put("tableName", "没有找到对应同步数据的表,表名" + jsonData.getString("tableName"));
				return returnJsonData;
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnJsonData.put("desc", "同步数据出现异常");
			return returnJsonData;
		}

		returnJsonData.put("returnJsonData", "同步"+jsonData.getString("tableName")+"表数据完成,同步表:"+num);

		return returnJsonData;
	}

}
