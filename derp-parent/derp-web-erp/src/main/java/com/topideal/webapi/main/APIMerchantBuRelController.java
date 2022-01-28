package com.topideal.webapi.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.BusinessUnitService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/system/merchantBuRel")
@Api(tags = "商家事业部关系表")
public class APIMerchantBuRelController {
	
	private static final String[] EXPORT_COLUMNS = { "公司编码", "公司简称", "事业部编码", "事业部名称", "状态", "创建时间"};
	private static final String[] EXPORT_KEYS = {"merchantCode", "merchantName", "buCode", "buName", "statusLabel", "createDate"} ;
	
	@Autowired
	BusinessUnitService businessUnitService ;
	@Autowired
	MerchantInfoService merchantInfoService ;
	@Autowired
	MerchantBuRelService merchantBuRelService ;
	
	/**
	 * 获取采购订单的审批方式
	 * @param model  公司信息
	 * @return
	 */
	@ApiOperation(value = "获取采购订单的审批方式返回 1-OA审批 2-经分销审批")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部Id"),
			@ApiImplicitParam(name = "supplierId", value = "供应商id"),
	})
	@PostMapping(value="/getPurchaseAuditMethod.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> getPurchaseAuditMethod(String token,Long buId,Long supplierId) {
		try{
			//根据用户当前登录的公司+选择的事业部，自动带出公司事业部配置的采购审批方式的值。增加的需求：在配置的前提下，如果选择的供应商为内部公司，则采购审批方式为经分销。
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集			
			String auditMethod = merchantBuRelService.getPurchaseAuditMethod(user,buId,supplierId);						
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,auditMethod);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	

	/**
	 * 访问列表页面
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage() throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			MerchantInfoModel merchant = new MerchantInfoModel() ;
	        List<SelectBean> merchantList = merchantInfoService.getSelectBean(merchant);
	        resultMap.put("merchantList", merchantList);        
	        List<BusinessUnitModel> businessUnitList = businessUnitService.getAllList();
	        resultMap.put("businessUnitList", businessUnitList);
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantList);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * @param model  公司信息
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "merchantCode", value = "公司编码"),
			@ApiImplicitParam(name = "merchantName", value = "公司简称"),
			@ApiImplicitParam(name = "buId", value = "事业部Id")
	})
	@PostMapping(value="/listMerchantBuRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listMerchantBuRel(String merchantCode,String merchantName
			,Long buId,int begin,int pageSize) {
		try{
			// 响应结果集
			MerchantBuRelDTO dto=new MerchantBuRelDTO();
			dto.setMerchantCode(merchantCode);
			dto.setMerchantName(merchantName);
			dto.setBuId(buId);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = merchantBuRelService.listMerchantBuRelPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 保存
	 * @param
	 * @return
	 */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "公司id", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部id", required = true),
			@ApiImplicitParam(name = "purchasePriceManage", value = "采购价格管理",required = true),
			@ApiImplicitParam(name = "status", value = "状态",required = true),
			@ApiImplicitParam(name = "salePriceManage", value = "销售价格管理",required = true),
			@ApiImplicitParam(name = "purchaseAuditMethod", value = "采购审批方式",required = true),
			@ApiImplicitParam(name = "stockLocationManage", value = "库位管理",required = true)
	})
	@PostMapping(value="/saveMerchantBuRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean saveMerchantBuRel(Long merchantId,Long buId,String purchasePriceManage,
										   String status,String salePriceManage, String purchaseAuditMethod, String stockLocationManage) {
		try{
			boolean isEmpty = new EmptyCheckUtils()
					.addObject(merchantId).addObject(buId).addObject(purchasePriceManage)
					.addObject(status).addObject(salePriceManage).addObject(purchaseAuditMethod)
					.addObject(stockLocationManage)
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			MerchantBuRelModel model=new MerchantBuRelModel();
			model.setMerchantId(merchantId);
			model.setBuId(buId);
			model.setPurchasePriceManage(purchasePriceManage);
			model.setStatus(status);
			model.setSalePriceManage(salePriceManage);
			model.setPurchaseAuditMethod(purchaseAuditMethod);
			model.setStockLocationManage(stockLocationManage);
			merchantBuRelService.save(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 启用、禁用
	 * @param model  公司信息
	 * @return
	 */

/*	@ApiOperation(value = "启用/禁用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "status", value = "status", required = true)
	})
	@PostMapping(value="/changeStaus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean changeStaus(Long id,String status) {
		try{
			MerchantBuRelModel model=new MerchantBuRelModel();
			model.setId(id);
			model.setStatus(status);
			merchantBuRelService.changeStaus(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}*/
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportMerchantBuRel.asyn")
	public ResponseBean exportMerchantBuRel(String merchantCode,String merchantName
			,Long buId, HttpServletResponse response, HttpServletRequest request) throws Exception {
		// 响应结果集
		try {
			MerchantBuRelDTO dto=new MerchantBuRelDTO();
			dto.setMerchantCode(merchantCode);
			dto.setMerchantName(merchantName);
			dto.setBuId(buId);
			List<MerchantBuRelDTO> result = merchantBuRelService.getExportList(dto);
			
			String sheetName = "公司事业部导出";
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBean(Long merchantId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			MerchantBuRelModel model= new MerchantBuRelModel();
			model.setMerchantId(merchantId);
			result = merchantBuRelService.getSelectBean(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取当前公司和用户所关联的事业部下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取当前公司和用户所关联的事业部下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "公司ID", required = true)
	})
	@PostMapping(value="/getSelectBeanByUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBeanByUser(String token, Long merchantId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			boolean isEmpty = new EmptyCheckUtils()
					.addObject(merchantId).addObject(token)
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(token);
			MerchantBuRelModel model= new MerchantBuRelModel();
			model.setMerchantId(merchantId);
			result = merchantBuRelService.getSelectBeanByUser(user, model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 修改
	 * @param model  公司信息
	 * @return
	 */
	@ApiOperation(value = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "purchasePriceManage", value = "采购价格管理",required = true),
			@ApiImplicitParam(name = "status", value = "状态",required = true),
			@ApiImplicitParam(name = "salePriceManage", value = "销售价格管理",required = true),
			@ApiImplicitParam(name = "stockLocationManage", value = "库位管理",required = true)
	})
	@PostMapping(value="/modifyMerchantBuRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean modifyMerchantBuRel(Long id,String purchasePriceManage,String status,String salePriceManage,
											 String purchaseAuditMethod, String stockLocationManage) {
		try{
			MerchantBuRelModel model=new MerchantBuRelModel();
			model.setId(id);
			model.setPurchasePriceManage(purchasePriceManage);
			model.setStatus(status);
			model.setSalePriceManage(salePriceManage);
			model.setPurchaseAuditMethod(purchaseAuditMethod);
			model.setStockLocationManage(stockLocationManage);
			merchantBuRelService.modifyMerchantBuRel(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 获取详情
	 * @return
	 */
	@ApiOperation(value = "获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) {
		MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();
		try {
			merchantBuRelModel = merchantBuRelService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantBuRelModel);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 通过“公司+事业部”查询公司事业部是否启用了库位管理
	 * @return
	 */
	@ApiOperation(value = "通过公司+事业部查询是否启用了库位管理")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "公司ID"),
			@ApiImplicitParam(name = "buId", value = "事业部ID")
	})
	@PostMapping(value="/getLocationManage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getLocationManage(String token, Long merchantId, Long buId) {
		Boolean result = new Boolean(false);
		try {
			boolean isEmpty = new EmptyCheckUtils()
					.addObject(buId).addObject(token)
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			MerchantBuRelDTO dto = new MerchantBuRelDTO();
			dto.setBuId(buId);
			if(merchantId == null) {
				dto.setMerchantId(user.getMerchantId());
			}else {
				dto.setMerchantId(merchantId);
			}
			dto.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
			result = merchantBuRelService.getLocationManage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
}
