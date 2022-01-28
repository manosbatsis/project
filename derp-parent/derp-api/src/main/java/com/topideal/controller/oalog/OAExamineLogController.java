package com.topideal.controller.oalog;


import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.tools.ResponseFactory;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;

@Controller
@RequestMapping("/derpapi/1302")
public class OAExamineLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAExamineLogController.class);

    @Autowired
    private RMQProducer rocketMQProducer;//MQ

    //OA审批过程日志接口
    @RequestMapping(params="method=erp8912",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    @SystemControllerLog(point= DERP_LOG_POINT.POINT_12108000000,model=DERP_LOG_POINT.POINT_12108000000_Label,keyword = "requestId")
    public JSONObject getOAExamineLog(@RequestBody String json){
        //json = new String(json.getBytes(), Charset.forName("UTF-8"));

        LOGGER.info("OA审批过程日志接收参数："+json);

        JSONObject jsonMQData=new JSONObject();
        //OA单据号
        String oaBillCode ="";
        //json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        try{

            //流程id
            if (jsonData.get("requestId")==null|| StringUtils.isBlank(jsonData.getString("requestId"))) {
                return ResponseFactory.OAError("requestId is null", oaBillCode);
            }
            //OA单据号
            if (jsonData.get("oaBillCode")==null|| StringUtils.isBlank(jsonData.getString("oaBillCode"))) {
                return ResponseFactory.OAError("oaBillCode is null", oaBillCode);
            }
            //OA单据号
            oaBillCode = (String)jsonData.get("oaBillCode");
            //处理人工号
           /* if (jsonData.get("psnCode")==null|| StringUtils.isBlank(jsonData.getString("psnCode"))) {
                return ResponseFactory.OAError("psnCode is null", oaBillCode);
            }*/

            //处理人名称
            if (jsonData.get("psnName")==null|| StringUtils.isBlank(jsonData.getString("psnName"))) {
                return ResponseFactory.OAError("psnName is null", oaBillCode);
            }
            //处理人岗位名称
            if(jsonData.get("jobTitleName")==null || StringUtils.isBlank(jsonData.getString("jobTitleName"))){
                return ResponseFactory.OAError("jobTitleName is null", oaBillCode);
            }
            //处理日期
            if (jsonData.get("operateDate")==null|| StringUtils.isBlank(jsonData.getString("operateDate"))) {
                return ResponseFactory.OAError("operateDate is null", oaBillCode);
            }
            //处理时间
            if (jsonData.get("operateTime")==null|| StringUtils.isBlank(jsonData.getString("operateTime"))) {
                return ResponseFactory.OAError("operateTime is null", oaBillCode);
            }
            //处理类型 0：批准；3：退回
            if (jsonData.get("logType")==null|| StringUtils.isBlank(jsonData.getString("logType"))) {
                return ResponseFactory.OAError("logType is null", oaBillCode);
            }
            if(!("0".equals(jsonData.get("logType")) || "3".equals(jsonData.get("logType")))) {
                return ResponseFactory.OASuccess("logType is "+jsonData.get("logType"), oaBillCode);
            }

            // 实例化推送MQ的jJSON 对象
            jsonMQData.put("requestId", (String)jsonData.get("requestId"));
            jsonMQData.put("psnName", (String)jsonData.get("psnName"));
            jsonMQData.put("jobTitleName",(String)jsonData.get("jobTitleName"));
            jsonMQData.put("logType", (String)jsonData.get("logType"));
            jsonMQData.put("operateDate", (String)jsonData.get("operateDate"));
            jsonMQData.put("operateTime", (String)jsonData.get("operateTime"));
            jsonMQData.put("remark", jsonData.get("remark"));
            rocketMQProducer.send(jsonMQData.toString(),MQOrderEnum.OAEXAMINE_ACOUNT_LOG.getTopic(),MQOrderEnum.OAEXAMINE_ACOUNT_LOG.getTags());
            LOGGER.info("OA审批过程日志推送接口,推送MQ报文"+jsonMQData.toString());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("OA审批过程日志推送接口回推异常{}", e.getMessage());
            return ResponseFactory.OAError("系统异常", oaBillCode);
        }
        return ResponseFactory.OASuccess("success",oaBillCode);
    }

}
