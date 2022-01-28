package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PreSaleOrderItemDao;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.sale.PreSaleOrderItemModel;
import com.topideal.mapper.sale.PreSaleOrderItemMapper;
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
public class PreSaleOrderItemDaoImpl implements PreSaleOrderItemDao {

    @Autowired
    private PreSaleOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PreSaleOrderItemModel> list(PreSaleOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PreSaleOrderItemModel model) throws SQLException {
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
    public int modify(PreSaleOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PreSaleOrderItemModel  searchByPage(PreSaleOrderItemModel  model) throws SQLException{
        PageDataModel<PreSaleOrderItemModel> pageModel=mapper.listByPage(model);
        return (PreSaleOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PreSaleOrderItemModel  searchById(Long id)throws SQLException {
        PreSaleOrderItemModel  model=new PreSaleOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PreSaleOrderItemModel searchByModel(PreSaleOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<PreSaleOrderItemDTO> listPreSaleOrderItemDTO(PreSaleOrderItemDTO dto) throws SQLException {
        return mapper.listPreSaleOrderItemDTO(dto);
    }

    @Override
    public List<PreSaleOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id) {
        return mapper.getListByBesidesIds(itemIds,id);
    }

    @Override
    public void delBesidesIds(List<Long> itemIds, Long id) {
	    mapper.delBesidesIds(itemIds,id);
    }

    @Override
    public List<PreSaleOrderItemModel> getDetailsByReceive(Map<String, Object> map) {
        return mapper.getDetailsByReceive(map);
    }
}