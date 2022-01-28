package com.topideal.entity.vo.user;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 权限信息表
 */
public class PermissionInfoModel extends PageModel implements Serializable{

    //id
    private Long id;
     //权限名称
     private String authorityName;
     //修改时间
     private Timestamp modifyDate;
     //修改人
     private Long modifier;
     //图标样式
     private String icon;
     //授权码
     private String permission;
     //备注
     private String remark;
     //路径
     private String spathid;
     //权限类型  1 菜单  2  按钮
     private Integer type;
     //权限地址
     private String url;
     //父级权限ID
     private Long parentId;
     //是否启用  0 不启用  1 启用
     private Integer isEnabled;
     //创建人
     private Long creater;
     //序号
     private Integer seq;
     //创建时间（CURRENT_TIMESTAMP）
     private Timestamp createDate;
     //服务器地址
     private String serverAddr;
     //模块 1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report
     private String module;


     /*serverAddr get 方法 */
     public String getServerAddr(){
         return serverAddr;
     }
     /*serverAddr set 方法 */
     public void setServerAddr(String  serverAddr){
         this.serverAddr=serverAddr;
     }
    /*authorityName get 方法 */
    public String getAuthorityName(){
        return authorityName;
    }
    /*authorityName set 方法 */
    public void setAuthorityName(String  authorityName){
        this.authorityName=authorityName;
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
    /*icon get 方法 */
    public String getIcon(){
        return icon;
    }
    /*icon set 方法 */
    public void setIcon(String  icon){
        this.icon=icon;
    }
    /*permission get 方法 */
    public String getPermission(){
        return permission;
    }
    /*permission set 方法 */
    public void setPermission(String  permission){
        this.permission=permission;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*spathid get 方法 */
    public String getSpathid(){
        return spathid;
    }
    /*spathid set 方法 */
    public void setSpathid(String  spathid){
        this.spathid=spathid;
    }
    /*type get 方法 */
    public Integer getType(){
        return type;
    }
    /*type set 方法 */
    public void setType(Integer  type){
        this.type=type;
    }
    /*url get 方法 */
    public String getUrl(){
        return url;
    }
    /*url set 方法 */
    public void setUrl(String  url){
        this.url=url;
    }
    /*parentId get 方法 */
    public Long getParentId(){
        return parentId;
    }
    /*parentId set 方法 */
    public void setParentId(Long  parentId){
        this.parentId=parentId;
    }
    /*isEnabled get 方法 */
    public Integer getIsEnabled(){
        return isEnabled;
    }
    /*isEnabled set 方法 */
    public void setIsEnabled(Integer  isEnabled){
        this.isEnabled=isEnabled;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*seq get 方法 */
    public Integer getSeq(){
        return seq;
    }
    /*seq set 方法 */
    public void setSeq(Integer  seq){
        this.seq=seq;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}

