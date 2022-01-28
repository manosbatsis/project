package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SdInterestPriceDao;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.entity.vo.reporting.SdInterestPriceModel;
import com.topideal.mapper.reporting.SdInterestPriceMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdInterestPriceDaoImpl implements SdInterestPriceDao {

    @Autowired
    private SdInterestPriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdInterestPriceModel> list(SdInterestPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdInterestPriceModel model) throws SQLException {
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
    public int modify(SdInterestPriceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdInterestPriceModel  searchByPage(SdInterestPriceModel  model) throws SQLException{
        PageDataModel<SdInterestPriceModel> pageModel=mapper.listByPage(model);
        return (SdInterestPriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdInterestPriceModel  searchById(Long id)throws SQLException {
        SdInterestPriceModel  model=new SdInterestPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdInterestPriceModel searchByModel(SdInterestPriceModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 根据dto获取DTO分页
     * @param dto
     * @return
     */
    @Override
    public SdInterestPriceDTO getDtoListByPage(SdInterestPriceDTO dto) {
        PageDataModel<SdInterestPriceDTO> pageModel=mapper.getDtoListByPage(dto);
        return (SdInterestPriceDTO) pageModel.getModel();
    }

    @Override
    public List<SdInterestPriceDTO> listPrice(SdInterestPriceDTO dto) {
        return mapper.listPrice(dto);
    }

	@Override
	public int delSdInterestPrice(Map<String, Object> map) throws SQLException {
		return mapper.delSdInterestPrice(map);
	}
	@Override
	public SdInterestPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException {
		return mapper.getLastWeightedPrice(map);
	}
}