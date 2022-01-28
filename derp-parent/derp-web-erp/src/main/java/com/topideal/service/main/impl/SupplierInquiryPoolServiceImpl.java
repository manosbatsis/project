package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;
import com.topideal.service.main.SupplierInquiryPoolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * 供应商询价池service实现类
 */
@Service
public class SupplierInquiryPoolServiceImpl implements SupplierInquiryPoolService {
	@Autowired
    SupplierInquiryPoolDao sIPoolDao;
	@Autowired
    CustomerInfoDao customerInfoDao;
	@Autowired
    CustomerMerchantRelDao customerMerchantRelDao ;
	@Autowired
    MerchandiseInfoDao merchandiseInfoDao;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private CountryOriginDao countryOriginDao;
	@Autowired
	private MerchandiseCatDao merchandiseCatDao;
	//单位表
	@Autowired
	private UnitInfoDao unitInfoDao;
	@Override
	public SupplierInquiryPoolDTO listSIPool(SupplierInquiryPoolDTO dto) throws SQLException {
		return sIPoolDao.getlistByPage(dto);
	}

	@Override

	public boolean delSIPool(List ids) throws SQLException {
		int num = sIPoolDao.delete(ids);
        if(num >= 1){
            return true;
        }
        return false;
	}

	/**
	 * 导入
	 */
	@Override

