package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationDao;
import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;
import com.topideal.mapper.automatic.MonthlyAccountAutomaticVerificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MonthlyAccountAutomaticVerificationDaoImpl implements MonthlyAccountAutomaticVerificationDao {

    @Autowired
    private MonthlyAccountAutomaticVerificationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MonthlyAccountAutomaticVerificationModel> list(MonthlyAccountAutomaticVerificationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MonthlyAccountAutomaticVerificationModel model) throws SQLException {
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
    public int modify(MonthlyAccountAutomaticVerificationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MonthlyAccountAutomaticVerificationModel  searchByPage(MonthlyAccountAutomaticVerificationModel  model) throws SQLException{
        PageDataModel<MonthlyAccountAutomaticVerificationModel> pageModel=mapper.listByPage(model);
        return (MonthlyAccountAutomaticVerificationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MonthlyAccountAutomaticVerificationModel  searchById(Long id)throws SQLException {
        MonthlyAccountAutomaticVerificationModel  model=new MonthlyAccountAutomaticVerificationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MonthlyAccountAutomaticVerificationModel searchByModel(MonthlyAccountAutomaticVerificationModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int deleteByMap(Map<String, Object> params) {
		return mapper.deleteByMap(params);
	}
	@Override
	public MonthlyAccountAutomaticVerificationDTO listAutomaticVeriByPage(MonthlyAccountAutomaticVerificationDTO dto) {
		PageDataModel<MonthlyAccountAutomaticVerificationDTO> pageModel=mapper.getListByPage(dto);
        return (MonthlyAccountAutomaticVerificationDTO ) pageModel.getModel();
	}
	@Override
	public Integer countList(MonthlyAccountAutomaticVerificationDTO dto) {
		return mapper.countList(dto);
	}
	@Override
	public int modifyNullValue(MonthlyAccountAutomaticVerificationModel model) {
		return mapper.modifyNullValue(model);
	}

}