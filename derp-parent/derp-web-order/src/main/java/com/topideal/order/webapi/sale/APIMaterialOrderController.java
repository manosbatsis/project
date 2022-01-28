package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.dto.sale.MaterialOrderItemDTO;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.order.service.sale.MaterialOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.sale.form.MaterialOrderForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 领料单
 * @date 2021-03-31
 */
@RestController
@RequestMapping("/webapi/order/materialOrder")
@Api(tags = "领料单管理")
public class APIMaterialOrderController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(APIMaterialOrderController.class);
	@Autowired
	private MaterialOrderService materialOrderService;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao;
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询领料单列表分页数据")
   	@PostMapping(value="/listOrderByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MaterialOrderDTO> listOrderByPage(MaterialOrderForm form, HttpSession session) {
		MaterialOrderDTO dto = new MaterialOrderDTO();
		try{
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());

            dto = materialOrderService.listDTOByPage(dto, user);

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 复制、编辑
	 * */
	@ApiOperation(value = "复制、新增、编辑、详情获取单据信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/getDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MaterialOrderDTO> getDetail(String token,Long id) {
		MaterialOrderDTO dto = new MaterialOrderDTO();
		try{
			if(id == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            dto = materialOrderService.searchDetail(id);
            List<MaterialOrderItemDTO> itemList = materialOrderService.getItemDetail(dto.getId());
            dto.setItemList(itemList);

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 保存
	 * */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "领料单信息json串", required = true)
	})
   	@PostMapping(value="/saveMaterialOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> saveMaterialOrder(String json, String token) {
		String msg = "";
		try{
            if(json == null || StringUtils.isBlank(json)){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            msg = materialOrderService.saveOrModifyOrder(json,user);

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 保存并审核
	 * */
	@ApiOperation(value = "保存并审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "领料单信息json串", required = true)
	})
   	@PostMapping(value="/auditMaterialOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> auditMaterialOrder(String json, String token) {
		String msg = "";
		try{
            if(json == null || StringUtils.isBlank(json)){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            msg = materialOrderService.auditMaterialOrder(json,user);

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 获取导出数量
	 * */
	@ApiOperation(value = "获取导出数量")
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(MaterialOrderForm form, HttpSession session) {
		Integer total = null;
		try{
			MaterialOrderDTO dto = new MaterialOrderDTO();
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setIds(form.getIds());

            List<MaterialOrderDTO> result = materialOrderService.listMaterialOrder(dto,user);
            if(result != null) {
            	total = result.size();
            }

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}

	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
   	@GetMapping(value="/exportMaterialOrder.asyn")
	private ResponseBean<MaterialOrderDTO> exportMaterialOrder(MaterialOrderForm form, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			MaterialOrderDTO dto = new MaterialOrderDTO();
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setIds(form.getIds());
            //表头
            List<MaterialOrderDTO> result = materialOrderService.listMaterialOrder(dto,user);
            // 表体
            List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
            for(MaterialOrderDTO materialOrderDTO : result) {
            	List<MaterialOrderItemDTO> items = materialOrderService.getItemDetail(materialOrderDTO.getId());
            	if(items != null && items.size() > 0) {
            		for(MaterialOrderItemDTO itemDto:items) {
            			Map<String,Object> params = new HashMap<String, Object>();
        				params.put("commbarcode", itemDto.getCommbarcode());
        				CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(params);
        				String commBrandParentName = "";
        				if(commbarcode != null) {
        					commBrandParentName = commbarcode.getCommBrandParentName();//获取标准品牌名称
        				}
        				Map<String,Object> itemMap = new HashMap<String, Object>();
        				itemMap.put("orderCode", materialOrderDTO.getCode());
        				itemMap.put("poNo", materialOrderDTO.getPoNo());
        				itemMap.put("brandParentName", commBrandParentName);
        				itemMap.put("goodsNo", itemDto.getGoodsNo());
        				itemMap.put("goodsName", itemDto.getGoodsName());
        				itemMap.put("barcode", itemDto.getBarcode());
        				itemMap.put("num", itemDto.getNum());

        				itemList.add(itemMap);
            		}
            	}
            }

            String[] MATERIAL_COLUMNS = {"领料单编号","订单状态","出仓仓库名称","事业部","PO单号","客户","领取总数量","创建人","创建时间","审核人","审核时间",
            		"合同号","运输方式","车次","标准箱TEU","托数","承运商","提单毛重","出库地点","收货地址"};
            String[] MATERIAL_KEYS = {"code","stateLabel","outDepotName","buName","poNo","customerName","totalNum","createName","createDate","auditName"
            		,"auditDate","contractNo","transportLabel","trainno","teu","torusNumber","carrier","billWeight","outdepotAddr","receiverAddress"};
            List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
			String mainSheetName = "领料单信息";
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MATERIAL_COLUMNS, MATERIAL_KEYS, result);
			sheetList.add(mainSheet) ;

			String[] ITEM_COLUMNS ={"领料单编号","PO号","标准品牌","商品货号","商品名称","商品条形码","领取数量"};
			String[] ITEM_KEYS = {"orderCode","poNo","brandParentName","goodsNo","goodsName","barcode","num"};
			String itemSheetName = "商品信息";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, "领料单信息导出");
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
   	@PostMapping(value="/delMaterialOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean delMaterialOrder(String ids,String token) {
		try{
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
            	//输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = materialOrderService.delMaterialOrder(list);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"删除失败");
            }
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 导出清关资料
	 * @param session
	 * @param response
	 * @param request
	 * @param id
	 * @throws Exception
	 */
	@ApiOperation(value = "导出清关资料",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "json串，包括单据id，模板ids", required = true)
	})
	@PostMapping(value="/exportCustomsInfo.asyn")
	public void exportCustomsDeclare(String token,HttpServletResponse response, String json) throws Exception {
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Long id = Long.valueOf(jsonData.getString("id"));
			String inFileTempIds = (String) jsonData.get("inFileTempIds");
			String outFileTempIds = (String) jsonData.get("outFileTempIds");

			if (id == null) {
				throw new RuntimeException("id不能为空！");
			}
			Map<String, Workbook> resultMap = new HashMap<>();
			if (!StringUtils.isEmpty(inFileTempIds)) {
				List<Long> inFileTempIdList = new ArrayList<>();
				List<String> list = Arrays.asList(inFileTempIds.split(","));
				for (String idStr : list) {
					inFileTempIdList.add(Long.valueOf(idStr));
				}
				Map<String, Object> resultInMap = materialOrderService.exportDepotCustomsDeclares(id, inFileTempIdList, "2");
				Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
				resultMap.putAll(multipartInCustomsDeclares);
			}

			if (!StringUtils.isEmpty(outFileTempIds)) {
				List<Long> outFileTempIdList = new ArrayList<>();
				List<String> list = Arrays.asList(outFileTempIds.split(","));
				for (String idStr : list) {
					outFileTempIdList.add(Long.valueOf(idStr));
				}
				Map<String, Object> resultOutMap = materialOrderService.exportDepotCustomsDeclares(id, outFileTempIdList, "1");
				Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
				resultMap.putAll(multipartOutCustomsDeclares);
			}

			//导出压缩文件
			String downloadFilename = URLEncoder.encode("领料单一线清关资料.zip", "UTF-8");
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			// 设置在下载框默认显示的文件名
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

			for (String key : resultMap.keySet()) {
				Workbook wb = resultMap.get(key);
				zos.putNextEntry(new ZipEntry(key));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				wb.write(bos);
				bos.writeTo(zos);
				zos.closeEntry();
			}
			zos.flush();
			zos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@ApiOperation(value = "导入领料单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importMaterialOrder.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse>  importMaterialOrder(String token,
									@ApiParam(value = "上传的文件", required = true)
									@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			resultMap = materialOrderService.importMaterialOrder(data,user);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
}