	public Map importSIPool(Map<Integer, List<List<Object>>> data,Long merchantId,Long userId) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		//校验数据
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
				try{
					//必填字段的校验
					String customerCode = map.get("供应商编码");
					if(customerCode == null || "".equals(customerCode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("供应商编码不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					String customerName = map.get("供应商名称");
					if(customerName == null || "".equals(customerName)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("供应商名称不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					String unitCode = map.get("计量单位");
					if (unitCode == null || "".equals(unitCode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("计量单位不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					UnitInfoModel unitInfo = new UnitInfoModel();
					unitInfo.setCode(unitCode);
					unitInfo = unitInfoDao.searchByModel(unitInfo);
					if (unitInfo == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("找不到该计量单位");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String goodsType = map.get("商品品类");
					if (goodsType == null || "".equals(goodsType)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("商品品类不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					MerchandiseCatModel cat = new MerchandiseCatModel();
					cat.setCode(goodsType);
					cat = merchandiseCatDao.searchByModel(cat);
					if (cat == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("找不到该商品品类");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String brandName = map.get("品牌");
					if(brandName == null || "".equals(brandName)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("品牌不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					String country = map.get("原产国");
					if (country == null || "".equals(country)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("原产国不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					CountryOriginModel countryOrigin = new CountryOriginModel();
					countryOrigin.setCode(country);
					countryOrigin = countryOriginDao.searchByModel(countryOrigin);
					if (countryOrigin == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("找不到该原产国");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String goodsName = map.get("商品名称");
					if(goodsName == null || "".equals(goodsName)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("商品名称不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					String supplyPrice = map.get("供货价");
					if(supplyPrice == null || "".equals(supplyPrice)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("供货价不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					//判断供应商是否存在
					CustomerInfoModel customerInfoModel =new CustomerInfoModel();
					customerInfoModel.setCode(customerCode);
					customerInfoModel.setName(customerName);
					customerInfoModel.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
					List<CustomerInfoModel> customerInfoModels = customerInfoDao.getDetailsList(customerInfoModel);
					if(customerInfoModels == null || customerInfoModels.size()==0){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("供应商不存在");
						resultList.add(message);
						failure+=1;
						continue;
					}
					
					customerInfoModel = customerInfoModels.get(0) ;
					
					CustomerMerchantRelModel relModel = new CustomerMerchantRelModel() ;
					relModel.setMerchantId(merchantId);
					relModel.setCustomerId(customerInfoModel.getId());

					relModel = customerMerchantRelDao.searchByModel(relModel) ;
					
					//注入数据
					SupplierInquiryPoolModel model = new SupplierInquiryPoolModel();
					model.setMerchantId(merchantId);//商家ID
					model.setCustomerId(relModel.getCustomerId());//客户ID
					model.setCountryId(countryOrigin.getId());//原产国ID
					model.setCountryName(countryOrigin.getName());//原产国名称
					model.setMerchandiseCatId(cat.getId());//品类ID
					model.setMerchandiseCatName(cat.getName());//品类名称
					model.setUnitId(unitInfo.getId());//计量单位ID
					model.setUnit(unitInfo.getName());//计量单位名称
					model.setModifier(userId);
					//获取品牌
					BrandModel brandModel = new BrandModel();
					brandModel.setName(brandName);
					brandModel = brandDao.searchByModel(brandModel);
					if(brandModel == null){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("品牌不存在");
						resultList.add(message);
						failure+=1;
						continue;
					}
					model.setBrandId(brandModel.getId());
					model.setBrandName(brandName);
					if(map.get("最小起订量")!= null && StringUtils.isNotBlank(map.get("最小起订量"))){
						model.setMinimum(Integer.valueOf(map.get("最小起订量")));
					}
					if(map.get("最大供货量")!= null && StringUtils.isNotBlank(map.get("最大供货量"))){
						model.setMaximum(Integer.valueOf(map.get("最大供货量")));
					}
					if(map.get("规格型号")!= null && StringUtils.isNotBlank(map.get("规格型号"))){
						model.setSpec(map.get("规格型号"));
					}
					if(map.get("计量单位")!= null && StringUtils.isNotBlank(map.get("计量单位"))){
						model.setUnit(map.get("计量单位"));
					}
					model.setCreateDate(TimeUtils.getNow());
					model.setCreater(userId);
					model.setCustomerId(customerInfoModel.getId());
					model.setCustomerCode(customerCode);
					model.setCustomerName(customerName);
					model.setGoodsName(goodsName);
					model.setSupplyPrice(new BigDecimal(supplyPrice));
					sIPoolDao.save(model);
					success+=1;
				}catch(SQLException e){
					e.printStackTrace();
					failure+=1;
				}
			}
		}
		Map map =new HashMap();
		map.put("success",success);
		map.put("pass",pass);
		map.put("failure",failure);
		map.put("message",resultList);
		return map;
	}

	@Override
	public SupplierInquiryPoolDTO searchDetail(Long id) throws SQLException {
		return  sIPoolDao.getDetails(id);
	}

	@Override

	public boolean modifySIPool(SupplierInquiryPoolModel model,Long userId) throws SQLException {
		//获取计量单位
		UnitInfoModel unitInfoModel = unitInfoDao.searchById(model.getUnitId());
		model.setUnit(unitInfoModel.getName());
		//获取品牌
		BrandModel brandModel = new BrandModel();
		brandModel.setId(model.getBrandId());
		brandModel = brandDao.searchByModel(brandModel);
		model.setBrandName(brandModel.getName());
		//获取原产国
		CountryOriginModel countryOriginModel = new CountryOriginModel();
		countryOriginModel.setId(model.getCountryId());
		countryOriginModel = countryOriginDao.searchByModel(countryOriginModel);
		model.setCountryName(countryOriginModel.getName());
		//获取商品品类
		MerchandiseCatModel merchandiseCatModel = new MerchandiseCatModel();
		merchandiseCatModel.setId(model.getMerchandiseCatId());
		merchandiseCatModel = merchandiseCatDao.searchByModel(merchandiseCatModel);
		model.setMerchandiseCatName(merchandiseCatModel.getName());
		model.setModifier(userId);
		model.setModifyDate(TimeUtils.getNow());
		int num = sIPoolDao.modify(model);
        if(num >= 1){
            return true;
        }
        return false;
	}
	/**
     * 把两个数组组成一个map
     * @param keys   键数组
     * @param values 值数组
     * @return 键值对应的map
     */
    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null && keys.length == values.length) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }

    /**
     * 导出供应商询价池
     */
	@Override
	public List<SupplierInquiryPoolModel> exportList(SupplierInquiryPoolModel model) throws SQLException {
		return sIPoolDao.exportList(model);
	}
}
