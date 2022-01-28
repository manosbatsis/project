package com.topideal.entity.vo.main;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class CommbarcodeModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
	@ApiModelProperty(value = "标准品牌Id")
    private Long commBrandParentId;
	@ApiModelProperty(value = "标准品牌名")
    private String commBrandParentName;
	@ApiModelProperty(value = "标准品类Id(标准二级分类id)")
    private Long commTypeId;
	@ApiModelProperty(value = "标准品类名（标准二级分类名）")
    private String commTypeName;
	@ApiModelProperty(value = "标准品名（标准商品名称）")
    private String commGoodsName;
	@ApiModelProperty(value = "维护状态 0-未维护 1-已维护")
    private String maintainStatus;
	@ApiModelProperty(value = "创建日期")
    private Timestamp createDate;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*commBrandParentId get 方法 */
    public Long getCommBrandParentId(){
    return commBrandParentId;
    }
    /*commBrandParentId set 方法 */
    public void setCommBrandParentId(Long  commBrandParentId){
    this.commBrandParentId=commBrandParentId;
    }
    /*commBrandParentName get 方法 */
    public String getCommBrandParentName(){
    return commBrandParentName;
    }
    /*commBrandParentName set 方法 */
    public void setCommBrandParentName(String  commBrandParentName){
    this.commBrandParentName=commBrandParentName;
    }
    /*commTypeId get 方法 */
    public Long getCommTypeId(){
    return commTypeId;
    }
    /*commTypeId set 方法 */
    public void setCommTypeId(Long  commTypeId){
    this.commTypeId=commTypeId;
    }
    /*commTypeName get 方法 */
    public String getCommTypeName(){
    return commTypeName;
    }
    /*commTypeName set 方法 */
    public void setCommTypeName(String  commTypeName){
    this.commTypeName=commTypeName;
    }
    /*commGoodsName get 方法 */
    public String getCommGoodsName(){
    return commGoodsName;
    }
    /*commGoodsName set 方法 */
    public void setCommGoodsName(String  commGoodsName){
    this.commGoodsName=commGoodsName;
    }
    /*maintainStatus get 方法 */
    public String getMaintainStatus(){
    return maintainStatus;
    }
    /*maintainStatus set 方法 */
    public void setMaintainStatus(String  maintainStatus){
    this.maintainStatus=maintainStatus;
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
