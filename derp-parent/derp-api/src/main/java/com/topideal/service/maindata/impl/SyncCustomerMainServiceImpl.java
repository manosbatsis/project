package com.topideal.service.maindata.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerMainDao;
import com.topideal.entity.vo.main.CustomerMainModel;
import com.topideal.service.maindata.SyncCustomerMainService;

import net.sf.json.JSONObject;

/**
 * （主数据新系统）客商信息下发接口2.0
 * 说明: 主数据接口 :4.13 企业信息推送接口
 * @author 杨创
 */
@Service
public class SyncCustomerMainServiceImpl implements SyncCustomerMainService{
	
	@Autowired
	private CustomerMainDao customerMainDao;
	
	/**
	 * 新增或者修改主数据客商详情
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean synsCustomerMain(JSONObject jsonData) throws Exception {		       
        String code = getJsonStrOrNull(jsonData, "code") ;       
        //查询是否存在，有则修改，无则新增
        CustomerMainModel queryModel = new CustomerMainModel() ;
        queryModel.setCcode(code);       
        queryModel = customerMainDao.searchByModel(queryModel) ;
        
        //封装实体
        CustomerMainModel tempModel= getCustomerMain(jsonData);
               
        if(queryModel == null) {
        	tempModel.setCcode(code);
        	tempModel.setIsCustomer(DERP_SYS.CUSTOMER_MAIN_IS_0);
        	tempModel.setIsSupplier(DERP_SYS.CUSTOMER_MAIN_IS_0);
        	tempModel.setCreateDate(TimeUtils.getNow());        	
        	customerMainDao.save(tempModel) ;
        }else {
        	tempModel.setId(queryModel.getId());
        	tempModel.setModifyDate(TimeUtils.getNow());       	
        	customerMainDao.modify(tempModel) ;
        }
        
        
		return true;
	}
	
	private CustomerMainModel getCustomerMain(JSONObject jsonData) {       
        CustomerMainModel tempModel = new CustomerMainModel() ;
        //tempModel.setCcode(getJsonStrOrNull(jsonData, "code"));//客户ID
        tempModel.setCname(getJsonStrOrNull(jsonData, "name"));  //客户名称  
        tempModel.setCcodetypes(getJsonStrOrNull(jsonData, "clientType"));   //客商类型  
        tempModel.setCshortname(getJsonStrOrNull(jsonData, "shortName"));//客户简称
        tempModel.setCmprop(getJsonStrOrNull(jsonData, "merchantNature"));//企业性质
        tempModel.setRegisteredaddr(getJsonStrOrNull(jsonData, "registeredAddress"));//注册地
        tempModel.setAddr(getJsonStrOrNull(jsonData, "address"));//企业地址       
        tempModel.setBusilicenseno(getJsonStrOrNull(jsonData, "idCard"));//营业执照号
        tempModel.setIdType(getJsonStrOrNull(jsonData, "idType"));//证件类型 枚举: Y,Z,G,H,J,S 枚举备注: Y 营业执照 Z 注册登记证 G 个人身份证 H 护照 J 机构代码 S 社会团体法人登记证书
        tempModel.setMainStatus(getJsonStrOrNull(jsonData, "status"));// 客户状态 枚举: 1,0 枚举备注: 1：有效/0：锁定，默认有效
        String registeredDate = getJsonStrOrNull(jsonData, "registeredDate");
        tempModel.setRegisteredDate(TimeUtils.parse(registeredDate,"yyyy-MM-dd HH:mm:ss"));// 主数时间        
        tempModel.setMainSource(getJsonStrOrNull(jsonData, "source"));// 主数据数据来源 
        tempModel.setTenantCode(getJsonStrOrNull(jsonData, "tenantCode"));//租户编码
        tempModel.setMainType(getJsonStrOrNull(jsonData, "type"));//客户类型(客户角色) 电商企业\\仓储企业\\物流公司\\电商平台\\支付企业\\监管场所经营人\\报关企业\\委托单位\\账册主体\\资金方\\客户\\供应商;可多选考虑用置位方式       
        tempModel.setRemark(getJsonStrOrNull(jsonData, "remark"));//备注
        tempModel.setVersion(getJsonStrOrNull(jsonData, "version"));
        
		return tempModel;
	}

	//判断是否为空返回值
	private String getJsonStrOrNull(JSONObject jsonData, String key) {
		if(jsonData.get(key)== null||StringUtils.isBlank(jsonData.getString(key))||"null".equals(jsonData.getString(key))) {
			return null;
		}		
		return jsonData.getString(key) ;
	}

}
