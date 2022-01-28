package com.topideal.service.maindata.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.pms.PMSUtils;
import com.topideal.api.pms.p_001.PMSGoodQueryRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.dao.main.DepotCustomsRelDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseDepotRelDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchandiseScheduleDao;
import com.topideal.dao.main.MerchantBrandRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchandiseScheduleModel;
import com.topideal.entity.vo.main.MerchantBrandRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.maindata.SyncGoodsSendService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by 杨创 2020-08-04
 */
@Service
public class SyncGoodsSendServiceImpl implements SyncGoodsSendService {
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncGoodsSendServiceImpl.class);

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
    @Autowired
    private DepotCustomsRelDao depotCustomsRelDao;// 仓库关区关系表
	@Autowired
	private MerchandiseDepotRelDao merchandiseDepotRelDao;// 商品仓库关系表

    
    //新增/或修改多商家商品
	@Override
	public boolean synsMerchantGoods(String json) throws Exception {
		//json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        // 根据卓志编码查询商家
        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setTopidealCode(jsonData.getString("merchantCode"));
        merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
        if(merchantInfoModel==null){
        	LOGGER.error("商家卓志编码不存在");
            return true;
        }
        // 查询商家id和商品货号查询商品
        MerchandiseInfoModel merchandiseInfoQuery =new MerchandiseInfoModel();
        merchandiseInfoQuery.setMerchantId(merchantInfoModel.getId());// 商家id
        merchandiseInfoQuery.setGoodsNo(jsonData.getString("goodsCode"));// 商品货号        
        merchandiseInfoQuery = merchandiseInfoDao.searchByModel(merchandiseInfoQuery);         
        //判断是否来源菜鸟，如果是跳过
        if(merchandiseInfoQuery != null &&
        		DERP_SYS.MERCHANDISEINFO_SOURCE_2.equals(merchandiseInfoQuery.getSource())) {
        	return true ;
        }
        Long sourceGoodsId = goodsSend(jsonData,merchantInfoModel,merchandiseInfoQuery,null);
        // 保存嘉宝商品
       /* if ("0000138".equals(jsonData.getString("merchantCode"))) {
        	merchantInfoModel = new MerchantInfoModel();
            merchantInfoModel.setTopidealCode("1000000594");
            merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
            // 查询商品
            merchandiseInfoQuery =new MerchandiseInfoModel();
            merchandiseInfoQuery.setMerchantId(merchantInfoModel.getId());// 商家id
            merchandiseInfoQuery.setGoodsNo(jsonData.getString("goodsCode"));// 商品货号        
            merchandiseInfoQuery = merchandiseInfoDao.searchByModel(merchandiseInfoQuery);  
            goodsSend(jsonData,merchantInfoModel,merchandiseInfoQuery,sourceGoodsId);            
            
        }*/
        // 保存卓普信商品
     /*   merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setTopidealCode("1000000544");
        merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
        // 查询商品
        merchandiseInfoQuery =new MerchandiseInfoModel();
        merchandiseInfoQuery.setMerchantId(merchantInfoModel.getId());// 商家id
        merchandiseInfoQuery.setGoodsNo(jsonData.getString("goodsCode"));// 商品货号        
        merchandiseInfoQuery = merchandiseInfoDao.searchByModel(merchandiseInfoQuery);  
        
        goodsSend(jsonData,merchantInfoModel,merchandiseInfoQuery,sourceGoodsId);
*/
		return true;
	}
    
    
    @Override
    public Long goodsSend(JSONObject jsonData,MerchantInfoModel merchantInfoModel,MerchandiseInfoModel merchandiseInfoQuery,Long sourceGoodsId) throws Exception {

              
        String goodsbarcodes = jsonData.getString("barCode");  //条码
        String[] goodsbarcodeArr = goodsbarcodes.split(",");
        String goodsbarcode = goodsbarcodeArr[0].trim();//收尾取空格
        String commonBarcode=jsonData.getString("productCode");//标准条码
        commonBarcode = commonBarcode.trim();//收尾取空格
        boolean falg=false;//返回true 库存加减明细存在  false 不存在
        // 商品不为空 并且 (商品的条码或者标准条码和主数据不同) 查询商品收发明细看是否有出入库 有的话不更新 没有的话更新
        if (merchandiseInfoQuery!=null&&(!goodsbarcode.equals(merchandiseInfoQuery.getBarcode())||
        		!commonBarcode.equals(merchandiseInfoQuery.getCommbarcode()))) {
        	falg= merchandiseInfoDao.getInventoryDetials(merchantInfoModel.getId(),merchandiseInfoQuery.getId());        	
        }
        // 库存加减明细存在就不更新 标准条码和条码
        if (falg) {
        	commonBarcode=merchandiseInfoQuery.getCommbarcode();
        	goodsbarcode=merchandiseInfoQuery.getBarcode();
		}
        //-----------------------------封装商品---------------------------------
        MerchandiseInfoModel mModel = getMerchandiseInfoModel(jsonData,merchantInfoModel); 
        mModel.setCommbarcode(commonBarcode);//标准条码
        mModel.setBarcode(goodsbarcode);// 商品条形码
        mModel.setSourceGoodsId(sourceGoodsId);// 商品id来源
  
        //-----------------------------封装产品开始---------------------------------
        // 查询计量单位
        UnitInfoModel unitInfoModel = getUnitInfoModel(jsonData);
        mModel.setUnit(unitInfoModel.getId());
        // 获取二级分类
        MerchandiseCatModel catModel2=new MerchandiseCatModel();       
        if (jsonData.get("class2")!=null && StringUtils.isNotBlank(jsonData.getString("class2"))&&!"null".equals(jsonData.getString("class2"))) {
        	catModel2.setSysCode(jsonData.getString("class2"));
        	catModel2 = merchandiseCatDao.searchByModel(catModel2);		
		}
        //获取一级分类
        MerchandiseCatModel catModel1=new MerchandiseCatModel();        
        if (catModel2!=null&&catModel2.getParentId()!=null) {
        	catModel1.setId(catModel2.getParentId());
        	catModel1 = merchandiseCatDao.searchByModel(catModel1);	
		}
        if (catModel2==null)catModel2=new MerchandiseCatModel();
        if (catModel1==null)catModel1=new MerchandiseCatModel();
        mModel.setProductTypeId(catModel2.getId());//二级分类id 
		mModel.setProductTypeName(catModel2.getName());//二级分类名称
		mModel.setProductTypeId0(catModel1.getId());// 一级分类id
		mModel.setProductTypeName0(catModel1.getName());// 一级分类名称
        // 获取原产国
        CountryOriginModel countryOriginModel = getCountryOriginModel(jsonData);
        mModel.setCountyId(countryOriginModel.getId());
        // 获取品牌数据 并生成商家和品牌关系数据
        BrandModel brandModel = saveBrand(jsonData,merchantInfoModel);        
        mModel.setBrandId(brandModel.getId()); // 品牌id 
        
        //-----------------------------封装产品结束---------------------------------


        // 新增或修改商品
        MerchandiseInfoModel saveMerchandiseInfoModel = saveMerchandiseInfoModel(falg,merchantInfoModel,merchandiseInfoQuery,mModel,goodsbarcodes);
        
        // 生成标准条码表数据
        saveCommBarbarcode(mModel, brandModel, catModel2);
        
        // 生成/修改商品附表( 这个要放到商品生成和修改后面)
        saveMerchandiseScheduleModel(jsonData,mModel,merchantInfoModel);
       
		JSONObject json=new JSONObject();
		json.put("merchantId", saveMerchandiseInfoModel.getMerchantId());
		json.put("goodsId", saveMerchandiseInfoModel.getId());
		json.put("tag", "2");//"2" 页面/其他触发单个货号 "1" 定时器 

		//  推送复制商品到卓普信/宝丰/嘉宝	
		SendResult send = rocketMQProducer.send(json.toString(), MQErpEnum.COPY_MERCHANDISE.getTopic(), MQErpEnum.COPY_MERCHANDISE.getTags());		
		if (send==null||!send.getSendStatus().name().equals("SEND_OK")) {
			throw new RuntimeException("推送商品复制数据失败");
		}
        return saveMerchandiseInfoModel.getId();
    }
    
    /**
	 * 调用PMS的商品查询接口 获取毛重 净重
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
    /**
     * 新增/修改商品
     * @param mModel
     * @param productId
     * @throws Exception 
     */
    private MerchandiseInfoModel saveMerchandiseInfoModel(boolean falg,MerchantInfoModel merchantInfoModel,MerchandiseInfoModel merchandiseInfoQuery,
    		MerchandiseInfoModel mModel,String barcode) throws Exception {
    	
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(merchantInfoModel.getTopidealCode());
		productCodes.add(mModel.getGoodsNo());
		// 获取pms 商品信息
		Map<String, Map<String, Object>> pmsGoodsMap=new  HashMap<String, Map<String,Object>>();
		pmsGoodsMap = getPmsGoods(productCodes, cCodes);					
		// 获取pms商品 毛重 净重
		String accountCode =null;
		Map<String, Object> map = pmsGoodsMap.get(mModel.getGoodsNo());
		if (map!=null) {
			Double net = (Double) map.get("net");
			Double kgs = (Double) map.get("kgs");
			Long countryId = (Long) map.get("countryId");
			accountCode = (String) map.get("accountCode");
			mModel.setNetWeight(net);
			mModel.setGrossWeight(kgs);
			mModel.setCountyId(countryId);			
		}
    	
    	
    	// 新增或修改商品
        if (merchandiseInfoQuery==null) {// 新增
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
		}else{
			//修改商品信息
			mModel.setIsGroup(merchandiseInfoQuery.getIsGroup());//是否组合(1-是，0-否)		        
		    mModel.setStatus(merchandiseInfoQuery.getStatus());//状态(1使用中,0已禁用)
			mModel.setId(merchandiseInfoQuery.getId());
			
			// 修改商品从新封装
			MerchandiseInfoModel newMerchandise=new MerchandiseInfoModel();
			newMerchandise.setModifyDate(TimeUtils.getNow());
			newMerchandise.setId(merchandiseInfoQuery.getId());
			// 条码和标准条码没有被使用并且接口来的和查询的不同修改
			if (falg==false) {								
				newMerchandise.setBarcode(mModel.getBarcode());
				newMerchandise.setCommbarcode(mModel.getCommbarcode());				
			}			
			newMerchandise.setUnit(mModel.getUnit());
			newMerchandise.setProductTypeId(mModel.getProductTypeId());
			newMerchandise.setProductTypeName(mModel.getProductTypeName());
			newMerchandise.setProductTypeId0(mModel.getProductTypeId0());
			newMerchandise.setProductTypeName0(mModel.getProductTypeName0());
			newMerchandise.setCountyId(mModel.getCountyId());
			newMerchandise.setBrandId(mModel.getBrandId());
			newMerchandise.setVersion(mModel.getVersion());
			newMerchandise.setUniques(mModel.getUniques());
			newMerchandise.setFilingPrice(mModel.getFilingPrice());
			newMerchandise.setEnglishGoodsName(mModel.getEnglishGoodsName());
			newMerchandise.setSpec(mModel.getSpec());
			newMerchandise.setLength(mModel.getLength());
			newMerchandise.setWidth(mModel.getWidth());
			newMerchandise.setHeight(mModel.getHeight());
			newMerchandise.setVolume(mModel.getVolume());
			newMerchandise.setGrossWeight(mModel.getGrossWeight());
			newMerchandise.setNetWeight(mModel.getNetWeight());
			newMerchandise.setShelfLifeDays(mModel.getShelfLifeDays());			
			newMerchandise.setName(mModel.getName());
			merchandiseInfoDao.modify(newMerchandise);// 条码不修改		

		}
        //新增商品仓库关系数据
		// 新增商品仓库关系数据
		if (StringUtils.isNotBlank(accountCode)) {
			//查询仓库关区关系表
			DepotCustomsRelModel relModel=new DepotCustomsRelModel();
			relModel.setOnlineRegisterNo(accountCode);
			List<DepotCustomsRelModel> relList = depotCustomsRelDao.list(relModel);
			for (DepotCustomsRelModel depotCustomsRel : relList) {
				MerchandiseDepotRelModel merchandiseDepotRel=new MerchandiseDepotRelModel();
				merchandiseDepotRel.setDepotId(depotCustomsRel.getDepotId());
				merchandiseDepotRel.setDepotName(depotCustomsRel.getDepotName());
				merchandiseDepotRel.setMerchantId(merchantInfoModel.getId());
				merchandiseDepotRel.setGoodsId(mModel.getId());
				merchandiseDepotRelDao.save(merchandiseDepotRel);
			}
			
			
		}
		
        
        return mModel;
		
	}
	/**
     * 生成/修改商品附表
     * @param jsonData
     * @throws SQLException 
     */
    private void saveMerchandiseScheduleModel(JSONObject jsonData,MerchandiseInfoModel mModel,
    		MerchantInfoModel merchantInfoModel) throws SQLException {
    	 //附表唯一标识
        MerchandiseScheduleModel merchandiseScheduleModel = new MerchandiseScheduleModel();
        if(jsonData.get("code")!=null && StringUtils.isNotBlank(jsonData.getString("code"))) {
        	merchandiseScheduleModel.setUniques(jsonData.getString("code"));
        } 
      //根据唯一标识 查询商品附表        
        MerchandiseScheduleModel mScheduleModel = new MerchandiseScheduleModel();
        mScheduleModel.setUniques(jsonData.getString("code"));//商品唯一标识
        mScheduleModel.setMerchantId(merchantInfoModel.getId());
        mScheduleModel = merchandiseScheduleDao.searchByModel(mScheduleModel);
        if (null==mScheduleModel) {// 新增
        	merchandiseScheduleModel.setGoodsId(mModel.getId());
        	merchandiseScheduleModel.setMerchantId(merchantInfoModel.getId());       	
        	merchandiseScheduleModel.setGoodsNo(jsonData.getString("goodsCode"));
        	merchandiseScheduleDao.save(merchandiseScheduleModel);
			
		}else{//修改
			merchandiseScheduleModel.setId(mScheduleModel.getId());
			merchandiseScheduleModel.setMerchantId(merchantInfoModel.getId());    
			merchandiseScheduleModel.setGoodsId(mModel.getId());
        	merchandiseScheduleModel.setGoodsNo(jsonData.getString("goodsCode"));
        	merchandiseScheduleModel.setModifyDate(TimeUtils.getNow());
        	merchandiseScheduleDao.modify(merchandiseScheduleModel);
		}
		
	}
	/**
     * 获取原产国
     * @param jsonData
     * @throws SQLException 
     */
    private CountryOriginModel getCountryOriginModel(JSONObject jsonData) throws SQLException {
    	CountryOriginModel countryOriginModel =new CountryOriginModel();
        if (jsonData.get("cmcOriginCountry")!=null && StringUtils.isNotBlank(jsonData.getString("cmcOriginCountry"))&&!"null".equals(jsonData.getString("cmcOriginCountry"))) {
        	countryOriginModel.setCode(jsonData.getString("cmcOriginCountry"));
        	countryOriginModel = countryOriginDao.searchByModel(countryOriginModel);			
		}
        if (countryOriginModel==null) countryOriginModel=new CountryOriginModel();
        return countryOriginModel;
		
	}

	private BrandModel saveBrand(JSONObject jsonData, MerchantInfoModel merchantInfoModel) throws SQLException {
    	//品牌名称(当品牌不存在新增品牌)
        BrandModel brandModel =new BrandModel();       
        if (jsonData.get("brand")!=null && StringUtils.isNotBlank(jsonData.getString("brand"))&&!"null".equals(jsonData.getString("brand"))) {
        	brandModel.setName(jsonData.getString("brand"));
        	brandModel = brandDao.searchByModel(brandModel);			
		}
        String englishBrandName=null;
    	if (jsonData.get("englishBrand")!=null&&StringUtils.isNotBlank(jsonData.getString("englishBrand"))&&!"null".equals(jsonData.getString("englishBrand"))){
    		englishBrandName=jsonData.getString("englishBrand");
		}
    	String chinaBrandName=null;
		if (jsonData.get("chinaBrand")!=null&&StringUtils.isNotBlank(jsonData.getString("chinaBrand"))&&!"null".equals(jsonData.getString("chinaBrand"))){
			chinaBrandName=jsonData.getString("chinaBrand");
		}
		String brandName=null;
        if (jsonData.get("brand")!=null && StringUtils.isNotBlank(jsonData.getString("brand"))&&!"null".equals(jsonData.getString("brand"))) {
        	brandName=jsonData.getString("brand");
		}
        //新增品牌
        if (null==brandModel) {
        	brandModel =new BrandModel();
        	brandModel.setName(jsonData.getString("brand"));        	
        	brandModel.setEnglishBrandName(englishBrandName);// 英文名称
        	brandModel.setChinaBrandName(chinaBrandName);;// 中文名称
        	if (brandName!=null)brandDao.save(brandModel);// 品牌名称不为空才新增
		}else {
			BrandModel brand =new BrandModel();
			brand.setId(brandModel.getId());
        	brand.setName(brandName);
        	brand.setEnglishBrandName(englishBrandName);
        	brand.setChinaBrandName(chinaBrandName);
        	brand.setModifyDate(TimeUtils.getNow());
        	brandDao.modify(brand);
		}
        

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
		
        return brandModel;
	}

	/**
     * 获取计量单位
     * @param jsonData
     * @throws SQLException 
     */
    private UnitInfoModel getUnitInfoModel(JSONObject jsonData) throws SQLException {
    	UnitInfoModel unitInfoModel =new UnitInfoModel();
        if (jsonData.get("unit")!=null && StringUtils.isNotBlank(jsonData.getString("unit"))&&!"null".equals(jsonData.getString("unit"))) {
        	unitInfoModel.setCode(jsonData.getString("unit"));
        	unitInfoModel = unitInfoDao.searchByModel(unitInfoModel);
		}
        if (null==unitInfoModel)unitInfoModel=new UnitInfoModel();		
        return unitInfoModel;       		
	}
	/**
     * 获取商品实体
     * @param jsonData
     * @return
     */
    private MerchandiseInfoModel getMerchandiseInfoModel(JSONObject jsonData,MerchantInfoModel merchantInfoModel) {
    	 MerchandiseInfoModel mModel=new MerchandiseInfoModel();
         //商品名称
         if(jsonData.get("goodsName")!=null && StringUtils.isNotBlank(jsonData.getString("goodsName"))&&!"null".equals(jsonData.getString("goodsName"))) {
             mModel.setName(jsonData.getString("goodsName"));
         }
         //商品货号
         if(jsonData.get("goodsCode")!=null && StringUtils.isNotBlank(jsonData.getString("goodsCode"))&&!"null".equals(jsonData.getString("goodsCode"))) {
        	 String goodsNo = jsonData.getString("goodsCode");
        	 goodsNo = goodsNo.trim();
        	 mModel.setGoodsNo(goodsNo);
         }
         //唯一标识
         if(jsonData.get("code")!=null && StringUtils.isNotBlank(jsonData.getString("code"))&&!"null".equals(jsonData.getString("code"))) {
             mModel.setUniques(jsonData.getString("code"));
         }   
         //备案价格   主数据是分 我们保存的是 元
         if(jsonData.get("price")!=null && StringUtils.isNotBlank(jsonData.getString("price"))&&!"null".equals(jsonData.getString("price"))) {
         	BigDecimal upric = new BigDecimal(jsonData.getDouble("price"));
         	BigDecimal b = new BigDecimal(100);
         	BigDecimal filingPrice = upric.divide(b, 4, RoundingMode.HALF_UP);
            mModel.setFilingPrice(filingPrice);
         }
         if (jsonData.get("goodsEnName")!=null && StringUtils.isNotBlank(jsonData.getString("goodsEnName"))&&!"null".equals(jsonData.getString("goodsEnName"))) {
        	 mModel.setEnglishGoodsName(jsonData.getString("goodsEnName"));
         }
       //规格型号
         if(jsonData.get("spec")!=null && StringUtils.isNotBlank(jsonData.getString("spec"))&&!"null".equals(jsonData.getString("spec"))) {
        	 mModel.setSpec(jsonData.getString("spec"));
         }
         // 长   主数据是毫米 我们存的是厘米
         if (jsonData.get("length")!=null && StringUtils.isNotBlank(jsonData.getString("length"))&&!"null".equals(jsonData.getString("length"))) {
         	Double length = Double.valueOf(jsonData.getString("length"))*0.1;
         	mModel.setLength(length);
 		}
         //宽  主数据是毫米 我们存的是厘米
         if (jsonData.get("width")!=null && StringUtils.isNotBlank(jsonData.getString("width"))&&!"null".equals(jsonData.getString("width"))) {
         	Double width = Double.valueOf(jsonData.getString("width"))*0.1;
         	mModel.setWidth(width);
 		}
         //高   主数据是毫米 我们存的是厘米
         if (jsonData.get("height")!=null && StringUtils.isNotBlank(jsonData.getString("height"))&&!"null".equals(jsonData.getString("height"))) {
         	Double height = Double.valueOf(jsonData.getString("height"))*0.1;
         	mModel.setHeight(height);
 		}
         // 体积  主数据是立方毫米 我们存的是立方厘米
         if (jsonData.get("volume")!=null && StringUtils.isNotBlank(jsonData.getString("volume"))&&!"null".equals(jsonData.getString("volume"))) {
         	Double volume = Double.valueOf(jsonData.getString("volume"))*0.001;
         	mModel.setVolume(volume);
 		}
         //毛重  主数据是g 我们保存的是kg
         if (jsonData.get("grossWeight")!=null && StringUtils.isNotBlank(jsonData.getString("grossWeight"))&&!"null".equals(jsonData.getString("grossWeight"))) {
         	Double kgs = Double.valueOf(jsonData.getString("grossWeight"))*0.001;
         	mModel.setGrossWeight(kgs);
 		}
         //net净重   主数据是g 我们保存的是kg
         if (jsonData.get("netWeight")!=null && StringUtils.isNotBlank(jsonData.getString("netWeight"))&&!"null".equals(jsonData.getString("netWeight"))) {
         	Double net = Double.valueOf(jsonData.getString("netWeight"))*0.001;
         	mModel.setNetWeight(net);
 		}
         //保质期天数
         if (jsonData.get("qualityDays")!=null && StringUtils.isNotBlank(jsonData.getString("qualityDays"))&&!"null".equals(jsonData.getString("qualityDays"))) {
        	 mModel.setShelfLifeDays(Integer.valueOf(jsonData.getString("qualityDays")));
 		}
         
         
         mModel.setTenantCode(jsonData.getString("tenantCode"));
         String version = jsonData.getString("version");
         mModel.setVersion(Integer.valueOf(version));
         mModel.setMerchantId(merchantInfoModel.getId());// 商家id
         mModel.setMerchantName(merchantInfoModel.getName());// 商家名称         
    	return mModel;
		
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

    /**
     * 判断是否是经分销的商家
     */
	@Override
	public MerchantInfoModel isExitMerchant(MerchantInfoModel model) throws Exception {		
		return merchantInfoDao.searchByModel(model);
	}
	
}
