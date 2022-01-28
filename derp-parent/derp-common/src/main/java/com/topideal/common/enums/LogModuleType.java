package com.topideal.common.enums;

/**
 * 日志模块类型枚举
 * Created by weixiaolei on 2018/8/17.
 */
public enum LogModuleType {

    MODULE_ORDER(1,"业务模块"),
    MODULE_PUSH_API(2,"推送外部API"),
    MODULE_STORAGE(3,"仓储模块"),
    MODULE_INVENTORY(4,"库存模块"),
    MODULE_API(5,"api接口"),
	MODULE_REPORT(6,"报表模块");

    private LogModuleType(int type,String name){
         this.type=type;
        this.name=name;
    }

    private int type;

    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
