package com.topideal.common.system.web;


import java.util.Map;

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
    public static ViewResponseBean error(StateCodeEnum stateCodeEnum, Exception e){
        ViewResponseBean responseBean=new ViewResponseBean();
        responseBean.setState(stateCodeEnum.getCode());
        responseBean.setMessage(stateCodeEnum.getMessage());
        responseBean.setExpMessage(e.getMessage());
        return responseBean;
    }






}
