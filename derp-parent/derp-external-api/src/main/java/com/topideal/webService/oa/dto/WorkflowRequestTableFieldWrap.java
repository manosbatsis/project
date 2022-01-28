package com.topideal.webService.oa.dto;

import java.io.Serializable;

/**
 * 列对象
 * @author zhouhr
 * @date 2021/12/23 11:49
 */
public class WorkflowRequestTableFieldWrap implements Serializable {

    //字段名称
    private String columnName;
    //字段类型，java基本数据类型 string int double 附件
    //附件以两种形式传输，url=http:附件名字（包含附件类型） base64编码流 = base64:附件名字（包含附件类型）
    private String columnType;
    //字段值，附件是url=url地址  附件是编码字符串=base64编码字符串
    private String columnValue;
    //字段是否显示，默认true
    private Boolean view;
    //字段是否编辑，默认true
    private Boolean edit;
    //需转换数据类型，0=人员 1=部门 2=客商，以转换码表为准
    private String conversionType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public String getConversionType() {
        return conversionType;
    }

    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }
}
