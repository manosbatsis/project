package com.topideal.order.webapi.sale;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.order.service.sale.SaleShelfService;
import com.topideal.order.service.sale.ShelfService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.ShelfResponseDTO;
import com.topideal.order.webapi.sale.form.ShelfForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * webapi 销售上架入库
 *
 */
@RequestMapping("/webapi/order/shelf")
@RestController
@Api(tags = "销售上架")
public class APIShelfController {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIShelfController.class);

	private static final String[] EXPORT_COLUMNS = { "上架单号", "销售订单号", "PO号", "客户","事业部", "上架日期", "单据状态",
			"商品货号", "商品条码","标准条码", "商品名称", "上架量", "残损量", "少货量",
			"操作人", "操作时间", "备注"};
	private static final String[] EXPORT_KEYS = {"code" , "sale_order_code","po_no", "customer_name","bu_name","shelf_date","state",
			"goods_no", "barcode", "commbarcode",  "goods_name","shelf_num", "damaged_num", "lack_num",
			"operator", "modify_date", "remark"} ;

	@Autowired
	private ShelfService shelfService ;

	@Autowired
	private SaleShelfService saleShelfService;

	@Autowired
	private RMQProducer rocketMQProducer;// MQ

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ShelfResponseDTO> toDetailsPage(Model model, Long id)throws Exception{
		ShelfResponseDTO responseDTO = new ShelfResponseDTO();
		try {
			ShelfDTO shelfDTO = shelfService.searchDetail(id);
			List<SaleShelfDTO> itemList = saleShelfService.listItemBySaleShelfId(id);
			responseDTO.setShelfDTO(shelfDTO);
			responseDTO.setItemList(itemList);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 获取上架单分页数据
	 * */
	@ApiOperation(value = "查询销售上架列表信息")
   	@PostMapping(value="/listShelf.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ShelfDTO> listShelf(ShelfForm form) {
		ShelfDTO dto = new ShelfDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form,dto);

			// 响应结果集
			dto = shelfService.listShelf(dto,user);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 获取导出销售出库清单的数量
	 */
	@ApiOperation(value = "获取导出销售上架单的数量")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 导出的销售上架单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(ShelfForm form) throws Exception{
		Integer total =0;
		try{
			ShelfDTO dto  = new ShelfDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form,dto);
			// 响应结果集
			total = shelfService.getOrderCount(dto,user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售上架单
	 * */
	@ApiOperation(value = "导出上架单",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportShelf.asyn")
	private void exportShelf(HttpServletResponse response, HttpServletRequest request,ShelfForm form) {
		try {
			ShelfDTO dto  = new ShelfDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form,dto);
			// 响应结果集
			List<Map<String,Object>> result = shelfService.getExportList(dto,user);

			String sheetName = "上架单";
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * 生成销售SD单
	 * */
	@ApiOperation(value = "生成销售SD单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id, 多个用逗号隔开", required = true)
	})
	@PostMapping(value="/generateSaleSdOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean generateSaleSdOrder(String token, String ids){
		try {
			User user= ShiroUtils.getUserByToken(token);
			List<String> shelfCodeList = shelfService.generateSaleSdOrder(ids, user);

			Map<String,Object> map = new HashMap<String, Object>();
    		map.put("orderCodes", StringUtils.join(shelfCodeList, ","));
    		map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
            rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

	}

	/**
	 * 生成上架入库单
	 * */
	@ApiOperation(value = "生成上架入库单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id, 多个用逗号隔开", required = true)
	})
	@PostMapping(value="/saveShelfIdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveShelfIdepot(String token, String ids){
		try {
			User user= ShiroUtils.getUserByToken(token);
			shelfService.saveShelfIdepot(ids, user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

	}
	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id, 多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delShelfOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delShelfOrder(String token, String ids){
		try {
			User user= ShiroUtils.getUserByToken(token);
			shelfService.delShelfOrder(ids, user, token);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

	}

}
