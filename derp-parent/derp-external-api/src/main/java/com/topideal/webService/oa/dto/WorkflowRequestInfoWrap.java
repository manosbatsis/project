package com.topideal.webService.oa.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 工作流请求信息
 * @author zhouhr
 * @date 2021/12/23 11:35
 */
public class WorkflowRequestInfoWrap implements Serializable {
    //创建人工号
    private String creatorCode;
    //请求级别 0 正常，1重要，2紧急
    private String requestLevel;
    //请求流程名称
    private String requestName;
    //工作流
    private WorkflowBaseInfoWrap baseInfo;
    //主表数据
    private WorkflowMainTableInfoWrap mainTableInfo;
    //明细表数据
    private List<WorkflowDetailTableInfoWrap> detailTableInfoWraps;

    // 来源
    private String lyxt;

    public String getCreatorCode() {
        return creatorCode;
    }

    public void setCreatorCode(String creatorCode) {
        this.creatorCode = creatorCode;
    }

    public String getRequestLevel() {
        return requestLevel;
    }

    public void setRequestLevel(String requestLevel) {
        this.requestLevel = requestLevel;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public WorkflowBaseInfoWrap getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(WorkflowBaseInfoWrap baseInfo) {
        this.baseInfo = baseInfo;
    }

    public WorkflowMainTableInfoWrap getMainTableInfo() {
        return mainTableInfo;
    }

    public void setMainTableInfo(WorkflowMainTableInfoWrap mainTableInfo) {
        this.mainTableInfo = mainTableInfo;
    }

    public List<WorkflowDetailTableInfoWrap> getDetailTableInfoWraps() {
        return detailTableInfoWraps;
    }

    public void setDetailTableInfoWraps(List<WorkflowDetailTableInfoWrap> detailTableInfoWraps) {
        this.detailTableInfoWraps = detailTableInfoWraps;
    }

    public String getLyxt() {
        return lyxt;
    }

    public void setLyxt(String lyxt) {
        this.lyxt = lyxt;
    }
}
