package com.topideal.order.webapi.sale;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.order.service.base.PackTypeService;
import com.topideal.order.service.sale.SaleReturnOdepotService;
import com.topideal.order.service.sale.SaleReturnOrderService;
import com.topideal.order.service.sale.SaleShelfService;
import com.topideal.order.service.sale.ShelfService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderGoodsInfoNumDTO;
import com.topideal.order.webapi.sale.dto.ResultDTO;
import com.topideal.order.webapi.sale.dto.SaleReturnOrderResponseDTO;
import com.topideal.order.webapi.sale.form.SaleReturnOrderForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * webapi 销售退货订单
 *
 */
@RequestMapping("/webapi/order/saleReturn")
@RestController
@Api(tags = "销售退货订单")
public class APISaleReturnOrderController {
	private static final String[] SALERETURN_COLUMNS = {"销售退订单号","关联销售订单号","PO号","退出仓库","退入仓库","事业部","客户","服务类型","退货方式","装货港","合同号","发票号","LBX单号","收货地址","境外发货人","退货币种","退货原因","创建人","创建时间","库位类型"};
	private static final String[] SALERETURN_KEYS = {"code","saleOrderRelCode","poNo","outDepotName","inDepotName","buName","customerName","serveTypesLabel","returnModeLabel","portLoading","contractNo","invoiceNo","lbxNo","deliveryAddr","shipper","currencyLabel","remark","createName","createDate","stockLocationTypeName"};
	private static final String[] SALERETURN_ITEM_COLUMNS ={"销售退订单号","PO号码","退出商品货号","退入商品货号","退入商品名称","退入商品条形码","退货商品数量","退入商品单价（不含税）","退入商品总金额（不含税）","退入商品总金额（含税）","税率","税额"};
	private static final String[] SALERETURN_ITEM_KEYS ={"orderCode","poNo","outGoodsNo","inGoodsNo","inGoodsName","barcode","totalPreNum","price","amount","taxAmount","taxRate","tax"};
	// 销售退货订单service
	@Autowired
	private SaleReturnOrderService saleReturnOrderService;
	@Autowired
	private PackTypeService packTypeService;
	// 仓库商家关联表
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 销售退出库订单service
	@Autowired
	private SaleReturnOdepotService saleReturnOdepotService;
	@Autowired
	private SaleShelfService saleShelfService;
	@Autowired
	private ShelfService shelfService;

