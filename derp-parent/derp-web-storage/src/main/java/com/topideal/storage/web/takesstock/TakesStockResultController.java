package com.topideal.storage.web.takesstock;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.storage.service.takesstock.TakesStockResultItemService;
import com.topideal.storage.service.takesstock.TakesStockResultService;
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
 * 盘点指令
 * @author yucaifu
 */
@RequestMapping("/takesstockresult")
@Controller
public class TakesStockResultController {
	
	@Autowired
	public TakesStockResultService takesStockResultService;
	@Autowired
	public TakesStockResultItemService takesStockResultItemService;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	/**
	 * 访问列表页面
	 * @param model
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception{
		List<DepotInfoMongo> depotList = new ArrayList<DepotInfoMongo>();
		//根据仓库编码查询黄埔仓、南沙仓(综合1、综合2)
		/*Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("code", "WMS_360_04");//综合1
		depotList.add(depotInfoMongoDao.findOne(paramMap));
		paramMap.put("code", "WMS_360_05");//综合2
		depotList.add(depotInfoMongoDao.findOne(paramMap));
		paramMap.put("code", "WMS_360_06");//黄埔卓志保税仓
		depotList.add(depotInfoMongoDao.findOne(paramMap));*/
		
		model.addAttribute("depotList", depotList);
		return "derp/takesstock/takesstockresult-list";
	}
	
	/**
	 * 获取分页数据
	 * @param dto
	 * */
	@RequestMapping("/listTakesStockResult.asyn")
	@ResponseBody
	private ViewResponseBean listTakesStockResult(TakesStockResultsDTO dto) {
		try{
			User user = ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			
			// 响应结果集
			dto = takesStockResultService.listTakesStockResultPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 访问详情页面
	 * @param model
	 * */
	@RequestMapping("/toDetailPage.html")
	public String toDetailPage(Model model,String id) throws Exception{
		//查询盘点申请
		TakesStockResultsDTO takesStockResult = takesStockResultService.queryById(Long.valueOf(id));
		
		//查询盘点申请详情
		TakesStockResultItemDTO param = new TakesStockResultItemDTO();
		param.setTakesStockResultId(takesStockResult.getId());
		List<TakesStockResultItemDTO> itemList = takesStockResultItemService.list(param);
		
		model.addAttribute("takesStockResult", takesStockResult);
		model.addAttribute("itemList", itemList);
		return "derp/takesstock/takesstockresult-detail";
	}
	
	/**
     * 盘点结果-确认、驳回
     * @param confirmType 10-驳回 20-确认
     */
	@RequestMapping("/confirmTakesStock.asyn")
	@ResponseBody
    public ViewResponseBean confirmTakesStock(String ids,String confirmType){
		Map<String, String> data = new HashMap<String, String>();
		try{
			//校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if(!isRight){
	            //输入信息不完整
	            return ResponseFactory.error(StateCodeEnum.ERROR_303);
	        }
	        User user = ShiroUtils.getUser();
			
			//发送调拨
			String failCode = takesStockResultService.updateConfirmTakesStock(user.getId(), user.getName(), user.getTopidealCode(), ids, confirmType);
	    	data.put("failCode",failCode);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		 }	
		 return ResponseFactory.success(data);	
    }

	/**
	 * 访问导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws Exception {
		return "/derp/takesstock/takesstockresult-import";
	}


	/**
	 * 导入
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/importTakesStockResult.asyn")
	@ResponseBody
	public ViewResponseBean importTakesStockResult(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		// 返回的结果集
		Map resultMap = new HashMap();
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (data == null) {
				// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user = ShiroUtils.getUser();
			resultMap = takesStockResultService.importTakesStockResult(data, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 判断单据归属日期是否大于关账日期/月结日期
	 * @param ids 盘点结果单ids
	 * */
	@RequestMapping("/checkSourceTime.asyn")
	@ResponseBody
	private ViewResponseBean checkSourceTime(String ids) {
		//返回的结果集
		Map resultMap = new HashMap();
		try{
			String[] idArr = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for(String id:idArr){
				idList.add(Long.valueOf(id));
			}
			String codes = takesStockResultService.checkSourceTime(idList);
			resultMap.put("codes", codes);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
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
	@RequestMapping("/getTakesStockResultSum.asyn")
	@ResponseBody
	private ViewResponseBean getTakesStockResultSum(String ids) {
		//返回的结果集
		Map resultMap = new HashMap();
		try{
			String[] idArr = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for(String id:idArr){
				idList.add(Long.valueOf(id));
			}
			//查询明细
			List<Map<String, Object>> itemList = takesStockResultService.getItemByResultIds(idList);
			//合并好的明细
			List<Map<String, Object>> mergeList = null;
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
			resultMap.put("mergeList", mergeList);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 查询某个盘点申请详情
	 * @param
	 */
	@RequestMapping("/toGoodsDetailById.asyn")
	@ResponseBody
	public ViewResponseBean toGoodsDetailById(String id){
		List<TakesStockResultItemDTO> itemList = new ArrayList<TakesStockResultItemDTO>();
		try{
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(id).empty();
			if(isNull){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			//查询盘点申请详情
			TakesStockResultItemDTO param = new TakesStockResultItemDTO();
			param.setTakesStockResultId(Long.valueOf(id));
			itemList = takesStockResultItemService.list(param);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(itemList);
	}
	/**
	 * 保存盘点结果单表体事业部信息，并校验是否推送库存
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
			classMap.put("details",TakesStockResultItemDTO.class);
			TakesStockResultsDTO dto = (TakesStockResultsDTO) JSONObject.toBean(jsonData, TakesStockResultsDTO.class, classMap);
			User user= ShiroUtils.getUser();
			retMap = takesStockResultService.confirmInDepot(user, dto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 根据查询条件导出盘点结果单
	 */
	@RequestMapping("/exportTakesStockResult.asyn")
	public void exportTakesStockResult(TakesStockResultsDTO dto, HttpServletResponse response, HttpServletRequest request) {
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

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
		String[] columns2 = {"盘点单号","事业部","商品货号","商品名称","商品条码","结算单价","盘盈数量","盘亏数量","调整类型",
				"批次号","是否坏品","总调整数量", "生产日期","失效日期","理货单位", "盘点原因"};
		String[] keys2 = {"code","bu_name","goods_no","goods_name","barcode","settlement_price","surplus_num","deficient_num",
				"type","batch_no","is_damage","adjust_total","production_date","overdue_date","tally_unit","reason_des"};
		
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);
		
		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request,"盘点结果导出");
	}
}
