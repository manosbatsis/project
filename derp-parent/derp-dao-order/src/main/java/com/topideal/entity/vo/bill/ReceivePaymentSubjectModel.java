package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class ReceivePaymentSubjectModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;

	@ApiModelProperty(value = "收支费项编码")
    private String code;

	@ApiModelProperty(value = "NC收支费项")
    private String name;

	@ApiModelProperty(value = "科目编码")
    private String subCode;

	@ApiModelProperty(value = "科目名称")
    private String subName;

	@ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
	
	@ApiModelProperty(value = "状态(中文)")
    private String statusLabel;

	@ApiModelProperty(value = "创建人")
    private Long creater;
 
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
   
	@ApiModelProperty(value = "创建人名称")
    private String createrName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*subCode get 方法 */
    public String getSubCode(){
    return subCode;
    }
    /*subCode set 方法 */
    public void setSubCode(String  subCode){
    this.subCode=subCode;
    }
    /*subName get 方法 */
    public String getSubName(){
    return subName;
    }
    /*subName set 方法 */
    public void setSubName(String  subName){
    this.subName=subName;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receive_payment_subject_typeList, this.status) ;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }

    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
    /*createrName get 方法 */
    public String getCreaterName(){
    return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
    this.createrName=createrName;
    }






}