	/**
	 * 访问新增页面
	 * @param model
	 * @param ids   销售订单 ID
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问新增页面")
	@PostMapping(value="/toAddPage.html",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<SaleReturnOrderResponseDTO> toAddPage(@RequestBody SaleReturnOrderForm form, HttpSession session)throws Exception{
		SaleReturnOrderResponseDTO responseDTO = new SaleReturnOrderResponseDTO();
		try {
			//初始化数据
			responseDTO.setItemCount("0");
			User user= ShiroUtils.getUserByToken(form.getToken());
			if(form.getSaleOrderId() != null){//ids不为空，需要获取数据

				SaleReturnOrderDTO saleReturnOrderDTO = new SaleReturnOrderDTO();
				BeanUtils.copyProperties(form,saleReturnOrderDTO);
				saleReturnOrderDTO = saleReturnOrderService.listSaleOrderAndOutDepotOrder(saleReturnOrderDTO,user.getMerchantId());
				BeanUtils.copyProperties(saleReturnOrderDTO,responseDTO);
				responseDTO.setItemCount(saleReturnOrderDTO.getItemList().size()+"");
				// 如果是购销退货则退货类型、客户两个字段均不予重新编辑
				responseDTO.setIsForbid("no");
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);

	}
	/**
	 *  多选订单时，需校验是否相同出仓仓库、相同客户，若有不同则报错提示，不予保存提交。
	 * */
	@ApiOperation(value = "校验是否相同出仓仓库、相同客户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/isSameParameter.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean isSameParameter(String token,String ids,HttpSession session) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            saleReturnOrderService.isSameParameter(ids);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@PostMapping(value="/saveSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveSaleReturnOrder(@RequestBody SaleReturnOrderForm form) {
		try{
            User user= ShiroUtils.getUserByToken(form.getToken());
			SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
			BeanUtils.copyProperties(form, dto);
            saleReturnOrderService.saveSaleReturnOrder(dto, user);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 访问编辑页面
	 * */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据ids", required = true)
	})
	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnOrderResponseDTO> toEditPage(String token,Long id, HttpSession session)throws Exception{
		SaleReturnOrderResponseDTO responseDTO = new SaleReturnOrderResponseDTO();
		try {
			User user= ShiroUtils.getUserByToken(token);
			SaleReturnOrderDTO saleReturnOrderDTO = saleReturnOrderService.searchDetail(id);
			if(StringUtils.isNotBlank(saleReturnOrderDTO.getSaleOrderRelCode())){
				// 如果是购销退货则退货类型、客户两个字段均不予重新编辑
				responseDTO.setIsForbid("no");
			}
			List<SaleReturnOrderItemDTO> saleReturnOrderItemList = saleReturnOrderService.listItemByOrderId(id);
			List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.getOrignGrossNetWeightItem(saleReturnOrderItemList);
			saleReturnOrderDTO.setItemList(itemList);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleReturnOrderDTO.getInDepotId());
			DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params);
			params.clear();
			params.put("depotId", saleReturnOrderDTO.getInDepotId());
			params.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(params);
			int is_required = 0; // 是否必填
			// 购销退货、代销退货类型，退入仓为保税仓（并在对应商家下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
			if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleReturnOrderDTO.getReturnType())
				|| DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleReturnOrderDTO.getReturnType())){
				if(inDepotInfo!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepotInfo.getType())
					&& depotMerchantRel!=null && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())){
					is_required = 1;
				}
			}
			BeanUtils.copyProperties(saleReturnOrderDTO,responseDTO);
			responseDTO.setIsRequired(is_required+"");
			responseDTO.setInDepotType(inDepotInfo!=null?inDepotInfo.getType():"");
			responseDTO.setInIsInOutInstruction(depotMerchantRel!=null?depotMerchantRel.getIsInOutInstruction():"");
			responseDTO.setItemCount(itemList.size()+"");

			List<SelectBean> packTypeBean = packTypeService.getSelectBean();
			responseDTO.setPackTypeBean(packTypeBean);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改")
	@PostMapping(value="/modifySaleReturnOrder.asyn", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean modifySaleReturnOrder(@RequestBody SaleReturnOrderForm form) {
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
			BeanUtils.copyProperties(form, dto);
			saleReturnOrderService.saveSaleReturnOrder(dto, user);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnOrderDTO> toDetailsPage(String token, Long id)throws Exception{
		SaleReturnOrderDTO saleReturnOrderDTO = new SaleReturnOrderDTO();
		try {
			saleReturnOrderDTO = saleReturnOrderService.searchDetail(id);
			List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.listItemByOrderId(id);
			saleReturnOrderDTO.setItemList(itemList);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleReturnOrderDTO);
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询销售退货订单列表信息")
	@PostMapping(value="/listSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleReturnOrderDTO> listSaleReturnOrder(SaleReturnOrderForm form, HttpSession session) {
		SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = saleReturnOrderService.listSaleReturnOrderByPage(dto,user);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/delSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleReturnOrder(String token,String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = saleReturnOrderService.delSaleReturnOrder(list);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");
            }
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 审核
	 * */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<UploadResponse> auditSaleReturnOrder(String token,String ids, HttpSession session) {
		try{
			Map<String,Object> bl = null;
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			bl = saleReturnOrderService.auditSaleReturnOrder(list,user);
			Integer success = (Integer)bl.get("success");
			Integer failure = (Integer)bl.get("failure");
			ImportMessage message = new ImportMessage();
			message.setMessage((String)bl.get("msg"));
			List<ImportMessage> errorList = new ArrayList<ImportMessage>();
			errorList.add(message);
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


	/**
	 * 导入销售退货订单(代销退货的)
	 * */
	@ApiOperation(value = "导入销售退货订单(代销退货的)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSaleReturn.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleReturn(String token,
											@ApiParam(value = "上传的文件", required = true)
											@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
            if(file==null){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleReturnOrderService.importSaleReturnOrder(data,user,user.getRelMerchantIds());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 导入销售退货订单(消费者退货的)(新增的)
	 * */
	@ApiOperation(value = "导入销售退货订单(消费者退货的)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSaleReturn2.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleReturn2(String token,
										@ApiParam(value = "上传的文件", required = true)
										@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		Map resultMap = new HashMap();//返回的结果集
		try{
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleReturnOrderService.importSaleReturnOrder2(data,user ,user.getRelMerchantIds());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * */
	@ApiOperation(value = "获取选中订单的所有商品和对应数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message = "data => code:由 商品id-出库仓id-出库仓编码-商品货号-是否同关区-退货类型 拼接而成 ")
	})
	@PostMapping(value="/getOrderGoodsInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<OrderGoodsInfoNumDTO>> getOrderGoodsInfo(String token,String ids) {
		try{
			Map<String,Integer> map =new HashMap<String,Integer>();
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
			// 响应结果集
            map = saleReturnOrderService.getOrderGoodsInfo(list);
            List<OrderGoodsInfoNumDTO> goodsInfoNumList = new ArrayList<OrderGoodsInfoNumDTO>();
			for(Entry<String, Integer> entry : map.entrySet()) {
            	OrderGoodsInfoNumDTO numDTO = new OrderGoodsInfoNumDTO();
            	numDTO.setCode(entry.getKey());
            	numDTO.setNum(entry.getValue());
            	goodsInfoNumList.add(numDTO);
            }
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,goodsInfoNumList);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 判断是否可以出库打托
	 * */
	@ApiOperation(value = "是否可以出库打托")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售退单据id", required = true)
	})
	@PostMapping(value="/isOutdepotReport.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ResultDTO> isOutdepotReport(String token,Long id) {
		ResultDTO dto =  new ResultDTO();
		try{
			Map<String, String> result = new HashMap<>();
			// 响应结果集
			result = saleReturnOrderService.isOutdepotReport(id);
			dto.setCode(result.get("code"));
			if("01".equals(result.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),result.get("message"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 访问出库打托页面
	 * */
	@ApiOperation(value = "访问出库打托页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售退单据id", required = true)
	})
	@PostMapping(value="/toOutDepotReport.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnOrderDTO> toOutDepotReport(String token,Long id, HttpSession session)throws Exception{
		SaleReturnOrderDTO saleReturnOrderDTO = new SaleReturnOrderDTO();
		try {
			saleReturnOrderDTO = saleReturnOrderService.searchDetail(id);
			List<SaleReturnOrderItemDTO> itemList = saleReturnOrderService.listItemByOrderId(id);
			saleReturnOrderDTO.setItemList(itemList);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleReturnOrderDTO);


	}
	/**
	 * 保存出库打托信息
	 * @param json
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "保存出库打托信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装销售退订单信息", required = true)
	})
	@PostMapping(value="/saveOdepotReport.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOdepotReport(String token ,String json, HttpSession session) {
		try{
            if(json == null || StringUtils.isBlank(json)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            boolean b = saleReturnOdepotService.saveOdepotReport(json,user);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"打托出库失败");
            }
        }catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 访问上架入库页面
	 * @param model
	 * */
	@ApiOperation(value = "访问上架入库页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售退单据id", required = true)
	})
	@PostMapping(value="/toSaleReturnInPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnOrderDTO> toSaleReturnInPage(String id) throws Exception {
		SaleReturnOrderDTO saleReturnOrder = new SaleReturnOrderDTO();
		try {
			saleReturnOrder = saleReturnOrderService.searchDetail(Long.valueOf(id));

			//查询调拨订单商品
			SaleReturnOrderItemDTO itemDto = new SaleReturnOrderItemDTO();
			itemDto.setOrderId(saleReturnOrder.getId());
			List<SaleReturnOrderItemDTO> orderItem= saleReturnOrderService.getSaleReturnOrderItem(itemDto);

			saleReturnOrder.setItemList(orderItem);

		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleReturnOrder);
	}

	@ApiOperation(value = "校验入库时间是否满足条件")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售退单据id", required = true),
		@ApiImplicitParam(name = "returnInDate", value = "入库时间", required = true)
	})
	@PostMapping(value="/validInDepotDate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> validInDepotDate(Long id, String returnInDate) {
		ResultDTO dto = new ResultDTO();
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(returnInDate).empty();
			Map<String, String> result = new HashMap<>();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			result = saleReturnOrderService.validInDepotDate(id, returnInDate);
			dto.setCode(result.get("code"));
			dto.setMessage(result.get("message"));

			if("01".equals(result.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),result.get("message"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}



	@ApiOperation(value = "是否存在销售退入库信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售退单据id", required = true)
	})
	@PostMapping(value="/isExistSaleReturnIdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> isExistSaleReturnIdepot(String token,String id) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
			dto.setId(Long.valueOf(id));

			Map<String, String> result = new HashMap<>();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			dto.setMerchantId(user.getMerchantId());
			result = saleReturnOrderService.isExistSaleReturnIdepot(dto);
			resultDTO.setCode(result.get("code"));
			resultDTO.setMessage(result.get("message"));
			if("01".equals(result.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),result.get("message"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}

	/**
	 * 上架入库
	 */
	@ApiOperation(value = "上架入库")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了销售退信息", required = true)
	})
	@PostMapping(value="/saveSaleReturnIdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> saveSaleReturnIdepot(String token,String json) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, String> retMap = new HashMap<>();
			JSONObject jsonData=JSONObject.fromObject(json);
			User user= ShiroUtils.getUserByToken(token);
			retMap = saleReturnOrderService.saveSaleReturnIdepot(jsonData, user);
			resultDTO.setCode(retMap.get("code"));
			resultDTO.setMessage(retMap.get("message"));

			if("01".equals(retMap.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),retMap.get("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}
	/**
	 * 获取导出订单的数量
	 */
	@ApiOperation(value = "获取导出订单的数量")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 导出的订单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(SaleReturnOrderForm form) throws Exception{
		Integer total = 0;
		try{
			SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(form.getOutDepotId());
			dto.setCustomerId(form.getCustomerId());
			dto.setBuId(form.getBuId());
			dto.setStatus(form.getStatus());
			dto.setSaleOrderRelCode(form.getSaleOrderRelCode());
			dto.setReturnType(form.getReturnType());
			dto.setCreateStartDate(form.getCreateStartDate());
			dto.setCreateEndDate(form.getCreateEndDate());
			dto.setIds(form.getIds());
			// 响应结果集
			List<SaleReturnOrderDTO> result = saleReturnOrderService.getListDTO(dto,user);
			total = result.size();
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售退订单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleReturn.asyn")
	private ResponseBean exportSaleReturn(SaleReturnOrderForm form,HttpServletResponse response, HttpServletRequest request) throws Exception{
		try {
			SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(form.getOutDepotId());
			dto.setCustomerId(form.getCustomerId());
			dto.setBuId(form.getBuId());
			dto.setStatus(form.getStatus());
			dto.setSaleOrderRelCode(form.getSaleOrderRelCode());
			dto.setReturnType(form.getReturnType());
			dto.setCreateStartDate(form.getCreateStartDate());
			dto.setCreateEndDate(form.getCreateEndDate());
			dto.setIds(form.getIds());
			List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
			// 表头
			List<SaleReturnOrderDTO> result = saleReturnOrderService.getListDTO(dto,user);
			String mainSheetName = "销售退订单导出";
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, SALERETURN_COLUMNS, SALERETURN_KEYS, result);
			sheetList.add(mainSheet) ;
			// 表体
			List<SaleReturnOrderItemDTO> itemList = new ArrayList<SaleReturnOrderItemDTO>();
			if(result != null && result.size() > 0) {
				for(SaleReturnOrderDTO rDto:result){
					//获取商品信息
					List<SaleReturnOrderItemDTO> item = saleReturnOrderService.listItemByOrderId(rDto.getId());
					if(item == null || item.size() < 1) continue;

					for(SaleReturnOrderItemDTO itemDto: item) {
						Integer num = itemDto.getReturnNum() + itemDto.getBadGoodsNum();
						itemDto.setTotalPreNum(num);
						itemDto.setOrderCode(rDto.getCode());
						itemDto.setAmount(new BigDecimal(num).multiply(itemDto.getPrice()));
					}
					if(item != null && item.size() > 0){
						itemList.addAll(item);
					}
				}

			}
			String itemSheetName = "商品信息";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, SALERETURN_ITEM_COLUMNS, SALERETURN_ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "选择销售退货PO号页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "saleOrderId", value = "销售订单id", required = true),
			@ApiImplicitParam(name = "saleOrderCode", value = "销售订单号", required = true),
			@ApiImplicitParam(name = "poNos", value = "po号，多个用逗号隔开")
	})
	@PostMapping(value="/getReturnItemListAndPOList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String,Object>> getReturnItemListAndPOList(String token,Long saleOrderId, String saleOrderCode, String poNos) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if(saleOrderId == null){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"销售订单id不能为空");
			}
			// 存放该销售订单的已上架的PO单号
			ShelfDTO shelfDTO = new ShelfDTO();
			shelfDTO.setSaleOrderCode(saleOrderCode);
			List<ShelfDTO> shelfList = shelfService.listShelfDTO(shelfDTO);
			List<String> poList=new ArrayList<>();
			if(shelfList != null && shelfList.size() > 0) {
				poList = shelfList.stream().map(ShelfDTO::getPoNo).distinct().collect(Collectors.toList());
			}
			List<String> tempPoList=new ArrayList<>();
			if(StringUtils.isNotEmpty(poNos)) {
				tempPoList = Arrays.stream(poNos.split(",")).collect(Collectors.toList());
			}
			List<Map<String,Object>> returnItemList = saleShelfService.getItemByPoNoAndOrderId(saleOrderId , tempPoList);
			resultMap.put("poNoList",poList);
			resultMap.put("returnItemList",returnItemList);
			resultMap.put("saleOrderId",saleOrderId);
			resultMap.put("saleOrderCode",saleOrderCode);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}

	@ApiOperation(value = "判断是否可生成预申报单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的单据id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/checkCreateDeclare.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Boolean> checkCreateDeclare(String token,String ids) {
		boolean flag = false;
		try{
			// 响应结果集
			flag = saleReturnOrderService.checkCreateDeclare(ids);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
	}
	@ApiOperation(value = "购销退货 选择商品弹窗")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "saleOrderCode", value = "关联销售单号", required = true),
		@ApiImplicitParam(name = "unNeedGoodsIds", value = "已选择的商品, 例如：poNo_goodsId"),
		@ApiImplicitParam(name = "goodsName", value = "商品名称"),
		@ApiImplicitParam(name = "goodsNo", value = "商品货号"),
		@ApiImplicitParam(name = "barcode", value = "商品条形码"),
		@ApiImplicitParam(name = "poNo", value = "po号"),
		@ApiImplicitParam(name = "inDepotId", value = "inDepotId")
	})
	@PostMapping(value="/getSalePopupList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleReturnOrderItemDTO>> getSalePopupList(String saleOrderCode , String unNeedGoodsIds, String goodsName,String goodsNo,String barcode,String poNo,Long inDepotId) {
		try {
			List<SaleReturnOrderItemDTO> list = saleReturnOrderService.getSalePopupListByPage(saleOrderCode, unNeedGoodsIds, goodsName, goodsNo, barcode,poNo, inDepotId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
