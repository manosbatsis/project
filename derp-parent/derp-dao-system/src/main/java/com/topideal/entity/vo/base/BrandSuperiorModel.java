package com.topideal.entity.vo.base;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class BrandSuperiorModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "品牌名称")
    private String name;
	@ApiModelProperty(value = "NC编码")
    private String ncCode;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "创建人")
    private Long creater;
	@ApiModelProperty(value = "创建人名称")
    private String createrName;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "修改人")
    private Long modifier;
	@ApiModelProperty(value = "修改人名称")
    private String modifierName;
	@ApiModelProperty(value = "申报系数")
    private Double priceDeclareRatio;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*ncCode get 方法 */
    public String getNcCode(){
    return ncCode;
    }
    /*ncCode set 方法 */
    public void setNcCode(String  ncCode){
    this.ncCode=ncCode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createrName get 方法 */
    public String getCreaterName(){
    return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
    this.createrName=createrName;
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

    public Double getPriceDeclareRatio() {
        return priceDeclareRatio;
    }

    public void setPriceDeclareRatio(Double priceDeclareRatio) {
        this.priceDeclareRatio = priceDeclareRatio;
    }
}
