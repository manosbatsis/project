package com.topideal.order.webapi.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.order.service.purchase.PurchaseReturnService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseReturnOdepotAddForm;
import com.topideal.order.webapi.purchase.form.PurchaseReturnOrderAddForm;
import com.topideal.order.webapi.purchase.form.PurchaseReturnOrderForm;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购退货订单 控制层
 *
 * @author Gy
 */
@RestController
@RequestMapping("/webapi/order/purchaseReturn")
@Api(tags = "purchaseReturn-采购退货订单列表")
public class APIPurchaseReturnController {

	private static final Logger LOG = Logger.getLogger(APIPurchaseReturnController.class) ;

	private static final String[] COLUMNS = { "采购退货订单号", "公司", "出仓仓库", "入库仓库", "事业部",
			"PO单号", "供应商", "采购币种", "合同号", "理货单位", "目的地", "目的地址",
			"备注", "商品名称", "商品货号", "条码", "退货数量", "退货单价", "退货单价(含税)", "申报单价","退货总金额", "退货总金额(含税)", "税款", "税率",
			"品牌类型", "箱号","合同号","库位类型"};

	private static final String[] KEYS = { "code", "merchantName", "outDepotName", "inDepotName", "buName",
			"poNo", "supplierName", "currencyLabel", "contractNo", "tallyingUnitLabel", "destinationNameLabel", "destinationAddress",
			"remark","goodsName", "goodsNo", "barcode", "returnNum", "returnPrice", "taxReturnPrice", "declarePrice", "returnAmount", "taxReturnAmount", "tax", "taxRate",
			"brandName", "boxNo","subcontractNo","stockLocationTypeName"};

	// 采购退货订单service
	@Autowired
	PurchaseReturnService purchaseReturnService;

