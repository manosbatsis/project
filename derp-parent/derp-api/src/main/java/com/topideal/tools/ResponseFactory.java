package com.topideal.tools;



import com.topideal.code.OPCodeEnum;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端响应工具类
 * Created by weixiaolei on 2018/4/26.
 */
public class ResponseFactory {

    //======================= 主数据================================
	/**
     * 主数据  成功
     * 同步商品
     * 请求成功 返回参数信息
     * @param opCodeEnum  错误码类型
     * @return
     */
    public static JSONObject success(OPCodeEnum opCodeEnum){
        JSONObject responseData =new JSONObject();
        responseData.put("flag", "SUCCESS");//结果 SUCCESS,FAILURE
        responseData.put("code", opCodeEnum.getCode());//错误代码  0成功
        responseData.put("message", opCodeEnum.getMessage());//错误信息
        //主数据编码
        responseData.put("derp-code","001");
        return responseData;
    }

    /**
     * 主数据  失败
     * 同步商品
     * 请求成功 返回参数信息
     * @param opCodeEnum  错误码类型
     * @param errInfo  错误定位
     * @return
     */
    public static JSONObject error(OPCodeEnum opCodeEnum,String errInfo){
        JSONObject responseData =new JSONObject();
        responseData.put("flag", "FAILURE");//结果 SUCCESS,FAILURE
        responseData.put("code", opCodeEnum.getCode());//错误代码  0成功
        responseData.put("message", opCodeEnum.getMessage());//错误信息
        responseData.put("errInfo", errInfo);
        //主数据编码
        responseData.put("derp-code","001");
        return responseData;
    }
    /**
     * 主数据
     * @param opCodeEnum
     * @param errInfo
     * @return
     */
    public static JSONObject syncError(String errInfo){
        JSONObject responseData =new JSONObject();
        responseData.put("status", 2);//错误代码  1成功 2失败
        responseData.put("notes", "失败");
        responseData.put("flag", "FAILURE");
        responseData.put("message", errInfo);//错误信息
        responseData.put("errInfo", errInfo);
        responseData.put("derp-code","001");
        return responseData;
    }
	/**
     * 主数据  成功
     * 同步商品
     * 请求成功 返回参数信息
     * @param opCodeEnum  错误码类型
     * @return
     */
    public static JSONObject syncSuccess(){
        JSONObject responseData =new JSONObject();
        responseData.put("status", 1);//错误代码  1成功 2失败
        responseData.put("notes", "成功");//错误信息
        responseData.put("flag", "SUCCESS");
        responseData.put("message", "SUCCESS");//错误信息
        responseData.put("errInfo", "SUCCESS");
        responseData.put("derp-code","001");
        return responseData;
    }
    
    /**
     *跨境宝 接口 成功
     * @param asn_no 
     * @param notes 错误和成功信息
     * @return
     */
    public static JSONObject success(String noName,String no){
    	JSONObject responseData =new JSONObject();
    	responseData.put(noName, no);
    	responseData.put("status", "1");//1 成功 2失败
    	responseData.put("notes", "提交成功");//如失败，则放错误信息
        //主数据编码
        responseData.put("derp-code","002");
    	return responseData;
    }

    /**
     * 跨境宝 接口 失败
     * @param asn_no 
     * @param notes 错误和成功信息
     * @return
     */
    public static JSONObject error(String noName,String no,String notes){
    	JSONObject responseData =new JSONObject();
    	responseData.put(noName, no);
    	responseData.put("status", "2");//1 成功 2失败
    	responseData.put("notes", notes);//如失败，则放错误信息
        //主数据编码
        responseData.put("derp-code","002");
    	return responseData;
    }


    /**
     * OA审批日志接口 成功
     * @param msg
     * @param oaBillCode
     * @return
     */
    public static JSONObject OASuccess(String msg,String oaBillCode){
        JSONObject responseData =new JSONObject();
        responseData.put("status", "1001");//状态 1001：成功；1002：失败
        responseData.put("msg", msg);//报错返回信息
        responseData.put("oaBillCode", oaBillCode);//OA单据号
        responseData.put("derp-code","004");
        return responseData;
    }

    /**
     * OA审批日志接口 失败
     * @param msg
     * @param oaBillCode
     * @return
     */
    public static JSONObject OAError(String msg,String oaBillCode){
        JSONObject responseData =new JSONObject();
        responseData.put("status", "1002");//状态 1001：成功；1002：失败
        responseData.put("msg", msg);//报错返回信息
        responseData.put("oaBillCode", oaBillCode);//OA单据号
        responseData.put("derp-code","004");
        return responseData;
    }


}
