package com.topideal.controller.oalog;

import com.topideal.common.constant.DERP_LOG_POINT;
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

@Controller
@RequestMapping("/derpapi/1301")
public class OAExamineResultController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAExamineResultController.class);

    @Autowired
    private RMQProducer rocketMQProducer;//MQ

    //OA审批结果推送接口
    @RequestMapping(params="method=erp8913",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    @SystemControllerLog(point= DERP_LOG_POINT.POINT_12108000001,model=DERP_LOG_POINT.POINT_12108000001_Label,keyword = "requestId")
    public JSONObject getOAExamineLog(@RequestBody String json) {
        LOGGER.info("OA审批结果接收参数："+json);
        //OA单据号
        String oaBillCode ="";

        try{
            //转换为json对象
            JSONObject jsonData = JSONObject.fromObject(json);
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

            //审批结果 0：审批驳回；1：审批通过
            if (jsonData.get("appResult")==null|| StringUtils.isBlank(jsonData.getString("appResult"))) {
                return ResponseFactory.OAError("appResult is null", oaBillCode);
            }
            if(!("0".equals(jsonData.get("appResult")) || "1".equals(jsonData.get("appResult")))) {
                return ResponseFactory.OAError("appResult is "+jsonData.get("logType"), oaBillCode);
            }

            // 实例化推送MQ的jJSON 对象
            JSONObject jsonMQData = new JSONObject();
            jsonMQData.put("requestId", (String)jsonData.get("requestId"));
            jsonMQData.put("appResult", (String)jsonData.get("appResult"));
            jsonMQData.put("oaBillCode", (String)jsonData.get("oaBillCode"));

            rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.OAEXAMINE_ACOUNT_RESULT.getTopic(),MQOrderEnum.OAEXAMINE_ACOUNT_RESULT.getTags());
            LOGGER.info("OA审批结果推送接口,推送MQ报文"+jsonMQData.toString());
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("OA审批结果推送接口推送接口回推异常{}", e.getMessage());
            return ResponseFactory.OAError("系统异常", oaBillCode);
        }
        return ResponseFactory.OASuccess("success",oaBillCode);
    }
}
