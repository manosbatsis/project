package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.*;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.service.main.MerchantBuRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


/**
 * 公司事业部关联service
 * @author Gy
 *
 */
@Service
public class MerchantBuRelServiceImpl implements MerchantBuRelService{

	@Autowired
	private MerchantBuRelDao merchantBuRelDao ;
	
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	
	@Autowired
	private BusinessUnitDao businessUnitDao ;
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao ;
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private UserBuRelDao userBuRelDao;
	

	@Override
	public String getPurchaseAuditMethod(User user,Long buId,Long supplierId) throws SQLException {
		String auditMethod="";
		if (buId!=null) {
			MerchantBuRelDTO dto=new MerchantBuRelDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setBuId(buId);
			dto = merchantBuRelDao.getMerchantBuRel(dto);
			if (dto!=null)auditMethod=dto.getPurchaseAuditMethod();
		}
		
		if (supplierId!=null) {
			CustomerInfoModel customerInfoModel = customerInfoDao.searchById(supplierId);
			if (customerInfoModel!=null&&customerInfoModel.getInnerMerchantId()!=null) {
				auditMethod=DERP_ORDER.PURCHASEORDER_AUDITMETHOD_2;
			}
		}
		
		return auditMethod;
	}
	
	@Override
	public List<SelectBean> getSelectBean(MerchantBuRelModel merchantBuRelModel) throws SQLException {
		return merchantBuRelDao.getSelectBean(merchantBuRelModel) ;
	}

	@Override
	public MerchantBuRelDTO listMerchantBuRelPage(MerchantBuRelDTO dto) throws SQLException {
		return merchantBuRelDao.getListByPage(dto);
	}

	@Override
	public boolean save(MerchantBuRelModel model) throws SQLException {
		
		MerchantBuRelModel queryModel = new MerchantBuRelModel() ;
		queryModel.setBuId(model.getBuId());
		queryModel.setMerchantId(model.getMerchantId());
		
		queryModel = merchantBuRelDao.searchByModel(queryModel) ;
		
		if(queryModel != null) {
			throw new RuntimeException("公司事业部关联已存在") ;
		}
		
		MerchantInfoModel merchant = merchantInfoDao.searchById(model.getMerchantId());
		
		if(merchant == null) {
			throw new RuntimeException("公司不存在") ;
		}
		
		BusinessUnitModel businessUnit = businessUnitDao.searchById(model.getBuId());
		
		if(businessUnit == null) {
			throw new RuntimeException("事业部不存在") ;
		}
		
		model.setMerchantCode(merchant.getCode());
		model.setMerchantName(merchant.getName());
		model.setBuCode(businessUnit.getCode());
		model.setBuName(businessUnit.getName());
		model.setCreateDate(TimeUtils.getNow());
		model.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		
		Long id = merchantBuRelDao.save(model);
		
		if(id != null) {
			return true ;
		}else {
			return false ;
		}
		
	}

/*	@Override
	public void changeStaus(MerchantBuRelModel model) throws Exception {
		
		MerchantBuRelModel tempModel = merchantBuRelDao.searchById(model.getId());
		
		MerchantDepotBuRelModel queryModel = new MerchantDepotBuRelModel() ;
		queryModel.setBuId(tempModel.getBuId());
		queryModel.setMerchantId(tempModel.getMerchantId());
		List<MerchantDepotBuRelModel> merchantDepotBuList = merchantDepotBuRelDao.list(queryModel);
		
		if(DERP_SYS.MERCHANT_BU_REL_STATUS_0.equals(model.getStatus()) 
				&& merchantDepotBuList.size() > 0) {
			throw new RuntimeException("公司仓库事业部关联表存在关联关系，禁用失败") ;
		}
		
		merchantBuRelDao.modify(model);
		
	}*/

	@Override
	public List<MerchantBuRelDTO> getExportList(MerchantBuRelDTO dto) {
		return merchantBuRelDao.getExportList(dto);
	}

	@Override
	public boolean modifyMerchantBuRel(MerchantBuRelModel model) throws SQLException {

		model.setModifyDate(TimeUtils.getNow());
		int id = merchantBuRelDao.modify(model);
		if(id > 0) {
			return true ;
		}else {
			return false ;
		}
	}

	@Override
	public MerchantBuRelModel searchDetail(Long id) throws SQLException {
		return merchantBuRelDao.searchById(id);
	}

	@Override
	public List<SelectBean> getSelectBeanByUser(User user, MerchantBuRelModel model) throws Exception{
		List<SelectBean> merchantBuList = merchantBuRelDao.getSelectBean(model);

		List<UserBuRelModel> userBuRelModelList = userBuRelDao.getListByUserId(user.getId());
		List<SelectBean> userSelectList = userBuRelModelList.stream()
				.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(entity -> entity.getBuId()))), ArrayList::new))
				.stream().map(entity -> {
					SelectBean selectBean = new SelectBean();
					selectBean.setLabel(entity.getBuName());
					selectBean.setValue(entity.getBuId().toString());
					return selectBean;
		}).collect(Collectors.toList());

		List<SelectBean> collect = userSelectList.stream().filter(entity -> merchantBuList.stream().map(SelectBean::getValue)
				.collect(Collectors.toList()).contains(entity.getValue()))
				.collect(Collectors.toList());

		return collect;
	}

	@Override
	public Boolean getLocationManage(MerchantBuRelDTO dto) throws Exception {
		Boolean result = false;
		MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(dto);
		if(merchantBuRel == null) {
			return result;
		}
		if(StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0, merchantBuRel.getStockLocationManage())) {
			return true;
		}
		return result;
	}
}
