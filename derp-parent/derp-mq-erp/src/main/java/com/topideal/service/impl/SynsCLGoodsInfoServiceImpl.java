package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.taobao.api.response.WlbWmsSkuGetResponse;
import com.topideal.api.finance.FinanceUtils;
import com.topideal.api.finance.f01.GoodsClassificationItemRequest;
import com.topideal.api.finance.f01.GoodsClassificationRequest;
import com.topideal.api.pms.PMSUtils;
import com.topideal.api.pms.p_001.PMSGoodQueryRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.APILog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseDepotRelDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantShopRelDao;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.service.SynsCLGoodsInfoService;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SynsCLGoodsInfoServiceImpl implements SynsCLGoodsInfoService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SynsCLGoodsInfoServiceImpl.class);
	
	@Autowired
	private MerchantShopRelDao merchantShopRelDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	@Autowired
    private MerchandiseCatDao merchandiseCatDao;// 产品分类
    @Autowired
    private CommbarcodeDao commbarcodeDao ;
    @Autowired
    private CommbarcodeItemDao commbarcodeItemDao ;
	@Autowired
	private MerchandiseDepotRelDao merchandiseDepotRelDao;// 商品仓库关系表
    @Autowired
	private JSONMongoDao jsonMongoDao;
	@Autowired
	private CountryOriginDao countryOriginDao;
	@Autowired
	private RMQLogProducer rocketLogMQProducer;
    @Autowired
	private RMQProducer rocketMQProducer;//MQ

	@Override
	public Map<String, Object> synsCLGoodsInfo(WlbWmsSkuGetResponse itemRsp, MerchantShopRelModel merchantShopRelModel,MerchantInfoModel merchantInfoModel,Long sourceGoodsId) throws Exception {
		
		
		
		SyncGoodsRequest request = new SyncGoodsRequest() ;
		request.setSyncType(1);//同步类型 枚举备注: 1:新增；2:更新
		request.setSubscriberCode("JFX");
		request.setTenantCode("zhuozhi");
		request.setIsUpdateProduct(0);
		request.setMerchantCode(merchantInfoModel.getTopidealCode());
		
		MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
		
		
		String barCode = itemRsp.getBarCode();
		if(StringUtils.isBlank(barCode)) {
			//throw new RuntimeException("菜鸟商品ID "+ itemRsp.getItemId() +" 条形码为空") ;
			return null ;
		}
		
		/**
		 * 若appkey为（28332830或30079979）且bar_code=iitem_code也认为bar_code为空
		 */
		Map<String, String> params = itemRsp.getParams(); 
		if( params != null &&
				( "28332830".equals(merchantShopRelModel.getAppKey()) ||
						"30079979".equals(merchantShopRelModel.getAppKey())) && 
				itemRsp.getBarCode().equals(itemRsp.getIitemCode())) {
			//throw new RuntimeException("菜鸟商品ID "+ itemRsp.getItemId() +"bar_code与iitem_code相等 默认条形码为空") ;
			return null ;
		}
		
		String[] barCodeArr = barCode.split(";");
		for(int i = 0 ; i < barCodeArr.length; i++ ) {
			barCode = barCodeArr[i] ;
			
			if(barCodeArr.length == 1 || 
					barCode.length() == 12 || 
					barCode.length() == 13) {
				merchandiseInfoModel.setBarcode(barCode);
				break ;
			}
			
		}
		request.setBarCode(barCode);		
        /**
         * 保质期
         */
        if(itemRsp.getLifecycle() != null) {
        	merchandiseInfoModel.setShelfLifeDays(itemRsp.getLifecycle().intValue());
        	request.setQualityDays(itemRsp.getLifecycle().intValue());
        }
        
        /**
         * 毛重
         */
        if(itemRsp.getGrossWeight() != null) {
        	BigDecimal grossWeightBD =new BigDecimal(itemRsp.getGrossWeight()) ;
        	merchandiseInfoModel.setGrossWeight(grossWeightBD.divide(new BigDecimal(1000)).setScale( 3, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        	request.setGrossWeight(itemRsp.getGrossWeight().doubleValue()) ;
        }
        
        /**
         * 净重
         */
        if(itemRsp.getNetWeight() != null) {
        	BigDecimal netWeightBD =new BigDecimal(itemRsp.getNetWeight()) ;
        	merchandiseInfoModel.setNetWeight(netWeightBD.divide(new BigDecimal(1000)).setScale( 3, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        	request.setNetWeight(itemRsp.getNetWeight().doubleValue()) ;
        }
        
        /**
         * 长度
         */
        if(itemRsp.getLength() != null) {
        	BigDecimal lengthBD =new BigDecimal(itemRsp.getLength()) ;
        	merchandiseInfoModel.setLength(lengthBD.divide(new BigDecimal(10)).setScale( 3, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        	request.setLength(itemRsp.getLength().doubleValue()) ;
        }
        
        /**
         * 宽度
         */
        if(itemRsp.getWidth() != null) {
        	BigDecimal widthBD =new BigDecimal(itemRsp.getWidth()) ;
        	merchandiseInfoModel.setWidth(widthBD.divide(new BigDecimal(10)).setScale( 3, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        	request.setWidth(itemRsp.getWidth().doubleValue()) ;
        }
        
        /**
         * 高度
         */
        if(itemRsp.getHeight() != null) {
        	BigDecimal heightBD =new BigDecimal(itemRsp.getHeight()) ;
        	merchandiseInfoModel.setHeight(heightBD.divide(new BigDecimal(10)).setScale( 3, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        	request.setHeight(itemRsp.getHeight().doubleValue()) ;
        }
		
		/**
		 * 单价
		 */
		if(itemRsp.getItemPrice() != null) {
			BigDecimal itemBD =new BigDecimal(itemRsp.getItemPrice()) ;
			merchandiseInfoModel.setFilingPrice(itemBD.divide(new BigDecimal(100)).setScale( 4, BigDecimal.ROUND_HALF_DOWN));
			merchandiseInfoModel.setFilingPrice(itemBD.divide(new BigDecimal(100)).setScale( 4, BigDecimal.ROUND_HALF_DOWN));
			request.setPrice(new BigDecimal(itemRsp.getItemPrice())) ;
		}
		
		/***************************请求通用商品库开始*****************************************/
		GoodsClassificationRequest GoodsRequest= new GoodsClassificationRequest();
		GoodsRequest.setRequestTime(TimeUtils.formatFullTime());
		List<GoodsClassificationItemRequest>goodsList=new ArrayList<GoodsClassificationItemRequest>();
		GoodsClassificationItemRequest goodsItemRequest =new GoodsClassificationItemRequest();
		goodsItemRequest.setGoodsBarcode(merchandiseInfoModel.getBarcode());
		goodsList.add(goodsItemRequest);
		GoodsRequest.setGoodsList(goodsList);
		
		// 请求金融 商品分类查询接口 获取标准条码和分类
		String result = FinanceUtils.goodsClassificationRequest(JSONObject.fromObject(GoodsRequest));
		JSONObject classJson = JSONObject.fromObject(result);
		String status = classJson.getString("status");
		
		// 如果标准条码为空报错
		if (!"1".equals(status)) { 
			throw new RuntimeException("调用通用商品库的商品分类查询接口失败");
		}
		JSONArray goodsjsonArray = classJson.getJSONArray("goodsCategory");
		if (goodsjsonArray==null||goodsjsonArray.size()!=1) {
			throw new RuntimeException("调用通用商品库的商品分类查询标准条码commbarcode为空");
		}
		JSONObject goodsObject = (JSONObject) goodsjsonArray.get(0);
		if (goodsObject.get("commonBarcode")==null||StringUtils.isBlank(goodsObject.getString("commonBarcode"))) {
			 throw new RuntimeException("调用通用商品库的商品分类查询标准条码commbarcode为空");
		}
		
		String commonBarcode = goodsObject.getString("commonBarcode");// 标准条码
		
		String firstCategory = "" ;
		MerchandiseCatModel merchandiseCat = null ;
		if (goodsObject.get("firstCategory")!=null&&StringUtils.isNotBlank(goodsObject.getString("firstCategory"))) {
			firstCategory = goodsObject.getString("firstCategory");// 一级分类名称
			// 通过一级级分类名称查询商品分类表
        	merchandiseCat= new MerchandiseCatModel();
        	merchandiseCat.setName(firstCategory);
        	merchandiseCat = merchandiseCatDao.searchByModel(merchandiseCat);
        				        	
		}
		
		String secondCategory = "" ;
		MerchandiseCatModel merchandiseCatSecond = null ;
		if (goodsObject.get("secondCategory")!=null&&StringUtils.isNotBlank(goodsObject.getString("secondCategory"))) {
			secondCategory = goodsObject.getString("secondCategory");// 二级分类名称
			// 通过二级分类名称查询商品分类表
			merchandiseCatSecond= new MerchandiseCatModel();
			merchandiseCatSecond.setName(secondCategory);
			merchandiseCatSecond = merchandiseCatDao.searchByModel(merchandiseCatSecond);
        				        	
		}
		
		/***************************请求通用商品库结束*****************************************/
		
		/**
		 * 货品设置一级品类
		 */
		if(merchandiseCat != null) {
			merchandiseInfoModel.setProductTypeId0(merchandiseCat.getId());
			merchandiseInfoModel.setProductTypeName0(merchandiseCat.getName());
			
			request.setClass1(merchandiseCat.getSysCode());
		}
		
		/**
		 * 货品设置二级品类
		 */
		if(merchandiseCatSecond != null) {
			merchandiseInfoModel.setProductTypeId(merchandiseCatSecond.getId());
			merchandiseInfoModel.setProductTypeName(merchandiseCatSecond.getName());
			request.setClass2(merchandiseCatSecond.getSysCode());
		}
		
		//货品、商品设置标准条码		
		merchandiseInfoModel.setCommbarcode(commonBarcode);
		merchandiseInfoModel.setMerchantId(merchantInfoModel.getId());
		merchandiseInfoModel.setMerchantName(merchantInfoModel.getName());
		merchandiseInfoModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));
		merchandiseInfoModel.setGoodsCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));
		String goodsNo = itemRsp.getItemId();
		goodsNo = goodsNo.trim();
		merchandiseInfoModel.setGoodsNo(goodsNo);
		merchandiseInfoModel.setName(itemRsp.getTitle());
		merchandiseInfoModel.setIsGroup(DERP_SYS.CUSTOMERINFO_ISGROUP_0);
		merchandiseInfoModel.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
		merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		merchandiseInfoModel.setCreateName("系统自动生成");
		merchandiseInfoModel.setSource(DERP_SYS.MERCHANDISEINFO_SOURCE_2);
		merchandiseInfoModel.setCreateDate(TimeUtils.getNow());
		
		/**
		 * 是否备案：APPKEY为（28332830或30079979或 30084355）值为是，APPKEY为（30083371）值为否
		 */
		if("30084355".equals(merchantShopRelModel.getAppKey())||
				"28332830".equals(merchantShopRelModel.getAppKey())
				|| "30079979".equals(merchantShopRelModel.getAppKey())) {
			merchandiseInfoModel.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
		}else if("30083371".equals(merchantShopRelModel.getAppKey())) {
			merchandiseInfoModel.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_0);
		}
		
		//保存商品、货品信息		
		merchandiseInfoModel.setSourceGoodsId(sourceGoodsId);
		// 获取pms 商品净重和毛重
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(merchantInfoModel.getTopidealCode());
		productCodes.add(merchandiseInfoModel.getGoodsNo());
		// 获取pms 商品信息
		Map<String, Map<String, Object>> pmsGoodsMap=new  HashMap<String, Map<String,Object>>();
		pmsGoodsMap = getPmsGoods(productCodes, cCodes);					
		// 获取pms商品 毛重 净重
		Map<String, Object> map = pmsGoodsMap.get(merchandiseInfoModel.getGoodsNo());
		if (map!=null) {
			Double net = (Double) map.get("net");
			Double kgs = (Double) map.get("kgs");
			Long countryId = (Long) map.get("countryId");
			merchandiseInfoModel.setNetWeight(net);
			merchandiseInfoModel.setGrossWeight(kgs);
			merchandiseInfoModel.setCountyId(countryId);
		}

		Long goodsId = merchandiseInfoDao.save(merchandiseInfoModel);
		// 新增商品仓库关系数据
		if (merchantShopRelModel.getDepotId()!=null) {
			MerchandiseDepotRelModel merchandiseDepotRel=new MerchandiseDepotRelModel();
			merchandiseDepotRel.setDepotId(merchantShopRelModel.getDepotId());
			merchandiseDepotRel.setDepotName(merchantShopRelModel.getDepotName());
			merchandiseDepotRel.setMerchantId(merchandiseInfoModel.getMerchantId());
			merchandiseDepotRel.setGoodsId(goodsId);
			merchandiseDepotRelDao.save(merchandiseDepotRel);
		}
		
		merchandiseInfoModel.setId(goodsId);
		JSONObject json=new JSONObject();
		json.put("merchantId", merchandiseInfoModel.getMerchantId());
		json.put("goodsId", merchandiseInfoModel.getId());
		json.put("tag", "2");//"2" 页面/其他触发单个货号 "1" 定时器 
		//  推送复制商品到卓普信/宝丰/嘉宝	
		SendResult send = rocketMQProducer.send(json.toString(), MQErpEnum.COPY_MERCHANDISE.getTopic(), MQErpEnum.COPY_MERCHANDISE.getTags());		
		if (send==null||!send.getSendStatus().name().equals("SEND_OK")) {
			throw new RuntimeException("推送商品复制数据失败");
		}
		/**
		 * 检查标准条码在ERP系统是否存在，若不存在则在ERP新增标准条码
		 */
		saveCommBarbarcode(merchandiseInfoModel, merchandiseCatSecond);
		
		request.setProductName(merchandiseInfoModel.getName());
		request.setGoodsName(merchandiseInfoModel.getName());
		request.setGoodsCode(merchandiseInfoModel.getGoodsNo());
		request.setGoodsBusiCode(merchandiseInfoModel.getGoodsCode());
		request.setProductCode(merchandiseInfoModel.getCommbarcode());
		
		// 返回 数据
		Map<String, Object> requesMap= new HashMap<String, Object>();
		requesMap.put("syncGoodsRequest", request);
		requesMap.put("sourceGoodsId", goodsId);				
		return requesMap;
	}
	/**
	 * 调用PMS的商品查询接口 获取毛重 净重 账册号
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, Map<String, Object>> getPmsGoods(List<String> productCodes,List<String> cCodes) throws SQLException {
		PMSGoodQueryRequest pmsRequest=new PMSGoodQueryRequest();
		pmsRequest.setProductCodes(productCodes);// 商品货号
		pmsRequest.setcCodes(cCodes);// 商家卓志编码
		pmsRequest.setQueryType("0");//枚举: 0,1枚举备注: 0:商品信息,1:+海关账册商品信息
		pmsRequest.setSource("JFX");
		pmsRequest.setRowsPerPage(300);// 每页3白条
		pmsRequest.setPageNo(0);
		pmsRequest.setCuiCode("zhuozhi");		
		JSONObject fromObject = JSONObject.fromObject(pmsRequest);

		String goodsStr=null;
		JSONObject reslutJson=null;
		Integer totalPageRows=0;
		try {
			goodsStr = PMSUtils.getGoods(fromObject);
			reslutJson = JSONObject.fromObject(goodsStr);
			totalPageRows =Integer.valueOf(reslutJson.getString("totalPageRows"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 商品封装
		Map<String, Map<String, Object>> pmsGoodsMap=new HashMap<>();
		for (int i = 0; i <totalPageRows; i++) {
			pmsRequest.setPageNo(i);
			goodsStr=null;
			reslutJson=null;
			JSONArray jsonArray=null;
			try {
				goodsStr = PMSUtils.getGoods(fromObject);
				if (StringUtils.isBlank(goodsStr)) continue;
				reslutJson = JSONObject.fromObject(goodsStr);						
				if (goodsStr!=null) jsonArray= reslutJson.getJSONArray("goodsInfo");
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			if (jsonArray!=null) {
				for (Object object : jsonArray) {
					JSONObject jSONObject=(JSONObject) object;
					if (jSONObject.get("productCode")==null||StringUtils.isBlank(jSONObject.getString("productCode")))continue;
					String productCode = jSONObject.getString("productCode");//货号
					productCode=productCode.trim();
					if (pmsGoodsMap.containsKey(productCode))continue;
					Map<String, Object>map=new  HashMap<>();
					Double net=0.0;
					if (jSONObject.get("net")!=null&&StringUtils.isNotBlank(jSONObject.getString("net"))) {
						net=Double.valueOf(jSONObject.getString("net"));
					}
					Double kgs =0.0;
					if (jSONObject.get("kgs")!=null&&StringUtils.isNotBlank(jSONObject.getString("kgs"))) {
						kgs=Double.valueOf(jSONObject.getString("kgs"));
					}		

					CountryOriginModel countryOrigin=new CountryOriginModel();
					if (jSONObject.get("countryName")!=null&&StringUtils.isNotBlank(jSONObject.getString("countryName"))) {
						countryOrigin.setName(jSONObject.getString("countryName"));
						countryOrigin = countryOriginDao.searchByModel(countryOrigin);
					}
					if (countryOrigin==null)countryOrigin=new CountryOriginModel();
					String accountCode =null;
					if (jSONObject.get("accountCode")!=null&&StringUtils.isNotBlank(jSONObject.getString("accountCode"))) {
						accountCode=jSONObject.getString("accountCode");
					}	
					
					map.put("net", net);
					map.put("kgs", kgs);
					map.put("countryId", countryOrigin.getId());
					map.put("accountCode", accountCode);					
					pmsGoodsMap.put(productCode, map);
				}

			}
		}

		
		return pmsGoodsMap;
	}

	@Override
	public List<MerchantShopRelModel> getSycnMerchantShop() throws SQLException {
		
		MerchantShopRelModel model = new MerchantShopRelModel();
		model.setIsSycnMerchandise(DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1);
		model.setStatus(DERP_SYS.MERCHANTSHOPREL_STATUS_1);
		
		return merchantShopRelDao.list(model) ;
		
	}


	@Override
	public boolean checkMerchandiseExsit(Long merchantId, String goodsNo) throws SQLException {
		
		MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
		merchandiseInfoModel.setMerchantId(merchantId);
		merchandiseInfoModel.setGoodsNo(goodsNo);
		
		merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel) ;
		
		if(merchandiseInfoModel != null) {
			return true ;
		}
		
		return false;
	}
	
	/**
     * 根据数据生成标准条码记录
     * @param merchandiseInfoModel
     * @param brandModel
     * @param merchandiseCatModel
     * @throws SQLException
     */
    private void saveCommBarbarcode(MerchandiseInfoModel merchandiseInfoModel , MerchandiseCatModel merchandiseCatModel) throws SQLException {
    	
    	if(merchandiseCatModel == null) {
    		merchandiseCatModel = new MerchandiseCatModel() ;
    	}
    	
    	//判断标准条码是否存在，若不存在，在标准条码管理新增一条未维护数据
    	CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
    	commbarcodeModel.setCommbarcode(merchandiseInfoModel.getCommbarcode());
    	commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
    	
    	Long commbarcodeId = null ;
    	
    	//不存在新增一条记录
    	if(commbarcodeModel == null ) {
    		commbarcodeModel = new CommbarcodeModel() ;
    		commbarcodeModel.setCommbarcode(merchandiseInfoModel.getCommbarcode());
    		commbarcodeModel.setCommGoodsName(merchandiseInfoModel.getName());
    		commbarcodeModel.setCommTypeId(merchandiseCatModel.getId());
    		commbarcodeModel.setCommTypeName(merchandiseCatModel.getName());
    		commbarcodeModel.setMaintainStatus("0");
    		commbarcodeModel.setCreateDate(TimeUtils.getNow());
    		commbarcodeModel.setModifyDate(TimeUtils.getNow());
    		
    		commbarcodeId = commbarcodeDao.save(commbarcodeModel);
    		//新增表体
    		if(commbarcodeId != null) {
    			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
    			commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());
    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}
    	}
    	//存在修改表体内容
    	else {
    		commbarcodeId = commbarcodeModel.getId() ;
    		//根据标准条码ID、商品货号查表体是否存在
    		CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    		commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    		commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    		
    		commbarcodeItemModel = commbarcodeItemDao.searchByModel(commbarcodeItemModel) ;
    		
    		if(commbarcodeItemModel == null) {
    			commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());
    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}else {
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());
    			commbarcodeItemDao.modify(commbarcodeItemModel) ;
    		}
    	}
    }


	/**
	 * 发送错误日志
	 * @param e 
	 * @param itemRsp 
	 */
    @Override
	public void synsCLGoodsCollectionError(String keryword, String errorMsg, JSONObject requestMessage) throws Exception {
		LOGGER.info("================同步菜鸟商品  抛出异常===============");
        //API日志实体
        APILog apiLog=new APILog();
        //设置状态为失败
        apiLog.setState(0);
        //失败原因
        apiLog.setExpMsg(errorMsg);
		apiLog.setModel("同步菜鸟商品");
		apiLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_12106000001));
		apiLog.setKeyword(keryword);
		apiLog.setKeywordName("goodsNo");
		apiLog.setRequestMethod("com.topideal.service.maindata.SynsCLGoodsInfoService.synsCLGoodsInfo(WlbWmsSkuGetResponse, MerchantInfoModel)");
		apiLog.setReceiveData(System.currentTimeMillis());
		apiLog.setDerpCode("000");
		
		//API日志报文
		JSONObject jsonObject = JSONObject.fromObject(apiLog);
		
        //设置请求报文
        jsonObject.put("requestMessage", requestMessage);
        //响应报文
        jsonObject.put("responseMessage", errorMsg);
        jsonObject.put("endDate", System.currentTimeMillis());
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
        jsonObject.put("modulCode", "1005");

		SendResult sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
		LOGGER.info("==报文："+jsonObject.toString()+"==");
		LOGGER.info("==响应报文："+sendResult+"==");
		if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
			jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
			LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
		}
        //推送到日志MQ平台
        /**String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
        if (resultMsg==null) {
        	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
		}else{
			JSONObject resultObject=JSONObject.fromObject(resultMsg);
			if (resultObject.get("state")==null||!"1".equals(String.valueOf(resultObject.get("state")))) {
				jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());	
			}				
		}
        LOGGER.info("==报文："+jsonObject.toString()+"==");
        LOGGER.info("==响应报文："+resultMsg+"==");*/
        LOGGER.info("================同步菜鸟商品  抛出异常结束===============");
	}


    /**
     * 发送成功日志
     */
	@Override
	public void synsCLGoodsCollectionSucc(WlbWmsSkuGetResponse itemRsp) throws Exception {
		LOGGER.info("================同步菜鸟商品  成功日志===============");
        //API日志实体
        APILog apiLog=new APILog();
        //设置状态为失败
        apiLog.setState(1);
		apiLog.setModel("同步菜鸟商品");
		apiLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_12106000001));
		apiLog.setKeyword(itemRsp.getItemId());
		apiLog.setKeywordName("goodsNo");
		apiLog.setRequestMethod("com.topideal.service.maindata.SynsCLGoodsInfoService.synsCLGoodsInfo(WlbWmsSkuGetResponse, MerchantInfoModel)");
		apiLog.setReceiveData(System.currentTimeMillis());
		apiLog.setDerpCode("000");
		
		//API日志报文
		JSONObject jsonObject = JSONObject.fromObject(apiLog);
		
        //设置请求报文
        jsonObject.put("requestMessage", JSONObject.fromObject(itemRsp));
        //响应报文
        jsonObject.put("responseMessage", "成功");
        jsonObject.put("endDate", System.currentTimeMillis());
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
        jsonObject.put("modulCode", "1005");

		SendResult sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
		LOGGER.info("==报文："+jsonObject.toString()+"==");
		LOGGER.info("==响应报文："+sendResult+"==");
		if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
			jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
			LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
		}
        //推送到日志MQ平台
        /**String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
        if (resultMsg==null) {
        	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
		}else{
			JSONObject resultObject=JSONObject.fromObject(resultMsg);
			if (resultObject.get("state")==null||!"1".equals(String.valueOf(resultObject.get("state")))) {
				jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());	
			}				
		}
        LOGGER.info("==报文："+jsonObject.toString()+"==");
        LOGGER.info("==响应报文："+resultMsg+"==");*/
        LOGGER.info("================同步菜鸟商品  成功日志结束===============");
	}


}
