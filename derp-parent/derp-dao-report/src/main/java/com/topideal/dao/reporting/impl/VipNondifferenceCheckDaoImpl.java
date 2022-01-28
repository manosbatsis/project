package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipNondifferenceCheckDao;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.entity.vo.reporting.VipNondifferenceCheckModel;
import com.topideal.mapper.reporting.VipNondifferenceCheckMapper;
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
public class VipNondifferenceCheckDaoImpl implements VipNondifferenceCheckDao {

    @Autowired
    private VipNondifferenceCheckMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipNondifferenceCheckModel> list(VipNondifferenceCheckModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipNondifferenceCheckModel model) throws SQLException {
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
    public int modify(VipNondifferenceCheckModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipNondifferenceCheckModel  searchByPage(VipNondifferenceCheckModel  model) throws SQLException{
        PageDataModel<VipNondifferenceCheckModel> pageModel=mapper.listByPage(model);
        return (VipNondifferenceCheckModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipNondifferenceCheckModel  searchById(Long id)throws SQLException {
        VipNondifferenceCheckModel  model=new VipNondifferenceCheckModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipNondifferenceCheckModel searchByModel(VipNondifferenceCheckModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delByMap(Map<String, Object> delMap) {
		return mapper.delByMap(delMap);
	}
	@Override
	public List<VipNondifferenceCheckDTO> getNoDifferenceExportList(VipNondifferenceCheckDTO nondifferenceCheckDTO) {
		return mapper.getNoDifferenceExportList(nondifferenceCheckDTO);
	}

}