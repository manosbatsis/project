package com.topideal.storage.webapi.takesstock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.storage.service.takesstock.TakesStockResultItemService;
import com.topideal.storage.service.takesstock.TakesStockResultService;
import com.topideal.storage.shiro.ShiroUtils;
import com.topideal.storage.webapi.dto.MergeDTO;
import com.topideal.storage.webapi.dto.ResultDTO;
import com.topideal.storage.webapi.form.TakesStockResultsForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;

/**
 * webapi盘点结果
 * @date 2021-02-05
 */
@RestController
@RequestMapping("/webapi/storage/takesstockresult")
@Api(tags = "盘点结果")
public class APITakesStockResultController {
	
	@Autowired
	public TakesStockResultService takesStockResultService;
	@Autowired
	public TakesStockResultItemService takesStockResultItemService;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	/**
	 * 获取分页数据
	 * @param
	 * */
	@ApiOperation(value = "盘点结果列表查询")	
	@PostMapping(value="/listTakesStockResult.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<TakesStockResultsDTO> listTakesStockResult(TakesStockResultsForm form) {
		TakesStockResultsDTO dto = new TakesStockResultsDTO();
		try{
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(org.apache.commons.lang3.StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setMerchantName(form.getMerchantName());
			dto.setServerType(form.getServerType());
			dto.setStatus(form.getStatus());
			dto.setModel(form.getModel());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			// 响应结果集
			dto = takesStockResultService.listTakesStockResultPage(dto);
			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 访问详情页面
	 * @param model
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "盘点结果id", required = true)
	})
	@PostMapping(value="/toDetailPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<TakesStockResultsDTO> toDetailPage(String token,Model model,String id) throws Exception{
		TakesStockResultsDTO takesStockResult = null;
		try {
			//查询盘点申请
			takesStockResult = takesStockResultService.queryById(Long.valueOf(id));
			
			//查询盘点申请详情
			TakesStockResultItemDTO param = new TakesStockResultItemDTO();
			param.setTakesStockResultId(takesStockResult.getId());
			List<TakesStockResultItemDTO> itemList = takesStockResultItemService.list(param);
			
			takesStockResult.setDetails(itemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,takesStockResult);
	}
	
	/**
     * 盘点结果-确认、驳回
     * @param confirmType 10-驳回 20-确认
     */
	@ApiOperation(value = "盘点结果-确认、驳回")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "盘点结果ids(多选用逗号隔开)", required = true),
			@ApiImplicitParam(name = "confirmType", value = "盘点结果类型 10-驳回 20-确认", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=200,message="data => 指令提交失败单号")
	})
	@PostMapping(value="/confirmTakesStock.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<String> confirmTakesStock(String token,String ids,String confirmType){
		String failCode = null;
		try{
			//校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if(!isRight){
	            //输入信息不完整
	            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//数据格式不正确
	        }
	        if (org.apache.commons.lang.StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
	        User user = ShiroUtils.getUserByToken(token);
			
			//发送调拨
			failCode = takesStockResultService.updateConfirmTakesStock(user.getId(), user.getName(), user.getTopidealCode(), ids, confirmType);
	    	
			if(StringUtils.isEmpty(failCode)) {
				 return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			}
			
			return WebResponseFactory.responseBuild("99997",failCode);//已知异常
		 } catch (Exception e) {
			 e.printStackTrace();
			 return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		 }	
		
    }

	/**
	 * 导入
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importTakesStockResult.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importTakesStockResult(String token,@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		try {
			// 返回的结果集
			Map resultMap = new HashMap();
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}
			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (data == null) {
				// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = takesStockResultService.importTakesStockResult(data, user);
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
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 按 仓库id、商品id、好坏品、是否过期、批次合并盘亏数量
	 * */
	private List<Map<String, Object>> mergeItem(List<Map<String, Object>> itemList){
		List<Map<String, Object>> mergeList = new ArrayList<Map<String,Object>>();

		/**合并
		 * key=仓库id、商品id、是否坏品、是否过期、理货单位(香港仓)、批次
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
				isExpire = TimeUtils.isNotIsExpireByDate(overdueDate);
			}
			//理货单位默认为空，香港仓时才需要
			String tallyingUnit = "";
			String depotType = (String) itemMap.get("depot_type");
			if(depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				tallyingUnit = (String) itemMap.get("tally_unit");
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
			}
			itemMap.put("tallying_unit", tallyingUnit);
			//批次
			String batchNo = (String) itemMap.get("batch_no");
			if(StringUtils.isEmpty(batchNo)){
				batchNo = "";
			}
			String key = depotId + goodsId + isDamage + isExpire + tallyingUnit + batchNo;

			if (mergeMap.get(key) != null) {
				Map<String, Object> itemValueMap = mergeMap.get(key);
				int adjust_total = (int) itemValueMap.get("deficient_num");
				int total = (int) itemMap.get("deficient_num");
				//数量相加
				itemValueMap.put("deficient_num", adjust_total+total);
			}else{
				Map<String, Object> newItemValueMap = new HashMap<String, Object>();
				newItemValueMap.put("depot_id", itemMap.get("depot_id"));
				newItemValueMap.put("depot_code", itemMap.get("depot_code"));
				newItemValueMap.put("depot_type", itemMap.get("depot_type"));
				newItemValueMap.put("goods_id", itemMap.get("goods_id"));
				newItemValueMap.put("goods_no", itemMap.get("goods_no"));
				String batch_no = (String) itemMap.get("batch_no");
				if(StringUtils.isEmpty(batch_no)) {
					batch_no = null;
				}
				newItemValueMap.put("batch_no",batch_no);
				newItemValueMap.put("is_damage", itemMap.get("is_damage"));
				newItemValueMap.put("isExpire", isExpire);
				newItemValueMap.put("tallying_unit", itemMap.get("tally_unit"));
				newItemValueMap.put("deficient_num", itemMap.get("deficient_num"));
				mergeMap.put(key, newItemValueMap);
			}
		}
		//遍历合并好的Map组装成list
		for(Map<String, Object> map:mergeMap.values()){
			mergeList.add(map);
		}
		return mergeList;
	}

	/**
	 * 获取调减商品分组统计数量
	 * @param ids 盘点结果单ids
	 * */
	@ApiOperation(value = "获取调减商品分组统计数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的盘点结果ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/getTakesStockResultSum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<MergeDTO>> getTakesStockResultSum(String token ,String ids) {
		List<MergeDTO> resultList = new ArrayList<MergeDTO>();//返回结果集
		try{		
	        if (org.apache.commons.lang.StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			String[] idArr = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for(String id:idArr){
				idList.add(Long.valueOf(id));
			}
			//查询明细
			List<Map<String, Object>> itemList = takesStockResultService.getItemByResultIds(idList);
			//合并好的明细
			List<Map<String, Object>> mergeList = new ArrayList<Map<String,Object>>();;
			if (itemList != null && itemList.size() > 0) {
				for (Map<String, Object> itemMap : itemList) {
					//获取仓库编码、仓库类型
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("depotId", itemMap.get("depot_id"));
					DepotInfoMongo depot = depotInfoMongoDao.findOne(map);
					itemMap.put("depot_code", depot.getDepotCode());
					itemMap.put("depot_type", depot.getType());//仓库类型
				}
				//合并相同项
				mergeList = mergeItem(itemList);
			}
			for (Map<String, Object> map : mergeList) {
				MergeDTO mergeDTO = new MergeDTO();
				mergeDTO.setDepot_id((String)map.get("depot_id"));
				mergeDTO.setDepot_code((String)map.get("depot_code"));
				mergeDTO.setDepot_type((String)map.get("depot_type"));
				mergeDTO.setGoods_id((String)map.get("goods_id"));
				mergeDTO.setGoods_no((String)map.get("goods_no"));
				mergeDTO.setBatch_no((String)map.get("batch_no"));
				mergeDTO.setIs_damage((String)map.get("is_damage"));
				mergeDTO.setIsExpire((String)map.get("isExpire"));
				mergeDTO.setTallying_unit((String)map.get("tallying_unit"));
				mergeDTO.setAdjust_total((String)map.get("deficient_num"));
				
				resultList.add(mergeDTO);
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	/**
	 * 查询某个盘点申请详情
	 * @param
	 */
	@ApiOperation(value = "查询某个盘点申请详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "盘点结果id", required = true)
	})
	@PostMapping(value="/toGoodsDetailById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<TakesStockResultItemDTO>> toGoodsDetailById(String token,String id){
		List<TakesStockResultItemDTO> itemList = new ArrayList<TakesStockResultItemDTO>();
		try{
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(id).empty();
			if(isNull){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			//查询盘点申请详情
			TakesStockResultItemDTO param = new TakesStockResultItemDTO();
			param.setTakesStockResultId(Long.valueOf(id));
			itemList = takesStockResultItemService.list(param);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,itemList);
	}
	/**
	 * 保存盘点结果单表体事业部信息，并校验是否推送库存
	 * @param json
	 * @return
	 */
	@ApiOperation(value = "保存盘点结果单表体事业部信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "盘点结果单表体事业部信息json串", required = true)
	})
	@PostMapping(value="/confirmInDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> confirmInDepot(String token,String json) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("details",TakesStockResultItemDTO.class);
			TakesStockResultsDTO dto = (TakesStockResultsDTO) JSONObject.toBean(jsonData, TakesStockResultsDTO.class, classMap);
			User user= ShiroUtils.getUserByToken(token);
			retMap = takesStockResultService.confirmInDepot(user, dto);
			
			resultDTO.setCode(retMap.get("code"));
			resultDTO.setMessage(retMap.get("message"));
			
			if(retMap.get("code").equals("01")) {				
				return WebResponseFactory.responseBuild("99997",retMap.get("message"));//已知异常
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}

	/**
	 * 根据查询条件导出盘点结果单
	 */
	@ApiOperation(value = "根据查询条件导出盘点结果单",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportTakesStockResult.asyn")
	public ResponseBean exportTakesStockResult(TakesStockResultsForm form, HttpServletResponse response, HttpServletRequest request) {
		try {
			TakesStockResultsDTO dto = new TakesStockResultsDTO();
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(org.apache.commons.lang3.StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setMerchantName(form.getMerchantName());
			dto.setServerType(form.getServerType());
			dto.setStatus(form.getStatus());
			dto.setModel(form.getModel());
			dto.setIds(form.getIds());
			//表头信息
			List<Map<String,Object>> orderList = takesStockResultService.listForExport(dto);

			//表头信息
			String sheetName1 = "基本信息";
			String[] columns1 = {"盘点单号","盘点指令单号", "单据状态", "盘点仓库","服务类型","业务场景","单据日期",
					"创建人","入库确认人","入库确认时间","备注"};
			String[] keys1 = {"code","takes_stock_code","status","depot_name","server_type","model","source_time",
					"create_username","in_confirm_username","in_confirm_time","remark"};

			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, orderList);
			
			//商品信息
			List<Map<String,Object>> itemList = takesStockResultService.listForExportItem(dto);

			String sheetName2 = "商品信息";
			String[] columns2 = {"盘点单号","事业部", "库存类型","商品货号","商品名称","商品条码","结算单价","盘盈数量","盘亏数量","调整类型",
					"批次号","是否坏品","总调整数量", "生产日期","失效日期","理货单位", "盘点原因"};
			String[] keys2 = {"code","bu_name","stock_location_type_name","goods_no","goods_name","barcode","settlement_price","surplus_num","deficient_num",
					"type","batch_no","is_damage","adjust_total","production_date","overdue_date","tally_unit","reason_des"};
			
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);
			
			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;
			
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request,"盘点结果导出");
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
}
