package com.topideal.service.impl;

import com.supplychain.gateway.api.GatewayResponse;
import com.topideal.api.dstp.DSTPUtil;
import com.topideal.api.dstp.d01.GoodsItem;
import com.topideal.api.dstp.d01.GoodsDstp;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.CountryOriginMongoDao;
import com.topideal.mongo.entity.CountryOriginMongo;
import com.topideal.service.DSTPSynGoodsService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DSTPSynGoodsServiceImpl implements DSTPSynGoodsService {
	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;
	
	/* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(DSTPSynGoodsServiceImpl.class);
    

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_20600000002, model = DERP_LOG_POINT.POINT_20600000002_Label)
	public void synGoosDSTP(String json, String keys, String topics, String tags) throws Exception{
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchantId");//商家id
		String barcode = (String) jsonMap.get("barcode");//条码
		String startTime = (String) jsonMap.get("startTime");//开始时间
		String endTime = (String) jsonMap.get("endTime");//结束时间

		/**1.计算同步时间 未指定时间则默认为昨天*/
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
			String yesterday = TimeUtils.getYesterday();
			startTime = yesterday+" 00:00:00";
			endTime = yesterday+" 23:59:59";
		}

		/**2.获取原产国*/
		List<CountryOriginMongo> countryOriginList = countryOriginMongoDao.findAll(new HashMap<>());
		Map<String,String> countryOriginMap = new HashMap<>();//key=id，value=名称
		for(CountryOriginMongo country : countryOriginList){
			countryOriginMap.put(country.getCountryOriginId().toString(),country.getName());
		}
		countryOriginList = null;//释放内存

         /**查询公司 只同步宝信、健太、润佰、卓烨*/
		MerchantInfoModel queryModel = new MerchantInfoModel();
		if(merchantId!=null){
			queryModel.setId(Long.valueOf(merchantId));
		}
		List<MerchantInfoModel> merchantList = merchantInfoDao.listDstp(queryModel);
		for(MerchantInfoModel merchant : merchantList){
            //分页查询昨天新增和修改的条码
			Integer startIndex = 0;
			Integer pageSize = 100;
			while(true){
                Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("merchantId",merchant.getId());
				paramMap.put("startIndex",startIndex);
				paramMap.put("pageSize",pageSize);
				if(StringUtils.isNotBlank(barcode)){
					paramMap.put("barcode",barcode);
				}else{
					paramMap.put("startTime",startTime);
					paramMap.put("endTime",endTime);
				}

				List<String> barcodeList = merchandiseInfoDao.getBarcodeListDSTP(paramMap);
				if(barcodeList==null||barcodeList.size()<=0) {
					logger.info("新增/修改的条码数量为0，结束");
					break;
				}
				//查询商品信息
				paramMap.put("barcodeList",barcodeList);
				List<Map<String,Object>> merchandiseList = merchandiseInfoDao.getDetailList(paramMap);
				//封装实体
				List<GoodsDstp> goodsRootList= createEntity(countryOriginMap,merchant,merchandiseList);
				//推送商品到DSTP
				for(GoodsDstp goodsRoot:goodsRootList){
					JSONObject jsonObj = JSONObject.fromObject(goodsRoot);
					GatewayResponse response = DSTPUtil.sendGoodsDSTP(jsonObj.toString(),merchant.getTopidealCode());
					if(response==null||!response.getFlag().equals("success")){
						logger.info("商品同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" barcode="+goodsRoot.getBarCode());
						throw new RuntimeException("商品同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" barcode="+goodsRoot.getBarCode());
					}
				}
				startIndex += pageSize;
			}

		}


	}

	/**封装报文
	 * */
	private List<GoodsDstp> createEntity(Map<String,String> countryOriginMap, MerchantInfoModel merchant, List<Map<String,Object>> merchandiseList){
      Map<String, GoodsDstp> goodsMap = new HashMap<>();//存储商品表头 去重 key=barcode value=GoodsRoot
      Map<String,List<GoodsItem>> GoodsItemMap = new HashMap<>();//存储商品表体 key=barcode value=List<GoodsItem>
	  List<String> goodsNoList = new ArrayList<>();//用于货号去重
      for(Map merchandiseMap : merchandiseList){
		  String barcode = (String)merchandiseMap.get("barcode");
		  String goodsNo = (String)merchandiseMap.get("goods_no");
		  BigDecimal filingPrice = (BigDecimal)merchandiseMap.get("filing_price");
		  if(filingPrice==null) filingPrice = new BigDecimal(0);
		  String countryName = countryOriginMap.get(String.valueOf(merchandiseMap.get("county_id")));

		  GoodsDstp goodsRoot = goodsMap.get(barcode);
		  if(goodsRoot==null){
			  List<String> picUrl = new ArrayList<>();
			  if(merchandiseMap.get("image_url1")!=null) picUrl.add((String)merchandiseMap.get("image_url1"));
			  if(merchandiseMap.get("image_url2")!=null) picUrl.add((String)merchandiseMap.get("image_url2"));
			  if(merchandiseMap.get("image_url3")!=null) picUrl.add((String)merchandiseMap.get("image_url3"));
			  if(merchandiseMap.get("image_url4")!=null) picUrl.add((String)merchandiseMap.get("image_url4"));
			  if(merchandiseMap.get("image_url5")!=null) picUrl.add((String)merchandiseMap.get("image_url5"));
			  String countyName = (String)countryOriginMap.get(String.valueOf(merchandiseMap.get("county_id")));//原产国

			  goodsRoot = new GoodsDstp();
			  goodsRoot.setCustomerCode(merchant.getRegistrationCert());
			  goodsRoot.setBarCode(barcode);
			  goodsRoot.setGoodsName((String)merchandiseMap.get("name"));
			  goodsRoot.setBrand((String)merchandiseMap.get("band"));
			  goodsRoot.setBrandEn((String)merchandiseMap.get("english_name"));
			  String spec = (String)merchandiseMap.get("spec");
			  if(StringUtils.isNotBlank(spec)&&spec.length()>64) spec = spec.substring(0,64);
			  goodsRoot.setSpecification(spec);//规格型号
			  goodsRoot.setOrgCountry(countryName);//原产国
			  goodsRoot.setProduceCompany((String)merchandiseMap.get("manufacturer"));//生产厂家
			  goodsRoot.setFirstLegalUnit((String)merchandiseMap.get("unit_name_one"));//第一法定单位
			  goodsRoot.setSecondLegalUnit((String)merchandiseMap.get("unit_name_two"));//第二法定单位
			  String grossWeight = (String) merchandiseMap.get("gross_weight");
			  String netWeight = (String) merchandiseMap.get("net_weight");
			  if(StringUtils.isBlank(grossWeight)) grossWeight = "0.0";
			  if(StringUtils.isBlank(netWeight)) netWeight = "0.0";
			  goodsRoot.setGrossWeight(grossWeight);//毛重
			  goodsRoot.setNetWeight(netWeight);//净重
			  goodsRoot.setPicUrl(picUrl);//图片
			  goodsMap.put(barcode,goodsRoot);
		  }

		  if(goodsNoList.contains(goodsNo)) continue;//去重
		  List<GoodsItem> itemList = GoodsItemMap.get(barcode);
		  if(itemList==null) itemList = new ArrayList<>();
		  GoodsItem item = new GoodsItem();
		  item.setEntGoodsNo(goodsNo);
		  item.setRecordPrice(filingPrice.toString());
		  itemList.add(item);
		  GoodsItemMap.put(barcode,itemList);
	  }

      //合并表体
	  List<GoodsDstp> goodsRootList = new ArrayList<>();
	  for(GoodsDstp goodsRoot:goodsMap.values()){
		  List<GoodsItem> itemList = GoodsItemMap.get(goodsRoot.getBarCode());
		  goodsRoot.setRecordInfo(itemList);
		  goodsRootList.add(goodsRoot);
	  }

      return goodsRootList;
	}

}
