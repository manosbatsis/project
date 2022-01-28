//package com.topideal.order.web.sale;
//
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.topideal.common.constant.DERP_ORDER;
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.bean.SelectBean;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.EmptyCheckUtils;
//import com.topideal.common.tools.ExcelUtil;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.common.tools.excel.ExportExcelSheet;
//import com.topideal.dao.sale.SaleOrderDao;
//import com.topideal.entity.dto.sale.SaleOrderDTO;
//import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
//import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
//import com.topideal.entity.vo.sale.SaleOrderModel;
//import com.topideal.entity.vo.sale.SaleReturnOrderModel;
//import com.topideal.mongo.dao.DepotInfoMongoDao;
//import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
//import com.topideal.mongo.entity.DepotInfoMongo;
//import com.topideal.mongo.entity.DepotMerchantRelMongo;
//import com.topideal.order.service.base.PackTypeService;
//import com.topideal.order.service.sale.SaleOrderService;
//import com.topideal.order.service.sale.SaleReturnOdepotService;
//import com.topideal.order.service.sale.SaleReturnOrderService;
//import com.topideal.order.shiro.ShiroUtils;
//
//import net.sf.json.JSONObject;
//
//
///**
// * 销售退货订单 controller
// *
// */
//@RequestMapping("/saleReturn")
//@Controller
//public class SaleReturnOrderController {
//	private static final String[] SALERETURN_COLUMNS = {"销售退订单号","退出仓库","退入仓库","事业部","客户","服务类型","装货港","合同号","发票号","LBX单号","收货地址","境外发货人","退货原因","创建人","创建时间"};
//	private static final String[] SALERETURN_KEYS = {"code","outDepotName","inDepotName","buName","customerName","serveTypesLabel","portLoading","contractNo","invoiceNo","lbxNo","deliveryAddr","shipper","remark","createName","createDate"};
//	private static final String[] SALERETURN_ITEM_COLUMNS ={"销售退订单号","PO号码","退出商品货号","退入商品货号","退入商品名称","退入商品条形码","退入商品单价","退入商品金额","退货商品数量","好品数量","坏品数量","退货商品毛重","退货商品净重"};
//	private static final String[] SALERETURN_ITEM_KEYS ={"orderCode","poNo","outGoodsNo","inGoodsNo","inGoodsName","barcode","price","amount","totalPreNum","returnNum","badGoodsNum","grossWeight","netWeight"};
//	// 销售退货订单service
//	@Autowired
//	private SaleReturnOrderService saleReturnOrderService;
//	@Autowired
//	private SaleOrderService saleOrderService;
//	@Autowired
//	private PackTypeService packTypeService;
//	// 仓库商家关联表
//	@Autowired
//	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
//	//仓库
//	@Autowired
//	private DepotInfoMongoDao depotInfoMongoDao;
//	@Autowired
//	private SaleOrderDao saleOrderDao;
//	// 销售退出库订单service
//	@Autowired
//	private SaleReturnOdepotService saleReturnOdepotService;
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws Exception {
////		UserInfoModel user= ShiroUtils.getUser();
////		List<SelectBean> depotBean = depotService.getSelectBean(user.getMerchantId());
////		model.addAttribute("depotBean", depotBean);
//		return "/derp/sale/salereturn-list";
//	}
//
//	/**
//	 * 访问新增页面
//	 * @param model
//	 * @param ids   销售订单 ID
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/toAddPage.html")
//	public String toAddPage(Model model, String ids, HttpSession session)throws Exception{
//		//初始化数据
//		model.addAttribute("itemCount", 0);
//		User user= ShiroUtils.getUser();
//		if(ids != null && StrUtils.validateIds(ids)){//ids不为空，需要获取数据
//			model.addAttribute("saleOrderRelCode", ids);
//			SaleReturnOrderModel saleReturnOrderModel = saleReturnOrderService.listSaleOrderAndOutDepotOrder(StrUtils.parseIds(ids),user.getMerchantId());
//			model.addAttribute("detail", saleReturnOrderModel);
//			model.addAttribute("itemCount", saleReturnOrderModel.getItemList().size());
//			// 如果是购销退货则退货类型、客户两个字段均不予重新编辑
//			model.addAttribute("isForbid","no");
//			List<Long> idList = StrUtils.parseIds(ids);
//			for (Long id :idList) {
//				SaleOrderDTO saleOrderDto = new SaleOrderDTO();
//				saleOrderDto.setId(id);
//				saleOrderDto = saleOrderService.searchDetail(id);
//				model.addAttribute("businessModel",saleOrderDto.getBusinessModel());
//			}
//		}
//		model.addAttribute("merchantId", user.getMerchantId());
//		model.addAttribute("merchantName", user.getMerchantName());
//		List<SelectBean> packTypeBean = packTypeService.getSelectBean();
//		model.addAttribute("packTypeBean", packTypeBean);
//		return "/derp/sale/salereturn-add";
//	}
//	/**
//	 *  多选订单时，需校验是否相同出仓仓库、相同客户，若有不同则报错提示，不予保存提交。
//	 * */
//	@RequestMapping("/isSameParameter.asyn")
//	@ResponseBody
//	private ViewResponseBean isSameParameter(String ids,HttpSession session) {
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            saleReturnOrderService.isSameParameter(ids);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//	/**
//	 * 新增
//	 * */
//	@RequestMapping("/saveSaleReturnOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean saveSaleReturnOrder(String json,HttpSession session) {
//		 String msg = null;
//			try{
//	            if(json == null || StringUtils.isBlank(json)){
//	                //输入信息不完整
//	                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//	            }
//	            User user= ShiroUtils.getUser();
//	            msg = saleReturnOrderService.saveSaleReturnOrder(json,user.getId(),user.getName(), user.getTopidealCode());
//	        }catch(SQLException e){
//	            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//	        }catch(NullPointerException e){
//	            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//	        }catch(RuntimeException e){
//	            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//	        }catch(Exception e){
//	            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//	        }
//	        return ResponseFactory.success(msg);
//	}
//	/**
//	 * 访问编辑页面
//	 * */
//	@RequestMapping("/toEditPage.html")
//	public String toEditPage(Model model, Long id, HttpSession session)throws Exception{
//		User user= ShiroUtils.getUser();
//		SaleReturnOrderDTO saleOrderDTO = saleReturnOrderService.searchDetail(id);
//		if(StringUtils.isNotBlank(saleOrderDTO.getSaleOrderRelCode())){
//			SaleOrderModel saleOrderModel = new SaleOrderModel();
//			saleOrderModel.setCode(saleOrderDTO.getSaleOrderRelCode());
//			SaleOrderModel saleOrder = saleOrderDao.searchByModel(saleOrderModel);
//			if (saleOrder != null) {
//				model.addAttribute("businessModel", saleOrder.getBusinessModel());
//			}
//			// 如果是购销退货则退货类型、客户两个字段均不予重新编辑
//			model.addAttribute("isForbid","no");
//		}
//		List<SaleReturnOrderItemDTO> saleReturnOrderItemList = saleReturnOrderService.listItemByOrderId(id);
//		List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.getOrignGrossNetWeightItem(saleReturnOrderItemList);
//		saleOrderDTO.setItemList(itemList);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("depotId", saleOrderDTO.getInDepotId());
//		DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params);
//		params.clear();
//		params.put("depotId", saleOrderDTO.getInDepotId());
//		params.put("merchantId", user.getMerchantId());
//		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(params);
//		int is_required = 0; // 是否必填
//		// 购销退货、代销退货类型，退入仓为保税仓（并在对应商家下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
//		if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleOrderDTO.getReturnType())
//			|| DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleOrderDTO.getReturnType())){
//			if(inDepotInfo!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepotInfo.getType())
//				&& depotMerchantRel!=null && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())){
//				is_required = 1;
//			}
//		}
//		model.addAttribute("buId", saleOrderDTO.getBuId());
//		model.addAttribute("inDepotId", saleOrderDTO.getInDepotId());
//		model.addAttribute("outDepotId", saleOrderDTO.getOutDepotId());
//		model.addAttribute("isRequired", is_required);
//		model.addAttribute("inDepotTypeVal",inDepotInfo!=null?inDepotInfo.getType():"");
//		model.addAttribute("inIsInOutInstructionVal",depotMerchantRel!=null?depotMerchantRel.getIsInOutInstruction():"");
//		model.addAttribute("inDepotNameVal",inDepotInfo!=null?inDepotInfo.getName():"");
//		model.addAttribute("itemCount", itemList.size());
//		model.addAttribute("detail", saleOrderDTO);
//		List<SelectBean> packTypeBean = packTypeService.getSelectBean();
//		model.addAttribute("packTypeBean", packTypeBean);
//		return "/derp/sale/salereturn-edit";
//	}
//	/**
//	 * 修改
//	 * */
//	@RequestMapping("/modifySaleReturnOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean modifySaleReturnOrder(String json, HttpSession session) {
//		String msg=null;
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            msg = saleReturnOrderService.modifySaleReturnOrder(json,user.getId(),user.getName(),user.getTopidealCode());
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(msg);
//	}
//
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id, String pageSource)throws Exception{
//		SaleReturnOrderDTO saleReturnOrderDTO = saleReturnOrderService.searchDetail(id);
//		List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.listItemByOrderId(id);
//		saleReturnOrderDTO.setItemList(itemList);
//		model.addAttribute("detail", saleReturnOrderDTO);
//		if (StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource", pageSource);
//		}
//		return "/derp/sale/salereturn-details";
//	}
//
//	/**
//	 * 导入页面
//	 * */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage(){
//		return "/derp/sale/salereturn-import";
//	}
//	/**
//	 * 导入页面
//	 * */
//	@RequestMapping("/toImportPage2.html")
//	public String toImportPage2(){
//		return "/derp/sale/salereturn-import2";
//	}
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listSaleReturnOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleReturnOrder(SaleReturnOrderDTO dto, HttpSession session) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = saleReturnOrderService.listSaleReturnOrderByPage(dto,user);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 删除
//	 * */
//	@RequestMapping("/delSaleReturnOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean delSaleReturnOrder(String ids) {
//		try{
//            //校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//            boolean b = saleReturnOrderService.delSaleReturnOrder(list);
//            if(!b){
//                return ResponseFactory.error(StateCodeEnum.ERROR_301);
//            }
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//
//	/**
//	 * 审核
//	 * */
//	@RequestMapping("/auditSaleReturnOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean auditSaleReturnOrder(String ids, HttpSession session) {
//		Map<String,Object> bl = null;
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//            User user= ShiroUtils.getUser();
//			// 响应结果集
//			bl = saleReturnOrderService.auditSaleReturnOrder(list,user.getId(),user.getName(),user.getMerchantId(),user.getTopidealCode());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(bl);
//	}
//
//
//	/**
//	 * 导入销售退货订单(代销退货的)
//	 * */
//	@RequestMapping("/importSaleReturn.asyn")
//	@ResponseBody
//	public ViewResponseBean importSaleReturn(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			User user= ShiroUtils.getUser();
//			resultMap = saleReturnOrderService.importSaleReturnOrder(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
//        }catch(NullPointerException e){
//        	e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//	/**
//	 * 导入销售退货订单(消费者退货的)(新增的)
//	 * */
//	@RequestMapping("/importSaleReturn2.asyn")
//	@ResponseBody
//	public ViewResponseBean importSaleReturn2(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			User user= ShiroUtils.getUser();
//			resultMap = saleReturnOrderService.importSaleReturnOrder2(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
//        }catch(NullPointerException e){
//        	e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
//	 * */
//	@RequestMapping("/getOrderGoodsInfo.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderGoodsInfo(String ids) {
//		Map<String,Integer> map =new HashMap<String,Integer>();
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//			// 响应结果集
//            map = saleReturnOrderService.getOrderGoodsInfo(list);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//
//		return ResponseFactory.success(map);
//	}
//	/**
//	 * 判断是否可以出库打托
//	 * */
//	@RequestMapping("/isOutdepotReport.asyn")
//	@ResponseBody
//	private ViewResponseBean isOutdepotReport(Long id) {
//		Map<String, String> result = new HashMap<>();
//		try{
//			// 响应结果集
//			result = saleReturnOrderService.isOutdepotReport(id);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(result);
//	}
//	/**
//	 * 访问出库打托页面
//	 * */
//	@RequestMapping("/toOutDepotReport.html")
//	public String toOutDepotReport(Model model, Long id, HttpSession session)throws Exception{
//		SaleReturnOrderDTO saleReturnOrderDTO = saleReturnOrderService.searchDetail(id);
//		List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.listItemByOrderId(id);
//		saleReturnOrderDTO.setItemList(itemList);
//		model.addAttribute("detail", saleReturnOrderDTO);
//		return "/derp/sale/salereturnodepot-report";
//	}
//	/**
//	 * 保存出库打托信息
//	 * @param json
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/saveOdepotReport.asyn")
//	@ResponseBody
//	public ViewResponseBean saveOdepotReport(String json, HttpSession session) {
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            boolean b = saleReturnOdepotService.saveOdepotReport(json,user);
//            if(!b){
//                return ResponseFactory.error(StateCodeEnum.ERROR_301);
//            }
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//	/**
//	 * 访问上架入库页面
//	 * @param model
//	 * */
//	@RequestMapping("/toSaleReturnInPage.html")
//	public String toSaleReturnInPage(Model model, String id) throws Exception {
//		//查询销售退货订单
//
//		SaleReturnOrderDTO saleReturnOrder = saleReturnOrderService.searchDetail(Long.valueOf(id));
//
//		//查询调拨订单商品
//		SaleReturnOrderItemDTO itemDto = new SaleReturnOrderItemDTO();
//		itemDto.setOrderId(saleReturnOrder.getId());
//		List<SaleReturnOrderItemDTO> orderItem= saleReturnOrderService.getSaleReturnOrderItem(itemDto);
//
//		model.addAttribute("saleReturnOrder", saleReturnOrder);
//		model.addAttribute("orderItem", orderItem);
//		return "/derp/sale/salereturn-shelf";
//	}
//
//	@RequestMapping("/validInDepotDate.asyn")
//	@ResponseBody
//	public ViewResponseBean validInDepotDate(Long id, String returnInDate) {
//		boolean isNull = new EmptyCheckUtils().addObject(id).addObject(returnInDate).empty();
//		Map<String, String> result = new HashMap<>();
//		if (isNull) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//		try {
//			result = saleReturnOrderService.validInDepotDate(id, returnInDate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		}
//		return ResponseFactory.success(result);
//	}
//
//
//
//	@RequestMapping("/isExistSaleReturnIdepot.asyn")
//	@ResponseBody
//	public ViewResponseBean isExistSaleReturnIdepot(SaleReturnOrderDTO dto) {
//		boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).empty();
//		Map<String, String> result = new HashMap<>();
//		if (isNull) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//		User user= ShiroUtils.getUser();
//		dto.setMerchantId(user.getMerchantId());
//		try {
//			result = saleReturnOrderService.isExistSaleReturnIdepot(dto);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		}
//		return ResponseFactory.success(result);
//	}
//
//	/**
//	 * 上架入库
//	 */
//	@RequestMapping("/saveSaleReturnIdepot.asyn")
//	@ResponseBody
//	public ViewResponseBean saveSaleReturnIdepot(String json) {
//		Map<String, String> retMap = new HashMap<>();
//		try {
//			JSONObject jsonData=JSONObject.fromObject(json);
//			User user= ShiroUtils.getUser();
//			retMap = saleReturnOrderService.saveSaleReturnIdepot(jsonData, user);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(retMap);
//	}
//	/**
//	 * 获取导出订单的数量
//	 */
//	@RequestMapping("/getOrderCount.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderCount(SaleReturnOrderDTO dto) throws Exception{
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			List<SaleReturnOrderDTO> result = saleReturnOrderService.getListDTO(dto,user);
//			data.put("total",result.size());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//	/**
//	 * 导出销售退订单
//	 * */
//	@RequestMapping("/exportSaleReturn.asyn")
//	@ResponseBody
//	private void exportSaleReturn(SaleReturnOrderDTO dto,HttpServletResponse response, HttpServletRequest request) throws Exception{
//		User user= ShiroUtils.getUser();
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
//		// 表头
//		List<SaleReturnOrderDTO> result = saleReturnOrderService.getListDTO(dto,user);
//		String mainSheetName = "销售退订单导出";
//		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, SALERETURN_COLUMNS, SALERETURN_KEYS, result);
//		sheetList.add(mainSheet) ;
//		// 表体
//		List<SaleReturnOrderItemDTO> itemList = new ArrayList<SaleReturnOrderItemDTO>();
//		if(result != null && result.size() > 0) {
//			for(SaleReturnOrderDTO rDto:result){
//				//获取商品信息
//				List<SaleReturnOrderItemDTO> item = saleReturnOrderService.listItemByOrderId(rDto.getId());
//				if(item == null || item.size() < 1) continue;
//
//				for(SaleReturnOrderItemDTO itemDto: item) {
//					Integer num = itemDto.getReturnNum() + itemDto.getBadGoodsNum();
//					itemDto.setTotalPreNum(num);
//					itemDto.setOrderCode(rDto.getCode());
//					itemDto.setAmount(new BigDecimal(num).multiply(itemDto.getPrice()));
//				}
//				if(item != null && item.size() > 0){
//					itemList.addAll(item);
//				}
//			}
//
//		}
//		String itemSheetName = "商品信息";
//		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, SALERETURN_ITEM_COLUMNS, SALERETURN_ITEM_KEYS, itemList);
//		sheetList.add(itemSheet) ;
//
//		//生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
//	}
//}
