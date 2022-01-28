package com.topideal.mongo.entity;

import java.sql.Timestamp;

/**
 * 原产国
 * @author lian_
 */
public class CountryOriginMongo {
	 //名称
    private String name;
    //编码
    private String code;
    //原产国id
    private Long countryOriginId;
    //创建人
    private Long creater;



    public Long getCountryOriginId() {
		return countryOriginId;
	}
	public void setCountryOriginId(Long countryOriginId) {
		this.countryOriginId = countryOriginId;
	}
/*creater set 方法 */
   public Long getCreater() {
		return creater;
	}
   /*creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/*name get 方法 */
   public String getName(){
       return name;
   }
   /*name set 方法 */
   public void setName(String  name){
       this.name=name;
   }
   /*code get 方法 */
   public String getCode(){
       return code;
   }
   /*code set 方法 */
   public void setCode(String  code){
       this.code=code;
   }
}
