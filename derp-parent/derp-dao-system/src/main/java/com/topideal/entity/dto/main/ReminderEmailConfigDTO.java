package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReminderEmailConfigDTO  extends PageModel implements Serializable {
    /**
     * 自增ID
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 商家ID
     */
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    /**
     * 商家名
     */
    @ApiModelProperty(value = "商家名")
    private String merchantName;
    /**
     * 事业部ID
     */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
     * 业务模块类型 1:应收 2:采购 3:销售
     */
    @ApiModelProperty(value = "业务模块类型")
    private String businessModuleType;
    @ApiModelProperty(value = "业务模块类型中文 1:应收 2:采购 3:销售 4:采购价格管理 5:销售价格管理 6:销售赊销模块 7:平台采购单 8:应付")
    private String businessModuleTypeLabel;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long creator;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
     * 修改人ID
     */
    @ApiModelProperty(value = "修改人ID")
    private Long modifier;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyName;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*buId get 方法 */
    public Long getBuId(){
        return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
        this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
        return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
        this.buName=buName;
    }
    /*businessModuleType get 方法 */
    public String getBusinessModuleType(){
        return businessModuleType;
    }
    /*businessModuleType set 方法 */
    public void setBusinessModuleType(String  businessModuleType){
        this.businessModuleType=businessModuleType;
        this.businessModuleTypeLabel= DERP_ORDER.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList, businessModuleType) ;
    }
    /*creator get 方法 */
    public Long getCreator(){
        return creator;
    }
    /*creator set 方法 */
    public void setCreator(Long  creator){
        this.creator=creator;
    }
    /*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*modifyName get 方法 */
    public String getModifyName(){
        return modifyName;
    }
    /*modifyName set 方法 */
    public void setModifyName(String  modifyName){
        this.modifyName=modifyName;
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
