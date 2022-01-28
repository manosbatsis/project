package com.topideal.mongo.entity;

public class BrandSuperiorMongo{

    /**
    * id
    */
    private Long brandSuperiorId;
    /**
    * 品牌名称
    */
    private String name;
    /**
    * NC编码
    */
    private String ncCode;

    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createrName;
    
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改人名称
    */
    private String modifierName;
    /**
     * 申报系数
     */
    private Double priceDeclareRatio;

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
	public Long getBrandSuperiorId() {
		return brandSuperiorId;
	}
	public void setBrandSuperiorId(Long brandSuperiorId) {
		this.brandSuperiorId = brandSuperiorId;
	}
	public Double getPriceDeclareRatio() {
		return priceDeclareRatio;
	}
	public void setPriceDeclareRatio(Double priceDeclareRatio) {
		this.priceDeclareRatio = priceDeclareRatio;
	}

}
