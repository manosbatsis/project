package com.topideal.storage.webapi.adjustmenttype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.dto.AdjustmentTypeItemDTO;
import com.topideal.storage.service.adjustmenttype.AdjustmentTypeService;
import com.topideal.storage.shiro.ShiroUtils;
import com.topideal.storage.webapi.dto.MergeDTO;
import com.topideal.storage.webapi.dto.ResultDTO;
import com.topideal.storage.webapi.form.AdjustmentTypeForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**
 * webapi类型调整 控制层
 */
@RestController
@RequestMapping("/webapi/storage/adjustmentType")
@Api(tags = "类型调整单")
public class APIAdjustmentTypeController {
	
	@Autowired
	private AdjustmentTypeService adjustmentTypeService;

	/**
	 * 类型调整单列表查询
	 */
	@ApiOperation(value = "类型调整单列表查询")
	@PostMapping(value="/listAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AdjustmentTypeDTO> listAdjustmentType(AdjustmentTypeForm form) {
		AdjustmentTypeDTO dto = new AdjustmentTypeDTO();
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())){
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setStatus(form.getStatus());
			dto.setModel(form.getModel());
			dto.setSource(form.getSource());
			dto.setAdjustmentStartTime(form.getAdjustmentStartTime());
			dto.setAdjustmentEndTime(form.getAdjustmentEndTime());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());

			dto = adjustmentTypeService.listByPage(dto);

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 调整单详情
	 */
	@ApiOperation(value = "调整单详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/getDetailById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AdjustmentTypeDTO> toDetailsPage(String token,Long id){
		AdjustmentTypeDTO typeModel = new AdjustmentTypeDTO();
		try{
			typeModel.setId(id);
			typeModel = adjustmentTypeService.getDetails(typeModel);

		}catch (Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,typeModel);
	}
	/**
	 * 查询某个类型调整详情--分配事业部用
	 * @param
	 */
	@ApiOperation(value = "查询某个类型调整详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/toGoodsDetailById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<AdjustmentTypeItemDTO>> toGoodsDetailById(String token,String id){
		List<AdjustmentTypeItemDTO> itemList = new ArrayList<AdjustmentTypeItemDTO>();
		try{
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(id).empty();
			if(isNull){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空;
			}
			AdjustmentTypeDTO typeModel = new AdjustmentTypeDTO();
			typeModel.setId(Long.valueOf(id));
			typeModel = adjustmentTypeService.getDetails(typeModel);
			itemList = typeModel.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,itemList);
	}
	
	/**确认调整(手工导入)、分配事业部前合并数量校验可用量
	 * 获取调减商品分组统计数量
	 * @param id 盘点结果单ids
	 * */
	@ApiOperation(value = "获取调减商品分组统计数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/getAdjustmentTypeSum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<MergeDTO>> getAdjustmentTypeSum(String token,String id) {
		//返回的结果集
		List<MergeDTO> resultList = new ArrayList<MergeDTO>();//返回结果集
		try{
			AdjustmentTypeDTO typeModel = adjustmentTypeService.getAdjustDetails(Long.valueOf(id));
			List<AdjustmentTypeItemDTO> itemList = typeModel.getItemList();
			String depotId = String.valueOf(typeModel.getDepotId());//仓库
			String depotType = typeModel.getDepotType();
			//合并好的明细 key=仓库id、商品id、是否坏品、是否过期、理货单位(香港仓)、批次
			Map<String, AdjustmentTypeItemDTO> mergeMap = new HashMap<>();
			for (AdjustmentTypeItemDTO itemDTO : itemList) {

				String goodsId = String.valueOf(itemDTO.getGoodsId());//商品
				String isDamage = itemDTO.getIsDamage();//是否坏品 0-好品 1-坏品
				Date overdueDate = itemDTO.getOverdueDate();
				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;//是否过期 0-是 1-否
				if(overdueDate!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(overdueDate);
				}
				//理货单位默认为空，香港仓时才需要
				String tallyingUnit = "";
				if(depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)){
					tallyingUnit = itemDTO.getTallyingUnit();
					//转换为库存的理货单位
					if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)){
						//托盘
						tallyingUnit = DERP.INVENTORY_UNIT_0;
					}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)){
						//箱
						tallyingUnit = DERP.INVENTORY_UNIT_1;
					}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)){
						//件
						tallyingUnit = DERP.INVENTORY_UNIT_2;
					}
					itemDTO.setTallyingUnit(tallyingUnit);
				}
				//批次
				String batchNo = itemDTO.getOldBatchNo();
				if(StringUtils.isEmpty(batchNo)){
					batchNo = "";
				}
				itemDTO.setIsExpire(isExpire);
				String key = depotId + goodsId + isDamage + isExpire + tallyingUnit + batchNo;
				if (mergeMap.containsKey(key)) {
					AdjustmentTypeItemDTO adjustmentTypeItemDTO = mergeMap.get(key);
					adjustmentTypeItemDTO.setAdjustTotal(itemDTO.getAdjustTotal() + adjustmentTypeItemDTO.getAdjustTotal());
					mergeMap.put(key,adjustmentTypeItemDTO);
				}else{
					mergeMap.put(key, itemDTO);
				}
			}
			
			for (String key : mergeMap.keySet()) {
				
				MergeDTO mergeDTO = new MergeDTO();
				mergeDTO.setDepot_id(String.valueOf(typeModel.getDepotId()));
				mergeDTO.setDepot_code(typeModel.getDepotCode());
				mergeDTO.setDepot_type(typeModel.getDepotType());
				mergeDTO.setGoods_id(String.valueOf(mergeMap.get(key).getGoodsId()));
				mergeDTO.setGoods_no(mergeMap.get(key).getGoodsNo());
				mergeDTO.setBatch_no(mergeMap.get(key).getOldBatchNo());
				mergeDTO.setIs_damage(mergeMap.get(key).getIsDamage());
				mergeDTO.setIsExpire(mergeMap.get(key).getIsExpire());
				mergeDTO.setTallying_unit(mergeMap.get(key).getTallyingUnit());
				mergeDTO.setDeficient_num(String.valueOf(mergeMap.get(key).getAdjustTotal()));
				
				resultList.add(mergeDTO);
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	/**
	 * 保存库存调整单表体事业部信息，并校验是否推送库存
	 * @param json
	 * @return
	 */
	@ApiOperation(value = "保存库存调整单表体事业部信息并推送库存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "调整单表体事业部信息json串", required = true)
	})
	@PostMapping(value="/confirmInDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean<ResultDTO> confirmInDepot(String token,String json) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("itemList",AdjustmentTypeItemDTO.class);
			AdjustmentTypeDTO dto = (AdjustmentTypeDTO) JSONObject.toBean(jsonData, AdjustmentTypeDTO.class, classMap);
			User user= ShiroUtils.getUserByToken(token);
			retMap = adjustmentTypeService.confirmInDepot(user, dto);
			
			resultDTO.setCode(retMap.get("code"));
			resultDTO.setMessage(retMap.get("message"));
			if(retMap.get("code").equals("01")) {
				return WebResponseFactory.responseBuild("99997",retMap.get("message"));//已知异常
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}

	/**
	 * 导入
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importAdjustment.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importAdjustment(String token,
													@ApiParam(value = "上传的文件", required = true)
													@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		try {
			if (file == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}
			List<List<Map<String, String>>> sheetList = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (sheetList == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			User user = ShiroUtils.getUserByToken(token);
			Map resultMap = adjustmentTypeService.importAdjustment(sheetList, user);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch (Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	/**
	 *  删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的id(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/deleteAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean deleteAdjustment(String token,String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//数据为空
			}
			if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			List list = StrUtils.parseIds(ids);
			adjustmentTypeService.deleteByIds(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 确认调整
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "确认调整")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/confirmAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean confirmAdjustment(String token,String id) {
		try {
			// 校验id是否有值
			if (StringUtils.isEmpty(id)) {
				// 输入信息不完整
				return  WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			Long idLong = Long.valueOf(id);
			User user= ShiroUtils.getUserByToken(token);
			adjustmentTypeService.confirmAdjustment(idLong,user);			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 根据查询条件导出类型调整单
	 * @return
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportAdjustmentType.asyn")
	public ResponseBean exportAdjustmentType(AdjustmentTypeForm form, HttpServletResponse response, HttpServletRequest request) {
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			AdjustmentTypeDTO dto = new AdjustmentTypeDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())){
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setStatus(form.getStatus());
			dto.setModel(form.getModel());
			dto.setSource(form.getSource());
			dto.setAdjustmentStartTime(form.getAdjustmentStartTime());
			dto.setAdjustmentEndTime(form.getAdjustmentEndTime());
			dto.setIds(form.getIds());
			//表头信息
			List<Map<String,Object>> orderList = adjustmentTypeService.listForExport(dto);

			//表头信息
			String sheetName1 = "基本信息";
			String[] columns1 = {"调整单号","单据状态","业务类别","调整仓库","来源单据号","确认人","单据日期","调整时间","单据来源","调整原因"};
			String[] keys1 = {"code","status","model","depot_name","source_code",
					"confirm_username","code_time","adjustment_time","source","adjustment_remark"};
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, orderList);

			//商品信息
			List<Map<String,Object>> itemList = adjustmentTypeService.listForExportItem(dto);

			String sheetName2 = "商品信息";
			String[] columns2 = {"调整单号","事业部","库位类型","商品货号","商品名称","商品条形码","原始批次号","生产日期" ,
					"失效日期","调整数量","库存类型","调整类型","是否过期","理货单位"};
			String[] keys2 = {"code","bu_name","stock_location_type_name","goods_no","goods_name","barcode","old_batch_no",
					"production_date","overdue_date","adjust_total","is_damage","type","is_expire","tallying_unit"};
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);

			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request,"类型调整单");

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch (Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


}
