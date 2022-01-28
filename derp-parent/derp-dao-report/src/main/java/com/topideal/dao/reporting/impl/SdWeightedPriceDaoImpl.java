package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SdWeightedPriceDao;
import com.topideal.entity.dto.SdWeightedPriceDTO;
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.entity.vo.reporting.SdWeightedPriceModel;
import com.topideal.mapper.reporting.SdWeightedPriceMapper;
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
public class SdWeightedPriceDaoImpl implements SdWeightedPriceDao {

    @Autowired
    private SdWeightedPriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdWeightedPriceModel> list(SdWeightedPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdWeightedPriceModel model) throws SQLException {
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
    public int modify(SdWeightedPriceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdWeightedPriceModel  searchByPage(SdWeightedPriceModel  model) throws SQLException{
        PageDataModel<SdWeightedPriceModel> pageModel=mapper.listByPage(model);
        return (SdWeightedPriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdWeightedPriceModel  searchById(Long id)throws SQLException {
        SdWeightedPriceModel  model=new SdWeightedPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdWeightedPriceModel searchByModel(SdWeightedPriceModel model) throws SQLException {
		return mapper.get(model);
	}
    @Override
    public SdWeightedPriceDTO getDtoListByPage(SdWeightedPriceDTO dto) throws SQLException {
        PageDataModel<SdWeightedPriceDTO> pageModel=mapper.getDtoListByPage(dto);
        return (SdWeightedPriceDTO) pageModel.getModel();
    }
    @Override
    public List<SdWeightedPriceDTO> listPrice(SdWeightedPriceDTO dto) throws SQLException{
	    return mapper.listPrice(dto);
    }
	@Override
	public int delSdWeightedPrice(Map<String, Object> map) throws SQLException {
		return mapper.delSdWeightedPrice(map);
	}
	@Override
	public SdWeightedPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException {
		return mapper.getLastWeightedPrice(map);
	}
}