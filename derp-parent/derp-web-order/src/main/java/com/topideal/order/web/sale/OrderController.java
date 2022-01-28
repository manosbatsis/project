package com.topideal.order.web.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderHistoryDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.sale.OrderService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * 电商订单 controller
 * 
 */
@RequestMapping("/order")
@Controller
public class OrderController {

	// 电商订单service
	@Autowired
	private OrderService orderService;
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		User user= ShiroUtils.getUser(); 
		List<MerchantShopRelMongo> shopList = orderService.getMerchantShopRelList(user.getMerchantId());
		model.addAttribute("shopList",shopList);
		return "/derp/sale/order-list";
	}	
	/**
	 * 访问事业部补录列表
	 * */
	@RequestMapping("/toBusinessUnitRecordPage.html")
	public String toBusinessUnitRecordPage(Model model) throws Exception {
		User user= ShiroUtils.getUser(); 
		List<MerchantShopRelMongo> shopList = orderService.getMerchantShopRelList(user.getMerchantId());
		model.addAttribute("shopList",shopList);
		return "/derp/sale/order-businessunit-record-list";
	}
	
	/**
	 * 获取事业部补录列表分页数据
	 * */
	@RequestMapping("/listBusinessUnitRecord.asyn")
	@ResponseBody
	private ViewResponseBean listBusinessUnitRecord(OrderDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = orderService.listBusinessUnitRecordByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 查看某个商品的详情
	 * */
	@RequestMapping("/listItemDetailsById.html")
	@ResponseBody
	public ViewResponseBean listItemDetailsById(Long id)throws Exception{
		List<OrderItemDTO> itemList = new  ArrayList<OrderItemDTO>();
		OrderDTO dto =new OrderDTO();
		try{
			dto = orderService.searchDtoDetail(id);
			itemList = orderService.listItemByOrderId(id);
			dto.setItemList(itemList);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		OrderDTO dto = orderService.searchDtoDetail(id);
		if(dto==null){// 查询历史表
			OrderHistoryDTO orderHistoryDTO = orderService.searchHistoryDtoDetail(id);
			dto = new OrderDTO();
			BeanUtils.copyProperties(orderHistoryDTO,dto);
		}
		List<OrderItemDTO> itemList = orderService.listItemByOrderId(id);
		if(itemList == null){
			itemList = new ArrayList<OrderItemDTO>();
		}
		List<WayBillItemDTO> wayBillList = orderService.listWayBillItemByOrderId(id);
		dto.setItemList(itemList);
		// 脱敏信息 加****
		
		dto.setMakerName(StrUtils.desensitization(dto.getMakerName()));// 订购人
		dto.setMakerRegisterNo(StrUtils.desensitization(dto.getMakerRegisterNo()));//注册号
		dto.setMakerTel(StrUtils.desensitization(dto.getMakerTel()));//电话号码
		dto.setReceiverName(StrUtils.desensitization(dto.getReceiverName()));//收件人：
		dto.setReceiverTel(StrUtils.desensitization(dto.getReceiverTel()));//收件人电话
		dto.setReceiverAddress(StrUtils.desensitization(dto.getReceiverAddress()));//收件人地址
		
		model.addAttribute("detail", dto);
		model.addAttribute("batchData", wayBillList);
		return "/derp/sale/order-details";
	}

	/**
	 * 访问导入页面
	 *
	 */
	@RequestMapping("toImportPage.html")
	public String toImportPage() {
		return "/derp/sale/order-import";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listOrder.asyn")
	@ResponseBody
	private ViewResponseBean listOrder(OrderDTO dto, HttpSession session) {
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = orderService.listOrderAndItemByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 获取导出电商订单的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(OrderDTO dto,HttpSession session) {
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			int count = orderService.listOrder(dto);
			data.put("total",count);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出电商订单
	 * */
	@RequestMapping("/exportOrder.asyn")
	@ResponseBody
	private void exportOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request, OrderDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		//获取数据
		List<Map<String,Object>> result = orderService.getExportOrderMap(dto);
		for (Map<String, Object> map : result) {
			map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, (String) map.get("status")));
			map.put("store_platform_name", DERP.getLabelByKey(DERP.storePlatformList, (String) map.get("store_platform_name")));
			map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, (String) map.get("currency")));
			map.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, (String) map.get("shop_type_code")));		
		}
        String sheetName = "电商订单";
        String[] columns={"订单号","外部订单号","平台订单号","单据状态","平台名称","店铺名称","店铺类型","仓库名称","物流企业名称","运单号","包裹重量","交易时间","申报时间","放行时间","发货时间","订单总金额","运费","税费","总优惠金额","订单总佣金","事业部","客户","商品货号","条形码","商品编码","商品名称","数量","销售单价","销售总额","结算单价","结算总额","商品优惠金额","商品佣金","币种","商品税费"};
        String[] keys={"code","external_code","ex_order_id","status","store_platform_name", "shop_name","shop_type_code","depot_name","logistics_name","way_bill_no","weight","trading_date","declare_date","release_date","deliver_date","payment","way_frt_fee","taxes","discount","sale_com","bu_name","customer_name","goods_no", "barcode" ,"goods_code","goods_name","num",
        		"original_price","original_dec_total","price","dec_total","goods_discount","goods_sale_com","currency","goods_tax"};
        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 电商订单导入
	 * @param file
	 * @return
	 */
	@RequestMapping("importOrder.asyn")
	@ResponseBody
	public ViewResponseBean importOrder(@RequestParam(value = "file", required = false) MultipartFile file) {
		Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 2);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			User user= ShiroUtils.getUser();
			resultMap = orderService.importOrder(user , data) ;
			
			return ResponseFactory.success(resultMap);
		}catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		}catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
	}
	
	/**
	 * 删除手工导入订单
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("delOrder.asyn")
	@ResponseBody
	public ViewResponseBean delOrder(String ids) {
		//校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
        
        List<Long> list = StrUtils.parseIds(ids);
        
        try {
			int rows = orderService.delImportOrder(list);
			
			if(rows > 0) {
				return ResponseFactory.success() ;
			}else {
				return ResponseFactory.error(StateCodeEnum.ERROR_301) ;
			}
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		}
	}
	
	/**
	 * 检查手工导入订单是否满足条件
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("checkConditionsOrder.asyn")
	@ResponseBody
	public ViewResponseBean checkConditionsOrder(String ids) {
		User user= ShiroUtils.getUser();
		//校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
        
        List<Long> list = StrUtils.parseIds(ids);
        
        try {
			boolean flag = orderService.checkConditionsOrder(list) ;
			
			if(flag) {
				Map<String,Integer> map = orderService.getOrderGoodsInfo(list,user.getMerchantId()) ;	
				return ResponseFactory.success(map) ;
			}else {
				return ResponseFactory.error(StateCodeEnum.ERROR_305 , new RuntimeException("操作失败，所选项存在非手工导入订单")) ;
			}
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}
	
	/**
	 * 订单审核
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("examineOrder.asyn")
	@ResponseBody
	public ViewResponseBean examineOrder(String ids) {
		//校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
        
        List<Long> list = StrUtils.parseIds(ids);
        
        User user= ShiroUtils.getUser();
        
        try {
			orderService.auditOrder(list , user.getTopidealCode(),user.getMerchantId()) ;
			
			return ResponseFactory.success() ;
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}

	/**
	 * 事业部补录列表--批量更新
	 * @param info
	 * @param buId
	 * @param buName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("updateBusinessUnitRecord.asyn")
	@ResponseBody
	public ViewResponseBean updateBusinessUnitRecord(String info,Long buId,String buName) {
		String msg = null;//返回的结果集
        List<String> list = StrUtils.parseIdsToStr(info);// 外部交易单号：商品ID
        User user= ShiroUtils.getUser();
        try {
        	msg = orderService.modifyBusinessUnitRecord(list ,buId, buName,user.getTopidealCode(),user.getMerchantId(),user.getId()) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
        return ResponseFactory.success(msg);
	}
}
