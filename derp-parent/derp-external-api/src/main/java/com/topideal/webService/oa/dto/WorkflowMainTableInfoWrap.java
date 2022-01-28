package com.topideal.webService.oa.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 主表对象
 * @author zhouhr
 * @date 2021/12/23 11:38
 */
public class WorkflowMainTableInfoWrap implements Serializable {
    //表名
    private String tableName;
    //行数据，主表只有一行数据
    private List<WorkflowRequestTableFieldWrap> tableFields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<WorkflowRequestTableFieldWrap> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<WorkflowRequestTableFieldWrap> tableFields) {
        this.tableFields = tableFields;
    }
}
