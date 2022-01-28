package com.topideal.webService.oa.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 行对象
 * @author zhouhr
 * @date 2021/12/23 11:39
 */
public class WorkflowRequestTableRecordWrap implements Serializable {
    //列数据
    private List<WorkflowRequestTableFieldWrap> tableFields;

    public List<WorkflowRequestTableFieldWrap> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<WorkflowRequestTableFieldWrap> tableFields) {
        this.tableFields = tableFields;
    }
}
