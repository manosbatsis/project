package com.topideal.entity.vo.base;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 原产国
 * @author lchenxing
 *
 */
public class CountryOriginModel extends PageModel implements Serializable{


     //名称
     private String name;
     //编码
     private String code;
     //ID
     private Long id;
     //创建人
     private Long creater;
     //创建时间
     private Timestamp createDate;



     /*creater set 方法 */
    public Long getCreater() {
		return creater;
	}
    /*creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/*createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}
	/*createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }






}

