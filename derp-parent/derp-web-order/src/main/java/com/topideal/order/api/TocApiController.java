package com.topideal.order.api;

import com.alibaba.fastjson.JSON;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.webapi.APIResponse;
import com.topideal.common.tools.DPSign;
import com.topideal.mongo.dao.ApiExternalConfigMongoDao;
import com.topideal.mongo.entity.ApiExternalConfigMongo;
import com.topideal.order.api.bean.dto.QueryTocTempReceiveBillDTO;
import com.topideal.order.service.bill.ToCTempReceiveBillService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 10:44
 * @Description: Toc外部接口Controller
 */
@Controller
@RequestMapping("/api/toc")
public class TocApiController {

    private static final Logger LOG = Logger.getLogger(TocApiController.class) ;

    @Autowired
    private ToCTempReceiveBillService toCTempReceiveBillService;
    @Autowired
    private ApiExternalConfigMongoDao apiExternalConfigMongoDao;

    /**
     * 查询经分销toc暂估数据
     * @param dto
     * @return
     */
    @PostMapping(value = "/queryTocTempReceiveBill.asyn")
    @ResponseBody
    private APIResponse queryTocTempReceiveBill(@RequestBody QueryTocTempReceiveBillDTO dto) {
        APIResponse response = new APIResponse();
        boolean signTag = true;
        try {
            //验签
            String appKey = dto.getAppKey();
            Map<String, Object> param = new HashMap<>();
            param.put("appKey", appKey);
            param.put("status", DERP_SYS.APIEXTERNALCONFIG_STATUS_1);
            ApiExternalConfigMongo configModel = apiExternalConfigMongoDao.findOne(param);
            if(configModel==null){
                response.setCode("9999");
                response.setMessage("商家appkey不存在");
                return response;
            }



            JSONObject jsonObject = JSONObject.fromObject(JSON.toJSONString(dto));
            LOG.debug(DPSign.sortMd5Encrypt(jsonObject,configModel.getAppSecret(), "sign"));
            String sign = dto.getSign();
            signTag = DPSign.signVerify(sign, jsonObject, configModel.getAppSecret(),"sign");

            if(signTag!=true) {
                response.setCode("999");
                response.setMessage("验签失败");
                return response;
            }

            response = toCTempReceiveBillService.queryTocTempReceiveBill(dto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode("999");
            response.setMessage("系统出现未知错误");
            return response;
        }
        return response;
    }
}
