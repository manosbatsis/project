package com.topideal.storage.web.adjustmenttype;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.dto.AdjustmentTypeItemDTO;
import com.topideal.storage.service.adjustmenttype.AdjustmentTypeService;
import com.topideal.storage.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * 类型调整 控制层
 */
@RequestMapping("/adjustmentType")
@Controller
public class AdjustmentTypeController {
	
	@Autowired
	private AdjustmentTypeService adjustmentTypeService;
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/typeadjust/adjustmenttype-list";
	}

	/**
	 * 访问导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws Exception {
		return "/derp/typeadjust/adjustmenttype-import";
	}

	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Long id, Model model) throws Exception {
		AdjustmentTypeDTO typeModel = new AdjustmentTypeDTO();
		typeModel.setId(id);
		typeModel = adjustmentTypeService.getDetails(typeModel);
		model.addAttribute("detail", typeModel);
		String page="/derp/typeadjust/adjustmenttype-details";
		if(typeModel.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2)){//货号变更
			page="/derp/typeadjust/adjustmenttype-details-h";
		}else if(typeModel.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3)){//效期调整
			page="/derp/typeadjust/adjustmenttype-details-x";
		}else if(typeModel.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4)){//大货理货
			page="/derp/typeadjust/adjustmenttype-details-d";
		}else if (typeModel.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_5)) {// 自然过期
			page="/derp/typeadjust/adjustmenttype-details-z";
		}
		
		return page;
	}
	
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listAdjustment.asyn")
	@ResponseBody
	private ViewResponseBean listAdjustmentType(AdjustmentTypeDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = adjustmentTypeService.listByPage(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 获取调减商品分组统计数量
	 * @param id 盘点结果单ids
	 * */
	@RequestMapping("/getAdjustmentTypeSum.asyn")
	@ResponseBody
	private ViewResponseBean getAdjustmentTypeSum(String id) {
		//返回的结果集
		Map resultMap = new HashMap();
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
			List<Map<String, Object>> itemDTOList = new ArrayList<>();
			for (String key : mergeMap.keySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("depot_id", typeModel.getDepotId());
				map.put("goods_id", mergeMap.get(key).getGoodsId());
				map.put("goods_no", mergeMap.get(key).getGoodsNo());
				map.put("depot_code", typeModel.getDepotCode());
				map.put("depot_type", typeModel.getDepotType());
				map.put("is_damage", mergeMap.get(key).getIsDamage());
				map.put("isExpire", mergeMap.get(key).getIsExpire());
				map.put("batch_no",mergeMap.get(key).getOldBatchNo() );
				map.put("deficient_num", mergeMap.get(key).getAdjustTotal());
				map.put("tallying_unit", mergeMap.get(key).getTallyingUnit());
				itemDTOList.add(map);
			}
			resultMap.put("mergeList", itemDTOList);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 查询某个类型调整详情
	 * @param
	 */
	@RequestMapping("/toGoodsDetailById.asyn")
	@ResponseBody
	public ViewResponseBean toGoodsDetailById(String id){
		List<AdjustmentTypeItemDTO> itemList = new ArrayList<AdjustmentTypeItemDTO>();
		try{
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(id).empty();
			if(isNull){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			AdjustmentTypeDTO typeModel = new AdjustmentTypeDTO();
			typeModel.setId(Long.valueOf(id));
			typeModel = adjustmentTypeService.getDetails(typeModel);
			itemList = typeModel.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(itemList);
	}
	/**
	 * 保存库存调整单表体事业部信息，并校验是否推送库存
	 * @param json
	 * @return
	 */
	@RequestMapping("/confirmInDepot.asyn")
	@ResponseBody
	public ViewResponseBean confirmInDepot(String json) {
		Map<String, String> retMap = new HashMap<>();
		try {
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("itemList",AdjustmentTypeItemDTO.class);
			AdjustmentTypeDTO dto = (AdjustmentTypeDTO) JSONObject.toBean(jsonData, AdjustmentTypeDTO.class, classMap);
			User user= ShiroUtils.getUser();
			retMap = adjustmentTypeService.confirmInDepot(user, dto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 导入
	 * @param file
	 * @return
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
			resultMap = adjustmentTypeService.importAdjustment(sheetList, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
	/**
	 *  删除
	 * @param ids
	 * @return
	 */
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
			adjustmentTypeService.deleteByIds(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 确认调整
	 * @param id
	 * @return
	 */
	@RequestMapping("/confirmAdjustment.asyn")
	@ResponseBody
	public ViewResponseBean confirmAdjustment(String id) {
		try {
			// 校验id是否有值
			if (StringUtils.isEmpty(id)) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Long idLong = Long.valueOf(id);
			User user= ShiroUtils.getUser();
			boolean bl = adjustmentTypeService.confirmAdjustment(idLong,user);
			if(!bl){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 根据查询条件导出类型调整单
	 */
	@RequestMapping("/exportAdjustmentType.asyn")
	public void exportAdjustmentType(AdjustmentTypeDTO dto, HttpServletResponse response, HttpServletRequest request) {
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

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
		String[] columns2 = {"调整单号","事业部","商品货号","商品名称","商品条形码","原始批次号","生产日期" ,
				"失效日期","调整数量","库存类型","调整类型","是否过期","理货单位"};
		String[] keys2 = {"code","bu_name","goods_no","goods_name","barcode","old_batch_no",
				"production_date","overdue_date","adjust_total","is_damage","type","is_expire","tallying_unit"};
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);
		
		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request,"类型调整单");
	}
}
