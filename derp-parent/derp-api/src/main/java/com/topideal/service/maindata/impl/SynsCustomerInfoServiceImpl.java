package com.topideal.service.maindata.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerMainDao;
import com.topideal.entity.vo.main.CustomerMainModel;
import com.topideal.service.maindata.SynsCustomerInfoService;

import net.sf.json.JSONObject;

/**
 * 主服务同步客商信息
 * @author gy
 *
 */
@Service
public class SynsCustomerInfoServiceImpl implements SynsCustomerInfoService{
	
	@Autowired
	private CustomerMainDao customerMainDao;
	

	@Override
	public boolean synsCustomerInfo(String json) throws Exception {
		
		//json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        
        String ccode = getJsonStrOrNull(jsonData, "ccode") ;
        String cname = getJsonStrOrNull(jsonData, "cname") ;
        String cShortname = getJsonStrOrNull(jsonData, "cShortname") ;
        String ccodeTypes = getJsonStrOrNull(jsonData, "ccodeTypes") ;
        String ccodeGrade = getJsonStrOrNull(jsonData, "ccodeGrade") ;
        String cnameEn = getJsonStrOrNull(jsonData, "cnameEn") ;
        String cShortnameEn = getJsonStrOrNull(jsonData, "cShortnameEn") ;
        String registeredAddr = getJsonStrOrNull(jsonData, "registeredAddr") ;
        String ccodeThk = getJsonStrOrNull(jsonData, "ccodeThk") ;
        String cmprop = getJsonStrOrNull(jsonData, "cmprop") ;
        String businessScope = getJsonStrOrNull(jsonData, "businessScope") ;
        String citycode = getJsonStrOrNull(jsonData, "citycode") ;
        String addr = getJsonStrOrNull(jsonData, "addr") ;
        String addrEn = getJsonStrOrNull(jsonData, "addrEn") ;
        String busiLicenseNo = getJsonStrOrNull(jsonData, "busiLicenseNo") ;
        String oTelNo = getJsonStrOrNull(jsonData, "oTelNo") ;
        String linkman = getJsonStrOrNull(jsonData, "linkman") ;
        String linkTel = getJsonStrOrNull(jsonData, "linkTel") ;
        String depositBank = getJsonStrOrNull(jsonData, "depositBank") ;
        String cnyAccount = getJsonStrOrNull(jsonData, "cnyAccount") ;
        String usdAccount = getJsonStrOrNull(jsonData, "usdAccount") ;
        String lgRepresentative = getJsonStrOrNull(jsonData, "lgRepresentative") ;
        String lgrNcode = getJsonStrOrNull(jsonData, "lgrNcode") ;
        String lgrId = getJsonStrOrNull(jsonData, "lgrId") ;
        String lgrTel = getJsonStrOrNull(jsonData, "lgrTel") ;
        String fax = getJsonStrOrNull(jsonData, "fax") ;
        String email = getJsonStrOrNull(jsonData, "email") ;
        String remark = getJsonStrOrNull(jsonData, "remark") ;
        
        //查询是否存在，有则修改，无则新增
        CustomerMainModel queryModel = new CustomerMainModel() ;
        queryModel.setCcode(ccode);
        
        queryModel = customerMainDao.searchByModel(queryModel) ;
        
        
        CustomerMainModel tempModel = new CustomerMainModel() ;
        tempModel.setCname(cname);
        tempModel.setCshortname(cShortname);
        tempModel.setAddr(addr);
        tempModel.setAddren(addrEn);
        tempModel.setBusilicenseno(busiLicenseNo);
        tempModel.setBusinessscope(businessScope);
        tempModel.setCcode(ccode);
        tempModel.setCcodegrade(ccodeGrade);
        tempModel.setCcodethk(ccodeThk);
        tempModel.setCcodetypes(ccodeTypes);
        tempModel.setCcodetypes(ccodeTypes);
        tempModel.setCitycode(citycode);
        tempModel.setCnameen(cnameEn);
        tempModel.setCshortnameen(cShortnameEn);
        tempModel.setRegisteredaddr(registeredAddr);
        tempModel.setCmprop(cmprop);
        tempModel.setOtelno(oTelNo);
        tempModel.setLinkman(linkman);
        tempModel.setLinktel(linkTel);
        tempModel.setDepositbank(depositBank);
        tempModel.setCnyaccount(cnyAccount);
        tempModel.setUsdaccount(usdAccount);
        tempModel.setLgrepresentative(lgRepresentative);
        tempModel.setLgrncode(lgrNcode);
        tempModel.setLgrid(lgrId);
        tempModel.setLgrtel(lgrTel);
        tempModel.setFax(fax);
        tempModel.setEmail(email);
        tempModel.setRemark(remark);
        
        if(queryModel == null) {
        	tempModel.setCcode(ccode);
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
	
	private String getJsonStrOrNull(JSONObject jsonData, String key) {
		if(jsonData.get(key) != null) {
			return jsonData.getString(key) ;
		}
		
		return null ;
	}

}
