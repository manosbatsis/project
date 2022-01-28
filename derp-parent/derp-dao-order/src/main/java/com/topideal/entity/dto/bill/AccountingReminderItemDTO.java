package com.topideal.entity.dto.bill;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class AccountingReminderItemDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
    * 账期配置ID
    */
    @ApiModelProperty("账期配置ID")
    private Long configId;
    /**
    * 节点 1-付款 2-出账单 3-账单确认 4-开票 5-回款
    */
    @ApiModelProperty("节点 1-付款 2-出账单 3-账单确认 4-开票 5-回款")
    private String node;
    @ApiModelProperty("节点中文 1-付款 2-出账单 3-账单确认 4-开票 5-回款")
    private String nodeLabel;
    /**
    *  计划节点时效
    */
    @ApiModelProperty("计划节点时效")
    private Integer nodeEffective;
    /**
    * 基准单位 1-工作日 2-自然日
    */
    @ApiModelProperty("基准单位 1-工作日 2-自然日")
    private String benchmarkUnit;
    @ApiModelProperty("基准单位中文 1-工作日 2-自然日")
    private String benchmarkUnitLabel;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
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
    this.nodeLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.accountingReminderConfig_noteList, node) ;
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
    this.benchmarkUnitLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.accountingReminderConfig_benchmarkUnitList, benchmarkUnit) ;
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


    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getBenchmarkUnitLabel() {
        return benchmarkUnitLabel;
    }

    public void setBenchmarkUnitLabel(String benchmarkUnitLabel) {
        this.benchmarkUnitLabel = benchmarkUnitLabel;
    }
}
