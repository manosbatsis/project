package com.topideal.api.finance;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.finance.f2_01.FinanceApplyItemRequest;
import com.topideal.api.finance.f2_01.FinanceApplyRequest;
import com.topideal.api.finance.f2_02.PayBackRedeemApplyItemRequest;
import com.topideal.api.finance.f2_02.PayBackRedeemItemRequest;
import com.topideal.api.finance.f2_02.PayBackRedeemRequest;
import com.topideal.api.finance.f2_03.PayBackTrialBillRequest;
import com.topideal.api.finance.f2_03.PayBackTrialGoodRequest;
import com.topideal.api.finance.f2_03.PayBackTrialRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpClientUtil;
import com.topideal.tools.EpassSignUtils;

import net.sf.json.JSONObject;

/**
 * 金融接口工具类
 * @author 杨创
 */
public class FinanceUtils {
	
	
	private static final Logger LOGGER= LoggerFactory.getLogger(FinanceUtils.class);
	
	//金融api地址
    private static final String FINANCE_API=ApolloUtil.financeApi;
    //金融秘钥
    private static final String FINANCE_APP_SECRET=ApolloUtil.financeAppsecret;
    //金融appkey
    private static final String FINANCE_APP_KEY=ApolloUtil.financeAppkey;
    //金融版本
    private static final String FINANCE_V=ApolloUtil.financeV;
    //金融商品分类查询接口
    private static final String FINANCE_001_METHOD=ApolloUtil.finance001Method;   
    //金融商品分类查询接口
    private static final String FINANCE_002_METHOD=ApolloUtil.finance002Method;
    
	//金融融资接口api地址
    private static final String FINANCE_API2=ApolloUtil.financeApi2;
    //金融秘钥
    private static final String FINANCE_APP_SECRET2=ApolloUtil.financeAppsecret2;
    //金融appkey
    private static final String FINANCE_APP_KEY2=ApolloUtil.financeAppkey2;
    //金融版本
    private static final String FINANCE_V2=ApolloUtil.financeV2;
    
	//金融同步商品同步接口api地址
    private static final String FINANCE_API3=ApolloUtil.financeApi3;
    
    private static final String Fa=ApolloUtil.finance001Method3;
    private static final String FB=ApolloUtil.finance003Method2;


