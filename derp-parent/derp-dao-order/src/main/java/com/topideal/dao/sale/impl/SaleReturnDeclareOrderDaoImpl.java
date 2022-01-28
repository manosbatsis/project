package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleReturnDeclareOrderDao;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderModel;
import com.topideal.mapper.sale.SaleReturnDeclareOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleReturnDeclareOrderDaoImpl implements SaleReturnDeclareOrderDao {

    @Autowired
    private SaleReturnDeclareOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnDeclareOrderModel> list(SaleReturnDeclareOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleReturnDeclareOrderModel model) throws SQLException {
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
    public int modify(SaleReturnDeclareOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleReturnDeclareOrderModel  searchByPage(SaleReturnDeclareOrderModel  model) throws SQLException{
        PageDataModel<SaleReturnDeclareOrderModel> pageModel=mapper.listByPage(model);
        return (SaleReturnDeclareOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleReturnDeclareOrderModel  searchById(Long id)throws SQLException {
        SaleReturnDeclareOrderModel  model=new SaleReturnDeclareOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleReturnDeclareOrderModel searchByModel(SaleReturnDeclareOrderModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public SaleReturnDeclareOrderDTO listDTOByPage(SaleReturnDeclareOrderDTO dto) throws SQLException {
        PageDataModel<SaleReturnDeclareOrderDTO> pageModel = mapper.listDTOByPage(dto);
	    return (SaleReturnDeclareOrderDTO) pageModel.getModel();
    }

    @Override
    public SaleReturnDeclareOrderDTO searchDetail(SaleReturnDeclareOrderDTO dto) throws SQLException {
        return mapper.searchDetail(dto);
    }
    @Override
    public List<SaleReturnDeclareOrderDTO> listReturnDeclareOrder(SaleReturnDeclareOrderDTO dto){
	    return mapper.listReturnDeclareOrder(dto);
    }
	@Override
	public int updateWithNull(SaleReturnDeclareOrderModel model) throws SQLException {
		return mapper.updateWithNull(model);
	}
}