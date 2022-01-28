package com.topideal.webService.oa.dto;

import java.io.Serializable;

/**
 * 流程对象
 * @author zhouhr
 * @date 2021/12/23 11:32
 */
public class WorkflowBaseInfoWrap implements Serializable {

    //工作流ID
    private String workflowId;
    //工作流名称
    private String workflowName;
    //工作流类型ID
    private String workflowTypeId;
    //工作流类型名称
    private String workflowTypeName;

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowTypeId() {
        return workflowTypeId;
    }

    public void setWorkflowTypeId(String workflowTypeId) {
        this.workflowTypeId = workflowTypeId;
    }

    public String getWorkflowTypeName() {
        return workflowTypeName;
    }

    public void setWorkflowTypeName(String workflowTypeName) {
        this.workflowTypeName = workflowTypeName;
    }
}
