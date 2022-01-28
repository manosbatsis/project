package com.topideal.common.system.webapi;


/**
 * 前端响应工具类
 *
 */
public class WebResponseFactory {

    /**
     * 构建返回对象
     * @param MsEnum 消息枚举
     */
    public static ResponseBean responseBuild(MessageEnum MsEnum){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(MsEnum.getCode());
        responseBean.setMessage(MsEnum.getMessage());
        responseBean.setData("{}");
        return responseBean;
    }
    /**
     * 构建返回对象
     * @param code 返回码
     * @param message 消息
     */
    public static ResponseBean responseBuild(String code ,String message){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(code);
        responseBean.setMessage(message);
        responseBean.setData("{}");
        return responseBean;
    }
    /**
     * 构建返回对象
     * @param MsEnum 消息枚举
     * @param data 返回内容体
     */
    public static ResponseBean responseBuild(MessageEnum MsEnum,Object data){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(MsEnum.getCode());
        responseBean.setMessage(MsEnum.getMessage());
        responseBean.setData(data);
        return responseBean;
    }
    /**
     * 构建返回对象
     * @param MsEnum 消息枚举
     * @param e 异常
     */
    public static ResponseBean responseBuild(MessageEnum MsEnum,Exception e){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(MsEnum.getCode());
        //若是手工抛出的异常则获取手工抛出的中文提示给前端，若是代码运行异常则提示未知异常给前端
        //if(e instanceof DerpException){
            responseBean.setMessage(e.getMessage());
        /*}else {
            responseBean.setMessage(MsEnum.getMessage());
            responseBean.setExpMessage(e.getMessage());//异常消息
        }*/
        responseBean.setData("{}");
        return responseBean;
    }
}
