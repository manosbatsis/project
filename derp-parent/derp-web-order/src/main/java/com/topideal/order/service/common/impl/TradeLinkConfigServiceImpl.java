package com.topideal.order.service.common.impl;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.TradeLinkConfigDao;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.order.service.common.TradeLinkConfigService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;


/**
 * 交易链路配置
 */
@Service
public class TradeLinkConfigServiceImpl implements TradeLinkConfigService {

	@Autowired
	private TradeLinkConfigDao tradeLinkConfigDao;

	/**
	 * 分页查询
	 */
	@Override
	public TradeLinkConfigDTO getTradeLinkConfigListByPage(TradeLinkConfigDTO dto) throws SQLException {
		TradeLinkConfigDTO tradeLinkConfigDTO = tradeLinkConfigDao.getTradeLinkConfigListByPage(dto);
		return tradeLinkConfigDTO;
	}

	/**
	 * 删除交易链路
	 * @param ids
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public boolean delTradeLinkConfig(Long ids) throws SQLException, RuntimeException {
		//判断该交易链路是否存在
		TradeLinkConfigModel tradeLinkConfigModel = tradeLinkConfigDao.searchById(ids);
		if(tradeLinkConfigModel==null){
			throw new DerpException("删除失败，该交易链路不存在");
		}
		int num = tradeLinkConfigDao.delete(Arrays.asList(ids));
		if(num >= 1){
			return true;
		}
		return false;
	}

	@Override
	public String saveTradeLinkConfig(String json, User user) throws SQLException {
		//解析json
		
		JSONObject jsonObj = JSONObject.fromObject(json);
		TradeLinkConfigModel saveModel = (TradeLinkConfigModel) JSONObject.toBean(jsonObj, TradeLinkConfigModel.class) ;
		
		boolean isNull = new EmptyCheckUtils().addObject(saveModel.getStartPointBuId())
				.addObject(saveModel.getStartPointBuName()).addObject(saveModel.getStartPointMerchantId())
				.addObject(saveModel.getStartPointMerchantName()).addObject(saveModel.getStartPointPremiumRate())
				.addObject(saveModel.getStartSupplierId()).addObject(saveModel.getStartSupplierName())
				.addObject(saveModel.getLinkName()).empty();
		
		if(isNull) {
			throw new DerpException("起点公司信息不全");
		}
		
		TradeLinkConfigModel searchConfigModel = new TradeLinkConfigModel();
		searchConfigModel.setLinkName(saveModel.getLinkName());
		searchConfigModel = tradeLinkConfigDao.searchByModel(searchConfigModel);
		if(searchConfigModel!=null){
			throw new DerpException("链路名称不能重复");
		}

		saveModel.setCreateDate(TimeUtils.getNow());
		saveModel.setCreater(user.getId());
		saveModel.setCreateName(user.getName());
		
		tradeLinkConfigDao.save(saveModel);
		
		return "保存成功";
	}
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public TradeLinkConfigModel searchDetail(Long id) throws SQLException {
		TradeLinkConfigModel model = new TradeLinkConfigModel();
		model.setId(id);
		return tradeLinkConfigDao.searchByModel(model);
	}
	/**
	 * 修改
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String modifyTradeLinkConfig(String json, User user) throws SQLException {
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		TradeLinkConfigModel updateModel = (TradeLinkConfigModel) JSONObject.toBean(jsonObj, TradeLinkConfigModel.class) ;
		
		boolean isNull = new EmptyCheckUtils().addObject(updateModel.getStartPointBuId())
				.addObject(updateModel.getStartPointBuName()).addObject(updateModel.getStartPointMerchantId())
				.addObject(updateModel.getStartPointMerchantName()).addObject(updateModel.getStartPointPremiumRate())
				.addObject(updateModel.getStartSupplierId()).addObject(updateModel.getStartSupplierName())
				.addObject(updateModel.getLinkName()).addObject(updateModel.getId()).empty();
		
		if(isNull) {
			throw new DerpException("起点公司信息不全");
		}
		
		TradeLinkConfigModel searchConfigModel = new TradeLinkConfigModel();
		searchConfigModel.setLinkName(updateModel.getLinkName());
		searchConfigModel = tradeLinkConfigDao.searchByModel(searchConfigModel);
		if(searchConfigModel!=null
				&& searchConfigModel.getId().longValue() != updateModel.getId().longValue()){
			throw new DerpException("链路名称不能重复");
		}
		
		updateModel.setModifyDate(TimeUtils.getNow());
		updateModel.setModifier(user.getId());
		updateModel.setModifierName(user.getName());

		tradeLinkConfigDao.modify(updateModel);
		return "修改成功";
	}
}
