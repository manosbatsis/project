package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleCreditOrderItemDao;
import com.topideal.entity.dto.sale.SaleCreditOrderItemDTO;
import com.topideal.entity.vo.sale.SaleCreditOrderItemModel;
import com.topideal.mapper.sale.SaleCreditOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleCreditOrderItemDaoImpl implements SaleCreditOrderItemDao {

    @Autowired
    private SaleCreditOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleCreditOrderItemModel> list(SaleCreditOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleCreditOrderItemModel model) throws SQLException {
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
    public int modify(SaleCreditOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleCreditOrderItemModel  searchByPage(SaleCreditOrderItemModel  model) throws SQLException{
        PageDataModel<SaleCreditOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleCreditOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleCreditOrderItemModel  searchById(Long id)throws SQLException {
        SaleCreditOrderItemModel  model=new SaleCreditOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleCreditOrderItemModel searchByModel(SaleCreditOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public List<SaleCreditOrderItemDTO> listDTO(SaleCreditOrderItemDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
}