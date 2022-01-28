package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SaleTargetDao;
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.entity.vo.reporting.SaleTargetModel;
import com.topideal.mapper.reporting.SaleTargetMapper;
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
public class SaleTargetDaoImpl implements SaleTargetDao {

    @Autowired
    private SaleTargetMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleTargetModel> list(SaleTargetModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleTargetModel model) throws SQLException {
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
    public int modify(SaleTargetModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleTargetModel  searchByPage(SaleTargetModel  model) throws SQLException{
        PageDataModel<SaleTargetModel> pageModel=mapper.listByPage(model);
        return (SaleTargetModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleTargetModel  searchById(Long id)throws SQLException {
        SaleTargetModel  model=new SaleTargetModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleTargetModel searchByModel(SaleTargetModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer batchSave(List<SaleTargetModel> saleTargetList) {
		return mapper.batchSave(saleTargetList);
	}
	@Override
	public List<SaleTargetDTO> getSaleTargetList(SaleTargetDTO dto) {
		return mapper.getSaleTargetList(dto);
	}
	@Override
	public Integer getCountSaleTargetList(SaleTargetDTO dto) {
		return mapper.getCountSaleTargetList(dto);
	}
	@Override
	public List<SaleTargetDTO> getExpotList(SaleTargetDTO queryDto) {
		return mapper.getExpotList(queryDto);
	}
	@Override
	public Integer delSaleTarget(SaleTargetDTO queryDto) {
		return mapper.delSaleTarget(queryDto);
	}
	@Override
	public List<SaleTargetDTO> listDto(SaleTargetDTO dto) {
		return mapper.listDto(dto);
	}
	@Override
	public Map<String, Object> countByMap(Map<String, Object> countMap) {
		return mapper.countByMap(countMap);
	}
	@Override
	public List<String> getHasPlatformData(Map<String, Object> queryMap) {
		return mapper.getHasPlatformData(queryMap);
	}
	@Override
	public List<String> getHasShopData(Map<String, Object> queryMap) {
		return mapper.getHasShopData(queryMap);
	}
}