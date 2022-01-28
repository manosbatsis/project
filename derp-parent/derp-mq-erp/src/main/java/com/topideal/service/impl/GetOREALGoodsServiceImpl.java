package com.topideal.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.oreal.OrealUtils;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.dao.main.SupplierMerchandiseDao;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.SupplierMerchandiseModel;
import com.topideal.service.GetOREALGoodsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取欧莱雅供应商商品
 * @author 杨创
 *
 */
@Service
public class GetOREALGoodsServiceImpl implements GetOREALGoodsService {

	private static final Logger LOGGER= LoggerFactory.getLogger(GetOREALGoodsServiceImpl.class);

	@Autowired
	private SupplierMerchandiseDao supplierMerchandiseDao;
	@Autowired
	private BusinessUnitDao businessUnitDao;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700004,model=DERP_LOG_POINT.POINT_13201700004_Label)
	public void saveOREALGoods(String json) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		LOGGER.info("----------获取欧莱雅供应商商品--------开始"+json);
		//获取宝信事业部
		BusinessUnitModel businessUnit=new BusinessUnitModel();
		businessUnit.setCode("E070200");
		businessUnit = businessUnitDao.searchByModel(businessUnit);
		if (businessUnit==null) {
			throw new RuntimeException("没有查询到事业部");
		}		
		
		// 获取欧莱雅接口Token
		String token = OrealUtils.getOREALToken();
		// 获取请求xml字符串
		String xml=getXmlStr();
		JSONArray orealGoods = OrealUtils.getOREALGoods(xml, token);		
		if (orealGoods==null) {
			throw new RuntimeException("获取欧莱雅商返回结果为空");
		}
				
		List<SupplierMerchandiseModel> goodsList=new ArrayList<>();
		for (Object object : orealGoods) {
			JSONObject jSONObject=(JSONObject) object;
			//产品编码
			if (jSONObject.get("invcode")==null||StringUtils.isBlank(jSONObject.getString("invcode"))||"null".equals(jSONObject.getString("invcode"))) {
				continue;
			}
			//产品编码
			String invcode=jSONObject.getString("invcode");
			//品牌编码
			String brandcode=null;
			if (jSONObject.get("brandcode")!=null&&StringUtils.isNotBlank(jSONObject.getString("brandcode"))&&!"null".equals(jSONObject.getString("brandcode"))) {
				brandcode=jSONObject.getString("brandcode");
			}
			//品牌名称
			String brandname=null;
			if (jSONObject.get("brandname")!=null&&StringUtils.isNotBlank(jSONObject.getString("brandname"))&&!"null".equals(jSONObject.getString("brandname"))) {
				brandname=jSONObject.getString("brandname");
			}
			//经销商货号
			String cinvmnecode=null;
			if (jSONObject.get("cinvmnecode")!=null&&StringUtils.isNotBlank(jSONObject.getString("cinvmnecode"))&&!"null".equals(jSONObject.getString("cinvmnecode"))) {
				cinvmnecode=jSONObject.getString("cinvmnecode");
			}
			//产品条码
			String invbarcode=null;
			if (jSONObject.get("invbarcode")!=null&&StringUtils.isNotBlank(jSONObject.getString("invbarcode"))&&!"null".equals(jSONObject.getString("invbarcode"))) {
				invbarcode=jSONObject.getString("invbarcode");
			}
			//产品名称
			String invmane=null;
			if (jSONObject.get("invmane")!=null&&StringUtils.isNotBlank(jSONObject.getString("invmane"))&&!"null".equals(jSONObject.getString("invmane"))) {
				invmane=jSONObject.getString("invmane");
			}
			//建议零售价
			BigDecimal refsaleprice=null;
			if (jSONObject.get("refsaleprice")!=null&&StringUtils.isNotBlank(jSONObject.getString("refsaleprice"))&&!"null".equals(jSONObject.getString("refsaleprice"))) {				
				refsaleprice=new BigDecimal(jSONObject.getString("refsaleprice"));
			}
			//产品类型
			String def3=null;
			if (jSONObject.get("def3")!=null&&StringUtils.isNotBlank(jSONObject.getString("def3"))&&!"null".equals(jSONObject.getString("def3"))) {
				def3=jSONObject.getString("def3");
			}
			
			
			
			
			
			//Object object2 = jSONObject.get("invcode");
			
			SupplierMerchandiseModel goods=new SupplierMerchandiseModel();
			goods.setGoodsCode(invcode);
			goods.setName(invmane);
			goods.setBrandName(brandname);
			goods.setBrandCode(brandcode);
			goods.setBarcode(invbarcode);
			goods.setGoodsType(def3);
			goods.setSalePrice(refsaleprice);
			goods.setSupplierGoodsNo(cinvmnecode);
			goods.setBuId(businessUnit.getId());
			goods.setBuName(businessUnit.getName());
			goods.setSource("1");			
			goodsList.add(goods);			
		}
		//
		for (SupplierMerchandiseModel model : goodsList) {
			SupplierMerchandiseModel query=new SupplierMerchandiseModel();
			query.setGoodsCode(model.getGoodsCode());
			query = supplierMerchandiseDao.searchByModel(query);
			if (query!=null) {
				model.setId(query.getId());
				model.setModifyDate(TimeUtils.getNow());
				supplierMerchandiseDao.modify(model);
			}else {
				model.setCreateDate(TimeUtils.getNow());
				supplierMerchandiseDao.save(model);
			}
		}
		
	}
	/**
	 * 获取请求xml字符串
	 * @return
	 */
	private String getXmlStr() {
		long time = TimeUtils.getNow().getTime();
		String requesttime = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
		String xml="<ufinterface account=\"05\" billtype=\"disQuery\" filename=\"\" isexchange=\"Y\" proc=\"add\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"DIS\" subbilltype=\"\">";
		xml=xml+"<bill id=\""+time+"\">";
		xml=xml+"<billhead>"
				+ "<billid>"+time+"</billid>"
				+ "<userid>"+13942001+"</userid>"
				+ "<pk_corp>"+1735+"</pk_corp>"
				+ "<requesttime>"+requesttime+"</requesttime>"
				+ "<sqlcode>"+13942001+"</sqlcode>"
				+"</billhead>"
				+"<billbody>"
				+"<entry>"
				+"<code>"+1394200101+"</code>"  
				+"<field>"+"bodycode"+"</field>" 
				+"<table>"+"bd_calbody"+"</table>" 
				+"<condition>"+"in"+"</condition>" 
				+"<value>"+"19"+"</value>"
				+"</entry>"
				+"</billbody>"
				+"</bill>"
				+"</ufinterface>";		
		return xml;
	}



}
