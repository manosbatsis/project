package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleDeclareOrderItemDao;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.sale.SaleDeclareOrderItemModel;
import com.topideal.mapper.sale.SaleDeclareOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleDeclareOrderItemDaoImpl implements SaleDeclareOrderItemDao {

    @Autowired
    private SaleDeclareOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleDeclareOrderItemModel> list(SaleDeclareOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleDeclareOrderItemModel model) throws SQLException {
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
    public int modify(SaleDeclareOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleDeclareOrderItemModel  searchByPage(SaleDeclareOrderItemModel  model) throws SQLException{
        PageDataModel<SaleDeclareOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleDeclareOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleDeclareOrderItemModel  searchById(Long id)throws SQLException {
        SaleDeclareOrderItemModel  model=new SaleDeclareOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleDeclareOrderItemModel searchByModel(SaleDeclareOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public int delByOrderId(Long orderId) throws SQLException {
		return mapper.delByOrderId(orderId);
	}
	@Override
	public Integer getToTalDeclareNum(Map<String, Object> map) throws SQLException {
		return mapper.getToTalDeclareNum(map);
	}
	@Override
	public List<SaleDeclareOrderItemDTO> listDTO(SaleDeclareOrderItemDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
	@Override
	public SaleDeclareOrderItemDTO getSalePopupListByPage(SaleDeclareOrderItemDTO dto) throws SQLException {
		PageDataModel<SaleDeclareOrderDTO> pageModel = mapper.getSalePopupListByPage(dto);
		return (SaleDeclareOrderItemDTO) pageModel.getModel();
	}
	/**
	 * 查询所有数据 按价格排序
	 * @return
	 */
	@Override
	public List<SaleDeclareOrderItemModel> listOrderByPrice(SaleDeclareOrderItemModel model)throws SQLException{
		return mapper.listOrderByPrice(model);
	}
}