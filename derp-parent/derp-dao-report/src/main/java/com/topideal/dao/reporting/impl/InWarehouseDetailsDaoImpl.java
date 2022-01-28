package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.InWarehouseDetailsDao;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.mapper.reporting.InWarehouseDetailsMapper;
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
public class InWarehouseDetailsDaoImpl implements InWarehouseDetailsDao {

    @Autowired
    private InWarehouseDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InWarehouseDetailsModel> list(InWarehouseDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InWarehouseDetailsModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(InWarehouseDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InWarehouseDetailsModel  searchByPage(InWarehouseDetailsModel  model) throws SQLException{
        PageDataModel<InWarehouseDetailsModel> pageModel=mapper.listByPage(model);
        return (InWarehouseDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InWarehouseDetailsModel  searchById(Long id)throws SQLException {
        InWarehouseDetailsModel  model=new InWarehouseDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InWarehouseDetailsModel searchByModel(InWarehouseDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 删除该月报表数据
	 * @param delMap
	 * @return
	 */
	@Override
	public int delDepotMonthReport(Map<String, Object> delMap) {
		return mapper.delDepotMonthReport(delMap) ;
	}
	
	/**
	 * 获取在库天数范围内数据
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listInWarehouseDaysRange(Map<String, Object> queryMap) {
		return mapper.listInWarehouseDaysRange(queryMap);
	}
	
	/**
	 * 查出规定氛围时间内列表数据
	 * @param queryMap
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public InWarehouseDetailsModel listInWarehouseDetails(InWarehouseDetailsModel model) throws SQLException {
		
		PageDataModel<InWarehouseDetailsModel> listInWarehouseDetails = mapper.listByPage(model);
		return (InWarehouseDetailsModel)listInWarehouseDetails.getModel();
	}
	@Override
	public List<InWarehouseDetailsModel> getDetailsByMonthAndCommbarcode(Map<String, Object> queryMap) {
		return mapper.getDetailsByMonthAndCommbarcode(queryMap);
	}
	
	@Override
	public Map<String, Object> getRangeDate(Map<String, Object> queryMap) {
		return mapper.getRangeDate(queryMap);
	}
	/**
	 * 删除在库天数为空的明细
	 * @param queryMap
	 * @return
	 */
	@Override
	public boolean deleteNullInWarehouseNumDetails() {
		return mapper.deleteNullInWarehouseNumDetails();
	}
	
	/**
	 * 根据入库时间倒序查询明细
	 */
	@Override
	public List<InWarehouseDetailsDTO> listInWarehouseOrderByInWarehousedays(InWarehouseDetailsDTO dto) {
		return mapper.listInWarehouseOrderByInWarehousedays(dto);
	}
	@Override
	public List<Map<String, Object>> listInWarehouseDays(Map<String, Object> queryMap) {
		return mapper.listInWarehouseDays(queryMap);
	}

	@Override
	public List<Map<String, Object>> inWarehouseDaysRangeData(Map<String, Object> queryMap) {
		return mapper.inWarehouseDaysRangeData(queryMap);
	}

	@Override
	public List<Map<String, Object>> getBrandInWarehouse(Map<String, Object> queryMap) {
		return mapper.getBrandInWarehouse(queryMap);
	}

	@Override
	public Map<String, Object> getBrandInWarehouseOther(Map<String, Object> queryMap) {
		return mapper.getBrandInWarehouseOther(queryMap);
	}

	@Override
	public List<Map<String, Object>> getInWarehouseDaysExportList(Map<String, Object> queryMap) {
		return mapper.getInWarehouseDaysExportList(queryMap);
	}
}