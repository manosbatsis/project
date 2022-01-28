package com.topideal.dao.sale.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleReturnOrderRelDao;
import com.topideal.entity.vo.sale.SaleReturnOrderRelModel;
import com.topideal.mapper.sale.SaleReturnOrderRelMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * 销售订单退货商品信息 daoImpl
 * @author lchenxing
 */
@Repository
public class SaleReturnOrderRelDaoImpl implements SaleReturnOrderRelDao {

//    @Autowired
//    private SaleReturnOrderRelMapper mapper;
//
//	/**
//	 * 列表查询
//	 * @param model
//	 */
//	@Override
//	public List<SaleReturnOrderRelModel> list(SaleReturnOrderRelModel model) throws SQLException {
//		return mapper.list(model);
//	}
//	/**
//	 * 新增
//	 * @param model
//	 */
//    @Override
//    public Long save(SaleReturnOrderRelModel model) throws SQLException {
//        int num=mapper.insert(model);
//        if(num==1){
//            return model.getId();
//        }
//        return null;
//    }
//
//	/**
//     * 删除
//     * @param ids
//     */
//    @Override
//    public int delete(List ids) throws SQLException {
//        return mapper.batchDel(ids);
//    }
//
//	/**
//     * 修改
//     * @param model
//     */
//    @Override
//    public int modify(SaleReturnOrderRelModel  model) throws SQLException {
//        return mapper.update(model);
//    }
//
//	/**
//     * 分页查询
//     * @param model
//     */
//    @Override
//    public SaleReturnOrderRelModel  searchByPage(SaleReturnOrderRelModel  model) throws SQLException{
//        PageDataModel<SaleReturnOrderRelModel> pageModel=mapper.listByPage(model);
//        return (SaleReturnOrderRelModel ) pageModel.getModel();
//    }
//
//    /**
//     * 通过id查询实体类信息
//     * @param id
//     */
//    @Override
//    public SaleReturnOrderRelModel  searchById(Long id)throws SQLException {
//        SaleReturnOrderRelModel  model=new SaleReturnOrderRelModel ();
//        model.setId(id);
//        return mapper.get(model);
//    }
//
//       /**
//     	* 根据商家实体类查询商品
//     	* @param model
//     	* */
//	@Override
//	public SaleReturnOrderRelModel searchByModel(SaleReturnOrderRelModel model) throws SQLException {
//		return mapper.get(model);
//	}
//
//	/**
//	 * 通过销售订单id获取商品已经退货的数据（过滤已删除的销退订单）
//	 * @param saleId
//	 * @param merchandiseId
//	 * @return
//	 */
//	@Override
//	public List<SaleReturnOrderRelModel> listSaleReturnOrderRel(Long saleId, Long merchandiseId) {
//		return mapper.listSaleReturnOrderRel(saleId,merchandiseId);
//	}
    
}
