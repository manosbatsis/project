package com.topideal.order.webapi.transfer;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.transfer.TransferInDepotItemService;
import com.topideal.order.service.transfer.TransferInDepotService;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.transfer.dto.ResultDTO;
import com.topideal.order.webapi.transfer.dto.TransferInDepotResponseDTO;
import com.topideal.order.webapi.transfer.form.TransferInDepotForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库 控制层
 * @author yucaifu
 */
@RestController
@RequestMapping("/webapi/transfer/transferIn")
@Api(tags = "调拨入库")
public class APITransferInDepotController {

	
	@Autowired
	private TransferInDepotService transferInDepotService;
    //仓库信息service
    @Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	@Autowired
	private TransferInDepotItemService transferInDepotItemService;
	
	@Autowired
	private TransferOrderService transferOrderService;

	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/transferInDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TransferInDepotDTO> transferInDepot(TransferInDepotForm form) {
		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			TransferInDepotDTO dto = new TransferInDepotDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setCode(form.getCode());
			dto.setOutDepotId(form.getOutDepotId());
			dto.setInDepotId(form.getInDepotId());
			dto.setTransferOrderCode(form.getTransferOrderCode());
			dto.setContractNo(form.getContractNo());
			dto.setStatus(form.getStatus());
			dto.setBuId(form.getBuId());
			dto.setTransferStartDate(form.getTransferStartDate());
			dto.setTransferEndDate(form.getTransferEndDate());
			// 响应结果集
			dto = transferInDepotService.searchByPage(dto, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据ID查找详情
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID查找详情")
	@PostMapping("/toDetailPage.html")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调拨入库单id", required = true) })
	public ResponseBean<TransferInDepotResponseDTO> toDetailPage(@RequestParam(value = "token", required = true) String token,
																 @RequestParam(value = "id", required = true) Long id) throws Exception{
		try {
			//查询调拨入库
			TransferInDepotDTO transferInDepot = transferInDepotService.searchById(Long.valueOf(id));

			//查询调拨订单
			TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(transferInDepot.getTransferOrderId());
			//查询调出仓库
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("depotId", transferOrder.getOutDepotId());
			DepotInfoMongo outDepotModel= depotInfoMongoDao.findOne(paraMap);
			List<TransferInDepotItemDTO> itemList = transferInDepotItemService.searchItemList(transferInDepot.getId());
			TransferInDepotResponseDTO responseDTO = new TransferInDepotResponseDTO();
			responseDTO.setTransferInDepotDTO(transferInDepot);
			responseDTO.setTransferOrderDTO(transferOrder);
			responseDTO.setOutDepotModel(outDepotModel);
			responseDTO.setItemList(itemList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	@PostMapping("/exportInDepotCount.asyn")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 获取导出调拨入库单的数量")
	})
	@ApiOperation(value = "获取导出调拨入库单的数量")
	public ResponseBean<ResultDTO> exportInDepotCount(TransferInDepotForm form) throws Exception {
		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			TransferInDepotDTO dto = new TransferInDepotDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(form.getOutDepotId());
			dto.setInDepotId(form.getInDepotId());
			dto.setTransferOrderCode(form.getTransferOrderCode());
			dto.setContractNo(form.getContractNo());
			dto.setStatus(form.getStatus());
			dto.setBuId(form.getBuId());
			dto.setTransferStartDate(form.getTransferStartDate());
			dto.setTransferEndDate(form.getTransferEndDate());
			dto.setIds(form.getIds());
			Integer count = transferInDepotService.listForCount(dto, user);
			Integer max = DERP.EXPORT_MAX;
			ResultDTO resultDTO = new ResultDTO();
			resultDTO.setCode("00");
			resultDTO.setMessage("检查通过");
			if(count.intValue()>max.intValue()){
				resultDTO.setCode("01");
				resultDTO.setMessage("导出数量超过"+max+"请分多次导出");
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 导出
	 * */
	@ApiOperation(value = "调拨入库单导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportInDepot.asyn")
	public ResponseBean exportInDepot(HttpServletResponse response, HttpServletRequest request,TransferInDepotForm form) throws Exception{

		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			TransferInDepotDTO dto = new TransferInDepotDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(form.getOutDepotId());
			dto.setInDepotId(form.getInDepotId());
			dto.setTransferOrderCode(form.getTransferOrderCode());
			dto.setContractNo(form.getContractNo());
			dto.setStatus(form.getStatus());
			dto.setBuId(form.getBuId());
			dto.setTransferStartDate(form.getTransferStartDate());
			dto.setTransferEndDate(form.getTransferEndDate());
			dto.setIds(form.getIds());
			List<Map<String,Object>> list1 = transferInDepotService.listForMap(dto, user);
			if(list1!=null&&list1.size()>0){
				for(Map<String, Object> map:list1){
					String status = (String) map.get("status");
					map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.transferInDepot_statusList,status));
				}
			}
			List<Map<String,Object>> listitem = transferInDepotService.listForMapItem(dto, user);
			if(listitem!=null&&listitem.size()>0){
				for(Map<String, Object> map:listitem){
					String tallyingUnit = (String) map.get("tallying_unit");//理货单位 00-托盘 01-箱 02-件
					if(!StringUtils.isEmpty(tallyingUnit)) {
						map.put("tallying_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit));
					}
				}
			}
			//基础信息
			String sheetName1 = "表头";
			String[] columns1={"调拨入库单号","单据状态","企业调拨单号","调出仓库","调入仓库","事业部","调出公司名称","调入公司名称","合同号","调拨入库时间"};
			String[] keys1={"code","status","transfer_order_code","out_depot_name","in_depot_name","bu_name","merchant_name","in_customer_name","contract_no","transfer_date"};
			//商品信息
			String sheetName2 = "商品信息";
			String[] columns2={"调拨入库单号","调入商品编号","调入商品货号","调入商品名称","调入数量","正常数量","坏品数量","过期数量","调入批次","生产日期","失效日期","理货单位"};
			String[] keys2={"code","in_goods_code","in_goods_no","in_goods_name","tranfer_num_all","transfer_num","worn_num","expire_num","transfer_batch_no","production_date","overdue_date","tallying_unit"};
			//生成表格

			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, list1);
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, listitem);

			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			String fileName = "调拨入库单"+TimeUtils.formatDay();
			FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "调拨单上架信息json", required = true),
	})
	@PostMapping(value="/saveTransferInDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "调拨订单上架入库保存")
	public ResponseBean<ResultDTO> saveTransferInDepot(String token, String json) {
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("goodsList",TransferInDepotItemDTO.class);
			TransferInDepotDTO model = (TransferInDepotDTO) JSONObject.toBean(jsonData, TransferInDepotDTO.class, classMap);
			User user= ShiroUtils.getUserByToken(token);
			retMap = transferInDepotService.saveTransferInDepot(model, user);
			ResultDTO resultDTO = new ResultDTO();
			resultDTO.setCode(retMap.get("code"));
			resultDTO.setMessage(retMap.get("message"));
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}


	@ApiOperation(value = "导入调拨入库单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value = "/importTransferInOrder.asyn", headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importTransferInOrder(String token,
													   @ApiParam(value = "上传的文件", required = true)
													   @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		try {

			if (file == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}

			User user = ShiroUtils.getUserByToken(token);
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			Map<String, Object> resultMap = transferInDepotService.saveImportTransferIn(user, data);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "审核调拨入库单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调拨入库单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditTransferInDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> auditTransferInDepot(String token,String ids) {
		String errorMsg = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (org.apache.commons.lang.StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			List list = StrUtils.parseIds(ids);
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			Map<String, Object> result = transferInDepotService.auditTransferInDepot(list, user);

			errorMsg = result.get("failureMsg").toString();
			if(org.apache.commons.lang.StringUtils.isNotBlank(errorMsg)) {
				return WebResponseFactory.responseBuild("99997",errorMsg);
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

}
