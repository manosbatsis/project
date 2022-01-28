package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SaleDataDao;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.ManageDepartmentSaleDataDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.reporting.SaleDataModel;
import com.topideal.mapper.reporting.SaleDataMapper;
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
public class SaleDataDaoImpl implements SaleDataDao {

    @Autowired
    private SaleDataMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleDataModel> list(SaleDataModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleDataModel model) throws SQLException {
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
    public int modify(SaleDataModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleDataModel  searchByPage(SaleDataModel  model) throws SQLException{
        PageDataModel<SaleDataModel> pageModel=mapper.listByPage(model);
        return (SaleDataModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleDataModel  searchById(Long id)throws SQLException {
        SaleDataModel  model=new SaleDataModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleDataModel searchByModel(SaleDataModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer deleteByMap(Map<String, Object> delMap) {
		return mapper.deleteByMap(delMap);
	}
	
	/**
	 * 批量插入
	 */
	@Override
	public Integer batchSave(List<SaleDataModel> subList) {
		return mapper.batchSave(subList);
	}
	
	/**
	 * 按类型统计
	 */
	@Override
	public List<Map<String, Object>> countByMap(Map<String, Object> countMap) {
		return mapper.countByMap(countMap);
	}
	@Override
	public List<SaleDataDTO> getBrandSaleTop(SaleDataDTO dto,String startDate,String endDate) {
		return mapper.getBrandSaleTop(dto,startDate,endDate);
	}

	@Override
	public List<SaleDataDTO> getCusSaleTop(SaleDataDTO dto,String startDate,String endDate) {
		return mapper.getCusSaleTop(dto,startDate,endDate);
	}
	@Override
	public SaleDataDTO getBrandSaleOther(SaleDataDTO dto, String startDate, String endDate, List<String> names) {
		return mapper.getBrandSaleOther(dto, startDate, endDate, names);
	}
	@Override
	public SaleDataDTO getCusSaleOther(SaleDataDTO dto, String startDate, String endDate, List<String> ids) {
		return mapper.getCusSaleOther(dto, startDate, endDate, ids);
	}

	@Override
	public List<SaleDataModel> getSaleDataByCommbarcode(List<String> months) throws SQLException {
		return mapper.getSaleDataByCommbarcode(months);
	}
	
	@Override
	public SaleDataDTO getListAddSale(SaleDataDTO model) throws SQLException {
		List<SaleDataDTO>addList=mapper.getListAddSale(model);
		int total= mapper.getListAddSaleCount(model);
		SaleDataDTO maxModel=mapper.getMaxCreatDate(model);
		if (maxModel!=null) {
			model.setCreateDate(maxModel.getCreateDate());
		}
		model.setList(addList);
		model.setTotal(total);
        return model;
	}
	@Override
	public List<SaleDataDTO> getListAddExport(SaleDataDTO model) throws SQLException {
		return mapper.getListAddExport(model);
	}

	@Override
	public List<Map<String, Object>> getSaleAmountYear(SaleDataDTO model,String isParentBrand,List<Long> brandIds) {
		return mapper.getSaleAmountYear(model,isParentBrand,brandIds);
	}

	@Override
	public List<Map<String, Object>> getAchieveExportList(Map<String, Object> map) {
		return mapper.getAchieveExportList(map);
	}

	@Override
	public List<Map<String, Object>> getCustomerExportList(SaleDataDTO dto, String startDate, String endDate) {
		return mapper.getCustomerExportList(dto, startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getBrandExportList(SaleDataDTO dto, String startDate, String endDate) {
		return mapper.getBrandExportList(dto, startDate, endDate);
	}
	@Override
	public List<Map<String, Object>> getSaleCnyAmountList(Map<String,Object> queryMap){
		return mapper.getSaleCnyAmountList(queryMap);
	}

	@Override
	public List<ManageDepartmentSaleDataDTO> getDepartmentSalesAmountStatistic(Map<String, Object> queryMap) {
		return mapper.getDepartmentSalesAmountStatistic(queryMap);
	}

	@Override
	public List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatistic(Map<String, Object> queryMap) {
		return mapper.getDepartmentSalesAchieveStatistic(queryMap);
	}

	@Override
	public List<Map<String, Object>> getMonthYearAchieveAmount(Map<String, Object> map) {
		return mapper.getMonthYearAchieveAmount(map);
	}
}