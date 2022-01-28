package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnIdepotItemDao;
import com.topideal.entity.vo.order.SaleReturnIdepotItemModel;
import com.topideal.mapper.order.SaleReturnIdepotItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售退货入库表体  daoImpl
 * @author lian_
 */
@Repository
public class SaleReturnIdepotItemDaoImpl implements SaleReturnIdepotItemDao {

    @Autowired
    private SaleReturnIdepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnIdepotItemModel> list(SaleReturnIdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleReturnIdepotItemModel model) throws SQLException {
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
    public int modify(SaleReturnIdepotItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleReturnIdepotItemModel  searchByPage(SaleReturnIdepotItemModel  model) throws SQLException{
        PageDataModel<SaleReturnIdepotItemModel> pageModel=mapper.listByPage(model);
        return (SaleReturnIdepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleReturnIdepotItemModel  searchById(Long id)throws SQLException {
        SaleReturnIdepotItemModel  model=new SaleReturnIdepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleReturnIdepotItemModel searchByModel(SaleReturnIdepotItemModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据销售退货入库ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getList(List ids) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getList(ids);
	}

	@Override
	public Double getPrice(String orderCode, String goodsNo) throws SQLException {
		return mapper.getPrice(orderCode, goodsNo);
	}
	@Override
	public Integer getYJVeriSaleRetunIdepotAccount(Map<String, Object> queryMap) {
		return mapper.getYJVeriSaleRetunIdepotAccount(queryMap);
	}
}