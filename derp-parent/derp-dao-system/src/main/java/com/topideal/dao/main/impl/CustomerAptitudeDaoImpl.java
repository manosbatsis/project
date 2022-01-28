package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerAptitudeDao;
import com.topideal.entity.vo.main.CustomerAptitudeModel;
import com.topideal.mapper.main.CustomerAptitudeMapper;

/**
 *供应商资质 impl
 * @author lchenxing
 */
@Repository
public class CustomerAptitudeDaoImpl implements CustomerAptitudeDao {

    @Autowired
    private CustomerAptitudeMapper mapper;  //供应商资质
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerAptitudeModel> list(CustomerAptitudeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param CustomerAptitudeModel
	 */
    @Override
    public Long save(CustomerAptitudeModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(CustomerAptitudeModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param CustomerAptitudeModel
     */
    @Override
    public CustomerAptitudeModel  searchByPage(CustomerAptitudeModel  model) throws SQLException{
        PageDataModel<CustomerAptitudeModel> pageModel=mapper.listByPage(model);
        return (CustomerAptitudeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public CustomerAptitudeModel  searchById(Long id)throws SQLException {
        CustomerAptitudeModel  model=new CustomerAptitudeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public CustomerAptitudeModel searchByModel(CustomerAptitudeModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
