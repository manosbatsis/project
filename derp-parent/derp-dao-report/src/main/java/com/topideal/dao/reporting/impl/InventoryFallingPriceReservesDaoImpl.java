package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.InventoryFallingPriceReservesDao;
import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.entity.dto.InventoryWarningCountDto;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;
import com.topideal.mapper.reporting.InventoryFallingPriceReservesMapper;
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
public class InventoryFallingPriceReservesDaoImpl implements InventoryFallingPriceReservesDao {

    @Autowired
    private InventoryFallingPriceReservesMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryFallingPriceReservesModel> list(InventoryFallingPriceReservesModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryFallingPriceReservesModel model) throws SQLException {
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
    public int modify(InventoryFallingPriceReservesModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryFallingPriceReservesModel  searchByPage(InventoryFallingPriceReservesModel  model) throws SQLException{
        PageDataModel<InventoryFallingPriceReservesModel> pageModel=mapper.listByPage(model);
        return (InventoryFallingPriceReservesModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryFallingPriceReservesModel  searchById(Long id)throws SQLException {
        InventoryFallingPriceReservesModel  model=new InventoryFallingPriceReservesModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryFallingPriceReservesModel searchByModel(InventoryFallingPriceReservesModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delReportByMap(Map<String, Object> delMap) {
		return mapper.delReportByMap(delMap);
	}
	@Override
	public List<InventoryFallingPriceReservesDTO> listInventoryFallingPriceReservesPage(
			InventoryFallingPriceReservesDTO model) {
		return mapper.listInventoryFallingPriceReservesPage(model);
	}
	@Override
	public int countInventoryFallingPriceReserves(InventoryFallingPriceReservesDTO model) {
		return mapper.countInventoryFallingPriceReserves(model);
	}
	@Override
	public List<InventoryFallingPriceReservesModel> searchByIds(String ids) {
		return mapper.searchByIds(ids);
	}
	@Override
	public List<InventoryFallingPriceReservesDTO> getExportList(InventoryFallingPriceReservesDTO dto) {
		return mapper.getExportList(dto);
	}

	@Override
	public List<InventoryWarningCountDto> countInventoryWarning(Map<String, Object> params) throws SQLException {
		return mapper.countInventoryWarning(params);
	}
	@Override
	public List<String> getInventoryGoodsNo(Map<String, Object> queryMap) {
		return mapper.getInventoryGoodsNo(queryMap);
	}

	@Override
	public List<Map<String, Object>> getDepotUnsellableWarning(Map<String, Object> queryMap){
		return mapper.getDepotUnsellableWarning(queryMap);
	}

	@Override
	public List<Map<String, Object>> getDepotExpireWarning(Map<String, Object> queryMap){
		return mapper.getDepotExpireWarning(queryMap);
	}
	@Override
	public List<Map<String, Object>> getDepotInUnsellableWarning(Map<String, Object> queryMap){
		return mapper.getDepotInUnsellableWarning(queryMap);
	}

	@Override
	public List<Map<String, Object>> getBrandInDepotExpireWarning(Map<String, Object> queryMap){
		return mapper.getBrandInDepotExpireWarning(queryMap);
	}

	@Override
	public List<Map<String, Object>> getDepotExpireWarningExportList(Map<String, Object> queryMap) {
		return mapper.getDepotExpireWarningExportList(queryMap);
	}

	@Override
	public List<Map<String, Object>> getDepotUnsellableWarningExportList(Map<String, Object> queryMap) {
		return mapper.getDepotUnsellableWarningExportList(queryMap);
	}
	@Override
	public List<Map<String, Object>> getDepotUnsellableDetail(Map<String, Object> queryMap){
		return mapper.getDepotUnsellableDetail(queryMap);
	}
}