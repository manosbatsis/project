package com.topideal.webService.oa.services.webservices;

import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowBaseInfo;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowRequestInfo;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.WorkflowRequestLog;

public class WorkflowServicePortTypeProxy implements WorkflowServicePortType {
  private String _endpoint = null;
  private WorkflowServicePortType workflowServicePortType = null;
  
  public WorkflowServicePortTypeProxy() {
    _initWorkflowServicePortTypeProxy();
  }
  
  public WorkflowServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initWorkflowServicePortTypeProxy();
  }
  
  private void _initWorkflowServicePortTypeProxy() {
    try {
      workflowServicePortType = (new WorkflowServiceLocator()).getWorkflowServiceHttpPort();
      if (workflowServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)workflowServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)workflowServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (workflowServicePortType != null)
      ((javax.xml.rpc.Stub)workflowServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public WorkflowServicePortType getWorkflowServicePortType() {
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType;
  }
  
  public String forward2WorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.forward2WorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getAllWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getAllWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo getWorkflowRequest(int in0, int in1, int in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequest(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getHendledWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getToDoWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo getWorkflowRequest4Split(int in0, int in1, int in2, int in3) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequest4Split(in0, in1, in2, in3);
  }
  
  public String submitWorkflowRequest(WorkflowRequestInfo in0, int in1, int in2, String in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.submitWorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public int getHendledWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestCount(in0, in1);
  }
  
  public String getLeaveDays(String in0, String in1, String in2, String in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getLeaveDays(in0, in1, in2, in3, in4);
  }
  
  public WorkflowBaseInfo[] getCreateWorkflowList(int in0, int in1, int in2, int in3, int in4, String[] in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowList(in0, in1, in2, in3, in4, in5);
  }
  
  public int getCreateWorkflowCount(int in0, int in1, String[] in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowCount(in0, in1, in2);
  }
  
  public int getProcessedWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestCount(in0, in1);
  }
  
  public String forwardWorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.forwardWorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public String doCreateWorkflowRequest(WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.doCreateWorkflowRequest(in0, in1);
  }
  
  public String doForceOver(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.doForceOver(in0, in1);
  }
  
  public int getCCWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestCount(in0, in1);
  }
  
  public WorkflowRequestInfo[] getProcessedWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public int getAllWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getAllWorkflowRequestCount(in0, in1);
  }
  
  public WorkflowRequestInfo getCreateWorkflowRequestInfo(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowRequestInfo(in0, in1);
  }
  
  public WorkflowRequestInfo[] getMyWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowBaseInfo[] getCreateWorkflowTypeList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowTypeList(in0, in1, in2, in3, in4);
  }
  
  public int getMyWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestCount(in0, in1);
  }
  
  public String[] getWorkflowNewFlag(String[] in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowNewFlag(in0, in1);
  }
  
  public void writeWorkflowReadFlag(String in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    workflowServicePortType.writeWorkflowReadFlag(in0, in1);
  }
  
  public int getToDoWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestCount(in0, in1);
  }
  
  public String givingOpinions(int in0, int in1, String in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.givingOpinions(in0, in1, in2);
  }
  
  public int getCreateWorkflowTypeCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowTypeCount(in0, in1);
  }
  
  public WorkflowRequestLog[] getWorkflowRequestLogs(String in0, String in1, int in2, int in3, int in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequestLogs(in0, in1, in2, in3, in4);
  }
  
  public boolean deleteRequest(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.deleteRequest(in0, in1);
  }
  
  public WorkflowRequestInfo[] getCCWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public String getUserId(String in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getUserId(in0, in1);
  }
  
  
}