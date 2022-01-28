package com.topideal.entity.vo.system;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class MerchantRelModel extends PageModel implements Serializable {

	// 代理商家
	private Long proxyMerchantId;
	// ID
	private Long id;
	// 商家ID
	private Long merchantId;
    //修改时间
    private Timestamp modifyDate;
    //创建时间
    private Timestamp createDate;
    
    //拓展字段
	// 商家名称
	private String name;
	// 商家编码
	private String code;

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/* proxyMerchantId get 方法 */
	public Long getProxyMerchantId() {
		return proxyMerchantId;
	}

	/* proxyMerchantId set 方法 */
	public void setProxyMerchantId(Long proxyMerchantId) {
		this.proxyMerchantId = proxyMerchantId;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

}
