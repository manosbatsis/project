package com.topideal.web.derp.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.SupplierMerchandisePriceService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品价目表 控制层
 */
@RequestMapping("/supplierMerchandisePrice")
@Controller
public class SupplierMerchandisePriceController {
	private static final String[] MAIN_COLUMNS = {"所属公司","事业部","供应商编码", "供应商名称", "标准条码", "商品名称", "报价生效日期",
			"报价失效日期", "报价币种", "供货价"} ;

	private static final String[] MAIN_KEYS = {"merchantName","buName","customerCode", "customerName", "commbarcode", "goodsName", "effectiveDate",
			"expiryDate", "currencyLabel", "supplyPrice"} ;

	// 供应商商品价目表service
	@Autowired
	private SupplierMerchandisePriceService sMPriceService;
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	/**
	 * 访问列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		User user = ShiroUtils.getUser();
		List<SelectBean> supplierBean = customerService.getAllSelectBeanBySupplier();
		model.addAttribute("supplierBean", supplierBean);
		return "/derp/main/supplierMerchandisePrice-list";
	}

	/**
	 * 导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/main/supplierMerchandisePrice-import";
	}

	/**
	 * 获取分页数据
	 * 
	 * @param dto
	 *            供应商商品价格信息
	 * @return
	 */
	@RequestMapping("/listSMPrice.asyn")
	@ResponseBody
	private ViewResponseBean listSMPrice(SupplierMerchandisePriceDTO dto,String commbarcodes) {
		try {
			User user = ShiroUtils.getUser();
			// 商家id
			dto.setMerchantId(user.getMerchantId());
			if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}
			if (StringUtils.isNotBlank(dto.getCustomerIdsStr())) {
				List<Long> customerList = StrUtils.parseIds(dto.getCustomerIdsStr());
				dto.setSupplierList(customerList);
			}
			// 响应结果集
			dto = sMPriceService.listSMPrice(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delSMPrice.asyn")
	@ResponseBody
	public ViewResponseBean delSMPrice(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = sMPriceService.delSMPrice(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 批量导入
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping("/importSMPrice.asyn")
	@ResponseBody
	public ViewResponseBean importSMPrice(@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user = ShiroUtils.getUser();
			resultMap = sMPriceService.importSMPrice(data, user);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
	/**
	 * 获取导出数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request, SupplierMerchandisePriceDTO dto,String commbarcodes) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}
			// 响应结果集
			List<SupplierMerchandisePriceDTO> result = sMPriceService.getSMPriceList(dto);
			data.put("total",result.size());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出
	 * */
	@RequestMapping("/exportSMPrice.asyn")
	@ResponseBody
	private void exportSMPrice(HttpServletResponse response, HttpServletRequest request,SupplierMerchandisePriceDTO dto,String commbarcodes) throws Exception{
		User user= ShiroUtils.getUser();
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		if (StringUtils.isNotBlank(commbarcodes)) {
			List<String> commbarcodeList=new ArrayList<String>();
			String[] commbarcodeArr = commbarcodes.split(",");
			for(String commbarcodeStr:commbarcodeArr){
				if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
			}
			dto.setCommbarcodeList(commbarcodeList);
		}
		// 响应结果集
		List<SupplierMerchandisePriceDTO> mainList = sMPriceService.getSMPriceList(dto);
		String mainSheetName = "供应商商品价目";

		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList8(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
	}
	/**
	 * 审核
	 * @param ids
	 * @return
	 */
	@RequestMapping("/auditSMPrice.asyn")
	@ResponseBody
	public ViewResponseBean auditSMPrice(String ids, String auditType) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<Long> list = StrUtils.parseIds(ids);
		User user= ShiroUtils.getUser();
		try {
			sMPriceService.auditSMPrice(list,user,auditType) ;
			return ResponseFactory.success() ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}
	
	@RequestMapping("/getSMPriceByPurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model) {
		
		try {
			
			User user = ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			
			com.alibaba.fastjson.JSONObject jSONObject = sMPriceService.getSMPriceByPurchaseOrder(model);
			
			return ResponseFactory.success(jSONObject) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
		
	}

	/*统计各个状态下的采购价格数量*/
	@RequestMapping("/statisticsStateNum.asyn")
	@ResponseBody
	public ViewResponseBean statisticsStateNum(SupplierMerchandisePriceDTO model,String commbarcodes) {
		try {
			SupplierMerchandisePriceDTO dto = new SupplierMerchandisePriceDTO();
			User user = ShiroUtils.getUser();
			
			model.setMerchantId(user.getMerchantId());
			BeanUtils.copyProperties(model, dto);
			dto.setState(null);
			if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}
			if (StringUtils.isNotBlank(dto.getCustomerIdsStr())) {
				List<Long> customerList = StrUtils.parseIds(dto.getCustomerIdsStr());
				dto.setSupplierList(customerList);
			}
			dto = sMPriceService.statisticsStateNum(dto);
			return ResponseFactory.success(dto) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}

	}

	/**
	 * 提交
	 * @param ids
	 * @return
	 */
	@RequestMapping("/submitSMPrice.asyn")
	@ResponseBody
	public ViewResponseBean submitSMPrice(String ids) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<Long> list = StrUtils.parseIds(ids);
		User user= ShiroUtils.getUser();
		try {
			sMPriceService.submitSMPrice(list,user) ;
			return ResponseFactory.success() ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}

	@RequestMapping("/getDetailsById.asyn")
	@ResponseBody
	public ViewResponseBean getDetailsById(Long id) throws Exception{
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			SupplierMerchandisePriceDTO dto = sMPriceService.searchDTOById(id);
			return ResponseFactory.success(dto) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}

	@RequestMapping("/modifySMPrice.asyn")
	@ResponseBody
	public ViewResponseBean modifySMPrice(SupplierMerchandisePriceDTO dto) throws Exception{
		try {
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).addObject(dto.getSupplyPrice())
					.addObject(dto.getEffectiveDateStr()).addObject(dto.getExpiryDateStr()).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			sMPriceService.modifySMPrice(dto, user);
			return ResponseFactory.success() ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}
}
