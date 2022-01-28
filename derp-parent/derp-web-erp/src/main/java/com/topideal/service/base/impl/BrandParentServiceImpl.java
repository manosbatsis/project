package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.dao.base.BrandSuperiorDao;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.service.base.BrandParentService;

/**
 * 品牌父类
 * @author zhanghx
 */
@Service
public class BrandParentServiceImpl implements BrandParentService {

	@Autowired
	private BrandParentDao dao;
	// 商家
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao ;
    @Autowired
    private CommbarcodeDao commbarcodeDao ;
    @Autowired
    private BusinessUnitDao businessUnitDao ;
	@Autowired
	private BrandSuperiorDao supDao;
	
	/**
	 * 下拉
	 */
	@Override
	public List<SelectBean> getSelectBean(BrandParentModel model) throws SQLException {
		return dao.getSelectBean(model);
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BrandParentDTO listByPage(BrandParentDTO dto) throws SQLException {
		return dao.getListByPage(dto);
	}

	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override

	public boolean save(BrandParentModel model) throws SQLException, RuntimeException {
		if(StringUtils.isBlank(model.getName()) 
				|| model.getSuperiorParentBrandId() == null){
			throw new RuntimeException("缺少必填项");
		}
		//校验唯一性
		BrandParentModel brandParent = new BrandParentModel() ;
		brandParent.setName(model.getName());
		brandParent = dao.searchByModel(brandParent);
		if(brandParent != null){
			throw new RuntimeException("标准品牌名称已存在");
		}
		//上级母品牌 名称
//		String superiorParentBrand = DERP_SYS.getLabelByKey(DERP_SYS.brandParent_superiorParentBrandCodeList, model.getSuperiorParentBrandCode());		
		BrandSuperiorModel superiorModel = supDao.searchById(model.getSuperiorParentBrandId());
		if(superiorModel == null) {
			throw new RuntimeException("未匹配到上级母品牌");
		}
		model.setSuperiorParentBrandCode(superiorModel.getNcCode());
		model.setSuperiorParentBrand(superiorModel.getName());
		
		model.setModifyDate(TimeUtils.getNow());
		Long id = dao.save(model);
		if(id!=null){
			return true;
		}
		return false;
	}

	/**
	 * 编辑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override

	public boolean modify(BrandParentModel model, String oldName) throws SQLException, RuntimeException {
		if(StringUtils.isBlank(model.getName()) 				
				|| model.getSuperiorParentBrandId() == null){
			throw new RuntimeException("缺少必填项");
		}
		if(!model.getName().equals(oldName)){
			//校验唯一性
			BrandParentModel brandParent = new BrandParentModel();
			brandParent.setName(model.getName());
			brandParent = dao.searchByModel(brandParent);
			if(brandParent != null){
				throw new RuntimeException("标准品牌名称已存在");
			}
		}		
		//上级母品牌 名称
//		String superiorParentBrand = DERP_SYS.getLabelByKey(DERP_SYS.brandParent_superiorParentBrandCodeList, model.getSuperiorParentBrandCode());		
		BrandSuperiorModel superiorModel = supDao.searchById(model.getSuperiorParentBrandId());		
		if(superiorModel == null) {
			throw new RuntimeException("未匹配到上级母品牌");
		}
		model.setSuperiorParentBrandCode(superiorModel.getNcCode());
		model.setSuperiorParentBrand(superiorModel.getName());		
		model.setModifyDate(TimeUtils.getNow());
		dao.modify(model);
		return true;
	}

	/**
	 * 删除
	 * @param
	 * @return
	 * @throws SQLException
	 */
	@Override

	public boolean delete(List<Long> ids) throws SQLException, RuntimeException {
		//校验是否被关联
		for (Long id : ids) {

			
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel();
			commbarcodeModel.setCommBrandParentId(id);			
			List<CommbarcodeModel> commbarcodeList = commbarcodeDao.list(commbarcodeModel);
		}

		dao.delete(ids);
		return true;
	}

	/*@Override
	public List<SelectBean> getMerchantList(User user) {
		List<SelectBean> result = new ArrayList<SelectBean>();
        Map<String, Object> param = new HashMap<>();
        if (DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
            List<MerchantInfoMongo> merchantInfoMongos = merchantInfoMongoDao.findAll(param);
            for (MerchantInfoMongo merchantInfoMongo : merchantInfoMongos) {
                SelectBean bean = new SelectBean();
                bean.setValue(merchantInfoMongo.getMerchantId().toString());
                bean.setLabel(merchantInfoMongo.getName());
                result.add(bean);
            }
        } else {
            param.put("merchantId", user.getMerchantId());
            MerchantInfoMongo mongo = merchantInfoMongoDao.findOne(param);
            SelectBean bean = new SelectBean();
            bean.setValue(mongo.getMerchantId().toString());
            bean.setLabel(mongo.getName());
            result.add(bean);
        }
        return result;
	}*/

	@Override
	public List<BrandParentModel> list(BrandParentModel queryModel) throws SQLException {
		return dao.list(queryModel);
	}

	@Override
	public BrandParentModel detail(BrandParentModel model) throws SQLException {
		return dao.searchByModel(model);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map importBrandParent(Map<Integer, List<List<Object>>> data, User user) throws SQLException {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Integer success = 0;
		Integer pass = 0;
		Integer failure = 0;
		List<List<Object>> objects = data.get(0);
		// 存储心中的品牌
		List<BrandParentModel>brandParentList= new ArrayList<>();
		//存储是否包含相同品牌
		Map<String, String>brandParentContionMap=new HashMap<>();

		for (int j = 1; j < objects.size(); j++) {
			Map<String, String> map = this.toMap(data.get(0).get(0).toArray(), objects.get(j).toArray());
			String brandParentName = map.get("标准品牌") ;
			if(checkIsNullOrNot(j, brandParentName, "标准品牌不能为空", resultList) ) {
				failure += 1; 
				continue ;
			}
			brandParentName = brandParentName.trim();
			if (brandParentContionMap.containsKey(brandParentName)) {
				 checkIsNullOrNot(j, "", "列表中存在相同的品牌名称:"+brandParentName, resultList);
				failure += 1; 
				continue ;
			}else {
				brandParentContionMap.put(brandParentName, brandParentName);
			}
			
			
			String superiorParentBrand = map.get("上级母品牌名称") ;
			if(checkIsNullOrNot(j, superiorParentBrand, "上级母品牌名称不能为空", resultList) ) {
				failure += 1; 
				continue ;
			}
//			String superiorParentBrand = DERP_SYS.getLabelByKey(DERP_SYS.brandParent_superiorParentBrandCodeList,superiorParentBrandCode);
			BrandSuperiorModel supModel = new BrandSuperiorModel();
			supModel.setName(superiorParentBrand.trim());
			supModel = supDao.searchByModel(supModel);
			if (supModel == null) {
				setErrorMsg(j, "未匹配到上级母品牌", resultList );
				failure += 1;
				continue ;
			}


			//判断标准品牌是否重名
			BrandParentModel brandParent = new BrandParentModel();
			brandParent.setName(brandParentName);
			brandParent = dao.searchByModel(brandParent);
			if(brandParent != null){
				setErrorMsg(j, "标准品牌名称已存在", resultList );
				failure += 1; 
				continue ;
			}
						
			//存在英文名称，判断是否全英文
			String englishName = map.get("英文名称") ;
			if(StringUtils.isNotBlank(englishName)) {
				englishName = englishName.trim() ;				
				Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
				Matcher m = p.matcher(englishName);				
				if(m.find()) {
					setErrorMsg(j, "英文名称不为中文", resultList );
					failure += 1; 
					continue ;
				}
			}
			
			String  authorizer= map.get("授权方") ;
			if(StringUtils.isBlank(authorizer)){
				setErrorMsg(j, "授权方不能为空", resultList );
				failure += 1; 
				continue ;
			}
			String authorizerLabel = DERP_SYS.getLabelByKey(DERP_SYS.brandParent_authorizerList, authorizer);
			if (StringUtils.isBlank(authorizerLabel)) {
				setErrorMsg(j, "授权方输入值不正确", resultList );
				failure += 1; 
				continue ;
			}
			
			String type = map.get("分类") ;
			if(StringUtils.isBlank(type)){
				setErrorMsg(j, "分类不能为空", resultList );
				failure += 1; 
				continue ;
			}
			String typeLabel = DERP_SYS.getLabelByKey(DERP_SYS.brandParent_typeList, type);
			if (StringUtils.isBlank(typeLabel)) {
				setErrorMsg(j, "分类输入值不正确", resultList );
				failure += 1; 
				continue ;
			}
			
			brandParent = new BrandParentModel() ;
			brandParent.setCreater(user.getId());
			brandParent.setCreateDate(TimeUtils.getNow());
			brandParent.setEnglishName(englishName);
			brandParent.setModifyDate(TimeUtils.getNow());
			brandParent.setName(brandParentName);
			brandParent.setSuperiorParentBrand(supModel.getName());
			brandParent.setSuperiorParentBrandCode(supModel.getNcCode());
			brandParent.setSuperiorParentBrandId(supModel.getId());
			brandParent.setAuthorizer(authorizer);
			brandParent.setType(type);
			
			brandParentList.add(brandParent);
			
			
		}
		for (BrandParentModel brandParent : brandParentList) {
			Long brandParentId = dao.save(brandParent);
			if(brandParentId != null) {
				success += 1 ;
			}else {
				failure += 1 ;
			}
		}
		
		
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
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
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content , 
			String msg ,List<ImportErrorMessage> resultList ) {
		
		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);
			
			return true ;
			
		}else {
			return false ;
		}
		
	}
	
	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	@Override
	public List<BrandParentModel> getBrandParentByFuzzyQuery(String brandParent) throws SQLException{
		return dao.getBrandParentByFuzzyQuery(brandParent);
	}

	@Override
	public BrandParentModel getByCommbarcode(String commbarcode) throws SQLException {
		CommbarcodeModel commbarcodeModel = new CommbarcodeModel();
		commbarcodeModel.setCommbarcode(commbarcode);
		CommbarcodeModel existCommbarcode = commbarcodeDao.searchByModel(commbarcodeModel);
		if (existCommbarcode != null && existCommbarcode.getCommBrandParentId() != null) {
			BrandParentModel brandParentModel = dao.searchById(existCommbarcode.getCommBrandParentId());
			return brandParentModel;
		}
		return new BrandParentModel();
	}

	/**
	 * 导出
	 */
	@Override
	public List<BrandParentDTO> exportbrandParentList(BrandParentDTO dto) throws SQLException {
		return dao.exportbrandParentList(dto);
	}
}
