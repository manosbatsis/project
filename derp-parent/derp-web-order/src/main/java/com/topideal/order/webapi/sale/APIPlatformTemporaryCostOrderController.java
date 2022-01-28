package com.topideal.order.webapi.sale;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.PlatformTemporaryCostOrderDao;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.sale.PlatformTemporaryCostOrderService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.PlatformTemporaryCostOrderForm;
import com.topideal.order.webapi.sale.form.PlatformTemporaryCostOrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台暂估费用单
 */
@RestController
@RequestMapping("/webapi/order/platformTemporaryCostOrder")
@Api(tags = "平台暂估费用单")
public class APIPlatformTemporaryCostOrderController {
    @Autowired
    private PlatformTemporaryCostOrderService platformTemporaryCostOrderService;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private PlatformTemporaryCostOrderDao platformTempCostOrderDao;

    /**
     * 获取分页数据
     * */
    @ApiOperation(value = "查询平台暂估费用单")
    @PostMapping(value="/listPlatformTemporaryOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<PlatformTemporaryCostOrderDTO> listPlatformTemporaryOrder(PlatformTemporaryCostOrderForm form) {
        PlatformTemporaryCostOrderDTO dto = new PlatformTemporaryCostOrderDTO();
        try{
            BeanUtils.copyProperties(form, dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());

            // 响应结果集
            dto = platformTemporaryCostOrderService.listPlatformTemporaryCost(user,dto);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }


    /**
     * 查看详情
     * @param token
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value="/toDetailsById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PlatformTemporaryCostOrderDTO> toDetailsById(String token, Long id){
        try {
            User user=ShiroUtils.getUserByToken(token);
            PlatformTemporaryCostOrderDTO dto = platformTemporaryCostOrderService.searchDTOById(user,id) ;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 校验发货日期是否已关帐
     * @param token
     * @param id
     * @return
     */
    @ApiOperation(value = "校验发货日期是否已关帐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value="/checkDate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean checkDate(String token, Long id){
        try {
            User user=ShiroUtils.getUserByToken(token);
            Boolean flag = platformTemporaryCostOrderService.checkDate(user,id) ;
            if(flag){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000.getCode(),"校验成功");
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"单据所在月份已经关账,不能进行修或删除操作");
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 根据id删除
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "ids", required = true)
    })
    @PostMapping(value="/delete.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delete(String token, String ids){
        try {
            User user=ShiroUtils.getUserByToken(token);
            Map map = platformTemporaryCostOrderService.deleteById(user,ids);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 修改
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping(value="/updatePlatformCost.asyn")
    public ResponseBean updatePlatformCost(@RequestBody PlatformTemporaryCostOrderItemForm form){
        try {
            User user=ShiroUtils.getUserByToken(form.getToken());
            PlatformTemporaryCostOrderDTO dto=new PlatformTemporaryCostOrderDTO();
            BeanUtils.copyProperties(form,dto);

            Map map = platformTemporaryCostOrderService.updatePlatformTemporary(user,dto);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

    /**
     * 导出 (异步导出)
     * */
    @ApiOperation(value = "导出")
    @GetMapping("/exportPlatFormTemporary.asyn")
    private ResponseBean exportPlatFormTemporary(PlatformTemporaryCostOrderForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("code", "00");
        retMap.put("message", "成功");
        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            if(StringUtils.isBlank(form.getStorePlatformCode())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "平台编码不能为空");
            }

            PlatformTemporaryCostOrderDTO dto = new PlatformTemporaryCostOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setExternalCode(form.getExternalCode());
            dto.setStorePlatformCode(form.getStorePlatformCode());
            dto.setIds(form.getIds());
            dto.setOrderCode(form.getOrderCode());
            dto.setCode(form.getCode());
            dto.setDeliverStartDate(form.getDeliverStartDate());
            dto.setDeliverEndDate(form.getDeliverEndDate());
            int exportItemNum = platformTempCostOrderDao.getPlatFormTemporaryCount(dto);
            if(exportItemNum <= 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCPLATFORMTEMPCOSTTOTAL);
            taskModel.setTaskName("ToC平台暂估费用单");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("1");
            taskModel.setRemark("ToC平台暂估费用单导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("externalCode", form.getExternalCode());
            paramJson.put("ids", form.getIds());
            paramJson.put("storePlatformCode", form.getStorePlatformCode());
            paramJson.put("orderCode", form.getOrderCode());
            paramJson.put("code", form.getCode());
            paramJson.put("deliverStartDate", form.getDeliverStartDate());
            paramJson.put("deliverEndDate", form.getDeliverEndDate());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

    /**
     * 生成暂估费用单
     *
     * @return
     */
    @ApiOperation(value = "生成暂估费用单")
    @PostMapping(value = "/savePlatCostOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "orderType", value = "单据类型 0-发货订单 1-售后退款单 2-两者", required = true),
            @ApiImplicitParam(name = "storePlatformCodes", value = "平台编码, 多个以英文逗号隔开"),
            @ApiImplicitParam(name = "month", value = "生成月份", required = true)})
    public ResponseBean savePlatCostOrder(String token, String month, String orderType, String storePlatformCodes) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            Map map = platformTemporaryCostOrderService.savePlatformCostOrder(user, month, orderType, storePlatformCodes);
            String message = (String) map.get("message");
            String code = (String) map.get("code");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);//失败
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }




}
