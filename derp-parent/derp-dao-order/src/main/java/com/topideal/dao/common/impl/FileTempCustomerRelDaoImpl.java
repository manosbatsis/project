package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.FileTempCustomerRelDao;
import com.topideal.entity.vo.common.FileTempCustomerRelModel;
import com.topideal.mapper.common.FileTempCustomerRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class FileTempCustomerRelDaoImpl implements FileTempCustomerRelDao {

    @Autowired
    private FileTempCustomerRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<FileTempCustomerRelModel> list(FileTempCustomerRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(FileTempCustomerRelModel model) throws SQLException {
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
    public int modify(FileTempCustomerRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public FileTempCustomerRelModel  searchByPage(FileTempCustomerRelModel  model) throws SQLException{
        PageDataModel<FileTempCustomerRelModel> pageModel=mapper.listByPage(model);
        return (FileTempCustomerRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public FileTempCustomerRelModel  searchById(Long id)throws SQLException {
        FileTempCustomerRelModel  model=new FileTempCustomerRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public FileTempCustomerRelModel searchByModel(FileTempCustomerRelModel model) throws SQLException {
		return mapper.get(model);
	}
}