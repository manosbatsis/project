package com.topideal.order.service.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdSaleVerifyConfigDao;
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;
import com.topideal.entity.vo.common.SdSaleVerifyConfigModel;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.common.SdSaleVerifyConfigService;

@Service
public class SdSaleVerifyConfigServiceImpl implements SdSaleVerifyConfigService{
	@Autowired
	private SdSaleVerifyConfigDao sdSaleVerifyConfigDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	
	/**
	 * 获取l列表信息
	 */
	@Override
	public SdSaleVerifyConfigDTO listDTOByPage(SdSaleVerifyConfigDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
		return sdSaleVerifyConfigDao.listDTOByPage(dto);
	}

	/**
	 * 获取详情
	 */
	@Override
	public SdSaleVerifyConfigDTO searchDetail(Long id) throws Exception {
		return sdSaleVerifyConfigDao.searchDetail(id);
	}
	/**
	 * 保存、审核
	 */
	@Override
	public void saveOrAudit(SdSaleVerifyConfigDTO dto, User user, String operate) throws Exception {
		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", dto.getMerchantId());
		merchantBuRelParam.put("buId", dto.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" + dto.getBuId() + ",未查到该公司下事业部信息");
		}

		// 用户事业部
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), dto.getBuId());
		if (!userRelateBu) {
			throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
		}
		//客户信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", dto.getCustomerId());
		params.put("merchantId", dto.getMerchantId());
		CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(params);
		if (customer == null) {
			throw new RuntimeException("在客户信息关联表没找到对应信息");
		}
		
		//审核时，唯一性校验
		if("2".equals(operate)) {
			SdSaleVerifyConfigModel queryModel = new SdSaleVerifyConfigModel();
			queryModel.setMerchantId(dto.getMerchantId());
			queryModel.setBuId(dto.getBuId());
			queryModel.setCustomerId(dto.getCustomerId());
			queryModel.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1);
			List<SdSaleVerifyConfigModel> queryList = sdSaleVerifyConfigDao.list(queryModel);
			if(queryList != null && queryList.size() > 0) {
				throw new RuntimeException("保存失败，公司："+merchantBuRelMongo.getMerchantName()+"事业部："+merchantBuRelMongo.getBuName()+
						"客户："+customer.getName()+"已存在一条已启用配置");
			}
		}
		
		SdSaleVerifyConfigModel model = new SdSaleVerifyConfigModel();
		model.setMerchantId(dto.getMerchantId());
		model.setMerchantName(merchantBuRelMongo.getMerchantName());
		model.setBuId(dto.getBuId());
		model.setBuName(merchantBuRelMongo.getBuName());
		model.setCustomerId(dto.getCustomerId());
		model.setCustomerName(customer.getName());
		model.setVerifyType(dto.getVerifyType());
		model.setIsMerchandiseVerify(dto.getIsMerchandiseVerify());
		model.setId(dto.getId());
		model.setRemark(dto.getRemark());
		
		//operate 1-保存 2-审核
		String operateAction = "";
		if(model.getId() == null) {
			if("1".equals(operate)) {//新增 保存
				model.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_0);
				model.setCreater(user.getId());
				model.setCreateName(user.getName());
				model.setCreateDate(TimeUtils.getNow());
				
				operateAction = "保存";
			}else if("2".equals(operate)) {//新增 审核
				model.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1);
				model.setCreater(user.getId());
				model.setCreateName(user.getName());
				model.setCreateDate(TimeUtils.getNow());
				model.setAuditer(user.getId());
				model.setAuditName(user.getName());
				model.setAuditDate(TimeUtils.getNow());
				
				operateAction = "审核";
			}
			sdSaleVerifyConfigDao.save(model);			
		}else {
			if("1".equals(operate)) {//编辑 保存
				model.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_0);
				model.setModifier(user.getId());
				model.setModifiyName(user.getName());
				model.setModifyDate(TimeUtils.getNow());
				
				operateAction = "保存";
			}else if("2".equals(operate)) {//编辑 审核
				model.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1);
				model.setModifier(user.getId());
				model.setModifiyName(user.getName());
				model.setModifyDate(TimeUtils.getNow());
				model.setAuditer(user.getId());
				model.setAuditName(user.getName());
				model.setAuditDate(TimeUtils.getNow());
				
				operateAction = "审核";
			}
			sdSaleVerifyConfigDao.modify(model);
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_12, model.getId().toString(), operateAction, null, null);
		
	}
	/**
	 * 更新状态
	 */
	@Override
	public void modifyStatus(Long id, User user, String status) throws Exception {
		if(!DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1.equals(status) && !DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_2.equals(status)) {
			throw new RuntimeException("状态取值错误 ，1-已启用 2-已停用，当前状态："+status);
		}
		SdSaleVerifyConfigModel model = new SdSaleVerifyConfigModel();
		model.setId(id);
		model.setStatus(status);
		model.setModifier(user.getId());
		model.setModifiyName(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		sdSaleVerifyConfigDao.modify(model);
		
		String operateAction = "";
		if(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1.equals(status)) {
			operateAction = "启用";
		}else if (DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_2.equals(status)) {
			operateAction = "停用";
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_12, model.getId().toString(), operateAction, null, null);
	}
	/**
	 * 删除
	 */
	@Override
	public void delVerifyConfig(String ids) throws Exception {
		List<Long> configIds = StrUtils.parseIds(ids);
		for(Long configId : configIds) {
			SdSaleVerifyConfigModel model = sdSaleVerifyConfigDao.searchById(configId);
			if(model == null) {
				throw new RuntimeException("销售SD核销配置不存在");
			}
			if(!DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_0.equals(model.getStatus())) {
				throw new RuntimeException("销售SD核销配置状态不为“待审核”");
			}
		}
		sdSaleVerifyConfigDao.delete(configIds);
	}

}
