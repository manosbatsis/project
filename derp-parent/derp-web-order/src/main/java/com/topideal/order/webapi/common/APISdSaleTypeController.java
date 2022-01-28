package com.topideal.order.webapi.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.order.service.common.SdSaleTypeService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.SdSaleTypeForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/order/sdSaleType")
@Api(tags = "销售SD类型")
public class APISdSaleTypeController {

	@Autowired
	private SdSaleTypeService sdSaleTypeService;
	
	@ApiOperation(value = "获取销售SD类型分页列表")
    @PostMapping(value = "/listDTOByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SdSaleTypeDTO> listDTOByPage(SdSaleTypeForm form) {
		SdSaleTypeDTO dto = new SdSaleTypeDTO();
        try {
            BeanUtils.copyProperties(form,dto);           
            // 响应结果集
            dto = sdSaleTypeService.listDTOByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "编辑获取详情信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售SD类型id", required = true)
	})
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SdSaleTypeDTO> getDetail(String token,Long id) {
		SdSaleTypeDTO dto = new SdSaleTypeDTO();
        try {
        	 boolean isNull = new EmptyCheckUtils().addObject(id).empty();
             if (isNull) {
                 //输入信息不完整
                 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
             }
            // 响应结果集
            dto = sdSaleTypeService.searchById(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	@ApiOperation(value = "销售SD类型新增/编辑")
    @PostMapping(value = "/saveSdSaleType.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveSdSaleType(SdSaleTypeForm form) {
        try {
        	boolean isNull = new EmptyCheckUtils().addObject(form.getSdType()).addObject(form.getSdTypeName())
        					.addObject(form.getSdType()).addObject(form.getProjectId()).addObject(form.getPaymentSubjectId())
        					.addObject(form.getStatus()).addObject(form.getType()).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            
        	SdSaleTypeModel model = new SdSaleTypeModel();
            BeanUtils.copyProperties(form,model);
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            sdSaleTypeService.saveSdSaleType(model, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	/**
     * 获取下拉框
     * @param form
     * @return
     */
    @ApiOperation(value = "获取单比例、多比例下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "unNeedIds", value = "单比例已选择sd类型id,多个用逗号隔开", required = false),
            @ApiImplicitParam(name = "sdType", value = "sd类型，1-单比例，2-多比例", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=10000,message = "singleList => 单比例下拉列表，multipleList => 多比例下拉列表")
    })
    @PostMapping(value = "/getSdSaleTypeList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<Map<String,List<SdSaleTypeDTO>>> getSdSaleTypeList(String unNeedIds, String sdType ,String token) {
        Map<String,List<SdSaleTypeDTO>> result = new HashMap<String,List<SdSaleTypeDTO>>();
        try{
        	SdSaleTypeDTO dto = new SdSaleTypeDTO();
            dto.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);
        	//单比例
            if(StringUtils.isBlank(sdType) || DERP_ORDER.SDTYPECONFIG_TYPE_1.equals(sdType)){
                if(StringUtils.isNotBlank(unNeedIds)){
                    dto.setUnNeedIds(StrUtils.parseIds(unNeedIds));
                }
                dto.setType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
                List<SdSaleTypeDTO> singleList = sdSaleTypeService.getSdSaleTypeList(dto);
                result.put("singleList",singleList);
            }
            //多比例
            if(StringUtils.isBlank(sdType) || DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(sdType)){
                dto.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
                List<SdSaleTypeDTO> multipleList = sdSaleTypeService.getSdSaleTypeList(dto);
                result.put("multipleList",multipleList);
            }
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }
}
