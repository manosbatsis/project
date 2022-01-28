package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.BuMoveInventoryDao;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.BuMoveInventoryModel;
import com.topideal.mapper.sale.BuMoveInventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuMoveInventoryDaoImpl implements BuMoveInventoryDao {

    @Autowired
    private BuMoveInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuMoveInventoryModel> list(BuMoveInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuMoveInventoryModel model) throws SQLException {
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
    public int modify(BuMoveInventoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuMoveInventoryModel  searchByPage(BuMoveInventoryModel  model) throws SQLException{
        PageDataModel<BuMoveInventoryModel> pageModel=mapper.listByPage(model);
        return (BuMoveInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuMoveInventoryModel  searchById(Long id)throws SQLException {
        BuMoveInventoryModel  model=new BuMoveInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuMoveInventoryModel searchByModel(BuMoveInventoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BuMoveInventoryDTO queryDTOListByPage(BuMoveInventoryDTO dto) throws SQLException {
        PageDataModel<BuMoveInventoryDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (BuMoveInventoryDTO) pageModel.getModel();
    }

    @Override
    public BuMoveInventoryDTO searchDTOById(Long id) throws SQLException {
        BuMoveInventoryDTO dto = new BuMoveInventoryDTO();
        dto.setId(id);
        return mapper.getBuMoveInventoryDTOById(dto);
    }

    @Override
    public List<BuMoveInventoryDTO> queryDTOList(BuMoveInventoryDTO dto) throws SQLException {
        return mapper.queryDTOList(dto);
    }

    @Override
    public List<BuMoveInventoryExportDTO> getDetailsByExport(BuMoveInventoryDTO dto) throws SQLException {
        return mapper.getDetailsByExport(dto);
    }
}