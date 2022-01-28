package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.ReminderEmailRelDao;
import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import com.topideal.entity.vo.main.ReminderEmailRelModel;
import com.topideal.mapper.main.ReminderEmailRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReminderEmailRelDaoImpl implements ReminderEmailRelDao {

    @Autowired
    private ReminderEmailRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReminderEmailRelModel> list(ReminderEmailRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReminderEmailRelModel model) throws SQLException {
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
    public int modify(ReminderEmailRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReminderEmailRelModel  searchByPage(ReminderEmailRelModel  model) throws SQLException{
        PageDataModel<ReminderEmailRelModel> pageModel=mapper.listByPage(model);
        return (ReminderEmailRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReminderEmailRelModel  searchById(Long id)throws SQLException {
        ReminderEmailRelModel  model=new ReminderEmailRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReminderEmailRelModel searchByModel(ReminderEmailRelModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<ReminderEmailRelDTO> getConfigById(long configId) {
        return mapper.getConfigById(configId);
    }

    @Override
    public List<ReminderEmailRelModel> getConfigIdAndType(long configId, String type) {
        return mapper.getConfigIdAndType(configId,type);
    }
}