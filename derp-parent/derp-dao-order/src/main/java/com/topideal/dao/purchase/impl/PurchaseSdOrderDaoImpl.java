package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseSdOrderDao;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderModel;
import com.topideal.mapper.purchase.PurchaseSdOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseSdOrderDaoImpl implements PurchaseSdOrderDao {

    @Autowired
    private PurchaseSdOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseSdOrderModel> list(PurchaseSdOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseSdOrderModel model) throws SQLException {
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
    public int modify(PurchaseSdOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseSdOrderModel  searchByPage(PurchaseSdOrderModel  model) throws SQLException{
        PageDataModel<PurchaseSdOrderModel> pageModel=mapper.listByPage(model);
        return (PurchaseSdOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseSdOrderModel  searchById(Long id)throws SQLException {
        PurchaseSdOrderModel  model=new PurchaseSdOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseSdOrderModel searchByModel(PurchaseSdOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@SuppressWarnings("unchecked")
	@Override
	public PurchaseSdOrderPageDTO getPurchaseSdOrderPageList(PurchaseSdOrderPageDTO dto) {
		
		List<PurchaseSdOrderPageDTO> pageList = mapper.getPurchaseSdOrderPageList(dto) ;
		Integer total = mapper.countPurchaseSdOrderPageNum(dto) ;
		
		dto.setList(pageList);
		dto.setTotal(total);
		
		return dto;
	}
	@Override
	public PurchaseSdOrderDTO searchDTOById(Long id) {
		
		PurchaseSdOrderDTO dto = new PurchaseSdOrderDTO() ;
		dto.setId(id);
		
		return mapper.searchByDTO(dto);
	}
	@Override
	public List<PurchaseSdOrderDTO> listDTO(PurchaseSdOrderPageDTO dto) {
		return mapper.listDTO(dto);
	}
	@Override
	public List<PurchaseSdOrderPageDTO> getExportSdOrder(PurchaseSdOrderPageDTO dto) {
		return mapper.getExportSdOrder(dto);
	}
	@Override
	public List<PurchaseSdOrderModel> getBXListDTO(PurchaseSdOrderModel model,Long orderId) {
		return mapper.getBXListDTO(model,orderId);
	}
}