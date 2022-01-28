package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipPoBillVerificationDao;
import com.topideal.entity.dto.VipPoBillVerificationDTO;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;
import com.topideal.mapper.reporting.VipPoBillVerificationMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipPoBillVerificationDaoImpl implements VipPoBillVerificationDao {

    @Autowired
    private VipPoBillVerificationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipPoBillVerificationModel> list(VipPoBillVerificationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipPoBillVerificationModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(VipPoBillVerificationModel model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipPoBillVerificationModel searchByPage(VipPoBillVerificationModel model) throws SQLException{
        PageDataModel<VipPoBillVerificationModel> pageModel=mapper.listByPage(model);
        return (VipPoBillVerificationModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipPoBillVerificationModel searchById(Long id)throws SQLException {
        VipPoBillVerificationModel model=new VipPoBillVerificationModel();
        model.setId(id);
        return mapper.get(model);
    }
    
   /**
 	* 根据商家实体类查询商品
 	* @param model
 	* */
	@Override
	public VipPoBillVerificationModel searchByModel(VipPoBillVerificationModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 查询账单总量-上架总量不等于0的所有po、标准条码、sku。
	 */
	@Override
	public List<VipPoBillVerificationModel> getUnVeriPoBill(VipPoBillVerificationModel model) {
		return mapper.getUnVeriPoBill(model);
	}
	
	/**
	 * 按特定条件获取分页信息
	 */
	@Override
	public List<VipPoBillVerificationDTO> listVipPoBillVeriList(VipPoBillVerificationDTO model) {
		return mapper.listVipPoBillVeriList(model);
	}
	@SuppressWarnings("rawtypes")
	@Override
	
	/**
	 * 根据ID集合删除
	 */
	public List<VipPoBillVerificationModel> searchByIds(List ids) {
		return mapper.searchByIds(ids);
	}
	
	/**
	 * 获取列表数量
	 */
	@Override
	public Integer getVipPoBillVeriListCount(VipPoBillVerificationDTO model) {
		return mapper.getVipPoBillVeriListCount(model);
	}
	
	@Override
	public Integer modifyNecessaryValue(VipPoBillVerificationModel vipPoBillVeriModel) {
		return mapper.updateNecessaryValue(vipPoBillVeriModel);
	}
	
	/**
	 * 获取PO列表
	 */
	@Override
	public List<VipPoBillVerificationDTO> listVipPoBillVeriPoList(VipPoBillVerificationDTO model) {
		return mapper.listVipPoBillVeriPoList(model);
	}
	
	/**
	 * 获取PO列表数量
	 */
	@Override
	public Integer getVipPoBillVeriPoListCount(VipPoBillVerificationDTO model) {
		return mapper.getVipPoBillVeriPoListCount(model);
	}
	@Override
	public Integer countUnsettledAccount(VipPoBillVerificationModel model) {
		return mapper.countUnsettledAccount(model);
	}
	@Override
	public Integer modifyStatus(VipPoBillVerificationModel model) {
		return mapper.modifyStatus(model);
	}
	@Override
	public List<VipPoBillVerificationModel> getListByPo(Map<String , Object> map) {
		return mapper.getListByPo(map);
	}
	@Override
	public Map<String, Object> getDataTime(Long merchantId) {
		return mapper.getDataTime(merchantId);
	}

}