package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.sale.PlatformPurchaseOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.ResultDTO;
import com.topideal.order.webapi.sale.form.PlatformPurchaseOrderForm;
import com.topideal.order.webapi.sale.form.SaleOrderAddForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * webapi 平台采购单
 *
 */
@RequestMapping("/webapi/order/platformPurchaseOrder")
@RestController
@Api(tags = "平台采购单")
public class APIPlatformPurchaseOrderController {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIPlatformPurchaseOrderController.class);

	private static final String[] MAIN_COLUMNS = {"公司", "PO号","PR号", "客户", "下单时间", "入库时间", "币种", "采购总金额","客户仓库", "平台状态",
			"单据状态", "提交人", "转销人", "转销时间","平台货号", "条形码", "商品名称", "商品数量", "入库好品量", "入库坏品量" ,"单价", "金额"} ;

	private static final String[] MAIN_KEYS = {"merchantName", "poNo","prNo", "customerName", "orderTime", "deliverDate", "currencyLabel",
			"amount","platformDepotName", "platformStatusLabel", "statusLabel", "submitName", "resaleName", "resaleDate",
			"platformGoodsNo","platformBarcode", "platformGoodsName", "itemNum", "receiptOkNum", "receiptBadNum", "price", "itemAmount"};

	@Autowired
	PlatformPurchaseOrderService platformPurchaseService ;

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PlatformPurchaseOrderDTO> toDetailsPage(String token,Long id){
		try {
			PlatformPurchaseOrderDTO dto = platformPurchaseService.searchDTOById(id) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);

		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询平台采购单列表信息")
   	@PostMapping(value="/listPlatformPurchaseOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PlatformPurchaseOrderDTO> listPlatformPurchaseOrder(PlatformPurchaseOrderForm form) {
		PlatformPurchaseOrderDTO dto = new PlatformPurchaseOrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());

			// 响应结果集
			dto = platformPurchaseService.listPlatformPurchaseOrder(dto);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "根据销售订单号和po号查询平台采购单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "code", value = "销售订单号(多个逗号隔开)", required = true),
		@ApiImplicitParam(name = "poNo", value = "PO号", required = true)
	})
   	@PostMapping(value="/listPlatformPurchaseOrderByCodeAndPoNo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<PlatformPurchaseOrderDTO>> listPlatformPurchaseOrderByCodeAndPoNo(String token,String code, String poNo) {
		List<PlatformPurchaseOrderDTO> list = null;
		try{
			PlatformPurchaseOrderDTO dto = new PlatformPurchaseOrderDTO();
			User user= ShiroUtils.getUserByToken(token);
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleCode(code);
			dto.setPoNo(poNo);
			// 响应结果集
			list = platformPurchaseService.listPlatformPurchaseOrderByCodeAndPoNo(dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
	}

	/**
	 * 检查能否转销售单
	 * */
	@ApiOperation(value = "检查能否转销售单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
   	@PostMapping(value="/checkOrderToSales.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> checkOrderToSales(String token,String ids) {
		ResultDTO resultDTO = new ResultDTO();
		try{
			Map<String, Object> resultMap = null ;
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
            List<Long> list = StrUtils.parseIds(ids);

            resultMap = platformPurchaseService.checkOrderToSales(list);

            resultDTO.setCode((String)resultMap.get("status"));
            resultDTO.setMessage((String)resultMap.get("msg"));
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}

	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportPlatformPurchaseOrder.asyn")
	private ResponseBean exportPlatformPurchaseOrder(HttpServletResponse response,
			HttpServletRequest request,PlatformPurchaseOrderForm form) throws Exception{
		try {
			PlatformPurchaseOrderExportDTO dto = new PlatformPurchaseOrderExportDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			List<PlatformPurchaseOrderExportDTO> exportList = platformPurchaseService.getExportList(dto) ;
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("平台采购订单", MAIN_COLUMNS, MAIN_KEYS, exportList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, "平台采购订单导出");

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}

	/**
	 * 弹框获取明细
	 */
	@ApiOperation(value = "弹框获取平台采购单明细")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
   	@PostMapping(value="/getPlatformPurchaseOrderItems.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getPlatformPurchaseOrderItems(String token,String ids) {
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
			List<Long> list = StrUtils.parseIds(ids);
			PlatformPurchaseOrderDTO dto = platformPurchaseService.getPlatformPurchaseOrderItems(list) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto) ;
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "转销售单获取销售货号")
	@PostMapping(value="/getSaleItems.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<List<PlatformPurchaseOrderItemDTO>> getSaleItems(@RequestBody PlatformPurchaseOrderForm form){
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			Map<Long, List<MerchandiseInfoMogo>> itemDTOList = platformPurchaseService.getSaleItems(form.getItemList(),form.getOutDepotId(),user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,itemDTOList) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "提交单据前校验转销售单商品")
	@PostMapping(value="/checkBeforeConfirm.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<Map<String,List<String>>> checkBeforeConfirm(@RequestBody PlatformPurchaseOrderForm form){
		try {
			Map<String,Object> resultMap = new HashMap<String, Object>();
			User user = ShiroUtils.getUserByToken(form.getToken());
			List<String> messageList = platformPurchaseService.checkBeforeConfirm(form.getItemList(),form.getOutDepotId(),form.getBuId() ,user) ;

			if(messageList.size() > 0){
				resultMap.put("code","01");
				resultMap.put("messageList", messageList);
			}else{
				resultMap.put("code","00");
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "平台采购单转销售单 保存")
	@PostMapping(value="/savePlatFormPurchaseToSales.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean savePlatFormPurchaseToSales(@RequestBody SaleOrderAddForm form){
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			SaleOrderDTO dto = new SaleOrderDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			platformPurchaseService.savePlatFormPurchaseToSales(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
