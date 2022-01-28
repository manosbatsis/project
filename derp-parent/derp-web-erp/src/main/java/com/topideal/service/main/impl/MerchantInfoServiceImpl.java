package com.topideal.service.main.impl;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepotMerchantRelDao;
import com.topideal.dao.main.MerchantDepotBuRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.dao.main.MerchantRelDao;
import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.webapi.form.MerchantInfoAddDepotForm;

/**
 * 公司管理  impl
 */
@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {
	
	/* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);
	@Autowired
	private MerchantInfoDao merchantInfoDao;//公司信息
	@Autowired
	private MerchantRelDao merchantRelDao;//子公司
	// 仓库商家关系dao
	@Autowired
	private DepotMerchantRelDao depotMerchantRelDao;
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao ;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;

	@Autowired
	private UserMerchantRelDao userMerchantRelDao;
	
	/**
	 * 公司列表
	 */
	@Override
	public List<MerchantInfoModel> listMerchantInfo(MerchantInfoModel model)
			throws SQLException {
		return merchantInfoDao.list(model);
	}
	
	/**
	 * 查询公司表下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException {
		return merchantInfoDao.getSelectBean(model);
	}

    /**
	 * 根据id获取商品详情
	 * @param id
	 * @return
	 */
    @Override
    public MerchantInfoDTO searchDetail(Long id)throws SQLException {
        return  merchantInfoDao.searchDTOById(id);
    }
    
    /**
     * 修改公司信息
     * @return model
     * @throws Exception 
     */
	@Override
	public boolean modifyMerchantInfo(MerchantInfoModel model,List<Long> merchantIdList,
			List<MerchantInfoAddDepotForm> merchantDepotList)throws Exception {
		if(!model.getYname().equals(model.getName())){
			//根据公司名称判断是否存在
			MerchantInfoModel merchant = new MerchantInfoModel();
			merchant.setName(model.getName());
			merchant = merchantInfoDao.searchByModel(merchant);
			if(merchant!=null){
				throw new RuntimeException("公司名相同");
			}
		}
		// 修改非必填字段
		merchantInfoDao.updateNULL(model);
		int num = merchantInfoDao.modify(model);
		if (num<1)return false;
		merchantRelDao.delByMerchantId(model.getId());
    	if(null != merchantIdList && merchantIdList.size()>0){
        	//根据仓库id删除原有的关系
        	//获取需要关联的公司集合
    		for (Long proxyId : merchantIdList) {
    			if (proxyId==null)continue;
    			MerchantRelModel relModel = new MerchantRelModel();
				relModel.setProxyMerchantId(proxyId);
				relModel.setMerchantId(model.getId());// 公司id
				merchantRelDao.save(relModel);
			}
        }
    	
    	depotMerchantRelDao.delByMerchantId(model.getId());

    	MerchantDepotBuRelModel delModel = new MerchantDepotBuRelModel() ;
		delModel.setMerchantId(model.getId());
		merchantDepotBuRelDao.delByModel(delModel) ;
    	
		for (MerchantInfoAddDepotForm depotForm : merchantDepotList) {
			Long depotid = depotForm.getDepotId();
			List<Long> buIdList = depotForm.getBuIdList();			
			if (depotid==null) continue;			
			DepotMerchantRelModel relModel = new DepotMerchantRelModel();
			relModel.setDepotId(depotid);// 仓库id
			relModel.setMerchantId(model.getId());// 商家id
			relModel.setIsInDependOut(depotForm.getIsInDependOut());
			relModel.setIsInOutInstruction(depotForm.getIsInOutInstruction());
			relModel.setIsInvertoryFallingPrice(depotForm.getIsInvertoryFallingPrice());
			relModel.setIsOutDependIn(depotForm.getIsOutDependIn());
			relModel.setProductRestriction(depotForm.getProductRestriction());
			relModel.setContractCode(depotForm.getContractCode());
			depotMerchantRelDao.save(relModel);			
			for (Long buId : buIdList) {
				if (buId==null) continue;
				MerchantDepotBuRelModel saveModel = new MerchantDepotBuRelModel() ;
				saveModel.setDepotId(depotid);
				saveModel.setMerchantId(model.getId());
				saveModel.setBuId(buId);
				saveModel.setCreateDate(TimeUtils.getNow());							
				merchantDepotBuRelDao.save(saveModel) ;
			}
		}
		
        return true;
	}

	/**
	 * 列表（分页）
	 * @param model 
	 * @return
	 */
	@Override
	public MerchantInfoDTO listMerchantInfoPage(MerchantInfoDTO dto) throws SQLException {
		return merchantInfoDao.getListByPage(dto);
	}

	/**
	 * 新增公司信息
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@Override
	public boolean saveMerchantInfo(MerchantInfoModel model, 
			List<Long> merchantIdList,List<MerchantInfoAddDepotForm> merchantDepotList) throws Exception {
		
		//根据公司名称判断是否存在
		MerchantInfoModel merchant = new MerchantInfoModel();
		merchant.setName(model.getName());
		merchant = merchantInfoDao.searchByModel(merchant);
		if(merchant!=null){
			throw new RuntimeException("公司名相同");
		}
		model.setIsBindUser(DERP_SYS.MERCHANTINFO_ISBINDUSER_0);
		model.setStatus(DERP_SYS.MERCHANTINFO_STATUS_1);		
		model.setCreateDate(TimeUtils.getNow());
		Long id = merchantInfoDao.save(model);
		if (id==null)return false;
		// 新增商家关系数据
		for (Long proxyId : merchantIdList) {
			if (proxyId==null)continue;
			MerchantRelModel relModel = new MerchantRelModel();
			relModel.setProxyMerchantId(proxyId);
			relModel.setMerchantId(id);// 公司id
			merchantRelDao.save(relModel);
		}
		// 新增商家仓库信息
		for (MerchantInfoAddDepotForm depotForm : merchantDepotList) {
			Long depotid = depotForm.getDepotId();
			List<Long> buIdList = depotForm.getBuIdList();			
			if (depotid==null) continue;	
			DepotMerchantRelModel relModel = new DepotMerchantRelModel();
			relModel.setDepotId(depotid);// 仓库id
			relModel.setMerchantId(id);// 公司id
			relModel.setIsInDependOut(depotForm.getIsInDependOut());
			relModel.setIsInOutInstruction(depotForm.getIsInOutInstruction());
			relModel.setIsInvertoryFallingPrice(depotForm.getIsInvertoryFallingPrice());
			relModel.setIsOutDependIn(depotForm.getIsOutDependIn());
			relModel.setProductRestriction(depotForm.getProductRestriction());
			relModel.setContractCode(depotForm.getContractCode());			
			depotMerchantRelDao.save(relModel);
			// 新增仓库商家事业部
			for (Long buId : buIdList) {
				if (buId==null) continue;
				MerchantDepotBuRelModel saveModel = new MerchantDepotBuRelModel() ;
				saveModel.setDepotId(depotid);
				saveModel.setMerchantId(model.getId());
				saveModel.setBuId(buId);
				saveModel.setCreateDate(TimeUtils.getNow());							
				merchantDepotBuRelDao.save(saveModel) ;
			}
		}

        
		return true;
	}

	/**
	 * 禁用启用
	 * @param ids
	 * @return
	 */
	@Override
	public boolean isOrNotEnable(Long id,String status) throws SQLException {
		MerchantInfoModel model= new MerchantInfoModel();
		model.setId(id);
		model.setStatus(status);
		model.setModifyDate(TimeUtils.getNow());
		int num = merchantInfoDao.modify(model);
		if (num >= 1) {
			return true;
		}
		return false;
	}
	/**
	 * 根据条件查询下拉列表
	 * @param
	 * */
	@Override
	public List<SelectBean> getSelectBeanById(MerchantInfoModel model)throws SQLException {
		return merchantInfoDao.getSelectBeanById(model);
	}

	@Override
	public MerchantInfoModel searchDetailModel(Long merchantId) throws SQLException {
		return merchantInfoDao.searchById(merchantId);
	}

	@Override
	public List<SelectBean> getUserSelectBean(UserMerchantRelModel model) throws SQLException {
		return userMerchantRelDao.getUserSelectBean(model);
	}

	@Override
	public List<MerchantInfoModel> getUserMerchantList(UserMerchantRelModel model) throws SQLException {
		return userMerchantRelDao.getUserMerchantList(model);
	}

	/**
	 * 根据id获取商家
	 */
	@Override
	public List<MerchantInfoModel> getMerchantList(List<Long> list) throws SQLException {
		List<MerchantInfoModel> resultList=new ArrayList<>();
		for (Long id : list) {
			if (id!=null) {
				MerchantInfoModel MerchantInfoModel = merchantInfoDao.searchById(id);
				resultList.add(MerchantInfoModel);
			}
		}
		return resultList;
	}
}
