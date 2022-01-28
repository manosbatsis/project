//package com.topideal.order.web.transfer;
//
//
//import com.topideal.common.constant.DERP;
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.*;
//import com.topideal.common.tools.excel.ExportExcelSheet;
//import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
//import com.topideal.entity.dto.transfer.TransferOrderDTO;
//import com.topideal.entity.dto.transfer.TransferOrderFrom;
//import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
//import com.topideal.mongo.dao.DepotInfoMongoDao;
//import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
//import com.topideal.mongo.dao.MerchantInfoMongoDao;
//import com.topideal.mongo.dao.PackTypeMongoDao;
//import com.topideal.mongo.entity.DepotInfoMongo;
//import com.topideal.mongo.entity.DepotMerchantRelMongo;
//import com.topideal.mongo.entity.MerchantInfoMongo;
//import com.topideal.mongo.entity.PackTypeMogo;
//import com.topideal.order.service.transfer.TransferOrderItemService;
//import com.topideal.order.service.transfer.TransferOrderService;
//import com.topideal.order.shiro.ShiroUtils;
//import com.topideal.order.tools.DownloadExcelUtil;
//import com.topideal.order.webapi.transfer.enums.ActionTypeEnum;
//import net.sf.json.JSONObject;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.ByteArrayOutputStream;
//import java.net.URLEncoder;
//import java.sql.SQLException;
//import java.util.*;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
///**
// * 调拨订单管理 控制层
// * @author yucaifu
// */
//@RequestMapping("/transfer")
//@Controller
//public class TransferOrderController {
//
//
//	@Autowired
//	private TransferOrderService transferOrderService;
//
//	@Autowired
//	private TransferOrderItemService transferOrderItemService;
//	// 仓库信息service
//	@Autowired
//	private DepotInfoMongoDao depotInfoMongoDao;
//	@Autowired
//	private PackTypeMongoDao packTypeDao;
//	@Autowired
//	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
//	@Autowired
//	private MerchantInfoMongoDao merchantInfoMongoDao;
//
//	/**
//	 * 访问列表页面
//	 * @param model
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model,HttpSession session) throws Exception{
//		User user= ShiroUtils.getUser();
//
//		return "derp/transfer/transferorder-list";
//	}
//
//	/**
//	 * 获取分页数据
//	 * @param dto
//	 * */
//	@RequestMapping("/listTransferOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean listTransferOrder(TransferOrderDTO dto, String createDateStart, String createDateEnd) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			//设置时间相关的条件
//			// 创建时间开始
//			if(createDateStart != null){
//				dto.setCreateDateStart(createDateStart);
//			}
//			// 创建时间结束
//			if(createDateEnd != null){
//				dto.setCreateDateEnd(createDateEnd);
//			}
//			// 响应结果集
//			dto = transferOrderService.listTransferOrderPage(dto);
//		}catch(Exception e){
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 详情
//	 * @param id 调拨订单ID
//	 * */
//	@RequestMapping("/toDetailPage.html")
//	public String toDetailPage(Model model,String id, String pageSource) throws Exception{
//		//查询调拨订单
//		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
//
//		//查询调拨订单商品
//		TransferOrderItemDTO param = new TransferOrderItemDTO();
//		param.setTransferOrderId(transferOrder.getId());
//		List<TransferOrderItemDTO> orderItem= transferOrderItemService.searchTransferOrderItem(param);
//
//		//查询调入仓库
//		Map<String, Object> depotParamMap = new HashMap<String, Object>();
//		depotParamMap.put("depotId",transferOrder.getOutDepotId());
//		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(depotParamMap);
//
//		model.addAttribute("transferOrder", transferOrder);
//		model.addAttribute("orderItem", orderItem);
//		model.addAttribute("outDepot", outDepot);
//		/*model.addAttribute("createUser", createUser);
//		model.addAttribute("submitUser", submitUser);*/
//		if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		return "/derp/transfer/transferorder-detail";
//	}
//
//	/**
//	 * 访问新增页面
//	 * @param model
//	 * */
//	@RequestMapping("/toAddPage.html")
//	public String toAddPage(Model model, String pageSource, HttpSession session, String id) throws Exception{
//		User user= ShiroUtils.getUser();
//
//		List<PackTypeMogo> packTypeList = packTypeDao.findAll(new HashMap<String, Object>());
//		model.addAttribute("packTypeList", packTypeList);//打包方式
//		model.addAttribute("user", user);//当前用户
//
//		Map<String, Object> merchantParams = new HashMap<>();
//		merchantParams.put("merchantId", user.getMerchantId());
//		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
//		if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource", pageSource);
//		}
//		if (!StringUtils.isEmpty(id)) {
//			TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
//			Map<String, Object> relParam = new HashMap<>();
//			relParam.put("merchantId", user.getMerchantId());
//			relParam.put("depotId", transferOrder.getOutDepotId());
//			DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
//			relParam.put("depotId", transferOrder.getInDepotId());
//			DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
//			//查询调拨订单商品
//			TransferOrderItemDTO param = new TransferOrderItemDTO();
//			param.setTransferOrderId(transferOrder.getId());
//			List<TransferOrderItemDTO> orderItem= transferOrderItemService.searchTransferOrderItem(param);
//			model.addAttribute("transferOrder", transferOrder);
//			model.addAttribute("orderItem", orderItem);
//			model.addAttribute("inDepotIsInOutInstruction", outDepotMerchantRel.getIsInOutInstruction());
//			model.addAttribute("outDepotIsInOutInstruction", inDepotMerchantRel.getIsInOutInstruction());
//		}
//		return "/derp/transfer/transferorder-add";
//	}
//
//	/**
//	 * 新增页面——调拨订单保存并审核
//	 * */
//	@RequestMapping("/saveTransferOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean saveTransferOrder(String json,HttpSession session) {
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		try {
//			 // 实例化JSON对象
//	        JSONObject jsonData=JSONObject.fromObject(json);
//	        Map classMap = new HashMap();
//			classMap.put("goodsList",Map.class);
//			TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class,classMap);
//
//			User user= ShiroUtils.getUser();
////			retMap = transferOrderService.saveTransferOrder(user.getId(),user.getName(),
////					user.getMerchantId(),user.getMerchantName(),user.getTopidealCode(),model);
//			transferOrderService.saveOrUpdateTransferOrder(user, model, ActionTypeEnum.ADD);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(retMap);
//	}
//
//	/**
//	 * 访问编辑页面
//	 * @param id 调拨订单ID
//	 * */
//	@RequestMapping("/toEditPage.html")
//	public String toEditPage(Model model,String id,String pageSource, HttpSession session) throws Exception{
//
//		User user= ShiroUtils.getUser();
//        List<PackTypeMogo> packTypeList = packTypeDao.findAll(new HashMap<String, Object>());
//
//		//查询调拨订单
//		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
//		Map<String, Object> relParam = new HashMap<>();
//		relParam.put("merchantId", user.getMerchantId());
//		relParam.put("depotId", transferOrder.getOutDepotId());
//		DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
//		relParam.put("depotId", transferOrder.getInDepotId());
//		DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
//		//查询调拨订单商品
//		TransferOrderItemDTO param = new TransferOrderItemDTO();
//		param.setTransferOrderId(transferOrder.getId());
//		List<TransferOrderItemDTO> orderItem= transferOrderItemService.searchTransferOrderItem(param);
//
//		model.addAttribute("transferOrder", transferOrder);
//		model.addAttribute("orderItem", orderItem);
//		model.addAttribute("packTypeList", packTypeList);//打包方式;
//		model.addAttribute("user", user);//当前客户
//		if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		Map<String, Object> merchantParams = new HashMap<>();
//		merchantParams.put("merchantId", user.getMerchantId());
//		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
//		model.addAttribute("inDepotIsInOutInstruction", outDepotMerchantRel.getIsInOutInstruction());
//		model.addAttribute("outDepotIsInOutInstruction", inDepotMerchantRel.getIsInOutInstruction());
//		return "/derp/transfer/transferorder-edit";
//	}
//	/**
//	 * 编辑页面——调拨订单保存并审核
//	 * */
//	@RequestMapping("/updateTransferOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean updateTransferOrder(String json,HttpSession session) {
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		try {
//			 // 实例化JSON对象
//	        JSONObject jsonData=JSONObject.fromObject(json);
//	        Map classMap = new HashMap();
//			classMap.put("goodsList",Map.class);
//			TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class,classMap);
//
//			User user= ShiroUtils.getUser();
//
////			retMap = transferOrderService.updateTransferOrder(user.getId(),user.getName(),user.getMerchantId(), model);
//			transferOrderService.saveOrUpdateTransferOrder(user, model, ActionTypeEnum.UPDATE);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(retMap);
//	}
//	/**
//	 * 访问导入页面
//	 * @param model
//	 * */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage(Model model) throws Exception{
//
//
//		return "/derp/transfer/transferorder-import";
//	}
//
//	/**
//	 * 删除
//	 * @param ids
//	 * */
//	@RequestMapping("/delTransferOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean delTransferOrder(String ids,HttpSession session) {
//		try{
//            //校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            List list = StrUtils.parseIds(ids);
//			Map<String, String> resultMap = transferOrderService.delTransferOrder(user.getId(), user.getName(), list);
//			String code = resultMap.get("code");
//			String message = resultMap.get("message");
//			if (("01").equals(code)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//
//	/**
//	 * 调拨订单导入
//	 * */
//	@RequestMapping("/imporTransfer.asyn")
//	@ResponseBody
//	public ViewResponseBean imporTransfer(@RequestParam(value = "file", required = false) MultipartFile file,HttpSession session) throws Exception{
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//			List<Map<String, String>> mainList = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(),"基本信息");
//			List<Map<String, String>> detailList = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(),"商品信息");
//            if(mainList == null || detailList == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			List<List<Map<String, String>>> sheetList = new ArrayList<>();
//            sheetList.add(mainList);
//            sheetList.add(detailList);
//			User user= ShiroUtils.getUser();
//		    resultMap = transferOrderService.saveImportTransfer(user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(),
//		    		user.getTopidealCode(),user.getRelMerchantIds(),sheetList);
//        }catch(Exception e){
//        	e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 获取调出商品分组统计数量
//	 * @param Ids 调拨单id
//	 * */
//	@RequestMapping("/getItemSumByIds.asyn")
//	@ResponseBody
//	private ViewResponseBean getItemSumByIds(String Ids) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//			//新
//			String[] idsStr = Ids.split(",");
//			List<Long> idarr = new ArrayList<Long>();
//			for(String id:idsStr){
//				idarr.add(Long.valueOf(id));
//			}
//			//查询明细
//			List<Map<String, Object>> itemList = transferOrderService.getItemSumByIds(idarr);
//			List<Map<String, Object>> mergeList = null;//合并好的明细
//			if(itemList!=null&&itemList.size()>0){
//				for(Map<String, Object> itemMap:itemList){
//					//获取仓库编码、仓库类型
//					Map<String,Object> map=new HashMap<String, Object>();
//					map.put("depotId", itemMap.get("out_depot_id"));
//					DepotInfoMongo depot = depotInfoMongoDao.findOne(map);
//					itemMap.put("depot_code", depot.getCode());
//					itemMap.put("depot_name", depot.getName());
//					itemMap.put("depot_type", depot.getType());//仓库类型
//				}
//
//				//合并
//				mergeList = mergeItem(itemList);
//			}
//			resultMap.put("mergeList", mergeList);
//		  }catch(Exception e){
//	     	e.printStackTrace();
//	         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//	     }
//     return ResponseFactory.success(resultMap);
//	}
//	/**
//	 * 按 仓库id、商品id、理货单位(香港仓)
//	 * */
//	private List<Map<String, Object>> mergeItem(List<Map<String, Object>> itemList){
//		List<Map<String, Object>> mergeList = new ArrayList<Map<String,Object>>();
//
//		/**合并
//		 * key=仓库id、商品id、理货单位(香港仓)
//		 * **/
//		Map<String, Map<String, Object>> mergeMap = new HashMap<String, Map<String, Object>>();
//		//循环合并明细
//		for(Map<String, Object> itemMap:itemList){
//			String depotId = String.valueOf(itemMap.get("out_depot_id"));//仓库
//			String goodsId = String.valueOf(itemMap.get("out_goods_id"));//商品
//
//			String tallyingUnit = "";//理货单位默认为空，香港仓时才需要
//			String depotType = (String) itemMap.get("depot_type");
//			if(depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)){
//				tallyingUnit = (String) itemMap.get("tallying_unit");
//				if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)){//转换为库存的理货单位
//					tallyingUnit = DERP.INVENTORY_UNIT_0;//托盘
//				}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)){
//					tallyingUnit = DERP.INVENTORY_UNIT_1;//箱
//				}else if(tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)){
//					tallyingUnit = DERP.INVENTORY_UNIT_2;//件
//				}
//			}
//			itemMap.put("tallying_unit", tallyingUnit);
//
//			String key = depotId+goodsId+tallyingUnit;
//			if(mergeMap.get(key)!=null){
//				Map<String, Object> itemValueMap = mergeMap.get(key);
//				int numAll = (int) itemValueMap.get("transfer_num");//已合并数量
//				int transfer_num = (int) itemMap.get("transfer_num");
//				itemValueMap.put("transfer_num", transfer_num+numAll);//数量相加
//			}else{
//				Map<String, Object> newItemValueMap = new HashMap<String, Object>();
//				newItemValueMap.put("out_depot_id", itemMap.get("out_depot_id"));
//				newItemValueMap.put("depot_code", itemMap.get("depot_code"));
//				newItemValueMap.put("depot_name", itemMap.get("depot_name"));
//				newItemValueMap.put("depot_type", itemMap.get("depot_type"));
//				newItemValueMap.put("out_goods_id", itemMap.get("out_goods_id"));
//				newItemValueMap.put("out_goods_no", itemMap.get("out_goods_no"));
//				newItemValueMap.put("tallying_unit", itemMap.get("tallying_unit"));
//				newItemValueMap.put("transfer_num", itemMap.get("transfer_num"));
//				mergeMap.put(key, newItemValueMap);
//			}
//		}
//		//遍历合并好的Map组装成list
//	    for(Map<String, Object> map:mergeMap.values()){
//	    	mergeList.add(map);
//	    }
//		return mergeList;
//	}
//
//	/**
//	 * 获取页面必填字段
//	 * @param outDepotId 调出仓库id
//	 * @param inDepotId 调入仓库id
//	 * @param isSameArea 是否同关区（0-否，1-是）
//	 * */
//	@RequestMapping("/getMustParameter.asyn")
//	@ResponseBody
//	private ViewResponseBean getMustParameter(Long outDepotId,Long inDepotId,String isSameArea) {
//		User user = ShiroUtils.getUser();
//		List<String> mustParamList = new ArrayList<>();
//		List<String> list = new ArrayList<>();
//		try{
//			//1、调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号
//			String[] paramAT = {"inCustomerIdLabel","contractNoLabel"};
//			//2、1）调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
//			//   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格
//			String[] paramAK = {"inCustomerIdLabel","contractNoLabel","invoiceNoLabel","packTypeLabel","cartonsLabel","firstLadingBillNoLabel","priceLabel"};
//			//3、调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号
//			String[] paramAK2 = {"inCustomerIdLabel","contractNoLabel"};
//			//4、调出仓库类型是海外仓 : 合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,收货人姓名,国家,目的地,海外理货单位，调入客户
//			String[] paramCA = {"contractNoLabel","invoiceNoLabel","packTypeLabel","cartonsLabel","firstLadingBillNoLabel","priceLabel",
//					"consigneeUsernameLabel","countryLabel","destinationLabel","inCustomerIdLabel"};
//			//5、调入仓库类型海外仓 : 合同号、海外理货单位、调入客户
//			String[] paramAC = {"contractNoLabel","inCustomerIdLabel"};
//			//6、保税仓调备查库【同\跨关区】：调入客户、合同号
//			String[] paramAB = {"inCustomerIdLabel","contractNoLabel"};
//			//7、调出仓库类型备查库或暂存区：调入客户、合同号、价格
//			String[] paramBE = {"inCustomerIdLabel","contractNoLabel","priceLabel"};
//
//		    Map<String, Object> param = new HashMap<String, Object>();
//		    param.put("depotId", outDepotId);
//		    DepotInfoMongo outDepot = depotInfoMongoDao.findOne(param);
//		    param.put("depotId", inDepotId);
//		    DepotInfoMongo inDepot = depotInfoMongoDao.findOne(param);
//
//			Map<String, Object> depotRelParam = new HashMap<String, Object>();
//			depotRelParam.put("depotId", inDepotId);
//			depotRelParam.put("merchantId", user.getMerchantId());
//			DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(depotRelParam);
//			depotRelParam.put("depotId", outDepotId);
//			DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(depotRelParam);
//
//			boolean isOutStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotMerchantRel.getIsInOutInstruction());
//			boolean isInStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotMerchantRel.getIsInOutInstruction());
//			String outDepotType = outDepot.getType();
//			String inDepotType = inDepot.getType();
//
//			//1、调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号
//			if(!StringUtils.isEmpty(isSameArea)
//					&&inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&DERP.ISSAMEAREA_1.equals(isSameArea)
//					&&isOutStruction &&isInStruction){
//				mustParamList = Arrays.asList(paramAT);
//			}
//			//2、1）调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
//			//   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格
//			else if(!StringUtils.isEmpty(isSameArea)&&inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&((DERP.ISSAMEAREA_1.equals(isSameArea) && isInStruction && !isOutStruction)
//					|| DERP.ISSAMEAREA_0.equals(isSameArea))){
//				mustParamList = Arrays.asList(paramAK);
//	 		}
//			//3、调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号
//			else if(!StringUtils.isEmpty(isSameArea)&&inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
//					&&DERP.ISSAMEAREA_1.equals(isSameArea)
//					&&!isInStruction) {
//				mustParamList = Arrays.asList(paramAK2);
//			}
//			//4、调出仓库类型是海外仓 : 合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,收货人姓名,国家,目的地,海外理货单位，调入客户
//			else if (outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
//				mustParamList = Arrays.asList(paramCA);
//			}
//			//5、调入仓库类型海外仓 : 合同号、海外理货单位、调入客户
//			else if (inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
//				mustParamList = Arrays.asList(paramAC);
//			}
//			//6、保税仓调备查库【同\跨关区】：调入客户、合同号
//			else if (!StringUtils.isEmpty(isSameArea) && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotType)
//					&& DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotType)) {
//				mustParamList = Arrays.asList(paramAB);
//			}
//			//7、调出仓库类型备查库或暂存区：调入客户、合同号、价格
//			else if (outDepotType.matches(DERP_SYS.DEPOTINFO_TYPE_B + "|" + DERP_SYS.DEPOTINFO_TYPE_E)) {
//				mustParamList = Arrays.asList(paramBE);
//			}
//			list = new ArrayList<>(mustParamList);
//
//			//9、唯品代销仓: po号
//			if ("VIP001".equals(inDepot.getDepotCode()) || "VIP001".equals(outDepot.getDepotCode())) {
//				list.add("poNoLabel");
//			}
//
//			//10、入仓仓库是备查仓时，调入仓地址必填
//			if (DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
//				list.add("depotScheduleIdLabel");
//			}
//
//			//11、当调入/调出仓需推接口指令时，价格必填
//			if (isOutStruction || isInStruction) {
//				if (!list.contains("priceLabel")) {
//					list.add("priceLabel");
//				}
//			}
//			//当调出仓库为海外仓，调入仓库为保税仓时，商品信息列表栏合同号、箱号为必填
//			if (outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C) && inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
//				list.add("contNoLabel");
//				list.add("bargainnoLabel");
//			}
//
//			//入库仓为推送接口指令时，装货港必填；
//			if (isInStruction) {
//				list.add("portLoadingLabel");
//			}
//	     }catch(Exception e){
//	     	e.printStackTrace();
//	         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//	     }
//     	 return ResponseFactory.success(org.apache.commons.lang3.StringUtils.join(list.toArray(), ","));
//	}
//
//	/**
//	 * 修改调拨单LbxNo
//	 * */
//	@RequestMapping("/updateLbxNo.asyn")
//	@ResponseBody
//	public ViewResponseBean updateLbxNo(Long transOrderId,String newLbxNo,HttpSession session) {
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		try{
//			  User user= ShiroUtils.getUser();
//			  retMap = transferOrderService.updateLbxNo(user.getId(), user.getName(), transOrderId, newLbxNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(retMap);
//	}
//
//
//	/**
//	 * 新增/编辑页面——保存调拨订单,保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
//	 * */
//	@RequestMapping("/saveTempTransferOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean saveTempTransferOrder(String json) {
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		try {
//			// 实例化JSON对象
//			JSONObject jsonData=JSONObject.fromObject(json);
//			Map classMap = new HashMap();
//			classMap.put("goodsList",Map.class);
//			TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class,classMap);
//
//			User user= ShiroUtils.getUser();
//			retMap = transferOrderService.saveTempTransferOrder(user.getId(),user.getName(),
//					user.getMerchantId(),user.getMerchantName(),user.getTopidealCode(),model);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(retMap);
//	}
//
//
//	/**
//	 * 导出统计
//	 * */
//	@RequestMapping("/exportTransferCount.asyn")
//	@ResponseBody
//	private ViewResponseBean exportTransferCount(TransferOrderDTO dto) throws Exception{
//		User user= ShiroUtils.getUser();
//		dto.setMerchantId(user.getMerchantId());
//		Long count = transferOrderService.countForExport(dto);
//		Integer max = DERP.EXPORT_MAX;
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		retMap.put("code", "00");
//		retMap.put("message", "检查通过");
//		if(count.intValue()>max.intValue()){
//			retMap.put("code", "01");
//			retMap.put("message", "导出数量超过"+max+"请分多次导出");
//		}
//		return ResponseFactory.success(retMap);
//	}
//
//	/**
//	 * 根据查询条件导出调拨订单
//	 */
//	@RequestMapping("/exportTransferOrder.asyn")
//	public void exportTransferOrder(TransferOrderDTO dto, HttpServletResponse response, HttpServletRequest request) {
//		User user= ShiroUtils.getUser();
//		dto.setMerchantId(user.getMerchantId());
//
//		//表头信息
//		List<Map<String,Object>> transferOrderList = transferOrderService.listForExport(dto);
//
//		//表头信息
//		String sheetName1 = "表头信息";
//		String[] columns1 = {"调拨单号","合同号","调出仓库","调入仓库","事业部","单据状态","LBX单号","创建人","创建时间",
//				"发票号","包装方式", "箱数","提单号","运输方式","承运商","车次","标准箱TEU", "托数","出仓地点", "收货地址","海外理货单位"};
//		String[] keys1 = {"code","contract_no","out_depot_name","in_depot_name","bu_name","status","lbx_no","create_username","create_date",
//				"invoice_no","pack_type", "cartons", "first_lading_bill_no","transport","carrier","train_number","standard_case_teu","torr_num",
//				"outDepot_addr","receiving_address","tallying_unit"};
//
//		//商品信息
//		List<Map<String,Object>> itemList = transferOrderService.listForExportItem(dto);
//
//		String sheetName2 = "表体商品信息";
//		String[] columns2 = {"调拨单号","调出商品编号","调出商品货号","调出商品名称","调出单位","调出数量","调入商品编号","调入商品货号",
//				"调入商品名称","调入单位","调入数量","品牌类型", "调拨单价","毛重","净重","箱号","合同号","备注"};
//		String[] keys2 = {"code","out_goods_code","out_goods_no","out_goods_name","out_unit","transfer_num","in_goods_code","in_goods_no",
//				"in_goods_name","in_unit","in_transfer_num","brand_name","price","gross_weight","net_weight","cont_no","bargainno","remark"};
//		//生成表格
//		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, transferOrderList);
//		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, itemList);
//
//		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
//		sheets.add(mainSheet) ;
//		sheets.add(itemSheet) ;
//
//		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
//		//导出文件
//		String fileName = "调拨订单"+ TimeUtils.formatDay();
//		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
//	}
//
//	@RequestMapping("/isExistTransferOutOrInOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean isExistTransferOutOrInOrder(TransferOrderDTO dto) {
//		boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).addObject(dto.getOrderType()).empty();
//		Map<String, String> result = new HashMap<>();
//		if (isNull) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//		User user= ShiroUtils.getUser();
//		dto.setMerchantId(user.getMerchantId());
//		try {
//			result = transferOrderService.isExistOrder(dto);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		}
//		return ResponseFactory.success(result);
//	}
//
//	/**
//	 * 访问上架入库页面
//	 * @param model
//	 * */
//	@RequestMapping("/toTransferInPage.html")
//	public String toTransferInPage(Model model, String id) throws Exception {
//		//查询调拨订单
//		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
//
//		//查询调拨订单商品
//		TransferOrderItemDTO param = new TransferOrderItemDTO();
//		param.setTransferOrderId(transferOrder.getId());
//		List<TransferOrderItemDTO> orderItem= transferOrderItemService.searchTransferOrderItem(param);
//
//		model.addAttribute("transferOrder", transferOrder);
//		model.addAttribute("orderItem", orderItem);
//		return "/derp/transfer/transferorder-shelf";
//	}
//
//	/**
//	 * 访问出库报告页面
//	 * @param model
//	 * */
//	@RequestMapping("/toTransferOutPage.html")
//	public String toTransferOutPage(Model model, String id) throws Exception {
//		//查询调拨订单
//		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
//
//		//查询调拨订单商品
//		TransferOrderItemDTO param = new TransferOrderItemDTO();
//		param.setTransferOrderId(transferOrder.getId());
//		List<TransferOrderItemDTO> orderItem= transferOrderItemService.searchTransferOrderItem(param);
//
//		model.addAttribute("transferOrder", transferOrder);
//		model.addAttribute("orderItem", orderItem);
//		return "/derp/transfer/transferorder-out";
//	}
//
//	/**
//	 * 上架入库时间校验
//	 * @param id
//	 * @param transferInDate
//	 * @return
//	 */
//	@RequestMapping("/validInDepotDate.asyn")
//	@ResponseBody
//	public ViewResponseBean validInDepotDate(Long id, String transferInDate) {
//		boolean isNull = new EmptyCheckUtils().addObject(id).addObject(transferInDate).empty();
//		Map<String, String> result = new HashMap<>();
//		if (isNull) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//		try {
//			result = transferOrderService.validInDepotDate(id, transferInDate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		}
//		return ResponseFactory.success(result);
//	}
//	/**
//     * 商品导入
//     * @param file
//     * @param inDepotId
//     * @param session
//     * @return
//     */
//    @RequestMapping("/importTransferGoods.asyn")
//	@ResponseBody
//	public ViewResponseBean importTransferGoods(@RequestParam(value = "file", required = false) MultipartFile file,String outDepotId,String inDepotId, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			User user= ShiroUtils.getUser();
//			resultMap = transferOrderService.importTransferGoods(data,outDepotId,inDepotId,user);
//        }catch(Exception e){
//			e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//    /**
//	 * 商品导出
//	 * @param response
//	 * @param request
//	 * @throws Exception
//	 */
//	@RequestMapping("/exportItems.asyn")
//	public void export(HttpServletResponse response, HttpServletRequest request, String  json) throws Exception{
//		/** 标题  */
//
//		String[] COLUMNS= {"序号","调出商品货号","调入商品货号","条形码","托盘号","箱数","调出数量","调拨单价","净重（KG）","毛重（KG）"};
//		String[] KEYS= {"seq","outGoodsNo","inGoodsNo","outBarcode","torrNo","boxNum","transferNum","price","netWeightSum","grossWeightSum"};
//
//		String sheetName = "商品导出";
//		List<TransferOrderItemDTO> itemList=null;
//		if(json != null || org.apache.commons.lang3.StringUtils.isNotBlank(json)){			//输入信息不完整
//			itemList = transferOrderService.exportListItem(json);
//		}
//		//生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , itemList);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//
//	/**
//	 * 打托出库时间校验
//	 * @param id
//	 * @param transferOutDate
//	 * @return
//	 */
//	@RequestMapping("/validOutDepotDate.asyn")
//	@ResponseBody
//	public ViewResponseBean validOutDepotDate(Long id, String transferOutDate) {
//		boolean isNull = new EmptyCheckUtils().addObject(id).addObject(transferOutDate).empty();
//		Map<String, String> result = new HashMap<>();
//		if (isNull) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//		try {
//			result = transferOrderService.validOutDepotDate(id, transferOutDate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
//		}
//		return ResponseFactory.success(result);
//	}
//
//	/**
//	 * 一线清关资料导出
//	 */
//	@RequestMapping("/exportCustomsDeclare.asyn")
//	public void exportCustomsDeclare(HttpServletResponse response, String json) throws Exception {
//		try {
//			JSONObject jsonData = JSONObject.fromObject(json);
//			String id = (String) jsonData.get("id");
//			String inFileTempIds = (String) jsonData.get("inFileTempIds");
//			String outFileTempIds = (String) jsonData.get("outFileTempIds");
//
//			if (StringUtils.isEmpty(id)) {
//				return;
//			}
//			Map<String, Workbook> resultMap = new HashMap<>();
//			if (!StringUtils.isEmpty(inFileTempIds)) {
//				List<Long> inFileTempIdList = new ArrayList<>();
//				List<String> list = Arrays.asList(inFileTempIds.split(","));
//				for (String idStr : list) {
//					inFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, Object> resultInMap = transferOrderService.exportInDepotCustomsDeclares(Long.valueOf(id), inFileTempIdList);
//				Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
//				resultMap.putAll(multipartInCustomsDeclares);
//			}
//
//			if (!StringUtils.isEmpty(outFileTempIds)) {
//				List<Long> outFileTempIdList = new ArrayList<>();
//				List<String> list = Arrays.asList(outFileTempIds.split(","));
//				for (String idStr : list) {
//					outFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, FirstCustomsInfoDTO> resultOutMap = transferOrderService.exportOutDepotCustomsDeclares(Long.valueOf(id), outFileTempIdList);
//				Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
//				resultMap.putAll(multipartOutCustomsDeclares);
//			}
//
//			//导出压缩文件
//			String downloadFilename = URLEncoder.encode("调拨订单一线清关资料.zip", "UTF-8");
//			// 指明response的返回对象是文件流
//			response.setContentType("application/octet-stream");
//			// 设置在下载框默认显示的文件名
//			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
//			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//
//			for (String key : resultMap.keySet()) {
//				Workbook wb = resultMap.get(key);
//				zos.putNextEntry(new ZipEntry(key));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				wb.write(bos);
//				bos.writeTo(zos);
//				zos.closeEntry();
//			}
//			zos.flush();
//			zos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 装箱明细导入
//	 * @param file
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping("/importPackingDetails.asyn")
//	@ResponseBody
//	public ViewResponseBean importPackingDetails(@RequestParam(value = "file", required = false) MultipartFile file,Long id) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//			if(file==null){
//				//输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//			if(data == null){//数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//			User user= ShiroUtils.getUser();
//			resultMap = transferOrderService.importPackingDetails(data,id,user);
//		}catch(Exception e){
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(resultMap);
//	}
//
//
//}
