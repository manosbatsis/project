package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipTakesStockResultsDetailsDao;
import com.topideal.entity.dto.VipTakesStockResultsDetailsDTO;
import com.topideal.entity.vo.reporting.VipTakesStockResultsDetailsModel;
import com.topideal.mapper.reporting.VipTakesStockResultsDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipTakesStockResultsDetailsDaoImpl implements VipTakesStockResultsDetailsDao {

    @Autowired
    private VipTakesStockResultsDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipTakesStockResultsDetailsModel> list(VipTakesStockResultsDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	@Override
	public List<VipTakesStockResultsDetailsDTO> listDTO(VipTakesStockResultsDetailsDTO dto){
		return mapper.listDTO(dto);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipTakesStockResultsDetailsModel model) throws SQLException {
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
    public int modify(VipTakesStockResultsDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipTakesStockResultsDetailsModel  searchByPage(VipTakesStockResultsDetailsModel  model) throws SQLException{
        PageDataModel<VipTakesStockResultsDetailsModel> pageModel=mapper.listByPage(model);
        return (VipTakesStockResultsDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipTakesStockResultsDetailsModel  searchById(Long id)throws SQLException {
        VipTakesStockResultsDetailsModel  model=new VipTakesStockResultsDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipTakesStockResultsDetailsModel searchByModel(VipTakesStockResultsDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 删除明细
	 */
	@Override
	public int deleteByModel(VipTakesStockResultsDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipTakesStockResultsDetailsModel> list) {
		return mapper.batchInsert(list);
	}

}