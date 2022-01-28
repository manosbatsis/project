package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.PlatformCostConfigItemDao;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.vo.common.PlatformCostConfigItemModel;
import com.topideal.mapper.common.PlatformCostConfigItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformCostConfigItemDaoImpl implements PlatformCostConfigItemDao {

    @Autowired
    private PlatformCostConfigItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformCostConfigItemModel> list(PlatformCostConfigItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformCostConfigItemModel model) throws SQLException {
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
    public int modify(PlatformCostConfigItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformCostConfigItemModel  searchByPage(PlatformCostConfigItemModel  model) throws SQLException{
        PageDataModel<PlatformCostConfigItemModel> pageModel=mapper.listByPage(model);
        return (PlatformCostConfigItemModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformCostConfigItemModel  searchById(Long id)throws SQLException {
        PlatformCostConfigItemModel  model=new PlatformCostConfigItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformCostConfigItemModel searchByModel(PlatformCostConfigItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<PlatformCostConfigItemDTO> getConfigById(Long config) {
        return mapper.getConfigById(config);
    }
}