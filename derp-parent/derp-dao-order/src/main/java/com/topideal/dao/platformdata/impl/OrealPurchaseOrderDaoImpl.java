package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.OrealPurchaseOrderDao;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.mapper.platformdata.OrealPurchaseOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrealPurchaseOrderDaoImpl implements OrealPurchaseOrderDao {

    @Autowired
    private OrealPurchaseOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrealPurchaseOrderModel> list(OrealPurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrealPurchaseOrderModel model) throws SQLException {
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
    public int modify(OrealPurchaseOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrealPurchaseOrderModel  searchByPage(OrealPurchaseOrderModel  model) throws SQLException{
        PageDataModel<OrealPurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (OrealPurchaseOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrealPurchaseOrderModel  searchById(Long id)throws SQLException {
        OrealPurchaseOrderModel  model=new OrealPurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrealPurchaseOrderModel searchByModel(OrealPurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 分页
	 */
	@Override
	public OrealPurchaseOrderDTO getListByPage(OrealPurchaseOrderDTO dto) throws Exception {
		PageDataModel<OrealPurchaseOrderDTO> pageModel=mapper.getListByPage(dto);
        return (OrealPurchaseOrderDTO ) pageModel.getModel();
	}
	/**
	 * 导出
	 */
	@Override
	public List<Map<String, Object>> getExportList(OrealPurchaseOrderDTO dto) {		
		return mapper.getExportList(dto);
	}
	
	/**
	 * 详情
	 */
	@Override
	public OrealPurchaseOrderDTO searchDTODetail(OrealPurchaseOrderDTO model) throws Exception {
		return mapper.searchDTODetail(model);
	}
	

}