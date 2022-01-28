package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PreSaleOrderDao;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.PreSaleOrderModel;
import com.topideal.mapper.sale.PreSaleOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class PreSaleOrderDaoImpl implements PreSaleOrderDao {

    @Autowired
    private PreSaleOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PreSaleOrderModel> list(PreSaleOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PreSaleOrderModel model) throws SQLException {
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
    public int modify(PreSaleOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PreSaleOrderModel  searchByPage(PreSaleOrderModel  model) throws SQLException{
        PageDataModel<PreSaleOrderModel> pageModel=mapper.listByPage(model);
        return (PreSaleOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PreSaleOrderModel  searchById(Long id)throws SQLException {
        PreSaleOrderModel  model=new PreSaleOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PreSaleOrderModel searchByModel(PreSaleOrderModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PreSaleOrderDTO queryDTOListByPage(PreSaleOrderDTO dto) throws SQLException {
        PageDataModel<PreSaleOrderDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (PreSaleOrderDTO) pageModel.getModel();
    }

    @Override
    public PreSaleOrderDTO searchDTOById(Long id) throws SQLException {
        PreSaleOrderDTO dto = new PreSaleOrderDTO();
        dto.setId(id);
        return mapper.getPreSaleOrderDTOById(dto);
    }

    @Override
    public List<PreSaleOrderDTO> queryDTOList(PreSaleOrderDTO dto) throws SQLException {
        return mapper.queryDTOList(dto);
    }

    /**
     * 逻辑删除
     * @param ids
     * @return
     * @throws SQLException
     */
    @Override
    public int delPreSaleOrder(List ids) throws SQLException {
        return mapper.delPreSaleOrder(ids);
    }

    @Override
    public List<PreSaleOrderDTO> listPreSaleByAuditDate(List ids) throws SQLException {
        return mapper.listPreSaleByAuditDate(ids);
    }

    @Override
    public int getTotal(PreSaleOrderDTO dto) throws SQLException {
        return mapper.getTotal(dto);
    }

}