	/**
	 * 获取分页数据
	 *
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取分页数据")
	@PostMapping("/listPurchaseReturnOrder.asyn")
	private ResponseBean<PurchaseReturnOrderDTO> listPurchaseReturnOrder(PurchaseReturnOrderForm form) {

		try {

			if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			PurchaseReturnOrderDTO dto = new PurchaseReturnOrderDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user= ShiroUtils.getUserByToken(form.getToken()) ;
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseReturnService.listPurchaseReturnPage(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 *  多选订单时，需校验是否相同出仓仓库、相同供应商，若有不同则报错提示，不予保存提交。
	 * */
	@SuppressWarnings("unchecked")
	@PostMapping("/isSameParameter.asyn")
	@ApiOperation(value = "检验订单参数是否相同", notes="多选订单时，需校验是否相同出仓仓库、相同供应商，若有不同则报错提示，不予保存提交")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "退货单ID，多个以','隔开", required = true),
	})
	private ResponseBean<String> isSameParameter(@RequestParam(value="ids", required = true) String ids,
			@RequestParam(value="token", required = true) String token) {
		try{

			if(StringUtils.isBlank(token)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            purchaseReturnService.isSameParameter(ids);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch(Exception e){
        	LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 访问新增页面
	 * @param model
	 * @param ids   采购订单 ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/toAddPage.asyn")
	@ApiOperation(value = "访问新增页面", notes="访问新增页面前，根据采购订单信息构造采购退货订单信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = false),
	})
	public ResponseBean<PurchaseReturnOrderDTO> toAddPage(@RequestParam(value="ids", required = false) String ids,
			@RequestParam(value="token", required = true) String token) {

		try {
			if(StringUtils.isBlank(token)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			PurchaseReturnOrderDTO dto = new PurchaseReturnOrderDTO() ;

			User user= ShiroUtils.getUserByToken(token) ;
			if(ids != null && StrUtils.validateIds(ids)){//ids不为空，需要获取数据
				PurchaseReturnOrderModel purchaseReturnOrderModel = purchaseReturnService.listPurchaseOrderAndDepotOrder(StrUtils.parseIds(ids),user.getMerchantId());

				// 对象拷贝
				BeanUtils.copyProperties(purchaseReturnOrderModel, dto);

				dto.setPurchaseOrderRelCode(ids);

				String depotType = purchaseReturnService.getDepotTypeByDepotId(purchaseReturnOrderModel.getOutDepotId()) ;
				dto.setDepotType(depotType);

			}

			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 根据采购退货ID获取详情
	 * @param model
	 * @param ids   采购退货订单 ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDetailsById.asyn")
	@ApiOperation(value = "根据采购退货ID获取详情", notes="根据采购退货ID获取详情，涉及页面采购退货编辑，详情，打托出库")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购退货订单ID", required = true),
	})
	public ResponseBean<PurchaseReturnOrderDTO> getDetailsById(@RequestParam(value="id", required = true) Long id,
			@RequestParam(value="token", required = true) String token){

		if(StringUtils.isBlank(token)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {

			PurchaseReturnOrderDTO dto = purchaseReturnService.getDTOById(id);
			List<PurchaseReturnItemModel> itemList = purchaseReturnService.getItemListByOrderId(id);
			String depotType = purchaseReturnService.getDepotTypeByDepotId(dto.getOutDepotId());

			dto.setItemList(itemList);
			dto.setDepotType(depotType);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 非必填校验保存
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/savePurchaseReturnOrder.asyn")
	@ApiOperation(value = "采购退货非必填校验保存", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<String> savePurchaseReturnOrder(@RequestBody PurchaseReturnOrderAddForm form) {

		try{

			if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			if(form.getItemList() == null || form.getItemList().size() < 1) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体信息为空");
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			PurchaseReturnOrderModel model = new PurchaseReturnOrderModel() ;
			BeanUtils.copyProperties(form, model);


            purchaseReturnService.saveOrModifyPurchaseReturnOrder(model, user,"1");

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch(Exception e){
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 必填校验保存
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/saveRequirePurchaseReturnOrder.asyn")
	@ApiOperation(value = "采购退货必填校验保存",consumes= MediaType.APPLICATION_JSON_VALUE )
	public ResponseBean<String> saveRequirePurchaseReturnOrder(@RequestBody PurchaseReturnOrderAddForm form) {
		try{

			if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			if(form.getItemList() == null || form.getItemList().size() < 1) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体信息为空");
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			PurchaseReturnOrderModel model = new PurchaseReturnOrderModel() ;
			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId()).addObject(model.getBuId())
					.addObject(model.getOutDepotId()).empty();

			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

            purchaseReturnService.auditPurchaseReturnOrder(model, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch(Exception e){
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("delPurchaseReturnOrder.asyn")
	@ApiOperation(value = "删除采购退货订单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购退货订单ID，多个以','隔开", required = true),
	})
	public ResponseBean<String> delPurchaseReturnOrder(@RequestParam(value="ids", required = true) String ids,
			@RequestParam(value="token", required = true) String token) {
		try {

			if(StringUtils.isBlank(token)
					|| StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}

			List<Long> list = StrUtils.parseIds(ids);
			if(list.isEmpty()) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			boolean b = purchaseReturnService.delPurchaseReturnOrder(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@GetMapping("/exportPurchaseReturn.asyn")
	@ApiOperation(value = "导出采购退货订单")
	public void exportPurchaseReturn(HttpServletResponse response, HttpServletRequest request, PurchaseReturnOrderForm form) throws Exception {

		User user= ShiroUtils.getUserByToken(form.getToken()) ;
		String sheetName = "采购退货订单";

		PurchaseReturnOrderDTO dto = new PurchaseReturnOrderDTO() ;
		dto.setMerchantId(user.getMerchantId());

		BeanUtils.copyProperties(form, dto);

		// 获取导出的信息
		List<PurchaseReturnOrderExportDTO> result = purchaseReturnService.getDetailsByExport(dto, user);

		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 打托出库保存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/purchaseReturnDelivery.asyn")
	@ApiOperation(value = "打托出库保存",consumes= MediaType.APPLICATION_JSON_VALUE )
	public ResponseBean<String> purchaseReturnDelivery(@RequestBody PurchaseReturnOdepotAddForm form) {
		try{

			if(StringUtils.isBlank(form.getToken()) || form.getPurchaseReturnId() == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}


			if(form.getItemList() == null || form.getItemList().size() < 1) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体信息为空");
			}
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(form.getPurchaseReturnId()).addObject(form.getReturnOutDate()).empty();
			if(isNull){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(form.getToken());
			List<PurchaseReturnOdepotItemDTO> itemList = form.getItemList();

			PurchaseReturnOdepotModel returnModel = new PurchaseReturnOdepotModel() ;
			BeanUtils.copyProperties(form, returnModel);
			returnModel.setReturnOutDate(TimeUtils.parseFullTime(form.getReturnOutDate()));

            purchaseReturnService.savePurchaseReturnOdepot(returnModel, itemList ,user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
	@ApiOperation(value = "选择商品弹窗")
	@PostMapping(value="/getPurchaseItemPopupByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PurchaseReturnItemDTO> getPurchaseItemPopupByPage(MerchandiseForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			PurchaseReturnItemDTO list = purchaseReturnService.getPurchaseItemPopupByPage(form,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

}
