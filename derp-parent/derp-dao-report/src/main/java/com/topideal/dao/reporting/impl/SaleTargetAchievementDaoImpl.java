package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SaleTargetAchievementDao;
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.entity.vo.reporting.SaleTargetAchievementModel;
import com.topideal.mapper.reporting.SaleTargetAchievementMapper;
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
public class SaleTargetAchievementDaoImpl implements SaleTargetAchievementDao {

    @Autowired
    private SaleTargetAchievementMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleTargetAchievementModel> list(SaleTargetAchievementModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleTargetAchievementModel model) throws SQLException {
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
    public int modify(SaleTargetAchievementModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleTargetAchievementModel  searchByPage(SaleTargetAchievementModel  model) throws SQLException{
        PageDataModel<SaleTargetAchievementModel> pageModel=mapper.listByPage(model);
        return (SaleTargetAchievementModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleTargetAchievementModel  searchById(Long id)throws SQLException {
        SaleTargetAchievementModel  model=new SaleTargetAchievementModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleTargetAchievementModel searchByModel(SaleTargetAchievementModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer deleteByMap(Map<String, Object> delMap) {
		return mapper.deleteByMap(delMap);
	}
	@Override
	public SaleTargetAchievementDTO getListByPage(SaleTargetAchievementDTO dto) {
		PageDataModel<SaleTargetAchievementDTO> pageModel=mapper.getListByPage(dto);
        return (SaleTargetAchievementDTO ) pageModel.getModel() ;
	}
	@Override
	public List<SaleTargetAchievementDTO> getExportList(SaleTargetAchievementDTO dto) {
		return mapper.getExportList(dto);
	}
	@Override
	public List<String> getExsitplatform() {
		return mapper.getExsitplatform();
	}
	
	@Override
	public List<SaleTargetAchievementDTO> listGroupDto(SaleTargetAchievementDTO dto) {
		return mapper.listGroupDto(dto);
	}
	@Override
	public Integer countGroupDto(SaleTargetAchievementDTO dto) {
		return mapper.countGroupDto(dto);
	}
	@Override
	public List<String> getExsitShopName() {
		return mapper.getExsitShopName();
	}
}