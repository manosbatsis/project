package com.topideal.enums;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 模板
 * Created by weixiaolei on 2018/5/18.
 */
public enum ExcelTemplateEnum {

    M_001(101,"/template/excel/101.xlsx","订单100触发导入模板.xlsx"),
    M_002(102,"/template/excel/102.xlsx","日志监控批量重推导入摸版.xlsx"),
    M_003(103,"/template/excel/103.xlsx","库存批量回滚导入模版.xlsx"),
    M_004(104,"/template/excel/104.xlsx","电商订单金额导入更新模板.xlsx");


    //编码
    private int code;
    //路径
    private String path;
    //描述
    private String desc;

    private ExcelTemplateEnum(Integer code,String path,String desc){
        this.code=code;
        this.path=path;
        this.desc=desc;
    }


    public static String getPath(int code){
        for (ExcelTemplateEnum model : ExcelTemplateEnum.values()) {
               if(model.code==code){
                   return model.path;
               }
        }
        return null;
    }
    
    public static String getDesc(int code){
        for (ExcelTemplateEnum model : ExcelTemplateEnum.values()) {
               if(model.code==code){
                   return model.desc;
               }
        }
        return null;
    }

    public static InputStream getFile(String path)throws IOException{
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource("classpath:../../"+path);
        return resource.getInputStream();
    }
    
    public static void main(String[] args){
        System.out.println(ExcelTemplateEnum.getPath(001));
    }



}
