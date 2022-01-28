
package com.topideal.webService;
import java.net.URL;

import com.topideal.webService.oa.services.webservices.WorkflowService;
import com.topideal.webService.oa.services.webservices.WorkflowServiceLocator;
import com.topideal.webService.oa.services.webservices.WorkflowServicePortType;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowBaseInfo;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowMainTableInfo;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowRequestInfo;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowRequestTableField;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowRequestTableRecord;

/**
 * @author zhouhr
 * @date 2021/5/17 16:25
 */
public class B {
    public static void main(String[] args) {
        //主表字段对象数组
        WorkflowRequestTableField[] headTableField = new WorkflowRequestTableField[1];
        //主表字段对象
        headTableField[0] = new WorkflowRequestTableField();

        headTableField[0] = new WorkflowRequestTableField();
        headTableField[0].setFieldName("sqr");//申请人
        headTableField[0].setFieldValue("24");
        headTableField[0].setView(true);
        headTableField[0].setEdit(true);


        //主表对象数组 暂定一行
       WorkflowRequestTableRecord[] headTableRecord = new WorkflowRequestTableRecord[1];
        //主表第一行对象
        headTableRecord[0] = new WorkflowRequestTableRecord();
        //存放主表第一行字段信息
        headTableRecord[0].setWorkflowRequestTableFields(headTableField);

        //主表对象
        WorkflowMainTableInfo headTableInfo = new WorkflowMainTableInfo();
        //主表添加行
        headTableInfo.setRequestRecords(headTableRecord);

        //==========================
        //明细表对象数组，
        WorkflowRequestTableRecord[] bodyTableRecord = new WorkflowRequestTableRecord[1];

//        //第一行
//        bodyTableRecord[0] = new WorkflowRequestTableRecord();
//        //第一行6个字段
//        WorkflowRequestTableField[] bodyTableField = new WorkflowRequestTableField[6];
//        //第一行第一个字段
//        bodyTableField[0] = new WorkflowRequestTableField();
//        bodyTableField[0].setFieldName("xtmc");//系统名称
//        bodyTableField[0].setFieldValue("泛微OA外网");
//        bodyTableField[0].setView(true);
//        bodyTableField[0].setEdit(true);
//        //第一行第二个字段
//        bodyTableField[1] = new WorkflowRequestTableField();
//        bodyTableField[1].setFieldName("qxmc");//权限名称
//        bodyTableField[1].setFieldValue("CRM管理员");
//        bodyTableField[1].setView(true);
//        bodyTableField[1].setEdit(true);
//
//        bodyTableField[2] = new WorkflowRequestTableField();
//        bodyTableField[2].setFieldName("qxms");//权限描述
//        bodyTableField[2].setFieldValue("");
//        bodyTableField[2].setView(true);
//        bodyTableField[2].setEdit(true);
//
//        bodyTableField[3] = new WorkflowRequestTableField();
//        bodyTableField[3].setFieldName("qxjzyxq");//权限截止有效期
//        bodyTableField[3].setFieldValue("2099-12-31");
//        bodyTableField[3].setView(true);
//        bodyTableField[3].setEdit(true);
//
//        bodyTableField[4] = new WorkflowRequestTableField();
//        bodyTableField[4].setFieldName("zhlx");//租户类型
//        bodyTableField[4].setFieldValue("");
//        bodyTableField[4].setView(true);
//        bodyTableField[4].setEdit(true);
//
//        bodyTableField[5] = new WorkflowRequestTableField();
//        bodyTableField[5].setFieldName("szzz");//所属组织
//        bodyTableField[5].setFieldValue("");
//        bodyTableField[5].setView(true);
//        bodyTableField[5].setEdit(true);

//        //第一行存放列数组
//        bodyTableRecord[0].setWorkflowRequestTableFields(bodyTableField);


//        WorkflowDetailTableInfo[] bodyTableInfo = new WorkflowDetailTableInfo[1];//一个明细表
//        bodyTableInfo[0] = new WorkflowDetailTableInfo();
//        bodyTableInfo[0].setWorkflowRequestTableRecords(bodyTableRecord);

        //流程
        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        workflowBaseInfo.setWorkflowId("298");//流程ID（workflowId）
        workflowBaseInfo.setWorkflowName("1111");

        WorkflowRequestInfo requestInfo = new WorkflowRequestInfo();//流程基本信息
        requestInfo.setCanView(true);
        requestInfo.setCanEdit(true);
        requestInfo.setCreatorId("24");//创建人id
        requestInfo.setRequestLevel("0");//0 正常，1重要，2紧急
        requestInfo.setRequestName("1111");//流程标题
        requestInfo.setWorkflowBaseInfo(workflowBaseInfo);//添加流程
        requestInfo.setWorkflowMainTableInfo(headTableInfo);//添加主表数据
//        requestInfo.setWorkflowDetailTableInfos(bodyTableInfo);//添加明细主表数据
        try {
            //获取调用对象
            WorkflowService workflowService = new WorkflowServiceLocator();
            URL url = new URL("http://10.10.103.66:8000/services/WorkflowService");
            WorkflowServicePortType workflowServiceProxy = workflowService.getWorkflowServiceHttpPort(url);
            String s1 = workflowServiceProxy.doCreateWorkflowRequest(requestInfo, 24);
            System.out.println("流程号："+s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
