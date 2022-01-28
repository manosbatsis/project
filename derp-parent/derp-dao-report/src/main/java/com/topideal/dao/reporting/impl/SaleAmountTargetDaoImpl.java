package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SaleAmountTargetDao;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.entity.vo.reporting.SaleAmountTargetModel;
import com.topideal.mapper.reporting.SaleAmountTargetMapper;
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
public class SaleAmountTargetDaoImpl implements SaleAmountTargetDao {

    @Autowired
    private SaleAmountTargetMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleAmountTargetModel> list(SaleAmountTargetModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleAmountTargetModel model) throws SQLException {
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
    public int modify(SaleAmountTargetModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleAmountTargetModel  searchByPage(SaleAmountTargetModel  model) throws SQLException{
        PageDataModel<SaleAmountTargetModel> pageModel=mapper.listByPage(model);
        return (SaleAmountTargetModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleAmountTargetModel  searchById(Long id)throws SQLException {
        SaleAmountTargetModel  model=new SaleAmountTargetModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleAmountTargetModel searchByModel(SaleAmountTargetModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 列表展示 分页
     * @param dto
     * @return
     * @throws SQLException
     */
    @Override
    public SaleAmountTargetDTO getAmountListByPage(SaleAmountTargetDTO dto) throws SQLException {
        PageDataModel<SaleAmountTargetDTO> pageModel=mapper.getAmountListByPage(dto);
        return (SaleAmountTargetDTO) pageModel.getModel();
    }

    /**
     * 获取列表总数
     * @param dto
     * @return
     */
    @Override
    public Integer getOrderCount(SaleAmountTargetDTO dto) {
        return mapper.getOrderCount(dto);
    }

    /**
     * 列表查询
     * @param dto
     * @return
     */
    @Override
    public List<SaleAmountTargetDTO> getList(SaleAmountTargetDTO dto) {
        return mapper.getList(dto);
    }
    @Override
    public List<Map<String, Object>> getTargetExportList(Map<String, Object> map){
        return mapper.getTargetExportList(map);
    }

    @Override
    public List<Map<String, Object>> getMonthAndYearTargetAmount(Map<String, Object> map) {
        return mapper.getMonthAndYearTargetAmount(map);
    }

    @Override
    public List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesTargetStatistic(Map<String, Object> queryMap) {
        return mapper.getDepartmentSalesTargetStatistic(queryMap);
    }


}