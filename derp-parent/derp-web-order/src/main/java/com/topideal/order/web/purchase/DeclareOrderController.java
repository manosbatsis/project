/*package com.topideal.order.web.purchase;

import com.alibaba.fastjson.JSONArray;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExcelExportXlsx;
import com.topideal.common.tools.excelReader.ExcelReader;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.dto.purchase.DeclareOrderExportDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.FirstCustomsInfoModel;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.base.PackTypeService;
import com.topideal.order.service.purchase.DeclareOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * 预申报单 控制层
 *//*
@RequestMapping("/declare")
@Controller
public class DeclareOrderController {
	
	private static final Logger LOG = Logger.getLogger(DeclareOrderController.class) ;

	private static final String[] COLUMNS = { "预申报单号", "合同号", "PO号", "供应商名称", "运输方式", "仓库名称", "事业部","交货地点", "业务模式",
			"预计到港时间", "头程提单号", "发票号", "销售渠道", "箱数", "目的港名称", "目的地名称", "订单状态", "装货港", "包装方式", "付款条约", "提单毛重", "卸货港编码",
			"二程提单号", "邮箱", "进出口口岸", "lbx单号", "备注", "创建时间", "海外仓理货单位", "子合同号", "商品编码", "商品货号", "商品名称", "采购数量", "采购单价",
			"采购总金额", "采购单位", "毛重", "净重", "品牌名称", "箱号", "批次号", "成分占比" };
	
	private static final String[] KEYS = { "code", "contractNo", "poNo", "supplierName", "transportLabel", "depotName", "buName","deliveryAddr", "businessModelLabel",
			"arriveDate", "ladingBill", "invoiceNo", "channel", "boxNum", "destinationPortName", "destinationName", "statusLabel", "portLoading", "packType", "payRules", "billWeight", "portDestNo",
			"blNo", "email", "imExpPort", "lbxNo", "remark", "createDate", "tallyingUnitLabel", "subcontractNo", "goodsCode", "goodsNo", "goodsName", "num", "purchasePrice",
			"amount", "purchaseUnit", "grossWeight", "netWeight", "brandName", "boxNo", "batchNo", "constituentRatio" };
	
	private static final String[] EXPORT_ITEM_COLUMNS = { "序号", "商品货号", "申报数量", "采购单价", "申报单价", "采购总金额", "品牌类型","毛重（KG）", "净重（KG）"};
	
	private static final String[] EXPORT_ITEM_KEYS = { "seq", "goodsNo", "num", "purchasePrice", "price", "amount", "brandName","grossWeightSum", "netWeightSum"};

	// 预申报单service
	@Autowired
	private DeclareOrderService declareOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	// 包装方式
	@Autowired
	private PackTypeService packTypeService;

	*//**
	 * 访问列表页面
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/purchase/declare-list";
	}

	*//**
	 * 访问详情页面
	 *//*
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		DeclareOrderDTO dto = declareOrderService.searchDTODetail(id);
		List<DeclareOrderItemModel> itemList = declareOrderService.getItem(id);
		dto.setItemList(itemList);
		model.addAttribute("detail", dto);
		DepotInfoMongo depotInfo = depotInfoService.getDetails(dto.getDepotId());
		model.addAttribute("depotType", depotInfo.getType());
		return "/derp/purchase/declare-detail";
	}

	*//**
	 * 访问编辑页面
	 *//*
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		DeclareOrderDTO dto = declareOrderService.searchDTODetail(id);
		List<DeclareOrderItemModel> itemList = declareOrderService.getItem(id);
		dto.setItemList(itemList);
		model.addAttribute("detail", dto);

		int flag = 0;
		DepotInfoMongo depotInfo = depotInfoService.getDetails(dto.getDepotId());
		if (depotInfo != null && depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
			flag = 1;
		}
		model.addAttribute("depotType", depotInfo.getType());
		model.addAttribute("flag", flag);
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		if (dto.getArriveDate() != null) {
			String arriveDate = sft.format(dto.getArriveDate());
			model.addAttribute("arriveDate", arriveDate);
		}
		List<SelectBean> packTypeBean = packTypeService.getSelectBean();
		model.addAttribute("packTypeBean", packTypeBean);
		return "/derp/purchase/declare-edit";
	}

	*//**
	 * 获取分页数据
	 *//*
	@RequestMapping("/listDeclare.asyn")
	@ResponseBody
	private ViewResponseBean listDeclare(DeclareOrderDTO dto) {
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = declareOrderService.listDeclare(dto);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	*//**
	 * 根据ID获取预申报单详情
	 *//*
	@RequestMapping("/getDeclareDetails.asyn")
	@ResponseBody
	public ViewResponseBean getDeclareDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		DeclareOrderModel model = new DeclareOrderModel();
		try {
			model = declareOrderService.searchDetail(id);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	*//**
	 * 修改
	 * 
	 * @param session
	 * @param model
	 * @param contractNos
	 * @param ids
	 * @param nums
	 * @param prices
	 * @param boxNos
	 * @param batchNos
	 * @param ratios
	 * @param brandNames
	 * @return
	 *//*
	@RequestMapping("/modifyDeclare.asyn")
	@ResponseBody
	public ViewResponseBean modifyDeclare(DeclareOrderModel model, String arriveDate2,
			String items) {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			if (StringUtils.isNotBlank(arriveDate2)) {
				model.setArriveDate(TimeUtils.parse(arriveDate2, "yyyy-MM-dd"));
			}
			
			User user= ShiroUtils.getUser();
			DepotInfoMongo depotInfo = depotInfoService.getDetails(model.getDepotId());
			model.setDepotName(depotInfo.getName());
			
			// 仓库类别为 保税仓
			if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
				// 必填项空值校验
				boolean isNull = new EmptyCheckUtils().addObject(model.getContractNo()).addObject(model.getInvoiceNo())
						.addObject(model.getDeliveryAddr()).addObject(model.getPackType()).addObject(model.getBoxNum())
						.addObject(model.getLadingBill()).empty();
				if (isNull) {
					// 输入信息不完整
					return ResponseFactory.error(StateCodeEnum.ERROR_303);
				}
			}
			// 海外仓
			else {
				// 必填项空值校验
				boolean isNull = new EmptyCheckUtils().addObject(model.getPoNo()).addObject(model.getContractNo())
						.addObject(model.getTallyingUnit()).empty();
				if (isNull) {
					// 输入信息不完整
					return ResponseFactory.error(StateCodeEnum.ERROR_303);
				}
			}
			
			List<DeclareOrderItemModel> list = JSONArray.parseArray(items, DeclareOrderItemModel.class);
			
			// 商品至少选择一个
			if (list.isEmpty()) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			for (int i = 0; i < list.size(); i++) {
				DeclareOrderItemModel item = list.get(i) ;
				
				if (item.getSeq() == null) {
					item.setSeq(i + 1);// 序号
				}
				
				item.setCreater(user.getId());// 创建人
			}
			
			model.setItemList(list);
			model.setState("1");// 默认正常的单 1-正常
			result = declareOrderService.modifyDeclare(model, user);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	*//**
	 * 批量取消
	 * 
	 * @param model
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/cancelDeclare.asyn")
	@ResponseBody
	public ViewResponseBean cancelDeclare(String ids, HttpSession session) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = declareOrderService.cancelDeclare(list, user.getName());
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	*//**
	 * 批量提交
	 * 
	 * @param ids
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/submitDeclareOrder.asyn")
	@ResponseBody
	public ViewResponseBean submitDeclareOrder(String ids, HttpSession session) {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			List<Long> list = StrUtils.parseIds(ids);
			result = declareOrderService.submitDeclareOrder(list, user.getTopidealCode(), user.getName(),
					user.getMerchantId());
		
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	*//**
	 * 预申报导出
	 * 
	 * @param
	 * @return void
	 * @throws IOException
	 *//*
	@RequestMapping("/exportDeclare.asyn")
	public void exportRelation(DeclareOrderDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
		User user= ShiroUtils.getUser();
		String sheetName = "预申报单";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<DeclareOrderExportDTO> result = declareOrderService.getDetailsByExport(dto);
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	*//**
	 * 一线清关资料导出
	 * 
	 * @param
	 * @return void
	 * @throws IOException
	 *//*
	@RequestMapping("/exportCustomsDeclare.asyn")
	public void exportCustomsDeclare(HttpSession session, HttpServletResponse response, HttpServletRequest request,
			Long id) throws Exception {
		String sheetName = "一线清关资料";
		FirstCustomsInfoDTO model = declareOrderService.exportCustomsDeclare(id);
		// 生成表格
		SXSSFWorkbook wb = DownloadExcelUtil.createCustomsDeclareExcel(model);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	*//**
	 * 访问一线清关编辑页面
	 *//*
	@RequestMapping("/toCustomsDeclarePage.html")
	public String toCustomsDeclarePage(Long id, Model model) throws Exception {
		
		FirstCustomsInfoDTO firstModel = declareOrderService.exportCustomsDeclare(id);
		
		if (firstModel == null) {
			firstModel = declareOrderService.saveCustomsDeclare(id);
		}
		model.addAttribute("detail", firstModel);
		return "/derp/purchase/customs-declare-edit";
	}

	*//**
	 * 访问一线清关详情页面
	 *//*
	@RequestMapping("/toCustomsDeclareDetailsPage.html")
	public String toCustomsDeclareDetailsPage(Long id, Model model) throws Exception {
		
		FirstCustomsInfoDTO firstModel = declareOrderService.exportCustomsDeclare(id);
		
		if (firstModel == null) {
			firstModel = declareOrderService.saveCustomsDeclare(id);
		}
		model.addAttribute("detail", firstModel);
		return "/derp/purchase/customs-declare-details";
	}

	*//**
	 * 编辑清关资料
	 *//*
	@RequestMapping("/modifyCustomsDeclare.asyn")
	@ResponseBody
	public ViewResponseBean modifyCustomsDeclare(FirstCustomsInfoModel model, HttpSession session, String palletNos,
			String carton, String signingDateStr, String invoiceDateStr, String shipDateStr, String ids)
			throws Exception {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (signingDateStr != null && !"".equals(signingDateStr)) {
				Date date = sdf.parse(signingDateStr + " 00:00:00");
				model.setSigningDate(new Timestamp(date.getTime()));
			}
			if (invoiceDateStr != null && !"".equals(invoiceDateStr)) {
				Date date = sdf.parse(invoiceDateStr + " 00:00:00");
				model.setInvoiceDate(new Timestamp(date.getTime()));
			}
			if (shipDateStr != null && !"".equals(shipDateStr)) {
				Date date = sdf.parse(shipDateStr + " 00:00:00");
				model.setShipDate(new Timestamp(date.getTime()));
			}
			String[] goodsIds = ids.split(",");
			String[] palletNo = palletNos.split(",");
			String[] cartons = carton.split(",");
			User user= ShiroUtils.getUser();
			declareOrderService.modifyCustomsDeclare(model, goodsIds, palletNo, cartons, user.getName());
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		}  catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (RuntimeException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	@RequestMapping("/exportGoods.asyn")
	public void exportGoods(@RequestParam("json")String itemList, HttpServletResponse response, HttpServletRequest request) throws Exception {
			
		List<DeclareOrderItemModel> goodsList = declareOrderService.getGoodsList(itemList) ;
		
		SXSSFWorkbook wb = ExcelExportXlsx.createSXSSExcelByObjList("申报商品", EXPORT_ITEM_COLUMNS, EXPORT_ITEM_KEYS, goodsList);
		
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, "预申报-商品导入模板");	
			
	}
	
	@RequestMapping("/importGoods.asyn")
	@ResponseBody
	public ViewResponseBean importGoods(@RequestParam(value = "file", required = false) MultipartFile file, 
			@RequestParam(value = "orderId", required = false)Long id) throws IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			ExcelReader reader = ExcelUtil.getExcelReader(file.getOriginalFilename()) ;
			List<Map<String, String>> data = reader.processSingleSheet(file.getInputStream()) ;
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			resultMap = declareOrderService.importGoods(data, id);
			
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		
		return ResponseFactory.success(resultMap);
	}

	// 允许前端传进来的timestamp类型的值为空
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
*/