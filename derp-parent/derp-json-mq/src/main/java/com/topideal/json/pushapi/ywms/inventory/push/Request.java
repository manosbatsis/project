package com.topideal.json.pushapi.ywms.inventory.push;

/**
 * 	众邦云仓库存查询接口json--请求信息封装
 * 	XML 根节点 request
 * @author Guobs
 *
 */

public class Request {
	
	private CriteriaList criteriaList ;

	public CriteriaList getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(CriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}
    
    
}
