package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.sale.SaleShelfModel;
import com.topideal.order.service.sale.SaleOutDepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderGoodsInfoNumDTO;
import com.topideal.order.webapi.sale.dto.SaleOutDepotResponseDTO;
import com.topideal.order.webapi.sale.form.SaleOutDepotForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * webapi 销售出库
 *
 */
@RequestMapping("/webapi/order/saleOut")
@RestController
@Api(tags = " 销售出库")
public class APISaleOutDepotController {

	// 销售出库service
	@Autowired
	private SaleOutDepotService saleOutDepotService;

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询销售出库详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOutDepotResponseDTO> toDetailsPage(String token,Long id)throws Exception{
		SaleOutDepotResponseDTO responseDTO = new SaleOutDepotResponseDTO();
		try {
			SaleOutDepotDTO dto = saleOutDepotService.searchDetail(id);
			List<SaleOutDepotItemDTO> itemList = saleOutDepotService.listItemBySaleOutDepotId(id);
			dto.setItemList(itemList);
			// 查询是否有上架信息
			SaleShelfDTO saleShelfDTO=new SaleShelfDTO();
			saleShelfDTO.setSaleOutDepotId(id);
			 saleShelfDTO = saleOutDepotService.listSaleOutShelfByPage(saleShelfDTO);
			 if(saleShelfDTO.getList().size()>0){	// 如果有商品上架信息时
				 responseDTO.setIsNotShelf("yes");
			 }else{			// 如果没有商品上架信息时,隐藏整个商品上架明细模块
				 responseDTO.setIsNotShelf("no");
			 }
			responseDTO.setSaleOutDepotDTO(dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询销售出库列表信息")
	@PostMapping(value="/listSaleOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOutDepotDTO> listSaleSaleOutDepot(SaleOutDepotForm form, HttpSession session) {
		SaleOutDepotDTO dto = new SaleOutDepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setSaleType(form.getSaleType());
			dto.setPoNo(form.getPoNo());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setIsWriteOff(form.getIsWriteOff());
			dto.setSaleDeclareOrderCode(form.getSaleDeclareOrderCode());
			// 响应结果集
			dto = saleOutDepotService.listSaleOutDepotByPage(dto,user);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="importSaleOutDepot.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleOutDepot(String token,
											@ApiParam(value = "上传的文件", required = true)
											@RequestParam(value = "file", required = true) MultipartFile file) {
		try {
			Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleOutDepotService.importSaleOutDepot(user, data);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
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
	 * 审核
	 * */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditSaleOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<UploadResponse> auditSaleOutDepot(String token,String ids, HttpSession session) {
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
			bl = saleOutDepotService.auditSaleOutDepot(list,user.getId(),user.getName(), user.getMerchantId(), user.getMerchantName(),user.getTopidealCode());
			Integer success = (Integer)bl.get("success");
			Integer failure = (Integer)bl.get("failure");
			List<ImportMessage> errorList = new ArrayList<ImportMessage>();
			ImportMessage message = new ImportMessage();
			StringBuffer msg =(StringBuffer)bl.get("failureMsg");
			message.setMessage(msg.toString());
			errorList.add(message);
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
	 * 获取导出销售出库清单的数量
	 */
	@ApiOperation(value = "获取导出销售出库清单的数量")
	@ApiResponses({
		@ApiResponse(code=10000,message="data => 返回需要导出的销售出库清单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotForm form) throws Exception{
		Integer total = 0;
		try{
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setSaleType(form.getSaleType());
			dto.setPoNo(form.getPoNo());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setIds(form.getIds());
			dto.setIsWriteOff(form.getIsWriteOff());
			dto.setSaleDeclareOrderCode(form.getSaleDeclareOrderCode());
			// 响应结果集
			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepot(dto,user);
			total = result.size();
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售出库单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleOutDepot.asyn")
	private ResponseBean exportSaleOutDepot(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotForm form) throws Exception{
		try {
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setSaleType(form.getSaleType());
			dto.setPoNo(form.getPoNo());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setIds(form.getIds());
			dto.setIsWriteOff(form.getIsWriteOff());
			dto.setSaleDeclareOrderCode(form.getSaleDeclareOrderCode());

			// 响应结果集
			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepot(dto,user);
			List<SaleOutDepotItemDTO> itemList = new ArrayList<SaleOutDepotItemDTO>();
			for(SaleOutDepotDTO sModel:result){
				List<SaleOutDepotItemDTO> itemList1 = saleOutDepotService.listItemBySaleOutDepotId(sModel.getId());
				for(SaleOutDepotItemDTO item:itemList1){
					item.setSaleOutDepotCode(sModel.getCode());
				}
				if(itemList1 != null && itemList1.size()>0){
					itemList.addAll(itemList1);
				}
			}
			String sheetName = "销售出库清单";
	        String[] columns={"出库清单编号","单据状态","商家名称","客户名称","出库仓库","事业部","销售类型","销售订单编号","唯品账单号","LBX单号","物流企业名称","发货时间","提单号","运单号","签收日期","上架日期","备注"};
	        String[] keys = {"code", "statusLabel", "merchantName", "customerName", "outDepotName", "buName", "saleTypeLabel", "saleOrderCode", "vipsBillNo", "lbxNo", "logisticsName", "deliverDate", "blNo", "wayBillNo", "receiveDate", "shelfDate", "remark"} ;

	        String[] columns1={"出库清单编号","商品编号","商品货号","商品名称","商品条形码","数量","批次号","生产日期","失效日期","海外仓理货单位"};
	        String[] keys1 = {"saleOutDepotCode", "goodsCode", "goodsNo", "goodsName", "barcode", "transferNum", "transferBatchNo", "productionDate", "overdueDate", "tallyingUnitLabel"} ;
	        //生成表格
	        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", columns, keys, result) ;
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", columns1, keys1, itemList) ;

			List<ExportExcelSheet> sheets = new ArrayList<>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 获取上架明细分页数据
	 * */
	@ApiOperation(value = "查询详情页面上架明细列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "orderId", value = "销售出库单id", required = true),
		@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
		@ApiImplicitParam(name = "pageSize", value = "每一页记录数", required = true)
	})
	@PostMapping(value="/listSaleOutShelf.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleShelfModel> listSaleOutShelf(String token,String orderId,int begin,int pageSize) {
		SaleShelfDTO dto = new SaleShelfDTO();
		try{
			if(StringUtils.isBlank(orderId)) {
				WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			dto.setSaleOutDepotId(Long.valueOf(orderId));
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			// 响应结果集
			dto = saleOutDepotService.listSaleOutShelfByPage(dto);

		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 删除手工导入订单
	 * @param ids
	 * @return
	 */
//	@ApiOperation(value = "删除手工导入订单")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "token", value = "令牌", required = true),
//		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
//	})
//	@PostMapping(value="/delSaleOut.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	public ResponseBean delSaleOut(String token,String ids) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
//        }
//        if(StringUtils.isBlank(ids)){
//            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
//        }
//        List<Long> list = StrUtils.parseIds(ids);
//        try {
//			int rows = saleOutDepotService.delImportOrder(list);
//			if(rows > 0) {
//				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
//			}else {
//				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");//未知异常
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
//		}
//	}
	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * */
	@ApiOperation(value = "获取选中订单的所有商品和对应数量（相同商品数量叠加）")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="data => code: 由 商品id - 出库仓id - 出库仓编码 - 商品货号 - 理货单位 拼接而成")
	})
	@PostMapping(value="/getOrderGoodsInfo.asyn")
	private ResponseBean<List<OrderGoodsInfoNumDTO>> getOrderGoodsInfo(String token ,String ids) {
		Map<String,Integer> map =new HashMap<String,Integer>();
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
			// 响应结果集
            map = saleOutDepotService.getOrderGoodsInfo(list);
            List<OrderGoodsInfoNumDTO> goodsInfoNumList = new ArrayList<OrderGoodsInfoNumDTO>();
			for(Entry<String, Integer> entry : map.entrySet()) {
            	OrderGoodsInfoNumDTO numDTO = new OrderGoodsInfoNumDTO();
            	numDTO.setCode(entry.getKey());
            	numDTO.setNum(entry.getValue());
            	goodsInfoNumList.add(numDTO);
            }
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,goodsInfoNumList);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	@ApiOperation(value = "获取销售出库集合（不分页）")
	@PostMapping(value="/getSaleOutDepotList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<SaleOutDepotDTO>> getSaleOutDepotList(SaleOutDepotForm form) throws Exception{
		List<SaleOutDepotDTO> list = new ArrayList<SaleOutDepotDTO>();
		try{
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setSaleType(form.getSaleType());
			dto.setPoNo(form.getPoNo());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setSaleOrderId(form.getSaleOrderId());
			dto.setSaleDeclareOrderId(form.getSaleDeclareOrderId());

			// 响应结果集
			list = saleOutDepotService.listSaleOutDepot(dto,user);
			for(SaleOutDepotDTO outDTO : list) {
				Integer transferNum = 0;
				Integer wornNum = 0;
				List<SaleOutDepotItemDTO> itemList = saleOutDepotService.listItemBySaleOutDepotId(outDTO.getId());
				for(SaleOutDepotItemDTO itemDTO : itemList) {
					transferNum = transferNum + (itemDTO.getTransferNum() == null ? 0:itemDTO.getTransferNum());
					wornNum = wornNum + (itemDTO.getWornNum() == null ? 0:itemDTO.getWornNum());
				}
				outDTO.setTransferNum(transferNum);
				outDTO.setWornNum(wornNum);
			}


		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
	}
	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/delSaleOutOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleOutOrder(String token, Long id){
		try {
			User user= ShiroUtils.getUserByToken(token);
			saleOutDepotService.delSaleOutOrder(id, user, token);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

	}
}
