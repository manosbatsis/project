package com.topideal.report.webapi.reporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.report.service.reporting.MerchantInfoService;
import com.topideal.report.service.reporting.SettlementPriceWarnconfigService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SettlementPriceWarnconfigForm;
import com.topideal.report.webapi.form.SettlementPriceWarnconfigSearchForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 单价预警配置列表
 */
@RestController
@RequestMapping("/webapi/report/settlementPriceWarnconfig")
@Api(tags = "单价预警配置列表")
public class APISettlementPriceWarnconfigConroller {
    @Autowired
    private SettlementPriceWarnconfigService settlementPriceWarnconfigService;// 标准成本单价预警配置信息service
    @Autowired
    private MerchantInfoService merchantInfoService ;

    /**
     * 访问列表页面
     */
    @ApiOperation(value = "访问列表页面")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
    @PostMapping(value = "/toPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toPage(String token) {
        Map map=new HashMap();
        try {
            User user= ShiroUtils.getUserByToken(token);
            MerchantInfoModel merchant = new MerchantInfoModel() ;
            List<SelectBean> merchantList = merchantInfoService.getSelectBean(merchant);
            map.put("merchantList",merchantList);
            if("1".equals(user.getUserType())) {	// 超级管理员
                map.put("userType", 1);
            }else{
                map.put("userType", 2);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 访问详情页面
     */
    @ApiOperation(value = "访问详情页面")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)})
    @GetMapping("/toDetailsPage.asyn")
    public ResponseBean toDetailsPage(String token, Long id) throws Exception {
        Map map=new HashMap();
        User user=ShiroUtils.getUserByToken(token);

        MerchantInfoModel merchant = new MerchantInfoModel() ;
        List<SelectBean> merchantList = merchantInfoService.getSelectBean(merchant);
        map.put("merchantList", merchantList);
        // 查询邮件发送配置详情
        SettlementPriceWarnconfigDTO detail = settlementPriceWarnconfigService.searchDetail(id);
        map.put("detail", detail);
        if("1".equals(user.getUserType())) {	// 超级管理员
            map.put("userType", 1);
        }else{
            map.put("userType", 2);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);//成功
    }

    /**
     * 获取分页数据
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping("/listSettlementPriceWarnconfig.asyn")
    private ResponseBean listSettlementPriceWarnconfig(SettlementPriceWarnconfigSearchForm form) {
        SettlementPriceWarnconfigDTO dto=new SettlementPriceWarnconfigDTO();
        try {
            BeanUtils.copyProperties(form,dto);
            // 响应结果集
            User user = ShiroUtils.getUserByToken(form.getToken());
            if(!"1".equals(user.getUserType())) {
                dto.setMerchantId(user.getMerchantId());
            }
            dto = settlementPriceWarnconfigService.listEmail(user,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
    }


    /**
     * 新增
     */
    @ApiOperation(value = "新增")
    @PostMapping("/saveSettlementPriceWarnconfig.asyn")
    public ResponseBean saveSettlementPriceWarnconfig(SettlementPriceWarnconfigForm form) {
        Map<String, Object> saveEmail=new HashMap<>();
        SettlementPriceWarnconfigModel model=new SettlementPriceWarnconfigModel();
        BeanUtils.copyProperties(form,model);
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            saveEmail = settlementPriceWarnconfigService.saveEmail(model,user);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, saveEmail);//成功
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delSettlementPriceWarnconfig.asyn")
    public ResponseBean delSettlementPriceWarnconfig(String ids) {
        try {
            // 校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b=settlementPriceWarnconfigService.delEmail(list);
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/modifySettlementPriceWarnconfig.asyn")
    public ResponseBean modifySettlementPriceWarnconfig(SettlementPriceWarnconfigForm form) {
        Map<String, Object> modifyEmail=new HashMap<>();
        SettlementPriceWarnconfigModel model=new SettlementPriceWarnconfigModel();
        BeanUtils.copyProperties(form,model);
        try {
            // 校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if (!isRight) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(form.getToken());
            modifyEmail = settlementPriceWarnconfigService.modifyEmail(model,user);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    /**
     * 根据id禁用和启用
     * 状态(1启用,0禁用)
     * @return
     */
    @ApiOperation(value = "根据id禁用和启用")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "id", value = "id", required = true),
        @ApiImplicitParam(name = "status", value = "状态（0-禁用 1-启用）", required = true)
    })
    @PostMapping("/auditSettlementPriceWarnconfig.asyn")
    public ResponseBean auditSettlementPriceWarnconfig(String token , Long id , String status) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            boolean b = settlementPriceWarnconfigService.audit(id,status,user);
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }



}
