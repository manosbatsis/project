package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 初理货  业务
 * Created by weixiaolei on 2018/5/24.
 */
public interface FirstTallyService {

    //初存初理货信息
    JSONObject tallyBaseInfo(String json,Long merchantId)throws Exception;


}
