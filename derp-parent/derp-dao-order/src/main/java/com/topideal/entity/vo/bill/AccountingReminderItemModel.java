package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class AccountingReminderItemModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 账期配置ID
    */
    private Long configId;
    /**
    * 节点 1-付款 2-出账单 3-账单确认 4-开票 5-回款
    */
    private String node;
    /**
    *  计划节点时效
    */
    private Integer nodeEffective;
    /**
    * 基准单位 1-工作日 2-自然日
    */
    private String benchmarkUnit;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*configId get 方法 */
    public Long getConfigId(){
    return configId;
    }
    /*configId set 方法 */
    public void setConfigId(Long  configId){
    this.configId=configId;
    }
    /*node get 方法 */
    public String getNode(){
    return node;
    }
    /*node set 方法 */
    public void setNode(String  node){
    this.node=node;
    }
    /*nodeEffective get 方法 */
    public Integer getNodeEffective(){
    return nodeEffective;
    }
    /*nodeEffective set 方法 */
    public void setNodeEffective(Integer  nodeEffective){
    this.nodeEffective=nodeEffective;
    }
    /*benchmarkUnit get 方法 */
    public String getBenchmarkUnit(){
    return benchmarkUnit;
    }
    /*benchmarkUnit set 方法 */
    public void setBenchmarkUnit(String  benchmarkUnit){
    this.benchmarkUnit=benchmarkUnit;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }






}
