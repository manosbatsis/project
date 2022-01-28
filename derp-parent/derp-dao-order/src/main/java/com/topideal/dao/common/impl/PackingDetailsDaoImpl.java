package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.PackingDetailsDao;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.mapper.common.PackingDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PackingDetailsDaoImpl implements PackingDetailsDao {

    @Autowired
    private PackingDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PackingDetailsModel> list(PackingDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PackingDetailsModel model) throws SQLException {
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
    public int modify(PackingDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PackingDetailsModel  searchByPage(PackingDetailsModel  model) throws SQLException{
        PageDataModel<PackingDetailsModel> pageModel=mapper.listByPage(model);
        return (PackingDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PackingDetailsModel  searchById(Long id)throws SQLException {
        PackingDetailsModel  model=new PackingDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PackingDetailsModel searchByModel(PackingDetailsModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int deleteByModel(PackingDetailsModel packingDetailsModel) throws SQLException {
        return mapper.deleteByModel(packingDetailsModel);
    }

    @Override
    public List<PackingDetailsModel> listGroupCabinetNo(PackingDetailsModel packingDetailsModel) throws SQLException {
        return mapper.listGroupCabinetNo(packingDetailsModel);
    }
}