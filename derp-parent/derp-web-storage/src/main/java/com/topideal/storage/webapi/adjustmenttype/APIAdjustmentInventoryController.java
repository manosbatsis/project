package com.topideal.storage.webapi.adjustmenttype;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.topideal.common.constant.DERP_STORAGE;
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
import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.dto.AdjustmentInventoryItemDTO;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.storage.service.adjustmenttype.AdjustmentInventoryService;
import com.topideal.storage.shiro.ShiroUtils;
import com.topideal.storage.webapi.dto.MergeDTO;
import com.topideal.storage.webapi.dto.ResultDTO;
import com.topideal.storage.webapi.form.AdjustmentInventoryForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**
 *  webapi库存调整 控制层
 */
@RestController
@RequestMapping("/webapi/storage/adjustmentInventory")
@Api(tags = "库存调整单")
public class APIAdjustmentInventoryController {

	@Autowired
	private AdjustmentInventoryService adjustmentInventoryService;
	
	/**
	 * 详情
	 */
	@ApiOperation(value = "库存调整单详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AdjustmentInventoryDTO> toDetailsPage(String token,Long id) throws Exception {
		AdjustmentInventoryDTO inventoryDto = new AdjustmentInventoryDTO();
		try{
			User user = ShiroUtils.getUserByToken(token);
			inventoryDto.setId(id);
			inventoryDto = adjustmentInventoryService.getDetails(inventoryDto);
			// 如果不是月结直接返回
			if (DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2.equals(inventoryDto.getModel())) {
				// 月结商品合并
				List<AdjustmentInventoryItemDTO> itemList = inventoryDto.getItemList();
				// 用于库存调整单的合并
				Map<String, AdjustmentInventoryItemDTO> itemMap = new HashMap<>();
				for (AdjustmentInventoryItemDTO item : itemList) {
//					Timestamp overdueDate = item.getOverdueDate();
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;//是否过期 0-是 1-否
					if (item.getOverdueDate() != null) {
						if (TimeUtils.getNow().getTime() > item.getOverdueDate().getTime()) {
							isExpire = DERP.ISEXPIRE_0;
						}
					}
					String tallyingUnit = item.getTallyingUnit();// 理货单位
					if (StringUtils.isEmpty(tallyingUnit)) {
						tallyingUnit = "";
					}
					String key = item.getGoodsId() + item.getIsDamage() + item.getOldBatchNo() + isExpire + tallyingUnit;
					AdjustmentInventoryItemDTO itemModel = new AdjustmentInventoryItemDTO();
					if (itemMap.containsKey(key)) {
						AdjustmentInventoryItemDTO adjustmentInventoryItemDTO = itemMap.get(key);
						//调整类型 0 调减 1 调增
						if (!adjustmentInventoryItemDTO.getType().equals(item.getType())) {
							if (adjustmentInventoryItemDTO.getAdjustTotal() < item.getAdjustTotal()) {
								adjustmentInventoryItemDTO.setAdjustTotal(item.getAdjustTotal() - adjustmentInventoryItemDTO.getAdjustTotal());
								adjustmentInventoryItemDTO.setType(item.getType());
							} else {
								adjustmentInventoryItemDTO.setAdjustTotal(adjustmentInventoryItemDTO.getAdjustTotal() - item.getAdjustTotal());
							}
						}
						itemMap.put(key, adjustmentInventoryItemDTO);

					} else {
						itemModel.setGoodsId(item.getGoodsId());
						itemModel.setGoodsNo(item.getGoodsNo());
						itemModel.setGoodsName(item.getGoodsName());
						itemModel.setBarcode(item.getBarcode());
						itemModel.setType(item.getType());
						itemModel.setOldBatchNo(item.getOldBatchNo());
						itemModel.setIsDamage(item.getIsDamage());
						itemModel.setTallyingUnit(tallyingUnit);
						itemModel.setIsExpire(isExpire);
						itemModel.setAdjustTotal(item.getAdjustTotal());
						itemModel.setTallyingUnit(item.getTallyingUnit());
						itemModel.setPoNo(item.getPoNo());
						itemMap.put(key, itemModel);
					}
				}
				List<AdjustmentInventoryItemDTO> valuesList = new ArrayList<>(itemMap.values());
				inventoryDto.setItemList(valuesList);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,inventoryDto);
	}

	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "库存调整单列表查询")
	@PostMapping(value="/listAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<AdjustmentInventoryDTO> listAdjustmentInventory(AdjustmentInventoryForm form) {
		AdjustmentInventoryDTO dto = new AdjustmentInventoryDTO();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			dto.setMerchantName(form.getMerchantName());
			dto.setModel(form.getModel());
			dto.setStatus(form.getStatus());
			dto.setSource(form.getSource());
			dto.setSourceStartTime(form.getSourceStartTime());
			dto.setSourceEndTime(form.getSourceEndTime());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = adjustmentInventoryService.listByPage(dto);
			
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 审核库存调整
	 */
	@ApiOperation(value = "审核库存调整")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调整单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> auditAdjustment(String token,String ids) {
		String errorMsg = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//格式不对
			}
			List list = StrUtils.parseIds(ids);
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			Map<String, Object> result = adjustmentInventoryService.auditAdjustment(list, user);

			errorMsg = result.get("failureMsg").toString();
			if(StringUtils.isNotBlank(errorMsg)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),errorMsg);
			}
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@ApiOperation(value = "删除库存调整单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调整单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/deleteAdjustment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean deleteAdjustment(String token,String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			List list = StrUtils.parseIds(ids);
			adjustmentInventoryService.deleteByIds(list);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 导入
	 * 
	 * @param
	 * @return int
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
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}
			List<List<Map<String, String>>> sheetList = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (sheetList == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			User user = ShiroUtils.getUserByToken(token);
			Map resultMap = adjustmentInventoryService.importAdjustment(sheetList, user);
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 根据参数获取库存调整单信息
	 * @param sourceCode 来源单据号
	 * @param currentDate 当前时间
	 */
	@ApiOperation(value = "获取库存调整单信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "sourceCode", value = "来源单据号", required = true),
			@ApiImplicitParam(name = "currentDate", value = "当前时间", required = true)
	})
	@PostMapping(value="/getAdjustmentStatus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AdjustmentInventoryModel> getAdjustmentStatus(String token,String sourceCode, String currentDate) {
		AdjustmentInventoryModel model = new AdjustmentInventoryModel();
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(sourceCode).addObject(currentDate).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			model.setSourceCode(sourceCode);
			model.setMonths(currentDate);
			model = adjustmentInventoryService.searchByModel(model);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}
	/**
	 * 获取调减商品分组统计数量
	 * @param Ids 调整单id
	 * */
	@ApiOperation(value = " 获取调减商品分组统计数量",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的调整单ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/getAdjustmentSum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<MergeDTO>> getAdjustmentSum(String token,String ids) {
		List<MergeDTO> resultList = new ArrayList<MergeDTO>();//返回结果集
		try{
			if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			//新
			String[] idsStr = ids.split(",");
			List<Long> idarr = new ArrayList<Long>();
			for(String id:idsStr){
				idarr.add(Long.valueOf(id));
			}
			//查询明细
			User user = ShiroUtils.getUserByToken(token);
			List<Map<String, Object>> itemList = adjustmentInventoryService.getItemByInventoryIds(idarr,user);
			List<Map<String, Object>> mergeList = null;//合并好的明细
			if(itemList!=null&&itemList.size()>0){
				//合并
				mergeList = mergeItem(itemList);
				
				for(Map<String, Object> map : mergeList) {
					MergeDTO mergeDTO = new MergeDTO();
					mergeDTO.setDepot_id(String.valueOf(map.get("depot_id")));
					mergeDTO.setDepot_code((String)map.get("depot_code"));
					mergeDTO.setDepot_type((String)map.get("depot_type"));
					mergeDTO.setGoods_id(String.valueOf(map.get("goods_id")));
					mergeDTO.setGoods_no((String)map.get("goods_no"));
					mergeDTO.setOld_batch_no((String)map.get("old_batch_no"));
					mergeDTO.setIs_damage((String)map.get("is_damage"));
					mergeDTO.setIsExpire((String)map.get("isExpire"));
					mergeDTO.setTallying_unit((String)map.get("tallying_unit"));
					mergeDTO.setAdjust_total(String.valueOf(map.get("adjust_total")));
					
					resultList.add(mergeDTO);
				}
			}
		  }catch(Exception e){
	     	e.printStackTrace();
	        return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
	     }
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}
	/**
	 * 按 仓库id、商品id、好坏品、是否过期、批次合并调减数量
	 * */
	private List<Map<String, Object>> mergeItem(List<Map<String, Object>> itemList){
		List<Map<String, Object>> mergeList = new ArrayList<Map<String,Object>>();
		
		/**合并
		 * key1=仓库id、商品id、是否坏品、是否过期、理货单位(香港仓)
		 * key2=仓库id、商品id、是否坏品、是否过期、理货单位(香港仓)、批次
		 * 是否坏品 :0-否，1-是
		 * **/
		Map<String, Map<String, Object>> mergeMap = new HashMap<String, Map<String, Object>>();
		//循环合并明细
		for(Map<String, Object> itemMap:itemList){
			String depotId = String.valueOf(itemMap.get("depot_id"));//仓库
			String goodsId = String.valueOf(itemMap.get("goods_id"));//商品
			String isDamage = (String) itemMap.get("is_damage");//是否坏品 0-好品 1-坏品
			Date overdueDate = (Date) itemMap.get("overdue_date");
			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;//是否过期 0-是 1-否
			if(overdueDate!=null){
				if(TimeUtils.getNow().getTime()>overdueDate.getTime()){
					isExpire = DERP.ISEXPIRE_0;
				}
			}
			String tallyingUnit = "";//理货单位默认为空，香港仓时才需要
			String depotType = (String) itemMap.get("depot_type");
			if(depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				tallyingUnit = (String) itemMap.get("tallying_unit");
				if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)){//转换为库存的理货单位
					tallyingUnit = DERP.INVENTORY_UNIT_0;//托盘
				}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)){
					tallyingUnit = DERP.INVENTORY_UNIT_1;//箱
				}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)){
					tallyingUnit = DERP.INVENTORY_UNIT_2;//件
				}
			}
			itemMap.put("tallying_unit", tallyingUnit);
			
			String batchNo = (String) itemMap.get("old_batch_no");//批次
			if(StringUtils.isEmpty(batchNo)){
				batchNo = "";
			}
			
			String key1 = depotId+goodsId+isDamage+isExpire+tallyingUnit;
			String key2 = depotId+goodsId+isDamage+isExpire+tallyingUnit+batchNo;
			//批次号不为空，合并有批次的校验此批次的库存
			if(!StringUtils.isEmpty(batchNo)){
				if(mergeMap.get(key2)!=null){
					Map<String, Object> itemValue2Map = mergeMap.get(key2);
					int adjust_total = (int) itemValue2Map.get("adjust_total");
					int total = (int) itemMap.get("adjust_total");
					itemValue2Map.put("adjust_total", adjust_total+total);//数量相加
				}else{
					Map<String, Object> newItemValueMap = new HashMap<String, Object>();
					newItemValueMap.put("depot_id", itemMap.get("depot_id"));
					newItemValueMap.put("depot_code", itemMap.get("depot_code"));
					newItemValueMap.put("depot_type", itemMap.get("depot_type"));
					newItemValueMap.put("goods_id", itemMap.get("goods_id"));
					newItemValueMap.put("goods_no", itemMap.get("goods_no"));
					String old_batch_no = (String) itemMap.get("old_batch_no");
					if(StringUtils.isEmpty(old_batch_no)) old_batch_no = null;
					newItemValueMap.put("old_batch_no",old_batch_no);
					newItemValueMap.put("is_damage", itemMap.get("is_damage"));
					newItemValueMap.put("isExpire", isExpire);
					newItemValueMap.put("tallying_unit", itemMap.get("tallying_unit"));
					newItemValueMap.put("adjust_total", itemMap.get("adjust_total"));
					mergeMap.put(key2, newItemValueMap);
				}
			} else {
				if(mergeMap.get(key1)!=null){
					Map<String, Object> itemValueMap = mergeMap.get(key1);
					int adjust_total = (int) itemValueMap.get("adjust_total");
					int total = (int) itemMap.get("adjust_total");
					itemValueMap.put("adjust_total", adjust_total+total);//数量相加
				}else{
					Map<String, Object> newItemValueMap = new HashMap<String, Object>();
					newItemValueMap.put("depot_id", itemMap.get("depot_id"));
					newItemValueMap.put("depot_code", itemMap.get("depot_code"));
					newItemValueMap.put("depot_type", itemMap.get("depot_type"));
					newItemValueMap.put("goods_id", itemMap.get("goods_id"));
					newItemValueMap.put("goods_no", itemMap.get("goods_no"));
					newItemValueMap.put("old_batch_no", null);
					newItemValueMap.put("is_damage", itemMap.get("is_damage"));
					newItemValueMap.put("isExpire", isExpire);
					newItemValueMap.put("tallying_unit", itemMap.get("tallying_unit"));
					newItemValueMap.put("adjust_total", itemMap.get("adjust_total"));
					mergeMap.put(key1, newItemValueMap);
				}
			}
		}
		//遍历合并好的Map组装成list
	    for(Map<String, Object> map:mergeMap.values()){
	    	mergeList.add(map);	
	    }   
		return mergeList;
	}
	/**
	 * 修改备注和归属数据
	 * 
	 * @param
	 */
	@ApiOperation(value = "修改备注和归属数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", required = true),
			@ApiImplicitParam(name = "sourceTime", value = "归属时间", required = true)
	})	
	@PostMapping(value="/modfiyRemarkAndSourceTimeById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> modfiyRemarkAndSourceTimeById(String token,Long id,String remark,String sourceTime) {
		ResultDTO resultDTO = new ResultDTO();
		try {			
			AdjustmentInventoryModel model = new AdjustmentInventoryModel();
			model.setId(id);
			model = adjustmentInventoryService.searchByModel(model);
			if(model==null){
				resultDTO.setCode("01");
				resultDTO.setMessage("调整单不存在");
				return WebResponseFactory.responseBuild("99997",resultDTO.getMessage());//已知异常
			}
			if(model.getStatus().equals(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020)&&!StringUtils.isEmpty(sourceTime)){
				sourceTime = sourceTime+" 00:00:00";
				model.setSourceTime(Timestamp.valueOf(sourceTime));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				String months = sdf.format(model.getSourceTime());
				model.setMonths(months);
			}
			model.setRemark(remark);
			adjustmentInventoryService.modify(model);
			
			resultDTO.setCode("00");
			resultDTO.setMessage("保存成功");
		
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}


	/**
	 * 分配事业部加载单据详情
	 */
	@ApiOperation(value = "分配事业部加载单据详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/getDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AdjustmentInventoryDTO> getDetails(String token,Long id) throws Exception {
		AdjustmentInventoryDTO inventoryDto = new AdjustmentInventoryDTO();
		try {
			inventoryDto.setId(id);
			inventoryDto = adjustmentInventoryService.getDetails(inventoryDto);			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,inventoryDto);
	}

	/**
	 * @Description 保存库存调整单表体事业部信息，并校验是否推送库存
	 * @Param
	 * @return
	 */
	@ApiOperation(value = "保存库存调整单表体事业部信息并推送库存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "库存调整单表体事业部信息json串", required = true)
	})
	@PostMapping(value="/saveBuDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> saveBuDetails(String token,String json) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("itemList",AdjustmentInventoryItemDTO.class);
			AdjustmentInventoryDTO dto = (AdjustmentInventoryDTO) JSONObject.toBean(jsonData, AdjustmentInventoryDTO.class, classMap);
			User user= ShiroUtils.getUserByToken(token);
			retMap = adjustmentInventoryService.saveBuDetails(dto, user);
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
	 * 根据查询条件导出库存调整单
	 */
	@ApiOperation(value = "根据查询条件导出库存调整单",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportAdjustmentInventory.asyn")
	public ResponseBean exportAdjustmentInventory(AdjustmentInventoryForm form, HttpServletResponse response, HttpServletRequest request) {
		try {
			AdjustmentInventoryDTO dto = new AdjustmentInventoryDTO();
			
			User user= ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			dto.setMerchantName(form.getMerchantName());
			dto.setModel(form.getModel());
			dto.setStatus(form.getStatus());
			dto.setSource(form.getSource());
			dto.setSourceStartTime(form.getSourceStartTime());
			dto.setSourceEndTime(form.getSourceEndTime());
			dto.setIds(form.getIds());

			//表头信息
			List<Map<String,Object>> orderList = adjustmentInventoryService.listForExport(dto);

			//表头信息
			String sheetName1 = "基本信息";
			String[] columns1 = {"库存调整单号","单据状态","业务类别","来源单据号","调整仓库","调整时间",
					"归属月份","归属日期","创建人","创建时间","备注"};
			String[] keys1 = {"code","status","model","source_code","depot_name","adjustment_time",
					"months","source_time","create_username","create_date","remark"};

			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, orderList);
			
			//商品信息
			List<Map<String,Object>> itemList = adjustmentInventoryService.listForExportItem(dto);

			String sheetName2 = "商品信息";
			String[] columns2 = {"库存调整单号","事业部","库位类型","PO单号","商品货号","商品名称","商品条形码","调整类型",
					"库存类型","总调整数量","原始批次号","生产日期","失效日期","结算单价","理货单位"};
			String[] keys2 = {"code","bu_name", "stock_location_type_name","po_no","goods_no","goods_name","barcode","type",
					"is_damage","adjust_total","old_batch_no","production_date","overdue_date","settlement_price",
					"tally_unit"};
			
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);
			
			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;
			
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request,"库存调整单");
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
