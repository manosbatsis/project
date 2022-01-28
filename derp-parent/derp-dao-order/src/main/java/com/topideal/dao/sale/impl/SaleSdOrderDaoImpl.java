package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleSdOrderDao;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.vo.sale.SaleSdOrderModel;
import com.topideal.mapper.sale.SaleSdOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleSdOrderDaoImpl implements SaleSdOrderDao {

    @Autowired
    private SaleSdOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleSdOrderModel> list(SaleSdOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleSdOrderModel model) throws SQLException {
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
    public int modify(SaleSdOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleSdOrderModel  searchByPage(SaleSdOrderModel  model) throws SQLException{
        PageDataModel<SaleSdOrderModel> pageModel=mapper.listByPage(model);
        return (SaleSdOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleSdOrderModel  searchById(Long id)throws SQLException {
        SaleSdOrderModel  model=new SaleSdOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleSdOrderModel searchByModel(SaleSdOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SaleSdOrderDTO listDTOByPage(SaleSdOrderDTO dto) throws SQLException {
		PageDataModel<SaleSdOrderDTO> pageModel = mapper.listDTOByPage(dto);
		return (SaleSdOrderDTO) pageModel.getModel();
	}
	@Override
	public List<Map<String, Object>> exportSaleSdOrder(SaleSdOrderDTO dto) throws SQLException {
		return mapper.exportSaleSdOrder(dto);
	}
}