	public static void setFinanceBaseInfo(JSONObject jsonObject) {
		jsonObject.put("v",FINANCE_V);
		jsonObject.put("appKey", FINANCE_APP_KEY);// appkey	
		jsonObject.put("requestTime", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		jsonObject.put("timesTamp", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		
		String json=jsonObject.toString();
		try {
			 json= URLDecoder.decode(json,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sign = EpassSignUtils.sign(json, FINANCE_APP_SECRET);
		jsonObject.put("sign", sign);
	}
	
	/**
	 * 2.3商品分类查询接口
	 * @param jsonObject
	 * @return
	 */
	public static String goodsClassificationRequest(JSONObject jsonObject){
		jsonObject.put("method",FINANCE_001_METHOD);
		// 设置头部信息
		setFinanceBaseInfo(jsonObject);
		LOGGER.info("金融商品分类查询接口请求", jsonObject);
		System.out.println("请求报文"+jsonObject);
		// 获取sign
		String result = HttpClientUtil.doPost(FINANCE_API, jsonObject.toString(), "utf-8");
		LOGGER.info("金融商品分类查询接口响应"+result);
		return result;		
	}
	
	
	
	/**
	 * 商品同步接口（通用库）
	 * @param jsonObject
	 * @return
	 */
	public static String syncGoodsPush(JSONObject jsonObject){
		jsonObject.put("method",FINANCE_002_METHOD);
		LOGGER.info("商品同步接口（通用库）请求method:"+ FINANCE_002_METHOD);
		// 设置头部信息
		setFinanceBaseInfo(jsonObject);
		LOGGER.info("商品同步接口（通用库）请求:"+ jsonObject);
		LOGGER.info("商品同步接口（通用库）请求FINANCE_API:"+FINANCE_API);
		// 获取sign
		String result = HttpClientUtil.doPost(FINANCE_API, jsonObject.toString(), "utf-8");
		LOGGER.info("商品同步接口（通用库）响应"+result);
		
		if (StringUtils.isBlank(result)) {
			for (int i = 0; i < 3; i++) {
				result = HttpClientUtil.doPost(FINANCE_API, jsonObject.toString(), "utf-8");
				LOGGER.info("商品同步接口（通用库）响应"+result);
				if (StringUtils.isNotBlank(result)) {
					return result;
				}
			}
		}
		
		return result;		
	}
	
	
	//-----------------------------金融融资相关接口-------------------------------
	
	//金融融资签名
	public static void setFinanceBaseInfo2(JSONObject jsonObject) {
		jsonObject.put("v",FINANCE_V2);
		jsonObject.put("appKey", FINANCE_APP_KEY2);// appkey	
		jsonObject.put("requestTime", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		jsonObject.put("timesTamp", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));				
		String json=jsonObject.toString();
		//**处理% 特殊字符*/
		json = json.replaceAll("%(?![0-9]{2})", "%25") ;
		json = json.replaceAll("\\+", "%2B") ;
		try {
			 json= URLDecoder.decode(json,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sign = EpassSignUtils.sign(json, FINANCE_APP_SECRET2);
		jsonObject.put("sign", sign);
	}
	
	
	/**
	 * 调用金融融资接口
	 * @param jsonObject
	 * @return
	 */
	public static String getFiance(JSONObject jsonObject){
		//jsonObject.put("method",FINANCE_002_METHOD);
		//LOGGER.info("商品同步接口（通用库）请求method:"+ FINANCE_002_METHOD);
		// 设置头部信息
		setFinanceBaseInfo2(jsonObject);
		String jsonObjectStr = jsonObject.toString();
		LOGGER.info("请求:调用金融融资接口"+ jsonObjectStr);
		LOGGER.info("调用金融融资接口请求FINANCE_API2:"+FINANCE_API2);
		// 获取sign
		
		String result = HttpClientUtil.doPost(FINANCE_API2, jsonObjectStr, "utf-8");
		LOGGER.info("调用金融融资接口 响应:"+result);
		return result;		
	}
	
	/**
	 * 金融同步商品接口(签名和秘钥和融资一样 公用签名方法)
	 * @param jsonObject
	 * @return
	 */
	public static String getFianceSysGoods(JSONObject jsonObject){

		// 设置头部信息
		setFinanceBaseInfo2(jsonObject);
		String jsonObjectStr = jsonObject.toString();

		LOGGER.info("请求:调用金融同步商品接口:报文"+ jsonObjectStr);
		LOGGER.info("调用金融同步商品请求FINANCE_API3:"+FINANCE_API3);
		// 获取sign
		
		String result = HttpClientUtil.doPost(FINANCE_API3, jsonObjectStr, "utf-8");
		LOGGER.info("调用金融同步商品 响应:"+result);
		return result;		
	}
	
	
	
	public static void main(String[] args) {
		/*GoodsClassificationRequest GoodsRequest= new GoodsClassificationRequest();
		GoodsRequest.setRequestTime(TimeUtils.formatFullTime());
		List<GoodsClassificationItemRequest>goodsList=new ArrayList<GoodsClassificationItemRequest>();
		GoodsClassificationItemRequest goodsItemRequest =new GoodsClassificationItemRequest();
		goodsItemRequest.setGoodsBarcode("8853301000031");
		goodsList.add(goodsItemRequest);
		GoodsClassificationItemRequest goodsItemRequest1 =new GoodsClassificationItemRequest();

		goodsItemRequest1.setGoodsBarcode("9310022866401");
		goodsList.add(goodsItemRequest1);
		GoodsRequest.setGoodsList(goodsList);
		JSONObject requestJson = JSONObject.fromObject(GoodsRequest);
		String goodsClassificationRequest = goodsClassificationRequest(requestJson);
		System.out.println(goodsClassificationRequest);*/
		//企业还款申请
		FinanceApplyRequest applyRequest=new FinanceApplyRequest();
		applyRequest.setApplyNo("XS000002");
		applyRequest.setBorrower("201808061115");
		applyRequest.setSourceCode("10");
		applyRequest.setTenant("JFX");
		applyRequest.setCapital("健太");		
		applyRequest.setAmount(new BigDecimal("10000.00"));
		applyRequest.setBillCurrency("CNY");
		applyRequest.setApplyTime("2020-04-12 08:12:50");
		applyRequest.setTotalQty(100);
		applyRequest.setSupplier("JFXGYS01");
		
		applyRequest.setWarehouseId("WMS_360_04");
		applyRequest.setAddType("10");
		applyRequest.setInterestCurrency("CNY");
		applyRequest.setProduct("XJDC");
		applyRequest.setPaymentType("offline");
		applyRequest.setPaymentStatus("1");
		applyRequest.setPaymentDate("2021-03-15");
		applyRequest.setActualMargin(new BigDecimal("2"));

		List<FinanceApplyItemRequest>itemApplyRequestList=new ArrayList<>();
		FinanceApplyItemRequest itemApplyRequest=new FinanceApplyItemRequest();
		itemApplyRequest.setGoodsNo("Sku4545421");
		itemApplyRequest.setGoodsName("测试商品1");
		itemApplyRequest.setNum(50);
		itemApplyRequest.setUnitPrice(new BigDecimal("100"));
		itemApplyRequestList.add(itemApplyRequest);
		applyRequest.setCustentrystoreList(itemApplyRequestList);
		itemApplyRequest=new FinanceApplyItemRequest();
		itemApplyRequest.setGoodsNo("Sku4545422");
		itemApplyRequest.setGoodsName("测试商品2");
		itemApplyRequest.setNum(50);
		itemApplyRequest.setUnitPrice(new BigDecimal("100"));
		itemApplyRequestList.add(itemApplyRequest);
		applyRequest.setCustentrystoreList(itemApplyRequestList);
		
		/*JSONObject fromObject = JSONObject.fromObject(applyRequest);
		fromObject.put("method", "financing.apply.info.get");
		String fiance = FinanceUtils.getFiance(fromObject);
		System.out.println(fiance);*/
		
		
		// 企业还款赎回
		PayBackRedeemRequest payBack=new PayBackRedeemRequest();
		payBack.setApplyNo("XS000002");
		payBack.setApplyDate("2021-04-12");
		payBack.setRefundDate("2021-04-15");
		//payBack.setApplyAmount(new BigDecimal("13"));
		payBack.setBorrower("201808061115");
		payBack.setInterestCurrency("CNY");
		payBack.setRepaymentType("offline");
		//payBack.setActualDate("2021-04-11");
		payBack.setOnCredit("0");
		payBack.setStatus("10");
		List<PayBackRedeemItemRequest> goodList=new ArrayList<PayBackRedeemItemRequest>();
		PayBackRedeemItemRequest itemRequest=new PayBackRedeemItemRequest();
		itemRequest.setGoodsName("测试商品1");
		itemRequest.setGoodsNo("Sku4545421");
		itemRequest.setQty(50);
		itemRequest.setProcurementNo("XS000002");
		goodList.add(itemRequest);
		itemRequest=new PayBackRedeemItemRequest();
		itemRequest.setGoodsName("测试商品2");
		itemRequest.setGoodsNo("Sku4545422");
		itemRequest.setQty(50);
		itemRequest.setProcurementNo("XS000002");
		goodList.add(itemRequest);
		List<PayBackRedeemApplyItemRequest>applyList=new ArrayList<>();

		payBack.setGoodList(goodList);
		payBack.setApplyList(applyList);
	/*	JSONObject fromObject = JSONObject.fromObject(payBack);
		fromObject.put("method", "repayment.apply.info.get");
		String fiance = FinanceUtils.getFiance(fromObject);
		System.out.println(fiance);*/
		
		
		// 还款试算
		PayBackTrialRequest tialRequest=new PayBackTrialRequest();
		tialRequest.setRefundDate("2021-04-15");
		tialRequest.setBorrower("201808061115");
		tialRequest.setInterestCurrency("CNY");
		List<PayBackTrialGoodRequest>triaGoodsList=new ArrayList<>();
		PayBackTrialGoodRequest goods=new PayBackTrialGoodRequest();
		goods.setGoodsNo("Sku4545421");
		goods.setGoodsName("测试商品1");
		goods.setQty(50);
		goods.setProcurementNo("XS000002");
		triaGoodsList.add(goods);
		goods=new PayBackTrialGoodRequest();
		goods.setGoodsNo("Sku4545422");
		goods.setGoodsName("测试商品2");
		goods.setQty(10);
		goods.setProcurementNo("XS000002");
		triaGoodsList.add(goods);
		
		
		List<PayBackTrialBillRequest>billList=new ArrayList<>();
		/*PayBackTrialBillListRequest bill=new PayBackTrialBillListRequest();
		bill.setGoodsNo("1372010");
		bill.setGoodsName("WHISKAS 伟嘉猫粮 熟龄猫鲭鱼干贝口味 1.1kg");
		bill.setQty(2);
		bill.setProcurementNo("000232");
		billList.add(bill);*/
		tialRequest.setGoodList(triaGoodsList);

		/*JSONObject fromObject = JSONObject.fromObject(tialRequest);
		fromObject.put("method", "redemption.cost.count.get");
		String fiance = FinanceUtils.getFiance(fromObject);
		System.out.println(fiance);*/
		
		
		
		
		
	}
	
}
