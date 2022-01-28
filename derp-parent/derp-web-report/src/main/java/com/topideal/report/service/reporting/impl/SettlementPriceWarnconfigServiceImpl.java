package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.SettlementPriceWarnconfigDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SettlementPriceWarnconfigService;

/**
 * 标准成本单价预警配置信息service实现类
 */
@Service
public class SettlementPriceWarnconfigServiceImpl implements SettlementPriceWarnconfigService {

	// 标准成本单价预警配置信息dao
	@Autowired
	private SettlementPriceWarnconfigDao settlementPriceWarnconfigDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private BusinessUnitDao businessUnitDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	/**
	 * 标准成本单价预警配置信息（分页）
	 * @param model 
	 * @return
	 */
	@Override
	public SettlementPriceWarnconfigDTO listEmail(User user,SettlementPriceWarnconfigDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return settlementPriceWarnconfigDao.getListByPage(dto);
	}
	/**
	 * 新增标准成本单价预警配置
	 * @param model
	 * @return
	 */
	@Override

	public Map<String, Object> saveEmail(SettlementPriceWarnconfigModel model,User user) throws Exception {
		Map<String, Object> retMap=new HashMap<>();
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(model.getMerchantId());
		BusinessUnitModel businessUnit = businessUnitDao.searchById(model.getBuId());
		// 存储信息
		if(merchantInfo != null) {
			model.setMerchantName(merchantInfo.getName());
		}
		if(businessUnit != null) {
			model.setBuName(businessUnit.getName());
		}
		model.setStatus(DERP_SYS.EMAILCONFIG_STATUS_1);//'状态(1启用,0禁用)  新增默认启用
		model.setCreater(user.getId());
		model.setCreateName(user.getName());
		model.setCreateDate(TimeUtils.getNow());
		model.setModifier(user.getId());
		model.setModifierUsername(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		Long save = settlementPriceWarnconfigDao.save(model);
		if(save>0){
			retMap.put("code", "00");
			retMap.put("message", "保存成功");
		}else{
			retMap.put("code", "01");
			retMap.put("message", "保存失败");
		}
		return retMap;
	}
	/**
	 * 根据id删除标准成本单价预警配置(支持批量)
	 * @param ids
	 * @return
	 */
	@Override

	public boolean delEmail(List<Long> ids) throws SQLException {
		for(Long id:ids){
			SettlementPriceWarnconfigModel model = settlementPriceWarnconfigDao.searchById(id);
			// 仅对禁用状态的配置项可操作删除
			if(DERP_REPORT.SETTLEMENTPRICEWARNCONFIG_STATUS_1.equals(model.getStatus())){
				throw new RuntimeException("请选择启用单价预警"); 
			}
		}
		int num = settlementPriceWarnconfigDao.delete(ids);
		if (num>0) {
			return true;
		}
		return false;
	}
	/**
	 * 修改标准成本单价预警配置
	 * @param model
	 * @return
	 */
	@Override

	public Map<String, Object> modifyEmail(SettlementPriceWarnconfigModel model,User user) throws Exception {
		Map<String, Object> retMap=new HashMap<>();
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(model.getMerchantId());
		BusinessUnitModel businessUnit = businessUnitDao.searchById(model.getBuId());
		// 存储信息
		if(merchantInfo != null) {
			model.setMerchantName(merchantInfo.getName());
		}
		if(businessUnit != null) {
			model.setBuName(businessUnit.getName());
		}
		model.setModifier(user.getId());
		model.setModifierUsername(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		int modify = settlementPriceWarnconfigDao.modify(model);	
		if(modify>0){
			retMap.put("code", "00");
			retMap.put("message", "保存成功");
		}else{
			retMap.put("code", "01");
			retMap.put("message", "保存失败");
		}
		return retMap;
	}
	/**
	 * 根据id获取标准成本单价预警配置详情
	 * @param id
	 * @return
	 */
	@Override
	public SettlementPriceWarnconfigDTO searchDetail(Long id) throws SQLException {
		SettlementPriceWarnconfigDTO model = settlementPriceWarnconfigDao.searchDTOById(id);
		return model;
	}
	
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 */
	@Override

	public boolean audit(Long id ,String status,User user) throws SQLException {
		SettlementPriceWarnconfigModel model =new SettlementPriceWarnconfigModel();
		model.setId(id);
		model.setStatus(status);
		model.setModifier(user.getId());
		model.setModifierUsername(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		int num = settlementPriceWarnconfigDao.modify(model);
		if (num>0) {
			return true;
		}
		return false;
	}

	
	
}
