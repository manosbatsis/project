package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.DepartmentQuatoDao;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.vo.common.DepartmentQuatoModel;
import com.topideal.mapper.common.DepartmentQuatoMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class DepartmentQuatoDaoImpl implements DepartmentQuatoDao {

    @Autowired
    private DepartmentQuatoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<DepartmentQuatoModel> list(DepartmentQuatoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(DepartmentQuatoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(DepartmentQuatoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public DepartmentQuatoModel  searchByPage(DepartmentQuatoModel  model) throws SQLException{
        PageDataModel<DepartmentQuatoModel> pageModel=mapper.listByPage(model);
        return (DepartmentQuatoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public DepartmentQuatoModel  searchById(Long id)throws SQLException {
        DepartmentQuatoModel  model=new DepartmentQuatoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public DepartmentQuatoModel searchByModel(DepartmentQuatoModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public DepartmentQuatoDTO getListByPage(DepartmentQuatoDTO dto) {
		PageDataModel<DepartmentQuatoDTO> pageModel=mapper.getListByPage(dto);
        return (DepartmentQuatoDTO ) pageModel.getModel();
	}
	@Override
	public DepartmentQuatoModel getLatestDepartmentQuato(Map<String, Object> queryMap) {
		return mapper.getLatestDepartmentQuato(queryMap) ;
	}
	@Override
	public List<DepartmentQuatoModel> getCoincidenceTimeList(DepartmentQuatoModel queryModel) {
		return mapper.getCoincidenceTimeList(queryModel) ;
	}
}