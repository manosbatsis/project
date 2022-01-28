package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleSdOrderItemDao;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import com.topideal.entity.vo.sale.SaleSdOrderItemModel;
import com.topideal.mapper.sale.SaleSdOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleSdOrderItemDaoImpl implements SaleSdOrderItemDao {

    @Autowired
    private SaleSdOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleSdOrderItemModel> list(SaleSdOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleSdOrderItemModel model) throws SQLException {
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
    public int modify(SaleSdOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleSdOrderItemModel  searchByPage(SaleSdOrderItemModel  model) throws SQLException{
        PageDataModel<SaleSdOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleSdOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleSdOrderItemModel  searchById(Long id)throws SQLException {
        SaleSdOrderItemModel  model=new SaleSdOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleSdOrderItemModel searchByModel(SaleSdOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<SaleSdOrderItemDTO> listDTO(SaleSdOrderItemDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
	@Override
	public int delItemBySaleSdIds(List<Long> saleSdOrderIdList) throws SQLException {
		return mapper.delItemBySaleSdIds(saleSdOrderIdList);
	}
}