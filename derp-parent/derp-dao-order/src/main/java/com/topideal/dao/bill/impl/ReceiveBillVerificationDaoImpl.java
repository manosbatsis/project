package com.topideal.dao.bill.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillVerificationDao;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerificationModel;
import com.topideal.mapper.bill.ReceiveBillVerificationMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillVerificationDaoImpl implements ReceiveBillVerificationDao {

    @Autowired
    private ReceiveBillVerificationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillVerificationModel> list(ReceiveBillVerificationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillVerificationModel model) throws SQLException {
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
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ReceiveBillVerificationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillVerificationModel  searchByPage(ReceiveBillVerificationModel  model) throws SQLException{
        PageDataModel<ReceiveBillVerificationModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillVerificationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillVerificationModel  searchById(Long id)throws SQLException {
        ReceiveBillVerificationModel  model=new ReceiveBillVerificationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillVerificationModel searchByModel(ReceiveBillVerificationModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 分页查询
	 */
	@Override
	public ReceiveBillVerificationDTO listReceiveBillVerificationByPage(ReceiveBillVerificationDTO model)
			throws SQLException {
		PageDataModel<ReceiveBillVerificationDTO> pageModel=mapper.listReceiveBillVerificationByPage(model);
        return (ReceiveBillVerificationDTO) pageModel.getModel();
	}
	@Override
	public List<Map<String, Object>> exportBillVerification(ReceiveBillVerificationDTO dto) throws SQLException {
		return mapper.exportBillVerification(dto);
	}
	@Override
	public int deleteByReceiveId(List<Long> receiveIdList, String billType) throws SQLException{
		return mapper.deleteByReceiveId(receiveIdList, billType);
	}

	@Override
	public Map<String, BigDecimal> getByUncollectedAmount(Map<String, Object> queryMap) {
		return mapper.getByUncollectedAmount(queryMap);
	}

	@Override
	public List<Map<String, Object>> getSummary(Map<String, Object> queryMap) {
		return mapper.getSummary(queryMap);
	}

	@Override
	public List<Map<String, Object>> getItemBySearch(Map<String, Object> queryMap) {
		return mapper.getItemBySearch(queryMap) ;
	}

}