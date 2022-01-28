package com.topideal.service.maindata;

import net.sf.json.JSONObject;

/**
 * （主数据新系统）客商信息下发接口2.0
 * 说明: 主数据接口 :4.13 企业信息推送接口
 * @author 杨创
 */
public interface SyncCustomerMainService {


    /**
     * 新增/或修改主数据客商详情
     * @return
     * @throws Exception
     */
	boolean synsCustomerMain(JSONObject jsonData) throws Exception;





}
