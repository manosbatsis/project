package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.mapper.reporting.WeightedPriceMapper;
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
public class WeightedPriceDaoImpl implements WeightedPriceDao {

    @Autowired
    private WeightedPriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WeightedPriceModel> list(WeightedPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(WeightedPriceModel model) throws SQLException {
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
    public int modify(WeightedPriceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public WeightedPriceModel  searchByPage(WeightedPriceModel  model) throws SQLException{
        PageDataModel<WeightedPriceModel> pageModel=mapper.listByPage(model);
        return (WeightedPriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public WeightedPriceModel  searchById(Long id)throws SQLException {
        WeightedPriceModel  model=new WeightedPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public WeightedPriceModel searchByModel(WeightedPriceModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public WeightedPriceDTO getDtoListByPage(WeightedPriceDTO dto) throws SQLException {
        PageDataModel<WeightedPriceDTO> pageModel=mapper.getDtoListByPage(dto);
        return (WeightedPriceDTO) pageModel.getModel();
    }

    @Override
    public List<WeightedPriceDTO> listPrice(WeightedPriceDTO dto) throws SQLException {
        return mapper.listPrice(dto);
    }
	@Override
	public int delWeightedPrice(Map<String, Object> map) throws SQLException {		
		return mapper.delWeightedPrice(map);
	}
	@Override
	public WeightedPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException {
		return mapper.getLastWeightedPrice(map);
	}
	/**
	 * 获取小于当前月份的所有标准条码
	 */
	@Override
	public List<Map<String, Object>> getLastWeightedPriceAll(Map<String, Object> paramMap) throws SQLException {
		return mapper.getLastWeightedPriceAll(paramMap);
	}
}