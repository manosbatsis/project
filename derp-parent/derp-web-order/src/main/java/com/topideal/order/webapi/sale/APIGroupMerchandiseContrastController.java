package com.topideal.order.webapi.sale;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailModel;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.sale.GroupMerchandiseContrastService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.GroupMerchandiseContrastForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * webapi 组合商品对照表
 * @date 2021-02-07
 */
@RestController
@RequestMapping("/webapi/order/groupMerchandiseContrast")
@Api(tags = "组合商品对照表")
public class APIGroupMerchandiseContrastController {

    /** 标题  */
    private static final String[] COLUMNS= {"店铺编码","店铺名称","状态","商家","商品ID","商品名称","单品条形码","单品商品货号","单品商品名称","数量","创建时间","修改时间"};

    private static final String[] KEYS = {"shopCode", "shopName", "statusLabel", "merchantName", "skuId", "groupGoodsName", "barcode", "goodsNo", "goodsName", "num", "createDate", "modifyDate"} ;
    @Autowired
    private GroupMerchandiseContrastService groupMerchandiseContrastService;

    /**
     * 访问详情页面
     */
    @ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "事业部移库单id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<GroupMerchandiseContrastDTO> toDetailsPage(String token,Long id) throws Exception {
    	GroupMerchandiseContrastDTO dto = new GroupMerchandiseContrastDTO();
    	try {
        	dto = groupMerchandiseContrastService.getDTODetail(id);        	
        }catch(Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    /**
     * 访问编辑页面
     */
    @ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "组合商品对照表id", required = true)
	})
	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<GroupMerchandiseContrastDTO> toEditPage(String token, Long id) throws Exception {
    	GroupMerchandiseContrastDTO dto = new GroupMerchandiseContrastDTO();
    	try {
    		dto = groupMerchandiseContrastService.getDTODetail(id);        	
        }catch(Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "skuId", value = "组合品ID", required = true),
		@ApiImplicitParam(name = "shopId", value = "店铺ID", required = true),
		@ApiImplicitParam(name = "shopName", value = "店铺名称", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家ID", required = true),
		@ApiImplicitParam(name = "groupGoodsName", value = "组合品名称", required = true),
		@ApiImplicitParam(name = "status", value = "状态：0-已停用 1-已启用", required = true),
		@ApiImplicitParam(name = "goodsItem", value = "组合商品对照表体信息，为json串", required = true)
	})
	@PostMapping(value="/saveGroupMerchandiseContrast.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveGroupMerchandiseContrast(GroupMerchandiseContrastForm form, String goodsItem) {
        try {
        	GroupMerchandiseContrastModel model = new GroupMerchandiseContrastModel();
            User user = ShiroUtils.getUserByToken(form.getToken());
            boolean isNull = new EmptyCheckUtils().addObject(form.getShopId()).addObject(form.getShopName())
                    .addObject(form.getMerchantId()).addObject(form.getSkuId()).addObject(form.getGroupGoodsName())
                    .empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            
            JSONArray jsonArray = JSONArray.fromObject(goodsItem);
            List<GroupMerchandiseContrastDetailModel> detailModelList = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                if (!(json.containsKey("goodsNo") || json.containsKey("goodsId") || json.containsKey("goodsName")
                        || json.containsKey("barcode")|| json.containsKey("num") || json.containsKey("price"))) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
                }

                String goodsNo = json.getString("goodsNo");
                Long goodsId = json.getLong("goodsId");
                String goodsName = json.getString("goodsName");
                String barcode = json.getString("barcode");
                String brandName = json.containsKey("brandName") ? json.getString("brandName") : null;
                Integer num = json.getInt("num");
                double price = json.getDouble("price");
                GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                detailModel.setGoodsId(goodsId);
                detailModel.setGoodsName(goodsName);
                detailModel.setGoodsNo(goodsNo);
                detailModel.setBarcode(barcode);
                detailModel.setBrand(brandName);
                detailModel.setNum(num);
                detailModel.setPrice(new BigDecimal(price));
                detailModelList.add(detailModel);
            }
            model.setMerchantId(Long.valueOf(form.getMerchantId()));
            model.setShopId(Long.valueOf(form.getShopId()));
            model.setShopName(form.getShopName());
            model.setSkuId(form.getSkuId());
            model.setGroupGoodsName(form.getGroupGoodsName());
            model.setRemark(form.getRemark());
            model.setStatus(form.getStatus());            
            model.setDetailList(detailModelList);
            groupMerchandiseContrastService.saveGroupMerchandiseContrast(model, user);
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    /**
     * 获取分页数据
     */
    @ApiOperation(value = "查询商品对照表列表信息")   	
   	@PostMapping(value="/listGroupMerchandiseContrast.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<GroupMerchandiseContrastDTO> listGroupMerchandiseContrast(GroupMerchandiseContrastForm form) {
    	GroupMerchandiseContrastDTO dto = new GroupMerchandiseContrastDTO();
    	try {
    		if(StringUtils.isNotBlank(form.getMerchantId())) {
    			dto.setMerchantId(Long.valueOf(form.getMerchantId()));            	
            }
            if(StringUtils.isNotBlank(form.getShopId())) {
            	dto.setShopId(Long.valueOf(form.getShopId()));                  	
            }
            dto.setSkuId(form.getSkuId());
            dto.setGroupGoodsName(form.getGroupGoodsName());
            dto.setStatus(form.getStatus());  
            dto.setUpdateStartDate(form.getUpdateStartDate());
            dto.setUpdateEndDate(form.getUpdateEndDate());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            
            // 响应结果集
            dto = groupMerchandiseContrastService.listGroupMerchandiseContrast(dto);
           
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    /**
     * 判断同一店铺编码、同一商家、同一组合品ID是否存在
     */
    @ApiOperation(value = "判断同一店铺编码、同一商家、同一组合品ID是否存在")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "skuId", value = "组合品ID", required = true),
		@ApiImplicitParam(name = "shopId", value = "店铺ID", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家ID", required = true)
	})
    @PostMapping(value="/getGroupMerchantContrast.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getGroupMerchantContrast(GroupMerchandiseContrastForm form) {
        try {
        	GroupMerchandiseContrastModel model = new GroupMerchandiseContrastModel();
            if(StringUtils.isNotBlank(form.getMerchantId())) {
            	model.setMerchantId(Long.valueOf(form.getMerchantId()));            	
            }
            if(StringUtils.isNotBlank(form.getShopId())) {
            	model.setShopId(Long.valueOf(form.getShopId()));                  	
            }
            model.setSkuId(form.getSkuId());
            model = groupMerchandiseContrastService.getGroupMerchandiseContrast(model);
            if (model != null) {
    			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"存在重复的公司店铺组合品ID信息!");
            }
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    /**
     * 保存
     */
    @ApiOperation(value = "编辑保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "单据id", required = true),
		@ApiImplicitParam(name = "skuId", value = "组合品ID", required = true),
		@ApiImplicitParam(name = "shopId", value = "店铺ID", required = true),
		@ApiImplicitParam(name = "shopName", value = "店铺名称", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家ID", required = true),
		@ApiImplicitParam(name = "groupGoodsName", value = "组合品名称", required = true),
		@ApiImplicitParam(name = "status", value = "状态：0-已停用 1-已启用", required = true),
		@ApiImplicitParam(name = "goodsItem", value = "组合商品对照表体信息，为json串", required = true)
	})
	@PostMapping(value="/modifyGroupMerchandiseContrast.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean modifyGroupMerchandiseContrast(GroupMerchandiseContrastForm form, String goodsItem) {
        try {
        	GroupMerchandiseContrastModel model = new GroupMerchandiseContrastModel();
            User user = ShiroUtils.getUserByToken(form.getToken());
            boolean isNull = new EmptyCheckUtils().addObject(form.getId()).addObject(form.getShopId()).addObject(form.getShopName())
                    .addObject(form.getMerchantId()).addObject(form.getSkuId()).addObject(form.getGroupGoodsName())
                    .empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            JSONArray jsonArray = JSONArray.fromObject(goodsItem);
            List<GroupMerchandiseContrastDetailModel> detailModelList = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                if (!(json.containsKey("goodsNo") || json.containsKey("goodsId") || json.containsKey("goodsName")
                        || json.containsKey("barcode")|| json.containsKey("num") || json.containsKey("price"))) {
                	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
                }

                String goodsNo = json.getString("goodsNo");
                Long goodsId = json.getLong("goodsId");
                String goodsName = json.getString("goodsName");
                String barcode = json.getString("barcode");
                String brandName = json.containsKey("brandName") ? json.getString("brandName") : null;
                Integer num = json.getInt("num");
                double price = json.getDouble("price");
                GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                detailModel.setGoodsId(goodsId);
                detailModel.setGoodsName(goodsName);
                detailModel.setGoodsNo(goodsNo);
                detailModel.setBarcode(barcode);
                detailModel.setBrand(brandName);
                detailModel.setNum(num);
                detailModel.setPrice(new BigDecimal(price));
                detailModelList.add(detailModel);
            }
            
            model.setMerchantId(Long.valueOf(form.getMerchantId()));  
            model.setShopId(Long.valueOf(form.getShopId())); 
            model.setId(Long.valueOf(form.getId()));
            model.setSkuId(form.getSkuId());
            model.setGroupGoodsName(form.getGroupGoodsName());
            model.setRemark(form.getRemark());
            model.setStatus(form.getStatus());  
            model.setDetailList(detailModelList);
            groupMerchandiseContrastService.modifyGroupMerchandiseContrast(model, user);
        } catch (Exception e) {
        	e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    /**
     * 导出信息
     * @param response
     * @param request
     * @param
     * @throws Exception
     */
    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportInfo.asyn")
    public ResponseBean exportInfo(HttpServletResponse response, HttpServletRequest request, GroupMerchandiseContrastForm form) throws Exception{
    	GroupMerchandiseContrastDTO dto = new GroupMerchandiseContrastDTO();
    	try {
    		if(StringUtils.isNotBlank(form.getMerchantId())) {
    			dto.setMerchantId(Long.valueOf(form.getMerchantId()));            	
            }
            if(StringUtils.isNotBlank(form.getShopId())) {
            	dto.setShopId(Long.valueOf(form.getShopId()));                  	
            }
            dto.setSkuId(form.getSkuId());
            dto.setGroupGoodsName(form.getGroupGoodsName());
            dto.setStatus(form.getStatus());  
            dto.setUpdateStartDate(form.getUpdateStartDate());
            dto.setUpdateEndDate(form.getUpdateEndDate());
            dto.setIds(form.getIds());
            
	        String sheetName = "组合对照表导出";
	        List<GroupMerchandiseContrastDTO> list = groupMerchandiseContrastService.getexportList(dto);
	        //生成表格
	        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list) ;
	        //导出文件
	        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    	}catch(Exception e) {
    		e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
    	}
    }

    /**
     * 批量导入
     * @param
     * @return int
     * @throws IOException
     */
    @ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importExcel.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean importExcel(String token,
    									@ApiParam(value = "上传的文件", required = true)
    									@RequestParam(value = "file", required = true) MultipartFile file,HttpSession session) throws IOException {
        try {
        	Map resultMap = new HashMap();// 返回的结果集
            if (file == null) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),file.getOriginalFilename(), 2);
            if (data == null) {// 数据为空
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            resultMap = groupMerchandiseContrastService.batchImport(data);
            
            Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse); 
			
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    
    /**
     * 获取店铺
     * @return
     */
    @ApiOperation(value = "获取店铺")
   	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
   		@ApiImplicitParam(name = "shopName", value = "店铺名称", required = true)
   	})
   	@PostMapping(value="getShop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<List<MerchantShopRelMongo>> getShop(String token,String shopName) {
    	try {
			List<MerchantShopRelMongo> shopList = groupMerchandiseContrastService.getShopList(shopName) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,shopList); 
    	} catch (Exception e) {
    		e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
    }

    /**
     * 批量删除商品对照信息
     * @return
     */
    @ApiOperation(value = "批量删除商品对照信息")
   	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
   		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
   	})
   	@PostMapping(value="/deleteGroupMerchandise.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean deleteGroupMerchandise(String token,String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<Long> idList =  Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            groupMerchandiseContrastService.batchDel(idList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
}
