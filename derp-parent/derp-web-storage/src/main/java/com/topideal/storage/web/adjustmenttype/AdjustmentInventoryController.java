package com.topideal.storage.web.adjustmenttype;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
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

import net.sf.json.JSONObject;

/**
 * 库存调整 控制层
 */
@RequestMapping("/adjustmentInventory")
@Controller
public class AdjustmentInventoryController {

	@Autowired
	private AdjustmentInventoryService adjustmentInventoryService;
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/typeadjust/adjustmentinventory-list";
	}

	/**
	 * 访问导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws Exception {
		return "/derp/typeadjust/adjustmentinventory-import";
	}

	/**
	 * 详情
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Long id, Model model) throws Exception {
		User user = ShiroUtils.getUser();
		AdjustmentInventoryDTO inventoryDto = new AdjustmentInventoryDTO();
		inventoryDto.setId(id);
		inventoryDto = adjustmentInventoryService.getDetails(inventoryDto);
		// 如果不是月结直接返回
		if (!DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2.equals(inventoryDto.getModel())) {
			model.addAttribute("detail", inventoryDto);
			return "/derp/typeadjust/adjustmentinventory-details";
		} else {// 月结商品合并
			List<AdjustmentInventoryItemDTO> itemList = inventoryDto.getItemList();
			// 用于库存调整单的合并
			Map<String, AdjustmentInventoryItemDTO> itemMap = new HashMap<>();
			for (AdjustmentInventoryItemDTO item : itemList) {
//				Timestamp overdueDate = item.getOverdueDate();
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
			//List<AdjustmentInventoryItemModel> valuesList = (List<AdjustmentInventoryItemModel>) itemMap.values();
			inventoryDto.setItemList(valuesList);
			model.addAttribute("detail", inventoryDto);
			return "/derp/typeadjust/yuejieAdjustmentinventory-details";
		}
						
				
	}

	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listAdjustment.asyn")
	@ResponseBody
	private ViewResponseBean listAdjustmentInventory(AdjustmentInventoryDTO dto) {
		try {
			User user = ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = adjustmentInventoryService.listByPage(dto);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 审核库存调整
	 */
	@RequestMapping("/auditAdjustment.asyn")
	@ResponseBody
	private ViewResponseBean auditAdjustment(String ids) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			User user = ShiroUtils.getUser();
			// 响应结果集
			result = adjustmentInventoryService.auditAdjustment(list, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteAdjustment.asyn")
	@ResponseBody
	public ViewResponseBean deleteAdjustment(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			adjustmentInventoryService.deleteByIds(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 导入
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/importAdjustment.asyn")
	@ResponseBody
	public ViewResponseBean importGoods(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<List<Map<String, String>>> sheetList = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (sheetList == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user = ShiroUtils.getUser();
			resultMap = adjustmentInventoryService.importAdjustment(sheetList, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 根据参数获取库存调整单信息
	 * @param sourceCode 来源单据号
	 * @param currentDate 当前时间
	 */
	@RequestMapping("/getAdjustmentStatus.asyn")
	@ResponseBody
	public ViewResponseBean getAdjustmentStatus(String sourceCode, String currentDate) {
		AdjustmentInventoryModel model = new AdjustmentInventoryModel();
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(sourceCode).addObject(currentDate).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			model.setSourceCode(sourceCode);
			model.setMonths(currentDate);
			model = adjustmentInventoryService.searchByModel(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	/**
	 * 获取调减商品分组统计数量
	 * @param Ids 调整单id
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getAdjustmentSum.asyn")
	@ResponseBody
	private ViewResponseBean getAdjustmentSum(String ids) {
		Map resultMap = new HashMap();//返回的结果集
		try{
			//新
			String[] idsStr = ids.split(",");
			List<Long> idarr = new ArrayList<Long>();
			for(String id:idsStr){
				idarr.add(Long.valueOf(id));
			}
			//查询明细
			User user = ShiroUtils.getUser();
			List<Map<String, Object>> itemList = adjustmentInventoryService.getItemByInventoryIds(idarr,user);
			List<Map<String, Object>> mergeList = null;//合并好的明细
			if(itemList!=null&&itemList.size()>0){
				//合并
				mergeList = mergeItem(itemList);
			}
			resultMap.put("mergeList", mergeList);
		  }catch(Exception e){
	     	e.printStackTrace();
	         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
	     }
     return ResponseFactory.success(resultMap);
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
	@RequestMapping("/modfiyRemarkAndSourceTimeById.asyn")
	@ResponseBody
	public ViewResponseBean modfiyRemarkAndSourceTimeById(Long id,String remark,String sourceTime) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			
			AdjustmentInventoryModel model = new AdjustmentInventoryModel();
			model.setId(id);
			model = adjustmentInventoryService.searchByModel(model);
			if(model==null){
				retMap.put("code", "01");
				retMap.put("message", "调整单不存在");
				return ResponseFactory.success(retMap);
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
			
			retMap.put("code", "00");
			retMap.put("message", "保存成功");
		
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}


	/**
	 * 分配事业部加载单据详情
	 */
	@RequestMapping("/getDetails.asyn")
	@ResponseBody
	public ViewResponseBean getDetails(Long id) throws Exception {
		AdjustmentInventoryDTO inventoryDto = new AdjustmentInventoryDTO();
		inventoryDto.setId(id);
		inventoryDto = adjustmentInventoryService.getDetails(inventoryDto);
		return ResponseFactory.success(inventoryDto);
	}

	/**
	 * @Description 保存库存调整单表体事业部信息，并校验是否推送库存
	 * @Param
	 * @return
	 */
	@RequestMapping("/saveBuDetails.asyn")
	@ResponseBody
	public ViewResponseBean saveBuDetails(String json) {
		Map<String, String> retMap = new HashMap<>();
		try {
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("itemList",AdjustmentInventoryItemDTO.class);
			AdjustmentInventoryDTO dto = (AdjustmentInventoryDTO) JSONObject.toBean(jsonData, AdjustmentInventoryDTO.class, classMap);
			User user= ShiroUtils.getUser();
			retMap = adjustmentInventoryService.saveBuDetails(dto, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 根据查询条件导出库存调整单
	 */
	@RequestMapping("/exportAdjustmentInventory.asyn")
	public void exportAdjustmentInventory(AdjustmentInventoryDTO dto, HttpServletResponse response, HttpServletRequest request) {
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

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
		String[] columns2 = {"库存调整单号","事业部","PO单号","商品货号","商品名称","商品条形码","调整类型",
				"库存类型","总调整数量","原始批次号","生产日期","失效日期","结算单价","理货单位"};
		String[] keys2 = {"code","bu_name","po_no","goods_no","goods_name","barcode","type",
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
	}
}
