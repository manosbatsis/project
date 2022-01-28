package com.topideal.entity.vo.base;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class BrandParentModel extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 品牌名称
    */
	@ApiModelProperty(value = "品牌名称")
    private String name;
    /**
    * 英文名称
    */
	@ApiModelProperty(value = "英文名称")
    private String englishName;
    /**
    * 母品牌名称
    */
	@ApiModelProperty(value = "母品牌名称")
    private String superiorParentBrand;
    /**
    * 母品牌NC编码
    */
	@ApiModelProperty(value = "母品牌NC编码")
    private String superiorParentBrandCode;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 修改人
    */
	@ApiModelProperty(value = "修改人")
    private Long modifier;
    /**
     * 上级母品牌id
     */
	@ApiModelProperty(value = "上级母品牌id")
    private Long superiorParentBrandId;
    /**
     * 授权方 1-品牌方，2-经销商，3-无授权
     */
	@ApiModelProperty(value = "授权方 1-品牌方，2-经销商，3-无授权")
    private String authorizer;
    /**
     * 分类 1-跨境电商，2-内贸
     */
	@ApiModelProperty(value = "分类 1-跨境电商，2-内贸")
    private String type;

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
    /*englishName get 方法 */
    public String getEnglishName(){
    return englishName;
    }
    /*englishName set 方法 */
    public void setEnglishName(String  englishName){
    this.englishName=englishName;
    }
    /*superiorParentBrand get 方法 */
    public String getSuperiorParentBrand(){
    return superiorParentBrand;
    }
    /*superiorParentBrand set 方法 */
    public void setSuperiorParentBrand(String  superiorParentBrand){
    this.superiorParentBrand=superiorParentBrand;
    }
    /*superiorParentBrandCode get 方法 */
    public String getSuperiorParentBrandCode(){
    return superiorParentBrandCode;
    }
    /*superiorParentBrandCode set 方法 */
    public void setSuperiorParentBrandCode(String  superiorParentBrandCode){
    this.superiorParentBrandCode=superiorParentBrandCode;
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

    public Long getSuperiorParentBrandId() {
        return superiorParentBrandId;
    }

    public void setSuperiorParentBrandId(Long superiorParentBrandId) {
        this.superiorParentBrandId = superiorParentBrandId;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
