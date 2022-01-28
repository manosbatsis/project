package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class SettlementConfigDTO extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 项目ID
    */
    private String parentProjectCode;
    private String parentProjectCodeLabel;
    /**
    * 项目名称
    */
    private String parentProjectName;
    /**
    * 所有项目小类
    */
    private String allProjectName;
    /**
    * 项目小类ID
    */
    private String childProjectCode;
    /**
    * 项目小类名称
    */
    private String childProjectName;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 修改人ID
    */
    private Long modifier;
    /**
    * 修改人名称
    */
    private String modifierName;
    /**
     * 数据类型：1-仓内费用 2-快递费 3-取消订单服务费 4-综合税金
     */
    private String dataType;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*parentProjectCode get 方法 */
    public String getParentProjectCode(){
    return parentProjectCode;
    }
    /*parentProjectCode set 方法 */
    public void setParentProjectCode(String  parentProjectCode){
        this.parentProjectCode=parentProjectCode;
        if(StringUtils.isNotBlank(parentProjectCode)){
            this.parentProjectCodeLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.settlementConfig_projectList, parentProjectCode);
        }
    }
    /*parentProjectName get 方法 */
    public String getParentProjectName(){
    return parentProjectName;
    }
    /*parentProjectName set 方法 */
    public void setParentProjectName(String  parentProjectName){
    this.parentProjectName=parentProjectName;
    }
    /*allProjectName get 方法 */
    public String getAllProjectName(){
    return allProjectName;
    }
    /*allProjectName set 方法 */
    public void setAllProjectName(String  allProjectName){
    this.allProjectName=allProjectName;
    }
    /*childProjectCode get 方法 */
    public String getChildProjectCode(){
    return childProjectCode;
    }
    /*childProjectCode set 方法 */
    public void setChildProjectCode(String  childProjectCode){
    this.childProjectCode=childProjectCode;
    }
    /*childProjectName get 方法 */
    public String getChildProjectName(){
    return childProjectName;
    }
    /*childProjectName set 方法 */
    public void setChildProjectName(String  childProjectName){
    this.childProjectName=childProjectName;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getParentProjectCodeLabel() {
        return parentProjectCodeLabel;
    }

    public void setParentProjectCodeLabel(String parentProjectCodeLabel) {
        this.parentProjectCodeLabel = parentProjectCodeLabel;
    }
}
