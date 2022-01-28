package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.CostPriceDifferenceDao;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;
import com.topideal.entity.vo.main.CostPriceDifferenceModel;
import com.topideal.mapper.main.CostPriceDifferenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CostPriceDifferenceDaoImpl implements CostPriceDifferenceDao {

    @Autowired
    private CostPriceDifferenceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CostPriceDifferenceModel> list(CostPriceDifferenceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CostPriceDifferenceModel model) throws SQLException {
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
    public int modify(CostPriceDifferenceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CostPriceDifferenceModel  searchByPage(CostPriceDifferenceModel  model) throws SQLException{
        PageDataModel<CostPriceDifferenceModel> pageModel=mapper.listByPage(model);
        return (CostPriceDifferenceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CostPriceDifferenceModel  searchById(Long id)throws SQLException {
        CostPriceDifferenceModel  model=new CostPriceDifferenceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CostPriceDifferenceModel searchByModel(CostPriceDifferenceModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int batchSave(List<CostPriceDifferenceModel> insertList) {
        return mapper.batchSave(insertList);
    }

    @Override
    public CostPriceDifferenceDTO queryDTOListByPage(CostPriceDifferenceDTO dto) throws SQLException {
        PageDataModel<CostPriceDifferenceDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (CostPriceDifferenceDTO) pageModel.getModel();
    }

    @Override
    public List<CostPriceDifferenceDTO> listByDTO(CostPriceDifferenceDTO dto) throws SQLException {
        return mapper.listByDTO(dto);
    }
}