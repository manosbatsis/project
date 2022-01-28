package com.topideal.service.maindata.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.finance.FinanceUtils;
import com.topideal.api.finance.f01.GoodsClassificationItemRequest;
import com.topideal.api.finance.f01.GoodsClassificationRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchandiseScheduleDao;
import com.topideal.dao.main.MerchantBrandRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchandiseScheduleModel;
import com.topideal.entity.vo.main.MerchantBrandRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.maindata.SynsGoodsInfoService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/5/23.
 */
@Service
public class SynsGoodsInfoServiceImpl implements SynsGoodsInfoService {
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SynsGoodsInfoServiceImpl.class);

    @Autowired
    private MerchantInfoDao merchantInfoDao;// 商家表
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;//商品
    @Autowired
    private UnitInfoDao unitInfoDao;//单位表
    @Autowired
    private BrandDao brandDao;// 品牌
    @Autowired
    private CountryOriginDao countryOriginDao;//原产国
    @Autowired
    private MerchantBrandRelDao merchantBrandRelDao;//商家和品牌关联表
    @Autowired
    private MerchandiseScheduleDao merchandiseScheduleDao;
    @Autowired
    private MerchandiseCatDao merchandiseCatDao;// 产品分类
    @Autowired
	private RMQProducer rocketMQProducer;//MQ

    @Autowired
    private CommbarcodeDao commbarcodeDao ;
    @Autowired
    private CommbarcodeItemDao commbarcodeItemDao ;
    
    

    /**
     * 新增/或修改商家商品
     */
	@Override
	public boolean synsMerchantGoods(String json) throws Exception {
		  //json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        // 根据卓志编码查询商家
        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setTopidealCode(jsonData.getString("ccode"));
        merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
        if(merchantInfoModel==null){
        	LOGGER.error("商家卓志编码不存在");
            throw new RuntimeException("商家卓志编码不存在");
        }
        
        // 查询商家id和商品货号查询商品
        MerchandiseInfoModel merchandiseInfoModel =new MerchandiseInfoModel();
        merchandiseInfoModel.setMerchantId(merchantInfoModel.getId());// 商家id
        merchandiseInfoModel.setGoodsNo(jsonData.getString("gcode"));// 商品货号        
        merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);  
        
        //判断是否来源菜鸟，如果是跳过
        if(merchandiseInfoModel != null &&
        		DERP_SYS.MERCHANDISEINFO_SOURCE_2.equals(merchandiseInfoModel.getSource())) {
        	return true ;
        }
		
        Long sourceGoodsId = goodsInfo(jsonData, merchantInfoModel, merchandiseInfoModel, null);
     // 保存嘉宝商品
        /*if ("0000138".equals(jsonData.getString("ccode"))) {
        	merchantInfoModel = new MerchantInfoModel();
            merchantInfoModel.setTopidealCode("1000000594");// 嘉宝
            merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
            // 查询商品
            merchandiseInfoModel =new MerchandiseInfoModel();
            merchandiseInfoModel.setMerchantId(merchantInfoModel.getId());// 商家id
            merchandiseInfoModel.setGoodsNo(jsonData.getString("gcode"));// 商品货号        
            merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);  
            goodsInfo(jsonData,merchantInfoModel,merchandiseInfoModel,sourceGoodsId);
            
        }*/
        // 保存卓普新商品
       /* merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setTopidealCode("1000000544");
        merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
        // 查询商品
        merchandiseInfoModel =new MerchandiseInfoModel();
        merchandiseInfoModel.setMerchantId(merchantInfoModel.getId());// 商家id
        merchandiseInfoModel.setGoodsNo(jsonData.getString("gcode"));// 商品货号        
        merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);  
        goodsInfo(jsonData,merchantInfoModel,merchandiseInfoModel,sourceGoodsId);
        */
		
		return true;
	}
    
    @Override
    public Long goodsInfo(JSONObject jsonData, MerchantInfoModel merchantInfoModel,
			MerchandiseInfoModel merchandiseInfoModel, Long sourceGoodsId) throws Exception {
     
        //==============商品信息=================
        MerchandiseInfoModel mModel=new MerchandiseInfoModel();
        //产品备注
        if(jsonData.get("prdRemark")!=null && StringUtils.isNotBlank(jsonData.getString("prdRemark"))){
        	mModel.setRemark(jsonData.getString("prdRemark"));
        }
        //产品名称
        if(jsonData.get("prdName")!=null && StringUtils.isNotBlank(jsonData.getString("prdName"))&&!"null".equals(jsonData.getString("prdName"))){
        	mModel.setName(jsonData.getString("prdName"));
        }
        //产品条形码
        String barcode = jsonData.getString("barcode");
        barcode = barcode.trim();
        mModel.setBarcode(barcode);

        //hscode
        if(jsonData.get("gType")!=null && StringUtils.isNotBlank(jsonData.getString("gType"))){
            mModel.setHsCode(jsonData.getString("gType"));
        }
        //规格型号
        if(jsonData.get("spec")!=null && StringUtils.isNotBlank(jsonData.getString("spec"))) {
            mModel.setSpec(jsonData.getString("spec"));
        }
        // 长   主数据是毫米 我们存的是厘米
        if (jsonData.get("length")!=null && StringUtils.isNotBlank(jsonData.getString("length"))) {
        	Double length = Double.valueOf(jsonData.getString("length"))*0.1;
        	mModel.setLength(length);
		}
        //宽  主数据是毫米 我们存的是厘米
        if (jsonData.get("width")!=null && StringUtils.isNotBlank(jsonData.getString("width"))) {
        	Double width = Double.valueOf(jsonData.getString("width"))*0.1;
        	mModel.setWidth(width);
		}
        //高   主数据是毫米 我们存的是厘米
        if (jsonData.get("height")!=null && StringUtils.isNotBlank(jsonData.getString("height"))) {
        	Double height = Double.valueOf(jsonData.getString("height"))*0.1;
        	mModel.setHeight(height);
		}
        // 体积  主数据是立方毫米 我们存的是立方厘米
        if (jsonData.get("volume")!=null && StringUtils.isNotBlank(jsonData.getString("volume"))) {
        	Double volume = Double.valueOf(jsonData.getString("volume"))*0.001;
        	mModel.setVolume(volume);
		}
        //毛重  主数据是g 我们保存的是kg
        if (jsonData.get("kgs")!=null && StringUtils.isNotBlank(jsonData.getString("kgs"))) {
        	Double kgs = Double.valueOf(jsonData.getString("kgs"))*0.001;
        	mModel.setGrossWeight(kgs);
		}
        //net净重   主数据是g 我们保存的是kg
        if (jsonData.get("net")!=null && StringUtils.isNotBlank(jsonData.getString("net"))) {
        	Double net = Double.valueOf(jsonData.getString("net"))*0.001;
        	mModel.setNetWeight(net);
		}
        //保质期天数
        if (jsonData.get("lifeDays")!=null && StringUtils.isNotBlank(jsonData.getString("lifeDays"))) {
        	mModel.setShelfLifeDays(Integer.valueOf(jsonData.getString("lifeDays")));
		}
        //生产厂家
        if (jsonData.get("produceComp")!=null && StringUtils.isNotBlank(jsonData.getString("produceComp"))) {
        	mModel.setManufacturer(jsonData.getString("produceComp"));
		}
        //生产厂家地址
        if (jsonData.get("manufacturerAddr")!=null && StringUtils.isNotBlank(jsonData.getString("manufacturerAddr"))) {
        	mModel.setManufacturerAddr(jsonData.getString("manufacturerAddr"));
		}
        //颜色
        if (jsonData.get("color")!=null && StringUtils.isNotBlank(jsonData.getString("color"))) {
        	mModel.setColor(jsonData.getString("color"));
		}
        //尺码
        if (jsonData.get("prdSize")!=null && StringUtils.isNotBlank(jsonData.getString("prdSize"))) {
        	mModel.setSize(Double.valueOf(jsonData.getString("prdSize")));
		}
        //成分
        if (jsonData.get("ingredient")!=null && StringUtils.isNotBlank(jsonData.getString("ingredient"))) {
        	mModel.setComponent(jsonData.getString("ingredient"));
		}
        // 产品计量单位
    	UnitInfoModel unitInfoModel =new UnitInfoModel();
        if (jsonData.get("qtyUnit")!=null && StringUtils.isNotBlank(jsonData.getString("qtyUnit"))) {
        	unitInfoModel.setName(jsonData.getString("qtyUnit"));
        	unitInfoModel = unitInfoDao.searchByModel(unitInfoModel);
		}
        if (null!=unitInfoModel) {
        	mModel.setUnit(unitInfoModel.getId());
		}
        //品牌名称(当品牌不存在新增品牌)
        BrandModel brandModel =new BrandModel();       
        if (jsonData.get("brand")!=null && StringUtils.isNotBlank(jsonData.getString("brand"))&&!"null".equals(jsonData.getString("brand"))) {
        	brandModel.setName(jsonData.getString("brand"));
        	brandModel = brandDao.searchByModel(brandModel);			
		}
        //新增品牌
        if (null==brandModel) {
        	brandModel =new BrandModel();
        	brandModel.setName(jsonData.getString("brand"));
        	brandDao.save(brandModel);
		}
        mModel.setBrandId(brandModel.getId());
        // 商家品牌关联表
        MerchantBrandRelModel merchantBrandRelModel = new  MerchantBrandRelModel();
        if (null!=brandModel.getId()) {
        	merchantBrandRelModel.setBrandId(brandModel.getId());// 品牌id
        	merchantBrandRelModel.setMerchantId(merchantInfoModel.getId());// 品牌名称
        	merchantBrandRelModel = merchantBrandRelDao.searchByModel(merchantBrandRelModel);
		}
        // 新增商家和品牌中间表数据
        if (null==merchantBrandRelModel&&brandModel.getId()!=null) {
        	MerchantBrandRelModel merBrandRelModel = new  MerchantBrandRelModel();
        	merBrandRelModel.setBrandId(brandModel.getId());
        	merBrandRelModel.setMerchantId(merchantInfoModel.getId());// 品牌名称
        	merchantBrandRelDao.save(merBrandRelModel);
		}
        
        //原产国
        CountryOriginModel countryOriginModel =new CountryOriginModel();
        if (jsonData.get("originCountry")!=null && StringUtils.isNotBlank(jsonData.getString("originCountry"))) {
        	countryOriginModel.setCode(jsonData.getString("originCountry"));
        	countryOriginModel = countryOriginDao.searchByModel(countryOriginModel);			
		}
        if (countryOriginModel!=null) {
        	mModel.setCountyId(countryOriginModel.getId());
		}

        //商品名称
        if(jsonData.get("gName")!=null && StringUtils.isNotBlank(jsonData.getString("gName"))) {
            mModel.setName(jsonData.getString("gName"));
        }
        //商品货号
        if(jsonData.get("gcode")!=null && StringUtils.isNotBlank(jsonData.getString("gcode"))) {
        	String goodsNo = jsonData.getString("gcode");
        	goodsNo = goodsNo.trim();
            mModel.setGoodsNo(goodsNo);
        }
        //备注
        if(jsonData.get("remark")!=null && StringUtils.isNotBlank(jsonData.getString("remark"))) {
            mModel.setRemark(jsonData.getString("remark"));
        }
        //唯一标识
        if(jsonData.get("opgcode")!=null && StringUtils.isNotBlank(jsonData.getString("opgcode"))) {
            mModel.setUniques(jsonData.getString("opgcode"));
        }   
        //mModel.setModifyDate(new Timestamp(new java.util.Date().getTime()));// 修改时间
        //备案价格   主数据是分 我们保存的是 元
        if(jsonData.get("upric")!=null && StringUtils.isNotBlank(jsonData.getString("upric"))) {
        	BigDecimal upric = new BigDecimal(jsonData.getDouble("upric"));
        	BigDecimal b = new BigDecimal(100);
        	BigDecimal filingPrice = upric.divide(b, 4, RoundingMode.HALF_UP);
            mModel.setFilingPrice(filingPrice);
        }
        //客户ID
        String goodsbarcode = jsonData.getString("goodsbarcode");
        goodsbarcode = goodsbarcode.trim();//收尾取空格
        mModel.setMerchantId(merchantInfoModel.getId());// 商家id
        mModel.setMerchantName(merchantInfoModel.getName());// 商家名称
        mModel.setBarcode(goodsbarcode);// 商品条形码


        // 包装方式
        if (jsonData.get("packtype")!=null && StringUtils.isNotBlank(jsonData.getString("packtype"))) {
        	mModel.setPackType(jsonData.getString("packtype"));
		}
        
                
        mModel.setSourceGoodsId(sourceGoodsId);
      
        //==============商品附表信息=================
        //商品附表
        MerchandiseScheduleModel merchandiseScheduleModel = new MerchandiseScheduleModel();
        
        //进出口标记
        if(jsonData.get("ieFlag")!=null && StringUtils.isNotBlank(jsonData.getString("ieFlag"))){
        	merchandiseScheduleModel.setIeFlag(jsonData.getString("ieFlag"));
        }        
        //业务类型
        if(jsonData.get("busitype")!=null && StringUtils.isNotBlank(jsonData.getString("busitype"))){
        	merchandiseScheduleModel.setBusinessType(jsonData.getString("busitype"));            
        }
        //申报地海关
        if(jsonData.get("customscode")!=null && StringUtils.isNotBlank(jsonData.getString("customscode"))){
        	merchandiseScheduleModel.setCustomsNo(jsonData.getString("customscode"));            
        }
        //账册编码
        if(jsonData.get("accountcode")!=null && StringUtils.isNotBlank(jsonData.getString("accountcode"))){
        	merchandiseScheduleModel.setAccountCode(jsonData.getString("accountcode"));            
        }
        //附表唯一标识
        if(jsonData.get("opgcode")!=null && StringUtils.isNotBlank(jsonData.getString("opgcode"))) {
        	merchandiseScheduleModel.setUniques(jsonData.getString("opgcode"));
        }
        
        // ----------------------------------------------是否请求请求金融处理开始------------------------
        String commonBarcode=null;//标准条码
        Long productTypeId=merchandiseInfoModel.getProductTypeId();//二级分类id
        
        if (merchandiseInfoModel!=null) {// 商品不为空取商品的标准条码
        	commonBarcode=merchandiseInfoModel.getCommbarcode();
		}
        MerchandiseCatModel merchandiseCat2 = null ;
        // 如果标准条码为空 或者 二级分类为空 根据条商品码查询我们的数据库
        if (StringUtils.isBlank(commonBarcode)||productTypeId==null) {
        	MerchandiseInfoModel merchandise =new MerchandiseInfoModel();
            merchandise.setMerchantId(merchantInfoModel.getId());// 商家id
            merchandise.setBarcode(goodsbarcode);// 商品条码   
            List<MerchandiseInfoModel> byMerchantBarcodeList = merchandiseInfoDao.getByMerchantBarcode(merchandise);
            if (byMerchantBarcodeList.size()>0) {//先拿主数据报文的条码查询经分销商品表是否存在商品，存在则取此商品的标准条码回填到报文
            	if (StringUtils.isBlank(commonBarcode)) {
            		commonBarcode = byMerchantBarcodeList.get(0).getCommbarcode();//如果
				}
            	if(productTypeId==null) {
            		productTypeId=byMerchantBarcodeList.get(0).getProductTypeId();
            	}
    		}
		}
        
        // 如果二级分类或者标准条码还有为空查金融  优先取经分销的数据  经分销没有 取金融
        if (StringUtils.isBlank(commonBarcode)||productTypeId==null) {
        	GoodsClassificationRequest GoodsRequest= new GoodsClassificationRequest();
			GoodsRequest.setRequestTime(TimeUtils.formatFullTime());
			List<GoodsClassificationItemRequest>goodsList=new ArrayList<GoodsClassificationItemRequest>();
			GoodsClassificationItemRequest goodsItemRequest =new GoodsClassificationItemRequest();
			goodsItemRequest.setGoodsBarcode(goodsbarcode);
			goodsList.add(goodsItemRequest);
			GoodsRequest.setGoodsList(goodsList);
			// 请求金融 商品分类查询接口 获取标准条码和分类
			String result = FinanceUtils.goodsClassificationRequest(JSONObject.fromObject(GoodsRequest));
			JSONObject classJson = JSONObject.fromObject(result);
			String status = classJson.getString("status");
			// 如果 标准条码为空 取金融的  如果金融 还为空报错
			if (StringUtils.isBlank(commonBarcode)) {				
				if (!"1".equals(status)) {
					throw new RuntimeException("标准条码commbarcode为空");
				}
				JSONArray goodsjsonArray = classJson.getJSONArray("goodsCategory");
				if (goodsjsonArray==null||goodsjsonArray.size()!=1) {
					throw new RuntimeException("标准条码commbarcode为空");
				}
				JSONObject goodsObject = (JSONObject) goodsjsonArray.get(0);
				if (goodsObject.get("commonBarcode")==null||StringUtils.isBlank(goodsObject.getString("commonBarcode"))) {
					 throw new RuntimeException("标准条码commbarcode为空");
				}
				// 经分销标准条码为空 取 金融标准条码
				commonBarcode = goodsObject.getString("commonBarcode");// 标准条码
			}
			// 经分销二级分类为空取金融二级分类
			if (productTypeId==null) {
				JSONArray goodsjsonArray = classJson.getJSONArray("goodsCategory");
				if ("1".equals(status)&&goodsjsonArray.size()==1) {
					JSONObject goodsObject = (JSONObject) goodsjsonArray.get(0);
					if (goodsObject.get("secondCategory")!=null&&StringUtils.isNotBlank(goodsObject.getString("secondCategory"))) {
						String secondCategory = goodsObject.getString("secondCategory");// 二级分类名称
						// 通过二级分类名称查询商品分类表
			        	merchandiseCat2= new MerchandiseCatModel();
			        	merchandiseCat2.setName(secondCategory);
			        	merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
			        				        	
					}
				}								
			}else {// 二级分类id 不为空
				merchandiseCat2 = merchandiseCatDao.searchById(productTypeId);
			}
        	
		}else {// 二级分类id 不为空 并且标准条码不为空
			merchandiseCat2 = merchandiseCatDao.searchById(productTypeId);									
		}
        
        if (merchandiseCat2!=null) {
    		mModel.setProductTypeId(merchandiseCat2.getId());
    		mModel.setProductTypeName(merchandiseCat2.getName());
		}
        // 通过二级分类找一级分类
    	if (merchandiseCat2!=null&&merchandiseCat2.getParentId()!=null) {
    		MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
    		merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
    		merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
    		if (merchandiseCat1!=null) {
    			mModel.setProductTypeId0(merchandiseCat1.getId());
        		mModel.setProductTypeName0(merchandiseCat1.getName());
			}
		}
        // 商品标准条码
        mModel.setCommbarcode(commonBarcode);        
    	// ----------------------------------------------是否请求请求金融处理结束------------------------
        
        
        // 新增或修改商品
        if (merchandiseInfoModel==null) {// 新增
        	mModel.setIsGroup("0");//是否组合(1-是，0-否)             
            mModel.setStatus("1");//状态(1使用中,0已禁用)
            mModel.setCode(SmurfsUtils.getID("ERP"));// 商品编码
            mModel.setGoodsCode(SmurfsUtils.getID("ERP"));       // 商品编码 
        	mModel.setSource("1");// 数据来源 1主数据
            mModel.setIsRecord("1");//是否备案(1-是，0-否)
			if (isExistOutDepotMerchandise(merchantInfoModel,barcode)) {
				mModel.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
			} else {
				mModel.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}
			merchandiseInfoDao.save(mModel);
			saveCommBarbarcode(mModel, brandModel, merchandiseCat2);
			
			/*// 若是嘉宝的新增商品发消息给ordermq（宝洁商品货期配置MQ消费端）
			if("0000138".equals(merchantInfoModel.getTopidealCode())){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("merchantId", merchantInfoModel.getId());	// 商家ID
				jsonObj.put("merchantName", merchantInfoModel.getName());	// 商家名称
				jsonObj.put("merchantCode", merchantInfoModel.getCode());	// 商家编码
				jsonObj.put("goodsId", mModel.getId());			// 商品ID
				jsonObj.put("goodsNo", mModel.getGoodsNo());			// 商品货号
				jsonObj.put("goodsName", mModel.getName());			// 商品名称
				jsonObj.put("goodsCode", mModel.getGoodsCode());			// 商品编码
				jsonObj.put("commbarcode", mModel.getCommbarcode());		// 标准条码
				jsonObj.put("operateType", "1");		// 操作类型 0:删除 1:增加/修改
				try{//orderMQ
					SendResult result = rocketMQProducer.send(jsonObj.toString(), MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTopic(),MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTags());
					LOGGER.info("宝洁商品货期配置MsgId:"+result.getMsgId());
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("宝洁商品货期配置异常："+e.getMessage());
				}
			}*/
			JSONObject json=new JSONObject();
			json.put("merchantId", mModel.getMerchantId());
			json.put("goodsId", mModel.getId());
			json.put("tag", "2");//"2" 页面/其他触发单个货号 "1" 定时器 
			//  推送复制商品到卓普信/宝丰/嘉宝	
			SendResult send = rocketMQProducer.send(json.toString(), MQErpEnum.COPY_MERCHANDISE.getTopic(), MQErpEnum.COPY_MERCHANDISE.getTags());		
			if (send==null||!send.getSendStatus().name().equals("SEND_OK")) {
				throw new RuntimeException("推送商品复制数据失败");
			}
		}else{
			//修改商品信息
			mModel.setIsGroup(merchandiseInfoModel.getIsGroup());//是否组合(1-是，0-否)		        
		    mModel.setStatus(merchandiseInfoModel.getStatus());//状态(1使用中,0已禁用)
			mModel.setId(merchandiseInfoModel.getId());
			mModel.setBarcode(merchandiseInfoModel.getBarcode());//如果是修改 不修改条码字段
			//merchandiseInfoDao.modify(mModel);
			
			saveCommBarbarcode(mModel, brandModel, merchandiseCat2);
			
			//判断 条码/货号/商品名称/标准条码是否改变，是：推送到业务、仓储、库存、报表MQ修改商品的信息(不能影响后续流程)
			if(!mModel.getBarcode().equals(merchandiseInfoModel.getBarcode()) ||
					!mModel.getGoodsNo().equals(merchandiseInfoModel.getGoodsNo()) ||
					!mModel.getName().equals(merchandiseInfoModel.getName()) || 
					!mModel.getCommbarcode().equals(merchandiseInfoModel.getCommbarcode())){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("goodsId", mModel.getId());						// 商品ID
				jsonObj.put("barcode", mModel.getBarcode());				// 条码
				jsonObj.put("goodsNo", mModel.getGoodsNo());			// 货号
				jsonObj.put("goodsName", mModel.getName());			// 商品名称
				jsonObj.put("commbarcode", mModel.getCommbarcode());		// 修改以后的标准条码
				jsonObj.put("oldCommbarcode", merchandiseInfoModel.getCommbarcode());		// 修改以前的标准条码
				jsonObj.put("merchantId", merchantInfoModel.getId());	// 商家ID
				jsonObj.put("merchantName", merchantInfoModel.getName());	// 商家名称
				jsonObj.put("merchantCode", merchantInfoModel.getCode());	// 商家编码
				jsonObj.put("goodsCode", mModel.getGoodsCode());			// 商品编码
				jsonObj.put("operateType", "1");		// 操作类型 0:删除 1:增加/修改
				try{//orderMQ
					//SendResult result = rocketMQProducer.send(jsonObj.toString(), MQOrderEnum.UPDATE_ORDER_GOODS_INFO.getTopic(),MQOrderEnum.UPDATE_ORDER_GOODS_INFO.getTags());
					//LOGGER.info("更新业务库商品信息MsgId:"+result.getMsgId());
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("更新业务库商品信息异常："+e.getMessage());
				}
				/*// 若是嘉宝的新增商品发消息给ordermq
				if("0000138".equals(merchantInfoModel.getTopidealCode())){
					try{//orderMQ
						SendResult result = rocketMQProducer.send(jsonObj.toString(), MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTopic(),MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTags());
						LOGGER.info("宝洁商品货期配置MsgId:"+result.getMsgId());
					}catch(Exception e){
						e.printStackTrace();
						LOGGER.error("宝洁商品货期配置异常："+e.getMessage());
					}
				}*/
				try{//storageMQ
					//SendResult result = rocketMQProducer.send(jsonObj.toString(), MQStorageEnum.UPDATE_STORAGE_GOODS_INFO.getTopic(),MQStorageEnum.UPDATE_STORAGE_GOODS_INFO.getTags());
					//LOGGER.info("更新仓储库商品信息MsgId:"+result.getMsgId());
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("更新仓储库商品信息异常："+e.getMessage());
				}
				try{//inventoryMQ
					//SendResult result = rocketMQProducer.send(jsonObj.toString(), MQInventoryEnum.UPDATE_INVENTORY_GOODS_INFO.getTopic(),MQInventoryEnum.UPDATE_INVENTORY_GOODS_INFO.getTags());
					//LOGGER.info("更新库存库商品信息MsgId:"+result.getMsgId());
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("更新库存库商品信息异常："+e.getMessage());
				}
				try{//reportMQ
					//SendResult result = rocketMQProducer.send(jsonObj.toString(), MQReportEnum.UPDATE_REPORT_GOODS_INFO.getTopic(),MQReportEnum.UPDATE_REPORT_GOODS_INFO.getTags());
					//LOGGER.info("更新报表库商品信息MsgId:"+result.getMsgId());
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("更新报表库商品信息异常："+e.getMessage());
				}
			}
		}
         //根据唯一标识 查询商品附表        
        MerchandiseScheduleModel mScheduleModel = new MerchandiseScheduleModel();
        mScheduleModel.setMerchantId(merchantInfoModel.getId());
        mScheduleModel.setUniques(jsonData.getString("opgcode"));//商品唯一标识
        mScheduleModel = merchandiseScheduleDao.searchByModel(mScheduleModel);
        
     // 商品附表新增和修改        
        if (null==mScheduleModel) {// 新增
        	merchandiseScheduleModel.setGoodsId(mModel.getId());
        	merchandiseScheduleModel.setMerchantId(merchantInfoModel.getId());       	
        	merchandiseScheduleModel.setGoodsNo(jsonData.getString("gcode"));
        	merchandiseScheduleDao.save(merchandiseScheduleModel);
			
		}else{//修改
			merchandiseScheduleModel.setId(mScheduleModel.getId());
			merchandiseScheduleModel.setMerchantId(merchantInfoModel.getId());    
			merchandiseScheduleModel.setGoodsId(mModel.getId());
        	merchandiseScheduleModel.setGoodsNo(jsonData.getString("gcode"));
        	merchandiseScheduleModel.setModifyDate(TimeUtils.getNow());
        	merchandiseScheduleDao.modify(merchandiseScheduleModel);
		}
        return mModel.getId();
    }
    
    /**
     * 根据数据生成标准条码记录
     * @param merchandiseInfoModel
     * @param brandModel
     * @param merchandiseCatModel
     * @throws SQLException
     */
    private void saveCommBarbarcode(MerchandiseInfoModel merchandiseInfoModel , BrandModel brandModel ,MerchandiseCatModel merchandiseCatModel) throws SQLException {
    	
    	if(merchandiseCatModel == null) {
    		merchandiseCatModel = new MerchandiseCatModel() ;
    	}
    	
    	if(brandModel == null) {
    		brandModel = new BrandModel() ;
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
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
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
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
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
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());
    			commbarcodeItemDao.modify(commbarcodeItemModel) ;
    		}
    	}
    }


    /**
     * @Description 判断同个商家、同个标准条码下是否存在“外仓统一码”为"是"的商品
     * @param merchantInfo
	 * @param commbarcode
     * @return
     */
    private boolean isExistOutDepotMerchandise(MerchantInfoModel merchantInfo, String barcode) throws SQLException {
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantInfo.getId());
    	//判断商家是否是代理商家，若是则直接返回false, 打标“外仓统一码”为"否"
		if (merchantInfoModel.getIsProxy().equals(DERP_SYS.MERCHANTINFO_ISPROXY_1)) {
			return false;
		}
    	MerchandiseInfoModel isExistOutDepotMerchandise = new MerchandiseInfoModel();
		isExistOutDepotMerchandise.setMerchantId(merchantInfo.getId());
		isExistOutDepotMerchandise.setBarcode(barcode);
		isExistOutDepotMerchandise.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		isExistOutDepotMerchandise.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
		isExistOutDepotMerchandise = merchandiseInfoDao.searchByModel(isExistOutDepotMerchandise);
		if (isExistOutDepotMerchandise == null) {
			return false;
		} else {
			return true;
		}
	}


}
