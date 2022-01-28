package com.topideal.controller.maindata;

import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.code.OPCodeEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.service.maindata.SynsGoodsInfoService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONObject;


/**
 * （主数据）产品商品信息下发接口(主数据老系统)
 * @author 杨创
 *2018/4/23
 */
@Controller
@RequestMapping("/derpapi/1101")
public class SyncGoodsInfoController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncGoodsInfoController.class);
	@Autowired
	private SynsGoodsInfoService synsGoodsInfoService;



	
	/**
	 * 产品商品信息下发接口
	 * @param json (主数据传的是单个产品的json信息)
	 * @return
	 */
	@RequestMapping(params="method=erp5103",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point= DERP_LOG_POINT.POINT_12102001000,model=DERP_LOG_POINT.POINT_12102001000_Label,keyword = "opgcode")
    public Map getMainMerchandiseInfo(@RequestBody String json){
		try {
			//json对象
			JSONObject jsonData = JSONObject.fromObject(json);
			//商品id校验
			if (jsonData.get("opgcode")==null||StringUtils.isBlank(jsonData.getString("opgcode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "opgcode is NULL");
			}
			// 商品编码校验
			if (jsonData.get("gcode")==null||StringUtils.isBlank(jsonData.getString("gcode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "gcode is NULL");
			}
			//客户ID校验
			if (jsonData.get("ccode")==null||StringUtils.isBlank(jsonData.getString("ccode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "ccode is NULL");
			}
			if ("1000000594".equals(jsonData.getString("ccode"))||"1000000544".equals(jsonData.getString("ccode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "家编码不能为 嘉宝贸易有限公司(1000000594)/香港卓普信贸易有限公司(1000000544)");
			}
			
			//商品名称校验
			if (jsonData.get("gName")==null||StringUtils.isBlank(jsonData.getString("gName"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "gName is NULL");
			}
			//商品条形码校验
			if (jsonData.get("goodsbarcode")==null||StringUtils.isBlank(jsonData.getString("goodsbarcode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "goodsbarcode is NULL");
			}
			//产品条形码校验
			if (jsonData.get("barcode")==null||StringUtils.isBlank(jsonData.getString("barcode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "barcode is NULL");
			}
			/**if(!jsonData.getString("barcode").equals(jsonData.getString("goodsbarcode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "barcode 和 goodsbarcode 不同");
			}*/
			synsGoodsInfoService.synsMerchantGoods(jsonData.toString());
			//	rocketMQProducer.send(jsonData.toString(), MQEnum.OP_RECEIVE_GOODS_INFO);
		}catch(Exception e){
			LOGGER.error("主数据同步商品信息异常,错误信息:{}",e.getMessage());
			return ResponseFactory.error(OPCodeEnum.ERROR_902, e.getMessage());
		}
		return ResponseFactory.success(OPCodeEnum.SUCCESS);
	}

}
