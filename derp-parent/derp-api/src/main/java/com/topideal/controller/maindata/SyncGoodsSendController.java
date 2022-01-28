package com.topideal.controller.maindata;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.maindata.SyncGoodsSendService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONObject;


/**
 * （主数据）产品商品信息下发接口(主数新系统)
 * @author 杨创
 *2020/08/04
 */
@Controller
@RequestMapping("/derpapi/1103")
public class SyncGoodsSendController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncGoodsSendController.class);
	@Autowired
	private SyncGoodsSendService syncGoodsSendService;




	
	/**
	 * 产品商品信息下发接口
	 * @param json (主数据传的是单个产品的json信息)
	 * @return
	 */
	@RequestMapping(params="method=erp5105",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point="12102001001",model="产品商品信息下发接口",keyword = "code")
    public Map getGoodsSend(@RequestBody String json){
		try {
			//json对象
			JSONObject jsonData = JSONObject.fromObject(json);
			//商家编码(对应为经分销公司卓志编码)
			if (jsonData.get("merchantCode")==null||StringUtils.isBlank(jsonData.getString("merchantCode"))||"null".equals(jsonData.getString("merchantCode"))) {
				return ResponseFactory.syncError("merchantCode is NULL");
			}
			// 商家编码不能为嘉宝和卓普新
			if ("1000000594".equals(jsonData.getString("merchantCode"))||"1000000544".equals(jsonData.getString("merchantCode"))) {
				return ResponseFactory.syncError("家编码不能为 嘉宝贸易有限公司(1000000594)/香港卓普信贸易有限公司(1000000544)");
			}
			// 判断经分销商家是否存在
			MerchantInfoModel merchantInfo=new MerchantInfoModel();
			merchantInfo.setTopidealCode(jsonData.getString("merchantCode"));
			merchantInfo = syncGoodsSendService.isExitMerchant(merchantInfo);
			// 非经分销的商家不记录在日志监控
			if (merchantInfo==null) {
				return ResponseFactory.syncSuccess();
			}
			
			//主数据商品编码 (原OPG号，现MDM编码)
			if (jsonData.get("code")==null||StringUtils.isBlank(jsonData.getString("code"))||"null".equals(jsonData.getString("code"))) {
				return ResponseFactory.syncError("code is NULL");
			}	
			//来源系统编码
			if (jsonData.get("subscribeCode")==null||StringUtils.isBlank(jsonData.getString("subscribeCode"))||"null".equals(jsonData.getString("subscribeCode"))) {
				return ResponseFactory.syncError("subscribeCode is NULL");
			}
			
			//版本号
			if (jsonData.get("version")==null||StringUtils.isBlank(jsonData.getString("version"))||"null".equals(jsonData.getString("version"))) {
				return ResponseFactory.syncError( "version is NULL");
			}
			
			
			// 商品编码(对应为经分销商品货号)
			if (jsonData.get("goodsCode")==null||StringUtils.isBlank(jsonData.getString("goodsCode"))||"null".equals(jsonData.getString("goodsCode"))) {
				return ResponseFactory.syncError("goodsCode is NULL");
			}
			
			//商品名称(对应位经分销商品名称)
			if (jsonData.get("goodsName")==null||StringUtils.isBlank(jsonData.getString("goodsName"))||"null".equals(jsonData.getString("goodsName"))) {
				return ResponseFactory.syncError("goodsName is NULL");
			}
			//商品条形码 (对应为经分销商品条形码)
			if (jsonData.get("barCode")==null||StringUtils.isBlank(jsonData.getString("barCode"))||"null".equals(jsonData.getString("barCode"))) {
				return ResponseFactory.syncError("barCode is NULL");
			}
			//产品编码 (对应为经分销标准条码)
			if (jsonData.get("productCode")==null||StringUtils.isBlank(jsonData.getString("productCode"))||"null".equals(jsonData.getString("productCode"))) {
				return ResponseFactory.syncError("productCode is NULL");
			}

			syncGoodsSendService.synsMerchantGoods(jsonData.toString());
		}catch(Exception e){
			LOGGER.error("产品商品信息下发接口异常,错误信息:{}",e.getMessage());
			return ResponseFactory.syncError(e.getMessage());
		}
		return ResponseFactory.syncSuccess();
	}

}
