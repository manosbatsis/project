package com.topideal.report.webapi.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.report.service.reporting.RetailAdminService;
import com.topideal.report.shiro.ShiroUtils;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应链周报控制器
 *
 */
@RestController
@RequestMapping("/webapi/report/retailAdmin")
@Api(tags = "销售洞察")
public class APIRetailAdminController {

	private static final Logger LOGGER = Logger.getLogger(APIRetailAdminController.class);

	@Autowired
	private RetailAdminService  retailAdminService;

	@ApiOperation(value = "获取事业部信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getBuList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<BusinessUnitModel>> getBuList(String token) throws SQLException {
		List<BusinessUnitModel> buList = new ArrayList<BusinessUnitModel>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			buList = retailAdminService.getBuList(user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buList);
	}
	@ApiOperation(value = "仓库滞销预警获取仓库信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
	@PostMapping(value="/getDepotList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String,Object>>> getDepotList(String token, String month) throws Exception {
		List<Map<String,Object>> depotList = new ArrayList<Map<String,Object>>();
		try {
			depotList = retailAdminService.getDepotList(month);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,depotList);
	}
	@ApiOperation(value = "仓库效期预警获取品牌信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
	@PostMapping(value="/getBrandList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String,Object>>> getBrandList(String token, String month) throws Exception {
		List<Map<String,Object>> brandList = new ArrayList<Map<String,Object>>();
		try {
			brandList = retailAdminService.getBrandList(month);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,brandList);
	}

	@ApiOperation(value = "事业群销售达成")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "departmentId", value = "部门id", required = false),
			@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
	@PostMapping(value="/getTargetAndAchieve.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map> getTargetAndAchieve(String token,Long departmentId, String month) throws SQLException {
		List<Map<String, Object>> resultList = new ArrayList<>() ;// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultList = retailAdminService.getTargetAndAchieve(departmentId,month,user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	@ApiOperation(value = "品牌销量TOP8")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部", required = false),
		@ApiImplicitParam(name = "month", value = "月份", required = true),
		@ApiImplicitParam(name = "isParentBrand", value = "是否按母品牌查询，1-按子品牌，2-按母品牌", required = true)
	})
   	@PostMapping(value="/getBrandSaleTop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleDataDTO>> getBrandSaleTop(String token,Long buId, String month,String isParentBrand) throws SQLException {
		List<SaleDataDTO> list = null;
		try {
			SaleDataDTO dto = new SaleDataDTO();
			dto.setBuId(buId);
			dto.setMonth(month);
			dto.setIsParentBrand(isParentBrand);
			User user = ShiroUtils.getUserByToken(token);
			list = retailAdminService.getBrandSaleTop(dto,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
	}

	@ApiOperation(value = "客户销量TOP8")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部"),
		@ApiImplicitParam(name = "month", value = "月份", required = true),
		@ApiImplicitParam(name = "channelType", value = "渠道类型：To B  ， To C")
	})
   	@PostMapping(value="/getCusSaleTop.asyn" ,consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleDataDTO>> getCusSaleTop(String token,Long buId, String month, String channelType) throws SQLException {
		List<SaleDataDTO> list = null;
		try {
			SaleDataDTO dto = new SaleDataDTO();
			dto.setBuId(buId);
			dto.setMonth(month);
			dto.setChannelType(channelType);
			User user = ShiroUtils.getUserByToken(token);
			list = retailAdminService.getCusSaleTop(dto,user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
	}


	@ApiOperation(value = "商品在库天数")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部"),
		@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
   	@PostMapping(value="/getInWarehouseData.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getInWarehouseData(String token,Long buId, String month) throws SQLException {
		List<Map<String, Object>> resultMap = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getInWarehouseData(buId,month,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}

	@ApiOperation(value = "库存量分析")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部"),
		@ApiImplicitParam(name = "month", value = "月份", required = true),
		@ApiImplicitParam(name = "type", value = "渠道类型 1-按品牌，2-按仓库，3-按公司", required = true)
	})
   	@PostMapping(value="/getInventoryAnalysisData.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getInventoryAnalysisData(String token,Long buId, String month, String type) throws SQLException {
		List<Map<String, Object>> resultMap = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getInventoryAnalysisData(buId,month,type,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	@ApiOperation(value = "年度进销存分析")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "年份", required = true),
			@ApiImplicitParam(name = "brandIds", value = "品牌id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "isParentBrand", value = "是否按母品牌查询，1-按子品牌，2-按母品牌", required = true)
	})
	@ApiResponses({
			@ApiResponse(code=10000,message = "purchaseAmount=> 采购金额, inventoryAmount=>库存金额, saleAmount=>销售金额, brandPurchase=>品牌采购结算金额")
	})
	@PostMapping(value="/getYearFinanceInventoryAnalysis.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getYearFinanceInventoryAnalysis(String token,Long buId, String month, String isParentBrand,String brandIds) throws SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getYearFinanceInventoryAnalysis(buId,month,isParentBrand,brandIds,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	@ApiOperation(value = "品牌在库天数")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "inWarehouseRange", value = "在库天数范围", required = true),
			@ApiImplicitParam(name = "isParentBrand", value = "是否按母品牌查询，1-按子品牌，2-按母品牌", required = true)
	})
	@PostMapping(value="/getBrandInWarehouse.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getBrandInWarehouse(String token,Long buId, String month, String inWarehouseRange ,String isParentBrand) throws SQLException {
		List<Map<String, Object>> resultMap = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getBrandInWarehouse(buId,month,user,inWarehouseRange,isParentBrand);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	@ApiOperation(value = "仓库滞销预警、仓库效期预警 获取公司")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getMerchantList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<MerchantInfoModel>> getMerchantList(){
		List<MerchantInfoModel> resultList = new ArrayList<>();// 返回的结果集
		try {
			resultList = retailAdminService.getMerchantList();
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}
	@ApiOperation(value = "仓库滞销预警")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "公司Id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "depotIds", value = "仓库id集合，多个用逗号隔开")
	})
	@PostMapping(value="/getDepotUnsellableWarning.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getDepotUnsellableWarning(String token,String merchantIds, String month, String depotIds){
		Map<String, Object> resultMap = new HashMap<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getDepotUnsellableWarning(merchantIds,month,depotIds,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	@ApiOperation(value = "仓库效期预警")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "公司id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "brandParentIds", value = "品牌id集合，多个用逗号隔开")
	})
	@PostMapping(value="/getDepotExpireWarning.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getDepotExpireWarning(String token,String merchantIds, String month, String brandParentIds){
		Map<String, Object> resultMap = new HashMap<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultMap = retailAdminService.getDepotExpireWarning(merchantIds,month,brandParentIds,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	@ApiOperation(value = "获取仓库滞销预警各品牌金额明细")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "公司id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "brandParentId", value = "品牌id", required = true),
			@ApiImplicitParam(name = "depotIds", value = "仓库id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "inWarehouseInterval", value = "在仓天数区间划分为：1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上", required = true)
	})
	@PostMapping(value="/getDepotUnsellableDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getDepotUnsellableDetail(String token,String merchantIds, String month, Long brandParentId,String inWarehouseInterval,String depotIds){
		List<Map<String, Object>> resultList = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUserByToken(token);
			resultList = retailAdminService.getDepotUnsellableDetail(merchantIds,month,brandParentId,inWarehouseInterval,depotIds,user);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	@ApiOperation(value = "导出事业部销售达成",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "departmentId", value = "部门"),
			@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
	@GetMapping(value="/ExportTargetAndAchieve.asyn")
	public void ExportTargetAndAchieve(String token,Long departmentId, String month, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "事业部销售达成导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getTargetAndAchieveExportList(departmentId,month,user);
			String[] COLUMNS = {"部门","项目组","母品牌","月份","币种","月度销售金额","月度销售目标","月度达成","年度销售金额","年度销售目标","年度达成"};
			String[] KEYS = {"departmentName","buName","parentBrandName","month","currency","monthAchieveAmount","monthTargetAmount",
					"monthRate","yearAchieveAmount","yearTargetAmount","yearRate"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@ApiOperation(value = "导出年度进销存分析",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "brandIds", value = "品牌id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "isParentBrand", value = "是否按母品牌查询，1-按子品牌，2-按母品牌")
	})
	@GetMapping(value="/exportYearFinanceInventoryAnalysis.asyn")
	public void exportYearFinanceInventoryAnalysis(String token,Long buId, String month,String isParentBrand,String brandIds, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "年度进销存分析导出";
			// 获取导出的信息
			List<BuFinanceInventorySummaryDTO> resultMap  = retailAdminService.getYearFinanceInventoryAnalysisExportList(buId,month,isParentBrand,brandIds,user);
			String[] COLUMNS = {"事业部","月份","母品牌","标准品牌","币种","采购金额","销售金额","库存金额"};
			String[] KEYS = {"buName","month","superiorParentBrand","brandName","currency","purchaseEndAmount","saleEndAmount","endAmount"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@ApiOperation(value = "导出客户销量",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "channelType", value = "渠道类型：To B  ， To C", required = true)
	})
	@GetMapping(value="/exportgetCustomers.asyn")
	public void exportCustomers(String token,Long buId, String month,String channelType, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			SaleDataDTO dto = new SaleDataDTO();
			dto.setBuId(buId);
			dto.setMonth(month);
			dto.setChannelType(channelType);
			User user = ShiroUtils.getUserByToken(token);
			String sheetName = "客户销量导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getCustomersExportList(dto,user);
			String[] COLUMNS = {"公司","事业部","仓库","客户","货号","条形码","商品名称","月份","母品牌","标准品牌","币种","销售金额"};
			String[] KEYS = {"merchantName","buName","outDepotName","customerName","goodsNo","barcode","goodsName","month", "superiorParentBrand",
					"parentBrand","currency","cynAmount"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@ApiOperation(value = "导出品牌销量",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "isParentBrand", value = "是否按母品牌查询，1-按子品牌，2-按母品牌", required = true)
	})
	@GetMapping(value="/exportBrand.asyn")
	public void exportBrand(String token,Long buId, String month,String isParentBrand, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			SaleDataDTO dto = new SaleDataDTO();
			dto.setBuId(buId);
			dto.setMonth(month);
			dto.setIsParentBrand(isParentBrand);
			User user = ShiroUtils.getUserByToken(token);
			String sheetName = "品牌销量导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getBrandExportList(dto,user);
			String[] COLUMNS = {"公司","事业部","仓库","客户","货号","条形码","商品名称","月份","母品牌","标准品牌","币种","销售金额"};
			String[] KEYS = {"merchantName","buName","outDepotName","customerName","goodsNo","barcode","goodsName","month", "superiorParentBrand",
					"parentBrand","currency","cynAmount"};// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@ApiOperation(value = "导出库存量分析",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "type", value = "渠道类型 1-按品牌，2-按仓库，3-按公司", required = true)
	})
	@GetMapping(value="/exportInventoryAnalysis.asyn")
	public void exportInventoryAnalysis(String token,Long buId, String month, String type, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "库存量分析导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getInventoryAnalysisExportList(buId,month,type,user);
			String[] COLUMNS = {"事业部","月份","公司","仓库","标准条码","标准品牌","母品牌","币种","库存金额","日均销量","预计库存清空天数"};
			String[] KEYS = {"buName","month","merchantName","depotName","commbarcode","brandParent","superiorParentBrand","currency","amount","dailySaleNum","clearDays"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@ApiOperation(value = "导出商品在库天数",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部"),
			@ApiImplicitParam(name = "month", value = "月份", required = true)
	})
	@GetMapping(value="/exportInWarehouseDays.asyn")
	public void exportInWarehouseDays(String token,Long buId, String month, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "商品在库天数导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getInWarehouseDaysExportList(buId,month,user);
			String[] COLUMNS = {"事业部","商品条码","标准条码","商品名称","标准品牌","二级品类","在库天数","在库数量","加权单价","加权金额"};
			String[] KEYS = {"buName","barcode","commbarcode","goodsName","brandName","typeName","totalWarehoseDays","totalNum","weightedPrice","weightedAmount"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@ApiOperation(value = "导出仓库滞销预警",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "公司集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "depotIds", value = "仓库id集合，多个用逗号隔开")
	})
	@GetMapping(value="/exportDepotUnsellableWarning.asyn")
	public void exportDepotUnsellableWarning(String token, String merchantIds, String month, String depotIds, HttpServletResponse response, HttpServletRequest request){
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "仓库滞销预警导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getDepotUnsellableWarningExportList(merchantIds,month,user,depotIds);
			String[] COLUMNS = {"公司","统计日期","仓库名称","商品货号","商品名称","商品条码","批次号","生产日期","有效期至","库存数量","库存类型","标准品牌","母品牌",
					"进仓日期","滞销天数","滞销天数区间","加权单价（港币）","库存金额（港币）","库存金额（人民币）"};
			String[] KEYS =  {"merchantName", "reportDate", "depotName", "goodsNo", "goodsName","barcode","batchNo", "productionDate", "overdueDate",
					"surplusNum","inverntoryType","brandParent", "superiorParentBrand", "createDate","inWarehouseDays", "inWarehouseInterval",
					"settlementPrice","totalAmount", "totalAmountCNY"} ;
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@ApiOperation(value = "导出仓库效期预警",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "公司集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "month", value = "月份", required = true),
			@ApiImplicitParam(name = "brandIds", value = "品牌id集合，多个用逗号隔开")
	})
	@GetMapping(value="/exportDepotExpireWarning.asyn")
	public void exportDepotExpireWarning(String token,String merchantIds, String month, String brandIds,HttpServletResponse response, HttpServletRequest request){
		try {
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "仓库效期预警导出";
			// 获取导出的信息
			List<Map<String,Object>> resultMap  = retailAdminService.getDepotExpireWarningExportList(merchantIds,month,user,brandIds);
			String[] COLUMNS = {"商家名称","仓库名称","报表月份","商品货号","商品条码","标准条码","商品名称","标准品牌","母品牌","生产日期","失效日期",
					"生产批次号","总效期(天)","总库存","库存类型","剩余失效(天)","失效月份","剩余效期占比(%)","效期区间",
					"剩余效期占比(财务逻辑）","单价","总金额（原币）","总金额（人民币）"};
			String[] KEYS =  {"merchantName", "depotName", "reportMonth", "goodsNo", "barcode", "commbarcode", "goodsName",
					"brandParent", "superiorParentBrand", "productionDate", "overdueDate", "batchNo", "totalDays", "surplusNum",
					"inverntoryType", "surplusDays", "overdueMonth", "surplusProportion",
					"effectiveInterval", "financialSurplusProportion", "settlementPrice","totalAmount", "totalAmountCNY"} ;

			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,resultMap);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "获取部门信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getDepartList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String,Object>>> getDepartList(String token) throws SQLException {
		List<Map<String,Object>> departList = new ArrayList<Map<String,Object>>();
		try {//
			User user = ShiroUtils.getUserByToken(token);
			List<BusinessUnitModel> buList = retailAdminService.getBuList(user);
			List<Long> departIdList = new ArrayList<Long>();
			for(BusinessUnitModel buModel : buList) {
				if(buModel.getDepartmentId() != null && !departIdList.contains(buModel.getDepartmentId())) {
					Map<String,Object> departMap = new HashMap<String,Object>();
					departMap.put("departmentId", buModel.getDepartmentId());
					departMap.put("departmentName", buModel.getDepartmentName());
					departList.add(departMap);
					departIdList.add(buModel.getDepartmentId());
				}
			}

		} catch (Exception e) {//
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,departList);
	}
}
