package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.dao.main.DepotMerchantRelDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseDepotRelDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchandiseScheduleDao;
import com.topideal.dao.main.MerchantBrandRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantBrandRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.mapper.main.MerchandiseDepotRelMapper;
import com.topideal.service.CopyMerchandiseService;

import net.sf.json.JSONObject;

@Service
public class CopyMerchandiseServiceImpl implements CopyMerchandiseService {

	private static final Logger LOGGER= LoggerFactory.getLogger(CopyMerchandiseServiceImpl.class);

	@Autowired
	private MerchandiseInfoDao  merchandiseInfoDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;	
	@Autowired
	private BrandDao brandDao;
    @Autowired
    private MerchantBrandRelDao merchantBrandRelDao;//商家和品牌关联表
    @Autowired
    private MerchandiseScheduleDao merchandiseScheduleDao;
    @Autowired
    private CommbarcodeItemDao commbarcodeItemDao;
    @Autowired
    private CommbarcodeDao commbarcodeDao;
    @Autowired
    private MerchandiseCatDao merchandiseCatDao;// 产品分类
    @Autowired
    private MerchandiseDepotRelDao merchandiseDepotRelDao;// 商品仓库关系数据
    @Autowired
    private DepotMerchantRelDao depotMerchantRelDao;// 商家仓库关系表
    
