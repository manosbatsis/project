package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.ReminderEmailConfigDao;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.vo.main.ReminderEmailConfigModel;
import com.topideal.mapper.main.ReminderEmailConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReminderEmailConfigDaoImpl implements ReminderEmailConfigDao {

    @Autowired
    private ReminderEmailConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReminderEmailConfigModel> list(ReminderEmailConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReminderEmailConfigModel model) throws SQLException {
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
    public int modify(ReminderEmailConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReminderEmailConfigModel  searchByPage(ReminderEmailConfigModel  model) throws SQLException{
        PageDataModel<ReminderEmailConfigModel> pageModel=mapper.listByPage(model);
        return (ReminderEmailConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReminderEmailConfigModel  searchById(Long id)throws SQLException {
        ReminderEmailConfigModel  model=new ReminderEmailConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReminderEmailConfigModel searchByModel(ReminderEmailConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ReminderEmailConfigDTO listByPage(ReminderEmailConfigDTO model) {
        PageDataModel<ReminderEmailConfigDTO> pageModel=mapper.getListByPage(model);
        return (ReminderEmailConfigDTO ) pageModel.getModel();
    }

    @Override
    public int deleteById(long id) throws SQLException {
        return mapper.del(id);
    }

    @Override
    public ReminderEmailConfigDTO getById(Long id) {
        return mapper.getById(id);
    }
}