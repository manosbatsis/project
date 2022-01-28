package com.topideal.service.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.main.MerchantDepotBuRelDao;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.service.main.MerchantDepotBuRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司仓库事业部关联service
 */
@Service
public class MerchantDepotBuRelServiceImpl implements MerchantDepotBuRelService {

	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao;

	@Override
	public List<SelectBean> getSelectBean(MerchantDepotBuRelDTO relDTO) throws SQLException {
		List<SelectBean> depot1SelectBean = new ArrayList<>();
		List<SelectBean> depot2SelectBean = new ArrayList<>();
		List<SelectBean> depotSelectBean = new ArrayList<>();
		if (relDTO.getDepotId1() != null) {
			MerchantDepotBuRelModel depot1RelModel = new MerchantDepotBuRelModel();
			depot1RelModel.setDepotId(relDTO.getDepotId1());
			depot1RelModel.setMerchantId(relDTO.getMerchantId());
			depot1SelectBean = merchantDepotBuRelDao.getSelectBean(depot1RelModel);
		}

		if (relDTO.getDepotId2() != null) {
			MerchantDepotBuRelModel depot2RelModel = new MerchantDepotBuRelModel();
			depot2RelModel.setDepotId(relDTO.getDepotId2());
			depot2RelModel.setMerchantId(relDTO.getMerchantId());
			depot2SelectBean = merchantDepotBuRelDao.getSelectBean(depot2RelModel);
		}

		if (depot1SelectBean != null && depot1SelectBean.size() > 0
				&& depot2SelectBean != null && depot2SelectBean.size() > 0) {
			for (SelectBean selectBean : depot1SelectBean) {
				for (SelectBean bean : depot2SelectBean) {
					if (selectBean.getValue().equals(bean.getValue())) {
						depotSelectBean.add(selectBean);
						break;
					}
				}
			}
		} else if (depot1SelectBean != null && depot1SelectBean.size() > 0
				&& (depot2SelectBean == null || depot2SelectBean.size() == 0)) {
			depotSelectBean.addAll(depot1SelectBean);
		} else if (depot2SelectBean != null && depot2SelectBean.size() > 0
				&& (depot1SelectBean == null || depot1SelectBean.size() == 0)) {
			depotSelectBean.addAll(depot2SelectBean);
		}
		return depotSelectBean;
	}
	@Override
	public List<SelectBean> getSelectBean2(MerchantDepotBuRelDTO relDTO) throws SQLException {
		List<SelectBean> depotListSelectBean = new ArrayList<>();
		List<SelectBean> depotSelectBean = new ArrayList<>();
		if(relDTO.getDepotIdList()!=null && relDTO.getDepotIdList().size()>0){
			for (int i = 0; i < relDTO.getDepotIdList().size(); i++) {
				Long depotId = relDTO.getDepotIdList().get(i);
				MerchantDepotBuRelModel depot1RelModel = new MerchantDepotBuRelModel();
				depot1RelModel.setDepotId(depotId);
				depot1RelModel.setMerchantId(relDTO.getMerchantId());
				depotSelectBean = merchantDepotBuRelDao.getSelectBean(depot1RelModel);
			}
		}
		if(depotSelectBean!=null && depotSelectBean.size()>0){
			depotListSelectBean.addAll(depotSelectBean);
		}
		return depotListSelectBean;
	}

	@Override
	public String getSelectInfoByMerchantId(MerchantDepotBuRelModel model) {
		return merchantDepotBuRelDao.getSelectInfoByMerchantId(model);
	}

	@Override
	public String getBuNameByMerchantDepot(MerchantDepotBuRelModel model) {
		return merchantDepotBuRelDao.getBuNameByMerchantDepot(model);
	}
}
