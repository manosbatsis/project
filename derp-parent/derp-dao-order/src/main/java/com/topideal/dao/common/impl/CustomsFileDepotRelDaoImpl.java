package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.CustomsFileDepotRelDao;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileDepotRelModel;
import com.topideal.mapper.common.CustomsFileDepotRelMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CustomsFileDepotRelDaoImpl implements CustomsFileDepotRelDao {

    @Autowired
    private CustomsFileDepotRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomsFileDepotRelModel> list(CustomsFileDepotRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomsFileDepotRelModel model) throws SQLException {
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
    public int modify(CustomsFileDepotRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomsFileDepotRelModel  searchByPage(CustomsFileDepotRelModel  model) throws SQLException{
        PageDataModel<CustomsFileDepotRelModel> pageModel=mapper.listByPage(model);
        return (CustomsFileDepotRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomsFileDepotRelModel  searchById(Long id)throws SQLException {
        CustomsFileDepotRelModel  model=new CustomsFileDepotRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomsFileDepotRelModel searchByModel(CustomsFileDepotRelModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据条件删除
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delByModel(CustomsFileDepotRelModel model) throws SQLException {		
		return mapper.delByModel(model);
	}
	@Override
	public List<CustomsFileDepotRelDTO> listDTO(CustomsFileDepotRelDTO dto) throws SQLException {		
		return mapper.listDTO(dto);
	}

}