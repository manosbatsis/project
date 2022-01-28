package com.topideal.common.tools;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 空值 校验 工具类
 * Created by weixiaolei on 2018/4/26.
 */
public class EmptyCheckUtils {
    //校验对象集合
    private List  objectList;

      public EmptyCheckUtils(){
          objectList = new ArrayList();
      }

    /**
     * 添加校验对象
     * @param object
     * @return
     */
    public EmptyCheckUtils addObject(Object object){
        objectList.add(object);
        return this;
    }

    /**
     * 校验集合中的对象是否为空
     * @return  true 包含空值    false  无空值对象
     */
    public boolean empty(){
        for (Object  param : objectList) {
            if(param instanceof String){
                if(StringUtils.isBlank((String)param)){
                    return true;
                }
            }else{
                if(param==null){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args){
        boolean b=new EmptyCheckUtils().addObject("String").addObject(new Integer(25)).addObject(null).empty();
        System.out.println(
                b
        );

    }


}
