package com.topideal.service.base.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.base.OperateSysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌表 serviceImpl
 * 
 * @author zhanghx
 */
@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;
	@Autowired
	private BrandParentDao brandParentDao;

	@Autowired
	private OperateSysLogService operateSysLogService;

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return brandDao.getSelectBean();
	}

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBeanByMerchant(Map<String, Object> paramMap) throws SQLException {
		return brandDao.getSelectBeanByMerchant(paramMap);
	}

	/**
	 * 分页
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public BrandDTO listByPage(BrandDTO dto) throws SQLException {
		return brandDao.getListByPage(dto);
	}

	/**
	 * 获取品牌信息以及匹配的品牌信息
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BrandModel getDetails(Long id) throws SQLException {
		BrandModel model = brandDao.searchById(id);
		/*List<BrandModel> brandList = brandDao.getListByParentId(id);
		model.setBrandList(brandList);*/
		return model;
	}

	@Override
	public int modify(BrandModel model,User user) throws SQLException {
		model.setOperator(user.getId());
		model.setOperatorName(user.getName());
		model.setOperationDate(TimeUtils.getNow());
		model.setModifyDate(TimeUtils.getNow());
		if (model.getParentId() != null) {
			BrandParentModel brandParentModel = brandParentDao.searchById(model.getParentId());
			model.setParentName(brandParentModel.getName());
		}

		// 记录操作日志
		operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_5, model.getId(), "编辑标准品牌", null, null);
		return brandDao.modify(model);
	}

	/**
	 * 查询下拉列表，去除已经匹配的
	 * 
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public List<SelectBean> getSelectBeanByNoMatching() throws SQLException {
		return brandDao.getSelectBeanByNoMatching();
	}*/

	/**
	 * 弹窗 分页
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public BrandModel getBrandByPage(BrandModel model) throws SQLException {
		return brandDao.getBrandByPage(model);
	}*/

	/**
	 * 根据ids获取信息
	 * 
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<BrandModel> getListByIds(List list) throws SQLException {
		return brandDao.getListByIds(list);
	}

	@Override
	public List<Map<String, Object>> listForExport(BrandModel model) throws SQLException {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<BrandModel> brandModels = brandDao.list(model);
		for (BrandModel brandModel : brandModels) {
			Map<String, Object> brandMap = new HashMap<>();
			brandMap.put("name", brandModel.getName());
			brandMap.put("chinaBrandName", brandModel.getChinaBrandName());
			brandMap.put("englishBrandName", brandModel.getEnglishBrandName());
			brandMap.put("parentName", brandModel.getParentName());
			brandMap.put("operatorName", brandModel.getOperatorName());
			brandMap.put("modifyDate", brandModel.getModifyDate());
			mapList.add(brandMap);
		}
		return mapList;
	}

	@Override
	public ResponseBean save(User user,BrandModel model) throws SQLException {
		if(model.getParentId() != null) {
			BrandParentModel brandParentModel = brandParentDao.searchById(model.getParentId());
			model.setParentName(brandParentModel.getName());
		}
		if(StringUtils.isNotBlank(model.getName())) {
			BrandModel model1 = new BrandModel();
			model1.setName(model.getName());
			int count = brandDao.count(model1);
			if(count > 0) {
				return WebResponseFactory.responseBuild("10012", "该品牌已存在");
			}
		}

		Long id = brandDao.save(model);

		// 记录日志
		operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_5, id, "新增", null, null);
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 保存匹配
	 * 
	 * @param model
	 * @param brandIds
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	/*@Override

	public boolean saveMatching(BrandModel model, String[] brandIds, Long userId)
			throws SQLException, RuntimeException {
		for (int i = 0; i < brandIds.length; i++) {
			BrandModel brand = new BrandModel();
			// 修改
			brand.setId(Long.parseLong(brandIds[i]));
			brand.setIsMatching("1");// 是否匹配 1-是
			brand.setParentId(model.getParentId());// 父类品牌id
			BrandParentModel parentBrand = brandParentDao.searchById(model.getParentId());
			brand.setParentName(parentBrand.getName());// 父类品牌名称
			brand.setOperationDate(TimeUtils.getNow());// 匹配时间
			brand.setOperator(userId);// 操作人
			brandDao.modify(brand);
		}
		return true;
	}*/

	/**
	 * 保存解绑
	 * 
	 * @param model
	 * @param brandIds
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	/*@Override

	public boolean saveUnMatching(List<Long> ids, Long userId) throws SQLException, RuntimeException {
		// 解绑
		for (Long id : ids) {
			BrandModel brand = new BrandModel();
			brand.setId(id);// 品牌id
			brand.setModifyDate(TimeUtils.getNow());// 修改时间
			brandDao.updateUnMatching(brand);
		}
		return true;
	}*/

}
