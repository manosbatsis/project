package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.PurchaseCommissionDao;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;
import com.topideal.mapper.main.PurchaseCommissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseCommissionDaoImpl implements PurchaseCommissionDao {

    @Autowired
    private PurchaseCommissionMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseCommissionModel> list(PurchaseCommissionModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseCommissionModel model) throws SQLException {
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
    public int modify(PurchaseCommissionModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseCommissionModel  searchByPage(PurchaseCommissionModel  model) throws SQLException{
        PageDataModel<PurchaseCommissionModel> pageModel=mapper.listByPage(model);
        return (PurchaseCommissionModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseCommissionModel  searchById(Long id)throws SQLException {
        PurchaseCommissionModel  model=new PurchaseCommissionModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseCommissionModel searchByModel(PurchaseCommissionModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PurchaseCommissionDTO getListByPage(PurchaseCommissionDTO dto) {
		PageDataModel<PurchaseCommissionDTO> page = mapper.getListByPage(dto);
		return (PurchaseCommissionDTO) page.getModel() ;
	}

}