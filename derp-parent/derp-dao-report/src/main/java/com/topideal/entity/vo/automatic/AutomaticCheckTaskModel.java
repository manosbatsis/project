package com.topideal.entity.vo.automatic;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class AutomaticCheckTaskModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 任务编码(自生成)
    */
    private String taskCode;
    /**
    * 任务类型  1:POP流水核对 2:仓库流水核对
    */
    private String taskType;
    /**
    * 核对开始日期
    */
    private Timestamp checkStartDate;
    /**
    * 核对结束时间
    */
    private Timestamp checkEndDate;
    /**
    * 核对结果 0:未对平 1:已对平
    */
    private String checkResult;
    /**
    * 处理状态 1:待处理 2:处理中 3:已完成 4:处理失败
    */
    private String state;
    /**
    * 出仓仓库ID
    */
    private Long outDepotId;
    /**
    * 出仓仓库名称
    */
    private String outDepotName;
    /**
    * 出仓仓库编码
    */
    private String outDepotCode;
    /**
    * 电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包
    */
    private String storePlatformCode;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 核对备注
    */
    private String remark;
    /**
    * 源文件存储路径
    */
    private String sourcePath;
    /**
    * 文件的存储路径
    */
    private String storePath;
    /**
    * 数据源 1：GSS报表 2：菜鸟后台
    */
    private String dataSource;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改人名称
    */
    private String modifierName;
    /**
    * 修改日期
    */
    private Timestamp modifyDate;
    /**
    * 是否标记过（0-否，1-是）
    */
    private String isMark;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*taskCode get 方法 */
    public String getTaskCode(){
    return taskCode;
    }
    /*taskCode set 方法 */
    public void setTaskCode(String  taskCode){
    this.taskCode=taskCode;
    }
    /*taskType get 方法 */
    public String getTaskType(){
    return taskType;
    }
    /*taskType set 方法 */
    public void setTaskType(String  taskType){
    this.taskType=taskType;
    }
    /*checkStartDate get 方法 */
    public Timestamp getCheckStartDate(){
    return checkStartDate;
    }
    /*checkStartDate set 方法 */
    public void setCheckStartDate(Timestamp  checkStartDate){
    this.checkStartDate=checkStartDate;
    }
    /*checkEndDate get 方法 */
    public Timestamp getCheckEndDate(){
    return checkEndDate;
    }
    /*checkEndDate set 方法 */
    public void setCheckEndDate(Timestamp  checkEndDate){
    this.checkEndDate=checkEndDate;
    }
    /*checkResult get 方法 */
    public String getCheckResult(){
    return checkResult;
    }
    /*checkResult set 方法 */
    public void setCheckResult(String  checkResult){
    this.checkResult=checkResult;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
    }
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
    }
    /*outDepotCode get 方法 */
    public String getOutDepotCode(){
    return outDepotCode;
    }
    /*outDepotCode set 方法 */
    public void setOutDepotCode(String  outDepotCode){
    this.outDepotCode=outDepotCode;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
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
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*sourcePath get 方法 */
    public String getSourcePath(){
    return sourcePath;
    }
    /*sourcePath set 方法 */
    public void setSourcePath(String  sourcePath){
    this.sourcePath=sourcePath;
    }
    /*storePath get 方法 */
    public String getStorePath(){
    return storePath;
    }
    /*storePath set 方法 */
    public void setStorePath(String  storePath){
    this.storePath=storePath;
    }
    /*dataSource get 方法 */
    public String getDataSource(){
    return dataSource;
    }
    /*dataSource set 方法 */
    public void setDataSource(String  dataSource){
    this.dataSource=dataSource;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*isMark get 方法 */
    public String getIsMark(){
    return isMark;
    }
    /*isMark set 方法 */
    public void setIsMark(String  isMark){
    this.isMark=isMark;
    }






}
