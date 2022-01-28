package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PlatformCostOrderDao;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.entity.vo.bill.PlatformCostOrderModel;
import com.topideal.mapper.bill.PlatformCostOrderItemMapper;
import com.topideal.mapper.bill.PlatformCostOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformCostOrderDaoImpl implements PlatformCostOrderDao {

    @Autowired
    private PlatformCostOrderMapper mapper;  
    @Autowired
    private PlatformCostOrderItemMapper itemMapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformCostOrderModel> list(PlatformCostOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformCostOrderModel model) throws SQLException {
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
    public int modify(PlatformCostOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformCostOrderModel  searchByPage(PlatformCostOrderModel  model) throws SQLException{
        PageDataModel<PlatformCostOrderModel> pageModel=mapper.listByPage(model);
        return (PlatformCostOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformCostOrderModel  searchById(Long id)throws SQLException {
        PlatformCostOrderModel  model=new PlatformCostOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformCostOrderModel searchByModel(PlatformCostOrderModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public void batchUpdateOrderStatus(PlatformCostOrderModel model, List<Long> ids) throws Exception {
        mapper.batchUpdateOrderStatus(model, ids);
    }
    
	@Override
	public PlatformCostOrderDTO listLatformCostOrderByPage(PlatformCostOrderDTO model) throws SQLException {
		PageDataModel<PlatformCostOrderDTO> pageModel=mapper.listLatformCostOrderByPage(model);
        return (PlatformCostOrderDTO) pageModel.getModel();
	}
	@Override
	public PlatformCostOrderItemModel listLatformCostOrderItemByPage(PlatformCostOrderItemModel model)
			throws SQLException {
		PageDataModel<PlatformCostOrderItemModel> pageModel = itemMapper.listByPage(model);
		 return (PlatformCostOrderItemModel) pageModel.getModel();
	}
	/**
	 * 获取详情
	 * @throws SQLException 
	 */
	@Override
	public PlatformCostOrderDTO getDetails(PlatformCostOrderDTO dto) throws SQLException {
		return mapper.getDetails(dto);
	}
}