	@Autowired
	private RMQProducer rocketMQProducer;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700002,model=DERP_LOG_POINT.POINT_13201700002_Label,keyword="derpTime")
	public void saveCopyMerchandise(String json,MerchantInfoModel merchantInfoModel) throws Exception {
		LOGGER.info("----------生成/修改卓普信和嘉宝商品--------开始");
		JSONObject jsonData = JSONObject.fromObject(json);
		String tag = jsonData.getString("tag");//"2" 页面/其他触发单个货号 "1" 定时器 
		Long goodsId=null;
		if ("2".equals(tag)) {
			goodsId = Long.valueOf(jsonData.getString("goodsId"));	
		}
		//获取所有商家
		List<MerchantInfoModel> merchantList = merchantInfoDao.list(new MerchantInfoModel());
		Map<Long, String>merchantMap=new HashMap<Long, String>();
		for (MerchantInfoModel merchant : merchantList) {
			merchantMap.put(merchant.getId(), merchant.getTopidealCode());
		}
		// 获取嘉宝商家
		MerchantInfoModel jiabaoMerchant = new MerchantInfoModel();
		jiabaoMerchant = new MerchantInfoModel();
		jiabaoMerchant.setTopidealCode("1000000594");
		jiabaoMerchant= merchantInfoDao.searchByModel(jiabaoMerchant);
		if (jiabaoMerchant==null) {
			throw new RuntimeException("嘉宝卓志编码1000000594没有找到商家数据");
		}
		// 获取卓普信商家
		MerchantInfoModel zhuopuxinMerchant = new MerchantInfoModel();
		zhuopuxinMerchant = new MerchantInfoModel();
		zhuopuxinMerchant.setTopidealCode("1000000544");
		zhuopuxinMerchant= merchantInfoDao.searchByModel(zhuopuxinMerchant);
		if (zhuopuxinMerchant==null) {
			throw new RuntimeException("卓普信卓志编码1000000544没有找到商家数据");
		}
		// 获取宝丰商家
		MerchantInfoModel baofengMerchant = new MerchantInfoModel();
		baofengMerchant = new MerchantInfoModel();
		baofengMerchant.setTopidealCode("1000000536");
		baofengMerchant= merchantInfoDao.searchByModel(baofengMerchant);
		if (baofengMerchant==null) {
			throw new RuntimeException("宝丰卓志编码1000000536没有找到商家数据");
		}
		
		
		// 获取嘉宝和卓普信   宝丰外仓唯一码 (该商家启用状态标准条码存在的外仓唯一码)		
		/*List<Map<String, Object>> zpxAndJBCopyGoodsList = merchandiseInfoDao.getZPXAndJBCopyGoodsList();
		// 1.商家 启用状态 标准条码 外仓唯一码设置唯一索引
		Map<String, Object>merStatusComOutMap=new HashMap<>();
		for (Map<String, Object> map : zpxAndJBCopyGoodsList) {
			String merStatusComOut = (String) map.get("mer_status_com_out");// 商家 启用状态 标准条码 外仓唯一码
			merStatusComOutMap.put(merStatusComOut, merStatusComOut);
		}
		zpxAndJBCopyGoodsList=null;*/
				

		//3.查询品牌
		List<BrandModel> brandList = brandDao.list(new BrandModel());
		Map<Long, BrandModel>brandMap=new HashMap<>();
		for (BrandModel brand : brandList) {
			brandMap.put(brand.getId(), brand);
		}
		brandList=null;
		//4.查询分类
		List<MerchandiseCatModel> catList = merchandiseCatDao.list(new MerchandiseCatModel());
		Map<Long, MerchandiseCatModel>catMap=new HashMap<>();
		for (MerchandiseCatModel cat : catList) {
			catMap.put(cat.getId(), cat);
		}
		catList=null;
		
		int  num=0;
		// 获取所有非卓普信/嘉宝 /宝丰状态是启用的商品
		int startIndex = 0;
    	int pageSize = 1000;//每页1000
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("merchantId", merchantInfoModel.getId());
		paramMap.put("goodsId", goodsId);
		paramMap.put("pageSize", pageSize);
		
		
		while (true) {
			paramMap.put("startIndex",startIndex);
			List<MerchandiseInfoModel> goodslist = merchandiseInfoDao.getCopyGoodsList(paramMap);
			if (goodslist.size()==0) break;
			startIndex=startIndex+pageSize;
			
			for (MerchandiseInfoModel goods : goodslist) {
				Long merchantId = goods.getMerchantId();
				String topidealCode = merchantMap.get(merchantId);
				// 嘉宝 /宝丰/卓普新商品不做变更 
				if ("1000000594".equals(topidealCode)||"1000000438".equals(topidealCode)||"1000000536".equals(1000000536)) {
					continue;
				}
				if ("null".equals(goods.getCommbarcode())||StringUtils.isBlank(goods.getCommbarcode())) {
					continue;
				}
				//状态 1-启用,0-禁用 
				if (DERP_SYS.MERCHANDISEINFO_STATUS_0.equals(goods.getStatus())) {
					continue;
				}
				/*if (merStatusComOutMap.containsKey("1000051,1,08853301000031,0")) {
					System.out.println("1000051,1,08853301000031,0");
				}*/ 

				//品牌
				BrandModel brandModel=null;
				if (goods.getBrandId()!=null) {
					brandModel = brandMap.get(goods.getBrandId());
				}
				if (brandModel==null) brandModel=new BrandModel();
				//分类
				MerchandiseCatModel twoCat=null;       
				if (goods.getProductTypeId()!=null) {
					twoCat=catMap.get(goods.getProductTypeId()); 
				}
		    	if (twoCat==null)twoCat=new MerchandiseCatModel();
				
				
				//如果是宝信商家新增/修改嘉宝商品
				if ("0000138".equals(topidealCode)) {
					copyGoods(goods,jiabaoMerchant,brandModel,twoCat);
				}
				// 新增或修改卓普新商品
				copyGoods(goods, zhuopuxinMerchant,brandModel,twoCat);
				
				//新增或修改宝丰商品
				copyGoods(goods, baofengMerchant, brandModel, twoCat);	
				num=num+1;
				LOGGER.info("----------生成/修改卓普信和嘉宝商品第"+num+"条------------");
			}
		}
		
		
		
		
		
		LOGGER.info("----------生成/修改卓普信和嘉宝宝丰商品--------结束");
		
		
		
		
	}

	/**
	 * 新增或修改商品
	 * @param goods
	 * @param jiabaoMerchant
	 * @throws Exception 
	 */
	private void copyGoods(MerchandiseInfoModel goods, MerchantInfoModel newMerchant,
			BrandModel brandModel,MerchandiseCatModel twoCat) throws Exception {
		
		
		
		
		//新增商家品牌关系表
		if (goods!=null&&goods.getBrandId()!=null) {
			saveMerchantBrandRel(goods,newMerchant);
		}
		// 根据商家id和货号查询商品
		MerchandiseInfoModel goodsquery=new MerchandiseInfoModel();
		goodsquery.setMerchantId(newMerchant.getId());
		goodsquery.setGoodsNo(goods.getGoodsNo());
		goodsquery = merchandiseInfoDao.searchByModel(goodsquery);
		
		MerchandiseInfoModel newGoods= new MerchandiseInfoModel();
		/*1，BeanUtils是org.springframework.beans.BeanUtils， a拷贝到b
		2，BeanUtils是org.apache.commons.beanutils.BeanUtils，b拷贝到a*/
		BeanUtils.copyProperties(goods,  newGoods);
		newGoods.setMerchantId(newMerchant.getId());
		newGoods.setMerchantName(newMerchant.getName());		

		if (goodsquery==null) {//新增			
			if (isExistOutDepotMerchandise(newMerchant,newGoods.getBarcode())) {
				newGoods.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
			} else {
				newGoods.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}

			newGoods.setId(null);
			newGoods.setCode(SmurfsUtils.getID("ERP"));// 商品编码
			newGoods.setGoodsCode(SmurfsUtils.getID("ERP")); 
			newGoods.setModifyDate(null);
			newGoods.setCreateDate(TimeUtils.getNow());	
			newGoods.setSourceGoodsId(goods.getId());
			merchandiseInfoDao.save(newGoods);
			/*if (DERP_SYS.MERCHANDISEINFO_SOURCE_2.equals(newGoods.getSource())) {
				//推送主数据商品同步
				sendMaindata(newGoods,productInfo,newMerchant,twoCat);	
			}*/
			
		}else {// 修改
			newGoods.setOutDepotFlag(null);
			newGoods.setCode(null);
			newGoods.setGoodsCode(null);
			newGoods.setGoodsNo(null);
			newGoods.setCreateDate(null);
			newGoods.setSourceGoodsId(null);
			newGoods.setId(goodsquery.getId());
			newGoods.setBarcode(null);
			newGoods.setCommbarcode(null);
			newGoods.setModifyDate(TimeUtils.getNow());	
			merchandiseInfoDao.modify(newGoods);
			// 修改后从新赋值
			newGoods.setGoodsCode(goodsquery.getGoodsCode());// 从新赋值
			newGoods.setCode(goodsquery.getCode());// 从新赋值
			newGoods.setOutDepotFlag(goodsquery.getOutDepotFlag());// 从新赋值
			newGoods.setSourceGoodsId(goodsquery.getSourceGoodsId());// 从新赋值)
			newGoods.setGoodsNo(goodsquery.getGoodsNo());//从新赋值
			newGoods.setCommbarcode(goodsquery.getCommbarcode());
			
		}
		
		// 复制老的商品仓库数据到新的商品仓库数据		
		copyGoodsDepotRel(goods,newMerchant,newGoods);
		
		//生成附表
		//saveMerchandiseScheduleModel(goods, newGoods, newMerchant);
		// 修改 标准条码
		saveCommBarbarcode(newGoods, brandModel, twoCat);
		brandModel=null;
		twoCat=null;	
		newGoods=null;
		goodsquery=null;
	}
	/**
	 * 复制老的商品仓库数据到新的商品仓库数据
	 * @param goods
	 * @throws SQLException 
	 */
	private void copyGoodsDepotRel(MerchandiseInfoModel goods,MerchantInfoModel newMerchant,MerchandiseInfoModel newGoods) throws SQLException {
		MerchandiseDepotRelModel relQuey=new MerchandiseDepotRelModel();
		relQuey.setGoodsId(goods.getId());		
		List<MerchandiseDepotRelModel> list = merchandiseDepotRelDao.list(relQuey);
		if (list.size()==0)return;
		for (MerchandiseDepotRelModel saveModel : list) {
			if (saveModel.getDepotId()==null||newGoods.getId()==null)continue;
			// 判断是否存在商家仓库 如果存在 就不新增
			MerchandiseDepotRelModel relNewQuey=new MerchandiseDepotRelModel();
			relNewQuey.setGoodsId(newGoods.getId());
			relNewQuey.setDepotId(saveModel.getDepotId());
			List<MerchandiseDepotRelModel> relNewQueyList = merchandiseDepotRelDao.list(relNewQuey);
			if (relNewQueyList.size()>0) continue;
			//查询商家仓库关系 如果商家仓库关系没有维护不新增
			DepotMerchantRelModel depotMerchantRel=new DepotMerchantRelModel();
			depotMerchantRel.setMerchantId(newMerchant.getId());
			depotMerchantRel.setDepotId(saveModel.getDepotId());
			List<DepotMerchantRelModel> depotMerchantRelList = depotMerchantRelDao.list(depotMerchantRel);
			if (depotMerchantRelList.size()==0) continue;
			// 新增商品仓库关系
			MerchandiseDepotRelModel relNewSave=new MerchandiseDepotRelModel();
			relNewSave.setMerchantId(newMerchant.getId());
			relNewSave.setGoodsId(newGoods.getId());
			relNewSave.setDepotId(saveModel.getDepotId());
			relNewSave.setDepotName(saveModel.getDepotName());
			relNewSave.setCreateDate(TimeUtils.getNow());
			merchandiseDepotRelDao.save(relNewSave);
		}
		
		
	};
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
		 List<MerchandiseInfoModel> list = merchandiseInfoDao.list(isExistOutDepotMerchandise);
		if (list == null||list.size()==0) {
			return false;
		} else {
			return true;
		}
	}
	
    private void saveCommBarbarcode(MerchandiseInfoModel newGoods , BrandModel brandModel ,MerchandiseCatModel merchandiseCat) throws SQLException {
    	//判断标准条码是否存在，若不存在，在标准条码管理新增一条未维护数据
    	CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
    	commbarcodeModel.setCommbarcode(newGoods.getCommbarcode());
    	if (StringUtils.isBlank(newGoods.getCommbarcode()))return;
    	List<CommbarcodeModel> commbarcodeList= commbarcodeDao.list(commbarcodeModel);
    	
    	Long commbarcodeId = null ;    	
    	//不存在新增一条记录
    	if(commbarcodeList == null ||commbarcodeList.size()==0) {
    		commbarcodeModel = new CommbarcodeModel() ;
    		commbarcodeModel.setCommbarcode(newGoods.getCommbarcode());
    		commbarcodeModel.setCommGoodsName(newGoods.getName());
    		commbarcodeModel.setCommTypeId(merchandiseCat.getId());
    		commbarcodeModel.setCommTypeName(merchandiseCat.getName());
    		commbarcodeModel.setMaintainStatus("0");
    		commbarcodeModel.setCreateDate(TimeUtils.getNow());
    		commbarcodeModel.setModifyDate(TimeUtils.getNow());    		
    		commbarcodeId = commbarcodeDao.save(commbarcodeModel);
    		//新增表体
    		if(commbarcodeId != null) {
    			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(newGoods.getName());
    			commbarcodeItemModel.setGoodsCode(newGoods.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(newGoods.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(newGoods.getId());
    			commbarcodeItemModel.setBarcode(newGoods.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCat.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCat.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
    			commbarcodeItemModel.setOutDepotFlag(newGoods.getOutDepotFlag());
    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}
    	}
    	//存在修改表体内容
    	else {
    		commbarcodeId = commbarcodeModel.getId() ;
    		//根据标准条码ID、商品货号查表体是否存在
    		CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    		commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    		commbarcodeItemModel.setGoodsId(newGoods.getId());
    		
    		List<CommbarcodeItemModel> itemList = commbarcodeItemDao.list(commbarcodeItemModel) ;
    		
    		if(itemList == null||itemList.size()==0) {
    			commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(newGoods.getName());
    			commbarcodeItemModel.setGoodsCode(newGoods.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(newGoods.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(newGoods.getId());
    			commbarcodeItemModel.setBarcode(newGoods.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCat.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCat.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(newGoods.getOutDepotFlag());
    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}else {
    			CommbarcodeItemModel itemModel = itemList.get(0);
    			commbarcodeItemModel.setId(itemModel.getId());
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(newGoods.getName());
    			commbarcodeItemModel.setGoodsCode(newGoods.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(newGoods.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(newGoods.getId());
    			commbarcodeItemModel.setBarcode(newGoods.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCat.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCat.getName());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(newGoods.getOutDepotFlag());
    			commbarcodeItemDao.modify(commbarcodeItemModel) ;
    		}
    	}
    	commbarcodeModel=null;
    }

	
	
	/*
    * 生成/修改商品附表
    * @param jsonData
    * @throws SQLException 
    */
/*	private void saveMerchandiseScheduleModel(MerchandiseInfoModel goods,MerchandiseInfoModel newGoods,
   		MerchantInfoModel newMerchant) throws SQLException {
   	 
	   //根据唯一标识 查询商品附表       原商家的附表  
       MerchandiseScheduleModel mScheduleModel = new MerchandiseScheduleModel();
       mScheduleModel.setUniques(goods.getUniques());
       mScheduleModel.setGoodsId(goods.getId());
       mScheduleModel.setMerchantId(goods.getMerchantId());
       List<MerchandiseScheduleModel> mScheduleModelList= merchandiseScheduleDao.list(mScheduleModel);
       if (mScheduleModelList==null||mScheduleModelList.size()==0)return;   
       mScheduleModel= mScheduleModelList.get(0);

       
       
       for (MerchandiseScheduleModel model : mScheduleModelList) {
		   MerchandiseScheduleModel mScheduleQuery = new MerchandiseScheduleModel();
		   mScheduleQuery.setUniques(model.getUniques());//商品唯一标识
		   mScheduleQuery.setGoodsId(newGoods.getId());
		   mScheduleQuery.setMerchantId(newMerchant.getId());
		   List<MerchandiseScheduleModel> list = merchandiseScheduleDao.list(mScheduleQuery);// 新商家附表		   
		   MerchandiseScheduleModel saveScheduleModel = new MerchandiseScheduleModel();
		   saveScheduleModel.setUniques(model.getUniques());
		   saveScheduleModel.setGoodsId(newGoods.getId());
		   saveScheduleModel.setMerchantId(newMerchant.getId());       	
		   saveScheduleModel.setGoodsNo(newGoods.getGoodsNo());
		   if (list==null||list.size()==0) {// 新增			   
		       merchandiseScheduleDao.save(saveScheduleModel);					
			}else{//修改
				saveScheduleModel.setId(list.get(0).getId());
				saveScheduleModel.setModifyDate(TimeUtils.getNow());
		       	merchandiseScheduleDao.modify(saveScheduleModel);
			}		   
	   }

	}*/
	
	/**
	 * 新增商家品牌关系表
	 * @param productInfo
	 * @throws SQLException 
	 */
	private void saveMerchantBrandRel(MerchandiseInfoModel goods,MerchantInfoModel newMerchant) throws SQLException {
		 MerchantBrandRelModel merchantBrandRelModel = new  MerchantBrandRelModel();
		 merchantBrandRelModel.setBrandId(goods.getBrandId());// 品牌id
	     merchantBrandRelModel.setMerchantId(newMerchant.getId());// 商家id
	     List<MerchantBrandRelModel> list = merchantBrandRelDao.list(merchantBrandRelModel);
		 if (list==null||list.size()==0) {// 新增商家品牌关系表
			 MerchantBrandRelModel merBrandRelModel = new  MerchantBrandRelModel();
	         merBrandRelModel.setBrandId(goods.getBrandId());
	         merBrandRelModel.setMerchantId(newMerchant.getId());// 商家
	         merchantBrandRelDao.save(merBrandRelModel);	
		 }	
		
	}

	


}
