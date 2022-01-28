package com.topideal.order.webapi.transfer;


import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.service.transfer.TransferOutDepotItemService;
import com.topideal.order.service.transfer.TransferOutDepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.transfer.dto.MergeDTO;
import com.topideal.order.webapi.transfer.dto.ResultDTO;
import com.topideal.order.webapi.transfer.dto.TransferInDepotResponseDTO;
import com.topideal.order.webapi.transfer.dto.TransferOutDepotResponseDTO;
import com.topideal.order.webapi.transfer.form.TransferOutDepotForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨出库 控制层
 * @author yucaifu
 */
@RestController
@RequestMapping("/webapi/transfer/transferOut")
@Api(tags = "调拨出库")
public class APITransferOutDepotController {

	@Autowired
	private TransferOutDepotService transferOutDepotService;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private TransferOutDepotItemService transferOutDepotItemService;
	@Autowired
	private TransferOrderService transferOrderService;
	
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/transferOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TransferOutDepotDTO> transferOutDepot(TransferOutDepotForm form) {
		if (org.apache.commons.lang3.StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try{
			TransferOutDepotDTO dto = new TransferOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
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
			dto = transferOutDepotService.listTransferOutDepotPage(dto, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID查找详情")
	@PostMapping("/toDetailPage.html")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调拨出库单id", required = true) })
	public ResponseBean<TransferOutDepotResponseDTO> toDetailPage(@RequestParam(value = "token", required = true) String token,
																  @RequestParam(value = "id", required = true) Long id) throws Exception{
		
		try {
			//查询调拨出库
			TransferOutDepotDTO transferOutDepot = transferOutDepotService.searchDetail(id);
			//查询调拨订单
			TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(transferOutDepot.getTransferOrderId());
			//查询调出仓库
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("depotId", transferOrder.getOutDepotId());
			DepotInfoMongo outDepotModel= depotInfoMongoDao.findOne(paraMap);
			List<TransferOutDepotItemDTO> itemList = transferOutDepotItemService.searchItemList(transferOutDepot.getId());
			TransferOutDepotResponseDTO responseDTO = new TransferOutDepotResponseDTO();
			responseDTO.setTransferOutDepotDTO(transferOutDepot);
			responseDTO.setTransferOrderDTO(transferOrder);
			responseDTO.setOutDepotModel(outDepotModel);
			responseDTO.setItemList(itemList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}

	@PostMapping("/exportOutDepotCount.asyn")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 获取导出调拨出库单的数量")
	})
	@ApiOperation(value = "获取导出调拨出库单的数量")
	public ResponseBean<ResultDTO> exportOutDepotCount(TransferOutDepotForm form) throws Exception{
		if (org.apache.commons.lang3.StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try {
			TransferOutDepotDTO dto = new TransferOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
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
			Integer count = transferOutDepotService.listForCount(dto, user);
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
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
	/**
	 * 导出
	 * */
	@ApiOperation(value = "调拨出库单导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportOutDepot.asyn")
	public ResponseBean exportOutDepot(HttpServletResponse response, HttpServletRequest request,TransferOutDepotForm form) throws Exception{
		try {
			TransferOutDepotDTO dto = new TransferOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
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
			List<Map<String,Object>> list1 = transferOutDepotService.listForMap(dto, user);
			if(list1!=null&&list1.size()>0){
				for(Map<String, Object> map:list1){
					String status = (String) map.get("status");
					if (StringUtils.isNotBlank(status)) {
						map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.transferOutDepot_statusList, status));
					}
				}
			}
			List<Map<String,Object>> listitem = transferOutDepotService.listForMapItem(dto, user);
			//基础信息
			String sheetName1 = "表头";
			String[] columns1={"调拨出库单号","单据状态","企业调拨单号","调出仓库","调入仓库","事业部","调出公司名称","调入公司名称","合同号","调拨出库时间"};
			String[] keys1={"code","status","transfer_order_code","out_depot_name","in_depot_name","bu_name","merchant_name","in_customer_name","contract_no","transfer_date"};
			//商品信息
			String sheetName2 = "商品信息";
			String[] columns2={"调拨出库单号","调出商品编号","调出商品货号","调出商品名称","调拨出库数量","正常数量","坏品数量","调出批次","生产日期","失效日期"};
			String[] keys2={"code","out_goods_code","out_goods_no","out_goods_name","transfer_num","goods_num","worn_num","transfer_batch_no","production_date","overdue_date"};
			//生成表格
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, list1);
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, listitem);

			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			String fileName = "调拨出库单"+TimeUtils.formatDay();
			FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "调拨单打托出库信息json", required = true),
	})
	@PostMapping(value="/saveTransferOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "调拨订单打托出库保存")
	public ResponseBean<ResultDTO> saveTransferOutDepot(String token, String json) {
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("goodsList",TransferOutDepotItemDTO.class);
			TransferOutDepotDTO model = (TransferOutDepotDTO) JSONObject.toBean(jsonData, TransferOutDepotDTO.class, classMap);
			User user= ShiroUtils.getUserByToken(token);
			retMap = transferOutDepotService.saveTransferOutDepot(model, user);
			ResultDTO resultDTO = new ResultDTO();
			resultDTO.setCode(retMap.get("code"));
			resultDTO.setMessage(retMap.get("message"));
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


	/**
	 * 导入
	 * @return int
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importAdjustment.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importTransferOut(String token,
													@ApiParam(value = "上传的文件", required = true)
													@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}
			User user = ShiroUtils.getUserByToken(token);
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map resultMap = transferOutDepotService.saveImportTransferOut(data, user);

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
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "审核调拨出库单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调拨入库单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditTransferOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> auditTransferOutDepot(String token,String ids) {
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
			Map<String, Object> result = transferOutDepotService.auditTransferOutDepot(list, user);

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

	@ApiOperation(value = "获取调拨出库单商品合并详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调拨出库单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value = "/getTransferOutItemSumByIds.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<MergeDTO>> getTransferOutItemSumByIds(String token, String ids) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			String[] idsStr = ids.split(",");
			List<Long> idArr = new ArrayList<Long>();
			for (String id : idsStr) {
				idArr.add(Long.valueOf(id));
			}
			//查询明细
			List<Map<String, Object>> itemList = transferOutDepotService.getItemSumByIds(idArr, user.getTopidealCode());
			List<MergeDTO> itemDtoList = new ArrayList<>();
			List<MergeDTO> mergeList = null;//合并好的明细
			if (itemList != null && itemList.size() > 0) {
				for (Map<String, Object> itemMap : itemList) {
					Integer transferNum = (Integer) itemMap.get("transfer_num");
					Integer wornＮum = (Integer) itemMap.get("worn_num");
					Integer expireNum = (Integer) itemMap.get("expire_num");
					Integer totalNum = 0;
					if (transferNum != null) {
						totalNum += transferNum;
					}

					if (wornＮum != null) {
						totalNum += wornＮum;
					}

					if (expireNum != null) {
						totalNum += expireNum;
					}

					MergeDTO mergeDTO = new MergeDTO();
					//获取仓库编码、仓库类型
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("depotId", itemMap.get("out_depot_id"));
					DepotInfoMongo depot = depotInfoMongoDao.findOne(map);
					mergeDTO.setDepot_code(depot.getCode());
					mergeDTO.setDepot_name(depot.getName());
					mergeDTO.setDepot_type(depot.getType());
					mergeDTO.setOut_depot_id(String.valueOf(itemMap.get("out_depot_id")));
					mergeDTO.setOut_goods_id(String.valueOf(itemMap.get("out_goods_id")));
					mergeDTO.setOut_goods_no(String.valueOf(itemMap.get("out_goods_no")));
					mergeDTO.setTallying_unit((String) itemMap.get("tallying_unit"));
					mergeDTO.setTransfer_num(totalNum.toString());
					itemDtoList.add(mergeDTO);
				}
				//合并
				mergeList = mergeItem(itemDtoList);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, mergeList);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 按 仓库id、商品id、理货单位(香港仓)
	 */
	private List<MergeDTO> mergeItem(List<MergeDTO> itemList) {
		List<MergeDTO> mergeList = new ArrayList<>();

		/**合并
		 * key=仓库id、商品id、理货单位(香港仓)
		 * **/
		Map<String, MergeDTO> mergeMap = new HashMap<String, MergeDTO>();
		//循环合并明细
		for (MergeDTO itemDto : itemList) {
			String depotId = itemDto.getOut_depot_id(); //仓库
			String goodsId = itemDto.getOut_goods_id(); //商品
			String tallyingUnit = "";//理货单位默认为空，香港仓时才需要
			String depotType = itemDto.getDepot_type();
			if (depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
				tallyingUnit = itemDto.getTallying_unit();
				if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)) {//转换为库存的理货单位
					tallyingUnit = DERP.INVENTORY_UNIT_0;//托盘
				} else if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)) {
					tallyingUnit = DERP.INVENTORY_UNIT_1;//箱
				} else if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)) {
					tallyingUnit = DERP.INVENTORY_UNIT_2;//件
				}
			}
			String key = depotId + goodsId + tallyingUnit;
			if (mergeMap.get(key) != null) {
				MergeDTO itemValueDto = mergeMap.get(key);
				int numAll = Integer.valueOf(itemValueDto.getTransfer_num());//已合并数量
				int transfer_num = Integer.valueOf(itemDto.getTransfer_num());
				int mergeNum = numAll+transfer_num;
				itemValueDto.setTransfer_num(String.valueOf(mergeNum));
			} else {
				MergeDTO newItemDto = new MergeDTO();
				newItemDto.setOut_depot_id(itemDto.getOut_depot_id());
				newItemDto.setDepot_code(itemDto.getDepot_code());
				newItemDto.setDepot_name(itemDto.getDepot_name());
				newItemDto.setDepot_type(itemDto.getDepot_type());
				newItemDto.setOut_goods_id(itemDto.getOut_goods_id());
				newItemDto.setOut_goods_no(itemDto.getOut_goods_no());
				newItemDto.setTallying_unit(itemDto.getTallying_unit());
				newItemDto.setTransfer_num(itemDto.getTransfer_num());
				mergeMap.put(key, newItemDto);
			}
		}
		//遍历合并好的Map组装成list
		for (MergeDTO dto : mergeMap.values()) {
			mergeList.add(dto);
		}
		return mergeList;
	}

}
