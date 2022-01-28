package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.AutomaticCheckTaskDao;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.mapper.automatic.AutomaticCheckTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AutomaticCheckTaskDaoImpl implements AutomaticCheckTaskDao {

    @Autowired
    private AutomaticCheckTaskMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AutomaticCheckTaskModel> list(AutomaticCheckTaskModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AutomaticCheckTaskModel model) throws SQLException {
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
    public int modify(AutomaticCheckTaskModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AutomaticCheckTaskModel  searchByPage(AutomaticCheckTaskModel  model) throws SQLException{
        PageDataModel<AutomaticCheckTaskModel> pageModel=mapper.listByPage(model);
        return (AutomaticCheckTaskModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AutomaticCheckTaskModel  searchById(Long id)throws SQLException {
        AutomaticCheckTaskModel  model=new AutomaticCheckTaskModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AutomaticCheckTaskModel searchByModel(AutomaticCheckTaskModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public AutomaticCheckTaskDTO queryDTOListByPage(AutomaticCheckTaskDTO dto) throws SQLException {
		PageDataModel<AutomaticCheckTaskDTO> pageModel = mapper.queryDTOListByPage(dto);
		return (AutomaticCheckTaskDTO) pageModel.getModel();
	}
	@Override
	public AutomaticCheckTaskDTO searchDTOById(Long id) throws SQLException {
		AutomaticCheckTaskDTO dto = new AutomaticCheckTaskDTO();
		dto.setId(id);
		return mapper.getAutomaticCheckTaskDTOById(dto);
	}

}