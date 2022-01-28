package com.topideal.webService.oa.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 明显表对象
 * @author zhouhr
 * @date 2021/12/23 11:41
 */
public class WorkflowDetailTableInfoWrap implements Serializable {

    //明细表下标，第一个明细表=1，第二个明细表=2，依次类推
    private Integer index;
    //表名
    private String tableName;
    //行数据
    private List<WorkflowRequestTableRecordWrap> tableRecords;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<WorkflowRequestTableRecordWrap> getTableRecords() {
        return tableRecords;
    }

    public void setTableRecords(List<WorkflowRequestTableRecordWrap> tableRecords) {
        this.tableRecords = tableRecords;
    }
}
