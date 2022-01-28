//package com.topideal.web.derp.main;
//
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtil;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.entity.dto.main.CustomerSalePriceCountDTO;
//import com.topideal.entity.dto.main.CustomerSalePriceDTO;
//import com.topideal.entity.vo.main.CustomerSalePriceModel;
//import com.topideal.entity.vo.main.MerchandiseInfoModel;
//import com.topideal.service.base.BrandService;
//import com.topideal.service.main.CustomerSalePriceService;
//import com.topideal.service.main.MerchandiseService;
//import com.topideal.shiro.ShiroUtils;
//import com.topideal.webapi.form.CustomerSalePriceEditForm;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.assertj.core.util.Arrays;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 客户销售价格表
// * @author lian_
// */
//@RequestMapping("/customerSale")
//@Controller
//public class CustomerSalePriceController {
//
//	@Autowired
//	private CustomerSalePriceService service;//客户销售价格
//	// 商品信息service
//	@Autowired
//	private MerchandiseService merchandiseService;//品类
//	// 品牌信息service
//	@Autowired
//	private BrandService brandService;	//品牌
//	/**
//	 * 访问列表页面
//	 * @throws SQLException 
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws SQLException {
//		return "/derp/main/sale-price-list";
//	}
//
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listSalePrice.asyn")
//	@ResponseBody
//	private ViewResponseBean listSalePrice(CustomerSalePriceDTO dto,String commbarcodes,String customerIds) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			dto.setMerchantName(user.getMerchantName());//设置商家名称
//			if (StringUtils.isNotBlank(commbarcodes)) {
//				List<String> commbarcodeList=new ArrayList<String>();
//				String[] commbarcodeArr = commbarcodes.split(",");
//				for(String commbarcodeStr:commbarcodeArr){
//					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
//				}
//				dto.setCommbarcodeList(commbarcodeList);
//			}
//			if (StringUtils.isNotBlank(customerIds)) {
//				List<Long> customerList = StrUtils.parseIds(customerIds);
//				dto.setCustomerList(customerList);
//			}
//			// 响应结果集
//			dto = service.listSalePrice(dto,user);
//		}catch(SQLException e){
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 获取状态数量
//	 * @param dto
//	 * @return
//	 */
//	@RequestMapping("/listSalePriceCount.asyn")
//	@ResponseBody
//	private ViewResponseBean listSalePriceCount(CustomerSalePriceDTO dto,String commbarcodes,String customerIds) {
//		CustomerSalePriceCountDTO dtoCount=new CustomerSalePriceCountDTO();
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			dto.setMerchantName(user.getMerchantName());//设置商家名称
//			// 响应结果集
//			if (StringUtils.isNotBlank(commbarcodes)) {
//				List<String> commbarcodeList=new ArrayList<String>();
//				String[] commbarcodeArr = commbarcodes.split(",");
//				for(String commbarcodeStr:commbarcodeArr){
//					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
//				}
//				dto.setCommbarcodeList(commbarcodeList);
//			}
//			if (StringUtils.isNotBlank(customerIds)) {
//				List<Long> customerList = StrUtils.parseIds(customerIds);
//				dto.setCustomerList(customerList);
//			}
//			dtoCount = service.getCountStatus(dto,user);
//		}catch(Exception e){
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dtoCount);
//	}
//
//
//	/**
//	 * 提交
//	 * @param ids
//	 * @return
//	 */
//	@RequestMapping("/submitSMPrice.asyn")
//	@ResponseBody
//	public ViewResponseBean submitSMPrice(String ids) {
//		//校验id是否正确
//		boolean isRight = StrUtils.validateIds(ids);
//		if(!isRight){
//			//输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//
//		try{
//			User user= ShiroUtils.getUser();
//			Map<String,Object> map=service.submitSMPrice(ids,user);
//			return ResponseFactory.success(map);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//		}catch(NullPointerException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//
//	}
//
//
//	/**
//	 * 审核
//	 */
//	@RequestMapping("/auditSMPrice.asyn")
//	@ResponseBody
//	public ViewResponseBean auditSMPrice(String ids,String type) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//		try{
//			User user= ShiroUtils.getUser();
//			List idList = Arrays.asList(ids.split(","));
//			for (Object object : idList) {
//				CustomerSalePriceModel searchDetail = service.searchDetail(Long.valueOf(object.toString()));
//				if (searchDetail!=null&&DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003.equals(searchDetail.getStatus())) {
//					return ResponseFactory.error(StateCodeEnum.MESSAGE_10014);
//				}
//			}
//
//			Map<String,Object> map=service.auditSMPrice(ids,user,type);
//			return ResponseFactory.success(map);
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//	}
//
//	/**
//	 * 编辑
//	 * @return
//	 */
//	@RequestMapping("/editSMPrice.asyn")
//	@ResponseBody
//	public ViewResponseBean editSMPrice(CustomerSalePriceEditForm form) throws ParseException {
//		CustomerSalePriceDTO model = new CustomerSalePriceDTO();
//
//		model.setEffectiveDate(Timestamp.valueOf(form.getEffectiveDate()+" 00:00:00"));
//		model.setExpiryDate(Timestamp.valueOf(form.getExpiryDate()+" 00:00:00"));
//		BeanUtils.copyProperties(form,model);
//		try{
//			User user= ShiroUtils.getUser();
//			Map map=service.editSalePriceModel(model,user);
//			return ResponseFactory.success(map);
//		}catch(NullPointerException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//	}
//
//    /**
//     * 导出
//     * @throws IOException
//     */
//    @RequestMapping("/export.asyn")
//    public void export(HttpServletResponse response, HttpServletRequest request,CustomerSalePriceDTO dto,String commbarcodes) throws Exception {
//        String sheetName = "销售价格管理";
//        String[] COLUMNS = { "所属公司","事业部","客户编号","客户名称","标准条码","商品名称","品牌","规格型号","币种","销售价","状态","报价生效时间","报价失效时间","创建人",
//        		"创建时间","审核人","审核时间"};
//		String[] KEYS = {"merchantName","buName","customerCode" , "customerName" , "commbarcode","goodsName",
//				"brandName","spec","currencyLabel","salePrice","statusLabel","effectiveDate",
//				"expiryDate","createName","createDate","auditName","auditDate"};
//		User user= ShiroUtils.getUser();
//		dto.setMerchantId(user.getMerchantId());
//		if (StringUtils.isNotBlank(commbarcodes)) {
//			List<String> commbarcodeList=new ArrayList<String>();
//			String[] commbarcodeArr = commbarcodes.split(",");
//			for(String commbarcodeStr:commbarcodeArr){
//				if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
//			}
//			dto.setCommbarcodeList(commbarcodeList);
//		}
//        // 获取导出的信息
//        List<CustomerSalePriceDTO> list = service.getExportList(dto,user);
//        // 生成表格
//        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList8(sheetName, COLUMNS, KEYS, list) ;
//        // 导出文件
//        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//    }
//	
//	/**
//	 * 删除
//	 */
//	@RequestMapping("/delPriceSale.asyn")
//	@ResponseBody
//	public ViewResponseBean delPriceSale(String ids) {
//		try {
//			// 校验id是否正确
//			boolean isRight = StrUtils.validateIds(ids);
//			if (!isRight) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			List list = StrUtils.parseIds(ids);
//			boolean b = service.delPriceSale(list);
//			if (!b) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		} catch (NullPointerException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success();
//	}
//	
//	/**
//	 * 导入页面
//	 */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage() {
//		return "/derp/main/sale-price-import";
//	}
//	
//	/**
//	 * 导入销售价格
//	 */
//	@RequestMapping("/importPriceSale.asyn")
//	@ResponseBody
//	public ViewResponseBean importPriceSale(@RequestParam(value = "file", required = false) MultipartFile file) {
//		Map resultMap = new HashMap();// 返回的结果集
//		try {
//			if (file == null) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
//					file.getOriginalFilename(), 1);
//			if (data == null) {// 数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//			User user = ShiroUtils.getUser();
//			resultMap = service.importPriceSale(data,user);
//		} catch (NullPointerException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 商品信息弹框列表
//	 * 
//	 * @param model
//	 *            商品信息
//	 */
//	@RequestMapping("/getSaleListByPage.asyn")
//	@ResponseBody
//	public ViewResponseBean getSaleListByPage(MerchandiseInfoModel model, String unNeedIds) {
//		try {
//			User user = ShiroUtils.getUser();
//			List ids = null;
//			if (!StringUtils.isEmpty(unNeedIds)) {
//				ids = StrUtils.parseIds(unNeedIds);
//				model.setIds(ids);
//			}
//			model.setMerchantId(user.getMerchantId());
//			model = merchandiseService.getSaleListByPage(model);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		} catch (NullPointerException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(model);
//	}
//
//
//	/**
//	 * 根据商品id获取商品信息
//	 */
//	@RequestMapping("/getSaleListByIds.asyn")
//	@ResponseBody
//	public ViewResponseBean getSaleListByIds(String ids) {
//		List<MerchandiseInfoModel> result = new ArrayList<MerchandiseInfoModel>();
//		try {
//			// 校验id是否正确
//			boolean isRight = StrUtils.validateIds(ids);
//			if (!isRight) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			List list = StrUtils.parseIds(ids);
//			result = merchandiseService.getSaleListByIds(list);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		} catch (NullPointerException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(result);
//	}
//	/**
//	 * 根据ID获取货品详情
//	 */
//	@RequestMapping("/getSalePriceDetails.asyn")
//	@ResponseBody
//	public ViewResponseBean getSalePriceDetails(Long id) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateId(id);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        CustomerSalePriceModel model = new CustomerSalePriceModel();
//		try{
//			model = service.searchDetail(id);
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(model);
//	}
//	
//	/**
//	 * 根据客户ID回显客户编码和主数据客户ID
//	 * @param model
//	 * @param customerId
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/getCodeById.asyn")
//	@ResponseBody
//	public ViewResponseBean getCodeById(CustomerSalePriceModel model, Long customerId)throws Exception{
//		List<CustomerSalePriceModel> customerList=null;
//		if(customerId != null){
//			customerList = service.getCodeById(customerId);
//			if (customerList==null||customerList.size()==0) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_306);
//			}	
//		}
//		Map<String, Object> map = new  HashMap<>();
//		map.put("customerList", customerList);
//		return ResponseFactory.success(map);
//	}
//	
//}
