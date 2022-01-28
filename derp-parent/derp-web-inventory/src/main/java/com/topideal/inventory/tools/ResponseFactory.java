package com.topideal.inventory.tools;

import java.util.Map;

import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;

import net.sf.json.JSONObject;

/**
 * 前端响应工具类
 * Created by weixiaolei on 2018/4/26.
 */
public class ResponseFactory {

	/**
     * 请求成功 返回参数信息
     * @param data
     * @return
     */
    public static ViewResponseBean success(Object data, Map dataMap){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(StateCodeEnum.SUCCESS.getCode());
        responseBean.setMessage(StateCodeEnum.SUCCESS.getMessage());
        responseBean.setData(data);
        responseBean.setDataMap(dataMap);
        return responseBean;
    }
	
    /**
     * 请求成功 返回参数信息
     * @param data
     * @return
     */
    public static ViewResponseBean success(Object data){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(StateCodeEnum.SUCCESS.getCode());
        responseBean.setMessage(StateCodeEnum.SUCCESS.getMessage());
        responseBean.setData(data);
        return responseBean;
    }

    /**
     * 请求成功 返回参数信息
     * @param data
     * @return
     */
    public static ViewResponseBean success(){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(StateCodeEnum.SUCCESS.getCode());
        responseBean.setMessage(StateCodeEnum.SUCCESS.getMessage());
        return responseBean;
    }

    /**
     * 请求失败返回参数信息
     * @param stateCodeEnum
     * @return
     */
    public static ViewResponseBean error(StateCodeEnum stateCodeEnum){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(stateCodeEnum.getCode());
        responseBean.setMessage(stateCodeEnum.getMessage());
        return responseBean;
    }

    /**
     *
     * @param stateCodeEnum
     * @param e  异常信息
     * @return
     */
    public static ViewResponseBean error(StateCodeEnum stateCodeEnum,Exception e){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(stateCodeEnum.getCode());
        responseBean.setMessage(stateCodeEnum.getMessage());
        responseBean.setExpMessage(e.getMessage());
        return responseBean;
    }



    /**
     * 可用库存 接口 成功  baols
     * @param asn_no 
     * @param notes 错误和成功信息
     * @return
     */
    public static JSONObject inventorySuccess(String noName,int num){
    	JSONObject responseData =new JSONObject();
    	responseData.put(noName, num);
    	responseData.put("status", "1");//1 成功 2失败
    	responseData.put("notes", "成功");//如失败，则放错误信息
    	return responseData;
    }

    /**
     * 可用库存  接口 失败  baols
     * @param asn_no 
     * @param notes 错误和成功信息
     * @return
     */
    public static JSONObject inventoryError(String notes){
    	JSONObject responseData =new JSONObject();
    	responseData.put("status", "2");//1 成功 2失败
    	responseData.put("notes", notes);//如失败，则放错误信息
    	return responseData;
    }


}
