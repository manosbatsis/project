package com.topideal.order.webapi.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.order.service.common.SdSaleConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.SdSaleConfigForm;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/order/sdSaleConfig")
@Api(tags = "销售SD配置")
public class APISdSaleConfigController {

	@Autowired
	private SdSaleConfigService sdSaleConfigService;
	
	@ApiOperation(value = "获取销售SD配置分页列表")
    @PostMapping(value = "/listDTOByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SdSaleConfigDTO> listDTOByPage(SdSaleConfigForm form) {
		SdSaleConfigDTO dto = new SdSaleConfigDTO();
        try {        	
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            if(form.getMerchantId() == null) {
            	dto.setMerchantId(user.getMerchantId());            	
            }
            // 响应结果集
            dto = sdSaleConfigService.listDTOByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "编辑/复制 获取选中信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售SD配置id", required = true)
	})
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SdSaleConfigDTO> getDetail(String token,Long id) {
		SdSaleConfigDTO dto = new SdSaleConfigDTO();
        try {
        	 boolean isNull = new EmptyCheckUtils().addObject(id).empty();
             if (isNull) {
                 //输入信息不完整
                 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
             }
            // 响应结果集
            dto = sdSaleConfigService.searchDetail(id);
            SdSaleConfigItemModel itemModel = new SdSaleConfigItemModel();
            itemModel.setSaleConfigId(id);
            //单比例
            itemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
            List<SdSaleConfigItemModel> singleList = sdSaleConfigService.searchItemDetail(itemModel);
            dto.setSingleList(singleList);
            //多比例
            itemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
            List<SdSaleConfigItemModel> multipleList = sdSaleConfigService.searchItemDetail(itemModel);
            dto.setMultipleList(multipleList);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "销售SD配置保存/审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "operate", value = "操作 1-保存，2-审核", required = true)
	})
    @PostMapping(value = "/saveSdSaleConfig.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean saveSdSaleConfig(@RequestBody SdSaleConfigForm form) {
        try {
        	SdSaleConfigDTO dto = new SdSaleConfigDTO();
        	boolean isNull = new EmptyCheckUtils().addObject(form.getMerchantId()).addObject(form.getBuId())
					.addObject(form.getCustomerId()).addObject(form.getEffectiveDateStr())
					.addObject(form.getExpirationDateStr()).addObject(form.getOperate()).empty();
        	if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
        	BeanUtils.copyProperties(form,dto);
        	if(StringUtils.isNotBlank(form.getEffectiveDateStr())) {
        		dto.setEffectiveDate(TimeUtils.parseFullTime(form.getEffectiveDateStr()));
        	}
        	if(StringUtils.isNotBlank(form.getExpirationDateStr())) {
        		dto.setExpirationDate(TimeUtils.parseFullTime(form.getExpirationDateStr()));
        	}
        	
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            sdSaleConfigService.saveOrAudit(dto, user, form.getOperate());
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "销售SD配置id集合，多个用逗号隔开", required = true)
	})
    @PostMapping(value = "/delSdSaleConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delSdSaleConfig(String token,String ids) {
		SdSaleConfigDTO dto = new SdSaleConfigDTO();
        try {
        	 boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
             if (isNull) {
                 //输入信息不完整
                 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
             }
            // 响应结果集
            sdSaleConfigService.delSdSaleConfig(ids);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "多比例商品导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/importGoods.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importGoods(String token,
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
            resultMap = sdSaleConfigService.importGoods(data);

            Integer success = (Integer)resultMap.get("success");
            Integer failure = (Integer)resultMap.get("failure");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
            List<Map<String, String>> dataList = (List<Map<String, String>>) resultMap.get("configItemList");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            uploadResponse.setData(dataList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    @ApiOperation(value = "反审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "销售SD配置id", required = true)
    })
    @PostMapping(value = "/reverseAudit.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean reverseAudit(String token,Long id) {
        SdSaleConfigDTO dto = new SdSaleConfigDTO();
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            sdSaleConfigService.reverseAudit(id, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

}
